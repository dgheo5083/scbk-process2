package com.scbank.process.api.fw.channel.service.registry.impl;

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
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import com.scbank.process.api.fw.channel.service.ServiceId;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;

/**
 * ServiceRegistrar Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceRegistrarTest {

    @Mock
    private Environment environment;

    @Mock
    private BeanDefinitionRegistry registry;

    private ServiceRegistrar serviceRegistrar;

    @BeforeEach
    void setUp() {
        serviceRegistrar = new ServiceRegistrar();
        serviceRegistrar.setEnvironment(environment);
    }

    @Nested
    @DisplayName("setEnvironment tests")
    class SetEnvironmentTests {

        @Test
        @DisplayName("Should set environment successfully")
        void shouldSetEnvironmentSuccessfully() {
            ServiceRegistrar newRegistrar = new ServiceRegistrar();

            assertDoesNotThrow(() -> newRegistrar.setEnvironment(environment));
        }
    }

    @Nested
    @DisplayName("getOrder tests")
    class GetOrderTests {

        @Test
        @DisplayName("Should return LOWEST_PRECEDENCE")
        void shouldReturnLowestPrecedence() {
            int order = serviceRegistrar.getOrder();

            assertEquals(Ordered.LOWEST_PRECEDENCE, order);
        }
    }

    @Nested
    @DisplayName("getServiceDefinition tests")
    class GetServiceDefinitionTests {

        @Test
        @DisplayName("Should return null when no definition exists")
        void shouldReturnNullWhenNoDefinitionExists() {
            ServiceId serviceId = ServiceId.builder()
                    .service("TEST_SERVICE")
                    .url("/test/url")
                    .build();

            ServiceDefinitionMetadata result = serviceRegistrar.getServiceDefinition(serviceId);

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("getServiceDefinitions tests")
    class GetServiceDefinitionsTests {

        @Test
        @DisplayName("Should return empty list when no definitions")
        void shouldReturnEmptyListWhenNoDefinitions() {
            List<ServiceDefinitionMetadata> result = serviceRegistrar.getServiceDefinitions();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getServiceMethodMetadata tests")
    class GetServiceMethodMetadataTests {

        @Test
        @DisplayName("Should return null when no metadata exists for class and method")
        void shouldReturnNullWhenNoMetadataExists() {
            ServiceMethodMetadata result = serviceRegistrar.getServiceMethodMetadata(String.class, "toString");

            assertNull(result);
        }

        @Test
        @DisplayName("Should return null when no metadata exists for service method name")
        void shouldReturnNullWhenNoMetadataExistsForName() {
            ServiceMethodMetadata result = serviceRegistrar.getServiceMethodMetadata("com.test.Service@method");

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("getAllServiceMethodMetadata tests")
    class GetAllServiceMethodMetadataTests {

        @Test
        @DisplayName("Should return empty list when no metadata")
        void shouldReturnEmptyListWhenNoMetadata() {
            List<ServiceMethodMetadata> result = serviceRegistrar.getAllServiceMethodMetadata();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("postProcessBeanDefinitionRegistry tests")
    class PostProcessBeanDefinitionRegistryTests {

        @Test
        @DisplayName("Should handle missing properties binding")
        void shouldHandleMissingPropertiesBinding() {
            when(environment.getProperty(anyString())).thenReturn(null);

            // When properties binding fails, it should throw an exception
            assertThrows(Exception.class,
                    () -> serviceRegistrar.postProcessBeanDefinitionRegistry(registry));
        }
    }
}
