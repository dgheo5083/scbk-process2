package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgParameter;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgResult;

@DaoComponent(id = "TmbPushMsgDao", database = "smsdb", description = "PUSH 메세지", author = "김기주")
public interface TmbPushMsgDao {

    @ComponentOperation(name = "PUSH 메세지 조회", description = "PUSH 메세지 조회")
    List<ListPushMsgResult> listPushMsg(ListPushMsgParameter parameter);

}
