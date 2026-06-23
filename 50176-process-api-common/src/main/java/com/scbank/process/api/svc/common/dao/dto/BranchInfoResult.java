package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class BranchInfoResult {

    private String branchId;

    private String branchType;

    private String branchCd;

    private String branchName;

    private String branchTel1;

    private String branchTel2;

    private String branchTel3;

    private String branchAddress;
}
