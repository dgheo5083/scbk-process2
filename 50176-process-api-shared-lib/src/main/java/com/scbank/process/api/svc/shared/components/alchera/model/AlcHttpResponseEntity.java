package com.scbank.process.api.svc.shared.components.alchera.model;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.alchera.AlcAES256;
import com.scbank.process.api.svc.shared.components.alchera.AlcClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 알체라서버 연계 응답 Entity 클래스
 */
@Slf4j
public class AlcHttpResponseEntity {

	public static final String ERROR_CODE_SUCCESS = "999";
	public static final String ERROR_CODE = "result_code";

	/**
	 * 응답 메시지 Wrapper Object
	 */
	private AlcHttpJsonObject jsonRoot;

	/**
	 * 에러코드
	 */
	private String errorCode = null;
	
	/**
	 * 에러메시지
	 */
	private String errorMessage = null;

	/**
	 * 알체라 서버 연동 응답 Entity 객체를 생성한다.
	 * @param response httpClient 응답 객체 {@link com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse}
	 */
	public AlcHttpResponseEntity(HttpResponse response) {
		try {

			log.debug("#### AlcHttpResponseEntity HttpResponse #### : {}", response);

			int statusCode = response.getCode();
			log.debug("#### AlcHttpResponseEntity HttpResponse > statusCode #### : {}", statusCode);
			if (statusCode != 200) {
				this.errorCode = AlcClient.ERR_MA004;
				// 통신 정보가 유효하지 않습니다.
				this.errorMessage = AlcClient.ERR_MA004_MSG;
				this.jsonRoot = AlcHttpJsonObject.fromObject(new JSONObject());
				return;
			}

			String body = response.getBody();
			log.debug("#### AlcHttpResponseEntity > HttpResponse > body #### : {}", body);
			if (body == null) {
				this.errorCode = AlcClient.ERR_MA001;
				// JSON DATA를 확인해 주세요.
				this.errorMessage = AlcClient.ERR_MA001_MSG;
				this.jsonRoot = AlcHttpJsonObject.fromObject(new JSONObject());
				return;
			}

			// 알체라 서버 응답 값 특정 항목 치환 처리
			this.jsonRoot = AlcHttpJsonObject.fromObject(convertField(body));

			// 응답 에러 여부 체크
			String errorCode = this.jsonRoot.getAsString(ERROR_CODE, ERROR_CODE_SUCCESS);
			if (errorCode == null) {
				this.errorCode = errorCode;
				// 알수 없는 에러. 알체라 에러코드를 확인하세요.
				this.errorMessage = AlcClient.ERR_MA999_MSG;
			} else {
				this.errorCode = errorCode;
				this.errorMessage = errorCode;
			}

		} catch (Exception ex) {
			this.errorCode = AlcClient.ERR_MA001;
			this.errorMessage = ex.getMessage();
		}

		if (this.jsonRoot == null) {
			this.errorCode = AlcClient.ERR_MA002;
			// 응답 데이터가 존재 하지 않습니다.
			this.errorMessage = AlcClient.ERR_MA002_MSG;
		}
	}

	/**
	 * 에러코드를 반환한다.
	 * @return
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * 에러메시지를 반환한다.
	 * @return 에러메시지
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * AlcHttpJsonObject 응답 객체를 반환한다.
	 * @return AlcHttpJsonObject
	 */
	public AlcHttpJsonObject getBody() {
		return this.jsonRoot;
	}

	/**
	 * 응답 데이터를 JSONObject 타입으로 가져온다.
	 * @return JSONObject 타입의 응답 데이터
	 */
	public JSONObject getAsJsonObject() {
		if (this.jsonRoot == null) {
			return new JSONObject();
		}
		return this.jsonRoot.getAsJsonObject();
	}

	/**
	 * 에러코드를 재생성한다.
	 * @param code 원 에러코드
	 * @return 재성성된 에러코드 MAFU-{code}
	 */
	public String getAsFwErrorCode(String code) {
		return String.format("MAFU-%s", code);
	}

	/**
	 * ERROR코드가 SUCCESS 아닌 경우 Exception를 발생시킨다.
	 * @return {@link AlcHttpResponseEntity}
	 * @throws PRCServiceException
	 */
	public AlcHttpResponseEntity throwException() {
		if (getErrorCode().indexOf("SUCCESS") != 0) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage);
		}

		return this;
	}

	/**
	 * 민감정보 AES256 암호화 처리
	 * 
	 * @param jsonString
	 * @return
	 * @throws PRCServiceException
	 */
	protected JSONObject convertField(String jsonString) {
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(jsonString);
			String resultScanType = (String) jsonObject.get("result_scan_type"); // 스캔 타입
			String org_image = (String) jsonObject.get("org_image"); // 신분증 전체 이미지
			String markedObj = (String) jsonObject.get("marked"); // 신분증 크롭 이미지
			String portraitObj = (String) jsonObject.get("portrait"); // 신분증 사진
			String unmarked_image = (String) jsonObject.get("unmarked"); // 크롭된 마스킹 안된 이미지

			// 정상케이스
			if (!resultScanType.equals("UNKNOWN")) {
				// 신분증 이미지 AES256 암호화 처리
				// 원본이미지
				jsonObject.put("org_image", AlcAES256.encryptBytesToString(org_image));
				// 마스킹이미지
				jsonObject.put("marked", AlcAES256.encryptBytesToString(markedObj));
				// 얼굴이미지
				jsonObject.put("portrait", AlcAES256.encryptBytesToString(portraitObj));
				// 원본이미지(크롭)
				jsonObject.put("unmarked", AlcAES256.encryptBytesToString(unmarked_image));

				JSONObject idObject = (JSONObject) jsonObject.get("id");

				// 주민번호 평문 암호화 처리
				String jumin = (String) idObject.get("jumin");
				idObject.put("jumin", AlcAES256.encryptBytesToString(jumin));

				// 운전면허번호 평문 암호화 처리
				if (resultScanType.equals("DRIVER_LICENSE")) {
					JSONObject derverLicenseObject = (JSONObject) idObject.get("driver_license");
					String driverLicense = (String) derverLicenseObject.get("driver_number");
					derverLicenseObject.put("driver_number", AlcAES256.encryptBytesToString(driverLicense));
				}

				// 여권 평문 암호화 처리
				if (resultScanType.equals("PASSPORT")) {
					JSONObject passportObject = (JSONObject) idObject.get("passport");
					String passportNumber = (String) passportObject.get("passport_number");
					passportObject.put("passport_number", AlcAES256.encryptBytesToString(passportNumber));
				}
			}

		} catch (PRCServiceException e) {
			throw e;
		} catch (Exception e) {
			// 알수 없는 에러. 알체라 에러코드를 확인하세요.
			throw new PRCServiceException(AlcClient.ERR_MA999, e.getMessage(), e);
		}

		return jsonObject;
	}

	/**
	 * 텍스트 구간 마스킹 처리
	 * @param value 마스킹대상 문자열
	 * @param start 마스킹 시작 point
	 * @param end 마스킹 끝 point
	 * @return
	 */
	protected String maskValue(String value, int start, int end) {
		if (value == null || value.length() <= start || start >= end) {
			return value;
		}

		StringBuilder masked = new StringBuilder(value);
		for (int i = start; i < end && i < value.length(); i++) {
			masked.setCharAt(i, '*');
		}
		return masked.toString();
	}

}
