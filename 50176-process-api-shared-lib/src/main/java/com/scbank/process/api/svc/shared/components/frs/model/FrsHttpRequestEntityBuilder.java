package com.scbank.process.api.svc.shared.components.frs.model;

import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.frs.IFrsClientInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 안면인식 요청 데이터 빌더 클래스
 */
@Slf4j
public class FrsHttpRequestEntityBuilder {

	private String propName = "kftc.face-id";

	private FrsHttpRequestEntity entity;

	public static FrsHttpRequestEntityBuilder builder(String id) {
		return new FrsHttpRequestEntityBuilder(id);
	}

	public FrsHttpRequestEntityBuilder(String id) {
		this.entity = new FrsHttpRequestEntity();
		this.id(id);
	}

	/**
	 * 
	 * @param name kftc, other
	 * @return
	 */
	public FrsHttpRequestEntityBuilder prefix(String name) {
		propName = PropertiesUtils.getString(String.format("face-id.prop.%s.name"), name);
		return this;
	}

	/**
	 * 프로퍼티로 부터 값을 가져온다. url, scope, method
	 * 
	 * @return
	 */
	private FrsHttpRequestEntityBuilder id(String id) {

		// properties값을 가져와 기본 value값을 설정한다.
		String baseUrl = PropertiesUtils.getString(String.format("%s.api.base.url", propName));
		String url = PropertiesUtils.getString(String.format("%s.api.%s.url", propName, id));
		String method = PropertiesUtils.getString(String.format("%s.api.%s.method", propName, id));


		this.entity = new FrsHttpRequestEntity();

		this.entity.setPropName(propName);
		this.entity.setUrl(baseUrl + url);
		this.entity.setMethod(method);

		return this;
	}

	public FrsHttpRequestEntityBuilder accessToken(String accessTokean) {
		this.entity.setAccessToken(accessTokean);
		return this;
	}

	public FrsHttpRequestEntityBuilder headers(Map<String, String> headers) {
		this.entity.setHeaders(headers);
		log.info("FrsHttpRequestEntityBuilder headers => {} ", entity);

		return this;
	}

	public FrsHttpRequestEntityBuilder parameters(JSONObject parameters) {
		this.entity.setParameters(parameters);
		log.info("FrsHttpRequestEntityBuilder parameters => {}", entity);
		return this;
	}

	public FrsHttpRequestEntityBuilder bodyParameters(JSONObject parameters) {
		log.info("FrsHttpRequestEntityBuilder bodyParameters => {}", parameters);
		this.entity.setBodyParameters(parameters);

		return this;
	}
	
	public FrsHttpRequestEntityBuilder interceptor (IFrsClientInterceptor interceptor) {
		this.entity.setInterceptor(interceptor);
		
		return this;
	}

	public FrsHttpRequestEntity build() {
		return this.entity;
	}
}
