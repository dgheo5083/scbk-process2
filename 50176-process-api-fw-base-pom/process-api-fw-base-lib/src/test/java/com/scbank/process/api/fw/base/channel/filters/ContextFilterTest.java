package com.scbank.process.api.fw.base.channel.filters;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Generated unit test for {@link ContextFilter}.
 */
class ContextFilterTest {

    private final ContextFilter filter = new ContextFilter();

    @Test
    void populatesContextAndContinuesChain() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        lenient().when(request.getHeader(anyString())).thenReturn("header-value");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void clearsContextEvenWhenChainFails() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        when(request.getHeader(anyString())).thenReturn("v");
        doThrow(new ServletException("boom")).when(chain).doFilter(request, response);

        assertThatThrownBy(() -> filter.doFilterInternal(request, response, chain))
                .isInstanceOf(ServletException.class);
    }
}
