package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * process 서비스 요청 정보 클래스
 * 비대면인증 고객관리 조회
 */
@Data
@IntegrationMessage(id = "NonFaceCustomerInfoInquiryResponse", type = Type.RESPONSE)
public class NonFaceCustomerInfoInquiryResponse {
	@MessageField(id = "ssn", name = "ssn")
    private String ssn;

    @MessageField(id = "custNo", name = "custNo")
    private String custNo;

    @MessageField(id = "mblphnNo", name = "mblphnNo")
    private String mblphnNo;

    @MessageField(id = "userNm", name = "userNm")
    private String userNm;

    @MessageField(id = "cmpndCheckKey", name = "cmpndCheckKey")
    private String cmpndCheckKey;

    @MessageField(id = "srcNewUserFlg", name = "srcNewUserFlg")
    private String srcNewUserFlg;

    @MessageField(id = "newUserFlg", name = "newUserFlg")
    private String newUserFlg;

    @MessageField(id = "srcDelObjFlg", name = "srcDelObjFlg")
    private String srcDelObjFlg;

    @MessageField(id = "delObjFlg", name = "delObjFlg")
    private String delObjFlg;

    @MessageField(id = "initCnnctnDt", name = "initCnnctnDt")
    private String initCnnctnDt;

    @MessageField(id = "lstCnnctnDt", name = "lstCnnctnDt")
    private String lstCnnctnDt;
}
