package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 세금계산서 신청
 */
@Data
@IntegrationMessage(id = "CrtJctRequestCertTaxInvoiceResponse", type = Type.RESPONSE)
public class CrtJctRequestCertTaxInvoiceResponse implements IMessageObject {

}