package com.scbank.process.api.svc.common.service.functions;

import java.util.List;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.common.service.functions.dto.holiday.FncHldListResponse;
import com.scbank.process.api.svc.common.service.functions.dto.holiday.FncHldListResponse.HolidayInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/functions/holiday", name = "휴일관리")
public class FunctionsHolidayService {

    private final IHolidayManager holidayManager;

    /** ASIS - MA3CMMINF004_100S */
    @ServiceEndpoint(url = "/list", name = "휴일목록조회")
    public FncHldListResponse list(IServiceContext serviceContext) {
        FncHldListResponse response = new FncHldListResponse();

        List<HolidayInfo> holidayList = holidayManager.getHolidayList().stream().map(info -> {
            HolidayInfo holidayInfo = new HolidayInfo();
            holidayInfo.setDate(info.getDate());
            holidayInfo.setDescription(info.getDescription());
            return holidayInfo;
        }).toList();

        response.setHolidayList(holidayList);

        return response;
    }
}
