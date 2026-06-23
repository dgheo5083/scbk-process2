package com.scbank.process.api.fw.base.store.secure.vo;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 보안토큰 정보
 * 
 * author taemin.kwon
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecureKeypadInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 암호화 타입
     */
    private String encType;

    /**
     * 암호화 데이터
     */
    private String encValue;
    
    /**
     * 암호화 map
     */
    private Map<String, String> encMap;
    
    
    /**
     * 확장 E2E 평문 map
     */
    private Map<String, String> extE2EPlainMap;
    

}
