package com.scbank.process.api.svc.shared.components.ars.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SyncAt 바이트 유틸 SyncAtByteUtils")
public class SyncAtByteUtilsTest {

	@Test
	@DisplayName("long/int/short/byte 쓰기-읽기 라운드트립")
	public void primitiveRoundTripTest() {
		byte[] buf = new byte[64];

		SyncAtByteUtils.makeLongMsg(buf, 0, 123456789L);
		assertEquals(123456789L, SyncAtByteUtils.readLong(buf, 0));

		SyncAtByteUtils.makeIntMsg(buf, 8, 42);
		assertEquals(42, SyncAtByteUtils.readInt(buf, 8));

		SyncAtByteUtils.makeShortMsg(buf, 12, (short) 7);
		assertEquals((short) 7, SyncAtByteUtils.readShort(buf, 12));

		SyncAtByteUtils.makeByteMsg(buf, 14, (byte) 9);
		assertEquals((byte) 9, SyncAtByteUtils.readByte(buf, 14));
	}

//	@Test
//	@DisplayName("바이트 메시지 패딩 함수들")
//	public void byteMessageTest() {
//		byte[] buf = new byte[64];
//
//		assertDoesNotThrow(() -> {
//			SyncAtByteUtils.makeIntMsg2(buf, 0, 100);
//			SyncAtByteUtils.makeBytesMsg(buf, 4, "ab".getBytes(), 6);
//			SyncAtByteUtils.makeBytes20Msg(buf, 10, "cd".getBytes(), 6);
//			SyncAtByteUtils.makeBytes30Msg(buf, 16, "ef".getBytes(), 6);
//			SyncAtByteUtils.makeBytesDecimal20Msg(buf, 22, "gh".getBytes(), 6);
//			SyncAtByteUtils.makeOctetBytesMsg(buf, 28, "12".getBytes(), 6);
//			SyncAtByteUtils.makeNullBytes(buf, 34, 4);
//		});
//
//		assertEquals(8, SyncAtByteUtils.readBytes(buf, 0, 8).length);
//		assertNotNull(SyncAtByteUtils.readOctetBytes(buf, 28, 6));
//	}

	@Test
	@DisplayName("16진수/로그 변환 함수들")
	public void hexAndLogTest() {
		assertNotNull(SyncAtByteUtils.toHexaString(255));
		assertNotNull(SyncAtByteUtils.toHexaString((byte) 0x0A));
		assertNotNull(SyncAtByteUtils.writeHexaLog(new byte[] { 1, 2, 3 }));
		assertNotNull(SyncAtByteUtils.writeHexaLog(new byte[] { 1, 2, 3 }, true));
		assertNotNull(SyncAtByteUtils.calucLineNumber(1));
		// toCharacter: 출력 가능한 문자 / 비가시 문자 모두 처리
		SyncAtByteUtils.toCharacter((byte) 65);
		SyncAtByteUtils.toCharacter((byte) 1);
	}
}
