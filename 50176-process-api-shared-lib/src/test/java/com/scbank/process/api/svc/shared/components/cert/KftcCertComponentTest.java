package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.initech.oppra.IniOPPRA;
import com.initech.oppra.IniOPPRAIllegalFormatException;
import com.initech.oppra.IniOPPRAReadException;
import com.initech.oppra.IniOPPRASystemException;
import com.initech.oppra.util.IssueDataParser;
import com.initech.oppra.util.OppraMessageDataParser;
import com.initech.oppra.util.OppraSendDataParser;
import com.initech.oppra.util.StatusChangeDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;


@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class KftcCertComponentTest {

	@Mock private CertUtils certUtils;
	@InjectMocks private KftcCertComponent kftcCertComponent;

	@Test
	@DisplayName("금결원 인증서 상태조회 성공")
	public void searchCertTest() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(eq(50), any())).thenReturn("000$정상처리되었습니다");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(any(), any());
					doNothing().when(mock).setRecord(anyInt());

					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, String> rtn = kftcCertComponent.searchCert("04", "8007071999994");
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("금결원 인증서 상태조회 실패")
	public void searchCertTest2() throws Exception {
		assertThrows(PRCServiceException.class, () -> {
			kftcCertComponent.searchCert("04", "8007071999994");
		});
	}

	@Test
	@DisplayName("금결원 인증서 상태조회 실패2")
	public void searchCertTest3() throws Exception {
		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(any(), any());
					doNothing().when(mock).setRecord(anyInt());

					when(mock.getResCode()).thenReturn("000");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(eq(50), any())).thenThrow(new IniOPPRAReadException());

			when(oppra.getResCommonPart()).thenReturn("COMMON");
			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.searchCert("04", "8007071999994");
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 상태조회 실패3")
	public void searchCertTest4() throws Exception {
		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(any(), any());
					doNothing().when(mock).setRecord(anyInt());

					when(mock.getResCode()).thenReturn("000");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(eq(50), any())).thenThrow(new IniOPPRAIllegalFormatException());

			when(oppra.getResCommonPart()).thenReturn("COMMON");
			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.searchCert("04", "8007071999994");
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 상태변경 성공")
	public void changeStatusCert() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(eq(40), any())).thenReturn("000$정상처리되었습니다");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		try(
				MockedConstruction<StatusChangeDataParser> mocked = Mockito.mockConstruction(StatusChangeDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
					when(mock.getResMsg()).thenReturn("0109412$인증서가 이미 폐지 또는 정지되었습니다");
				});
		){
			Map<String, String> rtn = kftcCertComponent.changeStatusCert("123512","04", "30");
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("금결원 인증서 상태변경 실패")
	public void changeStatusCert2() throws Exception {
		assertThrows(PRCServiceException.class, () -> {
			kftcCertComponent.changeStatusCert("123512","04", "30");
		});
	}

	@Test
	@DisplayName("금결원 인증서 상태변경 실패2")
	public void changeStatusCert3() throws Exception {
		try(
				MockedConstruction<StatusChangeDataParser> mocked = Mockito.mockConstruction(StatusChangeDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
					when(mock.getResMsg()).thenReturn("0109412$인증서가 이미 폐지 또는 정지되었습니다");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(eq(40), any())).thenThrow(new IniOPPRAReadException());

			when(oppra.getResCommonPart()).thenReturn("COMMON");
			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.changeStatusCert("123512","04", "30");
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 상태변경 실패3")
	public void changeStatusCert4() throws Exception {
		try(
				MockedConstruction<StatusChangeDataParser> mocked = Mockito.mockConstruction(StatusChangeDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
					when(mock.getResMsg()).thenReturn("0109412$인증서가 이미 폐지 또는 정지되었습니다");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(eq(40), any())).thenThrow(new IniOPPRAIllegalFormatException());

			when(oppra.getResCommonPart()).thenReturn("COMMON");
			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.changeStatusCert("123512","04", "30");
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 성공[신규]")
	public void issueCert() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, String> rtn = kftcCertComponent.issueCert("1", raMap);
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 성공[재발급]")
	public void issueCert2() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, String> rtn = kftcCertComponent.issueCert("2", raMap);
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패")
	public void issueCert3() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		assertThrows(PRCServiceException.class, () -> {
			kftcCertComponent.issueCert("", raMap);
		});
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_정보통신부의 정책에 따라 금융결제원 범용 인증서 발급이 허용되지 않습니다")
	public void issueCert4() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("820");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_[00은행]에서 등록 및 미발급 상태")
	public void issueCert5() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("216");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_갱신 등록 후 미발급 상태")
	public void issueCert6() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		IniOPPRA oppraRevoke = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppraRevoke);
		when(oppraRevoke.requestRAW(anyInt(), any())).thenReturn("");

		IniOPPRA oppraRetry = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppraRetry);
		when(oppraRetry.requestRAW(anyInt(), any())).thenReturn("");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					int cnt = context.getCount();
					if(cnt == 1) when(mock.getResCode()).thenReturn("922");
					if(cnt > 1) when(mock.getResCode()).thenReturn("000");
				});
		){
				kftcCertComponent.issueCert("1", raMap);
		}

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("926");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("927");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_고객께서는 이미 인증서를 발급받으셨으며 현재 효력정지 상태입니다")
	public void issueCert7() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");
		Map<String, Object> raMap2 = new HashMap<String, Object>();
		raMap.put("CERTCODE", "16");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("217");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap2);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_이미 발급하신 금융인증서가 존재합니다")
	public void issueCert8() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");
		Map<String, Object> raMap2 = new HashMap<String, Object>();
		raMap.put("CERTCODE", "16");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("214");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap2);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_금융결제원에서 발행하는 공인인증서는 금융기관중 한곳에서만 발급받으실 수 있습니다")
	public void issueCert9() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("210");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패_가입자 등록에 실패하였습니다.")
	public void issueCert10() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("211");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패")
	public void issueCert11() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResCommonPart()).thenReturn("COMMON");
		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("999");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패2")
	public void issueCert12() throws Exception {
		Map<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("CERTCODE", "04");

		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResDataPart()).thenReturn("DATA");

		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패[신규]")
	public void issueCert13() throws Exception {
		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, Object> raMap = new HashMap<String, Object>();
			raMap.put("CERTCODE", "04");

			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getRegOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAReadException());

			when(oppra.getResCommonPart()).thenReturn("COMMON");
			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 발급 실패[신규]")
	public void issueCert14() throws Exception {
		OppraSendDataParser sendDataParser = mock(OppraSendDataParser.class);
		when(sendDataParser.getSendLastData()).thenReturn("202026011611295100010109kfbadmin0309$1$$업무테스트()$8007071999994$LOANTE80$01$01$04$TEST@SC.COM$010 11111111$02-2140-3699$110858$1$$");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, Object> raMap = new HashMap<String, Object>();
			raMap.put("CERTCODE", "04");

			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getRegOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAIllegalFormatException());

			when(oppra.getResCommonPart()).thenReturn("COMMON");
			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.issueCert("1", raMap);
			});
		}
	}

	@Test
	@DisplayName("타기관 인증서 등록 성공")
	public void regOtherCert() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("000$정상처리되었습니다");

		when(oppra.getResDataPart()).thenReturn("DATA");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(any(), any());
					doNothing().when(mock).setRecord(anyInt());

					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, String> rtn = kftcCertComponent.regOtherCert("123545", "04", "LOANTE80");
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("타기관 인증서 등록 실패")
	public void regOtherCert2() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("");

		when(oppra.getResDataPart()).thenReturn("DATA");

		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("999");
				});
		){
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.regOtherCert("123545", "04", "LOANTE80");
			});
		}
	}

	@Test
	@DisplayName("타기관 인증서 등록 실패")
	public void regOtherCert3() throws Exception {
		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(any(), any());
					doNothing().when(mock).setRecord(anyInt());

					when(mock.getResCode()).thenReturn("000");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAIllegalFormatException());

			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.regOtherCert("123545", "04", "LOANTE80");
			});

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.regOtherCert("123545", "16", "LOANTE80");
			});
		}
	}

	@Test
	@DisplayName("타기관 인증서 등록 실패2")
	public void regOtherCert4() throws Exception {
		try(
				MockedConstruction<OppraMessageDataParser> mocked = Mockito.mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(any(), any());
					doNothing().when(mock).setRecord(anyInt());

					when(mock.getResCode()).thenReturn("000");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAReadException());

			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.regOtherCert("123545", "04", "LOANTE80");
			});

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.regOtherCert("123545", "16", "LOANTE80");
			});
		}
	}

	@Test
	@DisplayName("타행 금결원 인증서 등록 해제 성공")
	public void revokeOtherCert() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("000$정상처리되었습니다");

		when(oppra.getResDataPart()).thenReturn("DATA");

		try(
				MockedConstruction<StatusChangeDataParser> mocked = Mockito.mockConstruction(StatusChangeDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
					when(mock.getResMsg()).thenReturn("0109412$인증서가 이미 폐지 또는 정지되었습니다");
				});
		){
			Map<String, String> rtn = kftcCertComponent.revokeOtherCert("123545", "04");
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("타행 금결원 인증서 등록 해제 실패")
	public void revokeOtherCert2() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("000$정상처리되었습니다");

		when(oppra.getResDataPart()).thenThrow(new PRCServiceException(""));

		assertThrows(PRCServiceException.class, () -> {
			kftcCertComponent.revokeOtherCert("123545", "04");
		});
	}

	@Test
	@DisplayName("타행 금결원 인증서 등록 해제 실패")
	public void revokeOtherCert3() throws Exception {
		try(
				MockedConstruction<StatusChangeDataParser> mocked = Mockito.mockConstruction(StatusChangeDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
					when(mock.getResMsg()).thenReturn("0109412$인증서가 이미 폐지 또는 정지되었습니다");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAIllegalFormatException());

			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.revokeOtherCert("123545", "04");
			});
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.revokeOtherCert("123545", "16");
			});
		}
	}

	@Test
	@DisplayName("타행 금결원 인증서 등록 해제 실패")
	public void revokeOtherCert4() throws Exception {
		try(
				MockedConstruction<StatusChangeDataParser> mocked = Mockito.mockConstruction(StatusChangeDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
					when(mock.getResMsg()).thenReturn("0109412$인증서가 이미 폐지 또는 정지되었습니다");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAReadException());

			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.revokeOtherCert("123545", "04");
			});
			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.revokeOtherCert("123545", "16");
			});
		}
	}

	@Test
	@DisplayName("금결원 인증서 갱신 성공")
	public void renewCert() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(certUtils.getOppra()).thenReturn(oppra);
		when(oppra.requestRAW(anyInt(), any())).thenReturn("000$정상처리되었습니다");

		when(oppra.getResDataPart()).thenReturn("DATA");

		try(
				MockedConstruction<IssueDataParser> mocked = Mockito.mockConstruction(IssueDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
				});
		){
			Map<String, String> rtn = kftcCertComponent.renewCert("1", "LOANTE80", "8007071999994", "04", "", "", "", "");
			assertNotNull(rtn);
		}
	}

	@Test
	@DisplayName("금결원 인증서 갱신 실패")
	public void renewCert2() throws Exception {
		try(
				MockedConstruction<IssueDataParser> mocked = Mockito.mockConstruction(IssueDataParser.class, (mock, context) -> {
					when(mock.getResCode()).thenReturn("000");
				});
		){
			IniOPPRA oppra = mock(IniOPPRA.class);
			when(certUtils.getOppra()).thenReturn(oppra);
			when(oppra.requestRAW(anyInt(), any())).thenThrow(new IniOPPRAReadException());

			when(oppra.getResDataPart()).thenReturn("DATA");

			assertThrows(PRCServiceException.class, () -> {
				kftcCertComponent.renewCert("1", "LOANTE80", "8007071999994", "04", "", "", "", "");
			});
		}
	}
}
