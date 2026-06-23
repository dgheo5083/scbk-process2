package com.scbank.process.api.fw.batch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * BatchProperties Test Class
 */
class BatchPropertiesTest {

    private BatchProperties properties;

    @BeforeEach
    void setUp() {
        properties = new BatchProperties();
    }

    @Nested
    @DisplayName("enabled property tests")
    class EnabledTests {

        @Test
        @DisplayName("Should return default false for enabled")
        void shouldReturnDefaultFalseForEnabled() {
            assertFalse(properties.isEnabled());
        }

        @Test
        @DisplayName("Should set and get enabled")
        void shouldSetAndGetEnabled() {
            properties.setEnabled(true);
            assertTrue(properties.isEnabled());
        }
    }

    @Nested
    @DisplayName("configLocation property tests")
    class ConfigLocationTests {

        @Test
        @DisplayName("Should return null for default configLocation")
        void shouldReturnNullForDefaultConfigLocation() {
            assertNull(properties.getConfigLocation());
        }

        @Test
        @DisplayName("Should set and get configLocation")
        void shouldSetAndGetConfigLocation() {
            String location = "classpath:batch/*.xml";
            properties.setConfigLocation(location);
            assertEquals(location, properties.getConfigLocation());
        }
    }

    @Nested
    @DisplayName("autoStartup property tests")
    class AutoStartupTests {

        @Test
        @DisplayName("Should return default false for autoStartup")
        void shouldReturnDefaultFalseForAutoStartup() {
            assertFalse(properties.isAutoStartup());
        }

        @Test
        @DisplayName("Should set and get autoStartup")
        void shouldSetAndGetAutoStartup() {
            properties.setAutoStartup(true);
            assertTrue(properties.isAutoStartup());
        }
    }

    @Nested
    @DisplayName("Lombok generated methods tests")
    class LombokTests {

        @Test
        @DisplayName("Should generate equals and hashCode")
        void shouldGenerateEqualsAndHashCode() {
            BatchProperties props1 = new BatchProperties();
            BatchProperties props2 = new BatchProperties();

            assertEquals(props1, props2);
            assertEquals(props1.hashCode(), props2.hashCode());

            props1.setEnabled(true);
            assertNotEquals(props1, props2);
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            properties.setEnabled(true);
            properties.setConfigLocation("test-location");
            properties.setAutoStartup(true);

            String toString = properties.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("enabled=true"));
            assertTrue(toString.contains("configLocation=test-location"));
            assertTrue(toString.contains("autoStartup=true"));
        }
    }
}
