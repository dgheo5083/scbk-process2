package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

/**
 * 오픈뱅킹: 등록된타행출금계좌 목록 조회 Parameter
 */
@Data
public class OpenBankAccountMgtListParameter {

    private String userId;
    private String userCifNo;
    private String ibCloseYn;

}
