package com.scbank.process.api.fw.security.xss.ruleset;

import java.util.List;

/**
 * XSS 필터링을 위한 룰셋을 외부 리소스(XML 등)에서 로딩하는 기능을 정의한 인터페이스입니다.
 * <p>
 * 구현체는 클래스패스 또는 외부 위치에서 룰 정보를 파싱하여 {@link XssRuleInfo} 리스트로 반환해야 합니다.
 * </p>
 */
public interface IXssRuleLoader {

    /**
     * XSS 룰셋을 로딩하여 {@link XssRuleInfo} 목록으로 반환합니다.
     *
     * @return 파싱된 XSS 룰 리스트
     */
    List<XssRuleInfo> load();
}
