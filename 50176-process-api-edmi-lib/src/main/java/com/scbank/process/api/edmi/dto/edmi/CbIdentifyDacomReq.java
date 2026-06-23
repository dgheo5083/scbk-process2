package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(
		id = "CbIdentifyDacomReq", 
		type = Type.REQUEST, 
		description = "휴대폰명의인증", 
		captureSystem = "VMS",
		typeName = "CoreBanking:mbOLTPCommonRoute",
		messageSenderBody = "MB",
		senderDomainBody = "CoreBanking")
public class CbIdentifyDacomReq implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "DEPTCODE", name = "부서코드", length = 12)
	private String deptCode;

	@MessageField(id = "OFFICECODE", name = "행번", length = 12)
	private String officeCode;

	@MessageField(id = "RESPCODE", name = "LG유플러스 결과코드", length = 4)
	private String respCode;

	@MessageField(id = "RESPMSG", name = "LG유플러스 결과메시지", length = 160)
	private String respMsg;

	@MessageField(id = "MOBILESTEP", name = "인증진행단계구분", length = 5)
	private String mobileStep;

	@MessageField(id = "TID", name = "LG유플러스 거래번호", length = 24)
	private String tId;

	@MessageField(id = "MOBILECOM", name = "이동통신사 구분", length = 1)
	private String mobileCom;

	@MessageField(id = "MOBILENUM", name = "휴대폰 번호", length = 12, masking = true, maskingType = "05")
	private String mobileNum;

	@MessageField(id = "MOBILESSN", name = "가입자 주민번호", length = 13, masking = true, maskingType = "01")
	private String mobileSsn;

	@MessageField(id = "AUTHMODE", name = "인증번호 SMS발송여부", length = 10)
	private String authMode;

	@MessageField(id = "AUTHNUMBER", name = "인증번호", length = 6)
	private String authNumber;

	@MessageField(id = "CALLBACK", name = "CALLBACK 번호", length = 12, defaultValue = "1588-1599")
	private String callback;

	@MessageField(id = "MERTNAME", name = "가맹점명", length = 20, defaultValue = "SC제일은행")
	private String mertName;

	@MessageField(id = "NAMECHECKYN", name = "실명확인유무", length = 1)
	private String nameCheckYn;

	@MessageField(id = "BUYER", name = "성명(가입자명)", length = 15)
	private String buyer;

	@MessageField(id = "BOHOSVCYN", name = "보호서비스 이용유무", length = 1)
	private String bohoSvcYn;

	@MessageField(id = "ISSUERCODE", name = "이통사 응답코드", length = 4)
	private String issuerCode;

	@MessageField(id = "ISSUERMSG", name = "이통사 응답메시지", length = 160)
	private String issuerMsg;
}
