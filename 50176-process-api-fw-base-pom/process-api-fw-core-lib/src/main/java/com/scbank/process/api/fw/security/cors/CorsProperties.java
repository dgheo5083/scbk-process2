package com.scbank.process.api.fw.security.cors;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * CORS 관련 설정을 외부 설정 파일(application.yml)로부터 로딩하는 프로퍼티 클래스입니다.
 */
@Data
@ConfigurationProperties(prefix = "csl.security.cors")
public class CorsProperties {

    private boolean enabled;
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
    private Boolean allowCredentials;
    private Long maxAge;
    private List<String> pathPatterns; // 허용 경로
}
