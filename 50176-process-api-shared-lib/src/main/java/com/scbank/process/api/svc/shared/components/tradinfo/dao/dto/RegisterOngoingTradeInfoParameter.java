package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행상태 등록/수정 Parameter
 */
@Data
public class RegisterOngoingTradeInfoParameter {
	private String bizType;
    private String cnnctnWay;
    private String reacdn;
    private String clerkNo;
    private String reaCd;
    private String integratedConselingYn;
    private String targetProcess;
    private String callCntrStsCd;
    private String custNo;
    private String prdctId;
    private String prdctCd;
    private String prdctNm;
    private String jobDetailCd;
    private String cddReqCd;
    private String newUserFlg;
    private String delObjFlg;
    private String brnchNo;
    private String errCd;
    private String errMsg;
    private String cnnctnTradNo;
    private String perPart;
    private String docEvdcCd;
    private String hlPrsnCd;
    private String adSbstCd;
    private String kwInsrCd;
    private String preLoanMoveYn;
    private String realLoanMoveYn;
    private String govmDataType;
    private String govmSignType;
    private String govmSignErr;
    private String loanPurpose;
    private String collateralType;
    private String oplCnsltYn;
    private String idCardCd;
    private String tradRegGb;
    private String idCardCnt;
    private String reqAmt;
    private String authntIndCd;
    private String authntInd;
    private String authntReqDt;
    private String authntReqExpireDt;
    private String authntReqCmpltnDt;
    private String kfbAcctNo;
    private String alliancCd;
    private String effctvInt;
    private String loanPrd;
    private String rdmptnMthd;
    private String execDt;
    private String rdmptnDt;
    private String loanReqNo;
    private String loanAccptNo;
    private String coplCmmnClltrlInd;
    private String rjctnCd;
    private String rjctnMsg;
    private String tradNo;
    private String prgrssStsCd;
    private String loanHopeDt;
    private String easyLoanYn;
    private String loanDocInvstgtFlg;
    private String securityAcctNo;
    private String hqPrimeRateApplyYn;
    private String hqPrimeRate;
    private String spouseCustNo;
    private String spouseTradNo;
    private String advSeqNo;
    private String advDt;
    private String cnnctnLoanLmtAmt;
    private String cnnctnSignId;
    private String cnnctnWayDynamic;
    private String cmpltnYn;
}
