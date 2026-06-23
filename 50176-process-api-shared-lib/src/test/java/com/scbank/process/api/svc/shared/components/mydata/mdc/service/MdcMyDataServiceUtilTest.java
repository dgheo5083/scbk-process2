package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import static org.mockito.Mockito.mock;
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

import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdcMyDataServiceUtilTest {

	@Mock private MdcMyDataAuthManager myDataAuthManager;

	@InjectMocks private MdcMyDataServiceUtil serviceUtil;

	@Test
	@DisplayName("getToken - 인증 관리자에 위임")
	public void getTokenTest() {
		MdcMyDataAuthVo vo = mock(MdcMyDataAuthVo.class);
		when(myDataAuthManager.getToken()).thenReturn(vo);

		serviceUtil.getToken();
		verify(myDataAuthManager).getToken();
	}

	@Test
	@DisplayName("changeToken - 인증 관리자에 위임")
	public void changeTokenTest() {
		serviceUtil.changeToken();
		verify(myDataAuthManager).changeToken();
	}

	@Test
	@DisplayName("enableToken - 인증 관리자에 위임")
	public void enableTokenTest() {
		MyDataHttpJsonObject result = mock(MyDataHttpJsonObject.class);
		serviceUtil.enableToken("20260601", result);
		verify(myDataAuthManager).enableToken("20260601", result);
	}

	@Test
	@DisplayName("disableToken - 인증 관리자에 위임")
	public void disableTokenTest() {
		serviceUtil.disableToken();
		verify(myDataAuthManager).disableToken();
	}
}
