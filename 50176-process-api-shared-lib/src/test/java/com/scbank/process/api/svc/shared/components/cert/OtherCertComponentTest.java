package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.security.cert.X509Certificate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.initech.ocspgd.OCSPGD;
import com.initech.ocspgd.OCSPGDConnectException;
import com.initech.ocspgd.OCSPGDReadException;
import com.initech.ocspgd.util.DefaultDataParser;
import com.initech.ocspgd.util.InquiryMsg;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class OtherCertComponentTest {

	@InjectMocks private OtherCertComponent otherCertComponent;

	MockedStatic<PropertiesUtils> mockPropertiesUtils;

	@BeforeEach
	void setUp() {
		mockPropertiesUtils = Mockito.mockStatic(PropertiesUtils.class);
	}

	@AfterEach
	void tearDown() {
		mockPropertiesUtils.close();
	}

	@Nested
    @DisplayName("타기관(금결원 외) 인증서 등록")
	class issueCert {

		@Test
		@DisplayName("성공")
		public void issueCertTest() throws Exception {
			X509Certificate cert = mock(X509Certificate.class);

			try(MockedStatic<InquiryMsg> inqueryMock = mockStatic(InquiryMsg.class);
				MockedStatic<DefaultDataParser> parserMock = mockStatic(DefaultDataParser.class);
				MockedConstruction<OCSPGD> mocked = Mockito.mockConstruction(OCSPGD.class, (mock, context) -> {
					when(mock.getResponseCommonPart()).thenReturn("COMMON");
					when(mock.getResponseDataPart()).thenReturn("DATA");
			})) {
				inqueryMock.when(() -> InquiryMsg.createRegMsg(any(), any(), any())).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
				parserMock.when(() -> DefaultDataParser.getMsg(any(), anyInt())).thenReturn("000");
				otherCertComponent.issueCert(cert, "testID", "8007071999994");
			}
		}

		@Test
		@DisplayName("실패_인증서 효력정지 상태")
		public void issueCertTest2() throws Exception {
			X509Certificate cert = mock(X509Certificate.class);

			try(MockedStatic<InquiryMsg> inqueryMock = mockStatic(InquiryMsg.class);
				MockedStatic<DefaultDataParser> parserMock = mockStatic(DefaultDataParser.class);
				MockedConstruction<OCSPGD> mocked = Mockito.mockConstruction(OCSPGD.class, (mock, context) -> {
					when(mock.getResponseCommonPart()).thenReturn("COMMON");
					when(mock.getResponseDataPart()).thenReturn("DATA");
			})) {
				inqueryMock.when(() -> InquiryMsg.createRegMsg(any(), any(), any())).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
				parserMock.when(() -> DefaultDataParser.getMsg(any(), anyInt())).thenReturn("999");

				assertThrows(PRCServiceException.class, () -> {
					otherCertComponent.issueCert(cert, "testID", "8007071999994");
				});
			}
		}

		@Test
		@DisplayName("실패_OCSPGD의 소켓으로부터 읽기 쓰기 실패입니다")
		public void issueCertTest3() throws Exception {
			X509Certificate cert = mock(X509Certificate.class);

			try(MockedStatic<InquiryMsg> inqueryMock = mockStatic(InquiryMsg.class);
				MockedStatic<DefaultDataParser> parserMock = mockStatic(DefaultDataParser.class);
				MockedConstruction<OCSPGD> mocked = Mockito.mockConstruction(OCSPGD.class, (mock, context) -> {
					doThrow(new OCSPGDReadException()).when(mock).requestRAW(anyInt(), any());
			})) {
				inqueryMock.when(() -> InquiryMsg.createRegMsg(any(), any(), any())).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");

				PRCServiceException ex = assertThrows(PRCServiceException.class, () -> otherCertComponent.issueCert(cert, "testID", "8007071999994"));
				assertTrue(ex.getMessage().contains("PRCCRT20554"));
			}
		}

		@Test
		@DisplayName("실패_OCSPGD관련 알수 없는 오류가 발생하였습니다")
		public void issueCertTest4() throws Exception {
			X509Certificate cert = mock(X509Certificate.class);

			try(MockedStatic<InquiryMsg> inqueryMock = mockStatic(InquiryMsg.class);
				MockedStatic<DefaultDataParser> parserMock = mockStatic(DefaultDataParser.class);
				MockedConstruction<OCSPGD> mocked = Mockito.mockConstruction(OCSPGD.class, (mock, context) -> {
					new OCSPGDConnectException();
			})) {
				PRCServiceException ex = assertThrows(PRCServiceException.class, () -> otherCertComponent.issueCert(cert, "testID", "8007071999994"));
				assertTrue(ex.getErrorCode().contains("PRCCRT20558"));
			}
		}
	}

}
