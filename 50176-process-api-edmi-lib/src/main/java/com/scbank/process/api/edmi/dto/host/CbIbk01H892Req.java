package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H892Req", type = Type.REQUEST, captureSystem = "OLTP", description = "아이디찾기 요청 전문")
public class CbIbk01H892Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "E2ERegNum", name = "주민사업자번호", length = 13, masking = true, maskingType = "01")
	private String E2ERegNum; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "ActType", name = "처리구분. 1:고객정보조회(예비처리), 2:계좌검증(본처리), 3:계좌미검증(본처리)", length = 1)
	private String ActType;

	@MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02")
	private String AcctNum;

	@MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
	private String AcctPassword; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

}
