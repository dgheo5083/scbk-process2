package com.scbank.process.api.svc.shared.components.verification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AdditionalTranInfo {

    /**
     * 내계좌여부 (N : 내계좌)
     */
    @JsonProperty("JgCode")
    private String jgCode;

    /**
     * 일일이체금액
     */
    @JsonProperty("RSgAmt")
    private String rsgAmt;

    /**
     * 입금은행명
     */
    @JsonProperty("IgBankName")
    private String igBankName;

    /**
     * 입금계좌예금주명
     */
    @JsonProperty("HgRNam1")
    private String hgRnam1;

    /**
     * 처리상태
     */
    @JsonProperty("RResult")
    private String rResult;

    /**
     * 예약이체여부 (예약이체만 Y 추가)
     */
    @JsonProperty("YyTranYn")
    private String yyTranYn;

}
