package com.scbank.process.api.fw.message.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link DelimiterPosition} 단위 테스트
 */
@DisplayName("DelimiterPosition 테스트")
class DelimiterPositionTest {

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTests {

        @Test
        @DisplayName("NONE 값이 존재한다")
        void hasNoneValue() {
            assertNotNull(DelimiterPosition.NONE);
        }

        @Test
        @DisplayName("PREFIX 값이 존재한다")
        void hasPrefixValue() {
            assertNotNull(DelimiterPosition.PREFIX);
        }

        @Test
        @DisplayName("SUFFIX 값이 존재한다")
        void hasSuffixValue() {
            assertNotNull(DelimiterPosition.SUFFIX);
        }

        @Test
        @DisplayName("WRAPPED 값이 존재한다")
        void hasWrappedValue() {
            assertNotNull(DelimiterPosition.WRAPPED);
        }

        @Test
        @DisplayName("4개의 enum 값이 존재한다")
        void hasFourValues() {
            assertEquals(4, DelimiterPosition.values().length);
        }
    }

    @Nested
    @DisplayName("valueOf 테스트")
    class ValueOfTests {

        @Test
        @DisplayName("valueOf로 NONE을 조회할 수 있다")
        void valueOfNone() {
            assertEquals(DelimiterPosition.NONE, DelimiterPosition.valueOf("NONE"));
        }

        @Test
        @DisplayName("valueOf로 PREFIX를 조회할 수 있다")
        void valueOfPrefix() {
            assertEquals(DelimiterPosition.PREFIX, DelimiterPosition.valueOf("PREFIX"));
        }

        @Test
        @DisplayName("valueOf로 SUFFIX를 조회할 수 있다")
        void valueOfSuffix() {
            assertEquals(DelimiterPosition.SUFFIX, DelimiterPosition.valueOf("SUFFIX"));
        }

        @Test
        @DisplayName("valueOf로 WRAPPED를 조회할 수 있다")
        void valueOfWrapped() {
            assertEquals(DelimiterPosition.WRAPPED, DelimiterPosition.valueOf("WRAPPED"));
        }

        @Test
        @DisplayName("존재하지 않는 값은 예외가 발생한다")
        void valueOfInvalidThrowsException() {
            assertThrows(IllegalArgumentException.class,
                    () -> DelimiterPosition.valueOf("INVALID"));
        }
    }

    @Nested
    @DisplayName("enum 기본 기능 테스트")
    class EnumBasicTests {

        @Test
        @DisplayName("name() 메서드가 정상 동작한다")
        void nameMethod() {
            assertEquals("NONE", DelimiterPosition.NONE.name());
            assertEquals("PREFIX", DelimiterPosition.PREFIX.name());
            assertEquals("SUFFIX", DelimiterPosition.SUFFIX.name());
            assertEquals("WRAPPED", DelimiterPosition.WRAPPED.name());
        }

        @Test
        @DisplayName("ordinal() 메서드가 정상 동작한다")
        void ordinalMethod() {
            assertEquals(0, DelimiterPosition.NONE.ordinal());
            assertEquals(1, DelimiterPosition.PREFIX.ordinal());
            assertEquals(2, DelimiterPosition.SUFFIX.ordinal());
            assertEquals(3, DelimiterPosition.WRAPPED.ordinal());
        }
    }
}
