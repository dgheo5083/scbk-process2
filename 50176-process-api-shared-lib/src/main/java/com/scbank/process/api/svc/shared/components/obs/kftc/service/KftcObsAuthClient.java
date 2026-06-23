package com.scbank.process.api.svc.shared.components.obs.kftc.service;

import org.json.JSONObject;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntityBuilder;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.obs.utils.KftcObsHelper;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@SharedComponent(name = "금결원 오픈뱅킹 인증 API 클라이언트 공유 컴포넌트")
public class KftcObsAuthClient extends KftcObsApiClientBase {

    /**
     * 생성자
     * @param httpClient {@link BaseHttpClient}
     */
    public KftcObsAuthClient(BaseHttpClient httpClient) {
        super(httpClient);
    }

    /**
     * API 토큰 조회
     * 
     * @return
     */
    @ComponentOperation(name = "금결원 오픈뱅킹 토큰 요청")
    public ObsHttpResponseEntity getToken() {
        JSONObject params = new JSONObject()
                .put("client_id", KftcObsHelper.getClientId().trim())
                .put("client_secret", KftcObsHelper.getClientSecret().trim())
                .put("grant_type", "client_credentials")
                .put("scope", "sa");
        try {
            ObsHttpRequestEntity request = ObsHttpRequestEntityBuilder.builder(KftcObsHelper.TOKEN)
                    .parameters(params)
                    .build();
            return this.executeCall(request).throwException();
        } catch (Exception e) {
            throw e;
        }
    }
}
