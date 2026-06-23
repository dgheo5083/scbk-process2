package com.scbank.process.api.fw.message.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * {@link IExtE2EIgnoreSegmentFieldMetadata} 구현체로,
 * 메시지에서 특정 구간(시작 ~ 종료 문자열) 사이의 데이터를 무시하기 위한 필드 메타데이터 클래스입니다.
 * <p>
 * {@code start} 문자열과 {@code end} 문자열 사이에 포함된 바이트 구간은 역직렬화, 마스킹 등 처리에서 제외됩니다.
 * 예를 들어, "[IGNORE_START]...무시할 데이터...[IGNORE_END]" 와 같은 패턴을 처리할 수 있습니다.
 * </p>
 * 
 * <p>
 * {@link DefaultMessageFieldMetadata}를 상속하며, 필드 이름, 길이, 타입 등의 공통 메타정보와 함께
 * 사용됩니다.
 * </p>
 * 
 * @since 25. 5. 8.
 * @version 1.0
 * @author sungdon.choi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DefaultExtE2EIgnoreSegmentFieldMetadata extends DefaultMessageFieldMetadata
		implements IExtE2EIgnoreSegmentFieldMetadata {

	private static final long serialVersionUID = 1L;

	/**
	 * 무시할 구간의 시작을 나타내는 문자열입니다.
	 * <p>
	 * 메시지 내에서 이 문자열이 등장한 이후부터 end 문자열 전까지의 구간은 무시됩니다.
	 * 예: "STX", "[IGNORE_START]"
	 * </p>
	 */
	private String start;

	/**
	 * 무시할 구간의 종료를 나타내는 문자열입니다.
	 * <p>
	 * 메시지 내에서 start 이후 이 문자열이 나타나면 무시 구간이 종료됩니다.
	 * 예: "ETX", "[IGNORE_END]"
	 * </p>
	 */
	private String end;
}
