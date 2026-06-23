package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.InsertUserPushAgreeParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectPushAgreeInfoResult;

@DaoComponent(id = "Ma30SedMsgAgrmntDao", database = "kfbdb", description = "푸시 동의 여부", author = "김기주")
public interface Ma30SedMsgAgrmntDao {

    @ComponentOperation(name = "푸시 동의 여부 조회", description = "푸시 동의 여부 조회")
    List<SelectPushAgreeInfoResult> selectPushAgreeInfo(String bankId);

    @ComponentOperation(name = "푸시 동의 여부 등록/수정", description = "푸시 동의 여부 등록/수정")
    int insertUserPushAgree(InsertUserPushAgreeParameter parameter);

}
