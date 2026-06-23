package com.scbank.process.api.fw.integration.connector.factory;

import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.connector.SimpleTcpConnector;
import com.scbank.process.api.fw.integration.connector.IntegrationConnector;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;

/**
 * 기본 TCP 클라이언트 팩토리 구현체
 *
 * <p>IntegrationClientFactory를 상속하여,
 * 지정된 시스템 설정(IntegrationSystemConfig)과 채널 초기화 설정을 기반으로 Netty 기반 TCP 클라이언트(NettyTcpClient)를 생성합니다.</p>
 *
 * <p>로드밸런싱 또는 헬스체크 전략 없이,
 * 단일 구성(config)만을 기반으로 클라이언트를 생성하는 기본 구현입니다.</p>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 18.
 */
public class SimpleTcpConnectorFactory extends IntegrationConnectorFactory {

	 /**
     * 시스템 ID, 설정 정보(config), 채널 초기화 구성(initializer)을 기반으로 Netty 기반 TCP 클라이언트를 생성합니다.
     *
     * @param systemId    대상 시스템 식별자 (예: "MCI", "EDMI" 등)
     * @param config      시스템별 TCP 통신 구성 (host, port, encoding 등 포함)
     * @param initializer Netty 채널 초기화 구성 (Handler, Codec 등 설정)
     * @return Netty 기반 TCP 클라이언트 인스턴스
     */
	@Override
	public IntegrationConnector<?, ?> create(String systemId,
			IntegrationSystemConfig config,
			IntegrationConnectorChannelInitializer initializer) {
		return new SimpleTcpConnector(config, initializer);
	}
}