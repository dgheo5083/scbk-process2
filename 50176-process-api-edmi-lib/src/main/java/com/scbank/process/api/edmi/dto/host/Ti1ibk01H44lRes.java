package com.scbank.process.api.edmi.dto.host;

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
@IntegrationMessage(id = "Ti1ibk01H44lRes", type = Type.RESPONSE, description = "MTS_계좌해지거래 신청 입력")
public class Ti1ibk01H44lRes implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YO_GRGB", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YO_GRGB;

    @MessageField(id = "MTS_CgACCTNUM", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MTS_CgACCTNUM;

    @MessageField(id = "YO_ZONG", name = "계좌종별", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YO_ZONG;

    @MessageField(id = "YO_CMFNA", name = "고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YO_CMFNA;

    @MessageField(id = "YO_DRAK", name = "거래금액", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YO_DRAK;

    @MessageField(id = "YO_MTSACCTNUM", name = "이체계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YO_MTSACCTNUM;

    @MessageField(id = "YO_JSNO", name = "자산번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YO_JSNO;

    @MessageField(id = "YO_JSNM", name = "자산명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YO_JSNM;

}