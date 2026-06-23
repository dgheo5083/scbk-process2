package com.scbank.process.api.svc.shared.components.account.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CardAccountInfoSession {

    /**
     * 계좌번호
     */
    @JsonProperty("DrawAcctNum")
    private String DrawAcctNum;

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
     * 계좌명
     */
    @JsonProperty("DrawAcctName")
    private String DrawAcctName;
}
