package com.scbank.process.api.fw.security.xss.processor.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link AbstractJacksonXssProcessor} 의 공통 로직을 구상 클래스
 * {@link JsonXssProtectionProcessor} 를 통해 검증하는 단위 테스트.
 */
class AbstractJacksonXssProcessorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IXssSanitizer upperSanitizer = source -> source == null ? null : source.toUpperCase();

    @BeforeEach
    void setUp() {
        // SanitizedBodyRequestWrapper 가 기본 인코딩을 RuntimeContext 에서 읽으므로 초기화한다.
        MockEnvironment env = new MockEnvironment();
        env.setProperty("csl.runtime.default-encoding", "UTF-8");
        ReflectionTestUtils.setField(RuntimeContext.class, "environment", env);
    }

    @AfterEach
    void tearDown() {
        ReflectionTestUtils.setField(RuntimeContext.class, "environment", null);
    }

    private JsonXssProtectionProcessor processor(List<String> ignore) {
        return new JsonXssProtectionProcessor(objectMapper, upperSanitizer, ignore);
    }

    private MockHttpServletRequest jsonRequest(String body) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        request.setContent(body.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return request;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readBody(ServletRequest wrapped) throws Exception {
        byte[] bytes = ((HttpServletRequest) wrapped).getInputStream().readAllBytes();
        return objectMapper.readValue(bytes, new TypeReference<Map<String, Object>>() {
        });
    }

    @Test
    @DisplayName("HttpServletRequest 가 아니면 원본을 그대로 반환")
    void nonHttpReturnedAsIs() throws Exception {
        ServletRequest request = mock(ServletRequest.class);

        assertThat(processor(List.of()).sanitize(request)).isSameAs(request);
    }

    @Test
    @DisplayName("Body 가 비어 있으면 원본 요청을 그대로 반환")
    void emptyBodyReturnsOriginal() throws Exception {
        MockHttpServletRequest request = jsonRequest("");

        assertThat(processor(List.of()).sanitize(request)).isSameAs(request);
    }

    @Test
    @DisplayName("중첩된 Map/List/String 을 재귀적으로 정화한다")
    void recursivelySanitizesBody() throws Exception {
        MockHttpServletRequest request =
                jsonRequest("{\"a\":\"hi\",\"b\":[\"x\",\"y\"],\"c\":{\"d\":\"z\"},\"n\":5}");

        ServletRequest wrapped = processor(List.of()).sanitize(request);
        Map<String, Object> result = readBody(wrapped);

        assertThat(result.get("a")).isEqualTo("HI");
        assertThat(result.get("b")).isEqualTo(List.of("X", "Y"));
        assertThat(result.get("c")).isEqualTo(Map.of("d", "Z"));
        assertThat(result.get("n")).isEqualTo(5);
    }

    @Test
    @DisplayName("값이 ignoreFieldNames 에 포함되면 정화하지 않고 그대로 둔다")
    void ignoredValueNotSanitized() throws Exception {
        MockHttpServletRequest request = jsonRequest("{\"a\":\"keepme\"}");

        ServletRequest wrapped = processor(List.of("keepme")).sanitize(request);
        Map<String, Object> result = readBody(wrapped);

        assertThat(result.get("a")).isEqualTo("keepme");
    }

    @Test
    @DisplayName("파싱할 수 없는 Body 는 원본 요청을 그대로 반환(예외를 삼킨다)")
    void unparsableBodyReturnsOriginal() throws Exception {
        MockHttpServletRequest request = jsonRequest("this-is-not-json");

        assertThat(processor(List.of()).sanitize(request)).isSameAs(request);
    }

    @Test
    @DisplayName("character encoding 이 없으면 RuntimeContext 기본 인코딩으로 본문을 읽는다")
    void usesDefaultEncodingWhenCharsetAbsent() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent("{\"a\":\"hi\"}".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        // characterEncoding 미지정 -> RuntimeContext.getDefaultEncoding() 사용

        ServletRequest wrapped = processor(List.of()).sanitize(request);
        Map<String, Object> result = readBody(wrapped);

        assertThat(result.get("a")).isEqualTo("HI");
    }

    @Test
    @DisplayName("래퍼의 getReader 로도 정화된 Body 를 읽을 수 있다")
    void wrapperReaderReturnsSanitizedBody() throws Exception {
        MockHttpServletRequest request = jsonRequest("{\"a\":\"hi\"}");

        HttpServletRequest wrapped = (HttpServletRequest) processor(List.of()).sanitize(request);
        String body = wrapped.getReader().lines().reduce("", String::concat);

        assertThat(body).contains("HI");
    }
}
