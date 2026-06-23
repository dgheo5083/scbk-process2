package com.scbank.process.api.svc.shared.components.mydata.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("날짜 유틸 MyDataDateUtils")
public class MyDataDateUtilsTest {

	@Test
	@DisplayName("long 기반 연/월/일 추출")
	public void longBasedTest() {
		long now = System.currentTimeMillis();
		assertTrue(MyDataDateUtils.getYear(now) >= 2024);
		assertTrue(MyDataDateUtils.getMonth(now) >= 0);
		assertTrue(MyDataDateUtils.getDate(now) >= 1);
	}

	@Test
	@DisplayName("Date 기반 연/월/일 추출")
	public void dateBasedTest() {
		Date now = new Date();
		assertTrue(MyDataDateUtils.getYear(now) >= 2024);
		assertTrue(MyDataDateUtils.getMonth(now) >= 0);
		assertTrue(MyDataDateUtils.getDate(now) >= 1);
	}

	@Test
	@DisplayName("calculate_Day - 일수 계산")
	public void calculateDayTest() {
		assertTrue(MyDataDateUtils.calculate_Day(2020, 1, 1) < 0);
	}
}
