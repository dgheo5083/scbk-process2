/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : FIDO 서버 통신 서비스 컴포넌트
*  작   성  자 : 이완주
*  작   성  일 : 2025.11.07
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2025.11.07/이완주)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.fido;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.initech.oppra.IniOPPRA;
import com.initech.oppra.IniOPPRAIllegalFormatException;
import com.initech.oppra.IniOPPRAReadException;
import com.initech.oppra.util.OppraMessageDataParser;
import com.initech.oppra.util.OppraSendDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.CertConfig;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.KftcCertComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "FIDO 서버 통신", description = "FIDO 서버 통신", author = "이완주")
@RequiredArgsConstructor
public class FidoHttpComponent {

	private final ISessionContextManager SessionManager; // 세션
	private final ICodeManager commonCode; // 공통코드
	private final CertUtils certUtils; // 인증서 공통 유틸
	private final FidoSequenceGenerator fidoSequenceGenerator; // 시퀀스
	private final KftcCertComponent kftcCertComponent; // 금결원인증서
	/**
	 * HTTP 통신
	 *
	 * @param jsonParam
	 * @return
	 * @throws Exception
	 */
	private JSONObject requestPost(JSONObject jsonParam) {
//		String domain = PropertiesUtils.getString("FIDO_SERVER_DOMAIN", "http://10.61.17.91:8890/fido/interfBiz/processRequest.do");
		// TODO 프라퍼티
		String domain = "https://127.0.0.1:9563/fido/interfBiz/processRequest.do";

		if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
			domain = "https://fido-ut.standardchartered.co.kr/fido/interfBiz/processRequest.do";
		}

		String siteId = PropertiesUtils.getString("FIDO_SITE_ID", "SIT01SCBANK000000000");
		String svcId = PropertiesUtils.getString("FIDO_SVC_ID", "SVC01SCBANK000000000");
		String timeout = PropertiesUtils.getString("FIDO_SERVER_TIMEOUT", "10000");

		log.debug("[fido http properties] domain={}, siteId={}, svcId={}, timeout={}", domain, siteId, svcId, timeout);
		log.debug("[fido http requestPost] jsonParam={}", jsonParam.toString());

		JSONObject requestBody = new JSONObject();
		requestBody.put("siteId", siteId);
		requestBody.put("svcId", svcId);
		for(String key:jsonParam.keySet()) {
			requestBody.put(key, jsonParam.get(key));
		}

		HttpResponse<String> response = null;
		try {
			// TODO 삭제// TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제// TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제
			TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						@Override
						public X509Certificate[] getAcceptedIssuers() {return null;}
						@Override
						public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
						@Override
						public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
					}
			};

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
			// TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제 // TODO 삭제

			HttpClient httpClient = HttpClient.newBuilder()
					.sslContext(sc)
					.build();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(domain))
					.header("Content-Type", "application/json")
					.timeout(Duration.ofMillis(Integer.parseInt(timeout)))
					.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), Charset.defaultCharset()))
					.build();

			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			log.debug("[fido http requestPost] response={}", response.toString());

			if (response.statusCode() != 200) {
				throw new PRCServiceException("PRCMNG30002", "FIDO 통신중 에러가 발생하였습니다.");
			}

 		} catch (Exception e) {
 			throw new PRCServiceException("PRCMNG30002", "FIDO 통신중 에러가 발생하였습니다.");

		} finally {
			System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "false");
		}

		return new JSONObject(response.body());
	}

	/**
	 * FIDO 지원 여부 체크 (device)
	 * @param verifyType
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 지원 여부 체크 (device)", description = "FIDO 지원 여부 체크 (device)", author = "이완주")
	public String allowedAuthnr(String verifyType) {
		log.debug("FIDO 지원 여부 체크 (device)");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("command", "allowedAuthnr");
		jsonParam.put("verifyType", verifyType);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO 지원 여부 체크 응답={}", responseBody.toString());

		StringBuilder returnSb = new StringBuilder();

		try {
			JSONObject resultData = (JSONObject) responseBody.optJSONObject("resultData", new JSONObject());
			JSONArray aaidAllowList = (JSONArray) resultData.optJSONArray("aaidAllowList", new JSONArray());

			aaidAllowList.forEach(obj -> {
				JSONObject item = (JSONObject) obj;
				returnSb.append(item.get("aaid")).append(",");
			});

		} catch (PRCServiceException csle) {
			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		return returnSb.toString().replaceAll(",$", ""); // {"aaidFinger": "0012#0007,0012#0021","aaidPin": "0012#0003,0012#0012"}
	}

	/**
	 * FIDO 등록상태 조회
	 * @param userId
	 * @param deviceId
	 * @param appId
	 * @param verifyType
	 * @param _input
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 등록상태 조회", description = "FIDO 등록상태 조회", author = "이완주")
	public Map<String, Object> checkRegisteredStatus(String userId, String deviceId, String appId, String verifyType, Map<String, Object> _input) {
		log.debug("FIDO 등록상태 조회");

		HashMap<String, Object> input = new HashMap<String, Object>();
		if (_input != null) {
			input.putAll(_input);
		}

		// 네이티브에서 Y로 올라온것만 발급된 인증서로 조회될것임. 다른 기기에서 조회하면 발급된 인증서 없는 것으로 나온다..
		log.debug("FIDO 등록상태 조회 ISSUE_BIO={}", input.getOrDefault("ISSUE_BIO", "N"));
		log.debug("FIDO 등록상태 조회 ISSUE_FACEID={}", input.getOrDefault("ISSUE_FACEID", "N"));
		log.debug("FIDO 등록상태 조회 ISSUE_PIN={}", input.getOrDefault("ISSUE_PIN", "N"));

		// 현재 발급받은 인증서 code
		Map<String, String> certCodeHashMap = (Map) input.get("CERTCODE_HASHMAP");
		if(certCodeHashMap == null) certCodeHashMap = new HashMap<String, String>();

		// 아이폰은 얼굴 또는 지문만 가능
		String faceDeviceYN = StringUtils.defaultIfBlank(SessionManager.getGlobalValue("faceDevice", String.class), "N");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("command", "checkRegisteredStatus");
		jsonParam.put("loginId", userId);
		jsonParam.put("deviceId", deviceId);
		jsonParam.put("appId", appId);
		jsonParam.put("verifyType", verifyType);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO 등록상태 조회 응답={}", responseBody.toString());

		if (!"000".equals(responseBody.optString("resultCode", ""))) {
			throw new PRCServiceException("PRCMNG30001", "디지털인증서 에러가 발생하였습니다.");
		}

		HashMap<String, Object> returnHashMap = new HashMap<String, Object>();

		try {

			JSONObject resultDataJSON = (JSONObject) responseBody.optJSONObject("resultData", new JSONObject());

			JSONObject aaidListJson = new JSONObject();
			JSONArray issuedCertListJsonArray = new JSONArray(); // 발급 인증서 정보
			JSONArray acceptedJsonArray = new JSONArray(); // 허용 인증 장치 목록
			JSONArray registeredJsonArray = new JSONArray(); // 등록된 인증 장치 목록
			JSONArray newIssuedCertListJsonArray = new JSONArray();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			if (resultDataJSON.get("aaidList") != null) {
				aaidListJson = (JSONObject) resultDataJSON.optJSONObject("aaidList", new JSONObject());
				// 허용 인증 장치 목록
				acceptedJsonArray = (JSONArray) aaidListJson.get("accepted");
				log.debug("허용 인증 장치 목록={}", acceptedJsonArray.toString());

				issuedCertListJsonArray = (JSONArray) resultDataJSON.optJSONArray("issuedCertList", new JSONArray());

				// 만료일 및 만료일수, 지문종류
				for (Object obj : issuedCertListJsonArray) {
					JSONObject issuedCertObj = (JSONObject) obj;

					// 등록된 인증 장치 목록
					registeredJsonArray = (JSONArray) aaidListJson.optJSONArray("registered", new JSONArray());

					for (Object obj2 : registeredJsonArray) {
						JSONObject registeredObj = (JSONObject) obj2;

						if (issuedCertObj.optString("aaid", "issuedCertObj").equals(registeredObj.optString("aaid", "registeredObj"))) {
							String verificationType = registeredObj.optString("verificationType");
							log.debug("verificationType={}, faceDeviceYN={}", verificationType, faceDeviceYN);

							if ("2".equals(verificationType) && "Y".equals(faceDeviceYN)) {
								issuedCertObj.put("verificationNm", commonCode.getCodeItem("MNG", "MNG009004"));
								issuedCertObj.put("icon_name", "face");
								issuedCertObj.put("verificationType", verificationType);

								// 본 정보는 앱에서 받아온 인증서 설치여부이다.
								// 아래에서 newIssuedCertListJsonArray 에 담을지 여부를 판단하게 된다.
								issuedCertObj.put("ISSUE_NAME", "ISSUE_FACEID"); // 앱스토리지 저장정보

								// 60V 에서 화면 리프레시 할때 넘겨받은 현재 발급한 인증서의 종류정보
								if ("Y".equals(certCodeHashMap.getOrDefault("FACEID", ""))) {
									issuedCertObj.put("CERT_COMPLETE", "Y"); // 발급화면(60V)에서 발급완료로 찍기 위해 사용
								}

							} else if ("2".equals(verificationType)) {
								issuedCertObj.put("verificationNm", commonCode.getCodeItem("MNG", "MNG009002"));
								issuedCertObj.put("icon_name", "finger");
								issuedCertObj.put("verificationType", verificationType);
								issuedCertObj.put("ISSUE_NAME", "ISSUE_BIO"); // 앱스토리지 저장정보

								// 60V 에서 화면 리프레시 할때 넘겨받은 현재 발급한 인증서의 종류정보
								if ("Y".equals(certCodeHashMap.getOrDefault("BIO", ""))) {
									issuedCertObj.put("CERT_COMPLETE", "Y"); // 발급화면(60V)에서 발급완료로 찍기 위해 사용
								}

							} else if ("4096".equals(verificationType)) {
								issuedCertObj.put("verificationNm", commonCode.getCodeItem("MNG", "MNG009003"));
								issuedCertObj.put("icon_name", "eye");
								issuedCertObj.put("verificationType", verificationType);
								issuedCertObj.put("ISSUE_NAME", "ISSUE_BIONIC"); // 앱스토리지 저장정보

								// 60V 에서 화면 리프레시 할때 넘겨받은 현재 발급한 인증서의 종류정보
								if ("Y".equals(certCodeHashMap.getOrDefault("BIONIC", ""))) {
									issuedCertObj.put("CERT_COMPLETE", "Y"); // 발급화면(60V)에서 발급완료로 찍기 위해 사용
								}

							} else if ("16384".equals(verificationType)) {
								issuedCertObj.put("verificationNm", commonCode.getCodeItem("MNG", "MNG009001"));
								issuedCertObj.put("icon_name", "pin");
								issuedCertObj.put("verificationType", verificationType);
								issuedCertObj.put("ISSUE_NAME", "ISSUE_PIN"); // 앱스토리지 저장정보

								// 60V 에서 화면 리프레시 할때 넘겨받은 현재 발급한 인증서의 종류정보
								if ("Y".equals(certCodeHashMap.getOrDefault("PIN", ""))) {
									issuedCertObj.put("CERT_COMPLETE", "Y"); // 발급화면(60V)에서 발급완료로 찍기 위해 사용
								}

							}

						}
					} // end-for

					String notAfter = DateUtils.parseToDateTime(issuedCertObj.optString("notAfter", "").toString()).format(formatter); // 만료일

					int dateCompare = DateUtils.compare(notAfter.replaceAll("-", ""));
					String expiredYn = dateCompare == 0 ? "Y" : "N";

					long remindDays = DateUtils.getDayBetween(DateUtils.getCurrentDate("yyyyMMdd"), notAfter.replaceAll("-", ""));
					String notAfterYn = (remindDays < 31) ? "Y" : "N"; // 만료여부, 만료일이 현재일에서 30일 이하면 Y

					issuedCertObj.put("notAfter", notAfter);
					issuedCertObj.put("expiredYN", expiredYn);
					issuedCertObj.put("remindDays", remindDays);
					issuedCertObj.put("notAfterYN", notAfterYn);

					log.debug("issuedCert={}", issuedCertObj.toString());

					String issueName = (String) issuedCertObj.optString("ISSUE_NAME", "");
					if (!"".equals(issueName) && "Y".equals(input.getOrDefault(issueName, ""))) {
						newIssuedCertListJsonArray.put(issuedCertObj);
						log.debug("newIssuedCertListJsonArray.push={}", newIssuedCertListJsonArray.toString());
					}
				} // end-for
			}

			if (resultDataJSON.get("regYn") != null) {
				returnHashMap.put("regYn", resultDataJSON.get("regYn"));
			}
			returnHashMap.put("acceptedSize", acceptedJsonArray.length());
			returnHashMap.put("acceptedJsonArray", acceptedJsonArray);
			returnHashMap.put("acceptedJSONString", acceptedJsonArray.toString());
			returnHashMap.put("registeredSize", registeredJsonArray.length());
			returnHashMap.put("registeredJsonArray", registeredJsonArray);
			returnHashMap.put("registeredJSONString", registeredJsonArray.toString());
			returnHashMap.put("issuedCertListSize", newIssuedCertListJsonArray.length());
			returnHashMap.put("issuedCertListJsonArray", newIssuedCertListJsonArray);
			returnHashMap.put("issuedCertListJSONString", newIssuedCertListJsonArray.toString());

//			"acceptedJsonString": "[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]"
//			"registeredJsonString": "[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"cl4mT1Lc958l2Bq44awqPzmiRpa75iFkjs6jKZAKEN4\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"ITcgBDqhqIZ2pn-721lo9HSg7hVF_2I1CIAyafyUSLk\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]"
//			"issuedCertListJsonString": "[{\"notAfter\":\"2026-05-07\",\"serialNumber\":\"5108182\",\"ISSUE_NAME\":\"ISSUE_PIN\",\"remindDays\":30,\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"notAfterYN\":\"Y\",\"keyId\":\"cl4mT1Lc958l2Bq44awqPzmiRpa75iFkjs6jKZAKEN4\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"icon_name\":\"pin\",\"issueType\":\"03\",\"verificationNm\":\"간편번호인증\",\"expiredYN\":\"N\",\"aaid\":\"0012#0003\",\"verificationType\":\"16384\",\"subjectDn\":\"CN=업무테스트()002344720251229123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"},{\"notAfter\":\"2026-05-07\",\"serialNumber\":\"5108184\",\"ISSUE_NAME\":\"ISSUE_BIO\",\"remindDays\":30,\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"notAfterYN\":\"Y\",\"keyId\":\"ITcgBDqhqIZ2pn-721lo9HSg7hVF_2I1CIAyafyUSLk\",\"oid\":\"1.2.410.200005.1.1.4.3\",\"icon_name\":\"finger\",\"issueType\":\"03\",\"verificationNm\":\"지문인증\",\"expiredYN\":\"N\",\"aaid\":\"0012#0021\",\"verificationType\":\"2\",\"subjectDn\":\"CN=업무테스트()002334720260316123000001, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}]"



//		} catch (PRCServiceException csle) {
//			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		log.debug("FIDO 등록 상태={}", returnHashMap.toString());
		return returnHashMap;
	}

	/**
	 * FIDO 인증서 발급
	 * @param refNum
	 * @param appCode
	 * @param deviceId
	 * @param appId
	 * @param verifyType
	 * @param issueType
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 인증서 발급", description = "FIDO 인증서 발급", author = "이완주")
	public Map<String, Object> requestIssueCert(String refNum, String appCode, String deviceId, String appId, String verifyType, String issueType) {
		log.debug("FIDO 인증서 발급");
		JSONObject jsonParam = new JSONObject();
		JSONObject ownerInfoJsonParam = new JSONObject();

		jsonParam.put("command", "requestIssueCert");
		jsonParam.put("bizReqType", "pc");
		UUID uuid = UUID.randomUUID();
		String svcTrId = Long.toString(uuid.getMostSignificantBits()).replaceAll("-", "");
		jsonParam.put("svcTrId", svcTrId);
		jsonParam.put("loginId", SessionManager.getGlobalValue("UserID", String.class));
		jsonParam.put("deviceId", SessionManager.getGlobalValue("deviceId", String.class));
		jsonParam.put("appId", SessionManager.getGlobalValue("appId", String.class));
		jsonParam.put("packageNms", "");
		jsonParam.put("verifyType", verifyType); // 인증 방식 "pin":"16384", "bio":"2", "bio-spass":"2048", "faceId":"256", "bionic":"4096"
		jsonParam.put("issueType", issueType); // 01 : S-PASS, 03 : 바이오인증서
		jsonParam.put("refNumber", refNum);
		jsonParam.put("authCode", appCode);
		ownerInfoJsonParam.put("hpNum", SessionManager.getGlobalValue("phoneNo", String.class));
		ownerInfoJsonParam.put("uName", SessionManager.getGlobalValue("DeptPersonName", String.class));
		ownerInfoJsonParam.put("uBirth", SessionManager.getGlobalValue("PerBusNo", String.class).substring(0, 6));
		String sex = SessionManager.getGlobalValue("PerBusNo", String.class).substring(6, 7);
		ownerInfoJsonParam.put("uSex", (sex.equals("1") || sex.equals("3") || sex.equals("5")) ? "1" : "0");
		ownerInfoJsonParam.put("foreigner", (sex.equals("5") || sex.equals("6")) ? "1" : "0");
		jsonParam.put("ownerInfo", ownerInfoJsonParam);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO인증서 발급 응답={}", responseBody.toString());

		if (!"000".equals(responseBody.optString("resultCode", ""))) {
			throw new PRCServiceException("PRCMNG30004", "인증서 발급 중 에러가 발생하였습니다.");
		}

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {

			JSONObject resultDataJSON = (JSONObject) responseBody.optJSONObject("resultData", new JSONObject());

			returnMap.put("resultCode", responseBody.get("resultCode"));
			returnMap.put("resultMsg", responseBody.get("resultMsg"));
			returnMap.put("trId", resultDataJSON.get("trId"));
			returnMap.put("svcTrId", svcTrId);

//		} catch (PRCServiceException csle) {
//			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		return returnMap;
	}

	/**
	 * FIDO 서비스 가입
	 * @param deviceId
	 * @param appId
	 * @param verifyType
	 * @param issueType
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 서비스 가입", description = "FIDO 서비스 가입", author = "이완주")
	public Map<String, Object> requestServiceRegist(String deviceId, String appId, String verifyType, String issueType) {
		log.debug("FIDO 서비스 가입");
		String phoneNo1 = SessionManager.getLoginValue("HPOne", String.class);
		String phoneNo2 = SessionManager.getLoginValue("HPTwo", String.class);
		String phoneNo3 = SessionManager.getLoginValue("HPThree", String.class);
		String phoneNo = phoneNo1 + phoneNo2 + phoneNo3;

		JSONObject jsonParam = new JSONObject();
		JSONObject ownerInfoJsonParam = new JSONObject();

		jsonParam.put("command", "requestServiceRegist");
		jsonParam.put("bizReqType", "pc");
		UUID uuid = UUID.randomUUID();
		String svcTrId = Long.toString(uuid.getMostSignificantBits()).replaceAll("-", "");
		jsonParam.put("svcTrId", svcTrId);

		if (SessionManager.isLogin()) {
			jsonParam.put("loginId", SessionManager.getLoginValue("UserID", String.class));
		} else {
			jsonParam.put("loginId", SessionManager.getGlobalValue("UserID", String.class));
		}
		jsonParam.put("deviceId", SessionManager.getGlobalValue("deviceId", String.class));
		jsonParam.put("appId", SessionManager.getGlobalValue("appId", String.class));
		jsonParam.put("packageNms", "");
		jsonParam.put("verifyType", verifyType); // 인증 방식 "pin":"16384", "bio":"2", "bio-spass":"2048", "faceId":"256", "bionic":"4096"

		String sex = "";

		if (SessionManager.isLogin()) {
			ownerInfoJsonParam.put("hpNum", phoneNo);
			ownerInfoJsonParam.put("uName", SessionManager.getLoginValue("CustName", String.class));
			ownerInfoJsonParam.put("uBirth", SessionManager.getLoginValue("PerBusNo", String.class).substring(0, 6));
			sex = SessionManager.getLoginValue("PerBusNo", String.class).substring(6, 7);

		} else {
			ownerInfoJsonParam.put("hpNum", SessionManager.getGlobalValue("phoneNo", String.class));
			ownerInfoJsonParam.put("uName", SessionManager.getGlobalValue("DeptPersonName", String.class));
			ownerInfoJsonParam.put("uBirth", SessionManager.getGlobalValue("PerBusNo", String.class).substring(0, 6));
			sex = SessionManager.getGlobalValue("PerBusNo", String.class).substring(6, 7);
		}
		ownerInfoJsonParam.put("uSex", (sex.equals("1") || sex.equals("3") || sex.equals("5")) ? "1" : "0");
		ownerInfoJsonParam.put("foreigner", (sex.equals("5") || sex.equals("6")) ? "1" : "0");
		jsonParam.put("ownerInfo", ownerInfoJsonParam);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO서비스 가입 응답={}", responseBody.toString());

		if (!"000".equals(responseBody.optString("resultCode", ""))) {
			throw new PRCServiceException("PRCMNG30004", "FIDO 서비스 등록 중 에러가 발생하였습니다.");
		}

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {

			JSONObject resultDataJSON = (JSONObject) responseBody.optJSONObject("resultData", new JSONObject());

			returnMap.put("resultCode", responseBody.get("resultCode"));
			returnMap.put("resultMsg", responseBody.get("resultMsg"));
			returnMap.put("trId", resultDataJSON.get("trId"));
			returnMap.put("svcTrId", svcTrId);

//		} catch (PRCServiceException csle) {
//			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		return returnMap;
	}

	/**
	 * FIDO 인증서 해지
	 * @param deviceId
	 * @param appId
	 * @param verifyType
	 * @param issueType
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 인증서 해지", description = "FIDO 인증서 해지", author = "이완주")
	public Map<String, Object> requestRevokeCert(String deviceId, String appId, String verifyType, String issueType) {
		log.debug("FIDO 인증서 해지");

		JSONObject jsonParam = new JSONObject();
		JSONObject ownerInfoJsonParam = new JSONObject();
		jsonParam.put("command", "requestRevokeCert");
		jsonParam.put("bizReqType", "pc");
		UUID uuid = UUID.randomUUID();
		String svcTrId = Long.toString(uuid.getMostSignificantBits()).replaceAll("-", "");
		jsonParam.put("svcTrId", svcTrId);
		jsonParam.put("loginId", SessionManager.getGlobalValue("UserID", String.class));
		jsonParam.put("deviceId", SessionManager.getGlobalValue("deviceId", String.class));
		jsonParam.put("appId", SessionManager.getGlobalValue("appId", String.class));
		jsonParam.put("packageNms", "");
		jsonParam.put("verifyType", verifyType); // 인증 방식 "pin":"16384", "bio":"2", "bio-spass":"2048", "faceId":"256",
													// "bionic":"4096"
		jsonParam.put("issueType", issueType); // 01 : S-PASS, 03 : 바이오인증서
		ownerInfoJsonParam.put("hpNum", SessionManager.getGlobalValue("phoneNo", String.class));
		ownerInfoJsonParam.put("uName", SessionManager.getGlobalValue("DeptPersonName", String.class));
		ownerInfoJsonParam.put("uBirth", SessionManager.getGlobalValue("PerBusNo", String.class).substring(0, 6));
		String sex = SessionManager.getGlobalValue("PerBusNo", String.class).substring(6, 7);
		ownerInfoJsonParam.put("uSex", (sex.equals("1") || sex.equals("3") || sex.equals("5")) ? "1" : "0");
		ownerInfoJsonParam.put("foreigner", (sex.equals("5") || sex.equals("6")) ? "1" : "0");
		jsonParam.put("ownerInfo", ownerInfoJsonParam);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO 인증서 해지 응답={}", responseBody.toString());

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {

			JSONObject resultDataJSON = (JSONObject) responseBody.get("resultData");

			returnMap.put("resultCode", responseBody.get("resultCode"));
			returnMap.put("resultMsg", responseBody.get("resultMsg"));

			if ("000".equals(responseBody.optString("resultCode", ""))) {
				returnMap.put("trId", resultDataJSON.get("trId"));
			}

//		} catch (PRCServiceException csle) {
//			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		return returnMap;
	}

	/**
	 * FIDO 서비스 해지
	 * @param deviceId
	 * @param appId
	 * @param verifyType
	 * @param issueType
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 서비스 해지", description = "FIDO 서비스 해지", author = "이완주")
	public Map<String, Object> requestServiceRelease(String deviceId, String appId, String verifyType, String issueType) {
		log.debug("FIDO 서비스 해지");

		JSONObject jsonParam = new JSONObject();
		JSONObject ownerInfoJsonParam = new JSONObject();
		jsonParam.put("command", "requestServiceRelease");
		jsonParam.put("bizReqType", "pc");
		UUID uuid = UUID.randomUUID();
		String svcTrId = Long.toString(uuid.getMostSignificantBits()).replaceAll("-", "");
		jsonParam.put("svcTrId", svcTrId);
		jsonParam.put("loginId", SessionManager.getGlobalValue("UserID", String.class));
		jsonParam.put("deviceId", SessionManager.getGlobalValue("deviceId", String.class));
		jsonParam.put("appId", SessionManager.getGlobalValue("appId", String.class));
		jsonParam.put("packageNms", "");
		jsonParam.put("verifyType", verifyType); // 인증 방식 "pin":"16384", "bio":"2", "bio-spass":"2048", "faceId":"256", "bionic":"4096"
		jsonParam.put("issueType", issueType); // 01 : S-PASS, 03 : 바이오인증서
		ownerInfoJsonParam.put("hpNum", SessionManager.getGlobalValue("phoneNo", String.class));
		ownerInfoJsonParam.put("uName", SessionManager.getGlobalValue("DeptPersonName", String.class));
		ownerInfoJsonParam.put("uBirth", SessionManager.getGlobalValue("PerBusNo", String.class).substring(0, 6));
		String sex = SessionManager.getGlobalValue("PerBusNo", String.class).substring(6, 7);
		ownerInfoJsonParam.put("uSex", (sex.equals("1") || sex.equals("3") || sex.equals("5")) ? "1" : "0");
		ownerInfoJsonParam.put("foreigner", (sex.equals("5") || sex.equals("6")) ? "1" : "0");
		jsonParam.put("ownerInfo", ownerInfoJsonParam);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO 서비스 해지 응답={}", responseBody.toString());

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {

			JSONObject resultDataJSON = (JSONObject) responseBody.get("resultData");

			returnMap.put("resultCode", responseBody.get("resultCode"));
			returnMap.put("resultMsg", responseBody.get("resultMsg"));

			if ("000".equals(responseBody.optString("resultCode", ""))) {
				returnMap.put("trId", resultDataJSON.get("trId"));
			}

//		} catch (PRCServiceException csle) {
//			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		return returnMap;
	}

	/**
	 * FIDO 거래결과확인
	 * @param svcTrId
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 거래결과확인", description = "FIDO 거래결과확인", author = "이완주")
	public Map<String, Object> trResultConfirm(String svcTrId) {
		log.debug("FIDO 거래결과확인");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("command", "trResultConfirm");
		jsonParam.put("svcTrId", svcTrId);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO 거래결과확인 응답={}", responseBody.toString());

		if (!"000".equals(responseBody.optString("resultCode", ""))) {
			throw new PRCServiceException("PRCMNG30005", "인증서 발급 확인 중 에러가 발생하였습니다.");
		}

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		try {
			JSONObject resultDataJSON = (JSONObject) responseBody.get("resultData");
			returnMap.put("resultCode", responseBody.get("resultCode"));
			returnMap.put("resultMsg", responseBody.get("resultMsg"));
			returnMap.put("resultData", resultDataJSON.toString());

//		} catch (PRCServiceException csle) {
//			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		return returnMap;
	}

	/**
	 * FIDO 등록상태 조회2 - DeviceID 정보 추출목적
	 * @param userId
	 * @param deviceId
	 * @param appId
	 * @param verifyType
	 * @param _input
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 등록상태 조회2 - DeviceID 정보 추출목적", description = "FIDO 등록상태 조회2 - DeviceID 정보 추출목적", author = "이완주")
	public Map<String, Object> checkRegisteredStatus2(String userId, String deviceId, String appId, String verifyType, HashMap<String, Object> _input) {
		log.debug("FIDO 등록상태 조회2");

		HashMap<String, Object> returnHashMap = new HashMap<String, Object>();

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("command", "checkRegisteredStatus2");
		jsonParam.put("loginId", userId);
		jsonParam.put("appId", appId);
		jsonParam.put("verifyType", verifyType);

		JSONObject responseBody = this.requestPost(jsonParam); // FIDO서버 응답 획득
		log.debug("FIDO 등록상태 조회2 응답={}", responseBody.toString());

		JSONObject resultJson = new JSONObject();
		JSONArray resultJsonArray = new JSONArray();

		try {
			JSONObject resultDataJSON = (JSONObject) responseBody.get("resultData");
			JSONArray registeredListJsonArray = (JSONArray) resultDataJSON.optJSONArray("registeredList", new JSONArray());

			for (Object obj : registeredListJsonArray) {

				JSONObject registeredObj = (JSONObject) obj;
				JSONArray deviceListJsonArray = (JSONArray) registeredObj.optJSONArray("deviceList", new JSONArray());

				for (Object obj2 : deviceListJsonArray) {

					JSONObject deviceObj = (JSONObject) obj2;

					JSONArray aaidList = (JSONArray) deviceObj.optJSONArray("aaidList", new JSONArray());
					JSONArray certList = (JSONArray) deviceObj.optJSONArray("certList", new JSONArray());

					List<JSONObject> aaidArrayList = IntStream.range(0,  aaidList.length()).mapToObj(aaidList::getJSONObject).collect(Collectors.toList());
					List<JSONObject> certArrayList = IntStream.range(0,  certList.length()).mapToObj(certList::getJSONObject).collect(Collectors.toList());
					aaidArrayList.stream().forEach(aaidObj -> {
						JSONObject registeredJsonObj = new JSONObject(); // 반환객체
						registeredJsonObj.put("deviceId", deviceObj.opt("deviceId"));
						registeredJsonObj.put("aaid", aaidObj.opt("aaid"));
						registeredJsonObj.put("keyId", aaidObj.opt("keyId"));
						registeredJsonObj.put("verificationType", aaidObj.opt("verificationType"));

						certArrayList.stream()
								.filter(f -> f.opt("keyId").toString().equalsIgnoreCase(aaidObj.opt("keyId").toString()))
								.findFirst()
								.ifPresentOrElse(p -> registeredJsonObj.put("serialNumber", p.get("keyId")), () -> registeredJsonObj.put("serialNumber", ""));

						synchronized (resultJsonArray) {
							resultJsonArray.put(registeredJsonObj); // 반환목록에 추가
						}
					});

				} // end-for:deviceListJsonArray

			} // end-for:registeredListJsonArray

			resultJson.put("registHistoryList", resultJsonArray); // 여러 형태로 반환하는듯?

		} catch (PRCServiceException csle) {
			throw csle;
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30003", "디지털인증서 전문 분석 중 에러가 발생하였습니다.");
		}

		returnHashMap.put("registeredJsonArray", resultJsonArray);
		returnHashMap.put("registeredJsonString", resultJson.toString());
		return returnHashMap;
	}

	/**
	 * 디지털인증서 발급
	 * @param certPolicyCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ComponentOperation(name = "디지털인증서 발급", description = "디지털인증서 발급", author = "이완주")
	public Map<String, Object> getRaCert(String certPolicyCode) {
		log.debug("디지털인증서 발급 시작");

		HashMap<String, Object> returnMap = new HashMap<String, Object>();

		String IssueGubun = "1"; // 발급구분, 신규:1, 재발급:2

		Map<String, Object> certIssueSession = (Map<String, Object>) SessionManager.getGlobalValue("certIssueSession", Map.class);

		IniOPPRA oppra = null;

		String refNum = "";
		String appCode = "";

		HashMap<String, Object> raMap = new HashMap<String, Object>();
		raMap.put("MANAGERID", CertConfig.ADID); // 운영자아아디. OPTIONAL 속성
		raMap.put("USERCODE", "1"); // 가입자구분코드 "1" 개인 "2" 기업
		raMap.put("OU_NAME", ""); // 개인은 법인명이 OPTIONAL
		raMap.put("CN_NAME", certIssueSession.getOrDefault("CN_NAME", "")); // 개인명, 법인/단체 세부명. "()"가 뒤에 포함되어야함. ex:)"홍길동()" 기업일 경우 "이니텍(INI)" ()안에 법인명이 들어감
		raMap.put("IDNO", certIssueSession.getOrDefault("PerBusNo", "")); // 주민(사업자)등록번호 **
		raMap.put("USERID", certIssueSession.getOrDefault("UserID", "")); // 사이트아이디 **
		raMap.put("SERVICEPROVIDER", "01"); // 01:금결원OPP(또는 범용게이트웨이), 02:타기관직접연동
		raMap.put("CACODE", "01"); // 01:금결원 02:SignKorea 03:전자인증
		raMap.put("CERTCODE", certPolicyCode); // 홍채/24, 지문/34, PIN/44
		raMap.put("EMAIL", (String)certIssueSession.get("EMAIL")); //이메일 **
		raMap.put("HANDPHONE", (String)certIssueSession.get("PHONE")); //휴대폰번호
		raMap.put("FAX", "02-2140-3699"); // 팩스
		raMap.put("STATISTICSCODE", "000386"); // for Advance version
		raMap.put("RESERVATION5", "0");

		String sendMsg = "";
		OppraMessageDataParser odp = null;

		try {
			oppra = certUtils.getRegOppra();

			String oppraResponse = "";
			if ("1".equals(IssueGubun)) { // 신규
				OppraSendDataParser oppraSendDataParser = new OppraSendDataParser("20", raMap);
				sendMsg = oppraSendDataParser.getSendLastData();
				log.debug("디지털인증서 신규요청={}", sendMsg);
				oppraResponse = oppra.requestRAW(20, sendMsg);

				if (oppraResponse.startsWith("999")) {
					log.debug("##authcenter 지연 ---------");
					Thread.sleep(7000);
					log.debug("##authcenter 지연 =========");

					oppra = certUtils.getRegOppra();
					oppraSendDataParser = new OppraSendDataParser("20", raMap);
					sendMsg = oppraSendDataParser.getSendLastData();

					log.debug("##authcenter [신규 요청시 해지처리 지연으로 인한 에러 발생으로 재요청 처리 추가]");
					oppraResponse = oppra.requestRAW(20, sendMsg);
				}

				odp = new OppraMessageDataParser("20", oppra.getResDataPart());
				odp.setLoopData(oppra.getResCommonPart(), oppra.getResDataPart());

			} else if ("2".equals(IssueGubun)) { // 재발급
				OppraSendDataParser oppraSendDataParser = new OppraSendDataParser("25", raMap);
				sendMsg = oppraSendDataParser.getSendLastData();
				log.debug("디지털인증서 재발급요청={}", sendMsg);
				oppraResponse = oppra.requestRAW(25, sendMsg);
				odp = new OppraMessageDataParser("25", oppra.getResDataPart());
				odp.setLoopData(oppra.getResCommonPart(), oppra.getResDataPart());
			}

		} catch (IniOPPRAReadException e) {
			throw new PRCServiceException("PRCCRT201121", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
//		} catch (IniOPPRASystemException e) {
//			throw new PRCServiceException("PRCCRT201122", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException("PRCCRT201123", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (PRCServiceException e) {
			throw new PRCServiceException("PRCCRT201124", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} catch (Exception e) {
			throw new PRCServiceException("PRCCRT201125", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} finally {
			certUtils.closeOppra(oppra);
		}

		if(odp == null) {
			throw new PRCServiceException("PRCCRT201124", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		}
		String errMsg = odp.getCodeData("RESMSG");

		log.debug("##authcenter [errMsg][" + errMsg + "]");
		log.debug("##authcenter [getResCode][" + odp.getResCode() + "]");

		if ("000".equals(odp.getResCode())) {
			refNum = odp.getCodeData("REFNUM");
			appCode = odp.getCodeData("APPCODE");

		} else if (odp.getResCode().equals("820")) { // 공인인증에러 추가
			// 정보통신부의 정책에 따라 금융결제원 범용 인증서 발급이 허용되지 않습니다
			log.debug("##authcenter hostSendAndCheckSSR : 정보통신부의 정책에 따라 금융결제원 범용 인증서 발급이 허용되지 않습니다.");
			throw new PRCServiceException("PRCCRT20142", "정보통신부의 정책에 따라 금융결제원 범용 인증서 발급이 허용되지 않습니다.");

		} else if (odp.getResCode().equals("216")) { // 응답코드 216으로 인한 에러처리 : ID상이로 인한 에러 (92번 전문처리 또는 강제폐기 시킬것)
			// [00은행]에서 등록 및 미발급 상태 : ID변경으로 인한 ID상이
			log.debug("##authcenter hostSendAndCheckSSR :[00은행]에서 등록 및 미발급 상태 : ID변경으로 인한 ID상이.");
			// String[] arguments = new String[] { errMsg };
			// throw new PRCServiceException("PRCCRT20118", "고객의 인증서는 {0}로 남아 있습니다. 해당 은행의
			// 인터넷뱅킹 웹사이트에서 공인인증서를 발급받으신 후 사용하시기 바랍니다. 공인인증 등록기관 관리자에게 문의하시기
			// 바랍니다.",arguments);
			throw new PRCServiceException("PRCCRT20118", "고객의 인증서는 " + errMsg + "로 남아 있습니다. 해당 은행의 인터넷뱅킹 웹사이트에서 공인인증서를 발급받으신 후 사용하시기 바랍니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");

		} else if (odp.getResCode().equals("922") || odp.getResCode().equals("926") || odp.getResCode().equals("927")) {
			// 응답코드 922으로 인한 처리 : 갱신 등록 후 미발급 상태일때 (82번 전문처리해서 등록정보 강제 삭제)
			// 가입자 갱신 등록 후, 미발급 사용자 입니다.

			// ---------- 등록정보 강제 삭제 시작 ----------==>
			oppra = certUtils.getOppra();

			String PolicyCode = certPolicyCode;
			String JuminNum = SessionManager.getGlobalValue("PerBusNo", String.class);

			StringBuilder infoDeleteMsg = new StringBuilder();
			infoDeleteMsg.append(CertConfig.RegCodeValue).append("$");
			infoDeleteMsg.append(PolicyCode).append("$").append(JuminNum).append("$");
			infoDeleteMsg.append(CertConfig.RegCodeValue).append(SessionManager.getGlobalValue("UserID", String.class)).append("$");

			try {
				oppra.requestRAW(82, infoDeleteMsg.toString());
			} catch (IniOPPRAReadException e) {
				log.error("requestRAW 82 error {}", e);
			} catch (IniOPPRAIllegalFormatException e) {
				log.error("requestRAW 82 error {}", e);
			}

			OppraMessageDataParser odp2 = new OppraMessageDataParser(82, oppra.getResDataPart());
			if (odp2.getResCode().equals("000")) {
			} else {
				log.debug("##authcenter 사용자 등록정보 강제 삭제 실패. [" + infoDeleteMsg.toString() + "]");
			}

			certUtils.closeOppra(oppra);
			// ---------- 등록정보 강제 삭제 끝 ----------

			// ---------- 인증서 발급 프로세스 시작 ----------
			oppra = certUtils.getRegOppra();

			if (IssueGubun.equals("1")) { // 신규이면......
				try {
					oppra.requestRAW(20, sendMsg);
				} catch (IniOPPRAReadException e) {
				} catch (IniOPPRAIllegalFormatException e) {
				}
				odp = new OppraMessageDataParser("20", oppra.getResDataPart());

			} else if (IssueGubun.equals("2")) { // 재발급이면..
				try {
					oppra.requestRAW(25, sendMsg);
				} catch (IniOPPRAReadException e) {
					log.error("requestRAW 25 error {}", e);
				} catch (IniOPPRAIllegalFormatException e) {
					log.error("requestRAW 25 error {}", e);
				}
				odp = new OppraMessageDataParser("25", oppra.getResDataPart());
			}
			odp.setLoopData(oppra.getResCommonPart(), oppra.getResDataPart());

			if (odp.getResCode().equals("000")) {
				refNum = odp.getCodeData("REFNUM");
				appCode = odp.getCodeData("APPCODE");
			} else {
				certUtils.closeOppra(oppra);
				String errMsg2 = odp.getCodeData("RESMSG");

				throw new PRCServiceException(odp.getResCode(), errMsg2);
			}
			certUtils.closeOppra(oppra);

		} else if (odp.getResCode().equals("217")) {
			throw new PRCServiceException("PRCCRT20116", "고객께서는 이미 인증서를 발급받으셨으며 현재 효력정지 상태입니다. 효력회복을 하셔서 사용하시기 바랍니다.");
		} else if (odp.getResCode().equals("214") || odp.getResCode().equals("210")) {
			// String[] arguments = new String[] { errMsg };
			// throw new PRCServiceException("PRCCRT20114", "{0} 금융결제원에서 발행하는 공인인증서는 금융기관중
			// 한곳에서만 발급받으실 수 있습니다. 1. 당행에서 발급 받으셨다면 인증서 재발급 메뉴를, 2. 타행에서 발급 받으셨다면 타기관 인증서 등록
			// 메뉴를, 3. 지점에서 이용자번호를 새로 신규하셨으면 기존 인증서 폐기를 먼저하시기 바랍니다.",arguments);
			throw new PRCServiceException("PRCCRT20114", errMsg + "금융결제원에서 발행하는 공인인증서는 금융기관중 한곳에서만 발급받으실 수 있습니다. 1. 당행에서 발급 받으셨다면 인증서 재발급 메뉴를, 2. 타행에서 발급 받으셨다면 타기관 인증서 등록 메뉴를, 3. 지점에서 이용자번호를 새로 신규하셨으면 기존 인증서 폐기를 먼저하시기 바랍니다.");
		} else if (odp.getResCode().equals("211")) {
			throw new PRCServiceException("PRCCRT20117", "가입자 등록에 실패하였습니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		} else if (odp.getResCode().equals("999")) {
			throw new PRCServiceException(odp.getResCode(), errMsg);
		} else {
			throw new PRCServiceException(odp.getResCode(), errMsg);
		}

		returnMap.put("refNum", refNum);
		returnMap.put("appCode", appCode);

		log.debug("디지털인증서 발급 종료={}", returnMap.toString());

		return returnMap;
	}

	/**
	 * 디지털인증서 폐기
	 * @param certPolicy
	 * @param PerBusNo
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "디지털인증서 폐기", description = "디지털인증서 폐기", author = "이완주")
	public Map<String, Object> RACertDelete(String certPolicy, String PerBusNo) {
		log.debug("디지털인증서 폐기 시작");

		String serialValue = "";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, String> deleteCertReturn = null;

		String resCode = "";
		String resMsg = "";

		try {
			// 정책코드로 등록된 인증서 시리얼 조회
			serialValue = certUtils.getSerialNumByRegnum(certPolicy, PerBusNo);
		} catch (Exception e) {
			throw new PRCServiceException("PRCMNG30006", "인증서 조회중 에러가 발생하였습니다.");
		}

		try {
			// RA 인증서 폐기
			deleteCertReturn = kftcCertComponent.changeStatusCert(serialValue, certPolicy, "30");
			if (deleteCertReturn == null) {
				throw new PRCServiceException("PRCCRT20213", "공인인증 등록기관 시스템 점검중으로 고객의 인증서를 폐기 할 수 없습니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");

			} else {
				resCode = deleteCertReturn.get("ResCode");
				resMsg = deleteCertReturn.get("ResMsg");

				returnMap.put("resCode", resCode);
				returnMap.put("resMsg", resMsg);
			}

		} catch (Exception e) {
			throw new PRCServiceException("PRCCRT20213", "공인인증 등록기관 시스템 점검중으로 고객의 인증서를 폐기 할 수 없습니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다. : " + e.getMessage());
		}

		log.debug("디지털인증서 폐기 종료:{}", returnMap.toString());
		return returnMap;
	}

	/**
	 * FIDO svcTrID 생성
	 */
	@ComponentOperation(name = "FIDO svcTrID 생성", description = "FIDO svcTrID 생성", author = "이완주")
	public String getFidoSvctrId() {
//		String serverId = SystemUtils.getInstanceId().substring(3); // 4
		// TODO 서버식별자 필요
		// TODO 서버식별자 필요
		// TODO 서버식별자 필요
		String serverId = "9999";
		// TODO 서버식별자 필요
		// TODO 서버식별자 필요
		// TODO 서버식별자 필요
		String serviceDate = DateUtils.getCurrentDate("yyMMdd");
		String serviceTime = DateUtils.getCurrentDate("HHmmss");

		StringBuilder nextUUID = new StringBuilder();
		nextUUID.append(serviceDate).append(serviceTime).append(fidoSequenceGenerator.next()); // yyMMdd(6) + HHmmss(6) + seq(4) = 16

		return StringUtils.rpad((serverId + nextUUID.toString()), 20, '0');// 0101 260602 175754 2100
	}

	/**
	 * 2.9. 등록허용 인증장치 목록 조회
	 *
	 * @param jsonParam
	 * @return
	 */
	@ComponentOperation(name = "등록허용 인증장치 목록 조회", description = "등록허용 인증장치 목록 조회", author = "이완주")
	public Map<String, Object> allowedAuthnr(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		jsonParam.put("command", "allowedAuthnr");

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		if (jsonResult != null) {
			JSONObject resultData = jsonResult.optJSONObject("resultData");
			if (resultData != null) {
				JSONArray aaidAllowList = resultData.optJSONArray("aaidAllowList");
				List<Map<String, Object>> aaidAllowListMap = new ArrayList<Map<String, Object>>();
				if (aaidAllowList != null) {
					Map<String, Object> aaidItem = new HashMap<String, Object>();
					for (int i = 0; i < aaidAllowList.length(); i++) {
						aaidItem = new HashMap<String, Object>();
						JSONObject item = (JSONObject) aaidAllowList.get(i);
						aaidItem.put("aaid", item.get("aaid")); // AAID
						aaidItem.put("verificationType", item.get("verificationType")); // 검증타입
						aaidItem.put("verificationNm", item.get("verificationNm")); // 검증타입명
						aaidItem.put("vendorNm", item.get("vendorNm")); // 인증장치벤더사
						aaidItem.put("vendorId", item.get("vendorId")); // 인증장치벤더ID
						aaidAllowListMap.add(aaidItem);
					}
				}
				resultMap.put("aaidAllowListMap", aaidAllowListMap);
			}
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		}

		log.debug("@@@ RaonUtil allowedAuthnr resultMap[{}]", resultMap.toString());
		return resultMap;
	}

	/**
	 * 2.10. 등록 상태 조회
	 * @param jsonParam
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "등록 상태 조회", description = "등록 상태 조회", author = "이완주")
	public Map<String, Object> checkRegisteredStatus(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String verifyType = jsonParam.optString("verifyType", "9999");

		jsonParam.put("command", "checkRegisteredStatus");

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		if (jsonResult != null) {
			JSONObject resultData = jsonResult.optJSONObject("resultData");
			if (resultData != null) {
				resultMap.put("regYn", resultData.optString("regYn").toString());
				resultMap.put("regMsg", resultData.optString("regMsg").toString());
				if ("Y".equals(resultData.get("regYn"))) {
					String keyId = "";

					JSONObject aaidList = resultData.optJSONObject("aaidList");
					if (aaidList != null) {
						JSONArray registered = aaidList.optJSONArray("registered");
						if (registered != null) {
							for (int i = 0; i < registered.length(); i++) {
								JSONObject item = (JSONObject) registered.get(i);
								if (verifyType.equals(item.get("verificationType"))) {
									keyId = StringUtils.nvl((String) item.optString("keyId"), "");
								}
							}
						}
					}
					String notAfter = "";
					JSONArray issuedCertList = resultData.optJSONArray("issuedCertList");
					if (issuedCertList != null) {
						for (int i = 0; i < issuedCertList.length(); i++) {
							JSONObject item = (JSONObject) issuedCertList.get(i);
							if (keyId.equals(item.get("keyId"))) {
								notAfter = StringUtils.nvl((String) item.optString("notAfter"), "");
							}
						}
					}
					resultMap.put("notAfter", notAfter);
				}
			}
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		}

		log.debug("@@@ RaonUtil checkRegisteredStatus resultMap[{}]", resultMap.toString());
		return resultMap;
	}

	/**
	 * 2.13.	바이오 인증서 서명
	 * @param jsonParam
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "바이오 인증서 서명", description = "바이오 인증서 서명", author = "이완주")
	public Map<String, Object> requestP7Sign(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		jsonParam.put("command", "requestP7Sign");

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		if (jsonResult != null) {
			JSONObject resultData = jsonResult.optJSONObject("resultData");
			if (resultData != null) {
				resultMap.put("trId", resultData.optString("trId"));
			}
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		}

		log.debug("@@@ RaonUtil requestP7Sign resultMap[{}]", resultMap.toString());
		return resultMap;
	}

	/**
	 * 2.3.	Raon 간편인증(디지털 인증서 연동X)
	 * @param jsonParam
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "Raon 간편인증(디지털 인증서 연동X)", description = "Raon 간편인증(디지털 인증서 연동X)", author = "이완주")
	public Map<String, Object> requestServiceAuth(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		jsonParam.put("command", "requestServiceAuth");

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		if (jsonResult != null) {
			JSONObject resultData = jsonResult.optJSONObject("resultData");
			if (resultData != null) {
				resultMap.put("trId", resultData.optString("trId"));
			}
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		}

		log.debug("@@@ RaonUtil requestServiceAuth resultMap[{}]", resultMap.toString());
		return resultMap;
	}

	/**
	 * 2.8.	거래결과 확인
	 * @param jsonParam
	 * @return
	 * @throws SCBKAppException
	 */
	@ComponentOperation(name = "거래결과 확인", description = "거래결과 확인", author = "이완주")
	public Map<String, Object> trResultConfirm(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		jsonParam.put("command", "trResultConfirm");

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		log.debug("FidoHttpComponent > trResultConfirm jsonResult :  [{}]", jsonResult == null ? "NULL" : jsonResult.toString());

		if (jsonResult != null) {
			JSONObject resultData = jsonResult.optJSONObject("resultData");
			if (resultData != null) {
				resultMap.put("trStatus", resultData.optString("trStatus"));// 거래상태
				resultMap.put("trStatusMsg", resultData.optString("trStatusMsg"));// 거래상태 메시지
				resultMap.put("signedData", resultData.optString("signedData"));// 서명문
				resultMap.put("signedDataList", resultData.optString("signedDataList"));// 서명문 리스트
				resultMap.put("multiSignedData", resultData.optString("multiSignedData"));// 멀티 서명문 리스트
				resultMap.put("fidoLoginId", resultData.optString("loginId")); //
				resultMap.put("fidoVerifyType", resultData.optString("verifyType"));// 멀티 서명문 리스트
			}
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		}
		log.debug("FidoHttpComponent > trResultConfirm resultMap :  [{}]", resultMap == null ? "NULL" : resultMap.toString());

		return resultMap;
	}

	/**
	 * FIDO 서비스 해지
	 * @param jsonParam
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "FIDO 서비스 해지", description = "FIDO 서비스 해지", author = "이완주")
	public Map<String, Object> requestUserMngtinitAuthnr(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		jsonParam.put("command", "requestUserMngtInitAuthnr");
		jsonParam.put("operatorId", PropertiesUtils.getString("FIDO_ADMIN_ID", "onepassadmin"));

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		if (jsonResult != null) {
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		} else {
			resultMap.put("resultCode", "FIDO_9000");
		}

		log.debug("@@@ RaonUtil requestUserMngtinitAuthnr resultMap[{}]", resultMap.toString());
		return resultMap;
	}

	/**
	 * 2.10.	나의 등록 인증장치 조회 결과
	 * @param jsonParam
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "나의 등록 인증장치 조회 결과", description = "나의 등록 인증장치 조회 결과", author = "이완주")
	public Map<String, Object> checkRegisteredStatusResult(JSONObject jsonParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		jsonParam.put("command", "checkRegisteredStatus");

		JSONObject jsonResult = this.requestPost(jsonParam); // FIDO서버 응답 획득

		if (jsonResult != null) {
			JSONObject resultData = jsonResult.optJSONObject("resultData");

			if (resultData != null) {
				resultMap.put("regYn", resultData.get("regYn"));

				if ("Y".equals(resultData.get("regYn"))) {

					List<Map<String, Object>> registeredListMap = new ArrayList<Map<String, Object>>();
					if (resultData.optJSONObject("aaidList") != null) {
						JSONObject aaidList = resultData.optJSONObject("aaidList");
						JSONArray registered = aaidList.optJSONArray("registered");

						if (registered != null) {
							Map<String, Object> registeredItem = new HashMap<String, Object>();

							for (int i = 0; i < registered.length(); i++) {
								registeredItem = new HashMap<String, Object>();
								JSONObject item = (JSONObject) registered.get(i);

								registeredItem.put("aaid", item.get("aaid")); // AAID
								registeredItem.put("keyId", item.get("keyId")); // keyId
								String keyId = item.optString("keyId");

								registeredItem.put("verificationType", item.get("verificationType")); // 검증타입
								registeredItem.put("verificationNm", item.get("verificationNm")); // 검증타입명
								registeredItem.put("vendorNm", item.get("vendorNm")); // 인증장치벤더사
								registeredItem.put("vendorId", item.get("vendorId")); // 인증장치벤더ID

								if (resultData.get("issuedCertList") != null) {
									JSONArray issuedCertList = resultData.optJSONArray("issuedCertList");

									if (issuedCertList != null) {
										for (int j = 0; j < issuedCertList.length(); j++) {
											JSONObject issuedItem = (JSONObject) issuedCertList.get(i);
											if (keyId.equals(issuedItem.get("keyId"))) {
												registeredItem.put("notAfter", issuedItem.get("notAfter")); // 유효기간 끝
											}
										}
									}
								}
								registeredListMap.add(registeredItem);
							}
						}
					}
					resultMap.put("digitalCertListMap", registeredListMap);
				}
			} else {
				resultMap.put("regYn", "N");
			}
			resultMap.put("resultCode", jsonResult.get("resultCode"));
			resultMap.put("resultMsg", jsonResult.get("resultMsg"));
		} else {
			resultMap.put("regYn", "N");
		}

		log.debug("@@@ RaonUtil checkRegisteredStatusResult resultMap[{}]", resultMap.toString());

		return resultMap;
	}
}
