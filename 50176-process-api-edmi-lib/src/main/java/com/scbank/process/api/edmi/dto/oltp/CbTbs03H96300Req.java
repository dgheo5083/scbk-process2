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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbTbs03H96300Req", type = Type.REQUEST, description = "타행계좌 유효성 검증 요청부")
public class CbTbs03H96300Req implements IMessageObject {

    @MessageField(id="YI_GRGB",name="거래구분",length=1,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YI_GRGB;

    @MessageField(id="UserID",name="이용자번호",length=10,masking=true,maskingType="01",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String UserID;

    @MessageField(id="TSPassword",name="통신비밀번호",length=8,masking=true,maskingType="03",align=AlignType.LEFT,padding=StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id="YI_JMNO",name="주민등록번호",length=13,masking=true,maskingType="01",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YI_JMNO;

    @MessageField(id="YI_JBNK",name="타행은행코드",length=3,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YI_JBNK;

    @MessageField(id="YI_GJNO",name="타행계좌번호",length=14,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YI_GJNO;

    @MessageField(id="YI_YGJR",name="예금종류",length=2,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YI_YGJR;

}
