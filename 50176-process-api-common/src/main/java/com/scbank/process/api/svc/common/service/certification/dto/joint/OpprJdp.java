package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class OpprJdp implements IMessageObject {

	@MessageField(id = "resCode", name = "응답 코드")
	private String resCode;

	@MessageField(id = "ckOppr", name = "ckOppr")
	private String ckOppr;

	@MessageField(id = "certStatus", name = "인증서 상태")
	private String certStatus;

	@MessageField(id = "certPolicy", name = "certPolicy")
	private String certPolicy;

}