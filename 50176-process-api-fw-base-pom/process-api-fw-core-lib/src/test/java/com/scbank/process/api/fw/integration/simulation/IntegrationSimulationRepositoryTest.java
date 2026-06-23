package com.scbank.process.api.fw.integration.simulation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;

class IntegrationSimulationRepositoryTest {

	private final IntegrationSimulationRepository repository = new IntegrationSimulationRepository() {
		
		@Override
		public <O extends IMessageObject> O getResponse(String systemId, String interfaceId, Class<O> responseType) {
			return null;
		}
		
		@Override
		public String getResponse(String systemId, String interfaceId) {
			return null;
		}
	};
	
	@Test
	void defaultGetHeaderReturnNull() {
		Object header = repository.getHeader("HOST", "INF", Object.class);
		assertNull(header);
	}
	
	@Test
	void defaultGetErrorReturnNull() {
		Object header = repository.getError("HOST", "INF", Object.class);
		assertNull(header);
	}
}
