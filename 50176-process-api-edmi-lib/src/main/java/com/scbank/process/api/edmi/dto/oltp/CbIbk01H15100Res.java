package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H15100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "대출현황(전체)조회 응답부")
public class CbIbk01H15100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "Rcount", name = "반복부", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer Rcount;

    @MessageField(id = "YSCode", name = "연속구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YSCode;

    @MessageField(id = "JumNum", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JumNum;

    @MessageField(id = "CustNum", name = "고객번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer CustNum;

    @MessageField(id = "Gamok", name = "과목", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Gamok;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "")
    private String AcctNum;

    @MessageField(id = "GubunCode", name = "구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GubunCode;

    @MessageField(id = "LoanStateReqRlt", name = "레코드룰명")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H15100Res/Rcount")
    private List<LoanStateReqRlt> LoanStateReqRlt;

    @Data
    public static class LoanStateReqRlt implements IMessageObject {

        @MessageField(id = "LoanCodeH", name = "대출종류", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String LoanCodeH;

        @MessageField(id = "AcctNum1", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
        private String AcctNum1;

        @MessageField(id = "LoanDate1", name = "대출일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer LoanDate1;

        @MessageField(id = "LoanBal", name = "대출잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal LoanBal;

        @MessageField(id = "LimitAmt", name = "한도금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal LimitAmt;

        @MessageField(id = "YJDueDate1", name = "약정기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YJDueDate1;

        @MessageField(id = "IsuEndDate", name = "이수종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer IsuEndDate;

        @MessageField(id = "NowRate", name = "현이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer NowRate;

        @MessageField(id = "TrxKind", name = "거래형태", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer TrxKind;

        @MessageField(id = "AcctCode", name = "계정코드", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AcctCode;

        @MessageField(id = "YODCJONG", name = "대출종류H:한G:개", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YODCJONG;

        @MessageField(id = "YOGAMYU", name = "한도감액가능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGAMYU;

        @MessageField(id = "YOSMSDR", name = "SMS등록여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSMSDR;

        @MessageField(id = "YOEMAIL", name = "EMAIL등록여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOEMAIL;

        @MessageField(id = "YOHJYU", name = "해지가능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOHJYU;

        @MessageField(id = "YOGBKCD", name = "결제은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOGBKCD;

        @MessageField(id = "YOGGJNO", name = "결제계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
        private String YOGGJNO;

        @MessageField(id = "YOGICIL", name = "결제일", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YOGICIL;

        @MessageField(id = "YOBSJUM", name = "대출계좌 BSJUM", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YOBSJUM;

        @MessageField(id = "YOFYN", name = "퍼스트론여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOFYN;

        @MessageField(id = "YORVKGB", name = "철회대상여부 Y:대상", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YORVKGB;

        @MessageField(id = "YODUMMY", name = "더미", length = 27, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YODUMMY;

    }
}