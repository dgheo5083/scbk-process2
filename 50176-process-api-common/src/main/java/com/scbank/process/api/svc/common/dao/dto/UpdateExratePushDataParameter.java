package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class UpdateExratePushDataParameter {

    private String type;

    private String fitRate;

    private String pushDay;

    private String pushTime;

    private String puserno;

    private String seqNo;

}
