package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PinUsrMgtUpdateParameter {

    private String userBankingId;

    private String beforeDeviceId;

    private String afterDeviceId;

}
