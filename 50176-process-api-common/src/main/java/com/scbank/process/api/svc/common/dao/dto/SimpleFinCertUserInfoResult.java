package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class SimpleFinCertUserInfoResult {

    private String userId;

    private String connectType;

    private String joinFlg;

    private String finCertSeqNum;

    private String deviceId;

    private String initRegDt;

    private String lstUpdDt;

    private String finCertSmpKeyTkn;

}
