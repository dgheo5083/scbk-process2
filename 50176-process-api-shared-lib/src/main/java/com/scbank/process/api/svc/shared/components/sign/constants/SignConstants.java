package com.scbank.process.api.svc.shared.components.sign.constants;

import java.util.Set;

public class SignConstants {

    public final static String SIGN_VERIFY_SERVICE_REQUEST = "SIGN_VERIFY_SERVICE_REQUEST";
    public final static String VERIFY_TYPE = "SIGN_VERIFY_TYPE";
    public final static String ACTION_TYPE = "SIGN_ACTION_TYPE";
    public final static String SIGN_VERIFY_COMPLETE_YN = "SIGN_VERIFY_COMPLETE_YN";
    public final static String SIGN_SAVE_COMPLETE_YN = "SIGN_SAVE_COMPLETE_YN";
    public final static String SIGN_COMPARE_VERIFY_COUNT_KEY = "SIGN_COMPARE_VERIFY_COUNT_KEY";
    public final static String SIGN_SKIP_RESULT_CODE = "8";

    // 서명키 중 제외 키
    public final static Set<String> excludeSignDataKeySet = Set.of("sign", "real_tran", "pass", "e2k",
            "url", "h201", "pre_tran",
            "sgate_tran", "formsubmsg", "hashtablesize");

    public final static String SIGN_IB_GUBUN_CODE = "IB";
    public final static String SIGN_MB_GUBUN_CODE = "MA";
    public final static String SIGN_IB_SYS_NAME = "OIB";
    public final static String SIGN_MB_SYS_NAME = "MA30";

    // Db암호화
    public final static String KEY = "461CB533CC0195DA39928063A138CBCA";
    public final static String IV = "D74CD6D90A7F7BCB3536AB80DE20C613";
    // Db암호화

    // 전자서명 저장 오류 백업/로그 파일
    public final static String SIGN_SAVE_FILE_NAME = "INISAFESignFile_";
    public final static String SIGN_SAVE_FILE_LOG_NAME = "backup";
}
