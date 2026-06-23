package com.scbank.process.api.svc.shared.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.interezen.common.util.coder.Coder;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CommonBizUtils {

	/*
	 * 세션 컨텍스트 매니저
	 */
	private ISessionContextManager sessionManager;

	/**
	 * <p>
	 * Device 정보를 Map 형태로 반환한다.
	 * </p>
	 *
	 * @param mduInfo Device정보 String
	 * @return Device 정보 Map (불필요한 부분은 삭제 처리)
	 */
	public static Map<String, String> getDataMap(String mduInfo) throws PRCServiceException {
		return getDataMap(mduInfo, false);
	}

	/**
	 * <p>
	 * Device 정보를 Map 형태로 반환한다.
	 * </p>
	 *
	 * @param mduInfo Device정보 String
	 * @return Device 정보 Map (불필요한 부분은 삭제 처리)
	 */
	public static Map<String, String> getDataMap(String mduInfo, boolean isRawStr) throws PRCServiceException {
		Map<String, String> resultMap = new HashMap<String, String>();
		String[] deviceDataArray = getDeviceDataArray(mduInfo);

		if (deviceDataArray.length > 0) {
			boolean isAndroid = PRCSharedUtils.isAndroid(); // OS구분 ex) Android

			if (isAndroid) {
				int osVersion = Integer.parseInt(PRCSharedUtils.getOsVersion().split("\\.")[0]); // OS 버전 앞자리만 가져옴 ex)
																									// "8".0.0
				boolean isAndroidIdAndWidevineId = ((osVersion > 9)
						&& (getSliceWith(deviceDataArray[1], ":").length() > 15)); // AndroidId+WidevineId 조합인지 여부

				if (isAndroidIdAndWidevineId) {
					resultMap.put(CommonBizConstants.IPINSIDE_DEVICE_DATA_TYPE,
							CommonBizConstants.IPINSIDE_ANDROID_ID_AND_WIDEVINE_ID);
					resultMap.put(CommonBizConstants.IPINSIDE_ANDROID_ID,
							isRawStr ? deviceDataArray[0] : getSliceWith(deviceDataArray[0], ":")); // Android ID
					resultMap.put(CommonBizConstants.IPINSIDE_WIDEVINE_ID,
							isRawStr ? deviceDataArray[1] : getSliceWith(deviceDataArray[1], ":")); // WIDVINE ID
				} else {
					resultMap.put(CommonBizConstants.IPINSIDE_DEVICE_DATA_TYPE,
							CommonBizConstants.IPINSIDE_MAC_AND_IMEI);
					resultMap.put(CommonBizConstants.IPINSIDE_MAC,
							isRawStr ? deviceDataArray[0] : getSliceWith(deviceDataArray[0], ":")); // MAC Address
					resultMap.put(CommonBizConstants.IPINSIDE_IMEI,
							isRawStr ? deviceDataArray[1] : getSliceWith(deviceDataArray[1], ":")); // IMEI
				}

			} else { // IOS
				resultMap.put(CommonBizConstants.IPINSIDE_DEVICE_DATA_TYPE,
						CommonBizConstants.IPINSIDE_VENDER_UUID_AND_UUID);
				resultMap.put(CommonBizConstants.IPINSIDE_VENDER_UUID,
						isRawStr ? deviceDataArray[0] : getSliceWith(deviceDataArray[0], ":")); // Vender UUID
				resultMap.put(CommonBizConstants.IPINSIDE_UUID,
						isRawStr ? deviceDataArray[1] : getSliceWith(deviceDataArray[1], ":")); // CF UUID
			}
		}

		return resultMap;
	}

	/**
	 * <p>
	 * Device 정보를 String Array 형태로 반환한다.
	 * </p>
	 *
	 * @param mduInfo Device정보 String
	 * @return Device 정보 String Array
	 */
	public static String[] getDeviceDataArray(String mduInfo) throws PRCServiceException {
		String decodedStr = StringUtils.defaultIfEmpty(getDecodeStr(mduInfo), "");
		return decodedStr.split(";");
	}

	/**
	 * <p>
	 * Device 정보를 Decoding해서 반환한다.
	 * </p>
	 *
	 * @param mduInfo Device정보 String
	 * @return Decoding된 Device 정보 String
	 */
	public static String getDecodeStr(String mduInfo) throws PRCServiceException {
		return new Coder().Decode(mduInfo);
	}

	private static String getSliceWith(String str, String target) {
		Pattern pattern = Pattern.compile("\\[(.+)\\]");
		Matcher matcher = pattern.matcher(str);
		String resultStr = "";

		if (matcher.find()) {
			String matchStr = matcher.group(1);
			int colonIdx = matchStr.indexOf(target);
			resultStr = matchStr.substring(0, colonIdx);
		}
		return resultStr;
	}

	/**
	 * 주민번호에서 나이 반환
	 * 
	 * @param ssn
	 * @return
	 */
	public static int getAge(String ssn) {

		String birthYear = ssn.substring(0, 2);
		String birthMonth = ssn.substring(2, 4);
		String birthDay = ssn.substring(4, 6);
		String sex = ssn.substring(6, 7);
		String fullYear = "";

		if ("1".equals(sex) || "2".equals(sex)) {
			fullYear = "19" + birthYear;
		} else if ("3".equals(sex) || "4".equals(sex)) {
			fullYear = "20" + birthYear;
		} else if ("5".equals(sex) || "6".equals(sex)) {
			fullYear = "19" + birthYear;
		} else if ("7".equals(sex) || "8".equals(sex)) {
			fullYear = "20" + birthYear;
		}

		String nowDate = DateUtils.getCurrentDate(DateUtils.YYYYMMDD);
		int nowYear = DateUtils.getYear(nowDate);
		int nowMonth = DateUtils.getMonth(nowDate);
		int nowDay = DateUtils.getDay(nowDate);

		int koAge = nowYear - Integer.parseInt(fullYear);
		int age = koAge;

		if (nowMonth < Integer.parseInt(birthMonth)) {
			age = koAge - 1;
		} else if (nowMonth == Integer.parseInt(birthMonth)) {
			if (nowDay < Integer.parseInt(birthDay)) {
				age = koAge - 1;
			}
		}

		return age;
	}

	/**
	 * 주민번호에서 생년월일 반환
	 * 
	 * @param ssn : 주민번호
	 * @return 생년월일(12345678)
	 */
	public static String getBirthday(String ssn) {

		if (StringUtils.isEmpty(ssn) || ssn.length() != 13) {
			return StringUtils.EMPTY;
		}

		ssn = ssn.substring(0, 7);

		int year = Integer.parseInt(ssn.substring(0, 2));
		String month = ssn.substring(2, 4);
		String day = ssn.substring(4, 6);
		char gender = ssn.charAt(6);

		ssn = ssn.length() == 13 ? ssn.substring(0, 7) : ssn;

		switch (gender) {
		case '0':
		case '9':
			year += 1800;
			break;
		case '1':
		case '2':
		case '5':
		case '6':
			year += 1900;
			break;
		case '3':
		case '4':
		case '7':
		case '8':
			year += 2000;
			break;
		}

		return String.valueOf(year) + month + day;
	}

	/**
	 * 주민번호에서 성별반환
	 * 
	 * @param ssn : 주민번호
	 * @return 성별 (1: 1900 남, 2: 1900 여, 3: 2000 남, 4: 2000 여, 5: 1900 외국인 남, 6:
	 *         1900 외국인 여, 7: 2000 외국인 남, 8: 2000 외국인 여)
	 */
	public static String getGender(String ssn) {

		if (StringUtils.isEmpty(ssn) || ssn.length() != 13) {
			return StringUtils.EMPTY;
		}

		char ssn7thNum = ssn.charAt(6);
		String gender = "";

		switch (ssn7thNum) {
		case '1':
		case '3':
			gender = "1";
			break;
		case '2':
		case '4':
			gender = "2";
			break;
		case '5':
			gender = "5";
			break;
		case '6':
			gender = "6";
			break;
		case '7':
			gender = "7";
			break;
		case '8':
			gender = "8";
			break;
		}

		return gender;

	}

	/**
	 * 외국인 여부
	 * 
	 * @param ssn
	 * @return true: 외국인, false: 내국인
	 */
	public static boolean isForeigner(String ssn) {
		return "5678".contains(CommonBizUtils.getGender(ssn));
	}

	/**
	 * 미성년자 여부
	 * 
	 * @param ssn
	 * @return
	 */
	public static boolean isMinor(String ssn) {
		return CommonBizUtils.getAge(ssn) < 19 ? true : false;
	}

	/**
	 * 핸드폰번호 split
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static String[] getPhoneNumberArr(String phoneNumber) {

		String[] phoneNumberArr = new String[3];
		phoneNumber = phoneNumber.replaceAll("\\D", "");

		switch (phoneNumber.length()) {
		case 10:
			phoneNumberArr[0] = phoneNumber.substring(0, 3);
			phoneNumberArr[1] = phoneNumber.substring(3, 6);
			phoneNumberArr[2] = phoneNumber.substring(6, 10);
			break;
		case 11:
			phoneNumberArr[0] = phoneNumber.substring(0, 3);
			phoneNumberArr[1] = phoneNumber.substring(3, 7);
			phoneNumberArr[2] = phoneNumber.substring(7, 11);
			break;
		}

		return phoneNumberArr;
	}

	public static String getBusinessDay(String strDate, int val) {

		IHolidayManager holidayManager = RuntimeContext.getBean(IHolidayManager.class);

		String calcDate = strDate.replaceAll("\\D", "");
		boolean isNegative = (val < 0);
		int dayCnt = (isNegative ? -1 : 1);

		while (true) {
			calcDate = DateUtils.getDate(calcDate, "yyyyMMdd", 'D', (isNegative ? -1 : 1));
			if (!holidayManager.isHoliday(calcDate)) {
				if (dayCnt == val) {
					break;
				}

				if (isNegative) {
					dayCnt--;
				} else {
					dayCnt++;
				}
			}
		}

		return calcDate;
	}

	// session value 획득
	public static String getSessionValue(String strKey) {
		String result = "";
		if (sessionManager.isLogin()) {
			// 로그인 한 경우 로그인세션 우선적으로 확인 후 없으면 글로벌세션값 리턴
			result = StringUtils.defaultIfEmpty(sessionManager.getLoginValue(strKey, String.class),
					StringUtils.nvl(sessionManager.getGlobalValue(strKey, String.class), ""));
		} else {
			// 비로그인일 경우 UserID, TSPassword는 Default값 전달
			switch (strKey) {
			case "UserID":
				result = CommonBizConstants.DEFAULT_USER_ID;
				break;
			case "TSPassword":
				result = CommonBizConstants.DEFAULT_TS_PASS_WORD;
				break;
			default:
				if (isAuthenticated()) {
					// 본인인증 한 경우 글로벌세션 우선적으로 확인 후 없으면 로그인세션값 리턴
					result = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue(strKey, String.class),
							StringUtils.nvl(sessionManager.getLoginValue(strKey, String.class), ""));
				} else {
					// 금융상품 업무공통 페이지 호출이 아닌 경우 BIZ_AUTH_FLAG 값이 세팅되지 않으므로, 사용중인 Global 세션값을 가져오지 못하는
					// 경우가 있어서 Default로 글로벌 세션 리턴 처리
					result = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue(strKey, String.class), "");
				}
				break;
			}
		}
		return result;
	}

	// 본인인증 여부 확인(session)
	public static boolean isAuthenticated() {
		return StringUtils.nvl(sessionManager.getGlobalValue("BIZ_AUTH_FLAG", String.class), "").equals("Y");
	}

}
