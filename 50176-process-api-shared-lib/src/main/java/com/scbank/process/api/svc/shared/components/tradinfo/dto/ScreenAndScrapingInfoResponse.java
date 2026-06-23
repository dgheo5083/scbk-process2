package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@IntegrationMessage(id = "ScreenAndScrapingInfoResponse", type = Type.RESPONSE)
public class ScreenAndScrapingInfoResponse implements IMessageObject {

	@MessageField(id = "tradeInfo", name = "")
	private TradeInfoResponse tradeInfo;

    @MessageField(id = "scrnDataInfo", name = "")
    private String scrnDataInfo;

    @MessageField(id = "scrppngData", name = "")
    private String scrppngData;
    
    @Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TradeInfoResponse {
    	@MessageField(id = "initRegistDt", name = "")
        private String initRegistDt;

        @MessageField(id = "custNo", name = "")
        private String custNo;

        @MessageField(id = "tradNo", name = "거래 번호", example = "9999")
        private String tradNo;

        @MessageField(id = "bizType", name = "")
        private String bizType;

        @MessageField(id = "prdctId", name = "")
        private String prdctId;

        @MessageField(id = "prdctCd", name = "")
        private String prdctCd;

        @MessageField(id = "prdctNm", name = "")
        private String prdctNm;

        @MessageField(id = "callCntrStsCd", name = "")
        private String callCntrStsCd;

        @MessageField(id = "prgrssStsCd", name = "")
        private String prgrssStsCd;

        @MessageField(id = "rjctnCd", name = "")
        private String rjctnCd;

        @MessageField(id = "rjctnMsg", name = "")
        private String rjctnMsg;

        @MessageField(id = "cddCnfrmDt", name = "")
        private String cddCnfrmDt;

        @MessageField(id = "cddReqCd", name = "")
        private String cddReqCd;

        @MessageField(id = "idCardCd", name = "")
        private String idCardCd;

        @MessageField(id = "authntIndCd", name = "")
        private String authntIndCd;

        @MessageField(id = "newUserFlg", name = "")
        private String newUserFlg;

        @MessageField(id = "cmpltnDt", name = "")
        private String cmpltnDt;

        @MessageField(id = "cnclDt", name = "")
        private String cnclDt;

        @MessageField(id = "cnnctnWay", name = "")
        private String cnnctnWay;

        @MessageField(id = "authntReqDt", name = "")
        private String authntReqDt;

        @MessageField(id = "authntReqExpireDt", name = "")
        private String authntReqExpireDt;

        @MessageField(id = "blkClerkNo", name = "")
        private String blkClerkNo;

        @MessageField(id = "clerkNo", name = "")
        private String clerkNo;

        @MessageField(id = "brnchNo", name = "")
        private String brnchNo;

        @MessageField(id = "reqAmt", name = "")
        private String reqAmt;

        @MessageField(id = "authntInd", name = "")
        private String authntInd;

        @MessageField(id = "authntReqCmpltnDt", name = "")
        private String authntReqCmpltnDt;

        @MessageField(id = "lstChngDt", name = "")
        private String lstChngDt;

        @MessageField(id = "kfbAcctNo", name = "")
        private String kfbAcctNo;

        @MessageField(id = "alliancCd", name = "")
        private String alliancCd;

        @MessageField(id = "apprverClerkNo", name = "")
        private String apprverClerkNo;

        @MessageField(id = "apprvlDt", name = "")
        private String apprvlDt;

        @MessageField(id = "effctvInt", name = "")
        private String effctvInt;

        @MessageField(id = "loanPrd", name = "")
        private String loanPrd;

        @MessageField(id = "rdmptnMthd", name = "")
        private String rdmptnMthd;

        @MessageField(id = "execDt", name = "")
        private String execDt;

        @MessageField(id = "rdmptnDt", name = "")
        private String rdmptnDt;

        @MessageField(id = "loanReqNo", name = "")
        private String loanReqNo;

        @MessageField(id = "emailAddr", name = "")
        private String emailAddr;

        @MessageField(id = "errCd", name = "")
        private String errCd;

        @MessageField(id = "errMsg", name = "")
        private String errMsg;

        @MessageField(id = "kfbMgtNo", name = "")
        private String kfbMgtNo;

        @MessageField(id = "ktnetMgtNo", name = "")
        private String ktnetMgtNo;

        @MessageField(id = "intType", name = "")
        private String intType;

        @MessageField(id = "coplCmmnClltrlInd", name = "")
        private String coplCmmnClltrlInd;

        @MessageField(id = "loanAccptNo", name = "")
        private String loanAccptNo;

        @MessageField(id = "currencyCode", name = "")
        private String currencyCode;

        @MessageField(id = "ssn", name = "")
        private String ssn;

        @MessageField(id = "delObjFlg", name = "")
        private String delObjFlg;

        @MessageField(id = "idCardCmpltnDt", name = "")
        private String idCardCmpltnDt;

        @MessageField(id = "idCardCnt", name = "")
        private String idCardCnt;

        @MessageField(id = "idCardReqDt", name = "")
        private String idCardReqDt;

        @MessageField(id = "reaCd", name = "")
        private String reaCd;
    
    }
}

