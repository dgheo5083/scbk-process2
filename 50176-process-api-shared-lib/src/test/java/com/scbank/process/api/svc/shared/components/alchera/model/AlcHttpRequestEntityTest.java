package com.scbank.process.api.svc.shared.components.alchera.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlcHttpRequestEntityTest {

	@Test
	@DisplayName("getter/setter 테스트")
	void getterSetterTest() {
		AlcHttpRequestEntity entity = new AlcHttpRequestEntity();
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer token");
		
		JSONObject body = new JSONObject();
		body.put("name", "test");
		
		entity.setHeaders(headers);
		entity.setBodyParameters(body);
		entity.setUrl("https://test.com");
		entity.setMethod("POST");
		entity.setOption(1);
		
		assertEquals(headers, entity.getHeaders());
		assertEquals(body, entity.getBodyParameters());
		assertEquals("https://test.com", entity.getUrl());
		assertEquals("POST", entity.getMethod());
		assertEquals(1, entity.getOption());
		
	}
	
	@Test
	@DisplayName("toString 테스트")
	void toStringTest() {
		AlcHttpRequestEntity entity = new AlcHttpRequestEntity();
		
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer token");
		
		JSONObject body = new JSONObject();
		body.put("name", "test");
		
		entity.setHeaders(headers);
		entity.setBodyParameters(body);
		entity.setUrl("https://test.com");
		entity.setMethod("POST");
		entity.setOption(1);
		
		String result = entity.toString();
		
		assertTrue(result.contains("https://test.com"));
		assertTrue(result.contains("POST"));
		assertTrue(result.contains("Authorization"));
		assertTrue(result.contains("name"));
		
	}

}
