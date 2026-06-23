package com.scbank.process.api.fw.security.xss.processor.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.security.xss.processor.IXssProtectionProcessor;
import com.scbank.process.api.fw.security.xss.sanitizer.IXssSanitizer;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Jackson 기반 Body 요청(JSON/XML 등)에 대해 재귀적 XSS 정화를 수행하는
 * 공통 {@link IXssProtectionProcessor} 추상 클래스입니다.
 *
 * <p>
 * 이 클래스는 {@link ObjectMapper}를 이용해 요청 본문을 {@code Map<String, Object>}로 파싱하고,
 * {@link IXssSanitizer}를 이용하여 문자열 필드에 포함된 XSS 위험 요소를 제거한 후,
 * 다시 JSON 또는 XML 형식으로 직렬화하여 {@link HttpServletRequestWrapper}에 담아 반환합니다.
 * </p>
 *
 * <p>
 * JSON 처리용 하위 클래스는 {@code ObjectMapper}를,
 * XML 처리용 하위 클래스는 {@code XmlMapper}를 주입받아 사용합니다.
 * </p>
 *
 * @param <M> Jackson 기반 ObjectMapper의 타입 (예: ObjectMapper, XmlMapper 등)
 * @see JsonXssProtectionProcessor
 * @see XmlXssProtectionProcessor
 * @since 2025.04
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractJacksonXssProcessor<M extends ObjectMapper> implements IXssProtectionProcessor {

    /** Jackson 기반 파서 (ObjectMapper, XmlMapper 등) */
    protected final M mapper;

    /** 문자열 내 XSS 제거 전략 */
    protected final IXssSanitizer xssSanitizer;
    
    /**
     * 
     */
    protected final List<String> ignoreFieldNames;

    /**
     * 요청 본문을 읽고, Jackson으로 파싱한 뒤,
     * 재귀적으로 문자열 필드를 정화하여 다시 직렬화하고 래핑된 요청 객체로 반환합니다.
     *
     * @param request ServletRequest (HttpServletRequest인 경우에만 처리)
     * @return XSS 정화된 요청 객체
     * @throws IOException 바디 읽기 또는 파싱 중 오류
     */
    @Override
    public ServletRequest sanitize(ServletRequest request) throws IOException {
        if (!(request instanceof HttpServletRequest httpRequest))
            return request;

        String rawBody = readBody(httpRequest);
        if (StringUtils.isEmpty(rawBody)) {
            return httpRequest;
        }

        try {
            Map<String, Object> original = mapper.readValue(rawBody, new TypeReference<>() {
            });
            Object sanitized = sanitizeRecursive(original);
            String sanitizedJson = mapper.writeValueAsString(sanitized);

            return wrap(httpRequest, sanitizedJson);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return httpRequest;
        }
    }

    /**
     * 정화된 Body 문자열을 새로운 {@link HttpServletRequestWrapper}로 감쌉니다.
     *
     * @param request       원본 HttpServletRequest
     * @param sanitizedBody 정화된 JSON/XML 문자열
     * @return 재정의된 getInputStream(), getReader()를 가진 요청 객체
     */
    protected ServletRequest wrap(HttpServletRequest request, String sanitizedBody) {
        return new SanitizedBodyRequestWrapper(request, sanitizedBody);
    }

    /**
     * 요청 본문을 문자열로 읽어옵니다.
     *
     * @param request 요청 객체
     * @return 본문 문자열
     * @throws IOException 바디 읽기 오류
     */
    protected String readBody(HttpServletRequest request) throws IOException {
        return new String(request.getInputStream().readAllBytes(), getEncoding(request));
    }

    /**
     * 요청 인코딩을 반환합니다. 지정되지 않았을 경우 프레임워크 기본 인코딩을 반환합니다.
     *
     * @param request 요청 객체
     * @return Charset 인코딩
     */
    protected Charset getEncoding(HttpServletRequest request) {
        return Optional.ofNullable(request.getCharacterEncoding())
                .map(Charset::forName)
                .orElse(Charset.forName(RuntimeContext.getDefaultEncoding()));
    }

    /**
     * Map/List 구조를 재귀적으로 순회하며 문자열 값을 XSS 정화 처리합니다.
     *
     * @param input 원본 객체 (Map, List, String 또는 기타)
     * @return 정화된 객체 (동일 구조 유지)
     */
    protected Object sanitizeRecursive(Object input) {
        if (input instanceof Map<?, ?> map) {
            Map<String, Object> sanitized = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sanitized.put(entry.getKey().toString(), sanitizeRecursive(entry.getValue()));
            }
            return sanitized;
        } else if (input instanceof List<?> list) {
            List<Object> sanitizedList = new ArrayList<>();
            for (Object item : list) {
                sanitizedList.add(sanitizeRecursive(item));
            }
            return sanitizedList;
        } else if (input instanceof String str) {
        	if (!CollectionUtils.isEmpty(ignoreFieldNames) && ignoreFieldNames.contains(str)) {
        		return str;
        	}
            return xssSanitizer.sanitize(str);
        } else {
            return input;
        }
    }

    /**
     * 정화된 Body 문자열을 기반으로 getInputStream(), getReader()를 오버라이드한 래퍼 클래스입니다.
     * Spring MVC의 {@code @RequestBody} 처리 흐름에서 안전하게 사용됩니다.
     */
    public class SanitizedBodyRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] bodyBytes;

        /**
         * 생성자
         *
         * @param request       원본 요청 객체
         * @param sanitizedBody 정화된 JSON/XML 문자열
         */
        public SanitizedBodyRequestWrapper(HttpServletRequest request, String sanitizedBody) {
            super(request);
            this.bodyBytes = sanitizedBody.getBytes(Charset.forName(RuntimeContext.getDefaultEncoding()));
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream bais = new ByteArrayInputStream(bodyBytes);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return bais.available() == 0;
                }

                @Override
                public int available() throws IOException {
                    return bais.available();
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
    }
}
