package com.scbank.process.api.svc.shared.components.hsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HSM 상수 정의")
public class HSMConstantsTest {

	@Test
	@DisplayName("암호화 관련 상수 값 확인")
	public void constantsTest() {
		assertEquals("AES/CBC/PKCS5Padding", HSMConstants.HSM_TRANSFORMATION);
		assertEquals("AES", HSMConstants.HSM_AES);
		assertEquals("Y", HSMConstants.STR_Y);
		assertEquals("N", HSMConstants.STR_N);
	}

	@Test
	@DisplayName("CSL API명 enum")
	public void cslApiNameEnumTest() {
		assertEquals("CreditLoanLimitInquiry", HSMConstants.CSLApiNameConstants.CREDITLOAN.getName());
		assertNotNull(HSMConstants.CSLApiNameConstants.MORTGAGELOAN.getDescription());
		assertTrue(HSMConstants.CSLApiNameConstants.values().length >= 3);
	}

	@Test
	@DisplayName("HSM 오류코드 enum")
	public void hsmErrorEnumTest() {
		assertEquals("HSM_0001", HSMConstants.HSMErrorConstants.H0001.getErrorCode());
		assertNotNull(HSMConstants.HSMErrorConstants.H0002.getErrorMsg());
	}
}
