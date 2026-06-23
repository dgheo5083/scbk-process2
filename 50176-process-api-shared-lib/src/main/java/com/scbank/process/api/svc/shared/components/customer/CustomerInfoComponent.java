package com.scbank.process.api.svc.shared.components.customer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H87300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H87300Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C01Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C01Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92F00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92F00Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.customer.dao.NfCustomerMgtDao;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoParameter;
import com.scbank.process.api.svc.shared.components.customer.dto.CustCddInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.CustCddInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.NonFaceCustomerInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.NonFaceCustomerInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistCustInfoRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistCustInfoResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistDestructionCustInfoRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.RegistDestructionCustInfoResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.RegisterNonFaceCustomerInfoRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.RegisterNonFaceCustomerInfoResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.ValidateCddExpireDateRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.ValidateCddExpireDateResponse;
import com.scbank.process.api.svc.shared.components.customer.mapper.CddInfoMapper;
import com.scbank.process.api.svc.shared.components.customer.mapper.NonFaceCustomerInfoMapper;
import com.scbank.process.api.svc.shared.components.tradinfo.TradInfoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterOngoingTradeInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegistIntlTradInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoResponse;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class CustomerInfoComponent {

    private final TradInfoComponent tradInfoComponent;
    private final VerificationComponent verificationComponent;
    
	/**
	 * EDMI 통합 클라이언트
	 */
	private final HostClient hostClient;

	/**
	 * 세션 컨텍스트 매니저
	 */
	private final ISessionContextManager sessionManager;

	/** Mapper */
	private final CddInfoMapper cddInfoMapper;
	private final NonFaceCustomerInfoMapper nonFaceCustomerInfoMapper;

	/** DAO */
	private final NfCustomerMgtDao nfCustMgtDao;
	private final NfTradeInfoMgtDao nfTradeInfoMgtDao;

	@ComponentOperation(name = "고객정보/CDD 여부 조회 [MA3CMMBIZ001_116S]")
	public CustCddInfoInquiryResponse getCustCddInfo(CustCddInfoInquiryRequest request) {
		CustCddInfoInquiryResponse response = new CustCddInfoInquiryResponse();

		String isFinal = StringUtils.nvl(request.getIsFinal(),"");
		String yiJhGb = StringUtils.nvl(request.getYijhgb(), "3");
		String yiFirst = StringUtils.nvl(request.getYifirst(),"");
		String yoCddOk = "";

		// 공통부 세팅
		OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_H92C");
		hostCfg.setImsTranCd("TI1IBK01");
		hostCfg.setInClassCd("H92C");
		hostCfg.setSvcCd("92C");
		hostCfg.setVanTp("56");
		hostCfg.setCaptureSystem("OLTP");

		CbIbk01H92C00Req h92Req = new CbIbk01H92C00Req();

		h92Req.setUserID(SessionUtils.getSessionValue("UserID"));
		h92Req.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
		h92Req.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));
		h92Req.setYICMFNA(SessionUtils.getSessionValue("CustName"));
		h92Req.setYIIDGB(sessionManager.isLogin() ? "1" : "2");
		h92Req.setYIJHGB(yiJhGb);
		h92Req.setYIFIRST(yiFirst);

		log.debug("고객정보/CDD 여부 조회 UserID => {}", SessionUtils.getSessionValue("UserID"));
		log.debug("고객정보/CDD 여부 조회 TSPassword => {}", SessionUtils.getSessionValue("TSPassword"));
		log.debug("고객정보/CDD 여부 조회 PerBusNo => {}", SessionUtils.getSessionValue("PerBusNo"));

		// 전문 호출
		OltpResponse<CbIbk01H92C00Res> hostResponse = hostClient.sendOltp(hostCfg, h92Req, CbIbk01H92C00Res.class);
		CbIbk01H92C00Res h92Res = hostResponse.getResponse();

		response = cddInfoMapper.toCustCddInfoResponseDto(h92Res);

		response.setUserCiInfo(sessionManager.getGlobalValue("USER_CI_INFO", String.class));
		response.setYoJmNo(response.getYoJmNo().substring(0, 7) + "******");

		yoCddOk = response.getYoCddOk();

		if ("Y".equals(isFinal)) {
			if (!"Y".equals(yoCddOk)) {
				throw new PRCServiceException("PRCCMMCDD_0001"); // 고객정보 업데이트가 필요합니다.</br>고객확인제도 완료 후 다시 시도해 주세요.
			}
		}

		// [CDD자동화]
		// S - H92C 전문전송 이후 CDD유효일자로 CDD 유효기간도래 사용자 체크 후 세션에 저장
		String cddFlagChk = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("YOCDDIL_FLAG", String.class), ""); // CDD유효기간도래
		// 체크
		if ("".equals(cddFlagChk) || cddFlagChk == null) {
			ValidateCddExpireDateRequest validateCddExpireDateRequest = new ValidateCddExpireDateRequest();
			validateCddExpireDateRequest.setYoCddKycLc(response.getYoCddKycLc());

			ValidateCddExpireDateResponse cddExprieDate = validateCddExpireDate(validateCddExpireDateRequest);
			response.setYoCddIlFlag(cddExprieDate.getCddFlag());
		} else {
			response.setYoCddIlFlag(yoCddOk);
		}
		// E - H92C 전문전송 이후 CDD유효일자로 CDD 유효기간도래 사용자 체크 후 세션에 저장

		sessionManager.setGlobalValue("H92C_YOGEORE", StringUtils.defaultIfEmpty(response.getYoGeore(), ""));

		log.debug("고객정보/CDD 여부 조회 response => {}", response);
		log.debug("####################### 고객정보/CDD 여부 조회 [MA3CMMBIZ001_116S] END #######################");

		return response;
	}

	/**
	 * CDD 유효일자 계산
	 *
	 * @param ValidateCddExpireDateRequest
	 * @return ValidateCddExpireDateResponse
	 * @throws
	 * @description
	 */
	@ComponentOperation(name = "CDD 유효일자 계산 [MA3CMMBIZ026_202S]", author = "")
	public ValidateCddExpireDateResponse validateCddExpireDate(ValidateCddExpireDateRequest input) {

		log.debug("####################### CDD 유효일자 계산 [MA3CMMBIZ026_202S] START #######################");

		// cmm.서버.properties
		String effectiveDate = StringUtils.defaultIfEmpty(PropertiesUtils.getString("CDD_EFFECTIVE_DATE"), "90"); // CDD유효기간
																													// 유효범위
																													// 날짜
		String YOCDDIL = StringUtils.defaultIfEmpty(input.getYoCddIl(), ""); // CDD유효일자(H035)
		String YOCDDKYCLC = StringUtils.defaultIfEmpty(input.getYoCddKycLc(), ""); // CDD유효일자(H92C)

		String cddFlag = "N"; // CDD 유효일자 계산 결과 Y:CDD 필요 대상, N:CDD 불필요 대상, 공백:신규

		String CDDdate = (YOCDDIL.equals("")) ? YOCDDKYCLC : YOCDDIL;

		// 유효기간 도래 계산
		String curDateStr = DateUtils.getCurrentDate("yyyyMMdd");
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		long Days = 0;

		if (!"0".equals(CDDdate)) {
			try {
				Date d1 = format.parse(CDDdate);
				Date d2 = format.parse(curDateStr);

				long diffSec = (d1.getTime() - d2.getTime());
				Days = diffSec / (24 * 60 * 60 * 1000);

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (Days < Integer.parseInt(effectiveDate)) {
				cddFlag = "Y";
			} else {
				cddFlag = "N";
			}
		} else {
			// NTB
			cddFlag = "";
		}

		ValidateCddExpireDateResponse output = new ValidateCddExpireDateResponse();
		sessionManager.setGlobalValue("YOCDDIL_FLAG", cddFlag); // Y:CDD 유효기간도래 사용자, N:정상, "":NTB
		output.setCddFlag(cddFlag);

		log.debug("CDD 유효일자 계산 response = > {}", output);
		log.debug("####################### CDD 유효일자 계산 [MA3CMMBIZ026_202S] END #######################");

		return output;
	}

	/*
	 * 고객정보를 변경한다.
	 * TI1IBK01_H873 (계좌비밀번호체크)
	 * TI1IBK01_H92C01 (고객정보등록변경)
	 * TI1IBK01_H92F (외화고객정보)
	 * TI1IBK01_H894 (파기대상고객 해제거래 및 국가코드등록)
	 * TI1IBK01_H920 (고객정보조회)
	 * TI1IBK01_H92C (고객정보조회)
	 */
	@ComponentOperation(name = "고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S]")
	public RegistCustInfoResponse registCddCustInfo(RegistCustInfoRequest request) {
		
		String perBusNo = SessionUtils.getSessionValue("PerBusNo");
		String bizType = StringUtils.defaultString(request.getBizType());
		String isUpdate = StringUtils.defaultString(request.getIsUpdate());
		String otherAcctAuthYn = StringUtils.defaultString(request.getOtherAcctAuthYn());
		
		//1.고객정보 변경 전 계좌 비밀번호 체크(H873)
		if(!otherAcctAuthYn.equals("Y")) { // 타행계좌인증여부
			if("Y".equals(isUpdate) || StringUtils.isNotEmpty(request.getAcctNum())) {
				log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 1.고객정보 변경 전 계좌 비밀번호 체크(H873)");
				
				
				OltpRequestOptions h873Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H873");
		        h873Options.setImsTranCd("TI1IBK01");
		        h873Options.setInClassCd("H873");
		        h873Options.setSvcCd("873");

		        CbIbk01H87300Req h873Req = new CbIbk01H87300Req();
		        h873Req.setCGAcctNum(request.getAcctNum());
		        h873Req.setAcctPassword(request.getAcctPss()); // 계좌비밀번호
		        h873Req.setPasswordVerifiYorN("Y"); // 암호검증여부
		        h873Req.setPerNo(SessionUtils.getSessionValue("PerBusNo"));
		        h873Req.setUserID(SessionUtils.getSessionValue("UserID"));
		        h873Req.setTSPassword(SessionUtils.getSessionValue("TSPassword"));

		        this.hostClient.sendOltp(h873Options, h873Req, CbIbk01H87300Res.class).getResponse();

			}
		}
		
		//출생년월일
		String _apYear = "";
		String _YIBTHIL = perBusNo.substring(6,7);
		if("1".equals(_YIBTHIL) ||"2".equals(_YIBTHIL)) {
			_apYear = "19";
		} else {
			_apYear = "20";
		}
		
		// 2. TI1IBK01_H92C01 (고객정보등록변경)
		if(!isUpdate.equals("N")) {		//신규("")거나 수정("Y")인 경우 고객원장 변경 또는 등록.
			// 공통부 세팅
			OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_D92C");
			hostCfg.setImsTranCd("TI1IBK01");
			hostCfg.setInClassCd("D92C");
			hostCfg.setSvcCd("92C");
			hostCfg.setVanTp(IntegrationConstant.SELF_VAN_TYPE);
	
			CbIbk01H92C01Req h92C01Req = cddInfoMapper.toCbIbk01H92C01ReqDto(request);
			
			h92C01Req.setYIJMNO(perBusNo);
			h92C01Req.setYICMFNA(SessionUtils.getSessionValue("CustName"));
			h92C01Req.setYIBTHIL(_apYear + perBusNo.substring(0,6));
			
			h92C01Req.setUserID(CommonBizConstants.DEFAULT_USER_ID);
			h92C01Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
			
			h92C01Req.setYIJHGB(request.getYiJHGB());
			h92C01Req.setYIIDGB(request.getYiIDGB());
	
			
			log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 2. TI1IBK01_H92C01 (고객정보등록변경)");
			OltpResponse<CbIbk01H92C01Res> hostH92Res = hostClient.sendOltp(hostCfg, h92C01Req, CbIbk01H92C01Res.class);
			CbIbk01H92C01Res h92C01Res = hostH92Res.getResponse();
			log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 2. TI1IBK01_H92C01 (고객정보등록변경) = ", h92C01Res);
			//response = cddInfoMapper.toCustCddInfoResponseDto(h92Res);
		}
		
		//3.외화예금인 경우 D92F 조회 및 등록 거래 필요 (D92F), BIB 2021-08-27 SC증권 외화계좌추가
		if(bizType.matches("FCSA|FXSB|SCSF")) {
			log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 3.외화예금인 경우 D92F 조회 및 등록 거래 필요 (D92F), BIB 2021-08-27 SC증권 외화계좌추가");
			// 3. TI1IBK01_H92F (외화고객정보)
			// 공통부 세팅
			OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_D92F");
			hostCfg.setImsTranCd("TI1IBK01");
			hostCfg.setInClassCd("D92F");
			hostCfg.setSvcCd("92F");
			hostCfg.setVanTp(IntegrationConstant.SELF_VAN_TYPE);
			hostCfg.setCaptureSystem("OLTP");
			
			CbIbk01H92F00Req h92FReq = new CbIbk01H92F00Req();
			h92FReq.setUserID(CommonBizConstants.DEFAULT_USER_ID);
			h92FReq.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
			h92FReq.setYIGRGB("3"); //거래구분 (1:등록, 3:조회)
			h92FReq.setYIBRNO(request.getYiCMFJN()); // 점번호
			h92FReq.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));
			h92FReq.setYIEJUSO1(request.getYiEJUSO1());	// 영문주소
			h92FReq.setYIEJUSO2(request.getYiEJUSO2());	// 영문주소
			h92FReq.setYIEBUNHO(request.getYiEBUNHO());	// 전화번호
			
			OltpResponse<CbIbk01H92F00Res> inqHostRes = hostClient.sendOltp(hostCfg, h92FReq, CbIbk01H92F00Res.class);
			CbIbk01H92F00Res h92FRes = inqHostRes.getResponse();

			if(!"Y".equals(h92FRes.getYOFXYN())) {
				h92FReq.setYIGRGB("1"); //거래구분 (1:등록, 3:조회)
				h92FReq.setYIENAME(request.getYiENAME()); // 영문성명
				h92FReq.setYIKNAME(SessionUtils.getSessionValue("CustName")); // 국문성명
				
				OltpResponse<CbIbk01H92F00Res> hostH92fRes = hostClient.sendOltp(hostCfg, h92FReq, CbIbk01H92F00Res.class);
				h92FRes = hostH92fRes.getResponse();
			}
		}
		
		//4.ETB인 경우 파기대상고객 해제거래 및 국가코드등록 (D894) > 거래구분(YIGRGB) 2:출생국
		if("Y".equals(request.getIsEtb())) {
			log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 4.ETB인 경우 파기대상고객 해제거래 및 국가코드등록 (D894) > 거래구분(YIGRGB) 2:출생국");
			
			OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_D894");
			hostCfg.setImsTranCd("TI1IBK01");
			hostCfg.setInClassCd("D894");
			hostCfg.setSvcCd("894");
			hostCfg.setVanTp(IntegrationConstant.SELF_VAN_TYPE);
			hostCfg.setCaptureSystem("OLTP");
			
			CbIbk01H89400Req H894Req = new CbIbk01H89400Req();
			H894Req.setUserID("FIRST894");
			H894Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
			H894Req.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));
			H894Req.setYIGRGB("2"); //거래구분 (1:파기해제, 2:출생국, 3:파기해제+출생국)
			H894Req.setYIKUK(StringUtils.defaultIfEmpty(request.getYoOBIRNC(), "100")); // 출생국 (100. 대한민국)
			
			OltpResponse<CbIbk01H89400Res> hostH894Res = hostClient.sendOltp(hostCfg, H894Req, CbIbk01H89400Res.class);
			CbIbk01H89400Res H894Res = hostH894Res.getResponse();
		}
		
		//5.고객정보 조회/변경 (H920)
		// 고객정보조회하여 직업 찾음
        OltpRequestOptions cbIbk01H920Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");
        cbIbk01H920Options.setImsTranCd("TI1IBK01");
        cbIbk01H920Options.setInClassCd("H920");
        cbIbk01H920Options.setSvcCd("920");
        cbIbk01H920Options.setVanTp(IntegrationConstant.SELF_VAN_TYPE);

        // 개별부 세팅
        CbIbk01H92000Req h920Req = new CbIbk01H92000Req();
        h920Req.setUserID(CommonBizConstants.DEFAULT_USER_ID);
        h920Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
        h920Req.setInJuMinNo(SessionUtils.getSessionValue("PerBusNo", String.class));
        h920Req.setYISALES("Y");
        h920Req.setBKGuBun("2");
        if(bizType.matches("FCSA|FXSB|SCSF")) {
        	h920Req.setYIEADRGB("Y");
        }
        else {
        	h920Req.setYIEADRGB("");
        }
        
        log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_117S] :: 5.고객정보 조회/변경 (H920)");
        OltpResponse<CbIbk01H92000Res> hostH920Res = this.hostClient.sendOltp(cbIbk01H920Options, h920Req, CbIbk01H92000Res.class);
        CbIbk01H92000Res h920Res = hostH920Res.getResponse();
        
        RegistCustInfoResponse resposne = cddInfoMapper.toRegistCustInfoResponseDto(h920Res);
        resposne.setUserID(h920Res.getUserID());
        resposne.setBkGuBun(h920Res.getBKGuBun());
        resposne.setCustName(h920Res.getCustName());
        resposne.setJuMinNo(h920Res.getJuMinNo());
        resposne.setBirthday(h920Res.getBirthday());
        resposne.setSolarLunarCalPart(h920Res.getSolarLunarCalPart());
        resposne.setHomeHT(h920Res.getHomeHT());
        resposne.setHomeJL(h920Res.getHomeJL());
        resposne.setHomePostNum(h920Res.getHomePostNum());
        resposne.setHomeAddr(h920Res.getHomeAddr());
        resposne.setHomeTele1(h920Res.getHomeTele1());
        resposne.setHomeTele2(h920Res.getHomeTele2());
        resposne.setHomeTele3(h920Res.getHomeTele3());
        resposne.setJobTele1(h920Res.getJobTele1());
        resposne.setJobTele2(h920Res.getJobTele2());
        resposne.setJobTele3(h920Res.getJobTele3());
        resposne.setJobPostNum(h920Res.getJobPostNum());
        resposne.setJobAddr(h920Res.getJobAddr());
        resposne.setJobName(h920Res.getJobName());
        resposne.setJobBSName(h920Res.getJobBSName());
        resposne.setJobposition(h920Res.getJobposition());
        resposne.setJob(h920Res.getJob());
        resposne.setEmail(h920Res.getEmail());
        resposne.setHandPhone1(h920Res.getHandPhone1());
        resposne.setHandPhone2(h920Res.getHandPhone2());
        resposne.setHandPhone3(h920Res.getHandPhone3());
        resposne.setWeddingday(h920Res.getWeddingday());
        resposne.setReligion(h920Res.getReligion());
        resposne.setHobby(h920Res.getHobby());
        resposne.setYearPay(h920Res.getYearPay());
        resposne.setMarryYeubu(h920Res.getMarryYeubu());
        resposne.setMailSend(h920Res.getMailSend());
        resposne.setOneTimeDepLimt(h920Res.getOneTimeDepLimt());
        resposne.setOneDayDepLimt(h920Res.getOneDayDepLimt());
        resposne.setInterest(h920Res.getInterest());
        resposne.setSecureLv(h920Res.getSecureLV());
        
        String hpNum = StringUtils.defaultString(SessionUtils.getSessionValue("HpNum"));
        String[] authHpNumArr = BankingBizUtils.retSplitPhoneNumber(hpNum);

        resposne.setAuthMobileNum1(authHpNumArr[0]);
        resposne.setAuthMobileNum2(authHpNumArr[1]);
        resposne.setAuthMobileNum3(authHpNumArr[2]);
        
        //6.신용대출,간편대출인, 모바일 전세보증론, 새희망홀씨론 경우 고객번호(CMF) 필요하므로 고객정보조회 (H92C)
        if(bizType.matches("CRPL|SPPL|MAPL|CMJG|CMJL|NSPL|FHLS|OPPL|DRPL")) {
        	log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 6.신용대출,간편대출인, 모바일 전세보증론, 새희망홀씨론 경우 고객번호(CMF) 필요하므로 고객정보조회 (H92C)");
        	
        	OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_H92C");
    		hostCfg.setImsTranCd("TI1IBK01");
    		hostCfg.setInClassCd("H92C");
    		hostCfg.setSvcCd("92C");
    		hostCfg.setVanTp("56");
    		hostCfg.setCaptureSystem("OLTP");
    		
        	CbIbk01H92C00Req h92C00Req = new CbIbk01H92C00Req();
        	h92C00Req.setUserID(SessionUtils.getSessionValue("UserID"));
        	h92C00Req.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
        	h92C00Req.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));
        	h92C00Req.setYIJHGB("3");
        	h92C00Req.setYIIDGB("2");
    		
    		OltpResponse<CbIbk01H92C00Res> hostH92C00Res = hostClient.sendOltp(hostCfg, h92C00Req, CbIbk01H92C00Res.class);
    		CbIbk01H92C00Res h92C00Res = hostH92C00Res.getResponse();
    		
    		List<CbIbk01H92C00Res.YOMSSU_REC_GRID> yoMSSU_REC = h92C00Res.getYOMSSU_REC();
    		resposne.setYoMSSU(h92C00Res.getYOMSSU());
    		resposne.setYoMSSU_REC(yoMSSU_REC);
    		String yiCMFJN = StringUtils.defaultString(request.getYiCMFJN());
    		
    		Iterator<CbIbk01H92C00Res.YOMSSU_REC_GRID> itr = yoMSSU_REC.iterator();
    		while(itr.hasNext()) {
    			CbIbk01H92C00Res.YOMSSU_REC_GRID recH92C = itr.next();
				if(yiCMFJN.equals(recH92C.getYOACMFBR())) {
					resposne.setYoACMFNO(recH92C.getYOACMFBR());
					break;
				}
			}
        }
		
        //7.개인사업자 정보 저장 - PASS
        if(bizType.equals("BBCA") || bizType.equals("MCDD")) {
        	String YOGEORE = StringUtils.defaultString(request.getYoGEORE()); //거래자구분
        	String YISASAUP = StringUtils.defaultString(request.getYiSASAUP()); //사업자번호
        	
        	if ("5".equals(YOGEORE) && !YISASAUP.equals("")) {
        		log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 7.개인사업자 정보 저장 - PASS!!!");
        		//주요품목 및 서비스 입력 했을 경우 D92G실행
        		// 업무공통 > 개인사업자 등록
        	}
        }
        
        // 8. 진행상태 업데이트
        if(StringUtils.isNotEmpty(bizType) ) {
        	log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: 8. 진행상태 업데이트");
        	RegisterOngoingTradeInfoRequest tradReq = new RegisterOngoingTradeInfoRequest();
        	if(!"FHLS".equals(bizType)){		//담보대출이 아닐경우 
        		tradReq.setCallCntrStsCd("COCU"); //진행상태코드 - 업무별 확인 필요.
				
			}else{
				tradReq.setCallCntrStsCd("HLCU"); //진행상태코드 - 퍼스트홈론일때는 HLCU로(고객정보확인)
			}
        	
        	String tradNo = StringUtils.defaultString(request.getTradNo());
        	String custNo = StringUtils.defaultString(sessionManager.getGlobalValue("CUST_NO", String.class));
        	tradReq.setBizType(bizType);
        	tradReq.setCustNo(custNo);
        	tradReq.setTradNo(StringUtils.defaultIfEmpty(tradNo, sessionManager.getGlobalValue("TRAD_NO", String.class)));
        	tradReq.setCnnctnWay(StringUtils.defaultString(request.getCnnctnWay()));
        	tradReq.setCnnctnTradNo(StringUtils.defaultString(request.getCnnctnTradNo()));
        	tradReq.setReacdn(StringUtils.defaultString(request.getReacdn()));
        	tradReq.setPrdctId(StringUtils.defaultString(request.getPrdctId()));
        	tradReq.setPrdctCd(StringUtils.defaultString(request.getPrdctCd()));
        	tradReq.setPrdctNm(StringUtils.defaultString(request.getPrdctNm()));
        	tradReq.setCddReqCd(StringUtils.defaultString(request.getCddReqCd()));
        	tradReq.setNewUserFlg("Y".equals(request.getIsEtb())? "N" : "Y"); 		// NTB : Y , ETB : N
        	tradReq.setDelObjFlg(StringUtils.defaultString(request.getIsPagi())); 	// 파기여부
        	tradReq.setBrnchNo(StringUtils.defaultString(request.getYiCMFJN()));	// 관리점
        	tradReq.setClerkNo(StringUtils.defaultString(request.getClerkNo()));
        	tradReq.setPerPart(StringUtils.defaultString(request.getYoGEORE()));
        	
        	RegisterOngoingTradeInfoResponse tradRes = tradInfoComponent.registOngoingTradeInfoAndSendLms(tradReq);
        	
        	tradNo = tradRes.getTradNo();
        	resposne.setTradNo(tradNo);
        	resposne.setCustNo(custNo);
        	
        	sessionManager.setGlobalValue("TRAD_NO", tradNo);
        	
        	//CDD전자문서 정보 등록 (NF_EDOC_RCVRY_MGT_I_01)
        	if(StringUtils.isNotEmpty(request.getRcvryData())) {
        		RegisterEdocRecoveryDataRequest rcvryDataReq = new RegisterEdocRecoveryDataRequest();
        		rcvryDataReq.setTradNo(tradNo);
        		rcvryDataReq.setRcvryData(request.getRcvryData());
        		tradInfoComponent.registEdocRecoveryData(rcvryDataReq);
        	}
        }
        
        // TODO : 오류여도 proccess 진행
        if(StringUtils.isNotEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class))) {
    		verificationComponent.checkUserCiInfo();
    	}
        
        String outputJsonString = BankingBizUtils.toJSONStringFromObject(resposne);
        resposne.setOutputJsonString(outputJsonString);
        
        log.debug("고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] :: resposne = ", resposne);
		return resposne;
	}
	

	@ComponentOperation(name = "파기대상고객정보 신규등록 [MA3CMMBIZ001_114S]", author = "")
	public RegistDestructionCustInfoResponse registDestructionCustInfo(RegistDestructionCustInfoRequest request) {
		RegistDestructionCustInfoResponse response = new RegistDestructionCustInfoResponse();
		
		if( StringUtils.defaultIfEmpty(request.getCnnctnWay(), "").length() > 6 ) {
			PRCServiceException prcExcception = new PRCServiceException("PRCCMM0070"); // 제휴처코드가 정상적이지 않아 처리중 오류가 발생하였습니다.
			prcExcception.setNextPage("MA3PRDALL001");
			
			throw prcExcception;
		}
		
		if( StringUtils.defaultIfEmpty(request.getReaCdn(), "").length() > 6 ) {
			PRCServiceException prcExcception = new PRCServiceException("PRCCMM0071"); // REA코드가 정상적이지 않아 처리중 오류가 발생하였습니다.
			prcExcception.setNextPage("MA3PRDALL001");
			
			throw prcExcception;
		}
		
		String custNo = StringUtils.defaultString(sessionManager.getGlobalValue("CUST_NO", String.class));
		String yiCddGb = StringUtils.defaultString(request.getYiCddGb());
		
		
		// 공통부 세팅
		OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_D894");
		hostCfg.setImsTranCd("TI1IBK01");
		hostCfg.setInClassCd("D894");
		hostCfg.setSvcCd("894");
		hostCfg.setVanTp("56");
		hostCfg.setCaptureSystem("OLTP");

		CbIbk01H89400Req h894Req = new CbIbk01H89400Req();
		
		h894Req.setUserID("FIRST894");
		h894Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
		h894Req.setYIKUK("");
		h894Req.setYICDDGB(yiCddGb);
		h894Req.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));
		h894Req.setYIJAGSRC(request.getYiJagSrc());
		h894Req.setYIUSECD(request.getYiUseCd());
		h894Req.setYIYSGUPPR(request.getYiYsGupPr());
		h894Req.setYIYSAUM(request.getYiYsAum());
		h894Req.setYIISIC(request.getYiIsIc());
		h894Req.setYIWTYPE(request.getYiWType());
		h894Req.setYIDTABYN(request.getYiDtabYn());
		h894Req.setYIDTABAUM(request.getYiDtabAum());
		h894Req.setYIDTABSU(request.getYiDtabSu());
		h894Req.setYICHANGB(request.getYiChanGb());
		
		//개인사업자정보 추가
		h894Req.setYISAUPIL(request.getYiSaupIl());
		h894Req.setYIIPJIGS(request.getYiIpjiGs());
		h894Req.setYIIPJIAK(request.getYiIpjiAk());
		h894Req.setYIMAECAK(request.getYiMaecAk());
		h894Req.setYISASAUP(request.getYiSaSaup());
		
		//CDD자동화
		//NTB, ETB, CDD overDue 대상 입력값 추가
		h894Req.setYIENAYN(request.getYiENayn());
		h894Req.setYIENAME(request.getYiEname());
		h894Req.setYIRSDYN(request.getYiRsdYn());
		h894Req.setYIFATYN(request.getYiFatYn());
		h894Req.setYIFNAME(request.getYiFname());
		h894Req.setYIGKCD(request.getYiGkCd());
		h894Req.setYIAUSCD(request.getYiAusCd());
		h894Req.setYIJJSRC(request.getYiJjSrc());
		h894Req.setYIJISIC(request.getYiJiSic());
		h894Req.setYIBISIC(request.getYiBiSic());
		h894Req.setYIMAUM(request.getYiMAum());
		h894Req.setYIGRGB(!"Y".equals(yiCddGb)?"1":"2");//거래구분(1:파기해제, 2:출생국, 3:파기해제+출생국)
		
		// 전문 호출
		OltpResponse<CbIbk01H89400Res> hostResponse = hostClient.sendOltp(hostCfg, h894Req, CbIbk01H89400Res.class);
		
		CbIbk01H89400Res h894Res = hostResponse.getResponse();
		
		response.setUserId(h894Res.getUserID());
		response.setYoGrGb(h894Res.getYOGRGB());
		response.setYoKuk(h894Res.getYOKUK());
		
		if (!"".equals(custNo) && !"Y".equals(yiCddGb)) {
			// 진행상태 심플정보 등록
			RegisterOngoingTradeInfoParameter param = new RegisterOngoingTradeInfoParameter();
			param.setCustNo(custNo);
			param.setPrdctCd("");
			param.setPrdctNm(StringUtils.nvl(request.getPrdctNm(), ""));
			param.setCallCntrStsCd(StringUtils.nvl(request.getCallCntrStsCd(), ""));
			param.setPrgrssStsCd(StringUtils.nvl(request.getPrgrssStsCd(), ""));
			param.setClerkNo(StringUtils.nvl(request.getClerkNo(), ""));
			param.setCnnctnWay(request.getCnnctnWay());
			param.setTradRegGb( ("0"));
			param.setReaCd(request.getReaCdn());
			// MA3_CMM_BIZ.NF_TRADINFO_MGT_I_01
			nfTradeInfoMgtDao.insertOngoingTradeSimpleInfo(param);
		}
		
		// 해외비지니스 거래국가정보 DB저장
		String gaesaCodeStr = StringUtils.defaultString(request.getGaesaCode());
		if (StringUtils.isNotEmpty(gaesaCodeStr)) {
			//TODO MA3CMMBIZ018_301S
			ArrayList gaesaServiceList = BankingBizUtils.toJSONFromStr(gaesaCodeStr, ArrayList.class);
			
			if (gaesaServiceList != null && gaesaServiceList.size() > 0) {
				RegistIntlTradInfoRequest tradInfoReq = new RegistIntlTradInfoRequest();
				
				tradInfoReq.setCustNo(StringUtils.nvl(request.getCustNo(), custNo));
				tradInfoReq.setTradNo(StringUtils.nvl(request.getTradNo(), SessionUtils.getSessionValue("TRAD_NO")));
				tradInfoReq.setGaesaCode(gaesaCodeStr);
				tradInfoReq.setCodeType(StringUtils.nvl(request.getCodeType(), "1"));
				
				tradInfoComponent.registIntlTradInfo(tradInfoReq);
			}
		}
		
		response.setDustYn("Y");
		response.setNewYn("Y");
		
		log.debug("파기대상고객정보 신규등록 [MA3CMMBIZ001_114S] response => {}", response);
		
		return response;
	}
	

	/**
	 * 비대면인증 고객관리 조회
	 *
	 * @param registerStatusAndPartnerTransaction
	 * @return
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_101S
	 */
	@ComponentOperation(name = "비대면인증 고객관리 조회 [ASIS:MA3CMMBIZ010_101S]", author = "")
	public NonFaceCustomerInfoInquiryResponse getNonFaceCustomerInfo(NonFaceCustomerInfoInquiryRequest request) {

		log.debug("####################### 비대면인증 고객관리 조회 [ASIS:MA3CMMBIZ010_101S] START #######################");

		NonFaceCustomerInfoInquiryResponse response = new NonFaceCustomerInfoInquiryResponse();

		NonFaceCustomerInfoParameter inqDbParam = new NonFaceCustomerInfoParameter();

		inqDbParam.setSsn(request.getSsn());

		NonFaceCustomerInfoInquiryResult dbResponse = new NonFaceCustomerInfoInquiryResult();

		dbResponse = nfCustMgtDao.selectNonFaceCustomerInfo(inqDbParam);

		response = nonFaceCustomerInfoMapper.toNonFaceCustInfoResponseDto(dbResponse);

		log.debug("####################### 비대면인증 고객관리 조회 [ASIS:MA3CMMBIZ010_101S] END #######################");
		log.debug("🏃‍➡️🏃‍➡️🏃‍➡️ {} 🏃🏃🏃", response);
		log.debug("#################################################################################################");

		return response;
	}
	
	/**
	 * 비대면인증 고객관리 수정
	 *
	 * @param registerStatusAndPartnerTransaction
	 * @return
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_102S
	 */
	@ComponentOperation(name = "비대면인증 고객관리 수정 [ASIS:MA3CMMBIZ010_102S]", author = "")
	public RegisterNonFaceCustomerInfoResponse updateNonFaceCustomerInfo(RegisterNonFaceCustomerInfoRequest request) {

		log.debug("####################### 비대면인증 고객관리 수정 [ASIS:MA3CMMBIZ010_102S] START #######################");

		RegisterNonFaceCustomerInfoResponse response = new RegisterNonFaceCustomerInfoResponse();

		NonFaceCustomerInfoParameter dbUpParam = new NonFaceCustomerInfoParameter();

		dbUpParam.setSsn(request.getSsn());
		dbUpParam.setMblphnNo(request.getMblphnNo());
		dbUpParam.setUserNm(request.getUserNm());
		dbUpParam.setCmpndCheckKey(request.getCmpndCheckKey());
		dbUpParam.setSrcNewUserFlg(request.getSrcNewUserFlg());
		dbUpParam.setNewUserFlg(request.getNewUserFlg());
		dbUpParam.setSrcDelObjFlg(request.getSrcDelObjFlg());
		dbUpParam.setDelObjFlg(request.getDelObjFlg());
		
		int result = nfCustMgtDao.updateNonFaceCustomerInfo(dbUpParam);

		log.debug("####################### 비대면인증 고객관리 조회 [ASIS:MA3CMMBIZ010_101S] END #######################");
		log.debug("🏃‍➡️🏃‍➡️🏃‍➡️ {} 🏃🏃🏃", result);
		log.debug("#################################################################################################");

		return response;
	}
}
