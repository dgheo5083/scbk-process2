package com.scbank.process.api.svc.common.service.certification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 핀테크인증서 이용등록
 */
@Data
@IntegrationMessage(id = "CrtFtcAuthorizeFintechCertIssueResponse", type = Type.RESPONSE)
public class CrtFtcAuthorizeFintechCertIssueResponse implements IMessageObject {

}