package com.scbank.process.api.svc.shared.components.frs.util;

import java.lang.management.ManagementFactory;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

public class FrsUniqueUtils {

	private static int nNumber = 0;

	public static synchronized String randomIdByPidToString11() {
		String processId = ManagementFactory.getRuntimeMXBean().getName();
		String strId = StringUtils.split(processId, "@")[0];
		
		// ProcessId 자리수 증가 개선
		// 1048575 까지 4자리
		if(strId.length() > 6) {
			strId = strId.substring(0, 6);
		}
		
		return randomIdByPortToString11(Integer.parseInt(strId));
	}
	
	public static String randomIdByPortToString11(int port) {

		String strPort = StringUtils.leftPad(String.valueOf(FrsNotationUtils.digits32(port)),5,"0");
		String strTime = StringUtils.leftPad(String.valueOf(FrsNotationUtils.digits32(currentDateToLong())), 5, "0");
		
		nNumber = (++nNumber % 32);

		String strSeq = StringUtils.leftPad(FrsNotationUtils.digits32(nNumber), 1, "0");
		
		return String.format("%s%s%s", strPort, strTime, strSeq);
	}	
	
	public static long currentDateToLong() {
		SimpleDateFormat dayTime_24 = new SimpleDateFormat("kkmmss");

		String day_time_24 = dayTime_24.format(new Date(System.currentTimeMillis()));
		
		return Long.parseLong(day_time_24);
	}
	
}
