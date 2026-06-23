package com.scbank.process.api.svc.shared.components.verification.dto;

import lombok.Data;

@Data
public class VerificationGetCustomerCiResponse {

    private String result;

    /** CIF값 */
    private String cif;

    /** 본인인증 URL */
    private String url;

    /** 주민번호 불일치 여부 */
    private String isCIERR;

    /**  0000 정상종료, 9999 오류발생 */
    private String msgCd;

    /** 사활구분(Y/N) */
    private String statusYN;

    /** 이름 비교 결과 */
    private String isNameChkErr;
}
