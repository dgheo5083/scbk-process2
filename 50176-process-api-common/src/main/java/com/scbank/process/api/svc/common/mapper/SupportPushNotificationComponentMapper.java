package com.scbank.process.api.svc.common.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.scbank.process.api.svc.common.components.dto.SupPntListPushCustomerRateRegistInfoResponse;
import com.scbank.process.api.svc.common.dao.dto.SelectExratePushDataResult;

@Mapper(componentModel = "spring")
public interface SupportPushNotificationComponentMapper {

    List<SupPntListPushCustomerRateRegistInfoResponse.ExrateData> toExrateDataList(
            List<SelectExratePushDataResult> dataList);

}
