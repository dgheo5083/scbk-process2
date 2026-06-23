package com.scbank.process.api.svc.common.service.verification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.VerificationFDSComponent;
import com.scbank.process.api.svc.common.components.VerificationTokensComponent;
import com.scbank.process.api.svc.common.dao.SaoOtpChargeDao;
import com.scbank.process.api.svc.common.dao.TmbCampContentDao;
import com.scbank.process.api.svc.common.dao.dto.MotpRegistrationInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectTmbObjUsrMgtInfoResult;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensActivateRequest;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensActivateResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensCheckAbroadResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensSelectMotpDeviceStatusRequest;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensSelectMotpDeviceStatusResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensSendMotpPushResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensTestRequest;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("VerificationTokensService")
class VerificationTokensServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private VerificationTokensComponent verificationTokensComponent;

    @Mock
    private VerificationFDSComponent verificationFDSComponent;

    @Mock
    private TmbCampContentDao tmbCampContentDao;

    @Mock
    private SaoOtpChargeDao saoOtpChargeDao;

    @Mock
    private SecureDataComponent secureData;

    @InjectMocks
    private VerificationTokensService service;

    private void mockDefaultSession(MockedStatic<SessionUtils> session, String connectType, String safeCardState,
            String safeCardKind) {
        session.when(() -> SessionUtils.getSessionValue("ConnectType")).thenReturn(connectType);
        session.when(() -> SessionUtils.getSessionValue("TransPWUseYN")).thenReturn("0");
        session.when(() -> SessionUtils.getSessionValue("SafeCardState")).thenReturn(safeCardState);
        session.when(() -> SessionUtils.getSessionValue("SafeCardKind")).thenReturn(safeCardKind);
        session.when(() -> SessionUtils.getSessionValue("SmartOTP")).thenReturn("");
        session.when(() -> SessionUtils.getSessionValue("SafeCardINDEX")).thenReturn("1");
        session.when(() -> SessionUtils.getSessionValue("SafeCardINDEX2")).thenReturn("2");
        session.when(() -> SessionUtils.getSessionValue("SafeCardSeq1")).thenReturn("01");
        session.when(() -> SessionUtils.getSessionValue("SafeCardSeq2")).thenReturn("02");
        session.when(() -> SessionUtils.getSessionValue("SafeCardSeq3")).thenReturn("03");
        session.when(() -> SessionUtils.getSessionValue("TeleOne")).thenReturn("010");
        session.when(() -> SessionUtils.getSessionValue("TeleTwo")).thenReturn("1234");
        session.when(() -> SessionUtils.getSessionValue("TeleThree")).thenReturn("5678");
    }

    private void mockDefaultFds() {
        when(verificationTokensComponent.isMyAcctTran(anyString())).thenReturn(false);
        when(verificationFDSComponent.isTranBlock()).thenReturn(false);
        when(verificationFDSComponent.isIDCapture()).thenReturn(false);
        when(verificationFDSComponent.isWdrawFreeze()).thenReturn(false);
        when(verificationTokensComponent.isAdditional(any(), anyString(), anyDouble())).thenReturn(false);
    }

    @Nested
    @DisplayName("test")
    class Test {

        @Test
        @DisplayName("보안매체 검증테스트")
        void testEndpointTest() {
            doNothing().when(secureData).verifyVerification();
            service.test(ctx, new VerificationTokensTestRequest());
            verify(secureData).verifyVerification();
        }
    }

    @Nested
    @DisplayName("activate")
    class Activate {

        @Test
        @DisplayName("내계좌송금이면 보안매체/추가인증 skip")
        void myAcctSkipTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");

            when(verificationTokensComponent.isMyAcctTran("TRAN_IMMEDIATE")).thenReturn(true);

            VerificationTokensActivateResponse response = service.activate(ctx, request);

            assertEquals("N", response.getTokensYn());
            assertEquals("N", response.getAdditionalYn());
            verify(sessionManager).setGlobalValue("myAcctSkipYn", "Y");
        }

        @Test
        @DisplayName("FDS 거래차단 시 예외")
        void fdsTranBlockTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");

            when(verificationTokensComponent.isMyAcctTran(anyString())).thenReturn(false);
            when(verificationFDSComponent.isTranBlock()).thenReturn(true);

            assertThrows(PRCServiceException.class, () -> service.activate(ctx, request));
        }

        @Test
        @DisplayName("일반 이체 - 보안카드 설정")
        void normalActivateSafeCardTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("100000");
            request.setSafeCardSeqYn("Y");

            when(verificationTokensComponent.isMyAcctTran(anyString())).thenReturn(false);
            when(verificationFDSComponent.isTranBlock()).thenReturn(false);
            when(verificationFDSComponent.isIDCapture()).thenReturn(false);
            when(verificationFDSComponent.isWdrawFreeze()).thenReturn(false);
            when(verificationTokensComponent.isAdditional(any(), anyString(), eq(100000.0))).thenReturn(false);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("ConnectType")).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("TransPWUseYN")).thenReturn("0");
                session.when(() -> SessionUtils.getSessionValue("SafeCardState")).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("SafeCardKind")).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("SafeCardINDEX")).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("SafeCardINDEX2")).thenReturn("2");
                session.when(() -> SessionUtils.getSessionValue("SafeCardSeq1")).thenReturn("01");
                session.when(() -> SessionUtils.getSessionValue("SafeCardSeq2")).thenReturn("02");
                session.when(() -> SessionUtils.getSessionValue("SafeCardSeq3")).thenReturn("03");
                session.when(() -> SessionUtils.getSessionValue("TeleOne")).thenReturn("010");
                session.when(() -> SessionUtils.getSessionValue("TeleTwo")).thenReturn("1234");
                session.when(() -> SessionUtils.getSessionValue("TeleThree")).thenReturn("5678");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getTokensYn());
                assertEquals("N", response.getAdditionalYn());
                assertEquals("11", response.getTokensType());
                assertEquals("Y", response.getTranPwdYn());
            }
        }

        @Test
        @DisplayName("보안카드 폐기 상태이면 예외")
        void discardedSafeCardTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");
            request.setTokensYn("Y");

            when(verificationTokensComponent.isMyAcctTran(anyString())).thenReturn(false);
            when(verificationFDSComponent.isTranBlock()).thenReturn(false);
            when(verificationFDSComponent.isIDCapture()).thenReturn(false);
            when(verificationFDSComponent.isWdrawFreeze()).thenReturn(false);
            when(verificationTokensComponent.isAdditional(any(), anyString(), eq(0.0))).thenReturn(false);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("ConnectType")).thenReturn("1");
                session.when(() -> SessionUtils.getSessionValue("SafeCardState")).thenReturn("0");

                assertThrows(PRCServiceException.class, () -> service.activate(ctx, request));
            }
        }

        @Test
        @DisplayName("추가인증 대상이면 additionalYn Y")
        void additionalAuthRequiredTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("100000");

            mockDefaultFds();
            when(verificationTokensComponent.isAdditional(any(), eq("TRAN_IMMEDIATE"), eq(100000.0))).thenReturn(true);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getAdditionalYn());
                assertEquals("A:D:F", response.getAdditionalType());
                verify(sessionManager).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "N");
            }
        }

        @Test
        @DisplayName("출금정지 상태이면 추가인증 대상")
        void wdrawFreezeTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");
            request.setAdditionalType("A");

            mockDefaultFds();
            when(verificationFDSComponent.isWdrawFreeze()).thenReturn(true);
            when(verificationFDSComponent.getRspActnMethCd()).thenReturn("03");

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getAdditionalYn());
                assertEquals("A", response.getAdditionalType());
                assertEquals("03", response.getRspActnMethCd());
            }
        }

        @Test
        @DisplayName("FDS 신분증촬영 IB이면 additionalType P 설정")
        void fdsIdCaptureIbTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");

            mockDefaultFds();
            when(verificationFDSComponent.isIDCapture()).thenReturn(true);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");
                shared.when(PRCSharedUtils::isIB).thenReturn(true);

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getAdditionalYn());
                verify(sessionManager).setGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG", "Y");
            }
        }

        @Test
        @DisplayName("FDS 신분증촬영 비IB이면 예외")
        void fdsIdCaptureNonIbTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");

            mockDefaultFds();
            when(verificationFDSComponent.isIDCapture()).thenReturn(true);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");
                shared.when(PRCSharedUtils::isIB).thenReturn(false);

                assertThrows(PRCServiceException.class, () -> service.activate(ctx, request));
            }
        }

        @Test
        @DisplayName("tokensYn 직접 지정 시 해당값 사용")
        void tokensYnFromRequestTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");
            request.setTokensYn("N");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("N", response.getTokensYn());
            }
        }

        @Test
        @DisplayName("디지털인증 이체금액 이하면 fidoSignSkipYn Y")
        void fidoSignSkipTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("1000000");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                mockDefaultSession(session, "9", "1", "1");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                codeUtils.when(() -> CodeUtils.getCodeValue("FIDO_REJECT_MENU", "REJECT")).thenReturn("OTHER_MENU");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getFidoSignSkipYn());
                verify(sessionManager).setGlobalValue("fidoSignSkipYn", "Y");
            }
        }

        @Test
        @DisplayName("금융인증서 일일한도 초과 시 tokensYn Y")
        void fincertDailyLimitExceededTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("600000000");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                mockDefaultSession(session, "C", "1", "1");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                codeUtils.when(() -> CodeUtils.getCodeValue("FIDO_REJECT_MENU", "REJECT")).thenReturn("OTHER_MENU");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getTokensYn());
            }
        }

        @Test
        @DisplayName("금융인증서 1회한도 초과 시 tokensYn Y")
        void fincertOnetimeLimitExceededTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("1000000");

            Map<String, Object> transferInfo = new HashMap<>();
            transferInfo.put("RSgAmt", "200000000");
            when(sessionManager.getGlobalValue("addCertTranInfoList", List.class))
                    .thenReturn(List.of(transferInfo));

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                mockDefaultSession(session, "C", "1", "1");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("MENU001");
                codeUtils.when(() -> CodeUtils.getCodeValue("FIDO_REJECT_MENU", "REJECT")).thenReturn("OTHER_MENU");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getTokensYn());
            }
        }

        @Test
        @DisplayName("MOTP 이체금액 이하면 motpSignSkipYn Y")
        void motpSignSkipTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("1000000");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "G", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getMotpSignSkipYn());
                assertEquals("N", response.getTokensYn());
            }
        }

        @Test
        @DisplayName("MOTP 이체금액 초과 시 tokensYn Y")
        void motpSignRequiredTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("3000000");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "G", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("N", response.getMotpSignSkipYn());
                assertEquals("Y", response.getTokensYn());
            }
        }

        @Test
        @DisplayName("보안카드 지시번호 미입력 시 예외")
        void safeCardSeqMissingTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("0");
            request.setSafeCardSeqYn("Y");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");
                session.when(() -> SessionUtils.getSessionValue("SafeCardSeq1")).thenReturn("");

                assertThrows(PRCServiceException.class, () -> service.activate(ctx, request));
            }
        }

        @Test
        @DisplayName("보안카드 지시번호 없이 tokensType 10")
        void safeCardWithoutSeqTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setSafeCardSeqYn("N");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("10", response.getTokensType());
            }
        }

        @Test
        @DisplayName("구 OTP tokensType 20")
        void oldOtpTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "2");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("20", response.getTokensType());
            }
        }

        @Test
        @DisplayName("스마트OTP tokensType 31")
        void smartOtpTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "3");
                session.when(() -> SessionUtils.getSessionValue("SmartOTP")).thenReturn("12345");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("31", response.getTokensType());
            }
        }

        @Test
        @DisplayName("모바일OTP tokensType 32")
        void mobileOtpTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "3");
                session.when(() -> SessionUtils.getSessionValue("SmartOTP")).thenReturn("M");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("32", response.getTokensType());
            }
        }

        @Test
        @DisplayName("일반OTP tokensType 30")
        void generalOtpTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "3");
                session.when(() -> SessionUtils.getSessionValue("SmartOTP")).thenReturn("X");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("30", response.getTokensType());
            }
        }

        @Test
        @DisplayName("알수없는 보안매체종류 tokensType 30")
        void unknownSafeCardKindTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setTokensYn("Y");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "9");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("30", response.getTokensType());
            }
        }

        @Test
        @DisplayName("이체비밀번호 미사용 시 tranPwdYn 미설정")
        void transPwdNotRequiredTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("LOGIN");
            request.setTodayTranAmt("0");
            request.setTokensYn("N");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");
                session.when(() -> SessionUtils.getSessionValue("TransPWUseYN")).thenReturn("1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertNull(response.getTranPwdYn());
            }
        }

        @Test
        @DisplayName("추가인증 대상이나 tranType 없으면 세션/타입 미설정")
        void additionalAuthEmptyTranTypeTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("");
            request.setTodayTranAmt("100000");

            mockDefaultFds();
            when(verificationTokensComponent.isAdditional(any(), eq(""), eq(100000.0))).thenReturn(true);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                mockDefaultSession(session, "1", "1", "1");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getAdditionalYn());
                assertNull(response.getAdditionalType());
                verify(sessionManager, org.mockito.Mockito.never()).setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "N");
            }
        }

        @Test
        @DisplayName("FIDO 제외대상 메뉴이면 tokensYn Y")
        void fidoRejectMenuTest() {
            VerificationTokensActivateRequest request = new VerificationTokensActivateRequest();
            request.setTranType("TRAN_IMMEDIATE");
            request.setTodayTranAmt("1000000");

            mockDefaultFds();

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<PRCSharedUtils> shared = mockStatic(PRCSharedUtils.class);
                    MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                mockDefaultSession(session, "9", "1", "1");
                shared.when(PRCSharedUtils::getMenuId).thenReturn("REJECT_MENU");
                codeUtils.when(() -> CodeUtils.getCodeValue("FIDO_REJECT_MENU", "REJECT"))
                        .thenReturn("REJECT_MENU,OTHER");

                VerificationTokensActivateResponse response = service.activate(ctx, request);

                assertEquals("Y", response.getTokensYn());
            }
        }
    }

    @Nested
    @DisplayName("selectMotpDeviceStatus")
    class SelectMotpDeviceStatus {

        @Test
        @DisplayName("등록정보 없으면 기기변경 상태")
        void noRegistrationTest() {
            VerificationTokensSelectMotpDeviceStatusRequest request = new VerificationTokensSelectMotpDeviceStatusRequest();
            request.setDeviceId("DEVICE01");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(saoOtpChargeDao.selectMotpRegistrationInfo(any())).thenReturn(null);

            VerificationTokensSelectMotpDeviceStatusResponse response = service.selectMotpDeviceStatus(ctx, request);

            assertEquals("02", response.getMotpDeviceStatus());
        }

        @Test
        @DisplayName("등록정보 있으면 상태 미설정")
        void hasRegistrationTest() {
            VerificationTokensSelectMotpDeviceStatusRequest request = new VerificationTokensSelectMotpDeviceStatusRequest();
            request.setDeviceId("DEVICE01");

            when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("USER01");
            when(sessionManager.isLogin()).thenReturn(false);
            when(saoOtpChargeDao.selectMotpRegistrationInfo(any())).thenReturn(new MotpRegistrationInfoResult());

            VerificationTokensSelectMotpDeviceStatusResponse response = service.selectMotpDeviceStatus(ctx, request);

            assertEquals(null, response.getMotpDeviceStatus());
        }
    }

    @Nested
    @DisplayName("getAbroadYn")
    class GetAbroadYn {

        @Test
        @DisplayName("해외접속 여부 반환")
        void getAbroadYnTest() {
            when(verificationTokensComponent.getAbroadYn()).thenReturn("Y");

            VerificationTokensCheckAbroadResponse response = service.getAbroadYn(ctx);

            assertEquals("Y", response.getFlagIsAbroadYN());
        }
    }

    @Nested
    @DisplayName("sendMotpPush")
    class SendMotpPush {

        @Test
        @DisplayName("PUSH 미등록 고객이면 resultCd 99")
        void pushNotRegisteredTest() {
            when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("USER01");
            when(sessionManager.isLogin()).thenReturn(false);
            when(tmbCampContentDao.selectTmbObjUsrMgtInfo("USER01")).thenReturn(null);

            VerificationTokensSendMotpPushResponse response = service.sendMotpPush(ctx);

            assertEquals("99", response.getResultCd());
        }

        @Test
        @DisplayName("PUSH 발송 성공")
        void pushSendSuccessTest() {
            SelectTmbObjUsrMgtInfoResult userInfo = new SelectTmbObjUsrMgtInfoResult();
            userInfo.setPushSrvcApprvlFlg("Y");
            userInfo.setCnfrmNo("CONFIRM01");
            userInfo.setOperType("1");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(tmbCampContentDao.selectTmbObjUsrMgtInfo("USER01")).thenReturn(userInfo);
            when(tmbCampContentDao.selectTmbCampContentSeq()).thenReturn("SEQ001");

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG001")).thenReturn("제목");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG002")).thenReturn("내용");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG004")).thenReturn("REG01");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG005")).thenReturn("DEPT01");

                VerificationTokensSendMotpPushResponse response = service.sendMotpPush(ctx);

                verify(tmbCampContentDao).insertTmbCampContents01(any());
                verify(tmbCampContentDao).insertTmbPushmsgCamp01(any());
            }
        }

        @Test
        @DisplayName("PUSH 미승인 고객이면 resultCd 99")
        void pushNotApprovedTest() {
            SelectTmbObjUsrMgtInfoResult userInfo = new SelectTmbObjUsrMgtInfoResult();
            userInfo.setPushSrvcApprvlFlg("N");
            userInfo.setCnfrmNo("CONFIRM01");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(tmbCampContentDao.selectTmbObjUsrMgtInfo("USER01")).thenReturn(userInfo);

            VerificationTokensSendMotpPushResponse response = service.sendMotpPush(ctx);

            assertEquals("99", response.getResultCd());
        }

        @Test
        @DisplayName("cnfrmNo 없으면 resultCd 99")
        void pushNoCnfrmNoTest() {
            SelectTmbObjUsrMgtInfoResult userInfo = new SelectTmbObjUsrMgtInfoResult();
            userInfo.setPushSrvcApprvlFlg("Y");
            userInfo.setCnfrmNo("");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(tmbCampContentDao.selectTmbObjUsrMgtInfo("USER01")).thenReturn(userInfo);

            VerificationTokensSendMotpPushResponse response = service.sendMotpPush(ctx);

            assertEquals("99", response.getResultCd());
        }

        @Test
        @DisplayName("PUSH 발송 operType 2")
        void pushSendOperType2Test() {
            SelectTmbObjUsrMgtInfoResult userInfo = new SelectTmbObjUsrMgtInfoResult();
            userInfo.setPushSrvcApprvlFlg("Y");
            userInfo.setCnfrmNo("CONFIRM01");
            userInfo.setOperType("2");

            when(sessionManager.isLogin()).thenReturn(true);
            when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("USER01");
            when(tmbCampContentDao.selectTmbObjUsrMgtInfo("USER01")).thenReturn(userInfo);
            when(tmbCampContentDao.selectTmbCampContentSeq()).thenReturn("SEQ002");

            try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG001")).thenReturn("제목");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG002")).thenReturn("내용");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG003")).thenReturn("내용2");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG004")).thenReturn("REG01");
                codeUtils.when(() -> CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG005")).thenReturn("DEPT01");

                service.sendMotpPush(ctx);

                verify(tmbCampContentDao).insertTmbCampContents01(any());
            }
        }
    }
}
