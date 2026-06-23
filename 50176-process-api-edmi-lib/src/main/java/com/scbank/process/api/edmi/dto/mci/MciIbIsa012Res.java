package com.scbank.process.api.edmi.dto.mci;

import java.math.BigDecimal;
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
@IntegrationMessage(id = "MciIbIsa012Res", type = Type.RESPONSE, description = "MTS 계좌 해지 신청 가능여부 조회")
public class MciIbIsa012Res implements IMessageObject {
    @MessageField(id = "MTS_CgAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MTS_CgAcctNum;

    @MessageField(id = "SOHJEGNEYB", name = "해제가능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOHJEGNEYB;

    @MessageField(id = "SOGRAK", name = "거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SOGRAK;

    @MessageField(id = "SOHJYJIL", name = "해지예정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SOHJYJIL;

    @MessageField(id = "SOCRRS2", name = "처리결과2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOCRRS2;

    @MessageField(id = "SOERWICH", name = "에러위치", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOERWICH;

    @MessageField(id = "SOERRMSG", name = "ERROR메시지", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SOERRMSG;

}