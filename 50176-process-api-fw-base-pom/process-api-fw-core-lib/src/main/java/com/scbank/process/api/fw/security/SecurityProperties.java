package com.scbank.process.api.fw.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 프레임워크 전반의 보안 설정을 구성하는 프로퍼티 클래스입니다.
 * <p>
 * application.yml 또는 application.properties에서 {@code csl.security.*}
 * 프리픽스로 시작하는
 * 외부 설정 값을 자동으로 바인딩합니다.
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "csl.security")
public class SecurityProperties {

    /**
     * 전체 보안 기능의 활성화 여부
     */
    private boolean enabled;

    /**
     * XSS 보호 기능에 대한 설정
     */
    private XssConfig xss;

    /**
     * XSS 정화 설정값
     *
     * @param rulesetLocation 룰셋 XML의 경로 (예: classpath:xss/xss-ruleset.xml)
     * @param ignorePaths
     * @param ignoreFieldNames
     */
    public record XssConfig(String rulesetLocation, List<String> ignorePaths, List<String> ignoreFieldNames) {
    }
}
