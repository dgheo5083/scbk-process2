package com.scbank.process.api.fw.channel.device.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * DefaultDeviceResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultDeviceResolverTest {

    private DefaultDeviceResolver deviceResolver;

    @Mock
    private IDeviceManager deviceManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private IDevice pcDevice;

    @Mock
    private IDevice mobileDevice;

    @Mock
    private IDevice defaultDevice;

    @BeforeEach
    void setUp() {
        deviceResolver = new DefaultDeviceResolver(deviceManager);
    }

    @Nested
    @DisplayName("DEFAULT_PARAMETER_NAME constant tests")
    class ConstantTests {

        @Test
        @DisplayName("Should have correct DEFAULT_PARAMETER_NAME value")
        void shouldHaveCorrectDefaultParameterNameValue() {
            assertEquals("device", DefaultDeviceResolver.DEFAULT_PARAMETER_NAME);
        }
    }

    @Nested
    @DisplayName("resolve with parameter tests")
    class ResolveWithParameterTests {

        @Test
        @DisplayName("Should return device by parameter when parameter is provided")
        void shouldReturnDeviceByParameterWhenParameterIsProvided() {
            when(request.getParameter("device")).thenReturn("PC");
            when(deviceManager.find("PC")).thenReturn(pcDevice);

            IDevice result = deviceResolver.resolve(request);

            assertSame(pcDevice, result);
        }

        @Test
        @DisplayName("Should use custom parameter name when set")
        void shouldUseCustomParameterNameWhenSet() {
            deviceResolver.setParameterName("customDevice");
            when(request.getParameter("customDevice")).thenReturn("MOBILE");
            when(deviceManager.find("MOBILE")).thenReturn(mobileDevice);

            IDevice result = deviceResolver.resolve(request);

            assertSame(mobileDevice, result);
        }
    }

    @Nested
    @DisplayName("resolve with User-Agent tests")
    class ResolveWithUserAgentTests {

        @Test
        @DisplayName("Should return default device when User-Agent is empty")
        void shouldReturnDefaultDeviceWhenUserAgentIsEmpty() {
            when(request.getParameter("device")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("");
            when(deviceManager.getDefaultDevice()).thenReturn(defaultDevice);

            IDevice result = deviceResolver.resolve(request);

            assertSame(defaultDevice, result);
        }

        @Test
        @DisplayName("Should return default device when User-Agent is null")
        void shouldReturnDefaultDeviceWhenUserAgentIsNull() {
            when(request.getParameter("device")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn(null);
            when(deviceManager.getDefaultDevice()).thenReturn(defaultDevice);

            IDevice result = deviceResolver.resolve(request);

            assertSame(defaultDevice, result);
        }

        @Test
        @DisplayName("Should match device by User-Agent regex")
        void shouldMatchDeviceByUserAgentRegex() {
            when(request.getParameter("device")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0)");
            when(deviceManager.getDefaultDevice()).thenReturn(defaultDevice);
            when(deviceManager.getDeviceList()).thenReturn(Arrays.asList(pcDevice));
            when(pcDevice.getRegEx()).thenReturn(".*Windows.*");
            when(pcDevice.getOrder()).thenReturn(1);
            when(pcDevice.getId()).thenReturn("PC");

            IDevice result = deviceResolver.resolve(request);

            assertSame(pcDevice, result);
        }

        @Test
        @DisplayName("Should return default device when no regex matches")
        void shouldReturnDefaultDeviceWhenNoRegexMatches() {
            when(request.getParameter("device")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("CustomBrowser/1.0");
            when(deviceManager.getDefaultDevice()).thenReturn(defaultDevice);
            when(deviceManager.getDeviceList()).thenReturn(Arrays.asList(pcDevice));
            when(pcDevice.getRegEx()).thenReturn(".*Windows.*");
            when(pcDevice.getOrder()).thenReturn(1);
            when(pcDevice.getId()).thenReturn("PC");

            IDevice result = deviceResolver.resolve(request);

            assertSame(defaultDevice, result);
        }
    }

    @Nested
    @DisplayName("resolve validation tests")
    class ResolveValidationTests {

        @Test
        @DisplayName("Should throw when deviceManager is null")
        void shouldThrowWhenDeviceManagerIsNull() {
            DefaultDeviceResolver resolverWithNullManager = new DefaultDeviceResolver(null);

            assertThrows(IllegalArgumentException.class, () -> {
                resolverWithNullManager.resolve(request);
            });
        }
    }

    @Nested
    @DisplayName("setParameterName tests")
    class SetParameterNameTests {

        @Test
        @DisplayName("Should set custom parameter name")
        void shouldSetCustomParameterName() {
            deviceResolver.setParameterName("myDeviceParam");

            when(request.getParameter("myDeviceParam")).thenReturn("PC");
            when(deviceManager.find("PC")).thenReturn(pcDevice);

            IDevice result = deviceResolver.resolve(request);

            assertSame(pcDevice, result);
            verify(request, atLeastOnce()).getParameter("myDeviceParam");
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IDeviceResolver")
        void shouldImplementIDeviceResolver() {
            assertTrue(deviceResolver instanceof IDeviceResolver);
        }
    }

    @Nested
    @DisplayName("Multiple regex pattern tests")
    class MultipleRegexPatternTests {

        @Test
        @DisplayName("Should match with pipe-separated regex patterns")
        void shouldMatchWithPipeSeparatedRegexPatterns() {
            when(request.getParameter("device")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (iPhone)");
            when(deviceManager.getDefaultDevice()).thenReturn(defaultDevice);
            when(deviceManager.getDeviceList()).thenReturn(Arrays.asList(mobileDevice));
            when(mobileDevice.getRegEx()).thenReturn(".*Android.*|.*iPhone.*");
            when(mobileDevice.getOrder()).thenReturn(1);
            when(mobileDevice.getId()).thenReturn("MOBILE");

            IDevice result = deviceResolver.resolve(request);

            assertSame(mobileDevice, result);
        }
    }

    @Nested
    @DisplayName("Device ordering tests")
    class DeviceOrderingTests {

        @Test
        @DisplayName("Should check devices in order")
        void shouldCheckDevicesInOrder() {
            when(request.getParameter("device")).thenReturn(null);
            when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Windows NT 10.0)");
            when(deviceManager.getDefaultDevice()).thenReturn(defaultDevice);
            when(deviceManager.getDeviceList()).thenReturn(Arrays.asList(mobileDevice, pcDevice));

            when(mobileDevice.getOrder()).thenReturn(2);
            when(mobileDevice.getRegEx()).thenReturn(".*Android.*");
            when(mobileDevice.getId()).thenReturn("MOBILE");

            when(pcDevice.getOrder()).thenReturn(1);
            when(pcDevice.getRegEx()).thenReturn(".*Windows.*");
            when(pcDevice.getId()).thenReturn("PC");

            IDevice result = deviceResolver.resolve(request);

            assertSame(pcDevice, result);
        }
    }
}
