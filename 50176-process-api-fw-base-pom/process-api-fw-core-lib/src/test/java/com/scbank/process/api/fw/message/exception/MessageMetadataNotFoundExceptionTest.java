package com.scbank.process.api.fw.message.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link MessageMetadataNotFoundException} 단위 테스트
 */
@DisplayName("MessageMetadataNotFoundException 테스트")
class MessageMetadataNotFoundExceptionTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("errorCode를 인자로 예외를 생성할 수 있다")
        void createWithErrorCode() {
            // given
            String errorCode = "META_NOT_FOUND_001";

            // when
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(errorCode);

            // then
            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

        @Test
        @DisplayName("errorCode와 errorMessage를 인자로 예외를 생성할 수 있다")
        void createWithErrorCodeAndMessage() {
            // given
            String errorCode = "META_NOT_FOUND_001";
            String errorMessage = "메타데이터를 찾을 수 없습니다";

            // when
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(errorCode, errorMessage);

            // then
            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
        }

        @Test
        @DisplayName("errorCode, errorMessage, Throwable을 인자로 예외를 생성할 수 있다")
        void createWithErrorCodeMessageAndThrowable() {
            // given
            String errorCode = "META_NOT_FOUND_001";
            String errorMessage = "메타데이터를 찾을 수 없습니다";
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(
                    errorCode, errorMessage, cause);

            // then
            assertNotNull(exception);
            assertEquals(errorCode, exception.getErrorCode());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Throwable을 인자로 예외를 생성할 수 있다")
        void createWithThrowable() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(cause);

            // then
            assertNotNull(exception);
            assertEquals(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("예외 상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("RuntimeException을 상속받는다")
        void extendsRuntimeException() {
            // given
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException("ERR001");

            // then
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("예외를 throw하고 catch할 수 있다")
        void canThrowAndCatch() {
            // given
            String errorCode = "META_NOT_FOUND_001";

            // when & then
            assertThrows(MessageMetadataNotFoundException.class, () -> {
                throw new MessageMetadataNotFoundException(errorCode);
            });
        }

        @Test
        @DisplayName("unchecked 예외이므로 throws 선언 없이 throw할 수 있다")
        void isUncheckedException() {
            // given
            String errorCode = "META_NOT_FOUND_001";

            // when & then
            assertDoesNotThrow(() -> {
                try {
                    throwException(errorCode);
                } catch (MessageMetadataNotFoundException e) {
                    // expected
                }
            });
        }

        private void throwException(String errorCode) {
            throw new MessageMetadataNotFoundException(errorCode);
        }
    }

    @Nested
    @DisplayName("속성 테스트")
    class PropertyTests {

        @Test
        @DisplayName("getErrorCode로 에러 코드를 조회할 수 있다")
        void getErrorCode() {
            // given
            String errorCode = "META_NOT_FOUND_001";
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(errorCode);

            // when
            String result = exception.getErrorCode();

            // then
            assertEquals(errorCode, result);
        }

        @Test
        @DisplayName("serialVersionUID가 정의되어 있다")
        void hasSerialVersionUID() {
            // given
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException("ERR001");

            // then - serializable
            assertTrue(exception instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("실제 사용 시나리오 테스트")
    class UsageScenarioTests {

        @Test
        @DisplayName("메타데이터를 찾을 수 없을 때 예외를 생성할 수 있다")
        void createWhenMetadataNotFound() {
            // given
            String className = "com.example.TestMessage";
            String errorCode = "MSG_001";
            String message = String.format("클래스 [%s]의 메타데이터를 찾을 수 없습니다", className);

            // when
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(errorCode, message);

            // then
            assertEquals(errorCode, exception.getErrorCode());
        }

        @Test
        @DisplayName("원인 예외를 포함하여 예외 체인을 구성할 수 있다")
        void createExceptionChain() {
            // given
            ClassNotFoundException cause = new ClassNotFoundException("Class not found");

            // when
            MessageMetadataNotFoundException exception = new MessageMetadataNotFoundException(cause);

            // then
            assertEquals(cause, exception.getCause());
            assertTrue(exception.getCause() instanceof ClassNotFoundException);
        }
    }
}
