package com.scbank.process.api.fw.session.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.HandlerInterceptor;

import com.scbank.process.api.fw.session.ISessionContextManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * {@link MockSessionInjectInterceptor} 단위 테스트
 */
@DisplayName("MockSessionInjectInterceptor 테스트")
@ExtendWith(MockitoExtension.class)
class MockSessionInjectInterceptorTest {

    private MockSessionInjectInterceptor interceptor;

    @Mock
    private MockSessionProperties properties;

    @Mock
    private ISessionContextManager sessionContextManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    private MockSessionProperties.LoginConfig loginConfig;

    @BeforeEach
    void setUp() {
        interceptor = new MockSessionInjectInterceptor(properties, sessionContextManager);
        loginConfig = new MockSessionProperties.LoginConfig();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("MockSessionProperties와 ISessionContextManager를 주입받아 생성할 수 있다")
        void createWithDependencies() {
            // when
            MockSessionInjectInterceptor newInterceptor =
                    new MockSessionInjectInterceptor(properties, sessionContextManager);

            // then
            assertNotNull(newInterceptor);
        }
    }

    @Nested
    @DisplayName("preHandle 메서드 테스트")
    class PreHandleMethodTests {

        @Test
        @DisplayName("enabled가 false이면 바로 true를 반환한다")
        void returnsTrueWhenDisabled() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(false);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager, never()).login(anyString());
        }

        @Test
        @DisplayName("enabled가 true이고 login이 enabled이면 로그인 처리를 한다")
        void performsLoginWhenLoginEnabled() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(true);
            loginConfig.setUserId("testUser");
            when(properties.getLogin()).thenReturn(loginConfig);
            when(properties.getLoginSession()).thenReturn(null);
            when(properties.getGlobalSession()).thenReturn(null);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager).login("testUser");
        }

        @Test
        @DisplayName("login이 disabled이면 로그인 처리를 하지 않는다")
        void skipsLoginWhenLoginDisabled() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(false);
            when(properties.getLogin()).thenReturn(loginConfig);
            when(properties.getGlobalSession()).thenReturn(null);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager, never()).login(anyString());
        }

        @Test
        @DisplayName("loginSession이 있으면 로그인 세션 값을 설정한다")
        void setsLoginSessionValues() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(true);
            loginConfig.setUserId("testUser");
            when(properties.getLogin()).thenReturn(loginConfig);

            Map<String, Object> loginSession = new HashMap<>();
            loginSession.put("key1", "value1");
            loginSession.put("key2", 123);
            when(properties.getLoginSession()).thenReturn(loginSession);
            when(properties.getGlobalSession()).thenReturn(null);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager).login("testUser");
            verify(sessionContextManager).setLoginValue("key1", "value1");
            verify(sessionContextManager).setLoginValue("key2", 123);
        }

        @Test
        @DisplayName("globalSession이 있으면 글로벌 세션 값을 설정한다")
        void setsGlobalSessionValues() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(false);
            when(properties.getLogin()).thenReturn(loginConfig);

            Map<String, Object> globalSession = new HashMap<>();
            globalSession.put("globalKey", "globalValue");
            when(properties.getGlobalSession()).thenReturn(globalSession);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager).setGlobalValue("globalKey", "globalValue");
        }

        @Test
        @DisplayName("loginSession이 비어있으면 로그인 세션 값을 설정하지 않는다")
        void skipsEmptyLoginSession() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(true);
            loginConfig.setUserId("testUser");
            when(properties.getLogin()).thenReturn(loginConfig);
            when(properties.getLoginSession()).thenReturn(new HashMap<>());
            when(properties.getGlobalSession()).thenReturn(null);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager).login("testUser");
            verify(sessionContextManager, never()).setLoginValue(anyString(), any());
        }

        @Test
        @DisplayName("globalSession이 비어있으면 글로벌 세션 값을 설정하지 않는다")
        void skipsEmptyGlobalSession() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(false);
            when(properties.getLogin()).thenReturn(loginConfig);
            when(properties.getGlobalSession()).thenReturn(new HashMap<>());

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager, never()).setGlobalValue(anyString(), any());
        }

        @Test
        @DisplayName("예외가 발생해도 true를 반환한다")
        void returnsTrueEvenOnException() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(true);
            loginConfig.setUserId("testUser");
            when(properties.getLogin()).thenReturn(loginConfig);
            doThrow(new RuntimeException("Test exception")).when(sessionContextManager).login(anyString());

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("login과 globalSession 모두 처리한다")
        void processesLoginAndGlobalSession() throws Exception {
            // given
            when(properties.isEnabled()).thenReturn(true);
            loginConfig.setEnabled(true);
            loginConfig.setUserId("testUser");
            when(properties.getLogin()).thenReturn(loginConfig);

            Map<String, Object> loginSession = new HashMap<>();
            loginSession.put("loginKey", "loginValue");
            when(properties.getLoginSession()).thenReturn(loginSession);

            Map<String, Object> globalSession = new HashMap<>();
            globalSession.put("globalKey", "globalValue");
            when(properties.getGlobalSession()).thenReturn(globalSession);

            // when
            boolean result = interceptor.preHandle(request, response, handler);

            // then
            assertTrue(result);
            verify(sessionContextManager).login("testUser");
            verify(sessionContextManager).setLoginValue("loginKey", "loginValue");
            verify(sessionContextManager).setGlobalValue("globalKey", "globalValue");
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("HandlerInterceptor 인터페이스를 구현한다")
        void implementsHandlerInterceptor() {
            // then
            assertInstanceOf(HandlerInterceptor.class, interceptor);
        }
    }
}
