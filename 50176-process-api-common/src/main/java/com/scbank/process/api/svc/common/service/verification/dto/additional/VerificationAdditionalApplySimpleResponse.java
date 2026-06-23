package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalApplySimpleResponse", description = "추가인증 - 간편 인증 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalApplySimpleResponse implements IMessageObject {

    @MessageField(id = "authYn", name = "인증여부")
    private String authYn;

    @MessageField(id = "resultCode", name = "응답결과코드")
    private String resultCode;

    @MessageField(id = "resultMsg", name = "응답결과메시지")
    private String resultMsg;

    @MessageField(id = "yohumgb", name = "요구불외화휴면계좌YN")
    private String yohumgb;

    @MessageField(id = "changeSmsAuthYn", name = "SMS 명의인증변환여부 - 디지털인증서 발급시에 법인폰 사용자")
    private String changeSmsAuthYn;

}
