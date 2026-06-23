package com.scbank.process.api.fw.message.option;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * 메시지 역직렬화(Deserialization) 시 추가 옵션을 설정하기 위한 클래스입니다.
 *
 * <p>
 * {@link MessageFormatOptions}를 상속하여, 역직렬화 과정에서 다양한 설정 값을 주입할 수 있습니다.<br>
 * Builder 패턴을 통해 옵션을 편리하게 구성할 수 있습니다.
 * </p>
 *
 * 예시:
 * 
 * <pre>{@code
 * DeserializationOptions options = DeserializationOptions.builder()
 *         .set(MessageFormatOption.TRIM, true)
 *         .set(MessageFormatOption.CHARSET, "UTF-8")
 *         .build();
 * }</pre>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 22.
 * @see MessageFormatOption
 */
public class DeserializationOptions extends MessageFormatOptions {

    /** Fixed-Length 기본 역직렬화 옵션 */
    public static final DeserializationOptions fixedLengthDeserializationOptions = DeserializationOptions.builder()
            .set(MessageFormatOption.FIELD_TRIM, true)
            .set(MessageFormatOption.UNPADDING, true)
            .set(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH, true)
            .set(MessageFormatOption.DEL_SOSI, true)
            .set(MessageFormatOption.DECRYPT, true)
            .set(MessageFormatOption.FORCE_SCALE_ON_READ, true)
            .set(MessageFormatOption.INCLUDE_DECIMAL_POINT, true)
            .set(MessageFormatOption.DECIMAL_ROUNDING_MODE, RoundingMode.UNNECESSARY)
            .set(MessageFormatOption.DEFAULT_DECIMAL_SCALE, 2)
            .set(MessageFormatOption.FILL_DEFAULT_VALUES, true)
            .build();

    /** JSON 기본 역직렬화 옵션 */
    public static final DeserializationOptions jsonDeserializationOptions = DeserializationOptions.builder()
            .set(MessageFormatOption.FIELD_TRIM, true)
            .set(MessageFormatOption.FULLWIDTH_TO_HALFWIDTH, true)
            .set(MessageFormatOption.DECRYPT, true)
            .set(MessageFormatOption.FORCE_SCALE_ON_READ, true)
            .set(MessageFormatOption.INCLUDE_DECIMAL_POINT, true)
            .set(MessageFormatOption.DECIMAL_ROUNDING_MODE, RoundingMode.UNNECESSARY)
            .set(MessageFormatOption.DEFAULT_DECIMAL_SCALE, 2)
            .set(MessageFormatOption.FILL_DEFAULT_VALUES, true)
            .set(MessageFormatOption.ENABLED_SET_DEFAULT_VALUE, true)
            .set(MessageFormatOption.IGNORE_LENGTH_FIELD_ON_DESERIALIZE, true)
            .build();

    /** XML 기본 역직렬화 옵션 */
    private static final DeserializationOptions xmlDeserializationOptions = DeserializationOptions
            .of(jsonDeserializationOptions);

    private static final DeserializationOptions formDeserializationOptions = DeserializationOptions
            .of(jsonDeserializationOptions);

    private static final DeserializationOptions multipartFormDeserializationOptions = DeserializationOptions
            .of(formDeserializationOptions);

    /** 메시지 포맷별 기본 역직렬화 옵션 매핑 (불변 Map) */
    private static final Map<MessageFormat, DeserializationOptions> defaultDeserializationOptions;

    static {
        Map<MessageFormat, DeserializationOptions> defaults = new HashMap<>();
        defaults.put(MessageFormat.FIXEDLENGTH, fixedLengthDeserializationOptions);
        defaults.put(MessageFormat.JSON, jsonDeserializationOptions);
        defaults.put(MessageFormat.XML, xmlDeserializationOptions);
        defaults.put(MessageFormat.FORM, formDeserializationOptions);
        defaults.put(MessageFormat.MULTIPART_FORM, multipartFormDeserializationOptions);

        defaultDeserializationOptions = Collections.unmodifiableMap(defaults);
    }

    /**
     * 지정한 메시지 포맷에 해당하는 기본 역직렬화 옵션을 조회합니다.
     *
     * @param messageFormat 메시지 포맷
     * @return 기본 옵션 (없으면 빈 Optional 반환)
     */
    public static Optional<DeserializationOptions> getDefaultOptions(MessageFormat messageFormat) {
        return Optional.ofNullable(defaultDeserializationOptions.get(messageFormat));
    }

    /**
     * 기존 {@link DeserializationOptions} 인스턴스를 복제합니다.
     *
     * @param other 복제할 대상 인스턴스
     * @return 복제된 {@link DeserializationOptions}
     */
    public static DeserializationOptions of(DeserializationOptions other) {
        DeserializationOptions copy = new DeserializationOptions();
        copy.options.putAll(other.options);
        return copy;
    }

    /**
     * 기본 옵션과 확장 옵션을 병합하여 새로운 {@link DeserializationOptions}를 생성합니다.
     * <p>
     * 기본 옵션을 복제한 후, 확장 옵션의 키-값을 덮어쓰기 방식으로 적용합니다.
     * </p>
     *
     * @param base      기본 옵션
     * @param overrides 확장 옵션 (key-value 구조)
     * @return 병합된 {@link DeserializationOptions}
     */
    public static DeserializationOptions merge(DeserializationOptions base, Map<String, Object> overrides) {
        DeserializationOptions merged = DeserializationOptions.of(base);
        if (overrides != null) {
            merged.options.putAll(overrides);
        }
        return merged;
    }

    /**
     * {@link DeserializationOptions} 빌더를 생성합니다.
     *
     * @return {@link Builder} 인스턴스
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link DeserializationOptions} 설정용 빌더 클래스입니다.
     */
    public static class Builder {
        private final DeserializationOptions options = new DeserializationOptions();

        /**
         * 옵션을 설정합니다.
         *
         * @param option 설정할 옵션 키
         * @param value  설정할 값
         * @param <T>    옵션 값 타입
         * @return 현재 {@link Builder} 인스턴스 (체이닝 가능)
         */
        public <T> Builder set(IMessageFormatOption<T> option, T value) {
            options.set(option, value);
            return this;
        }

        /**
         * 최종 {@link DeserializationOptions} 인스턴스를 반환합니다.
         *
         * @return 구성된 {@link DeserializationOptions}
         */
        public DeserializationOptions build() {
            return options;
        }
    }
}
