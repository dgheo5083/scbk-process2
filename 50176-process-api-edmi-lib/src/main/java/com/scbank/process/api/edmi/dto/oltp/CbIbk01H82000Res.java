package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H82000Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "포인트조회")
public class CbIbk01H82000Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "UserName", name = "이용자성명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserName;

    @MessageField(id = "JGPoint", name = "잔고포인트", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal JGPoint;

    @MessageField(id = "Automation", name = "자동화기기결제", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Automation;

    @MessageField(id = "CenterYY", name = "센터처리결제예약", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CenterYY;

    @MessageField(id = "SMSYY", name = "SMS결제예약", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SMSYY;

    @MessageField(id = "YOBASEDATE", name = "기준일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOBASEDATE;

    @MessageField(id = "YODELPOINT", name = "소멸예정포인트", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YODELPOINT;

}
