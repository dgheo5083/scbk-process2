package com.scbank.process.api.fw.channel.context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ServiceContextHolder Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceContextHolderTest {

    @Mock
    private IServiceContext mockContext;

    @AfterEach
    void tearDown() {
        ServiceContextHolder.clear();
    }

    @Nested
    @DisplayName("setContext tests")
    class SetContextTests {

        @Test
        @DisplayName("Should set context in ThreadLocal")
        void shouldSetContextInThreadLocal() {
            ServiceContextHolder.setContext(mockContext);

            assertEquals(mockContext, ServiceContextHolder.getContext());
        }

        @Test
        @DisplayName("Should allow setting null context")
        void shouldAllowSettingNullContext() {
            ServiceContextHolder.setContext(null);

            assertNull(ServiceContextHolder.getContext());
        }
    }

    @Nested
    @DisplayName("getContext tests")
    class GetContextTests {

        @Test
        @DisplayName("Should return null when context is not set")
        void shouldReturnNullWhenContextNotSet() {
            assertNull(ServiceContextHolder.getContext());
        }

        @Test
        @DisplayName("Should return previously set context")
        void shouldReturnPreviouslySetContext() {
            ServiceContextHolder.setContext(mockContext);

            IServiceContext result = ServiceContextHolder.getContext();

            assertSame(mockContext, result);
        }
    }

    @Nested
    @DisplayName("clear tests")
    class ClearTests {

        @Test
        @DisplayName("Should remove context from ThreadLocal")
        void shouldRemoveContextFromThreadLocal() {
            ServiceContextHolder.setContext(mockContext);

            ServiceContextHolder.clear();

            assertNull(ServiceContextHolder.getContext());
        }

        @Test
        @DisplayName("Should not throw when clearing empty context")
        void shouldNotThrowWhenClearingEmptyContext() {
            assertDoesNotThrow(() -> ServiceContextHolder.clear());
        }
    }

    @Nested
    @DisplayName("Thread isolation tests")
    class ThreadIsolationTests {

        @Test
        @DisplayName("Should maintain separate context per thread")
        void shouldMaintainSeparateContextPerThread() throws InterruptedException {
            IServiceContext mainContext = mock(IServiceContext.class);
            IServiceContext otherContext = mock(IServiceContext.class);

            ServiceContextHolder.setContext(mainContext);

            Thread otherThread = new Thread(() -> {
                ServiceContextHolder.setContext(otherContext);
                assertEquals(otherContext, ServiceContextHolder.getContext());
                ServiceContextHolder.clear();
            });

            otherThread.start();
            otherThread.join();

            // Main thread context should remain unchanged
            assertEquals(mainContext, ServiceContextHolder.getContext());
        }
    }
}
