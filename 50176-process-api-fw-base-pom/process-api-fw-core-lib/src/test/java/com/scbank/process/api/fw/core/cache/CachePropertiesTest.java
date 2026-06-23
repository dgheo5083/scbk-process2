package com.scbank.process.api.fw.core.cache;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.cache.CacheProperties.CacheConfig;

/**
 * CacheProperties Test Class
 */
class CachePropertiesTest {

    @Nested
    @DisplayName("BEAN_ID constant tests")
    class BeanIdTests {

        @Test
        @DisplayName("Should have correct BEAN_ID value")
        void shouldHaveCorrectBeanIdValue() {
            assertEquals("csl.cache.properties", CacheProperties.BEAN_ID);
        }
    }

    @Nested
    @DisplayName("CacheConfig record tests")
    class CacheConfigRecordTests {

        @Test
        @DisplayName("Should create CacheConfig with valid ttl and maxSize")
        void shouldCreateCacheConfigWithValidValues() {
            CacheConfig config = new CacheConfig(600, 1000);

            assertEquals(600, config.ttl());
            assertEquals(1000, config.maxSize());
        }

        @Test
        @DisplayName("Should create CacheConfig with zero values")
        void shouldCreateCacheConfigWithZeroValues() {
            CacheConfig config = new CacheConfig(0, 0);

            assertEquals(0, config.ttl());
            assertEquals(0, config.maxSize());
        }

        @Test
        @DisplayName("Should create CacheConfig with negative values")
        void shouldCreateCacheConfigWithNegativeValues() {
            CacheConfig config = new CacheConfig(-1, -1);

            assertEquals(-1, config.ttl());
            assertEquals(-1, config.maxSize());
        }

        @Test
        @DisplayName("Should have correct equals and hashCode")
        void shouldHaveCorrectEqualsAndHashCode() {
            CacheConfig config1 = new CacheConfig(600, 1000);
            CacheConfig config2 = new CacheConfig(600, 1000);
            CacheConfig config3 = new CacheConfig(300, 500);

            assertEquals(config1, config2);
            assertEquals(config1.hashCode(), config2.hashCode());
            assertNotEquals(config1, config3);
        }

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            CacheConfig config = new CacheConfig(600, 1000);

            assertNotNull(config.toString());
            assertTrue(config.toString().contains("600"));
            assertTrue(config.toString().contains("1000"));
        }
    }

    @Nested
    @DisplayName("Target map tests")
    class TargetMapTests {

        @Test
        @DisplayName("Should initialize with empty HashMap")
        void shouldInitializeWithEmptyHashMap() {
            CacheProperties properties = new CacheProperties();

            assertNotNull(properties.getTarget());
            assertTrue(properties.getTarget().isEmpty());
        }

        @Test
        @DisplayName("Should set and get target map")
        void shouldSetAndGetTargetMap() {
            CacheProperties properties = new CacheProperties();
            Map<String, CacheConfig> targetMap = new HashMap<>();
            targetMap.put("sampleCache", new CacheConfig(600, 1000));
            targetMap.put("anotherCache", new CacheConfig(300, 500));

            properties.setTarget(targetMap);

            assertEquals(targetMap, properties.getTarget());
            assertEquals(2, properties.getTarget().size());
        }

        @Test
        @DisplayName("Should add cache config to target map")
        void shouldAddCacheConfigToTargetMap() {
            CacheProperties properties = new CacheProperties();
            CacheConfig config = new CacheConfig(600, 1000);

            properties.getTarget().put("testCache", config);

            assertEquals(1, properties.getTarget().size());
            assertEquals(config, properties.getTarget().get("testCache"));
        }

        @Test
        @DisplayName("Should retrieve cache config by name")
        void shouldRetrieveCacheConfigByName() {
            CacheProperties properties = new CacheProperties();
            CacheConfig config1 = new CacheConfig(600, 1000);
            CacheConfig config2 = new CacheConfig(300, 500);

            properties.getTarget().put("cache1", config1);
            properties.getTarget().put("cache2", config2);

            assertEquals(config1, properties.getTarget().get("cache1"));
            assertEquals(config2, properties.getTarget().get("cache2"));
        }

        @Test
        @DisplayName("Should return null for non-existent cache name")
        void shouldReturnNullForNonExistentCacheName() {
            CacheProperties properties = new CacheProperties();

            assertNull(properties.getTarget().get("nonExistent"));
        }

        @Test
        @DisplayName("Should replace existing cache config")
        void shouldReplaceExistingCacheConfig() {
            CacheProperties properties = new CacheProperties();
            CacheConfig config1 = new CacheConfig(600, 1000);
            CacheConfig config2 = new CacheConfig(300, 500);

            properties.getTarget().put("testCache", config1);
            properties.getTarget().put("testCache", config2);

            assertEquals(1, properties.getTarget().size());
            assertEquals(config2, properties.getTarget().get("testCache"));
        }

        @Test
        @DisplayName("Should remove cache config from target map")
        void shouldRemoveCacheConfigFromTargetMap() {
            CacheProperties properties = new CacheProperties();
            CacheConfig config = new CacheConfig(600, 1000);

            properties.getTarget().put("testCache", config);
            properties.getTarget().remove("testCache");

            assertTrue(properties.getTarget().isEmpty());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself (reference equality)")
        void shouldBeEqualToItself() {
            CacheProperties props = new CacheProperties();

            assertEquals(props, props);
            assertEquals(props.hashCode(), props.hashCode());
        }

        @Test
        @DisplayName("Should not be equal for different instances (no @EqualsAndHashCode)")
        void shouldNotBeEqualForDifferentInstances() {
            CacheProperties props1 = new CacheProperties();
            CacheProperties props2 = new CacheProperties();

            // CacheProperties does not have @EqualsAndHashCode, so different instances are not equal
            assertNotEquals(props1, props2);
        }
    }

    @Nested
    @DisplayName("ToString tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return non-null toString")
        void shouldReturnNonNullToString() {
            CacheProperties properties = new CacheProperties();

            assertNotNull(properties.toString());
        }

        @Test
        @DisplayName("Should return Object.toString format (no @ToString annotation)")
        void shouldReturnObjectToStringFormat() {
            CacheProperties properties = new CacheProperties();
            properties.getTarget().put("testCache", new CacheConfig(600, 1000));

            String toStringResult = properties.toString();

            // CacheProperties does not have @ToString, so it uses Object.toString()
            // which returns className@hashCode format
            assertNotNull(toStringResult);
            assertTrue(toStringResult.contains("CacheProperties"));
        }
    }
}
