package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92500Res;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetAccountViewYnResponse;

@Mapper(componentModel = "spring")
public interface SettingsAppMapper {

    SetAppGetAccountViewYnResponse toSetAppGetAccountViewYnResponse(CbIbk01H92500Res dto);

}
