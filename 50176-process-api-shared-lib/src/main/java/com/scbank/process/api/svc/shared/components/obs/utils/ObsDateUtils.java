package com.scbank.process.api.svc.shared.components.obs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObsDateUtils {

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static int getMonth(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static int getYear(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static int getDate(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.DATE);
	}

	/**
	 * 
	 * @param dt
	 * @return
	 */
	public static int getMonth(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * 
	 * @param dt
	 * @return
	 */
	public static int getYear(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @param dt
	 * @return
	 */
	public static int getDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		return cal.get(Calendar.DATE);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static long calculate_Day(int year, int month, int date) {
		final String PATTERN = "yyyy-MM-dd";
		final int MILLIS_SECONDS_PER_DAY = 24 * 60 * 60 * 1000;
		long time = 0;
		try {

			String start_date = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(date);

			SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
			Date startDate = sdf.parse(start_date);
			Date endDate = new Date();
			time = (startDate.getTime() - endDate.getTime()) / MILLIS_SECONDS_PER_DAY;

			log.debug("----calculated result : {}----", time);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return time;
	}
}
