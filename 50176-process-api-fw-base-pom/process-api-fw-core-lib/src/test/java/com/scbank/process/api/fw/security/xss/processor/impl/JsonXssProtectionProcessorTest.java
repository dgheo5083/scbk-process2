package com.scbank.process.api.fw.security.xss.processor.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

/**
 * {@link JsonXssProtectionProcessor#supports(String)} 단위 테스트.
 */
class JsonXssProtectionProcessorTest {

    private final IXssSanitizer noop = s -> s;
    private final JsonXssProtectionProcessor processor =
            new JsonXssProtectionProcessor(new ObjectMapper(), noop, List.of());

    @Test
    @DisplayName("application/json Content-Type 을 지원한다")
    void supportsJson() {
        assertThat(processor.supports("application/json")).isTrue();
        assertThat(processor.supports("application/json;charset=UTF-8")).isTrue();
    }

    @Test
    @DisplayName("대소문자 구분 없이 지원 여부를 판단한다")
    void supportsCaseInsensitive() {
        assertThat(processor.supports("APPLICATION/JSON")).isTrue();
    }

    @Test
    @DisplayName("null 또는 비 JSON Content-Type 은 지원하지 않는다")
    void doesNotSupportOthers() {
        assertThat(processor.supports(null)).isFalse();
        assertThat(processor.supports("text/xml")).isFalse();
    }
}
