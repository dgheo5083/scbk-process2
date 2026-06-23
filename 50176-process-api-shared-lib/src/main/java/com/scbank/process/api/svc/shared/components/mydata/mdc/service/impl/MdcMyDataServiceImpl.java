package com.scbank.process.api.svc.shared.components.mydata.mdc.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataAuthManager;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataService;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "마이데이터 서비스 공유 컴포넌트", description = "마이데이터 토큰 조회/발급/폐기/갱신 처리")
public class MdcMyDataServiceImpl implements MdcMyDataService {

    private final MdcMyDataAuthManager myDataAuthManager;

    @ComponentOperation(name = "마이데이터 인증키 획득 및 신규 발급")
    @Override
    public MdcMyDataAuthVo getToken() {
        return this.myDataAuthManager.getToken();
    }

    @ComponentOperation(name = "마이데이터 인증키 DB 조회")
    @Override
    public MdcMyDataAuthVo getSelectToken() {
        return this.myDataAuthManager.getSelectToken();
    }

    @ComponentOperation(name = "마이데이터 인증키 변경")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    @Override
    public MdcMyDataAuthVo changeToken() {
        return this.myDataAuthManager.changeToken();
    }

    @ComponentOperation(name = "마이데이터 인증키 활성화")
    @Override
    public void enableToken(String tokenDate, MyDataHttpJsonObject result) {
        this.myDataAuthManager.enableToken(tokenDate, result);
    }

    @ComponentOperation(name = "마이데이터 인증키 폐기")
    @Override
    public void disableToken() {
        this.myDataAuthManager.disableToken();
    }
}
