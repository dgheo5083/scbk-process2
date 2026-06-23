package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PUSH_마케팅_개인정보수집이용동의 원장/이력 등록 및 변경
 */
@Data
@IntegrationMessage(id = "SupPntUpdateMarketingNoticeTermsAgreeResponse", type = Type.RESPONSE)
public class SupPntUpdateMarketingNoticeTermsAgreeResponse implements IMessageObject {

    @MessageField(id = "rtnFlag", name = "")
    private String rtnFlag;

}
