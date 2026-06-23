package com.scbank.process.api.fw.security.xss.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AntPathMatcher;

import com.scbank.process.api.fw.security.SecurityProperties;
import com.scbank.process.api.fw.security.xss.processor.impl.XssProtectionProcessorComposite;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 모든 HTTP 요청에 대해 XSS 방어 처리를 수행하는 서블릿 필터입니다.
 * <p>
 * 설정된 Content-Type별 Processor를 통해 요청 본문 또는 파라미터를 정화하고,
 * 정화가 완료된 요청을 다음 Filter 또는 Controller로 전달합니다.
 * <p>
 * SecurityProperties 설정 내 ignore-paths 목록을 기반으로, 특정 메서드+경로 조합에 대해서는
 * XSS 정화를 건너뛸 수 있습니다.
 * </p>
 */
public class XssProtectionFilter implements Filter {

    /** Content-Type별 요청 정화 Processor */
    private final XssProtectionProcessorComposite processor;

    /** XSS 정화를 무시할 경로 설정 목록 */
    private final List<XssIgnorePath> ignorePathRules;

    /** 경로 패턴 매칭 유틸리티 */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 생성자
     *
     * @param processor  Content-Type별 요청 정화 Processor
     * @param properties 설정 파일에서 XSS 무시 경로를 읽기 위한 SecurityProperties
     */
    public XssProtectionFilter(XssProtectionProcessorComposite processor, SecurityProperties properties) {
        this.processor = processor;
        this.ignorePathRules = XssIgnorePath.parse(properties.getXss().ignorePaths());
    }

    /**
     * 필터 실행 로직. 요청이 무시 대상이 아니면 sanitize() 처리 후 다음 필터로 전달합니다.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        if (shouldIgnore(httpRequest)) {
            chain.doFilter(request, response); // XSS 처리 생략
            return;
        }

        ServletRequest sanitized = processor.sanitize(request);
        chain.doFilter(sanitized, response);
    }

    /**
     * 현재 요청이 무시 대상인지 확인합니다.
     *
     * @param request 요청 객체
     * @return true면 sanitize 생략
     */
    private boolean shouldIgnore(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        return ignorePathRules.stream().anyMatch(rule -> rule.matches(method, uri, pathMatcher));
    }

    /**
     * METHOD + 경로 패턴 조합으로 정리된 ignore path 모델
     * <p>
     * 예) "POST /api/health", "ANY /docs/**"
     * </p>
     */
    public record XssIgnorePath(String method, String pattern) {

        /**
         * 설정 문자열 목록을 XssIgnorePath 모델로 변환합니다.
         *
         * @param rawRules 예: ["POST /api/health", "ANY /docs/**"]
         * @return 변환된 ignore path 목록
         */
        public static List<XssIgnorePath> parse(List<String> rawRules) {
            List<XssIgnorePath> list = new ArrayList<>();
            for (String rule : rawRules) {
                String[] parts = rule.trim().split("\\s+", 2);
                if (parts.length == 2) {
                    list.add(new XssIgnorePath(parts[0].toUpperCase(), parts[1]));
                }
            }
            return list;
        }

        /**
         * 현재 요청 method + uri가 이 ignore rule과 일치하는지 확인합니다.
         *
         * @param requestMethod 요청 HTTP 메서드
         * @param requestUri    요청 URI
         * @param pathMatcher   경로 매칭 유틸
         * @return 무시 조건에 해당되면 true
         */
        public boolean matches(String requestMethod, String requestUri, AntPathMatcher pathMatcher) {
            if (!"ANY".equals(method) && !method.equalsIgnoreCase(requestMethod)) {
                return false;
            }
            return pathMatcher.match(pattern, requestUri);
        }
    }
}
