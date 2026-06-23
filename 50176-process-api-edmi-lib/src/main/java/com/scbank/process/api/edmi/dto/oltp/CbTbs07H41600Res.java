package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs07H41600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "현대카드 e-그린세이브 M포인트 전환/해제 요청 전문")
public class CbTbs07H41600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJNO;

    @MessageField(id = "YOPNTGB", name = "M포인트전환구분(1:전환)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPNTGB;

    @MessageField(id = "DUMMY", name = "DUMMY", length = 48, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DUMMY;

}
