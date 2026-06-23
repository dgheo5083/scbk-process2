package com.scbank.process.api.svc.shared.components.account.dto.session;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PensionTrustAccountInfoSession {

    @JsonProperty("DrawAcctNum")
    private String drawAcctNum;

    @JsonProperty("DrawAcctName")
    private String drawAcctName;

    @JsonProperty("DrawAcctNameAlias")
    private String drawAcctNameAlias;

    @JsonProperty("Assort")
    private String assort;

    @JsonProperty("DepKind")
    private String depKind;

    @JsonProperty("Curcy")
    private String curcy;

    @JsonProperty("AccountDisplay")
    private String accountDisplay;

    @JsonProperty("BalSign")
    private String balSign;

    @JsonProperty("Balance")
    private BigDecimal balance;

    @JsonProperty("SavingStartDate")
    private String savingStartDate;

    @JsonProperty("SavingEndDate")
    private String savingEndDate;

    @JsonProperty("DrawYn")
    private String drawYn;
}
