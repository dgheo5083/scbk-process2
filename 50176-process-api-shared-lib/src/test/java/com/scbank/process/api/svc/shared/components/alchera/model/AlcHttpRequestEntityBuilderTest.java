package com.scbank.process.api.svc.shared.components.alchera.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

class AlcHttpRequestEntityBuilderTest {

	@Test
	@DisplayName("builder - id로 url/service/method 설정")
	void builderTest() {

		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("https://test.com/");

			AlcHttpRequestEntity entity = AlcFaceHttpRequestEntityBuilder.builder("face-check").build();

			assertThat(entity).isNotNull();

			assertThat(entity.getUrl()).isEqualTo("https://test.com/face-check");
			assertThat(entity.getMethod()).isEqualTo("POST");
		}
	}

	@Test
	@DisplayName("header - 헤더 설정")
	void headersTest() {

		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("https://test.com/");

			Map<String, String> headers = Map.of("Authorization", "Bearer token");

			AlcHttpRequestEntity entity = AlcHttpRequestEntityBuilder.builder("face-check").headers(headers).build();

			assertEquals(entity.getHeaders(), headers);
		}
	}

	@Test
	@DisplayName("body - body 설정")
	void bodyTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("https://test.com/");

			JSONObject body = new JSONObject();
			body.put("test", "1234");

			AlcHttpRequestEntity entity = AlcHttpRequestEntityBuilder.builder("face-check").bodyParameters(body)
					.build();

			assertEquals(entity.getBodyParameters(), body);
		}
	}

}
