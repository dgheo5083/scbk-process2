package com.scbank.process.api.svc.shared.components.account.dto.session;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoanAccountInfoSession {

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
     * 대출신규일
     */
    @JsonProperty("LoanStartDate")
    private String LoanStartDate;

    /**
     * 대출만기일
     */
    @JsonProperty("LoanEndDate")
    private String LoanEndDate;

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
    private String LoanRate;

    /**
     * 계좌명
     */
    @JsonProperty("DrawAcctName")
    private String DrawAcctName;

    @JsonProperty("LoanAcctKmCd")
    private String LoanAcctKmCd;

    @JsonProperty("LoanKind")
    private String LoanKind;
}
