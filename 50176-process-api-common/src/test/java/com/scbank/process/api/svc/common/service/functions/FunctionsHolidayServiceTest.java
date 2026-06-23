package com.scbank.process.api.svc.common.service.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import com.scbank.process.api.fw.common.holiday.IHolidayInfo;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.svc.common.service.functions.dto.holiday.FncHldListResponse;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FunctionsHolidayService")
class FunctionsHolidayServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private IHolidayManager holidayManager;

    @InjectMocks
    private FunctionsHolidayService service;

    @Test
    @DisplayName("휴일목록 조회")
    void listTest() {
        IHolidayInfo holidayInfo = org.mockito.Mockito.mock(IHolidayInfo.class);
        when(holidayInfo.getDate()).thenReturn("20250101");
        when(holidayInfo.getDescription()).thenReturn("신정");
        when(holidayManager.getHolidayList()).thenReturn(List.of(holidayInfo));

        FncHldListResponse response = service.list(ctx);

        assertThat(response.getHolidayList()).hasSize(1);
        assertThat(response.getHolidayList().get(0).getDate()).isEqualTo("20250101");
        assertThat(response.getHolidayList().get(0).getDescription()).isEqualTo("신정");
    }
}
