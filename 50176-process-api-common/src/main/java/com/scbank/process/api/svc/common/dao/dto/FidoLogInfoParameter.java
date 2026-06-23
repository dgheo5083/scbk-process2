package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FidoLogInfoParameter {

    private String deviceMngtId;

    private String regDt;

    private String deviceMac;

    private String hpNum;

    private String deviceImei;

    private String spassYn;

    private String delDt;

}
