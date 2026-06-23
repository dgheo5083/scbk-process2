package com.scbank.process.api.svc.shared.components.account.dto;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "AllAccountInfo", type = Type.RESPONSE, description = "전계좌목록조회 응답")

public class AllAccountInfo implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "assort", name = "계좌종별")
    private String assort;

    @MessageField(id = "drawYn", name = "출금여부", defaultValue = "N")
    private String drawYn;

    @MessageField(id = "depKind", name = "계좌종류(1:입출금, 2:예금, 3:BC카드, H:현대카드)")
    private String depKind;

    @MessageField(id = "balSign", name = "잔액부호")
    private String balSign;

    @MessageField(id = "balance", name = "잔액")
    private BigDecimal balance;

    @MessageField(id = "curcy", name = "통화코드")
    private String curcy;

    @MessageField(id = "drawAcctNameAlias", name = "계좌별명/제휴카드명")
    private String drawAcctNameAlias;

    @MessageField(id = "acctDisplay", name = "송금인명지정방법")
    private String acctDisplay;

    @MessageField(id = "savingStartDate", name = "예금신규일")
    private String savingStartDate;

    @MessageField(id = "savingEndDate", name = "예금만기일")
    private String savingEndDate;

    @MessageField(id = "fundGubun", name = "펀드구분")
    private String fundGubun;

    @MessageField(id = "fundType", name = "펀드종류")
    private String fundType;

    @MessageField(id = "fundCode", name = "펀드코드")
    private String fundCode;

    @MessageField(id = "custodianAmountSign", name = "수탁금액Sign")
    private String custodianAmountSign;

    @MessageField(id = "custodianAmount", name = "수탁금액")
    private BigDecimal custodianAmount;

    @MessageField(id = "estimateAmountSign", name = "평가금액Sign")
    private String estimateAmountSign;

    @MessageField(id = "estimateAmount", name = "평가금액")
    private BigDecimal estimateAmount;

    @MessageField(id = "sumReciveRateSign", name = "누적수익률Sign")
    private String sumReciveRateSign;

    @MessageField(id = "sumReciveRate", name = "누적수익률")
    private BigDecimal sumReciveRate;

    @MessageField(id = "aliveAccountSign", name = "잔존좌수Sign")
    private String aliveAccountSign;

    @MessageField(id = "aliveAccount", name = "잔존좌수")
    private BigDecimal aliveAccount;

    @MessageField(id = "saleStandardPriceSign", name = "매매기준가격Sign")
    private String saleStandardPriceSign;

    @MessageField(id = "saleStandardPrice", name = "매매기준가격")
    private BigDecimal saleStandardPrice;

    @MessageField(id = "umbrellaGroup", name = "엄블렐러펀드그룹")
    private String umbrellaGroup;

    @MessageField(id = "umbrellaTransferYn", name = "전환가능BIT")
    private String umbrellaTransferYn;

    @MessageField(id = "umbrellaCancelYn", name = "전환등록취소가능")
    private String umbrellaCancelYn;

    @MessageField(id = "umbrellaAutoTransferYn", name = "자동전환등록취소가능")
    private String umbrellaAutoTransferYn;

    @MessageField(id = "yopanyn", name = "판매사이동가능여부")
    private String yopanyn;

    @MessageField(id = "yomanyn", name = "만기연장가능여부")
    private String yomanyn;

    @MessageField(id = "paymentEstimateAmountSign", name = "기지급분평가금액Sign")
    private String paymentEstimateAmountSign;

    @MessageField(id = "paymentEstimateAmount", name = "기지급분평가금액")
    private BigDecimal paymentEstimateAmount;

    @MessageField(id = "loanBalanceSign", name = "대출잔액부호")
    private String loanBalanceSign;

    @MessageField(id = "loanBalance", name = "대출잔액")
    private BigDecimal loanBalance;

    @MessageField(id = "loanRepayPrinAmt", name = "승인한도")
    private BigDecimal loanRepayPrinAmt;

    @MessageField(id = "expectedDate", name = "이자예정일")
    private String expectedDate;

    @MessageField(id = "loanRate", name = "이율")
    private BigDecimal loanRate;

    @MessageField(id = "cardType", name = "카드종류")
    private String cardType;

    @MessageField(id = "familyType", name = "본인가족구분")
    private String familyType;

    @MessageField(id = "paymentDate", name = "결제일자")
    private String paymentDate;

    @MessageField(id = "openDate", name = "개설일자")
    private String openDate;

    @MessageField(id = "period", name = "유효기간")
    private String period;

    @MessageField(id = "checkCardType", name = "체크카드구분")
    private String checkCardType;

    @MessageField(id = "coOperCardCode", name = "제휴카드코드")
    private String coOperCardCode;

    @MessageField(id = "acctType", name = "계좌구분(1:예금, 2:펀드, 3:대출, 4:카드)")
    private String acctType;

    @MessageField(id = "loanKind", name = "대출종류")
    private String loanKind;

    @MessageField(id = "loanAcctKmCD", name = "계정과목코드")
    private String loanAcctKmCD;

    @MessageField(id = "drawAcctName", name = "계좌명")
    private String drawAcctName;

    @MessageField(id = "drawAcctEngName", name = "계좌영문명")
    private String drawAcctEngName;

    @MessageField(id = "drawAcctTrustName", name = "신탁계좌명")
    private String drawAcctTrustName;

    @MessageField(id = "mtsAssetName", name = "MTS 자산명")
    private String mtsAssetName;

    @MessageField(id = "mtsAssetNo", name = "MTS 자산계좌번호")
    private String mtsAssetNo;

    @MessageField(id = "newAccountNum", name = "신규계좌번호")
    private String newAccountNum;

    @MessageField(id = "newAccountName", name = "신규계좌명")
    private String newAccountName;

    @MessageField(id = "newAccountCurcy", name = "신규계좌 통화")
    private String newAccountCurcy;

    @MessageField(id = "h039NewAccountYN", name = "신규계좌생성시 데이터처리를 위한 flag")
    private String h039NewAccountYN;

    @MessageField(id = "investAssetNo", name = "신탁명 운용자산번호")
    private String investAssetNo;
}
