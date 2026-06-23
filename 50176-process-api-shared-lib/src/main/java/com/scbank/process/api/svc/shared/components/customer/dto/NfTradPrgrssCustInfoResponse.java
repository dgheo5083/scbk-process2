package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@IntegrationMessage(id = "NfTradPrgrssCustInfoResponse", type = Type.RESPONSE, description = "비대면진행고객정보 조회 응답")
public class NfTradPrgrssCustInfoResponse {
	private nfTradPrgrssCustInfo nfTradInfoMgtS02;
	
	@Getter
    @Setter
    public static class nfTradPrgrssCustInfo implements IMessageObject {

        @MessageField(id = "tradNo", name = "거래번호")
        private String tradNo;

        @MessageField(id = "custNo", name = "고객번호")
        private String custNo;

        @MessageField(id = "bizType", name = "업무구분")
        private String bizType;
        
        @MessageField(id = "prdctCd", name = "상품코드")
        private String prdctCd;
        
        @MessageField(id = "prdctId", name = "상품ID")
        private String prdctId;
        
        @MessageField(id = "prdctNm", name = "상품명")
        private String prdctNm;
        
        @MessageField(id = "prgrssStsCd", name = "진행상태코드")
        private String prgrssStsCd;
        
        @MessageField(id = "alliancCd", name = "제휴코드")
        private String alliancCd;
        
        @MessageField(id = "apprverClerkNo", name = "승인자행번")
        private String apprverClerkNo;
        
        @MessageField(id = "apprvlDt", name = "승인일시")
        private String apprvlDt;
        
        @MessageField(id = "authntInd", name = "업무구분")
        private String authntInd;
        
        @MessageField(id = "authntIndCd", name = "업무구분코드")
        private String authntIndCd;
        
        @MessageField(id = "authntReqCmpltnDt", name = "인증신청완료일자")
        private String authntReqCmpltnDt;
        
        @MessageField(id = "authntReqDt", name = "인증요청일자")
        private String authntReqDt;
        
        @MessageField(id = "authntReqExpireDt", name = "인증요청만료일자")
        private String authntReqExpireDt;
        
        @MessageField(id = "blkClerkNo", name = "블락행번")
        private String blkClerkNo;
        
        @MessageField(id = "brnchNo", name = "점번호")
        private String brnchNo;
        
        @MessageField(id = "callCntrStsCd", name = "콜센터상태코드")
        private String callCntrStsCd;
        
        @MessageField(id = "cddCnfrmDt", name = "CDD등록일자")
        private String cddCnfrmDt;
        
        @MessageField(id = "cddReqCd", name = "CDD요청코드")
        private String cddReqCd;
        
        @MessageField(id = "clerkNo", name = "행번")
        private String clerkNo;
        
        @MessageField(id = "cmpltnDt", name = "완료일자")
        private String cmpltnDt;
        
        @MessageField(id = "cnclDt", name = "취소일자")
        private String cnclDt;
        
        @MessageField(id = "cnnctnWay", name = "접속방식")
        private String cnnctnWay;
        
        @MessageField(id = "delObjFlg", name = "삭제대상여부")
        private String delObjFlg;
        
        @MessageField(id = "idCardCd", name = "신분증코드")
        private String idCardCd;
        
        @MessageField(id = "idCardCmpltnDt", name = "신분증진위완료일자")
        private String idCardCmpltnDt;
        
        @MessageField(id = "idCardCnt", name = "신분증진위건수")
        private String idCardCnt;
        
        @MessageField(id = "idCardReqDt", name = "신분증진위요청일자")
        private String idCardReqDt;
        
        @MessageField(id = "initRegistDt", name = "최초등록일자")
        private String initRegistDt;
        
        @MessageField(id = "kfbAcctNo", name = "당행계좌")
        private String kfbAcctNo;
        
        @MessageField(id = "lstChngDt", name = "최종수정일자")
        private String lstChngDt;
        
        @MessageField(id = "newUserFlg", name = "신규고객여부")
        private String newUserFlg;
        
        @MessageField(id = "reqAmt", name = "신청금액")
        private String reqAmt;
        
        @MessageField(id = "reaCd", name = "부동산코드")
        private String reaCd;
        
        @MessageField(id = "integratedConselingYn", name = "통화상담여부")
        private String integratedConselingYn;
        
    }
}
