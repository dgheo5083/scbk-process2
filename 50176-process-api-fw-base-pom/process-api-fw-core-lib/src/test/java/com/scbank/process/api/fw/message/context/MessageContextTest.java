package com.scbank.process.api.fw.message.context;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

/**
 * {@link MessageContext} 단위 테스트
 */
@DisplayName("MessageContext 테스트")
class MessageContextTest {

    private MessageContext messageContext;

    @BeforeEach
    void setUp() {
        messageContext = new MessageContext();
    }

    @Nested
    @DisplayName("pathValues 관리 테스트")
    class PathValueTests {

        @Test
        @DisplayName("addPathValue로 값을 추가하고 getPathValue로 조회할 수 있다")
        void addAndGetPathValue() {
            // given
            String path = "field.name";
            Object value = "testValue";

            // when
            messageContext.addPathValue(path, value);
            Object result = messageContext.getPathValue(path);

            // then
            assertEquals(value, result);
        }

        @Test
        @DisplayName("존재하지 않는 경로로 조회하면 null을 반환한다")
        void getPathValueReturnsNullForNonExistentPath() {
            // when
            Object result = messageContext.getPathValue("nonExistent");

            // then
            assertNull(result);
        }

        @Test
        @DisplayName("같은 경로에 값을 덮어쓸 수 있다")
        void overwritePathValue() {
            // given
            String path = "field.name";
            messageContext.addPathValue(path, "oldValue");

            // when
            messageContext.addPathValue(path, "newValue");

            // then
            assertEquals("newValue", messageContext.getPathValue(path));
        }

        @Test
        @DisplayName("getPathValues로 전체 Map을 조회할 수 있다")
        void getPathValues() {
            // given
            messageContext.addPathValue("path1", "value1");
            messageContext.addPathValue("path2", "value2");

            // when
            var pathValues = messageContext.getPathValues();

            // then
            assertEquals(2, pathValues.size());
            assertEquals("value1", pathValues.get("path1"));
            assertEquals("value2", pathValues.get("path2"));
        }
    }

    @Nested
    @DisplayName("logValues 관리 테스트")
    class LogValueTests {

        @Test
        @DisplayName("addLogValue로 로그 값을 추가할 수 있다")
        void addLogValue() {
            // given
            String path = "log.path";
            String value = "logValue";

            // when
            messageContext.addLogValue(path, value);

            // then
            assertEquals(value, messageContext.getLogValues().get(path));
        }

        @Test
        @DisplayName("getLogValues로 전체 로그 Map을 조회할 수 있다")
        void getLogValues() {
            // given
            messageContext.addLogValue("path1", "value1");
            messageContext.addLogValue("path2", "value2");

            // when
            var logValues = messageContext.getLogValues();

            // then
            assertEquals(2, logValues.size());
        }
    }

    @Nested
    @DisplayName("indexStack 관리 테스트")
    class IndexStackTests {

        @Test
        @DisplayName("pushIndex로 인덱스를 스택에 추가할 수 있다")
        void pushIndex() {
            // when
            messageContext.pushIndex(0);
            messageContext.pushIndex(1);

            // then
            assertEquals(2, messageContext.getIndexStack().size());
        }

        @Test
        @DisplayName("popIndex로 인덱스를 스택에서 제거할 수 있다")
        void popIndex() {
            // given
            messageContext.pushIndex(0);
            messageContext.pushIndex(1);

            // when
            messageContext.popIndex();

            // then
            assertEquals(1, messageContext.getIndexStack().size());
        }
    }

    @Nested
    @DisplayName("resolvePath 테스트")
    class ResolvePathTests {

        @Test
        @DisplayName("인덱스 플레이스홀더를 실제 인덱스로 치환한다")
        void resolvePathWithSingleIndex() {
            // given
            messageContext.pushIndex(0);
            String pathTemplate = "items[*].name";

            // when
            String resolved = messageContext.resolvePath(pathTemplate);

            // then
            assertEquals("items[0].name", resolved);
        }

        @Test
        @DisplayName("여러 인덱스 플레이스홀더를 스택 순서(LIFO)로 치환한다")
        void resolvePathWithMultipleIndices() {
            // given
            // 스택은 LIFO 구조이므로 push(0) 후 push(1)을 하면
            // 순회 시 1, 0 순서로 반복된다
            messageContext.pushIndex(0);
            messageContext.pushIndex(1);
            String pathTemplate = "items[*].subItems[*].name";

            // when
            String resolved = messageContext.resolvePath(pathTemplate);

            // then
            // 스택 순회는 가장 최근에 push된 값부터 시작하므로
            // 첫 번째 [*]는 1로, 두 번째 [*]는 0으로 치환된다
            assertEquals("items[1].subItems[0].name", resolved);
        }

        @Test
        @DisplayName("플레이스홀더가 없으면 원본 경로를 그대로 반환한다")
        void resolvePathWithoutPlaceholder() {
            // given
            String pathTemplate = "items.name";

            // when
            String resolved = messageContext.resolvePath(pathTemplate);

            // then
            assertEquals("items.name", resolved);
        }
    }

    @Nested
    @DisplayName("Options 관리 테스트")
    class OptionsTests {

        @Test
        @DisplayName("기본 SerializationOptions가 설정되어 있다")
        void defaultSerializationOptions() {
            // when
            SerializationOptions options = messageContext.getSerializationOptions();

            // then
            assertNotNull(options);
        }

        @Test
        @DisplayName("기본 DeserializationOptions가 설정되어 있다")
        void defaultDeserializationOptions() {
            // when
            DeserializationOptions options = messageContext.getDeserializationOptions();

            // then
            assertNotNull(options);
        }

        @Test
        @DisplayName("SerializationOptions를 설정할 수 있다")
        void setSerializationOptions() {
            // given
            SerializationOptions newOptions = SerializationOptions.builder().build();

            // when
            messageContext.setSerializationOptions(newOptions);

            // then
            assertEquals(newOptions, messageContext.getSerializationOptions());
        }

        @Test
        @DisplayName("DeserializationOptions를 설정할 수 있다")
        void setDeserializationOptions() {
            // given
            DeserializationOptions newOptions = DeserializationOptions.builder().build();

            // when
            messageContext.setDeserializationOptions(newOptions);

            // then
            assertEquals(newOptions, messageContext.getDeserializationOptions());
        }
    }

    @Nested
    @DisplayName("기타 속성 테스트")
    class OtherPropertiesTests {

        @Test
        @DisplayName("defaultEncoding을 설정하고 조회할 수 있다")
        void defaultEncoding() {
            // when
            messageContext.setDefaultEncoding("UTF-8");

            // then
            assertEquals("UTF-8", messageContext.getDefaultEncoding());
        }

        @Test
        @DisplayName("format을 설정하고 조회할 수 있다")
        void format() {
            // when
            messageContext.setFormat(MessageFormat.JSON);

            // then
            assertEquals(MessageFormat.JSON, messageContext.getFormat());
        }

        @Test
        @DisplayName("useDebugLog를 설정하고 조회할 수 있다")
        void useDebugLog() {
            // when
            messageContext.setUseDebugLog(true);

            // then
            assertTrue(messageContext.isUseDebugLog());
        }
    }

    @Nested
    @DisplayName("Builder 테스트")
    class BuilderTests {

        @Test
        @DisplayName("Builder로 MessageContext를 생성할 수 있다")
        void buildMessageContext() {
            // when
            MessageContext context = new MessageContext.Builder()
                    .format(MessageFormat.FIXEDLENGTH)
                    .defaultEncoding("EUC-KR")
                    .useDebugLog(true)
                    .build();

            // then
            assertEquals(MessageFormat.FIXEDLENGTH, context.getFormat());
            assertEquals("EUC-KR", context.getDefaultEncoding());
            assertTrue(context.isUseDebugLog());
        }

        @Test
        @DisplayName("Builder로 SerializationOptions를 설정할 수 있다")
        void buildWithSerializationOptions() {
            // given
            SerializationOptions options = SerializationOptions.builder().build();

            // when
            MessageContext context = new MessageContext.Builder()
                    .serializationOptions(options)
                    .build();

            // then
            assertEquals(options, context.getSerializationOptions());
        }

        @Test
        @DisplayName("Builder로 DeserializationOptions를 설정할 수 있다")
        void buildWithDeserializationOptions() {
            // given
            DeserializationOptions options = DeserializationOptions.builder().build();

            // when
            MessageContext context = new MessageContext.Builder()
                    .deserializationOptions(options)
                    .build();

            // then
            assertEquals(options, context.getDeserializationOptions());
        }
    }
}
