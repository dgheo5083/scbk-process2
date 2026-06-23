
package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07D55200Res", type = Type.RESPONSE, description = "국세이체 본처리 응답부")
public class CbTbs07D55200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "사용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YO_SuccessYN", name = "성공여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YO_SuccessYN;

    @MessageField(id = "YONBIDNO", name = "납부순번", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YONBIDNO;

    @MessageField(id = "YODGISNO", name = "납부 번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODGISNO;

    @MessageField(id = "YOHUJAN", name = "출금후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHUJAN;
}
