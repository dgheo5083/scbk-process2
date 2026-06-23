package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 타행금융인증서 해제
 */
@Data
@IntegrationMessage(id = "CrtFctAuthorizeOtherFincertDelResponse", type = Type.RESPONSE)
public class CrtFctAuthorizeOtherFincertDelResponse implements IMessageObject {

	@MessageField(id = "setDeviceStorageYn", name = "Y면 앱스토리지 생체인증정보 삭제")
	private String setDeviceStorageYn;

}