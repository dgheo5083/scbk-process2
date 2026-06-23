package com.scbank.process.api.svc.shared.components.sms;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.integration.HostClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class SmsComponentTest {

	@Mock private HostClient hostClient;

	@InjectMocks private SmsComponent component;

	private SmsRequest request() {
		SmsRequest request = mock(SmsRequest.class);
		when(request.getUserCode()).thenReturn("UC");
		when(request.getUserName()).thenReturn("UN");
		when(request.getDeptCode()).thenReturn("DC");
		when(request.getDeptName()).thenReturn("DN");
		when(request.getCallPhone1()).thenReturn("010");
		when(request.getCallPhone2()).thenReturn("1234");
		when(request.getCallPhone3()).thenReturn("5678");
		when(request.getCallMessage()).thenReturn("msg");
		when(request.getRateDate()).thenReturn("");
		when(request.getRateTime()).thenReturn("");
		when(request.getMember()).thenReturn("0");
		when(request.getReqPhone1()).thenReturn("02");
		when(request.getReqPhone2()).thenReturn("1588");
		when(request.getReqPhone3()).thenReturn("1599");
		when(request.getCallName()).thenReturn("name");
		when(request.getCallUrl()).thenReturn("http://x");
		return request;
	}

	private MockedConstruction<Socket> okSocket() {
		return Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
			when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
			InputStream in = mock(InputStream.class);
			when(in.read(any(byte[].class), anyInt(), anyInt())).thenReturn(100);
			when(socket.getInputStream()).thenReturn(in);
		});
	}

	@Test
	@DisplayName("init - 서버 IP/PORT 로딩")
	public void initTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString("sms.server.ip")).thenReturn("10.0.0.1");
			props.when(() -> PropertiesUtils.getString("sms.server.port")).thenReturn("9000");
			assertDoesNotThrow(() -> component.init());
		}
	}

	@Nested
	@DisplayName("SMS 송신")
	class Send {

		@Test
		@DisplayName("sendMain - 정상 송수신 후 flag 반환")
		public void sendMainTest() {
			try (MockedConstruction<Socket> socketCon = okSocket()) {
				assertNotNull(component.sendMain(request()));
			}
		}

		@Test
		@DisplayName("sendMain - 소켓 오류 시 MA3CMM0062")
		public void sendMainErrorTest() {
			try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
				when(socket.getOutputStream()).thenThrow(new IOException("socket error"));
			})) {
				assertThrows(PRCServiceException.class, () -> component.sendMain(request()));
			}
		}

		@Test
		@DisplayName("sendURLMain - 정상 송수신 (예외 흡수)")
		public void sendUrlMainTest() {
			try (MockedConstruction<Socket> socketCon = okSocket()) {
				assertDoesNotThrow(() -> component.sendURLMain(request()));
			}
		}
	}

	@Nested
	@DisplayName("발급완료 SMS")
	class CompleteSMS {

		@Test
		@DisplayName("sendCompleteSMS(4-arg) - 호스트 전문 전송")
		public void completeArgsTest() {
			when(hostClient.getOltpRequestOptions(anyString())).thenReturn(mock(OltpRequestOptions.class));

			assertTrue(component.sendCompleteSMS("user01", "pwd", "8001011234567", "발급완료"));
		}

		@Test
		@DisplayName("sendCompleteSMS(Map) - 호스트 전문 전송")
		public void completeMapTest() {
			when(hostClient.getOltpRequestOptions(anyString())).thenReturn(mock(OltpRequestOptions.class));

			Map<String, String> input = new HashMap<>();
			input.put("UserID", "user01");
			input.put("RegNo", "8001011234567");
			input.put("SMSMsg", "발급완료");

			assertTrue(component.sendCompleteSMS(input));
		}
	}
}
