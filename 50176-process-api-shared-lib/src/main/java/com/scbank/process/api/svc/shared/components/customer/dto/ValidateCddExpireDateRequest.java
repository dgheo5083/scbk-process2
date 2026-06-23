package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 고객정보/CDD 여부 조회
 */
@Data
@IntegrationMessage(id = "ValidateCddExpireDateRequest", type = Type.REQUEST)
public class ValidateCddExpireDateRequest {

    @MessageField(id = "yoCddIl", name = "CDD유효일자2308", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoCddIl;

    @MessageField(id = "yoCddKycLc", name = "CDD유효일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoCddKycLc;
}
