package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbSms01S00200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "완료시 SMS발송 요청 전문")
public class CbSms01S00200Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "CustomGB", name = "전문종별 IB1IB비정형", length = 3)
	private String CustomGB;

	@MessageField(id = "TransGB", name = "거래구분 001공인인증서(재발급)  002고객정보관리변경  003고객휴대폰번호변경  004고객이메일변경  005이체한도변경(감액) 006출금계좌 등록 007출금계좌 해지", length = 3)
	private String TransGB;

	@MessageField(id = "JuminNumgb", name = "주민번호구분. 값없음:개인,개인사업자 2:사업자", length = 1)
	private String JuminNumgb;

	@MessageField(id = "PerBusNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String PerBusNo; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "JoribGB", name = "사용자 메세지 조립YN", length = 1)
	private String JoribGB; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "JoribMsgHs", name = "JoribMsgHs", length = 1)
	private String JoribMsgHs;

	@MessageField(id = "JoribMsg", name = "사용자 조립메세지", length = 80)
	private String JoribMsg;

	@MessageField(id = "JoribMsgHe", name = "JoribMsgHe", length = 1)
	private String JoribMsgHe;

	@MessageField(id = "Dummy", name = "Dummy", length = 61)
	private String Dummy;

}
