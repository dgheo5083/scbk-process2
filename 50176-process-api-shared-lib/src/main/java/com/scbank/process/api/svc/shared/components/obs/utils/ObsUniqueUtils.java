package com.scbank.process.api.svc.shared.components.obs.utils;

import java.lang.management.ManagementFactory;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * PORT키를 이용하여 하루만 유일키를 만들어주는 클래스.
 * 
 * @author 929948
 *
 */
public class ObsUniqueUtils {

	private static int nNumber = 0;

	/**
	 * 
	 * @return
	 */
	public static synchronized String randomIdByPidToString9() {
		String processId = ManagementFactory.getRuntimeMXBean().getName();
		String strId = StringUtils.split(processId, "@")[0];

		// ProcessId 자리수 증가 개선
		// 1048575 까지 4자리
		if (strId.length() > 6) {
			strId = strId.substring(0, 6);
		}

		return randomIdByPortToString9(Integer.parseInt(strId));
	}

	/**
	 * 
	 * @param port
	 * @return
	 */
	public static String randomIdByPortToString9(int port) {

		String strPort = StringUtils.leftPad(String.valueOf(ObsNotationUtils.digits32(port)), 4, "0");
		String strTime = StringUtils.leftPad(String.valueOf(ObsNotationUtils.digits32(currentDateToLong())), 4, "0");

		nNumber = (++nNumber % 32);

		String strSeq = StringUtils.leftPad(ObsNotationUtils.digits32(nNumber), 1, "0");

		return String.format("%s%s%s", strPort, strTime, strSeq);
	}

	/**
	 * 
	 * @return
	 */
	public static long currentDateToLong() {
		SimpleDateFormat dayTime_24 = new SimpleDateFormat("kkmmss");

		String day_time_24 = dayTime_24.format(new Date(System.currentTimeMillis()));

		return Long.parseLong(day_time_24);
	}
}
