package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalApplySimpleRequest", description = "추가인증 - 간편 인증 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalApplySimpleRequest implements IMessageObject {

    @MessageField(id = "telecom", name = "통신사구분")
    private String telecom;

    @MessageField(id = "telNo", name = "전화번호")
    private String telNo;

    @MessageField(id = "custName", name = "고객명")
    private String custName;

    @MessageField(id = "perBusNo1", name = "주민등록번호 앞자리")
    private String perBusNo1;

    @MessageField(id = "perBusNo2", name = "주민등록번호 뒷자리")
    private String perBusNo2;

    @MessageField(id = "privacySharingAgreeYn", name = "개인정보 이용 동의 여부")
    private String privacySharingAgreeYn;

    @MessageField(id = "thirdPartyProvisionAgreeYn", name = "개인정보 제3자 제공 동의 여부")
    private String thirdPartyProvisionAgreeYn;

}
