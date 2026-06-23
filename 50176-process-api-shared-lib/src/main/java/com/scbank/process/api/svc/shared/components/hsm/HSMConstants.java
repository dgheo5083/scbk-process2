package com.scbank.process.api.svc.shared.components.hsm;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class HSMConstants {

	public static final String HSM_TRANSFORMATION = "AES/CBC/PKCS5Padding"; // 암호알고리즘이름
	public static final String HSM_AES = "AES"; // 알고리즘명
	public static final String HSM_KEYSTORE_TYPE = "nCipher.sworld"; // KeyStore 타입
	public static final String HSM_KCV_TRANSFORMATION = "AES/ECB/NoPadding"; // KCV_암호알고리즘이름
	public static final String HSM_KCV_PROVIDER = "nCipherKM"; // KCV_PROVIDER

	public static final String HSM_PROPERTY_USE_FLAG = "HSM_USE_FLAG"; // HSM 사용여부 관리
	public static final String HSM_PROPERTY_MASTER_KEY_ALIAS = "HSM_MASTER_KEY_ALIAS"; // HSM MASTER KEY 접근별명
	public static final String HSM_PROPERTY_KEY_STORE_FULL_PATH = "HSM_KEY_STORE_FULL_PATH"; // HSM KeyStore 파일 위치
	public static final String HSM_PROPERTY_RANDOM_AES_IV_VALUE = "HSM_RANDOM_AES_IV_VALUE"; // HSM_AES256_IV값
	public static final String HSM_PROPERTY_MASTER_KEY_PASS_PHASE = "HSM_MASTER_KEY_PASS_PHASE"; // HSM MASTER KEY에
																									// 접근하기위한
																									// PASS_PHASE값

	public static final String HSM_PROPERTY_PREFIX = "HSM"; // 프로퍼티 Key PreFix
	public static final String HSM_PROPERTY_ENCRYPT_TYPE = "EncryptType"; // 프로퍼티 Key 암호화타입 1:전체암호화(Body부)
																			// 2:부분암호화(지정필드암호화)
	public static final String HSM_PROPERTY_ENCRYPT_WORKINGKEY = "EncryptWorkingKey"; // 프로퍼티 Key 암호화된 WorkingKey
	public static final String HSM_PROPERTY_TEMP_DEC_WORKINGKEY = "TempDecWorkingKey"; // 프로퍼티 Key 복호화된
																						// WorkingKey(장애상황일때만사용)
	public static final String HSM_PROPERTY_PARTNERSHIP_CODE = "PartnershipCode"; // 업체 제휴코드

	public static final String HSM_PROPERTY_WORKINGKEY = "workingKey"; // 복호화된 WorkingKey명

	public static final String HSM_PROPERTY_COMMON_PATH = "HSM_WORKING_KEY_FILE"; // HSM WorkingKey가 정의되어있는
																					// Key명(cmm.prd.properties)

	public static final String CSL_PROPERTY_PREFIX = "CSL"; // CSL PREFIX

	public static final String HSM_NOT_USE_PROPERTY_PATH = "HSM_NOT_USE_PROPERTY_PATH"; // HSM 장비가 오류일경우 복호화된WorkingKey를
																						// 가져오는 프로퍼티위치

	public static final String HSM_RANDOM_IV_VALUE = "HSM_RANDOM_IV_VALUE"; // 암복호화 랜덤IV값(HSM WorkingKey 암복호화 , AES256
																			// 암복호화에서 사용함)

	/**
	 * API IN전문 암호화 필드Key명
	 */
	public static final String CSL_ENCRYPT_FIELD_IN = "EncryptField_IN";
	/**
	 * API OUT전문 암호화 필드Key명
	 */
	public static final String CSL_ENCRYPT_FIELD_OUT = "EncryptField_OUT";

	/**
	 * CSL API 전문명 정의 상수 클래스
	 * 
	 * @author 950535
	 *
	 */
	@AllArgsConstructor
	@Getter
	public enum CSLApiNameConstants {

		/**
		 * 신용대출한도조회 전문명 CSL -> EDMI -> 여신 인터페이스 전문명 : EDMI_CB_CLCL_A002
		 */
		CREDITLOAN("CreditLoanLimitInquiry", "신용대출한도조회"),
		/**
		 * 대환대출한도조회 전문명 CSL -> EDMI -> 여신 인터페이스 전문명 : EDMI_CB_CLCL_A002
		 */
		REFINANCINGLOAN("ReFinancingLoanLimitInquiry", "대환대출한도조회"),
		/**
		 * 담보대출한도조회 전문명 CSL -> EDMI -> 여신 인터페이스 전문명 : EDMI_LMLC_A001
		 */
		MORTGAGELOAN("MortgageLoanLimitInquiry", "담보대출한도조회");

		private final String name;
		/**
		 * Key명 상세설명(업무에서는 로그용으로 사용할것)
		 */
		private final String description;
	}

	public static final String STR_Y = "Y";
	public static final String STR_N = "N";
	public static final String STR_1 = "1";
	public static final String STR_2 = "2";

	/**
	 * HSM 오류코드 정의 상수 클래스
	 * 
	 * @author 950535
	 *
	 */
	@AllArgsConstructor
	@Getter
	public enum HSMErrorConstants {

		H0001("HSM_0001", "HSM 초기화를 진행해주세요"), H0002("HSM_0002", "지원하지않는 Vender사 입니다.");

		private final String errorCode;
		private final String errorMsg;

	}

}
