package com.scbank.process.api.svc.shared.components.verification.utils;

import com.dreammirae.kfb.mobile.SecureAnyOtp;
import com.dreammirae.kfb.mobile.SecureAnyOtpException;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SecureTokensInfo;
import com.scbank.process.api.svc.shared.components.verification.constants.VerificationConstants;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VerificationUtils {

    /**
     * 보안매체 입력정보 > 이체비밀번호
     * 
     * @return
     */
    public static String getTransPassword() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        return tokensInfo.getTransPassword().toUpperCase();
    }

    public static String getSafeCardSeq1() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        String safeCardSeqValue = tokensInfo.getSafeCardSeqValue();
        return safeCardSeqValue.substring(0, 1);
    }

    public static String getSafeCardSeq2() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        String safeCardSeqValue = tokensInfo.getSafeCardSeqValue();
        return safeCardSeqValue.substring(1, 2);
    }

    public static String getSafeCardSeq3() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        String safeCardSeqValue = tokensInfo.getSafeCardSeqValue();
        return safeCardSeqValue.substring(2, 3);
    }

    /**
     * 보안매체 입력정보 > 보안카드 첫번째 두자리
     * 
     * @return
     */
    public static String getSafeCardNumber1() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        String safeCardNumber = "";
        String tokensType = getTokensType();
        if (tokensType.equals(VerificationConstants.SAFE_CARD)) {
            safeCardNumber = tokensInfo.getSafeCardNumber1();
        } else if (tokensType.equals(VerificationConstants.OLD_OTP)) {
            safeCardNumber = tokensInfo.getOtpNumber();
        } else if (tokensType.equals(VerificationConstants.NORMAL_OTP)) {
            safeCardNumber = tokensInfo.getOtpNumber();
        } else if (tokensType.equals(VerificationConstants.MOTP)) {
            safeCardNumber = getMotpPinNumber();
        }

        return safeCardNumber;
    }

    /**
     * 보안매체 입력정보 > 보안카드 두번째 두자리
     * 
     * @return
     */
    public static String getSafeCardNumber2() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        return tokensInfo.getSafeCardNumber2();
    }

    public static String getOtpNumber() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        return tokensInfo.getOtpNumber();
    }

    public static String getMotpDeviceId() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        return tokensInfo.getDeviceId();
    }

    public static String getMotpPinNumber() {
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        SecureTokensInfo tokensInfo = SecureContextStore.getContext().map(SecureContext::getTokens).orElse(null);

        if (tokensInfo == null) {
            return "";
        }

        String pinNumber = "";
        try {
            if (PRCSharedUtils.isSB()) {
                pinNumber = SecureAnyOtp.decryptOTP(tokensInfo.getPinNumber(), tokensInfo.getDeviceId());
            } else {
                pinNumber = tokensInfo.getPinNumber();
            }

        } catch (SecureAnyOtpException e) {
            throw new PRCServiceException("[Error] doSecureService decryptOTP :: " + e.getMessage());
        }

        return pinNumber;
    }

    /**
     * 보안매체 입력정보 > 연락처 첫번째 3자리
     * 
     * @return
     */
    public static String getTelNo1() {

        return SecureContextStore.getContext().map(SecureContext::getTokens).map(SecureTokensInfo::getTokensTelNo1)
                .orElse("");

        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        // SecureTokensInfo tokensInfo = secureContext.get().getTokens();

        // if (tokensInfo == null) {
        // return "";
        // }

        // return tokensInfo.getTokensTelNo1();
    }

    /**
     * 보안매체 입력정보 > 연락처 두번째 4자리
     * 
     * @return
     */
    public static String getTelNo2() {

        return SecureContextStore.getContext().map(SecureContext::getTokens).map(SecureTokensInfo::getTokensTelNo2)
                .orElse("");
        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        // SecureTokensInfo tokensInfo = secureContext.get().getTokens();

        // if (tokensInfo == null) {
        // return "";
        // }

        // return tokensInfo.getTokensTelNo2();
    }

    /**
     * 보안매체 입력정보 > 연락처 세번째 4자리
     * 
     * @return
     */
    public static String getTelNo3() {
        return SecureContextStore.getContext().map(SecureContext::getTokens).map(SecureTokensInfo::getTokensTelNo3)
                .orElse("");

        // Optional<SecureContext> secureContext = SecureContextStore.getContext();
        // SecureTokensInfo tokensInfo = secureContext.get().getTokens();

        // if (tokensInfo == null) {
        // return "";
        // }

        // return tokensInfo.getTokensTelNo3();
    }

    /**
     * 현재 사용자의 보안매체 반환
     */
    public static String getTokensType() {

        // 보안카드상태 ( 0: 미사용 1: 사용 2: 분실등록 3: 오류횟수초과 , 4:일련번호 오류횟수 초과상태 )
        String safeCardState = SessionUtils.getSessionValue("SafeCardState");
        /*
         * 보안카드종류 ( 1:보안카드, 2:(구)OTP, 3:일반OTP 및 스마트OTP )
         * - 1: 보안카드
         * - 2: (구)OTP
         * - 3: 일반OTP or MOTP (스마트OTP는 없어짐) smartOTP 코드로 구분한다.
         * > SmartOTP: M : MOTP, 그외 일반OTP
         * - 그외: 일반OTP
         */
        String safeCardKind = SessionUtils.getSessionValue("SafeCardKind");
        String smartOTP = SessionUtils.getSessionValue("SmartOTP");

        if (!"0".equals(safeCardState)) {
            if ("1".equals(safeCardKind)) {
                return VerificationConstants.SAFE_CARD;
            } else if ("2".equals(safeCardKind)) {
                return VerificationConstants.OLD_OTP;
            } else if ("3".equals(safeCardKind)) {
                if ("M".equals(smartOTP)) {
                    // MOTP
                    return VerificationConstants.MOTP;
                } else {
                    // 일반OTP
                    return VerificationConstants.NORMAL_OTP;
                }
            } else {
                return VerificationConstants.NORMAL_OTP;
            }
        } else {
            // 미사용으로 반환값 없음
            return "";
        }

    }
}
