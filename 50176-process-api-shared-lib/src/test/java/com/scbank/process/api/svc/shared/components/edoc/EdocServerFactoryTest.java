package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;

import java.net.Socket;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class EdocServerFactoryTest {

//	@Test
//	@DisplayName("성공_가용 서버 소켓 연결 성공 시 서비스 URL 반환")
//	public void availableServerTest() {
//		ReflectionTestUtils.setField(EdocServerFactory.class, "serverUrls",
//				new java.util.ArrayList<>(List.of("http://localhost:9999")));
//
//		try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class)) {
//			String result = EdocServerFactory.getAvailableEdocServer();
//			assertTrue(result.contains("localhost"));
//		}
//	}
//
//	@Test
//	@DisplayName("실패_모든 서버 응답 없음 시 PRCServiceException")
//	public void noAvailableServerTest() {
//		ReflectionTestUtils.setField(EdocServerFactory.class, "serverUrls",
//				new java.util.ArrayList<>(List.of("http://localhost:9999")));
//
//		try (MockedConstruction<Socket> socketCon = Mockito.mockConstruction(Socket.class, (socket, ctx) -> {
//			doThrow(new IOException("connect fail")).when(socket).connect(any(), anyInt());
//		})) {
//			assertThrows(PRCServiceException.class, EdocServerFactory::getAvailableEdocServer);
//		}
//	}
}
