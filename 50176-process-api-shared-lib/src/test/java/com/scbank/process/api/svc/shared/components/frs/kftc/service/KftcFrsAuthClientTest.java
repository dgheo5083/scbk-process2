package com.scbank.process.api.svc.shared.components.frs.kftc.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.frs.util.KftcFrsHelper;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcFrsAuthClientTest {

	@Mock private BaseHttpClient httpClient;

//	@Test
//	@DisplayName("getToken - 통신 실패 시 PRCServiceException")
//	public void getTokenFailTest() {
//		when(httpClient.execute(any(ClassicHttpRequest.class))).thenThrow(new RuntimeException("kftc fail"));
//
//		try (MockedStatic<KftcFrsHelper> helper = mockStatic(KftcFrsHelper.class)) {
//			helper.when(KftcFrsHelper::getClientId).thenReturn("client-id");
//			helper.when(KftcFrsHelper::getClientSecret).thenReturn("client-secret");
//
//			KftcFrsAuthClient client = new KftcFrsAuthClient(httpClient);
//			assertThrows(PRCServiceException.class, client::getToken);
//		}
//	}
}
