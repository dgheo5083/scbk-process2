package com.scbank.process.api.svc.common.service.mydata.dto;

import java.math.BigDecimal;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FiveDaysInfoDto", type = Type.RESPONSE, description = "마이데이터 > 현대카드 배너영역 FiveDaysInfoDto")
public class FiveDaysInfoDto implements IMessageObject {

    @MessageField(id = "useDt", name = "")
    private String useDt;

    @MessageField(id = "amtCnt", name = "")
    private String amtCnt;

    @MessageField(id = "useAmt", name = "")
    private BigDecimal useAmt;

}
