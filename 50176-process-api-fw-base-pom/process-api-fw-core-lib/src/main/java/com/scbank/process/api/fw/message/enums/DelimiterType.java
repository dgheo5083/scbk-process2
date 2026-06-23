package com.scbank.process.api.fw.message.enums;

/**
 * 메시지 세그먼트 또는 가변 필드의 구분 방식 정의
 *
 * <pre>
 * - NONE: 구분 방식 없음
 * - FIXED: 고정 길이 기반 세그먼트
 * - DELIMITER: 구분자 기반 (ex. RS = 0x1E)
 * - LENGTH_BASED: 길이 prefix 기반 세그먼트 (ex. 02홍길동)
 * - TAG_BASED: 태그/필드명 기반 (ex. NM:홍길동|AGE:33)
 * </pre>
 *
 * @author sungdon.choi
 */
public enum DelimiterType {

    /**
     * 구분 방식 없음 (기본값)
     */
    NONE,

    /**
     * 고정 길이 기반 구분
     */
    FIXED,

    /**
     * 구분자 기반 구분
     */
    DELIMITER,

    /**
     * 길이 prefix 기반 구분
     */
    LENGTH_BASED,

    /**
     * 태그 기반 구분 (예: NM:홍길동|AGE:30)
     */
    TAG_BASED
}
