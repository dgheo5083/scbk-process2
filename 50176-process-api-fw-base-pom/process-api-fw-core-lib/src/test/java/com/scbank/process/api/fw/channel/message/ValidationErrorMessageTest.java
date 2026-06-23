package com.scbank.process.api.fw.channel.message;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;

/**
 * ValidationErrorMessage Test Class
 */
class ValidationErrorMessageTest {

    private ValidationErrorMessage validationErrorMessage;

    @BeforeEach
    void setUp() {
        validationErrorMessage = new ValidationErrorMessage();
    }

    @Nested
    @DisplayName("Getter/Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set errorCnt")
        void shouldGetAndSetErrorCnt() {
            validationErrorMessage.setErrorCnt(3);

            assertEquals(3, validationErrorMessage.getErrorCnt());
        }

        @Test
        @DisplayName("Should get and set errors list")
        void shouldGetAndSetErrorsList() {
            List<ValidationErrorMessage.ValidationError> errors = new ArrayList<>();
            ValidationErrorMessage.ValidationError error1 = new ValidationErrorMessage.ValidationError();
            error1.setField("username");
            error1.setMessage("Username is required");
            errors.add(error1);

            validationErrorMessage.setErrors(errors);

            assertEquals(1, validationErrorMessage.getErrors().size());
            assertEquals("username", validationErrorMessage.getErrors().get(0).getField());
        }
    }

    @Nested
    @DisplayName("ValidationError nested class tests")
    class ValidationErrorNestedClassTests {

        @Test
        @DisplayName("Should create ValidationError with field and message")
        void shouldCreateValidationErrorWithFieldAndMessage() {
            ValidationErrorMessage.ValidationError error = new ValidationErrorMessage.ValidationError();
            error.setField("email");
            error.setMessage("Invalid email format");

            assertEquals("email", error.getField());
            assertEquals("Invalid email format", error.getMessage());
        }

        @Test
        @DisplayName("ValidationError should implement IMessageObject")
        void validationErrorShouldImplementIMessageObject() {
            ValidationErrorMessage.ValidationError error = new ValidationErrorMessage.ValidationError();
            assertTrue(error instanceof IMessageObject);
        }

        @Test
        @DisplayName("Should allow null field")
        void shouldAllowNullField() {
            ValidationErrorMessage.ValidationError error = new ValidationErrorMessage.ValidationError();
            error.setField(null);

            assertNull(error.getField());
        }

        @Test
        @DisplayName("Should allow null message")
        void shouldAllowNullMessage() {
            ValidationErrorMessage.ValidationError error = new ValidationErrorMessage.ValidationError();
            error.setMessage(null);

            assertNull(error.getMessage());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IMessageObject")
        void shouldImplementIMessageObject() {
            assertTrue(validationErrorMessage instanceof IMessageObject);
        }
    }

    @Nested
    @DisplayName("Multiple errors tests")
    class MultipleErrorsTests {

        @Test
        @DisplayName("Should handle multiple validation errors")
        void shouldHandleMultipleValidationErrors() {
            List<ValidationErrorMessage.ValidationError> errors = new ArrayList<>();

            ValidationErrorMessage.ValidationError error1 = new ValidationErrorMessage.ValidationError();
            error1.setField("username");
            error1.setMessage("Username is required");

            ValidationErrorMessage.ValidationError error2 = new ValidationErrorMessage.ValidationError();
            error2.setField("password");
            error2.setMessage("Password must be at least 8 characters");

            ValidationErrorMessage.ValidationError error3 = new ValidationErrorMessage.ValidationError();
            error3.setField("email");
            error3.setMessage("Invalid email format");

            errors.add(error1);
            errors.add(error2);
            errors.add(error3);

            validationErrorMessage.setErrorCnt(3);
            validationErrorMessage.setErrors(errors);

            assertEquals(3, validationErrorMessage.getErrorCnt());
            assertEquals(3, validationErrorMessage.getErrors().size());
        }
    }

    @Nested
    @DisplayName("Empty state tests")
    class EmptyStateTests {

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertEquals(0, validationErrorMessage.getErrorCnt());
            assertNull(validationErrorMessage.getErrors());
        }

        @Test
        @DisplayName("Should allow empty errors list")
        void shouldAllowEmptyErrorsList() {
            validationErrorMessage.setErrors(new ArrayList<>());
            validationErrorMessage.setErrorCnt(0);

            assertEquals(0, validationErrorMessage.getErrorCnt());
            assertTrue(validationErrorMessage.getErrors().isEmpty());
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(validationErrorMessage, validationErrorMessage);
        }

        @Test
        @DisplayName("Should be equal to another message with same values")
        void shouldBeEqualToAnotherMessageWithSameValues() {
            ValidationErrorMessage another = new ValidationErrorMessage();
            another.setErrorCnt(0);

            validationErrorMessage.setErrorCnt(0);

            assertEquals(validationErrorMessage, another);
        }
    }
}
