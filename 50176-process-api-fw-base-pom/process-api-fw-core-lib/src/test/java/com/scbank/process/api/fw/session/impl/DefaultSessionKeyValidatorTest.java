package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.session.ISessionKeyValidator;
import com.scbank.process.api.fw.session.SessionProperties;
import com.scbank.process.api.fw.session.SessionScope;

/**
 * {@link DefaultSessionKeyValidator} 단위 테스트
 */
@DisplayName("DefaultSessionKeyValidator 테스트")
@ExtendWith(MockitoExtension.class)
class DefaultSessionKeyValidatorTest {

    private DefaultSessionKeyValidator validator;

    @Mock
    private SessionProperties sessionProperties;

    @BeforeEach
    void setUp() {
        Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
        allowedKeys.put(SessionScope.GLOBAL, Arrays.asList("traceId", "channelId"));
        allowedKeys.put(SessionScope.LOGIN, Arrays.asList("userId", "userRole"));

        when(sessionProperties.isEnabled()).thenReturn(true);
        when(sessionProperties.getAllowedKeys()).thenReturn(allowedKeys);

        validator = new DefaultSessionKeyValidator(sessionProperties);
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("SessionProperties를 주입받아 생성할 수 있다")
        void createWithSessionProperties() {
            // then
            assertNotNull(validator);
        }
    }

    @Nested
    @DisplayName("isAllowedLoginKey 메서드 테스트")
    class IsAllowedLoginKeyTests {

        @Test
        @DisplayName("허용된 로그인 키는 true를 반환한다")
        void returnsTrueForAllowedLoginKey() {
            // when
            boolean result = validator.isAllowedLoginKey("userId");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("허용되지 않은 로그인 키는 false를 반환한다")
        void returnsFalseForDisallowedLoginKey() {
            // when
            boolean result = validator.isAllowedLoginKey("unknownKey");

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("비활성화 상태에서는 모든 키가 허용된다")
        void allowsAllKeysWhenDisabled() {
            // given
            when(sessionProperties.isEnabled()).thenReturn(false);
            DefaultSessionKeyValidator disabledValidator = new DefaultSessionKeyValidator(sessionProperties);

            // when
            boolean result = disabledValidator.isAllowedLoginKey("anyKey");

            // then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("isAllowedGlobalKey 메서드 테스트")
    class IsAllowedGlobalKeyTests {

        @Test
        @DisplayName("허용된 글로벌 키는 true를 반환한다")
        void returnsTrueForAllowedGlobalKey() {
            // when
            boolean result = validator.isAllowedGlobalKey("traceId");

            // then
            assertTrue(result);
        }

        @Test
        @DisplayName("허용되지 않은 글로벌 키는 false를 반환한다")
        void returnsFalseForDisallowedGlobalKey() {
            // when
            boolean result = validator.isAllowedGlobalKey("unknownKey");

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("비활성화 상태에서는 모든 키가 허용된다")
        void allowsAllKeysWhenDisabled() {
            // given
            when(sessionProperties.isEnabled()).thenReturn(false);
            DefaultSessionKeyValidator disabledValidator = new DefaultSessionKeyValidator(sessionProperties);

            // when
            boolean result = disabledValidator.isAllowedGlobalKey("anyKey");

            // then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("키 목록이 비어있는 경우 테스트")
    class EmptyAllowedKeysTests {

        @Test
        @DisplayName("LOGIN 키 목록이 없으면 모든 로그인 키가 거부된다")
        void rejectsAllLoginKeysWhenNoLoginKeysConfigured() {
            // given
            Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
            allowedKeys.put(SessionScope.GLOBAL, Arrays.asList("traceId"));
            // LOGIN 키 없음

            when(sessionProperties.getAllowedKeys()).thenReturn(allowedKeys);
            DefaultSessionKeyValidator validatorWithNoLoginKeys = new DefaultSessionKeyValidator(sessionProperties);

            // when
            boolean result = validatorWithNoLoginKeys.isAllowedLoginKey("userId");

            // then
            assertFalse(result);
        }

        @Test
        @DisplayName("GLOBAL 키 목록이 없으면 모든 글로벌 키가 거부된다")
        void rejectsAllGlobalKeysWhenNoGlobalKeysConfigured() {
            // given
            Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
            allowedKeys.put(SessionScope.LOGIN, Arrays.asList("userId"));
            // GLOBAL 키 없음

            when(sessionProperties.getAllowedKeys()).thenReturn(allowedKeys);
            DefaultSessionKeyValidator validatorWithNoGlobalKeys = new DefaultSessionKeyValidator(sessionProperties);

            // when
            boolean result = validatorWithNoGlobalKeys.isAllowedGlobalKey("traceId");

            // then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionKeyValidator 인터페이스를 구현한다")
        void implementsISessionKeyValidator() {
            // then
            assertInstanceOf(ISessionKeyValidator.class, validator);
        }
    }
}
