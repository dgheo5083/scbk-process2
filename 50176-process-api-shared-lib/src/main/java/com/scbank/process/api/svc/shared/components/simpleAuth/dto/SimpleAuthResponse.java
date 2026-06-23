package com.scbank.process.api.svc.shared.components.simpleAuth.dto;

import lombok.Data;

@Data
public class SimpleAuthResponse {

    private String custId;

    private String serviceid;

    private String trId;

    private String reqDate;

    private String resultCode;

    private String resultMsg;

    private String authYn;
}
