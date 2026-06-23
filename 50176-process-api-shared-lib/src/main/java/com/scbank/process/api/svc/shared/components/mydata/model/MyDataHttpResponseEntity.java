package com.scbank.process.api.svc.shared.components.mydata.model;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.mydata.IMyDataClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * MAFU-XXXX 연계에러코드 정의.
 * 
 * @author 937827
 *
 */
@Slf4j
public class MyDataHttpResponseEntity {

	public static final String ERROR_CODE_SUCCESS = "A0000";
	public static final String ERROR_CODE = "rsp_code";
	public static final String ERROR_MSG = "rsp_message";

	private MyDataHttpJsonObject jsonRoot;

	private String errorCode = null;
	private String errorMessage = null;
	private Throwable cause = null;

	public MyDataHttpResponseEntity(HttpResponse response) {
		// set json object data
		try {
			int statusCode = response.getCode();
			if (statusCode != 200) {
				this.errorCode = IMyDataClient.ERR_MA003;
				this.errorMessage = IMyDataClient.ERR_MA003_MSG;
				this.jsonRoot = MyDataHttpJsonObject.fromObject(new JSONObject());
				// return;
			}

			String body = response.getBody();
			if (body == null) {
				this.errorCode = IMyDataClient.ERR_MA001;
				this.errorMessage = IMyDataClient.ERR_MA001_MSG;
				this.jsonRoot = MyDataHttpJsonObject.fromObject(new JSONObject());
				// return;
			}

			log.debug(" - RESPONSE >> {}", body);

			// 가져온 데이타를 검증한다.
			this.jsonRoot = MyDataHttpJsonObject.fromObject(new JSONObject(body));
			// error code 추출
			String errorCode = this.jsonRoot.getAsString(ERROR_CODE, ERROR_CODE_SUCCESS); // 에러코드가 없으므로 정상처리한다.
			if (errorCode == null) {
				this.errorCode = errorCode;
				this.errorMessage = "알수 없는 에러. MyData 에러코드를 확인하세요.";

			} else if (errorCode.indexOf("O") == 0) {
				this.errorCode = errorCode;
				this.errorMessage = this.jsonRoot.getAsString(ERROR_MSG, "알수 없는 에러. MyData 에러코드를 확인하세요.");
			} else {
				this.errorCode = errorCode;
				this.errorMessage = this.jsonRoot.getAsString(ERROR_MSG, "알수 없는 에러. MyData 에러코드를 확인하세요.");
			}
		} catch (Exception ex) {
			this.errorCode = IMyDataClient.ERR_MA001;
			this.errorMessage = ex.getMessage();
		}

		if (this.jsonRoot == null) {
			this.errorCode = IMyDataClient.ERR_MA002;
			this.errorMessage = IMyDataClient.ERR_MA002_MSG;
		}
	}

	public boolean isError() {
		if (errorCode == null || "".equals(errorCode.trim()) == true)
			return false;

		return true;
	}

	/**
	 * 에러코드가 일치하면 true를 리턴한다.
	 * 
	 * @param errorCodes
	 * @return
	 */
	public boolean compareError(String... errorCodes) {
		if (isError()) {
			for (String errCode : errorCodes) {
				if (errCode.equals(getErrorCode()))
					return true;
			}
		}

		return false;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public MyDataHttpJsonObject getBody() {
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
	public MyDataHttpResponseEntity throwException() {
		if (isError() && getErrorCode().indexOf("O") == 0) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage, this.cause);
		}

		return this;
	}

	/**
	 * ERROR코드가 (O)uth관련 에러일 경우 Exception를 발생시킨다.
	 * text를 넣어줄 경우 rsp_code를 셋팅해서 보낸다.
	 * 
	 * @return
	 * @throws Ma30FrameworkException
	 */
	public MyDataHttpResponseEntity throwException(String rsp_code) {
		if (isError() && getErrorCode().indexOf("O") == 0) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage, this.cause);
		}

		JSONObject a = this.jsonRoot.getAsJsonObject();
		a.put("rsp_code", rsp_code);

		this.errorCode = rsp_code;

		return this;
	}

	/**
	 * ERROR코드가 (O)uth관련 에러일 경우 Exception를 발생시킨다.
	 * text를 넣어줄 경우 rsp_code를 셋팅해서 보낸다.
	 * 
	 * @return
	 * @throws Ma30FrameworkException
	 */
	public MyDataHttpResponseEntity throwException(String rsp_code, String bank_rsp_code) {
		if (isError() && getErrorCode().indexOf("O") == 0) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage, this.cause);
		}

		JSONObject a = this.jsonRoot.getAsJsonObject();
		a.put("rsp_code", rsp_code);
		a.put("bank_rsp_code", bank_rsp_code);

		this.errorCode = rsp_code;

		return this;
	}
}
