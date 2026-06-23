package com.scbank.process.api.svc.shared.channel.interceptors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.exception.ServiceTimeException;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo.TimeRange;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.utils.DateUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServiceEndpointTimeCheckInterceptorTest {

	@Mock private IHolidayManager holidayManager;

	@InjectMocks private ServiceEndpointTimeCheckInterceptor interceptor;

	private final HttpServletRequest request = mock(HttpServletRequest.class);
	private final HttpServletResponse response = mock(HttpServletResponse.class);

	@Nested
	@DisplayName("요청 서비스 이용시간 체크 preHandle")
	class PreHandle {

		@Test
		@DisplayName("서비스 정의 메타데이터가 없으면 통과")
		public void noDefinitionTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(null);

				assertTrue(interceptor.preHandle(request, response, new Object()));
			}
		}

		@Test
		@DisplayName("서비스이용시간정보가 없으면 통과")
		public void noServiceTimeTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				ServiceDefinitionMetadata definition = mock(ServiceDefinitionMetadata.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(definition);
				when(definition.getServiceTime()).thenReturn(null);

				assertTrue(interceptor.preHandle(request, response, new Object()));
			}
		}

		@Test
		@DisplayName("서비스이용시간정보가 비활성화면 통과")
		public void disabledServiceTimeTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				ServiceDefinitionMetadata definition = mock(ServiceDefinitionMetadata.class);
				ServiceTimeInfo serviceTime = mock(ServiceTimeInfo.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(definition);
				when(definition.getServiceTime()).thenReturn(serviceTime);
				when(serviceTime.enabled()).thenReturn(false);

				assertTrue(interceptor.preHandle(request, response, new Object()));
			}
		}

		@Test
		@DisplayName("영업일/휴일 이용시간 설정이 없으면(timeRange null) 통과 - 영업일")
		public void noTimeRangeBusinessDayTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				ServiceDefinitionMetadata definition = mock(ServiceDefinitionMetadata.class);
				ServiceTimeInfo serviceTime = mock(ServiceTimeInfo.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(definition);
				when(definition.getServiceTime()).thenReturn(serviceTime);
				when(serviceTime.enabled()).thenReturn(true);

				dateUtils.when(DateUtils::getCurrentDate).thenReturn("20260601");
				dateUtils.when(() -> DateUtils.isHoliday(anyString())).thenReturn(false);
				when(holidayManager.isHoliday(anyString())).thenReturn(false);
				when(serviceTime.businessDay()).thenReturn(null);

				assertTrue(interceptor.preHandle(request, response, new Object()));
			}
		}

		@Test
		@DisplayName("startTime/endTime 미설정 시 통과")
		public void emptyTimeTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				ServiceDefinitionMetadata definition = mock(ServiceDefinitionMetadata.class);
				ServiceTimeInfo serviceTime = mock(ServiceTimeInfo.class);
				TimeRange timeRange = mock(TimeRange.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(definition);
				when(definition.getServiceTime()).thenReturn(serviceTime);
				when(serviceTime.enabled()).thenReturn(true);

				dateUtils.when(DateUtils::getCurrentDate).thenReturn("20260601");
				dateUtils.when(() -> DateUtils.isHoliday(anyString())).thenReturn(false);
				when(holidayManager.isHoliday(anyString())).thenReturn(false);
				when(serviceTime.businessDay()).thenReturn(timeRange);
				when(timeRange.startTime()).thenReturn("");
				when(timeRange.endTime()).thenReturn("");

				assertTrue(interceptor.preHandle(request, response, new Object()));
			}
		}

		@Test
		@DisplayName("성공_이용시간 내 - 휴일")
		public void validHolidayTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				ServiceDefinitionMetadata definition = mock(ServiceDefinitionMetadata.class);
				ServiceTimeInfo serviceTime = mock(ServiceTimeInfo.class);
				TimeRange timeRange = mock(TimeRange.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(definition);
				when(definition.getServiceTime()).thenReturn(serviceTime);
				when(serviceTime.enabled()).thenReturn(true);

				dateUtils.when(DateUtils::getCurrentDate).thenReturn("20260601");
				dateUtils.when(() -> DateUtils.isHoliday(anyString())).thenReturn(true);
				when(serviceTime.holiday()).thenReturn(timeRange);
				when(timeRange.startTime()).thenReturn("0000");
				when(timeRange.endTime()).thenReturn("2359");
				dateUtils.when(() -> DateUtils.isNowBetweenHHmm("0000", "2359")).thenReturn(true);

				assertTrue(interceptor.preHandle(request, response, new Object()));
			}
		}

		@Test
		@DisplayName("실패_이용가능시간 외 요청은 ServiceTimeException 발생")
		public void outOfBusinessHoursTest() throws Exception {
			try (MockedStatic<ServiceContextHolder> holder = mockStatic(ServiceContextHolder.class);
				 MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
				IServiceContext ctx = mock(IServiceContext.class);
				ServiceDefinitionMetadata definition = mock(ServiceDefinitionMetadata.class);
				ServiceTimeInfo serviceTime = mock(ServiceTimeInfo.class);
				TimeRange timeRange = mock(TimeRange.class);
				holder.when(ServiceContextHolder::getContext).thenReturn(ctx);
				when(ctx.serviceDefinition()).thenReturn(definition);
				when(definition.getServiceTime()).thenReturn(serviceTime);
				when(serviceTime.enabled()).thenReturn(true);

				dateUtils.when(DateUtils::getCurrentDate).thenReturn("20260601");
				dateUtils.when(() -> DateUtils.isHoliday(anyString())).thenReturn(false);
				when(holidayManager.isHoliday(anyString())).thenReturn(false);
				when(serviceTime.businessDay()).thenReturn(timeRange);
				when(timeRange.startTime()).thenReturn("0900");
				when(timeRange.endTime()).thenReturn("1800");
				dateUtils.when(() -> DateUtils.isNowBetweenHHmm("0900", "1800")).thenReturn(false);

				assertThrows(ServiceTimeException.class,
						() -> interceptor.preHandle(request, response, new Object()));
			}
		}
	}
}
