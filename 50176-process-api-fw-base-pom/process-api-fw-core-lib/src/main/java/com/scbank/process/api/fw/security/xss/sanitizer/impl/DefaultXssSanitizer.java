package com.scbank.process.api.fw.security.xss.sanitizer.impl;

import java.util.List;

import com.scbank.process.api.fw.security.xss.ruleset.XssRuleInfo;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

import lombok.RequiredArgsConstructor;

/**
 * 룰셋 기반으로 문자열을 정화(Sanitize)하는 기본 XSS Sanitizer 구현체입니다.
 * <p>
 * 제공된 {@link XssRuleInfo} 목록을 기준으로 입력 문자열 내의 위험 요소를 제거하거나 치환합니다.
 * </p>
 */
@RequiredArgsConstructor
public class DefaultXssSanitizer implements IXssSanitizer {

    /**
     * 적용할 XSS 룰 목록
     */
    private final List<XssRuleInfo> xssRules;

    /**
     * 주어진 입력 문자열에 대해 등록된 룰셋을 순차적으로 적용하여
     * 위험 요소를 제거하거나 치환합니다.
     *
     * @param source 입력 문자열
     * @return 정화된 문자열 (XSS 요소 제거 또는 치환)
     */
    @Override
    public String sanitize(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }

        String sanitized = source;
        for (XssRuleInfo rule : xssRules) {
            String target = rule.getTarget();
            String replacement = rule.getReplacement() != null ? rule.getReplacement() : "";

            if (target != null && !target.isEmpty()) {
                sanitized = sanitized.replaceAll(target, replacement);
            }
        }

        return sanitized;
    }
}
