package com.scbank.process.api.fw.base.gateway.prc.base.simulation;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.annotation.SimulationMode;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.SimulationResponseRepository;
import com.scbank.process.api.fw.core.utils.StringUtils;

import feign.Request;
import feign.Response;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;

/**
 * 시뮬레이션 응답 팩토리 클래스
 */
@RequiredArgsConstructor
public class SimulationResponseFactory {

	/**
	 * 시뮬레이션 응답 레파지토리
	 */
	private final SimulationResponseRepository repository;
	
	/**
	 * 정상응답 디코더
	 */
	private final Decoder decoder;
	
	/**
	 * 오류응답 디코더
	 */
	private final ErrorDecoder errorDecoder;
	
	
	/**
	 * 
	 */
	private final ObjectMapper objectMapper;
	
	/**
	 * 
	 * @param target
	 * @param method
	 * @param simulation
	 * @param configLocation
	 * @return
	 * @throws Exception
	 */
	public Object createOrThrow(Target<?> target, Method method, SimulationMode simulation, String configLocation) throws Exception {
		String fileName = SimulationKeyResolver.fileName(method);
		JsonNode node = this.repository.getResponse(configLocation, fileName, simulation.senario());
		if (node == null) {
			return null;
		}
		
		JsonNode typeNode = node.get("type");
		if (typeNode != null && "HTTP_ERROR".equalsIgnoreCase(typeNode.asText())) {
			throw decodeHttpError(target, method, node);
		}
		
		return this.decode(target, method, node);
	}
	
	/**
	 * 
	 * @param target
	 * @param method
	 * @param node
	 * @return
	 * @throws Exception
	 */
	private Object decode(Target<?> target, Method method, JsonNode node) throws Exception {
		String httpMethod = SimulationKeyResolver.httpMethod(method);
		String path = SimulationKeyResolver.path(method);
		String url = this.join(target.url(), path);
		
		Map<String, Collection<String>> headers = this.parseHeaders(node);
		String body = toBodyString(node.get("payload"));
		
		Request req = Request.create(
				Request.HttpMethod.valueOf(httpMethod), 
				url, 
				Collections.emptyMap(), 
				null, 
				StandardCharsets.UTF_8, 
				null
			);
			
			Response res = Response.builder()
				.status(HttpStatus.OK.value())
				.reason("")
				.request(req)
				.headers(headers)
				.body(body, StandardCharsets.UTF_8)
				.build();
			
		JavaType javaType = objectMapper.getTypeFactory().constructType(method.getGenericReturnType());
		return decoder.decode(res, javaType);
	}
	
	/**
	 * 
	 * @param target
	 * @param method
	 * @param errorResponse
	 * @return
	 * @throws Exception
	 */
	private Exception decodeHttpError(Target<?> target, Method method, JsonNode errorResponse) throws Exception {
		int status = errorResponse.path("status").asInt(500);
		String reason = errorResponse.path("reason").asText("SIMULATED_HTTP_ERROR");
		
		Map<String, Collection<String>> headers = this.parseHeaders(errorResponse);
		String body = toBodyString(errorResponse.get("payload"));
		
		String httpMethod = SimulationKeyResolver.httpMethod(method);
		String path = SimulationKeyResolver.path(method);
		String url = this.join(target.url(), path);
		
		Request req = Request.create(
			Request.HttpMethod.valueOf(httpMethod), 
			url, 
			Collections.emptyMap(), 
			null, 
			StandardCharsets.UTF_8, 
			null
		);
		
		Response res = Response.builder()
			.status(status)
			.reason(reason)
			.request(req)
			.headers(headers)
			.body(body, StandardCharsets.UTF_8)
			.build();
		
		String methodKey = target.type().getName() + "#" + method.getName();
		return errorDecoder.decode(methodKey, res);
	}
	
	/**
	 * 
	 * @param headersNode
	 * @return
	 */
	private Map<String, Collection<String>> parseHeaders(JsonNode headersNode) {
		Map<String, Collection<String>> headers = new LinkedHashMap<>();
		if (headersNode == null || !headersNode.isObject()) {
			return headers;
		}
		
		headersNode.properties().forEach(e -> {
			List<String> values = new ArrayList<>();
			if (e.getValue().isArray()) {
				e.getValue().forEach(v -> values.add(v.asText()));
			} else {
				values.add(e.getValue().asText());
			}
			headers.put(e.getKey(), values);
		});
		return headers;
	}
	
	/**
	 * 
	 * @param bodyNode
	 * @return
	 * @throws Exception
	 */
	private String toBodyString(JsonNode bodyNode) throws Exception {
		if (bodyNode == null) {
			return StringUtils.EMPTY;
		}
		
		if (bodyNode.isTextual()) {
			return bodyNode.asText();
		}
		
		return objectMapper.writeValueAsString(bodyNode);
	}
	
	/**
	 * 
	 * @param baseUrl
	 * @param path
	 * @return
	 */
	private String join(String baseUrl, String path) {
		if (baseUrl.endsWith("/") && path.startsWith("/")) {
			return baseUrl.substring(0, baseUrl.length() - 1) + path;
		}
		
		if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
			return baseUrl + "/" + path;
		}
		return baseUrl + path;
	}
}
