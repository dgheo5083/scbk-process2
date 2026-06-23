package com.scbank.process.api.fw.base.channel.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.properties.AuthFilterProperty;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Generated unit test for {@link AuthFilter}.
 */
class AuthFilterTest {

    private AuthFilter filterWithSkipPaths(List<String> skipPaths) {
        AuthFilterProperty property = new AuthFilterProperty();
        property.setSkipPathWithoutAuthHeader(skipPaths);
        return new AuthFilter(property);
    }

    @Test
    void shouldSkipReturnsTrueForConfiguredPath() {
        AuthFilter filter = filterWithSkipPaths(List.of("/health"));
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/health");

        assertThat(filter.shouldSkip(request)).isTrue();
    }

    @Test
    void shouldSkipReturnsFalseForOtherPath() {
        AuthFilter filter = filterWithSkipPaths(List.of("/health"));
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/v1");

        assertThat(filter.shouldSkip(request)).isFalse();
    }

    @Test
    void doFilterInternalSkipsAuthForSkipPath() throws Exception {
        AuthFilter filter = filterWithSkipPaths(List.of("/health"));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/health");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternalReadsAuthHeaderForProtectedPath() throws Exception {
        AuthFilter filter = filterWithSkipPaths(List.of("/health"));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getRequestURI()).thenReturn("/api/v1");
        lenient().when(request.getHeader("Authorization")).thenReturn("Bearer token");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
