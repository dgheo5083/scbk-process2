package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import lombok.Data;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

@Data
@IntegrationMessage(id = "GetCardBasicInfoResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 카드기본정보조회 API")
public class GetCardBasicInfoResponse implements IMessageObject {

    @MessageField(id = "rspCode", name = "응답코드")
    private String rspCode;

    @MessageField(id = "rspMsg", name = "응답메시지")
    private String rspMsg;

    @MessageField(id = "issueDt", name = "발급일")
    private String issueDt;

    @MessageField(id = "isHCD", name = "전체 > 현대카드 진입")
    private String isHCD;

}
