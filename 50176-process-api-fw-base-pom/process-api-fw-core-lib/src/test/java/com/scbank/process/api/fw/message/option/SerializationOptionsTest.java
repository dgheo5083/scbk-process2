package com.scbank.process.api.fw.message.option;

import static org.junit.jupiter.api.Assertions.*;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;

/**
 * {@link SerializationOptions} 단위 테스트
 */
@DisplayName("SerializationOptions 테스트")
class SerializationOptionsTest {

    @Nested
    @DisplayName("Builder 테스트")
    class BuilderTests {

        @Test
        @DisplayName("빈 옵션으로 생성할 수 있다")
        void createEmptyOptions() {
            // when
            SerializationOptions options = SerializationOptions.builder().build();

            // then
            assertNotNull(options);
        }

        @Test
        @DisplayName("Boolean 옵션을 설정할 수 있다")
        void setBooleanOption() {
            // when
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .build();

            // then
            assertTrue(options.enabled(MessageFormatOption.PADDING));
        }

        @Test
        @DisplayName("여러 옵션을 체이닝하여 설정할 수 있다")
        void chainMultipleOptions() {
            // when
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .set(MessageFormatOption.LENGTH_CHECK, true)
                    .set(MessageFormatOption.FIELD_TRIM, false)
                    .build();

            // then
            assertTrue(options.enabled(MessageFormatOption.PADDING));
            assertTrue(options.enabled(MessageFormatOption.LENGTH_CHECK));
            assertFalse(options.enabled(MessageFormatOption.FIELD_TRIM));
        }

        @Test
        @DisplayName("RoundingMode 옵션을 설정할 수 있다")
        void setRoundingModeOption() {
            // when
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.DECIMAL_ROUNDING_MODE, RoundingMode.HALF_UP)
                    .build();

            // then
            Optional<Object> result = options.get(MessageFormatOption.DECIMAL_ROUNDING_MODE);
            assertTrue(result.isPresent());
            assertEquals(RoundingMode.HALF_UP, result.get());
        }

        @Test
        @DisplayName("Integer 옵션을 설정할 수 있다")
        void setIntegerOption() {
            // when
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.DEFAULT_DECIMAL_SCALE, 4)
                    .build();

            // then
            Optional<Object> result = options.get(MessageFormatOption.DEFAULT_DECIMAL_SCALE);
            assertTrue(result.isPresent());
            assertEquals(4, result.get());
        }
    }

    @Nested
    @DisplayName("getDefaultOptions 테스트")
    class GetDefaultOptionsTests {

        @Test
        @DisplayName("FIXEDLENGTH 포맷의 기본 옵션을 조회할 수 있다")
        void getFixedLengthDefaultOptions() {
            // when
            Optional<SerializationOptions> options = SerializationOptions.getDefaultOptions(MessageFormat.FIXEDLENGTH);

            // then
            assertTrue(options.isPresent());
            assertTrue(options.get().enabled(MessageFormatOption.PADDING));
            assertTrue(options.get().enabled(MessageFormatOption.LENGTH_CHECK));
        }

        @Test
        @DisplayName("JSON 포맷의 기본 옵션을 조회할 수 있다")
        void getJsonDefaultOptions() {
            // when
            Optional<SerializationOptions> options = SerializationOptions.getDefaultOptions(MessageFormat.JSON);

            // then
            assertTrue(options.isPresent());
            assertTrue(options.get().enabled(MessageFormatOption.FIELD_TRIM));
        }

        @Test
        @DisplayName("XML 포맷의 기본 옵션을 조회할 수 있다")
        void getXmlDefaultOptions() {
            // when
            Optional<SerializationOptions> options = SerializationOptions.getDefaultOptions(MessageFormat.XML);

            // then
            assertTrue(options.isPresent());
        }

        @Test
        @DisplayName("FORM 포맷의 기본 옵션을 조회할 수 있다")
        void getFormDefaultOptions() {
            // when
            Optional<SerializationOptions> options = SerializationOptions.getDefaultOptions(MessageFormat.FORM);

            // then
            assertTrue(options.isPresent());
        }

        @Test
        @DisplayName("MULTIPART_FORM 포맷의 기본 옵션을 조회할 수 있다")
        void getMultipartFormDefaultOptions() {
            // when
            Optional<SerializationOptions> options = SerializationOptions.getDefaultOptions(MessageFormat.MULTIPART_FORM);

            // then
            assertTrue(options.isPresent());
        }

        @Test
        @DisplayName("지원하지 않는 포맷은 빈 Optional을 반환한다")
        void returnsEmptyForUnsupportedFormat() {
            // when
            Optional<SerializationOptions> options = SerializationOptions.getDefaultOptions(MessageFormat.NONE);

            // then
            assertFalse(options.isPresent());
        }
    }

    @Nested
    @DisplayName("of (복제) 테스트")
    class CopyTests {

        @Test
        @DisplayName("기존 옵션을 복제할 수 있다")
        void copyOptions() {
            // given
            SerializationOptions original = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .set(MessageFormatOption.LENGTH_CHECK, true)
                    .build();

            // when
            SerializationOptions copy = SerializationOptions.of(original);

            // then
            assertNotNull(copy);
            assertNotSame(original, copy);
            assertTrue(copy.enabled(MessageFormatOption.PADDING));
            assertTrue(copy.enabled(MessageFormatOption.LENGTH_CHECK));
        }

        @Test
        @DisplayName("복제 후 원본을 수정해도 복제본에 영향을 주지 않는다")
        void copyIsIndependent() {
            // given
            SerializationOptions original = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .build();
            SerializationOptions copy = SerializationOptions.of(original);

            // when - 원본 수정
            original.set(MessageFormatOption.LENGTH_CHECK, true);

            // then - 복제본은 영향 없음
            assertFalse(copy.enabled(MessageFormatOption.LENGTH_CHECK));
        }
    }

    @Nested
    @DisplayName("merge 테스트")
    class MergeTests {

        @Test
        @DisplayName("기본 옵션과 확장 옵션을 병합할 수 있다")
        void mergeOptions() {
            // given
            SerializationOptions base = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .build();
            Map<String, Object> overrides = new HashMap<>();
            overrides.put(MessageFormatOption.LENGTH_CHECK.key(), true);

            // when
            SerializationOptions merged = SerializationOptions.merge(base, overrides);

            // then
            assertTrue(merged.enabled(MessageFormatOption.PADDING));
            assertTrue(merged.enabled(MessageFormatOption.LENGTH_CHECK));
        }

        @Test
        @DisplayName("확장 옵션이 null이면 기본 옵션만 복제한다")
        void mergeWithNullOverrides() {
            // given
            SerializationOptions base = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .build();

            // when
            SerializationOptions merged = SerializationOptions.merge(base, null);

            // then
            assertTrue(merged.enabled(MessageFormatOption.PADDING));
        }

        @Test
        @DisplayName("확장 옵션이 기존 값을 덮어쓴다")
        void overridesExistingValues() {
            // given
            SerializationOptions base = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .build();
            Map<String, Object> overrides = new HashMap<>();
            overrides.put(MessageFormatOption.PADDING.key(), false);

            // when
            SerializationOptions merged = SerializationOptions.merge(base, overrides);

            // then
            assertFalse(merged.enabled(MessageFormatOption.PADDING));
        }
    }

    @Nested
    @DisplayName("enabled 테스트")
    class EnabledTests {

        @Test
        @DisplayName("설정되지 않은 옵션은 false를 반환한다")
        void unsetOptionReturnsFalse() {
            // given
            SerializationOptions options = SerializationOptions.builder().build();

            // when
            boolean result = options.enabled(MessageFormatOption.PADDING);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("false로 설정된 옵션은 false를 반환한다")
        void falseOptionReturnsFalse() {
            // given
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, false)
                    .build();

            // when
            boolean result = options.enabled(MessageFormatOption.PADDING);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("true로 설정된 옵션은 true를 반환한다")
        void trueOptionReturnsTrue() {
            // given
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .build();

            // when
            boolean result = options.enabled(MessageFormatOption.PADDING);

            // then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("getAll 테스트")
    class GetAllTests {

        @Test
        @DisplayName("모든 옵션을 Map으로 조회할 수 있다")
        void getAllOptions() {
            // given
            SerializationOptions options = SerializationOptions.builder()
                    .set(MessageFormatOption.PADDING, true)
                    .set(MessageFormatOption.LENGTH_CHECK, true)
                    .build();

            // when
            Map<String, Object> all = options.getAll();

            // then
            assertEquals(2, all.size());
            assertTrue((Boolean) all.get(MessageFormatOption.PADDING.key()));
            assertTrue((Boolean) all.get(MessageFormatOption.LENGTH_CHECK.key()));
        }
    }

    @Nested
    @DisplayName("static 상수 테스트")
    class StaticConstantsTests {

        @Test
        @DisplayName("fixedLengthSerializationOptions 상수가 존재한다")
        void hasFixedLengthOptions() {
            // then
            assertNotNull(SerializationOptions.fixedLengthSerializationOptions);
        }

        @Test
        @DisplayName("jsonSerializationOptions 상수가 존재한다")
        void hasJsonOptions() {
            // then
            assertNotNull(SerializationOptions.jsonSerializationOptions);
        }
    }
}
