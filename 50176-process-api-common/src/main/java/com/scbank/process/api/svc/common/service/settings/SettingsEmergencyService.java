package com.scbank.process.api.svc.common.service.settings;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.common.components.NoticeComponent;
import com.scbank.process.api.svc.common.dao.Ma30BbsMyDataItemDao;
import com.scbank.process.api.svc.common.mapper.SettingsEmergencyMapper;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgListNoticeForPopupRequest;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgListNoticeForPopupResponse;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchRequest;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "설정 - 긴급공지", url = "/settings/emergency")
public class SettingsEmergencyService {

    private final NoticeComponent noticeComponent;
    private final Ma30BbsMyDataItemDao ma30BbsMyDataItemDao;
    private final SettingsEmergencyMapper settingsEmergencyMapper;

    /* ASIS : MA3CMMNFP001_101S */
    @ServiceEndpoint(url = "/search", name = "긴급공지 조회")
    public SetEmgSearchResponse search(IServiceContext serviceContext, SetEmgSearchRequest request) {

        return noticeComponent.getPmsNotice(request.getCtgryCd());
    }

    @ServiceEndpoint(url = "/listNoticeForPopup", name = "팝업용 긴급공지 조회")
    public SetEmgListNoticeForPopupResponse listNoticeForPopup(IServiceContext serviceContext,
            SetEmgListNoticeForPopupRequest request) {

        SetEmgListNoticeForPopupResponse output = new SetEmgListNoticeForPopupResponse();

        output.setNoticeForPopupList(this.settingsEmergencyMapper
                .toSetEmgListNoticeForPopupResponse(this.ma30BbsMyDataItemDao.selectNoticeForPopup()));
        return output;
    }
}
