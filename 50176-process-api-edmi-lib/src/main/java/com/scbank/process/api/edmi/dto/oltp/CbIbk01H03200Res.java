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
@IntegrationMessage(id = "CbIbk01H03200Res", type = Type.RESPONSE, description = "펀드계좌조회 응답부")
public class CbIbk01H03200Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ContinueInfo", name = "연속정보", length = 90, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ContinueInfo;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "DrawAcctNumListRecord", name = "출력계좌수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DrawAcctNumListRecord;

    @MessageField(id = "Dummy1", name = "더미1", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "REC_01", name = "계좌반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H03200Res/DrawAcctNumListRecord")
    private List<REC_01> REC_01;

    @Getter
    @Setter
    public static class REC_01 implements IMessageObject {
        @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DrawAcctNum;

        @MessageField(id = "Assort", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Assort;

        @MessageField(id = "DepKind", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String DepKind;

        @MessageField(id = "BalSign", name = "잔액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BalSign;

        @MessageField(id = "Balance", name = "잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal Balance;

        @MessageField(id = "Curcy", name = "통화", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String Curcy;

        @MessageField(id = "SavingStartDate", name = "예금신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String SavingStartDate;

        @MessageField(id = "SavingEndDate", name = "예금만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String SavingEndDate;

        @MessageField(id = "Gubun", name = "구분", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String Gubun;

        @MessageField(id = "FundType", name = "펀드종류", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FundType;

        @MessageField(id = "FundCode", name = "펀드코드", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String FundCode;

        @MessageField(id = "EstimateAmountSign", name = "평가금액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String EstimateAmountSign;

        @MessageField(id = "EstimateAmount", name = "평가금액", length = 15)
        private BigDecimal EstimateAmount;

        @MessageField(id = "SumReciveRateSign", name = "누적수익율 잔액", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String SumReciveRateSign;

        @MessageField(id = "SumReciveRate", name = "누적수익율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer SumReciveRate;

        @MessageField(id = "AliveAccountSign", name = "잔존좌수부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AliveAccountSign;

        @MessageField(id = "AliveAccount", name = "잔존좌수", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AliveAccount;

        @MessageField(id = "SaleStandardPrice", name = "매매기준가격", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String SaleStandardPrice;

        @MessageField(id = "UmbrellaGroup", name = "엄블렐러펀드그룹", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String UmbrellaGroup;

        @MessageField(id = "UmbrellaTransferYN", name = "전환가능BIT", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String UmbrellaTransferYN;

        @MessageField(id = "UmbrellaCancelYN", name = "전환등록취소가능", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String UmbrellaCancelYN;

        @MessageField(id = "UmbrellaAutoTransferYN", name = "자동전환등록취소가능", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String UmbrellaAutoTransferYN;

        @MessageField(id = "YOPANYN", name = "판매사이동가능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOPANYN;

        @MessageField(id = "YOMANYN", name = "만기연장가능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOMANYN;

        @MessageField(id = "BalSign1", name = "지급부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String BalSign1;

        @MessageField(id = "Balance1", name = "지급잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal Balance1;

        @MessageField(id = "YOSGSAYU", name = "신규사유", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSGSAYU;

    }
}