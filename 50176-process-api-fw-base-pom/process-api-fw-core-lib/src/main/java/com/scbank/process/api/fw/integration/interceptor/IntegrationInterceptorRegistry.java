package com.scbank.process.api.fw.integration.interceptor;

import java.util.List;

/**
 * 연계 시스템 인터셉터 레지스트리 인터페이스
 *
 * <p>
 * 설정 파일에 정의된 인터셉터 이름 목록을 기반으로
 * {@link IntegrationInterceptorChain}을 동적으로 생성하는 책임을 가집니다.
 *
 * <p>
 * 예: "loggingInterceptor", "slaInterceptor" 등의 Bean 이름을 전달받아 해당 인터셉터를 조립
 * <p>
 * 사용 예시:
 * 
 * <pre>{@code
 * List<String> names = List.of("loggingInterceptor", "slaInterceptor");
 * IntegrationInterceptorChain chain = interceptorRegistry.resolve(names);
 * }</pre>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public interface IntegrationInterceptorRegistry {

    /**
     * 설정된 인터셉터 Bean 이름 목록을 기반으로 인터셉터 체인을 구성합니다.
     *
     * @param interceptorBeanNames Spring Bean 이름 목록
     * @return IntegrationInterceptorChain 인터셉터 체인
     */
    IntegrationInterceptorChain resolve(List<String> interceptorBeanNames);
}
