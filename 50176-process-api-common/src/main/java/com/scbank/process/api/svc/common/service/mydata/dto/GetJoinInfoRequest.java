package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * 마이데이터 가입유무 조회 API
 * 
 */
@Data
@IntegrationMessage(id = "GetJoinInfoRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 가입여부조회 API")
public class GetJoinInfoRequest implements IMessageObject {

    @MessageField(id = "isRtFlg", name = "새로고침여부'Y':새로고침(실시간API호출) 'N':DB조회(최근이력)")
    private String isRtFlg;

    @MessageField(id = "hdFlag", name = "현대카드 새로고침 플래그(Y: 새로고침, H: 새로고침 아님)")
    private String hdFlag;

    @MessageField(id = "scrnMkCd", name = "화면구분코드")
    private String scrnMkCd;

}
