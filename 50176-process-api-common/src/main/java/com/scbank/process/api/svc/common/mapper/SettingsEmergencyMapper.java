package com.scbank.process.api.svc.common.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.scbank.process.api.svc.common.dao.dto.NoticeForPopupResult;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgListNoticeForPopupResponse;

@Mapper(componentModel = "spring")
public interface SettingsEmergencyMapper {
    List<SetEmgListNoticeForPopupResponse.NoticeForPopup> toSetEmgListNoticeForPopupResponse(
            List<NoticeForPopupResult> list);
}
