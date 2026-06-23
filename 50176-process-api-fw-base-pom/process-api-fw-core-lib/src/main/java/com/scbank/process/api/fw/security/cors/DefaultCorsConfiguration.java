package com.scbank.process.api.fw.security.cors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@ConditionalOnProperty(prefix = "csl.security.cors", name = "enabled", havingValue = "true")
public class DefaultCorsConfiguration {

    /**
     * CORS 설정 Bean 등록
     *
     * @param customizers 사용자 정의 커스터마이저 리스트
     * @return CorsConfigurationSource
     */
    @Bean
    @ConditionalOnMissingBean
    CorsConfigurationSource corsConfigurationSource(CorsProperties props) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(props.getAllowedOrigins());
        config.setAllowedMethods(props.getAllowedMethods());
        config.setAllowedHeaders(props.getAllowedHeaders());
        config.setExposedHeaders(props.getExposedHeaders());
        config.setAllowCredentials(props.getAllowCredentials());
        config.setMaxAge(props.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 여러 경로에 동일 설정 적용
        for (String path : props.getPathPatterns()) {
            source.registerCorsConfiguration(path, config);
        }

        return source;
    }
}
