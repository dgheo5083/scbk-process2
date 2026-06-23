package com.scbank.process.api.fw.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LocaleUtils 테스트")
class LocaleUtilsTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        LocaleUtils localeUtils = new LocaleUtils();
        assertNotNull(localeUtils);
    }

    @Nested
    @DisplayName("toLocale 메서드 테스트")
    class ToLocaleTests {

        @Test
        @DisplayName("toLocale - null 입력")
        void toLocale_null() {
            assertNull(LocaleUtils.toLocale(null));
        }

        @Test
        @DisplayName("toLocale - 2자리 언어 코드 (en)")
        void toLocale_2chars() {
            Locale result = LocaleUtils.toLocale("en");

            assertNotNull(result);
            assertEquals("en", result.getLanguage());
            assertEquals("", result.getCountry());
        }

        @Test
        @DisplayName("toLocale - 5자리 언어_국가 코드 (en_GB)")
        void toLocale_5chars() {
            Locale result = LocaleUtils.toLocale("en_GB");

            assertNotNull(result);
            assertEquals("en", result.getLanguage());
            assertEquals("GB", result.getCountry());
        }

        @Test
        @DisplayName("toLocale - 7자리 이상 언어_국가_변형 코드 (en_GB_xxx)")
        void toLocale_7chars() {
            Locale result = LocaleUtils.toLocale("en_GB_xxx");

            assertNotNull(result);
            assertEquals("en", result.getLanguage());
            assertEquals("GB", result.getCountry());
            assertEquals("xxx", result.getVariant());
        }

        @Test
        @DisplayName("toLocale - 언어__변형 코드 (en__xxx)")
        void toLocale_withEmptyCountry() {
            Locale result = LocaleUtils.toLocale("en__xxx");

            assertNotNull(result);
            assertEquals("en", result.getLanguage());
            assertEquals("", result.getCountry());
            assertEquals("xxx", result.getVariant());
        }

        @Test
        @DisplayName("toLocale - 잘못된 길이")
        void toLocale_invalidLength() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("e");
            });
        }

        @Test
        @DisplayName("toLocale - 잘못된 언어 코드 (대문자)")
        void toLocale_invalidLanguage() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("EN");
            });
        }

        @Test
        @DisplayName("toLocale - 3자리에 구분자 없음")
        void toLocale_noSeparator() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("enGB");
            });
        }

        @Test
        @DisplayName("toLocale - 잘못된 국가 코드 (소문자)")
        void toLocale_invalidCountry() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("en_gb");
            });
        }

        @Test
        @DisplayName("toLocale - 6자리에 구분자 없음")
        void toLocale_noSecondSeparator() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("en_GBx");
            });
        }

        @Test
        @DisplayName("toLocale - 언어 코드 첫 글자 유효하지 않음")
        void toLocale_invalidFirstChar() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("1n");
            });
        }

        @Test
        @DisplayName("toLocale - 언어 코드 두 번째 글자 유효하지 않음")
        void toLocale_invalidSecondChar() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("e1");
            });
        }

        @Test
        @DisplayName("toLocale - 국가 코드 첫 글자 유효하지 않음")
        void toLocale_invalidCountryFirstChar() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("en_1B");
            });
        }

        @Test
        @DisplayName("toLocale - 국가 코드 두 번째 글자 유효하지 않음")
        void toLocale_invalidCountrySecondChar() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("en_G1");
            });
        }

        @Test
        @DisplayName("toLocale - 3자리 잘못된 형식")
        void toLocale_3charsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("abc");
            });
        }

        @Test
        @DisplayName("toLocale - 4자리 잘못된 형식")
        void toLocale_4charsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("abcd");
            });
        }

        @Test
        @DisplayName("toLocale - 6자리 잘못된 형식")
        void toLocale_6charsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                LocaleUtils.toLocale("en_GBx");
            });
        }
    }

    @Nested
    @DisplayName("localeLookupList 메서드 테스트")
    class LocaleLookupListTests {

        @Test
        @DisplayName("localeLookupList - 단일 locale")
        void localeLookupList_single() {
            Locale locale = new Locale("en");
            List<Locale> result = LocaleUtils.localeLookupList(locale);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.contains(locale));
        }

        @Test
        @DisplayName("localeLookupList - 국가 포함 locale")
        void localeLookupList_withCountry() {
            Locale locale = new Locale("en", "GB");
            List<Locale> result = LocaleUtils.localeLookupList(locale);

            assertNotNull(result);
            assertTrue(result.size() >= 2);
            assertTrue(result.contains(locale));
            assertTrue(result.contains(new Locale("en")));
        }

        @Test
        @DisplayName("localeLookupList - 변형 포함 locale")
        void localeLookupList_withVariant() {
            Locale locale = new Locale("en", "GB", "xxx");
            List<Locale> result = LocaleUtils.localeLookupList(locale);

            assertNotNull(result);
            assertTrue(result.size() >= 3);
            assertTrue(result.contains(locale));
            assertTrue(result.contains(new Locale("en", "GB")));
            assertTrue(result.contains(new Locale("en")));
        }

        @Test
        @DisplayName("localeLookupList - defaultLocale 지정")
        void localeLookupList_withDefault() {
            Locale locale = new Locale("fr", "CA");
            Locale defaultLocale = new Locale("en");
            List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);

            assertNotNull(result);
            assertTrue(result.contains(locale));
            assertTrue(result.contains(defaultLocale));
        }

        @Test
        @DisplayName("localeLookupList - null locale")
        void localeLookupList_null() {
            Locale defaultLocale = new Locale("en");
            List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("localeLookupList - defaultLocale이 이미 포함됨")
        void localeLookupList_defaultAlreadyIncluded() {
            Locale locale = new Locale("en", "GB");
            Locale defaultLocale = new Locale("en");
            List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);

            // 중복되지 않아야 함
            long count = result.stream().filter(l -> l.equals(defaultLocale)).count();
            assertEquals(1, count);
        }
    }

    @Test
    @DisplayName("availableLocaleList")
    void availableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("availableLocaleSet")
    void availableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("availableLocaleSet - 캐싱 확인")
    void availableLocaleSet_cached() {
        Set<Locale> result1 = LocaleUtils.availableLocaleSet();
        Set<Locale> result2 = LocaleUtils.availableLocaleSet();

        assertSame(result1, result2);
    }

    @Nested
    @DisplayName("isAvailableLocale 메서드 테스트")
    class IsAvailableLocaleTests {

        @Test
        @DisplayName("isAvailableLocale - 사용 가능한 locale")
        void isAvailableLocale_available() {
            Locale locale = Locale.ENGLISH;

            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        @Test
        @DisplayName("isAvailableLocale - 사용 불가능한 locale")
        void isAvailableLocale_notAvailable() {
            Locale locale = new Locale("xx", "YY", "zzz");

            assertFalse(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Nested
    @DisplayName("languagesByCountry 메서드 테스트")
    class LanguagesByCountryTests {

        @Test
        @DisplayName("languagesByCountry - 유효한 국가 코드")
        void languagesByCountry_valid() {
            List<Locale> result = LocaleUtils.languagesByCountry("US");

            assertNotNull(result);
        }

        @Test
        @DisplayName("languagesByCountry - null 국가 코드")
        void languagesByCountry_null() {
            List<Locale> result = LocaleUtils.languagesByCountry(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("languagesByCountry - 캐싱 확인")
        void languagesByCountry_cached() {
            List<Locale> result1 = LocaleUtils.languagesByCountry("GB");
            List<Locale> result2 = LocaleUtils.languagesByCountry("GB");

            assertSame(result1, result2);
        }

        @Test
        @DisplayName("languagesByCountry - 존재하지 않는 국가 코드")
        void languagesByCountry_nonExistent() {
            List<Locale> result = LocaleUtils.languagesByCountry("XX");

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("countriesByLanguage 메서드 테스트")
    class CountriesByLanguageTests {

        @Test
        @DisplayName("countriesByLanguage - 유효한 언어 코드")
        void countriesByLanguage_valid() {
            List<Locale> result = LocaleUtils.countriesByLanguage("en");

            assertNotNull(result);
        }

        @Test
        @DisplayName("countriesByLanguage - null 언어 코드")
        void countriesByLanguage_null() {
            List<Locale> result = LocaleUtils.countriesByLanguage(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("countriesByLanguage - 캐싱 확인")
        void countriesByLanguage_cached() {
            List<Locale> result1 = LocaleUtils.countriesByLanguage("en");
            List<Locale> result2 = LocaleUtils.countriesByLanguage("en");

            assertSame(result1, result2);
        }

        @Test
        @DisplayName("countriesByLanguage - 존재하지 않는 언어 코드")
        void countriesByLanguage_nonExistent() {
            List<Locale> result = LocaleUtils.countriesByLanguage("xx");

            assertNotNull(result);
        }
    }
}
