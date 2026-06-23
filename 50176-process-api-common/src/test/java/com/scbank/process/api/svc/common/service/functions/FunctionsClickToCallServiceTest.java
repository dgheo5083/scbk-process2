package com.scbank.process.api.svc.common.service.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.common.dao.NfCustMgtDao;
import com.scbank.process.api.svc.common.dao.dto.NfCustInfoResult;
import com.scbank.process.api.svc.common.service.functions.dto.clickToCall.FncCtcSendRequest;
import com.scbank.process.api.svc.common.service.functions.dto.clickToCall.FncCtcSendResponse;
import com.scbank.process.api.svc.common.service.functions.dto.clickToCall.FncCtcValidateResponse;
import com.scbank.process.api.svc.shared.components.clickToCall.ClickToCallComponent;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallResponse;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FunctionsClickToCallService")
class FunctionsClickToCallServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private ClickToCallComponent clickToCall;

    @Mock
    private NfCustMgtDao nfCustMgtDao;

    @InjectMocks
    private FunctionsClickToCallService service;

    @Nested
    @DisplayName("validate")
    class Validate {

        @Test
        @DisplayName("주민번호 없으면 이름 빈값")
        void blankPerBusNoTest() {
            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("");

                FncCtcValidateResponse response = service.validate(ctx);

                assertEquals("", response.getName());
            }
        }

        @Test
        @DisplayName("고객정보 있고 인증 성공 시 이름/전화번호 반환")
        void validateSuccessTest() {
            NfCustInfoResult custInfo = new NfCustInfoResult();
            custInfo.setSsn("9001011234567");
            custInfo.setCmpndCheckKey("9001011234567_20250101120000000");

            when(nfCustMgtDao.selectNfCustInfo("9001011234567")).thenReturn(custInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                session.when(() -> SessionUtils.isLoginOrAuth("AUTH")).thenReturn(true);
                session.when(() -> SessionUtils.getSessionValue("CustName")).thenReturn("홍길동");
                session.when(() -> SessionUtils.getSessionValue("HpNum")).thenReturn("01012345678");
                dateUtils.when(() -> DateUtils.getCurrentDate("yyyyMMddHHmmssSS"))
                        .thenReturn("20250101120000000");

                FncCtcValidateResponse response = service.validate(ctx);

                assertEquals("홍길동", response.getName());
                assertEquals("01012345678", response.getMobileNum());
            }
        }

        @Test
        @DisplayName("인증키 불일치 시 예외")
        void validateAuthFailTest() {
            NfCustInfoResult custInfo = new NfCustInfoResult();
            custInfo.setSsn("9001011234567");
            custInfo.setCmpndCheckKey("invalid_key");

            when(nfCustMgtDao.selectNfCustInfo("9001011234567")).thenReturn(custInfo);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                session.when(() -> SessionUtils.isLoginOrAuth("AUTH")).thenReturn(true);
                dateUtils.when(() -> DateUtils.getCurrentDate("yyyyMMddHHmmssSS"))
                        .thenReturn("20250101120000000");

                assertThrows(PRCServiceException.class, () -> service.validate(ctx));
            }
        }
    }

    @Nested
    @DisplayName("send")
    class Send {

        @Test
        @DisplayName("시간체크 Y이고 영업시간 외이면 timeOver Y")
        void sendTimeOverTest() {
            FncCtcSendRequest request = new FncCtcSendRequest();
            request.setTimeChkYn("Y");

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                dateUtils.when(() -> DateUtils.getCurrentDate("HHmmss")).thenReturn("200000");

                FncCtcSendResponse response = service.send(ctx, request);

                assertEquals("Y", response.getTimeOver());
            }
        }

        @Test
        @DisplayName("비운영환경이면 errCode 200")
        void sendNonPrdTest() {
            FncCtcSendRequest request = new FncCtcSendRequest();
            request.setTimeChkYn("N");
            request.setCommand("&gt;test");

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.LOCAL);

                FncCtcSendResponse response = service.send(ctx, request);

                assertEquals("200", response.getErrCode());
            }
        }

        @Test
        @DisplayName("운영환경이면 clickToCall 호출")
        void sendPrdTest() {
            FncCtcSendRequest request = new FncCtcSendRequest();
            request.setTimeChkYn("N");
            request.setCustName("홍길동");
            request.setServicePath("path");
            request.setCallGroup("group");
            request.setCustTelNo("01012345678");
            request.setUrl("url");
            request.setCommand("cmd");

            ClickToCallResponse clickResponse = new ClickToCallResponse();
            clickResponse.setErrorCode("0000");
            when(clickToCall.send(any())).thenReturn(clickResponse);

            try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class);
                    MockedStatic<RuntimeContext> runtime = mockStatic(RuntimeContext.class)) {
                session.when(() -> SessionUtils.getSessionValue("PerBusNo")).thenReturn("9001011234567");
                runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.PRD);

                FncCtcSendResponse response = service.send(ctx, request);

                assertEquals("0000", response.getErrCode());
                verify(clickToCall).send(any());
            }
        }
    }
}
