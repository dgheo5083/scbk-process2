package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 신청내역 조회
 */
@Data
@IntegrationMessage(id = "SupDocListApplyRequest", type = Type.REQUEST)
public class SupDocListApplyRequest implements IMessageObject {

    @MessageField(id = "acctNum", name = "계좌번호")
    private String acctNum;

    @MessageField(id = "assort", name = "종별코드")
    private String assort;

    @MessageField(id = "yiGRGBN", name = "yiGRGBN")
    private String yiGRGBN;

    @MessageField(id = "balance", name = "대출잔액(전계좌 잔액으로 변경)")
    private String balance;

    @MessageField(id = "loanRepayPrinAmt", name = "승인한도")
    private String loanRepayPrinAmt;

    @MessageField(id = "savingStartDate", name = "신규일")
    private String savingStartDate;

    @MessageField(id = "requestURLCheck", name = "requestURLCheck")
    private String requestURLCheck;

}
