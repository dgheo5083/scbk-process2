package com.scbank.process.api.edmi.dto.host;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;
@Data
@IntegrationMessage(id = "TI1IBK01_D847_REQ", type = Type.REQUEST, description = "스마트박스 이자지급 요청부")
public class Ti1ibk01D847Req implements IMessageObject {
    @MessageField(id="YIUSID",name="이용자번호",length=10,masking=true,maskingType="01",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id="YIPASS",name="통신비밀번호",length=8,masking=true,maskingType="03",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id="YIGJNO",name="계좌번호",length=11,masking=true,maskingType="02",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YIGJNO;
}