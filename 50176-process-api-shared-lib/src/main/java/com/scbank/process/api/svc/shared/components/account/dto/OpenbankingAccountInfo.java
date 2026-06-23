package com.scbank.process.api.svc.shared.components.account.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "OpenbankingAccountInfo", type = Type.RESPONSE, description = "오픈뱅킹 계좌목록조회 응답")
public class OpenbankingAccountInfo implements IMessageObject {

    @MessageField(id = "bankCd", name = "")
    private String bankCd;

    @MessageField(id = "bankNm", name = "")
    private String bankNm;

    @MessageField(id = "bankImg", name = "")
    private String bankImg;

    @MessageField(id = "agrSts", name = "")
    private String agrSts;

    @MessageField(id = "fintechUseNum", name = "")
    private String fintechUseNum;

    @MessageField(id = "payerNum", name = "")
    private String payerNum;

    @MessageField(id = "fcode", name = "")
    private String fcode;

    @MessageField(id = "acctNo", name = "")
    private String acctNo;

    @MessageField(id = "acctNm", name = "")
    private String acctNm;

    @MessageField(id = "acctComment", name = "")
    private String acctComment;

    @MessageField(id = "updateTime", name = "")
    private String updateTime;

    @MessageField(id = "registTime", name = "")
    private String registTime;

    @MessageField(id = "agreeExpireFlag", name = "")
    private String agreeExpireFlag;

    @MessageField(id = "agreeExpireDate", name = "")
    private String agreeExpireDate;
}
