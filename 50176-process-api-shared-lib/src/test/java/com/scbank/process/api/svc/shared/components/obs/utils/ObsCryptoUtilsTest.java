package com.scbank.process.api.svc.shared.components.obs.utils;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

@DisplayName("OBS 암복호화 유틸 ObsCryptoUtils")
public class ObsCryptoUtilsTest {

	@Test
	@DisplayName("encrypt - 입력 없으면 PRCServiceException")
	public void encryptNullTest() {
		assertThrows(PRCServiceException.class, () -> ObsCryptoUtils.encrypt(null));
		assertThrows(PRCServiceException.class, () -> ObsCryptoUtils.encrypt(""));
	}

	@Test
	@DisplayName("decrypt - 입력 없으면 null")
	public void decryptNullTest() {
		assertNull(ObsCryptoUtils.decrypt(null));
		assertNull(ObsCryptoUtils.decrypt(""));
	}

	@Test
	@DisplayName("encrypt - 키 미설정/모듈 오류 시 PRCServiceException")
	public void encryptErrorTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn(null);
			assertThrows(PRCServiceException.class, () -> ObsCryptoUtils.encrypt("data"));
		}
	}

	@Test
	@DisplayName("decrypt - 키 미설정/모듈 오류 시 PRCServiceException")
	public void decryptErrorTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn(null);
			assertThrows(PRCServiceException.class, () -> ObsCryptoUtils.decrypt("data"));
		}
	}
}
