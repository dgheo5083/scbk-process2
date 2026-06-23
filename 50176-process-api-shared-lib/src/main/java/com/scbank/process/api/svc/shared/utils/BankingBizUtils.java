package com.scbank.process.api.svc.shared.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.session.NewAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.helper.AccountHelper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class BankingBizUtils {
	/**
	 * 세션 컨텍스트 매니저
	 */
	private ISessionContextManager sessionManager;

	private AccountListComponent accountList;

	private AccountHelper helper;

	/*
	 * 예금자보호법 : 예금상품 비보호안내 표시여부
	 * 
	 * BizcommonUtils.isUnprotectTargetAct(과목코드, 종별코드)
	 * 
	 * 1. 원화저축성
	 * - 과목코드 : 94
	 * - 과목코드 : 80
	 * - 계좌종별 : 91
	 * - 과목코드 : 80, 90, 60, 70, 46, 47, 48, 49
	 * - 과세구분 : 8 && 거래자구분 : 20, 21, 24, 63
	 * - 과세구분 : 9 && 거래자구분 : 10, 11, 14, 32, 33, 35, 36, 37, 38, 41, 42, 43, 45, 46,
	 * 47, 51, 52
	 * 
	 * 2. 원화요구불 || 외화예금
	 * - 과목코드 : 10, 20, 30, 85, 86, 87, 88, 89
	 * - 과세구분 : 8 && 거래자구분 : 20, 21, 24, 63
	 * - 과세구분 : 9 && 거래자구분 : 10, 11, 14, 32, 33, 35, 36, 37, 38, 41, 42, 43, 45, 46,
	 * 47, 51, 52
	 */
	public static boolean isUnprotectTargetAct(String acctType, String actAssort) {
		// default 예금자보호 대상으로 노출
		if (acctType == null) {
			return false;
		}

		// 계좌번호 full 로 들어오는 경우 과목코드를 재정의한다.
		if (acctType != null && acctType.length() == 11) {
			acctType = acctType.substring(3, 5);
		}

		String YOKWASE = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("YOKWASE", String.class), ""); // 과세구분
		String YOGEORE = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("YOGEORE", String.class), ""); // 거래자구분

		// 비보호대상 구분
		boolean isUnprotectTarget = false;

		// 계좌 과목코드별로 동일한 로직을 수행하지만, 추후 과목코드별로 달라질 수 있어 별도로 case를 선언
		switch (acctType) {
			case "94":
				isUnprotectTarget = true;
				break;
			case "80":
			case "90":
			case "60":
			case "70":
			case "46":
			case "47":
			case "48":
			case "49":
				if ("80".equals(acctType) && "91".equals(actAssort)) {
					isUnprotectTarget = true;
					break;
				}

				if ("8".equals(YOKWASE) && YOGEORE.matches("20|21|24|63")) {
					isUnprotectTarget = true;
				} else if ("9".equals(YOKWASE)
						&& YOGEORE.matches("10|11|14|32|33|35|36|37|38|41|42|43|45|46|47|51|52")) {
					isUnprotectTarget = true;
				}
				break;

			case "10":
			case "20":
			case "30":
			case "85":
			case "86":
			case "87":
			case "88":
			case "89":
				if ("8".equals(YOKWASE) && YOGEORE.matches("20|21|24|63")) {
					isUnprotectTarget = true;
				} else if ("9".equals(YOKWASE)
						&& YOGEORE.matches("10|11|14|32|33|35|36|37|38|41|42|43|45|46|47|51|52")) {
					isUnprotectTarget = true;
				}
				break;
			case "59": // 신탁은 모두 비보호
				isUnprotectTarget = true;
				break;
		}

		return isUnprotectTarget;
	}

	public static void accountSessionClear() {
		sessionManager = RuntimeContext.getBean(ISessionContextManager.class);

		if (sessionManager.isLogin()) {
			sessionManager.removeLoginValue("ALL_ACCOUNTLIST");
			sessionManager.removeLoginValue("FUND_REACHGOAL_ACCOUNTLIST");
			sessionManager.removeLoginValue("FUND_CHGREACHGOAL_ACCOUNTLIST");
			sessionManager.removeLoginValue("LOAN_ACCOUNTLIST");
			sessionManager.removeLoginValue("FUND_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEPOSIT_TRUST_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEPOSIT_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEPOSIT_SAVING_ACCOUNTLIST");
			// sessionManager.removeLoginValue("PAYMENT_ACCOUNTLIST");
			// sessionManager.removeLoginValue("PAYMENT_NORMAL_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEMAND_DEPOSIT_ACCOUNTLIST");
			sessionManager.removeLoginValue("FX_ACCOUNTLIST");
			sessionManager.removeLoginValue("MTS_ACCOUNTLIST");
			sessionManager.removeLoginValue("ISA_ACCOUNTLIST");
			sessionManager.removeLoginValue("MORTAGE_LOAN_ACCOUNTLIST");
			sessionManager.removeLoginValue("CARD_ACCOUNTLIST");

			// 이 케이스는 모든계좌세션삭제하여 ALL_ACCOUNTLIST 조회 시 H035 전문호출하려 조립함
			// > H039 세션은 이시점에 삭제가 안되어 홈화면/전계좌조회같이 ALL_ACCOUNTLIST 호출하면 1개 껍데기 계좌가 신규되어 삭제조치
			sessionManager.removeLoginValue("H039_NEW_ACCOUNTLIST");
		}
	}

	/**
	 * 계좌신규 , 계좌해지시 전계좌세션에 계좌 정보 업데이트 (H039조회시 금액 업데이트하기위함)
	 * 
	 * @param clearMap
	 */
	public static void accountSessionClear(String newAccount) {
		sessionManager = RuntimeContext.getBean(ISessionContextManager.class);

		if (sessionManager.isLogin()) {
			// sessionManager.removeLoginValue("ALL_ACCOUNTLIST");

			if (!StringUtils.defaultIfEmpty(newAccount, "").equals("")) {
				List<NewAccountInfoSession> sessionNewAccountList = helper.getNewAccountSessionList();

				NewAccountInfoSession r = new NewAccountInfoSession();

				r.setNEW_ACCOUNT_NUM(newAccount);

				if (sessionNewAccountList == null) {
					List<NewAccountInfoSession> newAccountList = new ArrayList<>();

					newAccountList.add(r);

					sessionManager.setLoginValue("H039_NEW_ACCOUNTLIST", newAccountList);
				} else {
					sessionNewAccountList.add(r);

					sessionManager.setLoginValue("H039_NEW_ACCOUNTLIST", sessionNewAccountList);
				}
			} else {
				sessionManager.removeLoginValue("ALL_ACCOUNTLIST");
			}

			sessionManager.removeLoginValue("FUND_REACHGOAL_ACCOUNTLIST");
			sessionManager.removeLoginValue("FUND_CHGREACHGOAL_ACCOUNTLIST");
			sessionManager.removeLoginValue("LOAN_ACCOUNTLIST");
			sessionManager.removeLoginValue("FUND_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEPOSIT_TRUST_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEPOSIT_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEPOSIT_SAVING_ACCOUNTLIST");
			// sessionManager.removeLoginValue("PAYMENT_ACCOUNTLIST");
			// sessionManager.removeLoginValue("PAYMENT_NORMAL_ACCOUNTLIST");
			sessionManager.removeLoginValue("DEMAND_DEPOSIT_ACCOUNTLIST");
			sessionManager.removeLoginValue("FX_ACCOUNTLIST");
			sessionManager.removeLoginValue("MTS_ACCOUNTLIST");
			sessionManager.removeLoginValue("ISA_ACCOUNTLIST");
			sessionManager.removeLoginValue("MORTAGE_LOAN_ACCOUNTLIST");
			sessionManager.removeLoginValue("CARD_ACCOUNTLIST");
		}
	}

	/**
	 * 계좌신규 , 계좌해지시 전계좌세션에 계좌 정보 업데이트 (H039조회시 금액 업데이트하기위함)
	 * 
	 * @param clearMap
	 */
	public static void accountSessionClear(List<NewAccountInfoSession> newAccountList) {
		sessionManager = RuntimeContext.getBean(ISessionContextManager.class);

		if (sessionManager.isLogin()) {
			// sessionManager.removeLoginValue("ALL_ACCOUNTLIST");

			if (newAccountList != null) {
				List<NewAccountInfoSession> sessionNewAccountList = helper.getNewAccountSessionList();

				if (sessionNewAccountList != null) {
					newAccountList.addAll(sessionNewAccountList);
				}

				sessionManager.setLoginValue("H039_NEW_ACCOUNTLIST", newAccountList);
				sessionManager.removeLoginValue("FUND_REACHGOAL_ACCOUNTLIST");
				sessionManager.removeLoginValue("FUND_CHGREACHGOAL_ACCOUNTLIST");
				sessionManager.removeLoginValue("LOAN_ACCOUNTLIST");
				sessionManager.removeLoginValue("FUND_ACCOUNTLIST");
				sessionManager.removeLoginValue("DEPOSIT_TRUST_ACCOUNTLIST");
				sessionManager.removeLoginValue("DEPOSIT_ACCOUNTLIST");
				sessionManager.removeLoginValue("DEPOSIT_SAVING_ACCOUNTLIST");
				// sessionManager.removeLoginValue("PAYMENT_ACCOUNTLIST");
				// sessionManager.removeLoginValue("PAYMENT_NORMAL_ACCOUNTLIST");
				sessionManager.removeLoginValue("DEMAND_DEPOSIT_ACCOUNTLIST");
				sessionManager.removeLoginValue("FX_ACCOUNTLIST");
				sessionManager.removeLoginValue("MTS_ACCOUNTLIST");
				sessionManager.removeLoginValue("ISA_ACCOUNTLIST");
				sessionManager.removeLoginValue("MORTAGE_LOAN_ACCOUNTLIST");
				sessionManager.removeLoginValue("CARD_ACCOUNTLIST");
			} else {
				sessionManager.removeLoginValue("ALL_ACCOUNTLIST");
			}

		}
	}

	public static <T> T toJSONFromStr(String jsonStr, Class<T> clsName) {
		try {
			return new ObjectMapper().readValue(jsonStr, clsName);
		} catch (IOException e) {
			log.debug("################# BizCommonUtils toJSONFromStr IOException Occurred!!! ##################");
			log.debug("########################################################################################");
			log.debug(String.valueOf(clsName));
			log.debug(jsonStr);
			log.debug("########################################################################################");
			log.error("@@@ LGS BizCommonUtils toJSONFromStr IOException [" + e.getMessage() + "]", e);
			return null;
		}
	}

	public static String toJSONStringFromObject(Object json) {
		String jsonStr = "";
		try {
			jsonStr = new ObjectMapper().writeValueAsString(json);
		} catch (JsonProcessingException e) {
			log.debug(
					"################# BizCommonUtils toJSONStringFromObject IOException Occurred!!! ##################");
			log.debug("########################################################################################");
			log.debug(String.valueOf(json));
			log.debug("########################################################################################");
			log.error("@@@ LGS BizCommonUtils toJSONStringFromObject JsonProcessingException [" + e.getMessage() + "]",
					e);
		}

		return jsonStr;
	}

	public static boolean isAuthenticated() {
		return StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("BIZ_AUTH_FLAG", String.class), "").equals("Y");
	}

	public static String[] retSplitPhoneNumber(String strPhoneNum) {
		String[] phoneNumArr = new String[3];
		strPhoneNum = strPhoneNum.replaceAll("\\D", "");

		switch (strPhoneNum.length()) {
			case 10:
				phoneNumArr[0] = strPhoneNum.substring(0, 3);
				phoneNumArr[1] = strPhoneNum.substring(3, 6);
				phoneNumArr[2] = strPhoneNum.substring(6, 10);
				break;
			case 11:
				phoneNumArr[0] = strPhoneNum.substring(0, 3);
				phoneNumArr[1] = strPhoneNum.substring(3, 7);
				phoneNumArr[2] = strPhoneNum.substring(7, 11);
				break;
		}

		return phoneNumArr;
	}

	public static Map<String, String> getForeignerAndUnderAge(String perBusNo) {
		// 나이 기준이 전달되지 않으면 기본적으로 미성년자(19세) 체크로 진행
		return getForeignerAndUnderAge(perBusNo, "19");
	}

	// 미성년자, 외국인 체크
	// PMS 상품정보 연동 이용가능 나이 체크로 인해 기준이 되는 나이 값 전달받도록 추가 - 2023.07.03 이규선
	public static Map<String, String> getForeignerAndUnderAge(String perBusNo, String targetAge) {
		HashMap<String, String> result = new HashMap<String, String>();
		boolean isLowAge = false;
		boolean isForeigner = false;
		String userAge = ""; // 사용자 나이

		if (StringUtils.isNotEmpty(perBusNo)) {
			String birthYear = perBusNo.substring(0, 2);
			String birthMonth = perBusNo.substring(2, 4);
			String birthDay = perBusNo.substring(4, 6);
			String sex = perBusNo.substring(6, 7);
			String fullYear = "";

			switch (sex) {
				case "1":
				case "2":
					fullYear = "19" + birthYear;
					break;
				case "3":
				case "4":
					fullYear = "20" + birthYear;
					break;
				case "5":
				case "6":
					fullYear = "19" + birthYear;
					isForeigner = true;
					break;
				case "7":
				case "8":
					fullYear = "20" + birthYear;
					isForeigner = true;
					break;
			}

			String nowDate = DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS);
			String nowYear = nowDate.substring(0, 4);
			String nowMonth = nowDate.substring(4, 6);
			String nowDay = nowDate.substring(6, 8);

			int koAge = Integer.parseInt(nowYear) - Integer.parseInt(fullYear);
			int age = koAge;

			if (Integer.parseInt(nowMonth) < Integer.parseInt(birthMonth)) {
				age = koAge - 1;
			} else if (Integer.parseInt(nowMonth) == Integer.parseInt(birthMonth)) {
				if (Integer.parseInt(nowDay) < Integer.parseInt(birthDay)) {
					age = koAge - 1;
				}
			}

			if (age < Integer.parseInt(StringUtils.defaultIfEmpty(targetAge, "19"))) {
				isLowAge = true;
			}

			userAge = String.valueOf(age);
		}

		result.put("isLowAge", isLowAge ? "Y" : "N");
		result.put("isForeigner", isForeigner ? "Y" : "N");
		result.put("userAge", userAge);

		return result;
	}

	/**
	 * 외국인 허용 가능업무 체크
	 * ----------------------------------------------------------------------------
	 * BizcommonUtils.getCheckForeignerAllowed(업무구분, 외국인여부)
	 * ----------------------------------------------------------------------------
	 * ----------------------------------------------------------------------------
	 * 주민번호 체계 (뒷 번호 첫 자리)
	 * ----------------------------------------------------------------------------
	 * 내국인 (한국국적+국내거주)
	 * 재외국민 (한국국적+해외거주)
	 * → 주민번호 (1/2/3/4)
	 * 
	 * 외국인 (외국국적)
	 * → 주민번호 (5/6/7/8)
	 * 
	 * @param bizType     업무구분
	 * @param isForeigner 국적구분 (Y:외국인, N:내국인)
	 * @return
	 */
	public static Map<String, String> getCheckForeignerAllowed(String bizType, String isForeigner) {
		sessionManager = RuntimeContext.getBean(ISessionContextManager.class);

		log.debug("getCheckForeignerAllowed :::::::::::::::::::::::::::::::::::::");
		boolean isAllowed = false; // 외국인 업무 진행가능 여부 (true: 진행가능, false: 진행불가)
		String errorCode = "PRCAAT001074"; // 외국인 및 재외국민은 서비스를 이용하실 수 없어요.

		// TODO 업무구분이 없다면..
		if (StringUtils.isEmpty(bizType)) {
			bizType = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("BIZ_TYPE"), "");
		}

		// TODO 외국인여부가 없다면..
		if (StringUtils.isEmpty(isForeigner)) {
			String perBusNo = SessionUtils.getSessionValue("PerBusNo");
			isForeigner = getForeignerAndUnderAge(perBusNo).get("isForeigner");
		}

		// TODO 조건0) 외국인일 경우
		if ("Y".equalsIgnoreCase(isForeigner)) {

			// TODO 조건1) APP + 로그인
			if (PRCSharedUtils.isSB() && sessionManager.isLogin()) {

				String YOKWASE = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("YOKWASE"), ""); // 과세구분 (1:개인,
																											// 5:외국인)
				String YOPASSYN = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("YOPASSYN"), ""); // 실명확인증표
																											// (Y:여권,
																											// 4:외국인등록증,
																											// 7:거소중)
				String YOGEOJU = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("YOGEOJU"), ""); // 거주구분
																											// (0:거주자,
																											// 1:외국인비거주자,
																											// 2:재외국민거주자,
																											// 3:재외국민비거주,
																											// 4:내국인비거주자)

				// TODO 조건2) 외국인 허용업무 체크
				String allowedType = "FOREIGNER_ALLOWED_TARGET_" + bizType;
				String foreignerTargetYN = StringUtils.defaultIfEmpty(PropertiesUtils.getString(allowedType), "N");

				log.debug("getCheckForeignerAllowed :: YOKWASE [" + YOKWASE + "] ::");
				log.debug("getCheckForeignerAllowed :: YOPASSYN [" + YOPASSYN + "] ::");
				log.debug("getCheckForeignerAllowed :: YOGEOJU [" + YOGEOJU + "] ::");
				log.debug("getCheckForeignerAllowed :: foreignerTargetYN [" + foreignerTargetYN + "] ::");

				if ("Y".equalsIgnoreCase(foreignerTargetYN)) {

					// TODO 조건3) 과세구분(YOKWASE)
					if ("5".equals(YOKWASE)) {

						// TODO 조건4) 실명확인증표여권(YOPASSYN)
						// TODO 조건5) 거주구분(YOGEOJU)
						if (YOPASSYN.matches("Y")) { // 과세구분(5) + 실명확인증표여권(Y)
							isAllowed = false; // 진행불가
							errorCode = "PRCAAT001072"; // 등록되어 있는 고객정보와 일치하지 않아<br>서비스를 이용하실 수 없어요.

						} else if (YOPASSYN.matches("4|7") && YOGEOJU.matches("0")) { // 과세구분(5) + 실명확인증표여권(4|7) +
																						// 거주구분(0)
							isAllowed = true; // 진행가능
							errorCode = "";

						} else if (YOPASSYN.matches("4|7") && YOGEOJU.matches("1|3|4")) { // 과세구분(5) + 실명확인증표여권(4|7) +
																							// 거주구분(1|3|4)
							isAllowed = false; // 진행불가
							errorCode = "PRCAAT001073"; // 한국에 거주하고 계셔야 <br>서비스를 이용하실 수 있어요.

						} else { // 그 외. 기존과 동일한 문구
							isAllowed = false; // 진행불가
							errorCode = "PRCAAT001074"; // 외국인 및 재외국민은 서비스를 이용하실 수 없어요.
						}

					} else {
						isAllowed = false; // 진행불가
						errorCode = "PRCAAT001074"; // 외국인 및 재외국민은 서비스를 이용하실 수 없어요.
					}

				} else {
					isAllowed = false; // 진행불가
					errorCode = "PRCAAT001074"; // 외국인 및 재외국민은 서비스를 이용하실 수 없어요.
				}

			} else {
				isAllowed = false; // 진행불가
				errorCode = "PRCAAT001074"; // 외국인 및 재외국민은 서비스를 이용하실 수 없어요.
			}

		} else {
			isAllowed = false; // 진행불가
			errorCode = "PRCAAT001074"; // 외국인 및 재외국민은 서비스를 이용하실 수 없어요.
		}

		Map<String, String> result = new HashMap<String, String>();
		result.put("isAllowed", isAllowed ? "Y" : "N"); // 외국인 업무 진행가능여부 (Y:진행가능, N:진행불가)
		result.put("errorCode", errorCode); // 외국인 진행불가일때 오류코드
		result.put("bizType", bizType); // 업무구분
		result.put("isForeigner", isForeigner); // 외국인 여부

		log.debug("getCheckForeignerAllowed :::::::::::::::::::::::::::::::::::::");
		return result;
	}

	// 영문+숫자+특수문자 입력여부 체크
	public static boolean isAlphaNumSpeCheck(String arg) {
		boolean hasSpe = arg.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?┼※'].*");
		boolean hasEng = arg.matches(".*[a-zA-Z].*");
		boolean hasNum = arg.matches(".*[0-9].*");

		return hasEng && hasNum && hasSpe;
	}
}
