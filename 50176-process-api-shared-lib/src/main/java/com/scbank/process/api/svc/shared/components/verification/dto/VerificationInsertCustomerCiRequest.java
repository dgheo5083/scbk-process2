package com.scbank.process.api.svc.shared.components.verification.dto;

import lombok.Data;

@Data
public class VerificationInsertCustomerCiRequest {

    private String userCiInfo;

    private String jmno;

    /** 거래구분", defaultValue = "C" */
    private String aigrgb; 

    /** 등록구분(1:조회, 2:등록, 3:해지)", defaultValue = "2" */
    private String aigubun;

    /** 처리구분(1:CI번호, 2:CIF번호, 3:주민번호, 4:계좌번호)", defaultValue = "3" */
    private String aicrgb;

    /** CIF번호 */
    private String aicifno;

    /** 점번호 */
    private String aijumno;

    /** 과목코드 */
    private String aikmcod;

    /** 계좌번호 */
    private String aigjwno;

    /** 생성업무코드 */
    private String aiftrcod;
}
