package com.scbank.process.api.fw.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.security.SecurityProperties.XssConfig;

/**
 * {@link SecurityProperties} 및 {@link XssConfig} 단위 테스트.
 */
class SecurityPropertiesTest {

    @Test
    @DisplayName("enabled / xss getter/setter 동작")
    void gettersAndSetters() {
        SecurityProperties properties = new SecurityProperties();
        XssConfig xss = new XssConfig("classpath:xss.xml", List.of("POST /a"), List.of("token"));

        properties.setEnabled(true);
        properties.setXss(xss);

        assertThat(properties.isEnabled()).isTrue();
        assertThat(properties.getXss()).isSameAs(xss);
    }

    @Test
    @DisplayName("XssConfig 레코드 접근자")
    void xssConfigAccessors() {
        XssConfig xss = new XssConfig("classpath:rules.xml",
                List.of("GET /health"), List.of("password", "ssn"));

        assertThat(xss.rulesetLocation()).isEqualTo("classpath:rules.xml");
        assertThat(xss.ignorePaths()).containsExactly("GET /health");
        assertThat(xss.ignoreFieldNames()).containsExactly("password", "ssn");
    }

    @Test
    @DisplayName("XssConfig equals/hashCode")
    void xssConfigEquality() {
        XssConfig a = new XssConfig("c", List.of("p"), List.of("f"));
        XssConfig b = new XssConfig("c", List.of("p"), List.of("f"));

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }
}
