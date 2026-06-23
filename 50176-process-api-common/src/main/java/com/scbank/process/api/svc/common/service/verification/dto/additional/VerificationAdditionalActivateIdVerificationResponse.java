package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalActivateIdVerificationResponse", description = "추가인증 - FDS 출금정지 비대면실명인증 초기화 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalActivateIdVerificationResponse implements IMessageObject {

    @MessageField(id = "custNo", name = "고객번호")
    private String custNo;

    @MessageField(id = "tradNo", name = "거래번호")
    private String tradNo;

    @MessageField(id = "bizType", name = "bizType")
    private String bizType;

    @MessageField(id = "prdctCd", name = "상품코드")
    private String prdctCd;

    @MessageField(id = "prdctNm", name = "상품명")
    private String prdctNm;

    @MessageField(id = "brnchNo", name = "영업점번호")
    private String brnchNo;
}
