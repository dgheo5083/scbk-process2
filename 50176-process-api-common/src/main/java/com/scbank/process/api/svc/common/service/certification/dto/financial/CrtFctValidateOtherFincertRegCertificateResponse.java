package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 타행금융인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtFctValidateOtherFincertRegCertificateResponse", type = Type.RESPONSE)
public class CrtFctValidateOtherFincertRegCertificateResponse implements IMessageObject {

}