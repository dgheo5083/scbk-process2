package com.scbank.process.api.fw.integration.client.filter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

/**
 * FeignFilterContext Test Class
 */
class FeignFilterContextTest {

    private FeignFilterContext context;

    @BeforeEach
    void setUp() {
        context = new FeignFilterContext();
    }

    @Nested
    @DisplayName("systemId property tests")
    class SystemIdTests {

        @Test
        @DisplayName("Should set and get systemId")
        void shouldSetAndGetSystemId() {
            context.setSystemId("MCI");
            assertEquals("MCI", context.getSystemId());
        }

        @Test
        @DisplayName("Should return null when not set")
        void shouldReturnNullWhenNotSet() {
            assertNull(context.getSystemId());
        }
    }

    @Nested
    @DisplayName("interfaceId property tests")
    class InterfaceIdTests {

        @Test
        @DisplayName("Should set and get interfaceId")
        void shouldSetAndGetInterfaceId() {
            context.setInterfaceId("IF001");
            assertEquals("IF001", context.getInterfaceId());
        }
    }

    @Nested
    @DisplayName("responseBytes property tests")
    class ResponseBytesTests {

        @Test
        @DisplayName("Should set and get responseBytes")
        void shouldSetAndGetResponseBytes() {
            byte[] bytes = "test response".getBytes();
            context.setResponseBytese(bytes);
            assertArrayEquals(bytes, context.getResponseBytese());
        }

        @Test
        @DisplayName("Should return null when not set")
        void shouldReturnNullWhenNotSet() {
            assertNull(context.getResponseBytese());
        }
    }

    @Nested
    @DisplayName("responseContentType property tests")
    class ResponseContentTypeTests {

        @Test
        @DisplayName("Should set and get responseContentType")
        void shouldSetAndGetResponseContentType() {
            context.setResponseContentType("application/json");
            assertEquals("application/json", context.getResponseContentType());
        }
    }

    @Nested
    @DisplayName("attributes tests")
    class AttributesTests {

        @Test
        @DisplayName("Should set and get attribute")
        void shouldSetAndGetAttribute() {
            context.setAttr("key1", "value1");
            assertEquals("value1", context.getAttr("key1"));
        }

        @Test
        @DisplayName("Should get typed attribute")
        void shouldGetTypedAttribute() {
            context.setAttr("intKey", 42);
            Integer value = context.getAttr("intKey");
            assertEquals(42, value);
        }

        @Test
        @DisplayName("Should return null for non-existent attribute")
        void shouldReturnNullForNonExistentAttribute() {
            assertNull(context.getAttr("nonExistent"));
        }

        @Test
        @DisplayName("Should return all attributes")
        void shouldReturnAllAttributes() {
            context.setAttr("key1", "value1");
            context.setAttr("key2", "value2");

            Map<String, Object> attrs = context.atttrs();

            assertNotNull(attrs);
            assertEquals(2, attrs.size());
            assertEquals("value1", attrs.get("key1"));
            assertEquals("value2", attrs.get("key2"));
        }

        @Test
        @DisplayName("Should handle different value types")
        void shouldHandleDifferentValueTypes() {
            context.setAttr("string", "value");
            context.setAttr("integer", 123);
            context.setAttr("boolean", true);
            context.setAttr("list", java.util.List.of("a", "b"));

            assertEquals("value", context.getAttr("string"));
            assertEquals(123, (Integer) context.getAttr("integer"));
            assertEquals(true, context.getAttr("boolean"));
        }
    }

    @Nested
    @DisplayName("serialization tests")
    class SerializationTests {

        @Test
        @DisplayName("Should be serializable")
        void shouldBeSerializable() {
            assertTrue(context instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("FeignFilterContextHolder tests")
    class FeignFilterContextHolderTests {

        @AfterEach
        void tearDown() {
            FeignFilterContextHolder.clear();
        }

        @Test
        @DisplayName("Should set and get context")
        void shouldSetAndGetContext() {
            FeignFilterContext ctx = new FeignFilterContext();
            ctx.setSystemId("TEST");

            FeignFilterContextHolder.set(ctx);

            assertNotNull(FeignFilterContextHolder.get());
            assertEquals("TEST", FeignFilterContextHolder.get().getSystemId());
        }

        @Test
        @DisplayName("Should return null when not set")
        void shouldReturnNullWhenNotSet() {
            FeignFilterContextHolder.clear();
            assertNull(FeignFilterContextHolder.get());
        }

        @Test
        @DisplayName("Should clear context")
        void shouldClearContext() {
            FeignFilterContext ctx = new FeignFilterContext();
            FeignFilterContextHolder.set(ctx);
            assertNotNull(FeignFilterContextHolder.get());

            FeignFilterContextHolder.clear();

            assertNull(FeignFilterContextHolder.get());
        }

        @Test
        @DisplayName("Should allow setting null context")
        void shouldAllowSettingNullContext() {
            FeignFilterContextHolder.set(null);
            assertNull(FeignFilterContextHolder.get());
        }

        @Test
        @DisplayName("Should isolate context per thread")
        void shouldIsolateContextPerThread() throws InterruptedException {
            FeignFilterContext mainCtx = new FeignFilterContext();
            mainCtx.setSystemId("MAIN");
            FeignFilterContextHolder.set(mainCtx);

            Thread otherThread = new Thread(() -> {
                assertNull(FeignFilterContextHolder.get());

                FeignFilterContext otherCtx = new FeignFilterContext();
                otherCtx.setSystemId("OTHER");
                FeignFilterContextHolder.set(otherCtx);

                assertEquals("OTHER", FeignFilterContextHolder.get().getSystemId());
                FeignFilterContextHolder.clear();
            });

            otherThread.start();
            otherThread.join();

            assertEquals("MAIN", FeignFilterContextHolder.get().getSystemId());
        }
    }
}
