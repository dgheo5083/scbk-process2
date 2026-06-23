package com.scbank.process.api.svc.shared.components.obs.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("날짜 유틸 ObsDateUtils")
public class ObsDateUtilsTest {

	@Test
	@DisplayName("long 기반 연/월/일 추출")
	public void longBasedTest() {
		long now = System.currentTimeMillis();
		assertTrue(ObsDateUtils.getYear(now) >= 2024);
		assertTrue(ObsDateUtils.getMonth(now) >= 0);
		assertTrue(ObsDateUtils.getDate(now) >= 1);
	}

	@Test
	@DisplayName("Date 기반 연/월/일 추출")
	public void dateBasedTest() {
		Date now = new Date();
		assertTrue(ObsDateUtils.getYear(now) >= 2024);
		assertTrue(ObsDateUtils.getMonth(now) >= 0);
		assertTrue(ObsDateUtils.getDate(now) >= 1);
	}

	@Test
	@DisplayName("calculate_Day - 일수 계산")
	public void calculateDayTest() {
		// 과거 날짜이므로 음수 (예외 없이 계산되는지 확인)
		long days = ObsDateUtils.calculate_Day(2020, 1, 1);
		assertTrue(days < 0);
	}
}
