package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import com.wizvera.util.Base64;

import kr.co.pace.crypto.SeedUtil;

/**
 * {@link EdocUtils} Base64/쿼리스트링/SEED 파일 암복호화 유틸 커버리지 테스트.
 */
@DisplayName("EdocUtils")
class EdocUtilsTest {

//	// ---------------- Base64 URL-Safe ----------------
//
//	@DisplayName("base64SafeDecode : 빈값은 null")
//	@Test
//	void base64SafeDecode_empty() {
//		assertNull(EdocUtils.base64SafeDecode(""));
//		assertNull(EdocUtils.base64SafeDecode(null));
//	}
//
//	@DisplayName("base64SafeDecode : URL-Safe 치환 + 패딩 후 디코딩")
//	@Test
//	void base64SafeDecode_decode() {
//		byte[] decoded = "ok".getBytes(StandardCharsets.UTF_8);
//		try (MockedStatic<Base64> b = mockStatic(Base64.class)) {
//			b.when(() -> Base64.decode(anyString())).thenReturn(decoded);
//			assertArrayEquals(decoded, EdocUtils.base64SafeDecode("ab-c_d\r\n"));
//		}
//	}
//
//	@DisplayName("base64SafeReplace : 빈값은 빈 문자열, 그 외 치환")
//	@Test
//	void base64SafeReplace() {
//		assertEquals("", EdocUtils.base64SafeReplace("  "));
//		assertEquals("a+b/c", EdocUtils.base64SafeReplace("a-b_c\r\n"));
//	}
//
//	@DisplayName("base64EncodeSafeReplace : 빈값은 빈 문자열, 그 외 치환")
//	@Test
//	void base64EncodeSafeReplace() {
//		assertEquals("", EdocUtils.base64EncodeSafeReplace(" "));
//		assertEquals("a-b_c", EdocUtils.base64EncodeSafeReplace("a+b/c\n\r"));
//	}
//
//	// ---------------- json2QueryString ----------------
//
//	@DisplayName("json2QueryString : key=value 쿼리스트링 변환")
//	@Test
//	void json2QueryString() {
//		JSONObject json = new JSONObject();
//		json.put("a", "1");
//		assertEquals("&a=1&", EdocUtils.json2QueryString(json));
//	}
//
//	@DisplayName("json2QueryStringToURLEncode : 값을 URL 인코딩한다")
//	@Test
//	void json2QueryStringToURLEncode() {
//		JSONObject json = new JSONObject();
//		json.put("a", "한 글");
//		String result = EdocUtils.json2QueryStringToURLEncode(json);
//		assertTrue(result.startsWith("&a="));
//		assertTrue(result.contains("%"), "URL 인코딩된 값이 포함되어야 한다: " + result);
//		assertTrue(result.endsWith("&"));
//	}
//
//	// ---------------- deCryptionFile ----------------
//
//	@DisplayName("deCryptionFile : 원본 파일이 없으면 false")
//	@Test
//	void deCryptionFile_notFound(@TempDir Path tempDir) {
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(mock(SeedUtil.class));
//			assertFalse(EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/", "missing.enc"));
//		}
//	}
//
//	@DisplayName("deCryptionFile : 0바이트 파일이면 false")
//	@Test
//	void deCryptionFile_zeroByte(@TempDir Path tempDir) throws IOException {
//		Files.write(tempDir.resolve("z.enc"), new byte[0]);
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(mock(SeedUtil.class));
//			assertFalse(EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/", "z.enc"));
//		}
//	}
//
//	@DisplayName("deCryptionFile : 복호화 파일이 생성되면 true")
//	@Test
//	void deCryptionFile_success(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("a.enc"), "enc".getBytes());
//		Path decFile = Files.write(tempDir.resolve("a.dec"), "dec".getBytes());
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getDecryptFile(any(File.class), anyString())).thenReturn(decFile.toFile());
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			assertTrue(EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/", "a.enc"));
//		}
//	}
//
//	@DisplayName("deCryptionFile : 복호화 결과 파일이 없으면 false")
//	@Test
//	void deCryptionFile_decFileMissing(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("a.enc"), "enc".getBytes());
//		File nonExistent = tempDir.resolve("nope.dec").toFile();
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getDecryptFile(any(File.class), anyString())).thenReturn(nonExistent);
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			assertFalse(EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/", "a.enc"));
//		}
//	}
//
//	@DisplayName("deCryptionFile : 복호화 중 예외 발생 시 false")
//	@Test
//	void deCryptionFile_exception(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("a.enc"), "enc".getBytes());
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getDecryptFile(any(File.class), anyString())).thenThrow(new RuntimeException("boom"));
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			assertFalse(EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/", "a.enc"));
//		}
//	}
//
//	@DisplayName("deCryptionFile(List) : 일부 실패하면 false")
//	@Test
//	void deCryptionFile_listPartialFail(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("ok.enc"), "enc".getBytes());
//		Files.write(tempDir.resolve("bad.enc"), new byte[0]); // 0바이트 -> 실패
//		Path decFile = Files.write(tempDir.resolve("ok.dec"), "dec".getBytes());
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getDecryptFile(any(File.class), anyString())).thenReturn(decFile.toFile());
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			boolean result = EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/",
//					List.of("ok.enc", "bad.enc"));
//			assertFalse(result);
//		}
//	}
//
//	@DisplayName("deCryptionFile(List) : 예외(목록 null) 발생 시 false")
//	@Test
//	void deCryptionFile_listException(@TempDir Path tempDir) {
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(mock(SeedUtil.class));
//			assertFalse(EdocUtils.deCryptionFile(tempDir + "/", tempDir + "/dec/", (List<String>) null));
//		}
//	}
//
//	// ---------------- enCryptionFile ----------------
//
//	@DisplayName("enCryptionFile(4) : 암호화 파일 생성 시 encPath/encName 반환")
//	@Test
//	void enCryptionFile4_success(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("org.txt"), "data".getBytes());
//		Path encFile = Files.write(tempDir.resolve("org.enc"), "enc".getBytes());
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getEncryptFile(any(File.class), anyString())).thenReturn(encFile.toFile());
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			Map<String, String> out = EdocUtils.enCryptionFile(tempDir + "/", tempDir + "/", "org.txt", "org.enc");
//			assertEquals("org.enc", out.get("encName"));
//			assertTrue(out.get("encPath").endsWith(File.separator));
//		}
//	}
//
//	@DisplayName("enCryptionFile(4) : 암호화 파일 미생성 시 빈 맵")
//	@Test
//	void enCryptionFile4_encMissing(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("org.txt"), "data".getBytes());
//		File nonExistent = tempDir.resolve("nope.enc").toFile();
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getEncryptFile(any(File.class), anyString())).thenReturn(nonExistent);
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			Map<String, String> out = EdocUtils.enCryptionFile(tempDir + "/", tempDir + "/", "org.txt", "nope.enc");
//			assertTrue(out.isEmpty());
//		}
//	}
//
//	@DisplayName("enCryptionFile(4) : 원본 파일이 없으면 빈 맵")
//	@Test
//	void enCryptionFile4_orgMissing(@TempDir Path tempDir) {
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(mock(SeedUtil.class));
//			Map<String, String> out = EdocUtils.enCryptionFile(tempDir + "/", tempDir + "/", "missing.txt", "x.enc");
//			assertTrue(out.isEmpty());
//		}
//	}
//
//	@DisplayName("enCryptionFile(4) : 예외 발생 시 빈 맵")
//	@Test
//	void enCryptionFile4_exception(@TempDir Path tempDir) throws Exception {
//		Files.write(tempDir.resolve("org.txt"), "data".getBytes());
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getEncryptFile(any(File.class), anyString())).thenThrow(new RuntimeException("boom"));
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			assertTrue(EdocUtils.enCryptionFile(tempDir + "/", tempDir + "/", "org.txt", "x.enc").isEmpty());
//		}
//	}
//
//	@DisplayName("enCryptionFile(2) : 암호화 파일 생성 시 encPath/encName 반환")
//	@Test
//	void enCryptionFile2_success(@TempDir Path tempDir) throws Exception {
//		Path org = Files.write(tempDir.resolve("org2.txt"), "data".getBytes());
//		Path encFile = Files.write(tempDir.resolve("org2.enc"), "enc".getBytes());
//
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getEncryptFile(any(File.class), anyString())).thenReturn(encFile.toFile());
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			Map<String, String> out = EdocUtils.enCryptionFile(org.toString(), tempDir + "/org2.enc");
//			assertEquals("org2.enc", out.get("encName"));
//		}
//	}
//
//	@DisplayName("enCryptionFile(2) : 원본 파일이 없으면 빈 맵")
//	@Test
//	void enCryptionFile2_orgMissing(@TempDir Path tempDir) {
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(mock(SeedUtil.class));
//			assertTrue(EdocUtils.enCryptionFile(tempDir + "/missing.txt", tempDir + "/x.enc").isEmpty());
//		}
//	}
//
//	@DisplayName("enCryptionFile(2) : 예외 발생 시 빈 맵")
//	@Test
//	void enCryptionFile2_exception(@TempDir Path tempDir) throws Exception {
//		Path org = Files.write(tempDir.resolve("org3.txt"), "data".getBytes());
//		SeedUtil seed = mock(SeedUtil.class);
//		when(seed.getEncryptFile(any(File.class), anyString())).thenThrow(new RuntimeException("boom"));
//
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(seed);
//			assertTrue(EdocUtils.enCryptionFile(org.toString(), tempDir + "/x.enc").isEmpty());
//		}
//	}
//
//	// HashMap import 사용 보장 (명시적 빈 맵 비교)
//	@DisplayName("enCryptionFile : 반환 타입은 HashMap")
//	@Test
//	void returnTypeIsMap() {
//		Map<String, String> expectedEmpty = new HashMap<>();
//		try (MockedStatic<SeedUtil> s = mockStatic(SeedUtil.class)) {
//			s.when(SeedUtil::getInstance).thenReturn(mock(SeedUtil.class));
//			assertEquals(expectedEmpty, EdocUtils.enCryptionFile("/no/such/file", "/no/such/enc"));
//		}
//	}
}
