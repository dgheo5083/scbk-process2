package com.scbank.process.api.fw.message.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * {@link AlignType} 단위 테스트
 */
@DisplayName("AlignType 테스트")
class AlignTypeTest {

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTests {

        @Test
        @DisplayName("LEFT 값이 존재한다")
        void hasLeftValue() {
            // then
            assertNotNull(AlignType.LEFT);
        }

        @Test
        @DisplayName("RIGHT 값이 존재한다")
        void hasRightValue() {
            // then
            assertNotNull(AlignType.RIGHT);
        }

        @Test
        @DisplayName("NONE 값이 존재한다")
        void hasNoneValue() {
            // then
            assertNotNull(AlignType.NONE);
        }

        @Test
        @DisplayName("3개의 enum 값이 존재한다")
        void hasThreeValues() {
            // when
            AlignType[] values = AlignType.values();

            // then
            assertEquals(3, values.length);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회할 수 있다")
        void canGetByValueOf() {
            // then
            assertEquals(AlignType.LEFT, AlignType.valueOf("LEFT"));
            assertEquals(AlignType.RIGHT, AlignType.valueOf("RIGHT"));
            assertEquals(AlignType.NONE, AlignType.valueOf("NONE"));
        }
    }

    @Nested
    @DisplayName("of 메서드 테스트")
    class OfMethodTests {

        @Test
        @DisplayName("'left' 문자열로 LEFT를 반환한다")
        void ofLeftString() {
            // when
            AlignType result = AlignType.of("left");

            // then
            assertEquals(AlignType.LEFT, result);
        }

        @Test
        @DisplayName("'LEFT' 문자열로 LEFT를 반환한다 (대소문자 무시)")
        void ofLeftUpperCase() {
            // when
            AlignType result = AlignType.of("LEFT");

            // then
            assertEquals(AlignType.LEFT, result);
        }

        @Test
        @DisplayName("'Left' 문자열로 LEFT를 반환한다 (대소문자 무시)")
        void ofLeftMixedCase() {
            // when
            AlignType result = AlignType.of("Left");

            // then
            assertEquals(AlignType.LEFT, result);
        }

        @Test
        @DisplayName("'right' 문자열로 RIGHT를 반환한다")
        void ofRightString() {
            // when
            AlignType result = AlignType.of("right");

            // then
            assertEquals(AlignType.RIGHT, result);
        }

        @Test
        @DisplayName("'RIGHT' 문자열로 RIGHT를 반환한다 (대소문자 무시)")
        void ofRightUpperCase() {
            // when
            AlignType result = AlignType.of("RIGHT");

            // then
            assertEquals(AlignType.RIGHT, result);
        }

        @Test
        @DisplayName("'Right' 문자열로 RIGHT를 반환한다 (대소문자 무시)")
        void ofRightMixedCase() {
            // when
            AlignType result = AlignType.of("Right");

            // then
            assertEquals(AlignType.RIGHT, result);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 문자열로 NONE을 반환한다")
        void ofNullOrEmpty(String input) {
            // when
            AlignType result = AlignType.of(input);

            // then
            assertEquals(AlignType.NONE, result);
        }

        @ParameterizedTest
        @ValueSource(strings = {"center", "middle", "top", "bottom", "unknown", "123"})
        @DisplayName("알 수 없는 문자열로 NONE을 반환한다")
        void ofUnknownString(String input) {
            // when
            AlignType result = AlignType.of(input);

            // then
            assertEquals(AlignType.NONE, result);
        }
    }

    @Nested
    @DisplayName("enum 기본 기능 테스트")
    class EnumBasicTests {

        @Test
        @DisplayName("name() 메서드가 정상 동작한다")
        void nameMethod() {
            // then
            assertEquals("LEFT", AlignType.LEFT.name());
            assertEquals("RIGHT", AlignType.RIGHT.name());
            assertEquals("NONE", AlignType.NONE.name());
        }

        @Test
        @DisplayName("ordinal() 메서드가 정상 동작한다")
        void ordinalMethod() {
            // then
            assertEquals(0, AlignType.LEFT.ordinal());
            assertEquals(1, AlignType.RIGHT.ordinal());
            assertEquals(2, AlignType.NONE.ordinal());
        }

        @Test
        @DisplayName("toString() 메서드가 정상 동작한다")
        void toStringMethod() {
            // then
            assertEquals("LEFT", AlignType.LEFT.toString());
            assertEquals("RIGHT", AlignType.RIGHT.toString());
            assertEquals("NONE", AlignType.NONE.toString());
        }
    }
}
