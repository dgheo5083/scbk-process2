package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class SelectExratePushDataResult {

    private String seqNo;

    private String puSerno;

    private String gb;

    private String currnm;

    private String type;

    private String fitRate;

    private String pushDay;

    private String pushTime;

}
