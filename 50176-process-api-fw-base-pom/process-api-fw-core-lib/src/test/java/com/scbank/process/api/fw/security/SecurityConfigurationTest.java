package com.scbank.process.api.fw.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.security.SecurityProperties.XssConfig;
import com.scbank.process.api.fw.security.xss.filter.XssProtectionFilter;
import com.scbank.process.api.fw.security.xss.processor.IXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.FormXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.JsonXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.XmlXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.XssProtectionProcessorComposite;
import com.scbank.process.api.fw.security.xss.ruleset.IXssRuleRegistry;
import com.scbank.process.api.fw.security.xss.ruleset.impl.DefaultXssRuleRegistry;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;
import com.scbank.process.api.fw.security.xss.sanitizer.impl.DefaultXssSanitizer;

import jakarta.servlet.Filter;

/**
 * {@link SecurityConfiguration} 의 {@code @Bean} 팩토리 메서드 단위 테스트.
 */
class SecurityConfigurationTest {

    private final SecurityConfiguration configuration = new SecurityConfiguration();

    private SecurityProperties properties() {
        SecurityProperties properties = new SecurityProperties();
        properties.setEnabled(true);
        properties.setXss(new XssConfig("classpath:xss-test/test-ruleset.xml",
                List.of("POST /ignore"), List.of("token")));
        return properties;
    }

    @Test
    @DisplayName("init 은 예외 없이 동작한다")
    void initRuns() {
        configuration.init();
    }

    @Test
    @DisplayName("xssRuleRegistry 는 DefaultXssRuleRegistry 를 생성")
    void xssRuleRegistryBean() {
        IXssRuleRegistry registry = configuration.xssRuleRegistry(properties());

        assertThat(registry).isInstanceOf(DefaultXssRuleRegistry.class);
    }

    @Test
    @DisplayName("xssSanitizer 는 레지스트리 룰로 DefaultXssSanitizer 를 생성")
    void xssSanitizerBean() {
        IXssRuleRegistry registry = mock(IXssRuleRegistry.class);
        when(registry.getXssRules()).thenReturn(List.of());

        IXssSanitizer sanitizer = configuration.xssSanitizer(registry);

        assertThat(sanitizer).isInstanceOf(DefaultXssSanitizer.class);
    }

    @Test
    @DisplayName("formXssProtectionProcessor 빈 생성")
    void formProcessorBean() {
        FormXssProtectionProcessor processor =
                configuration.formXssProtectionProcessor(mock(IXssSanitizer.class), properties());

        assertThat(processor).isNotNull();
        assertThat(processor.supports("application/x-www-form-urlencoded")).isTrue();
    }

    @Test
    @DisplayName("jsonXssProtectionProcessor 빈 생성")
    void jsonProcessorBean() {
        JsonXssProtectionProcessor processor = configuration.jsonXssProtectionProcessor(
                new ObjectMapper(), mock(IXssSanitizer.class), properties());

        assertThat(processor.supports("application/json")).isTrue();
    }

    @Test
    @DisplayName("xmlXssProtectionProcessor 빈 생성")
    void xmlProcessorBean() {
        XmlXssProtectionProcessor processor =
                configuration.xmlXssProtectionProcessor(mock(IXssSanitizer.class), properties());

        assertThat(processor.supports("text/xml")).isTrue();
    }

    @Test
    @DisplayName("xssProtectionProcessorComposite 빈 생성")
    void compositeBean() {
        XssProtectionProcessorComposite composite = configuration.xssProtectionProcessorComposite(
                List.of(mock(IXssProtectionProcessor.class)));

        assertThat(composite).isNotNull();
    }

    @Test
    @DisplayName("xssProtectionFilter 는 최상단 순서로 모든 경로에 등록된다")
    void filterRegistrationBean() {
        XssProtectionProcessorComposite composite = mock(XssProtectionProcessorComposite.class);

        FilterRegistrationBean<Filter> registration =
                configuration.xssProtectionFilter(properties(), composite);

        assertThat(registration.getFilter()).isInstanceOf(XssProtectionFilter.class);
        assertThat(registration.getOrder()).isEqualTo(Integer.MIN_VALUE + 10);
        assertThat(registration.getUrlPatterns()).containsExactly("/*");
    }
}
