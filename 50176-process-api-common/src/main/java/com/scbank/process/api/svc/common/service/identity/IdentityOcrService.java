package com.scbank.process.api.svc.common.service.identity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H27100Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H27100Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H96300Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H96300Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.IdentityOcrComponent;
import com.scbank.process.api.svc.common.components.dto.GetCheckAvailableRequest;
import com.scbank.process.api.svc.common.components.dto.GetCheckAvailableResponse;
import com.scbank.process.api.svc.common.constant.IdentityConstants;
import com.scbank.process.api.svc.common.dao.FaceRecgTblDao;
import com.scbank.process.api.svc.common.dao.NfRemitMgtDao;
import com.scbank.process.api.svc.common.dao.NfTradInfoMgtDao;
import com.scbank.process.api.svc.common.dao.NonFaceTblDao;
import com.scbank.process.api.svc.common.dao.TblBranchDao;
import com.scbank.process.api.svc.common.dao.dto.AcctAuthNumberParameter;
import com.scbank.process.api.svc.common.dao.dto.AcctAuthNumberResult;
import com.scbank.process.api.svc.common.dao.dto.BranchNameResult;
import com.scbank.process.api.svc.common.dao.dto.CnnctnTradInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.CntrtyRemitReqCntParameter;
import com.scbank.process.api.svc.common.dao.dto.FaceRecgParameter;
import com.scbank.process.api.svc.common.dao.dto.IdCardShotInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.IdCardShotInfoResult;
import com.scbank.process.api.svc.common.dao.dto.IdVerificationCountParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthInfoResult;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthTradInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceTblParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceTradInfoResult;
import com.scbank.process.api.svc.common.dao.dto.RemitCntParameter;
import com.scbank.process.api.svc.common.dao.dto.RemitInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.RemitInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectNonFaceInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectNonFaceInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SsaTruthInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdateNfInfoParameter;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrCancelFaceRecgResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrCheckDepositorRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrCheckDepositorResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrExtractUniquePointsRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrExtractUniquePointsResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetAcctAuthAbleYnResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetFaceVerificationInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetIdCardShotAblListRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetIdCardShotAblListResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetIdCardShotInfoRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetIdCardShotInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetNonFaceAuthCompleteYnRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetNonFaceAuthCompleteYnResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetNonFaceInfoRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetNonFaceInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetNonFaceSeqNoRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrGetNonFaceSeqNoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrNonFaceAuthInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrNonFaceImageDataProcessRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrRequestFaceRecgApiRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrRequestFaceVerificationRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrRequestFaceVerificationResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrSendAcctAuthRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrSendAcctAuthResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrSendIdCardImageRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrSendIdCardImageResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrSetAuthInitInfoRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrSetAuthInitInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrUpdateNonFaceIdCardStatusRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrUpdateSSATruthInfoRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrUpdateSSATruthInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrValidateAcctAuthNumberRequest;
import com.scbank.process.api.svc.common.service.identity.dto.ocr.IdtOcrValidateAcctAuthNumberResponse;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo.yoMyInfRecList;
import com.scbank.process.api.svc.shared.components.alchera.AlcAES256;
import com.scbank.process.api.svc.shared.components.auth.AuthVerifyComponent;
import com.scbank.process.api.svc.shared.components.auth.dto.NonFaceAuthCompleteYnRequest;
import com.scbank.process.api.svc.shared.components.auth.dto.NonFaceAuthCompleteYnResponse;
import com.scbank.process.api.svc.shared.components.customer.CustomerInfoComponent;
import com.scbank.process.api.svc.shared.components.customer.dto.CustCddInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.CustCddInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.customer.dto.NonFaceCustomerInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.customer.dto.NonFaceCustomerInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.edoc.EdocXvarmComponent;
import com.scbank.process.api.svc.shared.components.frs.kftc.interceptor.KftcFrsLogInterceptor;
import com.scbank.process.api.svc.shared.components.frs.kftc.service.KftcFrsApiClient;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntityBuilder;
import com.scbank.process.api.svc.shared.components.frs.util.KftcFrsHelper;
import com.scbank.process.api.svc.shared.components.tradinfo.TradInfoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoDataRequest;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.RandomKeyUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;
import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/identity/ocr", name = "실명확인 - 신분증촬영")
public class IdentityOcrService {
	
	/**
     * EDMI 통합 클라이언트
     */
    private final HostClient hostClient;
    
    /**
     * 안면인식 API 클라이언트
     */
    private final KftcFrsApiClient kftcFrsApiClient;
	
	private final ISessionContextManager sessionManager;
	
	private final NonFaceTblDao nonFaceTblDao;
	private final TblBranchDao tblBranchDao;
	private final NfTradInfoMgtDao nfTradInfoMgtDao;
	private final NfRemitMgtDao nfRemitMgtDao;
	private final FaceRecgTblDao faceRecgTblDao;
	
	private final AccountListComponent accountListComponent; 
	private final IdentityOcrComponent identityOcrComponent;
	private final EdocXvarmComponent edocXvarmComponent;
	private final CustomerInfoComponent customerInfoComponent;
	private final AuthVerifyComponent authVerifyComponent;
	private final TradInfoComponent tradInfoComponent;
	
	@ServiceEndpoint(url = "/getNonFaceSeqNo", name = "비대면일련번호 조회 [MA3CMMBIZ012_102S]")
    public IdtOcrGetNonFaceSeqNoResponse getNonFaceSeqNo(IServiceContext serviceContext,
    		IdtOcrGetNonFaceSeqNoRequest request) {
		log.debug("비대면일련번호 조회 request => {}",request);
		
		IdtOcrGetNonFaceSeqNoResponse response = new IdtOcrGetNonFaceSeqNoResponse();
		
		if ( "R".equals(request.getShootingType()) ) {
			//고객정보 조회
			IdCardShotInfoParameter idCardShotInfoParam = new IdCardShotInfoParameter();
			
			idCardShotInfoParam.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
			idCardShotInfoParam.setIdCardCd("R");
			idCardShotInfoParam.setTradRegGb( "Y".equals(SessionUtils.getSessionValue("isCSL"))?"1":"0" );
			
			List<IdCardShotInfoResult> idCardShotInfoList = nfTradInfoMgtDao.selectIdCardShotInfo(idCardShotInfoParam);
			
			if (idCardShotInfoList != null) {
				ArrayList<String> seqNoArr = new ArrayList<String>();
				ArrayList<String> tradNoArr = new ArrayList<String>();
				
				for (IdCardShotInfoResult result : idCardShotInfoList) {
					String seqNo = result.getSeqNo();
					String tradNo = result.getTradNo();
					
					if(StringUtils.isNotEmpty(seqNo) && StringUtils.isNotEmpty(tradNo)) {
						seqNoArr.add(seqNo);
						tradNoArr.add(tradNo);
					}
				}
				
				response.setSeqNoList(seqNoArr);
				response.setTradNoList(tradNoArr);
				response.setCount(idCardShotInfoList.size());
			}
			
			response.setShootingType("R");
		} else {
			String reqBrCd = request.getReqBrCd();
			String reqBrNm = request.getReqBrNm();
			
			if ( StringUtils.isEmpty(reqBrCd) || StringUtils.isEmpty(reqBrNm) ) {
				
				String tsPassword = SessionUtils.getSessionValue("TSPassword");
				String userId = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("UserID"),
						sessionManager.getGlobalValue("uid", String.class)); // 공인인증서 본인인증인 경우, 사용자아이디 가져오기 위한 처리
				String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"),
						sessionManager.getGlobalValue("cid", String.class)); // 공인인증서 본인인증인 경우, 주민번호 가져오기 위한 처리
				
				CbIbk01H86600Req sendData = new CbIbk01H86600Req();
				
				sendData.setUserID(userId);
				sendData.setTSPassword(tsPassword);
				sendData.setYIJMNO(perBusNo);
				
				// 보유계좌 조회
				ListAccountHeldInfo accountHeldInfo = accountListComponent.getListAccountHeld(sendData);
				
				if (accountHeldInfo != null) {
					List<yoMyInfRecList> yomyinfRecList = accountHeldInfo.getYoMyInfRec();
					
					if (yomyinfRecList != null && yomyinfRecList.size() > 0) {
						reqBrCd = yomyinfRecList.get(yomyinfRecList.size()-1).getYoBsJum();
						//영업점명 조회
						List<String> brCdList = new ArrayList<String>();
						
						brCdList.add(reqBrCd);
						
						List<BranchNameResult> branchInfoList =  tblBranchDao.selectBranchName(brCdList);
						
						if (branchInfoList != null) {
							for(BranchNameResult branchInfo : branchInfoList) {
								if(reqBrCd.equals(branchInfo.getBranchCd().trim())) {
									reqBrNm = branchInfo.getBranchName();
									break;
								}
							}
						} else {
							reqBrCd = "100";
							reqBrNm = "영업부";
						}
					}
				}
			}
			
			request.setReqBrCd(reqBrCd);
			request.setReqBrNm(reqBrNm);
			
			//신분증 촬영정보 INSERT
			String nowDate = DateUtils.getCurrentDate(DateUtils.YYYYMMDD);
			String productName = request.getProductName();
			
			if(StringUtils.isNotEmpty(productName) && productName.length() > 7) {
				productName = productName.substring(0, 7);
			}
			
			// 비대면 인증 일련번호 조회
			String seqNo = nonFaceTblDao.selectNonFaceTblSeqNo(nowDate);
			
			NonFaceTblParameter insertNonFaceTblParam = new NonFaceTblParameter();
			
			insertNonFaceTblParam.setSeqNo(seqNo);
			insertNonFaceTblParam.setPhone(StringUtils.defaultString(SessionUtils.getSessionValue("HpNum")).replaceAll("\\D", ""));
			insertNonFaceTblParam.setPsno(SessionUtils.getSessionValue("PerBusNo"));
			insertNonFaceTblParam.setCusNm(SessionUtils.getSessionValue("CustName"));
			insertNonFaceTblParam.setApplyId(StringUtils.defaultString(nowDate));
			insertNonFaceTblParam.setProductCode(StringUtils.defaultString(request.getProductCode()));
			insertNonFaceTblParam.setProductName(productName);
			insertNonFaceTblParam.setAcctNo(StringUtils.defaultString(request.getAcctNo()));
			insertNonFaceTblParam.setBankName(StringUtils.defaultString(request.getBankName()));
			insertNonFaceTblParam.setAnotherAcctNo(StringUtils.defaultString(request.getAnotherAcctNo()));
			insertNonFaceTblParam.setAgtYn(StringUtils.defaultString(request.getAgtYn()));
			insertNonFaceTblParam.setJobTypeCd(StringUtils.defaultString(request.getJobTypeCd()));
			insertNonFaceTblParam.setAssetsFlag(StringUtils.defaultString(request.getAssetsFlag()));
			insertNonFaceTblParam.setDetbFlag(StringUtils.defaultString(request.getDetbFlag()));
			insertNonFaceTblParam.setDeposit(new BigDecimal(request.getDeposit()));
			insertNonFaceTblParam.setInvest(new BigDecimal(request.getInvest()));
			insertNonFaceTblParam.setProperty(new BigDecimal(request.getProperty()));
			insertNonFaceTblParam.setAssets(new BigDecimal(request.getAssets()));
			insertNonFaceTblParam.setMortgage(new BigDecimal(request.getMortgage()));
			insertNonFaceTblParam.setLoan(new BigDecimal(request.getLoan()));
			insertNonFaceTblParam.setDebt(new BigDecimal(request.getDebt()));
			insertNonFaceTblParam.setReqBrCd(request.getReqBrCd());
			insertNonFaceTblParam.setReqBrNm(request.getReqBrNm());
			insertNonFaceTblParam.setPassCode("1111");
			insertNonFaceTblParam.setBusinessDate("");
			
			log.debug("비대면정보 최초 등록 param => {}", insertNonFaceTblParam);
			
			// 비대면 인증 신규 등록
			nonFaceTblDao.insertNonFaceTbl(insertNonFaceTblParam);
			
			response.setSeqNo(seqNo);
		}
		
		log.debug("비대면일련번호 조회 response => {}",response);
		
		return response;
	}
	
	@ServiceEndpoint(url = "/getIdCardShotInfo", name = "신분증촬영 정보조회 [MA3CMMBIZ012_101S]")
    public IdtOcrGetIdCardShotInfoResponse getIdCardShotInfo(IServiceContext serviceContext,
    		IdtOcrGetIdCardShotInfoRequest request) {
		IdtOcrGetIdCardShotInfoResponse response = new IdtOcrGetIdCardShotInfoResponse();
		
		String screenFlag = request.getScreenFlag();
		
		if ( "Y".equals(request.getIsRAS()) || StringUtils.isNotEmpty(screenFlag) ) {
			sessionManager.setGlobalValue("CUST_NO", StringUtils.defaultString(request.getCustNo()));
			sessionManager.setGlobalValue("TRAD_NO", StringUtils.defaultString(request.getTradNo()));
			sessionManager.setGlobalValue("BIZ_TYPE", StringUtils.defaultString(request.getBizType()));
		}
		
		// 비대면 인증 관련 세션 데이터 삭제
		sessionManager.removeGlobalValue("idRecogResultYn");
		sessionManager.removeGlobalValue("idTruthResultYn");
		sessionManager.removeGlobalValue("faceResultYn");
		sessionManager.removeGlobalValue("videoResultYn");
		sessionManager.removeGlobalValue("nonFaceInputPerBusNo2");
		sessionManager.removeGlobalValue("nonFaceInputMidLicNo");
		
		String custNo = StringUtils.isNotEmpty(screenFlag)?request.getCustNo():sessionManager.getGlobalValue("CUST_NO", String.class);
		String tradNo = StringUtils.isNotEmpty(screenFlag)?request.getTradNo():sessionManager.getGlobalValue("TRAD_NO", String.class);
		String bizType = StringUtils.isNotEmpty(screenFlag)?request.getBizType():sessionManager.getGlobalValue("BIZ_TYPE", String.class);
		
		log.debug("신분증촬영 정보조회 custNo => {}",custNo);
		log.debug("신분증촬영 정보조회 tradNo => {}",tradNo);
		log.debug("신분증촬영 정보조회 bizType => {}",bizType);
		
		CustCddInfoInquiryRequest cddInfoRequest = new CustCddInfoInquiryRequest();
		
		// 고객정보/CDD 여부를 조회한다
		CustCddInfoInquiryResponse cddInfoResponse = new CustCddInfoInquiryResponse();
		cddInfoResponse = customerInfoComponent.getCustCddInfo(cddInfoRequest);
		
		String propertiesName = "";
		String faceTargetYn = "";
		String videoTargetYn = "";
		String osType = "ios".equalsIgnoreCase(PRCSharedUtils.getOsType()) ? "IOS" : "AND";
		int curAppVersion = Integer.parseInt(StringUtils.defaultIfEmpty(PRCSharedUtils.getOsVersion(),"0").replace(".", ""));
		int lastAppVersion = Integer.parseInt(StringUtils.defaultIfEmpty(PropertiesUtils.getString("LAST_APP_VER_"+osType),"0").replace(".", ""));
		String isOldApp = (curAppVersion < lastAppVersion) ? "Y" : "N";
		
		log.debug("현재 앱버전 => {}", curAppVersion);
		log.debug("마지막 앱버전 => {}", lastAppVersion);
		
		//안면인식 여부
		if (PRCSharedUtils.isSB()) { // APP
			propertiesName = "FACE_TARGET_" + StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("BIZ_TYPE",String.class), request.getBizType());
        } else {
        	propertiesName = "WEB_FACE_TARGET_" + StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("BIZ_TYPE",String.class), request.getBizType());
        }
		
		faceTargetYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString(propertiesName),"N");
		faceTargetYn = "Y".equals(request.getIsForceFace())?"Y" : faceTargetYn;
		
		//영상통화 여부
		if ( "Y".equals(request.getIsForceVideo()) ) {
			videoTargetYn = "Y";
			sessionManager.setGlobalValue("isForceVideo", "Y");
		} else {
			propertiesName = "VIDEO_CALL_TARGET_" + StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("BIZ_TYPE",String.class), request.getBizType());
			videoTargetYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString(propertiesName),"N");
		}
		
		sessionManager.setGlobalValue("faceYn", faceTargetYn);
		sessionManager.setGlobalValue("videoCallYn", videoTargetYn);
		
		log.debug("안면인식 대상여부 => {}",faceTargetYn);
		log.debug("영상통화 대상여부 => {}",videoTargetYn);
		
		// 신분증 촬영 목록 조회
		IdtOcrGetIdCardShotAblListRequest idCardShotAblListParam = new IdtOcrGetIdCardShotAblListRequest();
		
		idCardShotAblListParam.setBizType(StringUtils.defaultIfEmpty(request.getBizType(), SessionUtils.getSessionValue("BIZ_TYPE")));
		idCardShotAblListParam.setParamJsonString(request.getParamJsonString());
		idCardShotAblListParam.setIsCommonEntered(request.getIsCommonEntered());
		
		IdtOcrGetIdCardShotAblListResponse idCardShotAblList = getIdCardShotAblList(serviceContext, idCardShotAblListParam);
		
		response.setViewIdTypeJsonString(idCardShotAblList.getViewIdType());
		response.setFaceTargetYN(faceTargetYn);
		response.setKcddPollingYn(PropertiesUtils.getString("KCDD_POLLING_YN", "N"));
		response.setIsCddOk(StringUtils.defaultString(cddInfoResponse.getYoCddOk()));
		response.setCddIlFlag(StringUtils.defaultString(cddInfoResponse.getYoCddIlFlag()));
		response.setAlcheraClientLicenseKey(StringUtils.defaultString(PropertiesUtils.getString("ALCHERA_CLIENT_LICENSE_KEY")));
		response.setCustNo(custNo);
		response.setBizType(bizType);
		response.setTradNo(tradNo);
		response.setDataModifyYn(StringUtils.defaultIfEmpty(PropertiesUtils.getString("IS_DATA_MODIFY"), "N"));
		response.setIsOldApp(isOldApp);
		
		return response;
	}
	
	
	@ServiceEndpoint(url = "/getIdCardShotAblList", name = "신분증촬영 가능목록조회 [MA3CMMBIZ012_107S]")
    public IdtOcrGetIdCardShotAblListResponse getIdCardShotAblList(IServiceContext serviceContext,
    		IdtOcrGetIdCardShotAblListRequest request) {
		IdtOcrGetIdCardShotAblListResponse response = new IdtOcrGetIdCardShotAblListResponse();
		
		String isOverSeasKorean = StringUtils.defaultIfEmpty(request.getIsOverSeasKorean(), "N");
		GetCheckAvailableRequest chkAblParam = new GetCheckAvailableRequest();
		chkAblParam.setBizType(request.getBizType());
		chkAblParam.setIsCommonEntered(request.getIsCommonEntered());
		chkAblParam.setParamJsonString(request.getParamJsonString());
		
		GetCheckAvailableResponse chkAblOutput = identityOcrComponent.getCheckAvailable(serviceContext, chkAblParam);
		
		int userAge = Integer.parseInt(chkAblOutput.getUserAge());
		String isLowAge = chkAblOutput.getIsLowAge();
		String isForeigner = chkAblOutput.getIsForeigner();
		String isCommonEntered = StringUtils.defaultIfEmpty(request.getIsCommonEntered(), "N");
		
		String bizType = StringUtils.defaultIfEmpty(request.getBizType(), SessionUtils.getSessionValue("BIZ_TYPE"));
		JSONObject screenJsonData = new JSONObject();
		String entryInfo = "";
		String paramJsonString = "";
		
		try {
			paramJsonString = URLDecoder.decode(request.getParamJsonString(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			paramJsonString = "{}";
		}
		
		try {
			paramJsonString = StringUtils.isEmpty(paramJsonString)?"{}":"";
			screenJsonData = new JSONObject(paramJsonString);
			entryInfo = screenJsonData.optString("ENTRY_INFO");
		} catch (JSONException e) {
			log.debug("JSON 파싱 실패");
		}
		
		log.debug("getIdCardShotAblList screenJsonData => {}",screenJsonData);		
		// 회원가입 체크
		if ("INBK".equals(bizType) || ("MOTP".equals(bizType) && "INBK".equals(entryInfo))) {
			bizType = "INBK";
		}
		
		// 신분증촬영공통 체크 (FDS)
		if ("Y".equals(isCommonEntered) || "IDCM".equals(bizType)) {
			bizType = "IDCM";
		}
		
		// 이용가능 신분증 리스트 초기세팅
		Map<IdentityConstants.IdType,String> idCardMap = identityOcrComponent.idCardMapInit();
		
		// 최소 이용나이 이상인 경우
		if (!"Y".equals(isLowAge)) {
			if ("Y".equalsIgnoreCase(isForeigner)) {
				Map<String,String> chkForerignerAllow = BankingBizUtils.getCheckForeignerAllowed(bizType, isForeigner);
				String isAllowed = chkForerignerAllow.get("isAllowed");
				
				if ("Y".equals(isAllowed)) {
					// 외국인 이용가능 시 신분증 목록 세팅
					identityOcrComponent.enableForeignIds(idCardMap, bizType, userAge);
				}
				
			} else {
				if ("Y".equalsIgnoreCase(isOverSeasKorean)) {
					identityOcrComponent.enableOverSeasKoreanIds(idCardMap, bizType, userAge);
				} else {
					identityOcrComponent.enableKoreanIds(idCardMap, bizType, userAge);
				}
			}
		}
		
		
		Map<IdentityConstants.Medium, Map<IdentityConstants.IdType, String>> resultMap = identityOcrComponent.idCardPolicyFilter(idCardMap);
		
		response.setViewIdType(BankingBizUtils.toJSONStringFromObject(resultMap));
				
		return response;

	}
	
	@ServiceEndpoint(url = "/updateSSATruthInfo", name = "사본판별결과 저장 [MA3CMMBIZ012_105S]")
    public IdtOcrUpdateSSATruthInfoResponse updateSSATruthInfo(IServiceContext serviceContext,
    		IdtOcrUpdateSSATruthInfoRequest request) {
		IdtOcrUpdateSSATruthInfoResponse response = new IdtOcrUpdateSSATruthInfoResponse();
		
		String inpSsaTruth = StringUtils.defaultString(request.getSsaTruth());
		String ssaTruth = "";
		String ssaConfidence = StringUtils.defaultIfEmpty(request.getSsaConfidence(),"0");
		
		if(ssaConfidence.length() > 5) {
			ssaConfidence = ssaConfidence.substring(0, 5);
		} else {
			StringBuilder sb = new StringBuilder(ssaConfidence);
			
			if( !ssaConfidence.contains(".") ) {
				sb.append(".");
			}
			
			while(sb.length() < 5) {
				sb.append("0");
			}
			
			ssaConfidence = sb.toString();
		}
		
		switch(inpSsaTruth) {
			case "REAL": ssaTruth = PRCSharedUtils.isSB()?"RA":"RW";break;
			case "FAKE": ssaTruth = PRCSharedUtils.isSB()?"FA":"FW";break;
		}
		
		SsaTruthInfoParameter param = new SsaTruthInfoParameter();
		
		param.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
		param.setTradNo(SessionUtils.getSessionValue("TRAD_NO"));
		
		param.setSsaTruth(ssaTruth);
		param.setSsaConfidence(ssaConfidence);
		
		sessionManager.removeGlobalValue("idRecogResultYn");
		
		try {
			
			log.debug("사본판별 결과 저장 param => {}", param);
			
			nfTradInfoMgtDao.updateSSATruthInfo(param);
			
			if("REAL".equals(inpSsaTruth)) {				
				sessionManager.setGlobalValue("idRecogResultYn" , "Y");
			}
			response.setUpdateResult("SUCCESS");
		} catch(Exception e) {
			sessionManager.setGlobalValue("idRecogResultYn" , "N");
			response.setUpdateResult("FAILED");
		}
		log.debug("ssaTruth => {}", ssaTruth);
		log.debug("idRecogResultYn => {}", SessionUtils.getSessionValue("idRecogResultYn"));
		
		return response;
	}
	
	@ServiceEndpoint(url = "/extractUniquePoints", name = "특장점 추출 [MA3CMMBIZ012_103S]")
	public IdtOcrExtractUniquePointsResponse extractUniquePoints(IServiceContext serviceContext,
			IdtOcrExtractUniquePointsRequest request) {
		IdtOcrExtractUniquePointsResponse response = new IdtOcrExtractUniquePointsResponse();
		
//		if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
//			// 강제성공 처리
//			response.setResultCode("Y");
//			response.setPhotoInfo("000");
//			response.setPhotoInfoSize("4668");
//			response.setExtractScore("100");
//			
//			return response;
//		}
		
		String idCardImage = StringUtils.defaultString(request.getImage());
		String idCardOcrType = StringUtils.defaultString(request.getOcrType());
		String score = "";
		String photoInfo = "";
		BigDecimal scoreDec = new BigDecimal(0);
		BigDecimal zero = new BigDecimal(0); 
		JSONObject faceJsonData = new JSONObject();
		
		/**
		 * 1 : 주민등록증
		 * 2 : 운전면허증
		 * 3 : 외국인등록증
		 * 4 : 국가보훈증
		 * 6 : 여권
		 */
		if ("6".equalsIgnoreCase(idCardOcrType)) {
			try {
				faceJsonData = identityOcrComponent.executePassportVerfication(idCardImage);
				
				JSONArray jsonArr = faceJsonData.optJSONArray("faces_with_features");
				JSONObject jsonObj = jsonArr.optJSONObject(0);
				
				score = jsonObj.isNull("score") ? "0" : String.valueOf(jsonObj.optDouble("score"));
				photoInfo = jsonObj.optString("feature");
				scoreDec = new BigDecimal(score);
				
			} catch(ClientProtocolException cpe) {
				log.error(cpe.getMessage(),cpe);
				PRCServiceException prcException = new PRCServiceException("PRCCMM0068"); // 이미지 파일 변환시 오류가 발생하였습니다. 확인 후 다시 시도해주세요.
				prcException.setErrorCode("PRCCMM0068");
    			throw prcException;
			} catch(Exception e) {
				log.error(e.getMessage(),e);
				PRCServiceException prcException = new PRCServiceException("PRCCMM0068"); // 이미지 파일 변환시 오류가 발생하였습니다. 확인 후 다시 시도해주세요.
				prcException.setErrorCode("PRCCMM0068");
    			throw prcException;
			}
		} else {
			byte[] photoImageByte = AlcAES256.decrypt(idCardImage).getBytes();
			
			log.debug("특장점 추출 [MA3CMMBIZ012_103S] 알체라 복호화(얼굴) photoImageByte => {}", Arrays.toString(photoImageByte));
			
			try {
				faceJsonData = identityOcrComponent.facePrint(Base64.decodeBase64(photoImageByte));
				score = StringUtils.defaultString(faceJsonData.optString("extractScore"));
				photoInfo = StringUtils.defaultString(faceJsonData.optString("phothinfo"));
				
				scoreDec = new BigDecimal(StringUtils.defaultIfEmpty(score, "0"));
			} catch(Exception ioe) {
				PRCServiceException prcException = new PRCServiceException("PRCCMM0068"); // 이미지 파일 변환시 오류가 발생하였습니다. 확인 후 다시 시도해주세요.
				prcException.setErrorCode("PRCCMM0068");
    			throw prcException;
			}
		}
		
		if(faceJsonData.isEmpty()) {
			response.setResultCode("N");
		} else {
			if(scoreDec.compareTo(zero) < 0) {
				response.setResultCode("N");
			} else {
				response.setResultCode("Y");
				response.setPhotoInfo(photoInfo);
				
				if ("6".equals(idCardOcrType)) {
					response.setPhotoInfoSize(Integer.toString(photoInfo.length()));
				} else {
					response.setPhotoInfoSize("4668");
				}
				
				response.setExtractScore(score);
			}
		}
		
		return response;
	}
	
	@ServiceEndpoint(url = "/updateNonFaceIdCardStatus", name = "비대면인증상태 수정 [MA3CMMBIZ012_204S]")
	public void updateNonFaceIdCardStatus(IServiceContext serviceContext,
			IdtOcrUpdateNonFaceIdCardStatusRequest request) {
		String custNo = StringUtils.defaultString(SessionUtils.getSessionValue("CUST_NO"));
		String idCardCd = StringUtils.defaultString(request.getIdCardCd());
		String cnnctnTradNo = StringUtils.defaultString(SessionUtils.getSessionValue("CNNCTN_TRAD_NO"));
		String bizType = StringUtils.defaultString(request.getBizType());
		String cnnctnWay = StringUtils.defaultString(SessionUtils.getSessionValue("CNNCTN_WAY"));
		String shootingType = StringUtils.defaultString(request.getShootingType());
		String mobileSessionClearYn = StringUtils.defaultString(request.getMobileSessionClearYn());
		String idCardSessionClearYn = StringUtils.defaultString(request.getIdCardSessionClearYn());
		
		if ("Y".equals(mobileSessionClearYn)) {
			sessionManager.removeGlobalValue("mobileLicNctSessionYN");
		}
		if ("Y".equals(idCardSessionClearYn)) {
			sessionManager.removeGlobalValue("idcardNctSessionYN");
		}
		
		if ("R".equals(shootingType)) {
			String seqNoList = StringUtils.defaultString(request.getSeqNoList());
			String tradNoList = StringUtils.defaultString(request.getTradNoList());
			
			if (StringUtils.isNotEmpty(custNo) && StringUtils.isNotEmpty(seqNoList) && StringUtils.isNotEmpty(tradNoList)) {
				String seqNoArr[] = seqNoList.split(",");
				String tradNoArr[] = tradNoList.split(",");
				
				if (seqNoArr.length > 0 && tradNoArr.length > 0) {
					int idx = 0;
					NonFaceAuthTradInfoParameter param = new NonFaceAuthTradInfoParameter();
					
					param.setCustNo(custNo);
					param.setIdCardCd(idCardCd);
					
					for (String tradNo : tradNoArr) {
						if (seqNoArr.length<idx+1) {
							break;
						} else if(StringUtils.isEmpty(seqNoArr[idx]) || StringUtils.isEmpty(tradNo)) {
							idx++;
							continue;
						}
						
						param.setSeqNo(seqNoArr[idx]);
						param.setTradNo(tradNo);
						
						nfTradInfoMgtDao.updateNonFaceAuthTradInfo(param);
						
						idx++;
					}
				}
			}
		} else {
			String tradNo = StringUtils.defaultString(request.getTradNo());
			String seqNo = StringUtils.defaultString(request.getSeqNo());
			
			NonFaceAuthTradInfoParameter param = new NonFaceAuthTradInfoParameter();
			
			param.setCallCntrStsCd(StringUtils.defaultString(request.getCallCntrStsCd()));
			param.setPrgrssStsCd(StringUtils.defaultString(request.getPrgrssStsCd()));
			param.setSeqNo(seqNo);
			param.setIdCardCd(idCardCd);
			param.setCustNo(custNo);
			param.setTradNo(tradNo);
			if(StringUtils.isNotEmpty(cnnctnWay)) {
				param.setCnnctnWayDynamic(cnnctnWay);
			}
			
			nfTradInfoMgtDao.updateNonFaceAuthTradInfo(param);
			
			if (StringUtils.isNotEmpty(cnnctnTradNo) && ("CASA".equals(bizType) || "BBCA".equals(bizType))) {
				
				CnnctnTradInfoParameter cnnctnTradInfoParam = new CnnctnTradInfoParameter();
				
				cnnctnTradInfoParam.setCnnctnWay(cnnctnWay);
				cnnctnTradInfoParam.setTradNo(tradNo);
				cnnctnTradInfoParam.setCnnctnTradNo(cnnctnTradNo);
				cnnctnTradInfoParam.setCustNo(custNo);
				if(StringUtils.isNotEmpty(cnnctnWay)) {
					cnnctnTradInfoParam.setCnnctnWayDynamic(cnnctnWay);
				}
				
				nfTradInfoMgtDao.insertCnnctnTradInfo(cnnctnTradInfoParam);
			}
		}
		
		if (!RunMode.PRD.equals(RuntimeContext.getRunMode()) && "Y".equals(request.getTestSkipYn())) {
			//운영제외 테스트 스킵용 세션세팅
			sessionManager.setGlobalValue("idRecogResultYn", "Y");
			sessionManager.setGlobalValue("idTruthResultYn", "Y");
			sessionManager.setGlobalValue("faceResultYn", "Y");
		}
	}
	
	@ServiceEndpoint(url = "/getNonFaceInfo", name = "비대면인증상태 수정 [MA3CMMBIZ012_202S]")
	public IdtOcrGetNonFaceInfoResponse getNonFaceInfo(IServiceContext serviceContext,
			IdtOcrGetNonFaceInfoRequest request) {
		IdtOcrGetNonFaceInfoResponse response = new IdtOcrGetNonFaceInfoResponse();
		
		String isDataModify = StringUtils.defaultIfEmpty(PropertiesUtils.getString("IS_DATA_MODIFY"), "N");
		String perBusNo2Enc = StringUtils.defaultString(request.getPerBusNo2Enc());
		String inpPerBusNo2 = StringUtils.defaultString(request.getPerBusNo2());
		String inpMidLicNo = StringUtils.defaultString(request.getLicenseMiddleNumber());
		String perBusNo2 = 
				("Y".equals(isDataModify)&&StringUtils.isNotEmpty(inpPerBusNo2)) ? inpPerBusNo2 : alcheraDataDecrypt(perBusNo2Enc);
		
		sessionManager.setGlobalValue("nonFaceInputPerBusNo2", inpPerBusNo2);
		sessionManager.setGlobalValue("nonFaceInputMidLicNo", inpMidLicNo);
		
		String psNo = StringUtils.defaultString(request.getPerBusNo1())+perBusNo2;
		String name = request.getName();
		String shootingType = request.getShootingType(); //신분증 촬영 구분("R":재촬영)
		boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());
		String ocrType = StringUtils.defaultString(request.getOcrType()); // 1:주민등록증, 2:운전면허증, 3:외국인등록증, 4:국가보훈등록증, 6:여권
		
		String sessionName = "3".equals(ocrType)?SessionUtils.getSessionValue("EngCustName"):SessionUtils.getSessionValue("CustName");
		String sessionPerBusNo = SessionUtils.getSessionValue("PerBusNo");
		
		boolean compareName = name.equals(sessionName);
		boolean comparePerBusNo = psNo.equals(sessionPerBusNo);
		boolean isPassFlag = false;
		
		if (isReal) {
			if ("6".equals(ocrType)) {
				//여권은 이름만 체크
				isPassFlag = compareName;
			}else {
				isPassFlag = compareName&&comparePerBusNo;
			}
		}else {
//			String IS_MP_SEND_TEST = StringUtils.defaultIfEmpty(PropertiesUtils.getString("IS_MP_SEND_TEST"), "N");
//			isPassFlag = "Y".equals(IS_MP_SEND_TEST); // 테스트용 스킵 여부
//			
//			if(!isPassFlag) {
//				isPassFlag = comparePerBusNo;//주민등록번호만 체크
//			}
			
			isPassFlag = true; // 운영제외 테스트용 스킵
		}
		
		if (isPassFlag) {
			if (shootingType.equals("R")) {
				response = prcIdCardReShooting(request);
			} else {
				response = prcIdCardShooting(request);
			}
		}else {
			response.setPassCode("F");
		}
		
		return response;
	}
	
	@ServiceEndpoint(url = "/sendIdCardImage", name = "실물신분증 이미지 전송 [MA3CMMBIZ012_203S]")
	public IdtOcrSendIdCardImageResponse sendIdCardImage(IServiceContext serviceContext,
			IdtOcrSendIdCardImageRequest request) {
		IdtOcrSendIdCardImageResponse response = new IdtOcrSendIdCardImageResponse();
		
		String shootingType = StringUtils.defaultString(request.getShootingType());
		String isTruthCheck = StringUtils.defaultIfEmpty(request.getIsTruthCheck(), "N");
		String clerkNo = StringUtils.defaultString(request.getClerkNo());
		String branchNo = StringUtils.defaultString(request.getBranchNo());
		String branchNm = StringUtils.defaultString(request.getBranchNm());
		String modifyYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString("IS_DATA_MODIFY"), "N");
		String oplicNo = "";
		String pssprtNo = alcheraDataDecrypt(StringUtils.defaultString(request.getPssprtNo()));
		String mrtsNo = "";
		String perBusNoEnc = alcheraDataDecrypt(StringUtils.defaultString(request.getPerBusNoEnc()));
		String inputBusNo2 = sessionManager.getGlobalValue("nonFaceInputPerBusNo2", String.class);
		String perBusNo2 = 
				"Y".equals(modifyYn) && StringUtils.isNotEmpty(inputBusNo2) ? inputBusNo2 : perBusNoEnc;
		String mrz1 = request.getMrz1Enc();
		String mrz2 = request.getMrz2Enc();
		
		
		String licenseFontNumber = StringUtils.defaultString(request.getLicenseFrontNumber());
		String licenseMiddleNumberEnc = alcheraDataDecrypt(StringUtils.defaultString(request.getLicenseMiddleNumberEnc()));
		String inputLicenseMiddleNumber = sessionManager.getGlobalValue("nonFaceInputMidLicNo", String.class);
		String licenseLastNumber = StringUtils.defaultString(request.getLicenseLastNumber());
		
		String veteransNumber1 = alcheraDataDecrypt(StringUtils.defaultString(request.getVeteransNumber1()));
		String veteransNumber2 = alcheraDataDecrypt(StringUtils.defaultString(request.getVeteransNumber2()));
		
		String licenseMiddleNumber = 
				"Y".equals(modifyYn) && StringUtils.isNotEmpty(inputLicenseMiddleNumber) ? inputLicenseMiddleNumber : licenseMiddleNumberEnc;
		oplicNo = licenseFontNumber+licenseMiddleNumber+licenseLastNumber;
		mrtsNo = veteransNumber1+veteransNumber2;
		
		IdtOcrNonFaceImageDataProcessRequest param = new IdtOcrNonFaceImageDataProcessRequest();
		
		param.setIdCardData(request.getIdCardData());
		param.setIdCdIsuDt(StringUtils.defaultString(request.getIdCdIsuDt()).replaceAll("[^0-9]", ""));
//		param.setDataModifyYn(modifyYn);
		param.setOplicNo(oplicNo);
		param.setPssprtNo(pssprtNo);
		param.setMrtsNo(mrtsNo);
		param.setIdCdDsCd(request.getIdCdDsCd());
		param.setFngrInfo(request.getFngrInfo());
		param.setPhotoInfo(request.getPhotoInfo());
		param.setPhotoInfoSize(request.getPhotoInfoSize());
		param.setVtrnNoChgYn(request.getVtrnNoChgYn());
		param.setPassportNoChgYn(request.getPassportNoChgYn());
		param.setCustIssDtChgYn(request.getCustIssDtChgYn());
		param.setCustNmChgYn(request.getCustNmChgYn());
		param.setCustNoChgYn(request.getCustNoChgYn());
		param.setExtractScore(request.getExtractScore());
		param.setClerkNo(clerkNo);
		param.setBranchNo(branchNo);
		param.setBranchNm(branchNm);
		param.setIsTruthCheck(isTruthCheck);
		param.setAlienType(request.getAlienType());
		param.setMrz1Enc(mrz1);
		param.setMrz2Enc(mrz2);
		param.setName(request.getName());
		param.setPerBusNo1(request.getPerBusNo1());
		param.setPerBusNo2(perBusNo2);
		param.setForeignerTypeChgYn(request.getForeignerTypeChgYn());
		
		log.debug("실물신분증 이미지 전송 param => {}", param);
		
		if ("R".equals(shootingType)) {
			param.setPassCode("0000");
			
			String seqNoList = StringUtils.defaultString(request.getSeqNoList());
			String tradNoList = StringUtils.defaultString(request.getTradNoList());
			String businessDateList = StringUtils.defaultString(request.getBusinessDateList());
			String fileNameList = StringUtils.defaultString(request.getFileNameList());
			String seqNoArr[] = seqNoList.split(",");
			String tradNoArr[] = tradNoList.split(",");
			String bDayArr[] = businessDateList.split(",");
			String fileNameArr[] = fileNameList.split(",");
			
			if (seqNoArr.length > 0 && tradNoArr.length > 0) {
				int idx = 0;
				for (String seqNo : seqNoArr) {
					if (tradNoArr.length < idx+1 || fileNameArr.length < idx+1 || bDayArr.length < idx+1) {
						break;
					} else if(StringUtils.isEmpty(seqNo) || StringUtils.isEmpty(tradNoArr[idx])) {
						continue;
					}
					
					if (!"N".equals(response.getResultCode())) {
						param.setSeqNo(seqNo);
						param.setTradNo(tradNoArr[idx]);
						param.setFileName(fileNameArr[idx]+".jpg");
						param.setBusinessDate(bDayArr[idx]);
						
						response = nonFaceImageDataProcess(param);
					}
				}
			}
		} else {
			param.setPassCode("1111");
			param.setSeqNo(request.getSeqNo());
			param.setTradNo(request.getTradNo());
			param.setFileName(request.getFileName()+".jpg");
			param.setBusinessDate(request.getBusinessDate());
			
			response = nonFaceImageDataProcess(param);
		}
		
		sessionManager.setGlobalValue("idTruthResultYn", response.getResultCode());
		
		return response;
		
	}
	
	@ServiceEndpoint(url = "/getAcctAuthAbleYn", name = "계좌인증가능여부 조회 [MA3ACTOPN001_801S]")
	public IdtOcrGetAcctAuthAbleYnResponse getAcctAuthAbleYn(IServiceContext serviceContext) {
		IdtOcrGetAcctAuthAbleYnResponse response = new IdtOcrGetAcctAuthAbleYnResponse();
		
		String custNo = StringUtils.defaultString(SessionUtils.getSessionValue("CUST_NO"));
		
		if (StringUtils.isEmpty(custNo)) {
			NonFaceCustomerInfoInquiryRequest param = new NonFaceCustomerInfoInquiryRequest();
			
			param.setSsn(SessionUtils.getSessionValue("PerBusNo"));
			
			NonFaceCustomerInfoInquiryResponse result = customerInfoComponent.getNonFaceCustomerInfo(param);
			
			custNo = result.getCustNo();
		}
		
		
		CntrtyRemitReqCntParameter remitReqCntParam = new CntrtyRemitReqCntParameter();
		
		remitReqCntParam.setCustNo(custNo);
		remitReqCntParam.setTradRegGb( "Y".equals(sessionManager.getGlobalValue("isCSL", String.class)) ? "1" : "0" );
		
		String cntrprtyRemitReqCnt = nfRemitMgtDao.selectCntrtyRemitReqCnt(remitReqCntParam);
		String cntrtyAuthFailCnt = nfRemitMgtDao.selectCntrtyAuthFailCnt(remitReqCntParam);
		
		response.setCntrprtyRemitReqCnt( Integer.parseInt(StringUtils.defaultIfEmpty(cntrprtyRemitReqCnt, "0")) );//1원송금 요청 건수
		response.setCntrprtyAuthntFailCnt( Integer.parseInt(StringUtils.defaultIfEmpty(cntrtyAuthFailCnt, "0")) );//1원송금 실패 건수
		response.setCustNm(SessionUtils.getSessionValue("CustName"));
		
		return response;
	}
	
	@ServiceEndpoint(url = "/checkDepositor", name = "예금주 확인 [MA3ACTOPN001_A01S]")
	public IdtOcrCheckDepositorResponse checkDepositor(IServiceContext serviceContext,
			IdtOcrCheckDepositorRequest request) {
		IdtOcrCheckDepositorResponse response = new IdtOcrCheckDepositorResponse();
		
		boolean isCsl = "Y".equals(sessionManager.getGlobalValue("isCSL", String.class));
		boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());
		
		RemitCntParameter remitCntParam = new RemitCntParameter();
		
		remitCntParam.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
		remitCntParam.setTradRegGb(isCsl?"1":"0");
		
		Integer cnt = nfRemitMgtDao.selectRemitCnt(remitCntParam);
		cnt = (cnt == null) ? 0 : cnt;
		
		// 공통부 세팅
        OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_TBS03_H963");

        hostCfg.setImsTranCd("TI1TBS03");
        hostCfg.setInClassCd("H963");
        hostCfg.setSvcCd("963");
        hostCfg.setCaptureSystem("OLTP");
        hostCfg.setVanTp("56");

        CbTbs03H96300Req sendData = new CbTbs03H96300Req();
        
        sendData.setUserID(CommonBizConstants.DEFAULT_USER_ID);
        sendData.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
        sendData.setYI_JBNK(StringUtils.defaultIfEmpty(request.getBnkCd(), ""));
        sendData.setYI_GJNO(StringUtils.defaultIfEmpty(request.getAcctNo(), ""));
        sendData.setYI_JMNO(SessionUtils.getSessionValue("PerBusNo"));
        sendData.setYI_GRGB("2");
        
        try {
        	OltpResponse<CbTbs03H96300Res> hostResponse =  hostClient.sendOltp(hostCfg, sendData, CbTbs03H96300Res.class);
            
            CbTbs03H96300Res h963Res = hostResponse.getResponse();
            
    		response.setExtantFlag( (cnt > 0) ? "Y" : "N" );
    		response.setErrCd(h963Res.getYO_ERCD());
		}catch(IntegrationException ie) {
			if(!isReal && ( "3446".equals(ie.getErrorCode()) ) ) { //TODO 타행 본인계좌 확인 실패 or 계좌번호 체크 실패 케이스 제외 테스트용
				response.setExtantFlag((cnt > 0) ? "Y" : "N");
			}else {
				throw ie;
			}
		}
		
		return response;
	}
	
	@ServiceEndpoint(url = "/sendAcctAuth", name = "계좌인증 1원 송금 [MA3ACTOPN001_A02S]")
	public IdtOcrSendAcctAuthResponse sendAcctAuth(IServiceContext serviceContext,
			IdtOcrSendAcctAuthRequest request) {
		IdtOcrSendAcctAuthResponse response = new IdtOcrSendAcctAuthResponse();
		
		String custNo = StringUtils.defaultString(SessionUtils.getSessionValue("CUST_NO"));
		String tradNo = StringUtils.defaultString(SessionUtils.getSessionValue("TRAD_NO"));
		String acctNo = StringUtils.defaultString(request.getAcctNo());
		String bnkCd = StringUtils.defaultString(request.getBnkCd());
		String extantFlag = StringUtils.defaultString(request.getExtantFlag());
		String changeAcctFlag = StringUtils.defaultString(request.getChangeAcctFlag());
		String custNm = StringUtils.defaultString(SessionUtils.getSessionValue("CustName"));
		String casaFlag = StringUtils.defaultIfEmpty(request.getCasaFlag(), "N");
		String randomKey = RandomKeyUtils.getKey();
		String yioName = "Y".equals(casaFlag)? "계좌개설"+randomKey : randomKey;
		
		log.debug("sendAcctAuth randomKey => {}", randomKey);
		log.debug("sendAcctAuth request => {}", request);
		
		OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_TBS03_D271");

        hostCfg.setImsTranCd("TI1TBS03");
        hostCfg.setInClassCd("D271");
        hostCfg.setSvcCd("271");
        hostCfg.setVanTp("56");
        hostCfg.setCaptureSystem("OLTP");
        
        CbTbs03H27100Req h271Req = new CbTbs03H27100Req();
        
        h271Req.setYISCNM(custNm);
        h271Req.setYIICGM("1");
        h271Req.setYITICGM("1");
        h271Req.setYIICGB("1");
        h271Req.setUserID("FIRST271");
        h271Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
        h271Req.setYIIGJNO(acctNo);
        h271Req.setYIIBKCD(bnkCd);
        h271Req.setYIONAME(toFullChar(yioName));
        
        log.debug("sendAcctAuth h271Req => {}", h271Req);
        
        OltpResponse<CbTbs03H27100Res> hostResponse = hostClient.sendOltp(hostCfg, h271Req, CbTbs03H27100Res.class);
        
        CbTbs03H27100Res h271Res = hostResponse.getResponse();
        
        log.debug("계좌인증 1원 송금 전문 response => {}", h271Res);
        
        RemitInfoParameter dbParam = new RemitInfoParameter();
        
        dbParam.setCustNo(custNo);
        dbParam.setTradNo(tradNo);
        dbParam.setCntrprtyAcctNo(acctNo);
        dbParam.setCntrprtyCd(bnkCd);
        dbParam.setCntrprtyAuthntCd(randomKey);
        
        Integer cnt;
        
        if ("Y".equals(changeAcctFlag) && "Y".equals(extantFlag)) {
        	//타행송금요청 건수 갱신
        	cnt = nfRemitMgtDao.updateRemitInfo(dbParam);
        } else {
        	//타행송금요청 insert
        	dbParam.setCntrprtyRemitReqCnt("1");
        	cnt = nfRemitMgtDao.insertRemitInfo(dbParam);
        }
        
        String sendYn = (cnt !=null && cnt.compareTo(0) > 0 ) ? "Y" : "N";
        
        response.setSendYn(sendYn);
		
		return response;
	}
	
	/***
	 * 비대면 인증 정보 조회 [MA3CMMBIZ012_301S]
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/getNonFaceAuthInfo", name = "비대면 인증 정보 조회 [MA3CMMBIZ012_301S]")
	public IdtOcrNonFaceAuthInfoResponse getNonFaceAuthInfo(IServiceContext serviceContext) {
		IdtOcrNonFaceAuthInfoResponse response = new IdtOcrNonFaceAuthInfoResponse();
		
		NonFaceAuthInfoParameter param = new NonFaceAuthInfoParameter();
		
		param.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
		param.setTradNo(SessionUtils.getSessionValue("TRAD_NO"));
		
		NonFaceAuthInfoResult result = nfTradInfoMgtDao.selectNonFaceAuthInfo(param);
		
		if (result != null) {
			response.setAuthntIndCd(result.getAuthntIndCd());
			response.setCddReqCd(result.getCddReqCd());
			response.setIdCardCd(result.getIdCardCd());
		}
		
		return response;
	}
	
	/***
	 * 비대면 인증 정보 조회 [MA3CMMBIZ012_301S]
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/validateAcctAuthNumber", name = "계좌인증번호 검증 [MA3ACTOPN001_A11S]")
	public IdtOcrValidateAcctAuthNumberResponse validateAcctAuthNumber(IServiceContext serviceContext,
			IdtOcrValidateAcctAuthNumberRequest request) {
		IdtOcrValidateAcctAuthNumberResponse response = new IdtOcrValidateAcctAuthNumberResponse();
		
		String authNumber = StringUtils.defaultString(request.getAuthNumber());
		String bizType = StringUtils.defaultString(request.getBizType());
		String custNo = StringUtils.defaultString(SessionUtils.getSessionValue("CUST_NO"));
		String tradNo = StringUtils.defaultString(SessionUtils.getSessionValue("TRAD_NO"));
		
		log.debug("계좌인증번호 검증 request => {}", request);
		
		sessionManager.setGlobalValue("COMPARE_RESULT_YN_INTERCEPTER", "Y");
		
		RemitInfoParameter infoParam = new RemitInfoParameter();
		
		infoParam.setCustNo(custNo);
		infoParam.setTradNo(tradNo);
		
		//10분 이내 송금 정보 조회
		List<RemitInfoResult> infoResult = nfRemitMgtDao.selectRemitInfo(infoParam);
		
		if (infoResult != null && infoResult.size() > 0) {
			AcctAuthNumberParameter authNumberParam = new AcctAuthNumberParameter();
			
			authNumberParam.setCustNo(custNo);
			authNumberParam.setTradRegGb("Y".equals(sessionManager.getGlobalValue("isCSL", String.class)) ? "1" : "0");
			authNumberParam.setBizType(bizType);
			
			if(StringUtils.isNotEmpty(tradNo)) {
				authNumberParam.setTradNo(tradNo);
			}
			
			//인증코드 조회
			AcctAuthNumberResult authNumberResult = nfRemitMgtDao.selectAcctAuthNumber(authNumberParam);
			
			String authntCd = "";
			
			if (authNumberResult != null) {
				authntCd = StringUtils.defaultString(authNumberResult.getCntrprtyAuthntCd());
			}
			
			if (authntCd.equals(authNumber)) {
				response.setResultYn("Y");
				sessionManager.setGlobalValue("COMPARE_RESULT_YN", "Y");
			} else {
				response.setResultYn("N");
				sessionManager.setGlobalValue("COMPARE_RESULT_YN", "N");
				
				RemitInfoParameter failParam = new RemitInfoParameter();
				
				failParam.setCustNo(custNo);
				failParam.setTradNo(tradNo);
				
				nfRemitMgtDao.updateRemitFailCnt(failParam);
			}
		} else {
			response.setResultYn("T");
			sessionManager.setGlobalValue("COMPARE_RESULT_YN", "T");
		}
		
		return response;
	}
	
	/***
	 * 비대면 인증 완료여부 조회 [MA3ACTOPN001_A12S]
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/getNonFaceAuthCompleteYn", name = "비대면 인증 완료여부 조회 [MA3ACTOPN001_A12S]")
	public IdtOcrGetNonFaceAuthCompleteYnResponse getNonFaceAuthCompleteYn(IServiceContext serviceContext,
			IdtOcrGetNonFaceAuthCompleteYnRequest request) {
		IdtOcrGetNonFaceAuthCompleteYnResponse response = new IdtOcrGetNonFaceAuthCompleteYnResponse();
		
		NonFaceAuthCompleteYnRequest comlYnRequest = new NonFaceAuthCompleteYnRequest();
		
		comlYnRequest.setCustNo(StringUtils.defaultIfEmpty(request.getCustNo(), SessionUtils.getSessionValue("CUST_NO")));
		comlYnRequest.setTradNo(StringUtils.defaultIfEmpty(request.getTradNo(), SessionUtils.getSessionValue("TRAD_NO")));
		
		NonFaceAuthCompleteYnResponse comlYnResponse = authVerifyComponent.getNonFaceAuthCompleteYn(comlYnRequest);
		
		response.setAuthntIndCd(comlYnResponse.getAuthntIndCd());
		response.setCddReqCd(comlYnResponse.getCddReqCd());
		response.setDocEvdcCd(comlYnResponse.getDocEvdcCd());
		response.setIdCardCd(comlYnResponse.getIdCardCd());
		response.setKcddPollingYn(PropertiesUtils.getString("KCDD_POLLING_YN", "N"));
		response.setCddFlag(SessionUtils.getSessionValue("YOCDDIL_FLAG"));
		
		return response;
	}
	
	/***
	 * 안면인식 요청 [MA3CMMBIZ012_602S]
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/requestFaceVerification", name = "안면인식 요청 [MA3CMMBIZ012_602S]")
	public IdtOcrRequestFaceVerificationResponse requestFaceVerification(IServiceContext serviceContext,
			IdtOcrRequestFaceVerificationRequest request) {
		IdtOcrRequestFaceVerificationResponse response = new IdtOcrRequestFaceVerificationResponse();
		
		String transactionId = "";
		
		String idCardData = StringUtils.defaultString(request.getIdCardData());
		String facialRecognitionData = StringUtils.defaultString(request.getFacialRecognitionData());
		
		String base64IdCardData = AlcAES256.decrypt(idCardData);
		String base64FacialRecognitionData = AlcAES256.decrypt(facialRecognitionData);
		
		try {
			transactionId = kftcFrsApiClient.generateTranId();
			
			IdtOcrRequestFaceRecgApiRequest faceRecgApiRequest = new IdtOcrRequestFaceRecgApiRequest();
			
			faceRecgApiRequest.setIdCardData(idCardData);
			faceRecgApiRequest.setIdCardType(request.getIdCardType());
			faceRecgApiRequest.setIsTruthYn(request.getIsTruthYn());
			faceRecgApiRequest.setFacialRecognitionData(facialRecognitionData);
			faceRecgApiRequest.setBase64IdCardData(base64IdCardData);
			faceRecgApiRequest.setBase64FacialRecognitionData(base64FacialRecognitionData);
			
			JSONObject result = callFaceRecgApi(faceRecgApiRequest, transactionId);
			
			log.debug("안면인식 API 호출 result => {}", result);
			
			if (result == null) {
				response.setResultYn("N");
				response.setResultCode("MA999");
				
				return response;
			}
			
			String tranId = StringUtils.defaultIfEmpty(result.optString("transaction_id"), "");
			String rspCode = StringUtils.defaultIfEmpty(result.optString("response_code"), "");
			String resMsg = StringUtils.defaultIfEmpty(result.optString("response_message"), "");
			String resultYn = KftcFrsHelper.SUCCESS_RSP_CODE.equals(rspCode) ? "Y" : "N";
			String userCifNo = SessionUtils.getSessionValue("UserCifNo");
			String isTruthYn = StringUtils.defaultIfEmpty(request.getIsTruthYn(), "N");
			
			if (StringUtils.isEmpty(userCifNo)) {
				CbIbk01H86600Req acctHeldRequest = new CbIbk01H86600Req();
				
				acctHeldRequest.setYIJMNO(SessionUtils.getSessionValue("PerBusNo"));
				
				ListAccountHeldInfo acctHeldResponse = accountListComponent.getListAccountHeld(acctHeldRequest);
				
				userCifNo = acctHeldResponse.getYoCifNo();
				sessionManager.setGlobalValue("UserCifNo", userCifNo);
			}
			
			FaceRecgParameter faceRecgParam = new FaceRecgParameter();
			
			faceRecgParam.setUserCifNo(userCifNo);
			faceRecgParam.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
			faceRecgParam.setTradNo(SessionUtils.getSessionValue("TRAD_NO"));
			faceRecgParam.setFaceTransactionId(StringUtils.defaultIfEmpty(result.optString("transaction_id"), "").trim());
			faceRecgParam.setFaceRecgYn(resultYn);
			faceRecgParam.setFaceRecgScore(result.optBigDecimal("similar_confidence", new BigDecimal(0.0)));
			faceRecgParam.setFaceRecgCd(rspCode);
			faceRecgParam.setFaceRecgMsg(resMsg);
			faceRecgParam.setFaceRecgData(StringUtils.defaultIfEmpty(result.optString("response_datetime"), "").trim());
			
			if("N".equals(isTruthYn)) {
				Map<String,String> saveFileInfo = fileCopyToXVarmFaceRecg(facialRecognitionData, tranId);
				
				faceRecgParam.setSelfiePath(StringUtils.defaultIfEmpty(saveFileInfo.get("filePath"), "").trim());
				faceRecgParam.setFileName(StringUtils.defaultIfEmpty(saveFileInfo.get("fileName"), "").trim());
			}
			
			faceRecgTblDao.insertFaceRecg(faceRecgParam);
			
			if("Y".equals(resultYn)) {
				
				log.debug("bizType => {}", SessionUtils.getSessionValue("BIZ_TYPE"));
				
				RegisterOngoingTradeInfoDataRequest tradInfoRequest = new RegisterOngoingTradeInfoDataRequest();
				
				tradInfoRequest.setBizType(SessionUtils.getSessionValue("BIZ_TYPE"));
				tradInfoRequest.setCallCntrStsCd("CAID");
				tradInfoRequest.setPrgrssStsCd("PRGS");
				
				tradInfoComponent.registOngoingTradeInfoAndScrnDataRcvryData(tradInfoRequest);
			}
			
			response.setResultYn(resultYn);
			response.setResultCode(rspCode);
			sessionManager.setGlobalValue("faceResultYn", resultYn);
			
		} catch (Exception e) {
			log.error("안면인식요청 Exception");
			log.error(e.getMessage(),e);
			sessionManager.setGlobalValue("faceResultYn", "N");
			response.setResultYn("N");
			response.setResultCode("MA999");
		} finally {
			Path pathToFile = Paths.get(PropertiesUtils.getString("FACE_ID_IMAGE_PATH") + transactionId + ".jpeg");
			if(Files.exists(pathToFile)) {
				try {
					Files.delete(pathToFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return response;
	}
	
	/***
	 * 안면인식정보 조회
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/getFaceVerificationInfo", name = "안면인식정보 조회")
	public IdtOcrGetFaceVerificationInfoResponse getFaceVerificationInfo(IServiceContext serviceContext) {
		
		IdtOcrGetFaceVerificationInfoResponse response = new IdtOcrGetFaceVerificationInfoResponse();
		
		String faceResultYn = sessionManager.getGlobalValue("faceResultYn", String.class);
		String videoCallTargetYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString("VIDEO_CALL_TARGET_" + SessionUtils.getSessionValue("BIZ_TYPE")), "N"); // 프로퍼티에서 영상통화 대상여부 플래그 조회
		String faceRecgTryCnt = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("FACE_RECG_TRY_CNT", String.class), "0"); //안면인식 시도 횟수
		
		if(!"Y".equals(faceResultYn)) {
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("videoCallTargetYn", videoCallTargetYn);
			result.put("faceRecgTryCnt", faceRecgTryCnt);
			PRCServiceException prcException = new PRCServiceException("PRCCMM0069");
			prcException.setErrorPageParameters(result);
			throw prcException;
		}
		
		response.setVideoCallTargetYn(videoCallTargetYn);
		response.setFaceRecgTryCnt(faceRecgTryCnt);
		response.setFaceResultYn(faceResultYn);
		
		return response;
	}
	
	/***
	 * 안면인식실패 취소처리[MA3CMMBIZ012_205S]
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/cancelFaceRecg", name = "안면인식실패 취소처리[MA3CMMBIZ012_205S]")
	public IdtOcrCancelFaceRecgResponse cancleFaceRecg(IServiceContext serviceContext) {
		IdtOcrCancelFaceRecgResponse response = new IdtOcrCancelFaceRecgResponse();
		
		String custNo = StringUtils.defaultString(SessionUtils.getSessionValue("CUST_NO"));
		String tradNo = StringUtils.defaultString(SessionUtils.getSessionValue("TRAD_NO"));
		
		sessionManager.setGlobalValue("CNT_ID_CARD_TRUTH_YN", "0");
		sessionManager.removeGlobalValue("TRAD_NO");
		sessionManager.setGlobalValue("FACE_RECG_TRY_CNT", "0");
		
		OngoingTradeInfoCancelRequest cancelParam = new OngoingTradeInfoCancelRequest();
		
		cancelParam.setCustNo(custNo);
		cancelParam.setTradNo(tradNo);
		
		tradInfoComponent.cancelOngoingTradeInfo(cancelParam);
		
		response.setResultYn("Y");
		
		return response;
	}
	
	/***
	 * 신분증인증 검증[MA3CMMBIZ012_401S]
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/validateIdVerification", name = "신분증인증 검증[MA3CMMBIZ012_401S]")
	public void validateIdVerification() {
		
		String idRecogResultYn = SessionUtils.getSessionValue("idRecogResultYn");
		String idTruthResultYn = SessionUtils.getSessionValue("idTruthResultYn");
		String faceResultYn = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("faceResultYn"),"N");
		String videoResultYn = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("videoResultYn"),"N");
		String faceYn = SessionUtils.getSessionValue("faceYn");
		String videoCallYn = SessionUtils.getSessionValue("videoCallYn");
		
		log.debug("validateIdVerification idRecogResultYn => {}", idRecogResultYn);
		log.debug("validateIdVerification idTruthResultYn => {}", idTruthResultYn);
		log.debug("validateIdVerification faceResultYn => {}", faceResultYn);
		log.debug("validateIdVerification videoResultYn => {}", videoResultYn);
		log.debug("validateIdVerification faceYn => {}", faceYn);
		log.debug("validateIdVerification videoCallYn => {}", videoCallYn);
		
		PRCServiceException prcException = new PRCServiceException("PRCTRN1007");
		prcException.setErrorCode("PRCTRN1007"); // 비정상적인 접근입니다.
		
		if (!"Y".equals(idRecogResultYn) || !"Y".equals(idTruthResultYn)) {
			throw prcException;
		}
		
		// 안면인식 업무인경우
		if ("Y".equals(faceYn)) {
			// 안면실패 & 영상통화 안하는경우 실패
			if ("N".equals(faceResultYn) && "N".equals(videoCallYn))
				throw prcException;

			// 안면실패 & 영상통화업무 & 영통 실패인경우 실패
			if ("N".equals(faceResultYn) && "Y".equals(videoCallYn) && "N".equals(videoResultYn))
				throw prcException;
		}
	}
	
	/***
	 * 인증기본정보세팅
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/setAuthInitInfo", name = "인증기본정보세팅")
	public IdtOcrSetAuthInitInfoResponse setAuthInitInfo(IServiceContext serviceContext,
			IdtOcrSetAuthInitInfoRequest request) {
		IdtOcrSetAuthInitInfoResponse response = new IdtOcrSetAuthInitInfoResponse();
		
		String custNo = StringUtils.defaultString(request.getCustNo());
		String tradNo = StringUtils.defaultString(request.getTradNo());
		String bizType = StringUtils.defaultString(request.getBizType());
		
		if (StringUtils.isNotEmpty(custNo)) {
			sessionManager.setGlobalValue("CUST_NO", custNo);
		}
		
		if (StringUtils.isNotEmpty(tradNo)) {
			sessionManager.setGlobalValue("TRAD_NO", tradNo);
		}
		
		if (StringUtils.isNotEmpty(bizType)) {
			sessionManager.setGlobalValue("BIZ_TYPE", bizType);
		}
		
		sessionManager.setGlobalValue("CNNCTN_WAY", StringUtils.defaultString(request.getCnnctnWay()));
		sessionManager.setGlobalValue("CNNCTN_TRAD_NO", StringUtils.defaultString(request.getCnnctnTradNo()));
		sessionManager.setGlobalValue("CLERK_NO", StringUtils.defaultString(request.getClerkNo()));
		
		response.setKcddPollingYn(PropertiesUtils.getString("KCDD_POLLING_YN", "N"));
		
		return response;
	}
	
	/**
	 * 비대면 실명인증 이미지 FileWrite후 SCP로 전송하는 함수 호출
	 * @param param
	 * @return
	 * @throws IOException
	 */
	private IdtOcrSendIdCardImageResponse nonFaceImageDataProcess(IdtOcrNonFaceImageDataProcessRequest input) {
		IdtOcrSendIdCardImageResponse result = new IdtOcrSendIdCardImageResponse();
		
		log.debug("비대면 실명인증 이미지 FileWrite후 SCP로 전송 시작");
		
		FileOutputStream fos = null;
		
		String idCardData = StringUtils.defaultString(input.getIdCardData());
		byte idCardByteData[] = idCardData.getBytes();
		
		String filePath = PropertiesUtils.getString("NON_FACE_IMAGE_PATH");
		String fileName = StringUtils.defaultString(input.getFileName());
		
		log.debug("filePath => {}", filePath);
		log.debug("fileName => {}", fileName);
		
		try {
			File file = new File(filePath+fileName);
			fos = new FileOutputStream(file);
			fos.write(idCardByteData);
			
			log.debug("file write => {}", file.isFile());
			
			if (file.isFile()) {
				UpdateNfInfoParameter param = new UpdateNfInfoParameter();
				param.setSeqNo(input.getSeqNo());
				param.setFilePath("");
				param.setFileName(fileName);
				param.setPassCode(input.getPassCode());
				param.setPsNo(SessionUtils.getSessionValue("PerBusNo"));
				param.setIdCdIsuDt(input.getIdCdIsuDt().replaceAll("[^0-9]", ""));//발급일자
				param.setOplicNo(input.getOplicNo());
				param.setPssprtNo(input.getPssprtNo());
				param.setMrtsNo(input.getMrtsNo());
				param.setIdCdDsCd(input.getIdCdDsCd());
				param.setFngrInfo(input.getFngrInfo());
				param.setPhotoInfo(input.getPhotoInfo());
				param.setPhotoInfoSize(input.getPhotoInfoSize());
				param.setMrtsNo(input.getVtrnNoChgYn());
				param.setPssprtNoChgYn(input.getPassportNoChgYn());
				param.setIdCdDsImg(idCardData);
				param.setCustIssDtChgYn(input.getCustIssDtChgYn());
				param.setCustNmChgYn(input.getCustNmChgYn());
				param.setCustNoChgYn(input.getCustNoChgYn());
				param.setExtractScore(input.getExtractScore());
				param.setBusinessDate(input.getBusinessDate());
				
				nonFaceDBUpdatePhysicalId(param);
				
				Map<String,String> xvarmResult = fileCopyToXVarmPhysicalId(input);
				
				result.setResultCode(xvarmResult.get("RESULTCODE"));
				result.setCode(xvarmResult.get("CODE"));
				result.setReqSeq(xvarmResult.get("REQSEQ"));
				result.setIdCardTruthCnt(sessionManager.getGlobalValue("CNT_ID_CARD_TRUTH_YN", String.class));
			}else {
				result.setResultCode("N");
				result.setCode("");
				result.setReqSeq("");
				result.setIdCardTruthCnt(sessionManager.getGlobalValue("CNT_ID_CARD_TRUTH_YN", String.class));
			}
			
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			result.setResultCode("N");
			result.setCode("");
			result.setReqSeq("");
			result.setIdCardTruthCnt(sessionManager.getGlobalValue("CNT_ID_CARD_TRUTH_YN", String.class));
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	/**
	 * EDOC서버 XVARM에 신분증이미지를 Paceseed 암호화하여 저장한다.
	 * @param param
	 */
	private Map<String, String> fileCopyToXVarmPhysicalId(IdtOcrNonFaceImageDataProcessRequest input) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String srcFileNm = PropertiesUtils.getString("NON_FACE_IMAGE_PATH") + input.getFileName();
		asysConnectData con = null; //XVARM Engine 접근 객체
		String elementID = "";
		
		try {
			int ret = -1;
			edocXvarmComponent.init();
			con = edocXvarmComponent.getXvarmEngineConnector();
			
			asysUsrElement uePage1 = new asysUsrElement(con);
			uePage1.m_localFile = srcFileNm;
			uePage1.m_cClassId = "EDOC_CC";
			uePage1.m_descr = "IDV";
			uePage1.m_userSClass = "SUPER";
			uePage1.m_eClassId = "IMAGE";
			
			ret = uePage1.create("XVARM_MAIN");
			
			if ( ret != 0 ) {
				log.debug("fileCopyToXVarmPhysicalId file Insert Xvarm Exception => {}",ret);
				log.debug("fileCopyToXVarmPhysicalId file Insert Xvarm Exception => {}",uePage1.getLastError());
				resultMap.put("RESULTCODE", "N");
			} else {
				log.debug("fileCopyToXVarmPhysicalId elementid => {} ", uePage1.m_elementId);
				elementID = uePage1.m_elementId;
				
				input.setFilePath(elementID);
		        log.debug("fileCopyToXVarmPhysicalId fileCopyToXVarm 신분증 진위여부 로직 실행");
	        	resultMap = nonFaceIdVerificationProcess(input, PropertiesUtils.getString("EDOC_CHECK_SCP_PATH1"));
			}
		} catch (Exception e) {
			log.debug("fileCopyToXVarmPhysicalId Exception => {}",e);
			resultMap.put("RESULTCODE", "N");
		} finally {
			if(con != null){
				con.close();
				con = null;
			}
		}
		return resultMap;
	}
	
	private Map<String,String> nonFaceIdVerificationProcess(IdtOcrNonFaceImageDataProcessRequest input, String edocServerUrl){
		HttpURLConnection conn = null;
		Map<String,String> result = new HashMap<String,String>();
		
		boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());
		String IS_MP_SEND_TEST = StringUtils.defaultIfEmpty(PropertiesUtils.getString("IS_MP_SEND_TEST"), "N");
			
		try {
			JSONObject jsonData = new JSONObject();
			
			jsonData.put("action", "IdvEdoc");
			jsonData.put("SEQNO", input.getSeqNo());
			// 알체라 복호화 모듈 적용 25.01.17
			jsonData.put("ENCTYPE", "ALCHERA");
			
			jsonData.put("custNoChgYn", input.getCustNoChgYn()); // 실명번호 수정여부
			jsonData.put("custNmChgYn", input.getCustNmChgYn()); // 신분증이름 수정여부
			jsonData.put("custIssDtChgYn", input.getCustIssDtChgYn()); // 발급일자 수정여부
			
			jsonData.put("fngrInfoSize", "0");			
			jsonData.put("fngrInfo", input.getFngrInfo());			
			jsonData.put("photoInfoSize", input.getPhotoInfoSize());
			jsonData.put("photoInfo", input.getPhotoInfo());
			jsonData.put("extractScore", input.getExtractScore());
			jsonData.put("tlgHeader", "NF");
			jsonData.put("FILE_PATH", input.getFilePath());
			jsonData.put("IS_NONSAVEIDCD", input.getIsTruthCheck()); //신분증저장 여부 Flag 추가 (22.03 정기이행) - 제신고 업무 사용
			jsonData.put("REQEMPNO", input.getClerkNo()); // 화상상담 - 소개자 행번 추가 
			jsonData.put("REQBRCD", input.getBranchNo()); // 화상상담 - 신분증 진위확인 영업점 번호 
			jsonData.put("REQBRNM", input.getBranchNm()); // 화상상담 - 신분증 진위확인 영업점명
			
			// ======================
			// TODO: 2026.02 추가
			// ======================
			// 국가보훈증
			jsonData.put("vtrnNoChgYn", input.getVtrnNoChgYn()); // 보훈번호 수정여부
			
			// 여권
			jsonData.put("passportNoChgYn", input.getPassportNoChgYn()); // 여권번호 수정여부
			jsonData.put("passportMrz1", input.getMrz1Enc()); // 여권-MRZ1
			jsonData.put("passportMrz2", input.getMrz2Enc()); // 여권-MRZ2
			
			// 외국인등록증
			jsonData.put("foreignerTypeChgYn", input.getForeignerTypeChgYn()); // 외국인등록증구분 수정여부
			jsonData.put("alienType", input.getAlienType()); // 외국인등록증 종류
			
			if("Y".equals(IS_MP_SEND_TEST)) {
				//테스트용 세팅
				String perBusNo1 = StringUtils.defaultString(input.getPerBusNo1());
				String perBusNo2 = alcheraDataDecrypt(StringUtils.defaultString(input.getPerBusNo2()));
				
				String ocrName = StringUtils.defaultString(input.getName());
				
				jsonData.put("IS_MP_SEND_TEST", IS_MP_SEND_TEST); //신분증 진위여부 플래그 Y:테스트
				jsonData.put("OCRName", Base64.encodeBase64String(ocrName.getBytes()));
				jsonData.put("OCRPsno", perBusNo1+perBusNo2);
			}
			
			JSONObject responseData = new JSONObject();
			
			if (isReal || "Y".equals(IS_MP_SEND_TEST)) {
				//운영 or TEST여부 Y => 신분증 진위확인 요청
				
				StringBuilder sb = new StringBuilder();
				
				log.debug("전송서버 => {}", edocServerUrl);
				
				URI uri = URI.create(edocServerUrl);
				conn = (HttpURLConnection) uri.toURL().openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				
				// 요청 전송
				try (OutputStream os = conn.getOutputStream()) {
					os.write(jsonData.toString().getBytes(StandardCharsets.UTF_8));
					os.flush();
				}
				
				// 응답 읽기
				int status = conn.getResponseCode();
				InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

				try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				}
				
				log.debug("진위확인 응답 => {}", sb.toString());
				
				try {
					JSONObject jsonResult = new JSONObject(sb.toString());
					
					JSONObject root = jsonResult.optJSONObject("_msg_");
					
					if (root == null) {
						responseData = new JSONObject();
					} else {
						JSONObject body = root.optJSONObject("_body_");
						responseData = (body != null) ? body : new JSONObject();
					}
				} catch(Exception e) {
					responseData = new JSONObject();
				}
			} else {
				log.debug("신분증 진위확인 스킵 성공처리");
				// 개발은 성공 처리
				responseData = new JSONObject("{\"C_CODE\":\"200\", \"CODE\":\"000\", \"REQSEQ\":\"\"}");
			}
			
			log.debug("신분증 진위확인 body => {}", responseData);
			
			String C_CODE = StringUtils.defaultString(responseData.optString("C_CODE"));
			String REQSEQ = StringUtils.defaultString(responseData.optString("REQSEQ"));
			String CODE = StringUtils.defaultString(responseData.optString("CODE"));
						
			if((!isReal && ("038".equals(CODE) || "201".equals(CODE) ))) {
				//개발,테스트에서 특징점 추출실패 응답, 038 : 운전면허증 데이터 불일치, 201 : 여권 데이터 미존재
				CODE = "000";
			}
			
			result.put("REQSEQ", REQSEQ);
			result.put("CODE", CODE);
			
			String idCardCnt = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CNT_ID_CARD_TRUTH_YN", String.class), "0");
			
			/*
			003 : 분실신분증
			004 : 습득주민등록증
			005 : 회수주민등록증
			034 : 개인 등록상태와 증 종류 매칭 실패(개인의 재외국민여부와 증의 재외국민여부가 불일치 시)
			035 : 사망자/말소자
			040 : 재발급
			041 : 취소운전면허증
			*/
			if (CODE.matches("003|004|005|034|035|040|041")) {
				//세션에만 세팅
				sessionManager.setGlobalValue("CNT_ID_CARD_TRUTH_YN", Integer.toString(Integer.parseInt(idCardCnt)+1));
			} else if(CODE.matches("110|111|112|113|114|115|116|117|118|119")) {
				/*  
				110 ~ 119 : 장애case
				*/
				log.error("nonFaceIdVerificationProcess ERRCODE : ["+CODE+"] ###");
			} else if (!"000".equals(CODE)) {
				sessionManager.setGlobalValue("CNT_ID_CARD_TRUTH_YN", Integer.toString(Integer.parseInt(idCardCnt)+1));
			}
			
			if ("200".equals(C_CODE) ) {
				//정상응답
				UpdateNfInfoParameter param = new UpdateNfInfoParameter();
				param.setSeqNo(input.getSeqNo());
				param.setFilePath("");
				param.setFileName(input.getFileName());
				param.setPassCode(input.getPassCode());
				param.setPsNo(SessionUtils.getSessionValue("PerBusNo"));
				param.setIdCdIsuDt(input.getIdCdIsuDt());//발급일자
				param.setOplicNo(input.getOplicNo());
				param.setPssprtNo(input.getPssprtNo());
				param.setMrtsNo(input.getMrtsNo());
				param.setIdCdDsCd(input.getIdCdDsCd());
				param.setFngrInfo(input.getFngrInfo());
				param.setPhotoInfo(input.getPhotoInfo());
				param.setPhotoInfoSize(input.getPhotoInfoSize());
				param.setMrtsNoChgYn(input.getVtrnNoChgYn());
				param.setPssprtNoChgYn(input.getPassportNoChgYn());
				param.setIdCdDsImg(input.getIdCardData());
				param.setCustIssDtChgYn(input.getCustIssDtChgYn());
				param.setCustNmChgYn(input.getCustNmChgYn());
				param.setCustNoChgYn(input.getCustNoChgYn());
				param.setExtractScore(input.getExtractScore());
				param.setBusinessDate(input.getBusinessDate());
				
				nonFaceDBUpdatePhysicalId(param);
				
				result.put("RESULTCODE", "Y");
			} else {
				result.put("RESULTCODE", "N");
			}
			
			// 신분증 진위확인 결과 저장 NF_TRADINFO_MGT_U_04
			IdVerificationCountParameter updateParam = new IdVerificationCountParameter();
			
			updateParam.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
			updateParam.setTradNo(input.getTradNo());
			updateParam.setIdCardCnt(StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CNT_ID_CARD_TRUTH_YN", String.class), "0"));
			
			log.debug("신분증진위확인 결과 저장 updateIdVerificationCount param => {}", updateParam);
						
			nfTradInfoMgtDao.updateIdVerificationCount(updateParam);
			
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("MalformedURL! " + edocServerUrl);
		} catch (IOException ioe) {
			log.debug("신분증 진위확인 IOException 발생, 현재 url : {}", edocServerUrl);
			if (PropertiesUtils.getString("EDOC_CHECK_SCP_PATH1").equals(edocServerUrl)) {
				//2 서버로 재시도
				result = nonFaceIdVerificationProcess(input, PropertiesUtils.getString("EDOC_CHECK_SCP_PATH2"));
			} else {
				UpdateNfInfoParameter param = new UpdateNfInfoParameter();
				
				param.setSeqNo(input.getSeqNo());
				param.setErrorCode("E");
				param.setPassCode(input.getPassCode());
				param.setPsNo(SessionUtils.getSessionValue("PerBusNo"));
				param.setBusinessDate(input.getBusinessDate());
				
				nonFaceDBUpdatePhysicalId(param);
				
				result.put("RESULTCODE", "N");
			}
		} catch(Exception e) {
			log.error("신분증 진위확인 Exception 발생");
			log.error(e.getMessage(), e);
			result.put("RESULTCODE", "N");
		} finally {
       		try {
				if (conn != null) { conn.disconnect(); conn = null; }
			} catch ( Exception ignoredException ) {}
		}
		
		return result;
	}
	
	/**
	 * 신분증 촬영 처리
	 * @param input
	 * @return
	 */
	private IdtOcrGetNonFaceInfoResponse prcIdCardShooting(IdtOcrGetNonFaceInfoRequest input) {
		IdtOcrGetNonFaceInfoResponse result = new IdtOcrGetNonFaceInfoResponse();
		
		boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());
		
		//비대면 인증 등록 정보 조회
		SelectNonFaceInfoParameter nonFaceInfoParam = new SelectNonFaceInfoParameter();
		
		nonFaceInfoParam.setPassCode("1111");
		nonFaceInfoParam.setPsNo(StringUtils.defaultString(SessionUtils.getSessionValue("PerBusNo")));
		nonFaceInfoParam.setSeqNo(StringUtils.defaultString(input.getSeqNo()));
		
		SelectNonFaceInfoResult nonFaceInfoResult = nonFaceTblDao.selectNonFaceInfo(nonFaceInfoParam);
		
		if (nonFaceInfoResult != null) {
			
			String calsBusinessDay = CommonBizUtils.getBusinessDay(nonFaceInfoResult.getRegisterDate(), (isReal ? 3 : 5));
			
			result.setCalsBusinessDay(calsBusinessDay);
			result.setProductName(nonFaceInfoResult.getProductName());
			result.setSeqNo(nonFaceInfoResult.getSeqNo());
			result.setPassCode("Y");
		}else {
			result.setCalsBusinessDay("");
			result.setProductName("");
			result.setSeqNo("");
			result.setPassCode("N");
		}
		
		return result;
	}
	
	/**
	 * 신분증 촬영 처리(재촬영)
	 * @param input
	 * @return
	 */
	private IdtOcrGetNonFaceInfoResponse prcIdCardReShooting(IdtOcrGetNonFaceInfoRequest input) {
		IdtOcrGetNonFaceInfoResponse result = new IdtOcrGetNonFaceInfoResponse();
		
		String seqNo = StringUtils.defaultString(input.getSeqNoList());
		String tradNo = StringUtils.defaultString(input.getTradNoList());
		
		result.setPassCode("N");
		
		if (StringUtils.isNotEmpty(seqNo) && StringUtils.isNotEmpty(tradNo)) {
			String seqNoArr[] = seqNo.split(",");
			String tradNoArr[] = tradNo.split(",");
			
			if (seqNoArr.length > 0 && tradNoArr.length > 0) {
				ArrayList<String> seqNoList = new ArrayList<String>();
				ArrayList<String> tradNoList = new ArrayList<String>();
				ArrayList<String> bizDateList = new ArrayList<String>();
				
				int idx = 0;
				
				boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());
				String prdctId;
				String prdctNm;
				String reqBrCd;
				String bizType;
				String jobTypeCd;
				String reqBrNm;
				String registerDate;
				String calsBusinessDay;
				
				List<String> branchCodeList = new ArrayList<String>();
				
				for (String str : tradNoArr) {
					if (seqNoArr.length<idx+1) {
						break;
					} else if (StringUtils.isEmpty(seqNoArr[idx]) || StringUtils.isEmpty(str)) {
						continue;
					}
					
					NonFaceTradInfoResult tradInfoResult = nfTradInfoMgtDao.selectNonFaceTradInfo(tradNo);
					
					tradInfoResult = tradInfoResult == null ? new NonFaceTradInfoResult() : tradInfoResult;
					
					prdctId = StringUtils.defaultIfEmpty(tradInfoResult.getPrdctId(), "1111");
					prdctNm = StringUtils.defaultIfEmpty(tradInfoResult.getPrdctNm(), "기타");
					reqBrCd = StringUtils.defaultIfEmpty(tradInfoResult.getBrnchNo(), "100");
					bizType = StringUtils.defaultString(tradInfoResult.getBizType());
					jobTypeCd = bizType.matches("OPPL|MAPL|NSPL")?"C":"Z";
					reqBrNm = "영업부";
					registerDate = tradInfoResult.getInitRegistDt();
					calsBusinessDay = CommonBizUtils.getBusinessDay(registerDate,isReal?3:5);
					
					branchCodeList = new ArrayList<String>();
					branchCodeList.add(reqBrCd);
					
					//영업점 정보 조회
					List<BranchNameResult> branchInfoList = tblBranchDao.selectBranchName(bizDateList);
					
					if(branchInfoList != null) {
						for(BranchNameResult info : branchInfoList) {
							reqBrNm = info.getBranchName();
						}
					}else {
						reqBrCd = "100";
						reqBrNm = "영업부";
					}
					
					// 비대면 신청 일련번호 조회
					String nowDate = DateUtils.getCurrentDate(DateUtils.YYYYMMDD);
					
					String newSeqNo = nonFaceTblDao.selectNonFaceTblSeqNo(nowDate);
					
					//재촬영 진행건 적재
					NonFaceTblParameter insertNonFaceTblParam = new NonFaceTblParameter();
					
					insertNonFaceTblParam.setSeqNo(newSeqNo);
					insertNonFaceTblParam.setPhone(StringUtils.defaultString(SessionUtils.getSessionValue("HpNum")).replaceAll("\\D", ""));
					insertNonFaceTblParam.setPsno(StringUtils.defaultString(SessionUtils.getSessionValue("PerBusNo")));
					insertNonFaceTblParam.setCusNm(StringUtils.defaultString(SessionUtils.getSessionValue("CustName")));
					insertNonFaceTblParam.setApplyId(nowDate);
					insertNonFaceTblParam.setProductCode(prdctId);
					insertNonFaceTblParam.setProductName(prdctNm);
					insertNonFaceTblParam.setAcctNo("0");
					insertNonFaceTblParam.setBankName("");
					insertNonFaceTblParam.setAnotherAcctNo("0");
					insertNonFaceTblParam.setAgtYn("0");
					insertNonFaceTblParam.setJobTypeCd(jobTypeCd);
					insertNonFaceTblParam.setAssetsFlag("N");
					insertNonFaceTblParam.setDetbFlag("Y");
					insertNonFaceTblParam.setDeposit(new BigDecimal(0));
					insertNonFaceTblParam.setInvest(new BigDecimal(0));
					insertNonFaceTblParam.setProperty(new BigDecimal(0));
					insertNonFaceTblParam.setAssets(new BigDecimal(0));
					insertNonFaceTblParam.setMortgage(new BigDecimal(0));
					insertNonFaceTblParam.setLoan(new BigDecimal(0));
					insertNonFaceTblParam.setDebt(new BigDecimal(0));
					insertNonFaceTblParam.setReqBrCd(reqBrCd);
					insertNonFaceTblParam.setReqBrNm(reqBrNm);
					insertNonFaceTblParam.setPassCode("0000");
					insertNonFaceTblParam.setBusinessDate(calsBusinessDay);
					
					log.debug("비대면정보 최초 등록 param => {}", insertNonFaceTblParam);
					
					nonFaceTblDao.insertNonFaceTbl(insertNonFaceTblParam);
					tradNoList.add(str);
					seqNoList.add(newSeqNo);
					bizDateList.add(calsBusinessDay);
				}
				
				result.setTradNoList(tradNoList);
				result.setSeqNoList(seqNoList);
				result.setBusinessDateList(bizDateList);
				result.setPassCode("Y");
			}
		}
		
		return result;
	}
	
	/**
	 * 알체라 암호화 -> 복호화
	 * @param param
	 * @param key
	 * @return
	 */
	private String alcheraDataDecrypt(String paramData) {
		try {
			return AlcAES256.decrypt(StringUtils.defaultString(paramData));
		} catch (Exception e) {
			return paramData;
		}
	}
	
	private void nonFaceDBUpdatePhysicalId(UpdateNfInfoParameter input) {
		UpdateNfInfoParameter param = new UpdateNfInfoParameter();
		
		param.setPsNo(input.getPsNo());
		param.setPassCode(input.getPassCode());
		param.setSeqNo(input.getSeqNo());
		
		if ("E".equals(input.getErrorCode())) {
			param.setSendYn("3");
			nonFaceTblDao.updateNonFaceImgSendYn(param);
		}else {
			param.setIdCdDsCd(input.getIdCdDsCd());
			param.setIdCdIsuDt(input.getIdCdIsuDt());
			param.setOplicNo(input.getOplicNo());
			param.setPssprtNo(input.getPssprtNo());
			param.setMrtsNo(input.getMrtsNo());
			param.setFileName(input.getFileName());
			param.setBusinessDate(input.getBusinessDate());
			param.setFilePath(input.getFilePath());
			param.setSendYn("1");
			
			param.setCustIssDtChgYn(input.getCustIssDtChgYn());
			param.setCustNmChgYn(input.getCustNmChgYn());
			param.setCustNoChgYn(input.getCustNoChgYn());
			param.setExtractScore(input.getExtractScore());
			param.setFngrInfo(input.getFngrInfo());
			param.setFngrInfoSize("0");
			param.setPhotoInfo(input.getPhotoInfo());
			param.setPhotoInfoSize(input.getPhotoInfoSize());
			param.setIdCdDsImg(input.getIdCdDsImg());
			
			param.setMrtsNoChgYn(input.getMrtsNoChgYn());
			param.setPssprtNoChgYn(input.getPssprtNoChgYn());
			
			nonFaceTblDao.updateNonFaceInfo(param);
		}
	}

	/*******************************************************************************
	* 전각문자로변경
	* CaAPIController.java
	* 역송금발송에서만 사용(공통화X)
	******************************************************************************/
	private String toFullChar(String param) {
		// 입력된 스트링이 null 이면 null 을 리턴
		if (null == param) return null;

		// 변환된 문자들을 쌓아놓을 StringBuffer를 마련한다
		StringBuffer strBuf = new StringBuffer();

		char c = 0;
		int nSrcLength = param.length();

		for (int i = 0; i < nSrcLength; i++) {
			c = param.charAt(i);
			// 영문이거나 특수문자일 경우.
			if( ( c >= 0x21 ) && ( c <= 0x7e ) ) {
				c += 0xfee0;
			}
			// 공백일 경우 
			else if ( c == 0x20 ) { 
				c = 0x3000;
			}

			// 문자열 버퍼에 변환된 문자를 쌓는다
			strBuf.append(c);
		}

		return strBuf.toString();
	}
	
	/**
	 * 안면인식 API 호출
	 * @param input{{@link IdtOcrRequestFaceRecgApiRequest}
	 * @param transactionId
	 * @return
	 */
	private JSONObject callFaceRecgApi(IdtOcrRequestFaceRecgApiRequest input, String transactionId) {
		JSONObject result = new JSONObject();
		
		String currentDay = DateUtils.getCurrentDate(DateUtils.YYYYMMDDHHMMSS);
		
		try {
			kftcFrsApiClient.init();
			
			JSONObject request = new JSONObject();
			
			request.put("transaction_id", transactionId); // 거래고유번호
			request.put("request_datetime", currentDay); // 요청일시
			request.put("org_code", KftcFrsHelper.ORG_CODE); // 참가기관코드
			request.put("customer_channel", "1"); // 고객요청채널 ( 모바일 앱:1 , 모바일 웹:2)
			request.put("id_card_code", input.getIdCardType()); // 신분증 구분코드 ( 주민등록증:1, 운전면허증:2, 여권:3, 외국인등록증:4, 국가보훈등록증:5)
			request.put("selfie_datetime", currentDay); // 셀피촬영일시
			request.put("id_card_data", input.getBase64IdCardData()); // 신분증얼굴사진
			request.put("selfie_data", input.getBase64FacialRecognitionData()); // 셀피사진
			
			log.debug("안면인식 호출 토큰 => {}", kftcFrsApiClient.getAuth().getAccessToken());
			
			FrsHttpRequestEntity requestEntity = FrsHttpRequestEntityBuilder.builder(KftcFrsHelper.FACE_ID)
					.accessToken(kftcFrsApiClient.getAuth().getAccessToken())
					.bodyParameters(request)
					.interceptor(KftcFrsLogInterceptor.client(
							SessionUtils.getSessionValue("UserCifNo"),
							transactionId,
							SessionUtils.getSessionValue("CUST_NO"),
							SessionUtils.getSessionValue("TRAD_NO")))
					.build();
			
			result = kftcFrsApiClient.execute(requestEntity).throwException().getAsJsonObject();
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			String faceRecgTryCnt = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("FACE_RECG_TRY_CNT", String.class), "0");
			sessionManager.setGlobalValue("FACE_RECG_TRY_CNT", Integer.toString(Integer.parseInt(faceRecgTryCnt)+1));
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param selfieData
	 * @param transactionId
	 * @return
	 * @throws Exception 
	 */
	private Map<String,String> fileCopyToXVarmFaceRecg(String selfieData, String transactionId) throws Exception {
		Map<String,String> result = new HashMap<String,String>();
		
		byte sefieByteData[] = StringUtils.defaultString(selfieData).getBytes();
		
		String fileName = transactionId+".jpeg";
		String filePath = PropertiesUtils.getString("FACE_ID_IMAGE_PATH")+fileName;
		
		asysConnectData con = null;
		
		try {
			Path pathToFile = Paths.get(filePath);
			Files.createDirectories(pathToFile.getParent());
			Files.write(pathToFile, sefieByteData);
			
			int ret = -1;
			
			edocXvarmComponent.init();
			con = edocXvarmComponent.getXvarmEngineConnector();
			
			asysUsrElement uePage1 = new asysUsrElement(con);
			uePage1.m_localFile = filePath;
			uePage1.m_cClassId = "SELFIE_CC";
			uePage1.m_descr = "EDOC";
			uePage1.m_userSClass = "SUPER";
			uePage1.m_eClassId = "IMAGE";
			
			ret = uePage1.create("XVARM_MAIN");
			
			if(ret != 0) {
				log.debug("fileCopyToXVarmFaceRecg file Insert Xvarm Exception => {}",ret);
				log.debug("fileCopyToXVarmFaceRecg file Insert Xvarm Exception => {}",uePage1.getLastError());
			}
			
			result.put("filePath", uePage1.m_elementId);
			result.put("fileName", fileName);
		} catch (Exception e) {
			throw e;
		} finally {
			
			if (con != null) {
				con.close();
				con = null;
			}
			
		}
		
		return result;
	}
}
