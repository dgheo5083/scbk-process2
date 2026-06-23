package com.scbank.process.api.svc.shared.components.obs.kftc.service;

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
import com.scbank.process.api.svc.shared.components.obs.utils.KftcObsHelper;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcObsAuthClientTest {

	@Mock private BaseHttpClient httpClient;

	@Test
	@DisplayName("getToken - 통신 실패 시 예외 전파")
	public void getTokenFailTest() {
		when(httpClient.execute(any(ClassicHttpRequest.class))).thenThrow(new RuntimeException("kftc fail"));

		try (MockedStatic<KftcObsHelper> helper = mockStatic(KftcObsHelper.class)) {
			helper.when(KftcObsHelper::getClientId).thenReturn("client-id");
			helper.when(KftcObsHelper::getClientSecret).thenReturn("client-secret");

			KftcObsAuthClient client = new KftcObsAuthClient(httpClient);
			assertThrows(NullPointerException.class, client::getToken);
		}
	}
}
