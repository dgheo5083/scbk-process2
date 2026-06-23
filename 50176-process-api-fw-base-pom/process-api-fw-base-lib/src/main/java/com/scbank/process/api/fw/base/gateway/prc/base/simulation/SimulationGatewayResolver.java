package com.scbank.process.api.fw.base.gateway.prc.base.simulation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.gateway.prc.PRCGatewayProperties;
import com.scbank.process.api.fw.base.gateway.prc.PRCGatewayProperties.Gateway;

import lombok.RequiredArgsConstructor;

/**
 * 프로세스 API 게이트웨이 시뮬레이션 정보 획득 Resolver 클래스
 */
@RequiredArgsConstructor
@Component
public class SimulationGatewayResolver {

	private final PRCGatewayProperties properties;

	/**
	 * 
	 * @param targetUrl 게이트웨이 요청 target url
	 * @return
	 */
	public Optional<ResolvedGateway> resolve(String targetUrl) {
		return properties.getGateway().entrySet().stream()
				.filter(e -> targetUrl.startsWith(e.getValue().getBaseUrl()))
				.map((e) -> new ResolvedGateway(e.getKey(), e.getValue()))
				.findFirst();
	}

	/**
	 * target 별 게이트웨이 정보
	 */
	public static record ResolvedGateway(String target, Gateway config) {

	}
}
