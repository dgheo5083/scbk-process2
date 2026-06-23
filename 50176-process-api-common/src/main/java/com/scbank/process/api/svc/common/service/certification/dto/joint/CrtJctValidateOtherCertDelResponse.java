package com.scbank.process.api.svc.common.service.certification.dto.joint;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.shared.components.cert.dto.OppraCertInfo;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 타기관 해제 본인확인
 */
@Data
@IntegrationMessage(id = "CrtJctValidateOtherCertDelResponse", type = Type.RESPONSE)
public class CrtJctValidateOtherCertDelResponse implements IMessageObject {

	@MessageField(id = "custName", name = "고객명")
	private String custName;

	@MessageField(id = "certList", name = "인증서 목록")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<OppraCertInfo> certList;

}