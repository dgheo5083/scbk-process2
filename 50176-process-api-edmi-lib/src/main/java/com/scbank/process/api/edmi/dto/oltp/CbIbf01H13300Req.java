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
@IntegrationMessage(id = "CbIbf01H13300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "본인정보 이용 및 제공 조회")
public class CbIbf01H13300Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PerBusNo", name = "실명번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "CGAcctNum", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private BigDecimal CGAcctNum;

    @MessageField(id = "YISILGB", name = "실명구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISILGB;

    @MessageField(id = "YIKNAME", name = "고객명호출구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKNAME;

    @MessageField(id = "YIMCGB", name = "매체구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMCGB;
}
