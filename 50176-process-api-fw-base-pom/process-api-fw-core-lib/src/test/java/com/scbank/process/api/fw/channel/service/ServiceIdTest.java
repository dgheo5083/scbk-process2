package com.scbank.process.api.fw.channel.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ServiceId Test Class
 */
class ServiceIdTest {

    private ServiceId serviceId;

    @BeforeEach
    void setUp() {
        serviceId = ServiceId.builder()
                .service("COMMON")
                .url("/api/common/login/userLogin")
                .build();
    }

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create ServiceId with all fields")
        void shouldCreateServiceIdWithAllFields() {
            assertNotNull(serviceId);
            assertEquals("COMMON", serviceId.getService());
            assertEquals("/api/common/login/userLogin", serviceId.getUrl());
        }

        @Test
        @DisplayName("Should create ServiceId with default values")
        void shouldCreateServiceIdWithDefaultValues() {
            ServiceId emptyServiceId = ServiceId.builder().build();

            assertNotNull(emptyServiceId);
            assertNull(emptyServiceId.getService());
            assertNull(emptyServiceId.getUrl());
        }
    }

    @Nested
    @DisplayName("Getter/Setter tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set service")
        void shouldGetAndSetService() {
            serviceId.setService("AUTH");
            assertEquals("AUTH", serviceId.getService());
        }

        @Test
        @DisplayName("Should get and set url")
        void shouldGetAndSetUrl() {
            serviceId.setUrl("/api/auth/logout");
            assertEquals("/api/auth/logout", serviceId.getUrl());
        }
    }

    @Nested
    @DisplayName("equals and hashCode tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(serviceId, serviceId);
        }

        @Test
        @DisplayName("Should be equal to another ServiceId with same values")
        void shouldBeEqualToAnotherServiceIdWithSameValues() {
            ServiceId anotherServiceId = ServiceId.builder()
                    .service("COMMON")
                    .url("/api/common/login/userLogin")
                    .build();

            assertEquals(serviceId, anotherServiceId);
            assertEquals(serviceId.hashCode(), anotherServiceId.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to ServiceId with different service")
        void shouldNotBeEqualToServiceIdWithDifferentService() {
            ServiceId anotherServiceId = ServiceId.builder()
                    .service("AUTH")
                    .url("/api/common/login/userLogin")
                    .build();

            assertNotEquals(serviceId, anotherServiceId);
        }

        @Test
        @DisplayName("Should not be equal to ServiceId with different url")
        void shouldNotBeEqualToServiceIdWithDifferentUrl() {
            ServiceId anotherServiceId = ServiceId.builder()
                    .service("COMMON")
                    .url("/api/common/logout")
                    .build();

            assertNotEquals(serviceId, anotherServiceId);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, serviceId);
        }
    }

    @Nested
    @DisplayName("Interface implementation tests")
    class InterfaceTests {

        @Test
        @DisplayName("Should implement Serializable")
        void shouldImplementSerializable() {
            assertTrue(serviceId instanceof Serializable);
        }
    }

    @Nested
    @DisplayName("toString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate meaningful toString")
        void shouldGenerateMeaningfulToString() {
            String toString = serviceId.toString();

            assertNotNull(toString);
            assertTrue(toString.contains("COMMON"));
            assertTrue(toString.contains("/api/common/login/userLogin"));
        }
    }
}
