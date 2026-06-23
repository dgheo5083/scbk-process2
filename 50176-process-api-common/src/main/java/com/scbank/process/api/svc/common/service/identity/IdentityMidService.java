package com.scbank.process.api.svc.common.service.identity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.NonFaceTblDao;
import com.scbank.process.api.svc.common.dao.dto.SelectNonFaceInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectNonFaceInfoResult;
import com.scbank.process.api.svc.common.dao.dto.UpdateNfInfoParameter;
import com.scbank.process.api.svc.common.service.identity.dto.mid.IdtMidConfirmMobileAuthUserInfoRequest;
import com.scbank.process.api.svc.common.service.identity.dto.mid.IdtMidConfirmMobileAuthUserInfoResponse;
import com.scbank.process.api.svc.common.service.identity.dto.mid.IdtMidRequestSpServerProcessRequest;
import com.scbank.process.api.svc.common.service.identity.dto.mid.IdtMidRequestSpServerProcessResponse;
import com.scbank.process.api.svc.shared.components.edoc.EdocServerFactory;
import com.scbank.process.api.svc.shared.components.edoc.EdocXvarmComponent;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/identity/mid", name = "실명확인 - 모바일신분증")
public class IdentityMidService {
	
	private final ISessionContextManager sessionManager;
	
	private final EdocXvarmComponent edocXvarmComponent;
	
	private final NonFaceTblDao nonFaceTblDao;
	
	@ServiceEndpoint(url = "/requestSpServerProcess", name = "SP서버처리 요청 [MA3CMMBIZ012_111S]")
	public IdtMidRequestSpServerProcessResponse requestSpServerProcess(IServiceContext serviceContext,
			IdtMidRequestSpServerProcessRequest request) {
		
		IdtMidRequestSpServerProcessResponse response = getmobileIDCardEdocConnectInfo(request);
		
		String action = request.getAction();
		String successYn = response.getSuccessYn();
		
		if ("requestSPServerAuthResult".equals(action)) {
			if ("Y".equals(successYn)) {
				JSONObject resultInfo = new JSONObject(response.getResultInfo());
				
				String image = response.getIdCardImg();
				String dlno = StringUtils.defaultString(resultInfo.optString("dlno"));
				String rephonno = StringUtils.defaultString(resultInfo.optString("rephonno"));
				
				sessionManager.setGlobalValue("dlno", dlno);
				sessionManager.setGlobalValue("rephonno", rephonno);
				
				//통신에 중요정보 평문 노출 방지 (운전면허번호, 주민등록번호, 모바일신분증이미지)
				resultInfo.put("dlno", ""); // 운전면허번호
				resultInfo.put("rephonno", ""); // 보훈번호
				resultInfo.put("ihidnum", ""); // 주민등록번호, 외국인등록번호
				
				response.setIdCardImg("");
				response.setResultInfo(resultInfo.toString());
				
				String issude = resultInfo.optString("issude");
				byte[] idCardByteData = Base64.decodeBase64(image.getBytes());
				
				String filePath = PropertiesUtils.getString("NON_FACE_IMAGE_PATH"); // 파일 서버 경로
				String fileName = StringUtils.defaultString(request.getSeqNo())+".jpg";
				
				UpdateNfInfoParameter param = new UpdateNfInfoParameter();
				
				param.setSeqNo(request.getSeqNo());
				param.setIdCdDsCd(request.getIdCdDsCd());
				//param.setBusinessDate(request.getBusinessDate());
				param.setIdCdIsuDt(issude);
				param.setOplicNo(dlno);
				param.setMrtsNo(rephonno);
				param.setFileName(fileName);
				param.setFilePath("");
				param.setAction(action);
				
				FileOutputStream fos = null;
				
				try {
					File file = new File(filePath+fileName);
					fos = new FileOutputStream(file);
					fos.write(idCardByteData);
					if(file.isFile()) {
						log.debug("requestSPServerAuthResult 파일경로 => {}",filePath+fileName);
						nonFaceDBUpdateMobileLicense(param);
						/* 2020.03.19 EDOC 전자문서서버 추가로 로직 변경 */
						//TODO fileCopyToXVarm 이미지 전송 추가
						Map<String, String> resultMap = fileCopyToXVarmMobileLicence(param);
						response.setResultCode(resultMap.get("RESULTCODE"));
					}
				} catch (IOException e) {
					//파일 생성시 에러 발생
					param.setErrorCode("E");
					nonFaceDBUpdateMobileLicense(param);
					response.setResultCode("N");
				} finally {
					if(fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
				}
				
			}
		} else if("IdvMobileID".equals(action)) {
			String errorCode = response.getErrorCode();
			
			if("000".equals(errorCode)) {
				String fileName = StringUtils.defaultString(request.getSeqNo())+".jpg";
				
				UpdateNfInfoParameter param = new UpdateNfInfoParameter();
				
				param.setIdCdDsCd(request.getIdCdDsCd());
				//param.setBusinessDate(request.getBusinessDate());
				param.setFileName(fileName);
				param.setFilePath("");
				param.setSeqNo(request.getSeqNo());
				param.setAction(action);
				
				nonFaceDBUpdateMobileLicense(param);
			}
		}
		
		return response;
	}
	
	@ServiceEndpoint(url = "/confirmMobileAuthUserInfo", name = "SP서버인증결과 확인 [MA3CMMBIZ012_113S]")
	public IdtMidConfirmMobileAuthUserInfoResponse confirmMobileAuthUserInfo(IServiceContext serviceContext,
			IdtMidConfirmMobileAuthUserInfoRequest request) {
		
		IdtMidConfirmMobileAuthUserInfoResponse response = new IdtMidConfirmMobileAuthUserInfoResponse();
		
		String perBusNo = SessionUtils.getSessionValue("PerBusNo");
		String custName = SessionUtils.getSessionValue("CustName");
		String mobileNo = sessionManager.getGlobalValue("MobileNo", String.class);
		String mobileNm = sessionManager.getGlobalValue("MobileNm", String.class);
		
		boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());
		
		if ( !isReal || (perBusNo.equals(mobileNo) && custName.equals(mobileNm)) ) {
			//개발에서 정보 비교 스킵
			response.setCompareResult("Y");
			
			SelectNonFaceInfoParameter nonFaceInfoParam = new SelectNonFaceInfoParameter();
			
			nonFaceInfoParam.setPassCode("1111");
			nonFaceInfoParam.setPsNo(perBusNo);
			nonFaceInfoParam.setSeqNo(StringUtils.defaultString(request.getSeqNo()));
			
			SelectNonFaceInfoResult nonFaceInfoResult = nonFaceTblDao.selectNonFaceInfo(nonFaceInfoParam);
			
			String calsBusinessDay = CommonBizUtils.getBusinessDay(nonFaceInfoResult.getRegisterDate(), (isReal ? 3 : 5));
			
			response.setCalsBusinessDay(calsBusinessDay);
			response.setProductName(StringUtils.defaultString(nonFaceInfoResult.getProductName()));
			response.setAcctNo(StringUtils.defaultString(nonFaceInfoResult.getAcctNo()));
			response.setBankName(StringUtils.defaultString(nonFaceInfoResult.getBankName()));
			response.setAnotherAcctNo(StringUtils.defaultString(nonFaceInfoResult.getAnotherAcctNo()));
			response.setSeqNo(StringUtils.defaultString(nonFaceInfoResult.getSeqNo()));
			
		} else {
			response.setCompareResult("N");
		}
		
		return response;
	}
	
	/**
	 * 모바일 운전면허증 EDOC서버 연동
	 * 
	 * @param param
	 */
	private IdtMidRequestSpServerProcessResponse getmobileIDCardEdocConnectInfo(
			IdtMidRequestSpServerProcessRequest request) {
		IdtMidRequestSpServerProcessResponse response = new IdtMidRequestSpServerProcessResponse();

		String action = StringUtils.defaultString(request.getAction());
		String idCdDsCd = StringUtils.defaultIfEmpty(request.getIdCdDsCd(), "12");

		JSONObject jsonData = new JSONObject();

		jsonData.put("action", action);
		jsonData.put("IDCDDSCD", idCdDsCd);

		if ("requestSPServerTrxCode".equals(action)) {
			jsonData.put("SERVICECODE", StringUtils.defaultString(request.getServiceCode()));
			jsonData.put("INTERFACETYPE", StringUtils.defaultString(request.getInterfaceType()));
			jsonData.put("SUBMITMODE", StringUtils.defaultString(request.getSubmitMode()));
			jsonData.put("INCLUDEPROFILE", StringUtils.defaultString(request.getIncludeProfile()));
			jsonData.put("CHANNELCODE", StringUtils.defaultString(request.getChannelCode()));
			jsonData.put("BRANCHCODE", StringUtils.defaultString(request.getBranchCode()));
			jsonData.put("BRANCHNAME", StringUtils.defaultString(request.getBranchName()));
			jsonData.put("EMPLOYEENUMBER", StringUtils.defaultString(request.getEmployeeNumber()));
			jsonData.put("EMPLOYEENAME", StringUtils.defaultString(request.getEmployeeName()));
		} else if ("requestSPServerAuthResult".equals(action)) {
			jsonData.put("TRXCODE", StringUtils.defaultString(request.getTrxCode()));
		} else if ("IdvMobileID".equals(action)) {
			jsonData.put("SEQNO", StringUtils.defaultString(request.getSeqNo()));
			jsonData.put("TLGTYPE", "L");
			jsonData.put("TLGHEADER", "NF");
			jsonData.put("IDCDISUDT", StringUtils.defaultString(request.getIssude()));
			jsonData.put("OPLICNO", StringUtils.defaultString(sessionManager.getGlobalValue("dlno", String.class)));
			jsonData.put("CUSNM", StringUtils.defaultString(request.getName()));
			jsonData.put("IS_NONSAVEIDCD", StringUtils.defaultIfEmpty(request.getIsTruthCheck(), "N"));
		} else if ("requestSPServerCaList".equals(action)) {
			jsonData.put("APP_TYPE", StringUtils.defaultString(request.getAppType()));
		}

		HttpURLConnection conn = null;

		String edocURL = EdocServerFactory.getAvailableEdocServer();
		String edocServerActionURL = PropertiesUtils.getString("EDOC_RELAY_SERVER_URL_ACTION");
		
		log.debug("모바일 신분증 EDOC서버 연동 url => {}",edocURL + edocServerActionURL);
		log.debug("모바일 신분증 action => {}",action);

		URI uri = URI.create(edocURL + edocServerActionURL);

		try {
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

			StringBuilder result = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			}
			
			JSONObject edocResponseData = new JSONObject();

			String jsonString = URLDecoder.decode(result.toString(),"UTF-8");
			
			JSONObject jsonResult = new JSONObject(jsonString);

			JSONObject root = jsonResult.optJSONObject("_msg_");
			
			log.debug("모바일 신분증 EDOC서버 result _msg_ => {}",root);

			JSONObject body = root.optJSONObject("_body_");

			edocResponseData = (body != null) ? body : new JSONObject();
			
			log.debug("모바일 신분증 EDOC서버 result body => {}",body);

			String successYn = (String) edocResponseData.optString("successYN");

			response.setSuccessYn(successYn);
			response.setErrorCode((String) edocResponseData.optString("errorCode"));
			response.setErrorMsg((String) edocResponseData.optString("errorMsg"));

			if ("requestSPServerTrxCode".equals(action)) {
				if ("Y".equals(successYn)) {
					JSONObject profileJSON = edocResponseData.optJSONObject("PROFILEINFO");

					response.setProfile(profileJSON.toString());
					response.setTrxCode(profileJSON.optString("trxcode"));
				}
			} else if ("requestSPServerAuthResult".equals(action)) {
				if ("Y".equals(successYn)) {
					
					JSONObject resultInfoJSON = edocResponseData.optJSONObject("RESULTINFO");

					response.setResultInfo(resultInfoJSON.toString());
					response.setIdCardImg(edocResponseData.optString("IDCARDIMG"));

					sessionManager.setGlobalValue("MobileNo",
							StringUtils.defaultString(resultInfoJSON.optString("ihidnum")));
					sessionManager.setGlobalValue("MobileNm",
							StringUtils.defaultString(resultInfoJSON.optString("name")));
				} else {
					sessionManager.setGlobalValue("MobileNo", "");
					sessionManager.setGlobalValue("MobileNm", "");
				}
			} else if ("IdvMobileID".equals(action)) {
				if ("Y".equals(successYn)) {
					response.setErrorCode("000");
				}
			} else if ("requestSPServerCaList".equals(action)) {
				if ("Y".equals(successYn)) {
					response.setErrorCode("000");
					response.setCaList((String) edocResponseData.optJSONArray(("CALIST")).toString());
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (!"requestSPServerTrxCode".equals(action)) {
				UpdateNfInfoParameter param = new UpdateNfInfoParameter();
				param.setErrorCode("E");
				nonFaceDBUpdateMobileLicense(param);
			}
			response.setErrorCode("N");
		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}
			} catch (Exception ignoredException) {
			}
		}

		return response;
	}
	
	/**
	 * EDOC서버 XVARM에 신분증이미지를 Paceseed 암호화하여 저장한다.
	 * @param param
	 */
	private Map<String, String> fileCopyToXVarmMobileLicence(UpdateNfInfoParameter param) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String srcFileNm = PropertiesUtils.getString("NON_FACE_IMAGE_PATH") + param.getFileName();
		
		log.debug("fileCopyToXVarmMobileLicence 파일 path + name => {}", srcFileNm);
		
		File srcFile = new File(srcFileNm);
		String elementID = "";
		//TODO 이미지 전송 추가
		try {
			edocXvarmComponent.init();
			Map<String,String> edocXvarmSaveData = edocXvarmComponent.create(srcFile, "EDOC_CC", "IDV", true, "jpg");
			
			String errorCode = edocXvarmSaveData.get("errorCode");
			
			if ( errorCode != "0000" ) {
				resultMap.put("RESULTCODE", "N");
			} else {
				elementID = edocXvarmSaveData.get("elementID");
				param.setFilePath(elementID);
				resultMap.put("RESULTCODE", "Y");
				nonFaceDBUpdateMobileLicense(param);
			}
		} catch (Exception e) {
			resultMap.put("RESULTCODE", "N");
		} finally {
			//
			if ( srcFile.isFile() ) {
				srcFile.delete();
				srcFile = null;
			}
		}
		return resultMap;
	}
	
	/**
	 * 비대면인증 정보 수정
	 * @param param
	 */
	private void nonFaceDBUpdateMobileLicense(UpdateNfInfoParameter param) {
		//String filePath = StringUtils.defaultString(param.getFilePath());
		boolean isReal = RunMode.PRD.equals(RuntimeContext.getRunMode());

		SelectNonFaceInfoParameter nonFaceInfoParam = new SelectNonFaceInfoParameter();
		
		nonFaceInfoParam.setPassCode("1111");
		nonFaceInfoParam.setPsNo(SessionUtils.getSessionValue("PerBusNo"));
		nonFaceInfoParam.setSeqNo(param.getSeqNo());
		
		if ( "IdvMobileID".equals(param.getAction()) ) {
			nonFaceInfoParam.setPassCode("Y");
		}
		
		SelectNonFaceInfoResult nonFaceInfoResult = nonFaceTblDao.selectNonFaceInfo(nonFaceInfoParam);

		String calsBusinessDay = CommonBizUtils.getBusinessDay(nonFaceInfoResult.getRegisterDate(), (isReal ? 3 : 5));

		param.setPsNo(SessionUtils.getSessionValue("PerBusNo"));
		param.setPassCode("1111");

		if ("E".equals(param.getErrorCode())) {
			param.setSendYn("3");
			nonFaceTblDao.updateNonFaceImgSendYn(param);
		} else {
			if ("IdvMobileID".equals(param.getAction())) {
				param.setPassCode("Y");
				param.setSendYn("2");
				param.setIdCdDsCd(nonFaceInfoResult.getIdCdDsCd());
				param.setIdCdIsuDt(nonFaceInfoResult.getIdCdIsuDt());
				param.setOplicNo(nonFaceInfoResult.getOplicNo());
				param.setFileName(nonFaceInfoResult.getFileName());
				param.setBusinessDate(nonFaceInfoResult.getBusinessDate());
				param.setFilePath(nonFaceInfoResult.getFilePath());
				param.setCustIssDtChgYn("");
				param.setCustNmChgYn("");
				param.setCustNoChgYn("");
				param.setExtractScore("0");
				param.setFngrInfo("");
				param.setFngrInfoSize("0");
				param.setPhotoInfo("");
				param.setPhotoInfoSize("0");
				param.setMrtsNoChgYn("");
				param.setPssprtNoChgYn("");
				param.setMrtsNo(nonFaceInfoResult.getMrtsNo());
				param.setPssprtNo("");
			} else {
				param.setBusinessDate(calsBusinessDay);
				param.setSendYn("1");

				param.setIdCdDsCd(nonFaceInfoResult.getIdCdDsCd());
				param.setIdCdIsuDt(param.getIdCdIsuDt());

				param.setCustIssDtChgYn("");
				param.setCustNmChgYn("");
				param.setCustNoChgYn("");
				param.setExtractScore("0");
				param.setFngrInfo("");
				param.setFngrInfoSize("0");
				param.setPhotoInfo("");
				param.setPhotoInfoSize("0");
				param.setMrtsNoChgYn("");
				param.setPssprtNoChgYn("");
				param.setMrtsNo(nonFaceInfoResult.getMrtsNo());
				param.setPssprtNo("");
			}

			nonFaceTblDao.updateNonFaceInfo(param);
		}
	}
	
}
