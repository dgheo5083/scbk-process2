package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행상태조회 Result <BR>
 * ASIS: NF_TRADINFO_MGT_S_03
 */
@Data
public class CheckOngoingTradeInfoInquiryResult {
	/** 고객번호 */
	private String custNo;
	/** 거래번호 */
    private String tradNo;
    /** 업무구분 */
    private String bizType;
    /** 상품ID */
    private String prdctId;
    /** 상품코드 */
    private String prdctCd;
    /** 상품명 */
    private String prdctNm;
    /** 진행상태코드 */
    private String prgrssStsCd;
    /** 제휴코드 */
    private String alliancCd;
    /** 승인자행번 */
    private String apprverClerkNo;
    /** 승인일시 */
    private String apprvlDt;
    /** 인증유형 */
    private String authntInd;
    /** 인증유형여부 */
    private String authntIndCd;
    /** 인증신청일자 */
    private String authntReqDt;
    /** 인증신청완료일자 */
    private String authntReqCmpltnDt;
    /** 인증신청만료일자 */
    private String authntReqExpireDt;
    /** 블락행번 */
    private String blkClerkNo;
    /** 점번호 */
    private String brnchNo;
    /** 콜센터상태코드 */
    private String callCntrStsCd;
    /** CDD 등록일자 */
    private String cddCnfrmDt;
    /** CDD요청여부 */
    private String cddReqCd;
    /** 행번 */
    private String clerkNo;
    /** 완료일자 */
    private String cmpltnDt;
    /** 취소일자 */
    private String cnclDt;
    /** 접속방식, 제휴처 */
    private String cnnctnWay;
    /** 부부공동담보여부 */
    private String coplCmmnClltrlInd;
    /** 삭제대상여부 */
    private String delObjFlg;
    /** 적용금리 */
    private String effctvInt;
    /** 전자서명여부 */
    private String elctrncSigntrFlg;
    /** 이메일주소 */
    private String emailAddr;
    /** 에러코드 */
    private String errCd;
    /** 에러메시지 */
    private String errMsg;
    /** 실행일자 */
    private String execDt;
    /** 신분증 코드 */
    private String idCardCd;
    /** 신분증 진위 완료 일자 */
    private String idCardCmpltnDt;
    /** 신분증 진위 건수 */
    private String idCardCnt;
    /** 신분증 진위 요청 일자 */
    private String idCardReqDt;
    /** 최초등록일 */
	private String initRegistDt;
	/** 금리유형 */
    private String intType;
    /** 당행계좌 */
    private String kfbAcctNo;
    /** SC제일은행 관리번호 */
    private String kfbMgtNo;
    /** KTNET 관리번호 */
    private String ktnetMgtNo;
    /** 대출접수번호 */
    private String loanAccptNo;
    /** 대출희망일 */
    private String loanHopeDt;
    /** 대출기간 */
    private String loanPrd;
    /** 대출신청번호 */
    private String loanReqNo;
    /** 신규고객여부 */
    private String newUserFlg;
    /** 약관 등록 여부 */
    private String prvsnRegistFlg;
    /** 상환기일 */
    private String rdmptnDt;
    /** 상환방법 */
    private String rdmptnMthd;
    /** 신청금액 */
    private String reqAmt;
    /** 거절코드 */
    private String rjctnCd;
    /** 거절메시지 */
    private String rjctnMsg;
    /** 연속번호 */
    private String seqNo;
    /** 최종변경일 */
    private String lstChngDt;
    /** 부동산코드 */
    private String reaCd;
    /** 통합상담여부 */
    private String integratedConselingYn;
    /** 'one product 상담 여부 */
    private String oplCnsltYn;
    
    /** Component로직에서 생성하여 추가하는 데이터 */
    private String nextURL;
}
