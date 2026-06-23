package com.scbank.process.api.svc.common.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.scbank.process.api.svc.common.service.verification.dto.additional.DeviceAuthInfo;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;

@Mapper(componentModel = "spring")
public interface VerificationAdditionalMapper {

    List<DeviceAuthInfo> toDeviceAuthInfo(List<DeviceAuthUserResult> deviceAuthUserList);

}
