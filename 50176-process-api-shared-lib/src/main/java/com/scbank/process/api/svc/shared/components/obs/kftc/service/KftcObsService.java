package com.scbank.process.api.svc.shared.components.obs.kftc.service;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.obs.kftc.model.KftcObsAuthVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 오픈뱅킹 서비스 공유 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "금결원 오픈뱅킹 공유 컴포넌트")
public class KftcObsService {

    private final KftcObsTokenManager kftcObsTokenManager;

    /**
     * 금결원 오픈뱅킹 API토큰 획득
     * 
     * @return
     */
    @ComponentOperation(name = "금결원 오픈뱅킹 API토큰 획득")
    public KftcObsAuthVo getToken() {
        return kftcObsTokenManager.getToken();
    }
}
