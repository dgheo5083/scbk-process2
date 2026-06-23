package com.scbank.process.api.fw.message.metadata;

/**
 * E2E 구간 무시 처리를 위한 필드 메타데이터 인터페이스입니다.
 * <p>
 * 메시지 내에서 특정 시작 문자열(start)과 종료 문자열(end)로 둘러싸인 구간을
 * 역직렬화, 직렬화, 마스킹 등 처리 대상에서 제외하기 위한 메타 정보를 제공합니다.
 * 주로 고정 길이 메시지나 바이트 기반 메시지에서 사용됩니다.
 * </p>
 *
 * @author Min-jun
 */
public interface IExtE2EIgnoreSegmentFieldMetadata extends IMessageFieldMetadata {

    /**
     * 무시할 구간의 시작을 나타내는 문자열을 반환합니다.
     * <p>
     * 예: "STX", "[IGNORE_START]" 등
     * </p>
     *
     * @return 무시할 구간의 시작 문자열
     */
    String getStart();

    /**
     * 무시할 구간의 종료를 나타내는 문자열을 반환합니다.
     * <p>
     * 예: "ETX", "[IGNORE_END]" 등
     * </p>
     *
     * @return 무시할 구간의 종료 문자열
     */
    String getEnd();
}
