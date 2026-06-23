package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 확장 E2E 메시지에서 특정 세그먼트 구간(start~end) 내에 포함될 경우 해당 필드를 무시하도록 지정하는 어노테이션입니다.
 * <p>
 * 예: _DNFE2E_ ~ _/DNFE2E_ 구간 내에 포함된 필드 제외 처리
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtE2EIgnoreSegment {

    /**
     * 무시 구간의 시작 문자열
     */
    String start();

    /**
     * 무시 구간의 종료 문자열
     */
    String end();
}
