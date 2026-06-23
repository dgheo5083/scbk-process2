package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H97700Res;
import com.scbank.process.api.svc.common.service.support.dto.businesshour.SupBzhGetWorkingDayResponse;

@Mapper(componentModel = "spring")
public interface SupportBusinessHourMapper {

    @Mapping(source = "YOUSID", target = "yoUSID")
    @Mapping(source = "YOYOGUHs", target = "yoYOGUHs")
    @Mapping(source = "YOYOGU", target = "yoYOGU")
    @Mapping(source = "YOSILJA", target = "yoSILJA")
    @Mapping(source = "YOEILJA", target = "yoEILJA")
    @Mapping(source = "YOPHGB", target = "yoPHGB")
    SupBzhGetWorkingDayResponse toSupBzhGetWorkingDayResponse(CbIbk01H97700Res dto);

}
