package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 고객정보/CDD 여부 조회
 */
@Data
@IntegrationMessage(id = "GetCheckAvailableRequest", type = Type.REQUEST)
public class GetCheckAvailableRequest implements IMessageObject {

	@MessageField(id = "bizType", name = "업무 구분")
    private String bizType;

    @MessageField(id = "paramJsonString", name = "JSONString 파라미터")
    private String paramJsonString;

    @MessageField(id = "isCommonEntered", name = "신분증 공통 진입 여부")
    private String isCommonEntered;

}
