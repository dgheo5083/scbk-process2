package com.scbank.process.api.svc.common.components;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.dto.GetCheckAvailableRequest;
import com.scbank.process.api.svc.common.components.dto.GetCheckAvailableResponse;
import com.scbank.process.api.svc.common.constant.IdentityConstants;
import com.scbank.process.api.svc.common.utils.Base64Util;
import com.scbank.process.api.svc.shared.components.alchera.AlcAES256;
import com.scbank.process.api.svc.shared.components.product.ProductInfoComponent;
import com.scbank.process.api.svc.shared.components.product.dto.PmsProductAvailableInfoRequest;
import com.scbank.process.api.svc.shared.components.product.dto.PmsProductAvailableInfoResponse;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;
import com.scbank.process.api.svc.shared.config.BaseHttpClient.HttpResponse;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import bio.face.Faceprint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class IdentityOcrComponent {

	private final ISessionContextManager sessionManager;
	private final ProductInfoComponent productInfoComponent;
	private final BaseHttpClient httpClient;

	/**
	 * PMS 이용가능여부
	 *
	 * @param GetCheckAvailableRequest
	 * @return GetCheckAvailableResponse
	 * @throws
	 * @description
	 */
	@ComponentOperation(name = "PMS 이용가능여부", author = "")
	public GetCheckAvailableResponse getCheckAvailable(IServiceContext ctx, GetCheckAvailableRequest input) {
		GetCheckAvailableResponse output = new GetCheckAvailableResponse();

		String perBusNo = SessionUtils.getSessionValue("PerBusNo");
		String bizType = StringUtils.defaultIfEmpty(input.getBizType(), SessionUtils.getSessionValue("BIZ_TYPE"));

		if (bizType.matches("TRLR|IDCM")) {
			// 이전에 진행한 상품코드로 인입되는 업무가 있어, 해당 BIZ_TYPE으로 인입시 초기화
			sessionManager.removeGlobalValue("PMS_PRODUCT_CODE");
		}

		String productCode = StringUtils.nvl(sessionManager.getGlobalValue("PMS_PRODUCT_CODE", String.class), ""); // PMS에 등록된 상품코드
		String availableAge = ""; // 상품별 이용가능나이
		String userAge = ""; // 사용자 나이
		String isLowAge = ""; // 최소나이 여부 (Y:최소나이 미해당, N:최소나이 해당)
		String isForeigner = ""; // 외국인 여부 (Y:외국인, N:내국인)

		if (StringUtils.isNotEmpty(bizType) && StringUtils.isNotEmpty(productCode)) {

			// TODO MA3CMMBIZ031_100S

			PmsProductAvailableInfoRequest productInfoReq = new PmsProductAvailableInfoRequest();

			productInfoReq.setTargetPrdctCd(productCode);
			productInfoReq.setThrowExceptionYn("N");

			PmsProductAvailableInfoResponse productInfoRes = productInfoComponent.getPmsProductAvailableInfo(productInfoReq);

			availableAge = productInfoRes.getAvailableAge();

			isLowAge = productInfoRes.getIsLowAge();
			isForeigner = productInfoRes.getIsForeigner();
			userAge = productInfoRes.getUserAge();

		}

		if (StringUtils.isEmpty(availableAge) && StringUtils.isNotEmpty(perBusNo)) {
			Map<String, String> chkResult = BankingBizUtils.getForeignerAndUnderAge(perBusNo, "19");
			userAge = chkResult.get("userAge");
			isLowAge = chkResult.get("isLowAge");
			isForeigner = chkResult.get("isForeigner");
		}

		output.setUserAge(userAge);
		output.setIsLowAge(isLowAge);
		output.setIsForeigner(isForeigner);

		log.debug("PMS 이용가능여부 response => {}", output);

		return output;
	}

	@ComponentOperation(name = "신분증 맵 초기화", author = "")
	public Map<IdentityConstants.IdType, String> idCardMapInit() {
		Map<IdentityConstants.IdType, String> idCardMap = new EnumMap<>(IdentityConstants.IdType.class);

		for (IdentityConstants.IdType t : IdentityConstants.IdType.values()) {
			idCardMap.put(t, "N");
		}

		return idCardMap;
	}

	/**
	 * 신분증 촬영가능 항목 추가
	 * 
	 * @param map
	 * @param types
	 */
	@ComponentOperation(name = "신분증 촬영 가능 항목 추가", author = "")
	public void enable(Map<IdentityConstants.IdType, String> idCardMap, IdentityConstants.IdType... types) {
		if (types == null)
			return;
		for (IdentityConstants.IdType t : types) {
			idCardMap.put(t, "Y");
		}
	}

	/**
	 * 신분증 촬영가능 항목 제외
	 * 
	 * @param map
	 * @param types
	 */
	@ComponentOperation(name = "신분증 촬영가능 항목 제외", author = "")
	public void disable(Map<IdentityConstants.IdType, String> map, IdentityConstants.IdType... types) {
		if (types == null)
			return;
		for (IdentityConstants.IdType t : types) {
			map.put(t, "N");
		}
	}

	/**
	 * 내국인 신분증 촬영 가능 항목 설정
	 * 
	 * @param idCardMap
	 * @param bizType
	 * @param userAge
	 */
	@ComponentOperation(name = "내국인 신분증 촬영 가능 항목 설정", author = "")
	public void enableKoreanIds(Map<IdentityConstants.IdType, String> idCardMap, String bizType, int userAge) {
		if (userAge >= 19) { // 19세이상
			switch (bizType) {
			case "INBK": // [회원가입] 회원가입
			case "CASA": // [금융상품] 입출금(원화)
			case "FCSA": // [금융상품] 입출금(외화)
			case "BBCA": // [금융상품] 입출금(다모아비즈(개사))
			case "OPPL": // [금융상품] 신용대출(SC제일 신용대출)
			case "NSPL": // [금융상품] 신용대출(새희망홀씨2)
			case "MCDD": // [제신고] 고객확인제도/CDD
			case "ca2": // [기타] 신분증 재촬영
			case "CMJG": // [금융상품] 전세론신청 (CDD 신분증징구를 위함)
			case "CMJL": // [금융상품] 전세론연기 (CDD 신분증징구를 위함)
				enable(idCardMap, IdentityConstants.IdType.JUMIN, IdentityConstants.IdType.DRIVER,
						IdentityConstants.IdType.VETERAN, IdentityConstants.IdType.DISABILITY);
				break;

			case "TRLR": // [제신고] 이체한도증액
			case "MPLR": // [제신고] 계좌비밀번호 재설정
			case "MHLR": // [제신고] 휴면계좌활성화
			case "MBAC": // [제신고] 전자금융 기본계좌변경
			case "MOTP": // [제신고] 모바일OTP 발급/재발급
			case "MOLR": // [제신고] OTP/보안카드 오류해제
			case "MACC": // [제신고] 출금계좌 허용등록
			case "MPRS": // [제신고] 로그인 비밀번호 재설정
			case "MRAS": // [기타] 화상상담
			case "IDCM": // [기타] 신분증 공통화면(FDS)
				enable(idCardMap, IdentityConstants.IdType.JUMIN, IdentityConstants.IdType.DRIVER,
						IdentityConstants.IdType.VETERAN, IdentityConstants.IdType.PASSPORT, IdentityConstants.IdType.DISABILITY);
				break;

			default: // 그 외 업무 불가
				idCardMap = idCardMapInit();
				break;
			}

		} else if (userAge >= 17) { // 17세이상
			switch (bizType) {
			case "INBK": // [회원가입] 회원가입
			case "CASA": // [금융상품] 입출금(원화)
			case "FCSA": // [금융상품] 입출금(외화)
			case "BBCA": // [금융상품] 입출금(다모아비즈(개사))
			case "MCDD": // [제신고] 고객확인제도/CDD
			case "ca2": // [기타] 신분증 재촬영
				enable(idCardMap, IdentityConstants.IdType.JUMIN, IdentityConstants.IdType.DRIVER,
						IdentityConstants.IdType.VETERAN, IdentityConstants.IdType.DISABILITY);
				break;

			case "MOTP": // [제신고] 모바일OTP 발급/재발급
			case "MOLR": // [제신고] OTP/보안카드 오류해제
			case "MPRS": // [제신고] 로그인 비밀번호 재설정
				enable(idCardMap, IdentityConstants.IdType.JUMIN, IdentityConstants.IdType.DRIVER,
						IdentityConstants.IdType.VETERAN, IdentityConstants.IdType.PASSPORT, IdentityConstants.IdType.DISABILITY);
				break;

			default: // 그 외 업무 불가
				idCardMap = idCardMapInit();
				break;
			}

		} else {
			idCardMap = idCardMapInit();
		}
	}

	/**
	 * 재외국민 신분증 목록
	 * 
	 * <pre>
	 * -주민등록증 - 운전면허증 - 국가보훈등록증 - 여권
	 * </pre>
	 * 
	 * @param idCardMap
	 * @param bizType
	 * @param userAge
	 */
	@ComponentOperation(name = "재외국인 신분증 촬영 가능 항목 설정", author = "")
	public void enableOverSeasKoreanIds(Map<IdentityConstants.IdType, String> idCardMap, String bizType, int userAge) {
		if (userAge >= 19) { // 19세이상
			switch (bizType) {
			case "INBK": // [회원가입] 회원가입
			case "CASA": // [금융상품] 입출금(원화)
			case "FCSA": // [금융상품] 입출금(외화)
			case "BBCA": // [금융상품] 입출금(다모아비즈(개사))
			case "OPPL": // [금융상품] 신용대출(SC제일 신용대출)
			case "NSPL": // [금융상품] 신용대출(새희망홀씨2)
			case "MCDD": // [제신고] 고객확인제도/CDD
			case "ca2": // [기타] 신분증 재촬영
			case "CMJG": // [금융상품] 전세론신청 (CDD 신분증징구를 위함)
			case "CMJL": // [금융상품] 전세론연기 (CDD 신분증징구를 위함)
				enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.VETERAN);
				break;

			case "TRLR": // [제신고] 이체한도증액
			case "MPLR": // [제신고] 계좌비밀번호 재설정
			case "MHLR": // [제신고] 휴면계좌활성화
			case "MOTP": // [제신고] 모바일OTP 발급/재발급
			case "MOLR": // [제신고] OTP/보안카드 오류해제
			case "MACC": // [제신고] 출금계좌 허용등록
				if (sessionManager.isLogin()) {
					enable(idCardMap, IdentityConstants.IdType.JUMIN, IdentityConstants.IdType.DRIVER,
							IdentityConstants.IdType.VETERAN, IdentityConstants.IdType.PASSPORT);
				} else {
					enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.VETERAN,
							IdentityConstants.IdType.PASSPORT);
				}
				break;

			case "MBAC": // [제신고] 전자금융 기본계좌변경
			case "MPRS": // [제신고] 로그인 비밀번호 재설정
			case "MRAS": // [기타] 화상상담
			case "IDCM": // [기타] 신분증 공통화면(FDS)
				enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.VETERAN,
						IdentityConstants.IdType.PASSPORT);
				break;

			default: // 그 외 업무 불가
				idCardMap = idCardMapInit();
				break;
			}

		} else if (userAge >= 17) { // 17세이상
			switch (bizType) {
			case "INBK": // [회원가입] 회원가입
			case "CASA": // [금융상품] 입출금(원화)
			case "FCSA": // [금융상품] 입출금(외화)
			case "BBCA": // [금융상품] 입출금(다모아비즈(개사))
			case "MCDD": // [제신고] 고객확인제도/CDD
			case "ca2": // [기타] 신분증 재촬영
				enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.VETERAN);
				break;

			case "MOTP": // [제신고] 모바일OTP 발급/재발급
			case "MOLR": // [제신고] OTP/보안카드 오류해제
				if (sessionManager.isLogin()) {
					enable(idCardMap, IdentityConstants.IdType.JUMIN, IdentityConstants.IdType.DRIVER,
							IdentityConstants.IdType.VETERAN, IdentityConstants.IdType.PASSPORT);
				} else {
					enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.VETERAN,
							IdentityConstants.IdType.PASSPORT);
				}
				break;

			case "MPRS": // [제신고] 로그인 비밀번호 재설정
				enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.VETERAN,
						IdentityConstants.IdType.PASSPORT);
				break;

			default: // 그 외 업무 불가
				idCardMap = idCardMapInit();
				break;
			}

		} else {
			idCardMap = idCardMapInit();
		}
	}

	/**
	 * 외국인 신분증 목록
	 * 
	 * <pre>
	 * -운전면허증 - 외국인등록증
	 * </pre>
	 * 
	 * @param idCardMap
	 * @param bizType
	 * @param userAge
	 */
	@ComponentOperation(name = "외국인 신분증 촬영 가능 항목 설정", author = "")
	public void enableForeignIds(Map<IdentityConstants.IdType, String> idCardMap, String bizType, int userAge) {
		if (userAge >= 19) { // 19세이상
			switch (bizType) {
			case "TRLR": // [제신고] 이체한도증액
			case "MPLR": // [제신고] 계좌비밀번호 재설정
			case "MHLR": // [제신고] 휴면계좌활성화
			case "MOTP": // [제신고] 모바일OTP 발급/재발급
			case "MOLR": // [제신고] OTP/보안카드 오류해제
			case "MACC": // [제신고] 출금계좌 허용등록
			case "MRAS": // [기타] 화상상담
			case "IDCM": // [기타] 신분증 공통화면(FDS)
				enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.ALIEN);
				break;

			case "ca2": // [기타] 신분증 재촬영
				enable(idCardMap, IdentityConstants.IdType.DRIVER);
				break;

			default: // 그 외 업무 불가
				idCardMap = idCardMapInit();
				break;
			}

		} else if (userAge >= 17) { // 17세이상
			switch (bizType) {
			case "TRLR": // [제신고] 이체한도증액
			case "MPLR": // [제신고] 계좌비밀번호 재설정
			case "MHLR": // [제신고] 휴면계좌활성화
			case "MOTP": // [제신고] 모바일OTP 발급/재발급
			case "MOLR": // [제신고] OTP/보안카드 오류해제
				enable(idCardMap, IdentityConstants.IdType.DRIVER, IdentityConstants.IdType.ALIEN);
				break;

			case "MRAS": // [기타] 화상상담
				enable(idCardMap, IdentityConstants.IdType.ALIEN);
				break;

			default: // 그 외 업무 불가
				idCardMap = idCardMapInit();
				break;
			}

		} else {
			idCardMap = idCardMapInit();
		}
	}

	/**
	 * 신분증(실물/모바일 분기) 및 필터 적용
	 * 
	 * @param idCardMap
	 * @return
	 * 
	 *         <pre>
	 * {
	 *     "PHYSICAL": { "JUMIN": "Y", "DRIVER": "Y", "PASSPORT": "Y", "ALIEN": "N", "VETERAN": "Y" },
	 *     "MOBILE"  : { "JUMIN": "Y", "DRIVER": "Y", "PASSPORT": "N", "ALIEN": "N", "VETERAN": "Y" },
	 * }
	 *         </pre>
	 * 
	 */
	@ComponentOperation(name = "신분증(실물/모바일 분기) 및 필터 적용", author = "")
	public Map<IdentityConstants.Medium, Map<IdentityConstants.IdType, String>> idCardPolicyFilter(
			Map<IdentityConstants.IdType, String> idCardMap) {
		Map<IdentityConstants.Medium, Map<IdentityConstants.IdType, String>> output = new LinkedHashMap<>();

		Map<IdentityConstants.IdType, String> physical = new LinkedHashMap<>(idCardMap);
		Map<IdentityConstants.IdType, String> mobile = new LinkedHashMap<>(idCardMap);

		output.put(IdentityConstants.Medium.PHYSICAL, physical); // 실물 신분증 목록
		output.put(IdentityConstants.Medium.MOBILE, mobile); // 모바일 신분증 목록

		// =====================
		// TODO: 실물 신분증 (목록: 주민등록증, 운전면허증, 국가보훈등록증, 외국인등록증, 여권)
		// → 실물 신분증은 여기에 정의해주세요.
		// =====================
		if (output.keySet().contains(IdentityConstants.Medium.PHYSICAL)) {
			// 외국인등록증 촬영 허용여부 (Y:허용, N:제외)
			String foreignerIdCardYn = StringUtils
					.defaultIfEmpty(PropertiesUtils.getString("FOREIGNER_ID_CARD_SHOOTING"), "N");

			// 국가보훈등록증 촬영 허용여부 (Y:허용, N:제외)
			String bohoonIdCardYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString("BOHOON_ID_CARD_SHOOTING"),
					"N");

			// 여권 촬영 허용여부 (Y:허용, N:제외)
			String passportIdCardYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString("PASSPORT_ID_CARD_SHOOTING"),
					"N");

			if ("N".equalsIgnoreCase(foreignerIdCardYn)) {
				disable(physical, IdentityConstants.IdType.ALIEN); // 외국인등록증 제외
			}

			if ("N".equalsIgnoreCase(bohoonIdCardYn)) {
				disable(physical, IdentityConstants.IdType.VETERAN); // 국가보훈등록증 제외
			}

			if ("N".equalsIgnoreCase(passportIdCardYn)) {
				disable(physical, IdentityConstants.IdType.PASSPORT); // 여권 제외
			}
			
			// 실물 신분증일때는 장애인신분증인 제외 ( 모바일신분증에서만 가능 )
			disable(physical, IdentityConstants.IdType.DISABILITY); // 장애인신분증 제외
		}

		// =====================
		// TODO: 모바일 신분증 (목록: 주민등록증, 운전면허증, 국가보훈등록증, 외국인등록증)
		// → 모바일 신분증은 여기에 정의해주세요.
		// =====================
		if (output.keySet().contains(IdentityConstants.Medium.MOBILE)) {
			disable(mobile, IdentityConstants.IdType.PASSPORT); // 여권 제외
		}

		return output;
	}

	/**
	 * [알체라] 특징점 정보 요청(사진정보, 점수) - 대상 신분증 (여권)
	 * 
	 * @param encryptBase64Image
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "특징점 정보 요청(여권)", author = "")
	public JSONObject executePassportVerfication(String encryptBase64Image) throws Exception {
		String SUCCESS_CODE = "SUCC-0000";
		String API_URL = PropertiesUtils.getString("ALCHERA_FACE_AUTH_SERVER_BASE_URL");

		// 암호화된 이미지 추출 및 복호화
		String decryptedImg = AlcAES256.decrypt(encryptBase64Image);
		// 알체라 API규격에 맞는 JSON Body 생성
		JSONObject requestJson = new JSONObject();
		requestJson.put("image", decryptedImg);
		// 통신설정
		// 통신 헤더 및 파라미터 셋팅
		HttpPost httpPost = new HttpPost(API_URL + "/v2/verification?target=passport");
		// 타임아웃 설정
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(Timeout.of(Duration.ofSeconds(60))) // 커넥션을 맺는데 걸리는 timeout 시간 설정
				.setResponseTimeout(Timeout.of(Duration.ofSeconds(60))) // 커넥션을 맺은후 응답대기 timeout 시간 설정 60초
				.build();

		httpPost.setConfig(requestConfig);
		// 헤더 및 엔티티 설정(JSON)
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		// JSON 바디 셋팅
		StringEntity entity = new StringEntity(requestJson.toString(), StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		
		HttpResponse response = httpClient.execute(httpPost);
		
		String responseStr = response.getBody();
		
		if(responseStr == null) {
			throw new Exception("응답 데이터가 비어있습니다.");
		}
		
		JSONObject resultJson = new JSONObject(responseStr);
		
		// 알체라 에러 메세지를 통한 체크
		if (resultJson.has("return_msg")) {
			JSONObject returnMsgObj = resultJson.getJSONObject("return_msg");
			String returnCode = returnMsgObj.optString("return_code");
			if (SUCCESS_CODE.equals(returnCode)) {
				// 통신 성공 : 전체 json 리턴
				return resultJson;
			} else {
				// 비지니스 로직 에러
				String errorMsg = returnMsgObj.optString("return_msg");
				throw new Exception("알체라 API 오류 : [" + returnCode + "] " + errorMsg);
			}
		}
		
		if (!response.isOK()) {
			throw new Exception("HTTP 통신 오류:" + response.getCode());
		}
		return resultJson;
		
	}

	/**
	 * [한국인식산업] 특징점 정보 요청(사진정보, 점수) - 대상 신분증 (주민등록증, 운전면허증, 외국인등록증, 국가보훈증)
	 * 
	 * @param imageByte
	 * @return
	 */
	@ComponentOperation(name = "특징점 정보 요청", author = "")
	public JSONObject facePrint(byte[] imageByte) {
		int extractScore = -1;
		JSONObject retunJsonData = new JSONObject();

		Faceprint fp = null;
		String unicode = "10000001";
		byte[] DBFeature = new byte[3500]; // db 특징점
		String homePath = (RunMode.PRD.equals(RuntimeContext.getRunMode()))
				? PropertiesUtils.getString("SCNF_HOME_PATH_REAL")
				: PropertiesUtils.getString("SCNF_HOME_PATH_DEV");
		String cmmLibPath = homePath + "libup64/"; // lib 위치
		String confPath = homePath; // data 폴더 위치
		byte[] dbimage = imageByte; // 얼굴이미지 (특징점)
		int dlen = dbimage.length;

		log.debug("IdentityOcrComponent facePrint [ facePrint lib path cmmLibPath ] : {}", cmmLibPath);
		log.debug("IdentityOcrComponent facePrint [ facePrint data path confPath ] : {}", confPath);
		log.debug("IdentityOcrComponent facePrint [ facePrint imagelength ] : {}", dlen);

		try {
			fp = new Faceprint();
			fp.jniFAStart(cmmLibPath); // 특징점 라이브러리 시작

			// 이미지 값에 대해서 검증하는 로직
			extractScore = fp.jniFAExtractDB(confPath, unicode, dbimage, dlen, 1, DBFeature);

			// 이미지 정보
			String dbimageString = new String(Base64Util.encode(DBFeature, false));
			log.debug("IdentityOcrComponent facePrint [ DBFeature length ] : {}", dbimageString.length());
			retunJsonData.put("phothinfo", dbimageString);

			log.debug("IdentityOcrComponent facePrint [ extractScore ] : {}", extractScore);
			// 이미지 점수
			retunJsonData.put("extractScore", String.valueOf(extractScore));

		} catch (Exception e) {
			log.debug("IdentityOcrComponent retOcrDecipherResult [ ERROR Exception ] count : {}", e.getMessage());
			retunJsonData.put("extractScore", String.valueOf(extractScore));
		} finally {
			try {
				if (fp != null) {
					fp.jniFAEnd(); // 특징점 라이브러리 종료
				}
			} catch (Exception e) {
			}
			fp = null;
		}
		return retunJsonData;
	}

}
