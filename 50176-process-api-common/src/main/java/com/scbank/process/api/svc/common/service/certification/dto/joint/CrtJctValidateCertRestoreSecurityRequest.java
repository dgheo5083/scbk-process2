package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 인증서 효력회복 보안매체 검증
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRestoreSecurityRequest", type = Type.REQUEST)
public class CrtJctValidateCertRestoreSecurityRequest implements IMessageObject {
}