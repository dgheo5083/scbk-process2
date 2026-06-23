package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceAuthUserParameter {

    private String serviceNo;

    private String userId;

    private String userNm;

    private String macAddress;

    private String hddSrlNo;

    private String deviceAlias;

    private String phoneNo;

    private String regGubun;

    private String deviceClass;
}
