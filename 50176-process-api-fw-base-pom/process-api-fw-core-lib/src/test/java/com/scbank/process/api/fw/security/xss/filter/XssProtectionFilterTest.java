package com.scbank.process.api.fw.security.xss.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.AntPathMatcher;

import com.scbank.process.api.fw.security.SecurityProperties;
import com.scbank.process.api.fw.security.SecurityProperties.XssConfig;
import com.scbank.process.api.fw.security.xss.filter.XssProtectionFilter.XssIgnorePath;
import com.scbank.process.api.fw.security.xss.processor.impl.XssProtectionProcessorComposite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;

/**
 * {@link XssProtectionFilter} 및 중첩 레코드 {@link XssIgnorePath} 단위 테스트.
 */
class XssProtectionFilterTest {

    private SecurityProperties propertiesWithIgnorePaths(List<String> ignorePaths) {
        SecurityProperties properties = new SecurityProperties();
        properties.setXss(new XssConfig("classpath:x.xml", ignorePaths, List.of()));
        return properties;
    }

    @Test
    @DisplayName("HttpServletRequest 가 아니면 정화 없이 체인을 통과시킨다")
    void nonHttpRequestPassesThrough() throws Exception {
        XssProtectionProcessorComposite processor = mock(XssProtectionProcessorComposite.class);
        XssProtectionFilter filter = new XssProtectionFilter(processor, propertiesWithIgnorePaths(List.of()));

        ServletRequest request = mock(ServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(processor, never()).sanitize(request);
    }

    @Test
    @DisplayName("무시 경로에 해당하면 정화를 생략하고 원본 요청을 전달한다")
    void ignoredPathSkipsSanitize() throws Exception {
        XssProtectionProcessorComposite processor = mock(XssProtectionProcessorComposite.class);
        XssProtectionFilter filter =
                new XssProtectionFilter(processor, propertiesWithIgnorePaths(List.of("POST /ignore/**")));

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/ignore/here");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(processor, never()).sanitize(request);
    }

    @Test
    @DisplayName("무시 대상이 아니면 정화 후 결과 요청을 체인으로 전달한다")
    void normalRequestSanitized() throws Exception {
        XssProtectionProcessorComposite processor = mock(XssProtectionProcessorComposite.class);
        XssProtectionFilter filter =
                new XssProtectionFilter(processor, propertiesWithIgnorePaths(List.of("POST /ignore/**")));

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/save");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        ServletRequest sanitized = mock(ServletRequest.class);
        when(processor.sanitize(request)).thenReturn(sanitized);

        filter.doFilter(request, response, chain);

        verify(processor).sanitize(request);
        verify(chain).doFilter(sanitized, response);
    }

    // ------------------------------------------------------------------
    // XssIgnorePath
    // ------------------------------------------------------------------

    @Test
    @DisplayName("parse 는 'METHOD 경로' 형식을 파싱하고 잘못된 형식은 건너뛴다")
    void parseRules() {
        List<XssIgnorePath> rules = XssIgnorePath.parse(List.of(
                "post /api/health",
                "ANY /docs/**",
                "malformed-single-token"));

        assertThat(rules).hasSize(2);
        assertThat(rules.get(0).method()).isEqualTo("POST");
        assertThat(rules.get(0).pattern()).isEqualTo("/api/health");
        assertThat(rules.get(1).method()).isEqualTo("ANY");
    }

    @Test
    @DisplayName("matches: ANY 메서드는 모든 메서드에 대해 경로만 비교한다")
    void matchesAnyMethod() {
        XssIgnorePath rule = new XssIgnorePath("ANY", "/docs/**");
        AntPathMatcher matcher = new AntPathMatcher();

        assertThat(rule.matches("GET", "/docs/intro", matcher)).isTrue();
        assertThat(rule.matches("DELETE", "/docs/a/b", matcher)).isTrue();
        assertThat(rule.matches("GET", "/other", matcher)).isFalse();
    }

    @Test
    @DisplayName("matches: 특정 메서드는 대소문자 무시 비교하며 메서드 불일치 시 false")
    void matchesSpecificMethod() {
        XssIgnorePath rule = new XssIgnorePath("POST", "/api/health");
        AntPathMatcher matcher = new AntPathMatcher();

        assertThat(rule.matches("post", "/api/health", matcher)).isTrue();
        assertThat(rule.matches("GET", "/api/health", matcher)).isFalse();
    }
}
