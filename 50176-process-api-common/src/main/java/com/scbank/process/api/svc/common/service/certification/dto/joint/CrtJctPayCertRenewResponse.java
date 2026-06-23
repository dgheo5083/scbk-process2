package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 갱신 수수료 납부 본거래
 */
@Data
@IntegrationMessage(id = "CrtJctPayCertRenewResponse", type = Type.RESPONSE)
public class CrtJctPayCertRenewResponse implements IMessageObject {

	@MessageField(id = "ssrDate", name = "납부일")
	private String ssrDate;

	@MessageField(id = "ssrTime", name = "납부시간")
	private String ssrTime;

	@MessageField(id = "ssrSecq", name = "접수번호")
	private String ssrSecq;

	@MessageField(id = "deptPersonName", name = "고객명")
	private String deptPersonName;

	@MessageField(id = "acctNum", name = "출금 계좌번호")
	private String acctNum;

	@MessageField(id = "ssrFee", name = "수수료")
	private String ssrFee;

	@MessageField(id = "ssrVat", name = "부가가치세")
	private String ssrVat;

	@MessageField(id = "serial", name = "인증서 serialNumber")
	private String serial;

	@MessageField(id = "certIndex", name = "인증서 index")
	private String certIndex;

	@MessageField(id = "encData", name = "인증서 비밀번호")
	private String encData;

	@MessageField(id = "yesSignCaIp", name = "금결원CA IP")
	private String yesSignCaIp;

	@MessageField(id = "yesSignCaPort", name = "금결원CA PORT")
	private String yesSignCaPort;

}