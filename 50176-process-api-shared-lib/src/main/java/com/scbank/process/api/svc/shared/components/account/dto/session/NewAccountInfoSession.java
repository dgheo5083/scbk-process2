package com.scbank.process.api.svc.shared.components.account.dto.session;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NewAccountInfoSession {

    /**
     * 신규계좌번호
     */
    @JsonProperty("NEW_ACCOUNT_NUM")
    private String NEW_ACCOUNT_NUM;

    /**
     * 신규계좌명
     */
    @JsonProperty("NEW_ACCOUNT_NAME")
    private String NEW_ACCOUNT_NAME;

    /**
     * 신규계좌통화
     */
    @JsonProperty("NEW_ACCOUNT_CURCY")
    private String NEW_ACCOUNT_CURCY;
    
    /**
     * 신규계좌통화
     */
    @JsonProperty("NEW_ACCOUNT_PRDCT_CD")
    private String NEW_ACCOUNT_PRDCT_CD;
}
