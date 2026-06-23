package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link JsonSessionMetadataRegistry} 단위 테스트
 */
@DisplayName("JsonSessionMetadataRegistry 테스트")
class JsonSessionMetadataRegistryTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("JSON InputStream으로 생성할 수 있다")
        void createWithJsonInputStream() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            // when
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // then
            assertNotNull(registry);
        }

        @Test
        @DisplayName("빈 JSON으로 생성할 수 있다")
        void createWithEmptyJson() {
            // given
            String json = "{}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            // when
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // then
            assertNotNull(registry);
        }

        @Test
        @DisplayName("유효하지 않은 JSON으로 생성 시 예외가 발생한다")
        void throwsExceptionForInvalidJson() {
            // given
            String invalidJson = "not a valid json";
            InputStream is = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));

            // then
            assertThrows(RuntimeException.class, () -> new JsonSessionMetadataRegistry(is));
        }
    }

    @Nested
    @DisplayName("getMetadata 메서드 테스트")
    class GetMetadataMethodTests {

        @Test
        @DisplayName("존재하는 키의 메타데이터를 조회할 수 있다")
        void canGetExistingMetadata() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<SessionMetadataInfo> result = registry.getMetadata("testKey");

            // then
            assertTrue(result.isPresent());
            assertEquals("v3Type", result.get().getV3());
            assertEquals("v4Type", result.get().getV4());
        }

        @Test
        @DisplayName("존재하지 않는 키는 빈 Optional을 반환한다")
        void returnsEmptyOptionalForNonExistentKey() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<SessionMetadataInfo> result = registry.getMetadata("nonExistentKey");

            // then
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getV3Type 메서드 테스트")
    class GetV3TypeMethodTests {

        @Test
        @DisplayName("존재하는 키의 v3 타입을 조회할 수 있다")
        void canGetV3Type() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<String> result = registry.getV3Type("testKey");

            // then
            assertTrue(result.isPresent());
            assertEquals("v3Type", result.get());
        }

        @Test
        @DisplayName("존재하지 않는 키는 빈 Optional을 반환한다")
        void returnsEmptyOptionalForNonExistentKey() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<String> result = registry.getV3Type("nonExistentKey");

            // then
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getV4Type 메서드 테스트")
    class GetV4TypeMethodTests {

        @Test
        @DisplayName("존재하는 키의 v4 타입을 조회할 수 있다")
        void canGetV4Type() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<String> result = registry.getV4Type("testKey");

            // then
            assertTrue(result.isPresent());
            assertEquals("v4Type", result.get());
        }
    }

    @Nested
    @DisplayName("getElementType 메서드 테스트")
    class GetElementTypeMethodTests {

        @Test
        @DisplayName("element가 있는 키의 element 타입을 조회할 수 있다")
        void canGetElementType() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\",\"element\":{\"v3\":\"elemV3\",\"v4\":\"elemV4\"}}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<SessionMetadataInfo.ElementTypeMapping> result = registry.getElementType("testKey");

            // then
            assertTrue(result.isPresent());
            assertEquals("elemV3", result.get().getV3());
            assertEquals("elemV4", result.get().getV4());
        }

        @Test
        @DisplayName("element가 없는 키는 빈 Optional을 반환한다")
        void returnsEmptyOptionalWhenNoElement() {
            // given
            String json = "{\"testKey\":{\"v3\":\"v3Type\",\"v4\":\"v4Type\"}}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // when
            Optional<SessionMetadataInfo.ElementTypeMapping> result = registry.getElementType("testKey");

            // then
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionMetadataRegistry 인터페이스를 구현한다")
        void implementsISessionMetadataRegistry() {
            // given
            String json = "{}";
            InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            // when
            JsonSessionMetadataRegistry registry = new JsonSessionMetadataRegistry(is);

            // then
            assertInstanceOf(ISessionMetadataRegistry.class, registry);
        }
    }
}
