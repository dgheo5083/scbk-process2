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
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01D75500Req", type = Type.REQUEST, description = "인터넷담보대출허용 요청 전문", captureSystem = "OLTP")
public class CbIbk01D75500Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIYGGB", name = "예금구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYGGB;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO;

    @MessageField(id = "YIGPASS", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YIGPASS;

    @MessageField(id = "YIDAMBOYN", name = "담대허용여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDAMBOYN;

    @MessageField(id = "YIDUMMY", name = "더미", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;
}
