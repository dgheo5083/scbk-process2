package com.scbank.process.api.svc.shared.components.clickToCall.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClickToCallResponse {

    private String soh;

    private String messageLength;

    private String perBusNo;

    private String custName;

    private String servicePath;

    private String callGroup;

    private String custTelNo;

    private String url;

    private String command;

    private String resultCode;

    private String errorCode;

    private String ext;

}
