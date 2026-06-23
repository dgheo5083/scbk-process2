package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.session.ISessionContext;

/**
 * {@link GenericSessionAdapter} 단위 테스트
 */
@DisplayName("GenericSessionAdapter 테스트")
@ExtendWith(MockitoExtension.class)
class GenericSessionAdapterTest {

    private GenericSessionAdapter adapter;

    @Mock
    private ISessionMetadataRegistry metadataRegistry;

    @BeforeEach
    void setUp() {
        adapter = new GenericSessionAdapter(metadataRegistry);
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("ISessionMetadataRegistry를 주입받아 생성할 수 있다")
        void createWithMetadataRegistry() {
            // when
            GenericSessionAdapter newAdapter = new GenericSessionAdapter(metadataRegistry);

            // then
            assertNotNull(newAdapter);
        }
    }

    @Nested
    @DisplayName("supports 메서드 테스트")
    class SupportsMethodTests {

        @Test
        @DisplayName("ISessionContext.SESSION_ATTR_KEY가 아닌 키를 지원한다")
        void supportsNonSessionAttrKey() {
            // when
            boolean result = adapter.supports("customKey", "value");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("ISessionContext.SESSION_ATTR_KEY는 지원하지 않는다")
        void doesNotSupportSessionAttrKey() {
            // when
            boolean result = adapter.supports(ISessionContext.SESSION_ATTR_KEY, "value");

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("wrap 메서드 테스트")
    class WrapMethodTests {

        @Test
        @DisplayName("메타데이터가 있으면 wrap을 수행한다")
        void wrapsWithMetadata() {
            // given
            SessionMetadataInfo metadataInfo = new SessionMetadataInfo();
            metadataInfo.setV3("v3Type");
            metadataInfo.setV4("v4Type");
            when(metadataRegistry.getMetadata("testKey")).thenReturn(Optional.of(metadataInfo));

            // when
            Object result = adapter.wrap("testKey", "testValue");

            // then
            assertNotNull(result);
        }

        @Test
        @DisplayName("메타데이터가 없으면 원본 값을 반환한다")
        void returnsOriginalWhenNoMetadata() {
            // given
            when(metadataRegistry.getMetadata("testKey")).thenReturn(Optional.empty());

            // when
            Object result = adapter.wrap("testKey", "testValue");

            // then
            assertEquals("testValue", result);
        }

        @Test
        @DisplayName("null 값은 그대로 반환한다")
        void returnsNullAsIs() {
            // when
            Object result = adapter.wrap("testKey", null);

            // then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("unwrap 메서드 테스트")
    class UnwrapMethodTests {

        @Test
        @DisplayName("메타데이터가 없으면 원본 값을 반환한다")
        void returnsOriginalWhenNoMetadata() {
            // given
            when(metadataRegistry.getMetadata("testKey")).thenReturn(Optional.empty());

            // when
            Object result = adapter.unwrap("testKey", "testValue");

            // then
            assertEquals("testValue", result);
        }

        @Test
        @DisplayName("null 값은 그대로 반환한다")
        void returnsNullAsIs() {
            // when
            Object result = adapter.unwrap("testKey", null);

            // then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("AbstractSessionAdapter를 상속한다")
        void extendsAbstractSessionAdapter() {
            // then
            assertInstanceOf(AbstractSessionAdapter.class, adapter);
        }

        @Test
        @DisplayName("ISessionAdapter 인터페이스를 구현한다")
        void implementsISessionAdapter() {
            // then
            assertInstanceOf(ISessionAdapter.class, adapter);
        }
    }
}
