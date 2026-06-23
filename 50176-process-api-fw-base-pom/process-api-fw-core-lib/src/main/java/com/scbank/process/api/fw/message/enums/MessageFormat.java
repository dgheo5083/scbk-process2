package com.scbank.process.api.fw.message.enums;

/**
 * 전문(Message) 포맷 구분 Enum.
 *
 * <p>
 * 메시지 변환(직렬화/역직렬화) 시 적용할 전문 포맷 종류를 정의한다.
 * </p>
 *
 * 포맷 종류:
 * <ul>
 * <li>{@link #FIXEDLENGTH} : 고정 길이 바이트 배열 포맷 (Fixed-Length)</li>
 * <li>{@link #JSON} : JSON 포맷</li>
 * <li>{@link #XML} : XML 포맷</li>
 * <li>{@link #FORM} : URL 인코딩 Form 데이터 포맷
 * (application/x-www-form-urlencoded)</li>
 * <li>{@link #MULTIPART_FORM} : 멀티파트 Form 데이터 포맷 (multipart/form-data)</li>
 * <li>{@link #DELIMITER} : 구분자 기반 문자열 포맷 (Delimited Text, CSV 등)</li>
 * <li>{@link #NONE} : 포맷 없음 (미정의 또는 기본값)</li>
 * </ul>
 *
 * @author sungdon.choi
 */
public enum MessageFormat {

    /** 고정 길이 바이트 배열 포맷 */
    FIXEDLENGTH,

    /** JSON 포맷 */
    JSON,

    /** XML 포맷 */
    XML,

    /** URL 인코딩 Form 데이터 포맷 (application/x-www-form-urlencoded) */
    FORM,

    /** Multipart Form 데이터 포맷 (multipart/form-data) */
    MULTIPART_FORM,

    /** 구분자 기반 문자열 포맷 (Delimited Text, CSV 등) */
    DELIMITER,

    /** 포맷 없음 (미정의 상태 또는 기본값) */
    NONE
}
