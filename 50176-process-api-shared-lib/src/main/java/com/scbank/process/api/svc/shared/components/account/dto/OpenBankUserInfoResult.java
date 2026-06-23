package com.scbank.process.api.svc.shared.components.account.dto;

import lombok.Data;

/**
 * 오픈뱅킹사용자 조회 Result
 */
@Data
public class OpenBankUserInfoResult {
    private String userId;
    private String userCifNo;
    private String ciInfo;
    private String userNm;
    private String userSeqNo;
    private String ibCloseYn;
    private String updateTime;
    private String registTIme;

}
