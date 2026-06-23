package com.scbank.process.api.fw.base.store.secure.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 전자서명 결과코드
     */
    private String signResultCode;

    /**
     * 전자서명 원문
     */
    private String signOrgData;

    /**
     * 전자서명 타입
     */
    private String signedType;

    /**
     * 전자서명 PKCS7 문자열
     */
    private String signedData;
    
    /**
     * 전자서명 공동인증서 vidRandom
     */
    private String vidRandom;

}
