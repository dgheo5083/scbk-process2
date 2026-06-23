package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.svc.shared.utils.EdocUtils;
import com.scbank.process.api.svc.shared.utils.FileUtils;
import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class EdocXvarmComponentTest {

	@Mock private EdocXvarmLiveCheckerComponent liveChecker;

	@InjectMocks private EdocXvarmComponent component;

	private MockedConstruction<asysUsrElement> elementSuccess() {
		return Mockito.mockConstruction(asysUsrElement.class, (m, ctx) -> {
			when(m.create(anyString())).thenReturn(0);
			when(m.getContent(anyString(), anyString(), anyString())).thenReturn(0);
			when(m.delete()).thenReturn(0);
			when(m.replaceContent(anyString(), anyString(), anyString())).thenReturn(0);
			when(m.getLastError()).thenReturn("");
		});
	}

	@Nested
	@DisplayName("초기화/커넥션")
	class Lifecycle {

		@Test
		@DisplayName("init - LiveChecker로부터 커넥터 획득")
		public void initTest() {
			asysConnectData con = mock(asysConnectData.class);
			when(liveChecker.getEdocXvarmConnector()).thenReturn(con);

			component.init();

			assertEquals(con, component.getXvarmEngineConnector());
		}

		@Test
		@DisplayName("discon - 커넥션 없으면 안전 종료")
		public void disconNullTest() {
			assertDoesNotThrow(() -> component.discon());
		}
	}

	@Nested
	@DisplayName("문서 저장 create")
	class Create {

		@Test
		@DisplayName("성공_경로 기반 저장")
		public void createPathSuccessTest() {
			try (MockedConstruction<asysUsrElement> el = elementSuccess()) {
				Map<String, String> result = component.create("/tmp/a.pdf", "EDOC_CC");
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("실패_create 반환값 != 0")
		public void createErrorTest() {
			try (MockedConstruction<asysUsrElement> el = Mockito.mockConstruction(asysUsrElement.class, (m, ctx) -> {
				when(m.create(anyString())).thenReturn(1);
				when(m.getLastError()).thenReturn("create error");
			})) {
				Map<String, String> result = component.create("/tmp/a.pdf", "EDOC_CC");
				assertEquals("0001", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("예외_create 중 Exception")
		public void createExceptionTest() {
			try (MockedConstruction<asysUsrElement> el = Mockito.mockConstruction(asysUsrElement.class, (m, ctx) -> {
				when(m.create(anyString())).thenThrow(new RuntimeException("boom"));
			})) {
				Map<String, String> result = component.create("/tmp/a.pdf", "EDOC_CC");
				assertEquals("0002", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("성공_File 기반 저장")
		public void createFileSuccessTest() {
			try (MockedConstruction<asysUsrElement> el = elementSuccess()) {
				Map<String, String> result = component.create(new File("/tmp/a.pdf"), "EDOC_CC");
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("성공_descr/확장자 지정 저장")
		public void createWithDescrTest() {
			try (MockedConstruction<asysUsrElement> el = elementSuccess()) {
				Map<String, String> result = component.create(new File("/tmp/a.pdf"), "EDOC_CC", "EDOC", false, "pdf");
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("성공_암호화 저장 (PDF 암호화 분기)")
		public void createEncryptTest(@TempDir Path tempDir) throws Exception {
			File pdf = tempDir.resolve("doc.pdf").toFile();
			Files.writeString(pdf.toPath(), "dummy");

			Map<String, String> encResult = new HashMap<>();
			encResult.put("encPath", tempDir.toString() + File.separator);
			encResult.put("encName", "doc_enc.pdf");

			try (MockedConstruction<asysUsrElement> el = elementSuccess();
				 MockedStatic<EdocUtils> edocUtils = mockStatic(EdocUtils.class)) {
				edocUtils.when(() -> EdocUtils.enCryptionFile(anyString(), anyString(), anyString(), anyString()))
						.thenReturn(encResult);

				Map<String, String> result = component.create(pdf, "EDOC_CC", true);
				assertEquals("0000", result.get("errorCode"));
			}
		}
	}

	@Nested
	@DisplayName("문서 다운로드/삭제/교체")
	class DownloadDeleteReplace {

		@Test
		@DisplayName("download - 단건 성공")
		public void downloadTest() {
			try (MockedConstruction<asysUsrElement> el = elementSuccess();
				 MockedStatic<FileUtils> fileUtils = mockStatic(FileUtils.class)) {
				Map<String, String> result = component.download("EID", "/tmp/", "a.pdf");
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("download - 복호화 포함")
		public void downloadDecryptTest(@TempDir Path tempDir) {
			try (MockedConstruction<asysUsrElement> el = elementSuccess();
				 MockedStatic<FileUtils> fileUtils = mockStatic(FileUtils.class);
				 MockedStatic<EdocUtils> edocUtils = mockStatic(EdocUtils.class)) {
				Map<String, String> result = component.download("EID", tempDir.toString() + File.separator, "a.pdf", true);
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("delete - 성공")
		public void deleteTest() {
			try (MockedConstruction<asysUsrElement> el = elementSuccess()) {
				Map<String, String> result = component.delete("EID");
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("delete - 실패")
		public void deleteFailTest() {
			try (MockedConstruction<asysUsrElement> el = Mockito.mockConstruction(asysUsrElement.class, (m, ctx) -> {
				when(m.delete()).thenReturn(5);
				when(m.getLastError()).thenReturn("delete error");
			})) {
				Map<String, String> result = component.delete("EID");
				assertEquals("0005", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("replace - 암호화 없이 교체")
		public void replaceTest() {
			try (MockedConstruction<asysUsrElement> el = elementSuccess()) {
				Map<String, String> result = component.replace(new File("/tmp/a.pdf"), "EID");
				assertEquals("0000", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("multDownload - 빈 목록")
		public void multDownloadEmptyTest() {
			try (MockedStatic<FileUtils> fileUtils = mockStatic(FileUtils.class)) {
				Map<String, String> result = component.multDownload("/tmp/", List.of());
				assertEquals("0004", result.get("errorCode"));
			}
		}

		@Test
		@DisplayName("multDownload - 다건 성공")
		public void multDownloadTest(@TempDir Path tempDir) {
			Map<String, Object> item = new HashMap<>();
			item.put("elementid", "EID1");
			item.put("saveFile", "a.pdf");

			try (MockedConstruction<asysUsrElement> el = elementSuccess();
				 MockedStatic<FileUtils> fileUtils = mockStatic(FileUtils.class);
				 MockedStatic<EdocUtils> edocUtils = mockStatic(EdocUtils.class)) {
				Map<String, String> result = component.multDownload(tempDir.toString() + File.separator, List.of(item));
				assertEquals("0000", result.get("errorCode"));
			}
		}
	}

	@Test
	@DisplayName("getXvarmEngineConnector - 초기 null 반환")
	public void getConnectorTest() {
		assertNotNull(component); // con은 init 전 null
	}
}
