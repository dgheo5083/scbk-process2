package com.scbank.process.api.fw.message;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;

/**
 * {@link MessageProperties} 단위 테스트
 */
@DisplayName("MessageProperties 테스트")
class MessagePropertiesTest {

    private MessageProperties properties;

    @BeforeEach
    void setUp() {
        properties = new MessageProperties();
    }

    @Nested
    @DisplayName("enabled 속성 테스트")
    class EnabledPropertyTests {

        @Test
        @DisplayName("기본값은 false이다")
        void defaultEnabledIsFalse() {
            // then
            assertFalse(properties.isEnabled());
        }

        @Test
        @DisplayName("enabled를 설정할 수 있다")
        void setEnabled() {
            // when
            properties.setEnabled(true);

            // then
            assertTrue(properties.isEnabled());
        }
    }

    @Nested
    @DisplayName("defaultEncoding 속성 테스트")
    class DefaultEncodingTests {

        @Test
        @DisplayName("기본값은 UTF-8이다")
        void defaultEncodingIsUtf8() {
            // then
            assertEquals(StandardCharsets.UTF_8.toString(), properties.getDefaultEncoding());
        }

        @Test
        @DisplayName("defaultEncoding을 설정할 수 있다")
        void setDefaultEncoding() {
            // when
            properties.setDefaultEncoding("EUC-KR");

            // then
            assertEquals("EUC-KR", properties.getDefaultEncoding());
        }
    }

    @Nested
    @DisplayName("basePackages 속성 테스트")
    class BasePackagesTests {

        @Test
        @DisplayName("기본값은 null이다")
        void defaultBasePackagesIsNull() {
            // then
            assertNull(properties.getBasePackages());
        }

        @Test
        @DisplayName("basePackages를 설정할 수 있다")
        void setBasePackages() {
            // given
            List<String> packages = Arrays.asList("com.example.message", "com.example.dto");

            // when
            properties.setBasePackages(packages);

            // then
            assertEquals(2, properties.getBasePackages().size());
            assertTrue(properties.getBasePackages().contains("com.example.message"));
            assertTrue(properties.getBasePackages().contains("com.example.dto"));
        }

        @Test
        @DisplayName("빈 리스트를 설정할 수 있다")
        void setEmptyBasePackages() {
            // when
            properties.setBasePackages(Arrays.asList());

            // then
            assertNotNull(properties.getBasePackages());
            assertTrue(properties.getBasePackages().isEmpty());
        }
    }

    @Nested
    @DisplayName("annotationClass 속성 테스트")
    class AnnotationClassTests {

        @Test
        @DisplayName("기본값은 IntegrationMessage.class이다")
        void defaultAnnotationClass() {
            // then
            assertEquals(IntegrationMessage.class, properties.getAnnotationClass());
        }

        @Test
        @DisplayName("annotationClass를 설정할 수 있다")
        void setAnnotationClass() {
            // when - IntegrationMessage 타입만 허용됨
            properties.setAnnotationClass(IntegrationMessage.class);

            // then
            assertEquals(IntegrationMessage.class, properties.getAnnotationClass());
        }
    }

    @Nested
    @DisplayName("useDebugLog 속성 테스트")
    class UseDebugLogTests {

        @Test
        @DisplayName("기본값은 false이다")
        void defaultUseDebugLogIsFalse() {
            // then
            assertFalse(properties.isUseDebugLog());
        }

        @Test
        @DisplayName("useDebugLog를 설정할 수 있다")
        void setUseDebugLog() {
            // when
            properties.setUseDebugLog(true);

            // then
            assertTrue(properties.isUseDebugLog());
        }
    }

    @Nested
    @DisplayName("Lombok 생성 메서드 테스트")
    class LombokMethodsTests {

        @Test
        @DisplayName("toString() 메서드가 정상 동작한다")
        void toStringMethod() {
            // when
            String result = properties.toString();

            // then
            assertNotNull(result);
            assertTrue(result.contains("MessageProperties"));
        }

        @Test
        @DisplayName("equals() 메서드가 정상 동작한다")
        void equalsMethod() {
            // given
            MessageProperties other = new MessageProperties();
            other.setEnabled(properties.isEnabled());
            other.setDefaultEncoding(properties.getDefaultEncoding());
            other.setUseDebugLog(properties.isUseDebugLog());

            // then
            assertEquals(properties, other);
        }

        @Test
        @DisplayName("hashCode() 메서드가 정상 동작한다")
        void hashCodeMethod() {
            // given
            MessageProperties other = new MessageProperties();
            other.setEnabled(properties.isEnabled());
            other.setDefaultEncoding(properties.getDefaultEncoding());
            other.setUseDebugLog(properties.isUseDebugLog());

            // then
            assertEquals(properties.hashCode(), other.hashCode());
        }
    }
}
