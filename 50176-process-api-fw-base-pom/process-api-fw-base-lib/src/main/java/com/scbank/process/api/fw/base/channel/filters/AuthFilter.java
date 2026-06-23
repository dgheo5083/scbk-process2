package com.scbank.process.api.fw.base.channel.filters;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.scbank.process.api.fw.base.properties.AuthFilterProperty;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프로세스API 요청 Auth Filter
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    /**
     * 프로세스API 인증 gateway
     */
    //private final CSLAuthGateway authGateway;

    /**
     * csl Auth filter property
     */
    private final AuthFilterProperty property;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (this.shouldSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = this.getCslAccessTokenHeader(request);
        if (log.isDebugEnabled()) {
            log.debug("# Authorization: {}", authHeader);
        }

        filterChain.doFilter(request, response);
    }

    protected boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = StringUtils.isNotBlank(path) && this.property.getSkipPathWithoutAuthHeader().stream()
                .anyMatch(skipPath -> skipPath.equalsIgnoreCase(path));
        return shouldSkip;
    }

    /**
     * 
     * @param request
     * @return
     */
    private String getCslAccessTokenHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
