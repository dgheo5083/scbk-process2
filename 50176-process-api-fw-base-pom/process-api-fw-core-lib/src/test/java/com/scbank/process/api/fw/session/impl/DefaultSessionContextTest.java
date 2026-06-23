package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.session.ISessionContext;

/**
 * {@link DefaultSessionContext} 단위 테스트
 */
@DisplayName("DefaultSessionContext 테스트")
class DefaultSessionContextTest {

    private DefaultSessionContext sessionContext;

    @BeforeEach
    void setUp() {
        sessionContext = new DefaultSessionContext();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("기본 생성자로 생성할 수 있다")
        void createWithDefaultConstructor() {
            // when
            DefaultSessionContext context = new DefaultSessionContext();

            // then
            assertNotNull(context);
        }

        @Test
        @DisplayName("기본 생성자로 생성 시 globalSession이 초기화된다")
        void globalSessionIsInitialized() {
            // then
            assertNotNull(sessionContext.getGlobalSession());
        }

        @Test
        @DisplayName("세션 ID로 생성할 수 있다")
        void createWithSessionId() {
            // when
            DefaultSessionContext context = new DefaultSessionContext("session123");

            // then
            assertEquals("session123", context.getSessionId());
        }

        @Test
        @DisplayName("세션 ID 앞뒤 공백이 제거된다")
        void trimsSessionId() {
            // when
            DefaultSessionContext context = new DefaultSessionContext("  session123  ");

            // then
            assertEquals("session123", context.getSessionId());
        }
    }

    @Nested
    @DisplayName("globalSession 테스트")
    class GlobalSessionTests {

        @Test
        @DisplayName("전역 세션에 값을 저장할 수 있다")
        void canSetGlobalAttribute() {
            // when
            sessionContext.setGlobalAttribute("traceId", "trace123");

            // then
            assertEquals("trace123", sessionContext.getGlobalSession().get("traceId"));
        }

        @Test
        @DisplayName("globalSession이 null이면 자동으로 생성된다")
        void createsGlobalSessionIfNull() {
            // given
            sessionContext.setGlobalSession(null);

            // when
            sessionContext.setGlobalAttribute("key", "value");

            // then
            assertNotNull(sessionContext.getGlobalSession());
        }
    }

    @Nested
    @DisplayName("loginSession 테스트")
    class LoginSessionTests {

        @Test
        @DisplayName("로그인 세션에 값을 저장할 수 있다")
        void canSetLoginAttribute() {
            // given
            sessionContext.setLoginSession(new LoginSessionObject());
            sessionContext.getLoginSession().setLogined(true);

            // when
            sessionContext.setLoginAttribute("userId", "user123");

            // then
            assertEquals("user123", sessionContext.getLoginSession().get("userId"));
        }

        @Test
        @DisplayName("로그인하지 않은 상태에서 setLoginAttribute 호출 시 예외가 발생한다")
        void throwsExceptionWhenNotLoggedIn() {
            // then
            assertThrows(IllegalStateException.class,
                    () -> sessionContext.setLoginAttribute("key", "value"));
        }
    }

    @Nested
    @DisplayName("isLogined 메서드 테스트")
    class IsLoginedTests {

        @Test
        @DisplayName("loginSession이 null이면 false를 반환한다")
        void returnsFalseWhenLoginSessionIsNull() {
            // given
            sessionContext.setLoginSession(null);

            // then
            assertFalse(sessionContext.isLogined());
        }

        @Test
        @DisplayName("loginSession이 로그인 상태면 true를 반환한다")
        void returnsTrueWhenLoggedIn() {
            // given
            LoginSessionObject loginSession = new LoginSessionObject();
            loginSession.setLogined(true);
            sessionContext.setLoginSession(loginSession);

            // then
            assertTrue(sessionContext.isLogined());
        }

        @Test
        @DisplayName("loginSession이 비로그인 상태면 false를 반환한다")
        void returnsFalseWhenNotLoggedIn() {
            // given
            sessionContext.setLoginSession(new LoginSessionObject());

            // then
            assertFalse(sessionContext.isLogined());
        }
    }

    @Nested
    @DisplayName("initOnLogin 메서드 테스트")
    class InitOnLoginTests {

        @Test
        @DisplayName("로그인 초기화를 수행할 수 있다")
        void canInitOnLogin() {
            // given
            sessionContext.setLoginSession(new LoginSessionObject());

            // when
            sessionContext.initOnLogin("testUser");

            // then
            assertTrue(sessionContext.isLogined());
        }

        @Test
        @DisplayName("loginSession이 null이면 아무 동작도 하지 않는다")
        void doesNothingWhenLoginSessionIsNull() {
            // given
            sessionContext.setLoginSession(null);

            // when & then
            assertDoesNotThrow(() -> sessionContext.initOnLogin("testUser"));
        }
    }

    @Nested
    @DisplayName("logout 메서드 테스트")
    class LogoutTests {

        @Test
        @DisplayName("로그아웃을 수행할 수 있다")
        void canLogout() {
            // given
            sessionContext.setLoginSession(new LoginSessionObject());
            sessionContext.initOnLogin("testUser");

            // when
            sessionContext.logout();

            // then
            assertFalse(sessionContext.isLogined());
        }
    }

    @Nested
    @DisplayName("initOnLogout 메서드 테스트")
    class InitOnLogoutTests {

        @Test
        @DisplayName("로그아웃 시 globalSession이 초기화된다")
        void clearsGlobalSession() {
            // given
            sessionContext.setGlobalAttribute("key", "value");
            sessionContext.setLoginSession(new LoginSessionObject());
            sessionContext.initOnLogin("testUser");

            // when
            sessionContext.initOnLogout();

            // then
            assertTrue(sessionContext.getGlobalSession().isEmpty());
        }

        @Test
        @DisplayName("globalSession이 null이어도 예외가 발생하지 않는다")
        void doesNotThrowWhenGlobalSessionIsNull() {
            // given
            sessionContext.setGlobalSession(null);
            sessionContext.setLoginSession(new LoginSessionObject());
            sessionContext.initOnLogin("testUser");

            // when & then
            assertDoesNotThrow(() -> sessionContext.initOnLogout());
        }

        @Test
        @DisplayName("loginSession이 null이어도 예외가 발생하지 않는다")
        void doesNotThrowWhenLoginSessionIsNull() {
            // given
            sessionContext.setLoginSession(null);

            // when & then
            assertDoesNotThrow(() -> sessionContext.initOnLogout());
        }
    }

    @Nested
    @DisplayName("clientIp 속성 테스트")
    class ClientIpTests {

        @Test
        @DisplayName("clientIp를 설정하고 조회할 수 있다")
        void canSetAndGetClientIp() {
            // when
            sessionContext.setClientIp("192.168.1.1");

            // then
            assertEquals("192.168.1.1", sessionContext.getClientIp());
        }
    }

    @Nested
    @DisplayName("sessionId 속성 테스트")
    class SessionIdTests {

        @Test
        @DisplayName("sessionId를 설정하고 조회할 수 있다")
        void canSetAndGetSessionId() {
            // when
            sessionContext.setSessionId("session123");

            // then
            assertEquals("session123", sessionContext.getSessionId());
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionContext 인터페이스를 구현한다")
        void implementsISessionContext() {
            // then
            assertInstanceOf(ISessionContext.class, sessionContext);
        }
    }
}
