package com.scbank.process.api.fw.integration.client.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.integration.client.filter.FeignFilter;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.interceptors.FilteredRequestInterceptor;
import com.scbank.process.api.fw.integration.client.invocation.FeignClientInvocationHandlerFactory;

import feign.InvocationHandlerFactory;
import feign.RequestInterceptor;

/**
 * BaseFeignClientConfig Test Class
 */
@ExtendWith(MockitoExtension.class)
class BaseFeignClientConfigTest {

    @Mock
    private FeignFilter mockFilter1;

    @Mock
    private FeignFilter mockFilter2;

    @Mock
    private FeignFilterChain mockFilterChain;

    @Mock
    private InvocationHandlerFactory mockInvocationHandlerFactory;

    /**
     * Concrete test implementation of BaseFeignClientConfig
     */
    private static class TestFeignClientConfig extends BaseFeignClientConfig {
    }

    private TestFeignClientConfig config;

    @BeforeEach
    void setUp() {
        config = new TestFeignClientConfig();
    }

    @Nested
    @DisplayName("feignFilterChain tests")
    class FeignFilterChainTests {

        @Test
        @DisplayName("Should create FeignFilterChain with filters")
        void shouldCreateFeignFilterChainWithFilters() {
            List<FeignFilter> filters = List.of(mockFilter1, mockFilter2);

            FeignFilterChain filterChain = config.feignFilterChain(filters);

            assertNotNull(filterChain);
            assertTrue(filterChain instanceof FeignFilterChain);
        }

        @Test
        @DisplayName("Should create FeignFilterChain with empty list")
        void shouldCreateFeignFilterChainWithEmptyList() {
            List<FeignFilter> filters = new ArrayList<>();

            FeignFilterChain filterChain = config.feignFilterChain(filters);

            assertNotNull(filterChain);
        }
    }

    @Nested
    @DisplayName("filteredRequestInterceptor tests")
    class FilteredRequestInterceptorTests {

        @Test
        @DisplayName("Should create FilteredRequestInterceptor with filter chain")
        void shouldCreateFilteredRequestInterceptorWithFilterChain() {
            RequestInterceptor interceptor = config.filteredRequestInterceptor(mockFilterChain);

            assertNotNull(interceptor);
            assertTrue(interceptor instanceof FilteredRequestInterceptor);
        }
    }

    @Nested
    @DisplayName("invocationHandlerFactory tests")
    class InvocationHandlerFactoryTests {

        @Test
        @DisplayName("Should create FeignClientInvocationHandlerFactory")
        void shouldCreateFeignClientInvocationHandlerFactory() {
            InvocationHandlerFactory factory = config.invocationHandlerFactory();

            assertNotNull(factory);
            assertTrue(factory instanceof FeignClientInvocationHandlerFactory);
        }
    }

//    @Nested
//    @DisplayName("feignBuilder tests")
//    class FeignBuilderTests {
//
//        @Test
//        @DisplayName("Should create Feign.Builder with invocation handler factory")
//        void shouldCreateFeignBuilderWithInvocationHandlerFactory() {
//            Feign.Builder builder = config.feignBuilder(mockInvocationHandlerFactory);
//
//            assertNotNull(builder);
//        }
//
//        @Test
//        @DisplayName("Should create functional Feign.Builder")
//        void shouldCreateFunctionalFeignBuilder() {
//            InvocationHandlerFactory factory = config.invocationHandlerFactory();
//            Feign.Builder builder = config.feignBuilder(factory);
//
//            assertNotNull(builder);
//        }
//    }

    @Nested
    @DisplayName("abstract class tests")
    class AbstractClassTests {

        @Test
        @DisplayName("Should be extendable as abstract class")
        void shouldBeExtendableAsAbstractClass() {
            assertTrue(config instanceof BaseFeignClientConfig);
        }
    }
}
