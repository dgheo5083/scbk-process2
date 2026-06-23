package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterOngoingTradeInfoRequest", type = Type.REQUEST)
public class RegisterOngoingTradeInfoRequest implements IMessageObject {
	@MessageField(id = "targetProcess", name = "MA3CMMBIZ010_10AS 등록/수정구분 I:NF_TRADINFO_MGT_I_02,U:NF_TRADINFO_MGT_U_01", example = "NF_TRADINFO_MGT_I_02")
    private String targetProcess;
	@MessageField(id = "bizType", name = "업무구분", example = "HELS")
	private String bizType;
	@MessageField(id = "custNo", name = "고객번호", example = "9999")
	private String custNo;
	@MessageField(id = "prdctId", name = "상품ID", example = "")
    private String prdctId;
    @MessageField(id = "prdctCd", name = "상품코드", example = "")
    private String prdctCd;
    @MessageField(id = "prdctNm", name = "상품명", example = "")
    private String prdctNm;
    @MessageField(id = "cnnctnWay", name = "제휴처", example = "")
	private String cnnctnWay;
    @MessageField(id = "reacdn", name = "REA코드", example = "")
    private String reacdn;
    @MessageField(id = "clerkNo", name = "행번", example = "")
    private String clerkNo;
    @MessageField(id = "integratedConselingYn", name = "통합상담여부", example = "")
    private String integratedConselingYn;
    @MessageField(id = "prgrssStsCd", name = "진행상태코드", example = "")
    private String prgrssStsCd;
    @MessageField(id = "callCntrStsCd", name = "콜센터상태코드", example = "")
    private String callCntrStsCd;
    @MessageField(id = "jobDetailCd", name = "상세직업코드", example = "")
    private String jobDetailCd;
    @MessageField(id = "cddReqCd", name = "CDD요청여부", example = "")
    private String cddReqCd;
    @MessageField(id = "newUserFlg", name = "신규고객여부", example = "")
    private String newUserFlg;
    @MessageField(id = "delObjFlg", name = "삭제대상여부", example = "")
    private String delObjFlg;
    @MessageField(id = "brnchNo", name = "점번호", example = "")
    private String brnchNo;
    @MessageField(id = "errCd", name = "에러코드", example = "")
    private String errCd;
    @MessageField(id = "errMsg", name = "에러메시지", example = "")
    private String errMsg;
    @MessageField(id = "perPart", name = "perPart", example = "")
    private String perPart;
    @MessageField(id = "docEvdcCd", name = "증빙서류확인", example = "")
    private String docEvdcCd;
    @MessageField(id = "hlPrsnCd", name = "무주택자확인", example = "")
    private String hlPrsnCd;
    @MessageField(id = "adSbstCd", name = "사전청약확인", example = "")
    private String adSbstCd;
    @MessageField(id = "kwInsrCd", name = "권원보험확인", example = "")
    private String kwInsrCd;
    @MessageField(id = "preLoanMoveYn", name = "대출이동여부 (상담)", example = "")
    private String preLoanMoveYn;
    @MessageField(id = "realLoanMoveYn", name = "대출이동여부 (실행)", example = "")
    private String realLoanMoveYn;
    @MessageField(id = "govmDataType", name = "스크래핑타입 - G:공공데이터, S:스크래핑", example = "")
    private String govmDataType;
    @MessageField(id = "govmSignType", name = "서명타입 - 1:공동, B:금융 pin, C:금융생체, 9:디지털", example = "")
    private String govmSignType;
    @MessageField(id = "govmSignErr", name = "서명오류", example = "")
    private String govmSignErr;
    @MessageField(id = "loanPurpose", name = "대출목적", example = "")
    private String loanPurpose;
    @MessageField(id = "collateralType", name = "담보유형", example = "")
    private String collateralType;
    @MessageField(id = "oplCnsltYn", name = "'one product 상담 여부", example = "")
    private String oplCnsltYn;
    @MessageField(id = "idCardCd", name = "신분증 코드", example = "")
    private String idCardCd;
    @MessageField(id = "idCardCnt", name = "신분증 진위 건수", example = "")
    private String idCardCnt;
    @MessageField(id = "reqAmt", name = "신청금액", example = "")
    private String reqAmt;
    @MessageField(id = "authntIndCd", name = "인증유형 여부", example = "")
    private String authntIndCd;
    @MessageField(id = "authntInd", name = "인증유형", example = "")
    private String authntInd;
    @MessageField(id = "authntReqDt", name = "인증신청일자", example = "")
    private String authntReqDt;
    @MessageField(id = "authntReqExpireDt", name = "인증신청만료일자", example = "")
    private String authntReqExpireDt;
    @MessageField(id = "authntReqCmpltnDt", name = "인증신청완료일자", example = "")
    private String authntReqCmpltnDt;
    @MessageField(id = "kfbAcctNo", name = "당행계좌", example = "")
    private String kfbAcctNo;
    @MessageField(id = "alliancCd", name = "제휴코드", example = "")
    private String alliancCd;
    @MessageField(id = "effctvInt", name = "적용금리", example = "")
    private String effctvInt;
    @MessageField(id = "loanPrd", name = "대출기간", example = "")
    private String loanPrd;
    @MessageField(id = "rdmptnMthd", name = "상환방법", example = "")
    private String rdmptnMthd;
    @MessageField(id = "execDt", name = "실행일자", example = "")
    private String execDt;
    @MessageField(id = "rdmptnDt", name = "상환기일", example = "")
    private String rdmptnDt;
    @MessageField(id = "loanReqNo", name = "대출신청번호", example = "")
    private String loanReqNo;
    @MessageField(id = "loanAccptNo", name = "대출접수번호", example = "")
    private String loanAccptNo;
    @MessageField(id = "coplCmmnClltrlInd", name = "부부공동담보여부", example = "")
    private String coplCmmnClltrlInd;
    @MessageField(id = "rjctnCd", name = "거절코드", example = "")
    private String rjctnCd;
    @MessageField(id = "rjctnMsg", name = "거절메시지", example = "")
    private String rjctnMsg;
    @MessageField(id = "tradNo", name = "거래번호", example = "")
    private String tradNo;
    @MessageField(id = "loanHopeDt", name = "대출희망일", example = "")
    private String loanHopeDt;
    @MessageField(id = "easyLoanYn", name = "이지론여부", example = "")
    private String easyLoanYn;
    @MessageField(id = "loanDocInvstgtFlg", name = "대출서류심사여부", example = "")
    private String loanDocInvstgtFlg;
    @MessageField(id = "securityAcctNo", name = "증권계좌번호", example = "")
    private String securityAcctNo;
    @MessageField(id = "hqPrimeRateApplyYn", name = "본부우대금리적용여부", example = "")
    private String hqPrimeRateApplyYn;
    @MessageField(id = "hqPrimeRate", name = "본부우대금리", example = "")
    private String hqPrimeRate;
    @MessageField(id = "spouseCustNo", name = "배우자 고객번호", example = "")
    private String spouseCustNo;
    @MessageField(id = "spouseTradNo", name = "배우자 거래번호", example = "")
    private String spouseTradNo;
    @MessageField(id = "advSeqNo", name = "상담일련번호", example = "")
    private String advSeqNo;
    @MessageField(id = "advDt", name = "상담일자", example = "")
    private String advDt;
    @MessageField(id = "cnnctnTradNo", name = "제휴처거래번호", example = "")
    private String cnnctnTradNo;
    @MessageField(id = "cnnctnLoanLmtAmt", name = "제휴대출한도금액", example = "")
    private String cnnctnLoanLmtAmt;
    @MessageField(id = "cnnctnSignId", name = "제휴전자서명ID", example = "")
    private String cnnctnSignId;
    @MessageField(id = "cnnctnWayDynamic", name = "CNNCTN_WAY_DYNAMIC", example = "")
    private String cnnctnWayDynamic;
    
    
    @MessageField(id = "skipSendLms", name = "LMS 발설 안하도록 설정", example = "")
    private String skipSendLms;
    @MessageField(id = "newPrdctNum", name = "newPrdctNum", example = "")
    private String newPrdctNum;
    @MessageField(id = "newPrdctCurrency", name = "newPrdctCurrency", example = "")
    private String newPrdctCurrency;
    @MessageField(id = "lmsID", name = "lmsID", example = "")
    private String lmsID;
}

