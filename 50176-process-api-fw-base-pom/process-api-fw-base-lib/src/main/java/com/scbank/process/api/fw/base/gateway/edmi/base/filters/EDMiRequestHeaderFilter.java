package com.scbank.process.api.fw.base.gateway.edmi.base.filters;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import com.scbank.process.api.fw.integration.client.filter.FeignFilter;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;

import feign.RequestTemplate;
import lombok.Data;

/**
 * EDMi 전문 요청 HTTP 헤더 설정 필터 구현 클래스
 * 
 * @author sungdon.choi
 */
@Data
public class EDMiRequestHeaderFilter implements FeignFilter {

    @Value("${edmi.gateway.default.userName}")
    private String userName;

    @Value("${edmi.gateway.default.credential}")
    private String userPassword;

    @Value("${edmi.gateway.default.charset:UTF-8}")
    private String charset;

    private static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    @Override
    public void onTemplate(RequestTemplate requestTemplate, FeignFilterContext ctx) {
        requestTemplate.header("Content-Type", DEFAULT_CONTENT_TYPE);
        requestTemplate.header("Charset", charset);

        requestTemplate.header("Accept", DEFAULT_CONTENT_TYPE);
        requestTemplate.header("Accept-Charset", charset);

        requestTemplate.header("Authorization", this.getUserCredentials());
    }
    
    protected String resolveUserName() {
    	return this.userName;
    }
    
    protected String resolveUserPassword() {
    	return this.userPassword;
    }

    /**
     * Authorization 문자열 생성
     * 
     * @return Authorization 문자열
     */
    private String getUserCredentials() {
    	String userName = this.resolveUserName();
    	String userPassword = this.resolveUserPassword();
        String userCredentials = Base64.getEncoder().encodeToString((userName + ":" + userPassword).getBytes());
        String basicAuth = "Basic " + userCredentials;
        return basicAuth;
    }
}
