package com.scbank.process.api.fw.integration.connector.loadbalance.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;
import com.scbank.process.api.fw.integration.connector.loadbalance.LoadBalanceStrategy;

/**
 * Round-Robin 방식의 로드밸런싱 전략 구현
 *
 * <p>주어진 Endpoint 리스트를 순차적으로 순환하며 하나씩 선택합니다.
 * <p>멀티 스레드 환경에서도 안전하게 인덱스를 증가시키기 위해 {@link AtomicInteger}를 사용합니다.
 *
 * <p>예: 3개의 서버가 있을 경우 호출 순서는 A → B → C → A → B ...
 *
 * <p>주요 특징:
 * <ul>
 *   <li>순서 보장</li>
 *   <li>부하 균등 분산</li>
 *   <li>스레드 안전</li>
 * </ul>
 *
 * <p>사용 예:
 * <pre>{@code
 * LoadBalanceStrategy strategy = new RoundRobinStrategy();
 * Endpoint endpoint = strategy.select(endpointList);
 * }</pre>
 *
 * @author sungdon.choi
 */
public class RoundRobinStrategy implements LoadBalanceStrategy {

    /**
     * 순환 인덱스 (스레드 안전)
     */
    private final AtomicInteger index = new AtomicInteger(0);

    /**
     * 현재 인덱스를 기반으로 Endpoint를 순차적으로 반환합니다.
     *
     * @param targets Endpoint 후보 목록
     * @return 선택된 Endpoint
     * @throws IllegalArgumentException 후보 목록이 비어 있을 경우
     */
    @Override
    public Endpoint select(List<Endpoint> targets) {
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("서버 목록이 비어 있습니다");
        }

        int i = index.getAndUpdate(n -> (n + 1) % targets.size());
        return targets.get(i);
    }
}
