package com.scbank.process.api.fw.integration.simulation.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.codec.XmlIntegrationClientCodec;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationHeaderStrategy;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;

/**
 * DefaultIntegrationSimulationRepository Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultIntegrationSimulationRepositoryTest {

    static class TestHeaderStrategy implements IntegrationSimulationHeaderStrategy<Object, Object> {

    	private final String systemId;
    	
    	public TestHeaderStrategy(String systemId) {
    		this.systemId = systemId;
    	}
    	
		@Override
		public boolean supported(String systemId) {
			return this.systemId.equals(systemId);
		}

		@Override
		public Object getHeader(String response) {
			return "HEADER";
		}

		@Override
		public Object getErrorMsg(String response) {
			return "ERROR";
		}
    }
    
    static class TestIntegrationProperties extends IntegrationProperties {
    	
    	private Map<String, IntegrationSystemConfig> systems = new HashMap<>();
    	
    	void addSystem(String systemId, IntegrationSystemConfig config) {
    		systems.put(systemId, config);
    	}

		@Override
		public Map<String, IntegrationSystemConfig> getSystem() {
			return this.systems;
		}
    }
    
    private DefaultIntegrationSimulationRepository spyRepo(IntegrationProperties props, List<IntegrationSimulationHeaderStrategy<?, ?>> strategies) {
    	DefaultIntegrationSimulationRepository repo = spy(new DefaultIntegrationSimulationRepository(props, strategies, new ObjectMapper(), mock(XmlIntegrationClientCodec.class), mock(IntegrationMessageContextCreator.class)));
    	
    	doReturn("<xml></xml>")
    		.when(repo)
    		.getResponse(anyString(), anyString());
    	return repo;
    }
    
    @Test
    void getResponse_throws_when_system_not_found() {
    	IntegrationProperties props = new TestIntegrationProperties();
    	
    	DefaultIntegrationSimulationRepository repo = new DefaultIntegrationSimulationRepository(props, List.of(), null, null, null);
    	
    	assertThrows(IllegalStateException.class, () -> repo.getResponse("SYS", "IF"));
    }
    
    @Test
    void getResponse_throws_when_simulation_disabled() {
    	TestIntegrationProperties props = new TestIntegrationProperties();
    	
    	IntegrationSystemConfig config = mock(IntegrationSystemConfig.class);
    	when(config.simulation()).thenReturn(null);
    	
    	props.addSystem("SYS", config);
    	
    	DefaultIntegrationSimulationRepository repo = new DefaultIntegrationSimulationRepository(props, List.of(), null, null, null);
    	
    	assertThrows(IllegalStateException.class, () -> repo.getResponse("SYS", "IF"));
    }
    
//    @Test
//    void getResponse_typed_returns_null_on_exception() {
//    	TestIntegrationProperties props = new TestIntegrationProperties();
//    	
//    	IntegrationSystemConfig config = mock(IntegrationSystemConfig.class);
//    	when(config.charset()).thenReturn("UTF-8");
//    	
//    	props.addSystem("SYS", config);
//    	
//    	DefaultIntegrationSimulationRepository repo = spyRepo(props, List.of());
//    	
//    	doReturn("<xml/>")
//    		.when(repo)
//    		.getResponse("SYS", "IF");
//    	
//    	IMessageObject result = repo.getResponse("SYS", "IF", IMessageObject.class);
//    	
//    	assertNull(result);
//    }
    
    @Test
    void getHeader_returns_null_when_no_strategies() {
    	TestIntegrationProperties props = new TestIntegrationProperties();
    	
    	DefaultIntegrationSimulationRepository repo = spyRepo(props, List.of());
    	
    	assertNull(repo.getHeader("SYS", "IF", Object.class));
    }
    
    @Test
    void getHeader_returns_null_when_no_supported() {
    	TestIntegrationProperties props = new TestIntegrationProperties();
    	
    	DefaultIntegrationSimulationRepository repo = spyRepo(props, List.of(new TestHeaderStrategy("OTHER")));
    	
    	assertNull(repo.getHeader("SYS", "IF", Object.class));
    }
    
    @Test
    void getHeader_returns_value_when_supported() {
    	TestIntegrationProperties props = new TestIntegrationProperties();
    	
    	DefaultIntegrationSimulationRepository repo = spyRepo(props, List.of(new TestHeaderStrategy("SYS")));
    	
    	Object result = repo.getHeader("SYS", "IF", Object.class);
    	
    	assertEquals("HEADER", result);
    }
    
    @Test
    void getError_returns_null_when_no_strategies() {
    	TestIntegrationProperties props = new TestIntegrationProperties();
    	
    	DefaultIntegrationSimulationRepository repo = spyRepo(props, List.of());
    	
    	assertNull(repo.getError("SYS", "IF", Object.class));
    }
    
    @Test
    void getError_returns_error_when_supported() {
    	TestIntegrationProperties props = new TestIntegrationProperties();
    	
    	DefaultIntegrationSimulationRepository repo = spyRepo(props, List.of(new TestHeaderStrategy("SYS")));
    	
    	Object result = repo.getError("SYS", "IF", Object.class);
    	
    	assertEquals("ERROR", result);
    }
}
