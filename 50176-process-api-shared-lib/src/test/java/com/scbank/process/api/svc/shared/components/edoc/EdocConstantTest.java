package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EdocConstant 상수 정의")
public class EdocConstantTest {

	@Test
	@DisplayName("주요 액션 상수 값 확인")
	public void constantValuesTest() {
		assertEquals("CreateSignPDF", EdocConstant.EDOC_AP_ACTION_CREATE_SIGN_PDF);
		assertEquals("healthCheck", EdocConstant.EDOC_AP_ACTION_HEALTH_CHECK);
		assertEquals("ContractDocDown", EdocConstant.EDOC_AP_ACTION_EDMS_DOC_DOWNLOAD);
		assertEquals("_msg_", EdocConstant.MessageConstants_MSG_ROOT);
		assertEquals("_body_", EdocConstant.MessageConstants_MSG_BODY);
	}
}
