package com.scbank.process.api.fw.channel.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * AbstractRequestInterceptor Test Class
 */
@ExtendWith(MockitoExtension.class)
class AbstractRequestInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private TestRequestInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TestRequestInterceptor();
    }

    /**
     * Concrete implementation for testing abstract class
     */
    private static class TestRequestInterceptor extends AbstractRequestInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            return true;
        }
    }

    @Nested
    @DisplayName("isSupported tests")
    class IsSupportedTests {

        @Test
        @DisplayName("Should return true when URI matches URL pattern")
        void shouldReturnTrueWhenUriMatchesPattern() {
            interceptor.setUrlPatterns(Arrays.asList("/api/**", "/service/**"));
            when(request.getRequestURI()).thenReturn("/api/users/list");

            boolean result = interceptor.isSupported(request);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true for exact match")
        void shouldReturnTrueForExactMatch() {
            interceptor.setUrlPatterns(Arrays.asList("/api/users", "/service/login"));
            when(request.getRequestURI()).thenReturn("/api/users");

            boolean result = interceptor.isSupported(request);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when URI does not match any pattern")
        void shouldReturnFalseWhenUriDoesNotMatch() {
            interceptor.setUrlPatterns(Arrays.asList("/api/**", "/service/**"));
            when(request.getRequestURI()).thenReturn("/other/endpoint");

            boolean result = interceptor.isSupported(request);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when URL patterns is null")
        void shouldReturnFalseWhenPatternsNull() {
            interceptor.setUrlPatterns(null);
            when(request.getRequestURI()).thenReturn("/api/users");

            boolean result = interceptor.isSupported(request);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when URL patterns is empty")
        void shouldReturnFalseWhenPatternsEmpty() {
            interceptor.setUrlPatterns(Collections.emptyList());
            when(request.getRequestURI()).thenReturn("/api/users");

            boolean result = interceptor.isSupported(request);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should support wildcard pattern matching")
        void shouldSupportWildcardPattern() {
            interceptor.setUrlPatterns(List.of("/api/*/details"));
            when(request.getRequestURI()).thenReturn("/api/user/details");

            boolean result = interceptor.isSupported(request);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should support double wildcard pattern matching")
        void shouldSupportDoubleWildcardPattern() {
            interceptor.setUrlPatterns(List.of("/api/**/info"));
            when(request.getRequestURI()).thenReturn("/api/v1/users/info");

            boolean result = interceptor.isSupported(request);

            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("index getter/setter tests")
    class IndexTests {

        @Test
        @DisplayName("Should set and get index")
        void shouldSetAndGetIndex() {
            interceptor.setIndex(5);

            assertEquals(5, interceptor.getIndex());
        }

        @Test
        @DisplayName("Should default to zero")
        void shouldDefaultToZero() {
            assertEquals(0, interceptor.getIndex());
        }
    }

    @Nested
    @DisplayName("urlPatterns getter/setter tests")
    class UrlPatternsTests {

        @Test
        @DisplayName("Should set and get URL patterns")
        void shouldSetAndGetUrlPatterns() {
            List<String> patterns = Arrays.asList("/api/**", "/service/**");
            interceptor.setUrlPatterns(patterns);

            assertEquals(patterns, interceptor.getUrlPatterns());
        }

        @Test
        @DisplayName("Should handle null URL patterns")
        void shouldHandleNullUrlPatterns() {
            interceptor.setUrlPatterns(null);

            assertNull(interceptor.getUrlPatterns());
        }
    }

    @Nested
    @DisplayName("Comparable implementation tests")
    class ComparableTests {

        @Test
        @DisplayName("Should compare by index for sorting")
        void shouldCompareByIndex() {
            TestRequestInterceptor interceptor1 = new TestRequestInterceptor();
            interceptor1.setIndex(1);

            TestRequestInterceptor interceptor2 = new TestRequestInterceptor();
            interceptor2.setIndex(2);

            assertTrue(interceptor1.compareTo(interceptor2) < 0);
            assertTrue(interceptor2.compareTo(interceptor1) > 0);
        }

        @Test
        @DisplayName("Should return zero for equal indices")
        void shouldReturnZeroForEqualIndices() {
            TestRequestInterceptor interceptor1 = new TestRequestInterceptor();
            interceptor1.setIndex(5);

            TestRequestInterceptor interceptor2 = new TestRequestInterceptor();
            interceptor2.setIndex(5);

            assertEquals(0, interceptor1.compareTo(interceptor2));
        }
    }
}
