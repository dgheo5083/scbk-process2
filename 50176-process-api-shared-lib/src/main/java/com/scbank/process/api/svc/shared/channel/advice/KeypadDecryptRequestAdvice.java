package com.scbank.process.api.svc.shared.channel.advice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SecureTokensInfo;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.svc.shared.utils.KeypadDecryptUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 키보드 보안 복호화
 */
@Slf4j
@RequiredArgsConstructor
@Order(11)
@RestControllerAdvice(basePackages = "com.scbank")
public class KeypadDecryptRequestAdvice extends RequestBodyAdviceAdapter {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasParameterAnnotation(RequestBody.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		// 1. JSON 노드를 먼저 읽는다.
		ObjectNode body = (ObjectNode) objectMapper.readTree(inputMessage.getBody());

		IServiceContext ctx = ServiceContextHolder.getContext();

		HttpServletRequest httpRequest = ctx.request();

		/**
		 * 1. SecureContextStore(ThreadLocal)에서 키패드 정보 추출 (encType : ASTX2, nFilter -
		 * 암호화 타입 / encValue : 암호화 값)
		 */
		Optional<SecureContext> secureContext = SecureContextStore.getContext();

		if (secureContext == null || secureContext.isEmpty() || secureContext.get().getKeypad() == null) {
			return new CustomHttpInputMessage(inputMessage, objectMapper.writeValueAsBytes(body));
		}

		Map<String, String> decryptedMap = KeypadDecryptUtils.decrypt(httpRequest);

		if (decryptedMap == null || decryptedMap.isEmpty()) {
			return new CustomHttpInputMessage(inputMessage, objectMapper.writeValueAsBytes(body));
		}

		log.debug("### before Body : [{}]", body);

		decryptedMap.forEach((k, v) -> {
			if (k != null && v != null) {
				JsonNode node = body.get(k);
				if(node != null && !node.isNull()) {
					// E2E 필드 validation 성공하면 기존값이 있으면 기존값 set
					log.info("body.get(k) : [{}]", body.get(k).asText());
					if(!"".equals(v) && StringUtils.isNotEmpty(body.get(k).asText())) {
						v = body.get(k).asText();
					}
				}
				body.put(k, v);
			}
		});

		secureContext.ifPresent(tokenCtx -> {
			SecureTokensInfo existing = tokenCtx.getTokens();
			if (existing == null) {
				existing = new SecureTokensInfo();
			}

			try {
				objectMapper.updateValue(existing, decryptedMap);
			} catch (JsonMappingException e) {
				throw new RuntimeException("SecureTokensInfo merge fail", e);
			}

			tokenCtx.setTokens(existing);
		});

		log.debug("### after Body : [{}]", body);

		log.debug("### secureContext : [{}]", secureContext);

		return new CustomHttpInputMessage(inputMessage, objectMapper.writeValueAsBytes(body));
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
