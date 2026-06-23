package com.scbank.process.api.fw.message.option;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 데이터 변환 처리 시 사용할 공통 옵션 컨테이너입니다.
 *
 * - 직렬화/역직렬화 과정에서 공통적으로 활용할 수 있는 옵션 관리<br>
 * - 키-값 기반 저장 방식 + 타입 안전성 지원 (Generic 기반)<br>
 * - 기본 제공 옵션({@link MessageFormatOption}) 및 사용자 확장
 * 옵션({@link IMessageFormatOption}) 지원
 *
 * 예시:
 * 
 * <pre>{@code
 * MessageFormatOptions options = new MessageFormatOptions().set(MessageFormatOption.FIELD_TRIM, true)
 * 		.set(MessageFormatOption.LENGTH_CHECK, false);
 * }</pre>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 22.
 */
public abstract class MessageFormatOptions {

	/** 옵션 저장소 (key-value 구조) */
	protected final Map<String, Object> options = new HashMap<>();

	/**
	 * 옵션을 설정합니다.
	 *
	 * @param option 옵션 키
	 * @param value  설정할 값
	 * @param <T>    값 타입
	 * @return 현재 {@link MessageFormatOptions} 인스턴스 (체이닝 가능)
	 */
	@SuppressWarnings("unchecked")
	public <T, O extends MessageFormatOptions> O set(IMessageFormatOption<T> option, T value) {
		options.put(option.key(), value);
		return (O) this;
	}

	/**
	 * 옵션 값을 조회합니다.
	 *
	 * @param option 조회할 옵션 키
	 * @param <T>    기대 타입
	 * @return 옵션 값 (존재하지 않거나 타입이 다르면 빈 Optional 반환)
	 */
	public <T> Optional<T> get(IMessageFormatOption<T> option) {
		Object value = options.get(option.key());
		if (option.valueType().isInstance(value)) {
			return Optional.of(option.valueType().cast(value));
		}
		return Optional.empty();
	}

	/**
	 * 옵션이 true로 설정되어 있는지 여부를 확인합니다.
	 *
	 * @param option 옵션 키
	 * @return true 또는 false
	 */
	public boolean enabled(MessageFormatOption option) {
		return get(option).map(Boolean.TRUE::equals).orElse(false);
	}

	/**
	 * 전체 옵션 맵을 반환합니다.
	 *
	 * @return 읽기 전용 옵션 Map
	 */
	public Map<String, Object> getAll() {
		return Collections.unmodifiableMap(options);
	}

	/**
	 * 변환 옵션 키를 표현하는 인터페이스입니다.
	 *
	 * @param <T> 옵션 값 타입
	 */
	public interface IMessageFormatOption<T> {

		/**
		 * 옵션 키 문자열을 반환합니다.
		 *
		 * @return 옵션 키
		 */
		String key();

		/**
		 * 옵션 값 타입을 반환합니다.
		 *
		 * @return 값 타입
		 */
		Class<T> valueType();
	}

	/**
	 * 기본 제공되는 변환 옵션 Enum입니다.
	 *
	 * 주요 옵션:
	 * <ul>
	 * <li>ADD_SOSI / DEL_SOSI: SO/SI 제어</li>
	 * <li>FIELD_MASK: 필드 마스킹</li>
	 * <li>FIELD_TRIM: 필드 공백 제거</li>
	 * <li>LENGTH_CHECK: 길이 검증</li>
	 * <li>INCLUDE_DECIMAL_POINT: 소수점 포함 여부</li>
	 * </ul>
	 */
	public enum MessageFormatOption implements IMessageFormatOption<Object> {

		ADD_SOSI("ADD_SOSI", Boolean.class),
		DEL_SOSI("DEL_SOSI", Boolean.class),
		FIELD_MASK("FIELD_MASK", Boolean.class),
		FULLWIDTH_TO_HALFWIDTH("FULLWIDTH_TO_HALFWIDTH", Boolean.class),
		HALFWIDTH_TO_FULLWIDTH("HALFWIDTH_TO_FULLWIDTH", Boolean.class),
		FIELD_TRIM("FIELD_TRIM", Boolean.class),
		LENGTH_CHECK("LENGTH_CHECK", Boolean.class),
		FIELD_LENGTH_TRUNCATE("FIELD_LENGTH_TRUNCATE", Boolean.class),
		KEEP_ORIGINAL("KEEP_ORIGINAL", Boolean.class),
		ENCRYPT("ENCRYPT", Boolean.class),
		DECRYPT("DECRYPT", Boolean.class),
		ENCODE_HEX("ENCODE_HEX", Boolean.class),
		DECODE_HEX("DECODE_HEX", Boolean.class),
		INCLUDE_DECIMAL_POINT("INCLUDE_DECIMAL_POINT", Boolean.class),
		DECIMAL_ROUNDING_MODE("DECIMAL_ROUNDING_MODE", RoundingMode.class),
		FORCE_SCALE_ON_READ("FORCE_SCALE_ON_READ", Boolean.class),
		IGNORE_UNKNOWN_FIELDS("IGNORE_UNKNOWN_FIELDS", Boolean.class),
		INCLUDE_NULL_FIELDS("INCLUDE_NULL_FIELDS", Boolean.class),
		ENCODING_CHARSET("ENCODING_CHARSET", String.class),
		DEFAULT_DECIMAL_SCALE("DEFAULT_DECIMAL_SCALE", Integer.class),
		FILL_DEFAULT_VALUES("FILL_DEFAULT_VALUES", Boolean.class),
		TRIM_STRICT_MODE("TRIM_STRICT_MODE", Boolean.class),
		PADDING("PADDING", Boolean.class),
		UNPADDING("UNPADDING", Boolean.class),
		SKIP_ZERO_UNPADDING("SKIP_ZERO_UNPADDING", Boolean.class),
		PRETTY_FORMAT("PRETTY_FORMAT", Boolean.class),
		ENABLED_SET_DEFAULT_VALUE("ENABLED_SET_DEFAULT_VALUE", Boolean.class),
		IGNORE_LENGTH_FIELD_ON_DESERIALIZE("IGNORE_LENGTH_FIELD_ON_DESERIALIZE", Boolean.class);

		private final String key;
		private final Class<?> type;

		/**
		 * MessageFormatOption 생성자
		 *
		 * @param key  옵션 키
		 * @param type 옵션 값 타입
		 */
		<T> MessageFormatOption(String key, Class<T> type) {
			this.key = key;
			this.type = type;
		}

		/**
		 * 옵션 키를 반환합니다.
		 *
		 * @return 옵션 키
		 */
		@Override
		public String key() {
			return this.key;
		}

		/**
		 * 옵션 값 타입을 반환합니다.
		 *
		 * @return 값 타입
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Class<Object> valueType() {
			return (Class<Object>) this.type;
		}
	}
}
