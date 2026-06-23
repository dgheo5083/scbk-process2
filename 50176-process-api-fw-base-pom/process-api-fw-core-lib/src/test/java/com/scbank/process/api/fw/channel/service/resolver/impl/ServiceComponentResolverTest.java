package com.scbank.process.api.fw.channel.service.resolver.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;

/**
 * ServiceComponentResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceComponentResolverTest {

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private ServiceDefinitionMetadata serviceDefinitionMetadata;

    private ServiceComponentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new ServiceComponentResolver();
    }

    @Nested
    @DisplayName("resolve tests")
    class ResolveTests {

        @Test
        @DisplayName("Should resolve service with matching condition")
        void shouldResolveServiceWithMatchingCondition() throws Exception {
            // Given - empty condition always matches (returns true from evaluator)
            ServiceInfo serviceInfo = createServiceInfo("testComponent", "", false, 1, null);
            List<ServiceInfo> services = List.of(serviceInfo);

            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(services);

            // When
            ServiceInfo result = resolver.resolve(serviceContext);

            // Then
            assertNotNull(result);
            assertEquals("testComponent", result.getComponent());
        }

        @Test
        @DisplayName("Should resolve higher priority service first")
        void shouldResolveHigherPriorityServiceFirst() throws Exception {
            // Given - empty condition always matches (returns true from evaluator)
            ServiceInfo lowPriorityService = createServiceInfo("lowPriority", "", false, 10, null);
            ServiceInfo highPriorityService = createServiceInfo("highPriority", "", false, 1, null);
            List<ServiceInfo> services = List.of(lowPriorityService, highPriorityService);

            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(services);

            // When
            ServiceInfo result = resolver.resolve(serviceContext);

            // Then
            assertEquals("highPriority", result.getComponent());
        }

        @Test
        @DisplayName("Should fallback when no condition matches")
        void shouldFallbackWhenNoConditionMatches() throws Exception {
            // Given
            ServiceInfo normalService = createServiceInfo("normal", "false", false, 1, null);
            ServiceInfo fallbackService = createServiceInfo("fallback", "", true, 100, null);
            List<ServiceInfo> services = List.of(normalService, fallbackService);

            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(services);
            when(serviceDefinitionMetadata.getServiceId()).thenReturn("TEST_SERVICE");
            when(serviceDefinitionMetadata.getDescription()).thenReturn("Test Service");
            when(serviceDefinitionMetadata.getUrl()).thenReturn("/test");

            // When
            ServiceInfo result = resolver.resolve(serviceContext);

            // Then
            assertEquals("fallback", result.getComponent());
        }

        @Test
        @DisplayName("Should use fallbackRef when specified")
        void shouldUseFallbackRefWhenSpecified() throws Exception {
            // Given
            ServiceInfo normalService = createServiceInfo("normal", "false", false, 1, null);
            ServiceInfo targetService = createServiceInfo("targetComponent", "", false, 2, null);
            ServiceInfo fallbackService = createServiceInfo("fallback", "", true, 100, "targetComponent");
            List<ServiceInfo> services = List.of(normalService, targetService, fallbackService);

            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(services);
            when(serviceDefinitionMetadata.getServiceId()).thenReturn("TEST_SERVICE");
            when(serviceDefinitionMetadata.getDescription()).thenReturn("Test Service");
            when(serviceDefinitionMetadata.getUrl()).thenReturn("/test");

            // When
            ServiceInfo result = resolver.resolve(serviceContext);

            // Then
            assertEquals("targetComponent", result.getComponent());
        }

        @Test
        @DisplayName("Should throw exception when no fallback available")
        void shouldThrowExceptionWhenNoFallbackAvailable() {
            // Given
            ServiceInfo normalService = createServiceInfo("normal", "false", false, 1, null);
            List<ServiceInfo> services = List.of(normalService);

            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(services);
            when(serviceDefinitionMetadata.getServiceId()).thenReturn("TEST_SERVICE");
            when(serviceDefinitionMetadata.getDescription()).thenReturn("Test Service");
            when(serviceDefinitionMetadata.getUrl()).thenReturn("/test");

            // When/Then
            assertThrows(IllegalStateException.class, () -> resolver.resolve(serviceContext));
        }

        @Test
        @DisplayName("Should skip fallback services during condition evaluation")
        void shouldSkipFallbackServicesDuringConditionEvaluation() throws Exception {
            // Given - empty condition always matches (returns true from evaluator)
            // fallback services are skipped during normal condition evaluation
            ServiceInfo fallbackFirst = createServiceInfo("fallbackFirst", "", true, 1, null);
            ServiceInfo normalService = createServiceInfo("normal", "", false, 2, null);
            List<ServiceInfo> services = List.of(fallbackFirst, normalService);

            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(services);

            // When
            ServiceInfo result = resolver.resolve(serviceContext);

            // Then
            assertEquals("normal", result.getComponent());
        }
    }

    /**
     * Helper method to create ServiceInfo
     */
    private ServiceInfo createServiceInfo(String component, String condition, boolean isFallback,
                                           int priority, String fallbackRef) {
        ServiceInfo serviceInfo = mock(ServiceInfo.class);
        when(serviceInfo.getComponent()).thenReturn(component);
        when(serviceInfo.getCondition()).thenReturn(condition);
        when(serviceInfo.isFallback()).thenReturn(isFallback);
        when(serviceInfo.getPriority()).thenReturn(priority);
        when(serviceInfo.getFallbackRef()).thenReturn(fallbackRef);
        return serviceInfo;
    }
}
