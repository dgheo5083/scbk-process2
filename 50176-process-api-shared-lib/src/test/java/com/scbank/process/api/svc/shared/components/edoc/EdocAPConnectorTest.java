package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocInfo;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class EdocAPConnectorTest {

//	@Mock private EdocHelper helper;
//
//	@InjectMocks private EdocAPConnector connector;
//
//	private EdocPayloadInfo fullPayload() {
//		EdocPayloadInfo payload = new EdocPayloadInfo();
//		payload.setAction(EdocConstant.EDOC_AP_ACTION_CREATE_SIGN_PDF);
//		payload.setCustNo("C001");
//		payload.setTradNo("T001");
//		payload.setBizType("FHLS"); // 퍼스트홈론 분기 커버
//		payload.setLpcMeta("META_SCNF_CUST_SSN");
//		payload.setLoanAccptNo("L001");
//		payload.setHeaderApplicationId("A001");
//		payload.setEmailAddr("a@b.com");
//		payload.setFidjbwgb("1");
//		payload.setSpouseCustNo("SC001");
//		payload.setSpouseTradNo("ST001");
//		payload.setSpouseTradNo2("ST002");
//		payload.setEndDate("20261231");
//		payload.setSsnNo("8001011234567");
//		payload.setTestKey("TK");
//		payload.setElementId("E001");
//		payload.setAcctNo("110");
//		payload.setCustSsn("8001011234567");
//		payload.setDocCode("D001");
//		payload.setChnnlMk("MB");
//		payload.setJobMk("J001");
//		payload.setBarcodeData("BC");
//		payload.setEdmsXvarmDocMeta("META");
//
//		EdocInfo edoc = new EdocInfo();
//		edoc.setEdocCode("ED01");
//		edoc.setEdocHash("H1");
//		edoc.setSignedHash("SH1");
//		edoc.setEdocFilePath("/tmp/a.pdf");
//		edoc.setElementId("EL1");
//		edoc.setEdocSignYn("Y");
//		payload.setEdocList(List.of(edoc));
//
//		return payload;
//	}
//
//	@Test
//	@DisplayName("request(payload) - 서버 불가 시 빈 JSONObject 반환")
//	public void requestNoServerTest() {
//		try (MockedStatic<EdocServerFactory> factory = mockStatic(EdocServerFactory.class);
//			 MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
//			factory.when(EdocServerFactory::getAvailableEdocServer).thenThrow(new PRCServiceException("no server"));
//			session.when(() -> SessionUtils.getSessionValue(anyString())).thenReturn("8001011234567");
//
//			JSONObject result = connector.request(fullPayload());
//			assertNotNull(result);
//		}
//	}
//
//	@Test
//	@DisplayName("request(payload, errorModule) - 오류 시 IT담당자 SMS 발송")
//	public void requestWithErrorModuleTest() {
//		try (MockedStatic<EdocServerFactory> factory = mockStatic(EdocServerFactory.class);
//			 MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
//			factory.when(EdocServerFactory::getAvailableEdocServer).thenThrow(new PRCServiceException("no server"));
//			session.when(() -> SessionUtils.getSessionValue(anyString())).thenReturn("8001011234567");
//
//			JSONObject result = connector.request(fullPayload(), "STEP1");
//
//			assertNotNull(result);
//			verify(helper, atLeastOnce()).sendLpcError(any(EdocPayloadInfo.class), anyString());
//		}
//	}
}
