package com.scbank.process.api.svc.shared.components.alchera.model;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 알체라 서버 연계 요청 Entity 빌더 클래스
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class AlcHttpRequestEntityBuilder {

	/**
	 * 요청 Entity
	 */
	private AlcHttpRequestEntity entity;

	/**
	 * 알체라 서버 연계 요청 Entity 빌더 인스턴스를 생성한다.
	 * @param id API ID
	 * @return 알체라 서버 연계 요청 Entity 빌더 인스턴스
	 */
	public static AlcHttpRequestEntityBuilder builder(String id) {
		return new AlcHttpRequestEntityBuilder(id);
	}

	/**
	 * 생성자
	 * @param id 요청 API 문자열
	 */
	public AlcHttpRequestEntityBuilder(String id) {
		this.entity = new AlcHttpRequestEntity();
		this.id(id);
	}

	/**
	 * 요청 API 문자열 설정한다.
	 * @param id 요청 API 문자열
	 * @return {@link AlcHttpRequestEntityBuilder}
	 */
	private AlcHttpRequestEntityBuilder id(String id) {
		String baseUrl = PropertiesUtils.getString("ALCHERA_SERVER_BASE_URL");
		String service = "/scan/" + id;
		String method = "POST";

		this.entity = new AlcHttpRequestEntity();
		this.entity.setUrl(baseUrl + service);
		this.entity.setMethod(method);
		return this;
	}

	/**
	 * 헤더정보를 설정한다.
	 * @param headers 헤더정보를 담고 있는 Map 객체
	 * @return {@link AlcHttpRequestEntityBuilder}
	 */
	public AlcHttpRequestEntityBuilder headers(Map<String, String> headers) {
		this.entity.setHeaders(headers);
		return this;
	}

	/**
	 * 요청 본문 정보를 설정한다.
	 * @param parameters JSONObject 타입의 요청 본문 데이터
	 * @return {@link AlcHttpRequestEntityBuilder}
	 */
	public AlcHttpRequestEntityBuilder bodyParameters(JSONObject parameters) {
		this.entity.setBodyParameters(parameters);
		return this;
	}

	/**
	 * AlcHttpRequestEntity 빌드
	 * @return
	 */
	public AlcHttpRequestEntity build() {
		return this.entity;
	}
}
