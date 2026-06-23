package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListForeignBranchAtmResult {

    private String serno;
    private String acctTbl;
    private String chnnlMk;
    private String chnnlNm;
    private String chnnlAddr;
    private String cntctPlce;
    private String opTm;
    private String rn;

}
