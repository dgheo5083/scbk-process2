package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.session.ISessionObject;
import com.scbank.process.api.fw.session.SessionScope;

/**
 * {@link LoginSessionData} 단위 테스트
 */
@DisplayName("LoginSessionData 테스트")
class LoginSessionDataTest {

    private LoginSessionData loginSessionData;

    @BeforeEach
    void setUp() {
        loginSessionData = new LoginSessionData();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("기본 생성자로 생성할 수 있다")
        void createWithDefaultConstructor() {
            // when
            LoginSessionData data = new LoginSessionData();

            // then
            assertNotNull(data);
        }

        @Test
        @DisplayName("LOGIN 세션 범위로 초기화된다")
        void initializesWithLoginScope() {
            // then
            assertEquals(SessionScope.LOGIN, loginSessionData.sessionScope);
        }

        @Test
        @DisplayName("userId를 포함하여 생성할 수 있다")
        void createWithUserId() {
            // when
            LoginSessionData data = new LoginSessionData("user123");

            // then
            assertEquals("user123", data.getUserId());
        }

        @Test
        @DisplayName("초기 데이터 Map을 포함하여 생성할 수 있다")
        void createWithInitialData() {
            // given
            Map<String, String> initialData = new HashMap<>();
            initialData.put("key1", "value1");
            initialData.put("key2", "value2");

            // when
            LoginSessionData data = new LoginSessionData(initialData);

            // then
            assertEquals("value1", data.get("key1"));
            assertEquals("value2", data.get("key2"));
        }

        @Test
        @DisplayName("빈 초기 데이터로 생성할 수 있다")
        void createWithEmptyInitialData() {
            // when
            LoginSessionData data = new LoginSessionData(new HashMap<>());

            // then
            assertTrue(data.isEmpty());
        }

        @Test
        @DisplayName("null 초기 데이터로 생성할 수 있다")
        void createWithNullInitialData() {
            // when
            LoginSessionData data = new LoginSessionData((Map<String, ? extends java.io.Serializable>) null);

            // then
            assertTrue(data.isEmpty());
        }
    }

    @Nested
    @DisplayName("userId 관련 테스트")
    class UserIdTests {

        @Test
        @DisplayName("userId를 설정할 수 있다")
        void canSetUserId() {
            // when
            loginSessionData.setUserId("testUser");

            // then
            assertEquals("testUser", loginSessionData.getUserId());
        }

        @Test
        @DisplayName("userId를 조회할 수 있다")
        void canGetUserId() {
            // given
            loginSessionData.put("userId", "testUser");

            // when
            String userId = loginSessionData.getUserId();

            // then
            assertEquals("testUser", userId);
        }

        @Test
        @DisplayName("userId가 설정되지 않으면 null을 반환한다")
        void returnsNullWhenUserIdNotSet() {
            // when
            String userId = loginSessionData.getUserId();

            // then
            assertNull(userId);
        }
    }

    @Nested
    @DisplayName("put 메서드 테스트")
    class PutMethodTests {

        @Test
        @DisplayName("키-값을 저장할 수 있다")
        void canPutKeyValue() {
            // when
            loginSessionData.put("userRole", "ADMIN");

            // then
            assertEquals("ADMIN", loginSessionData.get("userRole"));
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("AbstractSessionObject를 상속한다")
        void extendsAbstractSessionObject() {
            // then
            assertInstanceOf(AbstractSessionObject.class, loginSessionData);
        }

        @Test
        @DisplayName("ISessionObject 인터페이스를 구현한다")
        void implementsISessionObject() {
            // then
            assertInstanceOf(ISessionObject.class, loginSessionData);
        }
    }
}
