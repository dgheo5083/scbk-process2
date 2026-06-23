package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankApiLogInsertLogParameter;

@DaoComponent(database = "kfbdb", name = "오픈뱅킹 API 통신로그 DAO")
public interface OpenBankApiLogDao {

    /**
     * 오픈뱅킹 API 통신로그를 적재한다.
     * @param parameter {@link OpenBankApiLogInsertLogParameter}
     */
    @ComponentOperation(name = "오픈뱅킹 API 통신로그를 적재한다.")
    void insertLog(OpenBankApiLogInsertLogParameter parameter);
}