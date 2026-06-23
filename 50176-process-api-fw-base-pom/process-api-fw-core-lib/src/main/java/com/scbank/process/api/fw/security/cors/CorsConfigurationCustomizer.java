package com.scbank.process.api.fw.security.cors;

import org.springframework.web.cors.CorsConfiguration;

/**
 * CORS 설정을 사용자 정의 방식으로 확장하거나 덮어쓸 수 있는 커스터마이저입니다.
 */
@FunctionalInterface
public interface CorsConfigurationCustomizer {
    /**
     * 주어진 {@link CorsConfiguration} 객체에 사용자 정의 설정을 적용합니다.
     *
     * @param config 커스터마이징 대상 CORS 설정 객체
     */
    void customize(CorsConfiguration config);
}