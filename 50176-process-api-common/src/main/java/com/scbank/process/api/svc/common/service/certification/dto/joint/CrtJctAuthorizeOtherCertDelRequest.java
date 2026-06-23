package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 타기관 해제
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeOtherCertDelRequest", type = Type.REQUEST)
public class CrtJctAuthorizeOtherCertDelRequest implements IMessageObject {

	@MessageField(id = "tranType", name = "거래타입(1:HOST, 2:LDAP, 3:INFO)")
	private String tranType;

	@MessageField(id = "certSerial", name = "인증서 시리얼 넘버")
	private String certSerial;

	@MessageField(id = "certPolicyCode", name = "인증정책 식별코드")
	private String certPolicyCode;

	@MessageField(id = "oid", name = "인증서 OID")
	private String oid;

	@MessageField(id = "caGubun", name = "발급기관")
	private String caGubun;

	@MessageField(id = "safeCardNum", name = "안전카드값")
	private String safeCardNum;

	@MessageField(id = "safeCardNum2", name = "안전카드값2")
	private String safeCardNum2;

}