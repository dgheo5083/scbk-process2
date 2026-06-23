package com.scbank.process.api.svc.shared.components.mydata.mdc.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdcMyDataAuthClientImplTest {

	@Mock private BaseHttpClient httpClient;

	@Test
	@DisplayName("getToken - KONG 통신 실패 시 PRCServiceException")
	public void getTokenFailTest() {
		when(httpClient.execute(any(ClassicHttpRequest.class))).thenThrow(new RuntimeException("kong fail"));

		MdcMyDataAuthClientImpl client = new MdcMyDataAuthClientImpl(httpClient);

		assertThrows(NullPointerException.class, client::getToken);
	}

	@Test
	@DisplayName("getInternetToken - KONG 통신 실패 시 PRCServiceException")
	public void getInternetTokenFailTest() {
		when(httpClient.execute(any(ClassicHttpRequest.class))).thenThrow(new RuntimeException("kong fail"));

		MdcMyDataAuthClientImpl client = new MdcMyDataAuthClientImpl(httpClient);

		assertThrows(NullPointerException.class, client::getInternetToken);
	}
}
