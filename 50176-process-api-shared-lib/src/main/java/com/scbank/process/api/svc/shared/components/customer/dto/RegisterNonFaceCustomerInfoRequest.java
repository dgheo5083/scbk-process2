package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterNonFaceCustomerInfoRequest", type = Type.REQUEST)
public class RegisterNonFaceCustomerInfoRequest {

	@MessageField(id = "ssn", name = "주민번호")
	private String ssn;
	@MessageField(id = "custNo", name = "고객번호")
    private String custNo;
    @MessageField(id = "mblphnNo", name = "휴대폰번호")
    private String mblphnNo;
    @MessageField(id = "userNm", name = "사용자명")
    private String userNm;
    @MessageField(id = "cmpndCheckKey", name = "중복체크키")
    private String cmpndCheckKey;
    @MessageField(id = "srcNewUserFlg", name = "원천신규고객여부")
    private String srcNewUserFlg;
    @MessageField(id = "newUserFlg", name = "신규고객여부")
    private String newUserFlg;
    @MessageField(id = "srcDelObjFlg", name = "원천파기해제여부")
    private String srcDelObjFlg;
    @MessageField(id = "delObjFlg", name = "파기해제여부")
    private String delObjFlg;
    @MessageField(id = "initCnnctnDt", name = "최조접속일")
    private String initCnnctnDt;
    @MessageField(id = "lstCnnctnDt", name = "최종접속일")
    private String lstCnnctnDt;
}
