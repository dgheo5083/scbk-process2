package com.scbank.process.api.svc.shared.components.cert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import secu.bra.BRAProc;

import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class KicaCertComponentTest {

	@InjectMocks private KicaCertComponent kicaCertComponent;

	@Nested
    @DisplayName("한국정보인증 인증서 상태 검증")
	class getBraCertStatus {
		@Test
		@DisplayName("성공_[신규]")
		public void getBraCertStatusTest() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("nok");
			vRsltData.add("NODATA");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("실패_RA(CA)서버로 데이터 전송에 실패했습니다(SGE001)")
		public void getBraCertStatusTest2() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("nok");
			vRsltData.add("TEST");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				});
			}
		}

		@Test
		@DisplayName("실패_RA(CA)서버로 데이터 전송에 실패했습니다(SGE001)")
		public void getBraCertStatusTest2_2() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("nok");
			vRsltData.add(null);

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				});
			}
		}

		@Test
		@DisplayName("실패_RA(CA)서버로 데이터 전송에 실패했습니다(SGE001)")
		public void getBraCertStatusTest3() throws Exception {

			Vector vRsltData = new Vector();

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				});
			}
		}

		@Test
		@DisplayName("실패2_RA(CA)서버로 데이터 전송에 실패했습니다(SGE001)")
		public void getBraCertStatusTest3_2() throws Exception {
			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(null);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				});
			}
		}

		@Test
		@DisplayName("성공_무료[재등록]")
		public void getBraCertStatusTest4() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("20");
			vRsltData.add("Y");
			vRsltData.add("Y");
			vRsltData.add("Y");
			vRsltData.add("Y");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_무료[재등록]")
		public void getBraCertStatusTest4_2() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add(null);
			vRsltData.add("Y");
			vRsltData.add("Y");
			vRsltData.add("Y");
			vRsltData.add("Y");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_유료[재등록]")
		public void getBraCertStatusTest5() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("25");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_유료[재등록]")
		public void getBraCertStatusTest5_2() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("2");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_유료[재등록]")
		public void getBraCertStatusTest5_3() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("30");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("실패_RA(CA)서버로 부터 전송받은 데이터 검증에 실패했습니다(SGE003)[재등록]")
		public void getBraCertStatusTest6() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("-20");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.getBraCertStatus("2", "testID", "8007071999994");
				});
			}
		}
	}

	@Nested
    @DisplayName("한국정보인증 인증서 참조번호, 인가코드 발급")
	class reIssueCert {

		@Test
		@DisplayName("성공")
		public void reIssueCertTest() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "20", "1", 1);
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공")
		public void reIssueCertTest1() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "22", "1", 1);
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공2")
		public void reIssueCertTest2() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "25", "1", 1);
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("실패_인증서 (재)발급에 대한 응답전문을 수신하지 못하였습니다")
		public void reIssueCertTest3() throws Exception {

			Vector vRsltData = new Vector();

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "25", "1", 1);
				});
			}
		}

		@Test
		@DisplayName("실패2_인증서 (재)발급에 대한 응답전문을 수신하지 못하였습니다")
		public void reIssueCertTest3_2() throws Exception {
			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(null);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "25", "1", 1);
				});
			}
		}

		@Test
		@DisplayName("실패_다음과 같은 오류가 발생하였습니다")
		public void reIssueCertTest4() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("nok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "25", "1", 1);
				});
			}
		}

		@Test
		@DisplayName("실패_인증서 (재)발급에서 참조번호,인가코드를 받아오는데 실패하였습니다")
		public void reIssueCertTest5() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.reIssueCert("testID", "8007071999994", "25", "1", 1);
				});
			}
		}
	}

	@Nested
    @DisplayName("한국정보인증 인증서 발급")
	class issueCert {

		@Test
		@DisplayName("성공_개인[신규]")
		public void issueCertTest() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				Map<String, String> rtn = kicaCertComponent.issueCert(1, uniCertSession, "2", "1");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_개인사업자[신규]")
		public void issueCertTest2() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "2", "2");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_법인[재인가]")
		public void issueCertTest3() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				Map<String, String> rtn = kicaCertComponent.issueCert(1, uniCertSession, "19", "3");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("성공_법인[재인가]")
		public void issueCertTest4() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "19", "3");
				assertThat(rtn).isNotNull();
			}
		}

		@Test
		@DisplayName("실패_인증시스템에서 오류가 발생하였습니다.")
		public void issueCertTest5() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "1", "3");
				});
			}
		}

		@Test
		@DisplayName("실패_인증서 (재)발급에 대한 응답전문을 수신하지 못하였습니다.")
		public void issueCertTest6() throws Exception {

			Vector vRsltData = new Vector();

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "19", "3");
				});
			}
		}

		@Test
		@DisplayName("실패2_인증서 (재)발급에 대한 응답전문을 수신하지 못하였습니다.")
		public void issueCertTest6_2() throws Exception {
			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(null);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "19", "3");
				});
			}
		}

		@Test
		@DisplayName("실패_다음과 같은 오류가 발생하였습니다")
		public void issueCertTest7() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("nok");
			vRsltData.add("NODATA");
			vRsltData.add("1313141515");
			vRsltData.add("cfaf132154165sad");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "19", "3");
				});
			}
		}

		@Test
		@DisplayName("실패_인증서 (재)발급에서 참조번호,인가코드를 받아오는데 실패하였습니다")
		public void issueCertTest8() throws Exception {

			Vector vRsltData = new Vector();
			vRsltData.add("ok");
			vRsltData.add("1313141515");

			try(MockedConstruction<BRAProc> mocked = Mockito.mockConstruction(BRAProc.class, (mock, context) -> {
				when(mock.sendData(anyInt(), any())).thenReturn(vRsltData);
			})) {
				Map<String, Object> uniCertSession = new HashMap<>();
				uniCertSession.put("DeptPersonName", "김제일");
				uniCertSession.put("RegNo", "8007071999994");
				uniCertSession.put("SaupjaNo", "");
				uniCertSession.put("CompanyName", "");

				assertThrows(PRCServiceException.class, () -> {
					Map<String, String> rtn = kicaCertComponent.issueCert(2, uniCertSession, "19", "3");
				});
			}
		}

	}

}
