package com.scbank.process.api.svc.shared.components.accesscontrol;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeInfo;
import com.scbank.process.api.fw.common.servicetime.IServiceTimeManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.ServiceTimeCheckRequest;
import com.scbank.process.api.svc.shared.components.emergency.EmergencyIncidentManager;
import com.scbank.process.api.svc.shared.components.emergency.dto.EmergencyIncidentInfo;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class ServiceTimeCheckComponentTest {

	@Mock private IServiceTimeManager serviceTimeManager;
	@Mock private EmergencyIncidentManager emergencyIncidentManager;

	@InjectMocks private ServiceTimeCheckComponent component;

	private MockedStatic<DateUtils> dateUtils;
	private MockedStatic<PropertiesUtils> props;
	private MockedStatic<RuntimeContext> runtime;

	@BeforeEach
	void setUp() {
		dateUtils = Mockito.mockStatic(DateUtils.class);
		props = Mockito.mockStatic(PropertiesUtils.class);
		runtime = Mockito.mockStatic(RuntimeContext.class);

		// 기본 시간 스텁
		dateUtils.when(() -> DateUtils.getCurrentDate("HHmm")).thenReturn("1200");
		dateUtils.when(() -> DateUtils.getCurrentDate("yyyyMMdd")).thenReturn("20260601");
		dateUtils.when(() -> DateUtils.getCurrentDate("yyyyMMddHHmmss")).thenReturn("20260601120000");
		dateUtils.when(DateUtils::getCurrentDate).thenReturn("20260601");
		// 시스템 점검시간 통과 (end <= cur <= start)
		props.when(() -> PropertiesUtils.getString("S_CK_TIME")).thenReturn("2300,0600");
		// 휴일 아님
		runtime.when(() -> RuntimeContext.getBean(IHolidayManager.class)).thenReturn(null);
		// 긴급장애 없음
		when(emergencyIncidentManager.get(anyString(), anyString())).thenReturn(Collections.emptyList());
	}

	@AfterEach
	void tearDown() {
		dateUtils.close();
		props.close();
		runtime.close();
	}

	@Nested
	@DisplayName("checkServiceTime(request)")
	class CheckServiceTime {

		@Test
		@DisplayName("성공_서비스 이용시간 정보 없으면 통과")
		public void successNoServiceTimeInfoTest() {
			when(serviceTimeManager.getServiceTime(any(), anyString())).thenReturn(null);

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			assertDoesNotThrow(() -> component.checkServiceTime(request));
		}

		@Test
		@DisplayName("forceCheckCode 설정 시 code 대체")
		public void forceCheckCodeTest() {
			when(serviceTimeManager.getServiceTime(any(), eq("FORCE01"))).thenReturn(null);

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").forceCheckCode("FORCE01").build();

			assertDoesNotThrow(() -> component.checkServiceTime(request));
			Mockito.verify(serviceTimeManager).getServiceTime(any(), eq("FORCE01"));
		}

		@Test
		@DisplayName("실패_시스템 점검시간이면 PRCCMM0027")
		public void systemCheckTimeTest() {
			props.when(() -> PropertiesUtils.getString("S_CK_TIME")).thenReturn("0900,1800");

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> component.checkServiceTime(request));
			assertTrue(ex.getErrorCode().contains("PRCCMM0027"));
		}

		@Test
		@DisplayName("시스템 점검시간 미설정/형식오류 시 통과")
		public void systemCheckTimeSkipTest() {
			props.when(() -> PropertiesUtils.getString("S_CK_TIME")).thenReturn("0900"); // 길이 2 아님
			when(serviceTimeManager.getServiceTime(any(), anyString())).thenReturn(null);

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			assertDoesNotThrow(() -> component.checkServiceTime(request));
		}

		@Test
		@DisplayName("실패_긴급장애 발생(errCd 없음) PRCCMM0053")
		public void emergencyIncidentDefaultCodeTest() {
			EmergencyIncidentInfo info = Mockito.mock(EmergencyIncidentInfo.class);
			when(info.getStartTm()).thenReturn("20260101");
			when(info.getEndTm()).thenReturn("20261231");
			when(info.getErrCd()).thenReturn("");
			when(emergencyIncidentManager.get(eq("MN"), anyString())).thenReturn(List.of(info));

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> component.checkServiceTime(request));
			assertTrue(ex.getErrorCode().contains("PRCCMM0053"));
		}

		@Test
		@DisplayName("실패_긴급장애 발생(errCd 존재) 해당 코드")
		public void emergencyIncidentCustomCodeTest() {
			EmergencyIncidentInfo info = Mockito.mock(EmergencyIncidentInfo.class);
			when(info.getStartTm()).thenReturn("20260101000000");
			when(info.getEndTm()).thenReturn("20261231235959");
			when(info.getErrCd()).thenReturn("EMG9999");
			when(emergencyIncidentManager.get(eq("MS"), anyString())).thenReturn(List.of(info));

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("message").code("TR001").build();

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> component.checkServiceTime(request));
			assertTrue(ex.getErrorCode().contains("EMG9999"));
		}

		@Test
		@DisplayName("실패_이용시간 외 PRCCMM0028")
		public void outOfServiceTimeTest() {
			IServiceTimeInfo info = Mockito.mock(IServiceTimeInfo.class);
			when(info.getStartTime()).thenReturn("1300");
			when(info.getEndTime()).thenReturn("1400");
			when(info.getChkYn()).thenReturn("Y");
			when(info.getNextPage()).thenReturn("SVC003");
			when(info.getNextPageParameter()).thenReturn("");
			when(serviceTimeManager.getServiceTime(any(), anyString())).thenReturn(info);

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> component.checkServiceTime(request));
			assertTrue(ex.getErrorCode().contains("PRCCMM0028"));
		}

		@Test
		@DisplayName("실패_휴일 이용불가(chkYn=N) PRCCMM0054 + nextPageParameter 파싱")
		public void holidayNotAllowedTest() {
			IHolidayManager holidayManager = Mockito.mock(IHolidayManager.class);
			when(holidayManager.isHoliday(anyString())).thenReturn(true);
			runtime.when(() -> RuntimeContext.getBean(IHolidayManager.class)).thenReturn(holidayManager);

			IServiceTimeInfo info = Mockito.mock(IServiceTimeInfo.class);
			when(info.getStartTime()).thenReturn("0900");
			when(info.getEndTime()).thenReturn("1800");
			when(info.getChkYn()).thenReturn("N");
			when(info.getNextPage()).thenReturn("SVC003");
			when(info.getNextPageParameter()).thenReturn("a=1&b=2");
			when(serviceTimeManager.getServiceTime(any(), anyString())).thenReturn(info);

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			PRCServiceException ex = assertThrows(PRCServiceException.class,
					() -> component.checkServiceTime(request));
			assertTrue(ex.getErrorCode().contains("PRCCMM0054"));
		}

		@Test
		@DisplayName("이용시간 정보 형식 오류(길이 != 4)면 통과")
		public void invalidTimeFormatTest() {
			IServiceTimeInfo info = Mockito.mock(IServiceTimeInfo.class);
			when(info.getStartTime()).thenReturn("900");
			when(info.getEndTime()).thenReturn("1800");
			when(info.getChkYn()).thenReturn("Y");
			when(info.getNextPage()).thenReturn("");
			when(info.getNextPageParameter()).thenReturn("");
			when(serviceTimeManager.getServiceTime(any(), anyString())).thenReturn(info);

			ServiceTimeCheckRequest request = ServiceTimeCheckRequest.builder()
					.type("menu").code("M001").build();

			assertDoesNotThrow(() -> component.checkServiceTime(request));
		}
	}

	@Nested
	@DisplayName("이용시간 체크 무시 여부")
	class CheckIgnore {

		@Test
		@DisplayName("isCheckIgnoreMenuId - 무시 대상이면 true")
		public void ignoreMenuIdTrueTest() {
			try (MockedStatic<PRCSharedUtils> prc = mockStatic(PRCSharedUtils.class)) {
				prc.when(PRCSharedUtils::getMenuId).thenReturn("M001");
				props.when(() -> PropertiesUtils.getString("PAGECHECK_IGNORE_MENU_ID", "")).thenReturn("M001|M002");

				assertTrue(component.isCheckIgnoreMenuId("M001"));
			}
		}

		@Test
		@DisplayName("isCheckIgnoreMenuId - 메뉴ID 없으면 false")
		public void ignoreMenuIdEmptyTest() {
			try (MockedStatic<PRCSharedUtils> prc = mockStatic(PRCSharedUtils.class)) {
				prc.when(PRCSharedUtils::getMenuId).thenReturn("");

				assertFalse(component.isCheckIgnoreMenuId(""));
			}
		}

		@Test
		@DisplayName("isCheckIgnoreMenuId - 목록에 없으면 false")
		public void ignoreMenuIdNotInListTest() {
			try (MockedStatic<PRCSharedUtils> prc = mockStatic(PRCSharedUtils.class)) {
				prc.when(PRCSharedUtils::getMenuId).thenReturn("M999");
				props.when(() -> PropertiesUtils.getString("PAGECHECK_IGNORE_MENU_ID", "")).thenReturn("M001|M002");

				assertFalse(component.isCheckIgnoreMenuId(""));
			}
		}

		@Test
		@DisplayName("isCheckIgnoreRequestUri - 무시 대상 URI면 true")
		public void ignoreUriTrueTest() {
			props.when(() -> PropertiesUtils.getString("PAGECHECK_IGNORE_URI_LIST", "")).thenReturn("/a|/b");
			assertTrue(component.isCheckIgnoreRequestUri("/a"));
		}

		@Test
		@DisplayName("isCheckIgnoreRequestUri - 대상 아니면 false")
		public void ignoreUriFalseTest() {
			props.when(() -> PropertiesUtils.getString("PAGECHECK_IGNORE_URI_LIST", "")).thenReturn("/a|/b");
			assertFalse(component.isCheckIgnoreRequestUri("/c"));
		}
	}
}
