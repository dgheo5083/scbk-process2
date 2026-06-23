package com.scbank.process.api.svc.shared.components.clickToCall;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallRequest;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallResponse;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClickToCallComponentTest {

	private final ClickToCallComponent component = new ClickToCallComponent();

	private ClickToCallRequest request() {
		ClickToCallRequest request = mock(ClickToCallRequest.class);
		when(request.getPerBusNo()).thenReturn("8001011234567");
		when(request.getCustName()).thenReturn("홍길동");
		when(request.getServicePath()).thenReturn("WEB");
		when(request.getCallGroup()).thenReturn("01");
		when(request.getCustTelNo()).thenReturn("01012345678");
		when(request.getUrl()).thenReturn("http://x");
		when(request.getCommand()).thenReturn("CMD");
		return request;
	}

	@Test
	@DisplayName("init - 서버 IP/PORT 프로퍼티 로딩")
	public void initTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("10.0.0.1");
			props.when(() -> PropertiesUtils.getInt(anyString())).thenReturn(10010);

			assertDoesNotThrow(() -> component.init());
		}
	}

	@Nested
	@DisplayName("전화상담 호출 send")
	class Send {

		@Test
		@DisplayName("성공_소켓 송수신 후 응답 반환")
		public void sendSuccessTest() {
			try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
				when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
				InputStream in = mock(InputStream.class);
				when(in.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
				when(socket.getInputStream()).thenReturn(in);
			})) {
				ClickToCallResponse response = component.send(request());
				assertNotNull(response);
			}
		}

		@Test
		@DisplayName("실패_소켓 오류 시 PRCServiceException")
		public void sendErrorTest() {
			try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
				when(socket.getOutputStream()).thenThrow(new IOException("socket error"));
			})) {
				assertThrows(PRCServiceException.class, () -> component.send(request()));
			}
		}
	}

	@Test
	@DisplayName("receive - 응답 버퍼 파싱")
	public void receiveTest() throws Exception {
		BufferedInputStream bis = mock(BufferedInputStream.class);
		when(bis.read(any(byte[].class))).thenReturn(1305);

		ClickToCallResponse response = component.receive(bis);
		assertNotNull(response);
	}
}
