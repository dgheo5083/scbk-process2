package com.scbank.process.api.fw.integration.connector.factory;

import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.connector.IntegrationConnector;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;

import lombok.RequiredArgsConstructor;

/**
 * 연계 클라이언트 생성을 위한 추상 팩토리 클래스
 *
 * <p>시스템 ID, 연계 설정, 채널 초기화 전략 등을 기반으로 {@link IntegrationClient} 인스턴스를 생성합니다.
 *
 * <p>프로토콜(TCP, HTTP, gRPC 등) 또는 시스템 유형별로 하위 구현체를 만들어 확장 가능하며,
 * 프레임워크에서는 본 추상 클래스를 상속한 팩토리를 통해 연계 클라이언트를 생성하게 됩니다.
 *
 * <p>대표 구현체 예:
 * <ul>
 *     <li>{@code DefaultTcpClientFactory}</li>
 *     <li>{@code HttpClientFactory}</li>
 *     <li>{@code EdmiClientFactory} 등</li>
 * </ul>
 *
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public abstract class IntegrationConnectorFactory {

    /**
     * 연계 클라이언트를 생성합니다.
     *
     * @param systemId    연계 시스템 ID (예: MCI, FEP)
     * @param config      연계 시스템 설정 (타임아웃, 포맷, URL 등 포함)
     * @param initializer 채널 파이프라인 설정 전략 (Netty 전용)
     * @return 생성된 연계 클라이언트
     */
    public abstract IntegrationConnector<?, ?> create(
        String systemId,
        IntegrationProperties.IntegrationSystemConfig config,
        IntegrationConnectorChannelInitializer initializer
    );
}
