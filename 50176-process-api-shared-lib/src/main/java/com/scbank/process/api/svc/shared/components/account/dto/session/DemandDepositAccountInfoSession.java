package com.scbank.process.api.svc.shared.components.account.dto.session;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DemandDepositAccountInfoSession {

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
     * 계좌명
     */
    @JsonProperty("DrawAcctName")
    private String DrawAcctName;

    /**
     * 출금여부
     */
    @JsonProperty("DrawYN")
    private String DrawYn;

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
     * 계좌구분(1:예금, 2:펀드, 3:대출, 4:카드)
     */
    @JsonProperty("AcctType")
    private String AcctType;

}
