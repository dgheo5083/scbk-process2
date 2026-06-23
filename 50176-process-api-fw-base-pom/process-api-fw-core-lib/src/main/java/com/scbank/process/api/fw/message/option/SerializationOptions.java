package com.scbank.process.api.fw.message.option;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * 메시지 직렬화(Serialization) 시 추가 옵션을 설정하기 위한 클래스입니다.
 *
 * <p>
 * {@link MessageFormatOptions}를 상속하여, 직렬화 과정에서 다양한 설정 값을 주입할 수 있습니다.<br>
 * Builder 패턴을 통해 옵션을 편리하게 구성할 수 있습니다.
 * </p>
 *
 * 예시:
 * 
 * <pre>{@code
 * SerializationOptions options = SerializationOptions.builder()
 *         .set(MessageFormatOption.INDENT_OUTPUT, true)
 *         .set(MessageFormatOption.CHARSET, "UTF-8")
 *         .build();
 * }</pre>
 *
 * @version 1.0
 * @since 25. 4. 22.
 * @see MessageFormatOption
 */
public class SerializationOptions extends MessageFormatOptions {

    /** Fixed-Length 기본 직렬화 옵션 */
    public static final SerializationOptions fixedLengthSerializationOptions = SerializationOptions.builder()
            // .set(MessageFormatOption.FIELD_TRIM, true)
            .set(MessageFormatOption.PADDING, true)
            .set(MessageFormatOption.LENGTH_CHECK, true)
            .set(MessageFormatOption.FIELD_LENGTH_TRUNCATE, true)
            .set(MessageFormatOption.HALFWIDTH_TO_FULLWIDTH, true)
            .set(MessageFormatOption.ADD_SOSI, true)
            .set(MessageFormatOption.ENCRYPT, true)
            // .set(MessageFormatOption.FIELD_MASK, true)
            .set(MessageFormatOption.INCLUDE_DECIMAL_POINT, true)
            .set(MessageFormatOption.DECIMAL_ROUNDING_MODE, RoundingMode.UNNECESSARY)
            .set(MessageFormatOption.DEFAULT_DECIMAL_SCALE, 2)
            .set(MessageFormatOption.FILL_DEFAULT_VALUES, true)
            .build();

    /** JSON 기본 직렬화 옵션 */
    public static final SerializationOptions jsonSerializationOptions = SerializationOptions.builder()
            .set(MessageFormatOption.FIELD_TRIM, true)
            // .set(MessageFormatOption.LENGTH_CHECK, false)
            .set(MessageFormatOption.HALFWIDTH_TO_FULLWIDTH, true)
            .set(MessageFormatOption.ENCRYPT, true)
            // .set(MessageFormatOption.FIELD_MASK, true)
            .set(MessageFormatOption.INCLUDE_DECIMAL_POINT, true)
            .set(MessageFormatOption.DECIMAL_ROUNDING_MODE, RoundingMode.UNNECESSARY)
            .set(MessageFormatOption.DEFAULT_DECIMAL_SCALE, 2)
            .set(MessageFormatOption.FILL_DEFAULT_VALUES, true)
            .build();

    /** XML 기본 직렬화 옵션 */
    private static final SerializationOptions xmlSerializationOptions = SerializationOptions
            .of(jsonSerializationOptions);

    private static final SerializationOptions formSerializationOptions = SerializationOptions
            .of(jsonSerializationOptions);

    private static final SerializationOptions multipartFormSerializationOptions = SerializationOptions
            .of(formSerializationOptions);

    /** 메시지 포맷별 기본 직렬화 옵션 매핑 */
    private static final Map<MessageFormat, SerializationOptions> defaultSerializationOptions;

    static {
        Map<MessageFormat, SerializationOptions> defaults = new HashMap<>();
        defaults.put(MessageFormat.FIXEDLENGTH, fixedLengthSerializationOptions);
        defaults.put(MessageFormat.JSON, jsonSerializationOptions);
        defaults.put(MessageFormat.XML, xmlSerializationOptions);
        defaults.put(MessageFormat.FORM, formSerializationOptions);
        defaults.put(MessageFormat.MULTIPART_FORM, multipartFormSerializationOptions);

        defaultSerializationOptions = Collections.unmodifiableMap(defaults);
    }

    /**
     * 지정한 메시지 포맷에 해당하는 기본 직렬화 옵션을 조회합니다.
     *
     * @param messageFormat 메시지 포맷
     * @return 기본 옵션 (없으면 빈 Optional 반환)
     */
    public static Optional<SerializationOptions> getDefaultOptions(MessageFormat messageFormat) {
        return Optional.ofNullable(defaultSerializationOptions.get(messageFormat));
    }

    /**
     * 기존 {@link SerializationOptions} 인스턴스를 복제합니다.
     *
     * @param other 복제할 대상 인스턴스
     * @return 복제된 {@link SerializationOptions}
     */
    public static SerializationOptions of(SerializationOptions other) {
        SerializationOptions copy = new SerializationOptions();
        copy.options.putAll(other.options);
        return copy;
    }

    /**
     * 기본 옵션과 확장 옵션을 병합하여 새로운 {@link SerializationOptions}를 생성합니다.
     * <p>
     * 기본 옵션을 복제한 후, 확장 옵션의 키-값을 덮어쓰기 방식으로 적용합니다.
     * </p>
     *
     * @param base      기본 옵션
     * @param overrides 확장 옵션 (key-value 구조)
     * @return 병합된 {@link SerializationOptions}
     */
    public static SerializationOptions merge(SerializationOptions base, Map<String, Object> overrides) {
        SerializationOptions merged = SerializationOptions.of(base);
        if (overrides != null) {
            merged.options.putAll(overrides);
        }
        return merged;
    }

    /**
     * {@link SerializationOptions} 빌더를 생성합니다.
     *
     * @return {@link Builder} 인스턴스
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link SerializationOptions} 설정용 빌더 클래스입니다.
     */
    public static class Builder {
        private final SerializationOptions options = new SerializationOptions();

        /**
         * 옵션을 설정합니다.
         *
         * @param option 설정할 옵션 키
         * @param value  설정할 값
         * @param <T>    옵션 값 타입
         * @return 현재 {@link Builder} 인스턴스 (체이닝 가능)
         */
        public <T> Builder set(IMessageFormatOption<T> option, T value) {
            options.options.put(option.key(), value);
            return this;
        }

        /**
         * 최종 {@link SerializationOptions} 인스턴스를 반환합니다.
         *
         * @return 구성된 {@link SerializationOptions}
         */
        public SerializationOptions build() {
            return options;
        }
    }
}
