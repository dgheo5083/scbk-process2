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
@IntegrationMessage(id = "CbIbk01Iq12400Res", type = Type.RESPONSE, description = "통장표제부조회 응답 전문")
public class CbIbk01H12400Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "ItemName", name = "과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ItemName;

    @MessageField(id = "CustName", name = "예금주", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CustName;

    @MessageField(id = "AdminBranchName", name = "관리점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AdminBranchName;

    @MessageField(id = "NewDate", name = "신규일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String NewDate;

    @MessageField(id = "ExpiryDate", name = "만기일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ExpiryDate;

    @MessageField(id = "ContTerm", name = "계약기간", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ContTerm;

    @MessageField(id = "SewooGubun", name = "세금우대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SewooGubun;

    @MessageField(id = "SewooName", name = "세금우대명", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SewooName;

    @MessageField(id = "MonthPayAmt", name = "월부금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal MonthPayAmt;

    @MessageField(id = "FundCode", name = "펀드코드", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundCode;

    @MessageField(id = "YOJONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJONG;

    @MessageField(id = "YOIYUL", name = "약정이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOIYUL;

    @MessageField(id = "YOFIRST", name = "최초신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOFIRST;

    @MessageField(id = "YOBUGI", name = "부기명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBUGI;
}
