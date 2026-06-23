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
@IntegrationMessage(id = "CbTbs03H32100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드 원화 추가납입")
public class CbTbs03H32100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "JsNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JsNum;

    @MessageField(id = "ProcessDate", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ProcessDate;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "GrNum", name = "관리번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer GrNum;

    @MessageField(id = "JYCode", name = "즉시예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JYCode;

    @MessageField(id = "SgAmt", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SgAmt;

    @MessageField(id = "FeeAmt", name = "선취판매수수료 (9자리로 변환)", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FeeAmt;

    @MessageField(id = "FundAmt", name = "투자금액 (13 추가)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FundAmt;

    @MessageField(id = "YyDate", name = "입금예정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YyDate;

    @MessageField(id = "IgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgAcctNumFund", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String IgAcctNumFund;

    @MessageField(id = "FundName", name = "펀드명 신규", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundName;

    @MessageField(id = "NewDate", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NewDate;

    @MessageField(id = "DueDate", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DueDate;

    @MessageField(id = "FeeCode", name = "수수료구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FeeCode;

    @MessageField(id = "TransCode", name = "이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransCode;

    @MessageField(id = "AmtSign", name = "출금후잔액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign;

    @MessageField(id = "AfterBal", name = "출금후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AfterBal;

    @MessageField(id = "RJmNum", name = "전문번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RJmNum;

    @MessageField(id = "RError", name = "에러코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RError;

    @MessageField(id = "RResult", name = "처리결과내용", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RResult;

    @MessageField(id = "RState", name = "처리상태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RState;

    @MessageField(id = "IbFundName", name = "입금예금주명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String IbFundName;

    @MessageField(id = "ObFundName", name = "#출금예금주명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String ObFundName;

    @MessageField(id = "EndSale", name = "신규계좌개설중지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EndSale;

    @MessageField(id = "YOCGAIPGB", name = "펀드추가입금주의", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCGAIPGB;
}