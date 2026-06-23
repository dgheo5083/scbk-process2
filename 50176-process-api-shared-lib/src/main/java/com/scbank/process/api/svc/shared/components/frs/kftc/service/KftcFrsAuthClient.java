package com.scbank.process.api.svc.shared.components.frs.kftc.service;

import org.json.JSONObject;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntityBuilder;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.frs.util.KftcFrsHelper;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@SharedComponent(name = "금결원 안면인증 인증 API 클라이언트 공유 컴포넌트")
public class KftcFrsAuthClient extends KftcFrsApiClientBase {
	
	public KftcFrsAuthClient(BaseHttpClient httpClient) {
		super(httpClient);
	}
	
	/**
     * API 토큰 조회
     * 
     * @return
     */
    @ComponentOperation(name = "금결원 오픈뱅킹 API토큰 획득")
    public FrsHttpResponseEntity getToken() {
		JSONObject params = new JSONObject();
		params.put("client_id", KftcFrsHelper.getClientId().trim());
		params.put("client_secret", KftcFrsHelper.getClientSecret().trim());
		params.put("grant_type", KftcFrsHelper.GRANT_TYPE);
		params.put("scope", KftcFrsHelper.SCOPE);
		
		FrsHttpRequestEntity requestEntity = FrsHttpRequestEntityBuilder.builder(KftcFrsHelper.TOKEN).parameters(params).build();

		FrsHttpResponseEntity responseEntity = executeCall(requestEntity).throwException();

		return responseEntity;
	}

}
