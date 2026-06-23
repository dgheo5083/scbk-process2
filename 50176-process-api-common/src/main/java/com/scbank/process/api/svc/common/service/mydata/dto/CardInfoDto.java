package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "CardInfoDto", type = Type.RESPONSE, description = "마이데이터 > 현대카드 CardInfoDto")
public class CardInfoDto implements IMessageObject {

    @MessageField(id = "cardId", name = "")
    private String cardId;

    @MessageField(id = "ppId", name = "")
    private String ppId;

    @MessageField(id = "totBsAmt", name = "")
    private String totBsAmt;

    @MessageField(id = "cardProdNm", name = "")
    private String cardProdNm;

    @MessageField(id = "cardNo", name = "")
    private String cardNo;

    @MessageField(id = "cardMkCd", name = "")
    private String cardMkCd;

    @MessageField(id = "cardMkNm", name = "")
    private String cardMkNm;

    @MessageField(id = "inTimestamp", name = "")
    private String inTimestamp;

    @MessageField(id = "prodVeriFlg", name = "")
    private String prodVeriFlg;

    @MessageField(id = "rspCode", name = "")
    private String rspCode;

    @MessageField(id = "updateTime", name = "")
    private String updateTime;

}
