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
@IntegrationMessage(id = "CbTbs03D22800Res", type = Type.RESPONSE, description = "예약이체 변경 본처리 응답 전문")
public class CbTbs03D22800Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TransJJDate", name = "이체일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransJJDate;

    @MessageField(id = "IChDate", name = "이체등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String IChDate;

    @MessageField(id = "TransJJTime", name = "이체시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransJJTime;

    @MessageField(id = "TransSeriano", name = "접수일련번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransSeriano;

    @MessageField(id = "RCgAcctNum", name = "출금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RCgAcctNum;

    @MessageField(id = "ClientName", name = "의뢰인성명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientName;

    @MessageField(id = "PayGB", name = "지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PayGB;

    @MessageField(id = "RecDepBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecDepBankCode;

    @MessageField(id = "RecDepBankName", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecDepBankName;

    @MessageField(id = "RIgBankAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RIgBankAcctNum;

    @MessageField(id = "OrgAmt", name = "수정전금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal OrgAmt;

    @MessageField(id = "ModifyAmt", name = "수정후금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ModifyAmt;

    @MessageField(id = "RSgFee", name = "이체수수료", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal RSgFee;

    @MessageField(id = "RecipientName", name = "수취인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RecipientName;
}
