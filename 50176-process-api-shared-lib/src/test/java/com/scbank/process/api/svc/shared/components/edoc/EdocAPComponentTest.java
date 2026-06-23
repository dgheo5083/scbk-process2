package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class EdocAPComponentTest {

	@Mock private EdocAPConnector connector;

	@InjectMocks private EdocAPComponent component;

	private final EdocPayloadInfo payload = new EdocPayloadInfo();

	@BeforeEach
	void setUp() {
		when(connector.request(any(EdocPayloadInfo.class), anyString())).thenReturn(new JSONObject());
		when(connector.request(any(EdocPayloadInfo.class))).thenReturn(new JSONObject());
	}

	@Test
	@DisplayName("키 지정 호출 액션들이 connector.request(payload, key) 위임")
	public void keyedActionsTest() {
		assertNotNull(component.reqCreateSignPDF(payload));
		assertNotNull(component.requestAMSHost(payload));
		assertNotNull(component.requestLPCImage(payload));
		assertNotNull(component.requestLPCMeta(payload));
		assertNotNull(component.requestAddTrans(payload));
		assertNotNull(component.requestInternetLoanImageMetaTrans(payload));
		assertNotNull(component.requestOfficialTransRegist(payload));
		assertNotNull(component.requestLPCRecovery(payload));
		assertNotNull(component.requestOfficailRecovery(payload));
		assertNotNull(component.requestEmailSend(payload));
		assertNotNull(component.requestEmailTermsSend(payload));
		assertNotNull(component.requestLPCMetaTransfer(payload));
		assertNotNull(component.reqESLCreateSignPDF(payload));
		assertNotNull(component.requestAddDocs(payload));
	}

	@Test
	@DisplayName("키 미지정 호출 액션들이 connector.request(payload) 위임")
	public void noKeyActionsTest() {
		assertNotNull(component.requestWarrentPost(payload));
		assertNotNull(component.requestLPCImageBackUp(payload));
		assertNotNull(component.requestHealthCheck(payload));
		assertNotNull(component.requestEdmsXvarmDocDownload(payload));
		assertNotNull(component.requestEdmsXvarmDocCheck(payload));
	}
}
