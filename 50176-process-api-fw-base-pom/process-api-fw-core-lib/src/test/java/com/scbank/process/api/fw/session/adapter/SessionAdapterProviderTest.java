package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link SessionAdapterProvider} 단위 테스트
 */
@DisplayName("SessionAdapterProvider 테스트")
@ExtendWith(MockitoExtension.class)
class SessionAdapterProviderTest {

    private SessionAdapterProvider provider;

    @Mock
    private ISessionAdapter adapter1;

    @Mock
    private ISessionAdapter adapter2;

    @BeforeEach
    void setUp() {
        List<ISessionAdapter> adapters = Arrays.asList(adapter1, adapter2);
        provider = new SessionAdapterProvider(adapters);
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("어댑터 목록을 주입받아 생성할 수 있다")
        void createWithAdapters() {
            // given
            List<ISessionAdapter> adapters = Collections.singletonList(adapter1);

            // when
            SessionAdapterProvider newProvider = new SessionAdapterProvider(adapters);

            // then
            assertNotNull(newProvider);
        }

        @Test
        @DisplayName("null 어댑터 목록으로 생성할 수 있다")
        void createWithNullAdapters() {
            // when
            SessionAdapterProvider newProvider = new SessionAdapterProvider(null);

            // then
            assertNotNull(newProvider);
        }
    }

    @Nested
    @DisplayName("wrap 메서드 테스트")
    class WrapMethodTests {

        @Test
        @DisplayName("지원하는 어댑터가 있으면 wrap을 수행한다")
        void wrapsWithSupportingAdapter() {
            // given
            String sessionKey = "testKey";
            Object value = "testValue";
            Object wrappedValue = "wrappedValue";

            when(adapter1.supports(sessionKey, value)).thenReturn(true);
            when(adapter1.wrap(sessionKey, value)).thenReturn(wrappedValue);

            // when
            Object result = provider.wrap(sessionKey, value);

            // then
            assertEquals(wrappedValue, result);
            verify(adapter1).wrap(sessionKey, value);
        }

        @Test
        @DisplayName("첫 번째로 지원하는 어댑터를 사용한다")
        void usesFirstSupportingAdapter() {
            // given
            String sessionKey = "testKey";
            Object value = "testValue";
            Object wrappedValue1 = "wrappedValue1";

            when(adapter1.supports(sessionKey, value)).thenReturn(true);
            when(adapter1.wrap(sessionKey, value)).thenReturn(wrappedValue1);

            // when
            Object result = provider.wrap(sessionKey, value);

            // then
            assertEquals(wrappedValue1, result);
            verify(adapter2, never()).supports(anyString(), any());
        }

        @Test
        @DisplayName("지원하는 어댑터가 없으면 원본 값을 반환한다")
        void returnsOriginalWhenNoSupportingAdapter() {
            // given
            String sessionKey = "testKey";
            Object value = "testValue";

            when(adapter1.supports(sessionKey, value)).thenReturn(false);
            when(adapter2.supports(sessionKey, value)).thenReturn(false);

            // when
            Object result = provider.wrap(sessionKey, value);

            // then
            assertEquals(value, result);
        }

        @Test
        @DisplayName("어댑터 목록이 null이면 원본 값을 반환한다")
        void returnsOriginalWhenAdaptersIsNull() {
            // given
            SessionAdapterProvider nullProvider = new SessionAdapterProvider(null);
            String sessionKey = "testKey";
            Object value = "testValue";

            // when
            Object result = nullProvider.wrap(sessionKey, value);

            // then
            assertEquals(value, result);
        }
    }

    @Nested
    @DisplayName("unwrap 메서드 테스트")
    class UnwrapMethodTests {

        @Test
        @DisplayName("지원하는 어댑터가 있으면 unwrap을 수행한다")
        void unwrapsWithSupportingAdapter() {
            // given
            String sessionKey = "testKey";
            Object value = "wrappedValue";
            Object unwrappedValue = "unwrappedValue";

            when(adapter1.supports(sessionKey, value)).thenReturn(true);
            when(adapter1.unwrap(sessionKey, value)).thenReturn(unwrappedValue);

            // when
            Object result = provider.unwrap(sessionKey, value);

            // then
            assertEquals(unwrappedValue, result);
            verify(adapter1).unwrap(sessionKey, value);
        }

        @Test
        @DisplayName("지원하는 어댑터가 없으면 원본 값을 반환한다")
        void returnsOriginalWhenNoSupportingAdapter() {
            // given
            String sessionKey = "testKey";
            Object value = "testValue";

            when(adapter1.supports(sessionKey, value)).thenReturn(false);
            when(adapter2.supports(sessionKey, value)).thenReturn(false);

            // when
            Object result = provider.unwrap(sessionKey, value);

            // then
            assertEquals(value, result);
        }

        @Test
        @DisplayName("어댑터 목록이 null이면 원본 값을 반환한다")
        void returnsOriginalWhenAdaptersIsNull() {
            // given
            SessionAdapterProvider nullProvider = new SessionAdapterProvider(null);
            String sessionKey = "testKey";
            Object value = "testValue";

            // when
            Object result = nullProvider.unwrap(sessionKey, value);

            // then
            assertEquals(value, result);
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionAdapterProvider 인터페이스를 구현한다")
        void implementsISessionAdapterProvider() {
            // then
            assertInstanceOf(ISessionAdapterProvider.class, provider);
        }
    }
}
