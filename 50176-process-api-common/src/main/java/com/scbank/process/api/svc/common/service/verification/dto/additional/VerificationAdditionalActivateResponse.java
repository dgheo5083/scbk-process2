package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalActivateResponse", description = "추가인증 - 초기화 응답 DTO", type = Type.RESPONSE)
public class VerificationAdditionalActivateResponse implements IMessageObject {

    @MessageField(id = "wasTranNo", name = "WAS 거래번호")
    private String wasTranNo;

    @MessageField(id = "additionalType", name = "인증구분(A:간편인증, B:SMS명의인증, D:ARS인증, E:SMS인증(삭제), F:해외출국여부조회, G:공인인증서)")
    private String additionalType;

    @MessageField(id = "transType", name = "", defaultValue = "")
    private String transType;

    @MessageField(id = "phoneNo", name = "핸드폰번호(000-0000-0000)")
    private String phoneNo;
    @MessageField(id = "maskPhoneNo", name = "마스킹 핸드폰번호(000-0000-****)")
    private String maskPhoneNo;
    @MessageField(id = "phoneNo1", name = "핸드폰번호 첫번째자리")
    private String phoneNo1;
    @MessageField(id = "phoneNo2", name = "핸드폰번호 두번째자리")
    private String phoneNo2;
    @MessageField(id = "phoneNo3", name = "핸드폰번호 세번째자리")
    private String phoneNo3;

    @MessageField(id = "homeTelNo", name = "집전화번호(000-0000-0000)")
    private String homeTelNo;
    @MessageField(id = "maskHomeTelNo", name = "마스킹 집전화번호(000-0000-****)")
    private String maskHomeTelNo;
    @MessageField(id = "homeTelNo1", name = "집전화번호 첫번째자리")
    private String homeTelNo1;
    @MessageField(id = "homeTelNo2", name = "집전화번호 두번째자리")
    private String homeTelNo2;
    @MessageField(id = "homeTelNo3", name = "집전화번호 세번째자리")
    private String homeTelNo3;

    @MessageField(id = "jobTelNo", name = "직장전화번호(000-0000-0000)")
    private String jobTelNo;
    @MessageField(id = "maskJobTelNo", name = "마스킹 직장전화번호(000-0000-****)")
    private String maskJobTelNo;
    @MessageField(id = "jobTelNo1", name = "직장전화번호 첫번째자리")
    private String jobTelNo1;
    @MessageField(id = "jobTelNo2", name = "직장전화번호 두번째자리")
    private String jobTelNo2;
    @MessageField(id = "jobTelNo3", name = "직장전화번호 세번째자리")
    private String jobTelNo3;

}
