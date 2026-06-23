package com.scbank.process.api.fw.base.gateway.prc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 프로세스 API 게이트웨이 프로퍼티 클래스
 * 
 * @author sungdon.choi
 */
@Data
@ConfigurationProperties(prefix = "prc")
public class PRCGatewayProperties {

	/**
	 * 프로세스 API 어플리케이션 별 게이트웨이 정보
	 */
	private Map<String, Gateway> gateway = new HashMap<>();
	
	/**
	 * 프로세스 API 게이트웨이 정보
	 */
	@Data
	public static class Gateway {
		private String baseUrl;
		private SimulationConfig simulation;
	}
	
	/**
	 * 프로세스 API 게이트웨이별 시뮬레이션 정보
	 */
	@Data
	public static class SimulationConfig {
		private boolean enabled = false;
		private String configLocation = "classpath:simulation/";
	}
}
