package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 고객정보/CDD 여부 조회
 */
@Data
@IntegrationMessage(id = "ValidateCddExpireDateResponse", type = Type.RESPONSE)
public class ValidateCddExpireDateResponse {

    @MessageField(id = "cddFlag", name = "CDD 유효일자 계산 결과")
    private String cddFlag;
}
