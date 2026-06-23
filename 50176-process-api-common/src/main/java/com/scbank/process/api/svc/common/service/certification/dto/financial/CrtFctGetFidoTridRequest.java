package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 생체인증 TRID 조회
 */
@Data
@IntegrationMessage(id = "CrtFctGetFidoTridRequest", type = Type.REQUEST)
public class CrtFctGetFidoTridRequest implements IMessageObject {

	@MessageField(id = "fidoSvctrId", name = "비어있으면 신규 채번됨", defaultValue = "")
	private String fidoSvctrId;

	@MessageField(id = "command", name = "requestServiceAuth 고정")
	private String command;

	@MessageField(id = "verifyType", name = "verifyType")
	private String verifyType;

	@MessageField(id = "fidoAppId", name = "fidoAppId")
	private String fidoAppId;

	@MessageField(id = "fidoDeviceid", name = "fidoDeviceid")
	private String fidoDeviceid;

}