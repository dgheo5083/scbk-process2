package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.SignInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.SignInfoResult;

@DaoComponent(database = "kfbdb", description = "SignDao", author = "951303")
public interface SignTransactionDao {

    @ComponentOperation(name = "전자서명 저장", description = "전자서명 저장")
    void insertSignInfo(SignInfoResult parameter);

    @ComponentOperation(name = "전자서명 조회", description = "전자서명 조회")
    List<SignInfoResult> selectSignInfo(SignInfoParameter parameter);

    @ComponentOperation(name = "테스트", description = "테스트용다건전자서명")
    SignInfoResult testMultiSign();
}
