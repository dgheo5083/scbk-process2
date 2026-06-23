package com.scbank.process.api.fw.batch.metadata.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata.TriggerType;

/**
 * DefaultBatchTriggerMetadata Test Class
 */
class DefaultBatchTriggerMetadataTest {

    private DefaultBatchTriggerMetadata triggerMetadata;

    @BeforeEach
    void setUp() {
        triggerMetadata = new DefaultBatchTriggerMetadata();
    }

    @Nested
    @DisplayName("triggerType property tests")
    class TriggerTypeTests {

        @Test
        @DisplayName("Should set and get SIMPLE triggerType")
        void shouldSetAndGetSimpleTriggerType() {
            triggerMetadata.setTriggerType(TriggerType.SIMPLE);
            assertEquals(TriggerType.SIMPLE, triggerMetadata.getTriggerType());
        }

        @Test
        @DisplayName("Should set and get CRON triggerType")
        void shouldSetAndGetCronTriggerType() {
            triggerMetadata.setTriggerType(TriggerType.CRON);
            assertEquals(TriggerType.CRON, triggerMetadata.getTriggerType());
        }

        @Test
        @DisplayName("Should set and get NONE triggerType")
        void shouldSetAndGetNoneTriggerType() {
            triggerMetadata.setTriggerType(TriggerType.NONE);
            assertEquals(TriggerType.NONE, triggerMetadata.getTriggerType());
        }

        @Test
        @DisplayName("Should handle null triggerType")
        void shouldHandleNullTriggerType() {
            triggerMetadata.setTriggerType(null);
            assertNull(triggerMetadata.getTriggerType());
        }
    }

    @Nested
    @DisplayName("properties tests")
    class PropertiesTests {

        @Test
        @DisplayName("Should set and get properties")
        void shouldSetAndGetProperties() {
            Map<String, String> props = new HashMap<>();
            props.put("cronExpression", "0 0 * * * ?");
            props.put("startDelay", "1000");

            triggerMetadata.setProperties(props);
            assertEquals(props, triggerMetadata.getProperties());
        }

        @Test
        @DisplayName("Should handle null properties")
        void shouldHandleNullProperties() {
            triggerMetadata.setProperties(null);
            assertNull(triggerMetadata.getProperties());
        }

        @Test
        @DisplayName("Should handle empty properties")
        void shouldHandleEmptyProperties() {
            triggerMetadata.setProperties(new HashMap<>());
            assertNotNull(triggerMetadata.getProperties());
            assertTrue(triggerMetadata.getProperties().isEmpty());
        }
    }

    @Nested
    @DisplayName("getProperty default method tests")
    class GetPropertyTests {

        @Test
        @DisplayName("Should return property value for existing key")
        void shouldReturnPropertyValueForExistingKey() {
            Map<String, String> props = new HashMap<>();
            props.put("cronExpression", "0 0 12 * * ?");
            triggerMetadata.setProperties(props);

            assertEquals("0 0 12 * * ?", triggerMetadata.getProperty("cronExpression"));
        }

        @Test
        @DisplayName("Should return empty string for non-existing key")
        void shouldReturnEmptyStringForNonExistingKey() {
            Map<String, String> props = new HashMap<>();
            triggerMetadata.setProperties(props);

            assertEquals("", triggerMetadata.getProperty("nonExisting"));
        }

        @Test
        @DisplayName("Should return empty string when properties is null")
        void shouldReturnEmptyStringWhenPropertiesIsNull() {
            triggerMetadata.setProperties(null);
            assertEquals("", triggerMetadata.getProperty("anyKey"));
        }

        @Test
        @DisplayName("Should return empty string when properties is empty")
        void shouldReturnEmptyStringWhenPropertiesIsEmpty() {
            triggerMetadata.setProperties(new HashMap<>());
            assertEquals("", triggerMetadata.getProperty("anyKey"));
        }
    }

    @Nested
    @DisplayName("TriggerType enum tests")
    class TriggerTypeEnumTests {

        @Test
        @DisplayName("Should convert simple string to SIMPLE type")
        void shouldConvertSimpleStringToSimpleType() {
            assertEquals(TriggerType.SIMPLE, TriggerType.of("simple"));
        }

        @Test
        @DisplayName("Should convert cron string to CRON type")
        void shouldConvertCronStringToCronType() {
            assertEquals(TriggerType.CRON, TriggerType.of("cron"));
        }

        @Test
        @DisplayName("Should convert unknown string to NONE type")
        void shouldConvertUnknownStringToNoneType() {
            assertEquals(TriggerType.NONE, TriggerType.of("unknown"));
        }

        @Test
        @DisplayName("Should convert empty string to NONE type")
        void shouldConvertEmptyStringToNoneType() {
            assertEquals(TriggerType.NONE, TriggerType.of(""));
        }

        @Test
        @DisplayName("Should have three trigger types")
        void shouldHaveThreeTriggerTypes() {
            TriggerType[] types = TriggerType.values();
            assertEquals(3, types.length);
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchTriggerMetadata")
        void shouldImplementIBatchTriggerMetadata() {
            assertTrue(triggerMetadata instanceof IBatchTriggerMetadata);
        }
    }

    @Nested
    @DisplayName("Lombok generated methods tests")
    class LombokTests {

        @Test
        @DisplayName("Should generate equals correctly")
        void shouldGenerateEqualsCorrectly() {
            DefaultBatchTriggerMetadata meta1 = new DefaultBatchTriggerMetadata();
            meta1.setTriggerType(TriggerType.CRON);

            DefaultBatchTriggerMetadata meta2 = new DefaultBatchTriggerMetadata();
            meta2.setTriggerType(TriggerType.CRON);

            assertEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should return false for different triggerType")
        void shouldReturnFalseForDifferentTriggerType() {
            DefaultBatchTriggerMetadata meta1 = new DefaultBatchTriggerMetadata();
            meta1.setTriggerType(TriggerType.CRON);

            DefaultBatchTriggerMetadata meta2 = new DefaultBatchTriggerMetadata();
            meta2.setTriggerType(TriggerType.SIMPLE);

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should return false for null comparison")
        void shouldReturnFalseForNullComparison() {
            triggerMetadata.setTriggerType(TriggerType.CRON);
            assertNotEquals(triggerMetadata, null);
        }

        @Test
        @DisplayName("Should return false for different type comparison")
        void shouldReturnFalseForDifferentTypeComparison() {
            triggerMetadata.setTriggerType(TriggerType.CRON);
            assertNotEquals(triggerMetadata, "string");
        }

        @Test
        @DisplayName("Should return true for same instance")
        void shouldReturnTrueForSameInstance() {
            triggerMetadata.setTriggerType(TriggerType.CRON);
            assertEquals(triggerMetadata, triggerMetadata);
        }

        @Test
        @DisplayName("Should generate hashCode correctly")
        void shouldGenerateHashCodeCorrectly() {
            DefaultBatchTriggerMetadata meta1 = new DefaultBatchTriggerMetadata();
            meta1.setTriggerType(TriggerType.SIMPLE);

            DefaultBatchTriggerMetadata meta2 = new DefaultBatchTriggerMetadata();
            meta2.setTriggerType(TriggerType.SIMPLE);

            assertEquals(meta1.hashCode(), meta2.hashCode());
        }

        @Test
        @DisplayName("Should generate different hashCode for different values")
        void shouldGenerateDifferentHashCodeForDifferentValues() {
            DefaultBatchTriggerMetadata meta1 = new DefaultBatchTriggerMetadata();
            meta1.setTriggerType(TriggerType.CRON);

            DefaultBatchTriggerMetadata meta2 = new DefaultBatchTriggerMetadata();
            meta2.setTriggerType(TriggerType.SIMPLE);

            assertNotEquals(meta1.hashCode(), meta2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            triggerMetadata.setTriggerType(TriggerType.CRON);

            String toString = triggerMetadata.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("CRON"));
        }

        @Test
        @DisplayName("Should include properties in toString")
        void shouldIncludePropertiesInToString() {
            Map<String, String> props = new HashMap<>();
            props.put("key", "value");
            triggerMetadata.setTriggerType(TriggerType.CRON);
            triggerMetadata.setProperties(props);

            String toString = triggerMetadata.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("properties"));
        }

        @Test
        @DisplayName("Should consider properties in equals")
        void shouldConsiderPropertiesInEquals() {
            Map<String, String> props1 = new HashMap<>();
            props1.put("key", "value1");

            Map<String, String> props2 = new HashMap<>();
            props2.put("key", "value2");

            DefaultBatchTriggerMetadata meta1 = new DefaultBatchTriggerMetadata();
            meta1.setTriggerType(TriggerType.CRON);
            meta1.setProperties(props1);

            DefaultBatchTriggerMetadata meta2 = new DefaultBatchTriggerMetadata();
            meta2.setTriggerType(TriggerType.CRON);
            meta2.setProperties(props2);

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Map<String, String> props = new HashMap<>();
            props.put("cronExpression", "0 0 12 * * ?");

            DefaultBatchTriggerMetadata meta1 = new DefaultBatchTriggerMetadata();
            meta1.setTriggerType(TriggerType.CRON);
            meta1.setProperties(props);

            DefaultBatchTriggerMetadata meta2 = new DefaultBatchTriggerMetadata();
            meta2.setTriggerType(TriggerType.CRON);
            meta2.setProperties(new HashMap<>(props));

            assertEquals(meta1, meta2);
            assertEquals(meta1.hashCode(), meta2.hashCode());
        }
    }

    @Nested
    @DisplayName("TriggerType valueOf tests")
    class TriggerTypeValueOfTests {

        @Test
        @DisplayName("Should get SIMPLE from valueOf")
        void shouldGetSimpleFromValueOf() {
            assertEquals(TriggerType.SIMPLE, TriggerType.valueOf("SIMPLE"));
        }

        @Test
        @DisplayName("Should get CRON from valueOf")
        void shouldGetCronFromValueOf() {
            assertEquals(TriggerType.CRON, TriggerType.valueOf("CRON"));
        }

        @Test
        @DisplayName("Should get NONE from valueOf")
        void shouldGetNoneFromValueOf() {
            assertEquals(TriggerType.NONE, TriggerType.valueOf("NONE"));
        }
    }
}
