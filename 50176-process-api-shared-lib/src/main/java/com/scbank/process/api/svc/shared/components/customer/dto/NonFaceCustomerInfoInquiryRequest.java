package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * process 서비스 요청 정보 클래스
 * 비대면인증 고객관리 조회
 */
@Data
@IntegrationMessage(id = "NonFaceCustomerInfoInquiryRequest", type = Type.REQUEST)
public class NonFaceCustomerInfoInquiryRequest {
	@MessageField(id = "ssn", name = "ssn")
    private String ssn;
}
