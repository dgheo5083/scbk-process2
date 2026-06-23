package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "RiskCddRegisterMbRes", type = Type.RESPONSE, description = "KCDD고객자동화 처리 접수등록 응답부[AS-IS EDMI_RISK_CddRegister_MB]")
public class RiskCddRegisterMbRes implements IMessageObject {
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
	
	@MessageField(id = "RSPNS_CD", name = "응답코드(0000:정상,9999:에러)", length = 4)
	private String RSPNS_CD;
	
	@MessageField(id = "RSPNS_DATA", name = "응답메시지", length = 100)
	private String RSPNS_DATA;
	
	@MessageField(id = "PRGRSS_STS_CD", name = "처리상태코드", length = 3)
	private String PRGRSS_STS_CD;
}
