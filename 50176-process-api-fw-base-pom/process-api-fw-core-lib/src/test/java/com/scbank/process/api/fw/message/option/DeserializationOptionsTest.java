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
 * {@link DeserializationOptions} 단위 테스트
 */
@DisplayName("DeserializationOptions 테스트")
class DeserializationOptionsTest {

    @Nested
    @DisplayName("Builder 테스트")
    class BuilderTests {

        @Test
        @DisplayName("빈 옵션으로 생성할 수 있다")
        void createEmptyOptions() {
            // when
            DeserializationOptions options = DeserializationOptions.builder().build();

            // then
            assertNotNull(options);
        }

        @Test
        @DisplayName("Boolean 옵션을 설정할 수 있다")
        void setBooleanOption() {
            // when
            DeserializationOptions options = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .build();

            // then
            assertTrue(options.enabled(MessageFormatOption.UNPADDING));
        }

        @Test
        @DisplayName("여러 옵션을 체이닝하여 설정할 수 있다")
        void chainMultipleOptions() {
            // when
            DeserializationOptions options = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .set(MessageFormatOption.FIELD_TRIM, true)
                    .set(MessageFormatOption.DECRYPT, false)
                    .build();

            // then
            assertTrue(options.enabled(MessageFormatOption.UNPADDING));
            assertTrue(options.enabled(MessageFormatOption.FIELD_TRIM));
            assertFalse(options.enabled(MessageFormatOption.DECRYPT));
        }

        @Test
        @DisplayName("RoundingMode 옵션을 설정할 수 있다")
        void setRoundingModeOption() {
            // when
            DeserializationOptions options = DeserializationOptions.builder()
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
            DeserializationOptions options = DeserializationOptions.builder()
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
            Optional<DeserializationOptions> options = DeserializationOptions.getDefaultOptions(MessageFormat.FIXEDLENGTH);

            // then
            assertTrue(options.isPresent());
            assertTrue(options.get().enabled(MessageFormatOption.UNPADDING));
            assertTrue(options.get().enabled(MessageFormatOption.FIELD_TRIM));
        }

        @Test
        @DisplayName("JSON 포맷의 기본 옵션을 조회할 수 있다")
        void getJsonDefaultOptions() {
            // when
            Optional<DeserializationOptions> options = DeserializationOptions.getDefaultOptions(MessageFormat.JSON);

            // then
            assertTrue(options.isPresent());
            assertTrue(options.get().enabled(MessageFormatOption.FIELD_TRIM));
        }

        @Test
        @DisplayName("XML 포맷의 기본 옵션을 조회할 수 있다")
        void getXmlDefaultOptions() {
            // when
            Optional<DeserializationOptions> options = DeserializationOptions.getDefaultOptions(MessageFormat.XML);

            // then
            assertTrue(options.isPresent());
        }

        @Test
        @DisplayName("FORM 포맷의 기본 옵션을 조회할 수 있다")
        void getFormDefaultOptions() {
            // when
            Optional<DeserializationOptions> options = DeserializationOptions.getDefaultOptions(MessageFormat.FORM);

            // then
            assertTrue(options.isPresent());
        }

        @Test
        @DisplayName("MULTIPART_FORM 포맷의 기본 옵션을 조회할 수 있다")
        void getMultipartFormDefaultOptions() {
            // when
            Optional<DeserializationOptions> options = DeserializationOptions.getDefaultOptions(MessageFormat.MULTIPART_FORM);

            // then
            assertTrue(options.isPresent());
        }

        @Test
        @DisplayName("지원하지 않는 포맷은 빈 Optional을 반환한다")
        void returnsEmptyForUnsupportedFormat() {
            // when
            Optional<DeserializationOptions> options = DeserializationOptions.getDefaultOptions(MessageFormat.NONE);

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
            DeserializationOptions original = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .set(MessageFormatOption.FIELD_TRIM, true)
                    .build();

            // when
            DeserializationOptions copy = DeserializationOptions.of(original);

            // then
            assertNotNull(copy);
            assertNotSame(original, copy);
            assertTrue(copy.enabled(MessageFormatOption.UNPADDING));
            assertTrue(copy.enabled(MessageFormatOption.FIELD_TRIM));
        }

        @Test
        @DisplayName("복제 후 원본을 수정해도 복제본에 영향을 주지 않는다")
        void copyIsIndependent() {
            // given
            DeserializationOptions original = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .build();
            DeserializationOptions copy = DeserializationOptions.of(original);

            // when - 원본 수정
            original.set(MessageFormatOption.DECRYPT, true);

            // then - 복제본은 영향 없음
            assertFalse(copy.enabled(MessageFormatOption.DECRYPT));
        }
    }

    @Nested
    @DisplayName("merge 테스트")
    class MergeTests {

        @Test
        @DisplayName("기본 옵션과 확장 옵션을 병합할 수 있다")
        void mergeOptions() {
            // given
            DeserializationOptions base = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .build();
            Map<String, Object> overrides = new HashMap<>();
            overrides.put(MessageFormatOption.FIELD_TRIM.key(), true);

            // when
            DeserializationOptions merged = DeserializationOptions.merge(base, overrides);

            // then
            assertTrue(merged.enabled(MessageFormatOption.UNPADDING));
            assertTrue(merged.enabled(MessageFormatOption.FIELD_TRIM));
        }

        @Test
        @DisplayName("확장 옵션이 null이면 기본 옵션만 복제한다")
        void mergeWithNullOverrides() {
            // given
            DeserializationOptions base = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .build();

            // when
            DeserializationOptions merged = DeserializationOptions.merge(base, null);

            // then
            assertTrue(merged.enabled(MessageFormatOption.UNPADDING));
        }

        @Test
        @DisplayName("확장 옵션이 기존 값을 덮어쓴다")
        void overridesExistingValues() {
            // given
            DeserializationOptions base = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .build();
            Map<String, Object> overrides = new HashMap<>();
            overrides.put(MessageFormatOption.UNPADDING.key(), false);

            // when
            DeserializationOptions merged = DeserializationOptions.merge(base, overrides);

            // then
            assertFalse(merged.enabled(MessageFormatOption.UNPADDING));
        }
    }

    @Nested
    @DisplayName("enabled 테스트")
    class EnabledTests {

        @Test
        @DisplayName("설정되지 않은 옵션은 false를 반환한다")
        void unsetOptionReturnsFalse() {
            // given
            DeserializationOptions options = DeserializationOptions.builder().build();

            // when
            boolean result = options.enabled(MessageFormatOption.UNPADDING);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("false로 설정된 옵션은 false를 반환한다")
        void falseOptionReturnsFalse() {
            // given
            DeserializationOptions options = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, false)
                    .build();

            // when
            boolean result = options.enabled(MessageFormatOption.UNPADDING);

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("true로 설정된 옵션은 true를 반환한다")
        void trueOptionReturnsTrue() {
            // given
            DeserializationOptions options = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .build();

            // when
            boolean result = options.enabled(MessageFormatOption.UNPADDING);

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
            DeserializationOptions options = DeserializationOptions.builder()
                    .set(MessageFormatOption.UNPADDING, true)
                    .set(MessageFormatOption.FIELD_TRIM, true)
                    .build();

            // when
            Map<String, Object> all = options.getAll();

            // then
            assertEquals(2, all.size());
            assertTrue((Boolean) all.get(MessageFormatOption.UNPADDING.key()));
            assertTrue((Boolean) all.get(MessageFormatOption.FIELD_TRIM.key()));
        }
    }

    @Nested
    @DisplayName("static 상수 테스트")
    class StaticConstantsTests {

        @Test
        @DisplayName("fixedLengthDeserializationOptions 상수가 존재한다")
        void hasFixedLengthOptions() {
            // then
            assertNotNull(DeserializationOptions.fixedLengthDeserializationOptions);
        }

        @Test
        @DisplayName("jsonDeserializationOptions 상수가 존재한다")
        void hasJsonOptions() {
            // then
            assertNotNull(DeserializationOptions.jsonDeserializationOptions);
        }
    }
}
