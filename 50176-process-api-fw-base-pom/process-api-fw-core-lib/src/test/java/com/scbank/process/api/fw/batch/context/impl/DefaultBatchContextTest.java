package com.scbank.process.api.fw.batch.context.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.batch.context.IBatchContext;

/**
 * DefaultBatchContext Test Class
 */
class DefaultBatchContextTest {

    private DefaultBatchContext context;
    private Map<String, String> initParameters;

    @BeforeEach
    void setUp() {
        initParameters = new HashMap<>();
        initParameters.put("key1", "value1");
        initParameters.put("intKey", "42");
        initParameters.put("boolKey", "true");
        context = new DefaultBatchContext("testJob", "testTrigger", "instance-123", initParameters);
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create context with all parameters")
        void shouldCreateContextWithAllParameters() {
            assertNotNull(context);
            assertEquals("testJob", context.getJobName());
            assertEquals("testTrigger", context.getTriggerName());
            assertEquals("instance-123", context.getInstanceId());
            assertEquals(initParameters, context.getInitParameters());
        }

        @Test
        @DisplayName("Should create context with null parameters")
        void shouldCreateContextWithNullParameters() {
            DefaultBatchContext nullContext = new DefaultBatchContext("job", "trigger", "id", null);
            assertNotNull(nullContext);
            assertNull(nullContext.getInitParameters());
        }

        @Test
        @DisplayName("Should create context with empty parameters")
        void shouldCreateContextWithEmptyParameters() {
            DefaultBatchContext emptyContext = new DefaultBatchContext("job", "trigger", "id", new HashMap<>());
            assertNotNull(emptyContext);
            assertTrue(emptyContext.getInitParameters().isEmpty());
        }
    }

    @Nested
    @DisplayName("getInitParameter tests")
    class GetInitParameterTests {

        @Test
        @DisplayName("Should return parameter value for existing key")
        void shouldReturnParameterValueForExistingKey() {
            assertEquals("value1", context.getInitParameter("key1"));
        }

        @Test
        @DisplayName("Should return empty string for non-existing key")
        void shouldReturnEmptyStringForNonExistingKey() {
            assertEquals("", context.getInitParameter("nonExistingKey"));
        }
    }

    @Nested
    @DisplayName("getString tests")
    class GetStringTests {

        @Test
        @DisplayName("Should return value for existing key")
        void shouldReturnValueForExistingKey() {
            assertEquals("value1", context.getString("key1", "default"));
        }

        @Test
        @DisplayName("Should return default value for non-existing key")
        void shouldReturnDefaultValueForNonExistingKey() {
            assertEquals("defaultValue", context.getString("nonExisting", "defaultValue"));
        }

        @Test
        @DisplayName("Should return default value for null key")
        void shouldReturnDefaultValueForNullKey() {
            assertEquals("default", context.getString(null, "default"));
        }
    }

    @Nested
    @DisplayName("getInt tests")
    class GetIntTests {

        @Test
        @DisplayName("Should return integer value for existing key")
        void shouldReturnIntValueForExistingKey() {
            assertEquals(42, context.getInt("intKey", 0));
        }

        @Test
        @DisplayName("Should return default value for non-existing key")
        void shouldReturnDefaultIntValueForNonExistingKey() {
            assertEquals(100, context.getInt("nonExisting", 100));
        }

        @Test
        @DisplayName("Should throw exception for non-integer value")
        void shouldThrowExceptionForNonIntegerValue() {
            assertThrows(NumberFormatException.class, () -> {
                context.getInt("key1", 0);
            });
        }
    }

    @Nested
    @DisplayName("getBoolean tests")
    class GetBooleanTests {

        @Test
        @DisplayName("Should return boolean value for existing key")
        void shouldReturnBooleanValueForExistingKey() {
            assertTrue(context.getBoolean("boolKey", false));
        }

        @Test
        @DisplayName("Should return default value for non-existing key")
        void shouldReturnDefaultBooleanValueForNonExistingKey() {
            assertFalse(context.getBoolean("nonExisting", false));
        }

        @Test
        @DisplayName("Should parse false value correctly")
        void shouldParseFalseValueCorrectly() {
            initParameters.put("falseBool", "false");
            assertFalse(context.getBoolean("falseBool", true));
        }
    }

    @Nested
    @DisplayName("Error handling tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should set error and mark as failed")
        void shouldSetErrorAndMarkAsFailed() {
            Throwable error = new RuntimeException("Test error");
            context.setError(error);

            assertEquals(error, context.getError());
            assertTrue(context.isFailed());
        }

        @Test
        @DisplayName("Should mark as failed without error")
        void shouldMarkAsFailedWithoutError() {
            assertFalse(context.isFailed());
            context.markAsFailed();
            assertTrue(context.isFailed());
        }

        @Test
        @DisplayName("Should initially not be failed")
        void shouldInitiallyNotBeFailed() {
            assertFalse(context.isFailed());
            assertNull(context.getError());
        }
    }

    @Nested
    @DisplayName("Getter tests")
    class GetterTests {

        @Test
        @DisplayName("Should return correct job name")
        void shouldReturnCorrectJobName() {
            assertEquals("testJob", context.getJobName());
        }

        @Test
        @DisplayName("Should return correct trigger name")
        void shouldReturnCorrectTriggerName() {
            assertEquals("testTrigger", context.getTriggerName());
        }

        @Test
        @DisplayName("Should return correct instance ID")
        void shouldReturnCorrectInstanceId() {
            assertEquals("instance-123", context.getInstanceId());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchContext")
        void shouldImplementIBatchContext() {
            assertTrue(context instanceof IBatchContext);
        }
    }

    @Nested
    @DisplayName("Lombok generated methods tests")
    class LombokTests {

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            context.setJobName("newJobName");
            context.setTriggerName("newTriggerName");
            context.setInstanceId("newInstanceId");

            assertEquals("newJobName", context.getJobName());
            assertEquals("newTriggerName", context.getTriggerName());
            assertEquals("newInstanceId", context.getInstanceId());
        }

        @Test
        @DisplayName("Should set cause directly")
        void shouldSetCauseDirectly() {
            Throwable cause = new IllegalStateException("Direct cause");
            context.setCause(cause);
            assertEquals(cause, context.getCause());
        }

        @Test
        @DisplayName("Should mark as failed via markAsFailed")
        void shouldMarkAsFailedViaMarkAsFailed() {
            assertFalse(context.isFailed());
            context.markAsFailed();
            assertTrue(context.isFailed());
        }

        @Test
        @DisplayName("Should set initParameters")
        void shouldSetInitParameters() {
            Map<String, String> newParams = new HashMap<>();
            newParams.put("newKey", "newValue");
            context.setInitParameters(newParams);
            assertEquals(newParams, context.getInitParameters());
        }

        @Test
        @DisplayName("Should get and set error flag via Lombok methods")
        void shouldGetAndSetErrorFlag() {
            // Test initial state using isFailed which checks the error field
            assertFalse(context.isFailed());
            // markAsFailed sets the error field to true
            context.markAsFailed();
            assertTrue(context.isFailed());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(context, context);
            assertEquals(context.hashCode(), context.hashCode());
        }

        @Test
        @DisplayName("Should be equal to context with same values")
        void shouldBeEqualToContextWithSameValues() {
            DefaultBatchContext other = new DefaultBatchContext("testJob", "testTrigger", "instance-123", initParameters);
            assertEquals(context, other);
            assertEquals(context.hashCode(), other.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to context with different job name")
        void shouldNotBeEqualToContextWithDifferentJobName() {
            DefaultBatchContext other = new DefaultBatchContext("differentJob", "testTrigger", "instance-123", initParameters);
            assertNotEquals(context, other);
        }

        @Test
        @DisplayName("Should not be equal to context with different trigger name")
        void shouldNotBeEqualToContextWithDifferentTriggerName() {
            DefaultBatchContext other = new DefaultBatchContext("testJob", "differentTrigger", "instance-123", initParameters);
            assertNotEquals(context, other);
        }

        @Test
        @DisplayName("Should not be equal to context with different instance ID")
        void shouldNotBeEqualToContextWithDifferentInstanceId() {
            DefaultBatchContext other = new DefaultBatchContext("testJob", "testTrigger", "different-instance", initParameters);
            assertNotEquals(context, other);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, context);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("string", context);
        }

        @Test
        @DisplayName("Should not be equal when initParameters differs")
        void shouldNotBeEqualWhenInitParametersDiffers() {
            Map<String, String> differentParams = new HashMap<>();
            differentParams.put("differentKey", "differentValue");
            DefaultBatchContext other = new DefaultBatchContext("testJob", "testTrigger", "instance-123", differentParams);
            assertNotEquals(context, other);
        }

        @Test
        @DisplayName("Should not be equal when cause differs")
        void shouldNotBeEqualWhenCauseDiffers() {
            DefaultBatchContext other = new DefaultBatchContext("testJob", "testTrigger", "instance-123", initParameters);
            other.setCause(new RuntimeException("test"));
            assertNotEquals(context, other);
        }

        @Test
        @DisplayName("Should be equal when both have same error state")
        void shouldBeEqualWhenBothHaveSameErrorState() {
            DefaultBatchContext other = new DefaultBatchContext("testJob", "testTrigger", "instance-123", initParameters);
            context.markAsFailed();
            other.markAsFailed();
            assertEquals(context, other);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            assertNotNull(context.toString());
        }

        @Test
        @DisplayName("Should include field values in toString")
        void shouldIncludeFieldValuesInToString() {
            String toStringResult = context.toString();
            assertTrue(toStringResult.contains("testJob"));
            assertTrue(toStringResult.contains("testTrigger"));
            assertTrue(toStringResult.contains("instance-123"));
        }

        @Test
        @DisplayName("Should include class name in toString")
        void shouldIncludeClassNameInToString() {
            String toStringResult = context.toString();
            assertTrue(toStringResult.contains("DefaultBatchContext"));
        }
    }

    @Nested
    @DisplayName("Serializable tests")
    class SerializableTests {

        @Test
        @DisplayName("Should implement Serializable through IBatchContext")
        void shouldImplementSerializable() {
            assertTrue(context instanceof java.io.Serializable);
        }
    }

    @Nested
    @DisplayName("canEqual tests")
    class CanEqualTests {

        @Test
        @DisplayName("Should call canEqual method for proper equality check")
        void shouldCallCanEqualMethod() {
            DefaultBatchContext other = new DefaultBatchContext("testJob", "testTrigger", "instance-123", initParameters);
            assertTrue(context.canEqual(other));
        }

        @Test
        @DisplayName("Should return false for canEqual with incompatible type")
        void shouldReturnFalseForCanEqualWithIncompatibleType() {
            assertFalse(context.canEqual("string"));
        }
    }
}
