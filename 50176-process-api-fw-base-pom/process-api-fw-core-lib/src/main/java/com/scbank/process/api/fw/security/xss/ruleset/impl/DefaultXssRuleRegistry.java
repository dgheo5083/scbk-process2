package com.scbank.process.api.fw.security.xss.ruleset.impl;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.security.xss.ruleset.IXssRuleLoader;
import com.scbank.process.api.fw.security.xss.ruleset.IXssRuleRegistry;
import com.scbank.process.api.fw.security.xss.ruleset.XssRuleInfo;

import lombok.RequiredArgsConstructor;

/**
 * 기본 XSS 룰 레지스트리 구현체입니다.
 * <p>
 * 지정된 {@link IXssRuleLoader}를 통해 룰셋을 로딩하고 메모리에 보관하여
 * {@link IXssRuleRegistry} 인터페이스를 통해 룰을 제공합니다.
 * </p>
 */
@RequiredArgsConstructor
public class DefaultXssRuleRegistry implements IXssRuleRegistry {

    /**
     * 메모리에 보관 중인 XSS 룰 목록
     */
    private final List<XssRuleInfo> xssRules = new ArrayList<>();

    /**
     * 룰셋을 로드할 로더
     */
    private final IXssRuleLoader xssRuleLoader;

    /**
     * {@inheritDoc}
     * <p>
     * 초기화 시 룰셋을 로드하여 메모리에 적재합니다.
     * </p>
     */
    @Override
    public void init() {
        this.xssRules.addAll(xssRuleLoader.load());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<XssRuleInfo> getXssRules() {
        return this.xssRules;
    }
}
