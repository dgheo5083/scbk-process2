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
@IntegrationMessage(id = "CbIbk01Zz9900Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "고객별 자산정보 조회")
public class CbIbk01Zz9900Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "CIFNO", name = "고객식별번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CIFNO;

    @MessageField(id = "BASE_DT", name = "기준일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BASE_DT;

    @MessageField(id = "FNANCL_TOTAL_AMT", name = "예금합계(CASA,TD)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FNANCL_TOTAL_AMT;

    @MessageField(id = "FNANCL_CASH", name = "자산(현금)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FNANCL_CASH;

    @MessageField(id = "FNANCL_STCK", name = "자산(주식)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FNANCL_STCK;

    @MessageField(id = "FNANCL_BOND", name = "자산(채권)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FNANCL_BOND;

    @MessageField(id = "FNANCL_ALT", name = "자산(대안)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FNANCL_ALT;

    @MessageField(id = "FNANCL_COM", name = "자산(원자재)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FNANCL_COM;

    @MessageField(id = "INVEST_LOCAL_KOR", name = "투자지역(한국)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal INVEST_LOCAL_KOR;

    @MessageField(id = "INVEST_LOCAL_ASIA", name = "투자지역(아시아태평양)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal INVEST_LOCAL_ASIA;

    @MessageField(id = "INVEST_LOCAL_GLB", name = "투자지역(글로벌)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal INVEST_LOCAL_GLB;

    @MessageField(id = "INVEST_LOCAL_EMG", name = "투자지역 글로벌이머징(글로벌이머징,이머징유럽,남미)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal INVEST_LOCAL_EMG;

    @MessageField(id = "INVEST_LOCAL_ADV", name = "투자지역 선진시장(유럽,일본,미국)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal INVEST_LOCAL_ADV;

    @MessageField(id = "INVEST_LOCAL_THEMA", name = "투자지역(테마형)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal INVEST_LOCAL_THEMA;

    @MessageField(id = "FOCUS_YN", name = "포커스펀드(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOCUS_YN;

    @MessageField(id = "CHECK_1", name = "검증값1", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CHECK_1;

    @MessageField(id = "CHECK_2", name = "검증값2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CHECK_2;

    @MessageField(id = "CHECK_3", name = "검증값3", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CHECK_3;
}
