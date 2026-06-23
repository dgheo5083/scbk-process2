package com.scbank.process.api.fw.security.xss.ruleset;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * XSS 룰셋을 등록 및 제공하는 레지스트리 인터페이스입니다.
 * <p>
 * 이 인터페이스는 Spring의 {@link InitializingBean} 및 {@link DisposableBean}을 구현하여
 * 빈 초기화 및 소멸 시점에 룰셋 초기화 및 정리를 수행할 수 있도록 설계되었습니다.
 * </p>
 */
public interface IXssRuleRegistry extends InitializingBean, DisposableBean {

    /**
     * Spring 컨텍스트 초기화 시 호출되며, 내부적으로 {@link #init()} 메서드를 실행합니다.
     *
     * @throws Exception 초기화 중 오류 발생 시
     */
    @Override
    default void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * Spring 컨텍스트 종료 시 호출되며, 보유 중인 XSS 룰 목록을 정리합니다.
     *
     * @throws Exception 소멸 중 오류 발생 시
     */
    @Override
    default void destroy() throws Exception {
        List<XssRuleInfo> xssRules = getXssRules();
        if (xssRules != null) {
            xssRules.clear();
        }
    }

    /**
     * 룰셋 초기화를 수행합니다.
     * <p>
     * XML 파싱, 파일 로딩 등의 동작은 이 메서드 내에서 수행되어야 합니다.
     * </p>
     */
    void init();

    /**
     * 등록된 XSS 룰셋을 반환합니다.
     *
     * @return XSS 룰 정보 목록
     */
    List<XssRuleInfo> getXssRules();
}
