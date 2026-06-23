package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

@Data
public class NonFaceCddResultParameter {
	/** 고객번호 */
	private String custNo;
	/** 거래번호 */
    private String tradNo;
    /** 진행상태코드 */
    private String prgrssStsCd;
    /** CDD요청여부 */
    private String cddReqCd;
    /** KCDD 처리상태코드 */
    private String kcddResMsg;
}
