package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 마이데이터 기관 조회 결과 클래스
 */
@Data
@Builder
public class Ma3MydataOrgMgtResult {

    private String orgCd;
    private String orgMkCd;
    private String orgName;
    private String orgImg;
    private String orgImgName;
    private String orgOrder;
    private String openBnkFlag;
    private String openBnkCd;
}
