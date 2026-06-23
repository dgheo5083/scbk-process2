package com.scbank.process.api.fw.security.cors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link CorsProperties} 단위 테스트.
 */
class CorsPropertiesTest {

    @Test
    @DisplayName("모든 필드 getter/setter 동작")
    void gettersAndSetters() {
        CorsProperties props = new CorsProperties();
        props.setEnabled(true);
        props.setAllowedOrigins(List.of("https://example.com"));
        props.setAllowedMethods(List.of("GET", "POST"));
        props.setAllowedHeaders(List.of("Authorization"));
        props.setExposedHeaders(List.of("X-Total-Count"));
        props.setAllowCredentials(Boolean.TRUE);
        props.setMaxAge(3600L);
        props.setPathPatterns(List.of("/api/**"));

        assertThat(props.isEnabled()).isTrue();
        assertThat(props.getAllowedOrigins()).containsExactly("https://example.com");
        assertThat(props.getAllowedMethods()).containsExactly("GET", "POST");
        assertThat(props.getAllowedHeaders()).containsExactly("Authorization");
        assertThat(props.getExposedHeaders()).containsExactly("X-Total-Count");
        assertThat(props.getAllowCredentials()).isTrue();
        assertThat(props.getMaxAge()).isEqualTo(3600L);
        assertThat(props.getPathPatterns()).containsExactly("/api/**");
    }

    @Test
    @DisplayName("기본값 검증")
    void defaults() {
        CorsProperties props = new CorsProperties();

        assertThat(props.isEnabled()).isFalse();
        assertThat(props.getAllowedOrigins()).isNull();
        assertThat(props.getAllowCredentials()).isNull();
        assertThat(props.getMaxAge()).isNull();
    }
}
