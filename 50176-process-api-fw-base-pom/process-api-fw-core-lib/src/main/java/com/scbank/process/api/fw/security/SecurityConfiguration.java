package com.scbank.process.api.fw.security;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.security.SecurityProperties.XssConfig;
import com.scbank.process.api.fw.security.xss.filter.XssProtectionFilter;
import com.scbank.process.api.fw.security.xss.processor.IXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.FormXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.JsonXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.XmlXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.processor.impl.XssProtectionProcessorComposite;
import com.scbank.process.api.fw.security.xss.ruleset.IXssRuleRegistry;
import com.scbank.process.api.fw.security.xss.ruleset.impl.DefaultXssRuleLoader;
import com.scbank.process.api.fw.security.xss.ruleset.impl.DefaultXssRuleRegistry;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;
import com.scbank.process.api.fw.security.xss.sanitizer.impl.DefaultXssSanitizer;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 전역에 XSS 방어 기능을 제공하는 Spring Boot 자동 구성 클래스입니다.
 *
 * <p>
 * XSS 룰셋 로딩 → 문자열 정화기 → Content-Type별 처리기 → 서블릿 필터 순으로 자동 구성되며,
 * 사용자는 {@code application.yml}의 {@code framework.security.enabled=true} 설정을 통해
 * 활성화할 수 있습니다.
 * </p>
 *
 * <p>
 * 각 Bean은 {@code @ConditionalOnMissingBean} 조건으로 선언되어 있어
 * 필요 시 애플리케이션에서 재정의가 가능합니다.
 * </p>
 *
 * @author
 * @since 2025.04
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "csl.security", name = "enabled", havingValue = "true")
public class SecurityConfiguration {

    @PostConstruct
    public void init() {
        log.info("### 프레임워크 Security 설정 init");
    }

    /**
     * XSS 룰셋을 외부 설정 경로에서 로드하고, 메모리에 등록하는 레지스트리 Bean입니다.
     *
     * @param securityProperties 사용자 설정 (ruleset 위치 포함)
     * @return 룰셋 레지스트리
     */
    @Bean
    @ConditionalOnMissingBean(IXssRuleRegistry.class)
    IXssRuleRegistry xssRuleRegistry(SecurityProperties securityProperties) {
        XssConfig xssConfig = securityProperties.getXss();
        return new DefaultXssRuleRegistry(new DefaultXssRuleLoader(xssConfig.rulesetLocation()));
    }

    /**
     * 로드된 룰셋을 기반으로 문자열 정화를 수행하는 XSS 정화기 Bean입니다.
     *
     * @param xssRuleRegistry 룰셋 레지스트리
     * @return 기본 정화기 구현체
     */
    @Bean
    @ConditionalOnMissingBean(IXssSanitizer.class)
    IXssSanitizer xssSanitizer(IXssRuleRegistry xssRuleRegistry) {
        return new DefaultXssSanitizer(xssRuleRegistry.getXssRules());
    }

    /**
     * {@code application/x-www-form-urlencoded} 요청에 대한 정화 Processor입니다.
     *
     * @param xssSanitizer 정화기
     * @return Form 요청용 Processor
     */
    @Bean
    FormXssProtectionProcessor formXssProtectionProcessor(IXssSanitizer xssSanitizer, SecurityProperties securityProperties) {
    	XssConfig xssConfig = securityProperties.getXss();
        return new FormXssProtectionProcessor(xssSanitizer, xssConfig.ignoreFieldNames());
    }

    /**
     * {@code application/json} 요청에 대한 정화 Processor입니다.
     *
     * @param objectMapper Jackson의 ObjectMapper
     * @param xssSanitizer 정화기
     * @return JSON 요청용 Processor
     */
    @Bean
    JsonXssProtectionProcessor jsonXssProtectionProcessor(ObjectMapper objectMapper, IXssSanitizer xssSanitizer, SecurityProperties securityProperties) {
    	XssConfig xssConfig = securityProperties.getXss();
        return new JsonXssProtectionProcessor(objectMapper, xssSanitizer, xssConfig.ignoreFieldNames());
    }

    /**
     * {@code application/xml}, {@code text/xml} 요청에 대한 정화 Processor입니다.
     *
     * @param xmlMapper    Jackson XmlMapper
     * @param xssSanitizer 정화기
     * @return XML 요청용 Processor
     */
    @Bean
    XmlXssProtectionProcessor xmlXssProtectionProcessor(IXssSanitizer xssSanitizer, SecurityProperties securityProperties) {
    	XssConfig xssConfig = securityProperties.getXss();
        return new XmlXssProtectionProcessor(new XmlMapper(), xssSanitizer, xssConfig.ignoreFieldNames());
    }

    /**
     * 등록된 Content-Type별 Processor를 조합하여 요청에 적합한 정화 전략을 선택하는 Composite Processor입니다.
     *
     * @param processors 각 요청 타입별 Processor 리스트
     * @return Composite Processor
     */
    @Bean
    @ConditionalOnMissingBean
    XssProtectionProcessorComposite xssProtectionProcessorComposite(List<IXssProtectionProcessor> processors) {
        return new XssProtectionProcessorComposite(processors);
    }

    /**
     * Servlet Filter로 등록되는 XSS Protection 필터입니다.
     * <p>
     * Processor Composite을 통해 각 요청의 Content-Type에 따라 적절한 정화를 수행합니다.
     * 필터 체인 앞단에서 실행되며, 모든 요청에 자동 적용됩니다.
     * </p>
     *
     * @param processor Processor Composite
     * @return Filter 등록 Bean
     */
    @Bean
    @ConditionalOnMissingBean(name = "xssProtectionFilter")
    FilterRegistrationBean<Filter> xssProtectionFilter(SecurityProperties properties,
            XssProtectionProcessorComposite processor) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssProtectionFilter(processor, properties));
        registration.setOrder(Integer.MIN_VALUE + 10); // 필터 체인 상단에 배치
        registration.setName("xssProtectionFilter");
        registration.addUrlPatterns("/*"); // 모든 요청에 적용
        return registration;
    }
}
