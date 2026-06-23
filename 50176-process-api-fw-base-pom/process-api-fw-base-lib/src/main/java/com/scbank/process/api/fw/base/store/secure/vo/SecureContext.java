package com.scbank.process.api.fw.base.store.secure.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 보안영역 SecureContext 클래스
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecureContext implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 전자서명 정보
     */
    private SignInfo sign;

    /**
     * 보안 토큰 관련 정보(이체비밀번호/보안카드/OTP/MOTP)
     */
    private SecureTokensInfo tokens;
    
    /**
     * 키패드 정보(암호화 타입, 암호화 value)
     */
    private SecureKeypadInfo keypad;
}
