package com.scbank.process.api.svc.shared.components.ars;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleRequest;
import com.scbank.process.api.svc.shared.components.ars.dto.SyncAtClientCycleRequest.DepositInfo;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class SyncAtComponentTest {

	@InjectMocks private SyncAtComponent component;

	private MockedStatic<PropertiesUtils> props;

	@BeforeEach
	void setUp() {
		props = Mockito.mockStatic(PropertiesUtils.class);
		props.when(() -> PropertiesUtils.getString("SYNCAT_GW_IP")).thenReturn("10.0.0.1");
		props.when(() -> PropertiesUtils.getString("SYNCAT_GW_PORT")).thenReturn("9000");
	}

	@AfterEach
	void tearDown() {
		props.close();
	}

	private SyncAtClientCycleRequest request(String targetService, String arrayCnt, List<DepositInfo> deposits) {
		SyncAtClientCycleRequest request = mock(SyncAtClientCycleRequest.class);
		when(request.getAuthTelNo()).thenReturn("01012345678");
		when(request.getAccount()).thenReturn("110123456789");
		when(request.getTargetService()).thenReturn(targetService);
		when(request.getTranId()).thenReturn("TR0001");
		when(request.getWorkCode()).thenReturn("01");
		when(request.getSvcManChange()).thenReturn("N");
		when(request.getSsn()).thenReturn("8001011234567");
		when(request.getClientName()).thenReturn("홍길동");
		when(request.getInBankName()).thenReturn("SC");
		when(request.getInClientName()).thenReturn("임꺽정");
		when(request.getInAmount()).thenReturn("10000");
		when(request.getTotalCnt()).thenReturn("1");
		when(request.getTotalAmount()).thenReturn("10000");
		when(request.getArrayCnt()).thenReturn(arrayCnt);
		when(request.getDepositList()).thenReturn(deposits);
		return request;
	}

	private MockedConstruction<Socket> okSocket() {
		return Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
			when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
			InputStream in = mock(InputStream.class);
			when(in.read(any(byte[].class), anyInt(), anyInt())).thenReturn(50);
			when(socket.getInputStream()).thenReturn(in);
		});
	}

	@Nested
	@DisplayName("ARS 인증 (Request VO)")
	class ByRequest {

		@Test
		@DisplayName("성공_기본 서비스(01)")
		public void simpleServiceTest() {
			try (MockedConstruction<Socket> socketCon = okSocket()) {
				assertNotNull(component.syncAtClientCycle(request("01", "", null)));
			}
		}

		@Test
		@DisplayName("성공_이체 서비스(05) + 반복 입금내역")
		public void transferServiceTest() {
			DepositInfo d1 = mock(DepositInfo.class);
			when(d1.getBankName()).thenReturn("SC");
			when(d1.getClientName()).thenReturn("A");
			when(d1.getAmount()).thenReturn("100");
			DepositInfo d2 = mock(DepositInfo.class);
			when(d2.getBankName()).thenReturn("KB");
			when(d2.getClientName()).thenReturn("B");
			when(d2.getAmount()).thenReturn("200");

			try (MockedConstruction<Socket> socketCon = okSocket()) {
				assertNotNull(component.syncAtClientCycle(request("05", "2", List.of(d1, d2))));
			}
		}

		@Test
		@DisplayName("실패_소켓 오류 시 MA3CMM0061")
		public void socketErrorTest() {
			try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
				when(socket.getInputStream()).thenThrow(new IOException("socket error"));
			})) {
				assertThrows(PRCServiceException.class, () -> component.syncAtClientCycle(request("01", "", null)));
			}
		}
	}

	@Nested
	@DisplayName("ARS 인증 (Map)")
	class ByMap {

		private Map<String, Object> params(String targetService) {
			Map<String, Object> params = new HashMap<>();
			params.put("authTelNo", "01012345678");
			params.put("account", "110123456789");
			params.put("targetService", targetService);
			params.put("tranId", "TR0001");
			params.put("workCode", "01");
			params.put("svcManChange", "N");
			params.put("ssn", "8001011234567");
			params.put("clientName", "홍길동");
			params.put("inBankName", "SC");
			params.put("inClientName", "임꺽정");
			params.put("inAmount", "10000");
			params.put("totalCnt", "1");
			params.put("totalAmount", "10000");
			params.put("arrayCnt", "");
			return params;
		}

		@Test
		@DisplayName("성공_기본 서비스(01)")
		public void mapSimpleTest() {
			try (MockedConstruction<Socket> socketCon = okSocket()) {
				Map<String, Object> result = component.syncAtClientCycle(params("01"));
				assertNotNull(result);
			}
		}

		@Test
		@DisplayName("실패_소켓 오류 시 MA3CMM0061")
		public void mapSocketErrorTest() {
			try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
				when(socket.getInputStream()).thenThrow(new IOException("socket error"));
			})) {
				assertThrows(PRCServiceException.class, () -> component.syncAtClientCycle(params("01")));
			}
		}
	}
}
