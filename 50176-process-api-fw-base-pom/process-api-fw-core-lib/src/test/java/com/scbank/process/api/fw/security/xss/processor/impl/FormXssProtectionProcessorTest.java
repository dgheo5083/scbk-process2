package com.scbank.process.api.fw.security.xss.processor.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link FormXssProtectionProcessor} 단위 테스트.
 *
 * <p>정화기는 입력 문자열을 대문자로 바꾸는 단순 구현을 사용하여 적용 여부를 검증한다.</p>
 */
class FormXssProtectionProcessorTest {

    /** 입력을 대문자로 변환하는 테스트용 sanitizer (null 안전). */
    private final IXssSanitizer upperSanitizer = source -> source == null ? null : source.toUpperCase();

    private FormXssProtectionProcessor processor(List<String> ignore) {
        return new FormXssProtectionProcessor(upperSanitizer, ignore);
    }

    @Test
    @DisplayName("form-urlencoded Content-Type 을 지원한다")
    void supportsForm() {
        assertThat(processor(List.of()).supports("application/x-www-form-urlencoded")).isTrue();
    }

    @Test
    @DisplayName("Content-Type 이 null 이면 지원한다")
    void supportsNullContentType() {
        assertThat(processor(List.of()).supports(null)).isTrue();
    }

    @Test
    @DisplayName("다른 Content-Type 은 지원하지 않는다")
    void doesNotSupportOther() {
        assertThat(processor(List.of()).supports("application/json")).isFalse();
    }

    @Test
    @DisplayName("HttpServletRequest 가 아니면 원본을 그대로 반환")
    void nonHttpRequestReturnedAsIs() throws Exception {
        ServletRequest request = mock(ServletRequest.class);

        assertThat(processor(List.of()).sanitize(request)).isSameAs(request);
    }

    @Test
    @DisplayName("getParameter 는 정화된 값을 반환")
    void getParameterSanitized() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("q", "alert");

        HttpServletRequest wrapped = (HttpServletRequest) processor(List.of()).sanitize(request);

        assertThat(wrapped.getParameter("q")).isEqualTo("ALERT");
    }

    @Test
    @DisplayName("getParameterValues 는 각 값을 정화하며, null 은 null 로 반환")
    void getParameterValuesSanitized() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("q", new String[] { "a", "b" });

        HttpServletRequest wrapped = (HttpServletRequest) processor(List.of()).sanitize(request);

        assertThat(wrapped.getParameterValues("q")).containsExactly("A", "B");
        assertThat(wrapped.getParameterValues("absent")).isNull();
    }

    @Test
    @DisplayName("getParameterNames 는 원본 파라미터 이름을 반환")
    void getParameterNames() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name1", "v1");
        request.addParameter("name2", "v2");

        HttpServletRequest wrapped = (HttpServletRequest) processor(List.of()).sanitize(request);

        assertThat(java.util.Collections.list(wrapped.getParameterNames()))
                .containsExactlyInAnyOrder("name1", "name2");
    }

    @Test
    @DisplayName("getParameterMap 은 정화된 값을 반환하며 ignore 필드는 제외한다")
    void getParameterMapSanitizedWithIgnore() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("q", new String[] { "x", "keep" });

        HttpServletRequest wrapped =
                (HttpServletRequest) processor(List.of("keep")).sanitize(request);

        // "keep" 은 ignoreFieldNames 에 포함되어 필터링되고, "x" 만 정화된다.
        assertThat(wrapped.getParameterMap().get("q")).containsExactly("X");
    }
}
