package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 타기관 등록 인증서 유효성 검사
 */
@Data
@IntegrationMessage(id = "CrtJctValidateOtherCertRegCertificateResponse", type = Type.RESPONSE)
public class CrtJctValidateOtherCertRegCertificateResponse implements IMessageObject {

}