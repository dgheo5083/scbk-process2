package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 스크래핑데이터 Parameter
 */
@Data
public class NonFaceScrapingInfoParameter {
	private Integer custNoS;
    private Integer tradNoS;
    private String objInstS;
    private String objDocCdS;
    private Integer custNoW;
    private Integer tradNoW;
    private String objInstW;
    private String objDocCdW;
    private String scrppngDataU;
    private String errCdU;
    private String errMsgU;
    private String scrType;
    private String certType;
    private Integer custNo;
    private Integer tradNo;
    private String objInst;
    private String objDocCd;
    private String scrppngData;
    private String errCd;
    private String errMsg;
    private String certTypeErr;
}