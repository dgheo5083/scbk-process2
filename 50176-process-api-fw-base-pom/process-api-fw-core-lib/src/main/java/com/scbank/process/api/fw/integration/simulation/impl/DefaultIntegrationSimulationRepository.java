package com.scbank.process.api.fw.integration.simulation.impl;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.integration.IntegrationProperties;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.codec.XmlIntegrationClientCodec;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationHeaderStrategy;
import com.scbank.process.api.fw.integration.simulation.IntegrationSimulationRepository;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.MessageFormat;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템 연계 대응답 메시지 저장소 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultIntegrationSimulationRepository implements IntegrationSimulationRepository {

	/**
	 * 전문 대응답 저장소
	 */
	private Map<StoreKey, String> repository = new ConcurrentHashMap<>();

	/**
	 * Integration Configuration Properties
	 */
	private final IntegrationProperties properties;

	/**
	 * 연계시스템 별 헤더 생성 전략 인터페이스 목록
	 */
	private final List<IntegrationSimulationHeaderStrategy<?, ?>> headerStrategies;

	/**
	 * ObjectMapper
	 */
	private final ObjectMapper objectMapper;

	/**
	 * 
	 */
	private final XmlIntegrationClientCodec xmlIntegrationClientCodec;

	/**
	 * MessageContext Creator
	 */
	private final IntegrationMessageContextCreator integrationMessageContextCreator;

	/**
	 * resource loader
	 */
	private ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();

	@Override
	public String getResponse(String systemId, String interfaceId) {
		IntegrationProperties.IntegrationSystemConfig config = properties.getSystem().get(systemId);
		if (config == null || config.simulation() == null || !config.simulation().enabled()) {
			throw new IllegalStateException("[" + systemId + "]시스템 시뮬레이션 설정이 활성화되지 않았습니다.");
		}

		StoreKey key = StoreKey.builder()
				.systemId(systemId)
				.interfaceId(interfaceId)
				.build();

		return this.repository.computeIfAbsent(key, k -> {
			String basePath = config.simulation().configLocation();
			String path = basePath + interfaceId + ".xml";
			try {
				Resource resource = resourceLoader.getResource(path);
				return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				throw new IllegalArgumentException("시뮬레이션 응답 파일을 찾을 수 없습니다: " + path, e);
			}
		});
	}

	@Override
	public <O extends IMessageObject> O getResponse(String systemId, String interfaceId, Class<O> responseType) {
		String simulationResponse = this.getResponse(systemId, interfaceId);
		O response = null;
		try {
			final IntegrationSystemConfig config = properties.getSystem().get(systemId);
			MessageContext messageContext = integrationMessageContextCreator.create(MessageFormat.XML,
					config.charset());
			JsonNode rootNode = objectMapper.readTree(simulationResponse);
			JsonNode responseNode = rootNode.path("RES_DATA_PART");
			byte[] responseBytes = objectMapper.writeValueAsBytes(responseNode);
			response = xmlIntegrationClientCodec.decode(messageContext, responseBytes, responseType);
		} catch (Exception e) {
			// 소나큐브 관련 수정
			log.error(e.getMessage(), e);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H> H getHeader(String systemId, String interfaceId, Class<H> headerType) {
		String simulationResponse = this.getResponse(systemId, interfaceId);
		if (CollectionUtils.isEmpty(this.headerStrategies)) {
			return null;
		}

		IntegrationSimulationHeaderStrategy<?, ?> integrationSimulationHeaderStrategy = this.headerStrategies.stream()
				.filter(h -> h.supported(systemId)).findFirst().orElse(null);
		if (integrationSimulationHeaderStrategy == null) {
			return null;
		}

		return (H) integrationSimulationHeaderStrategy.getHeader(simulationResponse);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <E> E getError(String systemId, String interfaceId, Class<E> headerType) {
		String simulationResponse = this.getResponse(systemId, interfaceId);
		if (CollectionUtils.isEmpty(this.headerStrategies)) {
			return null;
		}

		IntegrationSimulationHeaderStrategy<?, ?> integrationSimulationHeaderStrategy = this.headerStrategies.stream()
				.filter(h -> h.supported(systemId)).findFirst().orElse(null);
		if (integrationSimulationHeaderStrategy == null) {
			return null;
		}

		return (E) integrationSimulationHeaderStrategy.getErrorMsg(simulationResponse);
	}
	
	
	@Data
	@Builder
	public static class StoreKey implements Serializable {

		private static final long serialVersionUID = 1L;

		private String systemId;
		private String interfaceId;
	}
}
