package com.scbank.process.api.svc.shared.components.simpleAuth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthRequest;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthResponse;
import com.scbank.process.api.svc.shared.utils.RandomKeyUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class TCertComponentTest {

	private final TCertComponent component = new TCertComponent();

	private SimpleAuthRequest fullRequest() {
		SimpleAuthRequest request = new SimpleAuthRequest();
		request.setTelecomType("1");
		request.setCtn("01012345678");
		request.setUiccid("U123");
		request.setImsi("I123");
		request.setImei("E123");
		request.setMos("A");
		request.setBirthday("19900101");
		request.setGender("1");
		request.setName("홍길동");
		request.setPrivacySharingAgreeYn("Y");
		request.setThirdPartyProvisionAgreeYn("Y");
		return request;
	}

	@Nested
	@DisplayName("간편인증 전문 송수신")
	class SendAuthMsg {

		@Test
		@DisplayName("성공_정상 응답 수신 후 파싱")
		public void successTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<RandomKeyUtils> randomKey = mockStatic(RandomKeyUtils.class);
				 MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
					 OutputStream out = mock(OutputStream.class);
					 InputStream in = mock(InputStream.class);
					 when(socket.getOutputStream()).thenReturn(out);
					 when(socket.getInputStream()).thenReturn(in);
					 // DataInputStream.read(byte[]) → 응답 전체 길이(183) 반환하여 루프 종료
					 when(in.read(any(byte[].class), anyInt(), anyInt())).thenReturn(183);
				 })) {

				props.when(() -> PropertiesUtils.getString("RAON_ADAPTER_IP")).thenReturn("127.0.0.1");
				props.when(() -> PropertiesUtils.getString("RAON_CUST_ID")).thenReturn("CUST01");
				props.when(() -> PropertiesUtils.getInt("RAON_ADAPTER_PORT")).thenReturn(9999);
				props.when(() -> PropertiesUtils.getInt("RAON_ADAPTER_TIMEOUT")).thenReturn(3000);
				randomKey.when(() -> RandomKeyUtils.getKey(anyInt())).thenReturn("TR0000000000000000");

				SimpleAuthResponse response = component.sendTauth(fullRequest());
				assertNotNull(response);
			}
		}

		@Test
		@DisplayName("실패_소켓 통신 오류 시 MA3CMM0060")
		public void socketErrorTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<RandomKeyUtils> randomKey = mockStatic(RandomKeyUtils.class);
				 MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
					 when(socket.getOutputStream()).thenThrow(new java.io.IOException("connect fail"));
				 })) {

				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("127.0.0.1");
				props.when(() -> PropertiesUtils.getInt(anyString())).thenReturn(1000);
				randomKey.when(() -> RandomKeyUtils.getKey(anyInt())).thenReturn("TR01");

				assertThrows(PRCServiceException.class, () -> component.sendAuthMsg(fullRequest()));
			}
		}
	}

	@Nested
	@DisplayName("전문 생성 / 파싱 유틸")
	class MessageUtils {

		@Test
		@DisplayName("makeAuthMsg - 전체 필드 세팅 시 181 바이트 메시지 생성")
		public void makeAuthMsgTest() {
			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class);
				 MockedStatic<RandomKeyUtils> randomKey = mockStatic(RandomKeyUtils.class)) {
				props.when(() -> PropertiesUtils.getString("RAON_CUST_ID")).thenReturn("CUST01");
				randomKey.when(() -> RandomKeyUtils.getKey(anyInt())).thenReturn("TR0000000000000000");

				byte[] msg = component.makeAuthMsg(fullRequest());
				assertEquals(component.TCERT_TCP_MSG_SIZE_AUTH + 4, msg.length);
			}
		}

		@Test
		@DisplayName("getString - 지정 위치/길이의 문자열 추출")
		public void getStringTest() throws Exception {
			assertEquals("BCDE", component.getString("ABCDEFGHIJ", 1, 4));
		}
	}
}
