package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * {@link ZipUtils} 실제 파일 압축/해제 동작 커버리지 테스트.
 */
@DisplayName("ZipUtils")
class ZipUtilsTest {

	@DisplayName("zip 으로 압축하고 unzip 으로 해제한다 (라운드트립)")
	@Test
	void zipAndUnzipRoundTrip(@TempDir Path tempDir) throws IOException {
		String dirPath = tempDir.toString() + "/";

		Files.write(tempDir.resolve("a.txt"), "AAA-content".getBytes(StandardCharsets.UTF_8));
		Files.write(tempDir.resolve("b.txt"), "BBB-content".getBytes(StandardCharsets.UTF_8));

		ZipUtils.zip(dirPath, "out.zip", List.of("a.txt", "b.txt"));

		Path zipFile = tempDir.resolve("out.zip");
		assertTrue(Files.exists(zipFile), "zip 파일이 생성되어야 한다");
		assertTrue(Files.size(zipFile) > 0, "zip 파일이 비어있지 않아야 한다");

		// 압축 해제 (같은 디렉터리에 풀어 내용 검증)
		Files.delete(tempDir.resolve("a.txt"));
		Files.delete(tempDir.resolve("b.txt"));

		List<String> unzipped = ZipUtils.unzip(dirPath, "out.zip");

		assertEquals(List.of("a.txt", "b.txt"), unzipped);
		assertEquals("AAA-content", Files.readString(tempDir.resolve("a.txt")));
		assertEquals("BBB-content", Files.readString(tempDir.resolve("b.txt")));
	}

	@DisplayName("빈 파일도 정상적으로 압축/해제된다 (read 루프 경계)")
	@Test
	void zipEmptyFile(@TempDir Path tempDir) throws IOException {
		String dirPath = tempDir.toString() + "/";
		Files.write(tempDir.resolve("empty.txt"), new byte[0]);

		ZipUtils.zip(dirPath, "empty.zip", List.of("empty.txt"));
		Files.delete(tempDir.resolve("empty.txt"));

		List<String> unzipped = ZipUtils.unzip(dirPath, "empty.zip");

		assertEquals(List.of("empty.txt"), unzipped);
		assertEquals(0, Files.size(tempDir.resolve("empty.txt")));
	}
}
