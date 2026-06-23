package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H504Res", type = Type.REQUEST, captureSystem = "OLTP", description = "개인정보노출자 조회 응답 전문")
public class CbIbk01H504Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
    private String YOUSID;

    @MessageField(id = "YOLRSOTGB", name = "개인정보노출등록구분 1:등록 3:미등록", length = 1)
    private String YOLRSOTGB;

}
