package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@IntegrationMessage(id = "CheckOngoingTradeInfoInquiryResponse", type = Type.RESPONSE)
public class CheckOngoingTradeInfoInquiryResponse implements IMessageObject {

	@MessageField(id = "successYn", name = "조회 성공여부", example = "Y")
    private String successYn;
	
	@MessageField(id = "prdctCnt", name = "상태별 진행상품수", example = "0")
	private Integer prdctCnt;
	
	@MessageField(id = "prdctIdE", name = "")
	private String prdctIdE;

	@MessageField(id = "prdctCdE", name = "")
	private String prdctCdE;

	@MessageField(id = "bizType", name = "")
	private String bizType;

	@MessageField(id = "prdctId", name = "")
	private String prdctId;
	
	@MessageField(id = "original", name = "")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<OngoingTradeInfoResponse> original = new ArrayList<>();

	@MessageField(id = "pageMoveArr", name = "")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<OngoingTradeInfoResponse> pageMoveArr = new ArrayList<>();

	@MessageField(id = "cancelPopupArr", name = "")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<OngoingTradeInfoResponse> cancelPopupArr = new ArrayList<>();

	@MessageField(id = "screeningPopupArr", name = "")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<OngoingTradeInfoResponse> screeningPopupArr = new ArrayList<>();

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OngoingTradeInfoResponse implements IMessageObject {
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
	    @MessageField(id = "authntReqDt", name = "인증신청일자", example = "")
	    private String authntReqDt;
	    @MessageField(id = "authntReqCmpltnDt", name = "인증신청완료일자", example = "")
	    private String authntReqCmpltnDt;
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
	    @MessageField(id = "coplCmmnClltrlInd", name = "부부공동담보여부", example = "")
	    private String coplCmmnClltrlInd;
	    @MessageField(id = "delObjFlg", name = "삭제대상여부", example = "")
	    private String delObjFlg;
	    @MessageField(id = "effctvInt", name = "적용금리", example = "")
	    private String effctvInt;
	    @MessageField(id = "elctrncSigntrFlg", name = "전자서명여부", example = "")
	    private String elctrncSigntrFlg;
	    @MessageField(id = "emailAddr", name = "이메일주소", example = "")
	    private String emailAddr;
	    @MessageField(id = "errCd", name = "에러코드", example = "")
	    private String errCd;
	    @MessageField(id = "errMsg", name = "에러메시지", example = "")
	    private String errMsg;
	    @MessageField(id = "execDt", name = "실행일자", example = "")
	    private String execDt;
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
	    @MessageField(id = "intType", name = "금리유형", example = "")
	    private String intType;
	    @MessageField(id = "kfbAcctNo", name = "당행계좌", example = "")
	    private String kfbAcctNo;
	    @MessageField(id = "kfbMgtNo", name = "SC제일은행 관리번호", example = "")
	    private String kfbMgtNo;
	    @MessageField(id = "ktnetMgtNo", name = "KTNET 관리번호", example = "")
	    private String ktnetMgtNo;
	    @MessageField(id = "loanAccptNo", name = "대출접수번호", example = "")
	    private String loanAccptNo;
	    @MessageField(id = "loanHopeDt", name = "대출희망일", example = "")
	    private String loanHopeDt;
	    @MessageField(id = "loanPrd", name = "대출기간", example = "")
	    private String loanPrd;
	    @MessageField(id = "loanReqNo", name = "대출신청번호", example = "")
	    private String loanReqNo;
	    @MessageField(id = "newUserFlg", name = "신규고객여부", example = "")
	    private String newUserFlg;
	    @MessageField(id = "prvsnRegistFlg", name = "약관 등록 여부", example = "")
	    private String prvsnRegistFlg;
	    @MessageField(id = "rdmptnDt", name = "상환기일", example = "")
	    private String rdmptnDt;
	    @MessageField(id = "rdmptnMthd", name = "상환방법", example = "")
	    private String rdmptnMthd;
	    @MessageField(id = "reqAmt", name = "신청금액", example = "")
	    private String reqAmt;
	    @MessageField(id = "rjctnCd", name = "거절코드", example = "")
	    private String rjctnCd;
	    @MessageField(id = "rjctnMsg", name = "거절메시지", example = "")
	    private String rjctnMsg;
	    @MessageField(id = "seqNo", name = "연속번호", example = "")
	    private String seqNo;
	    @MessageField(id = "lstChngDt", name = "최종변경일", example = "")
	    private String lstChngDt;
	    @MessageField(id = "reaCd", name = "부동산코드", example = "")
	    private String reaCd;
	    @MessageField(id = "integratedConselingYn", name = "통합상담여부", example = "")
	    private String integratedConselingYn;
	    @MessageField(id = "oplCnsltYn", name = "'one product 상담 여부", example = "")
	    private String oplCnsltYn;
	    @MessageField(id = "nextURL", name = "Page URL", example = "")
	    private String nextURL;
	}
}
