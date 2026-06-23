package com.scbank.process.api.svc.shared.components.obs.kftc.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.svc.shared.components.obs.kftc.model.KftcObsAuthVo;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcObsServiceTest {

	@Mock private KftcObsTokenManager kftcObsTokenManager;

	@InjectMocks private KftcObsService service;

	@Test
	@DisplayName("getToken - 토큰 관리자에 위임")
	public void getTokenTest() {
		when(kftcObsTokenManager.getToken()).thenReturn(new KftcObsAuthVo());

		assertNotNull(service.getToken());
		verify(kftcObsTokenManager).getToken();
	}
}
