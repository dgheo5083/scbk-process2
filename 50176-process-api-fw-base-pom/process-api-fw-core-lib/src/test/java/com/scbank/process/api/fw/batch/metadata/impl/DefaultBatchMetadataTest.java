package com.scbank.process.api.fw.batch.metadata.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;

/**
 * DefaultBatchMetadata Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultBatchMetadataTest {

    private DefaultBatchMetadata metadata;

    @Mock
    private IBatchJobMetadata jobMetadata;

    @Mock
    private IBatchTriggerMetadata triggerMetadata;

    @BeforeEach
    void setUp() {
        metadata = new DefaultBatchMetadata();
    }

    @Nested
    @DisplayName("id property tests")
    class IdTests {

        @Test
        @DisplayName("Should set and get id")
        void shouldSetAndGetId() {
            metadata.setId("batch-001");
            assertEquals("batch-001", metadata.getId());
        }

        @Test
        @DisplayName("Should handle null id")
        void shouldHandleNullId() {
            metadata.setId(null);
            assertNull(metadata.getId());
        }
    }

    @Nested
    @DisplayName("description property tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            metadata.setDescription("Test batch description");
            assertEquals("Test batch description", metadata.getDescription());
        }

        @Test
        @DisplayName("Should handle empty description")
        void shouldHandleEmptyDescription() {
            metadata.setDescription("");
            assertEquals("", metadata.getDescription());
        }
    }

    @Nested
    @DisplayName("targetNode property tests")
    class TargetNodeTests {

        @Test
        @DisplayName("Should set and get targetNode")
        void shouldSetAndGetTargetNode() {
            metadata.setTargetNode("node-1");
            assertEquals("node-1", metadata.getTargetNode());
        }

        @Test
        @DisplayName("Should handle null targetNode")
        void shouldHandleNullTargetNode() {
            metadata.setTargetNode(null);
            assertNull(metadata.getTargetNode());
        }
    }

    @Nested
    @DisplayName("batchJobMetadata property tests")
    class BatchJobMetadataTests {

        @Test
        @DisplayName("Should set and get batchJobMetadata")
        void shouldSetAndGetBatchJobMetadata() {
            metadata.setBatchJobMetadata(jobMetadata);
            assertEquals(jobMetadata, metadata.getBatchJobMetadata());
        }

        @Test
        @DisplayName("Should handle null batchJobMetadata")
        void shouldHandleNullBatchJobMetadata() {
            metadata.setBatchJobMetadata(null);
            assertNull(metadata.getBatchJobMetadata());
        }
    }

    @Nested
    @DisplayName("batchTriggerMetadata property tests")
    class BatchTriggerMetadataTests {

        @Test
        @DisplayName("Should set and get batchTriggerMetadata")
        void shouldSetAndGetBatchTriggerMetadata() {
            metadata.setBatchTriggerMetadata(triggerMetadata);
            assertEquals(triggerMetadata, metadata.getBatchTriggerMetadata());
        }

        @Test
        @DisplayName("Should handle null batchTriggerMetadata")
        void shouldHandleNullBatchTriggerMetadata() {
            metadata.setBatchTriggerMetadata(null);
            assertNull(metadata.getBatchTriggerMetadata());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchMetadata")
        void shouldImplementIBatchMetadata() {
            assertTrue(metadata instanceof IBatchMetadata);
        }
    }

    @Nested
    @DisplayName("Lombok generated methods tests")
    class LombokTests {

        @Test
        @DisplayName("Should generate equals correctly")
        void shouldGenerateEqualsCorrectly() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id-1");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id-1");

            assertEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should return false for different id")
        void shouldReturnFalseForDifferentId() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id-1");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id-2");

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should return false for null comparison")
        void shouldReturnFalseForNullComparison() {
            metadata.setId("id-1");
            assertNotEquals(metadata, null);
        }

        @Test
        @DisplayName("Should return false for different type comparison")
        void shouldReturnFalseForDifferentTypeComparison() {
            metadata.setId("id-1");
            assertNotEquals(metadata, "string");
        }

        @Test
        @DisplayName("Should return true for same instance")
        void shouldReturnTrueForSameInstance() {
            metadata.setId("id-1");
            assertEquals(metadata, metadata);
        }

        @Test
        @DisplayName("Should generate hashCode correctly")
        void shouldGenerateHashCodeCorrectly() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id-1");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id-1");

            assertEquals(meta1.hashCode(), meta2.hashCode());
        }

        @Test
        @DisplayName("Should generate different hashCode for different values")
        void shouldGenerateDifferentHashCodeForDifferentValues() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id-1");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id-2");

            assertNotEquals(meta1.hashCode(), meta2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            metadata.setId("test-id");
            metadata.setDescription("test-desc");

            String toString = metadata.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("test-id"));
        }

        @Test
        @DisplayName("Should include all fields in toString")
        void shouldIncludeAllFieldsInToString() {
            metadata.setId("id");
            metadata.setDescription("desc");
            metadata.setTargetNode("node");
            metadata.setBatchJobMetadata(jobMetadata);
            metadata.setBatchTriggerMetadata(triggerMetadata);

            String toString = metadata.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("id"));
            assertTrue(toString.contains("description"));
            assertTrue(toString.contains("targetNode"));
        }

        @Test
        @DisplayName("Should consider all fields in equals")
        void shouldConsiderAllFieldsInEquals() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id");
            meta1.setDescription("desc1");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id");
            meta2.setDescription("desc2");

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should consider targetNode in equals")
        void shouldConsiderTargetNodeInEquals() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id");
            meta1.setTargetNode("node1");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id");
            meta2.setTargetNode("node2");

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should consider batchJobMetadata in equals")
        void shouldConsiderBatchJobMetadataInEquals() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id");
            meta1.setBatchJobMetadata(jobMetadata);

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id");
            meta2.setBatchJobMetadata(null);

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should consider batchTriggerMetadata in equals")
        void shouldConsiderBatchTriggerMetadataInEquals() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id");
            meta1.setBatchTriggerMetadata(triggerMetadata);

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id");
            meta2.setBatchTriggerMetadata(null);

            assertNotEquals(meta1, meta2);
        }

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            DefaultBatchMetadata meta1 = new DefaultBatchMetadata();
            meta1.setId("id");
            meta1.setDescription("desc");
            meta1.setTargetNode("node");

            DefaultBatchMetadata meta2 = new DefaultBatchMetadata();
            meta2.setId("id");
            meta2.setDescription("desc");
            meta2.setTargetNode("node");

            assertEquals(meta1, meta2);
            assertEquals(meta1.hashCode(), meta2.hashCode());
        }
    }

    @Nested
    @DisplayName("Full object tests")
    class FullObjectTests {

        @Test
        @DisplayName("Should set all properties")
        void shouldSetAllProperties() {
            metadata.setId("batch-full");
            metadata.setDescription("Full description");
            metadata.setTargetNode("node-full");
            metadata.setBatchJobMetadata(jobMetadata);
            metadata.setBatchTriggerMetadata(triggerMetadata);

            assertEquals("batch-full", metadata.getId());
            assertEquals("Full description", metadata.getDescription());
            assertEquals("node-full", metadata.getTargetNode());
            assertEquals(jobMetadata, metadata.getBatchJobMetadata());
            assertEquals(triggerMetadata, metadata.getBatchTriggerMetadata());
        }
    }
}
