package com.scbank.process.api.svc.shared.components.account.dto;

import lombok.Data;

/**
 * 오픈뱅킹사용자 조회 Parameter
 */
@Data
public class OpenBankUserInfoParameter {
    private String userCifNo;
    private String userId;
    private String ibCloseYn;
}
