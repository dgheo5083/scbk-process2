package com.scbank.process.api.svc.shared.components.alchera.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

class AlcFaceHttpRequestEntityBuilderTest {

	@Test
	@DisplayName("builder - id로 url/service/method 설정")
	void builderTest() {
		 
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(()->PropertiesUtils.getString(anyString())).thenReturn("https://test.com/");
			
			AlcHttpRequestEntity entity = AlcFaceHttpRequestEntityBuilder.builder("face-check").build();
			
			assertThat(entity).isNotNull();
			
			assertThat(entity.getUrl()).isEqualTo("https://test.com/face-check");
			assertThat(entity.getMethod()).isEqualTo("POST");
		}
	}

}
