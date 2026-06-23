package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class InsertPushCllctUsageAgrmntHistoryParameter {

    private String ssn;

    private String usrNo;

    private String agrmntMk;

    private String chnlgb;

    private String updClerkNo;

}
