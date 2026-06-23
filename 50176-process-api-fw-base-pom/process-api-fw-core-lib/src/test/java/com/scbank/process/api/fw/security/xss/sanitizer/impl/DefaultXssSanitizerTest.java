package com.scbank.process.api.fw.security.xss.sanitizer.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.security.xss.ruleset.XssRuleInfo;

/**
 * {@link DefaultXssSanitizer} 단위 테스트.
 */
class DefaultXssSanitizerTest {

    private XssRuleInfo rule(String target, String replacement) {
        XssRuleInfo r = new XssRuleInfo();
        r.setTarget(target);
        r.setReplacement(replacement);
        return r;
    }

    @Test
    @DisplayName("null 입력은 그대로 반환")
    void nullSource() {
        DefaultXssSanitizer sanitizer = new DefaultXssSanitizer(List.of(rule("<", "&lt;")));

        assertThat(sanitizer.sanitize(null)).isNull();
    }

    @Test
    @DisplayName("빈 문자열 입력은 그대로 반환")
    void emptySource() {
        DefaultXssSanitizer sanitizer = new DefaultXssSanitizer(List.of(rule("<", "&lt;")));

        assertThat(sanitizer.sanitize("")).isEmpty();
    }

    @Test
    @DisplayName("룰을 순차 적용하여 위험 요소를 치환한다")
    void appliesRules() {
        DefaultXssSanitizer sanitizer = new DefaultXssSanitizer(List.of(
                rule("<", "&lt;"),
                rule("script", "")));

        assertThat(sanitizer.sanitize("<script>")).isEqualTo("&lt;>");
    }

    @Test
    @DisplayName("replacement 가 null 이면 빈 문자열로 치환한다")
    void nullReplacementTreatedAsEmpty() {
        DefaultXssSanitizer sanitizer = new DefaultXssSanitizer(List.of(rule("bad", null)));

        assertThat(sanitizer.sanitize("bad-input")).isEqualTo("-input");
    }

    @Test
    @DisplayName("target 이 null 이거나 비어 있으면 해당 룰은 건너뛴다")
    void nullOrEmptyTargetSkipped() {
        DefaultXssSanitizer sanitizer = new DefaultXssSanitizer(List.of(
                rule(null, "x"),
                rule("", "y")));

        assertThat(sanitizer.sanitize("unchanged")).isEqualTo("unchanged");
    }

    @Test
    @DisplayName("등록된 룰이 없으면 입력을 그대로 반환")
    void noRules() {
        DefaultXssSanitizer sanitizer = new DefaultXssSanitizer(List.of());

        assertThat(sanitizer.sanitize("anything")).isEqualTo("anything");
    }
}
