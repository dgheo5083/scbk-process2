package com.scbank.process.api.svc.shared.components.verification.dto;

import lombok.Data;

@Data
public class VerificationVerifyInfo {

    private String userId;

    private String fidoSkip;

    private String logSkip;

    /**
     * ASIS: TransPasswordconfirm - 0: 이체비밀번호 검증 패스
     */
    private String transPasswordYn;

    /*
     * ASIS: SafeCardconfirm - 0: 보안매체 검증 패스
     */
    private String safeCardConfirm;

}
