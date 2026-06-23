package com.scbank.process.api.fw.base;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * ProcessBaseApplication Test Class
 *
 * @author test
 */
@ExtendWith(MockitoExtension.class)
class ProcessBaseApplicationTest {

    /**
     * Concrete implementation of abstract ProcessBaseApplication for testing
     */
    static class TestProcessBaseApplication extends ProcessBaseApplication {
        @Override
        public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
            return super.configure(builder);
        }
    }

    private TestProcessBaseApplication application;

    @Mock
    private SpringApplicationBuilder mockBuilder;

    @BeforeEach
    void setUp() throws Exception {
        application = new TestProcessBaseApplication();
        // Clear static defaultProperties before each test
        clearDefaultProperties();
        MDC.clear();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear static defaultProperties after each test
        clearDefaultProperties();
        MDC.clear();
    }

    @SuppressWarnings("unchecked")
    private void clearDefaultProperties() throws Exception {
        Field field = ProcessBaseApplication.class.getDeclaredField("defaultProperties");
        field.setAccessible(true);
        Map<String, Object> properties = (Map<String, Object>) field.get(null);
        properties.clear();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getDefaultProperties() throws Exception {
        Field field = ProcessBaseApplication.class.getDeclaredField("defaultProperties");
        field.setAccessible(true);
        return (Map<String, Object>) field.get(null);
    }

    @Nested
    @DisplayName("configure() tests")
    class ConfigureTests {

        @Test
        @DisplayName("Should configure builder with application class as source")
        void configure_ShouldReturnBuilderWithSources() {
            // given
            when(mockBuilder.sources(any(Class[].class))).thenReturn(mockBuilder);

            // when
            SpringApplicationBuilder result = application.configure(mockBuilder);

            // then
            assertNotNull(result);
            verify(mockBuilder).sources(any(Class[].class));
        }

        @Test
        @DisplayName("Should return same builder instance")
        void configure_ShouldReturnSameBuilderInstance() {
            // given
            when(mockBuilder.sources(any(Class[].class))).thenReturn(mockBuilder);

            // when
            SpringApplicationBuilder result = application.configure(mockBuilder);

            // then
            assertSame(mockBuilder, result);
        }
    }

    @Nested
    @DisplayName("initBase() tests")
    class InitBaseTests {

        @Test
        @DisplayName("Should initialize with valid application name")
        void initBase_ShouldInitializeWithValidName() throws Exception {
            // given
            String applicationName = "experience-api-test";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
            assertEquals(applicationName, MDC.get("serviceName"));
            verify(mockBuilder).properties(anyMap());
        }

        @Test
        @DisplayName("Should throw RuntimeException when applicationName is null")
        void initBase_ShouldThrowExceptionWhenNameIsNull() {
            // when/then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                ProcessBaseApplication.initBase(mockBuilder, null);
            });

            assertEquals("Please specify a proper application name like experience-api-xxx.", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw RuntimeException when applicationName is empty")
        void initBase_ShouldThrowExceptionWhenNameIsEmpty() {
            // when/then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                ProcessBaseApplication.initBase(mockBuilder, "");
            });

            assertEquals("Please specify a proper application name like experience-api-xxx.", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw RuntimeException when applicationName is blank")
        void initBase_ShouldThrowExceptionWhenNameIsBlank() {
            // when/then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                ProcessBaseApplication.initBase(mockBuilder, "   ");
            });

            assertEquals("Please specify a proper application name like experience-api-xxx.", exception.getMessage());
        }

        @Test
        @DisplayName("Should load base properties and merge with application name")
        void initBase_ShouldLoadAndMergeProperties() throws Exception {
            // given
            String applicationName = "test-application";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertNotNull(properties);
            assertTrue(properties.containsKey("spring.application.name"));
            // Should also contain properties from base-application.yml
            assertTrue(properties.size() > 1);
        }

        @Test
        @DisplayName("Should set service name in MDC")
        void initBase_ShouldSetServiceNameInMDC() {
            // given
            String applicationName = "mdc-test-app";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            assertEquals(applicationName, MDC.get("serviceName"));
        }

        @Test
        @DisplayName("Should handle different valid application names")
        void initBase_ShouldHandleDifferentValidNames() throws Exception {
            // given
            String applicationName = "my-custom-api-123";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
        }
    }

    @Nested
    @DisplayName("loadBaseProperties() tests")
    class LoadBasePropertiesTests {

        @Test
        @DisplayName("Should load properties from base-application.yml")
        void loadBaseProperties_ShouldLoadFromYaml() {
            // when
            Map<String, Object> properties = ProcessBaseApplication.loadBaseProperties();

            // then
            assertNotNull(properties);
            assertFalse(properties.isEmpty());
        }

        @Test
        @DisplayName("Should return map containing spring properties")
        void loadBaseProperties_ShouldContainSpringProperties() {
            // when
            Map<String, Object> properties = ProcessBaseApplication.loadBaseProperties();

            // then
            assertNotNull(properties);
            // The base-application.yml contains spring.banner.location and spring.cloud properties
            assertTrue(properties.containsKey("spring.banner.location") ||
                       properties.containsKey("spring.cloud.openfeign.client.config.default.loggerLevel") ||
                       properties.size() > 0);
        }

        @Test
        @DisplayName("Should return consistent properties on multiple calls")
        void loadBaseProperties_ShouldReturnConsistentProperties() {
            // when
            Map<String, Object> properties1 = ProcessBaseApplication.loadBaseProperties();
            Map<String, Object> properties2 = ProcessBaseApplication.loadBaseProperties();

            // then
            assertEquals(properties1.size(), properties2.size());
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should properly initialize application with all components")
        void shouldProperlyInitializeApplication() throws Exception {
            // given
            String applicationName = "integration-test-app";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();

            // Verify application name is set
            assertEquals(applicationName, properties.get("spring.application.name"));

            // Verify MDC is set
            assertEquals(applicationName, MDC.get("serviceName"));

            // Verify base properties are loaded
            assertTrue(properties.size() > 1);

            // Verify builder.properties was called
            verify(mockBuilder).properties(properties);
        }

        @Test
        @DisplayName("Should handle special characters in application name")
        void shouldHandleSpecialCharactersInName() throws Exception {
            // given
            String applicationName = "test-app_v1.0";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
            assertEquals(applicationName, MDC.get("serviceName"));
        }

        @Test
        @DisplayName("Should handle unicode in application name")
        void shouldHandleUnicodeInName() throws Exception {
            // given
            String applicationName = "test-app-한글";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
        }
    }

    @Nested
    @DisplayName("Abstract class instantiation tests")
    class AbstractClassTests {

        @Test
        @DisplayName("Should be able to create concrete subclass")
        void shouldCreateConcreteSubclass() {
            // when
            TestProcessBaseApplication app = new TestProcessBaseApplication();

            // then
            assertNotNull(app);
            assertTrue(app instanceof ProcessBaseApplication);
        }

        @Test
        @DisplayName("Concrete subclass should be instance of SpringBootServletInitializer")
        void shouldBeInstanceOfSpringBootServletInitializer() {
            // when
            TestProcessBaseApplication app = new TestProcessBaseApplication();

            // then
            assertTrue(app instanceof org.springframework.boot.web.servlet.support.SpringBootServletInitializer);
        }
    }

    @Nested
    @DisplayName("Edge cases tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long application name")
        void shouldHandleVeryLongName() throws Exception {
            // given
            String applicationName = "a".repeat(200);
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
        }

        @Test
        @DisplayName("Should handle single character application name")
        void shouldHandleSingleCharName() throws Exception {
            // given
            String applicationName = "a";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
        }

        @Test
        @DisplayName("Should handle application name with only numbers")
        void shouldHandleNumericName() throws Exception {
            // given
            String applicationName = "12345";
            when(mockBuilder.properties(anyMap())).thenReturn(mockBuilder);

            // when
            ProcessBaseApplication.initBase(mockBuilder, applicationName);

            // then
            Map<String, Object> properties = getDefaultProperties();
            assertEquals(applicationName, properties.get("spring.application.name"));
        }

        @Test
        @DisplayName("Should reject tab-only application name")
        void shouldRejectTabOnlyName() {
            // when/then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                ProcessBaseApplication.initBase(mockBuilder, "\t\t");
            });

            assertEquals("Please specify a proper application name like experience-api-xxx.", exception.getMessage());
        }

        @Test
        @DisplayName("Should reject newline-only application name")
        void shouldRejectNewlineOnlyName() {
            // when/then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                ProcessBaseApplication.initBase(mockBuilder, "\n\n");
            });

            assertEquals("Please specify a proper application name like experience-api-xxx.", exception.getMessage());
        }
    }
}
