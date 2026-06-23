package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.SignInfoParameter;

@DaoComponent(database = "kfbdb", description = "SignDao", author = "951303")
public interface SignTransactionDao {

    @ComponentOperation(name = "전자서명 저장", description = "전자서명 저장")
    void insertSignInfo(SignInfoParameter parameter);
    
}
