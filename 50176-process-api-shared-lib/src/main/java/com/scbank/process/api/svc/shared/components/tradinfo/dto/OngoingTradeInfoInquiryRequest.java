package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "OngoingTradeInfoInquiryRequest", type = Type.REQUEST)
public class OngoingTradeInfoInquiryRequest implements IMessageObject {
	
	@MessageField(id = "custNo", name = "고객번호", example = "9999")
    private String custNo;

    @MessageField(id = "bizType", name = "업무구분", example = "HELS")
    private String bizType;
    
    @MessageField(id = "integratedConselingYn", name = "[OPTION] 대출 통합상담여부")
    private String integratedConselingYn;

}
