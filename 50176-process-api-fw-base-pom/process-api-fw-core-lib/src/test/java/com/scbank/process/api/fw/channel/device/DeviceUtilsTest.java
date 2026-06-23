package com.scbank.process.api.fw.channel.device;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;

/**
 * DeviceUtils Test Class
 */
@ExtendWith(MockitoExtension.class)
class DeviceUtilsTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private IDevice device;

    @Nested
    @DisplayName("CURRENT_DEVICE_ATTR constant tests")
    class ConstantTests {

        @Test
        @DisplayName("Should have correct CURRENT_DEVICE_ATTR value")
        void shouldHaveCorrectCurrentDeviceAttrValue() {
            assertEquals("ib.current.device", DeviceUtils.CURRENT_DEVICE_ATTR);
        }
    }

    @Nested
    @DisplayName("getCurrentDevice tests")
    class GetCurrentDeviceTests {

        @Test
        @DisplayName("Should return device from request attribute")
        void shouldReturnDeviceFromRequestAttribute() {
            when(request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTR)).thenReturn(device);

            IDevice result = DeviceUtils.getCurrentDevice(request);

            assertSame(device, result);
            verify(request).getAttribute(DeviceUtils.CURRENT_DEVICE_ATTR);
        }

        @Test
        @DisplayName("Should return null when no device set")
        void shouldReturnNullWhenNoDeviceSet() {
            when(request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTR)).thenReturn(null);

            IDevice result = DeviceUtils.getCurrentDevice(request);

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("setCurrentDevice tests")
    class SetCurrentDeviceTests {

        @Test
        @DisplayName("Should set device as request attribute")
        void shouldSetDeviceAsRequestAttribute() {
            DeviceUtils.setCurrentDevice(request, device);

            verify(request).setAttribute(DeviceUtils.CURRENT_DEVICE_ATTR, device);
        }

        @Test
        @DisplayName("Should allow setting null device")
        void shouldAllowSettingNullDevice() {
            DeviceUtils.setCurrentDevice(request, null);

            verify(request).setAttribute(DeviceUtils.CURRENT_DEVICE_ATTR, null);
        }
    }
}
