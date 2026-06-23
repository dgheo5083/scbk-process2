package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class UpdatePushJoinAlarmParameter {

    private String iotranlistFlag;

    private String notyExrateFlg;

    private String benefitFlag;

    private String financeFlag;

    private String financeVal;

    private String wmloungeFlag;

    private String serno;

    private String bnkingId;

}
