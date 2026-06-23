package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "MdcMyDataPubServiceUtil")
public class MdcMyDataPubServiceUtil {

    /**
     * 마이데이터 인증키 관리자 컴포넌트
     */
    private final MdcMyDataAuthManager myDataAuthManager;

    @ComponentOperation(name = "getInternetToken")
    public void getInternetToken() {
        log.debug("MdcMyDataPubServiceUtil::getInternetToken");

        this.changeInternetToken();

        // 유효하지 않는경우 아래 인증키를 다시 받는다.
        log.info("인증키를 만료로 인하여 인증키 새로 발급합니다.");

    }

    @ComponentOperation(name = "changeInternetToken")
    public MdcMyDataAuthVo changeInternetToken() {
        return this.myDataAuthManager.changeInternetToken();
    }

    @ComponentOperation(name = "enableInternetToken")
    public void enableInternetToken(String tokenDate, MyDataHttpJsonObject result) {
        this.myDataAuthManager.enableInternetToken(tokenDate, result);
    }

    @ComponentOperation(name = "disableInternetToken")
    public void disableInternetToken() {
        this.myDataAuthManager.disableInternetToken();
    }
}
