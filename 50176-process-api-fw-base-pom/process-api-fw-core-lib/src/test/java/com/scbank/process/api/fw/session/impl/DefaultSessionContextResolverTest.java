package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextAdapter;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * {@link DefaultSessionContextResolver} 단위 테스트
 */
@DisplayName("DefaultSessionContextResolver 테스트")
@ExtendWith(MockitoExtension.class)
class DefaultSessionContextResolverTest {

    private DefaultSessionContextResolver resolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession httpSession;

    @BeforeEach
    void setUp() {
        resolver = new DefaultSessionContextResolver();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("기본 생성자로 생성할 수 있다")
        void createWithDefaultConstructor() {
            // when
            DefaultSessionContextResolver newResolver = new DefaultSessionContextResolver();

            // then
            assertNotNull(newResolver);
        }
    }

    @Nested
    @DisplayName("resolve 메서드 테스트")
    class ResolveMethodTests {

        private Map<String, Object> sessionAttributes;

        @BeforeEach
        void setUpSessionMock() {
            sessionAttributes = new HashMap<>();

            // Simulate HttpSession setAttribute/getAttribute behavior
            doAnswer(invocation -> {
                String name = invocation.getArgument(0);
                Object value = invocation.getArgument(1);
                sessionAttributes.put(name, value);
                return null;
            }).when(httpSession).setAttribute(anyString(), any());

            doAnswer(invocation -> {
                String name = invocation.getArgument(0);
                return sessionAttributes.get(name);
            }).when(httpSession).getAttribute(anyString());
        }

        @Test
        @DisplayName("신규 세션인 경우 새 세션 컨텍스트를 생성한다")
        void createsNewSessionContextForNewSession() {
            // given
            when(request.getSession()).thenReturn(httpSession);
            when(httpSession.getId()).thenReturn("session123");

            // when
            ISessionContext result = resolver.resolve(request);

            // then
            assertNotNull(result);
            verify(httpSession).setAttribute(eq(ISessionContext.SESSION_ATTR_KEY), any(DefaultSessionContext.class));
        }

//        @Test
//        @DisplayName("기존 세션이 있는 경우 기존 세션 컨텍스트를 반환한다")
//        void returnsExistingSessionContext() {
//            // given
//            DefaultSessionContext existingContext = new DefaultSessionContext();
//            existingContext.setSessionId("session123");
//            sessionAttributes.put(ISessionContext.SESSION_ATTR_KEY, existingContext);
//            when(request.getSession()).thenReturn(httpSession);
//
//            // when
//            ISessionContext result = resolver.resolve(request);
//
//            // then
//            assertEquals(existingContext, result);
//        }

        @Test
        @DisplayName("신규 세션 초기화 시 globalSession과 loginSession이 생성된다")
        void initializesGlobalAndLoginSession() {
            // given
            when(request.getSession()).thenReturn(httpSession);
            when(httpSession.getId()).thenReturn("session123");

            // when
            ISessionContext result = resolver.resolve(request);

            // then
            assertNotNull(result);
            assertNotNull(result.getGlobalSession());
            assertNotNull(result.getLoginSession());
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionContextResolver 인터페이스를 구현한다")
        void implementsISessionContextResolver() {
            // then
            assertInstanceOf(ISessionContextResolver.class, resolver);
        }
    }
    
    @Test
    void checkSession_httpSession_null이면_return() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	when(request.getSession()).thenReturn(null);
    	
    	invokeCheckSession(request);
    	
    }
    
    @Test
    void checkSession_adapterBean_null이면_return() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpSession httpSession = mock(HttpSession.class);
    	
    	when(request.getSession()).thenReturn(httpSession);
    	when(httpSession.getAttribute(ISessionContext.SESSION_ATTR_KEY)).thenReturn(new Object());
    	
    	try (MockedStatic<RuntimeContext> mocked = mockStatic(RuntimeContext.class)) {
    		mocked.when(() -> RuntimeContext.getBean(ISessionContextAdapter.class)).thenReturn(null);
    		
    		invokeCheckSession(request);
    	}
    }
    
    private void invokeCheckSession(HttpServletRequest request) throws Exception {
    	Method method = DefaultSessionContextResolver.class.getDeclaredMethod("checkSession", HttpServletRequest.class);
    	method.setAccessible(true);
    	method.invoke(resolver, request);
    }
}
