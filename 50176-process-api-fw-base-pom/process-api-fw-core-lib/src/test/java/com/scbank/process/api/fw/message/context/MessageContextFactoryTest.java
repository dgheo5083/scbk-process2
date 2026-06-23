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
 * {@link MessageContextFactory} 단위 테스트
 */
@DisplayName("MessageContextFactory 테스트")
class MessageContextFactoryTest {

    private MessageContextFactory factory;

    @BeforeEach
    void setUp() {
        factory = new MessageContextFactory();
    }

    @Nested
    @DisplayName("create(MessageFormat, String) 테스트")
    class CreateWithFormatAndEncodingTests {

        @Test
        @DisplayName("FIXEDLENGTH 포맷으로 MessageContext를 생성할 수 있다")
        void createWithFixedLengthFormat() {
            // when
            MessageContext context = factory.create(MessageFormat.FIXEDLENGTH, "UTF-8");

            // then
            assertNotNull(context);
            assertEquals(MessageFormat.FIXEDLENGTH, context.getFormat());
            assertEquals("UTF-8", context.getDefaultEncoding());
            assertNotNull(context.getSerializationOptions());
            assertNotNull(context.getDeserializationOptions());
        }

        @Test
        @DisplayName("JSON 포맷으로 MessageContext를 생성할 수 있다")
        void createWithJsonFormat() {
            // when
            MessageContext context = factory.create(MessageFormat.JSON, "UTF-8");

            // then
            assertNotNull(context);
            assertEquals(MessageFormat.JSON, context.getFormat());
        }

        @Test
        @DisplayName("XML 포맷으로 MessageContext를 생성할 수 있다")
        void createWithXmlFormat() {
            // when
            MessageContext context = factory.create(MessageFormat.XML, "UTF-8");

            // then
            assertNotNull(context);
            assertEquals(MessageFormat.XML, context.getFormat());
        }

        @Test
        @DisplayName("FORM 포맷으로 MessageContext를 생성할 수 있다")
        void createWithFormFormat() {
            // when
            MessageContext context = factory.create(MessageFormat.FORM, "UTF-8");

            // then
            assertNotNull(context);
            assertEquals(MessageFormat.FORM, context.getFormat());
        }

        @Test
        @DisplayName("MULTIPART_FORM 포맷으로 MessageContext를 생성할 수 있다")
        void createWithMultipartFormFormat() {
            // when
            MessageContext context = factory.create(MessageFormat.MULTIPART_FORM, "UTF-8");

            // then
            assertNotNull(context);
            assertEquals(MessageFormat.MULTIPART_FORM, context.getFormat());
        }

        @Test
        @DisplayName("다른 인코딩으로 MessageContext를 생성할 수 있다")
        void createWithDifferentEncoding() {
            // when
            MessageContext context = factory.create(MessageFormat.FIXEDLENGTH, "EUC-KR");

            // then
            assertEquals("EUC-KR", context.getDefaultEncoding());
        }
    }

    @Nested
    @DisplayName("create(MessageFormat, String, SerializationOptions, DeserializationOptions) 테스트")
    class CreateWithAllOptionsTests {

        @Test
        @DisplayName("모든 옵션을 지정하여 MessageContext를 생성할 수 있다")
        void createWithAllOptions() {
            // given
            SerializationOptions serOptions = SerializationOptions.builder().build();
            DeserializationOptions deserOptions = DeserializationOptions.builder().build();

            // when
            MessageContext context = factory.create(
                    MessageFormat.JSON,
                    "UTF-8",
                    serOptions,
                    deserOptions);

            // then
            assertNotNull(context);
            assertEquals(MessageFormat.JSON, context.getFormat());
            assertEquals("UTF-8", context.getDefaultEncoding());
            assertEquals(serOptions, context.getSerializationOptions());
            assertEquals(deserOptions, context.getDeserializationOptions());
        }
    }

    @Nested
    @DisplayName("merge 테스트")
    class MergeTests {

        @Test
        @DisplayName("원본 컨텍스트의 기본 속성을 유지하며 옵션을 병합할 수 있다")
        void mergeWithNewOptions() {
            // given
            MessageContext original = factory.create(MessageFormat.FIXEDLENGTH, "UTF-8");
            SerializationOptions newSerOptions = SerializationOptions.builder().build();
            DeserializationOptions newDeserOptions = DeserializationOptions.builder().build();

            // when
            MessageContext merged = factory.merge(original, newSerOptions, newDeserOptions);

            // then
            assertEquals(MessageFormat.FIXEDLENGTH, merged.getFormat());
            assertEquals("UTF-8", merged.getDefaultEncoding());
            assertEquals(newSerOptions, merged.getSerializationOptions());
            assertEquals(newDeserOptions, merged.getDeserializationOptions());
        }

        @Test
        @DisplayName("SerializationOptions가 null이면 원본 옵션을 사용한다")
        void mergeWithNullSerializationOptions() {
            // given
            MessageContext original = factory.create(MessageFormat.JSON, "UTF-8");
            DeserializationOptions newDeserOptions = DeserializationOptions.builder().build();

            // when
            MessageContext merged = factory.merge(original, null, newDeserOptions);

            // then
            assertEquals(original.getSerializationOptions(), merged.getSerializationOptions());
            assertEquals(newDeserOptions, merged.getDeserializationOptions());
        }

        @Test
        @DisplayName("DeserializationOptions가 null이면 원본 옵션을 사용한다")
        void mergeWithNullDeserializationOptions() {
            // given
            MessageContext original = factory.create(MessageFormat.JSON, "UTF-8");
            SerializationOptions newSerOptions = SerializationOptions.builder().build();

            // when
            MessageContext merged = factory.merge(original, newSerOptions, null);

            // then
            assertEquals(newSerOptions, merged.getSerializationOptions());
            assertEquals(original.getDeserializationOptions(), merged.getDeserializationOptions());
        }

        @Test
        @DisplayName("양쪽 옵션이 모두 null이면 원본 옵션을 사용한다")
        void mergeWithBothNullOptions() {
            // given
            MessageContext original = factory.create(MessageFormat.JSON, "UTF-8");

            // when
            MessageContext merged = factory.merge(original, null, null);

            // then
            assertEquals(original.getSerializationOptions(), merged.getSerializationOptions());
            assertEquals(original.getDeserializationOptions(), merged.getDeserializationOptions());
        }
    }

    @Nested
    @DisplayName("useDebugLog 속성 테스트")
    class UseDebugLogTests {

        @Test
        @DisplayName("기본 useDebugLog 값은 false이다")
        void defaultUseDebugLog() {
            // then
            assertFalse(factory.isUseDebugLog());
        }

        @Test
        @DisplayName("useDebugLog를 설정할 수 있다")
        void setUseDebugLog() {
            // when
            factory.setUseDebugLog(true);

            // then
            assertTrue(factory.isUseDebugLog());
        }

        @Test
        @DisplayName("생성된 컨텍스트에 useDebugLog가 적용된다")
        void createdContextHasUseDebugLog() {
            // given
            factory.setUseDebugLog(true);

            // when
            MessageContext context = factory.create(MessageFormat.JSON, "UTF-8");

            // then
            assertTrue(context.isUseDebugLog());
        }
    }
}
