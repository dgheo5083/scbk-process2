package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.session.ISessionObject;
import com.scbank.process.api.fw.session.SessionScope;

/**
 * {@link GlobalSessionObject} 단위 테스트
 */
@DisplayName("GlobalSessionObject 테스트")
class GlobalSessionObjectTest {

    private GlobalSessionObject globalSession;

    @BeforeEach
    void setUp() {
        globalSession = new GlobalSessionObject();
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("기본 생성자로 생성할 수 있다")
        void createWithDefaultConstructor() {
            // when
            GlobalSessionObject session = new GlobalSessionObject();

            // then
            assertNotNull(session);
        }

        @Test
        @DisplayName("GLOBAL 세션 범위로 초기화된다")
        void initializesWithGlobalScope() {
            // then
            assertEquals(SessionScope.GLOBAL, globalSession.sessionScope);
        }
    }

    @Nested
    @DisplayName("put 메서드 테스트")
    class PutMethodTests {

        @Test
        @DisplayName("키-값을 저장할 수 있다")
        void canPutKeyValue() {
            // when
            globalSession.put("traceId", "trace123");

            // then
            assertEquals("trace123", globalSession.get("traceId"));
        }

        @Test
        @DisplayName("여러 키-값을 저장할 수 있다")
        void canPutMultipleKeyValues() {
            // when
            globalSession.put("key1", "value1");
            globalSession.put("key2", "value2");

            // then
            assertEquals(2, globalSession.size());
            assertEquals("value1", globalSession.get("key1"));
            assertEquals("value2", globalSession.get("key2"));
        }

        @Test
        @DisplayName("같은 키에 새 값을 저장하면 덮어쓴다")
        void putOverwritesExistingValue() {
            // given
            globalSession.put("key", "oldValue");

            // when
            globalSession.put("key", "newValue");

            // then
            assertEquals("newValue", globalSession.get("key"));
        }
    }

    @Nested
    @DisplayName("get 메서드 테스트")
    class GetMethodTests {

        @Test
        @DisplayName("저장된 값을 조회할 수 있다")
        void canGetStoredValue() {
            // given
            globalSession.put("channelId", "WEB");

            // when
            Object result = globalSession.get("channelId");

            // then
            assertEquals("WEB", result);
        }

        @Test
        @DisplayName("존재하지 않는 키는 null을 반환한다")
        void returnsNullForNonExistentKey() {
            // when
            Object result = globalSession.get("nonExistent");

            // then
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("remove 메서드 테스트")
    class RemoveMethodTests {

        @Test
        @DisplayName("저장된 값을 제거할 수 있다")
        void canRemoveStoredValue() {
            // given
            globalSession.put("key", "value");

            // when
            globalSession.remove("key");

            // then
            assertNull(globalSession.get("key"));
        }
    }

    @Nested
    @DisplayName("clear 메서드 테스트")
    class ClearMethodTests {

        @Test
        @DisplayName("모든 저장된 값을 제거할 수 있다")
        void canClearAllValues() {
            // given
            globalSession.put("key1", "value1");
            globalSession.put("key2", "value2");

            // when
            globalSession.clear();

            // then
            assertTrue(globalSession.isEmpty());
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("AbstractSessionObject를 상속한다")
        void extendsAbstractSessionObject() {
            // then
            assertInstanceOf(AbstractSessionObject.class, globalSession);
        }

        @Test
        @DisplayName("ISessionObject 인터페이스를 구현한다")
        void implementsISessionObject() {
            // then
            assertInstanceOf(ISessionObject.class, globalSession);
        }
    }
}
