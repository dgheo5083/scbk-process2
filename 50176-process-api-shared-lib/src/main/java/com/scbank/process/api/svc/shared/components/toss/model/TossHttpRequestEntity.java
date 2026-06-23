package com.scbank.process.api.svc.shared.components.toss.model;

import java.util.Map;

import org.json.JSONObject;

public class TossHttpRequestEntity {
	private String apiId; // URL등을 가져올 api 구분 값.
	private String url;
	private String accessToken;
	private String method;
	private String host;
	private int option;

	private Map<String, String> headers;
	private JSONObject bodyParameters;

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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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
		this.bodyParameters = bodyParameters;
	}

	@Override
	public String toString() {
		return "HttpOpenApiRequestEntity [url=" + url + ", method=" + method + ", host=" + host + ", option=" + option
				+ ", headers=" + headers + ", bodyParameters=" + bodyParameters + "]";
	}

}
