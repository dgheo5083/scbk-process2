package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SecRcvSetServiceNoticeResponse", type = Type.RESPONSE)
public class SecRcvSetServiceNoticeResponse implements IMessageObject {
    @MessageField(id = "paramJsonString", name = "paramJsonString")
    private String paramJsonString;

    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

    @MessageField(id = "screenFlag", name = "screenFlag")
    private String screenFlag;

    @MessageField(id = "isLoginYN", name = "isLoginYN")
    private String isLoginYN;

    @MessageField(id = "prdctId", name = "prdctId")
    private String prdctId;

    @MessageField(id = "prdctCd", name = "prdctCd")
    private String prdctCd;

    @MessageField(id = "bizType", name = "bizType")
    private String bizType;

    @MessageField(id = "prdctNm", name = "prdctNm")
    private String prdctNm;

}
