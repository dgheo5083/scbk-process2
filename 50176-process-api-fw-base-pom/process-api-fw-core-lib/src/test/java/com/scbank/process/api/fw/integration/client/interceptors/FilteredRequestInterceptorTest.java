package com.scbank.process.api.fw.integration.client.interceptors;

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

import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * FilteredRequestInterceptor Test Class
 */
@ExtendWith(MockitoExtension.class)
class FilteredRequestInterceptorTest {

    @Mock
    private FeignFilterChain mockFilterChain;

    @Mock
    private RequestTemplate mockTemplate;

    private FilteredRequestInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new FilteredRequestInterceptor(mockFilterChain);
    }

    @AfterEach
    void tearDown() {
        FeignFilterContextHolder.clear();
    }

    @Nested
    @DisplayName("apply tests")
    class ApplyTests {

        @Test
        @DisplayName("Should call applyOnTemplate with context from holder")
        void shouldCallApplyOnTemplateWithContextFromHolder() {
            FeignFilterContext ctx = new FeignFilterContext();
            ctx.setSystemId("MCI");
            FeignFilterContextHolder.set(ctx);

            interceptor.apply(mockTemplate);

            verify(mockFilterChain).applyOnTemplate(mockTemplate, ctx);
        }

        @Test
        @DisplayName("Should handle null context from holder")
        void shouldHandleNullContextFromHolder() {
            FeignFilterContextHolder.clear();

            assertDoesNotThrow(() -> interceptor.apply(mockTemplate));

            verify(mockFilterChain).applyOnTemplate(eq(mockTemplate), isNull());
        }

        @Test
        @DisplayName("Should pass template to filter chain")
        void shouldPassTemplateToFilterChain() {
            FeignFilterContext ctx = new FeignFilterContext();
            FeignFilterContextHolder.set(ctx);

            interceptor.apply(mockTemplate);

            verify(mockFilterChain).applyOnTemplate(eq(mockTemplate), any(FeignFilterContext.class));
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement RequestInterceptor interface")
        void shouldImplementRequestInterceptorInterface() {
            assertTrue(interceptor instanceof RequestInterceptor);
        }
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with filter chain")
        void shouldCreateWithFilterChain() {
            FilteredRequestInterceptor newInterceptor = new FilteredRequestInterceptor(mockFilterChain);
            assertNotNull(newInterceptor);
        }
    }
}
