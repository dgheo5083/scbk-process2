package com.scbank.process.api.svc.shared.components.alchera.model;

import java.util.Map;

import org.json.JSONObject;

/**
 * 알체라 서버 API 요청 Entity 클래스
 */
public class AlcHttpRequestEntity {
	private Map<String, String> headers;
	private JSONObject bodyParameters;

	private String url;
	private String method;
	private int option;

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public JSONObject getBodyParameters() {
		return bodyParameters;
	}

	public void setBodyParameters(JSONObject bodyParameters) {
		this.bodyParameters = bodyParameters;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	@Override
	public String toString() {
		return "AlcHttpRequestEntity [url=" + url + ", method=" + method + ", option=" + option + ", headers=" + headers
				+ ", bodyParameters=" + bodyParameters + "]";
	}

}
