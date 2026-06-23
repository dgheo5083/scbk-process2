package com.scbank.process.api.fw.channel.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.device.impl.DefaultDeviceManager;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

/**
 * ChannelCommonConfigurations Test Class
 */
@ExtendWith(MockitoExtension.class)
class ChannelCommonConfigurationsTest {

    private ChannelCommonConfigurations configurations;

    @BeforeEach
    void setUp() {
        configurations = new ChannelCommonConfigurations();
        ReflectionTestUtils.setField(configurations, "defaultLocale", "ko_KR");
    }

    @Nested
    @DisplayName("requestUUIdGenerator tests")
    class RequestUUIdGeneratorTests {

        @Test
        @DisplayName("Should create UUID generator that generates non-null UUIDs")
        void shouldCreateUuidGenerator() {
            IIdentifyGenerator generator = configurations.requestUUIdGenerator();

            assertNotNull(generator);
            String uuid = generator.generateId();
            assertNotNull(uuid);
            assertFalse(uuid.isEmpty());
        }

        @Test
        @DisplayName("Should generate unique UUIDs on each call")
        void shouldGenerateUniqueUuids() {
            IIdentifyGenerator generator = configurations.requestUUIdGenerator();

            String uuid1 = generator.generateId();
            String uuid2 = generator.generateId();

            assertNotEquals(uuid1, uuid2);
        }
    }

    @Nested
    @DisplayName("localeResolver tests")
    class LocaleResolverTests {

        @Test
        @DisplayName("Should create AcceptHeaderLocaleResolver")
        void shouldCreateLocaleResolver() {
            LocaleResolver resolver = configurations.localeResolver();

            assertNotNull(resolver);
            assertTrue(resolver instanceof AcceptHeaderLocaleResolver);
        }

        @Test
        @DisplayName("Should set default locale to Korean")
        void shouldSetDefaultLocale() {
            LocaleResolver resolver = configurations.localeResolver();

            // AcceptHeaderLocaleResolver uses default locale when Accept-Language header is not set
            assertNotNull(resolver);
            assertTrue(resolver instanceof AcceptHeaderLocaleResolver);
        }
    }

    @Nested
    @DisplayName("deviceList tests")
    class DeviceListTests {

        @Test
        @DisplayName("Should create device list with PC and MOBILE")
        void shouldCreateDeviceList() {
            List<IDevice> devices = configurations.deviceList();

            assertNotNull(devices);
            assertEquals(2, devices.size());
        }

        @Test
        @DisplayName("Should contain PC device as default")
        void shouldContainPcAsDefault() {
            List<IDevice> devices = configurations.deviceList();

            IDevice pcDevice = devices.stream()
                    .filter(d -> "PC".equals(d.getId()))
                    .findFirst()
                    .orElse(null);

            assertNotNull(pcDevice);
            assertEquals("PC", pcDevice.getName());
            assertTrue(pcDevice.isDefaultDevice());
        }

        @Test
        @DisplayName("Should contain MOBILE device with regex pattern")
        void shouldContainMobileDevice() {
            List<IDevice> devices = configurations.deviceList();

            IDevice mobileDevice = devices.stream()
                    .filter(d -> "MOBILE".equals(d.getId()))
                    .findFirst()
                    .orElse(null);

            assertNotNull(mobileDevice);
            assertEquals("MOBILE", mobileDevice.getName());
            assertFalse(mobileDevice.isDefaultDevice());
            assertNotNull(mobileDevice.getRegEx());
        }
    }

    @Nested
    @DisplayName("deviceManager tests")
    class DeviceManagerTests {

        @Test
        @DisplayName("Should create device manager with device list")
        void shouldCreateDeviceManager() {
            List<IDevice> deviceList = configurations.deviceList();
            IDeviceManager manager = configurations.deviceManager(deviceList);

            assertNotNull(manager);
            assertTrue(manager instanceof DefaultDeviceManager);
        }

        @Test
        @DisplayName("Should add devices to manager")
        void shouldAddDevicesToManager() {
            List<IDevice> deviceList = configurations.deviceList();
            IDeviceManager manager = configurations.deviceManager(deviceList);

            IDevice pcDevice = manager.find("PC");
            assertNotNull(pcDevice);
            assertEquals("PC", pcDevice.getId());
        }
    }

    @Nested
    @DisplayName("deviceResolver tests")
    class DeviceResolverTests {

        @Test
        @DisplayName("Should create device resolver with manager")
        void shouldCreateDeviceResolver() {
            List<IDevice> deviceList = configurations.deviceList();
            IDeviceManager manager = configurations.deviceManager(deviceList);
            IDeviceResolver resolver = configurations.deviceResolver(manager);

            assertNotNull(resolver);
        }
    }

    @Nested
    @DisplayName("sessionResolver tests")
    class SessionResolverTests {

        @Test
        @DisplayName("Should create session context resolver")
        void shouldCreateSessionResolver() {
            ISessionContextResolver resolver = configurations.sessionResolver();

            assertNotNull(resolver);
        }
    }
}
