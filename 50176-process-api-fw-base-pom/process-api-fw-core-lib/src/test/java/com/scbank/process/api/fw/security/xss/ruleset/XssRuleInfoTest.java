package com.scbank.process.api.fw.security.xss.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link XssRuleInfo} 단위 테스트.
 */
class XssRuleInfoTest {

    @Test
    @DisplayName("target/replacement getter/setter 동작")
    void gettersAndSetters() {
        XssRuleInfo rule = new XssRuleInfo();
        rule.setTarget("<");
        rule.setReplacement("&lt;");

        assertThat(rule.getTarget()).isEqualTo("<");
        assertThat(rule.getReplacement()).isEqualTo("&lt;");
    }

    @Test
    @DisplayName("equals/hashCode/toString 동작")
    void equalsHashCodeToString() {
        XssRuleInfo a = new XssRuleInfo();
        a.setTarget("script");
        a.setReplacement("");
        XssRuleInfo b = new XssRuleInfo();
        b.setTarget("script");
        b.setReplacement("");

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("XssRuleInfo");
    }
}
