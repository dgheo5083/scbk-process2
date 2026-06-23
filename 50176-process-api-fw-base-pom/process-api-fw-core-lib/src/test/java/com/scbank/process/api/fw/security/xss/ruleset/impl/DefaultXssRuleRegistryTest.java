package com.scbank.process.api.fw.security.xss.ruleset.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.security.xss.ruleset.IXssRuleLoader;
import com.scbank.process.api.fw.security.xss.ruleset.XssRuleInfo;

/**
 * {@link DefaultXssRuleRegistry} 단위 테스트.
 */
class DefaultXssRuleRegistryTest {

    private XssRuleInfo rule(String target) {
        XssRuleInfo r = new XssRuleInfo();
        r.setTarget(target);
        return r;
    }

    @Test
    @DisplayName("init 은 로더가 반환한 룰을 메모리에 적재한다")
    void initLoadsRules() {
        IXssRuleLoader loader = mock(IXssRuleLoader.class);
        when(loader.load()).thenReturn(List.of(rule("<"), rule("script")));
        DefaultXssRuleRegistry registry = new DefaultXssRuleRegistry(loader);

        registry.init();

        assertThat(registry.getXssRules()).hasSize(2);
    }

    @Test
    @DisplayName("afterPropertiesSet 은 init 을 호출한다(default 메서드)")
    void afterPropertiesSetInvokesInit() throws Exception {
        IXssRuleLoader loader = mock(IXssRuleLoader.class);
        when(loader.load()).thenReturn(List.of(rule("<")));
        DefaultXssRuleRegistry registry = new DefaultXssRuleRegistry(loader);

        registry.afterPropertiesSet();

        assertThat(registry.getXssRules()).hasSize(1);
    }

    @Test
    @DisplayName("destroy 는 보유 중인 룰 목록을 비운다(default 메서드)")
    void destroyClearsRules() throws Exception {
        IXssRuleLoader loader = mock(IXssRuleLoader.class);
        when(loader.load()).thenReturn(List.of(rule("<")));
        DefaultXssRuleRegistry registry = new DefaultXssRuleRegistry(loader);
        registry.init();

        registry.destroy();

        assertThat(registry.getXssRules()).isEmpty();
    }
}
