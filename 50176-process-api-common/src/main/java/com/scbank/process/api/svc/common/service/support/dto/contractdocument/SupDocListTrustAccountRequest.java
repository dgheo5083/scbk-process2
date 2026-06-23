package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 신탁 계약서조회
 */
@Data
@IntegrationMessage(id = "SupDocListTrustAccountRequest", type = Type.REQUEST)
public class SupDocListTrustAccountRequest implements IMessageObject {

    @MessageField(id = "acctNum", name = "acctNum")
    private String acctNum;

    @MessageField(id = "yiURL", name = "yiURL")
    private String yiURL;

}