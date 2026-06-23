package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 타기관인증서 조회
 */
@Data
@IntegrationMessage(id = "CrtJctGetOtherCertStatusResponse", type = Type.RESPONSE)
public class CrtJctGetOtherCertStatusResponse implements IMessageObject {

	@MessageField(id = "errorYn", name = "에러여부")
	private String errorYn;

	@MessageField(id = "errorCode", name = "에러코드")
	private String errorCode;

	@MessageField(id = "opprRes", name = "인증서 상태 조회 값")
	private String opprRes;

	@MessageField(id = "ckLdapInfoCnt", name = "당행 인증서 개수")
	private String ckLdapInfoCnt;

	@MessageField(id = "yoIbUser", name = "인터넷뱅킹 가입 여부")
	private String yoIbUser;

	@MessageField(id = "opprJdp", name = "인증서 조회결과")
	private OpprJdp opprJdp;

}