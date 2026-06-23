package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import java.math.BigDecimal;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "CbIbf01H52500Req", type = Type.REQUEST, captureSystem = "OLTP", description = "외화보통예금/당좌예금지급")
public class CbIbf01H52500Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "acceptNo", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer acceptNo;

    @MessageField(id = "frPayAcctNo", name = "외화출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String frPayAcctNo;

    @MessageField(id = "frPayAcctPass", name = "외화계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String frPayAcctPass;

    @MessageField(id = "wonCurrRcvAcctNo", name = "원화입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String wonCurrRcvAcctNo;

    @MessageField(id = "payFrCurrCode", name = "출금외화통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String payFrCurrCode;

    @MessageField(id = "payFrAmt", name = "출금외화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal payFrAmt;

    @MessageField(id = "applyCurr", name = "적용환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer applyCurr;

    @MessageField(id = "rcvWonAmt", name = "입금원화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal rcvWonAmt;

    @MessageField(id = "specialApply", name = "우대신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String specialApply;

    @MessageField(id = "areaGuBun", name = "보험구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String areaGuBun;

    @MessageField(id = "insustrdt", name = "보험시작일", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer insustrdt;

    @MessageField(id = "YIDEALNO", name = "DEAL번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDEALNO;

    @MessageField(id = "YIWFXGBN", name = "WFX tobe 구분필드", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIWFXGBN;

    @MessageField(id = "YIORDID", name = "WFX ORDERID", length = 21, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIORDID;

    @MessageField(id = "YIOFXID", name = "WFX ID", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOFXID;

    @MessageField(id = "YICCYPR", name = "USDKRW JPYKRW", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICCYPR;

    @MessageField(id = "YIBSGBN", name = "고객기준 BUY SELL", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIBSGBN;

    @MessageField(id = "YICLIRT", name = "CLIENT RATE", length = 20, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YICLIRT;

    @MessageField(id = "YICNTAMT", name = "KRW 금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YICNTAMT;

    @MessageField(id = "YIUSDAMT", name = "USD 환산금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIUSDAMT;

    @MessageField(id = "YIDUMY1", name = "Dummy", length = 292, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMY1;

    @MessageField(id = "YIIPN", name = "IP", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "YIINTHB", name = "소개자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIINTHB;

    @MessageField(id = "YIDUMY", name = "Dummy", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMY;

}