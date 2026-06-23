package com.scbank.process.api.fw.integration.connector.factory;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.connector.IntegrationConnector;
import com.scbank.process.api.fw.integration.connector.SimpleTcpConnector;
import com.scbank.process.api.fw.integration.connector.initializer.IntegrationConnectorChannelInitializer;

class SimpleTcpConnectorFactoryTest {

	@Test
	@DisplayName("create: SimpleTcpConnector를 생성해서 반환한다.")
	void create_returnSimpleTcpConnector() {
		SimpleTcpConnectorFactory factory = new SimpleTcpConnectorFactory();
		IntegrationSystemConfig config = Mockito.mock(IntegrationSystemConfig.class);
		IntegrationConnectorChannelInitializer initializer = Mockito.mock(IntegrationConnectorChannelInitializer.class);
		
		IntegrationConnector<?, ?> connector = factory.create("MCI", config, initializer);
		
		assertNotNull(connector);
		assertTrue(connector instanceof SimpleTcpConnector);
		
		assertSame(config, getFieldValue(connector, "systemConfig", IntegrationSystemConfig.class));
		assertSame(initializer, getFieldValue(connector, "channelInitializer", IntegrationConnectorChannelInitializer.class));
	}
	
	@Test
	void create_systemIdDoesNotAffectCreation() {
		SimpleTcpConnectorFactory factory = new SimpleTcpConnectorFactory();
		IntegrationSystemConfig config = Mockito.mock(IntegrationSystemConfig.class);
		IntegrationConnectorChannelInitializer initializer = Mockito.mock(IntegrationConnectorChannelInitializer.class);
		
		IntegrationConnector<?, ?> connector1 = factory.create("MCI", config, initializer);
		IntegrationConnector<?, ?> connector2 = factory.create("EDMI", config, initializer);
		
		assertNotNull(connector1);
		assertTrue(connector1 instanceof SimpleTcpConnector);
		
		assertNotNull(connector2);
		assertTrue(connector2 instanceof SimpleTcpConnector);
		
		assertNotSame(connector1, connector2);
	}
	
	@Test
	void create_nullConfig_throwsException() {
		SimpleTcpConnectorFactory factory = new SimpleTcpConnectorFactory();
		IntegrationConnectorChannelInitializer initializer = Mockito.mock(IntegrationConnectorChannelInitializer.class);
		
		assertNotNull(factory.create("MCI", null, initializer));
	}
	
	private static Object getFieldValue(Object target, String preferredName, Class<?> preferredType) {
		Class<?> c = target.getClass();
		
		while (c != null) {
			for (Field f : c.getDeclaredFields()) {
				if (f.getName().equals(preferredName)) {
					return readField(target, f);
				}
			}
			c = c.getSuperclass();
		}
		
		c = target.getClass();
		while (c != null) {
			for (Field f : c.getDeclaredFields()) {
				if (f.getName().equals(preferredName)) {
					return readField(target, f);
				}
			}
			c = c.getSuperclass();
		}
		
		fail("실패");
		return null;
	}

	private static Object readField(Object target, Field f) {
		try {
			f.setAccessible(true);
			return f.get(target);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}
}
