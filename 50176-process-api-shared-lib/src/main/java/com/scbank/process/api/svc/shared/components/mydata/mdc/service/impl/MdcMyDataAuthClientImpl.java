package com.scbank.process.api.svc.shared.components.mydata.mdc.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataHelper;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataApiClientBase;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataAuthClient;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntityBuilder;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpResponseEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "마이데이터 인증 API 클라이언트 공유 컴포넌트")
public class MdcMyDataAuthClientImpl extends MdcMyDataApiClientBase implements MdcMyDataAuthClient {

    public MdcMyDataAuthClientImpl(BaseHttpClient httpClient) {
        super(httpClient);
    }

    @ComponentOperation(name = "MYDATA 토큰 발급")
    public MyDataHttpResponseEntity getToken() {
        log.debug("::::: MdcMyDataApiClientImpl getToken KONG Connection Start ::::: ");

        long systemEpoch = System.currentTimeMillis() / 1000;
        long epoch = systemEpoch + 60 * 60 * 24 * 365;
        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(epoch * 1000));

        log.debug(" ############# systemEpoch ::" + systemEpoch);
        log.debug(" ############# epoch ::" + epoch);
        log.debug(" ############# 발급일자 ::" + date);

        JSONObject params = new JSONObject();
        params.put("grant_type", "client_credentials"); // 토큰발급 Type
        params.put("client_id", "Mobile"); // Kong에 등록된 Client 명
        params.put("client_secret", "Mobile_secret"); // Kong에 등록된 Client에 맵핑되는 secret 값
        params.put("refresh_token_expires_in", epoch); // 리프레시 토큰에 대한 만료기간(Unix Time Stamp)

        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> payloadMap = new HashMap<>();

        headerMap.put("alg", "HS256");
        headerMap.put("typ", "typ");

        payloadMap.put("iss", "A1AAAK0000");
        payloadMap.put("aud", "MobileB3");
        payloadMap.put("jti", String.valueOf(epoch));
        payloadMap.put("exp", epoch);
        payloadMap.put("scope", "mobile");

        params.put("header", headerMap);
        params.put("payload", payloadMap);

        try {
            MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder
                    .builder(MdcMyDataHelper.TOKEN_DATA)
                    .bodyParameters(params)
                    .build();

            MyDataHttpResponseEntity responseEntity = executeCall(requestEntity).throwException();

            responseEntity.getAsJsonObject().put("expiresIn", Long.toString(epoch)); // 토큰 유효기간
            responseEntity.getAsJsonObject().put("expires_date", date); // 발급일자 : 현재일자 + 365

            log.debug("::::: MdcMyDataApiClientImpl getToken KONG Connection End ::::: ");

            return responseEntity;

        } catch (PRCServiceException e) {
            throw e;
        }
    }

    @ComponentOperation(name = "공공꾸러미(인터넷뱅킹용) KONG 토큰 발급")
    public MyDataHttpResponseEntity getInternetToken() {
        log.debug("::::: MdcMyDataApiClientImpl getInternetToken KONG Connection Start ::::: ");

        long systemEpoch = System.currentTimeMillis() / 1000;
        long epoch = systemEpoch + 60 * 60 * 24 * 365;
        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(epoch * 1000));

        log.debug(" ############# systemEpoch ::" + systemEpoch);
        log.debug(" ############# epoch ::" + epoch);
        log.debug(" ############# 발급일자 ::" + date);

        JSONObject params = new JSONObject();
        params.put("grant_type", "client_credentials"); // 토큰발급 Type
        params.put("client_id", "OIB"); // Kong에 등록된 Client 명
        params.put("client_secret", "OIB_secret"); // Kong에 등록된 Client에 맵핑되는 secret 값
        params.put("refresh_token_expires_in", epoch); // 리프레시 토큰에 대한 만료기간(Unix Time Stamp)

        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> payloadMap = new HashMap<>();

        headerMap.put("alg", "HS256");
        headerMap.put("typ", "typ");

        payloadMap.put("iss", "A1AAAK0000");
        payloadMap.put("aud", "MobileB3");
        payloadMap.put("jti", String.valueOf(epoch));
        payloadMap.put("exp", epoch);
        payloadMap.put("scope", "internet");

        params.put("header", headerMap);
        params.put("payload", payloadMap);

        try {
            MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.TOKEN_DATA)
                    .bodyParameters(params)
                    .build();

            MyDataHttpResponseEntity responseEntity = executeCall(requestEntity).throwException();

            responseEntity.getAsJsonObject().put("expiresIn", Long.toString(epoch)); // 토큰 유효기간
            responseEntity.getAsJsonObject().put("expires_date", date); // 발급일자 : 현재일자 + 365

            log.debug("::::: MdcMyDataApiClientImpl getInternetToken KONG Connection End ::::: ");

            return responseEntity;
        } catch (PRCServiceException e) {
            throw e;
        }
    }
}
