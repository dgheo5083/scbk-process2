package com.scbank.process.api.fw.message.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link MessageFormat} 단위 테스트
 */
@DisplayName("MessageFormat 테스트")
class MessageFormatTest {

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTests {

        @Test
        @DisplayName("FIXEDLENGTH 값이 존재한다")
        void hasFixedLengthValue() {
            assertNotNull(MessageFormat.FIXEDLENGTH);
        }

        @Test
        @DisplayName("JSON 값이 존재한다")
        void hasJsonValue() {
            assertNotNull(MessageFormat.JSON);
        }

        @Test
        @DisplayName("XML 값이 존재한다")
        void hasXmlValue() {
            assertNotNull(MessageFormat.XML);
        }

        @Test
        @DisplayName("FORM 값이 존재한다")
        void hasFormValue() {
            assertNotNull(MessageFormat.FORM);
        }

        @Test
        @DisplayName("MULTIPART_FORM 값이 존재한다")
        void hasMultipartFormValue() {
            assertNotNull(MessageFormat.MULTIPART_FORM);
        }

        @Test
        @DisplayName("DELIMITER 값이 존재한다")
        void hasDelimiterValue() {
            assertNotNull(MessageFormat.DELIMITER);
        }

        @Test
        @DisplayName("NONE 값이 존재한다")
        void hasNoneValue() {
            assertNotNull(MessageFormat.NONE);
        }

        @Test
        @DisplayName("7개의 enum 값이 존재한다")
        void hasSevenValues() {
            // when
            MessageFormat[] values = MessageFormat.values();

            // then
            assertEquals(7, values.length);
        }
    }

    @Nested
    @DisplayName("valueOf 테스트")
    class ValueOfTests {

        @Test
        @DisplayName("valueOf로 FIXEDLENGTH를 조회할 수 있다")
        void valueOfFixedLength() {
            assertEquals(MessageFormat.FIXEDLENGTH, MessageFormat.valueOf("FIXEDLENGTH"));
        }

        @Test
        @DisplayName("valueOf로 JSON을 조회할 수 있다")
        void valueOfJson() {
            assertEquals(MessageFormat.JSON, MessageFormat.valueOf("JSON"));
        }

        @Test
        @DisplayName("valueOf로 XML을 조회할 수 있다")
        void valueOfXml() {
            assertEquals(MessageFormat.XML, MessageFormat.valueOf("XML"));
        }

        @Test
        @DisplayName("valueOf로 FORM을 조회할 수 있다")
        void valueOfForm() {
            assertEquals(MessageFormat.FORM, MessageFormat.valueOf("FORM"));
        }

        @Test
        @DisplayName("valueOf로 MULTIPART_FORM을 조회할 수 있다")
        void valueOfMultipartForm() {
            assertEquals(MessageFormat.MULTIPART_FORM, MessageFormat.valueOf("MULTIPART_FORM"));
        }

        @Test
        @DisplayName("valueOf로 DELIMITER를 조회할 수 있다")
        void valueOfDelimiter() {
            assertEquals(MessageFormat.DELIMITER, MessageFormat.valueOf("DELIMITER"));
        }

        @Test
        @DisplayName("valueOf로 NONE을 조회할 수 있다")
        void valueOfNone() {
            assertEquals(MessageFormat.NONE, MessageFormat.valueOf("NONE"));
        }

        @Test
        @DisplayName("존재하지 않는 값은 예외가 발생한다")
        void valueOfInvalidThrowsException() {
            assertThrows(IllegalArgumentException.class,
                    () -> MessageFormat.valueOf("INVALID"));
        }
    }

    @Nested
    @DisplayName("enum 기본 기능 테스트")
    class EnumBasicTests {

        @Test
        @DisplayName("name() 메서드가 정상 동작한다")
        void nameMethod() {
            assertEquals("FIXEDLENGTH", MessageFormat.FIXEDLENGTH.name());
            assertEquals("JSON", MessageFormat.JSON.name());
            assertEquals("XML", MessageFormat.XML.name());
            assertEquals("FORM", MessageFormat.FORM.name());
            assertEquals("MULTIPART_FORM", MessageFormat.MULTIPART_FORM.name());
            assertEquals("DELIMITER", MessageFormat.DELIMITER.name());
            assertEquals("NONE", MessageFormat.NONE.name());
        }

        @Test
        @DisplayName("ordinal() 메서드가 정상 동작한다")
        void ordinalMethod() {
            assertEquals(0, MessageFormat.FIXEDLENGTH.ordinal());
            assertEquals(1, MessageFormat.JSON.ordinal());
            assertEquals(2, MessageFormat.XML.ordinal());
            assertEquals(3, MessageFormat.FORM.ordinal());
            assertEquals(4, MessageFormat.MULTIPART_FORM.ordinal());
            assertEquals(5, MessageFormat.DELIMITER.ordinal());
            assertEquals(6, MessageFormat.NONE.ordinal());
        }

        @Test
        @DisplayName("toString() 메서드가 정상 동작한다")
        void toStringMethod() {
            assertEquals("FIXEDLENGTH", MessageFormat.FIXEDLENGTH.toString());
            assertEquals("JSON", MessageFormat.JSON.toString());
            assertEquals("XML", MessageFormat.XML.toString());
        }
    }
}
