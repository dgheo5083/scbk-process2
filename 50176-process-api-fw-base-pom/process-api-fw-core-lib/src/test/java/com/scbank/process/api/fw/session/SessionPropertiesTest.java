package com.scbank.process.api.fw.session;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link SessionProperties} 단위 테스트
 */
@DisplayName("SessionProperties 테스트")
class SessionPropertiesTest {

    private SessionProperties properties;

    @BeforeEach
    void setUp() {
        properties = new SessionProperties();
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
    @DisplayName("allowedKeys 속성 테스트")
    class AllowedKeysPropertyTests {

        @Test
        @DisplayName("기본 allowedKeys는 빈 맵이다")
        void defaultAllowedKeysIsEmptyMap() {
            // then
            assertNotNull(properties.getAllowedKeys());
            assertTrue(properties.getAllowedKeys().isEmpty());
        }

        @Test
        @DisplayName("allowedKeys에 GLOBAL 범위 키를 설정할 수 있다")
        void setGlobalAllowedKeys() {
            // given
            Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
            allowedKeys.put(SessionScope.GLOBAL, Arrays.asList("traceId", "channelId"));

            // when
            properties.setAllowedKeys(allowedKeys);

            // then
            assertEquals(2, properties.getAllowedKeys().get(SessionScope.GLOBAL).size());
            assertTrue(properties.getAllowedKeys().get(SessionScope.GLOBAL).contains("traceId"));
            assertTrue(properties.getAllowedKeys().get(SessionScope.GLOBAL).contains("channelId"));
        }

        @Test
        @DisplayName("allowedKeys에 LOGIN 범위 키를 설정할 수 있다")
        void setLoginAllowedKeys() {
            // given
            Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
            allowedKeys.put(SessionScope.LOGIN, Arrays.asList("userId", "userRole"));

            // when
            properties.setAllowedKeys(allowedKeys);

            // then
            assertEquals(2, properties.getAllowedKeys().get(SessionScope.LOGIN).size());
            assertTrue(properties.getAllowedKeys().get(SessionScope.LOGIN).contains("userId"));
            assertTrue(properties.getAllowedKeys().get(SessionScope.LOGIN).contains("userRole"));
        }

        @Test
        @DisplayName("allowedKeys에 여러 범위의 키를 설정할 수 있다")
        void setMultipleScopeAllowedKeys() {
            // given
            Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
            allowedKeys.put(SessionScope.GLOBAL, Arrays.asList("traceId"));
            allowedKeys.put(SessionScope.LOGIN, Arrays.asList("userId"));

            // when
            properties.setAllowedKeys(allowedKeys);

            // then
            assertEquals(2, properties.getAllowedKeys().size());
            assertEquals(1, properties.getAllowedKeys().get(SessionScope.GLOBAL).size());
            assertEquals(1, properties.getAllowedKeys().get(SessionScope.LOGIN).size());
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
            assertTrue(result.contains("SessionProperties"));
        }

        @Test
        @DisplayName("equals 메서드가 정상 동작한다")
        void equalsWorks() {
            // given
            SessionProperties other = new SessionProperties();

            // then
            assertEquals(properties, other);
        }

        @Test
        @DisplayName("hashCode 메서드가 정상 동작한다")
        void hashCodeWorks() {
            // given
            SessionProperties other = new SessionProperties();

            // then
            assertEquals(properties.hashCode(), other.hashCode());
        }
    }
}
