package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.session.ISessionObject;
import com.scbank.process.api.fw.session.SessionScope;

/**
 * {@link AbstractSessionObject} 단위 테스트
 */
@DisplayName("AbstractSessionObject 테스트")
class AbstractSessionObjectTest {

    private TestSessionObject sessionObject;

    @BeforeEach
    void setUp() {
        sessionObject = new TestSessionObject(SessionScope.GLOBAL);
    }

    /**
     * 테스트용 구체 클래스
     */
    private static class TestSessionObject extends AbstractSessionObject {
        private static final long serialVersionUID = 1L;

        public TestSessionObject(SessionScope sessionScope) {
            super(sessionScope);
        }
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("GLOBAL 세션 범위로 생성할 수 있다")
        void createWithGlobalScope() {
            // when
            TestSessionObject obj = new TestSessionObject(SessionScope.GLOBAL);

            // then
            assertEquals(SessionScope.GLOBAL, obj.sessionScope);
        }

        @Test
        @DisplayName("LOGIN 세션 범위로 생성할 수 있다")
        void createWithLoginScope() {
            // when
            TestSessionObject obj = new TestSessionObject(SessionScope.LOGIN);

            // then
            assertEquals(SessionScope.LOGIN, obj.sessionScope);
        }
    }

    @Nested
    @DisplayName("put 메서드 테스트")
    class PutMethodTests {

        @Test
        @DisplayName("키-값을 저장할 수 있다")
        void canPutKeyValue() {
            // when
            sessionObject.put("key", "value");

            // then
            assertEquals("value", sessionObject.get("key"));
        }

        @Test
        @DisplayName("같은 키에 새 값을 저장하면 이전 값을 반환한다")
        void returnsOldValueWhenOverwriting() {
            // given
            sessionObject.put("key", "oldValue");

            // when
            Object result = sessionObject.put("key", "newValue");

            // then
            assertEquals("oldValue", result);
        }
    }

    @Nested
    @DisplayName("putAll 메서드 테스트")
    class PutAllMethodTests {

        @Test
        @DisplayName("여러 키-값을 한번에 저장할 수 있다")
        void canPutAllKeyValues() {
            // given
            Map<String, String> data = new HashMap<>();
            data.put("key1", "value1");
            data.put("key2", "value2");

            // when
            sessionObject.putAll(data);

            // then
            assertEquals("value1", sessionObject.get("key1"));
            assertEquals("value2", sessionObject.get("key2"));
        }

        @Test
        @DisplayName("null 맵은 무시된다")
        void ignoresNullMap() {
            // when & then
            assertDoesNotThrow(() -> sessionObject.putAll(null));
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTests {

        @Test
        @DisplayName("ConcurrentHashMap을 상속한다")
        void extendsConcurrentHashMap() {
            // then
            assertInstanceOf(ConcurrentHashMap.class, sessionObject);
        }

        @Test
        @DisplayName("ISessionObject 인터페이스를 구현한다")
        void implementsISessionObject() {
            // then
            assertInstanceOf(ISessionObject.class, sessionObject);
        }
    }

    @Nested
    @DisplayName("스레드 안전성 테스트")
    class ThreadSafetyTests {

        @Test
        @DisplayName("동시 접근 시에도 데이터 일관성이 유지된다")
        void maintainsConsistencyUnderConcurrentAccess() throws InterruptedException {
            // given
            int threadCount = 10;
            int iterationCount = 100;
            Thread[] threads = new Thread[threadCount];

            // when
            for (int i = 0; i < threadCount; i++) {
                int threadId = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < iterationCount; j++) {
                        sessionObject.put("thread" + threadId + "_key" + j, "value" + j);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // then
            assertEquals(threadCount * iterationCount, sessionObject.size());
        }
    }
}
