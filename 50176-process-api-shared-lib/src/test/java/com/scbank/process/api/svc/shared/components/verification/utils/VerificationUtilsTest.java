package com.scbank.process.api.svc.shared.components.verification.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.dreammirae.kfb.mobile.SecureAnyOtp;
import com.dreammirae.kfb.mobile.SecureAnyOtpException;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SecureTokensInfo;
import com.scbank.process.api.svc.shared.components.verification.constants.VerificationConstants;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

class VerificationUtilsTest {

    private SecureContext mockContext(SecureTokensInfo tokensInfo) {

        SecureContext context = new SecureContext();
        context.setTokens(tokensInfo);

        return context;
    }

    private SecureTokensInfo mockTokens() {

        SecureTokensInfo info = new SecureTokensInfo();

        info.setTransPassword("abcd");
        info.setSafeCardSeqValue("123");
        info.setSafeCardNumber1("11");
        info.setSafeCardNumber2("22");
        info.setOtpNumber("999999");
        info.setDeviceId("DEVICE01");
        info.setPinNumber("PIN01");
        info.setTokensTelNo1("010");
        info.setTokensTelNo2("1234");
        info.setTokensTelNo3("5678");

        return info;
    }

    @Test
    @DisplayName("이체비밀번호 대문자 변환")
    void getTransPassword() {

        SecureTokensInfo tokens = mockTokens();

        try (MockedStatic<SecureContextStore> mocked = mockStatic(SecureContextStore.class)) {

            mocked.when(SecureContextStore::getContext)
                    .thenReturn(Optional.of(mockContext(tokens)));

            assertThat(VerificationUtils.getTransPassword())
                    .isEqualTo("ABCD");
        }
    }

    @Test
    @DisplayName("SecureContext 없으면 빈문자열")
    void getTransPassword_empty() {

        try (MockedStatic<SecureContextStore> mocked = mockStatic(SecureContextStore.class)) {

            mocked.when(SecureContextStore::getContext)
                    .thenReturn(Optional.empty());

            assertThat(VerificationUtils.getTransPassword())
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("보안카드 일련번호 분리")
    void getSafeCardSeq() {

        SecureTokensInfo tokens = mockTokens();

        try (MockedStatic<SecureContextStore> mocked = mockStatic(SecureContextStore.class)) {

            mocked.when(SecureContextStore::getContext)
                    .thenReturn(Optional.of(mockContext(tokens)));

            assertThat(VerificationUtils.getSafeCardSeq1()).isEqualTo("1");
            assertThat(VerificationUtils.getSafeCardSeq2()).isEqualTo("2");
            assertThat(VerificationUtils.getSafeCardSeq3()).isEqualTo("3");
        }
    }

    @Test
    @DisplayName("연락처 조회")
    void getTelNo() {

        SecureTokensInfo tokens = mockTokens();

        try (MockedStatic<SecureContextStore> mocked = mockStatic(SecureContextStore.class)) {

            mocked.when(SecureContextStore::getContext)
                    .thenReturn(Optional.of(mockContext(tokens)));

            assertThat(VerificationUtils.getTelNo1()).isEqualTo("010");
            assertThat(VerificationUtils.getTelNo2()).isEqualTo("1234");
            assertThat(VerificationUtils.getTelNo3()).isEqualTo("5678");
        }
    }

    @Test
    @DisplayName("보안카드 타입")
    void getTokensType_safeCard() {

        try (MockedStatic<SessionUtils> mocked = mockStatic(SessionUtils.class)) {

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardState"))
                    .thenReturn("1");

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardKind"))
                    .thenReturn("1");

            assertThat(VerificationUtils.getTokensType())
                    .isEqualTo(VerificationConstants.SAFE_CARD);
        }
    }

    @Test
    @DisplayName("구 OTP 타입")
    void getTokensType_oldOtp() {

        try (MockedStatic<SessionUtils> mocked = mockStatic(SessionUtils.class)) {

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardState"))
                    .thenReturn("1");

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardKind"))
                    .thenReturn("2");

            assertThat(VerificationUtils.getTokensType())
                    .isEqualTo(VerificationConstants.OLD_OTP);
        }
    }

    @Test
    @DisplayName("MOTP 타입")
    void getTokensType_motp() {

        try (MockedStatic<SessionUtils> mocked = mockStatic(SessionUtils.class)) {

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardState"))
                    .thenReturn("1");

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardKind"))
                    .thenReturn("3");

            mocked.when(() -> SessionUtils.getSessionValue("SmartOTP"))
                    .thenReturn("M");

            assertThat(VerificationUtils.getTokensType())
                    .isEqualTo(VerificationConstants.MOTP);
        }
    }

    @Test
    @DisplayName("일반 OTP 타입")
    void getTokensType_normalOtp() {

        try (MockedStatic<SessionUtils> mocked = mockStatic(SessionUtils.class)) {

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardState"))
                    .thenReturn("1");

            mocked.when(() -> SessionUtils.getSessionValue("SafeCardKind"))
                    .thenReturn("3");

            mocked.when(() -> SessionUtils.getSessionValue("SmartOTP"))
                    .thenReturn("N");

            assertThat(VerificationUtils.getTokensType())
                    .isEqualTo(VerificationConstants.NORMAL_OTP);
        }
    }

    @Test
    @DisplayName("MOTP 복호화 성공")
    void getMotpPinNumber() throws Exception {

        SecureTokensInfo tokens = mockTokens();

        try (MockedStatic<SecureContextStore> secureMock = mockStatic(SecureContextStore.class);
             MockedStatic<PRCSharedUtils> prcMock = mockStatic(PRCSharedUtils.class);
             MockedStatic<SecureAnyOtp> otpMock = mockStatic(SecureAnyOtp.class)) {

            secureMock.when(SecureContextStore::getContext)
                    .thenReturn(Optional.of(mockContext(tokens)));

            prcMock.when(PRCSharedUtils::isSB)
                    .thenReturn(true);

            otpMock.when(() ->
                    SecureAnyOtp.decryptOTP("PIN01", "DEVICE01"))
                    .thenReturn("123456");

            assertThat(VerificationUtils.getMotpPinNumber())
                    .isEqualTo("123456");
        }
    }

    @Test
    @DisplayName("MOTP 복호화 실패")
    void getMotpPinNumber_fail() throws Exception {

        SecureTokensInfo tokens = mockTokens();

        try (MockedStatic<SecureContextStore> secureMock = mockStatic(SecureContextStore.class);
             MockedStatic<PRCSharedUtils> prcMock = mockStatic(PRCSharedUtils.class);
             MockedStatic<SecureAnyOtp> otpMock = mockStatic(SecureAnyOtp.class)) {

            secureMock.when(SecureContextStore::getContext)
                    .thenReturn(Optional.of(mockContext(tokens)));

            prcMock.when(PRCSharedUtils::isSB)
                    .thenReturn(true);

            otpMock.when(() ->
                    SecureAnyOtp.decryptOTP("PIN01", "DEVICE01"))
                    .thenThrow(new SecureAnyOtpException("decrypt fail"));

            assertThatThrownBy(VerificationUtils::getMotpPinNumber)
                    .isInstanceOf(PRCServiceException.class);
        }
    }
}