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
@IntegrationMessage(id = "CbIbk01H11404Res", type = Type.RESPONSE, description = "잔액조회(외환) 응답부", captureSystem = "OLTP")
public class CbIbk01H11404Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "ItemName", name = "과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ItemName;

    @MessageField(id = "CustName", name = "예금주", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "AdminBranch", name = "관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AdminBranch;

    @MessageField(id = "AdminBranchName", name = "관리점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AdminBranchName;

    @MessageField(id = "NewAmt", name = "신규금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NewAmt;

    @MessageField(id = "LedgerBalance", name = "원장잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LedgerBalance;

    @MessageField(id = "FinalTrxDate", name = "최종거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FinalTrxDate;

    @MessageField(id = "FirstNewDate", name = "최초신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FirstNewDate;

    @MessageField(id = "AcctState", name = "계좌상태", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctState;

    @MessageField(id = "Curcy", name = "통화명", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Curcy;

    @MessageField(id = "BalProofBasisDay", name = "잔액증명기준일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BalProofBasisDay;

    @MessageField(id = "Rate", name = "이율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Rate;

    @MessageField(id = "FirstExpiryDay", name = "최초만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FirstExpiryDay;

    @MessageField(id = "FinalExpiryDay", name = "최종만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FinalExpiryDay;

    @MessageField(id = "RolloverCnt", name = "기한연장건수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RolloverCnt;

    @MessageField(id = "ConvertBalance", name = "원화환산원장잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ConvertBalance;

    @MessageField(id = "YOMGIGYB", name = "만기자동입금여부 (1:자동입금계좌)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMGIGYB;

    @MessageField(id = "YODUMMY", name = "dummy", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;
}
