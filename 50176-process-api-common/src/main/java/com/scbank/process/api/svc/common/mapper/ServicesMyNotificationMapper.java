package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;

import com.scbank.process.api.svc.common.service.services.dto.SvcMntListMyNotificationResponse;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;

@Mapper(componentModel = "spring")
public interface ServicesMyNotificationMapper {

    SvcMntListMyNotificationResponse.AccountInfo toAccountInfo(AllAccountInfo allAccountInfo);

}
