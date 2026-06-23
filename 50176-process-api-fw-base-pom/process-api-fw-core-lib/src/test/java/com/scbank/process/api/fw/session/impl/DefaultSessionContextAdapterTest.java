package com.scbank.process.api.fw.session.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextAdapter;

/**
 * {@link DefaultSessionContextAdapter} 단위 테스트
 */
@DisplayName("DefaultSessionContextAdapter 테스트")
@ExtendWith(MockitoExtension.class)
class DefaultSessionContextAdapterTest {

    private DefaultSessionContextAdapter adapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        adapter = new DefaultSessionContextAdapter(objectMapper);
    }

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTests {

        @Test
        @DisplayName("ObjectMapper를 주입받아 생성할 수 있다")
        void createWithObjectMapper() {
            // when
            DefaultSessionContextAdapter newAdapter = new DefaultSessionContextAdapter(objectMapper);

            // then
            assertNotNull(newAdapter);
        }
    }

    @Nested
    @DisplayName("adapt 메서드 테스트")
    class AdaptMethodTests {

        @Test
        @DisplayName("ISessionContext 타입은 그대로 반환한다")
        void returnsISessionContextAsIs() {
            // given
            DefaultSessionContext context = new DefaultSessionContext();

            // when
            ISessionContext result = adapter.adapt(context);

            // then
            assertSame(context, result);
        }

        @Test
        @DisplayName("Map을 DefaultSessionContext로 변환한다")
        void convertsMapToDefaultSessionContext() {
            // given
            Map<String, Object> raw = new HashMap<>();
            raw.put("sessionId", "session123");
            raw.put("clientIp", "192.168.1.1");

            // when
            ISessionContext result = adapter.adapt(raw);

            // then
            assertInstanceOf(DefaultSessionContext.class, result);
            assertEquals("session123", result.getSessionId());
            assertEquals("192.168.1.1", result.getClientIp());
        }

        @Test
        @DisplayName("JSON 문자열을 DefaultSessionContext로 변환한다")
        void convertsJsonStringToDefaultSessionContext() {
            // given
            String json = "{\"sessionId\":\"session456\",\"clientIp\":\"10.0.0.1\"}";

            // when
            ISessionContext result = adapter.adapt(json);

            // then
            assertInstanceOf(DefaultSessionContext.class, result);
            assertEquals("session456", result.getSessionId());
            assertEquals("10.0.0.1", result.getClientIp());
        }

        @Test
        @DisplayName("유효하지 않은 JSON 문자열은 IllegalArgumentException을 발생시킨다")
        void throwsExceptionForInvalidJsonString() {
            // given
            String invalidJson = "not a valid json";

            // then
            assertThrows(IllegalArgumentException.class, () -> adapter.adapt(invalidJson));
        }

        @Test
        @DisplayName("지원하지 않는 타입은 IllegalArgumentException을 발생시킨다")
        void throwsExceptionForUnsupportedType() {
            // given
            Integer unsupportedType = 123;

            // then
            assertThrows(IllegalArgumentException.class, () -> adapter.adapt(unsupportedType));
        }
    }

    @Nested
    @DisplayName("인터페이스 구현 테스트")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("ISessionContextAdapter 인터페이스를 구현한다")
        void implementsISessionContextAdapter() {
            // then
            assertInstanceOf(ISessionContextAdapter.class, adapter);
        }
    }
}
