package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

/**
 * 금결원 오픈뱅킹 API 토큰 조회 결과 VO
 */
@Data
public class OpenBankApiTokenSelectTokenResult {

    /**
     * 
     */
    private String tokenDate;

    /**
     * 
     */
    private String connectLoc;

    /**
     * 
     */
    private String data;

    /**
     * 
     */
    private String tokenState;

    /**
     * 
     */
    private String useYn;

    /**
     * 
     */
    private String registTime;
}
