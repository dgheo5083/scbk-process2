package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class NfCustInfoResult {

    private String ssn;
    private String custNo;
    private String mblphnNo;
    private String userNm;
    private String cmpndCheckKey;
    private String srcNewUserFlg;
    private String newUserFlg;
    private String srcDelObjFlg;
    private String delObjFlg;
    private String initCnnctnDt;
    private String lstCnnctnDt;

}
