package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 요청 정보 클래스
 * 찾아가는뱅킹 신청
 */
@Data
@IntegrationMessage(id = "SupCscApplyPhoneConsultationRequest", type = Type.REQUEST)
public class SupCscApplyPhoneConsultationRequest implements IMessageObject {

    @MessageField(id = "callType", name = "callType")
    private String callType;
    @MessageField(id = "resDt", name = "resDt")
    private String resDt;
    @MessageField(id = "resTm", name = "resTm")
    private String resTm;
    @MessageField(id = "phoneNo", name = "phoneNo")
    private String phoneNo;
    @MessageField(id = "chnlDiv", name = "chnlDiv")
    private String chnlDiv;
    @MessageField(id = "grade", name = "grade")
    private String grade;
    @MessageField(id = "skill", name = "skill")
    private String skill;
    @MessageField(id = "host", name = "host")
    private String host;
    @MessageField(id = "prdId", name = "prdId")
    private String prdId;
    @MessageField(id = "userName", name = "userName")
    private String userName;
    @MessageField(id = "title", name = "title")
    private String title;

    // sms req
    @MessageField(id = "member", name = "member")
    private String member;
    @MessageField(id = "userCode", name = "userCode")
    private String userCode;
    @MessageField(id = "callPhone1", name = "callPhone1")
    private String callPhone1;
    @MessageField(id = "callPhone2", name = "callPhone2")
    private String callPhone2;
    @MessageField(id = "callPhone3", name = "callPhone3")
    private String callPhone3;
    @MessageField(id = "reqPhone1", name = "reqPhone1")
    private String reqPhone1;
    @MessageField(id = "deptName", name = "deptName")
    private String deptName;

}
