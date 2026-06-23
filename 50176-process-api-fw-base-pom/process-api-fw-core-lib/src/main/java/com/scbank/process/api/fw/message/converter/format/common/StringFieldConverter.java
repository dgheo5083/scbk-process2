package com.scbank.process.api.fw.message.converter.format.common;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.springframework.lang.NonNull;

import com.scbank.process.api.fw.core.masking.IMaskingStrategy.MaskingType;
import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.exception.MessageFieldConvertException;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.MessageFormatOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 문자열 필드를 고정 길이 바이트 배열로 읽고 쓰기 위한 변환기 클래스.
 *
 * 지원 기능:
 * - 인코딩 기반 문자열 변환
 * - 정렬(Align) 및 패딩(Padding) 처리
 * - SOSI 제어 문자 추가/제거
 * - 전각/반각 문자 변환
 * - 길이 초과 검증
 * - 기본값 설정 및 공백 제거(Trim)
 * - 암호화/복호화 처리
 * - 마스킹 처리 (로깅용)
 * </pre>
 *
 * @author sungdon.choi
 */
@Slf4j
public class StringFieldConverter extends AbstractByteArrayFieldConverter<String> {

    /**
     * 고정 길이 바이트 배열을 문자열로 변환하는 메서드 (역직렬화)
     */
    @Override
    public String read(@NonNull byte[] source,
            @NonNull IMessageFieldMetadata fieldMetadata,
            @NonNull MessageContext messageContext) throws Exception {
        try {
            MessageFormatOptions options = messageContext.getDeserializationOptions();
            String encoding = this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding());
            
            //***********************************************
            //[2026.05.14 최성돈] SO/SI 바이트 중복으로 오는 케이스 처리
            //***********************************************
            source = ByteUtils.normalizeSosiBytes(source);
            //메시지필드에 설정된 길이 속성 무시하고 source의 길이를 기준으로 처리하는 옵션 추가
            int length = source.length;
            if (!options.enabled(MessageFormatOption.IGNORE_LENGTH_FIELD_ON_DESERIALIZE)) {
            	length = fieldMetadata.getLength() > 0 ? fieldMetadata.getLength() : source.length;
            }

            byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, encoding, ByteUtils.SPACE_PAD_BYTES);
            AlignType align = fieldMetadata.getAlign() == AlignType.NONE ? AlignType.LEFT : fieldMetadata.getAlign();

            // 1. readString 호출 (마스킹 적용 여부 기준)
            String target = ByteUtils.readString(
                    new ByteArrayInputStream(source),
                    length,
                    paddingBytes,
                    align,
                    encoding);

            // 2. 값이 없는 경우 기본값으로 대체
            if (StringUtils.isEmpty(target) && options.enabled(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE)) {
                target = StringUtils.defaultIfEmpty(fieldMetadata.getDefaultValue(), StringUtils.EMPTY);
                if (log.isTraceEnabled()) {
                    log.trace("[{}] 필드 기본값 [{}] 적용", fieldMetadata.getId(), target);
                }
            }

            // 3. UNPADDING 처리 (패딩 제거)
            if (options.enabled(MessageFormatOption.UNPADDING)) {
                target = ByteUtils.removePadding(target.getBytes(encoding), paddingBytes, align, encoding);
            }

            // 4. 복호화 처리
            if (options.enabled(MessageFormatOption.DECRYPT)) {
                // target = EncryptUtils.decrypt(target);
            }

            // 5. SOSI 제어 문자 제거
            if (fieldMetadata.isSosi() && options.enabled(MessageFormatOption.DEL_SOSI)) {
//                byte[] sosiTargetBytes = target.getBytes(encoding);
//                if (ByteUtils.containsMultibyteCharacter(sosiTargetBytes, encoding)) {
//                    target = ByteUtils.delSOSI(sosiTargetBytes, encoding);
//                }
                //전각 -> 반각 문자 변환
                target = StringUtils.toHalfChar(target);
            }

            // 6. 전각 → 반각 문자 변환
            if (fieldMetadata.isMultiBytes() || options.enabled(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH)) {
                target = StringUtils.toHalfChar(target);
            }

            // 7. 앞뒤 공백 제거 (TRIM)
            if (options.enabled(MessageFormatOption.FIELD_TRIM)) {
                target = target.trim();
            }

            if (options.enabled(MessageFormatOption.FIELD_MASK) && fieldMetadata.isMasking()) {
                MaskingType maskingType = MaskingType
                        .from(StringUtils.defaultIfEmpty(fieldMetadata.getMaskingType(), ""));
                String masked = this.getMaskingManager().apply(maskingType, target, encoding);
                if (log.isTraceEnabled()) {
                    log.trace("[{}] 필드 마스킹 적용: [{}]", fieldMetadata.getId(), masked);
                }

                target = masked;
            }
            if (messageContext.isUseDebugLog()) {
                log.info("### String read {}({}): [{}]", fieldMetadata.getId(), fieldMetadata.getLength(),
                        target);
            }

            return target;
        } catch (Exception e) {
            throw new MessageFieldConvertException(e);
        }
    }

    /**
     * 문자열을 고정 길이 바이트 배열로 변환하는 메서드 (직렬화)
     */
    @Override
    public byte[] write(@NonNull String value,
            @NonNull IMessageFieldMetadata fieldMetadata,
            @NonNull MessageContext messageContext) throws Exception {
        try {
            MessageFormatOptions options = messageContext.getSerializationOptions();
            String charset = this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding());
            boolean enabledSosi = this.enabledSosiBytes(fieldMetadata, options);
            int fieldLegth = fieldMetadata.getLength();
            
            //******************************************
            //SO/SI 인 경우 2바이트를 빼고 계산해야한다.
            //******************************************
            int dataLength = enabledSosi ? fieldLegth - 2 : fieldLegth;
            
            
            // 1. 기본값 적용 (null 또는 빈 문자열인 경우)
            if (StringUtils.isEmpty(value)) {
            	byte[] defaultValueBytes = this.getDefaultValue(fieldMetadata, charset);
            	value = new String(defaultValueBytes, charset);
                if (log.isTraceEnabled()) {
                    log.trace("[{}] 필드 기본값 [{}] 적용", fieldMetadata.getId(), value);
                }
            }

            // 트림 처리
            if (options.enabled(MessageFormatOption.FIELD_TRIM)) {
                value = value.trim();
            }

            // 2. 반각 -> 전각 변환 (SO/SI 처릿 추가)
            if (fieldMetadata.isMultiBytes() && options.enabled(MessageFormatOption.HALFWIDTH_TO_FULLWIDTH) || enabledSosi) {
                value = StringUtils.toFullChar(value);
            }

            // 3. 전각 -> 반각 변환
            if (fieldMetadata.isMultiBytes() && options.enabled(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH)) {
                value = StringUtils.toHalfChar(value);
            }

            // 4. 암호화 처리
//            if (options.enabled(MessageFormatOption.ENCRYPT)) {
//                value = EncryptUtils.encrypt(value);
//            }
            
            byte[] rawBytes = value.getBytes(charset);
            byte[] workingBytes = rawBytes;
            
            // 6. 길이 초과 검증
            if (workingBytes.length > dataLength) {
            	if (options.enabled(MessageFormatOption.LENGTH_CHECK)) {
                    // 길이 초과인 경우 FIELD_LENGTH_TRUNCATE 옵션이 비활성인경우 예외처리
                    if (!options.enabled(MessageFormatOption.FIELD_LENGTH_TRUNCATE)) {
                        throw new MessageFieldConvertException("", String.format("[%s] 문자열 길이 초과 (%d > %d)",
                                fieldMetadata.getPath(), workingBytes.length, dataLength));
                    }

                    if (log.isTraceEnabled()) {
                        log.trace("[{}] 필드 길이만큰 truncate : [{}]", fieldMetadata.getId(), new String(workingBytes, charset));
                    }
                    
                    String truncateValue = this.truncateByByteLength(value, Charset.forName(charset), dataLength);
                    workingBytes = truncateValue.getBytes(charset);
                    
                    if (log.isDebugEnabled()) {
                    	log.debug("[{}] 필드 길이만큰 원본: [{}], truncate :[{}]", fieldMetadata.getId(), value, truncateValue);
                    }
                }
            }
            
            if (fieldMetadata.isMultiBytes() && options.enabled(MessageFormatOption.HALFWIDTH_TO_FULLWIDTH) || enabledSosi) {
            	workingBytes =  ByteUtils.delSOSI(workingBytes);
            }

            // 7. 최종 패딩 적용 여부 (PADDING)
            if (options.enabled(MessageFormatOption.PADDING)) {
                AlignType align = fieldMetadata.getAlign() == AlignType.NONE ? AlignType.LEFT : fieldMetadata.getAlign();
                byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, charset, ByteUtils.SPACE_PAD_BYTES);
                workingBytes = ByteUtils.writeBytes(workingBytes, dataLength, paddingBytes, align);
            }
            
            //8. 최종 SO/SI 처리
            byte[] resultBytes = workingBytes;
            if (enabledSosi) {
            	resultBytes = ByteUtils.addSOSI(resultBytes, -1);
            }
            
            if (options.enabled(MessageFormatOption.LENGTH_CHECK)) {
            	//최종 길이 체크
                if (resultBytes.length != fieldLegth) {
                	throw new MessageFieldConvertException("", String.format("필드 전문길이 불일치 path=%s, 현재 길이=%d, 필드 길이=%d",
                            fieldMetadata.getPath(), resultBytes.length, dataLength));
                }
            }

            // 9. 마스킹 처리 (로깅용)
            if (options.enabled(MessageFormatOption.FIELD_MASK) && fieldMetadata.isMasking()) {
                MaskingType maskingType = MaskingType.from(StringUtils.defaultIfEmpty(fieldMetadata.getMaskingType(), ""));
                byte[] masked = this.getMaskingManager().apply(maskingType, resultBytes, charset);
                if (log.isTraceEnabled()) {
                    log.trace("[{}] 필드 마스킹 적용: [{}]", fieldMetadata.getId(), new String(masked));
                }
                resultBytes = masked;
            }

            if (messageContext.isUseDebugLog()) {
                log.debug("### String write {}({}): [{}]", fieldMetadata.getId(), fieldMetadata.getLength(),
                        new String(resultBytes, charset));
            }

            return resultBytes;
        } catch (MessageFieldConvertException e) {
            throw e;
        } catch (Exception e) {
            throw new MessageFieldConvertException(e);
        }
    }
    
    /**
     * 
     * @param value
     * @param charset
     * @param maxBytes
     * @return
     */
    private String truncateByByteLength(String value, Charset charset, int maxBytes) {
    	if (value == null) {
    		return "";
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	int byteLength = 0;
    	
    	for (int i = 0; i < value.length(); i++) {
			String ch = String.valueOf(value.charAt(i));
			int chBytes = ByteUtils.delSOSI(ch.getBytes(charset)).length;
			
			if (byteLength + chBytes > maxBytes) {
				break;
			}
			
			sb.append(ch);
			byteLength += chBytes;
		}
    	
    	return sb.toString();
    }
    
//    public static void main(String[] args) throws Exception {
//    	IMessageFieldMetadata fieldMetadata = DefaultMessageFieldMetadata.builder()
//    			.align(AlignType.LEFT)
//    			.defaultValue("")
//    			.id("YINBNAME")
//    			.fieldName("YINBNAME")
//    			.fieldType(String.class)
//    			.length(12)
//    			.sosi(true)
//    			.build();
//    	
//    	MessageContext messageContext = new MessageContext();
//    	messageContext.setDefaultEncoding("cp933");
//    	messageContext.setSerializationOptions(SerializationOptions.fixedLengthSerializationOptions);
//    	
//		StringFieldConverter stringFieldConverter = new StringFieldConverter();
//		byte[] resultBytes = stringFieldConverter.write("금결원테스트", fieldMetadata, messageContext);
//		
//		System.out.println("[" + Hex.encodeHexString(resultBytes).toUpperCase() + "]");
//		System.out.println("[" + new String(resultBytes, "cp933") + "]");
//	}
}
