package com.scbank.process.api.svc.shared.components.frs.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유일키 생성 유틸 FrsUniqueUtils")
public class FrsUniqueUtilsTest {

	@Test
	@DisplayName("randomIdByPidToString11 - PID 기반 유일키 생성")
	public void randomIdByPidTest() {
		assertNotNull(FrsUniqueUtils.randomIdByPidToString11());
	}

	@Test
	@DisplayName("randomIdByPortToString11 - PORT 기반 유일키 생성")
	public void randomIdByPortTest() {
		assertNotNull(FrsUniqueUtils.randomIdByPortToString11(8080));
	}

	@Test
	@DisplayName("currentDateToLong - 현재 시각 long 변환")
	public void currentDateToLongTest() {
		assertTrue(FrsUniqueUtils.currentDateToLong() >= 0);
	}
}
