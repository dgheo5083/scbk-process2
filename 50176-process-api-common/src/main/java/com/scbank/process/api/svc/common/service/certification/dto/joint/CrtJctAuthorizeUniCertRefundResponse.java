package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 수수료 납부 취소
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeUniCertRefundResponse", type = Type.RESPONSE)
public class CrtJctAuthorizeUniCertRefundResponse implements IMessageObject {

	@MessageField(id = "cancelDate", name = "취소 완료 일자")
	private String cancelDate;

	@MessageField(id = "cancelTime", name = "취소 완료 시간")
	private String cancelTime;

	@MessageField(id = "deptPersonName", name = "예금주")
	private String deptPersonName;

	@MessageField(id = "cancelSsr", name = "취소 수수료 금액")
	private String cancelSsr;

	@MessageField(id = "cancelVat", name = "취소 부가세 금액")
	private String cancelVat;

	@MessageField(id = "cancelGjno", name = "취소 수수료 입금계좌")
	private String cancelGjno;

	@MessageField(id = "cancelCms", name = "CMS Code")
	private String cancelCms;

	@MessageField(id = "cancelOper", name = "취소 완료 조작자")
	private String cancelOper;
	
	@MessageField(id = "resMsg", name = "인증서 폐기 결과")
	private String resMsg;

}