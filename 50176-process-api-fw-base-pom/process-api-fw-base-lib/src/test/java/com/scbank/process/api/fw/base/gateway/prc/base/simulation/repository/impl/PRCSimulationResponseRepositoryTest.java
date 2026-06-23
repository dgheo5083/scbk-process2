package com.scbank.process.api.fw.base.gateway.prc.base.simulation.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PRCSimulationResponseRepositoryTest {

	@Mock
	ResourcePatternResolver resourceLoader;

	@Mock
	Resource resource;

	private PRCSimulationResponseRepository repository;

	@BeforeEach
	void setUp() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		repository = new PRCSimulationResponseRepository(objectMapper);

		Field field = PRCSimulationResponseRepository.class.getDeclaredField("resourceLoader");
		field.setAccessible(true);
		field.set(repository, resourceLoader);

		String json = """
				{
					"success": {"result": "OK" },
					"error": {"result": "FAIL" }
				}
				""";

		when(resource.exists()).thenReturn(true);
		when(resource.getFilename()).thenReturn("sample.json");
		when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(json.getBytes()));

		when(resourceLoader.getResources(anyString())).thenReturn(new Resource[] { resource });

//		clearRepositoryCache(repository);
	}

	@Test
	void getResponse_succcess() {
		JsonNode node = repository.getResponse("whatever-path", "sample.json", "success");

		assertNotNull(node);
		assertEquals("OK", node.get("result").asText());
	}

	@Test
	void getResponse_fallback() {
		JsonNode node = repository.getResponse("whatever-path", "sample.json", "not-exist");

		assertNotNull(node);
		assertEquals("OK", node.get("result").asText());
	}

	@Test
	void getResponse_empty() {
		JsonNode node = repository.getResponse("whatever-path", "sample.json", "");

		assertNotNull(node);
		assertEquals("OK", node.get("result").asText());
	}

//	@Test
//	void getResponse_noFile() throws Exception{
//
//		when(resourceLoader.getResource(anyString())).thenReturn(null);
//
//		assertThrows(RuntimeException.class,
//				() -> repository.getResponse("whatever-path", "not-exist.json", "success"));
//	}

	@Test
	void getResponse_cache() {

		JsonNode nodeFirst = repository.getResponse("whatever-path", "sample.json", "success");
		JsonNode nodeSecond = repository.getResponse("whatever-path", "sample.json", "success");

		assertSame(nodeFirst, nodeSecond);
	}

//	@SuppressWarnings("unchecked")
//	private void clearRepositoryCache(PRCSimulationResponseRepository repository) {
//
//		try {
//			var feild = PRCSimulationResponseRepository.class.getDeclaredField("cache");
//
//			feild.setAccessible(true);
//			((Map<String, ?>) feild.get(repository)).clear();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//
//	}

}
