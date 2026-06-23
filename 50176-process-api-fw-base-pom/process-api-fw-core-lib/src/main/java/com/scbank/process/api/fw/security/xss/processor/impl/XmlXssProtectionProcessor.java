package com.scbank.process.api.fw.security.xss.processor.impl;

import java.util.List;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

/**
 * {@code application/xml}, {@code text/xml} 요청의 Body 데이터를 정화(Sanitize)하는
 * XSS Protection Processor 구현체입니다.
 *
 * <p>
 * 본 구현체는 {@link AbstractJacksonXssProcessor}를 상속하며,
 * Jackson의 {@link XmlMapper}를 사용해 XML 요청 바디를 {@code Map<String, Object>} 형태로
 * 파싱한 뒤,
 * {@link IXssSanitizer}를 통해 문자열 필드 내의 XSS 위험 요소를 제거합니다.
 * </p>
 *
 * <p>
 * 정화된 XML 바디는 {@code HttpServletRequestWrapper}에 담겨 반환되며,
 * 이후 Spring의 {@code @RequestBody} 처리 과정에 안전하게 사용될 수 있습니다.
 * </p>
 *
 * @see AbstractJacksonXssProcessor
 * @see XmlMapper
 * @see IXssSanitizer
 * @since 2025.04
 */
public class XmlXssProtectionProcessor extends AbstractJacksonXssProcessor<XmlMapper> {

    /**
     * 생성자
     *
     * @param xmlMapper Jackson XML 처리용 {@link XmlMapper} 인스턴스
     * @param sanitizer XSS 정화 처리기
     */
    public XmlXssProtectionProcessor(XmlMapper xmlMapper, IXssSanitizer sanitizer, List<String> ignoreFieldNames) {
        super(xmlMapper, sanitizer, ignoreFieldNames);
    }

    /**
     * 요청의 Content-Type이 {@code application/xml} 또는 {@code text/xml}인 경우에만 처리 대상으로
     * 지정합니다.
     *
     * @param contentType 요청 Content-Type
     * @return XML 요청이면 {@code true}, 아니면 {@code false}
     */
    @Override
    public boolean supports(String contentType) {
        return contentType != null &&
                (contentType.toLowerCase().contains(MediaType.APPLICATION_XML_VALUE) ||
                        contentType.toLowerCase().contains(MediaType.TEXT_XML_VALUE));
    }
}
