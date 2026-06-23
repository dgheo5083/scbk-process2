package com.scbank.process.api.svc.shared.components.frs.model;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.frs.IFrsClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 안면인식 응답 Entity 클래스
 */
@Slf4j
public class FrsHttpResponseEntity {
	
	public static final String ERROR_CODE_SUCCESS = "999";
	public static final String ERROR_CODE = "response_code";
	public static final String ERROR_MSG = "response_message";
	
	private FrsHttpJsonObject jsonRoot;
	
	private String errorCode = null;
	private String errorMessage = null;
	private Throwable cause = null;
	
	public FrsHttpResponseEntity(HttpResponse response) {
		// set json object data 
		try {
			log.debug("### FrsHttpResponseEntity HttpResponse : {}", response);
			
			if (!response.isOK()) {
				this.errorCode = IFrsClient.ERR_MA003;
				this.errorMessage = IFrsClient.ERR_MA003_MSG;
				this.jsonRoot = FrsHttpJsonObject.fromObject(new JSONObject());
				return;
			} 
			
			if (response.getBody() == null) {
				this.errorCode = IFrsClient.ERR_MA001;
				this.errorMessage = IFrsClient.ERR_MA001_MSG;
				this.jsonRoot = FrsHttpJsonObject.fromObject(new JSONObject());
				return;
			}
			
			String jsonString = response.getBody();
			
			log.debug(" 안면인식 요청 응답 => {}", jsonString);
			
			// 가져온 데이타를 검증한다.
			this.jsonRoot = FrsHttpJsonObject.fromObject(new JSONObject(jsonString));
			// error code 추출
			String errorCode = this.jsonRoot.getAsString(ERROR_CODE, ERROR_CODE_SUCCESS);
			if (errorCode == null) {
				this.errorCode = errorCode;
				this.errorMessage = "알수 없는 에러. 금결원 에러코드를 확인하세요.";
			} else {
				this.errorCode = errorCode;
				this.errorMessage = this.jsonRoot.getAsString(ERROR_MSG, "알수 없는 에러. 금결원 에러코드를 확인하세요.");
			}
			
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			this.errorCode = IFrsClient.ERR_MA001;
			this.errorMessage = ex.getMessage();
		}
		
		if (this.jsonRoot == null) {
			this.errorCode = IFrsClient.ERR_MA002;
			this.errorMessage = IFrsClient.ERR_MA002_MSG;
		}
	}
	
	public boolean isError() {
		if (errorCode == null || "".equals(errorCode.trim()) == true) 
			return false;
		
		return true;
	}

	public String getErrorCode() {
		return this.errorCode;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public FrsHttpJsonObject getBody() {
		return this.jsonRoot;
	}
	
	public JSONObject getAsJsonObject() {
		if (this.jsonRoot == null) return new JSONObject();
		return this.jsonRoot.getAsJsonObject();
	}
	
	public String getAsFwErrorCode(String code) {
		return String.format("MAFU-%s", code);
	}
	
	/**
	 * ERROR코드가 (O)uth관련 에러일 경우 Exception를 발생시킨다.
	 * @return
	 * @throws Ma30FrameworkException
	 */
	public FrsHttpResponseEntity throwException() throws PRCServiceException {
		if (isError() && getErrorCode().indexOf("O") == 0) {
			throw new PRCServiceException(getAsFwErrorCode(this.errorCode), this.errorMessage, this.cause);
		}
		
		return this;
	}
	
}
