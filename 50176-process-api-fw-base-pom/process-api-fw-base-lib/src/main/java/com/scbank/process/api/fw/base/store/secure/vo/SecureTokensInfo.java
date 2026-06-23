package com.scbank.process.api.fw.base.store.secure.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 보안토큰 정보
 * 
 * author sungdon.choi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecureTokensInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 이체비밀번호
     */
    private String transPassword;

    /**
     * 이체비밀번호 사용여부
     */
    private String transPasswordYn;

    // --------------------------------------------
    // 보안카드 관련
    // --------------------------------------------

    /**
     * 보안카드 첫번째 일련번호
     */
    private String safeCardSeqValue;

    /**
     * 보안카드 첫번재 인덱스
     */
    private String safeCardIndex1;

    /**
     * 보안카드 두번재 인덱스
     */
    private String safeCardIndex2;

    /**
     * 보안카드 첫번째 인덱스 값
     */
    private String safeCardNumber1;

    /**
     * 보안카드 두번째 인덱스 값
     */
    private String safeCardNumber2;

    // --------------------------------------------
    // OTP
    // --------------------------------------------

    /**
     * (구)OTP에서 사용하는걸로 보이는데 정확한 사용처 확인필요
     */
    private String otpNumber;

    /**
     * 모바일OTP 디바이스 ID
     */
    private String deviceId;

    /**
     * 모바일OTP 핀번호
     */
    private String pinNumber;

    /**
     * 보안매체에서 사용하는 연락처
     */
    private String tokensTelNo1;
    private String tokensTelNo2;
    private String tokensTelNo3;

}
