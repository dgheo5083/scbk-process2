package com.scbank.process.api.fw.security.xss.processor.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import com.scbank.process.api.fw.security.xss.processor.IXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * {@code application/x-www-form-urlencoded} 요청에 대해
 * Query Parameter 또는 Form Parameter 값을 정화(Sanitize) 처리하는 XSS Protection
 * Processor 구현체입니다.
 *
 * <p>
 * {@link HttpServletRequest#getParameter(String)} 등 파라미터 관련 메서드를 오버라이드하여
 * {@link IXssSanitizer}를 통해 XSS 위험 요소를 제거하고,
 * 정화된 파라미터 값을 반환하는 래핑 요청 객체를 제공합니다.
 * </p>
 *
 * @see IXssProtectionProcessor
 * @see IXssSanitizer
 * @since 2025.04
 */
public class FormXssProtectionProcessor implements IXssProtectionProcessor {

    private final IXssSanitizer xssSanitizer;
    
    private final List<String> ignoreFieldNames;

    /**
     * 생성자
     *
     * @param xssSanitizer XSS 정화기 구현체
     * @param ignoreFieldNames
     */
    public FormXssProtectionProcessor(IXssSanitizer xssSanitizer, List<String> ignoreFieldNames) {
        this.xssSanitizer = xssSanitizer;
        this.ignoreFieldNames = ignoreFieldNames;
    }

    /**
     * 요청의 Content-Type이 {@code application/x-www-form-urlencoded} 인 경우 처리 대상임을
     * 반환합니다.
     *
     * @param contentType 요청의 Content-Type
     * @return 해당 Content-Type에 대응하면 {@code true}
     */
    @Override
    public boolean supports(String contentType) {
        return contentType == null || contentType.toLowerCase().startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    /**
     * 요청의 파라미터 값을 정화 처리한 {@link HttpServletRequestWrapper} 객체를 반환합니다.
     *
     * @param request 원본 요청 객체
     * @return 파라미터가 정화된 래핑 요청 객체
     * @throws IOException 요청 처리 중 오류 발생 시
     */
    @Override
    public ServletRequest sanitize(ServletRequest request) throws IOException {
        if (!(request instanceof HttpServletRequest))
            return request;
        return new SanitizedFormRequestWrapper((HttpServletRequest) request, xssSanitizer, ignoreFieldNames);
    }

    /**
     * Form 파라미터를 정화 처리하는 래퍼 클래스입니다.
     *
     * <p>
     * {@link HttpServletRequest}의 파라미터 관련 메서드들
     * ({@code getParameter()}, {@code getParameterValues()},
     * {@code getParameterMap()})을
     * 오버라이드하여 문자열 정화 로직을 적용합니다.
     * </p>
     */
    private static class SanitizedFormRequestWrapper extends HttpServletRequestWrapper {

        private final IXssSanitizer xssSanitizer;
        
        private final List<String> ignoreFieldNames;

        /**
         * 생성자
         *
         * @param request      원본 요청 객체
         * @param xssSanitizer 문자열 정화기
         */
        public SanitizedFormRequestWrapper(HttpServletRequest request, IXssSanitizer xssSanitizer, List<String> ignoreFieldNames) {
            super(request);
            this.xssSanitizer = xssSanitizer;
            this.ignoreFieldNames = ignoreFieldNames == null ? List.of() : ignoreFieldNames;
        }

        /**
         * 단일 파라미터 값을 정화하여 반환합니다.
         */
        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return xssSanitizer.sanitize(value);
        }

        /**
         * 다중 파라미터 값을 정화하여 반환합니다.
         */
        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null)
                return null;
            return Arrays.stream(values)
                    .map(xssSanitizer::sanitize)
                    .toArray(String[]::new);
        }

        /**
         * 파라미터 이름 목록을 그대로 반환합니다.
         */
        @Override
        public Enumeration<String> getParameterNames() {
            return super.getParameterNames();
        }

        /**
         * 전체 파라미터 맵을 정화하여 반환합니다.
         */
        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> originalMap = super.getParameterMap();
            Map<String, String[]> sanitizedMap = new HashMap<>();
            for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
                String[] sanitizedValues = Arrays.stream(entry.getValue())
                		.filter(v -> !ignoreFieldNames.contains(v))
                        .map(xssSanitizer::sanitize)
                        .toArray(String[]::new);
                sanitizedMap.put(entry.getKey(), sanitizedValues);
            }
            return sanitizedMap;
        }
    }
}
