package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalApplySMSIdentifyRequest", description = "추가인증 - SMS명의인증 실행 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalApplySMSIdentifyRequest implements IMessageObject {

    @MessageField(id = "mode", name = "명의인증모드(PASS, SMS)")
    private String mode;

    @MessageField(id = "custName", name = "고객명")
    private String custName;

    @MessageField(id = "perBusNo1", name = "주민등록번호 앞자리")
    private String perBusNo1;

    @MessageField(id = "perBusNo2", name = "주민등록번호 뒷자리")
    private String perBusNo2;

    @MessageField(id = "deptCode", name = "부서코드")
    private String deptCode;

    @MessageField(id = "pageStep", name = "화면스텝")
    private String pageStep;

    @MessageField(id = "telecom", name = "이동통신사 구분-(SKT:1,KT:2,LGU+:3,SKT알뜰폰:5,KT알뜰폰:6,LGU+알뜰폰:7)")
    private String telecom;

    @MessageField(id = "telNo", name = "휴대폰번호")
    private String telNo;

    @MessageField(id = "infoTelNo", name = "고객센터 전화번호")
    private String infoTelNo;

    @MessageField(id = "mertName", name = "가맹점명(SC제일은행)")
    private String mertName;

    @MessageField(id = "indentifyCheckYn", name = "실명확인유무")
    private String indentifyCheckYn;

    @MessageField(id = "authNumber", name = "인증번호")
    private String authNumber;

}
