package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 공동인증서 갱신 완료
 */
@Data
@IntegrationMessage(id = "CrtJctAuthorizeCertRenewRequest", type = Type.REQUEST)
public class CrtJctAuthorizeCertRenewRequest implements IMessageObject {

}