package com.scbank.process.api.fw.integration.simulation;

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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.codec.XmlIntegrationClientCodec;
import com.scbank.process.api.fw.integration.simulation.impl.DefaultIntegrationSimulationRepository;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;

/**
 * IntegrationSimulationConfiguration Test Class
 */
@ExtendWith(MockitoExtension.class)
class IntegrationSimulationConfigurationTest {

    @Mock
    private IntegrationProperties mockProperties;

    @Mock
    private IntegrationSimulationHeaderStrategy<?, ?> mockHeaderStrategy;

    @Mock
    private XmlMapper mockXmlMapper;

    @Mock
    private XmlIntegrationClientCodec mockXmlCodec;

    @Mock
    private IntegrationMessageContextCreator mockContextCreator;

    private IntegrationSimulationConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new IntegrationSimulationConfiguration();
    }

    @Nested
    @DisplayName("integrationSimulationRepository tests")
    class SimulationRepositoryTests {

        @Test
        @DisplayName("Should create DefaultIntegrationSimulationRepository bean")
        void shouldCreateDefaultIntegrationSimulationRepositoryBean() {
            List<IntegrationSimulationHeaderStrategy<?, ?>> headerStrategies = new ArrayList<>();
            headerStrategies.add(mockHeaderStrategy);

            IntegrationSimulationRepository repository = configuration.integrationSimulationRepository(
                    mockProperties,
                    headerStrategies,
                    mockXmlMapper,
                    mockXmlCodec,
                    mockContextCreator);

            assertNotNull(repository);
            assertTrue(repository instanceof DefaultIntegrationSimulationRepository);
        }

        @Test
        @DisplayName("Should create repository with empty header strategies list")
        void shouldCreateRepositoryWithEmptyHeaderStrategiesList() {
            List<IntegrationSimulationHeaderStrategy<?, ?>> headerStrategies = new ArrayList<>();

            IntegrationSimulationRepository repository = configuration.integrationSimulationRepository(
                    mockProperties,
                    headerStrategies,
                    mockXmlMapper,
                    mockXmlCodec,
                    mockContextCreator);

            assertNotNull(repository);
        }

        @Test
        @DisplayName("Should create repository with multiple header strategies")
        void shouldCreateRepositoryWithMultipleHeaderStrategies() {
            @SuppressWarnings("unchecked")
            IntegrationSimulationHeaderStrategy<Object, Object> mockStrategy2 = mock(IntegrationSimulationHeaderStrategy.class);

            List<IntegrationSimulationHeaderStrategy<?, ?>> headerStrategies = new ArrayList<>();
            headerStrategies.add(mockHeaderStrategy);
            headerStrategies.add(mockStrategy2);

            IntegrationSimulationRepository repository = configuration.integrationSimulationRepository(
                    mockProperties,
                    headerStrategies,
                    mockXmlMapper,
                    mockXmlCodec,
                    mockContextCreator);

            //TEST
            assertNotNull(repository);
        }
    }

    @Nested
    @DisplayName("configuration annotation tests")
    class ConfigurationAnnotationTests {

        @Test
        @DisplayName("Should be a configuration class")
        void shouldBeAConfigurationClass() {
            assertNotNull(configuration);
            assertTrue(IntegrationSimulationConfiguration.class.isAnnotationPresent(
                    org.springframework.context.annotation.Configuration.class));
        }

        @Test
        @DisplayName("Should have ConditionalOnProperty annotation")
        void shouldHaveConditionalOnPropertyAnnotation() {
            assertTrue(IntegrationSimulationConfiguration.class.isAnnotationPresent(
                    org.springframework.boot.autoconfigure.condition.ConditionalOnProperty.class));
        }
    }
}
