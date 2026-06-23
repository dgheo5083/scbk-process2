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
@IntegrationMessage(id = "MciIbBnc01007Req", type = Type.REQUEST, captureSystem = "MCI", description = "계약자별수익률조회 요청부")
public class MciIbBnc01007Req implements IMessageObject {
    @MessageField(id = "WIUSERID", name = "USER_ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WIUSERID;

    @MessageField(id = "WIBRANCHCODE", name = "지점코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WIBRANCHCODE;

    @MessageField(id = "WICHNNLGB", name = "채널구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WICHNNLGB;

    @MessageField(id = "WILOCALIP", name = "IP", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WILOCALIP;

    @MessageField(id = "WITRCODE", name = "트랜잭션코드", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WITRCODE;

    @MessageField(id = "WIINFNO", name = "실행메소드", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WIINFNO;

    @MessageField(id = "WISMNO", name = "실명번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String WISMNO;

    @MessageField(id = "WICMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WICMFNA;

    @MessageField(id = "WISENO", name = "증권번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WISENO;

    @MessageField(id = "WIINSUCODE", name = "보험사코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WIINSUCODE;

    @MessageField(id = "WIMODGB", name = "모드구분(1:펀드운용수익탭,2:펀드수익률현황탭3:펀드변경내역)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String WIMODGB;

    @MessageField(id = "WIGIJGGKJYIL", name = "기준가격적용일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String WIGIJGGKJYIL;

}
