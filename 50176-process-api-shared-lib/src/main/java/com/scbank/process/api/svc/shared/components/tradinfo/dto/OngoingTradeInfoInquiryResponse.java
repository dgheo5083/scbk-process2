package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "OngoingTradeInfoInquiryResponse", type = Type.RESPONSE)
public class OngoingTradeInfoInquiryResponse implements IMessageObject {

	@MessageField(id = "custNo", name = "고객번호", example = "")
	private String custNo;
	@MessageField(id = "tradNo", name = "거래번호", example = "")
    private String tradNo;
	@MessageField(id = "bizType", name = "업무구분", example = "")
    private String bizType;
	@MessageField(id = "prdctId", name = "상품ID", example = "")
    private String prdctId;
    @MessageField(id = "prdctCd", name = "상품코드", example = "")
    private String prdctCd;
    @MessageField(id = "prdctNm", name = "상품명", example = "")
    private String prdctNm;
    @MessageField(id = "prgrssStsCd", name = "진행상태코드", example = "")
    private String prgrssStsCd;
    @MessageField(id = "alliancCd", name = "제휴코드", example = "")
    private String alliancCd;
    @MessageField(id = "apprverClerkNo", name = "승인자행번", example = "")
    private String apprverClerkNo;
    @MessageField(id = "apprvlDt", name = "승인일시", example = "")
    private String apprvlDt;
    @MessageField(id = "authntInd", name = "인증유형", example = "")
    private String authntInd;
    @MessageField(id = "authntIndCd", name = "인증유형여부", example = "")
    private String authntIndCd;
    @MessageField(id = "authntReqCmpltnDt", name = "인증신청완료일자", example = "")
    private String authntReqCmpltnDt;
    @MessageField(id = "authntReqDt", name = "인증신청일자", example = "")
    private String authntReqDt;
    @MessageField(id = "authntReqExpireDt", name = "인증신청만료일자", example = "")
    private String authntReqExpireDt;
    @MessageField(id = "blkClerkNo", name = "블락행번", example = "")
    private String blkClerkNo;
    @MessageField(id = "brnchNo", name = "점번호", example = "")
    private String brnchNo;
    @MessageField(id = "callCntrStsCd", name = "콜센터상태코드", example = "")
    private String callCntrStsCd;
    @MessageField(id = "cddCnfrmDt", name = "CDD 등록일자", example = "")
    private String cddCnfrmDt;
    @MessageField(id = "cddReqCd", name = "CDD요청여부", example = "")
    private String cddReqCd;
    @MessageField(id = "clerkNo", name = "행번", example = "")
    private String clerkNo;
    @MessageField(id = "cmpltnDt", name = "완료일자", example = "")
    private String cmpltnDt;
    @MessageField(id = "cnclDt", name = "취소일자", example = "")
    private String cnclDt;
    @MessageField(id = "cnnctnWay", name = "접속방식, 제휴처", example = "")
    private String cnnctnWay;
    @MessageField(id = "delObjFlg", name = "삭제대상여부", example = "")
    private String delObjFlg;
    @MessageField(id = "idCardCd", name = "신분증 코드", example = "")
    private String idCardCd;
    @MessageField(id = "idCardCmpltnDt", name = "신분증 진위 완료 일자", example = "")
    private String idCardCmpltnDt;
    @MessageField(id = "idCardCnt", name = "신분증 진위 건수", example = "")
    private String idCardCnt;
    @MessageField(id = "idCardReqDt", name = "신분증 진위 요청 일자", example = "")
    private String idCardReqDt;
    @MessageField(id = "initRegistDt", name = "최초등록일", example = "")
	private String initRegistDt;
    @MessageField(id = "kfbAcctNo", name = "당행계좌", example = "")
    private String kfbAcctNo;
    @MessageField(id = "lstChngDt", name = "최종변경일", example = "")
    private String lstChngDt;
    @MessageField(id = "newUserFlg", name = "신규고객여부", example = "")
    private String newUserFlg;
    @MessageField(id = "reqAmt", name = "신청금액", example = "")
    private String reqAmt;
    @MessageField(id = "reaCd", name = "부동산코드", example = "")
    private String reaCd;
    @MessageField(id = "integratedConselingYn", name = "통합상담여부", example = "")
    private String integratedConselingYn;
       
}
