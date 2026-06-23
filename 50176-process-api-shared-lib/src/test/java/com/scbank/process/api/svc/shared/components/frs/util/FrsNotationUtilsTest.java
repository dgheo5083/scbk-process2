package com.scbank.process.api.svc.shared.components.frs.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("진수 변환 유틸 FrsNotationUtils")
public class FrsNotationUtilsTest {

	@Test
	@DisplayName("digits32 - 32진수 변환")
	public void digits32Test() {
		assertEquals("0", FrsNotationUtils.digits32(0));
		assertEquals("V", FrsNotationUtils.digits32(31));
		assertEquals("10", FrsNotationUtils.digits32(32));
	}
}
