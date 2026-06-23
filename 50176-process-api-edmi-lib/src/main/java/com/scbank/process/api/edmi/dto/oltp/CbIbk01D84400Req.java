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
@IntegrationMessage(id = "CbIbk01D84400Req", type = Type.REQUEST, captureSystem = "OLTP", description = "계좌 비밀번호 재설정 및 오류건수 해제")
public class CbIbk01D84400Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIUPGB", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUPGB;

    @MessageField(id = "YIJMNO", name = "주민/사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJMNO;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YIGJNO;

    @MessageField(id = "YIGJPASS", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YIGJPASS;
}
