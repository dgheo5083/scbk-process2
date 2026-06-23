package com.scbank.process.api.svc.shared.components.alchera.model;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

/**
 * 알체라 안면인식 서버 응답 Entity 빌더 클래스
 */
@Component
public class AlcFaceHttpRequestEntityBuilder {

	private AlcHttpRequestEntity entity;

	@SuppressWarnings("unused")
	private AlcFaceHttpRequestEntityBuilder() {
	};

	public static AlcFaceHttpRequestEntityBuilder builder(String id) {
		return new AlcFaceHttpRequestEntityBuilder(id);
	}

	public AlcFaceHttpRequestEntityBuilder(String id) {
		this.entity = new AlcHttpRequestEntity();
		this.id(id);
	}

	private AlcFaceHttpRequestEntityBuilder id(String id) {
		String baseUrl = PropertiesUtils.getString("ALCHERA_FACE_SERVER_BASE_URL");
		String service = id;
		String method = "POST";

		this.entity = new AlcHttpRequestEntity();
		this.entity.setUrl(baseUrl + service);
		this.entity.setMethod(method);
		return this;
	}

	public AlcFaceHttpRequestEntityBuilder headers(Map<String, String> headers) {
		this.entity.setHeaders(headers);
		return this;
	}

	public AlcFaceHttpRequestEntityBuilder bodyParameters(JSONObject parameters) {
		this.entity.setBodyParameters(parameters);
		return this;
	}

	public AlcHttpRequestEntity build() {
		return this.entity;
	}
}
