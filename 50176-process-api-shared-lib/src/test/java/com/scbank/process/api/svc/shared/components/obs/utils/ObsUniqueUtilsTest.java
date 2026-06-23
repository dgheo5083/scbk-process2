package com.scbank.process.api.svc.shared.components.obs.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유일키 생성 유틸 ObsUniqueUtils")
public class ObsUniqueUtilsTest {

	@Test
	@DisplayName("randomIdByPidToString9 - PID 기반 유일키 생성")
	public void randomIdByPidTest() {
		assertNotNull(ObsUniqueUtils.randomIdByPidToString9());
	}

	@Test
	@DisplayName("randomIdByPortToString9 - PORT 기반 유일키 생성")
	public void randomIdByPortTest() {
		String id = ObsUniqueUtils.randomIdByPortToString9(8080);
		assertNotNull(id);
		assertTrue(id.length() >= 9);
	}

	@Test
	@DisplayName("currentDateToLong - 현재 시각 long 변환")
	public void currentDateToLongTest() {
		assertTrue(ObsUniqueUtils.currentDateToLong() >= 0);
	}
}
