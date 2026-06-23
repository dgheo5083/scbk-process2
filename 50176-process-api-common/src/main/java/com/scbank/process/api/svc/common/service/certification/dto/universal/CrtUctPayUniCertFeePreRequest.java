package com.scbank.process.api.svc.common.service.certification.dto.universal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 범용인증서 발급 수수료 납부 예비거래
 */
@Data
@IntegrationMessage(id = "CrtUctPayUniCertFeePreRequest", type = Type.REQUEST)
public class CrtUctPayUniCertFeePreRequest implements IMessageObject {
	
	@MessageField(id = "payGubun", name = "수수료 납부 구분(1:당행계좌인출, 2:한국정보인증결제)")
	private String payGubun;

	@MessageField(id = "acctNum", name = "출금 계좌번호")
	private String acctNum;

	@MessageField(id = "acctBb", name = "계좌비밀번호")
	private String acctBb;

}