package com.scbank.process.api.svc.common.service.mydata.dto;

import java.util.List;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListCardOrganInfoResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드목록 조회 API")
public class ListCardOrganInfoResponse implements IMessageObject {

    @MessageField(id = "cardList", name = "카드목록")
    @RepeatedField
    private List<CardInfoDto> cardList;

    @MessageField(id = "rspCode", name = "응답코드")
    private String rspCode;

    @MessageField(id = "rspMsg", name = "응답메시지")
    private String rspMsg;

    @MessageField(id = "reqNo", name = "reqNo")
    private String reqNo;

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    @MessageField(id = "orgNm", name = "기관명")
    private String orgNm;

    @MessageField(id = "stlmtExpctdAmt", name = "결제예상금액")
    private String stlmtExpctdAmt;

    @MessageField(id = "stlmtExpctdDt", name = "결제예상일")
    private String stlmtExpctdDt;

    @MessageField(id = "bllngAmt", name = "bllngAmt")
    private String bllngAmt;

    @MessageField(id = "bllngYymm", name = "bllngYymm")
    private String bllngYymm;

    @MessageField(id = "blingDt", name = "blingDt")
    private String blingDt;

    @MessageField(id = "blingYn", name = "blingYn")
    private String blingYn;

    @MessageField(id = "pointYn", name = "pointYn")
    private String pointYn;

    @MessageField(id = "rvlvngYn", name = "rvlvngYn")
    private String rvlvngYn;

    @MessageField(id = "loanYn", name = "loanYn")
    private String loanYn;

    @MessageField(id = "lumpSumAmt", name = "lumpSumAmt")
    private String lumpSumAmt;

    @MessageField(id = "instllmntAmt", name = "instllmntAmt")
    private String instllmntAmt;

    @MessageField(id = "loanShortAmt", name = "loanShortAmt")
    private String loanShortAmt;

    @MessageField(id = "rvlvngAmt", name = "rvlvngAmt")
    private String rvlvngAmt;

    @MessageField(id = "loanLongAmt", name = "loanLongAmt")
    private String loanLongAmt;

    @MessageField(id = "etcAmt", name = "etcAmt")
    private String etcAmt;

    @MessageField(id = "isRtFlg", name = "새로고침 플레그 (새로고침 : Y , 초기 : N)")
    private String isRtFlg;

}
