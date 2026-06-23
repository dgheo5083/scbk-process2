package com.scbank.process.api.svc.shared.components.sms.dto;

import lombok.Data;

@Data
public class SmsRequest {

    /**
     * Client측 key일련번호
     */
    private String member;

    /**
     * 사용자 발신 코드
     */
    private String userCode;

    /**
     * 사용자명
     */
    private String userName;

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
     * 호출 메시지
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
     * 발신자명
     */
    private String callName;

    /**
     * 부서코드
     */
    private String deptCode;

    /**
     * 부서명
     */
    private String deptName;

    private String callUrl;

}
