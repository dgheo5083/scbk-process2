package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class EdocInfoResult {

    private String edocCd;

    private String edocNm;

    private String edocGbn;

    private String edocPicGbn;

    private String regUserid;

    private String clipFilePath;

    private String ozFilePath;

    private String lpcSndYn;

    private String edmsSndYn;

    private String orgSndYn;

    private String emailSndYn;

    private String edocSignYn;

    private String edocTsaYn;

    private String updDt;

    private String edocContractOfferYn;

}
