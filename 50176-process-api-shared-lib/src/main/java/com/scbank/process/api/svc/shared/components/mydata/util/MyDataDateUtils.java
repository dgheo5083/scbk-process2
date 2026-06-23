package com.scbank.process.api.svc.shared.components.mydata.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDataDateUtils {

	public static int getMonth(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		;

		return cal.get(Calendar.MONTH);
	}

	public static int getYear(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		;

		return cal.get(Calendar.YEAR);
	}

	public static int getDate(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		;

		return cal.get(Calendar.DATE);
	}

	public static int getMonth(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		return cal.get(Calendar.MONTH);
	}

	public static int getYear(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		return cal.get(Calendar.YEAR);
	}

	public static int getDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		return cal.get(Calendar.DATE);
	}

	public static long calculate_Day(int year, int month, int date) {
		final String PATTERN = "yyyy-MM-dd";
		final int MILLI_SECONDS_PER_DAY = 24 * 60 * 60 * 1000;
		long time = 0;
		try {

			String start_date = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(date);

			SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
			Date startDate = sdf.parse(start_date);
			Date endDate = new Date();
			time = (startDate.getTime() - endDate.getTime()) / MILLI_SECONDS_PER_DAY;

			System.out.println("----calculated result : " + time + "----");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return time;
	}
}
