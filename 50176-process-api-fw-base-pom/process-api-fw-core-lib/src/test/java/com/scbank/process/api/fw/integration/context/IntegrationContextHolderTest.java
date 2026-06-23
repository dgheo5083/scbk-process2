package com.scbank.process.api.fw.integration.context;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * IntegrationContextHolder Test Class
 */
class IntegrationContextHolderTest {

    @AfterEach
    void tearDown() {
        IntegrationContextHolder.clear();
    }

    @Nested
    @DisplayName("set tests")
    class SetTests {

        @Test
        @DisplayName("Should set IntegrationContext")
        void shouldSetIntegrationContext() {
            IntegrationContext context = IntegrationContext.builder()
                    .systemId("MCI")
                    .interfaceId("IF001")
                    .build();

            IntegrationContextHolder.set(context);

            assertNotNull(IntegrationContextHolder.get());
            assertEquals("MCI", IntegrationContextHolder.get().getSystemId());
        }

        @Test
        @DisplayName("Should allow setting null context")
        void shouldAllowSettingNullContext() {
            IntegrationContextHolder.set(null);
            assertNull(IntegrationContextHolder.get());
        }

        @Test
        @DisplayName("Should replace existing context")
        void shouldReplaceExistingContext() {
            IntegrationContext context1 = IntegrationContext.builder()
                    .systemId("MCI")
                    .build();
            IntegrationContext context2 = IntegrationContext.builder()
                    .systemId("FEP")
                    .build();

            IntegrationContextHolder.set(context1);
            assertEquals("MCI", IntegrationContextHolder.get().getSystemId());

            IntegrationContextHolder.set(context2);
            assertEquals("FEP", IntegrationContextHolder.get().getSystemId());
        }
    }

    @Nested
    @DisplayName("get tests")
    class GetTests {

        @Test
        @DisplayName("Should return null when not set")
        void shouldReturnNullWhenNotSet() {
            IntegrationContextHolder.clear();
            assertNull(IntegrationContextHolder.get());
        }

        @Test
        @DisplayName("Should return same instance")
        void shouldReturnSameInstance() {
            IntegrationContext context = IntegrationContext.builder()
                    .systemId("HOST")
                    .build();

            IntegrationContextHolder.set(context);

            assertSame(context, IntegrationContextHolder.get());
        }
    }

    @Nested
    @DisplayName("clear tests")
    class ClearTests {

        @Test
        @DisplayName("Should clear context")
        void shouldClearContext() {
            IntegrationContext context = IntegrationContext.builder()
                    .systemId("MCI")
                    .build();

            IntegrationContextHolder.set(context);
            assertNotNull(IntegrationContextHolder.get());

            IntegrationContextHolder.clear();
            assertNull(IntegrationContextHolder.get());
        }

        @Test
        @DisplayName("Should not throw when clearing empty context")
        void shouldNotThrowWhenClearingEmptyContext() {
            assertDoesNotThrow(() -> IntegrationContextHolder.clear());
        }

        @Test
        @DisplayName("Should allow multiple clears")
        void shouldAllowMultipleClears() {
            IntegrationContextHolder.set(IntegrationContext.builder().build());

            assertDoesNotThrow(() -> {
                IntegrationContextHolder.clear();
                IntegrationContextHolder.clear();
                IntegrationContextHolder.clear();
            });
        }
    }

    @Nested
    @DisplayName("thread isolation tests")
    class ThreadIsolationTests {

        @Test
        @DisplayName("Should isolate context per thread")
        void shouldIsolateContextPerThread() throws InterruptedException {
            IntegrationContext mainContext = IntegrationContext.builder()
                    .systemId("MAIN")
                    .build();
            IntegrationContextHolder.set(mainContext);

            Thread otherThread = new Thread(() -> {
                assertNull(IntegrationContextHolder.get());

                IntegrationContext otherContext = IntegrationContext.builder()
                        .systemId("OTHER")
                        .build();
                IntegrationContextHolder.set(otherContext);

                assertEquals("OTHER", IntegrationContextHolder.get().getSystemId());
                IntegrationContextHolder.clear();
            });

            otherThread.start();
            otherThread.join();

            assertEquals("MAIN", IntegrationContextHolder.get().getSystemId());
        }
    }
}
