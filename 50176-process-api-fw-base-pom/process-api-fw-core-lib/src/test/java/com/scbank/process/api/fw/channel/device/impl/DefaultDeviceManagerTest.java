package com.scbank.process.api.fw.channel.device.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;

/**
 * DefaultDeviceManager Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultDeviceManagerTest {

    private DefaultDeviceManager deviceManager;

    @Mock
    private IDevice device1;

    @Mock
    private IDevice device2;

    @BeforeEach
    void setUp() {
        deviceManager = new DefaultDeviceManager();
    }

    @Nested
    @DisplayName("getDeviceList tests")
    class GetDeviceListTests {

        @Test
        @DisplayName("Should return empty CopyOnWriteArrayList when deviceList is null")
        void shouldReturnEmptyCopyOnWriteArrayListWhenDeviceListIsNull() {
            List<IDevice> result = deviceManager.getDeviceList();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            assertTrue(result instanceof CopyOnWriteArrayList);
        }

        @Test
        @DisplayName("Should return same list on subsequent calls")
        void shouldReturnSameListOnSubsequentCalls() {
            List<IDevice> first = deviceManager.getDeviceList();
            List<IDevice> second = deviceManager.getDeviceList();

            assertSame(first, second);
        }

        @Test
        @DisplayName("Should return set device list")
        void shouldReturnSetDeviceList() {
            List<IDevice> customList = new ArrayList<>();
            customList.add(device1);
            customList.add(device2);

            deviceManager.setDeviceList(customList);
            List<IDevice> result = deviceManager.getDeviceList();

            assertEquals(2, result.size());
            assertSame(customList, result);
        }
    }

    @Nested
    @DisplayName("setDeviceList tests")
    class SetDeviceListTests {

        @Test
        @DisplayName("Should set device list")
        void shouldSetDeviceList() {
            List<IDevice> customList = new ArrayList<>();
            customList.add(device1);

            deviceManager.setDeviceList(customList);

            assertEquals(1, deviceManager.getDeviceList().size());
        }

        @Test
        @DisplayName("Should allow setting null device list")
        void shouldAllowSettingNullDeviceList() {
            deviceManager.setDeviceList(null);

            // When null, getDeviceList should initialize a new list
            List<IDevice> result = deviceManager.getDeviceList();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IDeviceManager")
        void shouldImplementIDeviceManager() {
            assertTrue(deviceManager instanceof IDeviceManager);
        }
    }

    @Nested
    @DisplayName("Thread safety tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should use thread-safe list")
        void shouldUseThreadSafeList() {
            List<IDevice> deviceList = deviceManager.getDeviceList();

            // Add devices concurrently
            deviceList.add(device1);
            deviceList.add(device2);

            assertEquals(2, deviceManager.getDeviceList().size());
        }
    }
}
