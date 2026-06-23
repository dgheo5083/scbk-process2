package com.scbank.process.api.svc.shared.components.lms.dto;

import lombok.Data;

@Data
public class LmsRequest {

    /**
     * 호출 번호 #1
     */
    private String callPhone1;

    /**
     * 호출 번호 #2
     */
    private String callPhone2;

    /**
     * 호출 번호 #3
     */
    private String callPhone3;

    /**
     * 발신자명
     */
    private String callName;

    /**
     * 제목
     */
    private String subject;

    /**
     * 메시지
     */
    private String callMessage;

    /**
     * 메시지 전송 예약일자
     */
    private String rateDate;

    /**
     * 메시지 전송 예약시간
     */
    private String rateTime;

    /**
     * 메시지 코드
     */
    private String messageCode;

    /**
     * 호출 번호 #1
     */
    private String reqPhone1;

    /**
     * 호출 번호 #2
     */
    private String reqPhone2;

    /**
     * 호출 번호 #3
     */
    private String reqPhone3;

    /**
     * 부서코드
     */
    private String deptCode;

    /**
     * 부서명
     */
    private String deptName;

}
