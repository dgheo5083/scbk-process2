package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "MciFund052Req", type = Type.REQUEST, captureSystem = "MCI", description = "목표 도달 자동환매 요청부")
public class MciFund052Req implements IMessageObject {
    @MessageField(id = "AIGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIGRGB;

    @MessageField(id = "REACH_ACCTNUM01", name = "계좌번호1", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String REACH_ACCTNUM01;

    @MessageField(id = "AIMPOSURTA01", name = "목표수익율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIMPOSURTA01;

    @MessageField(id = "AISERSTAIL01", name = "서비스시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AISERSTAIL01;

    @MessageField(id = "REACH_ACCTNUM02", name = "계좌번호2", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String REACH_ACCTNUM02;

    @MessageField(id = "AISERSTAIL02", name = "서비스시작일자2", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AISERSTAIL02;

    @MessageField(id = "REACH_ACCTNUM03", name = "계좌번호3", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String REACH_ACCTNUM03;

    @MessageField(id = "AIMPOSURTA03", name = "목표수익율3", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIMPOSURTA03;

    @MessageField(id = "AISERSTAIL03", name = "서비스시작일자3", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AISERSTAIL03;

}
