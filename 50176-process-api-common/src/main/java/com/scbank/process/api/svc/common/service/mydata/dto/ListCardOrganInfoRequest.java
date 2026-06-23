package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListCardOrganInfoRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드목록 조회 API")
public class ListCardOrganInfoRequest implements IMessageObject {

    @MessageField(id = "isRtFlg", name = "새로고침여부'Y':새로고침(실시간API호출) 'N':DB조회(최근이력)")
    private String isRtFlg;

    @MessageField(id = "hdFlag", name = "현대카드 새로고침 플래그(Y: 새로고침, H: 새로고침 아님)")
    private String hdFlag;

    @MessageField(id = "orgCd", name = "현대카드 기관코드")
    private String orgCd;

    @MessageField(id = "reqNo", name = "reqNo")
    private String reqNo;

}
