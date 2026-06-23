package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H28J00Req", type = Type.REQUEST, description = "자행 착오송금 내역 조회 요청부", captureSystem = "OLTP")
public class CbIbk01H28J00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자 ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YIJHGB", name = "조회구분-1:반환대상,2:반환완료", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "YIJUMIN", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIJHSTIL", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHSTIL;

    @MessageField(id = "YIJHEDIL", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHEDIL;

    @MessageField(id = "YIYCONT", name = "1:연속", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYCONT;

    @MessageField(id = "YIYPAGE", name = "출력총페이지", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYPAGE;

    @MessageField(id = "YIYCNT", name = "출력요청페이지", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYCNT;

    @MessageField(id = "YIDUMMY", name = "더미", length = 39, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}
