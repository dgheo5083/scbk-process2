package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "SelectTmbObjUsrMgtInfoResult", type = Type.RESPONSE, description = "PUSH 가입 여부 조회 응답")
public class SelectTmbObjUsrMgtInfoResult {
    private String serno;

    private String operType;

    private String bnkingId;

    private String cnfrmNo;

    private String pushSrvcApprvlFlg;

    private String benefitFlag;

    private String financeFlag;

    private String wmloungeFlag;

    private String iotranlistFlag;

    private String notyExrateFlg;
}
