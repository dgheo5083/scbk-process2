package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdcMyDataPubServiceUtilTest {

	@Mock private MdcMyDataAuthManager myDataAuthManager;

	@InjectMocks private MdcMyDataPubServiceUtil pubServiceUtil;

	@Test
	@DisplayName("getInternetToken - 인터넷 토큰 변경 수행")
	public void getInternetTokenTest() {
		pubServiceUtil.getInternetToken();
		verify(myDataAuthManager).changeInternetToken();
	}

	@Test
	@DisplayName("changeInternetToken - 위임")
	public void changeInternetTokenTest() {
		pubServiceUtil.changeInternetToken();
		verify(myDataAuthManager).changeInternetToken();
	}

	@Test
	@DisplayName("enableInternetToken - 위임")
	public void enableInternetTokenTest() {
		MyDataHttpJsonObject result = mock(MyDataHttpJsonObject.class);
		pubServiceUtil.enableInternetToken("20260601", result);
		verify(myDataAuthManager).enableInternetToken("20260601", result);
	}

	@Test
	@DisplayName("disableInternetToken - 위임")
	public void disableInternetTokenTest() {
		pubServiceUtil.disableInternetToken();
		verify(myDataAuthManager).disableInternetToken();
	}
}
