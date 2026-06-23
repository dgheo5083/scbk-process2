package com.scbank.process.api.svc.shared.components.toss.model;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.toss.ITossClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
public class TossHttpResponseEntity {
	private TossHttpJsonObject jsonRoot;

	private String errorCode = null;
	private String errorMessage = null;
	private Throwable cause = null;

	public TossHttpResponseEntity(HttpResponse response) {
		log.debug(" - response >> {}", response.toString());
		// set json object data
		try {
			int statusCode = response.getCode();
			log.debug(" - statusCode >> {}", statusCode);
			if (statusCode != 200 && statusCode != 400 && statusCode != 401) {
				this.errorCode = ITossClient.ERR_MA003;
				this.errorMessage = ITossClient.ERR_MA003_MSG;
				this.jsonRoot = TossHttpJsonObject.fromObject(new JSONObject());
				return;
			}

			String jsonString = response.getBody();

			if (jsonString == null) {
				this.errorCode = ITossClient.ERR_MA001;
				this.errorMessage = ITossClient.ERR_MA001_MSG;
				this.jsonRoot = TossHttpJsonObject.fromObject(new JSONObject());
				return;
			}

			log.debug(" - RESPONSE >> {}", jsonString);

			// 가져온 데이타를 검증한다.
			this.jsonRoot = TossHttpJsonObject.fromObject((JSONObject) new JSONObject(jsonString));

			// error code 추출
			String errorCode = null; // null은 기타에러로 빠짐.
			String resultType = this.jsonRoot.getAsString(TossHelper.RESULT_TYPE, null); // 에러코드가 없으면 에러 처리 한다.
			log.debug(" - resultType >> {}", resultType);

			if (resultType != null && TossHelper.SUCCESS_RESULT_TYPE.equals(resultType)) {
				errorCode = TossHelper.SUCCESS_RESULT_TYPE;
			}

			if (resultType != null && TossHelper.ERROE_RESULT_TYPE.equals(resultType)) {
				TossHttpJsonObject jsonObj = this.jsonRoot.getAsJsonObject(TossHelper.ERROE_RESULT_DATA_OBJ_NAME);
				errorCode = jsonObj.getAsString(TossHelper.ERROR_CODE, "").trim();
				this.errorMessage = jsonObj.getAsString(TossHelper.ERROR_MSG, "").trim();

				/* 20210315 KJS 토스통장에서 FAIL error 처리를 하기 위해서 추가함. start */
				/* 20210315 KJS 토스지정대리인에 영향을 주지 않기 위해서 errorCode null or "" 일 때만 처리함 추가함 */
				if (errorCode == null || "".equals(errorCode)) {

					TossHttpJsonObject jsonObjCASA = this.jsonRoot.getAsJsonObject(TossHelper.RESULT_DATA);

					String tempErrorCode = jsonObjCASA.getAsString(TossHelper.RESULT_DATA_CODE, "").trim();

					if (tempErrorCode.indexOf("TAA") > -1) {

						errorCode = jsonObjCASA.getAsString(TossHelper.RESULT_DATA_CODE, "").trim();
						this.errorMessage = jsonObjCASA.getAsString(TossHelper.RESULT_DATA_MESSAGE, "").trim();

						log.debug(" - errorCode0 >> {}", errorCode);
						log.debug(" - errorMessage0 >> {}", this.errorMessage);
					}
				}
				/* 20210315 KJS 토스통장에서 FAIL error 처리를 하기 위해서 추가함. end */
			}

			log.debug(" - errorCode1 >> " + errorCode);
			log.debug(" - errorMessage1 >> " + this.errorMessage);

			if (resultType == null || StringUtils.isEmpty(errorCode)) {
				this.errorCode = TossHelper.ERROE_RESULT_TYPE;
				if ("".equals(this.errorMessage)) {
					this.errorMessage = "알수 없는 에러. TOSS 에러코드를 확인하세요.";
				}
			} else if (!TossHelper.SUCCESS_RESULT_TYPE.equals(errorCode)) {
				this.errorCode = errorCode;
			}

			log.debug(" - errorCode2 >> {}", errorCode);
			log.debug(" - errorMessage2 >> {}", this.errorMessage);

		} catch (Exception ex) {
			this.errorCode = ITossClient.ERR_MA001;
			this.errorMessage = ex.getMessage();
		}

		if (this.jsonRoot == null) {
			this.errorCode = ITossClient.ERR_MA002;
			this.errorMessage = ITossClient.ERR_MA002_MSG;
		}

		log.debug(" - resultType >> this.errorCode : [{}] this.errorMessage : [{}]", this.errorCode, this.errorMessage);
	}

	public boolean isError() {
		log.debug(" - isError >> errorCode : [{}]", errorCode);
		if (errorCode == null || "".equals(errorCode.trim()) == true)
			return false;

		return true;
	}

	public String getErrorCode() {
		log.debug(" - getErrorCode >> this.errorCode : [{}]", this.errorCode);
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public TossHttpJsonObject getBody() {
		return this.jsonRoot;
	}

	public JSONObject getAsJsonObject() {
		if (this.jsonRoot == null)
			return new JSONObject();
		return this.jsonRoot.getAsJsonObject();
	}

	public String getAsFwErrorCode(String code) {
		return String.format("MAFU-%s", code);
	}

	/**
	 * ERROR코드가 (O)uth관련 에러일 경우 Exception를 발생시킨다.
	 * 
	 * @return
	 * @throws Ma30FrameworkException
	 */
	public TossHttpResponseEntity throwException() {
		if (isError() && getErrorCode().equals(TossHelper.ERROE_RESULT_TYPE)) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage, this.cause);
		} else if (isError()) {
			throw new PRCServiceException(this.errorCode, this.errorMessage, this.cause);
		}

		return this;
	}

}
