package com.scbank.process.api.fw.batch.metadata.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.batch.BatchProperties;
import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata.TriggerType;

/**
 * DefaultBatchMetadataRegistry Test Class
 * Comprehensive tests for 100% JaCoCo coverage
 */
@ExtendWith(MockitoExtension.class)
class DefaultBatchMetadataRegistryTest {

    private DefaultBatchMetadataRegistry registry;

    @Mock
    private BatchProperties properties;

    @BeforeEach
    void setUp() {
        registry = new DefaultBatchMetadataRegistry(properties);
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create registry with properties")
        void shouldCreateRegistryWithProperties() {
            assertNotNull(registry);
        }

        @Test
        @DisplayName("Should initialize empty metadatas list")
        void shouldInitializeEmptyMetadatasList() {
            List<IBatchMetadata> metadatas = registry.getMetadatas();
            assertNotNull(metadatas);
            assertTrue(metadatas.isEmpty());
        }
    }

    @Nested
    @DisplayName("init tests")
    class InitTests {

        @Test
        @DisplayName("Should not throw exception when config location returns no resources")
        void shouldNotThrowExceptionWhenNoResources() {
            when(properties.getConfigLocation()).thenReturn("classpath:non-existent/*.xml");

            assertDoesNotThrow(() -> registry.init());

            verify(properties).getConfigLocation();
        }

        @Test
        @DisplayName("Should handle null config location")
        void shouldHandleNullConfigLocation() {
            when(properties.getConfigLocation()).thenReturn(null);

            assertDoesNotThrow(() -> registry.init());
        }

        @Test
        @DisplayName("Should handle empty config location")
        void shouldHandleEmptyConfigLocation() {
            when(properties.getConfigLocation()).thenReturn("");

            assertDoesNotThrow(() -> registry.init());
        }

        @Test
        @DisplayName("Should handle invalid config location pattern")
        void shouldHandleInvalidConfigLocationPattern() {
            when(properties.getConfigLocation()).thenReturn("invalid://location");

            assertDoesNotThrow(() -> registry.init());
        }

        @Test
        @DisplayName("Should load valid batch metadata XML files")
        void shouldLoadValidBatchMetadataXmlFiles() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            registry.init();

            List<IBatchMetadata> metadatas = registry.getMetadatas();
            assertNotNull(metadatas);
            assertEquals(1, metadatas.size());

            IBatchMetadata metadata = metadatas.get(0);
            assertEquals("test-batch-001", metadata.getId());
            assertEquals("Test Batch Description", metadata.getDescription());
            assertEquals("node-1", metadata.getTargetNode());
        }

        @Test
        @DisplayName("Should parse job metadata with parameters correctly")
        void shouldParseJobMetadataWithParametersCorrectly() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchJobMetadata jobMetadata = metadata.getBatchJobMetadata();

            assertNotNull(jobMetadata);
            assertEquals("testComponent", jobMetadata.getComponentId());
            assertNotNull(jobMetadata.getInitParameters());
            assertEquals("/data/input", jobMetadata.getInitParameters().get("inputPath"));
            assertEquals("/data/output", jobMetadata.getInitParameters().get("outputPath"));
        }

        @Test
        @DisplayName("Should parse trigger metadata with cron type correctly")
        void shouldParseTriggerMetadataWithCronTypeCorrectly() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchTriggerMetadata triggerMetadata = metadata.getBatchTriggerMetadata();

            assertNotNull(triggerMetadata);
            assertEquals(TriggerType.CRON, triggerMetadata.getTriggerType());
            assertEquals("0 0 12 * * ?", triggerMetadata.getProperty("cronExpression"));
            assertEquals("Asia/Seoul", triggerMetadata.getProperty("timezone"));
        }

        @Test
        @DisplayName("Should parse simple trigger type correctly")
        void shouldParseSimpleTriggerTypeCorrectly() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-simple.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchTriggerMetadata triggerMetadata = metadata.getBatchTriggerMetadata();

            assertNotNull(triggerMetadata);
            assertEquals(TriggerType.SIMPLE, triggerMetadata.getTriggerType());
        }

        @Test
        @DisplayName("Should parse job metadata without parameters")
        void shouldParseJobMetadataWithoutParameters() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-simple.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchJobMetadata jobMetadata = metadata.getBatchJobMetadata();

            assertNotNull(jobMetadata);
            assertEquals("simpleComponent", jobMetadata.getComponentId());
            assertNotNull(jobMetadata.getInitParameters());
            assertTrue(jobMetadata.getInitParameters().isEmpty());
        }

        @Test
        @DisplayName("Should handle trigger with no type attribute (defaults to NONE)")
        void shouldHandleTriggerWithNoTypeAttribute() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-none.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchTriggerMetadata triggerMetadata = metadata.getBatchTriggerMetadata();

            assertNotNull(triggerMetadata);
            assertEquals(TriggerType.NONE, triggerMetadata.getTriggerType());
        }

        @Test
        @DisplayName("Should handle empty targetNode")
        void shouldHandleEmptyTargetNode() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-none.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            assertEquals("", metadata.getTargetNode());
        }

        @Test
        @DisplayName("Should load multiple XML files with wildcard pattern")
        void shouldLoadMultipleXmlFilesWithWildcardPattern() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-*.xml");

            registry.init();

            List<IBatchMetadata> metadatas = registry.getMetadatas();
            assertNotNull(metadatas);
            // Should load at least some valid files (excluding invalid ones that cause parse errors)
            assertTrue(metadatas.size() >= 1);
        }

        @Test
        @DisplayName("Should handle invalid XML file gracefully")
        void shouldHandleInvalidXmlFileGracefully() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-invalid.xml");

            // Should not throw exception due to error handling
            assertDoesNotThrow(() -> registry.init());

            // Invalid XML should not be added to metadatas
            assertTrue(registry.getMetadatas().isEmpty());
        }

        @Test
        @DisplayName("Should handle empty parameters element")
        void shouldHandleEmptyParametersElement() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-none.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchJobMetadata jobMetadata = metadata.getBatchJobMetadata();

            assertNotNull(jobMetadata);
            assertNotNull(jobMetadata.getInitParameters());
            assertTrue(jobMetadata.getInitParameters().isEmpty());
        }

        @Test
        @DisplayName("Should handle empty properties element in trigger")
        void shouldHandleEmptyPropertiesElementInTrigger() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-none.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            IBatchTriggerMetadata triggerMetadata = metadata.getBatchTriggerMetadata();

            assertNotNull(triggerMetadata);
            Map<String, String> props = triggerMetadata.getProperties();
            assertNotNull(props);
            assertTrue(props.isEmpty());
        }
    }

    @Nested
    @DisplayName("getMetadatas tests")
    class GetMetadatasTests {

        @Test
        @DisplayName("Should return empty list initially")
        void shouldReturnEmptyListInitially() {
            List<IBatchMetadata> metadatas = registry.getMetadatas();
            assertNotNull(metadatas);
            assertEquals(0, metadatas.size());
        }

        @Test
        @DisplayName("Should return same list instance")
        void shouldReturnSameListInstance() {
            List<IBatchMetadata> list1 = registry.getMetadatas();
            List<IBatchMetadata> list2 = registry.getMetadatas();
            assertSame(list1, list2);
        }
    }

    @Nested
    @DisplayName("destroy tests")
    class DestroyTests {

        @Test
        @DisplayName("Should clear metadatas list on destroy")
        void shouldClearMetadatasListOnDestroy() throws Exception {
            DefaultBatchMetadata metadata = new DefaultBatchMetadata();
            metadata.setId("test-batch");
            registry.getMetadatas().add(metadata);

            assertEquals(1, registry.getMetadatas().size());

            registry.destroy();

            assertEquals(0, registry.getMetadatas().size());
        }

        @Test
        @DisplayName("Should not throw exception when destroying empty registry")
        void shouldNotThrowExceptionWhenDestroyingEmptyRegistry() {
            assertDoesNotThrow(() -> registry.destroy());
        }

        @Test
        @DisplayName("Should handle multiple destroy calls")
        void shouldHandleMultipleDestroyCalls() throws Exception {
            registry.destroy();
            registry.destroy();

            assertEquals(0, registry.getMetadatas().size());
        }

        @Test
        @DisplayName("Should clear loaded metadata after destroy")
        void shouldClearLoadedMetadataAfterDestroy() throws Exception {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            registry.init();
            assertFalse(registry.getMetadatas().isEmpty());

            registry.destroy();
            assertTrue(registry.getMetadatas().isEmpty());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IBatchMetadataRegistry")
        void shouldImplementIBatchMetadataRegistry() {
            assertTrue(registry instanceof IBatchMetadataRegistry);
        }
    }

    @Nested
    @DisplayName("Integration scenario tests")
    class IntegrationScenarioTests {

        @Test
        @DisplayName("Should handle full lifecycle")
        void shouldHandleFullLifecycle() throws Exception {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            // Init
            registry.init();
            assertNotNull(registry.getMetadatas());
            assertFalse(registry.getMetadatas().isEmpty());

            // Verify loaded content
            IBatchMetadata metadata = registry.getMetadatas().get(0);
            assertNotNull(metadata.getBatchJobMetadata());
            assertNotNull(metadata.getBatchTriggerMetadata());

            // Destroy
            registry.destroy();
            assertTrue(registry.getMetadatas().isEmpty());
        }

        @Test
        @DisplayName("Should handle reinitialize after destroy")
        void shouldHandleReinitializeAfterDestroy() throws Exception {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            // First init
            registry.init();
            int firstSize = registry.getMetadatas().size();

            // Destroy
            registry.destroy();
            assertTrue(registry.getMetadatas().isEmpty());

            // Re-init
            registry.init();
            assertEquals(firstSize, registry.getMetadatas().size());
        }

        @Test
        @DisplayName("Should accumulate metadata on multiple init calls")
        void shouldAccumulateMetadataOnMultipleInitCalls() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            registry.init();
            int firstSize = registry.getMetadatas().size();

            registry.init();
            // Metadata should accumulate (addAll is called)
            assertEquals(firstSize * 2, registry.getMetadatas().size());
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle whitespace in attribute values")
        void shouldHandleWhitespaceInAttributeValues() {
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");

            registry.init();

            IBatchMetadata metadata = registry.getMetadatas().get(0);
            // Values should be trimmed
            assertFalse(metadata.getId().contains(" "));
        }

        @Test
        @DisplayName("Should verify all trigger types can be parsed")
        void shouldVerifyAllTriggerTypesCanBeParsed() {
            // Test CRON
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-full.xml");
            registry.init();
            assertEquals(TriggerType.CRON, registry.getMetadatas().get(0).getBatchTriggerMetadata().getTriggerType());
            registry.getMetadatas().clear();

            // Test SIMPLE
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-simple.xml");
            registry.init();
            assertEquals(TriggerType.SIMPLE, registry.getMetadatas().get(0).getBatchTriggerMetadata().getTriggerType());
            registry.getMetadatas().clear();

            // Test NONE (default)
            when(properties.getConfigLocation()).thenReturn("classpath:batch/test-batch-none.xml");
            registry.init();
            assertEquals(TriggerType.NONE, registry.getMetadatas().get(0).getBatchTriggerMetadata().getTriggerType());
        }
    }
}
