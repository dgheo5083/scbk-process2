package com.scbank.process.api.fw.channel.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * RequestInterceptorComposite Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestInterceptorCompositeTest {

    private RequestInterceptorComposite composite;

    @Mock
    private IRequestInterceptor interceptor1;

    @Mock
    private IRequestInterceptor interceptor2;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private ModelAndView modelAndView;

    private List<IRequestInterceptor> interceptors;

    @BeforeEach
    void setUp() {
        interceptors = new ArrayList<>();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create composite with interceptors list")
        void shouldCreateCompositeWithInterceptorsList() {
            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            assertNotNull(composite);
        }

        @Test
        @DisplayName("Should create composite with empty list")
        void shouldCreateCompositeWithEmptyList() {
            composite = new RequestInterceptorComposite(Collections.emptyList());

            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("preHandle tests")
    class PreHandleTests {

        @Test
        @DisplayName("Should return true when interceptors list is empty")
        void shouldReturnTrueWhenInterceptorsListIsEmpty() throws Exception {
            composite = new RequestInterceptorComposite(Collections.emptyList());

            boolean result = composite.preHandle(request, response, handler);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should call preHandle on supported interceptors")
        void shouldCallPreHandleOnSupportedInterceptors() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.preHandle(request, response, handler)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(0);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            boolean result = composite.preHandle(request, response, handler);

            assertTrue(result);
            verify(interceptor1).preHandle(request, response, handler);
        }

        @Test
        @DisplayName("Should skip non-supported interceptors")
        void shouldSkipNonSupportedInterceptors() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(false);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            boolean result = composite.preHandle(request, response, handler);

            assertTrue(result);
            verify(interceptor1, never()).preHandle(any(), any(), any());
        }

        @Test
        @DisplayName("Should return false when any interceptor returns false")
        void shouldReturnFalseWhenAnyInterceptorReturnsFalse() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.preHandle(request, response, handler)).thenReturn(false);
            when(interceptor1.getIndex()).thenReturn(0);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            boolean result = composite.preHandle(request, response, handler);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should execute interceptors in order by index")
        void shouldExecuteInterceptorsInOrderByIndex() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(1);
            when(interceptor1.preHandle(request, response, handler)).thenReturn(true);

            when(interceptor2.isSupported(request)).thenReturn(true);
            when(interceptor2.getIndex()).thenReturn(0);
            when(interceptor2.preHandle(request, response, handler)).thenReturn(true);

            when(interceptor1.compareTo(interceptor2)).thenReturn(1);
            when(interceptor2.compareTo(interceptor1)).thenReturn(-1);

            interceptors.add(interceptor1);
            interceptors.add(interceptor2);
            composite = new RequestInterceptorComposite(interceptors);

            composite.preHandle(request, response, handler);

            verify(interceptor1).preHandle(request, response, handler);
            verify(interceptor2).preHandle(request, response, handler);
        }
    }

    @Nested
    @DisplayName("postHandle tests")
    class PostHandleTests {

        @Test
        @DisplayName("Should do nothing when interceptors list is empty")
        void shouldDoNothingWhenInterceptorsListIsEmpty() throws Exception {
            composite = new RequestInterceptorComposite(Collections.emptyList());

            assertDoesNotThrow(() -> composite.postHandle(request, response, handler, modelAndView));
        }

        @Test
        @DisplayName("Should call postHandle on supported interceptors")
        void shouldCallPostHandleOnSupportedInterceptors() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(0);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            composite.postHandle(request, response, handler, modelAndView);

            verify(interceptor1).postHandle(request, response, handler, modelAndView);
        }

        @Test
        @DisplayName("Should skip non-supported interceptors in postHandle")
        void shouldSkipNonSupportedInterceptorsInPostHandle() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(false);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            composite.postHandle(request, response, handler, modelAndView);

            verify(interceptor1, never()).postHandle(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should handle null ModelAndView")
        void shouldHandleNullModelAndView() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(0);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            assertDoesNotThrow(() -> composite.postHandle(request, response, handler, null));
        }
    }

    @Nested
    @DisplayName("afterCompletion tests")
    class AfterCompletionTests {

        @Test
        @DisplayName("Should do nothing when interceptors list is empty")
        void shouldDoNothingWhenInterceptorsListIsEmpty() throws Exception {
            composite = new RequestInterceptorComposite(Collections.emptyList());

            assertDoesNotThrow(() -> composite.afterCompletion(request, response, handler, null));
        }

        @Test
        @DisplayName("Should call afterCompletion on supported interceptors")
        void shouldCallAfterCompletionOnSupportedInterceptors() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(0);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            composite.afterCompletion(request, response, handler, null);

            verify(interceptor1).afterCompletion(request, response, handler, null);
        }

        @Test
        @DisplayName("Should pass exception to afterCompletion")
        void shouldPassExceptionToAfterCompletion() throws Exception {
            Exception ex = new RuntimeException("Test exception");
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(0);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            composite.afterCompletion(request, response, handler, ex);

            verify(interceptor1).afterCompletion(request, response, handler, ex);
        }

        @Test
        @DisplayName("Should skip non-supported interceptors in afterCompletion")
        void shouldSkipNonSupportedInterceptorsInAfterCompletion() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(false);

            interceptors.add(interceptor1);
            composite = new RequestInterceptorComposite(interceptors);

            composite.afterCompletion(request, response, handler, null);

            verify(interceptor1, never()).afterCompletion(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Multiple interceptors tests")
    class MultipleInterceptorsTests {

        @Test
        @DisplayName("Should call all supported interceptors")
        void shouldCallAllSupportedInterceptors() throws Exception {
            when(interceptor1.isSupported(request)).thenReturn(true);
            when(interceptor1.getIndex()).thenReturn(0);
            when(interceptor1.preHandle(request, response, handler)).thenReturn(true);

            when(interceptor2.isSupported(request)).thenReturn(true);
            when(interceptor2.getIndex()).thenReturn(1);
            when(interceptor2.preHandle(request, response, handler)).thenReturn(true);

            interceptors.add(interceptor1);
            interceptors.add(interceptor2);
            composite = new RequestInterceptorComposite(interceptors);

            composite.preHandle(request, response, handler);

            verify(interceptor1).preHandle(request, response, handler);
            verify(interceptor2).preHandle(request, response, handler);
        }
    }
}
