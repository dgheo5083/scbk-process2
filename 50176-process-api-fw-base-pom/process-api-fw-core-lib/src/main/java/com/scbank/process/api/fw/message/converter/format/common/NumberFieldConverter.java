package com.scbank.process.api.fw.message.converter.format.common;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import org.springframework.lang.NonNull;

import com.scbank.process.api.fw.core.utils.ByteUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.exception.MessageFieldConvertException;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.option.MessageFormatOptions;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;

import lombok.extern.slf4j.Slf4j;

/**
 * 정수형(Integer, Long, Short) 필드를 고정 길이 바이트 배열로 변환하거나,
 * 고정 길이 바이트 배열을 정수형으로 변환하는 컨버터 클래스
 *
 * 적용 기능:
 * - PADDING / UNPADDING 처리
 * - FIELD_TRIM 처리
 * - LENGTH_CHECK 검증
 * - AlignType(LEFT/RIGHT) 정렬
 */
@Slf4j
public class NumberFieldConverter<T extends Number> extends AbstractByteArrayFieldConverter<T> {

    private final Class<T> type;

    /**
     * 생성자 - 정수 타입만 지원하며, 기본형은 Wrapper 타입으로 변환하여 저장
     *
     * @param type 정수 타입 클래스
     */
    public NumberFieldConverter(Class<T> type) {
        this.type = toWrapperType(type); // 항상 Wrapper 타입(Integer, Long, Short)로 변환
    }

    /**
     * 고정 길이 바이트 배열을 정수형으로 변환하는 메서드
     *
     * @param source         원본 바이트 배열
     * @param fieldMetadata  필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 정수값 (null 가능)
     */
    @Override
    public T read(@NonNull byte[] source,
            @NonNull IMessageFieldMetadata fieldMetadata,
            @NonNull MessageContext messageContext) throws Exception {
        try {
            MessageFormatOptions options = messageContext.getDeserializationOptions();
            Charset charset = Charset.forName(this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding()));
            // 정수타입인 경우 UNPADDING 옵션을 적용하지 않는다.
            // AlignType align = fieldMetadata.getAlign() == AlignType.NONE ?
            // AlignType.RIGHT : fieldMetadata.getAlign();
            // byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, charset.name(),
            // ByteUtils.ZERO_PAD_BYTES);

            // UNPADDING 옵션 적용
            // String raw;
            // if (options.enabled(MessageFormatOption.UNPADDING)) {
            // raw = ByteUtils.removePadding(source, paddingBytes, align, charset.name());
            // } else {
            // raw = new String(source, charset);
            // }

            String raw = new String(source, charset);

            // FIELD_TRIM 옵션 적용
            if (options.enabled(MessageFormatOption.FIELD_TRIM)) {
                raw = raw.trim();
            }

            if (raw.isEmpty()) {
                return parse(StringUtils.ZERO);
            }

            T result = parse(raw);

            if (messageContext.isUseDebugLog()) {
                log.info("### {} read {}({}): [{}]", fieldMetadata.getFieldType().getSimpleName(),
                        fieldMetadata.getId(),
                        fieldMetadata.getLength(),
                        result);
            }

            return result;
        } catch (Exception e) {
            throw new MessageFieldConvertException(e);
        }
    }

    /**
     * 정수형 값을 고정 길이 바이트 배열로 변환하는 메서드
     *
     * @param value          변환할 정수값
     * @param fieldMetadata  필드 메타데이터
     * @param messageContext 변환 컨텍스트
     * @return 변환된 바이트 배열
     */
    @Override
    public byte[] write(@NonNull T value,
            @NonNull IMessageFieldMetadata fieldMetadata,
            @NonNull MessageContext messageContext) throws Exception {
        try {
            MessageFormatOptions options = messageContext.getSerializationOptions();
            Charset charset = Charset.forName(this.getEncoding(fieldMetadata, messageContext.getDefaultEncoding()));
            AlignType align = fieldMetadata.getAlign() == AlignType.NONE ? AlignType.RIGHT : fieldMetadata.getAlign();
            byte[] paddingBytes = this.getPaddingBytes(fieldMetadata, charset.name(), ByteUtils.ZERO_PAD_BYTES);

            String stringValue = (value != null) ? String.valueOf(value) : "0";

            // FIELD_TRIM 옵션 적용
            if (options.enabled(MessageFormatOption.FIELD_TRIM)) {
                stringValue = stringValue.trim();
            }

            byte[] sourceBytes = stringValue.getBytes(charset);
            int fieldLength = fieldMetadata.getLength();

            // LENGTH_CHECK 옵션 적용
            if (options.enabled(MessageFormatOption.LENGTH_CHECK) && fieldLength > 0
                    && sourceBytes.length > fieldLength) {
                throw new MessageFieldConvertException(fieldMetadata.getId(),
                        String.format("[%s] 필드 값이 허용 길이(%d)를 초과했습니다. (현재: %d)", fieldMetadata.getId(), fieldLength,
                                sourceBytes.length));
            }

            // 길이 설정이 없으면 실제 길이로 강제 설정
            if (fieldLength == 0) {
                fieldLength = sourceBytes.length;
                // TODO 고정길이방식일때만 처리를 해야할거 같은데.. 다시한번 확인해보자.
                if (MessageFormat.FIXEDLENGTH == messageContext.getFormat()) {
                    if (log.isWarnEnabled()) {
                        log.warn("# [{}] 필드 길이 설정이 0입니다. MessageField length 속성을 확인하세요.", fieldMetadata.getId());
                    }
                }
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                if (options.enabled(MessageFormatOption.PADDING)) {
                    // 패딩 적용
                    ByteUtils.writeWithPadding(
                            out,
                            stringValue,
                            fieldLength,
                            paddingBytes,
                            AlignType.LEFT.equals(align),
                            charset.name());
                } else {
                    // 패딩 없이 그대로 출력
                    out.write(sourceBytes);
                }

                if (messageContext.isUseDebugLog()) {
                    log.info("### {} write {}({}): [{}]", fieldMetadata.getFieldType().getSimpleName(),
                            fieldMetadata.getId(),
                            fieldLength,
                            new String(out.toByteArray(), charset));
                }

                return out.toByteArray();
            }
        } catch (Exception e) {
            throw new MessageFieldConvertException(e);
        }
    }

    /**
     * 프리미티브 타입을 Wrapper 타입으로 변환
     *
     * @param type 입력 타입
     * @return Wrapper 타입
     */
    @SuppressWarnings("unchecked")
    private Class<T> toWrapperType(Class<T> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        if (type == int.class) {
            return (Class<T>) Integer.class;
        }
        if (type == long.class) {
            return (Class<T>) Long.class;
        }
        if (type == short.class) {
            return (Class<T>) Short.class;
        }
        throw new IllegalArgumentException("지원하지 않는 기본형 타입: " + type);
    }

    /**
     * 문자열을 정수형 객체로 변환
     *
     * @param value 문자열 값
     * @return 변환된 정수값
     */
    private T parse(String value) {
        if (type == Integer.class) {
            return type.cast(Integer.parseInt(value));
        } else if (type == Long.class) {
            return type.cast(Long.parseLong(value));
        } else if (type == Short.class) {
            return type.cast(Short.parseShort(value));
        }
        throw new UnsupportedOperationException("지원하지 않는 숫자 타입: " + type);
    }
}
