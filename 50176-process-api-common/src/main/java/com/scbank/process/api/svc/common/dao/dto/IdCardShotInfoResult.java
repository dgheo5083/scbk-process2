package com.scbank.process.api.svc.common.dao.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class IdCardShotInfoResult {
	private String alliancCd;
	private String apprverClerkNo;
	private Timestamp apprvlDt;
	private String authntInd;
	private String authntIndCd;
	private Timestamp authntReqCmpltnDt;
	private Timestamp authntReqDt;
	private Timestamp authntReqExpireDt;
	private String bizType;
	private String blkClerkNo;
	private String branchNo;
	private String callCntrStsCd;
	private Timestamp cddCnfrmDt;
	private String cddReqCd;
	private String clerkNo;
	private Timestamp cmpltnDt;
	private Timestamp cnclDt;
	private String cnnctnWay;
	private String custNo;
	private String delObjFlg;
	private String idCardCd;
	private Timestamp idCardCmpltnDt;
	private String idCardCnt;
	private Timestamp idCardReqDt;
	private Timestamp initRegIstDt;
	private String kfbAcctNo;
	private Timestamp lstChngDt;
	private String newUserFlg;
	private String prdctCd;
	private String prdctId;
	private String prdctNm;
	private String prgrssStsCd;
	private String reqAmt;
	private String tradNo;
	private String seqNo;
}
