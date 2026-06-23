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
@IntegrationMessage(id = "MciFund051Req", type = Type.REQUEST, captureSystem = "MCI", description = "목표도달 대상가능계좌 조회 입력 요청부")
public class MciFund051Req implements IMessageObject {

    @MessageField(id = "AIGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIGRGB;

    @MessageField(id = "AIJHGB2", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGB2;

    @MessageField(id = "REACH_ACCTNUM", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String REACH_ACCTNUM;

    @MessageField(id = "AIFJUMIN", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIFJUMIN;

    @MessageField(id = "AIBRNCH", name = "조회점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIBRNCH;

    @MessageField(id = "AIJHGGSTART", name = "조회기간시작일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGGSTART;

    @MessageField(id = "AIJHGGEND", name = "조회기간종료일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJHGGEND;

}
