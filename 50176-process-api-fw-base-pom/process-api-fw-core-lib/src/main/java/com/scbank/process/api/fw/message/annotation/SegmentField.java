package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.scbank.process.api.fw.message.enums.DelimiterPosition;
import com.scbank.process.api.fw.message.enums.DelimiterType;

/**
 * <pre>
 * 메시지 내 세그먼트(구간)를 정의하기 위한 어노테이션
 * 반복 구조나 구분자 기반 구조, 고정 길이 구조의 파싱 처리에 사용됨
 * </pre>
 *
 * @author sungdon.choi
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SegmentField {

    /**
     * <pre>
     * 구분자 바이트 배열
     * 세그먼트가 구분자 기반(DELIMITER)일 경우 사용
     * 예) {0x1E} = ASCII Record Separator
     * </pre>
     */
    byte[] delimiter() default {};

    /**
     * <pre>
     * 세그먼트 구분 방식
     * - NONE: 구분 방식 없음
     * - FIXED: 고정 길이 구조
     * - DELIMITER: 구분자 기반 구조
     * </pre>
     */
    DelimiterType type() default DelimiterType.NONE;

    /**
     * <pre>
     * 구분자의 위치
     * - NONE: 위치 정보 없음
     * - PREFIX: 구분자가 앞에 위치 (ex. '|' + 필드값)
     * - SUFFIX: 구분자가 뒤에 위치 (ex. 필드값 + '|')
     * - WRAPPED: 구분자가 양쪽에 감싸는 형태 (ex. '|' + 필드값 + '|')
     * </pre>
     */
    DelimiterPosition position() default DelimiterPosition.NONE;
}
