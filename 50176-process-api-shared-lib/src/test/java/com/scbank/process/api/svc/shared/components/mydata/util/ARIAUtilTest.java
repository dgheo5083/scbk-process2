package com.scbank.process.api.svc.shared.components.mydata.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ARIA 암복호화 유틸 ARIAUtil")
public class ARIAUtilTest {

	private static final byte[] ID = "0123456789abcdef".getBytes(StandardCharsets.UTF_8); // 16 byte (128bit)

	@Test
	@DisplayName("encrypt → decrypt 라운드트립")
	public void roundTripTest() throws Exception {
		ARIAUtil aria = new ARIAUtil(ID);

		byte[] plain = "마이데이터 ARIA 암복호화 테스트".getBytes(StandardCharsets.UTF_8);
		byte[] enc = aria.encrypt(plain);
		byte[] dec = aria.decrypt(enc);

		assertArrayEquals(plain, dec);
	}

	@Test
	@DisplayName("encrypt - null/empty 입력 처리")
	public void encryptEdgeTest() throws Exception {
		ARIAUtil aria = new ARIAUtil(ID);
		assertNull(aria.encrypt(null));
		assertEquals(0, aria.encrypt(new byte[0]).length);
	}

	@Test
	@DisplayName("decrypt - null/empty 입력 처리")
	public void decryptEdgeTest() throws Exception {
		ARIAUtil aria = new ARIAUtil(ID);
		assertNull(aria.decrypt(null));
		assertEquals(0, aria.decrypt(new byte[0]).length);
	}
}
