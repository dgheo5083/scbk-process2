package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 이용안내 앱 버전 조회
 */
@Data
@IntegrationMessage(id = "CrtJctGetCurrentAppVersionResponse", type = Type.RESPONSE)
public class CrtJctGetCurrentAppVersionResponse implements IMessageObject {

	@MessageField(id = "regDt", name = "최근 업데이트")
	private String regDt;

	@MessageField(id = "vsn", name = "앱 최신버전")
	private String vsn;

	@MessageField(id = "serverName", name = "서버정보")
	private String serverName;

}