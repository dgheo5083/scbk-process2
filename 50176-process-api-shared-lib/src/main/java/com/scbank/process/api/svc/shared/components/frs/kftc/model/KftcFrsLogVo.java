package com.scbank.process.api.svc.shared.components.frs.kftc.model;

import lombok.Data;

/**
 * 통신로그 적재 데이타.
 * @author 950696
 *
 */
@Data
public class KftcFrsLogVo {
	
	public static final String TRAD_CLASS_SEND = "S";
	public static final String TRAD_CLASS_RECV = "R";
	
	private String tradDate;
	private String userCifNo;
	private String faceTransactionId;
	private String custNo;
	private String tradNo;
	private String tradCls;
	
	private String chnlGb;
	private String apiUrl;
	private String data;
	private String resCode;
	private String registtime;
	
	public KftcFrsLogVo() {}
	
	public KftcFrsLogVo (String userCifNo, String faceTransactionId, String custNo, String tradNo) {
		this.userCifNo = userCifNo;
		this.faceTransactionId = faceTransactionId;
		this.custNo 	= custNo;
		this.tradNo 	= tradNo;
		this.chnlGb = "-";
		this.apiUrl = "-";
		this.resCode="-";
		this.data = "{}";
	}
	
}
