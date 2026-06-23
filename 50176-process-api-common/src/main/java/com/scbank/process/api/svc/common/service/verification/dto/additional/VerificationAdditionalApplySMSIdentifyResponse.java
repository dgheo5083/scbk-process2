package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalApplySMSIdentifyResponse", description = "추가인증 - SMS명의인증 실행 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalApplySMSIdentifyResponse implements IMessageObject {

    @MessageField(id = "resultYn", name = "응답결과여부", defaultValue = "N")
    private String resultYn;

    @MessageField(id = "minorYn", name = "미성년자여부", defaultValue = "N")
    private String minorYn;

    @MessageField(id = "foreignerYn", name = "외국인여부", defaultValue = "N")
    private String foreignerYn;

    @MessageField(id = "maskPerBusNo", name = "주민번호 마스킹(000000-0******)")
    private String maskPerBusNo;

    @MessageField(id = "bohoSvcYn", name = "보호서비스 이용유무")
    private String bohoSvcYn;

    @MessageField(id = "kcbCiErrYn", name = "kcbCiErrYn", defaultValue = "N")
    private String kcbCiErrYn;
    @MessageField(id = "nameErrYn", name = "nameErrYn", defaultValue = "N")
    private String nameErrYn;
    @MessageField(id = "ciErrYn", name = "ciErrYn", defaultValue = "N")
    private String ciErrYn;
}
