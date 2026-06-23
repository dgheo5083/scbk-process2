package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 수수료 납부 취소
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeUniCertRefundRequest", type = Type.REQUEST)
public class CrtJctAuthorizeUniCertRefundRequest implements IMessageObject {

	@MessageField(id = "gjno", name = "수수료 입금계좌")
	private String gjno;

	@MessageField(id = "cmsCode", name = "CMS Code")
	private String cmsCode;

	@MessageField(id = "mDate", name = "납부일")
	private String mDate;

	@MessageField(id = "mSeq", name = "납부번호")
	private String mSeq;

	@MessageField(id = "ssr", name = "수수료 금액")
	private String ssr;

	@MessageField(id = "vat", name = "부가세 금액")
	private String vat;

	@MessageField(id = "caGubun", name = "발급기관")
	private String caGubun;

	@MessageField(id = "custGubun", name = "발급자구분")
	private String custGubun;

	@MessageField(id = "raGubun", name = "인증서 종류")
	private String raGubun;

}