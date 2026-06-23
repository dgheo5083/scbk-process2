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
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01D95100Req", type = Type.REQUEST, description = "스피드계좌 조회", captureSystem = "OLTP")
public class CbIbk01D95100Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO;

    @MessageField(id = "YIGPASS", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGPASS;

    @MessageField(id = "YIUPGBN", name = "거래구분(1:조회, 2:등록, 3:해지)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUPGBN;

    @MessageField(id = "YIPSGBN", name = "계좌비밀번호검증유무(1:유, 2:무)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPSGBN;
}
