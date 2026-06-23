package com.scbank.process.api.svc.shared.components.device;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.shared.components.device.dto.DeviceAuthInfo;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeviceAuthComponentTest {

	@Mock private DeviceAuthUserDao deviceAuthUserDao;

	@InjectMocks private DeviceAuthComponent component;

	@Nested
	@DisplayName("등록 단말수 조회")
	class GetDeviceAuthCount {

		@Test
		@DisplayName("비로컬 환경 - DAO 조회")
		public void notLocalTest() {
			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);
				when(deviceAuthUserDao.selectDeviceAuthCount(any())).thenReturn(5);

				assertEquals(5, component.getDeviceAuthCount("user01"));
			}
		}

		@Test
		@DisplayName("로컬 환경 - 0 반환")
		public void localTest() {
			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

				assertEquals(0, component.getDeviceAuthCount("user01"));
			}
		}
	}

	@Nested
	@DisplayName("단말기지정 서비스 상태/타단말 접속 여부")
	class GetDeviceAuthInfo {

		@Test
		@DisplayName("미등록(pcFixValue=0) + 등록단말 없음")
		public void notRegisteredTest() {
			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);
				when(deviceAuthUserDao.selectDeviceAuthCount(any())).thenReturn(0);

				DeviceAuthInfo result = component.getDeviceAuthInfoNOtherYn("0", "user01");
				assertNotNull(result);
				assertEquals("N", result.getOtherDeviceYn());
			}
		}

		@Test
		@DisplayName("등록(pcFixValue=2) - 타단말 여부 조회")
		public void registeredTest() {
			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);
				when(deviceAuthUserDao.selectDeviceAuthCount(any())).thenReturn(2);
				DeviceAuthUserResult userResult = mock(DeviceAuthUserResult.class);
				when(userResult.getOtherpcYes()).thenReturn("Y");
				when(deviceAuthUserDao.selectDeviceAuthUserInfo(any())).thenReturn(userResult);

				DeviceAuthInfo result = component.getDeviceAuthInfoNOtherYn("2", "user01");
				assertNotNull(result);
			}
		}

		@Test
		@DisplayName("미등록(0)인데 등록단말 존재 시 등록 상태로 보정")
		public void resolveStatusTest() {
			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);
				when(deviceAuthUserDao.selectDeviceAuthCount(any())).thenReturn(3);
				when(deviceAuthUserDao.selectDeviceAuthUserInfo(any())).thenReturn(null);

				DeviceAuthInfo result = component.getDeviceAuthInfoNOtherYn("0", "user01");
				assertNotNull(result);
			}
		}

		@Test
		@DisplayName("UserID를 세션에서 획득하는 오버로드")
		public void sessionUserIdTest() {
			try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
				 MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
				runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
				session.when(() -> SessionUtils.getSessionValue("UserID")).thenReturn("user01");

				assertNotNull(component.getDeviceAuthInfoNOtherYn("0"));
			}
		}
	}
}
