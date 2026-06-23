package com.scbank.process.api.svc.common.service.mydata.dto;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "OrgCardInfoDto", type = Type.RESPONSE, description = "마이데이터 > 현대카드 기관 정보 OrgCardInfoDto")
public class OrgCardInfoDto implements IMessageObject {

    @MessageField(id = "blingYn", name = "")
    private String blingYn;

    @MessageField(id = "inTimeStamp", name = "")
    private String inTimeStamp;

    @MessageField(id = "rspCode", name = "")
    private String rspCode;

    @MessageField(id = "rspMsg", name = "")
    private String rspMsg;

    @MessageField(id = "orgCd", name = "")
    private String orgCd;

    @MessageField(id = "orgNm", name = "")
    private String orgNm;

    @MessageField(id = "orgCardCnt", name = "")
    private String orgCardCnt;

    @MessageField(id = "stlmtExpctdAmt", name = "")
    private BigDecimal stlmtExpctdAmt;

    @MessageField(id = "lumpSumAmt", name = "")
    private String lumpSumAmt;

    @MessageField(id = "instllmntAmt", name = "")
    private String instllmntAmt;

    @MessageField(id = "loanShortAmt", name = "")
    private String loanShortAmt;

    @MessageField(id = "rvlvngAmt", name = "")
    private String rvlvngAmt;

    @MessageField(id = "loanLongAmt", name = "")
    private String loanLongAmt;

    @MessageField(id = "etcAmt", name = "")
    private String etcAmt;

    @MessageField(id = "stlmtExpctdDt", name = "")
    private String stlmtExpctdDt;

    @MessageField(id = "reqNo", name = "")
    private String reqNo;

    @MessageField(id = "loanTermGb", name = "")
    private String loanTermGb;

    @MessageField(id = "rvlvngFlg", name = "")
    private String rvlvngFlg;

    @MessageField(id = "updateTime", name = "updateTime")
    private String updateTime;

}
