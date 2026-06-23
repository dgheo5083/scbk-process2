package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class GetForeignBranchAtmCountParameter {

    private String keyWord;
    private String ntnl;
    private String city;
    private String local;
}
