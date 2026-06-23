package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03H81800Req", type = Type.REQUEST, captureSystem = "OLTP", description = "타기관 OTP 등록")
public class CbTbs03H81800Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "YIGUBUN", name = "거래구분", length = 1)
	private String YIGUBUN;

	@MessageField(id = "CgAcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02")
	private String CgAcctNum;

	@MessageField(id = "CgAcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String CgAcctPassword; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "YIVDCD", name = "벤더코드", length = 3)
	private String YIVDCD;
	
	@MessageField(id = "YIOTPNO", name = "OTP일련번호", length = 12, masking = true, maskingType = "01")
	private String YIOTPNO;
	
	@MessageField(id = "YIDUMMY", name = "OTP더미", length = 20)
	private String YIDUMMY;
	
}