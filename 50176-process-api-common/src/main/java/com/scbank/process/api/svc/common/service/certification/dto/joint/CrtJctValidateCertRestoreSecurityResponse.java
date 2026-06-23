package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 인증서 효력회복 보안매체 검증
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRestoreSecurityResponse", type = Type.RESPONSE)
public class CrtJctValidateCertRestoreSecurityResponse implements IMessageObject {

}