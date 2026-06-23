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
@IntegrationMessage(id = "CbIbk01H23100Res", type = Type.REQUEST, captureSystem = "OLTP", description = "대출금이자 상환 응답부")
public class CbIbk01H23100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "JsNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JsNum;

    @MessageField(id = "CgAcctNum", name = "접수번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctNum;

    @MessageField(id = "HgSName", name = "의뢰인명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String HgSName;

    @MessageField(id = "JgCode", name = "지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JgCode;

    @MessageField(id = "LoanAcctNum", name = "대출계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String LoanAcctNum;

    @MessageField(id = "CjName", name = "차주명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CjName;

    @MessageField(id = "AmtSign", name = "출금후잔액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign;

    @MessageField(id = "AfterBal", name = "출금후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AfterBal;

    @MessageField(id = "LoanBal", name = "대출잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LoanBal;

    @MessageField(id = "YjInt", name = "약정이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YjInt;

    @MessageField(id = "YcInt", name = "연체이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YcInt;

    @MessageField(id = "TotInt", name = "이자합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TotInt;

    @MessageField(id = "IntSDate", name = "이자시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer IntSDate;

    @MessageField(id = "IntEDate", name = "이자종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer IntEDate;

    @MessageField(id = "NextIntDate", name = "다음이자일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NextIntDate;

    @MessageField(id = "YjDate", name = "약정기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YjDate;

    @MessageField(id = "YcYN", name = "연체유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YcYN;

    @MessageField(id = "InterRealPayment", name = "실납부이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal InterRealPayment;

    @MessageField(id = "InterPointPayment", name = "포인트납부이자", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer InterPointPayment;

}
