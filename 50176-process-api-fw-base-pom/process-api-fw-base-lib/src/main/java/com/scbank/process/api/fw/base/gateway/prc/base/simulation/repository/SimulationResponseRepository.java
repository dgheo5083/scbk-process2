package com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 시뮬레이션 응답 레파지토리 인터페이스
 */
public interface SimulationResponseRepository {

	/**
	 * 시나리오별 응답 데이터를 가져온다.
	 * @param configLocation
	 * @param fileName
	 * @param senario
	 * @return
	 */
	JsonNode getResponse(String configLocation, String fileName, String senario);
}
