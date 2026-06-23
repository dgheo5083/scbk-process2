package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * {@link FileUtils} 실제 파일 시스템 동작 커버리지 테스트.
 */
@DisplayName("FileUtils")
class FileUtilsTest {

	@DisplayName("makeDir : 존재하지 않으면 생성, 이미 있으면 true")
	@Test
	void makeDir(@TempDir Path tempDir) {
		assertTrue(FileUtils.makeDir(tempDir.toString()), "이미 존재하는 디렉터리");
		Path newDir = tempDir.resolve("a/b/c");
		assertTrue(FileUtils.makeDir(newDir.toString()), "신규 디렉터리 생성");
		assertTrue(Files.isDirectory(newDir));
	}

	@DisplayName("checkFile : 파일이면 true, 디렉터리면 false")
	@Test
	void checkFile(@TempDir Path tempDir) throws IOException {
		Path file = Files.write(tempDir.resolve("f.txt"), "x".getBytes());
		assertTrue(FileUtils.checkFile(file.toString()));
		assertFalse(FileUtils.checkFile(tempDir.toString()));
	}

	@DisplayName("fileDelete : 존재하면 삭제 true, 없으면 false")
	@Test
	void fileDelete(@TempDir Path tempDir) throws IOException {
		Path file = Files.write(tempDir.resolve("d.txt"), "x".getBytes());
		assertTrue(FileUtils.fileDelete(file.toString()));
		assertFalse(Files.exists(file));
		assertFalse(FileUtils.fileDelete(tempDir.resolve("missing.txt").toString()));
	}

	@DisplayName("fileSave : 데이터를 UTF-8 로 기록한다")
	@Test
	void fileSave(@TempDir Path tempDir) throws IOException {
		Path file = tempDir.resolve("save.txt");
		FileUtils.fileSave(file.toString(), "내용DATA");
		assertEquals("내용DATA", Files.readString(file, StandardCharsets.UTF_8));
	}

	@DisplayName("fileSave : 쓰기 불가 경로면 IOException 을 삼키고 예외 없이 종료한다")
	@Test
	void fileSave_ioError(@TempDir Path tempDir) {
		// 디렉터리를 파일 경로로 전달 -> FileOutputStream 에서 IOException
		FileUtils.fileSave(tempDir.toString(), "data");
	}

	@DisplayName("rename : overWrite=true 로 기존 대상 파일을 덮어쓴다")
	@Test
	void rename_overwrite(@TempDir Path tempDir) throws IOException {
		Path src = Files.write(tempDir.resolve("src.txt"), "S".getBytes());
		Path dst = Files.write(tempDir.resolve("dst.txt"), "OLD".getBytes());

		assertTrue(FileUtils.rename(src.toString(), dst.toString(), true));
		assertFalse(Files.exists(src));
		assertEquals("S", Files.readString(dst));
	}

	@DisplayName("rename : overWrite=false 로 새 이름으로 변경한다")
	@Test
	void rename_noOverwrite(@TempDir Path tempDir) throws IOException {
		Path src = Files.write(tempDir.resolve("src2.txt"), "S".getBytes());
		Path dst = tempDir.resolve("dst2.txt");

		assertTrue(FileUtils.rename(src.toString(), dst.toString(), false));
		assertTrue(Files.exists(dst));
	}

	@DisplayName("renameWithExistFile : 둘 다 존재하면 변경 true")
	@Test
	void renameWithExistFile_bothExist(@TempDir Path tempDir) throws IOException {
		Path org = Files.write(tempDir.resolve("o.txt"), "O".getBytes());
		Path nw = Files.write(tempDir.resolve("n.txt"), "N".getBytes());

		assertTrue(FileUtils.renameWithExistFile(org.toString(), nw.toString()));
		assertEquals("O", Files.readString(nw));
	}

	@DisplayName("renameWithExistFile : 하나라도 없으면 false")
	@Test
	void renameWithExistFile_missing(@TempDir Path tempDir) throws IOException {
		Path org = Files.write(tempDir.resolve("o2.txt"), "O".getBytes());
		Path nw = tempDir.resolve("n2.txt"); // 미존재

		assertFalse(FileUtils.renameWithExistFile(org.toString(), nw.toString()));
	}

	@DisplayName("copyFile : 원본을 새 경로로 복사한다")
	@Test
	void copyFile_success(@TempDir Path tempDir) throws IOException {
		Path org = Files.write(tempDir.resolve("c.txt"), "COPY".getBytes());
		String newPath = tempDir.resolve("out").toString() + "/";

		assertTrue(FileUtils.copyFile(org.toString(), newPath, "c.txt"));
		assertEquals("COPY", Files.readString(Path.of(newPath, "c.txt")));
	}

	@DisplayName("copyFile : 원본이 없으면 false")
	@Test
	void copyFile_orgMissing(@TempDir Path tempDir) {
		String newPath = tempDir.resolve("out2").toString() + "/";
		assertFalse(FileUtils.copyFile(tempDir.resolve("none.txt").toString(), newPath, "x.txt"));
	}

	@DisplayName("copyFile : 입출력 예외 발생 시 false")
	@Test
	void copyFile_ioError(@TempDir Path tempDir) {
		// 원본으로 디렉터리를 전달 -> FileInputStream 에서 예외
		String newPath = tempDir.resolve("out3").toString() + "/";
		assertFalse(FileUtils.copyFile(tempDir.toString(), newPath, "x.txt"));
	}

	@DisplayName("fldrDelete : 하위 파일/폴더를 재귀적으로 삭제한다")
	@Test
	void fldrDelete_recursive(@TempDir Path tempDir) throws IOException {
		Path root = tempDir.resolve("root");
		Path sub = root.resolve("sub");
		Files.createDirectories(sub);
		Files.write(root.resolve("a.txt"), "a".getBytes());
		Files.write(sub.resolve("b.txt"), "b".getBytes());

		FileUtils.fldrDelete(root.toString());

		assertFalse(Files.exists(root));
	}

	@DisplayName("fldrDelete : 존재하지 않는 폴더는 무시한다")
	@Test
	void fldrDelete_missing(@TempDir Path tempDir) {
		FileUtils.fldrDelete(tempDir.resolve("nope").toString());
	}
}
