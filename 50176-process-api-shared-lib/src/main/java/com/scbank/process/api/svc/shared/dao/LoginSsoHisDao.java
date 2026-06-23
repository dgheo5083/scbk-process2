package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.DuplicationLoginCheckInfoParameter;

@DaoComponent(database = "kfbdb", description = "중복로그인 체크정보", author = "2034263")
public interface LoginSsoHisDao {

    @ComponentOperation(name = "중복로그인 체크정보 조회", description = "중복로그인 체크정보 조회")
    Integer selectDuplicationLoginCheckInfo(DuplicationLoginCheckInfoParameter parameter);

}
