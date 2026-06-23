package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 메인 입출금 계좌 상품 리스트 Result
 */
@Data
public class MainDepositAccountProductResult {
    private String prdctId;
    private String prdctNm;
    private String prdctFturKr;
    private String prdctCd;
    private String ctgrySysNm;
    private String ctgrySysCd;
    private String hghstInt;
    private String nfEntrncUrl;
    private String nfEntrncBtnFlg;
    private String nfPreviewBtnFlg;
    private String cllctStartDt;
    private String cllctEndDt;
    private String lndOdr;
    private String prdctSmplExpln;
}

