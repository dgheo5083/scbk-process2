package com.scbank.process.api.fw.security.xss.ruleset;

import lombok.Data;

/**
 * XSS 필터링을 위한 단일 룰 정보를 표현하는 모델 클래스입니다.
 * <p>
 * {@code target} 문자열을 {@code replacement} 문자열로 치환하는 방식으로 동작합니다.
 * 정규표현식 기반 치환도 지원합니다.
 * </p>
 */
@Data
public class XssRuleInfo {

    /**
     * 치환 대상 문자열 또는 정규표현식.
     * 예: {@code <}, {@code script}, {@code eval\\((.*)\\)}
     */
    private String target;

    /**
     * 치환될 문자열.
     * 예: {@code &lt;}, 빈 문자열 ("") 등
     */
    private String replacement;
}
