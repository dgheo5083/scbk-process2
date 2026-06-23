package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link SessionAdapterProperties} 단위 테스트
 */
@DisplayName("SessionAdapterProperties 테스트")
class SessionAdapterPropertiesTest {

    private SessionAdapterProperties properties;

    @BeforeEach
    void setUp() {
        properties = new SessionAdapterProperties();
    }

    @Nested
    @DisplayName("enabled 속성 테스트")
    class EnabledPropertyTests {

        @Test
        @DisplayName("기본 enabled 값은 false이다")
        void defaultEnabledIsFalse() {
            // then
            assertFalse(properties.isEnabled());
        }

        @Test
        @DisplayName("enabled 값을 true로 설정할 수 있다")
        void setEnabledTrue() {
            // when
            properties.setEnabled(true);

            // then
            assertTrue(properties.isEnabled());
        }

        @Test
        @DisplayName("enabled 값을 false로 설정할 수 있다")
        void setEnabledFalse() {
            // given
            properties.setEnabled(true);

            // when
            properties.setEnabled(false);

            // then
            assertFalse(properties.isEnabled());
        }
    }

    @Nested
    @DisplayName("rulePath 속성 테스트")
    class RulePathPropertyTests {

        @Test
        @DisplayName("기본 rulePath 값은 null이다")
        void defaultRulePathIsNull() {
            // then
            assertNull(properties.getRulePath());
        }

        @Test
        @DisplayName("rulePath 값을 설정할 수 있다")
        void canSetRulePath() {
            // when
            properties.setRulePath("config/session-adapter-rules.json");

            // then
            assertEquals("config/session-adapter-rules.json", properties.getRulePath());
        }
    }

    @Nested
    @DisplayName("Lombok 생성 메서드 테스트")
    class LombokGeneratedMethodTests {

        @Test
        @DisplayName("toString 메서드가 정상 동작한다")
        void toStringWorks() {
            // when
            String result = properties.toString();

            // then
            assertNotNull(result);
            assertTrue(result.contains("SessionAdapterProperties"));
        }

        @Test
        @DisplayName("equals 메서드가 정상 동작한다")
        void equalsWorks() {
            // given
            SessionAdapterProperties other = new SessionAdapterProperties();

            // then
            assertEquals(properties, other);
        }

        @Test
        @DisplayName("hashCode 메서드가 정상 동작한다")
        void hashCodeWorks() {
            // given
            SessionAdapterProperties other = new SessionAdapterProperties();

            // then
            assertEquals(properties.hashCode(), other.hashCode());
        }
    }
}
