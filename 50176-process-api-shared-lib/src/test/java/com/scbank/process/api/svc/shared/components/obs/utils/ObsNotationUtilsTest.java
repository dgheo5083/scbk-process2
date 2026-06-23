package com.scbank.process.api.svc.shared.components.obs.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("진수 변환 유틸 ObsNotationUtils")
public class ObsNotationUtilsTest {

	@Test
	@DisplayName("digits32 - 32진수 변환")
	public void digits32Test() {
		assertEquals("0", ObsNotationUtils.digits32(0));
		assertEquals("V", ObsNotationUtils.digits32(31));
		assertEquals("10", ObsNotationUtils.digits32(32));
	}

	@Test
	@DisplayName("digits64 - 64진수 변환")
	public void digits64Test() {
		assertEquals("0", ObsNotationUtils.digits64(0));
		assertEquals("$", ObsNotationUtils.digits64(63));
		assertEquals("10", ObsNotationUtils.digits64(64));
	}
}
