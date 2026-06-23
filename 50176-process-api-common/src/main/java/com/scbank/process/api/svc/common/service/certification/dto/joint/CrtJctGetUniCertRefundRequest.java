package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 수수료 납부 취소 대상 조회
 */
@Data
@IntegrationMessage(id = "CrtJctGetUniCertRefundRequest", type = Type.REQUEST)
public class CrtJctGetUniCertRefundRequest implements IMessageObject {

	@MessageField(id = "gjno", name = "수수료입금계좌")
	private String gjno;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;

}