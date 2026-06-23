package com.scbank.process.api.svc.shared.components.alchera.model;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.alchera.AlcClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 알체라 안면인식 서버 응답 Entity 클래스
 */
@Slf4j
public class AlcFaceHttpResponseEntity {

	public static final String ERROR_CODE_SUCCESS = "999";
	public static final String ERROR_CODE = "return_code";

	private AlcHttpJsonObject jsonRoot;

	private String errorCode = null;
	private String errorMessage = null;

	public AlcFaceHttpResponseEntity(HttpResponse response) {
		try {

			log.debug("#### AlcFaceHttpResponseEntity HttpResponse #### : {}", response);

			int statusCode = response.getCode();
			log.debug("#### AlcFaceHttpResponseEntity HttpResponse > statusCode #### : {}", statusCode);
			if (statusCode != 200) {
				this.errorCode = AlcClient.ERR_MA004;
				// 통신 정보가 유효하지 않습니다.
				this.errorMessage = AlcClient.ERR_MA004_MSG;
				this.jsonRoot = AlcHttpJsonObject.fromObject(new JSONObject());
				return;
			}

			String body = response.getBody();
			log.debug("#### AlcFaceHttpResponseEntity > HttpResponse > body #### : {}", body);

			if (body == null) {
				this.errorCode = AlcClient.ERR_MA001;
				// JSON DATA를 확인해 주세요.
				this.errorMessage = AlcClient.ERR_MA001_MSG;
				this.jsonRoot = AlcHttpJsonObject.fromObject(new JSONObject());
				return;
			}

			// 알체라 안면인식 서버 응답 값 특정 항목 치환 처리
			this.jsonRoot = AlcHttpJsonObject.fromObject(body);

			// 응답 에러 여부 체크
			AlcHttpJsonObject jsonRoot2 = this.jsonRoot.getAsJsonObject("return_msg");
			String errorCode = jsonRoot2.getAsString(ERROR_CODE, ERROR_CODE_SUCCESS);
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

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public AlcHttpJsonObject getBody() {
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

	// ERROR코드가 SUCC-0000 아닌 경우 Exception를 발생시킨다.
	public AlcFaceHttpResponseEntity throwException(){
		if (getErrorCode().indexOf("SUCC-0000") != 0) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage);
		}
		return this;
	}
}
