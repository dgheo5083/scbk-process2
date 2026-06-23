package com.scbank.process.api.fw.message.context;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * {@link MessageContextHolder} 단위 테스트
 */
@DisplayName("MessageContextHolder 테스트")
class MessageContextHolderTest {

    @AfterEach
    void tearDown() {
        MessageContextHolder.clear();
    }

    @Nested
    @DisplayName("set/get 테스트")
    class SetGetTests {

        @Test
        @DisplayName("set으로 컨텍스트를 설정하고 get으로 조회할 수 있다")
        void setAndGet() {
            // given
            MessageContext context = new MessageContext.Builder()
                    .format(MessageFormat.JSON)
                    .defaultEncoding("UTF-8")
                    .build();

            // when
            MessageContextHolder.set(context);
            MessageContext result = MessageContextHolder.get();

            // then
            assertNotNull(result);
            assertEquals(context, result);
        }

        @Test
        @DisplayName("설정하지 않으면 get은 null을 반환한다")
        void getReturnsNullWhenNotSet() {
            // when
            MessageContext result = MessageContextHolder.get();

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("다른 값으로 덮어쓸 수 있다")
        void overwrite() {
            // given
            MessageContext context1 = new MessageContext.Builder()
                    .format(MessageFormat.JSON)
                    .build();
            MessageContext context2 = new MessageContext.Builder()
                    .format(MessageFormat.XML)
                    .build();

            // when
            MessageContextHolder.set(context1);
            MessageContextHolder.set(context2);
            MessageContext result = MessageContextHolder.get();

            // then
            assertEquals(context2, result);
            assertEquals(MessageFormat.XML, result.getFormat());
        }
    }

    @Nested
    @DisplayName("clear 테스트")
    class ClearTests {

        @Test
        @DisplayName("clear 호출 후 get은 null을 반환한다")
        void clearRemovesContext() {
            // given
            MessageContext context = new MessageContext.Builder()
                    .format(MessageFormat.JSON)
                    .build();
            MessageContextHolder.set(context);

            // when
            MessageContextHolder.clear();
            MessageContext result = MessageContextHolder.get();

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("clear를 여러 번 호출해도 에러가 발생하지 않는다")
        void multipleClearDoesNotThrow() {
            // when & then
            assertDoesNotThrow(() -> {
                MessageContextHolder.clear();
                MessageContextHolder.clear();
                MessageContextHolder.clear();
            });
        }
    }

    @Nested
    @DisplayName("ThreadLocal 격리 테스트")
    class ThreadLocalIsolationTests {

        @Test
        @DisplayName("다른 스레드에서 설정한 값은 현재 스레드에서 조회되지 않는다")
        void threadIsolation() throws InterruptedException {
            // given
            MessageContext mainContext = new MessageContext.Builder()
                    .format(MessageFormat.JSON)
                    .build();
            MessageContextHolder.set(mainContext);

            final MessageContext[] otherThreadResult = new MessageContext[1];

            // when
            Thread otherThread = new Thread(() -> {
                otherThreadResult[0] = MessageContextHolder.get();
            });
            otherThread.start();
            otherThread.join();

            // then
            assertNotNull(MessageContextHolder.get());
            assertNull(otherThreadResult[0]);
        }

        @Test
        @DisplayName("각 스레드는 독립적인 컨텍스트를 가진다")
        void independentContextPerThread() throws InterruptedException {
            // given
            MessageContext mainContext = new MessageContext.Builder()
                    .format(MessageFormat.JSON)
                    .build();
            MessageContextHolder.set(mainContext);

            final MessageContext[] otherThreadContext = new MessageContext[1];

            // when
            Thread otherThread = new Thread(() -> {
                MessageContext threadContext = new MessageContext.Builder()
                        .format(MessageFormat.XML)
                        .build();
                MessageContextHolder.set(threadContext);
                otherThreadContext[0] = MessageContextHolder.get();
            });
            otherThread.start();
            otherThread.join();

            // then
            assertEquals(MessageFormat.JSON, MessageContextHolder.get().getFormat());
            assertNotNull(otherThreadContext[0]);
            assertEquals(MessageFormat.XML, otherThreadContext[0].getFormat());
        }
    }
}
