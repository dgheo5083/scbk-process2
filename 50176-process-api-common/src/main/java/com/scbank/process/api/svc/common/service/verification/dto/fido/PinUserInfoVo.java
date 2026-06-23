package com.scbank.process.api.svc.common.service.verification.dto.fido;

import lombok.Data;

@Data
public class PinUserInfoVo {

    private boolean requireDeviceInfoUpdate = false;

    private String beforeDeviceId;

    private String afterDeviceId;

    private String userBankingId;

    private String deviceId;

    private String deviceType;

    private String nickName;

    private String executedDeviceInfoUpdateYn = "N";

}
