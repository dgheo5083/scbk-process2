package com.scbank.process.api.fw.message.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link DelimiterType} 단위 테스트
 */
@DisplayName("DelimiterType 테스트")
class DelimiterTypeTest {

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTests {

        @Test
        @DisplayName("NONE 값이 존재한다")
        void hasNoneValue() {
            assertNotNull(DelimiterType.NONE);
        }

        @Test
        @DisplayName("FIXED 값이 존재한다")
        void hasFixedValue() {
            assertNotNull(DelimiterType.FIXED);
        }

        @Test
        @DisplayName("DELIMITER 값이 존재한다")
        void hasDelimiterValue() {
            assertNotNull(DelimiterType.DELIMITER);
        }

        @Test
        @DisplayName("LENGTH_BASED 값이 존재한다")
        void hasLengthBasedValue() {
            assertNotNull(DelimiterType.LENGTH_BASED);
        }

        @Test
        @DisplayName("TAG_BASED 값이 존재한다")
        void hasTagBasedValue() {
            assertNotNull(DelimiterType.TAG_BASED);
        }

        @Test
        @DisplayName("5개의 enum 값이 존재한다")
        void hasFiveValues() {
            assertEquals(5, DelimiterType.values().length);
        }
    }

    @Nested
    @DisplayName("valueOf 테스트")
    class ValueOfTests {

        @Test
        @DisplayName("valueOf로 NONE을 조회할 수 있다")
        void valueOfNone() {
            assertEquals(DelimiterType.NONE, DelimiterType.valueOf("NONE"));
        }

        @Test
        @DisplayName("valueOf로 FIXED를 조회할 수 있다")
        void valueOfFixed() {
            assertEquals(DelimiterType.FIXED, DelimiterType.valueOf("FIXED"));
        }

        @Test
        @DisplayName("valueOf로 DELIMITER를 조회할 수 있다")
        void valueOfDelimiter() {
            assertEquals(DelimiterType.DELIMITER, DelimiterType.valueOf("DELIMITER"));
        }

        @Test
        @DisplayName("valueOf로 LENGTH_BASED를 조회할 수 있다")
        void valueOfLengthBased() {
            assertEquals(DelimiterType.LENGTH_BASED, DelimiterType.valueOf("LENGTH_BASED"));
        }

        @Test
        @DisplayName("valueOf로 TAG_BASED를 조회할 수 있다")
        void valueOfTagBased() {
            assertEquals(DelimiterType.TAG_BASED, DelimiterType.valueOf("TAG_BASED"));
        }

        @Test
        @DisplayName("존재하지 않는 값은 예외가 발생한다")
        void valueOfInvalidThrowsException() {
            assertThrows(IllegalArgumentException.class,
                    () -> DelimiterType.valueOf("INVALID"));
        }
    }

    @Nested
    @DisplayName("enum 기본 기능 테스트")
    class EnumBasicTests {

        @Test
        @DisplayName("name() 메서드가 정상 동작한다")
        void nameMethod() {
            assertEquals("NONE", DelimiterType.NONE.name());
            assertEquals("FIXED", DelimiterType.FIXED.name());
            assertEquals("DELIMITER", DelimiterType.DELIMITER.name());
            assertEquals("LENGTH_BASED", DelimiterType.LENGTH_BASED.name());
            assertEquals("TAG_BASED", DelimiterType.TAG_BASED.name());
        }

        @Test
        @DisplayName("ordinal() 메서드가 정상 동작한다")
        void ordinalMethod() {
            assertEquals(0, DelimiterType.NONE.ordinal());
            assertEquals(1, DelimiterType.FIXED.ordinal());
            assertEquals(2, DelimiterType.DELIMITER.ordinal());
            assertEquals(3, DelimiterType.LENGTH_BASED.ordinal());
            assertEquals(4, DelimiterType.TAG_BASED.ordinal());
        }
    }
}
