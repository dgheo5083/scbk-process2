package com.scbank.process.api.fw.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.enums.CenterMode;
import com.scbank.process.api.fw.integration.IntegrationProperties.Endpoint;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.IntegrationProperties.ReboundStrategyPolicy;
import com.scbank.process.api.fw.integration.IntegrationProperties.SimulationConfig;
import com.scbank.process.api.fw.integration.IntegrationProperties.SocketOption;
import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * IntegrationProperties Test Class
 */
class IntegrationPropertiesTest {

    private IntegrationProperties properties;

    @BeforeEach
    void setUp() {
        properties = new IntegrationProperties();
    }

    @Nested
    @DisplayName("enabled property tests")
    class EnabledPropertyTests {

        @Test
        @DisplayName("Should set and get enabled property")
        void shouldSetAndGetEnabledProperty() {
            properties.setEnabled(true);
            assertTrue(properties.isEnabled());

            properties.setEnabled(false);
            assertFalse(properties.isEnabled());
        }

        @Test
        @DisplayName("Should default to false")
        void shouldDefaultToFalse() {
            assertFalse(properties.isEnabled());
        }
    }

    @Nested
    @DisplayName("system property tests")
    class SystemPropertyTests {

        @Test
        @DisplayName("Should set and get system map")
        void shouldSetAndGetSystemMap() {
            Map<String, IntegrationSystemConfig> systemMap = new HashMap<>();
            IntegrationSystemConfig config = new IntegrationSystemConfig(
            		true,
                    MessageFormat.FIXEDLENGTH,
                    "UTF-8",
                    List.of(new Endpoint(CenterMode.MAIN, "127.0.0.1", 0, "")),
                    List.of("interceptor1"),
                    Map.of("key", "value"),
                    Map.of("header", "value"),
                    null,
                    null,
                    null,
                    new SocketOption(1000, 2000));
            systemMap.put("MCI", config);

            properties.setSystem(systemMap);

            assertNotNull(properties.getSystem());
            assertEquals(1, properties.getSystem().size());
            assertNotNull(properties.getSystem().get("MCI"));
        }

        @Test
        @DisplayName("Should return null when system not set")
        void shouldReturnNullWhenSystemNotSet() {
            assertNull(properties.getSystem());
        }
    }

    @Nested
    @DisplayName("IntegrationSystemConfig record tests")
    class IntegrationSystemConfigTests {

        @Test
        @DisplayName("Should create IntegrationSystemConfig with all parameters")
        void shouldCreateIntegrationSystemConfigWithAllParameters() {
            IntegrationSystemConfig config = new IntegrationSystemConfig(
            		true,
                    MessageFormat.XML,
                    "EUC-KR",
                    List.of(new Endpoint(CenterMode.MAIN, "127.0.0.1", 0, "")),
                    List.of("interceptor1", "interceptor2"),
                    Map.of("prop1", "val1"),
                    Map.of("header1", "headerVal1"),
                    null,
                    new ReboundStrategyPolicy(5, "itemList"),
                    new SimulationConfig(true, "classpath:/sim/"),
                    new SocketOption(1000, 2000));

            assertEquals(MessageFormat.XML, config.format());
            assertEquals("EUC-KR", config.charset());
            assertEquals(2, config.interceptors().size());
            assertEquals("val1", config.properties().get("prop1"));
            assertEquals("headerVal1", config.defaultHeaders().get("header1"));
            assertNotNull(config.reboundStrategyPolicy());
            assertNotNull(config.simulation());
        }

        @Test
        @DisplayName("Should create IntegrationSystemConfig with null values")
        void shouldCreateIntegrationSystemConfigWithNullValues() {
            IntegrationSystemConfig config = new IntegrationSystemConfig(
            		true, null, null, null, null, null, null, null, null, null, null);

            assertNull(config.format());
            assertNull(config.charset());
            assertNull(config.interceptors());
        }
    }

    @Nested
    @DisplayName("ReboundStrategyPolicy record tests")
    class ReboundStrategyPolicyTests {

        @Test
        @DisplayName("Should create ReboundStrategyPolicy with values")
        void shouldCreateReboundStrategyPolicyWithValues() {
            ReboundStrategyPolicy policy = new ReboundStrategyPolicy(10, "dataList");

            assertEquals(10, policy.maxLoopCnt());
            assertEquals("dataList", policy.defaultListFieldName());
        }

        @Test
        @DisplayName("Should create ReboundStrategyPolicy with zero and null")
        void shouldCreateReboundStrategyPolicyWithZeroAndNull() {
            ReboundStrategyPolicy policy = new ReboundStrategyPolicy(0, null);

            assertEquals(0, policy.maxLoopCnt());
            assertNull(policy.defaultListFieldName());
        }
    }

    @Nested
    @DisplayName("SimulationConfig record tests")
    class SimulationConfigTests {

        @Test
        @DisplayName("Should create SimulationConfig with enabled true")
        void shouldCreateSimulationConfigWithEnabledTrue() {
            SimulationConfig config = new SimulationConfig(true, "classpath:/simulation/");

            assertTrue(config.enabled());
            assertEquals("classpath:/simulation/", config.configLocation());
        }

        @Test
        @DisplayName("Should create SimulationConfig with enabled false")
        void shouldCreateSimulationConfigWithEnabledFalse() {
            SimulationConfig config = new SimulationConfig(false, null);

            assertFalse(config.enabled());
            assertNull(config.configLocation());
        }
    }
    
    @Test
    void record_Endpoint_isCovered() {
    	Endpoint e =  new Endpoint(null, "127.0.0.1", 8080, "http://localhost");
    	
    	assertNull(e.center());
    	assertEquals("127.0.0.1", e.ip());
    	assertEquals(8080, e.port());
    	assertEquals("http://localhost", e.url());
    }
    
    void record_SocketOption_isCovered() {
    	SocketOption s = new SocketOption(1000L, 2000L);
    	
    	assertEquals(1000L, s.connectTimeout());
    	assertEquals(2000L, s.readTimeout());
    	
    }
}
