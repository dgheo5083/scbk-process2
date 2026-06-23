package com.scbank.process.api.svc.shared.components.v3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ahnlab.v3mobileplus.webinterface.LicenseKeyManager;
import com.ahnlab.v3mobileplus.webinterface.V3MobileManager;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import org.mockito.MockedStatic;

@DisplayName("V3 모바일 웹 연동 컴포넌트")
public class V3WebMobileComponentTest {

	private final V3WebMobileComponent component = new V3WebMobileComponent();

	@Test
	@DisplayName("getV3LicenseEncTime - 라이선스 암호화 time 값 획득")
	public void getV3LicenseEncTimeTest() {
		try (MockedStatic<LicenseKeyManager> lm = mockStatic(LicenseKeyManager.class)) {
			LicenseKeyManager manager = mock(LicenseKeyManager.class);
			when(manager.getTimeValueForGettingEncLicenseKey()).thenReturn("160000");
			lm.when(LicenseKeyManager::getInstance).thenReturn(manager);

			assertEquals("160000", component.getV3LicenseEncTime());
		}
	}

	@Test
	@DisplayName("getV3LicenseEncTime - 예외 시 빈 문자열")
	public void getV3LicenseEncTimeErrorTest() {
		try (MockedStatic<LicenseKeyManager> lm = mockStatic(LicenseKeyManager.class)) {
			lm.when(LicenseKeyManager::getInstance).thenThrow(new RuntimeException("license error"));
			assertNotNull(component.getV3LicenseEncTime());
		}
	}

	@Test
	@DisplayName("getV3LicenseKeyEncrypt - 라이선스키 암호화")
	public void getV3LicenseKeyEncryptTest() {
		try (MockedStatic<LicenseKeyManager> lm = mockStatic(LicenseKeyManager.class);
			 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString("V3_MOBILE_WEB_LICENSEKEY")).thenReturn("license-key");
			LicenseKeyManager manager = mock(LicenseKeyManager.class);
			when(manager.getEncLicenseKey(anyString(), anyString())).thenReturn("enc-key");
			lm.when(LicenseKeyManager::getInstance).thenReturn(manager);

			assertEquals("enc-key", component.getV3LicenseKeyEncrypt("160000"));
		}
	}

	@Test
	@DisplayName("getV3CryptoKeyVersion - 암호화 키 버전 획득")
	public void getV3CryptoKeyVersionTest() {
		try (MockedStatic<V3MobileManager> vm = mockStatic(V3MobileManager.class)) {
			V3MobileManager manager = mock(V3MobileManager.class);
			when(manager.getCryptoKeyVersion()).thenReturn(7);
			vm.when(V3MobileManager::getInstance).thenReturn(manager);

			assertEquals("7", V3WebMobileComponent.getV3CryptoKeyVersion());
		}
	}
}
