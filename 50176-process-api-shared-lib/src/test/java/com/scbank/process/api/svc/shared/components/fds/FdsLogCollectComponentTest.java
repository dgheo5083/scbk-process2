package com.scbank.process.api.svc.shared.components.fds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

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

import com.scbank.process.api.fw.base.integration.log.IntegrationLogCollectEvent;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManager;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequest;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class FdsLogCollectComponentTest {

	@Mock private IpinsideComponent ipinsideComponent;
	@Mock private OltpManager oltpManager;
	@Mock private ISessionContextManager sessionContextManager;

	@InjectMocks private FdsLogCollectComponent component;

	private MockedStatic<DateUtils> dateUtils;
	private MockedStatic<IpUtils> ipUtils;
	private MockedStatic<PRCSharedUtils> prcUtils;

	@BeforeEach
	void setUp() {
		dateUtils = Mockito.mockStatic(DateUtils.class);
		ipUtils = Mockito.mockStatic(IpUtils.class);
		prcUtils = Mockito.mockStatic(PRCSharedUtils.class);

		dateUtils.when(() -> DateUtils.getCurrentDate(anyString())).thenReturn("20260601");
		ipUtils.when(IpUtils::getClientIp).thenReturn("127.0.0.1");
		prcUtils.when(PRCSharedUtils::getAppVersion).thenReturn("1.0.0");
	}

	@AfterEach
	void tearDown() {
		dateUtils.close();
		ipUtils.close();
		prcUtils.close();
	}

	@Nested
	@DisplayName("전문로그 수집 이벤트")
	class OnLogCollectEvent {

		@Test
		@DisplayName("이벤트 null이면 무시")
		public void nullEventTest() {
			assertDoesNotThrow(() -> component.onLogCollectEvent(null));
		}

		@Test
		@DisplayName("host 시스템이 아니면 무시")
		public void notHostTest() {
			IntegrationLogCollectEvent event = mock(IntegrationLogCollectEvent.class);
			when(event.getSystemId()).thenReturn("mci");

			component.onLogCollectEvent(event);
			verify(ipinsideComponent, never()).sendFdsLog(any(), any());
		}

//		@Test
//		@DisplayName("송신(S) + OLTP 요청 → FDS 로그 전송")
//		public void sendRequestTest() {
//			IntegrationLogCollectEvent event = mock(IntegrationLogCollectEvent.class);
//			when(event.getSystemId()).thenReturn("host");
//			when(event.getTxDscd()).thenReturn("S");
//			when(event.getUserId()).thenReturn("user01");
//			when(event.getDeviceKey()).thenReturn("dev01");
//			when(event.getChannelType()).thenReturn("Z");
//			when(event.getData()).thenReturn("rawdata");
//			OltpRequest<?> request = mock(OltpRequest.class, RETURNS_DEEP_STUBS);
//			when(request.getHeader().getOltpCommon().getTrCd()).thenReturn("0");
//			when(event.getRequest()).thenReturn(request);
//
//			component.onLogCollectEvent(event);
//			verify(ipinsideComponent).sendFdsLog(anyString(), anyString());
//		}

		@Test
		@DisplayName("송신(S) + 요청 null → 무시")
		public void sendNullRequestTest() {
			IntegrationLogCollectEvent event = mock(IntegrationLogCollectEvent.class);
			when(event.getSystemId()).thenReturn("host");
			when(event.getTxDscd()).thenReturn("S");
			when(event.getRequest()).thenReturn(null);

			component.onLogCollectEvent(event);
			verify(ipinsideComponent, never()).sendFdsLog(any(), any());
		}

//		@Test
//		@DisplayName("수신(R) + OLTP 응답 → FDS 로그 전송")
//		public void receiveResponseTest() {
//			IntegrationLogCollectEvent event = mock(IntegrationLogCollectEvent.class);
//			when(event.getSystemId()).thenReturn("host");
//			when(event.getTxDscd()).thenReturn("R");
//			when(event.getUserId()).thenReturn("user01");
//			when(event.getDeviceKey()).thenReturn("dev01");
//			when(event.getChannelType()).thenReturn("Z");
//			when(event.getData()).thenReturn("rawdata");
//			OltpResponse<?> response = mock(OltpResponse.class, RETURNS_DEEP_STUBS);
//			when(response.getHeader().getOltpCommon().getTrCd()).thenReturn("0");
//			when(event.getResponse()).thenReturn(response);
//			when(sessionContextManager.getGlobalValue("VFDSDeviceKey", String.class)).thenReturn("vdev");
//
//			component.onLogCollectEvent(event);
//			verify(ipinsideComponent).sendFdsLog(anyString(), anyString());
//		}
	}

	@Nested
	@DisplayName("오픈뱅킹 FDS 로그")
	class OpenBanking {

		@Test
		@DisplayName("sendOpenBankingFdsLog - 요청 생성 후 로그 전송")
		public void sendOpenBankingTest() {
			OltpRequestOptions options = mock(OltpRequestOptions.class);
			IMessageObject input = mock(IMessageObject.class);

			Map<String, Object> result = new HashMap<>();
			OltpRequest<?> request = mock(OltpRequest.class, RETURNS_DEEP_STUBS);
			when(request.getHeader().getOltpCommon().getTrCd()).thenReturn("0");
			result.put("request", request);
			result.put("data", "rawdata");
			when(oltpManager.createRequestBytes(any(), any())).thenReturn(result);

			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("UserID", String.class)).thenReturn("user01");
			when(sessionContextManager.getGlobalValue("VFDSDeviceKey", String.class)).thenReturn("vdev");

			component.sendOpenBankingFdsLog(options, input);
			verify(ipinsideComponent).sendFdsLog(anyString(), anyString());
		}
	}
}
