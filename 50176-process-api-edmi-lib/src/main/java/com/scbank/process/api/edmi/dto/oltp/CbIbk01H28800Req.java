package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H28800Req", type = Type.REQUEST, description = "착오송금 내역 조회 요청부")
public class CbIbk01H28800Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자 ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YIJHGB", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "YIJUMIN", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIJHSTIL", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHSTIL;

    @MessageField(id = "YIJHEDIL", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHEDIL;

    @MessageField(id = "YIYPAGE", name = "연속요구페이지번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYPAGE;

    @MessageField(id = "YILTERM", name = "연속거래Key", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YILTERM;

}
