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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs03H29300Res", type = Type.RESPONSE, description = "이체결과조회 응답 전문")
public class CbTbs03H29300Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "ReferPart", name = "ReferPart", length = 1)
    private String ReferPart;

    @MessageField(id = "CustName", name = "CustName", length = 22, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "ReferStartDate", name = "ReferStartDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferStartDate;

    @MessageField(id = "ReferEndDate", name = "ReferEndDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String ReferEndDate;

    @MessageField(id = "TransAmtSum", name = "TransAmtSum", length = 15)
    private BigDecimal TransAmtSum;

    @MessageField(id = "FeeSum", name = "FeeSum", length = 7)
    private BigDecimal FeeSum;

    @MessageField(id = "ReferGubun", name = "ReferGubun", length = 1)
    private String ReferGubun;

    @MessageField(id = "RCgAcctNum", name = "RCgAcctNum", length = 16, masking = true, maskingType = "02")
    private String RCgAcctNum;

    @MessageField(id = "RecDepBankCode", name = "RecDepBankCode", length = 3)
    private String RecDepBankCode;

    @MessageField(id = "Rcount", name = "Rcount", length = 2)
    private int Rcount;

    @MessageField(id = "YOPAGE", name = "YOPAGE", length = 15)
    private int YOPAGE;

    @MessageField(id = "YOTPAGE", name = "YOTPAGE", length = 15)
    private int YOTPAGE;

    @MessageField(id = "KeyTransSpecDate", name = "KeyTransSpecDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String KeyTransSpecDate;

    @MessageField(id = "KeyTransFinalDate", name = "KeyTransFinalDate", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
    private String KeyTransFinalDate;

    @MessageField(id = "KeyTransAmtSum", name = "KeyTransAmtSum", length = 13)
    private BigDecimal KeyTransAmtSum;

    @MessageField(id = "KeyFeeSum", name = "KeyFeeSum", length = 9)
    private BigDecimal KeyFeeSum;

    @MessageField(id = "KeyReceiptNum", name = "KeyReceiptNum", length = 4)
    private int KeyReceiptNum;

    @MessageField(id = "REC_01", name = "REC_01")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03H29300Res/Rcount")
    private List<Ti1tbs03H293ResGrid> REC_01;

    /**
     * 이체결과조회 응답 반복부 DTO
     */
    @Getter
    @Setter
    public static class Ti1tbs03H293ResGrid implements IMessageObject {

        @MessageField(id = "RecTransDate", name = "이체일자", length = 8, padding = StringUtils.ZERO, align = AlignType.RIGHT)
        private String RecTransDate;

        @MessageField(id = "RecReceiptNum", name = "접수번호", length = 4, padding = StringUtils.ZERO, align = AlignType.RIGHT)
        private String RecReceiptNum;

        @MessageField(id = "RecTransTime", name = "이체시간", length = 6, padding = StringUtils.ZERO, align = AlignType.RIGHT)
        private String RecTransTime;

        @MessageField(id = "RecDrawAcctNum", name = "출금계좌번호", length = 16, masking = true, maskingType = "02")
        private String RecDrawAcctNum;

        @MessageField(id = "RecDepBankCode", name = "입금은행코드", length = 3)
        private String RecDepBankCode;

        @MessageField(id = "RecDepBankName", name = "입금은행명", length = 6, multiBytes = true)
        private String RecDepBankName;

        @MessageField(id = "RecDepAcctNum", name = "입금계좌번호", length = 16, masking = true, maskingType = "02")
        private String RecDepAcctNum;

        @MessageField(id = "RecTransAmt", name = "이체금액", length = 13)
        private BigDecimal RecTransAmt;

        @MessageField(id = "RecFeeAmt", name = "수수료", length = 6)
        private BigDecimal RecFeeAmt;

        @MessageField(id = "RecRecipientName", name = "수취인명", length = 8, masking = true, maskingType = "04")
        private String RecRecipientName;

        @MessageField(id = "RecProcessState", name = "처리상태", length = 8)
        private String RecProcessState;

        @MessageField(id = "RecProcessCode", name = "영문처리상태", length = 3)
        private String RecProcessCode;

        @MessageField(id = "HgRNam1", name = "입금인이름", length = 22, multiBytes = true)
        private String HgRNam1;

        @MessageField(id = "FeeCode", name = "수수료구분", length = 1)
        private String FeeCode;

        @MessageField(id = "YOMBIGO", name = "입금통장표기내용", length = 26)
        private String YOMBIGO;

        @MessageField(id = "YOCMSCD", name = "CMS코드", length = 20)
        private String YOCMSCD;

        @MessageField(id = "YOMICJR", name = "이체구분", length = 18)
        private String YOMICJR;
    }
}
