package com.scbank.process.api.svc.shared.components.obs.kftc.model;

import lombok.Data;

/**
 * 금결원 오픈뱅킹 통신로그 적재 데이터 VO
 */
@Data
public class KftcObsLogVo {

    public static final String TRAD_CLASS_SEND = "S";
    public static final String TRAD_CLASS_RECV = "R";

    private String tradDate;
    private String userId;
    private String userCifNo;
    private String tradCls;
    private String bankTradId;
    private String apiTradId;
    private Integer tradSeq;
    private String chnlGb;
    private String apiUrl;
    private String data;
    private String registTime;
    private String apiDetailCls;
    private String resCode;
    private String bankRspCode;

    /**
     * 생성자
     * 
     * @param userId    사용자ID
     * @param userCifNo 사용자CI번호
     * @param tradSeq   거래번호
     */
    public KftcObsLogVo(String userId, String userCifNo, Integer tradSeq) {
        this.userId = userId;
        this.userCifNo = userCifNo;
        this.tradSeq = tradSeq;
        this.bankTradId = "-";
        this.apiTradId = "-";
        this.chnlGb = "-";
        this.apiUrl = "-";
        this.data = "{}";
        this.apiDetailCls = "-";
        this.resCode = "-";
        this.bankRspCode = "-";
    }
}
