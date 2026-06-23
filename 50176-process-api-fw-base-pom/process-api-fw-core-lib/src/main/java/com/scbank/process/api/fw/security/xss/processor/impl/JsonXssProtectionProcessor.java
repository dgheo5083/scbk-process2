package com.scbank.process.api.fw.security.xss.processor.impl;

import java.util.List;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

/**
 * {@code application/json} 요청의 Body 데이터를 정화(Sanitize)하는
 * XSS Protection Processor 구현체입니다.
 *
 * <p>
 * 본 구현체는 {@link AbstractJacksonXssProcessor}를 상속하며,
 * Jackson의 {@link ObjectMapper}를 사용해 JSON Body를 파싱한 후,
 * {@link IXssSanitizer}를 통해 각 문자열 필드에서 XSS 위험 요소를 제거합니다.
 * </p>
 *
 * <p>
 * 최종적으로 정화된 JSON 문자열을 {@link jakarta.servlet.http.HttpServletRequestWrapper}
 * 형태로 감싸 반환하여 Spring의 {@code @RequestBody} 처리에 안전하게 적용할 수 있습니다.
 * </p>
 *
 * @see AbstractJacksonXssProcessor
 * @see IXssSanitizer
 * @see ObjectMapper
 * @since 2025.04
 * @author
 */
public class JsonXssProtectionProcessor extends AbstractJacksonXssProcessor<ObjectMapper> {

    /**
     * 생성자
     *
     * @param mapper       Jackson의 {@link ObjectMapper} 인스턴스
     * @param xssSanitizer XSS 정화 전략 구현체
     * @param ignoreFieldNames
     */
    public JsonXssProtectionProcessor(ObjectMapper mapper, IXssSanitizer xssSanitizer, List<String> ignoreFieldNames) {
        super(mapper, xssSanitizer, ignoreFieldNames);
    }

    /**
     * Content-Type이 {@code application/json}일 경우 해당 Processor가 동작하도록 지정합니다.
     *
     * @param contentType 요청의 Content-Type
     * @return JSON 요청에 해당하면 {@code true}, 아니면 {@code false}
     */
    @Override
    public boolean supports(String contentType) {
        return contentType != null && contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
