package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkException;

/**
 * {@link LoginSessionObject} 단위 테스트
 */
@DisplayName("LoginSessionObject 테스트")
class LoginSessionObjectTest {

    private LoginSessionObject loginSession;

    @BeforeEach
    void setUp() {
        loginSession = new LoginSessionObject();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("기본 생성자로 생성할 수 있다")
        void createWithDefaultConstructor() {
            // when
            LoginSessionObject session = new LoginSessionObject();

            // then
            assertNotNull(session);
        }

        @Test
        @DisplayName("생성 시 로그인 상태는 false이다")
        void defaultLoginedIsFalse() {
            // then
            assertFalse(loginSession.isLogined());
        }

        @Test
        @DisplayName("생성 시 loginData는 null이다")
        void defaultLoginDataIsNull() {
            // then
            assertNull(loginSession.getLoginData());
        }
    }

    @Nested
    @DisplayName("initOnLogin 메서드 테스트")
    class InitOnLoginTests {

        @Test
        @DisplayName("로그인 초기화를 수행할 수 있다")
        void canInitOnLogin() throws FrameworkException {
            // when
            loginSession.initOnLogin("testUser");

            // then
            assertTrue(loginSession.isLogined());
            assertEquals("testUser", loginSession.getUserId());
        }

        @Test
        @DisplayName("로그인 시 loginData가 초기화된다")
        void loginDataIsInitialized() throws FrameworkException {
            // when
            loginSession.initOnLogin("testUser");

            // then
            assertNotNull(loginSession.getLoginData());
        }

        @Test
        @DisplayName("빈 userId로 로그인 시 예외가 발생한다")
        void throwsExceptionWhenUserIdIsEmpty() {
            // then
            assertThrows(FrameworkException.class, () -> loginSession.initOnLogin(""));
        }

        @Test
        @DisplayName("null userId로 로그인 시 예외가 발생한다")
        void throwsExceptionWhenUserIdIsNull() {
            // then
            assertThrows(FrameworkException.class, () -> loginSession.initOnLogin(null));
        }

        @Test
        @DisplayName("기존 세션이 있는 상태에서 재로그인하면 초기화된다")
        void clearsExistingDataOnReLogin() throws FrameworkException {
            // given
            loginSession.initOnLogin("user1");
            loginSession.put("key", "value");

            // when
            loginSession.initOnLogin("user2");

            // then
            assertEquals("user2", loginSession.getUserId());
            assertNull(loginSession.get("key"));
        }
    }

    @Nested
    @DisplayName("initOnLogout 메서드 테스트")
    class InitOnLogoutTests {

        @Test
        @DisplayName("로그아웃을 수행할 수 있다")
        void canInitOnLogout() throws FrameworkException {
            // given
            loginSession.initOnLogin("testUser");

            // when
            loginSession.initOnLogout();

            // then
            assertFalse(loginSession.isLogined());
        }

        @Test
        @DisplayName("로그인하지 않은 상태에서 로그아웃 시 예외가 발생한다")
        void throwsExceptionWhenNotLoggedIn() {
            // then
            assertThrows(FrameworkException.class, () -> loginSession.initOnLogout());
        }

        @Test
        @DisplayName("로그아웃 시 세션 데이터가 초기화된다")
        void clearsSessionDataOnLogout() throws FrameworkException {
            // given
            loginSession.initOnLogin("testUser");
            loginSession.put("key", "value");

            // when
            loginSession.initOnLogout();

            // then
            assertNull(loginSession.get("key"));
        }
    }

    @Nested
    @DisplayName("put 메서드 테스트")
    class PutMethodTests {

        @Test
        @DisplayName("값을 저장할 수 있다")
        void canPutValue() throws FrameworkException {
            // given
            loginSession.initOnLogin("testUser");

            // when
            loginSession.put("key", "value");

            // then
            assertEquals("value", loginSession.get("key"));
        }

        @Test
        @DisplayName("loginData가 null이면 자동으로 생성된다")
        void createsLoginDataIfNull() {
            // when
            loginSession.put("key", "value");

            // then
            assertNotNull(loginSession.getLoginData());
        }
    }

    @Nested
    @DisplayName("get 메서드 테스트")
    class GetMethodTests {

        @Test
        @DisplayName("저장된 값을 조회할 수 있다")
        void canGetStoredValue() throws FrameworkException {
            // given
            loginSession.initOnLogin("testUser");
            loginSession.put("key", "value");

            // when
            Object result = loginSession.get("key");

            // then
            assertEquals("value", result);
        }

        @Test
        @DisplayName("loginData가 null이면 null을 반환한다")
        void returnsNullWhenLoginDataIsNull() {
            // when
            Object result = loginSession.get("key");

            // then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("remove 메서드 테스트")
    class RemoveMethodTests {

        @Test
        @DisplayName("저장된 값을 제거할 수 있다")
        void canRemoveStoredValue() throws FrameworkException {
            // given
            loginSession.initOnLogin("testUser");
            loginSession.put("key", "value");

            // when
            loginSession.remove("key");

            // then
            assertNull(loginSession.get("key"));
        }

        @Test
        @DisplayName("loginData가 null이면 null을 반환한다")
        void returnsNullWhenLoginDataIsNull() {
            // when
            Object result = loginSession.remove("key");

            // then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("clear 메서드 테스트")
    class ClearMethodTests {

        @Test
        @DisplayName("세션을 초기화할 수 있다")
        void canClearSession() throws FrameworkException {
            // given
            loginSession.initOnLogin("testUser");
            loginSession.put("key", "value");

            // when
            loginSession.clear();

            // then
            assertNull(loginSession.get("key"));
        }

        @Test
        @DisplayName("loginData가 null이어도 예외가 발생하지 않는다")
        void doesNotThrowWhenLoginDataIsNull() {
            // then
            assertDoesNotThrow(() -> loginSession.clear());
        }
    }

    @Nested
    @DisplayName("logined 속성 테스트")
    class LoginedPropertyTests {

        @Test
        @DisplayName("logined 값을 설정할 수 있다")
        void canSetLogined() {
            // when
            loginSession.setLogined(true);

            // then
            assertTrue(loginSession.isLogined());
        }

        @Test
        @DisplayName("getLoginString은 로그인 상태에서 Login을 반환한다")
        void getLoginStringReturnsLoginWhenLoggedIn() {
            // given
            loginSession.setLogined(true);

            // when
            String result = loginSession.getLoginString();

            // then
            assertEquals("Login", result);
        }

        @Test
        @DisplayName("getLoginString은 비로그인 상태에서 Logout을 반환한다")
        void getLoginStringReturnsLogoutWhenNotLoggedIn() {
            // when
            String result = loginSession.getLoginString();

            // then
            assertEquals("Logout", result);
        }
    }

    @Nested
    @DisplayName("userId 속성 테스트")
    class UserIdPropertyTests {

        @Test
        @DisplayName("userId를 설정하고 조회할 수 있다")
        void canSetAndGetUserId() {
            // when
            loginSession.setUserId("testUser");

            // then
            assertEquals("testUser", loginSession.getUserId());
        }
    }

    @Nested
    @DisplayName("loginData 속성 테스트")
    class LoginDataPropertyTests {

        @Test
        @DisplayName("loginData를 설정하고 조회할 수 있다")
        void canSetAndGetLoginData() {
            // given
            LoginSessionData data = new LoginSessionData();
            data.put("key", "value");

            // when
            loginSession.setLoginData(data);

            // then
            assertEquals(data, loginSession.getLoginData());
        }
    }
}
