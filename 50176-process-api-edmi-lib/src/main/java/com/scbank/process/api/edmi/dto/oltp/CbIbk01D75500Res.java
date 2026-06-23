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
@IntegrationMessage(id = "CbIbk01D75500Res", type = Type.RESPONSE, description = "인터넷담보대출허용 응답 전문", captureSystem = "OLTP")
public class CbIbk01D75500Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJNO;

    @MessageField(id = "YODAMBOYN", name = "담대허용여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODAMBOYN;

    @MessageField(id = "YODUMMY", name = "더미", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;
}
