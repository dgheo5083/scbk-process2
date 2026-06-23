package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.DeleteAllExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.DeleteExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectExratePushDataResult;
import com.scbank.process.api.svc.common.dao.dto.UpdateExratePushDataParameter;

@DaoComponent(id = "TmbExrateMgtDao", database = "smsdb", description = "환율 PUSH", author = "김기주")
public interface TmbExrateMgtDao {

    @ComponentOperation(name = "PUSH 등록된 환율 정보 전체 삭제", description = "PUSH 등록된 환율 정보 전체 삭제")
    int deleteAllExratePushData(DeleteAllExratePushDataParameter parameter);

    @ComponentOperation(name = "PUSH 고객 환율 등록 정보 조회", description = "PUSH 고객 환율 등록 정보 조회")
    List<SelectExratePushDataResult> selectExratePushData(String serno);

    @ComponentOperation(name = "PUSH 등록된 환율 UPDATE", description = "PUSH 등록된 환율 UPDATE")
    int updateExratePushData(UpdateExratePushDataParameter parameter);

    @ComponentOperation(name = "PUSH 환율 등록 INSERT", description = "PUSH 환율 등록 INSERT")
    int insertExratePushData(InsertExratePushDataParameter parameter);

    @ComponentOperation(name = "PUSH 등록된 환율 삭제 DELETE", description = "PUSH 등록된 환율 삭제 DELETE")
    int deleteExratePushData(DeleteExratePushDataParameter parameter);

}
