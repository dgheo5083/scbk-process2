package com.scbank.process.api.svc.common.service.certification.dto.digital;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 디지털인증서 해지 완료
 */
@Data
@IntegrationMessage(id = "CrtDctConfirmDigitalcertRevokeResponse", type = Type.RESPONSE)
public class CrtDctConfirmDigitalcertRevokeResponse implements IMessageObject {

}