package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H50700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "대출이동제-상환확인증 조회")
public class CbIbk01H50700Req implements IMessageObject {
    @MessageField(id = "YIUSID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03", encoding = "cp500")
    private String YIPASS;

    @MessageField(id = "YIGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIGRIL;

    @MessageField(id = "YIYSNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYSNO;

}