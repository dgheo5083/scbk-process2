package com.scbank.process.api.svc.shared.components.toss.model;

/**
 * 
 * @author 929948
 *
 */
public class TossHelper {
	public static final String PROP_BASEURL = "toss.outbound.api.base.url";
	public static final String TRAD_INFO_INQ = "toss.outbound.api.TRAD_INFO_INQ"; // 거래정보조회(휴대폰본인인증 전에 실행.)
	public static final String TRAD_STAT_UPD = "toss.outbound.api.TRAD_STAT_UPD"; // 대출상태변경
	public static final String SIGN_DOCS_REQ = "toss.outbound.api.SIGN_DOCS_REQ"; // 전자서명요청
	public static final String SIGN_DOCS_RES = "toss.outbound.api.SIGN_DOCS_RES"; // 전자서명조회
	public static final String OUT_AUTH_TOKEN = "toss.outbound.api.AUTH_TOKEN"; // Toss Auth AccessToken 값

	/** TOSS ACCT OUT BOUND API **/
	public static final String TRAD_CASA_STAT_UPD = "toss.outbound.api.TRAD_CASA_STAT_UPD"; // 요구불 상태 변경

	/** TOSS 인증 토근 세션 키 */
	public static final String TOSS_OUT_AUTH_TOKEN_SESSION_KEY = "TOSS_OUT_AUTH_TOKEN_SESSION_KEY";

	/** TOSS정상/에러 구분 */
	public static final String RESULT_TYPE = "resultType"; // 결과Type 필드명
	public static final String ERROR_CODE = "errorCode"; // 에러코드 필드명
	public static final String ERROR_MSG = "reason"; // 에러메시지 필드명
	public final static String SUCCESS_RESULT_TYPE = "SUCCESS"; // 결과Type=정상코드
	public final static String ERROE_RESULT_TYPE = "FAIL"; // 결과Type=에러코드
	public final static String SUCCESS_RESULT_DATA_OBJ_NAME = "success"; // 정상결과 Object명
	public final static String ERROE_RESULT_DATA_OBJ_NAME = "error"; // 에러결과 Object명

	/** TOSS ACCT API resultData 추가 */
	public static final String RESULT_DATA = "resultData"; // 결과 resultData 필드명

	public static final String RESULT_DATA_CODE = "code"; // 결과 resultData.code 필드명
	public static final String RESULT_DATA_MESSAGE = "message"; // 결과 resultData.message 필드명

}