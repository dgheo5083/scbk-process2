package com.scbank.process.api.svc.shared.components.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

@DisplayName("PLSendMail SMTP 발송")
public class PLSendMailTest {

	@Test
	@DisplayName("생성자 - 다양한 인자 조합")
	public void constructorsTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("");

			assertNotNull(new PLSendMail());
			assertNotNull(new PLSendMail("clerk", "subject", "content"));
			assertNotNull(new PLSendMail("clerk", "subject", "content", "sender"));
			assertNotNull(new PLSendMail("clerk", "subject", "content", "sender", "@domain"));
		}
	}

	@Test
	@DisplayName("sendMail - 접속 실패 시 예외 없이 종료")
	public void sendMailConnectErrorTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("not-a-port");

			PLSendMail mail = new PLSendMail("clerk", "subject", "content");
			assertDoesNotThrow(mail::sendMail);
		}
	}

	@Test
	@DisplayName("run - sendMail 실행 (예외 흡수)")
	public void runTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("not-a-port");

			PLSendMail mail = new PLSendMail("clerk", "subject", "content");
			assertDoesNotThrow(mail::run);
		}
	}
}
