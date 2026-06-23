package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

/**
 * 오픈뱅킹: 등록된타행출금계좌 목록 조회 Result
 */
@Data
public class OpenBankAccountMgtListResult {

    private String userId;
    private String userCifNo;
    private String fintechUseNum;
    private String payerNum;
    private String fcode;
    private String bankCd;
    private String acctNo;
    private String acctNm;
    private String acctComment;
    private String agrSts;
    private String agrDate;
    private String updateTime;
    private String registTime;

}
