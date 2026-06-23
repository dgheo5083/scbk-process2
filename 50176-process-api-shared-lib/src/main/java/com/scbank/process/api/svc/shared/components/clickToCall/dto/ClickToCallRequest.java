package com.scbank.process.api.svc.shared.components.clickToCall.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClickToCallRequest {

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String perBusNo;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String custName;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String servicePath;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String callGroup;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String custTelNo;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String url;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String command;

}
