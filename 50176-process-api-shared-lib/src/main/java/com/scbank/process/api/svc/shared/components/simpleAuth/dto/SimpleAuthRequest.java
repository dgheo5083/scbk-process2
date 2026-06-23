package com.scbank.process.api.svc.shared.components.simpleAuth.dto;

import lombok.Data;

@Data
public class SimpleAuthRequest {

    private String custId;

    private String serviceId;

    private String trId;

    private String reqDate;

    private String telecomType;

    private String authType;

    private String ctn;

    private String uiccid;

    private String imsi;

    private String imei;

    private String mos;

    private String birthday;

    private String gender;

    private String name;

    private String privacySharingAgreeYn;

    private String thirdPartyProvisionAgreeYn;

}
