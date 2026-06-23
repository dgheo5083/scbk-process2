package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class EdocRecoveryInfoParameter {

    private String custNo;

    private String tradNo;

    private String edocCd;

    private String rcvryMk;

    private String rcvryData;

}
