package com.scbank.process.api.fw.security.xss.sanitizer;

/**
 * XSS 공격으로부터 입력 데이터를 보호하기 위한 정화(Sanitize) 기능을 제공하는 인터페이스입니다.
 * <p>
 * 이 인터페이스를 구현하는 클래스는 입력 문자열에 대해 위험한 문자를 치환하거나 제거하여
 * 안전한 문자열로 변환하는 기능을 수행합니다.
 * </p>
 */
public interface IXssSanitizer {

    /**
     * 주어진 입력 문자열에 대해 XSS 보호 룰을 적용하여 정화된 문자열을 반환합니다.
     *
     * @param source 입력 문자열
     * @return XSS 위험 요소가 제거된 정화된 문자열
     */
    String sanitize(String source);
}
