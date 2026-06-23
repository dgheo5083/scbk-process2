package com.scbank.process.api.fw.integration.connector.loadbalance;

import java.util.List;

import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;

import lombok.RequiredArgsConstructor;

/**
 * 기본 로드밸런서 구현체
 *
 * <p>내부적으로 {@link LoadBalanceStrategy}를 사용하여 대상 Endpoint 중 하나를 선택합니다.
 * <p>Endpoint 목록은 생성자 주입을 통해 전달되며, 전략에 따라 유연하게 분기할 수 있습니다.
 *
 * <p>예: RoundRobin, Failover, HealthAware 등 다양한 전략을 {@link LoadBalanceStrategy}로 추상화 가능
 *
 * <p>구성 예시:
 * <pre>{@code
 * LoadBalancer lb = new DefaultLoadBalancer(endpoints, new RoundRobinStrategy());
 * Endpoint selected = lb.next();
 * }</pre>
 *
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class DefaultLoadBalancer implements LoadBalancer {

    /**
     * 대상 Endpoint 목록 (시스템 설정 기반)
     */
    private final List<Endpoint> targets;

    /**
     * Endpoint 선택 전략
     */
    private final LoadBalanceStrategy loadBalanceStrategy;

    /**
     * 다음 호출에 사용할 Endpoint 선택
     *
     * @return 선택된 Endpoint
     */
    @Override
    public Endpoint next() {
        return loadBalanceStrategy.select(targets);
    }
}
