package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

@Data
public class DeviceAuthUserResult {

    private String userId;
    private String otherpcYes;
    private String otherpcStdt;
    private String otherpcEndt;

    private String deviceAlias;
    private String macAddr;
    private String regDt;
    private String ipAddr;
    private String hddSrlNo;
}
