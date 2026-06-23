package com.scbank.process.api.svc.shared.components.verification;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.edmi.dto.mci.MciIbCidi001Req;
import com.scbank.process.api.edmi.dto.mci.MciIbCidi001Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H20100Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H20100Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciManager;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManager;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.ipinside.dto.IpinsideHddInfo;
import com.scbank.process.api.svc.shared.components.verification.constants.VerificationConstants;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiResponse;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationInsertCustomerCiRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationVerifyInfo;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class VerificationComponent {

	/**
	 * OLTP hexstring 전문 송/수신 매니저 - HostClient 순환참조로 OltpManager 직접사용
	 */
	private final OltpManager hostManager;

	/**
	 * MCI 전문 송/수신 매니저 컴포넌트
	 */
	private final MciManager mciManaer;

	/**
	 * 세션 컨텍스트 매니저
	 */
	private final ISessionContextManager sessionManager;

	private final IpinsideComponent ipinside;

	@ComponentOperation(name = "추가인증 검증")
	public void verifyAdditional() {

		String addAuthSuccessFlag = StringUtils
				.defaultString(sessionManager.getGlobalValue("ADD_AUTH_SUCCESS_FLAG", String.class));

		if ("N".equals(addAuthSuccessFlag)) {
			throw new PRCServiceException("PRCCMM0013", "추가인증을 진행해 주세요.");
		}

	}

	@ComponentOperation(name = "보안매체 검증")
	public void verifyTokens(VerificationVerifyInfo info) {

		// 업무에서 사용자ID를 넘기는 경우 업무에서 전송한 사용자ID 사용
		String userId = StringUtils.defaultIfBlank(info.getUserId(),
				sessionManager.getLoginValue("UserID", String.class));

		String telNo1 = VerificationUtils.getTelNo1();
		String telNo2 = VerificationUtils.getTelNo2();
		String telNo3 = VerificationUtils.getTelNo3();

		if (StringUtils.isBlank(telNo1) || StringUtils.isBlank(telNo2) || StringUtils.isBlank(telNo3)) {
			telNo1 = SessionUtils.getSessionValue("TeleOne");
			telNo2 = SessionUtils.getSessionValue("TeleTwo");
			telNo3 = SessionUtils.getSessionValue("TeleThree");
		}

		String perBusNo = SessionUtils.getSessionValue("PerBusNo"); // 주민번호
		String safeCardState = SessionUtils.getSessionValue("SafeCardState");
		String safeCardKind = SessionUtils.getSessionValue("SafeCardKind");
		String safeCardIssueNum = SessionUtils.getSessionValue("SafeCardIssueNum");
		String safeCardBranchNum = SessionUtils.getSessionValue("SafeCardBranchNum");
		String safeCardIndex = SessionUtils.getSessionValue("SafeCardINDEX");
		String safeCardIndex2 = SessionUtils.getSessionValue("SafeCardINDEX2");
		String smartOTP = SessionUtils.getSessionValue("SmartOTP");

		IpinsideHddInfo ipinsideHddInfo = ipinside.getIpinsidInfo();

		String trackingId = ThreadLocalStoreDelegator.getTrackingId();

		OltpRequestOptions hostCfg = new OltpRequestOptions();
		hostCfg.setInterfaceId("CB_TBS03_H201");
		hostCfg.setTrackingId(trackingId);
		hostCfg.setExceptionOnError(true);

		hostCfg.setImsTranCd("TI1TBS03");
		hostCfg.setInClassCd("H201");
		hostCfg.setSvcCd("731");

		CbTbs03H20100Req req = new CbTbs03H20100Req();

		req.setUserID(userId); // 사용자ID
		req.setSafeCardKind(safeCardKind); // 보안카드종류
		req.setSafeCardState(safeCardState); // 안전카드 상태
		req.setSafeCardIssueNum(safeCardIssueNum); // 안전카드 발급번호
		req.setSafeCardBranchNum(safeCardBranchNum); // 안전카드 점번호
		req.setTelNo1(telNo1); // 연락처1
		req.setTelNo2(telNo2); // 연락처2
		req.setTelNo3(telNo3); // 연락처3
		req.setTransPasswordconfirm(this.getTransPasswordConfirm(info.getTransPasswordYn())); // 이체비밀번호확인
		req.setSafeCardconfirm(info.getSafeCardConfirm());
		req.setLogSkip(info.getLogSkip()); // 설정하는 곳이 없음. 공백처리
		req.setLogDummy(""); // 설정하는 곳이 없음. 공백처리
		req.setFidoSkip(info.getFidoSkip()); // 보안매체 Skip 여부
		req.setYIPINCH(""); // 설정하는 곳이 없음. 공백처리
		req.setYIPINPASS(""); // 설정하는 곳이 없음. 공백처리
		req.setInputJumin(perBusNo); // 주민번호
		req.setTransPassword(this.getTransPassword(info.getTransPasswordYn())); // 이체비밀번호
		req.setYIIPN(IpUtils.getClientIp()); // IP정보
		req.setYIMAC(ipinsideHddInfo.getMacAddress()); // MAC정보

		if ("1".equals(safeCardState)) {
			req.setSafeCardINDEX(safeCardIndex);
			req.setSafeCardINDEX2(safeCardIndex2);
		}

		req.setSafeCardNum(VerificationUtils.getSafeCardNumber1()); // 보안매체번호 (보안카드 첫번째, OTP, MOTP 번호)
		req.setSafeCardNum2(VerificationUtils.getSafeCardNumber2()); // 보안매체번호2

		String tokensType = VerificationUtils.getTokensType();
		if (tokensType.equals(VerificationConstants.MOTP)) {
			req.setYIMBOTP("Y"); // 모바일OTP사용여부
		}

		log.debug("CbTbs03H20100Req hdg req {}", req.toString());

		CbTbs03H20100Res outputDto;
		if (RunMode.LOCAL.equals(RuntimeContext.getRunMode()) && "3".equals(safeCardKind) && "M".equals(smartOTP)) {
			// 검증패스
			outputDto = new CbTbs03H20100Res();
			outputDto.setSafeCardKind(SessionUtils.getSessionValue("SafeCardKind"));
			outputDto.setSafeCardINDEX(SessionUtils.getSessionValue("SafeCardINDEX"));
			outputDto.setSafeCardINDEX2(SessionUtils.getSessionValue("SafeCardINDEX2"));
		} else {
			// 검증실행
			try {

				OltpResponse<CbTbs03H20100Res> hostResponse = this.hostManager.send(hostCfg, req,
						CbTbs03H20100Res.class);

				outputDto = hostResponse.getResponse();
			} catch (OltpSystemException e) {
				e.addErrorPageParameter("SECU_SERVICE_YN", "Y");
				throw e;
			}

		}

		this.removeTokensSession(outputDto, telNo1, telNo2, telNo3);

	}

	/**
	 * CI조회 및 등록
	 * 
	 * @param custName
	 * @return
	 */
	@ComponentOperation(name = "CI조회 및 등록")
	public VerificationGetCustomerCiResponse checkUserCiInfo() {

		VerificationGetCustomerCiResponse response = new VerificationGetCustomerCiResponse();

		try {

			String ciInfo = StringUtils.defaultString(sessionManager.getGlobalValue("USER_CI_INFO", String.class));

			VerificationGetCustomerCiRequest request = new VerificationGetCustomerCiRequest();

			if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
				request.setCi(ciInfo);
			} else {
				String perBusNum = SessionUtils.getSessionValue("PerBusNo");
				request.setCi(ciInfo);// CI정보
				request.setAismno(perBusNum);// 실명번호
				request.setAicrgb("3");// 처리구분(1:CI번호, 2:CIF번호, 3:주민번호, 4:계좌번호)
			}

			request.setDacomName("");
			request.setDacomTranYN("Y");

			response = getCustomerCi(request);

			String isCiYn = StringUtils.defaultIfBlank(response.getResult(), "N");
			String isCiErr = StringUtils.defaultIfBlank(response.getIsCIERR(), "N");
			String statusYn = StringUtils.defaultIfBlank(response.getStatusYN(), "N");
			String isNameChkErr = StringUtils.defaultIfBlank(response.getIsNameChkErr(), "N");

			if (!"Y".equals(isNameChkErr) && !"Y".equals(isCiErr) && !("Y".equals(isCiYn) && "Y".equals(statusYn))) {

				VerificationInsertCustomerCiRequest req = new VerificationInsertCustomerCiRequest();

				if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
					req.setUserCiInfo(ciInfo);
				} else {
					req.setUserCiInfo(StringUtils
							.defaultIfBlank(sessionManager.getGlobalValue("TEST_CI_INFO", String.class), ciInfo));
				}

				insertCustomerCi(req);
			}

		} catch (Exception e) {
			log.error("Exception : [{}]", e.getMessage());
			response.setIsCIERR("N");
			return response;
		}

		return response;

	}

	/**
	 * CI조회
	 *
	 * @param getCustomerCi
	 * @return
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ018_502S
	 */
	@ComponentOperation(name = "CI조회 [ASIS:MA3CMMBIZ018_502S]", author = "")
	public VerificationGetCustomerCiResponse getCustomerCi(VerificationGetCustomerCiRequest request)
			throws PRCServiceException {

		VerificationGetCustomerCiResponse response = new VerificationGetCustomerCiResponse();

		String reqCI = StringUtils.defaultIfEmpty(request.getCi(), "");

		MciIbCidi001Req sendData = new MciIbCidi001Req();

		String AIGRGB = StringUtils.defaultIfEmpty(request.getAigrgb(), "C");
		String AISMNO = StringUtils.defaultIfEmpty(request.getAismno(), ""); // 실명번호
		String AIGUBUN = StringUtils.defaultIfEmpty(request.getAigubun(), "1"); // 등록구분(1:조회, 2:등록, 3:해지)
		String AICRGB = StringUtils.defaultIfEmpty(request.getAicrgb(), "1"); // 처리구분(1:CI번호, 2:CIF번호, 3:주민번호,
																				// 4:계좌번호)
		String AICINOINF = reqCI; // CI정보
		String AICIFNO = StringUtils.defaultIfEmpty(request.getAicifno(), ""); // CIF번호
		String AIJUMNO = StringUtils.defaultIfEmpty(request.getAijumno(), "000"); // 점번호
		String AIKMCOD = StringUtils.defaultIfEmpty(request.getAikmcod(), ""); // 과목코드
		String AIGJWNO = StringUtils.defaultIfEmpty(request.getAigjwno(), ""); // 계좌번호
		String AIFTRCOD = StringUtils.defaultIfEmpty(request.getAiftrcod(), "");// 생성업무코드

		MciRequestOptions mciCfg = new MciRequestOptions();
		mciCfg.setInterfaceId("MCI_IB_CIDI_001");
		mciCfg.setTranCd("IB_CIDI_001");

		sendData.setAIGRGB(AIGRGB); // 거래구분
		sendData.setAISMNO(AISMNO); // 실명번호
		sendData.setAIGUBUN(AIGUBUN); // 등록구분(1:조회, 2:등록, 3:해지)
		sendData.setAICRGB(AICRGB); // 처리구분(1:CI번호, 2:CIF번호, 3:주민번호, 4:계좌번호)
		sendData.setAICINOINF(AICINOINF); // CI정보
		sendData.setAICIFNO(AICIFNO); // CIF번호
		sendData.setAIJUMNO(AIJUMNO); // 점번호
		sendData.setAIKMCOD(AIKMCOD); // 과목코드
		sendData.setAIGJWNO(AIGJWNO); // 계좌번호
		sendData.setAIFTRCOD(AIFTRCOD); // 생성업무코드

		log.debug("sendData > {}", sendData.toString());

		MciResponse<MciIbCidi001Res> mciResponse = mciManaer.send(mciCfg, sendData, MciIbCidi001Res.class);

		MciIbCidi001Res output = mciResponse.getResponse();

		String AOCONFIRM = StringUtils.defaultIfEmpty(output.getAOCONFIRM(), "");
		String AOSTSGB = StringUtils.defaultIfEmpty(output.getAOSTSGB(), "");
		String AOCIFNO = StringUtils.defaultIfEmpty(output.getAOCIFNO(), "");
		String AOSMNO = StringUtils.defaultIfEmpty(output.getAOSMNO(), "");
		String AOCMFNA = StringUtils.defaultIfEmpty(output.getAOCMFNA(), "");
		String AOFSTSSIL10 = StringUtils.defaultIfEmpty(output.getAOFSTSSIL10(), "");
		String AOJYENDIL = StringUtils.defaultIfEmpty(output.getAOJYENDIL(), "");
		String AOCINOINF = StringUtils.defaultIfEmpty(output.getAOCINOINF(), "");
		String isCIERR = "N";

		// 주민등록번호 세션 비교 및 저장(상태값(AOSTSGB)까지 체크하도록 수정)
		if (!"".equals(AOSMNO) && "Y".equals(AOCONFIRM) && "Y".equals(AOSTSGB)) {
			String PerBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);

			if (!PerBusNo.equals(AOSMNO)) {
				sessionManager.removeGlobalValue("CUST_CI");
				sessionManager.removeGlobalValue("PerBusNo");

				isCIERR = "Y";
			} else {
				// 세션 초기화후 다시 세팅
				sessionManager.removeGlobalValue("PerBusNo");
				sessionManager.setGlobalValue("PerBusNo", AOSMNO);

				// CI가 정상등록이고, 주민번호도 같고, 사활구분도 Y일때, 세션에 CI정보 업데이트(휴대폰인증 안타고 CI조회했을때 가져다쓸수가 없기때문에
				// 세션에서 CI정보 가져다쓰기 위함.)
				sessionManager.removeGlobalValue("USER_CI_INFO");
				sessionManager.setGlobalValue("USER_CI_INFO", AOCINOINF);
			}
		}

		// 테스트계일때 CI 기등록 고객일경우 조회된 CI로 다시 등록하기 위해 추가
		if (!RunMode.PRD.equals(RuntimeContext.getRunMode())) {
			if (!"".equals(AOSMNO) && !"".equals(AOCINOINF) && ("N".equals(AOCONFIRM) || "N".equals(AOSTSGB))) {
				sessionManager.setGlobalValue("TEST_CI_INFO", AOCINOINF);
			}
		}

		// TODO.추후 변경필요
		String url = PropertiesUtils.getString("MA30_URL")
				+ "/product/product/MA3PRDALL001.mnu?changeViewService=/common/MA3CMMBIZ018_10V.view";

		if ("Y".equals(AOCONFIRM)) {
			response.setResult("Y"); // CI값 보유여부
			response.setCif(AOCIFNO); // CIF값
			response.setUrl(""); // 본인인증 URL
			response.setIsCIERR(isCIERR); // 주민번호 불일치 여부
			response.setMsgCd("0000"); // 0000 정상종료
			response.setStatusYN(AOSTSGB); // 사활구분(Y/N)
		} else if ("N".equals(AOCONFIRM)) {
			response.setResult("N"); // CI값 보유여부
			response.setCif(""); // CIF값
			response.setUrl(url); // 본인인증 URL
			response.setIsCIERR(isCIERR); // 주민번호 불일치 여부
			response.setMsgCd("0000"); // 0000 정상종료
			response.setStatusYN(AOSTSGB); // 사활구분(Y/N)
		} else {
			response.setResult(""); // CI값 보유여부
			response.setCif(""); // CIF값
			response.setUrl(""); // 본인인증 URL
			response.setIsCIERR(isCIERR); // 주민번호 불일치 여부
			response.setMsgCd("9999"); // 9999 오류발생
			response.setStatusYN(AOSTSGB); // 사활구분(Y/N)
		}

		// dacom 진행한이름과 CI 조회결과 이름이 다른지 확인
		String dacomName = StringUtils.defaultIfEmpty(request.getDacomName(), "");
		String dacomTranYN = StringUtils.defaultIfEmpty(request.getDacomTranYN(), "");

		if (StringUtils.isNotEmpty(AOCMFNA) && StringUtils.isNotEmpty(dacomName) && "Y".equals(dacomTranYN)) {
			if (dacomName.equals(AOCMFNA)) {
				response.setIsNameChkErr("N");
			} else {
				response.setIsNameChkErr("Y");
			}
		} else {
			// 비교할 이름이 없는경우 체크하지않는다
			response.setIsNameChkErr("N");
		}

		return response;
	}

	@ComponentOperation(name = "CI등록 [ASIS:MA3CMMBIZ018_503S]", author = "")
	public MciIbCidi001Res insertCustomerCi(VerificationInsertCustomerCiRequest request) {

		String USER_CI_INFO = request.getUserCiInfo();
		String JMNO = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), request.getJmno());

		String AIGRGB = request.getAigrgb(); // 거래구분
		String AISMNO = JMNO; // 실명번호
		String AIGUBUN = request.getAigubun(); // 등록구분(1:조회, 2:등록, 3:해지)
		String AICRGB = request.getAicrgb(); // 처리구분(1:CI번호, 2:CIF번호, 3:주민번호, 4:계좌번호)
		String AICINOINF = USER_CI_INFO; // CI정보
		String AICIFNO = request.getAicifno(); // CIF번호
		String AIJUMNO = request.getAijumno(); // 점번호
		String AIKMCOD = request.getAikmcod(); // 과목코드
		String AIGJWNO = request.getAigjwno(); // 계좌번호
		String AIFTRCOD = request.getAiftrcod(); // 생성업무코드

		MciRequestOptions mciCfg = new MciRequestOptions();
		mciCfg.setInterfaceId("MCI_IB_CIDI_001");
		mciCfg.setTranCd("IB_CIDI_001");

		MciIbCidi001Req req = new MciIbCidi001Req();

		req.setAIGRGB(AIGRGB);
		req.setAISMNO(AISMNO);
		req.setAIGUBUN(AIGUBUN);
		req.setAICRGB(AICRGB);
		req.setAICINOINF(AICINOINF);
		req.setAICIFNO(AICIFNO);
		req.setAIJUMNO(AIJUMNO);
		req.setAIKMCOD(AIKMCOD);
		req.setAIGJWNO(AIGJWNO);
		req.setAIFTRCOD(AIFTRCOD);

		MciResponse<MciIbCidi001Res> mciResponse = mciManaer.send(mciCfg, req, MciIbCidi001Res.class);

		MciIbCidi001Res res = mciResponse.getResponse();

		String AOCONFIRM = res.getAOCONFIRM(); // CI등록여부(Y:정상, N:해지)

		// CI가 정상적으로 등록되었을때 등록된 CI로 세션에 다시 세팅(정상 프로세스에서 세션에 CI가 없어서 에러나는거 방지)
		if ("Y".equals(AOCONFIRM)) {
			sessionManager.removeGlobalValue("USER_CI_INFO");
			sessionManager.setGlobalValue(res.getAOCINOINF(), USER_CI_INFO);
		}

		return res;

	}

	/**
	 * 보안매체 검증 세션 초기화
	 * 
	 * @param outputDto
	 * @param telNo1
	 * @param telNo2
	 * @param telNo3
	 */
	private void removeTokensSession(CbTbs03H20100Res outputDto, String telNo1, String telNo2, String telNo3) {

		// 이체비밀번호 노출여부를 세션을 신규로 작성해서 적용

		// 업무에서 설정한 이체정보 초기회
		sessionManager.removeGlobalValue("addCertTranInfoList");

		// ID찾기 에서 설정한 고정 전화번호 초기화
		sessionManager.removeGlobalValue("FIND_USER_ID_TRCD");
		sessionManager.removeGlobalValue("FIND_USER_ID_TEL1");
		sessionManager.removeGlobalValue("FIND_USER_ID_TEL2");
		sessionManager.removeGlobalValue("FIND_USER_ID_TEL3");

		sessionManager.removeGlobalValue("GUEST_USER_TRCD");
		sessionManager.removeGlobalValue("GUEST_USER_TEL1");
		sessionManager.removeGlobalValue("GUEST_USER_TEL2");
		sessionManager.removeGlobalValue("GUEST_USER_TEL3");

		if (sessionManager.isLogin()) {
			sessionManager.setLoginValue("SafeCardKind", outputDto.getSafeCardKind());
			sessionManager.setLoginValue("SafeCardINDEX", outputDto.getSafeCardINDEX());
			sessionManager.setLoginValue("SafeCardINDEX2", outputDto.getSafeCardINDEX2());
			sessionManager.setLoginValue("TeleOne", telNo1);
			sessionManager.setLoginValue("TeleTwo", telNo2);
			sessionManager.setLoginValue("TeleThree", telNo3);
		} else {
			sessionManager.setGlobalValue("SafeCardKind", outputDto.getSafeCardKind());
			sessionManager.setGlobalValue("SafeCardINDEX", outputDto.getSafeCardINDEX());
			sessionManager.setGlobalValue("SafeCardINDEX2", outputDto.getSafeCardINDEX2());
			sessionManager.setGlobalValue("TeleOne", telNo1);
			sessionManager.setGlobalValue("TeleTwo", telNo2);
			sessionManager.setGlobalValue("TeleThree", telNo3);
		}

	}

	/**
	 * 이체비밀번호 설정 (검증시 본거래에도 설정필요)
	 * 
	 * @return
	 */
	private String getTransPassword(String transPasswordYn) {
		String transPwUseYn = SessionUtils.getSessionValue("TransPWUseYN");
		String transPw = "1".equals(transPwUseYn) || "N".equals(transPasswordYn) ? "99999999"
				: VerificationUtils.getTransPassword();

		// 업무 본거래 전문에 설정하기 위하여 세션설정
		sessionManager.setGlobalValue("transPassword", transPw);
		return transPw;
	}

	/**
	 * 이체비밀번호 검증여부
	 * 
	 * @return
	 */
	private String getTransPasswordConfirm(String transPasswordYn) {
		return "Y".equals(transPasswordYn) ? "" : "0";
	}
}
