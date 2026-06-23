package com.scbank.process.api.svc.shared.components.account.dto.session;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TrustAccountInfoSession {

    /**
     * 계좌번호
     */
    @JsonProperty("DrawAcctNum")
    private String DrawAcctNum;

    /**
     * 계좌종별
     */
    @JsonProperty("Assort")
    private String Assort;

    /**
     * 출금여부
     */
    @JsonProperty("DrawYN")
    private String DrawYn;

    /**
     * 계좌종류(1:입출금, 2:예금, 3:BC카드, H:현대카드)
     */
    @JsonProperty("DepKind")
    private String DepKind;

    /**
     * 잔액부호
     */
    @JsonProperty("BalSign")
    private String BalSign;

    /**
     * 잔액
     */
    @JsonProperty("Balance")
    private BigDecimal Balance;

    /**
     * 통화코드
     */
    @JsonProperty("Curcy")
    private String Curcy;

    /**
     * 계좌별명/제휴카드명
     */
    @JsonProperty("DrawAcctNameAlias")
    private String DrawAcctNameAlias;

    /**
     * 송금인명지정방법
     */
    @JsonProperty("AcctDisplay")
    private String AcctDisplay;

    /**
     * 예금신규일
     */
    @JsonProperty("SavingStartDate")
    private String SavingStartDate;

    /**
     * 예금만기일
     */
    @JsonProperty("SavingEndDate")
    private String SavingEndDate;

    /**
     * 펀드구분
     */
    @JsonProperty("FundGubun")
    private String FundGubun;

    /**
     * 펀드종류
     */
    @JsonProperty("FundType")
    private String FundType;

    /**
     * 펀드코드
     */
    @JsonProperty("FundCode")
    private String FundCode;

    /**
     * 수탁금액Sign
     */
    @JsonProperty("CustodianAmountSign")
    private String CustodianAmountSign;

    /**
     * 수탁금액
     */
    @JsonProperty("CustodianAmount")
    private BigDecimal CustodianAmount;

    /**
     * 평가금액Sign
     */
    @JsonProperty("EstimateAmountSign")
    private String EstimateAmountSign;

    /**
     * 평가금액
     */
    @JsonProperty("EstimateAmount")
    private BigDecimal EstimateAmount;

    /**
     * 누적수익률Sign
     */
    @JsonProperty("SumReciveRateSign")
    private String SumReciveRateSign;

    /**
     * 평가금액
     */
    @JsonProperty("SumReciveRate")
    private BigDecimal SumReciveRate;

    /**
     * 잔존좌수Sign
     */
    @JsonProperty("AliveAccountSign")
    private String AliveAccountSign;

    /**
     * 잔존좌수
     */
    @JsonProperty("AliveAccount")
    private BigDecimal AliveAccount;

    /**
     * 매매기준가격Sign
     */
    @JsonProperty("SaleStandardPriceSign")
    private String SaleStandardPriceSign;

    /**
     * 매매기준가격
     */
    @JsonProperty("SaleStandardPrice")
    private BigDecimal SaleStandardPrice;

    /**
     * 엄블렐러펀드그룹
     */
    @JsonProperty("UmbrellaGroup")
    private String UmbrellaGroup;

    /**
     * 전환가능BIT
     */
    @JsonProperty("UmbrellaTransferYN")
    private String UmbrellaTransferYN;

    /**
     * 전환등록취소가능
     */
    @JsonProperty("UmbrellaCancelYN")
    private String UmbrellaCancelYN;

    /**
     * 자동전환등록취소가능
     */
    @JsonProperty("UmbrellaAutoTransferYN")
    private String UmbrellaAutoTransferYN;

    /**
     * 판매사이동가능여부
     */
    @JsonProperty("YOPANYN")
    private String YOPANYN;

    /**
     * 만기연장가능여부
     */
    @JsonProperty("YOMANYN")
    private String YOMANYN;

    /**
     * 기지급분평가금액Sign
     */
    @JsonProperty("PaymentEstimateAmountSign")
    private String PaymentEstimateAmountSign;

    /**
     * 기지급분평가금액
     */
    @JsonProperty("PaymentEstimateAmount")
    private BigDecimal PaymentEstimateAmount;

    /**
     * 대출잔액부호
     */
    @JsonProperty("LoanBalanceSign")
    private String LoanBalanceSign;

    /**
     * 대출잔액
     */
    @JsonProperty("LoanBalance")
    private BigDecimal LoanBalance;

    /**
     * 승인한도
     */
    @JsonProperty("LoanRepayPrinAmt")
    private BigDecimal LoanRepayPrinAmt;

    /**
     * 이자예정일
     */
    @JsonProperty("ExpectedDate")
    private String ExpectedDate;

    /**
     * 이율
     */
    @JsonProperty("LoanRate")
    private BigDecimal LoanRate;

    /**
     * 카드종류
     */
    @JsonProperty("CardType")
    private String CardType;

    /**
     * 본인가족구분
     */
    @JsonProperty("FamilyType")
    private String FamilyType;

    /**
     * 결제일자
     */
    @JsonProperty("PaymentDate")
    private String PaymentDate;

    /**
     * 개설일자
     */
    @JsonProperty("OpenDate")
    private String OpenDate;

    /**
     * 유효기간
     */
    @JsonProperty("Period")
    private String Period;

    /**
     * 체크카드구분
     */
    @JsonProperty("CheckCardType")
    private String CheckCardType;

    /**
     * 제휴카드코드
     */
    @JsonProperty("CoOperCardCode")
    private String CoOperCardCode;

    /**
     * 계좌구분(1:예금, 2:펀드, 3:대출, 4:카드)
     */
    @JsonProperty("AcctType")
    private String AcctType;

    /**
     * 대출종류
     */
    @JsonProperty("LoanKind")
    private String LoanKind;

    /**
     * 계정과목코드
     */
    @JsonProperty("LoanAcctKmCD")
    private String LoanAcctKmCD;

    /**
     * 계좌명
     */
    @JsonProperty("DrawAcctName")
    private String DrawAcctName;

    /**
     * 계좌영문명
     */
    @JsonProperty("DrawAcctEngName")
    private String DrawAcctEngName;

    /**
     * 신탁계좌명
     */
    @JsonProperty("DrawAcctTrustName")
    private String DrawAcctTrustName;

    /**
     * MTS 자산명
     */
    @JsonProperty("MTS_AssetName")
    private String mtsAssetName;

    /**
     * MTS 자산계좌번호
     */
    @JsonProperty("MTS_AssetNo")
    private String mtsAssetNo;

    /**
     * 신규계좌번호
     */
    @JsonProperty("newAccountNum")
    private String newAccountNum;

    /**
     * 신규계좌명
     */
    @JsonProperty("newAccountName")
    private String newAccountName;

    /**
     * 신규계좌 통화
     */
    @JsonProperty("newAccountCurcy")
    private String newAccountCurcy;

    /**
     * 신규계좌생성시 데이터처리를 위한 flag
     */
    @JsonProperty("h039NewAccountYN")
    private String h039NewAccountYN;

    /**
     * 신탁명 운용자산번호
     */
    @JsonProperty("InvestAssetNo")
    private String InvestAssetNo;
}
