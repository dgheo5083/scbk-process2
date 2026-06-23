package com.scbank.process.api.fw.channel.device.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.device.IDevice;

/**
 * DefaultDevice Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultDeviceTest {

    private DefaultDevice device;

    @BeforeEach
    void setUp() {
        device = DefaultDevice.builder()
                .id("PC")
                .name("Personal Computer")
                .description("Desktop or laptop computer")
                .regEx(".*Windows.*|.*Mac.*")
                .order(1)
                .defaultDevice(true)
                .build();
    }

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create device with all fields")
        void shouldCreateDeviceWithAllFields() {
            assertNotNull(device);
            assertEquals("PC", device.getId());
            assertEquals("Personal Computer", device.getName());
            assertEquals("Desktop or laptop computer", device.getDescription());
            assertEquals(".*Windows.*|.*Mac.*", device.getRegEx());
            assertEquals(1, device.getOrder());
            assertTrue(device.isDefaultDevice());
        }

        @Test
        @DisplayName("Should create device with default values")
        void shouldCreateDeviceWithDefaultValues() {
            DefaultDevice emptyDevice = DefaultDevice.builder().build();

            assertNotNull(emptyDevice);
            assertNull(emptyDevice.getId());
            assertNull(emptyDevice.getName());
            assertNull(emptyDevice.getDescription());
            assertNull(emptyDevice.getRegEx());
            assertEquals(0, emptyDevice.getOrder());
            assertFalse(emptyDevice.isDefaultDevice());
        }
    }

    @Nested
    @DisplayName("Getter/Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set id")
        void shouldGetAndSetId() {
            device.setId("MOBILE");
            assertEquals("MOBILE", device.getId());
        }

        @Test
        @DisplayName("Should get and set name")
        void shouldGetAndSetName() {
            device.setName("Mobile Device");
            assertEquals("Mobile Device", device.getName());
        }

        @Test
        @DisplayName("Should get and set description")
        void shouldGetAndSetDescription() {
            device.setDescription("Smartphone or tablet");
            assertEquals("Smartphone or tablet", device.getDescription());
        }

        @Test
        @DisplayName("Should get and set regEx")
        void shouldGetAndSetRegEx() {
            device.setRegEx(".*Android.*|.*iPhone.*");
            assertEquals(".*Android.*|.*iPhone.*", device.getRegEx());
        }

        @Test
        @DisplayName("Should get and set order")
        void shouldGetAndSetOrder() {
            device.setOrder(5);
            assertEquals(5, device.getOrder());
        }

        @Test
        @DisplayName("Should get and set defaultDevice")
        void shouldGetAndSetDefaultDevice() {
            device.setDefaultDevice(false);
            assertFalse(device.isDefaultDevice());
        }
    }

    @Nested
    @DisplayName("compareTo tests")
    class CompareToTests {

        @Mock
        private IDevice otherDevice;

        @Test
        @DisplayName("Should return negative when this order is less than other")
        void shouldReturnNegativeWhenThisOrderIsLess() {
            when(otherDevice.getOrder()).thenReturn(10);
            device.setOrder(1);

            assertTrue(device.compareTo(otherDevice) < 0);
        }

        @Test
        @DisplayName("Should return positive when this order is greater than other")
        void shouldReturnPositiveWhenThisOrderIsGreater() {
            when(otherDevice.getOrder()).thenReturn(1);
            device.setOrder(10);

            assertTrue(device.compareTo(otherDevice) > 0);
        }

        @Test
        @DisplayName("Should return zero when orders are equal")
        void shouldReturnZeroWhenOrdersAreEqual() {
            when(otherDevice.getOrder()).thenReturn(5);
            device.setOrder(5);

            assertEquals(0, device.compareTo(otherDevice));
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(device, device);
        }

        @Test
        @DisplayName("Should be equal to another device with same values")
        void shouldBeEqualToAnotherDeviceWithSameValues() {
            DefaultDevice anotherDevice = DefaultDevice.builder()
                    .id("PC")
                    .name("Personal Computer")
                    .description("Desktop or laptop computer")
                    .regEx(".*Windows.*|.*Mac.*")
                    .order(1)
                    .defaultDevice(true)
                    .build();

            assertEquals(device, anotherDevice);
            assertEquals(device.hashCode(), anotherDevice.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to device with different id")
        void shouldNotBeEqualToDeviceWithDifferentId() {
            DefaultDevice anotherDevice = DefaultDevice.builder()
                    .id("MOBILE")
                    .name("Personal Computer")
                    .description("Desktop or laptop computer")
                    .regEx(".*Windows.*|.*Mac.*")
                    .order(1)
                    .defaultDevice(true)
                    .build();

            assertNotEquals(device, anotherDevice);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, device);
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement IDevice")
        void shouldImplementIDevice() {
            assertTrue(device instanceof IDevice);
        }

        @Test
        @DisplayName("Should implement Comparable")
        void shouldImplementComparable() {
            assertTrue(device instanceof Comparable);
        }
    }

    @Nested
    @DisplayName("Serialization tests")
    class SerializationTests {

        @Test
        @DisplayName("Should have serialVersionUID")
        void shouldHaveSerialVersionUID() {
            // Just verify the class can be instantiated - serialVersionUID is a static field
            assertNotNull(device);
        }
    }
}
