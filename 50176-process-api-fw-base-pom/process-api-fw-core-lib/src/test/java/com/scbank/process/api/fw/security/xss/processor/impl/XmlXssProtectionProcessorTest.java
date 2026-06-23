package com.scbank.process.api.fw.security.xss.processor.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link XmlXssProtectionProcessor} 단위 테스트.
 */
class XmlXssProtectionProcessorTest {

    private final IXssSanitizer upperSanitizer = s -> s == null ? null : s.toUpperCase();
    private final XmlXssProtectionProcessor processor =
            new XmlXssProtectionProcessor(new XmlMapper(), upperSanitizer, List.of());

    @BeforeEach
    void setUp() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("csl.runtime.default-encoding", "UTF-8");
        ReflectionTestUtils.setField(RuntimeContext.class, "environment", env);
    }

    @AfterEach
    void tearDown() {
        ReflectionTestUtils.setField(RuntimeContext.class, "environment", null);
    }

    @Test
    @DisplayName("application/xml, text/xml Content-Type 을 지원한다")
    void supportsXml() {
        assertThat(processor.supports("application/xml")).isTrue();
        assertThat(processor.supports("text/xml")).isTrue();
        assertThat(processor.supports("TEXT/XML")).isTrue();
    }

    @Test
    @DisplayName("null 또는 비 XML Content-Type 은 지원하지 않는다")
    void doesNotSupportOthers() {
        assertThat(processor.supports(null)).isFalse();
        assertThat(processor.supports("application/json")).isFalse();
    }

    @Test
    @DisplayName("XML Body 의 문자열 값을 정화한다")
    void sanitizesXmlBody() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/xml");
        request.setCharacterEncoding("UTF-8");
        request.setContent("<root><a>hi</a></root>".getBytes(java.nio.charset.StandardCharsets.UTF_8));

        ServletRequest wrapped = processor.sanitize(request);
        byte[] bytes = ((HttpServletRequest) wrapped).getInputStream().readAllBytes();

        assertThat(new String(bytes, java.nio.charset.StandardCharsets.UTF_8)).contains("HI");
    }
}
