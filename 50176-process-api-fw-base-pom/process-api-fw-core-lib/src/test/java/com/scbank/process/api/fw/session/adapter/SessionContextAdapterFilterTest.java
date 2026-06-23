package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * {@link SessionContextAdapterFilter} 단위 테스트
 */
@DisplayName("SessionContextAdapterFilter 테스트")
@ExtendWith(MockitoExtension.class)
class SessionContextAdapterFilterTest {

    private SessionContextAdapterFilter filter;

    @Mock
    private ISessionAdapterProvider sessionAdapterProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpSession httpSession;

    @BeforeEach
    void setUp() {
        filter = new SessionContextAdapterFilter(sessionAdapterProvider);
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("ISessionAdapterProvider를 주입받아 생성할 수 있다")
        void createWithSessionAdapterProvider() {
            // when
            SessionContextAdapterFilter newFilter = new SessionContextAdapterFilter(sessionAdapterProvider);

            // then
            assertNotNull(newFilter);
        }
    }

    @Nested
    @DisplayName("doFilterInternal 메서드 테스트")
    class DoFilterInternalTests {

        @Test
        @DisplayName("필터 체인을 정상적으로 호출한다")
        void callsFilterChain() throws ServletException, IOException {
            // when
            filter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(any(HttpServletRequest.class), eq(response));
        }
    }

    @Nested
    @DisplayName("SessionContextAdapterRequestWrapper 테스트")
    class RequestWrapperTests {

        @Test
        @DisplayName("getSession 호출 시 HttpSessionWrapper를 반환한다")
        void getSessionReturnsHttpSessionWrapper() throws ServletException, IOException {
            // given
            when(request.getSession(anyBoolean())).thenReturn(httpSession);

            doAnswer(invocation -> {
                HttpServletRequest wrappedRequest = invocation.getArgument(0);
                HttpSession session = wrappedRequest.getSession(true);
                assertNotNull(session);
                return null;
            }).when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));

            // when
            filter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(any(HttpServletRequest.class), eq(response));
        }

        @Test
        @DisplayName("getSession(false) 호출 시 null 세션이면 null을 반환한다")
        void getSessionReturnsNullWhenNoSession() throws ServletException, IOException {
            // given
            when(request.getSession(false)).thenReturn(null);

            doAnswer(invocation -> {
                HttpServletRequest wrappedRequest = invocation.getArgument(0);
                HttpSession session = wrappedRequest.getSession(false);
                assertNull(session);
                return null;
            }).when(filterChain).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));

            // when
            filter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(any(HttpServletRequest.class), eq(response));
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("OncePerRequestFilter를 상속한다")
        void extendsOncePerRequestFilter() {
            // then
            assertInstanceOf(OncePerRequestFilter.class, filter);
        }
    }
}
