package com.scbank.process.api.fw.session.mock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link MockSessionProperties} 단위 테스트
 */
@DisplayName("MockSessionProperties 테스트")
class MockSessionPropertiesTest {

    private MockSessionProperties properties;

    @BeforeEach
    void setUp() {
        properties = new MockSessionProperties();
    }

    @Nested
    @DisplayName("enabled 필드 테스트")
    class EnabledFieldTests {

        @Test
        @DisplayName("기본값은 false이다")
        void defaultValueIsFalse() {
            // then
            assertFalse(properties.isEnabled());
        }

        @Test
        @DisplayName("enabled를 true로 설정할 수 있다")
        void setEnabledToTrue() {
            // when
            properties.setEnabled(true);

            // then
            assertTrue(properties.isEnabled());
        }

        @Test
        @DisplayName("enabled를 false로 설정할 수 있다")
        void setEnabledToFalse() {
            // given
            properties.setEnabled(true);

            // when
            properties.setEnabled(false);

            // then
            assertFalse(properties.isEnabled());
        }
    }

    @Nested
    @DisplayName("login 필드 테스트")
    class LoginFieldTests {

        @Test
        @DisplayName("기본값으로 LoginConfig가 생성된다")
        void defaultLoginConfigIsCreated() {
            // then
            assertNotNull(properties.getLogin());
        }

        @Test
        @DisplayName("login을 설정할 수 있다")
        void setLogin() {
            // given
            MockSessionProperties.LoginConfig loginConfig = new MockSessionProperties.LoginConfig();
            loginConfig.setEnabled(true);
            loginConfig.setUserId("testUser");

            // when
            properties.setLogin(loginConfig);

            // then
            assertEquals(loginConfig, properties.getLogin());
            assertTrue(properties.getLogin().isEnabled());
            assertEquals("testUser", properties.getLogin().getUserId());
        }
    }

    @Nested
    @DisplayName("loginSession 필드 테스트")
    class LoginSessionFieldTests {

        @Test
        @DisplayName("기본값은 null이다")
        void defaultValueIsNull() {
            // then
            assertNull(properties.getLoginSession());
        }

        @Test
        @DisplayName("loginSession을 설정할 수 있다")
        void setLoginSession() {
            // given
            Map<String, Object> loginSession = new HashMap<>();
            loginSession.put("key1", "value1");
            loginSession.put("key2", 123);

            // when
            properties.setLoginSession(loginSession);

            // then
            assertEquals(loginSession, properties.getLoginSession());
            assertEquals("value1", properties.getLoginSession().get("key1"));
            assertEquals(123, properties.getLoginSession().get("key2"));
        }
    }

    @Nested
    @DisplayName("globalSession 필드 테스트")
    class GlobalSessionFieldTests {

        @Test
        @DisplayName("기본값은 null이다")
        void defaultValueIsNull() {
            // then
            assertNull(properties.getGlobalSession());
        }

        @Test
        @DisplayName("globalSession을 설정할 수 있다")
        void setGlobalSession() {
            // given
            Map<String, Object> globalSession = new HashMap<>();
            globalSession.put("globalKey1", "globalValue1");
            globalSession.put("globalKey2", true);

            // when
            properties.setGlobalSession(globalSession);

            // then
            assertEquals(globalSession, properties.getGlobalSession());
            assertEquals("globalValue1", properties.getGlobalSession().get("globalKey1"));
            assertEquals(true, properties.getGlobalSession().get("globalKey2"));
        }
    }

    @Nested
    @DisplayName("LoginConfig 내부 클래스 테스트")
    class LoginConfigTests {

        private MockSessionProperties.LoginConfig loginConfig;

        @BeforeEach
        void setUp() {
            loginConfig = new MockSessionProperties.LoginConfig();
        }

        @Test
        @DisplayName("기본 enabled 값은 false이다")
        void defaultEnabledIsFalse() {
            // then
            assertFalse(loginConfig.isEnabled());
        }

        @Test
        @DisplayName("enabled를 설정할 수 있다")
        void setEnabled() {
            // when
            loginConfig.setEnabled(true);

            // then
            assertTrue(loginConfig.isEnabled());
        }

        @Test
        @DisplayName("기본 userId 값은 null이다")
        void defaultUserIdIsNull() {
            // then
            assertNull(loginConfig.getUserId());
        }

        @Test
        @DisplayName("userId를 설정할 수 있다")
        void setUserId() {
            // when
            loginConfig.setUserId("user123");

            // then
            assertEquals("user123", loginConfig.getUserId());
        }
    }

    @Nested
    @DisplayName("인스턴스 생성 테스트")
    class InstanceCreationTests {

        @Test
        @DisplayName("기본 생성자로 인스턴스를 생성할 수 있다")
        void createInstance() {
            // when
            MockSessionProperties newProperties = new MockSessionProperties();

            // then
            assertNotNull(newProperties);
        }
    }
}
