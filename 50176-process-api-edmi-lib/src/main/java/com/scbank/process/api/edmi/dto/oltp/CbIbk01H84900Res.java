package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H87901Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "이체 이상계좌 조회")
public class CbIbk01H84900Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YODRYB", name = "불법계좌등록여부(Y:대상)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODRYB;

    @MessageField(id = "YODUMMY", name = "DUMMY", length = 29, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
