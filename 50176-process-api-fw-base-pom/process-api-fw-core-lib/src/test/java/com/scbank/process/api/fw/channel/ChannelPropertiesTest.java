package com.scbank.process.api.fw.channel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.channel.ChannelProperties.DeviceConfig;
import com.scbank.process.api.fw.channel.ChannelProperties.ExceptionConfig;
import com.scbank.process.api.fw.channel.ChannelProperties.MessageSourceConfig;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceComponentConfig;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties.ServiceMappingConfig;
import com.scbank.process.api.fw.channel.ChannelProperties.ValidationConfig;

/**
 * ChannelProperties Test Class
 */
class ChannelPropertiesTest {

    private ChannelProperties channelProperties;

    @BeforeEach
    void setUp() {
        channelProperties = new ChannelProperties();
    }

    @Nested
    @DisplayName("enabled property tests")
    class EnabledPropertyTests {

        @Test
        @DisplayName("Should get and set enabled")
        void shouldGetAndSetEnabled() {
            channelProperties.setEnabled(true);
            assertTrue(channelProperties.isEnabled());

            channelProperties.setEnabled(false);
            assertFalse(channelProperties.isEnabled());
        }

        @Test
        @DisplayName("Should have default false value")
        void shouldHaveDefaultFalseValue() {
            assertFalse(channelProperties.isEnabled());
        }
    }

    @Nested
    @DisplayName("defaultControllerClass property tests")
    class DefaultControllerClassPropertyTests {

        @Test
        @DisplayName("Should get and set defaultControllerClass")
        void shouldGetAndSetDefaultControllerClass() {
            channelProperties.setDefaultControllerClass(String.class);
            assertEquals(String.class, channelProperties.getDefaultControllerClass());
        }

        @Test
        @DisplayName("Should allow null defaultControllerClass")
        void shouldAllowNullDefaultControllerClass() {
            channelProperties.setDefaultControllerClass(null);
            assertNull(channelProperties.getDefaultControllerClass());
        }
    }

    @Nested
    @DisplayName("defaultControllerMethod property tests")
    class DefaultControllerMethodPropertyTests {

        @Test
        @DisplayName("Should get and set defaultControllerMethod")
        void shouldGetAndSetDefaultControllerMethod() {
            channelProperties.setDefaultControllerMethod("dispatch");
            assertEquals("dispatch", channelProperties.getDefaultControllerMethod());
        }
    }

    @Nested
    @DisplayName("service property tests")
    class ServicePropertyTests {

        @Test
        @DisplayName("Should get and set service list")
        void shouldGetAndSetServiceList() {
            List<ServiceProperties> services = new ArrayList<>();
            channelProperties.setService(services);
            assertEquals(services, channelProperties.getService());
        }

        @Test
        @DisplayName("Should allow null service list")
        void shouldAllowNullServiceList() {
            channelProperties.setService(null);
            assertNull(channelProperties.getService());
        }
    }

    @Nested
    @DisplayName("ExceptionConfig record tests")
    class ExceptionConfigTests {

        @Test
        @DisplayName("Should create ExceptionConfig with handleGlobalAdvice")
        void shouldCreateExceptionConfigWithHandleGlobalAdvice() {
            ExceptionConfig config = new ExceptionConfig(true);
            assertTrue(config.handleGlobalAdvice());
        }

        @Test
        @DisplayName("Should create ExceptionConfig with false")
        void shouldCreateExceptionConfigWithFalse() {
            ExceptionConfig config = new ExceptionConfig(false);
            assertFalse(config.handleGlobalAdvice());
        }
    }

    @Nested
    @DisplayName("ValidationConfig record tests")
    class ValidationConfigTests {

        @Test
        @DisplayName("Should create ValidationConfig with enabled true")
        void shouldCreateValidationConfigWithEnabledTrue() {
            ValidationConfig config = new ValidationConfig(true);
            assertTrue(config.enabled());
        }

        @Test
        @DisplayName("Should create ValidationConfig with enabled false")
        void shouldCreateValidationConfigWithEnabledFalse() {
            ValidationConfig config = new ValidationConfig(false);
            assertFalse(config.enabled());
        }
    }

    @Nested
    @DisplayName("DeviceConfig record tests")
    class DeviceConfigTests {

        @Test
        @DisplayName("Should create DeviceConfig with all fields")
        void shouldCreateDeviceConfigWithAllFields() {
            DeviceConfig config = new DeviceConfig("PC", "Personal Computer", "Desktop/Laptop", 1, ".*Windows.*", true);

            assertEquals("PC", config.id());
            assertEquals("Personal Computer", config.name());
            assertEquals("Desktop/Laptop", config.description());
            assertEquals(1, config.order());
            assertEquals(".*Windows.*", config.regEx());
            assertTrue(config.isDefault());
        }

        @Test
        @DisplayName("Should handle null values")
        void shouldHandleNullValues() {
            DeviceConfig config = new DeviceConfig(null, null, null, 0, null, false);

            assertNull(config.id());
            assertNull(config.name());
            assertNull(config.description());
            assertEquals(0, config.order());
            assertNull(config.regEx());
            assertFalse(config.isDefault());
        }
    }

    @Nested
    @DisplayName("ServiceProperties record tests")
    class ServicePropertiesTests {

        @Test
        @DisplayName("Should create ServiceProperties with all fields")
        void shouldCreateServicePropertiesWithAllFields() {
            ServiceMappingConfig mappingConfig = new ServiceMappingConfig(
                    "Test description",
                    Arrays.asList("/login/**", "/logout/**"),
                    Arrays.asList("application/json"),
                    Arrays.asList("GET", "POST"));

            ServiceComponentConfig componentConfig = new ServiceComponentConfig(
                    Arrays.asList("com.test.service"));

            ServiceProperties props = new ServiceProperties(
                    true,
                    "COMMON",
                    "/api/common",
                    Object.class,
                    "dispatch",
                    Arrays.asList("classpath:/services/*.xml"),
                    Arrays.asList(Object.class),
                    mappingConfig,
                    componentConfig);

            assertTrue(props.enabled());
            assertEquals("COMMON", props.serviceId());
            assertEquals("/api/common", props.basePath());
            assertEquals(Object.class, props.controllerClass());
            assertEquals("dispatch", props.controllerMethod());
            assertNotNull(props.configLocations());
            assertNotNull(props.interceptors());
            assertEquals(mappingConfig, props.serviceMapping());
            assertEquals(componentConfig, props.component());
        }
    }

    @Nested
    @DisplayName("ServiceMappingConfig record tests")
    class ServiceMappingConfigTests {

        @Test
        @DisplayName("Should create ServiceMappingConfig with all fields")
        void shouldCreateServiceMappingConfigWithAllFields() {
            ServiceMappingConfig config = new ServiceMappingConfig(
                    "API mapping config",
                    Arrays.asList("/api/**"),
                    Arrays.asList("application/json", "application/xml"),
                    Arrays.asList("GET", "POST", "PUT"));

            assertEquals("API mapping config", config.description());
            assertEquals(1, config.allowedUrlPatterns().size());
            assertEquals(2, config.allowedContentTypes().size());
            assertEquals(3, config.allowedMethods().size());
        }
    }

    @Nested
    @DisplayName("MessageSourceConfig record tests")
    class MessageSourceConfigTests {

        @Test
        @DisplayName("Should create MessageSourceConfig with all fields")
        void shouldCreateMessageSourceConfigWithAllFields() {
            MessageSourceConfig config = new MessageSourceConfig(
                    true,
                    "UTF-8",
                    false,
                    3600,
                    Arrays.asList("classpath:/messages/", "classpath:/i18n/"));

            assertTrue(config.enabled());
            assertEquals("UTF-8", config.encoding());
            assertFalse(config.fallbackToSystemLocale());
            assertEquals(3600, config.cacheSeconds());
            assertEquals(2, config.locations().size());
        }
    }

    @Nested
    @DisplayName("ServiceComponentConfig record tests")
    class ServiceComponentConfigTests {

        @Test
        @DisplayName("Should create ServiceComponentConfig with basePackages")
        void shouldCreateServiceComponentConfigWithBasePackages() {
            ServiceComponentConfig config = new ServiceComponentConfig(
                    Arrays.asList("com.test.service", "com.test.component"));

            assertEquals(2, config.basePackages().size());
            assertTrue(config.basePackages().contains("com.test.service"));
        }

        @Test
        @DisplayName("Should handle empty basePackages")
        void shouldHandleEmptyBasePackages() {
            ServiceComponentConfig config = new ServiceComponentConfig(new ArrayList<>());

            assertNotNull(config.basePackages());
            assertTrue(config.basePackages().isEmpty());
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(channelProperties, channelProperties);
        }

        @Test
        @DisplayName("Should be equal to another with same values")
        void shouldBeEqualToAnotherWithSameValues() {
            ChannelProperties another = new ChannelProperties();
            another.setEnabled(false);

            channelProperties.setEnabled(false);

            assertEquals(channelProperties, another);
        }
    }
}
