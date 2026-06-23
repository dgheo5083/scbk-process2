package com.scbank.process.api.svc.common.service.certification.dto.universal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 범용인증서 발급 수수료 납부
 */
@Data
@IntegrationMessage(id = "CrtUctPayUniCertFeeRequest", type = Type.REQUEST)
public class CrtUctPayUniCertFeeRequest implements IMessageObject {

	@MessageField(id = "acctNum", name = "출금 계좌번호")
	private String acctNum;

	@MessageField(id = "feePayIssueYn", name = "계산서 발급 여부")
	private String feePayIssueYn;

	@MessageField(id = "typesOfIndustry", name = "업종")
	private String typesOfIndustry;

	@MessageField(id = "item", name = "종목")
	private String item;

	@MessageField(id = "chairManName", name = "대표자명")
	private String chairManName;

}