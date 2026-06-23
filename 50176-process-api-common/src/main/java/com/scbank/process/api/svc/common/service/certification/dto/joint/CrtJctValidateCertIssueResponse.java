package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 발급 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertIssueResponse", type = Type.RESPONSE)
public class CrtJctValidateCertIssueResponse implements IMessageObject {

	@MessageField(id = "combPopType", name = "팝업타입")
	private String combPopType;

	@MessageField(id = "yoAGREEGB", name = "온라인 발급 사전동의여부")
	private String yoAGREEGB;

	@MessageField(id = "yoLRSOTGB", name = "개인정보노출구분 값 =1 차단")
	private String yoLRSOTGB;

	@MessageField(id = "isFinancial", name = "금결원 범용 재발급 가능 여부 (0:불가능, 1:가능)")
	private String isFinancial;

	@MessageField(id = "email1", name = "이메일1")
	private String email1;

	@MessageField(id = "email2", name = "이메일2")
	private String email2;

	@MessageField(id = "custName", name = "고객명")
	private String custName;

	@MessageField(id = "hpOne", name = "전화번호")
	private String hpOne;

	@MessageField(id = "hpTwo", name = "전화번호")
	private String hpTwo;

	@MessageField(id = "hpThree", name = "전화번호")
	private String hpThree;

	@MessageField(id = "phoneNo", name = "전화번호")
	private String phoneNo;

}