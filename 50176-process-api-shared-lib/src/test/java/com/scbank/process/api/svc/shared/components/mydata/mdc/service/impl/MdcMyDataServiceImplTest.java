package com.scbank.process.api.svc.shared.components.mydata.mdc.service.impl;

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

import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataAuthManager;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdcMyDataServiceImplTest {

	@Mock private MdcMyDataAuthManager myDataAuthManager;

	@InjectMocks private MdcMyDataServiceImpl service;

	@Test
	@DisplayName("getToken/getSelectToken/changeToken/disableToken 위임")
	public void delegationTest() {
		service.getToken();
		verify(myDataAuthManager).getToken();

		service.getSelectToken();
		verify(myDataAuthManager).getSelectToken();

		service.changeToken();
		verify(myDataAuthManager).changeToken();

		service.disableToken();
		verify(myDataAuthManager).disableToken();
	}

	@Test
	@DisplayName("enableToken 위임")
	public void enableTokenTest() {
		MyDataHttpJsonObject result = mock(MyDataHttpJsonObject.class);
		service.enableToken("20260601", result);
		verify(myDataAuthManager).enableToken("20260601", result);
	}
}
