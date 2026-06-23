package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 인증서 효력정지 계좌 검증
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertSuspensionAcctRequest", type = Type.REQUEST)
public class CrtJctValidateCertSuspensionAcctRequest implements IMessageObject {

	@MessageField(id = "acctNum", name = "계좌번호")
	private String acctNum;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;

}