package com.scbank.process.api.svc.shared.components.account.dto.session;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RepresentAccountInfoSession {

    /**
     * 계좌번호
     */
    @JsonProperty("DrawAcctNum")
    private String DrawAcctNum;

    /**
     * 계좌명
     */
    @JsonProperty("DrawAcctName")
    private String DrawAcctName;

    /**
     * 잔액
     */
    @JsonProperty("Balance")
    private BigDecimal Balance;

    /**
     * 정렬순서
     */
    @JsonProperty("number")
    private Integer number;

    /**
     * 잔액부호
     */
    @JsonProperty("BalSign")
    private String BalSign;

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

}
