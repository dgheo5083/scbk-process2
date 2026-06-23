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
@IntegrationMessage(id = "CbTbs07D42100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "자동이체 신청 응답부")
public class CbTbs07D42100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ScPart", name = "신청구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ScPart;

    @MessageField(id = "DrPart", name = "등록구분", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DrPart;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctNum;

    @MessageField(id = "IgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgBankName", name = "입금은행명", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankName;

    @MessageField(id = "IgBankAcctNum", name = "입금은행계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankAcctNum;

    @MessageField(id = "TransOAmt", name = "이체금액(출력)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TransOAmt;

    @MessageField(id = "TransDate", name = "이체일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransDate;

    @MessageField(id = "TransSMon", name = "이체시작월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransSMon;

    @MessageField(id = "TransEMon", name = "이체종료월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TransEMon;

    @MessageField(id = "HgSName", name = "송금인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSName;

    @MessageField(id = "HgRName", name = "수취인명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRName;

    @MessageField(id = "FeeAmt", name = "수수료", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FeeAmt;
}
