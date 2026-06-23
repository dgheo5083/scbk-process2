package com.scbank.process.api.fw.session;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link SessionScope} 단위 테스트
 */
@DisplayName("SessionScope 테스트")
class SessionScopeTest {

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValueTests {

        @Test
        @DisplayName("GLOBAL 값이 존재한다")
        void globalExists() {
            // when
            SessionScope scope = SessionScope.GLOBAL;

            // then
            assertNotNull(scope);
            assertEquals("GLOBAL", scope.name());
        }

        @Test
        @DisplayName("LOGIN 값이 존재한다")
        void loginExists() {
            // when
            SessionScope scope = SessionScope.LOGIN;

            // then
            assertNotNull(scope);
            assertEquals("LOGIN", scope.name());
        }

        @Test
        @DisplayName("총 2개의 enum 값이 존재한다")
        void hasTwoValues() {
            // when
            SessionScope[] values = SessionScope.values();

            // then
            assertEquals(2, values.length);
        }

        @Test
        @DisplayName("valueOf로 GLOBAL을 조회할 수 있다")
        void valueOfGlobal() {
            // when
            SessionScope scope = SessionScope.valueOf("GLOBAL");

            // then
            assertEquals(SessionScope.GLOBAL, scope);
        }

        @Test
        @DisplayName("valueOf로 LOGIN을 조회할 수 있다")
        void valueOfLogin() {
            // when
            SessionScope scope = SessionScope.valueOf("LOGIN");

            // then
            assertEquals(SessionScope.LOGIN, scope);
        }

        @Test
        @DisplayName("존재하지 않는 값으로 valueOf 호출 시 예외가 발생한다")
        void valueOfInvalidThrowsException() {
            // when & then
            assertThrows(IllegalArgumentException.class, () -> SessionScope.valueOf("INVALID"));
        }
    }

    @Nested
    @DisplayName("ordinal 테스트")
    class OrdinalTests {

        @Test
        @DisplayName("GLOBAL의 ordinal은 0이다")
        void globalOrdinal() {
            // then
            assertEquals(0, SessionScope.GLOBAL.ordinal());
        }

        @Test
        @DisplayName("LOGIN의 ordinal은 1이다")
        void loginOrdinal() {
            // then
            assertEquals(1, SessionScope.LOGIN.ordinal());
        }
    }
}
