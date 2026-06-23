package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 공공꾸러미 API 호출
 */
@Data
@IntegrationMessage(id = "GetMyDataResponse", type = Type.RESPONSE)
public class GetMyDataResponse implements IMessageObject {

    @MessageField(id = "isErr", name = "")
    private String isErr;

    @MessageField(id = "rspCode", name = "")
    private String rspCode;

    @MessageField(id = "rspMsg", name = "")
    private String rspMsg;

    @MessageField(id = "resData", name = "")
    private String resData;

}
