package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.DeleteTransferPushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertTransferPushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdateTransferPushDataParameter;

@DaoComponent(id = "TmbBkstatMgtDao", database = "smsdb", description = "Push 입출금 내역", author = "김기주")
public interface TmbBkstatMgtDao {

    @ComponentOperation(name = "입출금내역 DB 등록 계좌 존재 유무 조회", description = "입출금내역 DB 등록 계좌 존재 유무 조회")
    int selectTransferPushAcctCnt(String bsAcctNo);

    @ComponentOperation(name = "Push 입출금 내역 서비스 정보 삭제", description = "Push 입출금 내역 서비스 정보 삭제")
    int deleteTransferPushData(DeleteTransferPushDataParameter parameter);

    @ComponentOperation(name = "기등록 계좌 유무에 따른 계좌 삭제", description = "기등록 계좌 유무에 따른 계좌 삭제")
    int deleteTransferPushDataByAcctNo(String bsAcctNo);

    @ComponentOperation(name = "Push 입출금내역 서비스 정보 등록", description = "Push 입출금내역 서비스 정보 등록")
    int insertTransferPushData(InsertTransferPushDataParameter parameter);

    @ComponentOperation(name = "Push 입출금내역 서비스 정보 등록", description = "Push 입출금내역 서비스 정보 등록")
    int updateTransferPushData(UpdateTransferPushDataParameter parameter);

}
