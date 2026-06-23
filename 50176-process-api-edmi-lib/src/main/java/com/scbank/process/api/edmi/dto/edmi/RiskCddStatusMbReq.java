package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(
		id = "RiskCddStatusMbReq", 
		type = Type.REQUEST, 
		description = "KCDD고객자동화 처리 상태조회 요청부[AS-IS EDMI_RISK_CddStatus_MB]",
		captureSystem = "KAIS", 
		typeName = "Risk:mbcommonroute",
		messageSenderBody = "MB",
		senderDomainBody = "Risk")
public class RiskCddStatusMbReq implements IMessageObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "CIFNO", name = "고객번호(CIF)", length = 13)
	private String CIFNO;
	
	@MessageField(id = "CUST_NO", name = "NF고객번호", length = 13)
	private String CUST_NO;
	
	@MessageField(id = "TRAD_NO", name = "NF거래번호", length = 13)
	private String TRAD_NO;
}
