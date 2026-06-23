package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D847Res", type = Type.RESPONSE, description = "스마트박스 이자지급 응답부")
public class CbIbk01D847Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOIJA", name = "세전이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOIJA;

    @MessageField(id = "YOSOD", name = "소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOSOD;

    @MessageField(id = "YOBANG", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOBANG;

    @MessageField(id = "YOJUMIN", name = "주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOJUMIN;

    @MessageField(id = "YOGYOS", name = "", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOGYOS;

    @MessageField(id = "YOSETOT", name = "세금합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOSETOT;

    @MessageField(id = "YOCHJIG", name = "세후이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOCHJIG;

    @MessageField(id = "YOSOSE", name = "소득세율", length = 7, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOSOSE;

    @MessageField(id = "YOSBXJIGUN", name = "SMARTBOX 지급회차", length = 3, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOSBXJIGUN;

    @MessageField(id = "YODUMY", name = "", length = 96, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMY;
}