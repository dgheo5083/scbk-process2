package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H504Req", type = Type.REQUEST, captureSystem = "OLTP", description = "개인정보노출자 조회 요청 전문")
public class Ti1ibk01H504Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
    private String YIUSID;

    @MessageField(id = "YILGGB", name = "로그인여부 YN", length = 1)
    private String YILGGB;

    @MessageField(id = "E2ERegNum", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String E2ERegNum; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "YIPASS", name = "비밀번호", length = 8, masking = true, maskingType = "03")
    private String YIPASS;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02")
    private String YIGJNO;

}
