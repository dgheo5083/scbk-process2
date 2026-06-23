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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H44401Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드계산서 내역조회")
public class CbIbk01H44401Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "CustName", name = "고객명(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "JuminNo", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String JuminNo;

    @MessageField(id = "TranGubun", name = "거래구분 (0간략조회,1상세)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "CloseAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CloseAcctNum;

    @MessageField(id = "ReferGubun", name = "조회구분(1원가,3지급,4해지,7배당)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ReferGubun;

    @MessageField(id = "ReferStartDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferStartDate;

    @MessageField(id = "ReferEndDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReferEndDate;

    @MessageField(id = "TranDate", name = "상세조회 거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TranDate;

    @MessageField(id = "Index", name = "상세조회 회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer Index;

    @MessageField(id = "RCount", name = "명세건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer RCount;

    @MessageField(id = "REC_01", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H44401Res/RCount")
    private List<REC_01> REC_01;

    @Data
    public static class REC_01 implements IMessageObject {

        @MessageField(id = "RecepitIndex", name = "회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RecepitIndex;

        @MessageField(id = "RecepitTranDate", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RecepitTranDate;

        @MessageField(id = "RecepitSumAmtSign", name = "차감지급액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String RecepitSumAmtSign;

        @MessageField(id = "RecepitSumAmt", name = "차감지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal RecepitSumAmt;

        @MessageField(id = "RecepitBranch", name = "거래점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String RecepitBranch;

    }
}