package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

/**
 * MYDATA 업권노출 기관코드 Result
 */
@Data
public class MyDataExposureOrgResult {

    private String orgCd;
    private String orgMkCd;
    private String orgName;
    private String orgImg;
    private String orgImgName;
    private String orgOrder;
    private String openBnkFlag;
    private String openBnkCd;
    private String mobileChkFlag;

}
