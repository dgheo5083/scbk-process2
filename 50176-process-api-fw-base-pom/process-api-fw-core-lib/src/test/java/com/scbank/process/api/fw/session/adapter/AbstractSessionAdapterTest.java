package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link AbstractSessionAdapter} 단위 테스트
 */
@DisplayName("AbstractSessionAdapter 테스트")
@ExtendWith(MockitoExtension.class)
class AbstractSessionAdapterTest {

    private TestSessionAdapter adapter;

    @Mock
    private ISessionMetadataRegistry metadataRegistry;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        adapter = new TestSessionAdapter(metadataRegistry, objectMapper);
    }

    /**
     * 테스트용 구체 클래스
     */
    private static class TestSessionAdapter extends AbstractSessionAdapter {

        private SessionMetadataInfo resolvedMetadata;

        public TestSessionAdapter(ISessionMetadataRegistry metadataRegistry) {
            super(metadataRegistry);
        }

        public TestSessionAdapter(ISessionMetadataRegistry metadataRegistry, ObjectMapper objectMapper) {
            super(metadataRegistry, objectMapper);
        }

        public void setResolvedMetadata(SessionMetadataInfo metadata) {
            this.resolvedMetadata = metadata;
        }

        @Override
        public boolean supports(String sessionKey, Object value) {
            return "testKey".equals(sessionKey);
        }

        @Override
        protected SessionMetadataInfo resolveMetadata(String sessionKey, Object value) {
            return resolvedMetadata;
        }

        // 테스트를 위해 protected 메서드 노출
        public Object callWrapRecursive(Object value, SessionMetadataInfo meta) {
            return wrapRecursive(value, meta);
        }

        public Object callUnwrapRecursive(Object value, SessionMetadataInfo meta) {
            return unwrapRecursive(value, meta);
        }
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("ISessionMetadataRegistry만으로 생성할 수 있다")
        void createWithMetadataRegistryOnly() {
            // when
            TestSessionAdapter newAdapter = new TestSessionAdapter(metadataRegistry);

            // then
            assertNotNull(newAdapter);
        }

        @Test
        @DisplayName("ISessionMetadataRegistry와 ObjectMapper로 생성할 수 있다")
        void createWithMetadataRegistryAndObjectMapper() {
            // when
            TestSessionAdapter newAdapter = new TestSessionAdapter(metadataRegistry, objectMapper);

            // then
            assertNotNull(newAdapter);
        }
    }

    @Nested
    @DisplayName("wrap 메서드 테스트")
    class WrapMethodTests {

        @Test
        @DisplayName("null 값은 그대로 반환한다")
        void returnsNullAsIs() {
            // when
            Object result = adapter.wrap("testKey", null);

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("메타데이터가 null이면 원본 값을 반환한다")
        void returnsOriginalWhenMetadataIsNull() {
            // given
            adapter.setResolvedMetadata(null);

            // when
            Object result = adapter.wrap("testKey", "testValue");

            // then
            assertEquals("testValue", result);
        }

        @Test
        @DisplayName("메타데이터가 있으면 wrap을 수행한다")
        void wrapsWithMetadata() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();
            meta.setV3("v3Type");
            meta.setV4("v4Type");
            adapter.setResolvedMetadata(meta);

            Map<String, Object> value = new HashMap<>();
            value.put("field", "fieldValue");

            // when
            Object result = adapter.wrap("testKey", value);

            // then
            assertNotNull(result);
            assertInstanceOf(Map.class, result);
        }
    }

    @Nested
    @DisplayName("unwrap 메서드 테스트")
    class UnwrapMethodTests {

        @Test
        @DisplayName("null 값은 그대로 반환한다")
        void returnsNullAsIs() {
            // when
            Object result = adapter.unwrap("testKey", null);

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("메타데이터가 null이면 원본 값을 반환한다")
        void returnsOriginalWhenMetadataIsNull() {
            // given
            adapter.setResolvedMetadata(null);

            // when
            Object result = adapter.unwrap("testKey", "testValue");

            // then
            assertEquals("testValue", result);
        }
    }

    @Nested
    @DisplayName("wrapRecursive 메서드 테스트")
    class WrapRecursiveMethodTests {

        @Test
        @DisplayName("null 값은 null을 반환한다")
        void returnsNullForNullValue() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();
            meta.setV3("v3Type");
            meta.setV4("v4Type");

            // when
            Object result = adapter.callWrapRecursive(null, meta);

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("Map을 래핑한다")
        void wrapsMap() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();
            meta.setV3("v3Type");
            meta.setV4("v4Type");

            Map<String, Object> value = new HashMap<>();
            value.put("field", "fieldValue");

            // when
            Object result = adapter.callWrapRecursive(value, meta);

            // then
            assertInstanceOf(Map.class, result);
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) result;
            assertNotNull(resultMap.get(SessionAdapterConstants.FLD_NM_ADAPTER));
        }

        @Test
        @DisplayName("List를 래핑한다")
        void wrapsList() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();
            meta.setV3("v3Type");
            meta.setV4("v4Type");

            SessionMetadataInfo.ElementTypeMapping elementMeta = new SessionMetadataInfo.ElementTypeMapping();
            elementMeta.setV3("elemV3");
            elementMeta.setV4("elemV4");
            meta.setElement(elementMeta);

            Map<String, Object> item = new HashMap<>();
            item.put("field", "value");
            List<Object> value = Arrays.asList(item);

            // when
            Object result = adapter.callWrapRecursive(value, meta);

            // then
            assertInstanceOf(List.class, result);
        }

        @Test
        @DisplayName("element 메타가 없는 List는 그대로 반환한다")
        void returnsListAsIsWhenNoElementMeta() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();
            meta.setV3("v3Type");
            meta.setV4("v4Type");
            // element가 없음

            List<Object> value = Arrays.asList("item1", "item2");

            // when
            Object result = adapter.callWrapRecursive(value, meta);

            // then
            assertEquals(value, result);
        }
    }

    @Nested
    @DisplayName("unwrapRecursive 메서드 테스트")
    class UnwrapRecursiveMethodTests {

        @Test
        @DisplayName("null 값은 null을 반환한다")
        void returnsNullForNullValue() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();

            // when
            Object result = adapter.callUnwrapRecursive(null, meta);

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("null 메타데이터로 호출하면 원본 값을 반환한다")
        void returnsOriginalForNullMeta() {
            // when
            Object result = adapter.callUnwrapRecursive("testValue", null);

            // then
            assertEquals("testValue", result);
        }

        @Test
        @DisplayName("Map에서 _adapter_ 메타를 제거한다")
        void removesAdapterMetaFromMap() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();
            meta.setV3("v3Type");
            meta.setV4("java.util.HashMap");

            Map<String, Object> value = new HashMap<>();
            Map<String, String> adapterMeta = new HashMap<>();
            adapterMeta.put("v3", "v3Type");
            adapterMeta.put("v4", "java.util.HashMap");
            value.put(SessionAdapterConstants.FLD_NM_ADAPTER, adapterMeta);
            value.put("field", "fieldValue");

            // when
            Object result = adapter.callUnwrapRecursive(value, meta);

            // then
            assertInstanceOf(Map.class, result);
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) result;
            assertFalse(resultMap.containsKey(SessionAdapterConstants.FLD_NM_ADAPTER));
        }

        @Test
        @DisplayName("기타 타입은 그대로 반환한다")
        void returnsOtherTypesAsIs() {
            // given
            SessionMetadataInfo meta = new SessionMetadataInfo();

            // when
            Object result = adapter.callUnwrapRecursive("stringValue", meta);

            // then
            assertEquals("stringValue", result);
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionAdapter 인터페이스를 구현한다")
        void implementsISessionAdapter() {
            // then
            assertInstanceOf(ISessionAdapter.class, adapter);
        }
    }
}
