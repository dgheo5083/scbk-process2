package com.scbank.process.api.svc.shared.components.verification.constants;

public class VerificationConstants {

    public final static String SAFE_CARD = "1"; // 보안카드
    public final static String OLD_OTP = "2"; // 구 OTP
    public final static String NORMAL_OTP = "3"; // 일반OTP
    public final static String MOTP = "4"; // 모바일 OTP
    public final static String TRANS_PASSWORD = "5"; // 이체비밀번호 (보안매체 없이 이체비밀번호만 노출되는경우)
}
