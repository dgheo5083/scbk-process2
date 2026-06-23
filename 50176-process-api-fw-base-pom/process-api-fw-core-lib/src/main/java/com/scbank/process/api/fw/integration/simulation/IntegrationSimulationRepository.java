package com.scbank.process.api.fw.integration.simulation;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * 시스템 연계 대응답 메시지 저장소 관리 인터페이스
 * 
 * @author sungdon.choi
 */
public interface IntegrationSimulationRepository {

	/**
	 * 
	 * @param systemId
	 * @param interfaceId
	 * @return
	 */
	String getResponse(String systemId, String interfaceId);

	<O extends IMessageObject> O getResponse(String systemId, String interfaceId, Class<O> responseType);

	default <H> H getHeader(String systemId, String interfaceId, Class<H> headerType) {
		return null;
	}
	
	default <E> E getError(String systemId, String interfaceId, Class<E> headerType) {
		return null;
	}
}
