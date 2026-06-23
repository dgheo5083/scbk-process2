package com.scbank.process.api.svc.shared.components.mydata.mdc.service.impl;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataApiClient;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataApiClientBase;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataAuthManager;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpResponseEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "마이데이터 API 클라이언트 공유 컴포넌트")
public class MdcMyDataApiClientImpl extends MdcMyDataApiClientBase implements MdcMyDataApiClient {

    private static MdcMyDataAuthVo oauthInfo;

    private final MdcMyDataAuthManager myDataAuthManager;

    public MdcMyDataApiClientImpl(BaseHttpClient httpClient, MdcMyDataAuthManager myDataAuthManager) {
        super(httpClient);
        this.myDataAuthManager = myDataAuthManager;
    }

    @PostConstruct
    public void initPostConstruct() {
        oauthInfo = new MdcMyDataAuthVo();
    }

    @ComponentOperation(name = "마이데이터 API 클라이언트 초기화")
    public MdcMyDataApiClient init() {

        validationOAuth();

        return this;
    }

    @ComponentOperation(name = "마이데이터 인증정보 유효성 검증")
    public void validationOAuth() {
        try {
            log.debug("::::: MdcMyDataApiClientImpl validationOAuth () :::::");
            oauthInfo.clone(myDataAuthManager.getSelectToken());
        } catch (Exception e) {
            throw new PRCServiceException("MYDATA-MA004", "연동 정보가 유효하지 않습니다.", e);
        }
    }

    @Override
    @ComponentOperation(name = "마이데이터 인증정보 획득")
    public MdcMyDataAuthVo getAuth() {
        return oauthInfo;
    }

    @Override
    @ComponentOperation(name = "마이데이터 API 호출")
    public MyDataHttpResponseEntity execute(MyDataHttpRequestEntity entity) {
        log.debug("::::: MdcMyDataApiClientImpl execute ()::::: {}", entity.getAccessToken());
        return executeCall(entity);
    }
}
