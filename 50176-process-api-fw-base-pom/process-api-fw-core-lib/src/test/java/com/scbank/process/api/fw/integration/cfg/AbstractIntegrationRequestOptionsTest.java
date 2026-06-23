package com.scbank.process.api.fw.integration.cfg;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * AbstractIntegrationRequestOptions Test Class
 */
class AbstractIntegrationRequestOptionsTest {

    private AbstractIntegrationRequestOptions options;

    @BeforeEach
    void setUp() {
        options = new AbstractIntegrationRequestOptions();
    }

    @Nested
    @DisplayName("interfaceId property tests")
    class InterfaceIdTests {

        @Test
        @DisplayName("Should set and get interfaceId")
        void shouldSetAndGetInterfaceId() {
            options.setInterfaceId("IF001");
            assertEquals("IF001", options.getInterfaceId());
        }

        @Test
        @DisplayName("Should return null when not set")
        void shouldReturnNullWhenNotSet() {
            assertNull(options.getInterfaceId());
        }
    }

    @Nested
    @DisplayName("captureSystem property tests")
    class CaptureSystemTests {

        @Test
        @DisplayName("Should set and get captureSystem")
        void shouldSetAndGetCaptureSystem() {
            options.setCaptureSystem("OLTP");
            assertEquals("OLTP", options.getCaptureSystem());
        }
    }

    @Nested
    @DisplayName("timeout property tests")
    class TimeoutTests {

        @Test
        @DisplayName("Should set and get connectTimeout")
        void shouldSetAndGetConnectTimeout() {
            options.setConnectTimeout(5000L);
            assertEquals(5000L, options.getConnectTimeout());
        }

        @Test
        @DisplayName("Should set and get readTimeout")
        void shouldSetAndGetReadTimeout() {
            options.setReadTimeout(30000L);
            assertEquals(30000L, options.getReadTimeout());
        }

        @Test
        @DisplayName("Should default to zero")
        void shouldDefaultToZero() {
            assertEquals(0L, options.getConnectTimeout());
            assertEquals(0L, options.getReadTimeout());
        }
    }

    @Nested
    @DisplayName("exceptionOnError property tests")
    class ExceptionOnErrorTests {

        @Test
        @DisplayName("Should set and get exceptionOnError")
        void shouldSetAndGetExceptionOnError() {
            options.setExceptionOnError(true);
            assertTrue(options.isExceptionOnError());

            options.setExceptionOnError(false);
            assertFalse(options.isExceptionOnError());
        }
    }

    @Nested
    @DisplayName("simulationMode property tests")
    class SimulationModeTests {

        @Test
        @DisplayName("Should set and get simulationMode")
        void shouldSetAndGetSimulationMode() {
            options.setSimulationMode(true);
            assertTrue(options.isSimulationMode());
        }

        @Test
        @DisplayName("Should default to false")
        void shouldDefaultToFalse() {
            assertFalse(options.isSimulationMode());
        }
    }

    @Nested
    @DisplayName("attributes property tests")
    class AttributesTests {

        @Test
        @DisplayName("Should set and get attributes map")
        void shouldSetAndGetAttributesMap() {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("key1", "value1");
            attrs.put("key2", 123);

            options.setAttributes(attrs);

            assertNotNull(options.getAttributes());
            assertEquals("value1", options.getAttributes().get("key1"));
            assertEquals(123, options.getAttributes().get("key2"));
        }

        @Test
        @DisplayName("Should get attribute as string")
        void shouldGetAttributeAsString() {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("key1", "value1");
            attrs.put("key2", 123);
            options.setAttributes(attrs);

            assertEquals("value1", options.getAttribute("key1"));
            assertEquals("123", options.getAttribute("key2"));
        }

        @Test
        @DisplayName("Should return empty string for non-existent attribute")
        void shouldReturnEmptyStringForNonExistentAttribute() {
            Map<String, Object> attrs = new HashMap<>();
            options.setAttributes(attrs);

            assertEquals("", options.getAttribute("nonExistent"));
        }

        @Test
        @DisplayName("Should get attribute with type")
        void shouldGetAttributeWithType() {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("stringKey", "stringValue");
            attrs.put("intKey", 42);
            options.setAttributes(attrs);

            assertEquals("stringValue", options.getAttribute("stringKey", String.class));
            assertEquals(42, options.getAttribute("intKey", Integer.class));
        }

        @Test
        @DisplayName("Should return null for non-existent typed attribute")
        void shouldReturnNullForNonExistentTypedAttribute() {
            Map<String, Object> attrs = new HashMap<>();
            options.setAttributes(attrs);

            assertNull(options.getAttribute("nonExistent", String.class));
        }

        @Test
        @DisplayName("Should throw ClassCastException for wrong type")
        void shouldThrowClassCastExceptionForWrongType() {
            Map<String, Object> attrs = new HashMap<>();
            attrs.put("stringKey", "stringValue");
            options.setAttributes(attrs);

            assertThrows(ClassCastException.class, () ->
                    options.getAttribute("stringKey", Integer.class));
        }

        @Test
        @DisplayName("Should set individual attribute")
        void shouldSetIndividualAttribute() {
            options.setAttribute("newKey", "newValue");

            assertNotNull(options.getAttributes());
            assertEquals("newValue", options.getAttribute("newKey"));
        }

        @Test
        @DisplayName("Should create attributes map if null when setting attribute")
        void shouldCreateAttributesMapIfNullWhenSettingAttribute() {
            assertNull(options.getAttributes());

            options.setAttribute("key", "value");

            assertNotNull(options.getAttributes());
            assertEquals("value", options.getAttribute("key"));
        }
    }
}
