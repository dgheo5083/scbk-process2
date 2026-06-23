package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.InsertTmbCampContents01Parameter;
import com.scbank.process.api.svc.common.dao.dto.InsertTmbPushMsgCamp01Parameter;
import com.scbank.process.api.svc.common.dao.dto.SelectTmbObjUsrMgtInfoResult;

@DaoComponent(id = "TmbCampContentDao", database = "smsdb", description = "MOTP 푸시메세지 등록")
public interface TmbCampContentDao {
    /*
     * @param
     * 
     * @return PUSH 가입 여부 조회
     */
    @ComponentOperation(name = "PUSH 가입 여부 조회", description = "PUSH 가입 여부 조회")
    SelectTmbObjUsrMgtInfoResult selectTmbObjUsrMgtInfo(String bnkingId);

    /*
     * @param
     * 
     * @return Push Content 시퀀스 조회
     */
    @ComponentOperation(name = "Push Content 시퀀스 조회", description = "Push Content 시퀀스 조회")
    String selectTmbCampContentSeq();

    /*
     * @param
     * 
     * @return PUSH Message 등록
     */
    @ComponentOperation(name = "PUSH Message 등록", description = "PUSH Message 등록")
    void insertTmbCampContents01(InsertTmbCampContents01Parameter input);

    /*
     * @param
     * 
     * @return PUSH 발송 등록
     */
    @ComponentOperation(name = "PUSH 발송 등록", description = "PUSH 발송 등록")
    void insertTmbPushmsgCamp01(InsertTmbPushMsgCamp01Parameter input);

}
