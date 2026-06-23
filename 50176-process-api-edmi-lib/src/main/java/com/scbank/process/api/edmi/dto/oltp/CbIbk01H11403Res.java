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
@IntegrationMessage(id = "CbIbk01H11403Res", type = Type.RESPONSE, description = "정기예금상세조회 응답부", captureSystem = "OLTP")
public class CbIbk01H11403Res implements IMessageObject {

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

    @MessageField(id = "AdminBranchName", name = "점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AdminBranchName;

    @MessageField(id = "NewDate", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NewDate;

    @MessageField(id = "ExpiryDate", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ExpiryDate;

    @MessageField(id = "ContAmt", name = "계약금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ContAmt;

    @MessageField(id = "ContTerm", name = "계약기간", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ContTerm;

    @MessageField(id = "PayTime", name = "납입회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer PayTime;

    @MessageField(id = "LedgerBalSign", name = "원장잔액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LedgerBalSign;

    @MessageField(id = "LedgerBalance", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LedgerBalance;

    @MessageField(id = "MonthPayAmt", name = "월부금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal MonthPayAmt;

    @MessageField(id = "FinalTrxDate", name = "최종납입일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FinalTrxDate;

    @MessageField(id = "FinalPayTime", name = "최종납입월차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FinalPayTime;

    @MessageField(id = "PayDelayYN", name = "납입지연여부(지연)", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PayDelayYN;

    @MessageField(id = "PayDelayTime", name = "납입지연회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer PayDelayTime;

    @MessageField(id = "PayDelayAmt", name = "납입지연금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal PayDelayAmt;

    @MessageField(id = "Rate", name = "이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Rate;

    @MessageField(id = "SewooGubun", name = "세금우대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SewooGubun;

    @MessageField(id = "SewooName", name = "세금우대명", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SewooName;

    @MessageField(id = "PreDelayCnt", name = "선납지연일수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PreDelayCnt;

    @MessageField(id = "PreDelayGB", name = "선납지연여부(1:선납, 2:지연)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PreDelayGB;

    @MessageField(id = "AutoIchDay", name = "자동이체일수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AutoIchDay;

    @MessageField(id = "AutoIchGB", name = "자동이체구분(1:선납, 2:매일)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AutoIchGB;

    @MessageField(id = "JtRate", name = "현재 적용이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JtRate;

    @MessageField(id = "YOPERIOD", name = "기간", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOPERIOD;

    @MessageField(id = "YOTJGUBN", name = "통지구분(1: EMAIL,2: SMS,3: E-MAIL & SMS,4:  미등록)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTJGUBN;

    @MessageField(id = "YOPNTGB", name = "1:M POINT,3:SKYPASS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPNTGB;

    @MessageField(id = "YODUMMY", name = "", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

    @MessageField(id = "YOGIHAP", name = "정부기여금합계", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGIHAP;

    @MessageField(id = "YOZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZONG;

    @MessageField(id = "YOJHMIL", name = "항공사마일리지", length = 15, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String YOJHMIL;

    @MessageField(id = "YODUMMY2", name = "", length = 98, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY2;

}
