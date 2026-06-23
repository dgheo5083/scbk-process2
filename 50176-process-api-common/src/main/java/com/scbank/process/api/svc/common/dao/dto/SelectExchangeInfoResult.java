package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class SelectExchangeInfoResult {

    private String stateName;

    private String currency;

    private String dateTime;

    private String buyRate;

    private String ttBuyRate;

    private String ttSaleRate;

    private String cashBuyRate;

    private String cashSaleRate;

    private String usChangeRate;

    private String yyChangeRate;

    private String tmpRate;

}
