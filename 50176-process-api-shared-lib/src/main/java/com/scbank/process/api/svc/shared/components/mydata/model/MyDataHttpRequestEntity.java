package com.scbank.process.api.svc.shared.components.mydata.model;

import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.svc.shared.components.mydata.IMyDataClientInterceptor;

public class MyDataHttpRequestEntity {
	public static final int OPT_PARAM_MAP = 1;
	public static final int OPT_PARAM_JSON = 2;

	private String apiId; // URL등을 가져올 api 구분 값.

	private String url;
	private String accessToken;
	private String method;
	private int option;

	private Map<String, String> headers;
	private JSONObject bodyParameters;
	private IMyDataClientInterceptor interceptor;

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public JSONObject getBodyParameters() {
		return this.bodyParameters;
	}

	public void setBodyParameters(JSONObject bodyParameters) {
		this.option = OPT_PARAM_JSON;
		this.bodyParameters = bodyParameters;
	}

	public JSONObject getParameters() {
		return this.bodyParameters;
	}

	public void setParameters(JSONObject bodyParameters) {
		this.option = OPT_PARAM_MAP;
		this.bodyParameters = bodyParameters;
	}

	public void setInterceptor(IMyDataClientInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	public IMyDataClientInterceptor getInterceptor() {
		return this.interceptor;
	}

	@Override
	public String toString() {
		return "MyDataHttpRequestEntity [url=" + url + ", accessToken=" + accessToken + ", method=" + method
				+ ", option=" + option + ", headers=" + headers + ", bodyParameters=" + bodyParameters + "]";
	}

}
