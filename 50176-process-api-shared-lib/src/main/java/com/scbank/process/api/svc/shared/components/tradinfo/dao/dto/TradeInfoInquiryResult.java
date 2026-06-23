package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행상태 테이블 고객번호 & 거래번호 조회 <br>
 * ASIS : NF_TRADINFO_MGT_S_05
 */
@Data
public class TradeInfoInquiryResult {
	/** 최초등록일 */
	private String initRegistDt;

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
    /** 콜센터상태코드 */
    private String callCntrStsCd;
    /** 진행상태코드 */
    private String prgrssStsCd;
    /** 거절코드 */
    private String rjctnCd;
    /** 거절메시지 */
    private String rjctnMsg;
    /** CDD요청여부 */
    private String cddReqCd;
    /** 행번 */
    private String clerkNo;
    /** 신분증 코드 */
    private String idCardCd;
    /** 인증유형여부 */
    private String authntIndCd;
    /** 신규고객여부 */
    private String newUserFlg;
    /** 완료일자 */
    private String cmpltnDt;
    /** 취소일자 */
    private String cnclDt;
    /** 제휴처 */
    private String cnnctnWay;
    /** 점번호 */
    private String brnchNo;
    /** 신청금액 */
    private String reqAmt;
    /** 인증유형 */
    private String authntInd;
    /** 최종변경일 */
    private String lstChngDt;
    /** 당행계좌 */
    private String kfbAcctNo;
    /** 제휴코드 */
    private String alliancCd;
    /** 적용금리 */
    private String effctvInt;
    /** 대출기간 */
    private String loanPrd;
    /** 상환방법 */
    private String rdmptnMthd;
    /** 실행일자 */
    private String execDt;
    /** 상환기일 */
    private String rdmptnDt;
    /** 대출신청번호 */
    private String loanReqNo;
    /** 이메일주소 */
    private String emailAddr;
    /** 에러코드 */
    private String errCd;
    /** 에러메시지 */
    private String errMsg;
    /** SC제일은행 관리번호 */
    private String kfbMgtNo;
    /** KTNET 관리번호 */
    private String ktnetMgtNo;
    /** 금리유형 */
    private String intType;
    /** 부부공동담보여부 */
    private String coplCmmnClltrlInd;
    /** 대출접수번호 */
    private String loanAccptNo;
    /** 통화코드 */
    private String currencyCode;
    /** 주민번호 */
    private String ssn;    
}
