package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H11401Res", type = Type.RESPONSE, description = "출금계좌비밀번호 검증 요청 전문")
public class CbIbk01H11401Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "ItemName", name = "과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ItemName;

    @MessageField(id = "CustName", name = "예금주", length = 22, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "AdminBranch", name = "관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AdminBranch;

    @MessageField(id = "AdminBranchName", name = "관리점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AdminBranchName;

    @MessageField(id = "AvailBalSign", name = "지불가능잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AvailBalSign;

    @MessageField(id = "AvailBalance", name = "지불가능잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AvailBalance;

    @MessageField(id = "RealBalSign", name = "실질잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RealBalSign;

    @MessageField(id = "RealBalance", name = "실질잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal RealBalance;

    @MessageField(id = "NomiBalSign", name = "명목잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NomiBalSign;

    @MessageField(id = "NomiBalance", name = "명목잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NomiBalance;

    @MessageField(id = "UnpaidReportAmt", name = "미결제통보액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal UnpaidReportAmt;

    @MessageField(id = "UnFundAmt", name = "미자금화액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal UnFundAmt;

    @MessageField(id = "TheDayBankCheck", name = "당일자기앞", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TheDayBankCheck;

    @MessageField(id = "TheDayHouse", name = "당일가계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TheDayHouse;

    @MessageField(id = "TheDayOther", name = "당일기타", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TheDayOther;

    @MessageField(id = "NextDayBankCheck", name = "익일자기앞", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NextDayBankCheck;

    @MessageField(id = "NextDayHouse", name = "익일가계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NextDayHouse;

    @MessageField(id = "NextDayOther", name = "익일기타", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NextDayOther;

    @MessageField(id = "LoanLimit", name = "대출한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LoanLimit;

    @MessageField(id = "PromiseDueDate", name = "약정기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PromiseDueDate;

    @MessageField(id = "RevoDueDate", name = "회전기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RevoDueDate;

    @MessageField(id = "FinalTrxDate", name = "최종거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FinalTrxDate;

    @MessageField(id = "FirstNewDate", name = "최초신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FirstNewDate;

    @MessageField(id = "BlankCnt", name = "미기장건수", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer BlankCnt;

    @MessageField(id = "AcctState", name = "계좌상태", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctState;

    @MessageField(id = "NoBankBookInfo", name = "무통장전환여부", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NoBankBookInfo;

    @MessageField(id = "YOMWIJA", name = "연체이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOMWIJA;

    @MessageField(id = "YOBYN", name = "계좌별명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBYN;

    @MessageField(id = "YOITGBN", name = "인터넷신규유무", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOITGBN;

    @MessageField(id = "YOJDATE", name = "급여이체지정일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJDATE;

    @MessageField(id = "YOHDYN", name = "한도계좌여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHDYN;

    @MessageField(id = "YOYKIJASign", name = "약정이자Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYKIJASign;

    @MessageField(id = "YOYKIJA", name = "약정이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOYKIJA;

    @MessageField(id = "YOYCIJASign", name = "연체이자Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYCIJASign;

    @MessageField(id = "YOYCIJA", name = "연체이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOYCIJA;

    @MessageField(id = "YOGJSS", name = "계좌상태", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJSS;

    @MessageField(id = "YOSILY", name = "실명확인여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSILY;
}
