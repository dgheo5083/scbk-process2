package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListForeignBranchAtmParameter {

    private String keyWord;
    private String ntnl;
    private String city;
    private String local;
    private String pageSize;
    private String paging;

}
