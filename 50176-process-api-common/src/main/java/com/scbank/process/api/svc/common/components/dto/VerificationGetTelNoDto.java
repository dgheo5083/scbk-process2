package com.scbank.process.api.svc.common.components.dto;

import lombok.Data;

@Data
public class VerificationGetTelNoDto {

    private String wasTranNo;

    private String phoneNo;
    private String maskPhoneNo;
    private String phoneNo1;
    private String phoneNo2;
    private String phoneNo3;

    private String homeTelNo;
    private String maskHomeTelNo;
    private String homeTelNo1;
    private String homeTelNo2;
    private String homeTelNo3;

    private String jobTelNo;
    private String maskJobTelNo;
    private String jobTelNo1;
    private String jobTelNo2;
    private String jobTelNo3;

}
