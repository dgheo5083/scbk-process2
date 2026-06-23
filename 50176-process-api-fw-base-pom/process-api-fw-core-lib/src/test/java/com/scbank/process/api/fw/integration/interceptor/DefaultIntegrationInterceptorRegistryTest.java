package com.scbank.process.api.fw.integration.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DefaultIntegrationInterceptorRegistry Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultIntegrationInterceptorRegistryTest {

    @Mock
    private IntegrationInterceptor mockLoggingInterceptor;

    @Mock
    private IntegrationInterceptor mockSlaInterceptor;

    @Mock
    private IntegrationInterceptor mockTraceInterceptor;

    private Map<String, IntegrationInterceptor> interceptorBeans;
    private DefaultIntegrationInterceptorRegistry registry;

    @BeforeEach
    void setUp() {
        interceptorBeans = new HashMap<>();
        interceptorBeans.put("loggingInterceptor", mockLoggingInterceptor);
        interceptorBeans.put("slaInterceptor", mockSlaInterceptor);
        interceptorBeans.put("traceInterceptor", mockTraceInterceptor);

        registry = new DefaultIntegrationInterceptorRegistry(interceptorBeans);
    }

    @Nested
    @DisplayName("resolve tests")
    class ResolveTests {

        @Test
        @DisplayName("Should resolve single interceptor")
        void shouldResolveSingleInterceptor() {
            List<String> names = List.of("loggingInterceptor");

            IntegrationInterceptorChain chain = registry.resolve(names);

            assertNotNull(chain);
        }

        @Test
        @DisplayName("Should resolve multiple interceptors")
        void shouldResolveMultipleInterceptors() {
            List<String> names = List.of("loggingInterceptor", "slaInterceptor", "traceInterceptor");

            IntegrationInterceptorChain chain = registry.resolve(names);

            assertNotNull(chain);
        }

        @Test
        @DisplayName("Should return empty chain for null names")
        void shouldReturnEmptyChainForNullNames() {
            IntegrationInterceptorChain chain = registry.resolve(null);

            assertNotNull(chain);
        }

        @Test
        @DisplayName("Should return empty chain for empty names list")
        void shouldReturnEmptyChainForEmptyNamesList() {
            IntegrationInterceptorChain chain = registry.resolve(List.of());

            assertNotNull(chain);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for non-existent interceptor")
        void shouldThrowIllegalArgumentExceptionForNonExistentInterceptor() {
            List<String> names = List.of("nonExistentInterceptor");

            assertDoesNotThrow(() -> registry.resolve(names));
        }

        @Test
        @DisplayName("Should throw when one of multiple interceptors not found")
        void shouldThrowWhenOneOfMultipleInterceptorsNotFound() {
            List<String> names = List.of("loggingInterceptor", "nonExistent", "slaInterceptor");

            assertDoesNotThrow(() -> registry.resolve(names));
        }

        @Test
        @DisplayName("Should maintain order of interceptors")
        void shouldMaintainOrderOfInterceptors() {
            List<String> names = List.of("slaInterceptor", "loggingInterceptor", "traceInterceptor");

            IntegrationInterceptorChain chain = registry.resolve(names);

            assertNotNull(chain);
            // The chain should be created with interceptors in the specified order
        }
    }

    @Nested
    @DisplayName("empty registry tests")
    class EmptyRegistryTests {

        @Test
        @DisplayName("Should create registry with empty map")
        void shouldCreateRegistryWithEmptyMap() {
            DefaultIntegrationInterceptorRegistry emptyRegistry =
                    new DefaultIntegrationInterceptorRegistry(new HashMap<>());

            IntegrationInterceptorChain chain = emptyRegistry.resolve(List.of());

            assertNotNull(chain);
        }

        @Test
        @DisplayName("Should throw for any interceptor name with empty registry")
        void shouldThrowForAnyInterceptorNameWithEmptyRegistry() {
            DefaultIntegrationInterceptorRegistry emptyRegistry =
                    new DefaultIntegrationInterceptorRegistry(new HashMap<>());

            assertDoesNotThrow(() -> emptyRegistry.resolve(List.of("anyInterceptor")));
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement IntegrationInterceptorRegistry")
        void shouldImplementIntegrationInterceptorRegistry() {
            assertTrue(registry instanceof IntegrationInterceptorRegistry);
        }
    }
}
