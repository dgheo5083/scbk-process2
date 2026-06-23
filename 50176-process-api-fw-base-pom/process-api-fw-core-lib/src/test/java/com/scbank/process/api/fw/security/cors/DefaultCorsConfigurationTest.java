package com.scbank.process.api.fw.security.cors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * {@link DefaultCorsConfiguration} 의 {@code corsConfigurationSource} 빈 단위 테스트.
 */
class DefaultCorsConfigurationTest {

    private final DefaultCorsConfiguration configuration = new DefaultCorsConfiguration();

    private CorsProperties props(List<String> pathPatterns) {
        CorsProperties props = new CorsProperties();
        props.setAllowedOrigins(List.of("https://a.com"));
        props.setAllowedMethods(List.of("GET", "POST"));
        props.setAllowedHeaders(List.of("Authorization"));
        props.setExposedHeaders(List.of("X-Total"));
        props.setAllowCredentials(Boolean.TRUE);
        props.setMaxAge(1800L);
        props.setPathPatterns(pathPatterns);
        return props;
    }

    @Test
    @DisplayName("설정된 경로마다 CORS 구성을 등록한다")
    void registersConfigForEachPath() {
        CorsConfigurationSource source =
                configuration.corsConfigurationSource(props(List.of("/api/**", "/admin/**")));

        assertThat(source).isInstanceOf(UrlBasedCorsConfigurationSource.class);

        MockHttpServletRequest apiRequest = new MockHttpServletRequest();
        apiRequest.setRequestURI("/api/users");
        CorsConfiguration matched = source.getCorsConfiguration(apiRequest);

        assertThat(matched).isNotNull();
        assertThat(matched.getAllowedOrigins()).containsExactly("https://a.com");
        assertThat(matched.getAllowedMethods()).containsExactly("GET", "POST");
        assertThat(matched.getAllowCredentials()).isTrue();
        assertThat(matched.getMaxAge()).isEqualTo(1800L);
    }

    @Test
    @DisplayName("경로 패턴이 비어 있으면 어떤 구성도 등록되지 않는다")
    void noPathsRegistersNothing() {
        CorsConfigurationSource source = configuration.corsConfigurationSource(props(List.of()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/users");

        assertThat(source.getCorsConfiguration(request)).isNull();
    }
}
