package com.scbank.process.api.svc.shared.components.ars.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ARS 인증 응답 VO
 */
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SyncAtClientCycleResponse {

    /**
     * 
     */
    private String tranId;

    /**
     * 
     */
    private String resultCode;
}
