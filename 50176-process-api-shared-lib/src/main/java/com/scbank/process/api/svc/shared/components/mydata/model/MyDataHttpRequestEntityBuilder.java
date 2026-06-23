package com.scbank.process.api.svc.shared.components.mydata.model;

import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.mydata.IMyDataClientInterceptor;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component
public class MyDataHttpRequestEntityBuilder {

	private MyDataHttpRequestEntity entity;

	public static MyDataHttpRequestEntityBuilder builder(String id) {
		return new MyDataHttpRequestEntityBuilder(id);
	}

	public MyDataHttpRequestEntityBuilder(String id) {
		this.entity = new MyDataHttpRequestEntity();
		this.id(id);
	}

	/**
	 * 프로퍼티로 부터 값을 가져온다.
	 * url, scope, method
	 * 
	 * @return
	 */
	private MyDataHttpRequestEntityBuilder id(String id) {
		log.debug("# id : {}", id);
		// properties값을 가져와 기본 value값을 설정한다.
		String baseUrl = PropertiesUtils.getString(MdcMyDataHelper.MYDATA_PROP_BASEURL);
		String propurl = PropertiesUtils.getString(MdcMyDataHelper.MYDATA_PROP_URL);
		String url = PropertiesUtils.getString(String.format("%s.url", id));
		String method = PropertiesUtils.getString(String.format("%s.method", id));

		log.debug("###PSH MyDataHttpRequestEntityBuilder url : [{}]", url);
		log.debug("###PSH MyDataHttpRequestEntityBuilder baseUrl : [{}]", baseUrl);
		log.debug("###PSH MyDataHttpRequestEntityBuilder propurl : [{}]", propurl);
		log.debug("###PSH MyDataHttpRequestEntityBuilder url : [{}]", url);
		log.debug("###PSH MyDataHttpRequestEntityBuilder method : [{}]", method);

		this.entity = new MyDataHttpRequestEntity();
		this.entity.setApiId(id);
		this.entity.setUrl(propurl + url);
		this.entity.setMethod(method);

		return this;
	}

	public MyDataHttpRequestEntityBuilder accessToken(String accessToken) {
		this.entity.setAccessToken(accessToken);
		return this;
	}

	public MyDataHttpRequestEntityBuilder headers(Map<String, String> headers) {
		this.entity.setHeaders(headers);

		return this;
	}

	public MyDataHttpRequestEntityBuilder parameters(JSONObject parameters) {
		this.entity.setParameters(parameters);

		return this;
	}

	public MyDataHttpRequestEntityBuilder bodyParameters(JSONObject parameters) {
		this.entity.setBodyParameters(parameters);

		return this;
	}

	public MyDataHttpRequestEntityBuilder interceptor(IMyDataClientInterceptor interceptor) {
		this.entity.setInterceptor(interceptor);

		return this;
	}

	public MyDataHttpRequestEntity build() {
		return this.entity;
	}
}
