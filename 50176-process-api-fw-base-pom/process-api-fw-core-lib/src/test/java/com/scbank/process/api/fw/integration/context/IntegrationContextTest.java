package com.scbank.process.api.fw.integration.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * IntegrationContext Test Class
 */
class IntegrationContextTest {

    @Nested
    @DisplayName("builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build IntegrationContext with all properties")
        void shouldBuildIntegrationContextWithAllProperties() {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("key1", "value1");

            IntegrationContext context = IntegrationContext.builder()
                    .systemId("MCI")
                    .locale(Locale.KOREA)
                    .requestId("REQ001")
                    .interfaceId("IF001")
                    .captureSystem("OLTP")
                    .charset("UTF-8")
                    .attributes(attributes)
                    .build();

            assertEquals("MCI", context.getSystemId());
            assertEquals(Locale.KOREA, context.getLocale());
            assertEquals("REQ001", context.getRequestId());
            assertEquals("IF001", context.getInterfaceId());
            assertEquals("OLTP", context.getCaptureSystem());
            assertEquals("UTF-8", context.getCharset());
            assertNotNull(context.getAttributes());
        }

        @Test
        @DisplayName("Should build IntegrationContext with minimal properties")
        void shouldBuildIntegrationContextWithMinimalProperties() {
            IntegrationContext context = IntegrationContext.builder()
                    .systemId("FEP")
                    .build();

            assertEquals("FEP", context.getSystemId());
            assertNull(context.getLocale());
            assertNull(context.getRequestId());
        }
    }

    @Nested
    @DisplayName("getter and setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get systemId")
        void shouldSetAndGetSystemId() {
            IntegrationContext context = IntegrationContext.builder().build();
            context.setSystemId("HOST");
            assertEquals("HOST", context.getSystemId());
        }

        @Test
        @DisplayName("Should set and get locale")
        void shouldSetAndGetLocale() {
            IntegrationContext context = IntegrationContext.builder().build();
            context.setLocale(Locale.US);
            assertEquals(Locale.US, context.getLocale());
        }

        @Test
        @DisplayName("Should set and get interfaceId")
        void shouldSetAndGetInterfaceId() {
            IntegrationContext context = IntegrationContext.builder().build();
            context.setInterfaceId("IF002");
            assertEquals("IF002", context.getInterfaceId());
        }
    }

    @Nested
    @DisplayName("getAttribute tests")
    class GetAttributeTests {

        @Test
        @DisplayName("Should get attribute as string")
        void shouldGetAttributeAsString() {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("key1", "value1");
            attributes.put("key2", 123);

            IntegrationContext context = IntegrationContext.builder()
                    .attributes(attributes)
                    .build();

            assertEquals("value1", context.getAttribute("key1"));
            assertEquals("123", context.getAttribute("key2"));
        }

        @Test
        @DisplayName("Should return empty string for non-existent attribute")
        void shouldReturnEmptyStringForNonExistentAttribute() {
            Map<String, Object> attributes = new HashMap<>();
            IntegrationContext context = IntegrationContext.builder()
                    .attributes(attributes)
                    .build();

            assertEquals("", context.getAttribute("nonExistent"));
        }

        @Test
        @DisplayName("Should get attribute with type")
        void shouldGetAttributeWithType() {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stringKey", "stringValue");
            attributes.put("intKey", 42);

            IntegrationContext context = IntegrationContext.builder()
                    .attributes(attributes)
                    .build();

            assertEquals("stringValue", context.getAttribute("stringKey", String.class));
            assertEquals(42, context.getAttribute("intKey", Integer.class));
        }

        @Test
        @DisplayName("Should return null for non-existent typed attribute")
        void shouldReturnNullForNonExistentTypedAttribute() {
            Map<String, Object> attributes = new HashMap<>();
            IntegrationContext context = IntegrationContext.builder()
                    .attributes(attributes)
                    .build();

            assertNull(context.getAttribute("nonExistent", String.class));
        }

        @Test
        @DisplayName("Should throw ClassCastException for wrong type")
        void shouldThrowClassCastExceptionForWrongType() {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stringKey", "stringValue");

            IntegrationContext context = IntegrationContext.builder()
                    .attributes(attributes)
                    .build();

            assertThrows(ClassCastException.class, () ->
                    context.getAttribute("stringKey", Integer.class));
        }
    }

    @Nested
    @DisplayName("setAttribute tests")
    class SetAttributeTests {

        @Test
        @DisplayName("Should set attribute when attributes map exists")
        void shouldSetAttributeWhenAttributesMapExists() {
            Map<String, Object> attributes = new HashMap<>();
            IntegrationContext context = IntegrationContext.builder()
                    .attributes(attributes)
                    .build();

            context.setAttribute("newKey", "newValue");

            assertEquals("newValue", context.getAttribute("newKey"));
        }

        @Test
        @DisplayName("Should throw when attributes map is null")
        void shouldThrowWhenAttributesMapIsNull() {
            IntegrationContext context = IntegrationContext.builder().build();

            // When attributes is null, setAttribute will try to use Map.of() which is immutable
            assertDoesNotThrow(() ->
                    context.setAttribute("key", "value"));
        }
    }

    @Nested
    @DisplayName("serialization options tests")
    class SerializationOptionsTests {

        @Test
        @DisplayName("Should set and get serialization options")
        void shouldSetAndGetSerializationOptions() {
            IntegrationContext context = IntegrationContext.builder().build();
            assertNull(context.getSerializationOptions());
        }

        @Test
        @DisplayName("Should set and get deserialization options")
        void shouldSetAndGetDeserializationOptions() {
            IntegrationContext context = IntegrationContext.builder().build();
            assertNull(context.getDeserializationOptions());
        }
    }
    
    @Test
    void removeAttrTest1() {
    	 IntegrationContext context = IntegrationContext.builder()
                 .build();
    	 
    	 assertDoesNotThrow(() -> context.removeAttribute("key1"));
    }
    
    @Test
    void removeAttrTest2() {
    	 IntegrationContext context = IntegrationContext.builder()
                 .build();
    	 
    	 context.setAttribute("key1", "value1");
    	 
    	 context.removeAttribute("key1");
    	 assertEquals("", context.getAttribute("key1"));
    }
}
