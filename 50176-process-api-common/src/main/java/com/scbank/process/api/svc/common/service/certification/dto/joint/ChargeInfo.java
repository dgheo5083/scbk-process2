package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ChargeInfo", type = Type.RESPONSE)
public class ChargeInfo implements IMessageObject {

	@MessageField(id = "chargeDate", name = "납부일자")
	private String chargeDate;

	@MessageField(id = "chargeMSeq", name = "접수번호")
	private String chargeMSeq;

	@MessageField(id = "chargeFee", name = "수수료 금액")
	private String chargeFee;

	@MessageField(id = "chargeVat", name = "부가세 금액")
	private String chargeVat;

	@MessageField(id = "chargeCaGubun", name = "발급기관")
	private String chargeCaGubun;

	@MessageField(id = "chargeCustGubun", name = "발급자구분")
	private String chargeCustGubun;

	@MessageField(id = "chargeRaGubun", name = "인증서 종류")
	private String chargeRaGubun;

	@MessageField(id = "juminSaupjaNo", name = "주민 사업자 번호")
	private String juminSaupjaNo;

	@MessageField(id = "chargeGjno", name = "수수료 납부 계좌")
	private String chargeGjno;

	@MessageField(id = "chargeCYubu", name = "취소 가능 여부(1:가능,2:불가)")
	private String chargeCYubu;

}