package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import com.imas.lbs.TcpipClient;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

/**
 * {@link EmailUtils} IMAS 메일발송 래퍼 커버리지 테스트.
 */
@DisplayName("EmailUtils")
class EmailUtilsTest {

	private MockedStatic<PropertiesUtils> mockProps(String ip, String port) {
		MockedStatic<PropertiesUtils> mocked = mockStatic(PropertiesUtils.class);
		mocked.when(() -> PropertiesUtils.getString("IMAS_IP")).thenReturn(ip);
		mocked.when(() -> PropertiesUtils.getString("IMAS_PORT")).thenReturn(port);
		return mocked;
	}

	// ----- sendCompleteMail(AID, RegNo, UserName, EmailAddr) -----

	@DisplayName("sendCompleteMail(4) : 정상 발송 시 true")
	@Test
	void send4_success() {
		try (MockedStatic<PropertiesUtils> p = mockProps("127.0.0.1", "9100");
				MockedConstruction<TcpipClient> tc = mockConstruction(TcpipClient.class)) {
			assertTrue(EmailUtils.sendCompleteMail("AID", "REG", "NAME", "a@b.com"));
		}
	}

	@DisplayName("sendCompleteMail(4) : 예외(포트 파싱 실패) 가 발생해도 true 를 반환한다")
	@Test
	void send4_exceptionStillTrue() {
		try (MockedStatic<PropertiesUtils> p = mockProps("127.0.0.1", "not-a-port");
				MockedConstruction<TcpipClient> tc = mockConstruction(TcpipClient.class)) {
			assertTrue(EmailUtils.sendCompleteMail("AID", "REG", "NAME", "a@b.com"));
		}
	}

	// ----- sendCompleteMail(aid, Map) -----

	@DisplayName("sendCompleteMail(Map) : 유효성 검증 실패 시 false")
	@Test
	void sendMap_validation() {
		Map<String, String> args = new HashMap<>();
		args.put("NAME", "n");
		assertFalse(EmailUtils.sendCompleteMail(null, args));          // aid null
		assertFalse(EmailUtils.sendCompleteMail("   ", args));         // aid blank
		assertFalse(EmailUtils.sendCompleteMail("AID", (Map<String, String>) null)); // args null
		assertFalse(EmailUtils.sendCompleteMail("AID", new HashMap<>())); // args empty
	}

	@DisplayName("sendCompleteMail(Map) : 정상 발송 시 true")
	@Test
	void sendMap_success() {
		Map<String, String> args = new HashMap<>();
		args.put("CUSTNO", "123");
		args.put("NAME", "홍길동");

		try (MockedStatic<PropertiesUtils> p = mockProps("127.0.0.1", "9100");
				MockedConstruction<TcpipClient> tc = mockConstruction(TcpipClient.class)) {
			assertTrue(EmailUtils.sendCompleteMail("AID", args));
		}
	}

	@DisplayName("sendCompleteMail(Map) : 예외 발생 시 false")
	@Test
	void sendMap_exception() {
		Map<String, String> args = new HashMap<>();
		args.put("NAME", "n");

		try (MockedStatic<PropertiesUtils> p = mockProps("127.0.0.1", "bad");
				MockedConstruction<TcpipClient> tc = mockConstruction(TcpipClient.class)) {
			assertFalse(EmailUtils.sendCompleteMail("AID", args));
		}
	}

	// ----- sendCompleteMail(aid, recvName, recvEmail, subject, content) -----

	@DisplayName("sendCompleteMail(5) : 필수값 누락 시 false")
	@Test
	void send5_validation() throws Exception {
		assertFalse(EmailUtils.sendCompleteMail(null, "n", "e@x.com", "subj", "body"));
		assertFalse(EmailUtils.sendCompleteMail(" ", "n", "e@x.com", "subj", "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", null, "e@x.com", "subj", "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", " ", "e@x.com", "subj", "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", "n", null, "subj", "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", "n", " ", "subj", "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", "n", "e@x.com", null, "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", "n", "e@x.com", " ", "body"));
		assertFalse(EmailUtils.sendCompleteMail("AID", "n", "e@x.com", "subj", null));
		assertFalse(EmailUtils.sendCompleteMail("AID", "n", "e@x.com", "subj", " "));
	}

	@DisplayName("sendCompleteMail(5) : 정상 발송 시 true")
	@Test
	void send5_success() throws Exception {
		try (MockedStatic<PropertiesUtils> p = mockProps("127.0.0.1", "9100");
				MockedConstruction<TcpipClient> tc = mockConstruction(TcpipClient.class)) {
			assertTrue(EmailUtils.sendCompleteMail("AID", "n", "e@x.com", "subj", "body"));
		}
	}

	@DisplayName("sendCompleteMail(5) : 예외 발생 시 그대로 던진다")
	@Test
	void send5_exception() {
		try (MockedStatic<PropertiesUtils> p = mockProps("127.0.0.1", "bad");
				MockedConstruction<TcpipClient> tc = mockConstruction(TcpipClient.class)) {
			assertThrows(Exception.class,
					() -> EmailUtils.sendCompleteMail("AID", "n", "e@x.com", "subj", "body"));
		}
	}
}
