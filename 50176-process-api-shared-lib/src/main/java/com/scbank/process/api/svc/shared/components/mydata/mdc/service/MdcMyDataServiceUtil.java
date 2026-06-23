package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SharedComponent(name = "마이데이터 서비스 유틸리티 공유 컴포넌트")
public class MdcMyDataServiceUtil {

    private final MdcMyDataAuthManager myDataAuthManager;

    @ComponentOperation(name = "마이데이터 인증키 획득 및 신규 발급")
    public MdcMyDataAuthVo getToken() {
        return this.myDataAuthManager.getToken();
    }

    @ComponentOperation(name = "마이데이터 인증키 변경")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    public MdcMyDataAuthVo changeToken() {
        return this.myDataAuthManager.changeToken();
    }

    @ComponentOperation(name = "마이데이터 인증키 활성화")
    public void enableToken(String tokenDate, MyDataHttpJsonObject result) {
        this.myDataAuthManager.enableToken(tokenDate, result);
    }

    @ComponentOperation(name = "마이데이터 인증키 폐기")
    public void disableToken() {
        this.myDataAuthManager.disableToken();
    }
}
