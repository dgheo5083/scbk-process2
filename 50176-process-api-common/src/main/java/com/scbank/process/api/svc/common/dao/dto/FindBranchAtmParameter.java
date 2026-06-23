package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class FindBranchAtmParameter {
    private String keyword;
    private String subType;
    private int pageSize;
    private int paging;

}
