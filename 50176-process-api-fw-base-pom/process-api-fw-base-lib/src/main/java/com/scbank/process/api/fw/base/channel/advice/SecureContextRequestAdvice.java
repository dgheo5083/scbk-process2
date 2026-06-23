package com.scbank.process.api.fw.base.channel.advice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 보안영역 SecureContext 처리 RequestBodyAdvice 구현 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Order(10)
@RestControllerAdvice(basePackages = "com.scbank")
public class SecureContextRequestAdvice extends RequestBodyAdviceAdapter {

	private static final String FIELD_NM_COMMON = "common";
    private static final String FIELD_NM_BODY = "body";

    private final ObjectMapper objectMapper;
    
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasParameterAnnotation(RequestBody.class);
	}

	@Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        // 1. JSON 노드를 먼저 읽는다.
        JsonNode root = objectMapper.readTree(inputMessage.getBody());

        // 2. 공통영역이 있는지 확인 후, 없다면. 바로 return
        if (!root.has(FIELD_NM_COMMON)) {
            return new CustomHttpInputMessage(inputMessage, objectMapper.writeValueAsBytes(root));
        }

        JsonNode common = root.get(FIELD_NM_COMMON);
        log.debug("# 공통부: {}", common.toString());

        // SecureContext 생성 및 ThreadLocal 저장
        SecureContext secureContext = this.buildSecureContext(common);
        SecureContextStore.setContext(secureContext);

        // 3. 업무영역 있는지 확인
        if (!root.has(FIELD_NM_BODY)) {
            return new CustomHttpInputMessage(inputMessage, objectMapper.writeValueAsBytes(root));
        }

        // 3. 업무영역 처리
        JsonNode body = root.get(FIELD_NM_BODY);

        byte[] bodyContents = objectMapper.writeValueAsBytes(body);
        return new CustomHttpInputMessage(inputMessage, bodyContents);
    }
	
	/**
     * 공통부 전체를 SecureContext로 변환한다.
     * 
     * @param commonNode
     * @return
     */
    private SecureContext buildSecureContext(JsonNode commonNode) {
        return objectMapper.convertValue(commonNode, SecureContext.class);
    }
	
    /**
     * 커스텀 HttpInputMessage 클래스
     */
	private static class CustomHttpInputMessage implements HttpInputMessage {

        private final HttpHeaders headers;
        private final InputStream body;

        public CustomHttpInputMessage(HttpInputMessage inputMessage, byte[] bodyContents) {
            this.headers = inputMessage.getHeaders();
            this.body = new ByteArrayInputStream(bodyContents);
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.headers;
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.body;
        }

    }
}
