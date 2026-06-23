package com.scbank.process.api.fw.channel.config;

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
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.MessageSourceConfig;
import com.scbank.process.api.fw.core.validation.DefaultBeanValidator;
import com.scbank.process.api.fw.core.validation.IBeanValidator;
import com.scbank.process.api.fw.core.validation.attribute.DefaultValidationAttributeExtractorRegistry;
import com.scbank.process.api.fw.core.validation.attribute.IValidationAttributeExtractorRegistry;
import com.scbank.process.api.fw.core.validation.message.DefaultValidationMessageResolver;
import com.scbank.process.api.fw.core.validation.message.IValidationMessageResolver;

import jakarta.validation.Validator;

/**
 * ChannelValidationConfigurations Test Class
 * Comprehensive tests for 100% JaCoCo coverage
 */
@ExtendWith(MockitoExtension.class)
class ChannelValidationConfigurationsTest {

    private ChannelValidationConfigurations configurations;

    @Mock
    private Validator validator;

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private MessageSource messageSource;

    @Mock
    private IValidationAttributeExtractorRegistry validationAttributeExtractorRegistry;

    @BeforeEach
    void setUp() {
        configurations = new ChannelValidationConfigurations();
    }

    @Nested
    @DisplayName("BeanValidator tests")
    class BeanValidatorTests {

        @Test
        @DisplayName("Should create DefaultBeanValidator")
        void shouldCreateBeanValidator() {
            IBeanValidator<?> beanValidator = configurations.beanValidator(validator);

            assertNotNull(beanValidator);
            assertTrue(beanValidator instanceof DefaultBeanValidator);
        }

        @Test
        @DisplayName("Should create BeanValidator with provided Validator")
        void shouldCreateBeanValidatorWithProvidedValidator() {
            IBeanValidator<?> beanValidator = configurations.beanValidator(validator);

            assertNotNull(beanValidator);
        }
    }

    @Nested
    @DisplayName("MessageSource tests")
    class MessageSourceTests {

        @Test
        @DisplayName("Should create MessageSource with locations")
        void shouldCreateMessageSourceWithLocations() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    3600,
                    List.of("classpath:messages/validation", "classpath:messages/errors"));

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
            assertTrue(source instanceof ReloadableResourceBundleMessageSource);
        }

        @Test
        @DisplayName("Should create MessageSource with empty locations")
        void shouldCreateMessageSourceWithEmptyLocations() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    true,
                    1800,
                    List.of());

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }

        @Test
        @DisplayName("Should create MessageSource with single location")
        void shouldCreateMessageSourceWithSingleLocation() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    -1,
                    List.of("classpath:messages"));

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }

        @Test
        @DisplayName("Should create MessageSource with custom encoding")
        void shouldCreateMessageSourceWithCustomEncoding() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "EUC-KR",
                    false,
                    3600,
                    List.of("classpath:messages"));

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }

        @Test
        @DisplayName("Should create MessageSource with fallback to system locale enabled")
        void shouldCreateMessageSourceWithFallbackEnabled() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    true,
                    3600,
                    List.of("classpath:messages"));

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }

        @Test
        @DisplayName("Should create MessageSource with zero cache seconds")
        void shouldCreateMessageSourceWithZeroCacheSeconds() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    0,
                    List.of("classpath:messages"));

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }
    }

    @Nested
    @DisplayName("ValidationAttributeExtractorRegistry tests")
    class ValidationAttributeExtractorRegistryTests {

        @Test
        @DisplayName("Should create DefaultValidationAttributeExtractorRegistry")
        void shouldCreateValidationAttributeExtractorRegistry() {
            IValidationAttributeExtractorRegistry registry = configurations.validationAttributeExtractorRegistry();

            assertNotNull(registry);
            assertTrue(registry instanceof DefaultValidationAttributeExtractorRegistry);
        }
    }

    @Nested
    @DisplayName("ValidationMessageResolver tests")
    class ValidationMessageResolverTests {

        @Test
        @DisplayName("Should create DefaultValidationMessageResolver")
        void shouldCreateValidationMessageResolver() {
            IValidationMessageResolver resolver = configurations.validationMessageResolver(
                    messageSource,
                    validationAttributeExtractorRegistry);

            assertNotNull(resolver);
            assertTrue(resolver instanceof DefaultValidationMessageResolver);
        }

        @Test
        @DisplayName("Should create ValidationMessageResolver with actual registry")
        void shouldCreateValidationMessageResolverWithActualRegistry() {
            IValidationAttributeExtractorRegistry registry = configurations.validationAttributeExtractorRegistry();

            IValidationMessageResolver resolver = configurations.validationMessageResolver(
                    messageSource,
                    registry);

            assertNotNull(resolver);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should create all validation-related beans successfully")
        void shouldCreateAllValidationBeansSuccessfully() {
            // BeanValidator
            IBeanValidator<?> beanValidator = configurations.beanValidator(validator);
            assertNotNull(beanValidator);

            // ValidationAttributeExtractorRegistry
            IValidationAttributeExtractorRegistry registry = configurations.validationAttributeExtractorRegistry();
            assertNotNull(registry);

            // MessageSource
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    3600,
                    List.of("classpath:messages"));
            when(channelProperties.getMessageSource()).thenReturn(config);
            MessageSource source = configurations.messageSource(channelProperties);
            assertNotNull(source);

            // ValidationMessageResolver
            IValidationMessageResolver resolver = configurations.validationMessageResolver(source, registry);
            assertNotNull(resolver);
        }

        @Test
        @DisplayName("Should create full validation chain")
        void shouldCreateFullValidationChain() {
            // Create registry
            IValidationAttributeExtractorRegistry registry = configurations.validationAttributeExtractorRegistry();

            // Create message source with config
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    3600,
                    List.of("classpath:messages/validation"));
            when(channelProperties.getMessageSource()).thenReturn(config);
            MessageSource source = configurations.messageSource(channelProperties);

            // Create resolver using actual beans
            IValidationMessageResolver resolver = configurations.validationMessageResolver(source, registry);

            // All beans should be non-null
            assertNotNull(registry);
            assertNotNull(source);
            assertNotNull(resolver);
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle multiple message locations")
        void shouldHandleMultipleMessageLocations() {
            List<String> locations = new ArrayList<>();
            locations.add("classpath:messages/validation");
            locations.add("classpath:messages/errors");
            locations.add("classpath:messages/common");
            locations.add("classpath:i18n/messages");

            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    3600,
                    locations);

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }

        @Test
        @DisplayName("Should handle negative cache seconds")
        void shouldHandleNegativeCacheSeconds() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    -1,
                    List.of("classpath:messages"));

            when(channelProperties.getMessageSource()).thenReturn(config);

            MessageSource source = configurations.messageSource(channelProperties);

            assertNotNull(source);
        }
    }
}
