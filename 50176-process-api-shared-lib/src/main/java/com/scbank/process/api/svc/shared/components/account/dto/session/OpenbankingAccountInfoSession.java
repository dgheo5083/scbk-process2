package com.scbank.process.api.svc.shared.components.account.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OpenbankingAccountInfoSession {

    /**
     * 은행코드
     */
    @JsonProperty("BankCd")
    private String BankCd;

    /**
     * 은행명
     */
    @JsonProperty("BankNm")
    private String BankNm;

    /**
     * 은행이미지
     */
    @JsonProperty("BankImg")
    private String BankImg;

    /**
     * 동의상태
     */
    @JsonProperty("AgrSts")
    private String AgrSts;

    /**
     * 핀테크이용번호
     */
    @JsonProperty("FintechUseNum")
    private String FintechUseNum;

    /**
     * 납부자번호
     */
    @JsonProperty("PayerNum")
    private String PayerNum;

    /**
     * 스크래핑기관코드
     */
    @JsonProperty("Fcode")
    private String Fcode;

    /**
     * 계좌번호
     */
    @JsonProperty("AcctNo")
    private String AcctNo;

    /**
     * 계좌명
     */
    @JsonProperty("AcctNm")
    private String AcctNm;

    /**
     * 계좌별명
     */
    @JsonProperty("AcctComment")
    private String AcctComment;

    /**
     * 최근 업데이트 시간
     */
    @JsonProperty("UpdateTime")
    private String UpdateTime;

    /**
     * 최초등록시간
     */
    @JsonProperty("RegistTime")
    private String RegistTime;

    /**
     * 동의만료여부
     */
    @JsonProperty("AgreeExpireFlag")
    private String AgreeExpireFlag;

    /**
     * 동의만료일자
     */
    @JsonProperty("AgreeExpireDate")
    private String AgreeExpireDate;
}
