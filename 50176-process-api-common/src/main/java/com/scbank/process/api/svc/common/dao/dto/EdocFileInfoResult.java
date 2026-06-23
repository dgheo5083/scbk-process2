package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class EdocFileInfoResult {

    private String custNo;

    private String tradNo;

    private String edocCd;

    private String docFileNm;

    private String lpcSndYn;

    private String edmsSndYn;

    private String orgSndYn;

    private String emailSndYn;

    private String fileExsitYn;

    private String storageLoctn;

    private String hashVal;

    private String elementOrgKey;

    private String updDt;

}
