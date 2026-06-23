package com.scbank.process.api.svc.common.service.verification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import com.interezen.loader.QLoader;
import com.scbank.process.api.edmi.dto.edmi.CbIdentifyDacomRes;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H76500Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H76500Res;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.mci.exception.MciSystemException;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.common.dao.dto.TokenAuthResult;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySMSIdentifyRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySMSIdentifyResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySimpleRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplySimpleResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalRegistDeviceAuthRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalRegistDeviceAuthResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalValidateSimpleRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalValidateSimpleResponse;
import com.scbank.process.api.svc.shared.components.ipinside.dto.PcFixInfo;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthRequest;
import com.scbank.process.api.svc.shared.components.simpleAuth.dto.SimpleAuthResponse;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiResponse;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.edmi.dto.mci.MciIbFep01001Req;
import com.scbank.process.api.edmi.dto.mci.MciIbFep01001Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.components.VerificationAdditionalComponent;
import com.scbank.process.api.svc.common.components.dto.VerificationGetTelNoDto;
import com.scbank.process.api.svc.common.dao.UsimAuthMgtDao;
import com.scbank.process.api.svc.common.mapper.VerificationAdditionalMapper;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalActivateIdVerificationResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalActivateRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalActivateResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplyARSRequest;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalApplyARSResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalConfirmOverseasDepartureResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalCreateARSAuthKeyResponse;
import com.scbank.process.api.svc.common.service.verification.dto.additional.VerificationAdditionalGetDeviceAuthResponse;
import com.scbank.process.api.svc.common.service.verification.utils.VerificationHelper;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;
import com.scbank.process.api.svc.shared.components.customer.dao.NfCustomerMgtDao;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.device.DeviceAuthComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.simpleAuth.TCertComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoMgtDao;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.RandomKeyUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VerificationAdditionalService")
class VerificationAdditionalServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private HostClient hostClient;

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private UsimAuthMgtDao usimAuthMgtDao;

    @Mock
    private DeviceAuthUserDao deviceAuthUserDao;

    @Mock
    private NfCustomerMgtDao nfCustomerMgtDao;

    @Mock
    private NfTradeInfoMgtDao nfTradeInfoMgtDao;

    @Mock
    private TCertComponent tcert;

    @Mock
    private IpinsideComponent ipinside;

    @Mock
    private VerificationAdditionalComponent additional;

    @Mock
    private VerificationHelper helper;

    @Mock
    private VerificationComponent verificationComponent;

    @Mock
    private CertificationSharedComponent certificationSharedComponent;

    @Mock
    private AccountListComponent account;

    @Mock
    private DeviceAuthComponent deviceAuth;

    @Mock
    private SecureDataComponent secureData;

    @Mock
    private VerificationAdditionalMapper mapper;

    @InjectMocks
    private VerificationAdditionalService service;

    @Nested
    @DisplayName("activate")
    class Activate {

        @Test
        @DisplayName("추가인증 대상이면 ADD_AUTH_SUCCESS_FLAG N")
        void activateWithAdditionalTypeTest() {
            VerificationAdditionalActivateRequest request = new VerificationAdditionalActivateRequest();
            request.setAdditionalType("A:B");
            request.setTransType("LOGIN");

            VerificationGetTelNoDto telNoDto = new VerificationGetTelNoDto();
            telNoDto.setWasTranNo("TR001");
            telNoDto.setPhoneNo("01012345678");
            telNoDto.setMaskPhoneNo("010-1234-****");

            when(additional.getAdditionalType("A:B", "LOGIN")).thenReturn("A:B");
            when(additional.getTelNo()).thenReturn(telNoDto);

            VerificationAdditionalActivateResponse response = service.activate(ctx, request);

            assertEquals("A:B", response.getAdditionalType());
            assertEquals("01012345678", response.getPhoneNo());
            verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "N");
        }

        @Test
        @DisplayName("전화번호 전체 필드 세팅")
        void activateAllTelFieldsTest() {
            VerificationAdditionalActivateRequest request = new VerificationAdditionalActivateRequest();
            request.setTransType("LOGIN");

            VerificationGetTelNoDto telNoDto = new VerificationGetTelNoDto();
            telNoDto.setWasTranNo("TR001");
            telNoDto.setPhoneNo("01012345678");
            telNoDto.setMaskPhoneNo("010-1234-****");
            telNoDto.setPhoneNo1("010");
            telNoDto.setPhoneNo2("1234");
            telNoDto.setPhoneNo3("5678");
            telNoDto.setHomeTelNo("0212345678");
            telNoDto.setMaskHomeTelNo("02-1234-****");
            telNoDto.setHomeTelNo1("02");
            telNoDto.setHomeTelNo2("1234");
            telNoDto.setHomeTelNo3("5678");
            telNoDto.setJobTelNo("0312345678");
            telNoDto.setMaskJobTelNo("031-234-****");
            telNoDto.setJobTelNo1("031");
            telNoDto.setJobTelNo2("234");
            telNoDto.setJobTelNo3("5678");

            when(additional.getAdditionalType(null, "LOGIN")).thenReturn("A");
            when(additional.getTelNo()).thenReturn(telNoDto);

            VerificationAdditionalActivateResponse response = service.activate(ctx, request);

            assertEquals("010", response.getPhoneNo1());
            assertEquals("02", response.getHomeTelNo1());
            assertEquals("031", response.getJobTelNo1());
            verify(sessionManager).removeGlobalValue("IDENTIFY_SSN");
        }

        @Test
        @DisplayName("추가인증 대상 아니면 ADD_AUTH_SUCCESS_FLAG Y")
        void activatePassTest() {
            VerificationAdditionalActivateRequest request = new VerificationAdditionalActivateRequest();
            request.setTransType("LOGIN");

            VerificationGetTelNoDto telNoDto = new VerificationGetTelNoDto();

            when(additional.getAdditionalType(null, "LOGIN")).thenReturn("");
            when(additional.getTelNo()).thenReturn(telNoDto);

            service.activate(ctx, request);

            verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
        }
    }

    @Nested
    @DisplayName("createARSAuthNumber")
    class CreateARSAuthNumber {

        @Test
        @DisplayName("ARS 인증번호 생성")
        void createARSAuthNumberTest() {
            try (MockedStatic<RandomKeyUtils> randomKey = mockStatic(RandomKeyUtils.class)) {
                randomKey.when(RandomKeyUtils::getKey).thenReturn("X12");

                VerificationAdditionalCreateARSAuthKeyResponse response = service.createARSAuthNumber(ctx);

                assertEquals("12", response.getAuthNumber());
                verify(sessionManager).setGlobalValue("SEND_ARS_AUTH_NUMBER", "12");
            }
        }
    }

    @Nested
    @DisplayName("applyARS")
    class ApplyARS {

        @Test
        @DisplayName("일반 전화번호 ARS 인증 성공")
        void applyARSSuccessTest() {
            VerificationAdditionalApplyARSRequest request = new VerificationAdditionalApplyARSRequest();
            request.setAuthTelNo("01011112222");
            request.setWasTranNo("TR001");

            when(sessionManager.getGlobalValue("SEND_ARS_AUTH_NUMBER", String.class)).thenReturn("12");

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");

                VerificationAdditionalApplyARSResponse response = service.applyARS(ctx, request);

                assertEquals("0000", response.getResultCode());
                assertEquals("TR001", response.getTranId());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "D");
            }
        }
    }

    @Nested
    @DisplayName("confirmOverseasDeparture")
    class ConfirmOverseasDeparture {

        @Test
        @DisplayName("해외출국 확인 성공")
        void confirmOverseasDepartureSuccessTest() {
            MciRequestOptions options = org.mockito.Mockito.mock(MciRequestOptions.class);
            MciIbFep01001Res outputDto = new MciIbFep01001Res();
            outputDto.setRES_CD("000");
            outputDto.setABROAD_YN("00");

            MciResponse<MciIbFep01001Res> mciResponse = new MciResponse<>();
            mciResponse.setResponse(outputDto);

            when(hostClient.getMciRequestOptions("MCI_IB_FEP01_001")).thenReturn(options);
            when(hostClient.sendMci(eq(options), any(MciIbFep01001Req.class), eq(MciIbFep01001Res.class)))
                    .thenReturn(mciResponse);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                dateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.HHMMSS)).thenReturn("120000");
                dateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS)).thenReturn("20250101120000");

                VerificationAdditionalConfirmOverseasDepartureResponse response = service
                        .confirmOverseasDeparture(ctx);

                assertEquals("000", response.getResultCode());
                assertEquals("00", response.getAbroadYn());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "F");
            }
        }

        @Test
        @DisplayName("해외출국 확인 실패 - 세션 미설정")
        void confirmOverseasDepartureFailTest() {
            MciRequestOptions options = org.mockito.Mockito.mock(MciRequestOptions.class);
            MciIbFep01001Res outputDto = new MciIbFep01001Res();
            outputDto.setRES_CD("200");
            outputDto.setABROAD_YN("1");

            MciResponse<MciIbFep01001Res> mciResponse = new MciResponse<>();
            mciResponse.setResponse(outputDto);

            when(hostClient.getMciRequestOptions("MCI_IB_FEP01_001")).thenReturn(options);
            when(hostClient.sendMci(eq(options), any(MciIbFep01001Req.class), eq(MciIbFep01001Res.class)))
                    .thenReturn(mciResponse);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                dateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.HHMMSS)).thenReturn("120000");
                dateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS)).thenReturn("20250101120000");

                VerificationAdditionalConfirmOverseasDepartureResponse response = service
                        .confirmOverseasDeparture(ctx);

                assertEquals("200", response.getResultCode());
                verify(sessionManager, org.mockito.Mockito.never()).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
            }
        }

        @Test
        @DisplayName("해외출국 확인 - RES_CD 0 단일자리 성공")
        void confirmOverseasDepartureResCdZeroTest() {
            MciRequestOptions options = org.mockito.Mockito.mock(MciRequestOptions.class);
            MciIbFep01001Res outputDto = new MciIbFep01001Res();
            outputDto.setRES_CD("0");
            outputDto.setABROAD_YN("0");

            MciResponse<MciIbFep01001Res> mciResponse = new MciResponse<>();
            mciResponse.setResponse(outputDto);

            when(hostClient.getMciRequestOptions("MCI_IB_FEP01_001")).thenReturn(options);
            when(hostClient.sendMci(eq(options), any(MciIbFep01001Req.class), eq(MciIbFep01001Res.class)))
                    .thenReturn(mciResponse);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                dateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.HHMMSS)).thenReturn("120000");
                dateUtils.when(() -> DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS)).thenReturn("20250101120000");

                VerificationAdditionalConfirmOverseasDepartureResponse response = service
                        .confirmOverseasDeparture(ctx);

                assertEquals("0", response.getResultCode());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
            }
        }
    }

    @Nested
    @DisplayName("activateIdVerification")
    class ActivateIdVerification {

        @Test
        @DisplayName("주민번호 없으면 예외")
        void missingPerBusNoTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("");

                assertThrows(PRCServiceException.class, () -> service.activateIdVerification(ctx));
            }
        }

        @Test
        @DisplayName("비대면실명확인 초기화 성공")
        void activateIdVerificationSuccessTest() {
            NonFaceCustomerInfoInquiryResult customerInfo = new NonFaceCustomerInfoInquiryResult();
            customerInfo.setCustNo("CUST001");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("RegiBranchNum", String.class)).thenReturn("100");
            when(nfCustomerMgtDao.selectNonFaceCustomerInfo(any())).thenReturn(customerInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                dateUtils.when(() -> DateUtils.getCurrentDate("yyyyMMddHHmmss")).thenReturn("20250101120000");

                when(nfTradeInfoMgtDao.insertNonfaceTradeInfo(any())).thenAnswer(invocation -> {
                    invocation.getArgument(0, com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterOngoingTradeInfoParameter.class)
                            .setTradNo("TRAD001");
                    return 1;
                });

                VerificationAdditionalActivateIdVerificationResponse response = service.activateIdVerification(ctx);

                assertEquals("CUST001", response.getCustNo());
                assertEquals("TRAD001", response.getTradNo());
                assertEquals("IDCM", response.getBizType());
                verify(sessionManager).setGlobalValue("CUST_NO", "CUST001");
            }
        }

        @Test
        @DisplayName("비로그인 + 고객정보 없으면 insert 후 초기화")
        void activateIdVerificationInsertCustomerTest() {
            NonFaceCustomerInfoInquiryResult customerInfo = new NonFaceCustomerInfoInquiryResult();
            customerInfo.setCustNo("CUST002");

            when(sessionManager.isLogin()).thenReturn(false);
            when(sessionManager.getGlobalValue("BranchNum", String.class)).thenReturn("200");
            when(sessionManager.getGlobalValue("MOBILENUM", String.class)).thenReturn("01099998888");
            when(sessionManager.getGlobalValue("CustName", String.class)).thenReturn("테스트");
            when(nfCustomerMgtDao.selectNonFaceCustomerInfo(any()))
                    .thenReturn(null)
                    .thenReturn(customerInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                dateUtils.when(() -> DateUtils.getCurrentDate("yyyyMMddHHmmss")).thenReturn("20250101120000");

                when(nfTradeInfoMgtDao.insertNonfaceTradeInfo(any())).thenAnswer(invocation -> {
                    invocation.getArgument(0,
                            com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterOngoingTradeInfoParameter.class)
                            .setTradNo("TRAD002");
                    return 1;
                });

                VerificationAdditionalActivateIdVerificationResponse response = service.activateIdVerification(ctx);

                assertEquals("CUST002", response.getCustNo());
                assertEquals("200", response.getBrnchNo());
                verify(nfCustomerMgtDao).insertNonFaceCustomerInfo(any());
            }
        }
    }

    @Nested
    @DisplayName("getDeviceAuth")
    class GetDeviceAuth {

        @Test
        @DisplayName("단말기지정서비스 조회")
        void getDeviceAuthTest() {
            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(deviceAuthUserDao.selectDeviceAuthCount(any())).thenReturn(2);
            when(deviceAuthUserDao.selectDeviceAuthInfo(any()))
                    .thenReturn(List.of(new DeviceAuthUserResult()));
            when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "AA-BB-CC-DD-EE-FF", "HDD001" });
            when(mapper.toDeviceAuthInfo(any())).thenReturn(Collections.emptyList());

            VerificationAdditionalGetDeviceAuthResponse response = service.getDeviceAuth(ctx);

            assertEquals("AA-BB-CC-DD-EE-FF", response.getMacAddress());
            assertEquals("AA-BB-CC-DD-EE**-**", response.getMaskMacAddress());
            assertEquals(2, response.getDeviceCount());
            assertNotNull(response.getDeviceAuthList());
        }

        @Test
        @DisplayName("userId 없으면 deviceCount 0")
        void getDeviceAuthNoUserIdTest() {
            when(sessionManager.isLogin()).thenReturn(false);
            when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("");
            when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "AA-BB-CC-DD-EE-FF", "HDD001" });
            when(mapper.toDeviceAuthInfo(any())).thenReturn(Collections.emptyList());

            VerificationAdditionalGetDeviceAuthResponse response = service.getDeviceAuth(ctx);

            assertEquals(0, response.getDeviceCount());
        }
    }

    @Nested
    @DisplayName("applySimple")
    class ApplySimple {

        private VerificationAdditionalApplySimpleRequest buildValidRequest() {
            VerificationAdditionalApplySimpleRequest request = new VerificationAdditionalApplySimpleRequest();
            request.setCustName("홍길동");
            request.setPerBusNo1("900101");
            request.setPerBusNo2("1234567");
            request.setTelecom("1");
            request.setTelNo("01012345678");
            request.setPrivacySharingAgreeYn("Y");
            request.setThirdPartyProvisionAgreeYn("Y");
            return request;
        }

        @Test
        @DisplayName("필수값 누락 시 예외")
        void missingRequiredFieldsTest() {
            VerificationAdditionalApplySimpleRequest request = new VerificationAdditionalApplySimpleRequest();

            assertThrows(PRCServiceException.class, () -> service.applySimple(ctx, request));
        }

        @Test
        @DisplayName("iOS 미등록 단말이면 9999 반환")
        void iosUnregisteredDeviceTest() {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            when(usimAuthMgtDao.selectTokenAuth(any())).thenReturn(Collections.emptyList());

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(false);
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("DEVICE_TOKEN");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("N", response.getAuthYn());
                assertEquals("9999", response.getResultCode());
                assertEquals("MO 미등록 단말입니다.", response.getResultMsg());
            }
        }

        @Test
        @DisplayName("비운영환경 간편인증 성공")
        void applySimpleSuccessNonPrdTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
            simpleAuthResponse.setAuthYn("Y");
            simpleAuthResponse.setResultCode("0000");
            simpleAuthResponse.setResultMsg("성공");
            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenReturn(simpleAuthResponse);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbIbk01H86600Res h866Res = new CbIbk01H86600Res();
            h866Res.setYOHUMGB("N");
            OltpResponse<CbIbk01H86600Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h866Res);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H866")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbIbk01H86600Req.class), eq(CbIbk01H86600Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("8.0");
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("Y", response.getAuthYn());
                assertEquals("0000", response.getResultCode());
                assertEquals("N", response.getYohumgb());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "A");
            }
        }

        @Test
        @DisplayName("로그인 상태이면 세션 주민번호 사용")
        void applySimpleLoginTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("CustName", String.class)).thenReturn("김철수");
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("8007071999994");

            SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
            simpleAuthResponse.setAuthYn("N");
            simpleAuthResponse.setResultCode("9999");
            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenReturn(simpleAuthResponse);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbIbk01H86600Res h866Res = new CbIbk01H86600Res();
            h866Res.setYOHUMGB("N");
            OltpResponse<CbIbk01H86600Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h866Res);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H866")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbIbk01H86600Req.class), eq(CbIbk01H86600Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("8.0");
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("8007071999994");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("Y", response.getAuthYn());
                assertEquals("0000", response.getResultCode());
            }
        }

        @Test
        @DisplayName("iOS 등록 단말 비운영환경 성공")
        void iosRegisteredDeviceSuccessTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            when(usimAuthMgtDao.selectTokenAuth(any())).thenReturn(List.of(new TokenAuthResult()));

            SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
            simpleAuthResponse.setAuthYn("Y");
            simpleAuthResponse.setResultCode("0000");
            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenReturn(simpleAuthResponse);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbIbk01H86600Res h866Res = new CbIbk01H86600Res();
            h866Res.setYOHUMGB("N");
            OltpResponse<CbIbk01H86600Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h866Res);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H866")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbIbk01H86600Req.class), eq(CbIbk01H86600Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(false);
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("DEVICE_TOKEN");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("Y", response.getAuthYn());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
            }
        }

        @Test
        @DisplayName("디지털인증서 메뉴 0013 응답이면 SMS전환")
        void digitalCertMenuChangeSmsTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
            simpleAuthResponse.setAuthYn("Y");
            simpleAuthResponse.setResultCode("0013");
            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenReturn(simpleAuthResponse);

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("8.0");
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("CRTDCT001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("N", response.getAuthYn());
                assertEquals("Y", response.getChangeSmsAuthYn());
            }
        }

        @Test
        @DisplayName("안드로이드10 이상 미등록 단말이면 9999 반환")
        void android10UnregisteredDeviceTest() {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            when(usimAuthMgtDao.selectTokenAuth(any())).thenReturn(Collections.emptyList());

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("11.0");
                shared.when(PRCSharedUtils::getDeviceUUID).thenReturn("UUID001");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("9999", response.getResultCode());
            }
        }

        @Test
        @DisplayName("휴면계좌보유고객이면 인증세션 미설정")
        void dormantAccountTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
            simpleAuthResponse.setAuthYn("Y");
            simpleAuthResponse.setResultCode("0000");
            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenReturn(simpleAuthResponse);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbIbk01H86600Res h866Res = new CbIbk01H86600Res();
            h866Res.setYOHUMGB("Y");
            OltpResponse<CbIbk01H86600Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h866Res);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H866")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbIbk01H86600Req.class), eq(CbIbk01H86600Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("8.0");
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("12345ABCDE1234567890ABCDE");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("Y", response.getYohumgb());
                verify(sessionManager, org.mockito.Mockito.never()).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
            }
        }

        @Test
        @DisplayName("휴면계좌조회 예외 시 yohumgb N")
        void dormantAccountExceptionTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            SimpleAuthResponse simpleAuthResponse = new SimpleAuthResponse();
            simpleAuthResponse.setAuthYn("Y");
            simpleAuthResponse.setResultCode("0000");
            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenReturn(simpleAuthResponse);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H866"))
                    .thenThrow(new RuntimeException("host error"));

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("8.0");
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("N", response.getYohumgb());
            }
        }

        @Test
        @DisplayName("tcert 오류 비운영환경이면 로그만 처리")
        void tcertExceptionNonPrdTest() throws Exception {
            VerificationAdditionalApplySimpleRequest request = buildValidRequest();

            when(tcert.sendTauth(any(SimpleAuthRequest.class))).thenThrow(new RuntimeException("tcert error"));

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbIbk01H86600Res h866Res = new CbIbk01H86600Res();
            h866Res.setYOHUMGB("N");
            OltpResponse<CbIbk01H86600Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h866Res);
            when(hostClient.getOltpRequestOptions("CB_IBK01_H866")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbIbk01H86600Req.class), eq(CbIbk01H86600Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                shared.when(PRCSharedUtils::isAndroid).thenReturn(true);
                shared.when(PRCSharedUtils::getOsVersion).thenReturn("8.0");
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "MAC", "IMEI001" });

                VerificationAdditionalApplySimpleResponse response = service.applySimple(ctx, request);

                assertEquals("Y", response.getAuthYn());
                assertEquals("0000", response.getResultCode());
            }
        }
    }

    @Nested
    @DisplayName("validateSimple")
    class ValidateSimple {

        @Test
        @DisplayName("등록된 단말이면 Y 반환")
        void validateSimpleFoundTest() {
            VerificationAdditionalValidateSimpleRequest request = new VerificationAdditionalValidateSimpleRequest();
            request.setTelNo("01012345678");

            TokenAuthResult tokenAuth = new TokenAuthResult();
            when(usimAuthMgtDao.selectTokenAuth(any())).thenReturn(List.of(tokenAuth));

            try (MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                shared.when(PRCSharedUtils::getSimSerial).thenReturn("SIM001");

                VerificationAdditionalValidateSimpleResponse response = service.validateSimple(ctx, request);

                assertEquals("Y", response.getResultYn());
            }
        }
    }

    @Nested
    @DisplayName("applySMSIdentify")
    class ApplySMSIdentify {

        @Test
        @DisplayName("미성년자이면 minorYn Y")
        void minorTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP1");
            request.setPerBusNo1("201501");
            request.setPerBusNo2("4123456");

            VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

            assertEquals("Y", response.getMinorYn());
            assertEquals("N", response.getForeignerYn());
        }

        @Test
        @DisplayName("외국인이면 foreignerYn Y")
        void foreignerTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP1");
            request.setPerBusNo1("900101");
            request.setPerBusNo2("5234567");

            VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

            assertEquals("N", response.getMinorYn());
            assertEquals("Y", response.getForeignerYn());
        }

        @Test
        @DisplayName("STEP1 명의인증 성공")
        void step1SuccessTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP1");
            request.setPerBusNo1("900101");
            request.setPerBusNo2("1234567");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setTId("TID001");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

            assertEquals("9001011******", response.getMaskPerBusNo());
            verify(sessionManager).setGlobalValue("ADD_AUTH_SMS_TID", "TID001");
            verify(sessionManager).setGlobalValue("IDENTIFY_SSN", "9001011234567");
        }

        @Test
        @DisplayName("명의인증 실패 시 예외")
        void identifyFailTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP1");
            request.setPerBusNo1("900101");
            request.setPerBusNo2("1234567");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("9999");
            identifyRes.setRespMsg("실패");
            identifyRes.setMobileSsn("9001011234567");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            assertThrows(PRCServiceException.class, () -> service.applySMSIdentify(ctx, request));
        }

        @Test
        @DisplayName("STEP2 비운영환경 인증 성공")
        void step2NonPrdSuccessTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi("CI_VALUE_12345678901234567890");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setResult("Y");
            ciResponse.setStatusYN("Y");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertEquals("N", response.getCiErrYn());
                assertEquals("N", response.getNameErrYn());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_TYPE", "B");
                verify(sessionManager).setGlobalValue(eq("USER_CI_INFO"), anyString());
            }
        }

        @Test
        @DisplayName("STEP2 운영환경 OLTP CI 일치 시 인증 성공")
        void step2PrdSuccessTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            String ciValue = "CI_VALUE_12345678901234567890123456789012";

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("CUST_CI", String.class)).thenReturn(null);
            when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn(ciValue);

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi(ciValue);
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbTbs03H76500Res h765Res = new CbTbs03H76500Res();
            h765Res.setYOCINO(ciValue);
            OltpResponse<CbTbs03H76500Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h765Res);
            when(hostClient.getOltpRequestOptions("TI1TBS03_H765")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class)))
                    .thenReturn(oltpResponse);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setResult("Y");
            ciResponse.setStatusYN("Y");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                session.when(() -> SessionUtils.getSessionValue("UserID", String.class)).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("TSPassword", String.class)).thenReturn("PASS01");

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertEquals("N", response.getCiErrYn());
                assertEquals("N", response.getNameErrYn());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                verify(sessionManager).setGlobalValue("USER_CI_INFO", ciValue);
                verify(certificationSharedComponent).getYoLRSOTGB("9001011234567");
                verify(hostClient).sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class));
            }
        }

        @Test
        @DisplayName("STEP2 운영환경 KCB CI 불일치")
        void step2PrdKcbCiMismatchTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            String ciValue = "CI_VALUE_12345678901234567890123456789012";

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi(ciValue);
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbTbs03H76500Res h765Res = new CbTbs03H76500Res();
            h765Res.setYOCINO("DIFFERENT_CI_VALUE");
            OltpResponse<CbTbs03H76500Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h765Res);
            when(hostClient.getOltpRequestOptions("TI1TBS03_H765")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                session.when(() -> SessionUtils.getSessionValue("UserID", String.class)).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("TSPassword", String.class)).thenReturn("PASS01");

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertEquals("Y", response.getKcbCiErrYn());
                verify(sessionManager, org.mockito.Mockito.never()).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "Y");
                verify(sessionManager).removeGlobalValue("BF_DACOM_PERNO");
            }
        }

        @Test
        @DisplayName("STEP2 운영환경 세션 CUST_CI 불일치 시 예외")
        void step2PrdCustCiMismatchTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            String ciValue = "CI_VALUE_12345678901234567890123456789012";

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("CUST_CI", String.class)).thenReturn("SESSION_CI_OLD");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi(ciValue);
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbTbs03H76500Res h765Res = new CbTbs03H76500Res();
            h765Res.setYOCINO(ciValue);
            OltpResponse<CbTbs03H76500Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h765Res);
            when(hostClient.getOltpRequestOptions("TI1TBS03_H765")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class)))
                    .thenReturn(oltpResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                session.when(() -> SessionUtils.getSessionValue("UserID", String.class)).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("TSPassword", String.class)).thenReturn("PASS01");

                PRCServiceException ex = assertThrows(PRCServiceException.class,
                        () -> service.applySMSIdentify(ctx, request));

                assertEquals("CIINFO_ERR", ex.getErrorCode());
                verify(sessionManager).removeGlobalValue("CUST_CI");
                verify(sessionManager).removeGlobalValue("CNNCTN_TRAD_NO");
            }
        }

        @Test
        @DisplayName("STEP2 운영환경 고객명 2자 이하 및 CI 짧을 때 OLTP 요청")
        void step2PrdShortNameAndCiTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            String shortCi = "SHORT_CI";

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("CUST_CI", String.class)).thenReturn(null);
            when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn(shortCi);

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi(shortCi);
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbTbs03H76500Res h765Res = new CbTbs03H76500Res();
            h765Res.setYOCINO(shortCi);
            OltpResponse<CbTbs03H76500Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h765Res);
            when(hostClient.getOltpRequestOptions("TI1TBS03_H765")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class)))
                    .thenReturn(oltpResponse);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setResult("Y");
            ciResponse.setStatusYN("Y");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                session.when(() -> SessionUtils.getSessionValue("UserID", String.class)).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("TSPassword", String.class)).thenReturn("PASS01");

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertEquals("N", response.getCiErrYn());
                verify(hostClient).sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class));
            }
        }

        @Test
        @DisplayName("STEP2 운영환경 checkUserCiInfo insertCustomerCi 호출")
        void step2PrdCiInsertTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            String ciValue = "CI_VALUE_12345678901234567890123456789012";

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("CUST_CI", String.class)).thenReturn(null);
            when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn(ciValue);

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi(ciValue);
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            OltpRequestOptions options = org.mockito.Mockito.mock(OltpRequestOptions.class);
            CbTbs03H76500Res h765Res = new CbTbs03H76500Res();
            h765Res.setYOCINO(ciValue);
            OltpResponse<CbTbs03H76500Res> oltpResponse = new OltpResponse<>();
            oltpResponse.setResponse(h765Res);
            when(hostClient.getOltpRequestOptions("TI1TBS03_H765")).thenReturn(options);
            when(hostClient.sendOltp(eq(options), any(CbTbs03H76500Req.class), eq(CbTbs03H76500Res.class)))
                    .thenReturn(oltpResponse);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setResult("N");
            ciResponse.setStatusYN("N");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class);
                    MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);
                session.when(() -> SessionUtils.getSessionValue("UserID", String.class)).thenReturn("USER01");
                session.when(() -> SessionUtils.getSessionValue("TSPassword", String.class)).thenReturn("PASS01");

                service.applySMSIdentify(ctx, request);

                verify(verificationComponent).insertCustomerCi(any());
            }
        }

        @Test
        @DisplayName("비로그인 STEP3 - 세션 주민번호 사용")
        void nonLoginStep3Test() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP3");
            request.setCustName("홍길동");

            when(sessionManager.isLogin()).thenReturn(false);
            when(sessionManager.getGlobalValue("IDENTIFY_SSN", String.class)).thenReturn("9001011234567");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

            assertEquals("9001011******", response.getMaskPerBusNo());
        }

        @Test
        @DisplayName("STEP2 이름오류 반환")
        void step2NameErrTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi("CI_VALUE");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setIsNameChkErr("Y");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertEquals("Y", response.getNameErrYn());
            }
        }

        @Test
        @DisplayName("STEP2 CI오류 반환")
        void step2CiErrTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("CI_TEST");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi("CI_VALUE");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setIsCIERR("Y");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertEquals("Y", response.getCiErrYn());
            }
        }

        @Test
        @DisplayName("STEP2 CI 불일치 시 insertCustomerCi 호출")
        void step2CiInsertTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("CI_TEST");
            when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("9001011234567");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi("CI_VALUE");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);

            VerificationGetCustomerCiResponse ciResponse = new VerificationGetCustomerCiResponse();
            ciResponse.setResult("N");
            ciResponse.setStatusYN("N");
            when(verificationComponent.getCustomerCi(any())).thenReturn(ciResponse);

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                service.applySMSIdentify(ctx, request);

                verify(verificationComponent).insertCustomerCi(any());
            }
        }

        @Test
        @DisplayName("checkUserCiInfo MciSystemException 처리")
        void step2MciExceptionTest() {
            VerificationAdditionalApplySMSIdentifyRequest request = new VerificationAdditionalApplySMSIdentifyRequest();
            request.setPageStep("STEP2");
            request.setCustName("홍길동");
            request.setTelNo("01012345678");
            request.setTelecom("1");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("9001011234567");
            when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("CI_TEST");

            CbIdentifyDacomRes identifyRes = new CbIdentifyDacomRes();
            identifyRes.setRespCode("0000");
            identifyRes.setMobileSsn("9001011234567");
            identifyRes.setCi("CI_VALUE");
            when(helper.getCustNm(anyString(), anyString())).thenReturn("홍길동");
            when(helper.sendMciIdentifyDacom(any(), anyString(), anyString())).thenReturn(identifyRes);
            when(verificationComponent.getCustomerCi(any())).thenThrow(new MciSystemException(null, "ERR", "MCI 오류"));

            try (MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                VerificationAdditionalApplySMSIdentifyResponse response = service.applySMSIdentify(ctx, request);

                assertNull(response.getCiErrYn());
            }
        }
    }

    @Nested
    @DisplayName("registDeviceAuth")
    class RegistDeviceAuth {

        @Test
        @DisplayName("단말기지정서비스 등록 - 미지정단말 허용")
        void registDeviceAuthAllowOtherTest() {
            VerificationAdditionalRegistDeviceAuthRequest request = new VerificationAdditionalRegistDeviceAuthRequest();
            request.setDeviceAlias("내폰");
            request.setAuthTelNo("01012345678");
            request.setAllowOtherDeviceYn("Y");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "AA-BB-CC-DD-EE-FF", "HDD001" });
            when(deviceAuth.getDeviceAuthCount("USER01")).thenReturn(0);
            doNothing().when(secureData).verifyVerification();

            QLoader qLoader = mock(QLoader.class);
            when(ipinside.sendNPInsideInfo("USER01", true, false)).thenReturn(qLoader);
            when(qLoader.getPCDeginateInfo(null, "1016020500", "USER01", "HDD001")).thenReturn(1);

            PcFixInfo fixInfo = new PcFixInfo();
            fixInfo.setOtherPcYes("Y");
            fixInfo.setPcFixValue("2");
            when(ipinside.getPcFixInfoNOtherPCYes("1", "USER01")).thenReturn(fixInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                shared.when(PRCSharedUtils::getIpinsideHdd).thenReturn("HDD001");

                VerificationAdditionalRegistDeviceAuthResponse response = service.registDeviceAuth(ctx, request);

                assertNotNull(response);
                verify(deviceAuthUserDao).insertDeviceAuthSvc(any());
                verify(deviceAuthUserDao).updateAllowOtherDeviceAuthUser(any());
                verify(deviceAuthUserDao).insertAllowOtherDeviceAuthUser(any());
                verify(sessionManager).setLoginValue("OtherPCYes", "Y");
            }
        }

        @Test
        @DisplayName("단말기지정서비스 등록 - 기등록 단말 있으면 허용설정 생략")
        void registDeviceAuthExistingDeviceTest() {
            VerificationAdditionalRegistDeviceAuthRequest request = new VerificationAdditionalRegistDeviceAuthRequest();
            request.setDeviceAlias("내폰");
            request.setAuthTelNo("01012345678");
            request.setAllowOtherDeviceYn("N");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "AA-BB-CC-DD-EE-FF", "HDD001" });
            when(deviceAuth.getDeviceAuthCount("USER01")).thenReturn(1);
            doNothing().when(secureData).verifyVerification();

            QLoader qLoader = mock(QLoader.class);
            when(ipinside.sendNPInsideInfo("USER01", true, false)).thenReturn(qLoader);
            when(qLoader.getPCDeginateInfo(null, "1016020500", "USER01", "HDD001")).thenReturn(0);

            PcFixInfo fixInfo = new PcFixInfo();
            fixInfo.setOtherPcYes("N");
            fixInfo.setPcFixValue("0");
            when(ipinside.getPcFixInfoNOtherPCYes("0", "USER01")).thenReturn(fixInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                shared.when(PRCSharedUtils::getIpinsideHdd).thenReturn("HDD001");

                service.registDeviceAuth(ctx, request);

                verify(deviceAuthUserDao).insertDeviceAuthSvc(any());
                verify(deviceAuthUserDao, org.mockito.Mockito.never()).updateBlockOtherDeviceAuthUser(any());
            }
        }

        @Test
        @DisplayName("단말기지정서비스 등록 - 미지정단말 미허용")
        void registDeviceAuthBlockOtherTest() {
            VerificationAdditionalRegistDeviceAuthRequest request = new VerificationAdditionalRegistDeviceAuthRequest();
            request.setDeviceAlias("내폰");
            request.setAuthTelNo("01012345678");
            request.setAllowOtherDeviceYn("N");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(ipinside.simpleDataDecode2()).thenReturn(new String[] { "AA-BB-CC-DD-EE-FF", "HDD001" });
            when(deviceAuth.getDeviceAuthCount("USER01")).thenReturn(0);
            doNothing().when(secureData).verifyVerification();

            QLoader qLoader = mock(QLoader.class);
            when(ipinside.sendNPInsideInfo("USER01", true, false)).thenReturn(qLoader);
            when(qLoader.getPCDeginateInfo(null, "1016020500", "USER01", "HDD001")).thenReturn(2);

            PcFixInfo fixInfo = new PcFixInfo();
            fixInfo.setOtherPcYes("N");
            fixInfo.setPcFixValue("1");
            when(ipinside.getPcFixInfoNOtherPCYes("2", "USER01")).thenReturn(fixInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                shared.when(PRCSharedUtils::getIpinsideHdd).thenReturn("HDD001");

                service.registDeviceAuth(ctx, request);

                verify(deviceAuthUserDao).updateBlockOtherDeviceAuthUser(any());
                verify(deviceAuthUserDao).insertBlockOtherDeviceAuthUser(any());
            }
        }
    }
}
