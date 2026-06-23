package com.scbank.process.api.svc.shared.components.obs.model;

import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.obs.IObsClientInterceptor;

/**
 * 금결원 오픈뱅킹 요청 데이터 빌더 클래스
 */
public class ObsHttpRequestEntityBuilder {

    private String propName = "kftc.open-platform";

    private ObsHttpRequestEntity request;

    public static ObsHttpRequestEntityBuilder builder(String id) {
        return new ObsHttpRequestEntityBuilder(id);
    }

    public ObsHttpRequestEntityBuilder(String id) {
        this.request = new ObsHttpRequestEntity();
        this.id(id);
    }

    /**
     * 
     * @param name kftc, other
     * @return
     */
    public ObsHttpRequestEntityBuilder prefix(String name) {
        propName = PropertiesUtils.getString(String.format("openapi.prop.%s.name"), name);
        return this;
    }

    /**
     * 프로퍼티로 부터 값을 가져온다.
     * url, scope, method
     * 
     * @return
     */
    private ObsHttpRequestEntityBuilder id(String id) {
        // properties값을 가져와 기본 value값을 설정한다.
        String baseUrl = PropertiesUtils.getString(String.format("%s.api.base.url", propName));
        String url = PropertiesUtils.getString(String.format("%s.api.%s.url", propName, id));
        String method = PropertiesUtils.getString(String.format("%s.api.%s.method", propName, id));

        this.request = new ObsHttpRequestEntity();
        this.request.setPropName(propName);
        this.request.setApiId(id);

        this.request.setUrl(baseUrl + url);
        this.request.setMethod(method);

        return this;
    }

    /**
     * access 토큰 설정
     * 
     * @param accessToken
     * @return
     */
    public ObsHttpRequestEntityBuilder accessToken(String accessToken) {
        this.request.setAccessToken(accessToken);
        return this;
    }

    /**
     * 헤더 설정
     * 
     * @param headers
     * @return
     */
    public ObsHttpRequestEntityBuilder headers(Map<String, String> headers) {
        this.request.setHeaders(headers);

        return this;
    }

    /**
     * 파라미터 설정
     * 
     * @param parameters
     * @return
     */
    public ObsHttpRequestEntityBuilder parameters(JSONObject parameters) {
        this.request.setParameters(parameters);

        return this;
    }

    /**
     * 요청 body 데이터 설정
     * 
     * @param parameters
     * @return
     */
    public ObsHttpRequestEntityBuilder bodyParameters(JSONObject parameters) {
        this.request.setBodyParameters(parameters);

        return this;
    }
    
    /**
     * 인터셉터를 추가한다.
     * @param interceptor {@link IObsClientInterceptor}
     * @return
     */
    public ObsHttpRequestEntityBuilder interceptor (IObsClientInterceptor interceptor) {
		this.request.setInterceptor(interceptor);
		
		return this;
	}	

    public ObsHttpRequestEntity build() {
        return this.request;
    }
}
