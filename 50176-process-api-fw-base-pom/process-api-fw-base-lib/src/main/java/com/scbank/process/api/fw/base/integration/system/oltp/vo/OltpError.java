package com.scbank.process.api.fw.base.integration.system.oltp.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 호스트 오류 메시지부
 */
@Data
@IntegrationMessage(id = "hostError")
public class OltpError implements IMessageObject {

	private static final long serialVersionUID = 1L;

	@MessageField(id = "userNum", name = "이용자번호", length = 10)
	private String userNum;

	@MessageField(id = "sHostErrorModule", length = 8)
	private String sHostErrorModule;

	@MessageField(id = "connectCancelYN", name = "접속해제여부", length = 1)
	private String connectCancelYN;

	@MessageField(id = "ErrMsg1", length = 74, multiBytes = true)
	private String ErrMsg1;

	@MessageField(id = "ErrMsg2", length = 78, multiBytes = true)
	private String ErrMsg2;

	@MessageField(id = "WrnMsg1", length = 74, multiBytes = true)
	private String WrnMsg1;

	@MessageField(id = "WrnMsg2", length = 74, multiBytes = true)
	private String WrnMsg2;

	@MessageField(id = "WrnMsg3", length = 74, multiBytes = true)
	private String WrnMsg3;

	@MessageField(id = "otherBankGouNum", name = "타행이체고유번호", length = 12)
	private String otherBankGouNum;

	@MessageField(id = "otherBankResCd", name = "타행응답코드", length = 4)
	private String otherBankResCd;

	@MessageField(id = "categoryKey", name = "업무키", length = 30)
	private String categoryKey;
}
