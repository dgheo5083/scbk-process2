package com.scbank.process.api.svc.common.service.certification.dto.joint;


import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;


/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 참조번호 발급
 */
@Data
@IntegrationMessage(id = "CrtJctGetCertRefNumRequest", type = Type.REQUEST)
public class CrtJctGetCertRefNumRequest implements IMessageObject {

	@MessageField(id = "onAgreeChk", name = "온라인 발급 사전동의 값")
	private String onAgreeChk;

	@MessageField(id = "email1", name = "이메일1")
	private String email1;

	@MessageField(id = "email2", name = "이메일2")
	private String email2;

	@MessageField(id = "tel1", name = "전화번호1")
	private String tel1;

	@MessageField(id = "tel2", name = "전화번호2")
	private String tel2;

	@MessageField(id = "tel3", name = "전화번호3")
	private String tel3;
	
	@MessageField(id = "raGubun", name = "인증서 종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))")
	private String raGubun;

}