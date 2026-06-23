package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 오픈뱅킹 API토큰 활성화 처리 파라미터 클래스
 */
@Data
@Builder
public class OpenBankApiTokenEnabledTokenParameter {

    /**
     * 
     */
    private String data;

    /**
     * 
     */
    private String tokenDate;

    /**
     * 
     */
    private String connectLoc;
}
