package com.scbank.process.api.svc.shared.components.ars.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ARS 인증 요청 VO
 */
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SyncAtClientCycleRequest {

    private String authTelNo;

    private String account;

    private String targetService;

    private String tranId;

    private String workCode;

    private String svcManChange;

    private String ssn;

    private String clientName;

    private String inBankName;

    private String inClientName;

    private String inAmount;

    private String totalCnt;

    private String totalAmount;

    private String arrayCnt;

    /**
     * 입금정보 목록
     */
    private List<DepositInfo> depositList;

    /**
     * ARS 입금 정보
     */
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class DepositInfo {

        private String bankName;
        private String clientName;
        private String amount;
    }
}
