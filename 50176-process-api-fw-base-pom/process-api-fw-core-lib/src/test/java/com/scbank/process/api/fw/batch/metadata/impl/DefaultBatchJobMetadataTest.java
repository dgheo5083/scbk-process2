package com.scbank.process.api.fw.batch.metadata.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;

/**
 * DefaultBatchJobMetadata Test Class
 */
class DefaultBatchJobMetadataTest {

    private DefaultBatchJobMetadata jobMetadata;

    @BeforeEach
    void setUp() {
        jobMetadata = new DefaultBatchJobMetadata();
    }

    @Nested
    @DisplayName("componentId property tests")
    class ComponentIdTests {

        @Test
        @DisplayName("Should set and get componentId")
        void shouldSetAndGetComponentId() {
            jobMetadata.setComponentId("batch-component-001");
            assertEquals("batch-component-001", jobMetadata.getComponentId());
        }

        @Test
        @DisplayName("Should handle null componentId")
        void shouldHandleNullComponentId() {
            jobMetadata.setComponentId(null);
            assertNull(jobMetadata.getComponentId());
        }

        @Test
        @DisplayName("Should handle empty componentId")
        void shouldHandleEmptyComponentId() {
            jobMetadata.setComponentId("");
            assertEquals("", jobMetadata.getComponentId());
        }
    }

    @Nested
    @DisplayName("initParameters property tests")
    class InitParametersTests {

        @Test
        @DisplayName("Should set and get initParameters")
        void shouldSetAndGetInitParameters() {
            Map<String, String> params = new HashMap<>();
            params.put("key1", "value1");
            params.put("key2", "value2");

            jobMetadata.setInitParameters(params);
            assertEquals(params, jobMetadata.getInitParameters());
        }

        @Test
        @DisplayName("Should handle null initParameters")
        void shouldHandleNullInitParameters() {
            jobMetadata.setInitParameters(null);
            assertNull(jobMetadata.getInitParameters());
        }

        @Test
        @DisplayName("Should handle empty initParameters")
        void shouldHandleEmptyInitParameters() {
            jobMetadata.setInitParameters(new HashMap<>());
            assertNotNull(jobMetadata.getInitParameters());
            assertTrue(jobMetadata.getInitParameters().isEmpty());
        }

        @Test
        @DisplayName("Should access parameter values from map")
        void shouldAccessParameterValuesFromMap() {
            Map<String, String> params = new HashMap<>();
            params.put("param1", "paramValue1");

            jobMetadata.setInitParameters(params);
            assertEquals("paramValue1", jobMetadata.getInitParameters().get("param1"));
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchJobMetadata")
        void shouldImplementIBatchJobMetadata() {
            assertTrue(jobMetadata instanceof IBatchJobMetadata);
        }
    }

    @Nested
    @DisplayName("Lombok generated methods tests")
    class LombokTests {

        @Test
        @DisplayName("Should generate equals correctly")
        void shouldGenerateEqualsCorrectly() {
            DefaultBatchJobMetadata meta1 = new DefaultBatchJobMetadata();
            meta1.setComponentId("comp-1");

            DefaultBatchJobMetadata meta2 = new DefaultBatchJobMetadata();
            meta2.setComponentId("comp-1");

            assertEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should return false for different componentId")
        void shouldReturnFalseForDifferentComponentId() {
            DefaultBatchJobMetadata meta1 = new DefaultBatchJobMetadata();
            meta1.setComponentId("comp-1");

            DefaultBatchJobMetadata meta2 = new DefaultBatchJobMetadata();
            meta2.setComponentId("comp-2");

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should return false for null comparison")
        void shouldReturnFalseForNullComparison() {
            jobMetadata.setComponentId("comp-1");
            assertNotEquals(jobMetadata, null);
        }

        @Test
        @DisplayName("Should return false for different type comparison")
        void shouldReturnFalseForDifferentTypeComparison() {
            jobMetadata.setComponentId("comp-1");
            assertNotEquals(jobMetadata, "string");
        }

        @Test
        @DisplayName("Should return true for same instance")
        void shouldReturnTrueForSameInstance() {
            jobMetadata.setComponentId("comp-1");
            assertEquals(jobMetadata, jobMetadata);
        }

        @Test
        @DisplayName("Should generate hashCode correctly")
        void shouldGenerateHashCodeCorrectly() {
            DefaultBatchJobMetadata meta1 = new DefaultBatchJobMetadata();
            meta1.setComponentId("comp-1");

            DefaultBatchJobMetadata meta2 = new DefaultBatchJobMetadata();
            meta2.setComponentId("comp-1");

            assertEquals(meta1.hashCode(), meta2.hashCode());
        }

        @Test
        @DisplayName("Should generate different hashCode for different values")
        void shouldGenerateDifferentHashCodeForDifferentValues() {
            DefaultBatchJobMetadata meta1 = new DefaultBatchJobMetadata();
            meta1.setComponentId("comp-1");

            DefaultBatchJobMetadata meta2 = new DefaultBatchJobMetadata();
            meta2.setComponentId("comp-2");

            assertNotEquals(meta1.hashCode(), meta2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            jobMetadata.setComponentId("test-component");

            String toString = jobMetadata.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("test-component"));
        }

        @Test
        @DisplayName("Should include initParameters in toString")
        void shouldIncludeInitParametersInToString() {
            Map<String, String> params = new HashMap<>();
            params.put("key", "value");
            jobMetadata.setComponentId("comp");
            jobMetadata.setInitParameters(params);

            String toString = jobMetadata.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("initParameters"));
        }

        @Test
        @DisplayName("Should consider initParameters in equals")
        void shouldConsiderInitParametersInEquals() {
            Map<String, String> params1 = new HashMap<>();
            params1.put("key", "value1");

            Map<String, String> params2 = new HashMap<>();
            params2.put("key", "value2");

            DefaultBatchJobMetadata meta1 = new DefaultBatchJobMetadata();
            meta1.setComponentId("comp");
            meta1.setInitParameters(params1);

            DefaultBatchJobMetadata meta2 = new DefaultBatchJobMetadata();
            meta2.setComponentId("comp");
            meta2.setInitParameters(params2);

            assertNotEquals(meta1, meta2);
        }
    }

    @Nested
    @DisplayName("Full object tests")
    class FullObjectTests {

        @Test
        @DisplayName("Should set all properties")
        void shouldSetAllProperties() {
            Map<String, String> params = new HashMap<>();
            params.put("input", "/data/input");
            params.put("output", "/data/output");

            jobMetadata.setComponentId("data-processor");
            jobMetadata.setInitParameters(params);

            assertEquals("data-processor", jobMetadata.getComponentId());
            assertEquals(2, jobMetadata.getInitParameters().size());
            assertEquals("/data/input", jobMetadata.getInitParameters().get("input"));
        }
    }
}
