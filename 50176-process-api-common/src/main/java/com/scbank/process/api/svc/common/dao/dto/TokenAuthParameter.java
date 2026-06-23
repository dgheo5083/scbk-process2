package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class TokenAuthParameter {

    private String ctn;

    private String deviceToken;

    private String moMsg;

    private String moNumber;

    private String moRcvDate;
}
