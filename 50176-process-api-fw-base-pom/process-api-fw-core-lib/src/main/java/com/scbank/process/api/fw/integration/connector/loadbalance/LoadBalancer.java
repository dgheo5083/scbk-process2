package com.scbank.process.api.fw.integration.connector.loadbalance;

import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;

/**
 * 연계 시스템 Endpoint 선택을 위한 로드 밸런서 인터페이스
 *
 * <p>여러 개의 연계 대상 Endpoint 중 다음에 사용할 대상을 결정하는 전략을 제공합니다.
 * <ul>
 *   <li>예: 라운드 로빈, 랜덤, 우선순위 기반, 헬스 체크 기반 Failover 등</li>
 *   <li>프레임워크 내부에서 클라이언트 생성 시 동적으로 호출됩니다.</li>
 * </ul>
 *
 * <p>대표 구현 예시:
 * <ul>
 *   <li>{@code RoundRobinLoadBalancer}</li>
 *   <li>{@code FailoverLoadBalancer}</li>
 *   <li>{@code HealthAwareLoadBalancer}</li>
 * </ul>
 *
 * <p>스프링에서는 {@code @Component} 또는 팩토리 방식으로 주입 가능
 *
 * @author sungdon.choi
 */
@FunctionalInterface
public interface LoadBalancer {

    /**
     * 다음에 사용할 Endpoint를 반환합니다.
     *
     * @return 선택된 연계 대상 Endpoint
     */
    Endpoint next();
}
