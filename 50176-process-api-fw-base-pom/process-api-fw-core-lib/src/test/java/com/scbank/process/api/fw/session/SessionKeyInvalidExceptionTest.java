package com.scbank.process.api.fw.session;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * {@link SessionKeyInvalidException} 단위 테스트
 */
@DisplayName("SessionKeyInvalidException 테스트")
class SessionKeyInvalidExceptionTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("에러 메시지를 포함하여 생성할 수 있다")
        void createWithErrorMessage() {
            // given
            String errorMessage = "등록 되지 않는 세션키[LOGIN][testKey]입니다.";

            // when
            SessionKeyInvalidException exception = new SessionKeyInvalidException(errorMessage);

            // then
            assertNotNull(exception);
            assertEquals("[MACF1003]" + errorMessage, exception.getMessage());
        }

        @Test
        @DisplayName("에러 코드가 SESSION_KEY_INVALID로 설정된다")
        void errorCodeIsSessionKeyInvalid() {
            // given
            String errorMessage = "테스트 오류";

            // when
            SessionKeyInvalidException exception = new SessionKeyInvalidException(errorMessage);

            // then
            assertEquals(FrameworkErrorCode.SESSION_KEY_INVALID.getCode(), exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("FrameworkRuntimeException을 상속한다")
        void extendsFrameworkRuntimeException() {
            // given
            SessionKeyInvalidException exception = new SessionKeyInvalidException("test");

            // then
            assertInstanceOf(FrameworkRuntimeException.class, exception);
        }

        @Test
        @DisplayName("RuntimeException을 상속한다")
        void extendsRuntimeException() {
            // given
            SessionKeyInvalidException exception = new SessionKeyInvalidException("test");

            // then
            assertInstanceOf(RuntimeException.class, exception);
        }
    }

    @Nested
    @DisplayName("예외 던지기 테스트")
    class ThrowTests {

        @Test
        @DisplayName("예외를 던지고 잡을 수 있다")
        void throwAndCatch() {
            // given
            String errorMessage = "세션 키 오류";

            // then
            SessionKeyInvalidException thrown = assertThrows(
                    SessionKeyInvalidException.class,
                    () -> {
                        throw new SessionKeyInvalidException(errorMessage);
                    });
            assertEquals("[MACF1003]" + errorMessage, thrown.getMessage());
        }
    }
}
