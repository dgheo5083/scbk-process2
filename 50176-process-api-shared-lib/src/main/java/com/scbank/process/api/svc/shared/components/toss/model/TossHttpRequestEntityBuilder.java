package com.scbank.process.api.svc.shared.components.toss.model;

import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.toss.ITossClient;

import lombok.extern.slf4j.Slf4j;

/**
 * TossHttpRequestEntity 빌더 클래스
 */
@Slf4j
public class TossHttpRequestEntityBuilder {

	private TossHttpRequestEntity entity;

	public TossHttpRequestEntityBuilder(String id) {
		this.entity = new TossHttpRequestEntity();
		this.id(id);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static TossHttpRequestEntityBuilder builder(String id) {
		return new TossHttpRequestEntityBuilder(id);
	}

	/**
	 * 프로퍼티로 부터 값을 가져온다.
	 * url, scope, method
	 * 
	 * @param id
	 * @return
	 */
	private TossHttpRequestEntityBuilder id(String id) {
		ISessionContextManager sessionContextManager = RuntimeContext.getBean(ISessionContextManager.class);

		// properties값을 가져와 기본 value값을 설정한다.
		String baseUrl = PropertiesUtils.getString(TossHelper.PROP_BASEURL);
		String url = PropertiesUtils.getString(String.format("%s.url", id));
		String method = PropertiesUtils.getString(String.format("%s.method", id));
		String host = PropertiesUtils.getString(String.format("%s.host", id));
		String accessToken = sessionContextManager.getGlobalValue(TossHelper.TOSS_OUT_AUTH_TOKEN_SESSION_KEY,
				String.class);

		// session Toss Access Token이 null 또는 공백이면 properties 가져온다.
		if (StringUtils.isEmpty(accessToken)) {
			accessToken = PropertiesUtils.getString(TossHelper.OUT_AUTH_TOKEN);
			if (StringUtils.isEmpty(accessToken)) {
				throw new PRCServiceException(ITossClient.ERR_MA004, ITossClient.ERR_MA004_MSG);
			}
			sessionContextManager.setGlobalValue(TossHelper.TOSS_OUT_AUTH_TOKEN_SESSION_KEY, accessToken);
		}
		log.debug("TOSS API baseUrl [{}] url:[{}] method:[{}] host:[{}]", baseUrl, url, method, host);
		log.debug("TOSS API accessToken [{}]", accessToken);

		this.entity = new TossHttpRequestEntity();
		this.entity.setApiId(id);
		this.entity.setUrl(baseUrl + url);
		this.entity.setMethod(method);
		this.entity.setHost(host);
		this.entity.setAccessToken(accessToken);

		return this;
	}

	public TossHttpRequestEntityBuilder headers(Map<String, String> headers) {
		this.entity.setHeaders(headers);
		return this;
	}

	public TossHttpRequestEntityBuilder bodyParameters(JSONObject parameters) {
		this.entity.setBodyParameters(parameters);
		return this;
	}

	public TossHttpRequestEntity build() {
		return this.entity;
	}
}
