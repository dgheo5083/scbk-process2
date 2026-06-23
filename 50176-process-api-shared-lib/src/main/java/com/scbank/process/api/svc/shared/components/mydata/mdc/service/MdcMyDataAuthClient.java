package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpResponseEntity;

/**
 * 마이데이터 인증 API 클라이언트 인터페이스
 */
public interface MdcMyDataAuthClient {

    /**
     * MYDATA 토큰 발급
     * KONG 서버와 https 통신 으로 토큰을 발급받는다. (/sc/oauth2-jwt/token)
     * 
     * @return
     */
    MyDataHttpResponseEntity getToken();

    /**
     * 공공꾸러미(인터넷뱅킹용) KONG 토큰 발급
     * KONG 서버와 https 통신 으로 토큰을 발급받는다. (/sc/oauth2-jwt/token)  
     * @return
     */
    MyDataHttpResponseEntity getInternetToken();
}
