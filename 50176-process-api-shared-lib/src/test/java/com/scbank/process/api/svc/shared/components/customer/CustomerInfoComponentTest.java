package com.scbank.process.api.svc.shared.components.customer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.customer.dao.NfCustomerMgtDao;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.customer.dto.CustCddInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.CustCddInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.NonFaceCustomerInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistCustInfoRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistCustInfoResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistDestructionCustInfoRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.RegisterNonFaceCustomerInfoRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.ValidateCddExpireDateRequest;
import com.scbank.process.api.svc.shared.components.customer.mapper.CddInfoMapper;
import com.scbank.process.api.svc.shared.components.customer.mapper.NonFaceCustomerInfoMapper;
import com.scbank.process.api.svc.shared.components.tradinfo.TradInfoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoMgtDao;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerInfoComponentTest {

	@Mock private TradInfoComponent tradInfoComponent;
	@Mock private VerificationComponent verificationComponent;
	@Mock private HostClient hostClient;
	@Mock private ISessionContextManager sessionManager;
	@Mock private CddInfoMapper cddInfoMapper;
	@Mock private NonFaceCustomerInfoMapper nonFaceCustomerInfoMapper;
	@Mock private NfCustomerMgtDao nfCustMgtDao;
	@Mock private NfTradeInfoMgtDao nfTradeInfoMgtDao;

	@InjectMocks private CustomerInfoComponent component;

	private MockedStatic<SessionUtils> session;
	private MockedStatic<PropertiesUtils> props;
	private MockedStatic<DateUtils> dateUtils;

	@BeforeEach
	void setUp() {
		session = Mockito.mockStatic(SessionUtils.class);
		props = Mockito.mockStatic(PropertiesUtils.class);
		dateUtils = Mockito.mockStatic(DateUtils.class);

		session.when(() -> SessionUtils.getSessionValue(anyString())).thenReturn("");
		props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("90");
		dateUtils.when(() -> DateUtils.getCurrentDate(anyString())).thenReturn("20260601");

		when(hostClient.getOltpRequestOptions(anyString())).thenReturn(mock(OltpRequestOptions.class));
	}

	@AfterEach
	void tearDown() {
		session.close();
		props.close();
		dateUtils.close();
	}

//	@SuppressWarnings("unchecked")
//	private <T> void stubOltp(T responseBody) {
//		OltpResponse<T> response = mock(OltpResponse.class);
//		when(response.getResponse()).thenReturn(responseBody);
//		when(hostClient.sendOltp(any(), any(), any())).thenReturn(response);
//	}

	@Nested
	@DisplayName("고객정보/CDD 여부 조회")
	class GetCustCddInfo {

//		private CustCddInfoInquiryResponse mapped() {
//			CustCddInfoInquiryResponse response = mock(CustCddInfoInquiryResponse.class);
//			when(response.getYoJmNo()).thenReturn("8001011234567");
//			when(response.getYoCddOk()).thenReturn("Y");
//			when(response.getYoCddKycLc()).thenReturn("0");
//			when(response.getYoGeore()).thenReturn("1");
//			return response;
//		}

//		@Test
//		@DisplayName("성공_CDD 정보 조회")
//		public void successTest() {
//			stubOltp(mock(CbIbk01H92C00Res.class));
//			when(cddInfoMapper.toCustCddInfoResponseDto(any())).thenReturn(mapped());
//
//			CustCddInfoInquiryRequest request = mock(CustCddInfoInquiryRequest.class);
//			when(request.getIsFinal()).thenReturn("N");
//
//			assertNotNull(component.getCustCddInfo(request));
//		}

//		@Test
//		@DisplayName("실패_isFinal=Y인데 CDD 미완료 시 PRCCMMCDD_0001")
//		public void cddNotCompleteTest() {
//			CustCddInfoInquiryResponse response = mapped();
//			when(response.getYoCddOk()).thenReturn("N");
//			stubOltp(mock(CbIbk01H92C00Res.class));
//			when(cddInfoMapper.toCustCddInfoResponseDto(any())).thenReturn(response);
//
//			CustCddInfoInquiryRequest request = mock(CustCddInfoInquiryRequest.class);
//			when(request.getIsFinal()).thenReturn("Y");
//
//			assertThrows(PRCServiceException.class, () -> component.getCustCddInfo(request));
//		}
	}

	@Nested
	@DisplayName("CDD 유효일자 계산")
	class ValidateCddExpireDate {

		@Test
		@DisplayName("NTB(CDD일자 0) 이면 flag 공백")
		public void ntbTest() {
			ValidateCddExpireDateRequest request = new ValidateCddExpireDateRequest();
			request.setYoCddKycLc("0");
			assertNotNull(component.validateCddExpireDate(request));
		}

		@Test
		@DisplayName("CDD 유효일자 존재 시 계산")
		public void withDateTest() {
			ValidateCddExpireDateRequest request = new ValidateCddExpireDateRequest();
			request.setYoCddIl("20260701");
			assertNotNull(component.validateCddExpireDate(request));
		}
	}

//	@Nested
//	@DisplayName("고객정보확인변경 저장")
//	class RegistCddCustInfo {
//
//		@Test
//		@DisplayName("성공_기본 경로(타행인증/수정X)")
//		public void successTest() {
//			stubOltp(mock(CbIbk01H92000Res.class));
//			when(cddInfoMapper.toRegistCustInfoResponseDto(any())).thenReturn(mock(RegistCustInfoResponse.class));
//
//			RegistCustInfoRequest request = mock(RegistCustInfoRequest.class);
//			when(request.getBizType()).thenReturn("");
//			when(request.getIsUpdate()).thenReturn("N");
//			when(request.getOtherAcctAuthYn()).thenReturn("Y");
//
//			try (MockedStatic<BankingBizUtils> banking = Mockito.mockStatic(BankingBizUtils.class)) {
//				banking.when(() -> BankingBizUtils.retSplitPhoneNumber(anyString()))
//						.thenReturn(new String[] { "010", "1234", "5678" });
//				banking.when(() -> BankingBizUtils.toJSONStringFromObject(any())).thenReturn("{}");
//
//				// PerBusNo는 substring(6,7) 접근 → 세션값을 13자리로
//				session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("8001011234567");
//
//				assertNotNull(component.registCddCustInfo(request));
//			}
//		}
//	}

//	@Nested
//	@DisplayName("파기대상고객정보 신규등록")
//	class RegistDestructionCustInfo {
//
//		@Test
//		@DisplayName("성공_정상 등록")
//		public void successTest() {
//			stubOltp(mock(CbIbk01H89400Res.class));
//
//			RegistDestructionCustInfoRequest request = mock(RegistDestructionCustInfoRequest.class);
//			when(request.getCnnctnWay()).thenReturn("");
//			when(request.getReaCdn()).thenReturn("");
//			when(request.getYiCddGb()).thenReturn("");
//			when(request.getGaesaCode()).thenReturn("");
//
//			assertNotNull(component.registDestructionCustInfo(request));
//		}
//
//		@Test
//		@DisplayName("실패_제휴처코드 길이 초과 시 PRCCMM0070")
//		public void cnnctnWayTooLongTest() {
//			RegistDestructionCustInfoRequest request = mock(RegistDestructionCustInfoRequest.class);
//			when(request.getCnnctnWay()).thenReturn("1234567");
//
//			assertThrows(PRCServiceException.class, () -> component.registDestructionCustInfo(request));
//		}
//
//		@Test
//		@DisplayName("실패_REA코드 길이 초과 시 PRCCMM0071")
//		public void reaCdnTooLongTest() {
//			RegistDestructionCustInfoRequest request = mock(RegistDestructionCustInfoRequest.class);
//			when(request.getCnnctnWay()).thenReturn("");
//			when(request.getReaCdn()).thenReturn("1234567");
//
//			assertThrows(PRCServiceException.class, () -> component.registDestructionCustInfo(request));
//		}
//	}
//
//	@Nested
//	@DisplayName("비대면인증 고객관리")
//	class NonFaceCustomer {
//
//		@Test
//		@DisplayName("조회")
//		public void getNonFaceTest() {
//			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenReturn(mock(NonFaceCustomerInfoInquiryResult.class));
//
//			NonFaceCustomerInfoInquiryRequest request = mock(NonFaceCustomerInfoInquiryRequest.class);
//			when(request.getSsn()).thenReturn("8001011234567");
//
//			assertNotNull(component.getNonFaceCustomerInfo(request));
//		}
//
//		@Test
//		@DisplayName("수정")
//		public void updateNonFaceTest() {
//			when(nfCustMgtDao.updateNonFaceCustomerInfo(any())).thenReturn(1);
//
//			RegisterNonFaceCustomerInfoRequest request = mock(RegisterNonFaceCustomerInfoRequest.class);
//			when(request.getSsn()).thenReturn("8001011234567");
//
//			assertNotNull(component.updateNonFaceCustomerInfo(request));
//		}
//	}
}
