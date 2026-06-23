package com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.SimulationResponseRepository;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 프로세스API 시뮬레이션 응답 레포지토리 구현 클래스
 */
@RequiredArgsConstructor
public class PRCSimulationResponseRepository implements SimulationResponseRepository {

	private final ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
	
	private final ObjectMapper objectMapper;

	private final Map<String, JsonNode> cache = new ConcurrentHashMap<>();
	
	@PostConstruct
	public void init() {
		
	}

	@Override
	public JsonNode getResponse(String configLocation, String fileName, String senario) {
		String base = configLocation;//this.ensureEndsWithSlash(configLocation);
		String cacheKey = base + "|" + fileName;
		
		JsonNode root = cache.computeIfAbsent(cacheKey, k -> loadFile(base, fileName));
		if (root == null) {
			throw new PRCServiceException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), "등록된 대응답파일을 찾지 못하였습니다.");
		}
		
		String sc = (StringUtils.isEmpty(senario)) ? "success": senario.trim().toLowerCase();
		JsonNode node = root.get(sc);
		if (node == null) {
			node = root.get("success");
		}
		return node;
	}
	
	private JsonNode loadFile(String base, String fileName) {
		try {
			Resource[] resources = resourceLoader.getResources(base);
			if (resources == null || resources.length == 0) {
				return null;
			}
			
			Resource resource = Arrays.asList(resources).stream()
				.filter(Resource::exists)
				.filter(r -> r.getFilename().equals(fileName))
				.findFirst()
				.orElse(null);
			
			try (InputStream is = resource.getInputStream()){
				return  objectMapper.readTree(is);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load simulation json: " + fileName, e);
		}
	}
}
