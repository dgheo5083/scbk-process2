package com.scbank.process.api.fw.integration.interceptor;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 기본 인터셉터 레지스트리 구현체
 *
 * <p>
 * Spring에서 주입된 {@link IntegrationInterceptor} 목록을
 * 이름 기준(Map 구조)으로 보관하고, 설정된 이름 목록에 따라 {@link IntegrationInterceptorChain}을
 * 생성합니다.
 *
 * <p>
 * 주로 {@code integration.yml} 설정에서 인터셉터 이름을 정의하고,
 * 해당 이름과 일치하는 클래스의 simpleName 기준으로 매핑됩니다.
 * <p>
 * 예:
 * 
 * <pre>{@code
 * interceptors:
 *   - LoggingInterceptor
 *   - SlaInterceptor
 * }</pre>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultIntegrationInterceptorRegistry implements IntegrationInterceptorRegistry {

    /**
     * Bean 이름(String) → 인터셉터 인스턴스 매핑
     */
    private final Map<String, IntegrationInterceptor> interceptorBeans;

    /**
     * 설정된 인터셉터 이름 목록을 기준으로 실제 인스턴스를 조합하여 체인을 구성합니다.
     *
     * @param names 설정 파일에 정의된 인터셉터 이름 목록
     * @return 조립된 인터셉터 체인
     * @throws IllegalArgumentException 매칭되는 인터셉터가 없는 경우
     */
    @Override
    public IntegrationInterceptorChain resolve(List<String> names) {
        if (names == null || names.isEmpty()) {
            return new IntegrationInterceptorChain(List.of()); // 빈 체인 반환
        }

        List<IntegrationInterceptor> interceptors = names.stream()
        		.filter(name -> interceptorBeans.get(name) != null)
                .map(name -> interceptorBeans.get(name))
                .toList();

        return new IntegrationInterceptorChain(interceptors);
    }
}
