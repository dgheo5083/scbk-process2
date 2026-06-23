package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H843Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "CI조회 응답 전문")
public class CbIbk01H843Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YOCONFIRM", name = "CI등록여부(Y:등록, N:미등록)", length = 1)
    private String YOCONFIRM;

    @MessageField(id = "YOJUMIN", name = "개인：주민등록번호", length = 13, masking = true, maskingType = "01")
    private String YOJUMIN;

    @MessageField(id = "YOCINO", name = "CI번호", length = 88)
    private String YOCINO;

    @MessageField(id = "YOCOND", name = "사활구분 Y:정상, N:해지", length = 1)
    private String YOCOND;

}