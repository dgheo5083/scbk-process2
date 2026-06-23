package com.scbank.process.api.edmi.dto.mci;

import java.math.BigDecimal;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;
import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbIsa012Req", type = Type.REQUEST, captureSystem = "MCI", description = "MTS 계좌 해지 신청 가능여부 조회")
public class MciIbIsa012Req implements IMessageObject {
    @MessageField(id = "MTS_ACCTNUM", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MTS_ACCTNUM;

    @MessageField(id = "SIGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIGRGB;

    @MessageField(id = "SIGRAK", name = "거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SIGRAK;

    @MessageField(id = "SIMEGB", name = "매체구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIMEGB;

    @MessageField(id = "SIOPHBN", name = "조작자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIOPHBN;

}