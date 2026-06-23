package com.scbank.process.api.svc.common.service.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.svc.common.components.NoticeComponent;
import com.scbank.process.api.svc.common.dao.Ma30BbsMyDataItemDao;
import com.scbank.process.api.svc.common.dao.dto.NoticeForPopupResult;
import com.scbank.process.api.svc.common.mapper.SettingsEmergencyMapper;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgListNoticeForPopupRequest;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgListNoticeForPopupResponse;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchRequest;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchResponse;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class SettingsEmergencyServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private NoticeComponent noticeComponent;

    @Mock
    private Ma30BbsMyDataItemDao ma30BbsMyDataItemDao;

    @Mock
    private SettingsEmergencyMapper settingsEmergencyMapper;

    @InjectMocks
    private SettingsEmergencyService settingsEmergencyService;

    @Test
    @DisplayName("긴급공지 조회")
    public void shouldSearch() {
        SetEmgSearchRequest input = new SetEmgSearchRequest();
        SetEmgSearchResponse actualValue = settingsEmergencyService.search(ctx, input);

        // assert scenario
        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("팝업용 긴급공지 조회")
    public void shouldListNoticeForPopup() {
        SetEmgListNoticeForPopupRequest input = new SetEmgListNoticeForPopupRequest();
        List<NoticeForPopupResult> result = new ArrayList<>();
        List<SetEmgListNoticeForPopupResponse.NoticeForPopup> popupResult = new ArrayList<>();
        when(this.ma30BbsMyDataItemDao.selectNoticeForPopup()).thenReturn(result);
        when(this.settingsEmergencyMapper.toSetEmgListNoticeForPopupResponse(result))
                .thenReturn(popupResult);

        SetEmgListNoticeForPopupResponse actualValue = settingsEmergencyService.listNoticeForPopup(ctx, input);

        // assert scenario
        assertThat(actualValue).isNotNull();
    }

}
