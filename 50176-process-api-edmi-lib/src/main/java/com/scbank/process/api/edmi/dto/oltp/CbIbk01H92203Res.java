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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H92203Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "이체한도 조회/변경 응답 전문")
public class CbIbk01H92203Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "ReferPart", name = "조회구분", length = 1)
    private String ReferPart;

    @MessageField(id = "TimeDepLimt", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TimeDepLimt;

    @MessageField(id = "DayDepLimt", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal DayDepLimt;

    @MessageField(id = "RCount", name = "출력명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "K_ServiceCode", name = "서비스코드", length = 3)
    private String K_ServiceCode;

    @MessageField(id = "K_DepPayPart", name = "입지급구분", length = 1)
    private String K_DepPayPart;

    @MessageField(id = "K_DrawBankCode", name = "은행코드", length = 3)
    private String K_DrawBankCode;

    @MessageField(id = "K_DrawAcctNum", name = "계좌번호", length = 16)
    private String K_DrawAcctNum;

    @MessageField(id = "K_DepBankCode", name = "입금계좌은행코드", length = 3)
    private String K_DepBankCode;

    @MessageField(id = "K_DepAcctNum", name = "입금계좌번호", length = 16)
    private String K_DepAcctNum;

    @MessageField(id = "DrawAcctRecord", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H92203Res/RCount")
    private List<DrawAcctRecord_REC_GRID> DrawAcctRecord;

    @Getter
    @Setter
    public static class DrawAcctRecord_REC_GRID implements IMessageObject {

        @MessageField(id = "AcctNum", name = "계좌번호", length = 16)
        private String AcctNum;

        @MessageField(id = "RegiBranch", name = "등록점", length = 14, multiBytes = true)
        private String RegiBranch;

        @MessageField(id = "RegiDate", name = "등록일", length = 8)
        private String RegiDate;

        @MessageField(id = "CheckApprBeDrawLimit", name = "자기앞수표결제전지급한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal CheckApprBeDrawLimit;

        @MessageField(id = "CheckApprBeDrawDate", name = "자기앞수표결제전지급기일", length = 8)
        private String CheckApprBeDrawDate;

        @MessageField(id = "DepAcctDesignYN", name = "입금계좌지정여부", length = 18, multiBytes = true)
        private String DepAcctDesignYN;

    }
}
