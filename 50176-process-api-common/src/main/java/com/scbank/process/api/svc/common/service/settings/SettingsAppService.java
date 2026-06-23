package com.scbank.process.api.svc.common.service.settings;

import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92500Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92500Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.NoticeComponent;
import com.scbank.process.api.svc.common.dao.SmtPhoneBankingVsnMgtDao;
import com.scbank.process.api.svc.common.dao.dto.SmtPhoneBankingInfoResult;
import com.scbank.process.api.svc.common.mapper.SettingsAppMapper;
import com.scbank.process.api.svc.common.service.settings.dto.app.HomeMenuInfo;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetAccountViewYnRequest;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetAccountViewYnResponse;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppGetInitInfoResponse;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppSendMalWareResultRequest;
import com.scbank.process.api.svc.common.service.settings.dto.app.SetAppSetGlobalInfoRequest;
import com.scbank.process.api.svc.common.service.settings.dto.emergency.SetEmgSearchResponse;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.E2EUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "설정 - 앱연계정보", url = "/settings/app")
public class SettingsAppService {

	/**
	 * EDMI 통합 클라이언트
	 */
	private final HostClient hostClient;
	private final ISessionContextManager sessionManager;

	// propName = propertyManager.getString(String.format("openapi.prop.%s.name"),
	// name);

	private final static String CREATE_TABLE_QUERY = "CREATE TABLE MA3_APP_PUSHMSG(MSG_SEQ INTEGER COMPOSITE KEY, MSG_TYPE TEXT COMPOSITE KEY, BNKING_ID TEXT COMPOSITE KEY, ACCT_NO\tTEXT, INOUT_KIND TEXT, BANK_NAME TEXT, LONELY TEXT, AMOUNT TEXT, SEND_DATE TEXT, BALANCE TEXT, CAMP_SEQ TEXT, TITLE TEXT, CNTS TEXT, CONTENTS_MSG TEXT, WEB_URL TEXT, IMG_URL TEXT, VECTOR_ID TEXT, APP_NO TEXT)";
	private final static String PUSH_FCM_TK = "AAAAYzME-XY:APA91bGsxfG_epP6T39HMtrhUUVhsasnPjS1uZyz6zwMZDGAO_HueOfEm3pCkhu7HM7POHTdM0OXwrvIZAfInTNUTfgAV207Tx9iJKSRoBp2DxvfPx6jCudmYg8Yxeo5NkvFBxPtfVos";

	private final NoticeComponent noticeComponent;
	private final IpinsideComponent ipinsideComponent;

	private final SmtPhoneBankingVsnMgtDao smtPhoneBankingVsnMgtDao;

	private final SettingsAppMapper settingsAppMapper;

	/**
	 * ASIS: MA3CMMINF001_100S
	 */
	@ServiceEndpoint(url = "/getInitInfo", name = "앱 초기화 정보조회")
	public SetAppGetInitInfoResponse getInitInfo(IServiceContext serviceContext) {

		SetAppGetInitInfoResponse response = new SetAppGetInitInfoResponse();

		// 1. 긴급공지
		String emergencyJson = "";
		// TODO: 이거 긴급공지 사용하나??

		// 2. nfilter
		Map<String, String> nKeyMap = E2EUtil.getNkey(serviceContext.request().getSession());
		String nfilterPublicKey = (String) nKeyMap.get(E2EUtil.NFILTER_PUBLICKEY);

		response.setNfilterPublicKey(nfilterPublicKey);
		response.setSessionTime(serviceContext.request().getSession().getMaxInactiveInterval() + "");
		response.setHostUrl("https://m.sc.co.kr"); // HHS 090315: https://m.sc.co.kr
		response.setMainPageUrl(""); // ASIS : HHS: 20190522: 현재 없음
		response.setAuthUrl(""); // ASIS : 20190522 확인결과: 현재 app에서 사용안함(안드로이드, ios 동일)
		response.setTimeUrl(PropertiesUtils.getString("PKI_TIME_URL"));
		response.setRelayUrl(PropertiesUtils.getString("PKI_COPY_URL"));
		response.setVpcgAgentUrl(PropertiesUtils.getString("VPCG_AGENT_URL")); // VPCG WIZVERA 게이트웨이?
		response.setVpcgAppUrl(PropertiesUtils.getString("VPCG_APP_URL")); // VPCG WIZVERA G10 랜딩페이지

		response.setNfilterPublicKey(nfilterPublicKey);
		response.setNoticeEmergency(emergencyJson); // TODO: 사용하지 않는듯 삭제확인필요
		response.setCreateTableQuery(CREATE_TABLE_QUERY);
		response.setGatewayUrl(PropertiesUtils.getString("MA30_URL") + "/commons/gateway/COMGTW000_00MS"); // [ASIS]
																											// MA3CMMAAT010_201S

		response.setLoginMenuUrl("/login/advance/LGNADV000_00MS");
		response.setLoginTypeMenuUrl(""); // TODO: 로그인방식 페이지 (추후 필요시 추가)
		response.setJoinMembershipMenuUrl("/product/common/MA3PRDIBR003.mnu");
		response.setFinancialProductMainUrl("/product/product/MA3PRDALL001.mnu");
		response.setAuthcenterMainUrl("/certification/securityHome/CRNHOM000_00MS");
		response.setPushUrl("/keylanding/myNotifications/KLDUPD000_10MS"); // TODO: Push Url - 추후 확정 후 설정필요
		response.setPushFcmUrl(PropertiesUtils.getString("PUSH_FCM_URL"));
		response.setPushMotpUrl("/certification/mobileOtp/CRTSPL005_10MS"); // TODO: Push MOTP Url - 추후 확정 후 설정필요
		response.setPushFcmTk(PUSH_FCM_TK);

		// 긴급공지 세팅
		SetEmgSearchResponse setEmgResponse = noticeComponent.getPmsNotice("Z04001");
		response.setNtcShowYn(setEmgResponse.getNtcShowYn());
		response.setNtcTitle(setEmgResponse.getNtcTitle());
		response.setNtcContents(setEmgResponse.getNtcContents());
		response.setNtcBtnFlg(setEmgResponse.getNtcBtnFlg());
		response.setNtcBtnUrl(setEmgResponse.getNtcBtnUrl());
		response.setNtcBtnName(setEmgResponse.getNtcBtnName());

		HomeMenuInfo homeMenuInfo = HomeMenuInfo.builder()
				.normal(List.of(
						HomeMenuInfo.MenuInfo.builder().order(1).type("HOME")
								.url("/keylanding/home/KLDHOM000_00MS?webviewType=main").build(), // 홈
						HomeMenuInfo.MenuInfo.builder().order(2).type("TRANSFER")
								.url("/transfer/transferMain/TRNMAN000_10MS?webviewType=main").build(), // 이체 (이체메인)
						HomeMenuInfo.MenuInfo.builder().order(3).type("INVEST")
								.url("/asstmgt/ast/MA3ASTMGT001.mnu?webviewType=main").build(), // 투자
						HomeMenuInfo.MenuInfo.builder().order(4).type("PRODUCT")
								.url("/product/product/MA3PRDALL001.mnu?webviewType=main").build(), // 상품
						HomeMenuInfo.MenuInfo.builder().order(5).type("ALL")
								.url("/keylanding/menu/KLDMAP000_00MS?webviewType=main").build())) // 전체
				.easy(List.of(
						HomeMenuInfo.MenuInfo.builder().order(1).type("HOME")
								.url("/keylanding/comfy/KLDCMF000_00MS?webviewType=main").build(), // 홈
						HomeMenuInfo.MenuInfo.builder().order(2).type("INQUIRY")
								.url("/keylanding/comfy/KLDCMF000_10MS?webviewType=main")
								.build(), // 조회(미정)
						HomeMenuInfo.MenuInfo.builder().order(3).type("TRANSFER")
								.url("/transfer/singleTransfer/TRNSGL001_11MS?webviewType=main").build(), // 이체 (단건이체)
						HomeMenuInfo.MenuInfo.builder().order(4).type("PRODUCT")
								.url("/product/product/MA3PRDALL001.mnu?webviewType=main").build(), // 상품
						HomeMenuInfo.MenuInfo.builder().order(5).type("ALL")
								.url("/keylanding/menu/KLDMAP000_00MS?webviewType=main").build())) // 전체
				.english(List.of(
						HomeMenuInfo.MenuInfo.builder().order(1).type("HOME")
								.url("/keylanding/comfy/KLDCMF000_00MS?webviewType=main").build(), // 홈
						HomeMenuInfo.MenuInfo.builder().order(2).type("INQUIRY")
								.url("/keylanding/comfy/KLDCMF000_10MS?webviewType=main")
								.build(), // 조회
						HomeMenuInfo.MenuInfo.builder().order(3).type("TRANSFER")
								.url("/transfer/transferMain/TRNMAN000_10MS?webviewType=main").build(), // 이체 (이체메인)
						HomeMenuInfo.MenuInfo.builder().order(4).type("ALL")
								.url("/keylanding/menu/KLDMAP000_00MS?webviewType=main").build())) // 전체
				.build();

		response.setHomeMenuInfo(homeMenuInfo);

		// 최신 앱버전 세팅
		try {
			SmtPhoneBankingInfoResult smtPhoneBankingInfoResult = smtPhoneBankingVsnMgtDao.selectLatestAppVersion("M4");
			if (smtPhoneBankingInfoResult != null) {
				response.setSvrIosVersion(smtPhoneBankingInfoResult.getVsn());
			}

			smtPhoneBankingInfoResult = smtPhoneBankingVsnMgtDao.selectLatestAppVersion("M5");
			if (smtPhoneBankingInfoResult != null) {
				response.setSvrAndroidVersion(smtPhoneBankingInfoResult.getVsn());
			}

		} catch (PRCServiceException e) {
			e.printStackTrace();
			response.setSvrIosVersion("");
			response.setSvrAndroidVersion("");
		}

		// S - 24.09.23 앱 기동시 안드로이드 V3 탐지 On/Off Flag Set
		try {
			/*
			 * 0 : V3 Alert popup X 1 : V3 악성 Alert On 2 : V3 루팅 Alert On 3 : V3 악성 + 루팅
			 * Alert On 4 : V3 위험앱 Alert On 5 : V3 악성 + 위험앱 Alert On 6 : V3 루팅 + 위험앱 Alert
			 * On 7 : V3 악성 + 루팅 + 위험앱 Alert On
			 */

			int v3MobileDetectNum = 0;
			String v3MobileAI = StringUtils.parseString(PropertiesUtils.getString("V3_MOBILE_DETECT_AI"), "N");
			String v3MobileRC = StringUtils.parseString(PropertiesUtils.getString("V3_MOBILE_DETECT_RC"), "N");
			String v3MobileTI = StringUtils.parseString(PropertiesUtils.getString("V3_MOBILE_DETECT_TI"), "N");

			String v3MobileRealtimeAI = StringUtils.parseString(PropertiesUtils.getString("V3_MOBILE_REALTIME_AI"),
					"N");
			String v3MobileRealtimeRC = StringUtils.parseString(PropertiesUtils.getString("V3_MOBILE_REALTIME_RC"),
					"N");

			if ("Y".equals(v3MobileAI)) {
				v3MobileDetectNum += 1;
			}

			if ("Y".equals(v3MobileRC)) {
				v3MobileDetectNum += 2;
			}

			if ("Y".equals(v3MobileTI)) {
				v3MobileDetectNum += 4;
			}

			response.setV3MobileRealtimeAI(v3MobileRealtimeAI);
			response.setV3MobileRealtimeRC(v3MobileRealtimeRC);
			response.setV3MobileDetectNum(Integer.toString(v3MobileDetectNum));

		} catch (PRCServiceException e) {
			e.printStackTrace();
			response.setV3MobileDetectNum("0");
		}

		return response;

	}

	/**
	 * 계좌노출여부
	 * 
	 * @param ctx
	 * @param input
	 * @return
	 * @description MA3MSCMNG002_101S
	 */
	@ServiceEndpoint(url = "/getAccountViewYn", name = "계좌노출여부 [ASIS:MA3MSCMNG002_101S]", author = "송지섭")
	public SetAppGetAccountViewYnResponse getAccountViewYn(IServiceContext ctx, SetAppGetAccountViewYnRequest input) {

		String AcctNumType = StringUtils.defaultIfEmpty(input.getAcctNumType(), "");
		String CardType = StringUtils.defaultIfEmpty(input.getCardType(), "");
		String MenuType = StringUtils.defaultIfEmpty(input.getMenuType(), "");
		String RemitType = StringUtils.defaultIfEmpty(input.getRemitType(), "");

		// 공통부 세팅
		OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H925", "", "");
		hostCfg.setImsTranCd("TI1IBK01");
		hostCfg.setInClassCd("H925");
		hostCfg.setSvcCd("925");
		hostCfg.setCaptureSystem("OLTP");
		CbIbk01H92500Req sendData = new CbIbk01H92500Req();
		if ("".equals(AcctNumType)) {
			sendData.setGubun("01");
		} else {
			sendData.setGubun("02");
			sendData.setAcctNumType(AcctNumType);
			sendData.setCardType(CardType);
			sendData.setMenuType(MenuType);
			sendData.setRemitType(RemitType);
		}

		OltpResponse<CbIbk01H92500Res> hostResponse = this.hostClient.sendOltp(hostCfg, sendData,
				CbIbk01H92500Res.class);

		SetAppGetAccountViewYnResponse response = settingsAppMapper
				.toSetAppGetAccountViewYnResponse(hostResponse.getResponse());
		response.setYoGEORE(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("YOGEORE", String.class), ""));

		return response;
	}

	/**
	 * 게이트웨어 처리
	 * 
	 * @param ctx
	 * @param request
	 * @return
	 * @description MA3CMMAAT010_201S
	 */
	@ServiceEndpoint(url = "/setGlobalInfo", name = "게이트웨어 처리 [ASIS:MA3CMMAAT010_201S]", author = "2037688")
	public void setGloblaInfo(IServiceContext ctx, SetAppSetGlobalInfoRequest request) {

		String cnnCtnWay = StringUtils.nvl(request.getCnnCtnWay(), ""); // 제휴코드 세션저장
		String clerkNo = StringUtils.nvl(request.getClerkNo(), ""); // 소개자행번 세션 저장
		String reaCdn = StringUtils.nvl(request.getReaCdn(), ""); // REA코드 세션 저장
		// 제휴코드 세션저장
		if (StringUtils.isNotEmpty(cnnCtnWay)) {
			sessionManager.setGlobalValue("CNNCTN_WAY", cnnCtnWay);
		}

		// 소개자행번 세션저장
		if (StringUtils.isNotEmpty(clerkNo)) {
			sessionManager.setGlobalValue("CLERK_NO", clerkNo);
		}

		// 소개자행번 세션저장
		if (StringUtils.isNotEmpty(reaCdn)) {
			sessionManager.setGlobalValue("REACDN", reaCdn);
		}

	}

	/**
	 * ASIS: MA3CMMINF001_100S
	 */
	@ServiceEndpoint(url = "/sendMalWareResult", name = "악성앱 여부 전송")
	public void sendMalWareResult(IServiceContext serviceContext, SetAppSendMalWareResultRequest request) {

		// String v3EncryptedText = AESEncrypt(
		// "\"RC\":\"{\\\"ret\\\":0,\\\"reason\\\":0,\\\"description\\\":\\\"\\\",\\\"detectedType\\\":8,\\\"ruleid\\\":3}\",\"AI\":{},\"TI\":\"{\\\"behaviorDetectionInfo\\\":{\\\"associatedApp\\\":[],\\\"manipulatedCallInfo\\\":{\\\"connectedCallerId\\\":\\\"\\\",\\\"detectTime\\\":0,\\\"detectType\\\":0,\\\"displayCallerId\\\":\\\"\\\"},\\\"recentThreatApp\\\":[{\\\"category\\\":\\\"tampering\\\",\\\"firstInstalledTime\\\":1772081191267,\\\"lastUsedTime\\\":0,\\\"name\\\":\\\"com.scbank.ma30\\\"}],\\\"remote\\\":{\\\"detectTime\\\":0,\\\"firstInstalledTime\\\":0,\\\"name\\\":\\\"\\\"},\\\"sms\\\":{\\\"category\\\":0,\\\"detectTime\\\":0,\\\"detectType\\\":0,\\\"level\\\":0},\\\"sns\\\":[],\\\"url\\\":{\\\"category\\\":0,\\\"detectTime\\\":0,\\\"detectType\\\":0,\\\"level\\\":0},\\\"response\\\":{\\\"reason\\\":[\\\"recentThreatApp\\\"],\\\"riskGroup\\\":\\\"risk\\\",\\\"type\\\":\\\"vd\\\"}},\\\"deviceStatus\\\":{\\\"call\\\":{\\\"callStatus\\\":false,\\\"duration\\\":0}},\\\"manipulatedCallInfo\\\":[],\\\"mobileDeviceThreatInfo\\\":[{\\\"category\\\":\\\"forceCall\\\",\\\"data\\\":[]},{\\\"category\\\":\\\"remoteControl\\\",\\\"data\\\":[]},{\\\"category\\\":\\\"rooting\\\",\\\"data\\\":[{\\\"appName\\\":\\\"Magisk\\\",\\\"category\\\":\\\"rooting\\\",\\\"certHash\\\":\\\"cf6de8a3395f04c3\\\",\\\"certSha1\\\":\\\"dc0f2b61cbd7e9d3dbbe060b2b870d46bb060211\\\",\\\"certSha256\\\":\\\"b4cb83b4dad99f997dbe872f013aa16c14eec41d167021f371f7e1330f273ee6\\\",\\\"certificate\\\":\\\"3A8rYcvX6dPbvgYLK4cNRrsGAhE\\\\u003d\\\\n"
		// + //
		// "\\\",\\\"detectTime\\\":1772083285720,\\\"exceptionStatus\\\":false,\\\"firstInstalledTime\\\":1772080061637,\\\"installedTime\\\":1772080061637,\\\"isInstalled\\\":true,\\\"lastUsedTime\\\":0,\\\"name\\\":\\\"com.topjohnwu.magisk\\\",\\\"path\\\":\\\"\\\",\\\"permissions\\\":[],\\\"removeStatus\\\":false,\\\"severity\\\":\\\"high\\\",\\\"status\\\":\\\"installed\\\",\\\"updatedTime\\\":1772081687557,\\\"usageDuration\\\":0,\\\"version\\\":\\\"30.7\\\"}]},{\\\"category\\\":\\\"unIdentifiedInstaller\\\",\\\"data\\\":[]},{\\\"category\\\":\\\"specialPermission\\\",\\\"data\\\":[]},{\\\"category\\\":\\\"phishing\\\",\\\"data\\\":[]},{\\\"category\\\":\\\"tampering\\\",\\\"data\\\":[{\\\"appName\\\":\\\"SC제일은행\\\",\\\"category\\\":\\\"tampering\\\",\\\"certHash\\\":\\\"70d39a02f9f6d3f3\\\",\\\"certSha1\\\":\\\"9ddb6644952561040826eb4bd355ad36cc804256\\\",\\\"certSha256\\\":\\\"0312313b3d30a1c2bfbc79555a37317e5b00cd744a0bfe85c3c6795b52abc51f\\\",\\\"certificate\\\":\\\"ndtmRJUlYQQIJutL01WtNsyAQlY\\\\u003d\\\\n"
		// + //
		// "\\\",\\\"detectTime\\\":1772083285744,\\\"exceptionStatus\\\":false,\\\"firstInstalledTime\\\":1772081191267,\\\"installedTime\\\":1772083251421,\\\"isInstalled\\\":true,\\\"lastUsedTime\\\":0,\\\"name\\\":\\\"com.scbank.ma30\\\",\\\"path\\\":\\\"\\\",\\\"permissions\\\":[],\\\"removeStatus\\\":false,\\\"severity\\\":\\\"high\\\",\\\"status\\\":\\\"installed\\\",\\\"updatedTime\\\":1772083251421,\\\"usageDuration\\\":0,\\\"version\\\":\\\"1.8.3\\\"}]}],\\\"ret\\\":\\\"-18\\\"}\"}");

		// String ipinsideEncryptedText = AESEncrypt(
		// "{\"NATIP\":\"\",\"MDUInfo\":\"Omaob9cLhMMket3YW8JPuJxlMwldF5Bgh\\/w1CFGx7tdHQzFFoq+FCheXbqQp9C9I44vPcEeUZ457PFVatSKZIWvLHg\\/w\\/hshr9uciOEMs4FZ1uRSTsanT4wf6yECUbOyYyvN6OF+2WpYMMLYS1TCYQ==\",\"WDATA\":\"\\/wFnJjQmAPAeaYHtMTWAX+Fjlj94ubyTnSGJX2lpewGHnAdZjI7W6d2+iU5p5Qh+rr0E3CB8Uu7vVUJkImeSAhtv8d3qvFtn0TQMYk2lRhOrhz9c+Rc5J8SHv3PHaC4gDagUbu7Fx2EcBX7QBZ+XtDhP0JZQ5KMCP56evCVkqBdwcUFtZk7haPAzgUQ3eWPxm0+WBGXr0ycmKHwZHyg9+ylZ1LWE79yCp5VRAKIbYge6IcRbu022\\/xjCiOP\\/9gTp07CGQHeo4ISu5drvrtxQEYnRiqxPtHis4NsOFsW7JHfzuX3\\/86WUQUY1a+cyE1Pb\\/Y52I\\/CffH74\",\"ISIOS\":\"Y\"}");
		// request.setV3EncryptData(v3EncryptedText);
		// request.setIpinsideEncryptData(ipinsideEncryptedText);

		String v3DecryptData = "";
		String ipinsideDecryptData = "";
		String[] ipinsideDecryptDataArray = null;
		// v3DecryptData 복호화 시 생성된 줄바꿈 및 제어문자 제거위해 문자열 마지막 '}' 확인
		int lastIndex = 0;

		String WDATA = "";
		String NATIP = "";
		String MDUInfo = "";

		try {

			if ("N".equals(request.getIosYn())) {
				v3DecryptData = this.AESDecrypt(request.getV3EncryptData());
				// ipinsideDecryptData 복호화 시 생성된 줄바꿈 및 제어문자 제거
				lastIndex = v3DecryptData.lastIndexOf("}");
				v3DecryptData = v3DecryptData.substring(0, lastIndex + 1);

				log.info("### FDSAPP V3 Data ###  ::: " + v3DecryptData);
			}

			ipinsideDecryptData = this.AESDecrypt(request.getIpinsideEncryptData());
			log.info("### FDSAPP IpInside Data ###  ::: " + ipinsideDecryptData);

			// ipinsideDecryptData 복호화 시 생성된 제어문자 제거
			ipinsideDecryptDataArray = ipinsideDecryptData.split("\"}");
			log.info("### ipinsideDecryptDataArray ###  ::: " + ipinsideDecryptDataArray[0]);
			ipinsideDecryptData = ipinsideDecryptDataArray[0] + "\"}";
			log.info("### ipinsideDecryptData ###  ::: " + ipinsideDecryptData);

			JSONObject ipinsideParserParam = null;

			ipinsideParserParam = (JSONObject) new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(ipinsideDecryptData);
			log.info("######## FDSAPP Json Data ######## ipinsideParserParam : " + ipinsideParserParam);

			if (ipinsideParserParam != null) {
				WDATA = ipinsideParserParam.get("WDATA") != null ? String.valueOf(ipinsideParserParam.get("WDATA"))
						: "";
				NATIP = ipinsideParserParam.get("NATIP") != null ? String.valueOf(ipinsideParserParam.get("NATIP"))
						: "";
				MDUInfo = ipinsideParserParam.get("MDUInfo") != null
						? String.valueOf(ipinsideParserParam.get("MDUInfo"))
						: "";
			}

			log.info("######## FDSAPP WDATA Data ######## WDATA : " + WDATA);
			log.info("######## FDSAPP NATIP Data ######## NATIP : " + NATIP);

			ipinsideComponent.checkMalWare("MOBILE", WDATA, NATIP, "", v3DecryptData, "", MDUInfo);
		} catch (Exception e) {
			log.error("### FDSAPP IpInside Data Json Parser Error ###");
			e.printStackTrace();
		}

	}

	/**
	 * AES/GCM 방식 복호화처리
	 * 
	 * @param encrypted
	 * @return
	 */
	private String AESDecrypt(String encrypted) {
		log.debug("######## FDSAPP APP ######## AESDecrypt 복호화전 : " + encrypted);
		String PaykeyVal = PropertiesUtils.getString("V3_MOBILE_SYMMETRIC_KEY");
		String iv = PropertiesUtils.getString("V3_MOBILE_SYMMETRIC_KEY");
		log.debug("######## FDSAPP APP ######## AESDecrypt PaykeyVal : " + PaykeyVal);
		log.debug("######## FDSAPP APP ######## AESDecrypt iv : " + iv);
		String originalString = "";

		try {

			SecretKeySpec skeySpec = new SecretKeySpec(PaykeyVal.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

			cipher.init(2, skeySpec, new IvParameterSpec(PaykeyVal.getBytes()));
			byte[] byteStr = Base64.decodeBase64(encrypted.getBytes());
			byte[] original = cipher.doFinal(byteStr);
			originalString = new String(original);
		} catch (Exception e) {
			// e.printStackTrace();
			log.debug("######## FDSAPP APP ######## AESDecrypt 복호화에러 : " + e.getMessage());
		}

		log.debug("######## FDSAPP APP ######## AESDecrypt 복호화후 : " + originalString);
		return originalString;
	}

	/**
	 * AES/GCM 방식 암호화처리
	 * 
	 * @param encrypted
	 * @return
	 */
	private String AESEncrypt(String text) {
		log.debug("######## FDSAPP APP ######## AESEncrypt 평문 " + text);
		String PaykeyVal = PropertiesUtils.getString("V3_MOBILE_SYMMETRIC_KEY");
		String iv = PropertiesUtils.getString("V3_MOBILE_SYMMETRIC_KEY");
		log.debug("######## FDSAPP APP ######## AESEncrypt PaykeyVal : " + PaykeyVal);
		log.debug("######## FDSAPP APP ######## AESEncrypt iv : " + iv);

		String encryptedString = "";

		try {

			SecretKeySpec skeySpec = new SecretKeySpec(PaykeyVal.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv.getBytes()));

			byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));

			encryptedString = Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			// e.printStackTrace();
			log.debug("######## FDSAPP APP ######## AESEncrypt 암호화에러 : " + e.getMessage());
		}

		log.debug("######## FDSAPP APP ######## AESEncrypt 암호화후 : " + encryptedString);

		return encryptedString;
	}
}
