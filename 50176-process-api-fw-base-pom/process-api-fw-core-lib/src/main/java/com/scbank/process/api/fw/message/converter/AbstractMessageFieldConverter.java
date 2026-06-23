package com.scbank.process.api.fw.message.converter;

import java.io.IOException;
import java.nio.charset.Charset;

import com.scbank.process.api.fw.core.masking.IMaskingManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.MessageFormatOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;

/**
 * 메시지 필드 변환기 공통 추상 클래스.
 *
 * <p>
 * 이 클래스는 각 필드 변환기 구현체들이 공통적으로 사용하는
 * 인코딩 처리, 패딩 처리, 마스킹 활성화 판별 등의
 * 기본 유틸리티 메서드를 제공한다.
 * </p>
 *
 * 주요 제공 기능:
 * <ul>
 * <li>필드별 인코딩 조회</li>
 * <li>패딩 문자 조회 및 변환</li>
 * <li>필드 마스킹 활성화 여부 판단</li>
 * </ul>
 *
 * @author sungdon.choi
 */
public abstract class AbstractMessageFieldConverter {

    private IMaskingManager maskingManager;

    protected IMaskingManager getMaskingManager() {
        if (this.maskingManager == null) {
            this.maskingManager = RuntimeContext.getBean(IMaskingManager.class);
        }
        return this.maskingManager;
    }

    /**
     * 필드 메타데이터에서 인코딩 캐릭터셋 정보를 조회한다.
     * <p>
     * 필드에 인코딩 설정이 없으면 기본 인코딩을 반환한다.
     * </p>
     *
     * @param fieldMetadata   필드 메타데이터
     * @param defaultEncoding 기본 인코딩
     * @return 적용할 인코딩 문자열
     */
    protected String getEncoding(IMessageFieldMetadata fieldMetadata, String defaultEncoding) {
        String fieldEncoding = fieldMetadata.getEncoding();
        if (StringUtils.isEmpty(fieldEncoding)) {
            fieldEncoding = defaultEncoding;
        }
        return fieldEncoding;
    }

    /**
     * 필드 메타데이터에 등록된 패딩 문자를 바이트 배열로 변환하여 반환한다.
     *
     * <p>
     * 지원 포맷:
     * </p>
     * <ul>
     * <li>Hex 문자열(예: 0x20)</li>
     * <li>일반 문자열(예: " ")</li>
     * </ul>
     * <p>
     * 패딩 값이 없으면 기본 패딩 바이트를 반환한다.
     * </p>
     *
     * @param metadata            필드 메타데이터
     * @param charset             인코딩 문자열
     * @param defaultPaddingBytes 기본 패딩 바이트 배열
     * @return 변환된 패딩 바이트 배열
     * @throws IOException 패딩 변환 실패 시
     */
    protected byte[] getPaddingBytes(IMessageFieldMetadata metadata, String charset, byte[] defaultPaddingBytes) {
        return this.getPaddingBytes(metadata, Charset.forName(charset), defaultPaddingBytes);
    }

    /**
     * 
     * @param metadata
     * @param charset
     * @param defaultPaddingBytes
     * @return
     */
    protected byte[] getPaddingBytes(IMessageFieldMetadata metadata, Charset charset, byte[] defaultPaddingBytes) {
        String padding = StringUtils.defaultIfEmpty(metadata.getPadding(), StringUtils.EMPTY);
        if (StringUtils.isEmpty(padding)) {
            return new String(defaultPaddingBytes).getBytes(charset);
        }

        if (padding.startsWith("0x") || padding.startsWith("0X")) {
            // 16진수 문자열 → 바이트 배열 변환
            return new String(ByteUtils.hexStringToByteArray(padding)).getBytes(charset);
        }
        byte[] paddingBytes = padding.getBytes(charset);
        if (paddingBytes == null || paddingBytes.length == 0) {
            paddingBytes = new String(defaultPaddingBytes).getBytes(charset);
        }
        return paddingBytes;
    }

    /**
     * 필드 마스킹 활성화 여부를 판단한다.
     *
     * <p>
     * 활성화 조건:
     * </p>
     * <ul>
     * <li>필드 메타데이터의 {@code masking} 속성이 true</li>
     * <li>MessageFormatOptions {@link MessageFormatOption#FIELD_MASK} 옵션이 활성화</li>
     * </ul>
     *
     * @param fieldMetadata 필드 메타데이터
     * @param options       변환 옵션
     * @return 마스킹 적용 여부
     */
    protected boolean enableFieldMasking(IMessageFieldMetadata fieldMetadata, MessageFormatOptions options) {
        return fieldMetadata.isMasking() && options.enabled(MessageFormatOption.FIELD_MASK);
    }
    
    /**
     * SO/SI 처리 활성화 여부를 판단한다.
     * @param fieldMetadata 필드 메타데이터
     * @param options 변환 옵션
     * @return SO/SI 처리 활셩화 여부
     */
    protected boolean enabledSosiBytes(IMessageFieldMetadata fieldMetadata, MessageFormatOptions options) {
    	return fieldMetadata.isSosi() && (options.enabled(MessageFormatOption.ADD_SOSI) || options.enabled(MessageFormatOption.DEL_SOSI));
    }
    
    /**
     * 
     * @param fieldMetadata
     * @return
     * @throws Exception 
     */
    protected byte[] getDefaultValue(IMessageFieldMetadata fieldMetadata, String encoding) throws Exception {
    	String defaultEncoding = this.getEncoding(fieldMetadata, encoding);
    	String defaultValue = fieldMetadata.getDefaultValue();
    	if (defaultValue == null) {
    		return null;
    	}
    	
    	if (defaultValue.startsWith("0x") || defaultValue.startsWith("0X")) {
    		return ByteUtils.hexStringToByteArray(defaultValue);
    	}
    	
    	return defaultValue.getBytes(defaultEncoding);
    }
}
