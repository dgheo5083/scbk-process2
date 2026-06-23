package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 타행금융인증서 해제
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeOtherFincertDelRequest", type = Type.REQUEST)
public class CrtFctAuthorizeOtherFincertDelRequest implements IMessageObject {

	@MessageField(id = "safeCardNum", name = "안전카드값")
	private String safeCardNum;

	@MessageField(id = "safeCardNum2", name = "안전카드값2")
	private String safeCardNum2;

	@MessageField(id = "safeCardSeq1", name = "안전카드일련번호 사용자입력값1")
	private String safeCardSeq1;

	@MessageField(id = "safeCardSeq2", name = "안전카드일련번호 사용자입력값2")
	private String safeCardSeq2;

	@MessageField(id = "safeCardSeq3", name = "안전카드일련번호 사용자입력값3")
	private String safeCardSeq3;
	
	@MessageField(id = "tranType", name = "1:HOST, 2:LDAP, 3:INFO")
    private String tranType;
	
	@MessageField(id = "certserial", name = "인증서 일련번호")
	private String certserial;
	
	@MessageField(id = "certPolicyCode", name = "certPolicyCode")
	private String certPolicyCode;
}