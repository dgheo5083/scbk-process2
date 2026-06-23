package com.scbank.process.api.svc.common.components.dto;

import lombok.Data;

@Data
public class VerificationGetFindUserIdDto {
    private String findUserIdYn;
    private String wasTranNo;
    private String phoneNo;
    private String maskPhoneNo;
    private String phoneNo1;
    private String phoneNo2;
    private String phoneNo3;
}
