package com.scbank.process.api.svc.shared.components.account.dto.session;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FundAccountInfoSession {

    /**
     * 계좌번호
     */
    @JsonProperty("DrawAcctNum")
    private String DrawAcctNum;

    /**
     * 종별
     */
    @JsonProperty("Assort")
    private String Assort;

    /**
     * 계좌종류
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
     * 통화
     */
    @JsonProperty("Curcy")
    private String Curcy;

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
     * 구분
     */
    @JsonProperty("Gubun")
    private String Gubun;

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
     * 평가금액부호
     */
    @JsonProperty("EstimateAmountSign")
    private String EstimateAmountSign;

    /**
     * 평가금액
     */
    @JsonProperty("EstimateAmount")
    private String EstimateAmount;

    /**
     * 누적수익율 잔액
     */
    @JsonProperty("SumReciveRateSign")
    private String SumReciveRateSign;

    /**
     * 누적수익율
     */
    @JsonProperty("SumReciveRate")
    private Integer SumReciveRate;

    /**
     * 잔존좌수부호
     */
    @JsonProperty("AliveAccountSign")
    private String AliveAccountSign;

    /**
     * 잔존좌수
     */
    @JsonProperty("AliveAccount")
    private String AliveAccount;

    /**
     * 매매기준가격
     */
    @JsonProperty("SaleStandardPrice")
    private String SaleStandardPrice;

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
     * 지급부호
     */
    @JsonProperty("BalSign1")
    private String BalSign1;

    /**
     * 지급잔액
     */
    @JsonProperty("Balance1")
    private BigDecimal Balance1;

    /**
     * 신규사유
     */
    @JsonProperty("YOSGSAYU")
    private String YOSGSAYU;

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
}
