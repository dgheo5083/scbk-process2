package com.scbank.process.api.fw.core.cache;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * DefaultCacheManager Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultCacheManagerTest {

    @Mock
    private CacheManager mockCacheManager;

    @Mock
    private Cache mockCache;

    private DefaultCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new DefaultCacheManager(mockCacheManager);
    }

    @Nested
    @DisplayName("get() method tests")
    class GetMethodTests {

        @Test
        @DisplayName("Should get value from cache successfully")
        void shouldGetValueFromCacheSuccessfully() {
            String cacheName = "testCache";
            String key = "testKey";
            String expectedValue = "testValue";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);
            when(mockCache.get(key, String.class)).thenReturn(expectedValue);

            String result = cacheManager.get(cacheName, key, String.class);

            assertEquals(expectedValue, result);
            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).get(key, String.class);
        }

        @SuppressWarnings("unchecked")
		@Test
        @DisplayName("Should return null when cache does not exist")
        void shouldReturnNullWhenCacheDoesNotExist() {
            String cacheName = "nonExistentCache";
            String key = "testKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(null);

            String result = cacheManager.get(cacheName, key, String.class);

            assertNull(result);
            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache, never()).get(anyString(), any(Class.class));
        }

        @Test
        @DisplayName("Should return null when key does not exist")
        void shouldReturnNullWhenKeyDoesNotExist() {
            String cacheName = "testCache";
            String key = "nonExistentKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);
            when(mockCache.get(key, String.class)).thenReturn(null);

            String result = cacheManager.get(cacheName, key, String.class);

            assertNull(result);
            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).get(key, String.class);
        }

        @Test
        @DisplayName("Should get Integer value from cache")
        void shouldGetIntegerValueFromCache() {
            String cacheName = "testCache";
            String key = "intKey";
            Integer expectedValue = 12345;

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);
            when(mockCache.get(key, Integer.class)).thenReturn(expectedValue);

            Integer result = cacheManager.get(cacheName, key, Integer.class);

            assertEquals(expectedValue, result);
        }
    }

    @Nested
    @DisplayName("put() method tests")
    class PutMethodTests {

        @Test
        @DisplayName("Should put value into cache successfully")
        void shouldPutValueIntoCacheSuccessfully() {
            String cacheName = "testCache";
            String key = "testKey";
            String value = "testValue";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);

            assertDoesNotThrow(() -> cacheManager.put(cacheName, key, value));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).put(key, value);
        }

        @Test
        @DisplayName("Should throw NullPointerException when cache does not exist")
        void shouldThrowExceptionWhenCacheDoesNotExist() {
            String cacheName = "nonExistentCache";
            String key = "testKey";
            String value = "testValue";

            when(mockCacheManager.getCache(cacheName)).thenReturn(null);

            assertThrows(NullPointerException.class, () -> cacheManager.put(cacheName, key, value));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache, never()).put(any(), any());
        }

        @Test
        @DisplayName("Should put null value into cache")
        void shouldPutNullValueIntoCache() {
            String cacheName = "testCache";
            String key = "testKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);

            assertDoesNotThrow(() -> cacheManager.put(cacheName, key, null));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).put(key, null);
        }

        @Test
        @DisplayName("Should put complex object into cache")
        void shouldPutComplexObjectIntoCache() {
            String cacheName = "testCache";
            String key = "objectKey";
            Map<String, Object> complexObject = new HashMap<>();
            complexObject.put("field1", "value1");
            complexObject.put("field2", 123);

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);

            assertDoesNotThrow(() -> cacheManager.put(cacheName, key, complexObject));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).put(key, complexObject);
        }
    }

    @Nested
    @DisplayName("evict() method tests")
    class EvictMethodTests {

        @Test
        @DisplayName("Should evict key from cache successfully")
        void shouldEvictKeyFromCacheSuccessfully() {
            String cacheName = "testCache";
            String key = "testKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);

            assertDoesNotThrow(() -> cacheManager.evict(cacheName, key));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).evict(key);
        }

        @Test
        @DisplayName("Should not throw exception when cache does not exist")
        void shouldNotThrowExceptionWhenCacheDoesNotExist() {
            String cacheName = "nonExistentCache";
            String key = "testKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(null);

            assertDoesNotThrow(() -> cacheManager.evict(cacheName, key));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache, never()).evict(any());
        }

        @Test
        @DisplayName("Should evict key even if key does not exist")
        void shouldEvictKeyEvenIfKeyDoesNotExist() {
            String cacheName = "testCache";
            String key = "nonExistentKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);

            assertDoesNotThrow(() -> cacheManager.evict(cacheName, key));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).evict(key);
        }
    }

    @Nested
    @DisplayName("clear() method tests")
    class ClearMethodTests {

        @Test
        @DisplayName("Should clear cache successfully")
        void shouldClearCacheSuccessfully() {
            String cacheName = "testCache";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);

            assertDoesNotThrow(() -> cacheManager.clear(cacheName));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).clear();
        }

        @Test
        @DisplayName("Should not throw exception when cache does not exist")
        void shouldNotThrowExceptionWhenCacheDoesNotExist() {
            String cacheName = "nonExistentCache";

            when(mockCacheManager.getCache(cacheName)).thenReturn(null);

            assertDoesNotThrow(() -> cacheManager.clear(cacheName));

            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache, never()).clear();
        }
    }

    @Nested
    @DisplayName("contains() method tests")
    class ContainsMethodTests {

        @Test
        @DisplayName("Should return true when key exists in cache")
        void shouldReturnTrueWhenKeyExistsInCache() {
            String cacheName = "testCache";
            String key = "testKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);
            when(mockCache.get(key)).thenReturn(mock(Cache.ValueWrapper.class));

            boolean result = cacheManager.contains(cacheName, key);

            assertTrue(result);
            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).get(key);
        }

        @Test
        @DisplayName("Should return false when cache does not exist")
        void shouldReturnFalseWhenCacheDoesNotExist() {
            String cacheName = "nonExistentCache";
            String key = "testKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(null);

            boolean result = cacheManager.contains(cacheName, key);

            assertFalse(result);
            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache, never()).get(any());
        }

        @Test
        @DisplayName("Should return false when key does not exist in cache")
        void shouldReturnFalseWhenKeyDoesNotExistInCache() {
            String cacheName = "testCache";
            String key = "nonExistentKey";

            when(mockCacheManager.getCache(cacheName)).thenReturn(mockCache);
            when(mockCache.get(key)).thenReturn(null);

            boolean result = cacheManager.contains(cacheName, key);

            assertFalse(result);
            verify(mockCacheManager).getCache(cacheName);
            verify(mockCache).get(key);
        }
    }

    @Nested
    @DisplayName("getAll() method tests")
    class GetAllMethodTests {

        @Test
        @DisplayName("Should return null for getAll method")
        void shouldReturnNullForGetAllMethod() {
            String cacheName = "testCache";

            Map<ICacheKey, String> result = cacheManager.getAll(cacheName, String.class);

            assertNull(result);
        }

        @Test
        @DisplayName("Should return null for different types")
        void shouldReturnNullForDifferentTypes() {
            String cacheName = "testCache";

            Map<ICacheKey, Integer> intResult = cacheManager.getAll(cacheName, Integer.class);
            Map<ICacheKey, Object> objResult = cacheManager.getAll(cacheName, Object.class);

            assertNull(intResult);
            assertNull(objResult);
        }
    }
}
