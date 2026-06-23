package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "InsertTmbPushMsgCamp01Parameter", type = Type.REQUEST, description = "PUSH 발송 등록")
public class InsertTmbPushMsgCamp01Parameter {
    private String campSeq;
    private String pushType;
    private String pushToken;
    private String msgType;
    private String recvYN;
    private String resvDt;
    private String retryCnt;
    private String testYN;
    private String stsCd;
    private String regId;
    private String kind;
    private String sendDepartment;
    private String bnkingId;
    private String bandSeqNo;
}
