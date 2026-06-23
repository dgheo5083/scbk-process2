package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 갱신 수수료 납부 본거래
 */
@Data
@IntegrationMessage(id = "CrtJctPayCertRenewRequest", type = Type.REQUEST)
public class CrtJctPayCertRenewRequest implements IMessageObject {

	@MessageField(id = "acctNum", name = "출금 계좌번호")
	private String acctNum;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;

	@MessageField(id = "tel1", name = "전화번호1")
	private String tel1;

	@MessageField(id = "tel2", name = "전화번호2")
	private String tel2;

	@MessageField(id = "tel3", name = "전화번호3")
	private String tel3;

}