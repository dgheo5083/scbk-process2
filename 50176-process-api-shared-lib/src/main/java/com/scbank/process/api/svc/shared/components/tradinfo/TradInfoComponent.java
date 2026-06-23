package com.scbank.process.api.svc.shared.components.tradinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scbank.process.api.edmi.dto.mci.MciDy7200170001Req;
import com.scbank.process.api.edmi.dto.mci.MciDy7200170001Res;
import com.scbank.process.api.edmi.dto.mci.MciYp0049010001Req;
import com.scbank.process.api.edmi.dto.mci.MciYp0049010001Res;
import com.scbank.process.api.edmi.dto.mci.MciYp0049020001Req;
import com.scbank.process.api.edmi.dto.mci.MciYp0049020001Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo.yoMyInfRecList;
import com.scbank.process.api.svc.shared.components.account.dto.session.NewAccountInfoSession;
import com.scbank.process.api.svc.shared.components.customer.dao.NfCustomerMgtDao;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoParameter;
import com.scbank.process.api.svc.shared.components.lms.LmsComponent;
import com.scbank.process.api.svc.shared.components.lms.dto.LmsRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.FnanclPrdctCommDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfCddEdocRcvryMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfCnnctnTradInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfScrapingMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfScreenInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfScrnInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoDtlDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoHistMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.CddElectronicDocumentParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.CheckOngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.MainDepositAccountProductResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NfScrnInfoInqiryParam;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NfScrnInfoInqiryReult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScrapingInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScreenAndScrapingInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScreenAndScrapingInfoResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceTradeInfoDetailParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceTradeInfoHistoryParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoCancelParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoInquiryParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterNonFaceScreenInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterOngoingTradeInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterPartnerTradeInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.TradeInfoInquiryParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.TradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.CheckOngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.NfScrnInfoInqiryRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.NfScrnInfoInqiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegistIntlTradInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegistIntlTradInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterNonFaceScreenInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterNonFaceScreenInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterScrapingDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterScrapingDataResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.mapper.TradeInfoMapper;
import com.scbank.process.api.svc.shared.dao.NfTradinfoBizcdMgtDao;
import com.scbank.process.api.svc.shared.dao.dto.TradInfoBizCdMgtParam;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 거래정보관리
 * 
 * - 진행중인 업무 상태별 목록 조회 (asis : MA3CMMBIZ010_104S)
 * - 진행중인 업무 단건 조회 (asis : MA3CMMBIZ010_106S)
 * - 진행상태 등록/수정(ScrnData/RcvryData 등록) (asis : MA3CMMBIZ010_105S)
 * - 진행상태 등록/수정(LMS발송) (asis : MA3CMMBIZ010_10AS)
 * - 진행상태 등록/수정(거래처거래정보 처리) (asis : MA3CMMBIZ010_103S)
 * - 진행중인 상품 취소 처리 (asis : MA3CMMBIZ010_107S)
 * - UI 진행상태 업데이트 처리 – 대출 취소(MCI) (asis : MA3CMMBIZ010_108S)
 * - 비대면 화면정보관리 등록 (asis : MA3CMMBIZ010_109S)
 * - 스크래핑데이터 저장 (asis : MA3CMMBIZ015_101S)
 * - 화면정보 및 스크래핑 데이터 조회 (asis : MA3CMMBIZ015_102S)
 * - 복구데이터 등록 (asis : MA3CMMBIZ013_101S)
 * </pre>
 */
@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class TradInfoComponent {

	/**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;
    
    private final LmsComponent lmsComponent;
    private final HostClient hostClient;
    
    private final AccountListComponent accountListComponent;
    
	private final NfTradeInfoMgtDao nfTradeInfoMgtDao;
	private final FnanclPrdctCommDao fnanclPrdctCmmnDao;
	private final NfTradeInfoHistMgtDao nfTradeInfoHistMgtDao;
	private final NfCnnctnTradInfoMgtDao nfCnnctnTradInfoMgtDao;
	private final NfCustomerMgtDao nfCustMgtDao;
	private final NfTradeInfoDtlDao nfTradeInfoDtlDao;
	private final NfScreenInfoMgtDao nfScreenInfoMgtDao;
	private final NfScrapingMgtDao nfScrapingMgtDao;
	private final NfCddEdocRcvryMgtDao nfCddEdocRcvryMgtDao;
	private final NfScrnInfoMgtDao nfScrnInfoMgtDao;
	private final NfTradinfoBizcdMgtDao nfTradinfoBizcdMgtDao;
	
	private final TradeInfoMapper tradeInfoMapper;
	
	/**
	 * <pre>
	 * 진행중인 업무 상태별 목록 조회
	 * </pre>
	 * @param request OngoingTradeInfoInquiryRequest
	 * @return CheckOngoingTradeInfoInquiryResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_104S
	 */
	@ComponentOperation(name = "진행중인 업무 상태별 목록 조회 (asis : MA3CMMBIZ010_104S)")
	public CheckOngoingTradeInfoInquiryResponse getCheckOngoingTradeInfo(OngoingTradeInfoInquiryRequest request) throws PRCServiceException {
		
		log.debug("####################### 진행중인 업무 상태별 목록 조회 [ASIS:MA3CMMBIZ010_106S] START ###\n", request);
		CheckOngoingTradeInfoInquiryResponse response = new CheckOngoingTradeInfoInquiryResponse();
		
		boolean isLoginOrAuth = SessionUtils.isLoginOrAuth("LOGIN") || SessionUtils.isLoginOrAuth("AUTH");
		
		String INTEGRATED_CONSELING_YN = StringUtils.defaultIfEmpty(request.getIntegratedConselingYn(), "");			//대출 통합상담여부
		String BIZ_TYPE = StringUtils.defaultIfEmpty(request.getBizType(), StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("BIZ_TYPE"), ""));	//거래 후 현재상품과 동일상품인지 비교 시 사용
		String PRDCT_ID = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PRDCT_ID", String.class), 
							StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PRDCT_ID", String.class), ""));	//거래 후 현재상품과 동일상품인지 비교 시 사용
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), SessionUtils.getSessionValue("CUST_NO"));
		
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ BIZ_TYPE ] -> " + BIZ_TYPE);
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ PRDCT_ID ] -> " + PRDCT_ID);
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ CUST_NO ] -> " + custNo);
		
		//CDD 프로세스인 경우 고객번호(CUST_NO) 조회 or 드림론이벤트인 경우
		if("MCDD".equals(BIZ_TYPE) || "DLEV".equals(BIZ_TYPE)) {
			custNo = getCustNo();
		}
		
		if(!isLoginOrAuth) {
			response.setSuccessYn("Y");
			return response;
		}
				
		if(StringUtils.isEmpty(custNo)) {
			// 2025.12 '미성년자 또는 외국인'의 경우 해당 서비스로 인입시 세션에 고객번호(CUST_NO)가 없으므로, 고객번호가 없다면 주민번호로 고객번호를 조회한다.
			custNo = getCustNo();
		}
		
		OngoingTradeInfoInquiryParameter parameter = new OngoingTradeInfoInquiryParameter();
		parameter.setCustNo(custNo);
		parameter.setTradRegGb( ("Y".equals(StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("isCSL", String.class), "")) ? "1" : "0") );
		
		//진행중인 업무 조회
		List<CheckOngoingTradeInfoInquiryResult> resultNF_TRADINFO_MGT_S_03 = nfTradeInfoMgtDao.selectOngoingTradeInfoList(parameter);
		
		response.setSuccessYn("Y");
		response.setPrdctCnt(0);
		
		if(StringUtils.isEmpty(request.getBizType())) {
			return response;
		}
		
		if(resultNF_TRADINFO_MGT_S_03 != null) {
			if ("TDSB".equals(request.getBizType())) {
				//메인 입출금계좌 상품 리스트 : FNANCL_PRDCT_CMMN_S_01
				List<MainDepositAccountProductResult> resultFNANCL_PRDCT_CMMN_S_01 = fnanclPrdctCmmnDao.selectMainDepositAccountProduct();
				
				Optional.ofNullable(resultFNANCL_PRDCT_CMMN_S_01).ifPresent(list -> list.forEach(item -> {
                    if (String.valueOf(item.getPrdctCd()).indexOf("e-green") > -1) {
                    	response.setPrdctIdE(item.getPrdctId());
                    	response.setPrdctCdE(item.getPrdctCd());
                    }
                }));
			}
			
			CheckOngoingTradeInfoInquiryResponse chkProcessStateOutput = chkProcessState(resultNF_TRADINFO_MGT_S_03, BIZ_TYPE, INTEGRATED_CONSELING_YN);

			response.getOriginal().addAll(chkProcessStateOutput.getOriginal());
            if (chkProcessStateOutput.getPageMoveArr() != null && !chkProcessStateOutput.getPageMoveArr().isEmpty())
            	response.getPageMoveArr().addAll(chkProcessStateOutput.getPageMoveArr());

            if (chkProcessStateOutput.getCancelPopupArr() != null
                    && !chkProcessStateOutput.getCancelPopupArr().isEmpty())
            	response.getCancelPopupArr().addAll(chkProcessStateOutput.getCancelPopupArr());

            if (chkProcessStateOutput.getScreeningPopupArr() != null
                    && !chkProcessStateOutput.getScreeningPopupArr().isEmpty())
            	response.getScreeningPopupArr().addAll(chkProcessStateOutput.getScreeningPopupArr());

            response.setPrdctCnt((response.getPageMoveArr().size() + response.getCancelPopupArr().size() + response.getScreeningPopupArr().size()));
		}
		
		response.setBizType(BIZ_TYPE);
		response.setPrdctId(PRDCT_ID);
		log.debug("####################### 진행중인 업무 상태별 목록 조회 [ASIS:MA3CMMBIZ010_106S] END ###\n", response);
		return response;
	}
	
	/**
	 * <pre>
	 * 진행중인 업무 단건 조회
	 * </pre>
	 * @param request OngoingTradeInfoInquiryRequest
	 * @return OngoingTradeInfoInquiryResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_106S
	 */
	@ComponentOperation(name = "진행중인 업무 단건 조회 (asis : MA3CMMBIZ010_106S)") 
	public OngoingTradeInfoInquiryResponse getOngoingTradeInfo(OngoingTradeInfoInquiryRequest request) throws PRCServiceException {
		
		log.debug("####################### 진행중인 업무 단건 조회 [MA3CMMBIZ010_106S] START ###\n", request);
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), SessionUtils.getSessionValue("CUST_NO"));
        custNo = StringUtils.defaultIfEmpty(custNo, getCustNo());
		
		OngoingTradeInfoInquiryParameter parameter = new OngoingTradeInfoInquiryParameter();
		parameter.setCustNo(custNo);
		parameter.setBizType(StringUtils.nvl(request.getBizType(), ""));
		parameter.setTradRegGb(
                ("Y".equals(StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("isCSL", String.class), ""))
                        ? "1"
                        : "0"));
		parameter.setIntegratedConselingYn(StringUtils.nvl(request.getIntegratedConselingYn(), ""));
		
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ CUST_NO ] -> " + parameter.getCustNo());
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ BIZ_TYPE ] -> " + parameter.getBizType());
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ TRAD_REG_GB ] -> " + parameter.getTradRegGb());
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_104S [ INTEGRATED_CONSELING_YN ] -> " + parameter.getIntegratedConselingYn());
		
		// 진행중인 업무 조회 (asis : NF_TRADINFO_MGT_S_02)
		OngoingTradeInfoInquiryResult result = nfTradeInfoMgtDao.selectOngoingTradeInfo(parameter);
		
		OngoingTradeInfoInquiryResponse response = tradeInfoMapper.toOngoingTradeInfoResponse(result);
		//if(response == null) response = new OngoingTradeInfoInquiryResponse();
		
		log.debug("####################### 진행중인 업무 단건 조회 [MA3CMMBIZ010_106S] END ###\n", response);
		return response;
	}
	
	/**
	 * <pre>
	 * 진행상태 등록/수정, ScrnData/RcvryData 등록
	 * </pre>
	 * @param request RegisterOngoingTradeInfoDataRequest
	 * @return RegisterOngoingTradeInfoResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_105S
	 */
	@SuppressWarnings("unchecked")
	@ComponentOperation(name = "진행상태 등록/수정, ScrnData/RcvryData 등록 (asis : MA3CMMBIZ010_105S)") 
	public RegisterOngoingTradeInfoResponse registOngoingTradeInfoAndScrnDataRcvryData(RegisterOngoingTradeInfoDataRequest request) throws PRCServiceException {
		RegisterOngoingTradeInfoResponse response = new RegisterOngoingTradeInfoResponse();

		log.debug("####################### 진행상태 등록/수정, ScrnData/RcvryData 등록 [MA3CMMBIZ010_105S] START ###\n", request);
		
        HashMap<String, Object> paramInfo = new HashMap<String, Object>();
        
        String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), SessionUtils.getSessionValue("CUST_NO"));
        custNo = StringUtils.defaultIfEmpty(custNo, getCustNo());
        String tradNo = StringUtils.defaultIfEmpty(request.getTradNo(), sessionManager.getGlobalValue("TRAD_NO", String.class));
        String bizType = StringUtils.defaultIfEmpty(request.getBizType(), "");
        String tradInfo = StringUtils.defaultIfEmpty(request.getTradInfo(), "");
        String callCntrStsCd = StringUtils.defaultIfEmpty(request.getCallCntrStsCd(), "");
        String prgrssStsCd = StringUtils.defaultIfEmpty(request.getPrgrssStsCd(), "");
        String scrnDataInfo = StringUtils.defaultIfEmpty(request.getScrnDataInfo(), "");
        String cnnctnTradNo = StringUtils.defaultIfEmpty(request.getCnnctnTradNo(), "");
        String cnnctnWay = StringUtils.defaultIfEmpty(request.getCnnctnWay(), "");
        String recoveryData = StringUtils.defaultIfEmpty(request.getRcvryData(), "");
        String screenInfoOnly = StringUtils.defaultIfEmpty(request.getScrnInfoOnly(), ""); // SCRN_DATA_INFO만
        String skipSendLMS = StringUtils.defaultIfEmpty(request.getSkipSendLms(), ""); // 진행상태 DONE인 경우 LMS 발송 Skip하는지 여부
        String newPrdctNum = StringUtils.defaultIfEmpty(request.getNewPrdctNum(), ""); // 신규상품노출을 위한 전달값
        String newPrdctCurrency = StringUtils.defaultIfEmpty(request.getNewPrdctCurrency(), ""); // 신규상품노출을 위한 전달값
        String reaCdn = StringUtils.defaultIfEmpty(request.getReacdn(), "");

        // 대출이동제 필드 추가.
        String preLoanMoveYn = StringUtils.defaultIfEmpty(request.getPreLoanMoveYn(), ""); // 대출이동제 여부(상담)
        String realLoanMoveYn = StringUtils.defaultIfEmpty(request.getRealLoanMoveYn(), ""); // 대출이동제 여부(신청)
        String hqPrimeRateApplyYn = StringUtils.defaultIfEmpty(request.getHqPrimeRateApplyYn(), ""); // 본부우대금리 적용여부
        String hqPrimeRate = StringUtils.defaultIfEmpty(request.getHqPrimeRate(), ""); // 본부우대금리

        // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
        String loanPurpose = StringUtils.defaultIfEmpty(request.getLoanPurpose(), ""); // 대출목적
        String collateralType = StringUtils.defaultIfEmpty(request.getCollateralType(), ""); // 담보유형

        // 24.09.06 SC 제일 대출여부 추가
        String OPL_CNSLT_YN = StringUtils.defaultIfEmpty(request.getOplCnsltYn(), ""); // SC 제일 대출여부 추가

        if(StringUtils.hasLength(tradInfo)) {
			tradInfo = unescape(tradInfo);
		}
		if(StringUtils.hasLength(scrnDataInfo)) {
			scrnDataInfo = unescape(scrnDataInfo);
		}
		if(StringUtils.hasLength(recoveryData)) {
			recoveryData = unescape(recoveryData);
		}
		
        if (StringUtils.isNotEmpty(tradInfo)) {
        	paramInfo = BankingBizUtils.toJSONFromStr(tradInfo, HashMap.class);
        }	
       
        paramInfo.put("BIZ_TYPE", bizType);
        paramInfo.put("TRAD_NO", tradNo);
        
        if (!"Y".equals(screenInfoOnly)) {
	        paramInfo.put("CUST_NO", custNo);
	        paramInfo.put("CALL_CNTR_STS_CD", StringUtils.defaultIfEmpty(request.getCallCntrStsCd(), ""));
	        paramInfo.put("PRGRSS_STS_CD", prgrssStsCd);
	
	        if(StringUtils.isNotEmpty(cnnctnTradNo)) {
	        	paramInfo.put("CNNCTN_TRAD_NO", cnnctnTradNo);
	        }
	
	        if(StringUtils.isNotEmpty(cnnctnWay)) {
	        	paramInfo.put("CNNCTN_WAY", cnnctnWay);
	        }
	
	        if(StringUtils.isNotEmpty(reaCdn)) {
	        	paramInfo.put("REACDN", reaCdn);
	        }
	
	        if (StringUtils.isEmpty(tradNo)) {
	            // 진행상태 등록
	        	RegisterOngoingTradeInfoRequest paramInsTradInfo = new RegisterOngoingTradeInfoRequest();
	
	            paramInsTradInfo.setCustNo(custNo);
	            paramInsTradInfo.setBizType(bizType);
	            paramInsTradInfo.setPrdctId(String.valueOf(paramInfo.get("PRDCT_ID") != null ? paramInfo.get("PRDCT_ID") : ""));
	            paramInsTradInfo.setPrdctCd(String.valueOf(paramInfo.get("PRDCT_CD") != null ? paramInfo.get("PRDCT_CD") : ""));
	            paramInsTradInfo.setPrdctNm(String.valueOf(paramInfo.get("PRDCT_NM") != null ? paramInfo.get("PRDCT_NM") : ""));
	            paramInsTradInfo.setCallCntrStsCd(StringUtils.defaultIfEmpty(callCntrStsCd, "COCU"));
	            paramInsTradInfo.setCnnctnWay(String.valueOf(paramInfo.get("CNNCTN_WAY") != null ? paramInfo.get("CNNCTN_WAY") : ""));
	            paramInsTradInfo.setClerkNo(String.valueOf(paramInfo.get("CLERK_NO") != null ? paramInfo.get("CLERK_NO") : ""));
	            paramInsTradInfo.setCddReqCd(String.valueOf(paramInfo.get("CDD_REQ_CD") != null ? paramInfo.get("CDD_REQ_CD") : ""));
	            paramInsTradInfo.setIdCardCd(("CASA".equals(bizType) || "BBCA".equals(bizType)) ? "N" : String.valueOf(paramInfo.get("CDD_REQ_CD") != null ? paramInfo.get("CDD_REQ_CD") : ""));
	            paramInsTradInfo.setNewUserFlg(String.valueOf(paramInfo.get("NEW_USER_FLG") != null ? paramInfo.get("NEW_USER_FLG") : ""));
	            paramInsTradInfo.setDelObjFlg(String.valueOf(paramInfo.get("DEL_OBJ_FLG") != null ? paramInfo.get("DEL_OBJ_FLG"): ""));
	            paramInsTradInfo.setBrnchNo(String.valueOf(paramInfo.get("BRNCH_NO") != null ? paramInfo.get("BRNCH_NO"): ""));
	            paramInsTradInfo.setErrCd(String.valueOf(paramInfo.get("ERR_CD") != null ? paramInfo.get("ERR_CD") : ""));
	            paramInsTradInfo.setErrMsg(String.valueOf(paramInfo.get("ERR_MSG") != null ? paramInfo.get("ERR_MSG") : ""));
	            paramInsTradInfo.setCnnctnTradNo(String.valueOf(paramInfo.get("CNNCTN_TRAD_NO") != null ? paramInfo.get("CNNCTN_TRAD_NO") : ""));
	            paramInsTradInfo.setPerPart(String.valueOf(paramInfo.get("PER_PART") != null ? paramInfo.get("PER_PART") : ""));
	            paramInsTradInfo.setDocEvdcCd(String.valueOf(paramInfo.get("DOC_EVCD_CD") != null ? paramInfo.get("DOC_EVCD_CD") : ""));
	            paramInsTradInfo.setHlPrsnCd(String.valueOf(paramInfo.get("HL_PRSN_CD") != null ? paramInfo.get("HL_PRSN_CD") : "")); // 무주택자확인
	            paramInsTradInfo.setAdSbstCd(String.valueOf(paramInfo.get("AD_SBST_CD") != null ? paramInfo.get("AD_SBST_CD") : "")); // 사전청약확인
	            paramInsTradInfo.setKwInsrCd(String.valueOf(paramInfo.get("KW_INSR_CD") != null ? paramInfo.get("KW_INSR_CD") : "")); // 권원보험확인
	            paramInsTradInfo.setReacdn(String.valueOf(paramInfo.get("REACDN") != null ? paramInfo.get("REACDN") : ""));
	            paramInsTradInfo.setIntegratedConselingYn(String.valueOf(paramInfo.get("INTEGRATED_CONSELING_YN") != null ? paramInfo.get("INTEGRATED_CONSELING_YN") : "")); // 통합대출여부
	
	            paramInsTradInfo.setGovmDataType(String.valueOf(paramInfo.get("GOVM_DATA_TYPE") != null ? paramInfo.get("GOVM_DATA_TYPE") : "")); // 스크래핑타입 - G:공공데이터,  S:스크래핑
	            paramInsTradInfo.setGovmSignType(String.valueOf(paramInfo.get("GOVM_SIGN_TYPE") != null ? paramInfo.get("GOVM_SIGN_TYPE") : "")); // 서명타입 - 1:공동, B:금융 pin, C:금융생체, 9:디지털
	            paramInsTradInfo.setGovmSignErr(""); // 추가 적재내용필요시 해당필드 사용예정
	
	            /* 2023-03-14 대출이동여부 추가 */
	            paramInsTradInfo.setPreLoanMoveYn(StringUtils.defaultIfEmpty(preLoanMoveYn, String.valueOf(paramInfo.get("PRE_LOAN_MOVE_YN") != null ? paramInfo.get("PRE_LOAN_MOVE_YN") : ""))); // 대출이동여부-상담
	            paramInsTradInfo.setRealLoanMoveYn(StringUtils.defaultIfEmpty(realLoanMoveYn, String.valueOf( paramInfo.get("REAL_LOAN_MOVE_YN") != null ? paramInfo.get("REAL_LOAN_MOVE_YN") : ""))); // 대출이동여부-신청
	
	            // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
	            paramInsTradInfo.setLoanPurpose(StringUtils.defaultIfEmpty(loanPurpose, String.valueOf(paramInfo.get("LOAN_PURPOSE") != null ? paramInfo.get("LOAN_PURPOSE") : ""))); // 대출목적
	            paramInsTradInfo.setCollateralType(StringUtils.defaultIfEmpty(collateralType, String.valueOf(paramInfo.get("COLLATERAL_TYPE") != null ? paramInfo.get("COLLATERAL_TYPE") : ""))); // 담보유형
	
	            // 24.09.06 SC 제일 대출여부 추가
	            paramInsTradInfo.setOplCnsltYn(StringUtils.defaultIfEmpty(OPL_CNSLT_YN, String.valueOf(paramInfo.get("OPL_CNSLT_YN") != null ? paramInfo.get("OPL_CNSLT_YN") : ""))); // SC 제일 대출여부 추가
	
	            RegisterOngoingTradeInfoResponse nonFacdScreenInfoManagementResponse = new RegisterOngoingTradeInfoResponse();
	            nonFacdScreenInfoManagementResponse = registOngoingTradeInfoAndSendLms(paramInsTradInfo);
	            // 진행상태 등록/수정(LMS발송) (asis : MA3CMMBIZ010_10AS)
	            tradNo = nonFacdScreenInfoManagementResponse.getTradNo();
	        } else {
	                // 진행상태 변경
	        	RegisterOngoingTradeInfoRequest paramUpdTradInfo = new RegisterOngoingTradeInfoRequest();
	
	            paramUpdTradInfo.setCustNo(custNo);
	            paramUpdTradInfo.setTradNo(tradNo);
	            paramUpdTradInfo.setBizType(bizType);
	            paramUpdTradInfo.setPrdctId(String.valueOf(paramInfo.get("PRDCT_ID") != null ? paramInfo.get("PRDCT_ID") : ""));
	            paramUpdTradInfo.setPrdctCd(String.valueOf(paramInfo.get("PRDCT_CD") != null ? paramInfo.get("PRDCT_CD") : ""));
	            paramUpdTradInfo.setPrdctNm(paramInfo.get("PRDCT_NM") != null ? String.valueOf(paramInfo.get("PRDCT_NM")) : StringUtils.defaultIfEmpty(request.getPrdctNm(), ""));
	            paramUpdTradInfo.setCallCntrStsCd(callCntrStsCd);
	            paramUpdTradInfo.setPrgrssStsCd(prgrssStsCd);
	            paramUpdTradInfo.setCnnctnWay(String.valueOf(paramInfo.get("CNNCTN_WAY") != null ? paramInfo.get("CNNCTN_WAY") : ""));
	            paramUpdTradInfo.setClerkNo(String.valueOf(paramInfo.get("CLERK_NO") != null ? paramInfo.get("CLERK_NO") : ""));
	            paramUpdTradInfo.setCddReqCd(String.valueOf(paramInfo.get("CDD_REQ_CD") != null ? paramInfo.get("CDD_REQ_CD") : ""));
	            paramUpdTradInfo.setIdCardCd(String.valueOf(paramInfo.get("ID_CARD_CD") != null ? paramInfo.get("ID_CARD_CD") : ""));
	            paramUpdTradInfo.setIdCardCnt(String.valueOf(paramInfo.get("ID_CARD_CNT") != null ? paramInfo.get("ID_CARD_CNT") : ""));
	            paramUpdTradInfo.setBrnchNo(String.valueOf(paramInfo.get("BRNCH_NO") != null ? paramInfo.get("BRNCH_NO") : ""));
	            paramUpdTradInfo.setReqAmt(String.valueOf(paramInfo.get("REQ_AMT") != null ? paramInfo.get("REQ_AMT") : ""));
	            paramUpdTradInfo.setAuthntIndCd(String.valueOf(paramInfo.get("AUTHNT_IND_CD") != null ? paramInfo.get("AUTHNT_IND_CD") : ""));
	            paramUpdTradInfo.setAuthntInd(String.valueOf(paramInfo.get("AUTHNT_REQ_DT") != null ? paramInfo.get("AUTHNT_REQ_DT") : ""));
	            paramUpdTradInfo.setAuthntReqCmpltnDt(String.valueOf(paramInfo.get("AUTHNT_REQ_CMPLTN_DT") != null ? paramInfo.get("AUTHNT_REQ_CMPLTN_DT") : ""));
	            paramUpdTradInfo.setKfbAcctNo(String.valueOf(paramInfo.get("KFB_ACCT_NO") != null ? paramInfo.get("KFB_ACCT_NO") : ""));
	            paramUpdTradInfo.setAlliancCd(String.valueOf(paramInfo.get("ALLIANC_CD") != null ? paramInfo.get("ALLIANC_CD") : ""));
	            paramUpdTradInfo.setEffctvInt(String.valueOf(paramInfo.get("EFFCTV_INT") != null ? paramInfo.get("EFFCTV_INT") : ""));
	            paramUpdTradInfo.setLoanPrd(String.valueOf(paramInfo.get("LOAN_PRD") != null ? paramInfo.get("LOAN_PRD") : ""));
	            paramUpdTradInfo.setRdmptnMthd(String.valueOf(paramInfo.get("RDMPTN_MTHD") != null ? paramInfo.get("RDMPTN_MTHD") : ""));
	            paramUpdTradInfo.setExecDt(String.valueOf(paramInfo.get("EXEC_DT") != null ? paramInfo.get("EXEC_DT") : ""));
	            paramUpdTradInfo.setRdmptnDt(String.valueOf(paramInfo.get("RDMPTN_DT") != null ? paramInfo.get("RDMPTN_DT") : ""));
	            paramUpdTradInfo.setLoanReqNo(String.valueOf(paramInfo.get("LOAN_REQ_NO") != null ? paramInfo.get("LOAN_REQ_NO") : ""));
	            paramUpdTradInfo.setLoanAccptNo(String.valueOf(paramInfo.get("LOAN_ACCPT_NO") != null ? paramInfo.get("LOAN_ACCPT_NO") : ""));
	            paramUpdTradInfo.setErrCd(String.valueOf(paramInfo.get("ERR_CD") != null ? paramInfo.get("ERR_CD") : ""));
	            paramUpdTradInfo.setErrMsg(String.valueOf(paramInfo.get("ERR_MSG") != null ? paramInfo.get("ERR_MSG") : ""));
	            paramUpdTradInfo.setCoplCmmnClltrlInd(String.valueOf(paramInfo.get("COPL_CMMN_CLLTRL_IND") != null ? paramInfo.get("COPL_CMMN_CLLTRL_IND") : ""));
	            paramUpdTradInfo.setRjctnCd(String.valueOf(paramInfo.get("RJCTN_CD") != null ? paramInfo.get("RJCTN_CD") : ""));
	            paramUpdTradInfo.setRjctnMsg(String.valueOf(paramInfo.get("RJCTN_MSG") != null ? paramInfo.get("RJCTN_MSG") : ""));
	            paramUpdTradInfo.setCnnctnTradNo(String.valueOf(paramInfo.get("CNNCTN_TRAD_NO") != null ? paramInfo.get("CNNCTN_TRAD_NO") : ""));
	            paramUpdTradInfo.setSkipSendLms(skipSendLMS);
	            paramUpdTradInfo.setNewPrdctNum(newPrdctNum);
	            paramUpdTradInfo.setNewPrdctCurrency(newPrdctCurrency);
	            paramUpdTradInfo.setPerPart(String.valueOf(paramInfo.get("PER_PART") != null ? paramInfo.get("PER_PART") : ""));
	            paramUpdTradInfo.setDocEvdcCd(String.valueOf(paramInfo.get("DOC_EVDC_CD") != null ? paramInfo.get("DOC_EVDC_CD") : ""));
	            paramUpdTradInfo.setLoanHopeDt(String.valueOf(paramInfo.get("LOAN_HOPE_DT") != null ? paramInfo.get("LOAN_HOPE_DT") : ""));
	            paramUpdTradInfo.setHlPrsnCd(String.valueOf(paramInfo.get("HL_PRSN_CD") != null ? paramInfo.get("HL_PRSN_CD") : "")); // 무주택자확인
	            paramUpdTradInfo.setAdSbstCd(String.valueOf(paramInfo.get("AD_SBST_CD") != null ? paramInfo.get("AD_SBST_CD") : "")); // 사전청약확인
	            paramUpdTradInfo.setKwInsrCd(String.valueOf(paramInfo.get("KW_INSR_CD") != null ? paramInfo.get("KW_INSR_CD") : "")); // 권원보험확인
	            paramUpdTradInfo.setReacdn(String.valueOf(paramInfo.get("REACDN") != null ? paramInfo.get("REACDN") : ""));
	            paramUpdTradInfo.setIntegratedConselingYn(String.valueOf(paramInfo.get("INTEGRATED_CONSELING_YN") != null ? paramInfo.get("INTEGRATED_CONSELING_YN") : "")); // 통합대출여부
	            paramUpdTradInfo.setEasyLoanYn(String.valueOf(paramInfo.get("EASY_LOAN_YN") != null ? paramInfo.get("EASY_LOAN_YN") : "")); // 이지론여부
	            paramUpdTradInfo.setLoanDocInvstgtFlg(String.valueOf(paramInfo.get("LOAN_DOC_INVSTGT_FLG") != null ? paramInfo.get("LOAN_DOC_INVSTGT_FLG") : "")); // autobooking 여부
	            paramUpdTradInfo.setSecurityAcctNo(String.valueOf(paramInfo.get("SECURITY_ACCT_NO") != null ? paramInfo.get("SECURITY_ACCT_NO") : "")); // BIB 2021-08-27 증권계좌번호
	            paramUpdTradInfo.setGovmDataType(String.valueOf(paramInfo.get("GOVM_DATA_TYPE") != null ? paramInfo.get("GOVM_DATA_TYPE") : "")); // 스크래핑타입 - G:공공데이터, S:스크래핑
	            paramUpdTradInfo.setGovmSignType(String.valueOf(paramInfo.get("GOVM_SIGN_TYPE") != null ?  paramInfo.get("GOVM_SIGN_TYPE") : "")); // 서명타입 - 1:공동, B:금융 pin, C:금융생체, 9:디지털
	            paramUpdTradInfo.setGovmSignErr(""); // 추가 적재내용필요시 해당필드 사용예정
	
	            /* 2023-03-14 대출이동여부 추가 */
	            paramUpdTradInfo.setPreLoanMoveYn(StringUtils.defaultIfEmpty(preLoanMoveYn, String.valueOf(paramInfo.get("PRE_LOAN_MOVE_YN") != null ? paramInfo.get("PRE_LOAN_MOVE_YN")  : ""))); // 대출이동여부-상담
	            paramUpdTradInfo.setRealLoanMoveYn(StringUtils.defaultIfEmpty(realLoanMoveYn, String.valueOf(paramInfo.get("REAL_LOAN_MOVE_YN") != null ? paramInfo.get("REAL_LOAN_MOVE_YN") : ""))); // 대출이동여부-신청
	
	            // 본부우대금리 값 저장. 2023-04-03
	            paramUpdTradInfo.setHqPrimeRateApplyYn(StringUtils.defaultIfEmpty(hqPrimeRateApplyYn, String.valueOf(paramInfo.get("HQ_PRIME_RATE_APPLY_YN") != null ? paramInfo.get("HQ_PRIME_RATE_APPLY_YN") : "")));
	            paramUpdTradInfo.setHqPrimeRate(StringUtils.defaultIfEmpty(hqPrimeRate, String.valueOf(paramInfo.get("HQ_PRIME_RATE") != null ? paramInfo.get("HQ_PRIME_RATE") : "")));
	
	            // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
	            paramUpdTradInfo.setLoanPurpose(StringUtils.defaultIfEmpty(loanPurpose, String.valueOf(paramInfo.get("LOAN_PURPOSE") != null ? paramInfo.get("LOAN_PURPOSE") : ""))); // 대출목적(구입자금, 생안자금, 임차반환, 갈아타기)
	            paramUpdTradInfo.setCollateralType(StringUtils.defaultIfEmpty(collateralType, String.valueOf(paramInfo.get("COLLATERAL_TYPE") != null ? paramInfo.get("COLLATERAL_TYPE") : ""))); // 담보유형(APT, 오피스텔, 빌라/다세대)
	
	            // 24.09.06 SC 제일 대출여부 추가
	            paramUpdTradInfo.setOplCnsltYn(StringUtils.defaultIfEmpty(OPL_CNSLT_YN, String.valueOf(paramInfo.get("OPL_CNSLT_YN") != null ? paramInfo.get("OPL_CNSLT_YN") : ""))); // SC 제일 대출여부 추가
	
	            registOngoingTradeInfoAndSendLms(paramUpdTradInfo);
	        }
	       
	        // 거래번호(TRAD_NO) 세션 세팅
	        sessionManager.setGlobalValue("TRAD_NO", tradNo);
        }
        
        // 화면 데이터 정보
        if (StringUtils.isNotEmpty(scrnDataInfo)) {       	
        	RegisterNonFaceScreenInfoParameter dbParam = new RegisterNonFaceScreenInfoParameter();
    		dbParam.setCustNo(custNo);//StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CUST_NO", String.class), ""));
    		dbParam.setTradNo(tradNo);
    		dbParam.setBizType(bizType);
    		dbParam.setScrnDataInfo(scrnDataInfo);
    		// 비대면인증 화면정보관리 등록(NF_SCRN_INFO_MGT_I_01)
    		log.debug("화면데이터 정보 param => {}", dbParam);
    		nfScreenInfoMgtDao.insertNonFaceScreenInfoManagement(dbParam);
        }

        if (StringUtils.isNotEmpty(recoveryData)) {
            RegisterEdocRecoveryDataRequest cddElectronicParam = new RegisterEdocRecoveryDataRequest();
            cddElectronicParam.setTradNo(tradNo);
            cddElectronicParam.setRcvryData(recoveryData);
            // CDD전자문서 정보 등록 (asis : MA3CMMBIZ013_101S)
            registEdocRecoveryData(cddElectronicParam);
        }

        response.setTradNo(tradNo);        
		
        log.debug("####################### 진행상태 등록/수정, ScrnData/RcvryData 등록 [MA3CMMBIZ010_105S] END ###\n", response);
        
        return response;
	}
	
	/**
	 * <pre>
	 * 진행상태 등록/수정(LMS발송)
	 * </pre>
	 * @param request RegisterOngoingTradeInfoRequest
	 * @return RegisterOngoingTradeInfoResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_10AS
	 */
	@ComponentOperation(name = "진행상태 등록/수정(LMS발송) (asis : MA3CMMBIZ010_10AS)") 
	public RegisterOngoingTradeInfoResponse registOngoingTradeInfoAndSendLms(RegisterOngoingTradeInfoRequest request) throws PRCServiceException {
		log.debug("####################### 진행상태 등록/수정(LMS발송) [MA3CMMBIZ010_10AS] START ###\n", request);
		
		RegisterOngoingTradeInfoResponse response = new RegisterOngoingTradeInfoResponse();
		
        String tradNo = StringUtils.defaultIfEmpty(request.getTradNo(), "");

        // 2019.06.13 접속제휴사코드 overflow일 경우 처리.
        if (StringUtils.defaultIfEmpty(request.getCnnctnWay(), "").length() > 6) {
            PRCServiceException appException = new PRCServiceException("9999");
            appException.setErrorGuideMessage("제휴처코드가 정상적이지 않아 처리중 오류가 발생하였습니다.");
            appException.setNextPage("MA3PRDALL001"); // TODO.금융상품몰 메인 상품목록
            throw appException;
        }

        // REA코드 overflow일 경우 처리.
        if (StringUtils.defaultIfEmpty(request.getReacdn(), "").length() > 6) {
            PRCServiceException appException = new PRCServiceException("9999");
            appException.setErrorGuideMessage("REA코드가 정상적이지 않아 처리중 오류가 발생하였습니다.");
            appException.setNextPage("MA3PRDALL001"); // TODO.금융상품몰 메인 상품목록
            throw appException;
        }

     // 진행상태 등록
        if (StringUtils.isEmpty(tradNo)) {
            String bizType = request.getBizType();

            // 점번호, 지점명이 없을 경우 고객정보에서 점번호를 가져온다.
            if (!bizType.matches("CRPL|SPPL|MAPL|NSPL|CMJG|CMJL|MHLS|FHLS|MPPS|TBGL|OPPL")
                                && StringUtils.isEmpty(request.getBrnchNo())) {
				CbIbk01H86600Req h866Req = new CbIbk01H86600Req();
				h866Req.setUserID(SessionUtils.getSessionValue("UserID"));
				h866Req.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
				h866Req.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));

				// 보유계좌조회
				ListAccountHeldInfo listAccountHeld = accountListComponent.getListAccountHeld(h866Req);
				//List<yoMyInfRecList> yoMyInfRec;
				List<yoMyInfRecList> listYoMyInfRec = listAccountHeld.getYoMyInfRec();
				
				if (listYoMyInfRec != null) {
				    request.setBrnchNo(listYoMyInfRec.get(listYoMyInfRec.size() - 1).getYoBsJum());
				}
            }

            RegisterOngoingTradeInfoRequest statusAndParam = new RegisterOngoingTradeInfoRequest();
            String callCntrStsCd = StringUtils.defaultIfEmpty(request.getCallCntrStsCd(), "COCU");
            
            statusAndParam.setCustNo(StringUtils.defaultIfEmpty(request.getCustNo(), ""));
            statusAndParam.setBizType(bizType);
            statusAndParam.setPrdctId(StringUtils.defaultIfEmpty(request.getPrdctId(), ""));
            statusAndParam.setPrdctCd(StringUtils.defaultIfEmpty(request.getPrdctCd(), ""));
            statusAndParam.setPrdctNm(StringUtils.defaultIfEmpty(request.getPrdctNm(), ""));
            statusAndParam.setCallCntrStsCd(callCntrStsCd);
            statusAndParam.setCnnctnWay(StringUtils.defaultIfEmpty(request.getCnnctnWay(), ""));
            statusAndParam.setClerkNo(StringUtils.defaultIfEmpty(request.getClerkNo(), ""));
            statusAndParam.setJobDetailCd(StringUtils.defaultIfEmpty(request.getJobDetailCd(), ""));
            statusAndParam.setCddReqCd(StringUtils.defaultIfEmpty(request.getCddReqCd(), ""));
            statusAndParam.setIdCardCd(("CASA".equals(bizType) || "BBCA".equals(bizType)) ? "N" : StringUtils.defaultIfEmpty(request.getCddReqCd(), ""));
            statusAndParam.setNewUserFlg(StringUtils.defaultIfEmpty(request.getNewUserFlg(), ""));
            statusAndParam.setDelObjFlg(StringUtils.defaultIfEmpty(request.getDelObjFlg(), ""));
            statusAndParam.setBrnchNo(StringUtils.defaultIfEmpty(request.getBrnchNo(), ""));
            statusAndParam.setErrCd(StringUtils.defaultIfEmpty(request.getErrCd(), ""));
            statusAndParam.setErrMsg(StringUtils.defaultIfEmpty(request.getErrMsg(), ""));
            statusAndParam.setCnnctnTradNo(StringUtils.defaultIfEmpty(request.getCnnctnTradNo(), ""));
            statusAndParam.setTargetProcess("NF_TRADINFO_MGT_I_02"); // i:insert, u:update
            statusAndParam.setPerPart(StringUtils.isNotEmpty(request.getPerPart())
                            ? StringUtils.defaultIfEmpty(request.getPerPart(), "")
                            : StringUtils.defaultIfEmpty(
                                            sessionManager.getGlobalValue("H92C_YOGEORE", String.class),
                                            "")); // 20230905.SK2.UI-1784에 거래자구분 "개인"으로 노출되는 내용 개선.
                                                  // //고객거래구분(개인or개사)
            statusAndParam.setDocEvdcCd(StringUtils.defaultIfEmpty(request.getDocEvdcCd(), "")); // 개인사업자사진확인여부
            statusAndParam.setHlPrsnCd(StringUtils.defaultIfEmpty(request.getHlPrsnCd(), "")); // 무주택자확인
            statusAndParam.setAdSbstCd(StringUtils.defaultIfEmpty(request.getAdSbstCd(), "")); // 사전청약확인
            statusAndParam.setKwInsrCd(StringUtils.defaultIfEmpty(request.getKwInsrCd(), "")); // 권원보험확인
            statusAndParam.setReacdn(StringUtils.defaultIfEmpty(request.getReacdn(), "")); // REA코드
            statusAndParam.setIntegratedConselingYn(StringUtils.defaultIfEmpty(request.getIntegratedConselingYn(), "")); // 통합대출여부

            statusAndParam.setGovmDataType(StringUtils.defaultIfEmpty(request.getGovmDataType(), "")); // 스크래핑타입 - G:공공데이터, S:스크래핑
            statusAndParam.setGovmSignType(StringUtils.defaultIfEmpty(request.getGovmSignType(), "")); // 서명타입 - 1:공동, B:금융 pin, C:금융생체, 9:디지털
            statusAndParam.setGovmSignErr(StringUtils.defaultIfEmpty(request.getGovmSignErr(), ""));

            /* 2023-03-14 대출이동여부 추가 */
            statusAndParam.setPreLoanMoveYn(StringUtils.defaultIfEmpty(request.getPreLoanMoveYn(), "")); 	// 대출이동여부-상담
            statusAndParam.setRealLoanMoveYn(StringUtils.defaultIfEmpty(request.getRealLoanMoveYn(), "")); 	// 대출이동여부-신청

            // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
            statusAndParam.setLoanPurpose(StringUtils.defaultIfEmpty(request.getLoanPurpose(), "")); 		// 대출목적(구입자금,생안자금, 임차반환, 갈아타기)
            statusAndParam.setCollateralType(StringUtils.defaultIfEmpty(request.getCollateralType(), "")); 	// 담보유형(APT, 오피스텔, 빌라/다세대)

            // 24.09.06 SC 제일 대출여부 추가
            statusAndParam.setOplCnsltYn(StringUtils.defaultIfEmpty(request.getOplCnsltYn(), "")); // SC 제일 대출여부 추가

            // 진행상태 등록[MA3CMMBIZ010_103S]
            RegisterOngoingTradeInfoResponse regResponse = registOngoingTradeInfoAndPartnerTransaction(statusAndParam);
            tradNo = regResponse.getTradNo();
            
//            // 등록된 TRAD_NO조회
//            OngoingTradeInfoInquiryParameter tradeInfoInquiryDbParam = new OngoingTradeInfoInquiryParameter();
//            tradeInfoInquiryDbParam.setCustNo(StringUtils.defaultIfEmpty(request.getCustNo(), ""));
//            tradNo = getTradNoFromNF_TRADINFO_MGT(tradeInfoInquiryDbParam);
            
            // 주담대인경우 디테일 테이블 insert 2024.06.07
            if ("FHLS".equals(bizType) || "MPPS".equals(bizType) || "TBGL".equals(bizType) || "MHLS".equals(bizType)) {

            	NonFaceTradeInfoDetailParameter insFaceScreenDbParam = new NonFaceTradeInfoDetailParameter();
                insFaceScreenDbParam.setCustNo(StringUtils.defaultIfEmpty(request.getCustNo(), ""));
                insFaceScreenDbParam.setTradNo(tradNo);

                synchronized (this) {
                	// 비대면거래관리테이블상세 등록 (asis : NF_TRADINFO_DTL_I_01)
                	nfTradeInfoDtlDao.insertTradeInfoDetail(insFaceScreenDbParam);
                }
            }
        } else {
            String CUST_NO = request.getCustNo();
            String TRAD_NO = request.getTradNo();
            String BIZ_TYPE = request.getBizType();
            String PRGRSS_STS_CD = request.getPrgrssStsCd();
            String CALL_CNTR_STS_CD = request.getCallCntrStsCd();

            RegisterOngoingTradeInfoRequest statusAndUParam = new RegisterOngoingTradeInfoRequest();

            if (StringUtils.isEmpty(PRGRSS_STS_CD)) {
                PRGRSS_STS_CD = "PRGS"; // 진행상태코드 (PRGS:진행중, DONE:신청완료, RECT:신청거절, CNCL:신청취소)
            }

            boolean isIdCardCd = false;
            statusAndUParam.setPrdctId(request.getPrdctId()); 		// 상품ID
            statusAndUParam.setPrdctCd(request.getPrdctCd()); 		// 상품코드
            statusAndUParam.setPrdctNm(request.getPrdctNm()); 		// 상품명
            statusAndUParam.setClerkNo(request.getClerkNo()); 		// 소개자행번
            statusAndUParam.setCnnctnWay(request.getCnnctnWay()); 	// 접속방식

            if (StringUtils.isNotEmpty(request.getCddReqCd())) { 	// CDD정상여부
                statusAndUParam.setCddReqCd(request.getCddReqCd());
                if ("N".equals(request.getCddReqCd())) { 		// CDD가 정상이 아닐 경우 "N"
                    isIdCardCd = true;
                    statusAndUParam.setIdCardCd("N");
                }
            }

            if (!isIdCardCd && StringUtils.isNotEmpty(request.getIdCardCd())) { // 신분증 코드
                isIdCardCd = true;
                statusAndUParam.setIdCardCd(request.getIdCardCd());
            }

            statusAndUParam.setIdCardCnt(request.getIdCardCnt()); 		// 신분증 진위 건수
            statusAndUParam.setBrnchNo(request.getBrnchNo()); 			// 점번호
            statusAndUParam.setReqAmt(request.getReqAmt()); 			// 신청금액
            statusAndUParam.setAuthntIndCd(request.getAuthntIndCd()); 	// 인증유형 여부
            if (StringUtils.isNotEmpty(request.getAuthntInd())) { 		// 인증유형(CASA)
                statusAndUParam.setAuthntInd(request.getAuthntInd());
                if (StringUtils.isNotEmpty(request.getAuthntReqDt())) {
                    statusAndUParam.setAuthntReqDt("CURRENT TIMESTAMP");
                    statusAndUParam.setAuthntReqExpireDt("DATE(" + CommonBizUtils.getBusinessDay(DateUtils.getCurrentDate("yyyyMMdd"), 3) + ")"); // 3영업일 후
                }
            }

            statusAndUParam.setAuthntReqCmpltnDt("CURRENT TIMESTAMP"); 	// 인증신청만료일자
            statusAndUParam.setKfbAcctNo(request.getKfbAcctNo()); 		// 당행계좌
            statusAndUParam.setAlliancCd(request.getAlliancCd()); 		// 제휴코드
            statusAndUParam.setEffctvInt(request.getEffctvInt()); 		// 적용금리
            statusAndUParam.setLoanPrd(request.getLoanPrd()); 			// 대출기간
            statusAndUParam.setRdmptnMthd(request.getRdmptnMthd()); 	// 상환방법
            statusAndUParam.setExecDt(request.getExecDt()); 			// 대출 실행일자
            statusAndUParam.setRdmptnDt(request.getRdmptnDt()); 		// 대출상환기일
            statusAndUParam.setLoanReqNo(request.getLoanReqNo()); 		// 대출신청번호
            statusAndUParam.setLoanAccptNo(request.getLoanAccptNo()); 	// 대출접수번호
            statusAndUParam.setErrCd(request.getErrCd()); 				// 에러코드
            statusAndUParam.setErrMsg(request.getErrMsg()); 			// 에러메시지
            statusAndUParam.setCoplCmmnClltrlInd(request.getCoplCmmnClltrlInd()); // 부부공동담보여부
            if (StringUtils.isNotEmpty(request.getRjctnCd())) { // 거절코드 및 거절사유
                statusAndUParam.setRjctnCd(request.getRjctnCd());
                statusAndUParam.setRjctnMsg(request.getRjctnMsg());
            }

            statusAndUParam.setCustNo(CUST_NO);
            statusAndUParam.setTradNo(TRAD_NO);
            statusAndUParam.setCallCntrStsCd(CALL_CNTR_STS_CD);
            statusAndUParam.setPrgrssStsCd(PRGRSS_STS_CD);
            statusAndUParam.setBizType(BIZ_TYPE);
            statusAndUParam.setCnnctnTradNo(request.getCnnctnTradNo());
            statusAndUParam.setCnnctnWay(request.getCnnctnWay());
            statusAndUParam.setTargetProcess("NF_TRADINFO_MGT_U_01");
            statusAndUParam.setPerPart(StringUtils.defaultIfEmpty(request.getPerPart(),
                            StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("H92C_YOGEORE", String.class), ""))); // 20230905.SK2.UI-1784에 거래자구분 "개인"으로 노출되는 내용 개선. //고객거래구분(개인or개사)
            statusAndUParam.setDocEvdcCd(StringUtils.defaultIfEmpty(request.getDocEvdcCd(), "")); 	// 개인사업자사진확인여부
            statusAndUParam.setLoanHopeDt(StringUtils.defaultIfEmpty(request.getLoanHopeDt(), "")); // 대출희망일
            statusAndUParam.setHlPrsnCd(StringUtils.defaultIfEmpty(request.getHlPrsnCd(), "")); 	// 무주택자확인
            statusAndUParam.setAdSbstCd(StringUtils.defaultIfEmpty(request.getAdSbstCd(), "")); 	// 사전청약확인
            statusAndUParam.setKwInsrCd(StringUtils.defaultIfEmpty(request.getKwInsrCd(), "")); 	// 권원보험확인
            statusAndUParam.setReacdn(StringUtils.defaultIfEmpty(request.getReacdn(), "")); 		// REA코드
            statusAndUParam.setIntegratedConselingYn(StringUtils.defaultIfEmpty(request.getIntegratedConselingYn(), "")); // 통합대출여부
            statusAndUParam.setEasyLoanYn(StringUtils.defaultIfEmpty(request.getEasyLoanYn(), "")); // 이지론여부
            statusAndUParam.setLoanDocInvstgtFlg(StringUtils.defaultIfEmpty(request.getLoanDocInvstgtFlg(), "")); // autobooking  여부
            statusAndUParam.setSecurityAcctNo(StringUtils.defaultIfEmpty(request.getSecurityAcctNo(), "")); // BIB 2021-08-27 증권계좌번호

            statusAndUParam.setGovmDataType(StringUtils.defaultIfEmpty(request.getGovmDataType(), "")); // 스크래핑타입 - G:공공데이터, S:스크래핑
            statusAndUParam.setGovmSignType(StringUtils.defaultIfEmpty(request.getGovmSignType(), "")); // 서명타입 1:공동, B:금융 pin,C:금융생체, 9:디지털
            statusAndUParam.setGovmSignErr(StringUtils.defaultIfEmpty(request.getGovmSignErr(), ""));

            /* 2023-03-14 대출이동여부 추가 */
            statusAndUParam.setPreLoanMoveYn(StringUtils.defaultIfEmpty(request.getPreLoanMoveYn(), ""));   // 대출이동여부-상담
            statusAndUParam.setRealLoanMoveYn(StringUtils.defaultIfEmpty(request.getRealLoanMoveYn(), "")); // 대출이동여부-신청

            /* 2023-04-03 본부우대금리 추가 */
            statusAndUParam.setHqPrimeRateApplyYn(StringUtils.defaultIfEmpty(request.getHqPrimeRateApplyYn(), "")); // 본부우대금리 적용여부
            statusAndUParam.setHqPrimeRate(StringUtils.defaultIfEmpty(request.getHqPrimeRate(), "")); 				// 본부우대금리

            // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
            statusAndUParam.setLoanPurpose(StringUtils.defaultIfEmpty(request.getLoanPurpose(), "")); 		// 대출목적
            statusAndUParam.setCollateralType(StringUtils.defaultIfEmpty(request.getCollateralType(), "")); // 담보유형

            // 24.09.06 SC 제일 대출여부 추가
            statusAndUParam.setOplCnsltYn(StringUtils.defaultIfEmpty(request.getOplCnsltYn(), "")); // SC 제일 대출여부 추가
            
            // 진행상태 변경 [MA3CMMBIZ010_103S]
            registOngoingTradeInfoAndPartnerTransaction(statusAndUParam);

            // CASA계좌 개설 성공 시 NEW_USER_FLG = "E" 처리
            if (("CASA".equals(BIZ_TYPE) || "BBCA".equals(BIZ_TYPE)) && "DONE".equals(PRGRSS_STS_CD)) {
                String perBusNo = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class), 
                					StringUtils.defaultIfEmpty( sessionManager.getGlobalValue("PerBusNo", String.class), ""));
                
                NonFaceCustomerInfoInquiryResult responseNF_CUST_MGT_S_01 = new NonFaceCustomerInfoInquiryResult();
                NonFaceCustomerInfoParameter paramNF_CUST_MGT_S_01 = new NonFaceCustomerInfoParameter();
                paramNF_CUST_MGT_S_01.setSsn(perBusNo);
                // 비대면 고객정보 조회 (asis : NF_CUST_MGT_S_01)
                responseNF_CUST_MGT_S_01 = nfCustMgtDao.selectNonFaceCustomerInfo(paramNF_CUST_MGT_S_01);

                NonFaceCustomerInfoParameter paramNF_CUST_MGT_U_01 = new NonFaceCustomerInfoParameter();

                paramNF_CUST_MGT_U_01.setSsn(perBusNo);
                paramNF_CUST_MGT_U_01.setMblphnNo(responseNF_CUST_MGT_S_01.getMblphnNo());
                paramNF_CUST_MGT_U_01.setUserNm(responseNF_CUST_MGT_S_01.getUserNm());
                paramNF_CUST_MGT_U_01.setCmpndCheckKey(responseNF_CUST_MGT_S_01.getCmpndCheckKey());
                paramNF_CUST_MGT_U_01.setSrcNewUserFlg(responseNF_CUST_MGT_S_01.getSrcNewUserFlg());
                paramNF_CUST_MGT_U_01.setNewUserFlg("E");
                paramNF_CUST_MGT_U_01.setSrcDelObjFlg(responseNF_CUST_MGT_S_01.getSrcDelObjFlg());
                paramNF_CUST_MGT_U_01.setDelObjFlg(responseNF_CUST_MGT_S_01.getDelObjFlg());

                // 비대면인증 고객관리 수정(NF_CUST_MGT_U_01)
                nfCustMgtDao.updateNonFaceCustomerInfo(paramNF_CUST_MGT_U_01);
            }

            String skipSendLMS = StringUtils.defaultIfEmpty(request.getSkipSendLms(), "");
            String newPrdctNum = StringUtils.defaultIfEmpty(request.getNewPrdctNum(), "");

            // 거래가 완료 되면 LMS전송
            if ("DONE".equals(PRGRSS_STS_CD)) {
            	try {
					if (!"Y".equals(skipSendLMS)) {
						String returnFlag = prcSendLMS(request);
						response.setSendLmsFlag(returnFlag);
					}

                    if ("".equals(newPrdctNum)) {
                        String bizType = StringUtils.defaultIfEmpty(request.getBizType(), "");

                        // 금리인하요구권은 DONE 시점에 계좌리스트에 변동사항이 없어 세션 초기화 시 오류가 발생하여 RIRC 는 제외하도록 수정
                        if (!"RIRC".equals(bizType)) {
                        	BankingBizUtils.accountSessionClear();
                        }
                    } else {
                        String PRDCT_NM = StringUtils.defaultIfEmpty(request.getPrdctNm(), "");
                        String currencyKey = StringUtils.defaultIfEmpty(request.getNewPrdctCurrency(), "");
                        String newPrdctCurrency = "";
                        String PRDCT_CD = StringUtils.defaultIfEmpty(request.getPrdctCd(), ""); // 2025-10-15 신규 후 신규계좌세션(H039_NEW_ACCOUNTLIST)에 상품코드 추가
                        if (!"".equals(currencyKey)) {
                            List<ICodeItemInfo> codeList = CodeUtils.getCodes("CURRENCY");
                            for (ICodeItemInfo r : codeList) {
                                if (currencyKey.equals(r.getKey())) {
                                	String rValue = r.getValue();
                                    newPrdctCurrency = rValue.substring(0, 3);
                                    break;
                                }
                            }
                        }

                        newProductSessionClear(newPrdctNum, PRDCT_NM, newPrdctCurrency, PRDCT_CD);
                    }
                } catch (Exception e) {

                }
            }
        }

        response.setTradNo(tradNo);
        log.debug("####################### 진행상태 등록/수정, ScrnData/RcvryData 등록 [MA3CMMBIZ010_105S] END ###\n", response);
        return response;
	}
	
	private void newProductSessionClear(String newPrdctNum, String PRDCT_NM, String newPrdctCurrency,String PRDCT_CD) throws Exception {
		
		NewAccountInfoSession refrashAccountRecord = new NewAccountInfoSession();
        List<NewAccountInfoSession> refrashAccountList = new ArrayList<>();

        refrashAccountRecord.setNEW_ACCOUNT_NUM(newPrdctNum);
        refrashAccountRecord.setNEW_ACCOUNT_NAME(PRDCT_NM);
        refrashAccountRecord.setNEW_ACCOUNT_CURCY(newPrdctCurrency);
        refrashAccountRecord.setNEW_ACCOUNT_PRDCT_CD(PRDCT_CD); // 2025-10-15 신규 후 신규계좌세션(H039_NEW_ACCOUNTLIST)에 상품코드 추가

        refrashAccountList.add(refrashAccountRecord);

        BankingBizUtils.accountSessionClear(refrashAccountList);
	}

//	/**
//	 * 등록된 TRAD_NO조회 
//	 * 
//	 * @param paramMap OngoingTradeInfoInquiryParameter
//	 * @return
//	 */
//	private String getTradNoFromNF_TRADINFO_MGT(OngoingTradeInfoInquiryParameter paramMap) {
//	    paramMap.setTradRegGb(("Y".equals(
//	                    StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("isCSL", String.class), null)) ? "1" : "0"));
//	
//	    // 진행중인 상품 목록 조회 (asis : NF_TRADINFO_MGT_S_04)
//	    List<OngoingProductInfoInquiryResult> resultTradNo = nfTradeInfoMgtDao.selectOngoingProductInfoList(paramMap);
//	    return String.valueOf(resultTradNo.get(0).getTradNo());
//	}

	private String prcSendLMS(RegisterOngoingTradeInfoRequest paramData) {
		log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_10AS prcSendLMS START #");
		
		String returnFlag = "";
        String lmsID = null;
        String CUST_NO = StringUtils.defaultIfEmpty(paramData.getCustNo(), "");
        String TRAD_NO = StringUtils.defaultIfEmpty(paramData.getTradNo(), "");
        String PRGRSS_STS_CD = StringUtils.defaultIfEmpty(paramData.getPrgrssStsCd(), "");
        log.debug("@#@#@#@# LGS @#@#@#@## prcSendLMS PRGRSS_STS_CD >>> ["+PRGRSS_STS_CD+"] #");
        
        try {
        	Map<String, String> smsParam = new HashMap<>();

            if (PRGRSS_STS_CD != null && "DONE".equals(PRGRSS_STS_CD)) {
            	TradeInfoInquiryParameter paramNF_TRADINFO_MGT_S_05 = new TradeInfoInquiryParameter();
            	TradeInfoInquiryResult resultNF_TRADINFO_MGT_S_05 = new TradeInfoInquiryResult();

                paramNF_TRADINFO_MGT_S_05.setCustNo(CUST_NO);
                paramNF_TRADINFO_MGT_S_05.setTradNo(TRAD_NO);

                // 거래정보및고객정보(SSN)조회 (asis : NF_TRADINFO_MGT_S_05)
                resultNF_TRADINFO_MGT_S_05 = nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(paramNF_TRADINFO_MGT_S_05);

                String resultTradNo = resultNF_TRADINFO_MGT_S_05.getTradNo() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getTradNo()) : "";
                String resultBizType = resultNF_TRADINFO_MGT_S_05.getBizType() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getBizType()) : "";

                // 거래번호 유효하고, 삼성신용/체크 카드 제외.
                if (StringUtils.isNotEmpty(resultTradNo) && !(resultBizType.matches("SSCD|SSCK"))) {
                    String resultCallCntrStsCd = resultNF_TRADINFO_MGT_S_05.getCallCntrStsCd() != null ? 
                    								String.valueOf(resultNF_TRADINFO_MGT_S_05.getCallCntrStsCd()) : "";

                    if ("BCCK".equals(resultBizType) && !"CDEN".equals(resultCallCntrStsCd)) {
                            // 체크카드 && !카드신청완료 LMS 전송 안함.
                            lmsID = null;
                    } else if ("FCSA".equals(resultBizType) || "FXSB".equals(resultBizType) || 
                               "TDSB".equals(resultBizType) || "TDSV".equals(resultBizType) || 
                               "DRPL".equals(resultBizType)) {
                            // 20.03.14 FCSA(외화입출금), FXSB(외화예금), TDSB(예금), TDSV(적금), DRPL(신용대출연기갱신 - 개별처리) LMS 발송 삭제
                            lmsID = null;
                    } else if ("CRPL".equals(resultBizType) && StringUtils.isNotEmpty(paramData.getLmsID())) {
                            // 드림론 타행상환 있을 때,
                            lmsID = paramData.getLmsID();
                    } else {
                            lmsID = resultBizType;
                    }

                    smsParam.put("lmsID", lmsID);
                    smsParam.put("~~PRDCT_NM~~", resultNF_TRADINFO_MGT_S_05.getPrdctNm());

                    // 11월 마스킹 개선 (계좌번호 마스킹 추가)
                    String joinAcctNo = resultNF_TRADINFO_MGT_S_05.getKfbAcctNo() == null ? "" : String.valueOf(resultNF_TRADINFO_MGT_S_05.getKfbAcctNo());
                    String maskJoinAcctNo = "";

                    if (!"".equals(joinAcctNo)) {
                        maskJoinAcctNo = BizCommonUtils.getMaskCustData(joinAcctNo, "02");
                    }

                    smsParam.put("~~KFB_ACCT_NO~~", maskJoinAcctNo);
                }
            } else {
                    lmsID = paramData.getLmsID();
            }
            
            log.debug("@#@#@#@# LGS @#@#@#@## prcSendLMS LMS_ID >>> ["+lmsID+"] #");
            if (StringUtils.isNotEmpty(lmsID)) {
            	
                // CASA DONE SMS 발송전 상품명체크
                if ("CASA".equals(lmsID)) {
                	// 입출금 비교대상 상품명 SALES_CASA_PRODUCT_NM = 제일EZ|내월급|마이100|내지갑|마이시그니처|마이줌|마이심플|두드림|두드림2U|Hi|모아예금
                    String salesCasaProductName = StringUtils.defaultIfEmpty(PropertiesUtils.getString("SALES_CASA_PRODUCT_NM"), "");
                    //salesCasaProductName = "제일EZ|내월급|마이100|내지갑|마이시그니처|마이줌|마이심플|두드림|두드림2U|Hi|모아예금";
                    if (StringUtils.isNotEmpty(salesCasaProductName)) {
                        String[] arrProductNm = salesCasaProductName.split("\\|");
                        String curCasaPrdctNM = smsParam.get("~~PRDCT_NM~~") != null ? (String) smsParam.get("~~PRDCT_NM~~") : "";
                        curCasaPrdctNM = curCasaPrdctNM.trim();

                        for (int cidx = 0; cidx < arrProductNm.length; cidx++) {
                        	log.debug("####################_LIH_MA3CMMIBZ010_10AS_arrProductNm   ::" + arrProductNm[cidx] + "::");
							log.debug("####################_LIH_MA3CMMIBZ010_10AS_curCasaPrdctNM ::" + curCasaPrdctNM + "::");
							
                            if (StringUtils.isNotEmpty(curCasaPrdctNM) && curCasaPrdctNM.indexOf(arrProductNm[cidx]) > -1) {
                            	returnFlag = sendLMS(smsParam);
                                break;
                            }
                        }

                    }
                } else {
                	returnFlag = sendLMS(smsParam);
                }
            }
        } catch (Exception e) {

        }
        log.debug("@#@#@#@# LGS @#@#@#@## MA3CMMBIZ010_10AS sendLMS END : RESULT ["+returnFlag+"] #");
        return returnFlag;
	}

	private String sendLMS(Map<String, String> smsData) throws Exception {
		log.debug("@#@#@#@# LGS @#@#@#@## sendLMS DATA >>> [ "+smsData+" ] #");
		
        String smsInfo = CodeUtils.getCodeValue("SMS_MSG", StringUtils.defaultIfEmpty(smsData.get("lmsID"), ""));
        String[] smsInfoArray = smsInfo.split("#");
        String callmessage = StringUtils.defaultIfEmpty(smsInfoArray[6], "");

        // 호출메시지
        callmessage = StringUtils.replace(callmessage, "~~host~~", StringUtils.defaultIfEmpty(PropertiesUtils.getString("MA30_URL"), ""));
        // 상품번호
        callmessage = StringUtils.replace(callmessage, "~~prdId~~", StringUtils.defaultIfEmpty(smsInfoArray[2], ""));
        // 보내는 사람 이름
        callmessage = StringUtils.replace(callmessage, "~~name~~", StringUtils.defaultIfEmpty(smsData.get("userName"), ""));
        // 제목
        callmessage = StringUtils.replace(callmessage, "~~title~~", StringUtils.defaultIfEmpty(smsInfoArray[4], ""));

        // 추가적으로 정의된 치환 하기
        if (smsData != null) {
            Iterator<Entry<String, String>> smsItr = smsData.entrySet().iterator();
            while (smsItr.hasNext()) {
                Entry<String, String> entry = smsItr.next();
                callmessage = StringUtils.replace(callmessage, entry.getKey(), entry.getValue());
            }
        }

        String[] reqphone = smsInfoArray[0].split("-");
        String callphone1 = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("HPOne"), ""); 	// 호출 번호 #1
        String callphone2 = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("HPTwo"), ""); 	// 호출 번호 #2
        String callphone3 = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("HPThree"), "");// 호출 번호 #3
        String subject = StringUtils.defaultIfEmpty(smsData.get("subject"), ""); 					// 제목
        String callname = "[SC제일은행]"; 																// 발신자명
        String rdate = StringUtils.defaultIfEmpty(DateUtils.getCurrentDate("yyyyMMdd"), ""); 		// 메시지 전송 예약일자
        String rtime = StringUtils.defaultIfEmpty(DateUtils.getCurrentDate("HHmmss"), ""); 			// 메시지 전송 예약시간
        String messageCode = StringUtils.defaultIfEmpty(smsInfoArray[3], ""); 						// 메시지 코드
        String reqphone1 = StringUtils.defaultIfEmpty(smsData.get("reqphone1"), ""); 				// 호출 번호 #1
        String reqphone2 = StringUtils.defaultIfEmpty(reqphone[0], ""); 							// 호출 번호 #2
        String reqphone3 = StringUtils.defaultIfEmpty(reqphone[1], ""); 							// 호출 번호 #3
        String deptcode = StringUtils.defaultIfEmpty(smsInfoArray[5], ""); 							// 부서코드
        String deptname = StringUtils.defaultIfEmpty(smsData.get("deptName"), ""); 					// 부서명

        log.debug("@#@# LGS 고객휴대폰번호1["+ callphone1 +"] | 고객휴대폰번호2["+ callphone2 +"] | 고객휴대폰번호3["+ callphone3 +"] | subject["+ subject +"] | callmessage = ["+ callmessage +"] | callname["+ callname +"] | rdate["+rdate+"] | rtime["+rtime+"] | messageCode["+messageCode+"] | reqphone1["+reqphone1+"] | reqphone2["+reqphone2+"] | reqphone3["+reqphone3+"] | deptcode["+deptcode+"] | deptname["+deptname+"] #@#@");
        
        LmsRequest lmsParam = new LmsRequest();
        lmsParam.setCallPhone1(callphone1);
        lmsParam.setCallPhone2(callphone2);
        lmsParam.setCallPhone3(callphone3);
        lmsParam.setSubject(subject);
        lmsParam.setCallMessage(callmessage);
        lmsParam.setCallName(callname);
        lmsParam.setRateDate(rdate);
        lmsParam.setRateTime(rtime);
        lmsParam.setMessageCode(messageCode);
        lmsParam.setReqPhone1(reqphone1);
        lmsParam.setReqPhone2(reqphone2);
        lmsParam.setReqPhone3(reqphone3);
        lmsParam.setDeptCode(deptcode);
        lmsParam.setDeptName(deptname);

        /*
         * smsResponse값이 'O'가 아닐 경우 데이타값을 다시한번 확인 바람
         * szStatus='P'; 이통사번호오류
         * szStatus='N'; 전화번호오류
         * szStatus='I'; 사용자ID오류
         * szStatus='D'; 회사코드오류
         * szStatus='M'; 메시지가 NULL인 경우
         * SzStatus='T'; 예약일자 이상
         * szStatus='I'; 등록된 회사가 아니거나 회원이 아니다.
         * szStatus='C'; 잔액초과
         * szStatus='O'; 호스트 성공 리턴코드
         * szStatus='X'; 호스트 실패 리턴코드
         */
        String result = lmsComponent.sendMain(lmsParam);
        switch(result) {
        	case "P" : log.debug("@#@# LGS SMS 발송결과 = [P] :: 이통사번호오류"); break;
        	case "N" : log.debug("@#@# LGS SMS 발송결과 = [N] :: 전화번호오류"); break;
        	case "I" : log.debug("@#@# LGS SMS 발송결과 = [I] :: 사용자ID오류:등록된 회사가 아니거나 회원이 아니다."); break;
        	case "D" : log.debug("@#@# LGS SMS 발송결과 = [D] :: 회사코드오류"); break;
        	case "M" : log.debug("@#@# LGS SMS 발송결과 = [M] :: 메시지가 NULL인 경우"); break;
        	case "T" : log.debug("@#@# LGS SMS 발송결과 = [T] :: 예약일자 이상"); break;
        	case "C" : log.debug("@#@# LGS SMS 발송결과 = [C] :: 잔액초과"); break;
        	case "O" : log.debug("@#@# LGS SMS 발송결과 = [O] :: 호스트 성공 리턴코드"); break;
        	case "X" : log.debug("@#@# LGS SMS 발송결과 = [X] :: 호스트 실패 리턴코드"); break;
        }
        
        //log.debug("@#@# LGS SMS 발송결과 = ["+result+"]");
        return result;
	}
	
	/**
	 * <pre>
	 * 진행상태 등록/수정(거래처거래정보 처리)
	 * </pre>
	 * @param request RegisterOngoingTradeInfoRequest
	 * @return RegisterOngoingTradeInfoResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_103S
	 */
	@ComponentOperation(name = "진행상태 등록/변경 및 제휴처 거래 관련 처리 (asis : MA3CMMBIZ010_103S)") 
	public RegisterOngoingTradeInfoResponse registOngoingTradeInfoAndPartnerTransaction(RegisterOngoingTradeInfoRequest request) throws PRCServiceException {
		log.debug("####################### 진행상태 등록/변경 및 제휴처 거래 관련 처리 [MA3CMMBIZ010_103S] START ###\n", request);
		
		RegisterOngoingTradeInfoResponse response = new RegisterOngoingTradeInfoResponse();
		
		String BIZ_TYPE = StringUtils.nvl(request.getBizType(), "");
		String CNNCTN_WAY = StringUtils.defaultIfEmpty(request.getCnnctnWay(), sessionManager.getGlobalValue("CNNCTN_WAY", String.class));
		String CNNCTN_LOAN_LMT_AMT = StringUtils.nvl(sessionManager.getGlobalValue("FIEXTAPAMT", String.class), "");		//외부한도금액
		String CNNCTN_SIGN_ID = StringUtils.nvl(sessionManager.getGlobalValue("CNNCTN_SIGN_ID", String.class), "");
		String REACDN = StringUtils.defaultIfEmpty(request.getReacdn(), sessionManager.getGlobalValue("REACDN", String.class));
		String CLERK_NO = StringUtils.defaultIfEmpty(request.getClerkNo(), sessionManager.getGlobalValue("CLERK_NO", String.class));
		String INTEGRATED_CONSELING_YN = StringUtils.defaultIfEmpty(request.getIntegratedConselingYn(), "");
		
		String PRGRSS_STS_CD = StringUtils.nvl(request.getPrgrssStsCd(), "");
		
		// 2019.06.13 접속제휴사코드 overflow일 경우 처리.
        if (StringUtils.defaultIfEmpty(CNNCTN_WAY, "").length() > 6) {
            PRCServiceException appException = new PRCServiceException("9999");
            appException.setErrorGuideMessage("제휴처코드가 정상적이지 않아 처리중 오류가 발생하였습니다.");
            appException.setNextPage("MA3PRDALL001");
            throw appException;
        }

        // REA코드 overflow일 경우 처리
        if (StringUtils.defaultIfEmpty(REACDN, "").length() > 6) {
            PRCServiceException appException = new PRCServiceException("9999");
            appException.setErrorGuideMessage("REA코드가 정상적이지 않아 처리중 오류가 발생하였습니다.");
            appException.setNextPage("MA3PRDALL001");
            throw appException;
        }
        
        String TRAD_NO = request.getTradNo();
        switch (StringUtils.defaultIfEmpty(request.getTargetProcess(), "")) {
        	case "NF_TRADINFO_MGT_I_02": // 진행상태 등록
        		RegisterOngoingTradeInfoParameter dbParam = new RegisterOngoingTradeInfoParameter();
                String callCntrStsCd = StringUtils.defaultIfEmpty(request.getCallCntrStsCd(), "COCU");

                dbParam.setCustNo(StringUtils.defaultIfEmpty(request.getCustNo(), ""));					// 고객번호
                dbParam.setBizType(BIZ_TYPE);															// 업무구분
                dbParam.setPrdctId(StringUtils.defaultIfEmpty(request.getPrdctId(), ""));				// 상품ID
                dbParam.setPrdctCd(StringUtils.defaultIfEmpty(request.getPrdctCd(), ""));				// 상품코드
                dbParam.setPrdctNm(StringUtils.defaultIfEmpty(request.getPrdctNm(), ""));				// 상품명
                dbParam.setCallCntrStsCd(callCntrStsCd);												// 콜센터상태코드
                dbParam.setCnnctnWay(StringUtils.defaultIfEmpty(CNNCTN_WAY, ""));						// 제휴처
                dbParam.setClerkNo(StringUtils.defaultIfEmpty(CLERK_NO, ""));							// 행번
                dbParam.setJobDetailCd(StringUtils.defaultIfEmpty(request.getJobDetailCd(), ""));		// 상세직업코드
                dbParam.setCddReqCd(StringUtils.defaultIfEmpty(request.getCddReqCd(), ""));				// CDD요청여부
                dbParam.setIdCardCd(("CASA".equals(BIZ_TYPE) || "BBCA".equals(BIZ_TYPE)) ? "N" : StringUtils.defaultIfEmpty(request.getCddReqCd(), "")); // 신분증 코드
                dbParam.setNewUserFlg(StringUtils.defaultIfEmpty(request.getNewUserFlg(), ""));			// 신규고객여부
                dbParam.setDelObjFlg(StringUtils.defaultIfEmpty(request.getDelObjFlg(), ""));			// 삭제대상여부
                dbParam.setBrnchNo(StringUtils.defaultIfEmpty(request.getBrnchNo(), ""));				// 점번호
                dbParam.setErrCd(StringUtils.defaultIfEmpty(request.getErrCd(), ""));					// 에러코드
                dbParam.setErrMsg(StringUtils.defaultIfEmpty(request.getErrMsg(), ""));					// 에러메시지
                dbParam.setCnnctnTradNo(StringUtils.defaultIfEmpty(request.getCnnctnTradNo(), ""));		// 제휴처거래번호
                dbParam.setTradRegGb(("Y".equals(StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("isCSL", String.class), "")) ? "1" : "0")); // 0 : MA3.0, 1:OpenAPI
                dbParam.setPerPart(StringUtils.isNotEmpty(request.getPerPart())
                                ? StringUtils.defaultIfEmpty(request.getPerPart(), "")
                                : StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("H92C_YOGEORE", String.class), "")); // 20230905.SK2.UI-1784에 거래자구분 "개인"으로 노출되는 내용 개선.
                dbParam.setDocEvdcCd(StringUtils.defaultIfEmpty(request.getDocEvdcCd(), "")); 			// 증빙서류확인
                dbParam.setHlPrsnCd(StringUtils.defaultIfEmpty(request.getHlPrsnCd(), ""));				// 무주택자확인
                dbParam.setAdSbstCd(StringUtils.defaultIfEmpty(request.getAdSbstCd(), "")); 			// 사전청약확인
                dbParam.setKwInsrCd(StringUtils.defaultIfEmpty(request.getKwInsrCd(), "")); 			// 권원보험확인
                dbParam.setReaCd(StringUtils.defaultIfEmpty(REACDN, ""));
                dbParam.setIntegratedConselingYn(
                                StringUtils.defaultIfEmpty(INTEGRATED_CONSELING_YN, "")); 				// 통합대출여부

                /* 2023-03-14 대출이동여부 추가 */
                dbParam.setPreLoanMoveYn(StringUtils.defaultIfEmpty(request.getPreLoanMoveYn(), "")); 	// 대출이동여부-상담
                dbParam.setRealLoanMoveYn(StringUtils.defaultIfEmpty(request.getRealLoanMoveYn(), "")); // 대출이동여부-신청

                dbParam.setGovmDataType(StringUtils.defaultIfEmpty(request.getGovmDataType(), "")); 	// 스크래핑타입 G:공공데이터, S:스크래핑
                dbParam.setGovmSignType(StringUtils.defaultIfEmpty(request.getGovmSignType(), "")); 	// 서명타입 1:공동, B:금융 pin, C:금융생체, 9:디지털
                dbParam.setGovmSignErr(StringUtils.defaultIfEmpty(request.getGovmSignErr(), ""));

                // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
                dbParam.setLoanPurpose(StringUtils.defaultIfEmpty(request.getLoanPurpose(), "")); 		// 대출목적(구입자금, 생안자금, 임차반환, 갈아타기)
                dbParam.setCollateralType(StringUtils.defaultIfEmpty(request.getCollateralType(), "")); // 담보유형(APT, 오피스텔, 빌라/다세대)

                // 24.09.06 SC 제일 대출여부 추가
                dbParam.setOplCnsltYn(StringUtils.defaultIfEmpty(request.getOplCnsltYn(), "")); 		// SC 제일  대출여부 추가

                synchronized (this) {
                	// 진행상태 등록 (asis : NF_TRADINFO_MGT_I_02)
                	log.debug("진행상태 등록 param => {}", dbParam);
                	nfTradeInfoMgtDao.insertOngoingTradeInfo(dbParam);
                	TRAD_NO = dbParam.getTradNo();
                }

                break;

        	case "NF_TRADINFO_MGT_U_01": // 진행상태 변경
                // 진행상태 History 등록 (as0is : NF_TRADINFO_HIST_MGT_I_01)
        		NonFaceTradeInfoHistoryParameter dbHistoryParam = new NonFaceTradeInfoHistoryParameter();
                dbHistoryParam.setCustNo(request.getCustNo());
                dbHistoryParam.setTradNo(request.getTradNo());
                synchronized (this) {
                	nfTradeInfoHistMgtDao.insertTradInfoHist(dbHistoryParam);
                }

                RegisterOngoingTradeInfoParameter dbUParam = new RegisterOngoingTradeInfoParameter();

                dbUParam.setPrdctId(request.getPrdctId()); 											// 상품ID
                dbUParam.setPrdctCd(request.getPrdctCd()); 											// 상품코드
                dbUParam.setPrdctNm(request.getPrdctNm()); 											// 상품명
                dbUParam.setClerkNo(StringUtils.defaultIfEmpty(CLERK_NO, "")); 						// 소개자행번
                dbUParam.setCddReqCd(request.getCddReqCd()); 										// CDD정상여부 (정상이 아닐 경우 "N")
                dbUParam.setIdCardCd(request.getIdCardCd()); 										// 신분증 코드
                dbUParam.setIdCardCnt(request.getIdCardCnt()); 										// 신분증 진위 건수
                dbUParam.setBrnchNo(request.getBrnchNo()); 											// 점번호
                dbUParam.setReqAmt(request.getReqAmt()); 											// 신청금액
                dbUParam.setAuthntIndCd(request.getAuthntIndCd()); 									// 인증유형 여부
                dbUParam.setAuthntInd(request.getAuthntInd()); 										// 인증유형(CASA)
                dbUParam.setAuthntReqDt(request.getAuthntReqDt());
                dbUParam.setAuthntReqExpireDt(request.getAuthntReqExpireDt()); 						// 3영업일 후
                dbUParam.setAuthntReqCmpltnDt(request.getAuthntReqCmpltnDt()); 						// 인증신청만료일자
                dbUParam.setKfbAcctNo(request.getKfbAcctNo()); 										// 당행계좌
                dbUParam.setAlliancCd(request.getAlliancCd());	 									// 제휴코드
                dbUParam.setEffctvInt(request.getEffctvInt()); 										// 적용금리
                dbUParam.setLoanPrd(request.getLoanPrd()); 											// 대출기간
                dbUParam.setRdmptnMthd(request.getRdmptnMthd()); 									// 상환방법
                dbUParam.setExecDt(request.getExecDt()); 											// 대출 실행일자
                dbUParam.setRdmptnDt(request.getRdmptnDt()); 										// 대출상환기일
                dbUParam.setLoanReqNo(request.getLoanReqNo()); 										// 대출신청번호
                dbUParam.setLoanAccptNo(request.getLoanAccptNo()); 									// 대출접수번호
                dbUParam.setErrCd(request.getErrCd()); 												// 에러코드
                dbUParam.setErrMsg(request.getErrMsg()); 											// 에러메시지
                dbUParam.setCoplCmmnClltrlInd(request.getCoplCmmnClltrlInd()); 						// 부부공동담보여부
                dbUParam.setRjctnCd(request.getRjctnCd()); 											// 거절코드
                dbUParam.setRjctnMsg(request.getRjctnMsg()); 										// 거절사유

                dbUParam.setBizType(BIZ_TYPE);														// 업무구분
                dbUParam.setCustNo(StringUtils.defaultIfEmpty(request.getCustNo(), "")); 			// 고객번호
                dbUParam.setTradNo(StringUtils.defaultIfEmpty(request.getTradNo(), "")); 			// 거래번호
                dbUParam.setCallCntrStsCd(request.getCallCntrStsCd());								// 콜센터상태코드
                dbUParam.setPrgrssStsCd(request.getPrgrssStsCd());									// 진행상태코드
                dbUParam.setCnnctnTradNo(request.getCnnctnTradNo());								// 제휴처거래번호
                dbUParam.setCnnctnWay(StringUtils.defaultIfEmpty(CNNCTN_WAY, "")); 					// 제휴처
                dbUParam.setPerPart(StringUtils.isNotEmpty(request.getPerPart())
                                ? StringUtils.defaultIfEmpty(request.getPerPart(), "")
                                : StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("H92C_YOGEORE", String.class), "")); // 20230905.SK2.UI-1784에 거래자구분 "개인"으로 내용 개선.
                dbUParam.setDocEvdcCd(StringUtils.defaultIfEmpty(request.getDocEvdcCd(), "")); 		// 증빙서류확인
                dbUParam.setLoanHopeDt(StringUtils.defaultIfEmpty(request.getLoanHopeDt(), ""));	// 대출희망일
                dbUParam.setHlPrsnCd(StringUtils.defaultIfEmpty(request.getHlPrsnCd(), "")); 		// 무주택자확인
                dbUParam.setAdSbstCd(StringUtils.defaultIfEmpty(request.getAdSbstCd(), "")); 		// 사전청약확인
                dbUParam.setKwInsrCd(StringUtils.defaultIfEmpty(request.getKwInsrCd(), "")); 		// 권원보험확인
                dbUParam.setReaCd(StringUtils.defaultIfEmpty(REACDN, "")); 							// REA코드
                dbUParam.setIntegratedConselingYn(
                                StringUtils.defaultIfEmpty(INTEGRATED_CONSELING_YN, ""));	 		// 통합대출여부
                dbUParam.setEasyLoanYn(StringUtils.defaultIfEmpty(request.getEasyLoanYn(), "")); 	// 이지론여부
                dbUParam.setLoanDocInvstgtFlg(
                                StringUtils.defaultIfEmpty(request.getLoanDocInvstgtFlg(), "")); 	// autobooking 여부
                dbUParam.setSecurityAcctNo(StringUtils.defaultIfEmpty(request.getSecurityAcctNo(), "")); // BIB 2021-08-27 증권계좌번호

                dbUParam.setGovmDataType(StringUtils.defaultIfEmpty(request.getGovmDataType(), "")); // 스크래핑타입 G:공공데이터, S:스크래핑
                dbUParam.setGovmSignType(StringUtils.defaultIfEmpty(request.getGovmSignType(), "")); // 서명타입 1:공동, B:금융 pin, C:금융생체, 9:디지털
                dbUParam.setGovmSignErr(StringUtils.defaultIfEmpty(request.getGovmSignErr(), ""));	 // 서명오류

                /* 2023-03-14 대출이동여부 추가 */
                dbUParam.setPreLoanMoveYn(StringUtils.defaultIfEmpty(request.getPreLoanMoveYn(), "")); // 대출이동여부-상담
                dbUParam.setRealLoanMoveYn(StringUtils.defaultIfEmpty(request.getRealLoanMoveYn(), "")); // 대출이동여부-신청

                /* 2023-04-03 본부우대금리 추가 */
                dbUParam.setHqPrimeRateApplyYn(
                                StringUtils.defaultIfEmpty(request.getHqPrimeRateApplyYn(), "")); 	// 본부우대금리 적용여부
                dbUParam.setHqPrimeRate(StringUtils.defaultIfEmpty(request.getHqPrimeRate(), "")); 	// 본부우대금리

                // 주택담보대출 통합상담 - 대출목적, 담보유형 추가
                dbUParam.setLoanPurpose(StringUtils.defaultIfEmpty(request.getLoanPurpose(), "")); 		 // 대출목적(구입자금, 생안자금, 임차반환, 갈아타기)
                dbUParam.setCollateralType(StringUtils.defaultIfEmpty(request.getCollateralType(), "")); // 담보유형(APT, 오피스텔, 빌라/다세대)

                /* 2023-07-12 퍼스트홈론 (배우자 고객번호,배우자 거래번호,상담일련번호,상담일자 추가 */
                dbUParam.setSpouseCustNo(StringUtils.defaultIfEmpty(request.getSpouseCustNo(), "")); // 배우자 고객번호
                dbUParam.setSpouseTradNo(StringUtils.defaultIfEmpty(request.getSpouseTradNo(), "")); // 배우자 거래번호
                dbUParam.setAdvSeqNo(StringUtils.defaultIfEmpty(request.getAdvSeqNo(), "")); 		 // 상담일련번호
                dbUParam.setAdvDt(StringUtils.defaultIfEmpty(request.getAdvDt(), "")); 				 // 상담일자

                // 24.09.06 SC 제일 대출여부 추가
                dbUParam.setOplCnsltYn(StringUtils.defaultIfEmpty(request.getOplCnsltYn(), "")); // SC 제일 대출여부 추가

                if("DONE".equalsIgnoreCase(PRGRSS_STS_CD)) {
    				// 2026.02.06 비대면 거래완료 여부 추가
                	dbUParam.setCmpltnYn("Y");	// 비대면 거래완료 여부 추가 (Y:거래완료, 그외: 미완료)
    			}
                
                // 진행상태 수정  (asis : NF_TRADINFO_MGT_U_01)
                log.debug("진행상태 수정 param => {}", dbUParam);
                nfTradeInfoMgtDao.updateOngoingTradeInfo(dbUParam);

                break;
        }

        
        // LGS 페이코 적금으로 인한 추가(TDSV)
        // 제휴 추가 시 소스수정 최소화하기위해 해당 조건 변경하려했으나, 기존 CASA만 조건에 설정되어있었던 이유를 알지 못해 변경 불가....(업무구분만 추가함)
        String tossCnnctnWay = PropertiesUtils.getString("toss.cnnctn_way");
        String tossProcess = "N";
        String tossDoneProcess = "N";

        /*
         * 토스 거절났을 경우 해결
         */
        if( ("MAPL".equals(BIZ_TYPE) && tossCnnctnWay.equals(StringUtils.defaultIfEmpty(CNNCTN_WAY, "")) && 
        		("PRGS".equals(StringUtils.defaultIfEmpty(request.getPrgrssStsCd(), "")) || "DONE".equals(StringUtils.defaultIfEmpty(request.getPrgrssStsCd(), ""))))) {
            tossProcess = "Y";

            // 토스의 경우 마지막 DONE상태이고, CALL_CNTR_STS_CD "PLE2", "PLE3", "PLE4" 일 경우는
            // NF_CNNCTN_TRADINFO_MGT_I_01 를 타지 않도록 처리한다.
            String CALL_CNTR_STS_CD = StringUtils.defaultIfEmpty(request.getCallCntrStsCd(), "");
            if ("Y".equals(tossProcess) && 
            		("PLE2".equals(CALL_CNTR_STS_CD) || "PLE3".equals(CALL_CNTR_STS_CD) || "PLE4".equals(CALL_CNTR_STS_CD))
                            || "PLE5".equals(CALL_CNTR_STS_CD) || "PLEN".equals(CALL_CNTR_STS_CD) ) {
            	tossDoneProcess = "Y";
            }
        }

        /*
         * 토스 거절났을 경우 해결
         */
        if ("CASA".equals(BIZ_TYPE) || "TDSV".equals(BIZ_TYPE) || "Y".equals(tossProcess) || "TDSB".equals(BIZ_TYPE)) {
        	RegisterPartnerTradeInfoParameter tossDbParam = new RegisterPartnerTradeInfoParameter();
                
    		String CNNCTN_TRAD_NO = "";

            // 토스대출인 경우 cnnctn_trad_no 세션 유지 , 기존 거래는 유지
            if ("Y".equals(tossProcess)) {
                CNNCTN_TRAD_NO = StringUtils.defaultIfEmpty(request.getCnnctnTradNo(),
                                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CNNCTN_TRAD_NO", String.class), ""));
            } else {
            	CNNCTN_TRAD_NO = StringUtils.defaultIfEmpty(request.getCnnctnTradNo(), "");
            }

            //제휴거래처 테이블 TRADNO 에러 수정
            
            
            // 제휴거래처 테이블 TRADNO 에러 수정
            tossDbParam.setCustNo(request.getCustNo());
            
            if (StringUtils.isNotEmpty(TRAD_NO)) {
                tossDbParam.setTradNo(TRAD_NO);
            } else {
            	OngoingTradeInfoInquiryParameter tradeInfoParameter = new OngoingTradeInfoInquiryParameter();
            	tradeInfoParameter.setCustNo(request.getCustNo());
                tradeInfoParameter.setTradRegGb(("Y".equals(StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("isCSL", String.class), "")) ? "1" : "0"));
            	// SJS TRADNO을 안가져와서 NULL들어가는 현상 수정
            	tradeInfoParameter.setBizType(BIZ_TYPE);
                List<CheckOngoingTradeInfoInquiryResult> tossDbResponse = nfTradeInfoMgtDao.selectOngoingTradeInfoList(tradeInfoParameter);
                tossDbParam.setTradNo(tossDbResponse.get(0).getTradNo());
            }

            if (StringUtils.isNotEmpty(CNNCTN_TRAD_NO) && "N".equals(tossDoneProcess)) {
                tossDbParam.setCnnctnTradNo(CNNCTN_TRAD_NO);
                tossDbParam.setCnnctnWay(StringUtils.defaultIfEmpty(CNNCTN_WAY, ""));
                tossDbParam.setCnnctnWayDynamic(StringUtils.defaultIfEmpty(CNNCTN_WAY, ""));
                tossDbParam.setCnnctnLoanLmtAmt(StringUtils.defaultIfEmpty(CNNCTN_LOAN_LMT_AMT, ""));
                tossDbParam.setCnnctnSignId(StringUtils.defaultIfEmpty(CNNCTN_SIGN_ID, ""));

                log.debug("@#@#@#@# LGS @#@#@#@## 진행상태 등록/변경 및 제휴처 거래 관련 처리 [MA3CMMBIZ010_103S] :: 제휴처 거래 정보 등록[NF_CNNCTN_TRADINFO_MGT_I_01] [ PARAM ]: -> " + tossDbParam);
                // 제휴처 거래 정보 등록 (asis : NF_CNNCTN_TRADINFO_MGT_I_01)
                nfCnnctnTradInfoMgtDao.insertPartnerTransactionInfo(tossDbParam);
            }

            if (("DONE".equals(request.getPrgrssStsCd()) && "N".equals(tossProcess))
            		|| ("Y".equals(tossProcess) && "PLEN".equals(request.getCallCntrStsCd()))) { //토스의 경우 마지막 PLEN 에서만 update 한다
            	
            	log.debug("@#@#@#@# LGS @#@#@#@## 진행상태 등록/변경 및 제휴처 거래 관련 처리 [MA3CMMBIZ010_103S] :: 제휴처 거래완료 업데이트[NF_CNNCTN_TRADINFO_MGT_U_01] [ PARAM ]: -> " + tossDbParam);
                // 제휴처 거래완료 업데이트 (asis : NF_CNNCTN_TRADINFO_MGT_U_01)
                nfCnnctnTradInfoMgtDao.updatePartnerTransactionComplete(tossDbParam);
            }
        }

        response.setTradNo(TRAD_NO);
        log.debug("####################### 진행상태 등록/변경 및 제휴처 거래 관련 처리 [MA3CMMBIZ010_103S] END ###\n", response);
        return response;
	}
	
	/**
	 * <pre>
	 * 진행중인 상품 취소 처리
	 * </pre>
	 * @param request OngoingTradeInfoCancelRequest
	 * @return OngoingTradeInfoCancelResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_107S
	 */
	@ComponentOperation(name = "진행중인 상품 취소 처리 (asis : MA3CMMBIZ010_107S)") 
	public OngoingTradeInfoCancelResponse cancelOngoingTradeInfo(OngoingTradeInfoCancelRequest request) {
		
		log.debug("####################### 진행중인 상품 취소 처리 [MA3CMMBIZ010_107S] START ###\n", request);
		
		OngoingTradeInfoCancelResponse result = new OngoingTradeInfoCancelResponse();
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), sessionManager.getGlobalValue("CUST_NO", String.class));
        String tradNo = StringUtils.defaultIfEmpty(request.getTradNo(), sessionManager.getGlobalValue("TRAD_NO", String.class));
        
		OngoingTradeInfoCancelParameter parameter = new OngoingTradeInfoCancelParameter();
		parameter.setCustNo(custNo);
		parameter.setTradNo(tradNo);
		// 비대면 진행 상품 취소 (asis : NF_TRADINFO_MGT_U_02)
		nfTradeInfoMgtDao.updateOngoingProductCancellation(parameter);
		result.setSuccessYn("Y");
		
		log.debug("####################### 진행중인 상품 취소 처리 [MA3CMMBIZ010_107S] END ###\n", request);
		return result;
	}
	
	/**
	 * <pre>
	 * UI 진행상태 업데이트 처리 – 대출 취소(MCI)
	 * </pre>
	 * @param request OngoingProcessCancelRequest
	 * @return OngoingProcessCancelResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_108S
	 */
	@ComponentOperation(name = "UI 진행상태 업데이트 처리 – 대출 취소(MCI) (asis : MA3CMMBIZ010_108S)") 
	public OngoingProcessCancelResponse cancelOnoingProcess(OngoingProcessCancelRequest request) throws PRCServiceException { 
		log.debug("####################### UI 진행상태 업데이트 처리 – 대출 취소(MCI) [MA3CMMBIZ010_108S] START ###\n", request);
		OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
		
        if (sessionManager.isLogin() || BankingBizUtils.isAuthenticated()) {
        	TradeInfoInquiryParameter statusInquiryDbParam = new TradeInfoInquiryParameter();
        	TradeInfoInquiryResult statusInquiryDbResponse = new TradeInfoInquiryResult();

        	String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), sessionManager.getGlobalValue("CUST_NO", String.class));

            // session cust_no 없을 경우 조회후 session set Toss 강제 취소로 인한 추가
            if (StringUtils.isEmpty(custNo)) {
            	custNo = getCustNo();
            }

            statusInquiryDbParam.setCustNo(custNo);
            statusInquiryDbParam.setTradNo(StringUtils.defaultIfEmpty(request.getTradNo(), ""));

            // 거래정보및고객정보(SSN)조회 (asis : NF_TRADINFO_MGT_S_05)
            statusInquiryDbResponse = nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(statusInquiryDbParam);

            String bizType = statusInquiryDbResponse.getBizType() != null ? String.valueOf(statusInquiryDbResponse.getBizType()) : "";
            String tradNo = statusInquiryDbResponse.getTradNo() != null ? String.valueOf(statusInquiryDbResponse.getTradNo()) : "";
            String callCntrStsCd = statusInquiryDbResponse.getCallCntrStsCd() != null ? String.valueOf(statusInquiryDbResponse.getCallCntrStsCd()) : "";
            
            // 신용, 간편대출, 모바일소액대출인 경우만 처리
            if (StringUtils.isNotEmpty(tradNo) && bizType.matches("CRPL|SPPL|MAPL|NSPL|DLEV|DRPL|OPPL")) {
                // 20200608 새희망홀씨2로 인한 필드추가 start
                String businessFncID = "";
                String fiUpmuGb2 = "";
                String interfaceId = "";

                /*
                 * pattenStr
                 * CRPL/SPPL/MAPL
                 * 9047 기취소 건입니다. 해당 오류는 취소 완료이므로, 정상처리함
                 * 9132 대출신청 취소건 입니다.
                 * 9716 해당신청번호 없음
                 * 
                 * NSPL
                 * 9510/9720 해당접수번호로 신청건 없음
                 * 9132 대출신청 취소건 입니다.
                 * 9144 대출실행완료건 입니다.
                 */
                String pattenStr = "";

                if ("NSPL".equals(bizType)) {
                    MciYp0049020001Req sendData = new MciYp0049020001Req();

                    businessFncID = "YP0049020001";
                    fiUpmuGb2 = "30";
                    pattenStr = "9132|9510|9720|9144";
                    interfaceId = "MCI_YP0049020001";
                    // 20200608 새희망홀씨2로 인한 필드추가 end

                    MciRequestOptions mciCfg = this.hostClient.getMciRequestOptions(interfaceId);

                    mciCfg.setTranCd(businessFncID);
                    mciCfg.setTxnBrNo("0019"); // as-is HOMEBRANCH
                    mciCfg.setBlngBrNo("0019"); // as-is AGENTBRANCH
                    // to-be는 뭘까요?
                    // sendData.putMciComm("SUBTASKCODE" , "0001" );

                    String loanAccptNo = statusInquiryDbResponse.getLoanAccptNo() != null
                                    ? String.valueOf(statusInquiryDbResponse.getLoanAccptNo())
                                    : "";

                    sendData.setFIBRNO(SessionUtils.getSessionValue("PerBusNo")); // 주민번호
                    sendData.setFIJUBNO(loanAccptNo); 	// 접수번호 (NF_TRADINFO_MGT > LOAN_ACCPT_NO)
                    sendData.setFINXTTBL("1"); 			// 연속거래 (고정 : 1)
                    sendData.setFIGUBUN("2"); 			// 거래구분 (고정 : 2)
                    sendData.setFICHWSAYU("1"); 		// 취소사유 (고정 : 1)
                    sendData.setFICCNCHSHB("000000"); 	// 취소행번
                    sendData.setFISELFBANKFLG("Y"); 	// 셀프뱅크여부
                    sendData.setFIUPMUGB2(fiUpmuGb2); 	// 업무구분 (새희망홀씨:30)

                    try {
                        MciResponse<MciYp0049020001Res> mciResponse = hostClient.sendMci(mciCfg, sendData, MciYp0049020001Res.class);
                        MciYp0049020001Res output = mciResponse.getResponse();
                        response = tradeInfoMapper.toResponseFromMciYp0049020001Res(output);
                        response.setSuccessYn("Y");
                    } catch (PRCServiceException e) {
                        String errMsg = e.getErrorMessage();

                        Pattern pattern = Pattern.compile(pattenStr);
                        Matcher matcher = pattern.matcher(errMsg);

                        if (matcher.find()) {
                            // 9047 기취소 건입니다. 해당 오류는 취소 완료이므로, 정상처리함
                            // 9132 대출신청 취소건 입니다.
                            // 9716 해당신청번호 없음
                            response.setSuccessYn("Y");
                        } else {
                            /*
                             * PLE5단계 호스트본거래 정상진행했으나 타임아웃등 모바일채널에는 에러응답으로주는케이스가 발생함
                             * 해당케이스의 경우 여신취소거래에서 9178 에러로 응답주기로하여 모바일채널에서는 정상케이스로 판별 1784
                             * DONE 처리
                             * 
                             * pattenStr2
                             * CRPL/SPPL/MAPL/NSPL
                             * 9178 : 기완료건
                             */
                            String pattenStr2 = "9178";
                            pattern = Pattern.compile(pattenStr2);
                            matcher = pattern.matcher(errMsg);

                            if (matcher.find()) {
                                response.setSuccessYn("Y");
                                response.setErrType("9178");
                            } else {
                                response.setSuccessYn("N");
                                response.setErrType(errMsg);
                            }
                        }
                    }
                    log.debug("@#@#@#@# LGS @#@#@#@## inqMCIIBADDR0104 MCI_" + businessFncID + "_OUT ->" + response);
                } else {
                    MciYp0049010001Req sendData = new MciYp0049010001Req();

                    businessFncID = "YP0049010001";
                    fiUpmuGb2 = "";
                    pattenStr = "9047|9132|9716";
                    interfaceId = "MCI_YP0049010001";

                    MciRequestOptions mciCfg = this.hostClient.getMciRequestOptions(interfaceId);

                    mciCfg.setTranCd(businessFncID);
                    mciCfg.setTxnBrNo("0019"); // as-is HOMEBRANCH
                    mciCfg.setBlngBrNo("0019"); // as-is AGENTBRANCH
                    // to-be는 뭘까요?
                    // sendData.putMciComm("SUBTASKCODE" , "0001" );

                    String loanAccptNo = statusInquiryDbResponse.getLoanAccptNo() != null ? String.valueOf(statusInquiryDbResponse.getLoanAccptNo()) : "";

                    sendData.setFIJUBNO(loanAccptNo); // 접수번호 (NF_TRADINFO_MGT > LOAN_ACCPT_NO)
                    sendData.setFINXTTBL("1"); // 연속거래 (고정 : 1)
                    sendData.setFIGUBUN("2"); // 거래구분 (고정 : 2)
                    sendData.setFISINNO(""); // 신청번호 (NF_TRADINFO_MGT > LOAN_REQ_NO)
                    sendData.setFICHWSAYU("1"); // 취소사유 (고정 : 1)
                    sendData.setFICCNCHSHB("000000"); // 취소행번
                    sendData.setFISELFBANKFLG("Y"); // 셀프뱅크여부

                    try {
                        MciResponse<MciYp0049010001Res> mciResponse = hostClient.sendMci(mciCfg, sendData, MciYp0049010001Res.class);
                        MciYp0049010001Res output = mciResponse.getResponse();
                        response = tradeInfoMapper.toResponseFromMciYp0049010001Res(output);
                        response.setSuccessYn("Y");
                    } catch (PRCServiceException e) {
                        String errMsg = e.getErrorMessage();

                        Pattern pattern = Pattern.compile(pattenStr);
                        Matcher matcher = pattern.matcher(errMsg);

                        if (matcher.find()) {
                            // 9047 기취소 건입니다. 해당 오류는 취소 완료이므로, 정상처리함
                            // 9132 대출신청 취소건 입니다.
                            // 9716 해당신청번호 없음
                            response.setSuccessYn("Y");
                        } else {
                            /*
                             * PLE5단계 호스트본거래 정상진행했으나 타임아웃등 모바일채널에는 에러응답으로주는케이스가 발생함
                             * 해당케이스의 경우 여신취소거래에서 9178 에러로 응답주기로하여 모바일채널에서는 정상케이스로 판별 1784
                             * DONE 처리
                             * 
                             * pattenStr2
                             * CRPL/SPPL/MAPL/NSPL
                             * 9178 : 기완료건
                             */
                            String pattenStr2 = "9178";
                            pattern = Pattern.compile(pattenStr2);
                            matcher = pattern.matcher(errMsg);

                            if (matcher.find()) {
                                response.setSuccessYn("Y");
                                response.setErrType("9178");
                            } else {
                                response.setSuccessYn("N");
                                response.setErrType(errMsg);
                            }
                        }
                    }
                }
                log.debug("@#@#@#@# LGS @#@#@#@## inqMCIIBADDR0104 MCI_" + businessFncID + "_OUT ->" + response);
            } 
            // 모바일 전세보증론
            else if (StringUtils.isNotEmpty(tradNo) && bizType.matches("CMJG|CMJL")
                            && callCntrStsCd.matches("JGRR|JGSE|JLRR|JLSE")) {
                    MciDy7200170001Req sendData = new MciDy7200170001Req();

                    MciRequestOptions mciCfg = this.hostClient.getMciRequestOptions("MCI_DY7200170001");

                    mciCfg.setTranCd("DY7200170001");
                    mciCfg.setTxnBrNo("0019"); // as-is HOMEBRANCH
                    mciCfg.setBlngBrNo("0019"); // as-is AGENTBRANCH
                    // to-be는 뭘까요?
                    // sendData.putMciComm("SUBTASKCODE" , "0001" );

                    sendData.setFIBRNO(statusInquiryDbResponse.getBrnchNo()); // 점번호
                    sendData.setFIGUBUN("1"); // 거래번호
                    sendData.setFIDCGB("11"); // 대출구분 (11:전세보증론)
                    sendData.setFIJUBNO(statusInquiryDbResponse.getLoanAccptNo()); // 접수번호

                    MciResponse<MciDy7200170001Res> mciResponse = hostClient.sendMci(mciCfg, sendData, MciDy7200170001Res.class);
                    MciDy7200170001Res output = mciResponse.getResponse();
                    response = tradeInfoMapper.toResponseFromMciDy7200170001Res(output);
                    response.setSuccessYn("Y");
                    log.debug("@#@#@#@# LGS @#@#@#@## inqMCIIBADDR0104 MCI_DY7200170001_OUT ->" + response);
            } else {
                    // 성공처리
                    response.setSuccessYn("Y");
            }
	    } else {
	            response.setSuccessYn("N");
	    }
	
        log.debug("####################### UI 진행상태 업데이트 처리 – 대출 취소(MCI) [MA3CMMBIZ010_108S] END ###\n", response);
	    return response;

	}
	
	/**
	 * <pre>
	 * 비대면 화면정보관리 등록
	 * </pre>
	 * @param request RegisterNonFaceScreenInfoRequest
	 * @return RegisterNonFaceScreenInfoResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ010_109S
	 */
	@ComponentOperation(name = "비대면 화면정보관리 등록 (asis : MA3CMMBIZ010_109S)")
	public RegisterNonFaceScreenInfoResponse registNonFaceScreenInfo(RegisterNonFaceScreenInfoRequest request) throws PRCServiceException {
		log.debug("####################### 비대면 화면정보관리 등록 [MA3CMMBIZ010_109S] START ###\n", request);
		
		String scrnDataInfo = StringUtils.defaultIfEmpty(request.getScrnDataInfo(), "");
		if(StringUtils.hasLength(scrnDataInfo)) {
			scrnDataInfo = unescape(scrnDataInfo);
		}
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CUST_NO", String.class), ""));
		RegisterNonFaceScreenInfoResponse response = new RegisterNonFaceScreenInfoResponse();
		
		RegisterNonFaceScreenInfoParameter dbParam = new RegisterNonFaceScreenInfoParameter();
		dbParam.setCustNo(custNo);
		dbParam.setTradNo(StringUtils.defaultIfEmpty(request.getTradNo(), ""));
		dbParam.setBizType(StringUtils.defaultIfEmpty(request.getBizType(), ""));
		dbParam.setScrnDataInfo(scrnDataInfo);
		
		// 비대면 화면정보관리 등록 (asis : NF_SCRN_INFO_MGT_I_01)
		nfScreenInfoMgtDao.insertNonFaceScreenInfoManagement(dbParam);
		
		response.setSuccessYn("Y");
		
		log.debug("####################### 비대면 화면정보관리 등록 [MA3CMMBIZ010_109S] END ###\n", response);
		return response;
	}
	
	/**
	 * <pre>
	 * 스크래핑 데이터 저장
	 * </pre>
	 * @param request RegisterScrapingDataRequest
	 * @return RegisterScrapingDataResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ015_101S
	 */
	@ComponentOperation(name = "스크래핑데이터 저장 (asis : MA3CMMBIZ015_101S)")
	public RegisterScrapingDataResponse registScrapingData(RegisterScrapingDataRequest request) throws PRCServiceException {
		log.debug("####################### 스크래핑데이터 저장 [MA3CMMBIZ015_101S] START ###\n", request);
		RegisterScrapingDataResponse response = new RegisterScrapingDataResponse();
		
		NonFaceScrapingInfoParameter dbParam = new NonFaceScrapingInfoParameter();
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CUST_NO", String.class), ""));
		String scrppngData = StringUtils.defaultIfEmpty(request.getScrppngData(), "");
		if(StringUtils.hasLength(scrppngData)) {
			scrppngData = unescape(scrppngData);
		}
		
		// select
		dbParam.setCustNoS(Integer.parseInt(custNo)); 										// 고객번호(PK)
		dbParam.setTradNoS(request.getTradNo()); 											// 거래번호(PK)
		dbParam.setObjInstS(StringUtils.defaultIfEmpty(request.getObjInst(), "")); 			// 대상기관(PK)
		dbParam.setObjDocCdS(StringUtils.defaultIfEmpty(request.getObjDocCd(), "")); 		// 대상문서코드(PK)

        // 조건절
		dbParam.setCustNoW(Integer.parseInt(custNo)); 										// 고객번호(PK)
		dbParam.setTradNoW(request.getTradNo()); 											// 거래번호(PK)
		dbParam.setObjInstW(StringUtils.defaultIfEmpty(request.getObjInst(), "")); 			// 대상기관(PK)
		dbParam.setObjDocCdW(StringUtils.defaultIfEmpty(request.getObjDocCd(), "")); 		// 대상문서코드(PK)

        // update
		dbParam.setScrppngDataU(scrppngData); 	// 스크래핑데이터
		dbParam.setErrCdU(StringUtils.defaultIfEmpty(request.getErrCd(), "")); 				// 에러코드
		dbParam.setErrMsgU(StringUtils.defaultIfEmpty(request.getErrMsg(), "")); 			// 에러메시지

        // insert
		dbParam.setCustNo(Integer.parseInt(custNo)); 										// 고객번호(PK)
		dbParam.setTradNo(request.getTradNo()); 											// 거래번호(PK)
		dbParam.setObjInst(StringUtils.defaultIfEmpty(request.getObjInst(), "")); 			// 대상기관(PK)
		dbParam.setObjDocCd(StringUtils.defaultIfEmpty(request.getObjDocCd(), "")); 		// 대상문서코드(PK)
		dbParam.setScrppngData(scrppngData); 	// 스크래핑데이터
		dbParam.setErrCd(StringUtils.defaultIfEmpty(request.getErrCd(), "")); 				// 에러코드
		dbParam.setErrMsg(StringUtils.defaultIfEmpty(request.getErrMsg(), "")); 			// 에러메시지

		// 스크래핑타입 G:공공데이터, S:스크래핑
        String SCR_TYPE = StringUtils.defaultIfEmpty(request.getScrType(), ""); 
        // 서명타입 1:공동, B:금융 pin, C:금융생체, 9:디지털
        String CERT_TYPE = StringUtils.defaultIfEmpty(request.getCertType(), "");
        String CERT_TYPE_ERR = "";

        dbParam.setScrType(SCR_TYPE);
        dbParam.setCertType(CERT_TYPE);
        dbParam.setCertTypeErr(CERT_TYPE_ERR);

        // 스크래핑데이터 저장 (asis : NF_SCRPPNG_MGT_U_01)
        int updateResult = nfScrapingMgtDao.updateScrapingInfo(dbParam);
        response.setUpdateResult(updateResult);
        
        log.debug("####################### 스크래핑데이터 저장 [MA3CMMBIZ015_101S] END ###\n", response);
		return response;
	}
	
	/**
	 * <pre>
	 * CDD전자문서 정보 등록
	 * </pre>
	 * @param request RegisterEdocRecoveryDataRequest
	 * @return RegisterEdocRecoveryDataResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ013_101S
	 */
	@ComponentOperation(name = "CDD전자문서 정보 등록 (asis : MA3CMMBIZ013_101S)")
	public RegisterEdocRecoveryDataResponse registEdocRecoveryData(RegisterEdocRecoveryDataRequest request) throws PRCServiceException {
		log.debug("####################### CDD전자문서 정보 등록 [MA3CMMBIZ013_101S] START ###\n", request);
		CddElectronicDocumentParameter dbParam = new CddElectronicDocumentParameter();
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CUST_NO", String.class), ""));
		String RCVRY_MK = StringUtils.defaultIfEmpty(request.getRcvryMk(), "");
        String EDOC_CD = StringUtils.defaultIfEmpty(request.getEdocCd(), "");
        String recoveryData = StringUtils.defaultIfEmpty(request.getRcvryData(), "");
        
        if(StringUtils.hasLength(recoveryData)) {
			recoveryData = unescape(recoveryData);
		}
        
        dbParam.setCustNo(custNo);
        dbParam.setTradNo(StringUtils.defaultIfEmpty(request.getTradNo(), ""));
        dbParam.setRcvryData(recoveryData);
        dbParam.setRcvryMk(RCVRY_MK);
        dbParam.setEdocCd(EDOC_CD);
        
        // 복구 구분
        if (StringUtils.isEmpty(RCVRY_MK)) {
        	dbParam.setRcvryMk("CDD");
        }

        // 전자문서 코드
        if (StringUtils.isEmpty(EDOC_CD)) {
            dbParam.setEdocCd("CDD");
        }

        RegisterEdocRecoveryDataResponse response = new RegisterEdocRecoveryDataResponse();
        
        // CDD전자문서 정보 등록 (asis : NF_EDOC_RCVRY_MGT_I_01)
		nfCddEdocRcvryMgtDao.insertCddElectronicDocumentInfo(dbParam);
		response.setSuccessYn("Y");
		
		log.debug("####################### CDD전자문서 정보 등록 [MA3CMMBIZ013_101S] END ###\n", response);
		return response;
	}
	
	/**
	 * <pre>
	 * 화면정보 및 스크래핑 데이터 조회
	 * </pre>
	 * @param request ScreenAndScrapingInfoRequest
	 * @return ScreenAndScrapingInfoResponse
	 * @throws PRCServiceException
	 * @description MA3CMMBIZ015_102S
	 */
	@ComponentOperation(name = "화면정보 및 스크래핑 데이터 조회 (asis : MA3CMMBIZ015_102S)")
	public ScreenAndScrapingInfoResponse getScreenAndScrapingInfo(ScreenAndScrapingInfoRequest request) throws PRCServiceException {
		log.debug("####################### 비대면 화면정보 및 스크래핑 정보 조회 [MA3CMMBIZ015_102S] START ###\n", request);

		ScreenAndScrapingInfoResponse response = new ScreenAndScrapingInfoResponse();
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), SessionUtils.getSessionValue("CUST_NO"));
		String tradNo = StringUtils.defaultIfEmpty(request.getTradNo(), SessionUtils.getSessionValue("TRAD_NO"));
		String bizType = StringUtils.defaultIfEmpty(request.getBizType(), "");
		boolean isAvailable = true;
		
		NonFaceScreenAndScrapingInfoParameter paramNF_SCRN_INFO_MGT_S_01 = new NonFaceScreenAndScrapingInfoParameter();
		paramNF_SCRN_INFO_MGT_S_01.setBizType(bizType);
		paramNF_SCRN_INFO_MGT_S_01.setCustNo(custNo);
		paramNF_SCRN_INFO_MGT_S_01.setTradNo(tradNo);
		
		// 비대면 화면정보 및 스크래핑 정보 조회
		log.debug("@#@#@#@# SCRAPING INFO NF_SCRN_INFO_MGT_S_01 [ PARAM ] -> " + paramNF_SCRN_INFO_MGT_S_01);
		// 비대면 화면정보 및 스크래핑 정보 조회 (asis : NF_SCRN_INFO_MGT_S_01)
		NonFaceScreenAndScrapingInfoResult resultNF_SCRN_INFO_MGT_S_01 = nfScreenInfoMgtDao.selectScreenAndScrapingInfo(paramNF_SCRN_INFO_MGT_S_01);
		log.debug("@#@#@#@# SCRAPING INFO NF_SCRN_INFO_MGT_S_01 [ RESULT ] -> " + resultNF_SCRN_INFO_MGT_S_01);
		
		if (resultNF_SCRN_INFO_MGT_S_01 != null) {
	        String screenInfo = resultNF_SCRN_INFO_MGT_S_01.getScrnDataInfo() != null ? String.valueOf(resultNF_SCRN_INFO_MGT_S_01.getScrnDataInfo()) : "";
	        String scrapingInfo = resultNF_SCRN_INFO_MGT_S_01.getScrppngData() != null ? String.valueOf(resultNF_SCRN_INFO_MGT_S_01.getScrppngData()) : "";
	
	        //response.setScrnDataInfo(BankingBizUtils.toJSONStringFromObject(screenInfo));
            //response.setScrppngData(BankingBizUtils.toJSONStringFromObject(scrapingInfo));
	        response.setScrnDataInfo(screenInfo);
            response.setScrppngData(scrapingInfo);
            
	        log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] SCRN_DATA_INFO -> [" + screenInfo + "] #@#@#@");
	        log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] SCRPPNG_DATA -> ["+ scrapingInfo + "] #@#@#@");
	
	        // 직장인중금리(SPPL), 모바일소액(MAPL), 새희망홀씨(NSPL)인 경우에만 세션정보 업데이트 및 이어하기 데이터 비교 진행하도록 수정
	        if (bizType.matches("SPPL|MAPL|NSPL|CRPL|OPPL")) {
	            // 진행상태 테이블 조회해서 세션 Update
	        	TradeInfoInquiryParameter paramNF_TRADINFO_MGT_S_05 = new TradeInfoInquiryParameter();
	            paramNF_TRADINFO_MGT_S_05.setCustNo(custNo);
	            paramNF_TRADINFO_MGT_S_05.setTradNo(tradNo);
	
	            log.debug("@#@#@#@# SCRAPING INFO NF_TRADINFO_MGT_S_05 PARAM >>> [" + paramNF_TRADINFO_MGT_S_05 + "] #");
	            // 거래정보및고객정보(SSN)조회 (asis : NF_TRADINFO_MGT_S_05)
	            TradeInfoInquiryResult resultNF_TRADINFO_MGT_S_05 = nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(paramNF_TRADINFO_MGT_S_05);
	            log.debug("@#@#@#@# SCRAPING INFO NF_TRADINFO_MGT_S_05 RESULT >>> ["  + resultNF_TRADINFO_MGT_S_05 + "] #");
	
	            if (resultNF_TRADINFO_MGT_S_05 != null) {
	            	String infoCallCenterStatus = (resultNF_TRADINFO_MGT_S_05.getCallCntrStsCd() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getCallCntrStsCd()) : "").trim();
	                String infoTradNo = (resultNF_TRADINFO_MGT_S_05.getTradNo() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getTradNo()) : "").trim();
	                String infoBizType = (resultNF_TRADINFO_MGT_S_05.getBizType() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getBizType()) : "").trim();
	                String infoPrdctId = (resultNF_TRADINFO_MGT_S_05.getPrdctId() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getPrdctId()) : "").trim();
	                String infoPrdctCd = (resultNF_TRADINFO_MGT_S_05.getPrdctCd() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getPrdctCd()) : "").trim();
	                String infoPrdctNm = (resultNF_TRADINFO_MGT_S_05.getPrdctNm() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getPrdctNm()): "").trim();
	                String infoCnnctnWay = (resultNF_TRADINFO_MGT_S_05.getCnnctnWay() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getCnnctnWay()) : "").trim();
	                String infoClertNo = (resultNF_TRADINFO_MGT_S_05.getClerkNo() != null ? String.valueOf(resultNF_TRADINFO_MGT_S_05.getClerkNo()) : "").trim();
	
//	                String sessTradNo = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("TRAD_NO", String.class), "").trim();
	                String sessBizType = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("BIZ_TYPE", String.class), "").trim();
	                String sessPrdctId = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PRDCT_ID", String.class), "").trim();
                    String sessPrdctCd = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PRDCT_CD", String.class), "").trim();
                    String sessPrdctNm = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PRDCT_NM", String.class), "").trim();
                    String sessCnnctnWay = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CNNCTN_WAY", String.class), "").trim();
                    String sessClerkNo = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CLERK_NO", String.class), "").trim();

                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] TRAD_NO -> [" + infoTradNo + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] BIZ_TYPE -> [" + infoBizType + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] PRDCT_ID -> [" + infoPrdctId + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] PRDCT_CD -> [" + infoPrdctCd + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] PRDCT_NM -> [" + infoPrdctNm + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] CNNCTN_WAY -> [" + infoCnnctnWay + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] CLERK_NO -> [" + infoClertNo + "] #@#@#@");

                    // 진행상태 테이블 데이터와 현재 세션정보 비교 및 세션 업데이트
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_TRAD_NO[" + tradNo + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_BIZ_TYPE[" + infoBizType + "] SESS_BIZ_TYPE[" + sessBizType + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_PRD_ID[" + infoPrdctId + "] SESS_PRD_ID[" + sessPrdctId + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_PRD_CD[" + infoPrdctCd + "] SESS_PRD_CD[" + sessPrdctCd + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_PRD_NM[" + infoPrdctNm + "] SESS_PRD_NM[" + sessPrdctNm + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_CNNCTN_WAY[" + infoCnnctnWay + "] SESS_CNNCTN_WAY[" + sessCnnctnWay + "] #@#@#@");
                    log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] Session COMPARE >> INFO_CLERK_NO[" + infoClertNo + "] SESS_CLERK_NO[" + sessClerkNo + "] #@#@#@");

                    sessionManager.setGlobalValue("BIZ_TYPE", infoBizType);
                    sessionManager.setGlobalValue("TRAD_NO", infoTradNo);
                    sessionManager.setGlobalValue("PRDCT_ID", infoPrdctId);
                    sessionManager.setGlobalValue("PRDCT_CD", infoPrdctCd);
                    sessionManager.setGlobalValue("PRDCT_NM", infoPrdctNm);
                    // 제휴코드 및 소개자행번은 세션값 유지 필요하므로, 진행상태 테이블에 값이 없는 경우는 세션값으로 유지
                    sessionManager.setGlobalValue("CNNCTN_WAY", StringUtils.defaultIfEmpty(infoCnnctnWay, sessCnnctnWay));
                    sessionManager.setGlobalValue("CLERK_NO", StringUtils.defaultIfEmpty(infoClertNo, sessClerkNo));
	
                    // 이어하기 데이터 비교까지 진행하려고 했으나, 이어하기 데이터에 고정된 키(BIZ_TYPE, PRDCT_ID) 값으로 세팅되고 있는지 여부가
                    // 확실치 않아 일단 로그만 남기는걸로 변경
                    // 추후 이상없다고 판단될 시 이전 버전(46e9604)으로 재적용 필요. 재적용 시 업무로직상에서 이어하기데이터에 반드시 고정된
                    // 키(BIZ_TYPE, PRDCT_ID)에 해당하는 값을 세팅해야하는 문제가 존재함.
                    try {
                        // JSONObject screenInfoJson = new ObjectMapper().readValue(screenInfo,
                        // JSONObject.class);

                        Map<String, Object> screenInfoJson = BankingBizUtils.toJSONFromStr(screenInfo, HashMap.class);

                        if (screenInfoJson != null) {
                            String screenTradNo = (screenInfoJson.get("TRAD_NO") != null ? String.valueOf(screenInfoJson.get("TRAD_NO")) : "").trim();
                            String screenBizType = (screenInfoJson.get("BIZ_TYPE") != null ? String.valueOf(screenInfoJson.get("BIZ_TYPE")) : "").trim();
                            String screenPrdctId = (screenInfoJson.get("PRDCT_ID") != null ? String.valueOf(screenInfoJson.get("PRDCT_ID")) : "").trim();
                            String screenPrdctCd = (screenInfoJson.get("PRDCT_CD") != null ? String.valueOf(screenInfoJson.get("PRDCT_CD")) : "").trim();
                            String screenPrdctNm = (screenInfoJson.get("PRDCT_NM") != null ? String.valueOf(screenInfoJson.get("PRDCT_NM")) : "").trim();

                            /*
                             * 드림론인 경우, 기존 셀프뱅크 로직을 그대로 옮겼기 때문에 상품 분기되기 전(업체조회 이전)까지는 상품관련
                             * 정보를 3840/PRDLON0003840/신용대출로 Insert하므로, 아래 경우에는 이어하기 데이터를
                             * 무시하고 하드코딩 비교처리함.
                             * 기존 셀프뱅크때와 다르게, 3.0오면서 드림론 미리보기 화면이 추가되어, 처음부터 상품정보에 대해 명확히 알
                             * 수 있기때문에 하드코딩 적재하는 의미가 없어졌다고 생각되어 해당부분 개선을 고려하였으나,
                             * 미리보기 화면을 거치지 않고 다른 케이스로 진입하는 경우가 있는지 확실하지 않아 영향도 분석이 확실히 되기
                             * 전까지는 어쩔수 없이 하드코딩으로 비교하도록 예외적으로 처리함.
                             * - PLPA : 개인신용정보동의서
                             * - PLSS : 스크래핑완료
                             */
                            if (infoBizType.equals("CRPL") && infoCallCenterStatus.matches("PLPA|PLSS")) {
                                screenPrdctId = "3840";
                                screenPrdctCd = "PRDLON0003840";
                                screenPrdctNm = "신용대출";
                                log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] " + screenBizType + "/" + screenPrdctId + "/" + screenPrdctCd + "/" + screenPrdctNm + "로 비교처리!! #@#@#@");
                            }

                            log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] ScreenInfo COMPARE >> INFO_TRAD_NO[" + tradNo + "] SCRN_TRAD_NO[" + screenTradNo  + "] #@#@#@");
                            log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] ScreenInfo COMPARE >> INFO_BIZ_TYPE[" + infoBizType + "] SCRN_BIZ_TYPE[" + screenBizType + "] #@#@#@");
                            log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] ScreenInfo COMPARE >> INFO_PRD_ID[" + infoPrdctId + "] SCRN_PRD_ID[" + screenPrdctId + "] #@#@#@");
                            log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] ScreenInfo COMPARE >> INFO_PRD_CD[" + infoPrdctCd + "] SCRN_PRD_CD[" + screenPrdctCd + "] #@#@#@");
                            log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] ScreenInfo COMPARE >> INFO_PRD_NM[" + infoPrdctNm + "] SCRN_PRD_NM[" + screenPrdctNm + "] #@#@#@");

                            if (StringUtils.isNotEmpty(infoBizType)
                                            && StringUtils.isNotEmpty(infoPrdctId)
                                            && infoBizType.equals(screenBizType)
                                            && infoPrdctId.equals(screenPrdctId)) {
                                // isAvailable = true;
                                log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] COMPARE TRADINFO WITH SCRN_INFO IS EQUAL #@#@#@");
                            } else {
                                // isAvailable = false;
                                log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] COMPARE TRADINFO WITH SCRN_INFO IS NOT EQUAL #@#@#@");
                            }
                        } else {
                            // isAvailable = false;
                            log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] screenInfo IS NULL #@#@#@");
                        }
                    } catch (Exception e) {
                        // isAvailable = false;
                        log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] toJSONObject Exception -> ", e);
                    }
                } else {
                	log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] NF_TRADINFO_MGT IS NULL #@#@#@");
                }
	            ScreenAndScrapingInfoResponse.TradeInfoResponse tradeInfo = tradeInfoMapper.toListScrapingInfoResponseDto(resultNF_TRADINFO_MGT_S_05);
	            response.setTradeInfo(tradeInfo);
	            
	        }
		} else {
			log.debug("SCRAPING INFO " + custNo + "[" + tradNo + "] NF_SCRN_INFO_MGT IS NULL #@#@#@");
		}
		
		if (!isAvailable) {
	        PRCServiceException appException = new PRCServiceException("9999");
	        appException.setErrorGuideMessage("데이터가 유효하지 않습니다. 취소 후 다시 시도 부탁드립니다.");
	        // TODO: 금상몰메인 페이지로 이동 체크
	        appException.setNextPage("MA3PRDALL001");
	        throw appException;
		}
		
		log.debug("🏃‍➡️🏃‍➡️🏃‍➡️ {} 🏃🏃🏃", response);
		log.debug("####################### 비대면 화면정보 및 스크래핑 정보 조회 [MA3CMMBIZ015_102S] END ###\n", response);
		
		return response;
	}
	
	private CheckOngoingTradeInfoInquiryResponse chkProcessState(List<CheckOngoingTradeInfoInquiryResult> tradInfoList, String BIZ_TYPE, String INTEGRATED_CONSELING_YN) {

		CheckOngoingTradeInfoInquiryResponse output = new CheckOngoingTradeInfoInquiryResponse();
		
        log.debug("####################### chkProcessState START #######################");
        List<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse> originalArrList = new ArrayList<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse>();
        List<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse> pageMoveArrList = new ArrayList<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse>();
        List<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse> cancelPopupArrList = new ArrayList<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse>();
        List<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse> cancelArrList = new ArrayList<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse>();
        List<ICodeItemInfo> callCntrStsCdItr = CodeUtils.getCodes("CALL_CNTR_STS_CD");
        
        String PRDCT_ID = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PRDCT_ID", String.class), 
        		StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PRDCT_ID", String.class), "")); // 거래 후 현재상품과 동일상품인지 비교 시 사용
        
        
        // 모바일전세론 신청/연기 심사단계에서 재신청시 사용
        List<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse> screeningPopupArrList = new ArrayList<CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse>();

        Optional.ofNullable(tradInfoList).ifPresent(list -> list.forEach(tradInfo -> {
            String infoBizType = String.valueOf(tradInfo.getBizType()).trim();
            String infoPrdctId = String.valueOf(tradInfo.getPrdctId()).trim();
            String infoCallCntrStsCd = String.valueOf(tradInfo.getCallCntrStsCd()).trim();
            String infoIntegratedConseling = String.valueOf(
                    tradInfo.getIntegratedConselingYn() != null ? tradInfo.getIntegratedConselingYn() : "")
                    .trim();
            String oplCnsltYn = String.valueOf(tradInfo.getOplCnsltYn() != null ? tradInfo.getOplCnsltYn() : "")
                    .trim(); // OPL여부 추가

            // 대출취소시점 2020Toss 일경우 CNNCTN_WAY = '000120' 대출취소 전문 실행
            String cnnctnWay = String.valueOf(tradInfo.getCnnctnWay()).trim();
            String tossCnnctnWay = PropertiesUtils.getString("toss.cnnctn_way"); // 토스 cnnctn_way
            String isCNNCTNWAY = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("IS_CNNCTN_WAY", String.class), "");

            CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse original = tradeInfoMapper.toCheckOngoingTradeInfoResponse(tradInfo);
            originalArrList.add(original);
            
            // MA30에서 진행하면서 toss cnnCtnWay 가 들어왔을 경우 && 토스에서 진행하면서 토스 cnnCtnWay가 아닐경우
            if ((cnnctnWay.equals(tossCnnctnWay) && isCNNCTNWAY.equals(""))
                    || (!cnnctnWay.equals(tossCnnctnWay) && isCNNCTNWAY.equals("Y"))) {
                String tradNo = String.valueOf(tradInfo.getTradNo()).trim();

                OngoingTradeInfoCancelParameter updateUiStatusUpdateRequest = new OngoingTradeInfoCancelParameter();
                updateUiStatusUpdateRequest.setTradNo(tradNo);
                nfTradeInfoMgtDao.updateOngoingProductCancellation(updateUiStatusUpdateRequest);
                
                cancelArrList.add(original);
                return;
            }

            // 드림론이벤트 진행 중 타 상품으로 들어왔을 경우 && 드림론이벤트 진행하면서 드림론이벤트로 진행 할 경우
            if (infoBizType.equals("DLEV")) {
                String tradNo = String.valueOf(tradInfo.getTradNo()).trim();

                OngoingTradeInfoCancelParameter updateUiStatusUpdateRequest = new OngoingTradeInfoCancelParameter();
                updateUiStatusUpdateRequest.setTradNo(tradNo);
                nfTradeInfoMgtDao.updateOngoingProductCancellation(updateUiStatusUpdateRequest);

                cancelArrList.add(original);
                return;
            }

            // 2020.05.15
            // 1. 모바일전세론 신청/연기 심사단계일때 모바일전세론 신청/연기 진행시 심사중이라는 Alert 안내 내역 추가
            // 2. 모바일전세론 신청/연기 심사단계일때는 "PRGS" 상태라도 자동취소 안되도록 처리 추가
            if (BIZ_TYPE.matches("CMJG|CMJL") && infoCallCntrStsCd.matches(
                    "JGLP|JGPH|JLLP|JLPH|JGFW|JGGH|JGBC|JGUA|JGHC|JGHN|JGDA|JGOG|JGBD|JGWB|JGSG|JGDB|JGCB|JGEA|JGCP|JGDG|JGSA|JGSC|JGSD|JGLD")) {
                screeningPopupArrList.add(original);
                return;
            } else if (infoBizType.matches("CMJG|CMJL") && infoCallCntrStsCd.matches("JGLP|JLLP|JGUA|JGHC|JGWB")) {
                screeningPopupArrList.add(original);
                return;
            } else if (infoBizType.matches("CMJG|CMJL") && infoCallCntrStsCd.matches(
                    "JGPH|JLPH|JGFW|JGGH|JGBC|JGHN|JGDA|JGOG|JGBD|JGSG|JGDB|JGCB|JGEA|JGCP|JGDG|JGSA|JGSC|JGSD|JGLD")) {
                return;
            }

            // 2024.07.01 주담대 추가
            if (BIZ_TYPE.matches("MHLS|FHLS|MPPS|TBGL")
                    && infoCallCntrStsCd.matches("HLFN|HLPT|HLPR|HLPS|HLPC|HLHC|HLHR|HLUR|HLUS|HLUC|HLER|HLES|HLEC")) {// 신청완료~실행이전
                screeningPopupArrList.add(original);
                return;
            } else if (infoBizType.matches("FHLS") && infoCallCntrStsCd.matches("HLFN|HLPR|HLUR|HLER")) {
                // HLFN(신청 완료), HLPR(서류재촬영 요청), HLUR(보완서류요청), HLER(승인서류 요청)
                screeningPopupArrList.add(original);
                return;
            } else if (infoBizType.matches("FHLS")
                    && infoCallCntrStsCd.matches("HLPT|HLPS|HLPC|HLHC|HLHR|HLUS|HLUC|HLES|HLEC")) {
                // HLFN(신청 완료), HLPR(서류재촬영 요청), HLUR(보완서류요청), HLER(승인서류 요청) 외
                return;
            }

            String targetUrl = "";
            boolean isShowCancelPopup = false;
            String nextURLCheckCode = "";

            if (isCNNCTNWAY.equals("Y") && !"".equals(cnnctnWay)) { // 20200525 토스일경우 이어하기 처리
                nextURLCheckCode = infoBizType + "_" + infoCallCntrStsCd + "_" + cnnctnWay;
            } else if (StringUtils.isNotEmpty(infoIntegratedConseling) && !infoBizType.matches("FHLS|MPPS|TBGL")) {
                // 통합상담여부(통합상담:Y, 개별상품:N, AS-IS:""), 주담대 개별상품이 아닌경우
                // SC제일 신용대출일 경우
                if ("Y".equals(oplCnsltYn)) {
                    nextURLCheckCode = "MLOP_" + infoBizType + "_" + infoCallCntrStsCd;
                } else {
                    nextURLCheckCode = "MLIC_" + infoBizType + "_" + infoCallCntrStsCd;
                }
            } else {
                nextURLCheckCode = infoBizType + "_" + infoCallCntrStsCd;
            }

            // 해당 업무별 진행상태에 맞는 화면 URL 세팅
            for (int i = 0; i < callCntrStsCdItr.size(); i++) {
                ICodeItemInfo item = callCntrStsCdItr.get(i);

                if (item.getKey().equals(nextURLCheckCode)) {
                    targetUrl = item.getValue();
                    if (infoBizType.matches("CASA|FCSA|TRLR|BBCA|MOLR|MPLR|MHLR|SCSK|SCSF")) {
                        // 입출금, 이체한도증액,모바일제신고, BIB 2021-08-27 SC증권(SCSK-원화,SCSF-외화)
                        isShowCancelPopup = true;
                    } else if (infoCallCntrStsCd.equals("PLDT") && infoBizType.equals("CRPL")) {
                        // 상품상세는 돌려드림론(CRPL)인 경우만 true
                        isShowCancelPopup = true;
                    } else if (infoBizType
                            .matches("SPPL|CRPL|SBMA|MAPL|NSPL|CMJG|CMJL|MHLS|FHLS|MPPS|TBGL|DRPL|OPPL")) {
                        // 간편대출,신용대출,모기지론(담보)
                        // 20240229 신용대출 연기갱신 추가(DRPL)
                        isShowCancelPopup = true;
                    }
                    break;
                }
            }

            if (StringUtils.isNotEmpty(targetUrl) && (targetUrl.indexOf("screenFlag=CO") == -1)) {
                targetUrl += targetUrl.indexOf("?") > -1 ? "&screenFlag=CO" : "?screenFlag=CO";
            }

            original.setNextURL(targetUrl);
            tradInfo.setNextURL(targetUrl);

            // 입출금 , BIB 2021-08-27 SC증권(SCSK-원화, SCSF-외화) 추가
            if (BIZ_TYPE.matches("CASA|FCSA|TRLR|BBCA|MOLR|MPLR|MHLR|SCSK|SCSF")) {
                if (infoBizType.matches("CASA|FCSA|TRLR|BBCA|MOLR|MPLR|MHLR|SCSK|SCSF")) {
                    // 입출금 상품의 신분증 촬영 완료 건이 있는 경우 체크
                    // isShowCancelPopup = isCancelCasa(infoCallCntrStsCd);

                    // 동일상품일 경우 영상통화 완료건에 대해서는 이동, 나머지는 취소처리
                    if (PRDCT_ID.equals(infoPrdctId) && BIZ_TYPE.equals(infoBizType)) {
                        if (isShowCancelPopup) {
                            pageMoveArrList.add(original);
                        } else {
                            cancelArrList.add(original);
                        }
                        return;
                    }
                } else if (!BIZ_TYPE.equals("FCSA")
                        && infoBizType.matches("SPPL|CRPL|SBMA|MAPL|NSPL|CMJG|CMJL|MHLS|FHLS|MPPS|TBGL|DRPL|OPPL")) {
                    // 간편대출, 신용대출, 모기지론(담보), 전세보증론
                    // 20240229 신용대출 연기갱신 추가(DRPL)
                    return;
                }
            } else if (BIZ_TYPE.matches("SPPL|CRPL|SBMA|MAPL|NSPL|CMJG|CMJL|MHLS|FHLS|MPPS|TBGL|DRPL|OPPL")) {
                // 간편대출,신용대출,모기지론(담보), 전세보증론, 20240229 신용대출 연기갱신 추가(DRPL)
                if (infoBizType.matches("CASA|FCSA|BBCA|SCSK|SCSF")) {
                    // 입출금 상품의 신분증 촬영 완료 건이 있는 경우 체크
                    // isShowCancelPopup = isCancelCasa(infoCallCntrStsCd);
                } else if (infoBizType.matches("SPPL|CRPL|SBMA|MAPL|NSPL|CMJG|CMJL|MHLS|FHLS|MPPS|TBGL|DRPL|OPPL")) {
                    // 2024.08.13 주담대는 계속이어하기 되도록 INTEGRATED_CONSELING_YN, infoIntegratedConseling 를
                    // Y로 세팅
                    String mortgageProductReg = "MHLS|FHLS|MPPS|TBGL"; // 주택담보대출 통합상담 상품 체크
                    String integratedConselingYn = "N";
                    if (BIZ_TYPE.matches(mortgageProductReg) && infoBizType.matches(mortgageProductReg)) {
                        integratedConselingYn = "Y";
                        infoIntegratedConseling = "Y";
                    }
                    // isShowCancelPopup = isCancelLoan(infoCallCntrStsCd, infoBizType);

                    // 동일상품일 경우 이동, 나머지는 취소처리 (동일상품 체크 조건에 통합상담여부 값 추가)
                    if (StringUtils.isEmpty(infoIntegratedConseling)) {
                        // 통합상담 아닌 상품(기존 AS-IS)
                        if ((PRDCT_ID.equals(infoPrdctId) && BIZ_TYPE.equals(infoBizType))
                                || (BIZ_TYPE.equals("CRPL") && BIZ_TYPE.equals(infoBizType))) {
                            if (isShowCancelPopup) {
                                pageMoveArrList.add(original);
                            } else {
                                cancelArrList.add(original);
                            }
                            return;
                        }
                    } else if (integratedConselingYn.equals(infoIntegratedConseling)) {
                        // 통합상담인 경우는 통합상담 여부만 가지고 이어하기 가능여부 판단
                        // 통합상담 진행 > 상품선택 > 이탈 > 통합상담 진행 > 이전상품/현재상품 선택이 아닌 진행 건 이어하도록 처리하기 위함
                        // ---- 주택담보대출 통합상담 추가 ----
                        // 통합상담이 신용만 존재해서 별다른 분기가 필요없었으나, 주택담보대출 통합상담이 추가되어 동일 상품군(신용/담보)인지 체크가 필요해서 추가
                        // 신용대출 통합상담 : CRPL|SPPL|MAPL|NSPL (드림론, 직장인중금리, 모바일소액, 새희망홀씨)
                        // 주택담보대출 통합상담 : MHLS|FHLS|MPPS|TBGL (주택담보대출, 퍼스트홈론, 순수장기고정금리대출, 보금자리론)
                        String mortgageProductRegEx = "MHLS|FHLS|MPPS|TBGL"; // 주택담보대출 통합상담 상품 체크
                        boolean isEqualCategory = (BIZ_TYPE.matches(mortgageProductRegEx)
                                && infoBizType.matches(mortgageProductRegEx))
                                || (BIZ_TYPE.matches("^" + mortgageProductRegEx)
                                        && infoBizType.matches("^" + mortgageProductRegEx));
                        if ("Y".equals(infoIntegratedConseling) && isEqualCategory) {
                            // 통합상담이고, 현재 진행하는 상품군과 이전에 진행하던 상품군이 동일한 경우
                            if (isShowCancelPopup) {
                                pageMoveArrList.add(original);
                            } else {
                                cancelArrList.add(original);
                            }
                            return;
                        } else if (PRDCT_ID.equals(infoPrdctId) && BIZ_TYPE.equals(infoBizType)) {
                            // 개별상품인 경우는 상품정보까지 비교
                            if (isShowCancelPopup) {
                                pageMoveArrList.add(original);
                            } else {
                                cancelArrList.add(original);
                            }
                            return;
                        }
                    }
                }
            }

            if (isShowCancelPopup) {
                cancelPopupArrList.add(original);
            } else {
                cancelArrList.add(original);
            }
        }));

        // 취소처리 진행
//        Optional.ofNullable(cancelArrList).ifPresent(list -> list.forEach(tradInfo -> {
//        	OngoingTradeInfoCancelParameter input = new OngoingTradeInfoCancelParameter();
//            input.setTradNo(String.valueOf(tradInfo.getTradNo()));
//            nfTradeInfoMgtDao.updateOngoingProductCancellation(input);
//        }));
        if(cancelArrList != null && !cancelArrList.isEmpty()) {
        	for(CheckOngoingTradeInfoInquiryResponse.OngoingTradeInfoResponse tradInfo : cancelArrList) {
        		if(tradInfo == null) continue;
    			OngoingTradeInfoCancelParameter input = new OngoingTradeInfoCancelParameter();
    			input.setTradNo(String.valueOf(tradInfo.getTradNo()));
    			nfTradeInfoMgtDao.updateOngoingProductCancellation(input);
        		
            };
        }

        output.setOriginal(originalArrList);
        output.setPageMoveArr(pageMoveArrList);
        output.setCancelPopupArr(cancelPopupArrList);
        output.setScreeningPopupArr(screeningPopupArrList);
        log.debug("cancelArrList : {}", cancelArrList);
        log.debug("{}", output);
        log.debug("####################### chkProcessState END #######################");
        return output;
    }
	
	/**
	 * <pre>
	 * 비대면 화면정보 조회
	 * </pre>
	 * @param 
	 * @return 
	 * @throws 
	 * @description MA3CMMBIZ015_102S
	 */
	@ComponentOperation(name = "비대면 화면정보 조회 [MA3CMMBIZ015_102S]")
	public NfScrnInfoInqiryResponse getNfScrnInfo(NfScrnInfoInqiryRequest request) {
		NfScrnInfoInqiryResponse response = new NfScrnInfoInqiryResponse();
		
		String custNo = StringUtils.defaultString(request.getCustNo());
		String tradNo = StringUtils.defaultString(request.getTradNo());
		String bizType = StringUtils.defaultString(request.getBizType());
		
		NfScrnInfoInqiryParam param = new NfScrnInfoInqiryParam();
		
		param.setCustNo(custNo);
		param.setTradNo(tradNo);
		param.setBizType(bizType);
		
		NfScrnInfoInqiryReult result = nfScrnInfoMgtDao.selectNfScrnInfo(param);
		
		log.debug("비대면 화면정보 조회 결과 => {}", result);
		
		if(result != null) {
			//직장인중금리(SPPL), 모바일소액(MAPL), 새희망홀씨(NSPL)인 경우에만 세션정보 업데이트 및 이어하기 데이터 비교 진행하도록 수정
			if(bizType.matches("SPPL|MAPL|NSPL|CRPL|OPPL")) {
				TradeInfoInquiryParameter tradInfoReq = new TradeInfoInquiryParameter();
				
				tradInfoReq.setCustNo(custNo);
				tradInfoReq.setTradNo(tradNo);

            	TradeInfoInquiryResult tradInfoRes = nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(tradInfoReq);
            	
            	if(tradInfoRes != null) {
            		String infoTradNo = StringUtils.defaultString(tradInfoRes.getTradNo()).trim();
            		String infoBizType = StringUtils.defaultString(tradInfoRes.getBizType()).trim();
            		String infoPrdctId = StringUtils.defaultString(tradInfoRes.getPrdctId()).trim();
            		String infoPrdctCd = StringUtils.defaultString(tradInfoRes.getPrdctCd()).trim();
            		String infoPrdctNm = StringUtils.defaultString(tradInfoRes.getPrdctNm()).trim();
            		String infoCnnctnWay = StringUtils.defaultString(tradInfoRes.getCnnctnWay()).trim();
            		String infoClerkNo = StringUtils.defaultString(tradInfoRes.getClerkNo()).trim();
            		
            		sessionManager.setGlobalValue("BIZ_TYPE", infoBizType);
            		sessionManager.setGlobalValue("TRAD_NO", infoTradNo);
            		sessionManager.setGlobalValue("PRDCT_ID", infoPrdctId);
            		sessionManager.setGlobalValue("PRDCT_CD", infoPrdctCd);
            		sessionManager.setGlobalValue("PRDCT_NM", infoPrdctNm);
            		sessionManager.setGlobalValue("CNNCTN_WAY", infoCnnctnWay);
            		sessionManager.setGlobalValue("CLERK_NO", infoClerkNo);
            	}
			}
			
			response.setCustNo(result.getCustNo());
			response.setBizType(result.getBizType());
			response.setTradNo(result.getTradNo());
			response.setScrnDataInfo(result.getScrnDataInfo());
			response.setScrppngData(result.getScrppngData());
		}
		
		return response;
	}
	
	/**
	 * <pre>
	 * 해외거래내역 DB 저장
	 * </pre>
	 * @param 
	 * @return 
	 * @throws 
	 * @description MA3CMMBIZ018_301S
	 */
	@ComponentOperation(name = "해외거래내역 저장 [MA3CMMBIZ018_301S]")
	public RegistIntlTradInfoResponse registIntlTradInfo(RegistIntlTradInfoRequest request) {
		RegistIntlTradInfoResponse result = new RegistIntlTradInfoResponse();
		
		String custNo = StringUtils.defaultIfEmpty(request.getCustNo(), SessionUtils.getSessionValue("CUST_NO"));
		String tradNo = StringUtils.defaultIfEmpty(request.getTradNo(), SessionUtils.getSessionValue("TRAD_NO"));
		String codeType = StringUtils.defaultIfEmpty(request.getCodeType(), "1");
		
		TradInfoBizCdMgtParam deleteParam = new TradInfoBizCdMgtParam();
		
		deleteParam.setCustNo(custNo);
		deleteParam.setTradNo(tradNo);
		deleteParam.setCodeType(codeType);
		
		nfTradinfoBizcdMgtDao.deleteTradInfoBizCd(deleteParam);
		
		TradInfoBizCdMgtParam insertParam = new TradInfoBizCdMgtParam();
		
		insertParam.setCustNo(custNo);
		insertParam.setTradNo(tradNo);
		insertParam.setCodeType(codeType);
		insertParam.setGrpCodeName(StringUtils.defaultString(request.getGrpCodeName()));
		insertParam.setGrpCode(StringUtils.defaultString(request.getGrpCode()));
		
		String gaeSaCodeStr = StringUtils.defaultString(request.getGaesaCode());
		
		//TODO 확인필요
		if (StringUtils.isNotEmpty(gaeSaCodeStr)) {
			List<HashMap<String, Object>> gaeSaCodeList = BankingBizUtils.toJSONFromStr(gaeSaCodeStr, List.class);
			
			TradInfoBizCdMgtParam.gaesaCode gaesaCodeRec;
			List<TradInfoBizCdMgtParam.gaesaCode> gaesaCodeRecList = new ArrayList<TradInfoBizCdMgtParam.gaesaCode>();
			
			if (gaeSaCodeList != null && gaeSaCodeList.size() > 0) {
				int idx = 0;
				
				while (idx < gaeSaCodeList.size()) {
					gaesaCodeRec = new TradInfoBizCdMgtParam.gaesaCode();
					HashMap<String, Object> haesaCodeData = gaeSaCodeList.get(idx);
					String code = StringUtils.defaultIfEmpty((String)haesaCodeData.get("CODE"), (String)haesaCodeData.get("code"));
					String codeNm = StringUtils.defaultIfEmpty((String)haesaCodeData.get("CODE_NM"), (String)haesaCodeData.get("codeNm"));
					String cnt = StringUtils.defaultIfEmpty((String)haesaCodeData.get("CNT"), (String)haesaCodeData.get("cnt"));
					
					gaesaCodeRec.setCode(code);
					gaesaCodeRec.setCodeNm(codeNm);
					if(cnt != null) {
						gaesaCodeRec.setCnt(cnt);
					}
					gaesaCodeRecList.add(gaesaCodeRec);
					
					idx++;
				}
			}
			
			insertParam.setGaesaCodeList(gaesaCodeRecList);
			
		}
		
		Integer insertResult = nfTradinfoBizcdMgtDao.insertTradInfoBizCd(insertParam);
		
		result.setRegistResult( insertResult != null ? insertResult.intValue() : 0 );
		
		return result;
	}
	
	/**
	 * HTML Unescape 처리
	 * 
	 * @param input String
	 * @return
	 */
	private String unescape(String input) {
		return input.replace("&amp", "&")
				.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&quot;", "\"")
				.replace("&#39;","'");
	}
	
	private String getCustNo() {
		String custNo = "";
		
		String perBusNo = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class),
				StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PerBusNo", String.class), ""));

		NonFaceCustomerInfoParameter dbParam = new NonFaceCustomerInfoParameter();
		dbParam.setSsn(perBusNo);
		// 비대면 고객정보 조회 (asis : NF_CUST_MGT_S_01)
		NonFaceCustomerInfoInquiryResult dbResponse = nfCustMgtDao.selectNonFaceCustomerInfo(dbParam);
		if (dbResponse != null) {
			custNo = String.valueOf(dbResponse.getCustNo() != null ? dbResponse.getCustNo() : "");
			sessionManager.setGlobalValue("CUST_NO", custNo);
		}
		
		return custNo;
	}
}
