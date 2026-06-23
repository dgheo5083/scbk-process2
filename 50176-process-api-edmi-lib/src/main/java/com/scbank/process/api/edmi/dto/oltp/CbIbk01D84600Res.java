package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D84600Res", type = Type.RESPONSE, description = "비대면 및 금융거래 한도제한 계좌 관리", captureSystem = "OLTP")
public class CbIbk01D84600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOHMCD", name = "항목코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHMCD;

    @MessageField(id = "YODHGB", name = "등록해제구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODHGB;

}
