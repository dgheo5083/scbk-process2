package com.scbank.process.api.fw.integration.connector.loadbalance;

import java.util.List;

import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;

/**
 * 연계 시스템 로드 밸런싱 전략 인터페이스
 *
 * <p>여러 Endpoint 후보 중 하나를 선택하는 전략을 제공합니다.
 * <ul>
 *   <li>{@link LoadBalancer}에서 위임받아 실제 Endpoint 선택을 담당</li>
 *   <li>구현체로 라운드로빈, 랜덤, 헬스 체크 기반 전략 등이 가능합니다.</li>
 * </ul>
 *
 * <p>기본 전략 {@code Default}는 첫 번째 Endpoint를 반환합니다.
 *
 * <p>예시 구현:
 * <ul>
 *   <li>{@code RoundRobinStrategy}</li>
 *   <li>{@code RandomStrategy}</li>
 *   <li>{@code FailoverStrategy}</li>
 * </ul>
 *
 * @author sungdon.choi
 */
@FunctionalInterface
public interface LoadBalanceStrategy {

    /**
     * 기본 전략: 첫 번째 Endpoint 반환
     */
    LoadBalanceStrategy Default = (targets) -> targets.get(0);

    /**
     * 주어진 Endpoint 후보 중 하나를 선택합니다.
     *
     * @param targets 후보 Endpoint 목록
     * @return 선택된 Endpoint
     */
    Endpoint select(List<Endpoint> targets);
}
