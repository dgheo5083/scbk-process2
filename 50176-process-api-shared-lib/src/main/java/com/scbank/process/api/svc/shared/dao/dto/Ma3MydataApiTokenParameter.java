package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

/**
 * MA3_MYDATA_API_TOKEN 테이블 파라미터 클래스
 */
@Data
@Builder
public class Ma3MydataApiTokenParameter {

    private String tokenDate;
    private String connectLoc;
    private String accessToken;
    private String expiresIn;
    private String scope;
    private String tokenId;
    private String useYn;
    private String registTime;
}
