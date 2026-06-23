package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class FindBranchAtmCountParameter {
    private String keyword;
    private String subType;
}
