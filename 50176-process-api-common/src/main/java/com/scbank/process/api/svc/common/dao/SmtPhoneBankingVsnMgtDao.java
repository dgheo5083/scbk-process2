package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.SmtPhoneBankingInfoResult;

@DaoComponent(database = "kfbdb", description = "APP 버전 관리", author = "951301")
public interface SmtPhoneBankingVsnMgtDao {

    @ComponentOperation(name = "APP 최신 버전 정보 가져오기", description = "APP 최신 버전 정보 가져오기")
    SmtPhoneBankingInfoResult selectLatestAppVersion(String mblBankingType);
}
