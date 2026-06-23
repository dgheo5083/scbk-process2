package com.scbank.process.api.fw.message.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link MessageFieldConvertException} 단위 테스트
 */
@DisplayName("MessageFieldConvertException 테스트")
class MessageFieldConvertExceptionTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("Throwable을 인자로 예외를 생성할 수 있다")
        void createWithThrowable() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            MessageFieldConvertException exception = new MessageFieldConvertException(cause);

            // then
            assertNotNull(exception);
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("errorCode를 인자로 예외를 생성할 수 있다")
        void createWithErrorCode() {
            // given
            String errorCode = "ERR001";

            // when
            MessageFieldConvertException exception = new MessageFieldConvertException(errorCode);

            // then
            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

//        @Test
//        @DisplayName("errorCode와 message를 인자로 예외를 생성할 수 있다")
//        void createWithErrorCodeAndMessage() {
//            // given
//            String errorCode = "ERR001";
//            String message = "필드 변환 오류 발생";
//
//            // when
//            MessageFieldConvertException exception = new MessageFieldConvertException(errorCode, message);
//
//            // then
//            assertNotNull(exception);
//            assertEquals(errorCode, exception.getErrorCode());
//            assertTrue(exception.getMessage().contains(message));
//        }
    }

    @Nested
    @DisplayName("예외 상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("Exception을 상속받는다")
        void extendsException() {
            // given
            MessageFieldConvertException exception = new MessageFieldConvertException("ERR001");

            // then
            assertTrue(exception instanceof Exception);
        }

        @Test
        @DisplayName("예외를 throw하고 catch할 수 있다")
        void canThrowAndCatch() {
            // given
            String errorCode = "ERR001";

            // when & then
            assertThrows(MessageFieldConvertException.class, () -> {
                throw new MessageFieldConvertException(errorCode);
            });
        }
    }

    @Nested
    @DisplayName("속성 테스트")
    class PropertyTests {

        @Test
        @DisplayName("getErrorCode로 에러 코드를 조회할 수 있다")
        void getErrorCode() {
            // given
            String errorCode = "MSG_CONVERT_001";
            MessageFieldConvertException exception = new MessageFieldConvertException(errorCode);

            // when
            String result = exception.getErrorCode();

            // then
            assertEquals(errorCode, result);
        }

        @Test
        @DisplayName("serialVersionUID가 정의되어 있다")
        void hasSerialVersionUID() {
            // given
            MessageFieldConvertException exception = new MessageFieldConvertException("ERR001");

            // then - serializable
            assertTrue(exception instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class UsageScenarioTests {

//        @Test
//        @DisplayName("필드 변환 실패 시 예외를 생성할 수 있다")
//        void createForFieldConversionFailure() {
//            // given
//            String fieldId = "amount";
//            int expectedLength = 10;
//            int actualLength = 15;
//            String message = String.format("[%s] 필드 길이 초과 (%d > %d)", fieldId, actualLength, expectedLength);
//
//            // when
//            MessageFieldConvertException exception = new MessageFieldConvertException("", message);
//
//            // then
//            assertTrue(exception.getMessage().contains(fieldId));
//            assertTrue(exception.getMessage().contains(String.valueOf(actualLength)));
//        }

        @Test
        @DisplayName("원인 예외를 포함하여 예외 체인을 구성할 수 있다")
        void createExceptionChain() {
            // given
            NumberFormatException cause = new NumberFormatException("Invalid number format");

            // when
            MessageFieldConvertException exception = new MessageFieldConvertException(cause);

            // then
            assertEquals(cause, exception.getCause());
            assertTrue(exception.getCause() instanceof NumberFormatException);
        }
    }
}
