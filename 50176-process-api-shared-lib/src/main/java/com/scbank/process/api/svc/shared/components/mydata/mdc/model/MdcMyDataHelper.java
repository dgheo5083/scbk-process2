package com.scbank.process.api.svc.shared.components.mydata.mdc.model;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;

/**
 * 
 * @author 937827
 *
 */
public class MdcMyDataHelper {
	public static final String MYDATA_PROP_URL = "mydata.api.url";
	public static final String MYDATA_PROP_BASEURL = "mydata.api.baseurl"; // mydata 서버 API 통신 baseURL

	public static final String MYDATA_PROP_PUB_URL = "mydata.api.pub.url"; // mydata 공공데이터 서버 url
	public static final String MYDATA_PROP_PUB_BASEURL = "mydata.api.pub.baseurl"; // mydata 공공데이터 서버 API 통신 baseURL

	public static final String TOKEN_DATA = "mydata.api.TOKEN_DATA"; // MYDATA 토큰 발급 API
	public static final String TOKENDEL_DATA = "mydata.api.TOKENDEL_DATA"; // MYDATA 토큰 삭제 API
	public static final String AGRMNT_DATA = "mydata.api.AGRMNT_DATA"; // 가입공통 - 이용약관동의
	public static final String AUTHORIZE_DATA = "mydata.api.AUTHORIZE_DATA"; // 제공 - 개별인증 제공기관 리스트 선택(인가코드 요청) API
	public static final String ACCOUNT_DATA = "mydata.api.ACCOUNT_DATA"; // 제공 - 계좌목록조회 API
	public static final String CONSENTS_DATA = "mydata.api.CONSENTS_DATA"; // 제공 - 개인신용정보 전송요구 API
	public static final String USEAMT_DATA = "mydata.api.USEAMT_DATA"; // 이용 - 기관별 잔액 조회 API
	public static final String AUTHORGS_DATA = "mydata.api.AUTHORGS_DATA"; // 이용 - 전송요구내역 확인 API
	public static final String CONSENTSREQ_DATA = "mydata.api.CONSENTSREQ_DATA"; // 이용 - 통합인증 전송요구 API
	public static final String CONSENTSLIST_DATA = "mydata.api.CONSENTSLIST_DATA"; // 이용 - 통합인증 자산목록조회 API
	public static final String CONSENTSDTRMNTN_DATA = "mydata.api.CONSENTSDTRMNTN_DATA"; // 이용 - 통합인증 전송요구 API
	public static final String ORGSTOKEN_DATA = "mydata.api.ORGSTOKEN_DATA"; // 이용 - 전송완료 확인 API
	public static final String RETRY_ORGSTOKEN_DATA = "mydata.api.RETRY_ORGSTOKEN_DATA"; // 이용 - 실패기관 재전송 API
	public static final String REVOKE_DATA = "mydata.api.REVOKE_DATA"; // 이용 - 서비스탈퇴 API
	public static final String OAUTHAUTHORIZE_DATA = "mydata.api.OAUTHAUTHORIZE_DATA"; // 이용 - 개별인증 제공기관 리스트 선택(인가코드 요청)
																						// API
	public static final String OAUTHTOKEN_DATA = "mydata.api.OAUTHTOKEN_DATA"; // 이용 - 개별인증 종료(토큰 발급 요청) API
	public static final String SCRAPINGACCOUNTS_DATA = "mydata.api.SCRAPINGACCOUNTS_DATA"; // 이용 - 타행스크래핑 API
	public static final String SCRAPINGTRANSACTIONS_DATA = "mydata.api.SCRAPINGTRANSACTIONS_DATA"; // 이용 - 실시간 계좌조회 API
	public static final String AUTHOFFER_DATA = "mydata.api.AUTHOFFER_DATA"; // 이용 - 거래고유번호로 ORG CD 조회 API
	public static final String BANKACCOUNTLIST_DATA = "mydata.api.BANKACCOUNTLIST_DATA"; // 은행계좌목록 조회 API
	public static final String BANKACCOUNTDETAIL_DATA = "mydata.api.BANKACCOUNTDETAIL_DATA"; // 은행계좌 상세조회 API
	public static final String BANKACCOUNTTRAN_DATA = "mydata.api.BANKACCOUNTTRAN_DATA"; // 은행계좌 거래내역조회 API
	public static final String BANKACCOUNTINVESTER_DATA = "mydata.api.BANKACCOUNTINVESTER_DATA"; // 은행계좌 투자내역조회 API
	public static final String BANKACCOUNTSCDUL_DATA = "mydata.api.BANKACCOUNTSCDUL_DATA"; // 은행계좌 자동이체 등록정보 API
	public static final String CARDLIST_DATA = "mydata.api.CARDLIST_DATA"; // 카드목록 조회 API
	public static final String CARDDETAIL_DATA = "mydata.api.CARDDETAIL_DATA"; // 카드상세 조회 API(/sc/use/card/cardList)
	public static final String CARDTRAN_DATA = "mydata.api.CARDTRAN_DATA"; // 카드거래내역조회 (/sc/use/card/cardTran)
	public static final String CARDRVLVNG_DATA = "mydata.api.CARDRVLVNG_DATA"; // 카드 리볼빙 내역 조회(/sc/use/card/cardRvlvng)
	public static final String CARDSHORTTRMLOAN_DATA = "mydata.api.CARDSHORTTRMLOAN_DATA"; // 카드 단기대출
																							// 조회(/sc/use/card/cardShortTrmLoan)
	public static final String CARDLONGTRMLOAN_DATA = "mydata.api.CARDLONGTRMLOAN_DATA"; // 카드 장기대출
																							// 조회(/sc/use/card/cardLongTrmLoan)
	public static final String CARDLONGTRMLOANTRAN_DATA = "mydata.api.CARDLONGTRMLOANTRAN_DATA"; // 카드장기대출거래내역조회(/sc/use/card/cardLongTrmLoanTran)
	public static final String CARDBASICINFOR_DATA = "mydata.api.CARDBASICINFOR_DATA"; // 카드기본정보조회(/sc/use/card/detail)

	public static final String INVESTLIST_DATA = "mydata.api.INVESTLIST_DATA"; // 금융투자 목록조회(/sc/use/invest/investList)
	public static final String INVESTDFLT_DATA = "mydata.api.INVESTDFLT_DATA"; // 금융투자 기본정보조회(/sc/use/invest/investDflt)
	public static final String INVESTINVESTER_DATA = "mydata.api.INVESTINVESTER_DATA"; // 금융투자
																						// 투자내역조회(/sc/use/invest/investInvester)
	public static final String INVESTTRANS_DATA = "mydata.api.INVESTTRANS_DATA"; // 금융투자
																					// 거래내역조회(/sc/use/invest/investTrans)
	public static final String INVESTDETAIL_DATA = "mydata.api.INVESTDETAIL_DATA"; // 금융투자
																					// 상세정보조회(/sc/use/invest/investDetail)
	public static final String INVESTSCDUL_DATA = "mydata.api.INVESTSCDUL_DATA"; // 금융투자 자동이체 등록정보
																					// (/sc/use/invest/scdul)

	public static final String IRPDETAIL_DATA = "mydata.api.IRPDETAIL_DATA"; // IRP 상세정보조회(/sc/use/irp/dtl)
	public static final String IRPINVESTER_DATA = "mydata.api.IRPINVESTER_DATA"; // IRP 투자내역조회(/sc/use/irp/invest)
	public static final String IRPTRANS_DATA = "mydata.api.IRPTRANS_DATA"; // IRP 거래내역조회(/sc/use/irp/tran)

	public static final String REGISTYN_DATA = "mydata.api.REGISTYN_DATA"; // 가입여부조회(/sc/auth/entrncFlg)
	public static final String AUTHDELETE_DATA = "mydata.api.AUTHDELETE_DATA"; // 서비스삭제(/sc/auth/delete)

	public static final String INSURANCES_DATA = "mydata.api.INSURANCES_DATA"; // 보험목록 조회 API
	public static final String INSURANCES_DFLT = "mydata.api.INSURANCES_DFLT"; // 장기인보험 기본정보조회
	public static final String INSURANCES_SPCL = "mydata.api.INSURANCES_SPCL"; // 장기인보험 특약정보조회
	public static final String INSURANCES_TRAN = "mydata.api.INSURANCES_TRAN"; // 장기인보험 거래내역조회
	public static final String CARINSURANCES_DFLT = "mydata.api.CARINSURANCES_DFLT"; // 자동차보험 기본정보조회
	public static final String CARINSURANCES_TRAN = "mydata.api.CARINSURANCES_TRAN"; // 자동차보험 거래내역

	public static final String INSURANCESGNRL_DFLT = "mydata.api.INSURANCESGNRL_DFLT"; // 물, 일반보험 기본정보조회
	public static final String INSURANCESGNRL_SPCL = "mydata.api.INSURANCESGNRL_SPCL"; // 물, 일반보험 특약정보조회
	public static final String INSURANCESGNRL_TRAN = "mydata.api.INSURANCESGNRL_TRAN"; // 물, 일반보험 거래내역조회
	public static final String INSURANCESGNRL_PROTECT = "mydata.api.INSURANCESGNRL_PROTECT"; // 물, 일반보험 거래내역조회

	public static final String INSURED_DFLT = "mydata.api.INSURED_DFLT"; // 피보험자 장기인보험 기본정보조회
	public static final String INSURED_SPCL = "mydata.api.INSURED_SPCL"; // 피보험자 장기인보험 특약정보조회
	public static final String INSURED_PROTECT = "mydata.api.INSURED_PROTECT"; // 피보험자 장기인보험 보장정보조회

	public static final String CARINSURED_DFLT = "mydata.api.CARINSURED_DFLT"; // 피보험자 자동차보험 기본정보조회

	public static final String INSUREDGNRL_DFLT = "mydata.api.INSUREDGNRL_DFLT"; // 피보험자 물, 일반보험 기본정보조회
	public static final String INSUREDGNRL_SPCL = "mydata.api.INSUREDGNRL_SPCL"; // 피보험자 물, 일반보험 특약정보조회
	public static final String INSUREDGNRL_PROTECT = "mydata.api.INSUREDGNRL_PROTECT"; // 피보험자 물, 일반보험 보장정보조회

	public static final String LOANINSURANCES_DFLT = "mydata.api.LOANINSURANCES_DFLT"; // 대출 기본정보조회
	public static final String LOANINSURANCES_TRAN = "mydata.api.LOANINSURANCES_TRAN"; // 대출 거래내역조회
	public static final String INSURANCES_PROTECT = "mydata.api.INSURANCES_PROTECT"; // 보험 보장내역조회
	public static final String CAPITALLIST_DATA = "mydata.api.CAPITALLIST_DATA"; // 기타목록 조회(할부금융) API
	public static final String TELECOMELIST_DATA = "mydata.api.TELECOMELIST_DATA"; // 기타목록 조회(통신) API
	public static final String CAPITALTRANS_DATA = "mydata.api.CAPITALTRANS_DATA"; // 거래내역조회 (할부금융) API
	public static final String CAPITALDETAIL_DATA = "mydata.api.CAPITALDETAIL_DATA"; // 거래내역조회 상세(할부금융) API
	public static final String TELECOMETRANS_DATA = "mydata.api.TELECOMETRANS_DATA"; // 거래내역조회(통신) API
	public static final String TELECOMEDETAIL_DATA = "mydata.api.TELECOMEDETAIL_DATA"; // 거래내역조회 상세(통신) API
	public static final String USEEFINLIST_DATA = "mydata.api.USEEFINLIST_DATA"; // 전자금융목록조회 API
	public static final String USEEFINTRANS_DATA = "mydata.api.USEEFINTRANS_DATA"; // 전자금융목록 거래내역조회 API
	public static final String USEEFINDETAIL_DATA = "mydata.api.USEEFINDETAIL_DATA"; // 전자금융목록 상세조회 API
	public static final String CRDTINFOZINAGRMNT_DATA = "mydata.api.CRDTINFOZINAGRMNT_DATA"; // 개별인증 동의

	// 공공데이터 추가
	public static final String REGISTINFOLIST_DATA = "mydata.api.REGISTINFOLIST_DATA"; // 사업자등록증/폐업사실증명/휴업사실증명
	public static final String TAXRECEIVEINFO_DATA = "mydata.api.TAXRECEIVEINFO_DATA"; // 부가가치세과세표준증명Receive
	public static final String TAXLOANINFO_DATA = "mydata.api.TAXLOANINFO_DATA"; // 부가가치세과세표준증명Loan
	public static final String HLTHRECEIVEINFO_DATA = "mydata.api.HLTHRECEIVEINFO_DATA"; // 건강보험자격득실확인서Receive
	public static final String HLTHCARDINFO_DATA = "mydata.api.HLTHCARDINFO_DATA"; // 건강보험자격득실확인서Card
	public static final String HLTHLOANINFO_DATA = "mydata.api.HLTHLOANINFO_DATA"; // 건강보험자격득실확인서Loan
	public static final String MDCLRECEIVELIST_DATA = "mydata.api.MDCLRECEIVELIST_DATA"; // 건강보험납부확인서Receive
	public static final String MDCLCARDLIST_DATA = "mydata.api.MDCLCARDLIST_DATA"; // 건강보험납부확인서Card
	public static final String MDCLLOANLIST_DATA = "mydata.api.MDCLLOANLIST_DATA"; // 건강보험납부확인서Loan
	public static final String RSDNTINFO_DATA = "mydata.api.RSDNTINFO_DATA"; // 주민등록초본
	public static final String CERTINFO_DATA = "mydata.api.CERTINFO_DATA"; // 사업자등록증명
	public static final String NTNLINFO_DATA = "mydata.api.NTNLINFO_DATA"; // 납세증명서
	public static final String INCOMELIST_DATA = "mydata.api.INCOMELIST_DATA"; // 소득금액증명

	public static final String PRFRNCLCASECNFRM = "mydata.api.PRFRNCLCASECNFRM_DATA"; // 비과세종합저축 관련 우대사항확인(MDS0007931)
	public static final String PENSION_DATA = "mydata.api.PENSION_DATA"; // 공무원연금공단
	public static final String CONNHISTLIST_DATA = "mydata.api.CONNHISTLIST_DATA"; // 마이데이터 연결이력 조회
	public static final String HYUNDAICARD_FIVEDAYS_DATA = "mydata.api.HYUNDAICARD_FIVEDAYS_DATA"; // 마이데이터 현대카드 최근 5일
																									// 결제금액 조회
	public static final String HYUNDAICARD_THREEMONTH_DATA = "mydata.api.HYUNDAICARD_THREEMONTH_DATA"; // 마이데이터 현대카드 최근
																										// 3개월 결제금액 조회
	public static final String HYUNDAICARD_LASTMONTH_DATA = "mydata.api.HYUNDAICARD_LASTMONTH_DATA"; // 마이데이터 현대카드 지난달
																										// 가장많이쓴 가맹점(금액)
																										// 조회
	public static final String SELECT_AGR = "mydata.api.SELECT_AGR"; // 마이데이터 선택동의서 마케팅 동의

	public static final String personlSend_DATA = "mydata.api.personlSend_DATA"; // 임시 테스트 용 추후 삭제

	public static final String MGMTSSIGNUP_DATA = "mydata.api.MGMTSSIGNUP_DATA"; // 마이데이터 가입현황 통합조회
	public static final String MGMTSAGREMENTS_DATA = "mydata.api.MGMTSAGREMENTS_DATA"; // 제3자 제고 동의내역 요청
	public static final String MGMTSAGREMENTSDETAIL_DATA = "mydata.api.MGMTSAGREMENTSDETAIL_DATA"; // 제3자 제고 동의에 따른
																									// 제공내역요청

	public static final String RESULT_TYPE = "rsp_code"; // 결과Type 필드명 (성공 / 에러메시지)
	public static final String HOME_RESULT_TYPE = "ECODE"; // 결과Type 필드명 (성공 / 에러메시지)
	public static final String ERROR_CODE = "code"; // 에러코드 필드명
	public static final String ERROR_MSG = "reason"; // 에러메시지 필드명
	public final static String SUCCESS_RESULT_TYPE = "00000"; // 결과Type=정상코드
	public final static String HOME_SUCCESS_RESULT_TYPE = "00000"; // 결과Type=정상코드
	public final static String ERROR_RESULT_TYPE = "FAIL"; // 결과Type=에러코드
	public final static String SUCCESS_RESULT_DATA_OBJ_NAME = "success"; // 정상결과 Object명
	public final static String ERROR_RESULT_DATA_OBJ_NAME = "error"; // 에러결과 Object명

	public final static String SLEEPACCOUNTLIST_DATA = "mydata.api.SLEEPACCOUNTLIST_DATA"; // 숨은금융
	public final static String SLEEPACCOUNTINSULIST_DATA = "mydata.api.SLEEPACCOUNTINSULIST_DATA"; // 숨은금융- 보험

	public static String getBaseUrl() {
		return PropertiesUtils.getString(MYDATA_PROP_BASEURL);
	}

	public static boolean isHttps() {
		return (getBaseUrl().indexOf("https:") == 0);
	}

}
