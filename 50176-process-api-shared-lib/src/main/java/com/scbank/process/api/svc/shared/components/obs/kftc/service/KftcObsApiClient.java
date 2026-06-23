package com.scbank.process.api.svc.shared.components.obs.kftc.service;

import org.springframework.beans.factory.InitializingBean;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.obs.kftc.model.KftcObsAuthVo;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.obs.utils.ObsUniqueUtils;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 금결원 오픈뱅킹 API 클라이언트 공유 컴포넌트
 * AS-IS를 참고하여 재작성하였음
 */
@Slf4j
@SharedComponent(name = "금결원 오픈뱅킹 API 요청 공유 컴포넌트")
public class KftcObsApiClient extends KftcObsApiClientBase implements InitializingBean {

    /**
     * 오픈뱅킹 API토큰 정보
     */
    private static KftcObsAuthVo oauthInfo;

    /**
     * 오픈뱅킹 API토큰 관리 매니저 컴포넌트
     */
    private final KftcObsTokenManager kftcObsTokenManager;

    /**
     * 
     * @param httpClient
     * @param kftcObsTokenManager
     */
    public KftcObsApiClient(BaseHttpClient httpClient, KftcObsTokenManager kftcObsTokenManager) {
        super(httpClient);
        this.kftcObsTokenManager = kftcObsTokenManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        oauthInfo = new KftcObsAuthVo();
    }

    @ComponentOperation(name = "오픈뱅킹 API 클라이언트 초기화 처리")
    public KftcObsApiClient init() {
        validationOAuth();
        return this;
    }

    @ComponentOperation(name = "API토큰정보 획득 메소드")
    public KftcObsAuthVo getAuth() {
        return oauthInfo;
    }

    @ComponentOperation(name = "오픈뱅킹 랜덤 거래ID 생성")
    public String randomToBankTranId() {
        return String.format("%-10sU%s",
                new Object[] { oauthInfo.getClinetUseCode(), ObsUniqueUtils.randomIdByPidToString9() });
    }

    /**
     * 
     */
    @ComponentOperation(name = "오픈뱅킹 API 토큰 검증 요청")
    public void validationOAuth() {
        try {
            if (!oauthInfo.isAccessToken()) {
                oauthInfo.clone(this.kftcObsTokenManager.getToken());
            }
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException("KFTC-MA004", "연동 정보가 유효하지 않습니다.", e);
        }
    }

    @ComponentOperation(name = "오픈뱅킹 API 송/수신 처리")
    public ObsHttpResponseEntity execute(ObsHttpRequestEntity entity) {
        return this.executeCall(entity);
    }
}
