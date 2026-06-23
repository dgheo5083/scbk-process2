package com.scbank.process.api.svc.common.service.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.common.dao.NfEdocFileListMgtDao;
import com.scbank.process.api.svc.common.dao.NfEdocInfoDao;
import com.scbank.process.api.svc.common.dao.NfPrdctEdocDao;
import com.scbank.process.api.svc.common.dao.NfTradInfoMgtDao;
import com.scbank.process.api.svc.common.dao.dto.EdocFileInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocFileInfoResult;
import com.scbank.process.api.svc.common.dao.dto.EdocInfoResult;
import com.scbank.process.api.svc.common.dao.dto.EdocPrdctInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocPrdctInfoResult;
import com.scbank.process.api.svc.common.dao.dto.EdocTradInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocTradInfoResult;
import com.scbank.process.api.svc.common.service.functions.dto.edoc.EdocData;
import com.scbank.process.api.svc.common.service.functions.dto.edoc.FncEdcCreateRequest;
import com.scbank.process.api.svc.common.service.functions.dto.edoc.FncEdcCreateResponse;
import com.scbank.process.api.svc.common.service.functions.dto.edoc.FncEdcViewRequest;
import com.scbank.process.api.svc.common.service.functions.dto.edoc.FncEdcViewResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/functions/edoc", id = "edoc", name = "전자문서", description = "", author = "951303")
public class FunctionsEdocService {

	/**
	 * 전자문서 진행 상품 조회 Dao
	 */
	private final NfTradInfoMgtDao nfTradInfoDao;

	/**
	 * 전자문서 상품 조회 Dao
	 */
	private final NfPrdctEdocDao nfPrdctInfoMgtDao;

	/**
	 * 전자문서 조회 Dao
	 */
	private final NfEdocInfoDao nfEdocInfoDao;

	/**
	 * 전자문서 파일 조회 Dao
	 */
	private final NfEdocFileListMgtDao nfEdocFileListMgtDao;

	@ServiceEndpoint(url = "/view", name = "전자문서 뷰어(데이터 처리)")
	public FncEdcViewResponse view(IServiceContext ctx, FncEdcViewRequest request) {
		FncEdcViewResponse response = new FncEdcViewResponse();

		// String ibReportMode = StringUtils.defaultIfBlank(request.getIbReportMode(),
		// "");
		// String tranType = StringUtils.nvl(request.getTranType(), "");
		// String jsonParam = StringUtils.nvl(input.getString("jsonParam"));
		// String crfFileName = StringUtils.nvl(input.getString("crfFileName"));
		// String pdfFileName = StringUtils.nvl(input.getString("pdfFileName"));
		// String pdfType = StringUtils.nvl(input.getString("pdfType"));
		// String clipDocTitle = StringUtils.nvl(input.getString("clipDocTitle"));
		// String bizType = StringUtils.nvl(input.getString("bizType"));
		// String custNo = StringUtils.nvl(request.getCustNo(), "");
		// String tradNo = StringUtils.nvl(request.getTradNo(), "");
		// String prdctid = StringUtils.nvl(input.getString("prdctid"));
		// String mergePdfSize = StringUtils.nvl(input.getString("mergePdfSize"));

		// 펀드 , 방카만 해당
		// String mappingData = StringUtils.defaultIfEmpty(request.getMappingData(),
		// "");
		// String crfJsonData = StringUtils.defaultIfEmpty(request.getCrfJsonData(),
		// "");
		// 펀드 , 방카만 해당

		// RecordList<JSONObject> resultEdocList = new RecordList<JSONObject>();
		// //EdocList 결과 담을 List(Clip서버가 JSON으로 받아야 하므로 JSONObject 형
		// List<String> edocList = new ArrayList<String>(); // JSONObject형 reflection
		// 미지원

		// 통신가능한 clipUrl 확인
		// ClipServerFactory clipServerFactory = ClipServerFactory.getInstance();
		// String url = clipServerFactory.next(request);
		String clipReportUrl = "https://clipsit.standardchartered.co.kr/clip/";

		// TODO: 대출인 특별케이스 처리 확인 필요 및 대출업무대상 있는지 확인 필요
		// * 직장인중금리 심사대상O , 드림론 심사대상X 케이스가 신규되어 부득이하게 아래 로직을 추가
		// * 직장인중금리(4218) , LOAN_DOC_INVSTGT_FLG = Y -> 드림론순수(128)로 치환
		// * 드림론순수(128) , LOAN_DOC_INVSTGT_FLG = N -> 직장인중금리(4218)로 치환
		// 통합상담업무 추가로인한 실제 LPC 문서체계 판단을 인한 서비스 호출
		// if ("1".equals(tranType)) { //대출인 경우
		// output.setBody("custNo", custNo);
		// output.setBody("tradNo", tradNo);
		// output.setBody("ibReportMode", ibReportMode);

		// SCBKActionData docCodeCheckData = (SCBKActionData) this.delegate(ctx,
		// "MA3CMMEDC001_103S", input);
		// bizType = docCodeCheckData.getString("bizType");
		// prdctid = docCodeCheckData.getString("prdctid");
		// }
		// if (TranType.LOAN.isEquals(tranType)) {
		// View 모드이면서 대출인경우 , PDF Merge Case가 있기때문에 DocCode가 중복되는 문서코드가 존재하는지 체크를
		// 해야한다...
		setEdocViewLoanResponse(request, response);
		// } else {
		// output.setBody("edocCode", edocCode);

		// response.setMappingData(mappingData);
		// response.setCrfJsonData(crfJsonData);
		// }

		// response.setBizType(bizType);
		// response.setTranType(tranType);
		// response.setPdfType(pdfType);
		// response.setBody("crfFileName" , crfFileName);
		// response.setBody("pdfFileName" , pdfFileName);
		// response.setBody("clipDocTitle", clipDocTitle);
		// response.setBody("mergePdfSize", mergePdfSize);

		// response.setEdocList(edocList);
		// output.setBody("LOAN_CLIP_REPORT_URL",
		// PropertiesUtils.getString("LOAN_CLIP_REPORT_URL"));

		List<EdocData> edocDataList = request.getEdocList();
		String custNo = StringUtils.nvl(request.getCustNo(), "");
		String tradNo = StringUtils.nvl(request.getTradNo(), "");
		String bizType = StringUtils.nvl(request.getBizType(), "");
		String prdctId = StringUtils.nvl(request.getPrdctId(), "");
		String clipDocTitle = StringUtils.nvl(request.getClipDocTitle(), "");

		// TODO: ASIS MA3CMMEDC001_103S 임시처리 - 쓰는곳이 많으므로 별도 분리
		// 통합상담 분류분류 체계에 따른 bizType/ 상품ID로 변경
		return_MA3CMMEDC001_103S return_103S = temp_MA3CMMEDC001_103S(
				new params_MA3CMMEDC001_103S(custNo, tradNo, bizType, prdctId));
		bizType = return_103S.bizType;
		prdctId = return_103S.prdctId;

		if (!edocDataList.isEmpty() && prdctId.isBlank()) {
			// prdctId가 없다면 NF_TRADINFO_MGT 테이블 조회하여 상품ID가져오기
			EdocTradInfoParameter parameter = EdocTradInfoParameter.builder()
					.custNo(custNo)
					.tradNo(tradNo)
					.build();

			if (log.isDebugEnabled()) {
				log.debug("FunctionsEdocService > view_loan - parameter : [{}]", parameter);
			}

			EdocTradInfoResult result = nfTradInfoDao.selectEdocTradInfo(parameter);

			if (log.isDebugEnabled()) {
				log.debug("FunctionsEdocService > view_loan - result : [{}]", result);
			}

			prdctId = StringUtils.nvl((String) result.getPrdctId(), "");
		}

		if (prdctId.isBlank()) {
			// TODO: 그래도 없을경우 오류 처리 할지 (ASIS기준으로는 edocDataList에 넘어온 edocCode만 넣어서 다시 반환)
			throw new PRCServiceException("상품정보 조회 오류");
		}

		for (EdocData edocData : edocDataList) {

			String edocCode = edocData.getEdocCode(); // 전자문서 코드

			EdocPrdctInfoParameter edocPrdctInfoParameter = EdocPrdctInfoParameter.builder()
					.edocCd(edocCode)
					.prdctId(prdctId)
					.build();

			if (log.isDebugEnabled()) {
				log.debug("FunctionsEdocService > view_loan - edocPrdctInfoParameter : [{}]",
						edocPrdctInfoParameter);
			}

			// 중복되는 DocCode 조회하여 대상 문서인경우 복구테이블에 저장하고,
			// 문서를 다 확인하고나면 복구테이블에서 데이터를 꺼내서 PDF Merge 요청을 한다.
			List<EdocPrdctInfoResult> edocPrdctInfoResultList = nfPrdctInfoMgtDao
					.selectEdocPrdctInfoList(edocPrdctInfoParameter);
			if (log.isDebugEnabled()) {
				log.debug("FunctionsEdocService > view_loan - edocPrdctInfoResultList : [{}]",
						edocPrdctInfoResultList);
			}

			// Edoc

			// if(!edocPrdctInfoResultList.isEmpty()) {
			// for (EdocPrdctInfoResult prdctInfoResult : edocPrdctInfoResultList) {
			// if(edocCode.equalsIgnoreCase(subEdocCode)) {

			// edocMergeRcvryMap.put("APPLCD_GBN", subDocCode);
			// break;
			// }

			// }
			// }

			// HashMap<String , String> edocMergeRcvryMap = new HashMap<String , String>();

			// edocMergeRcvryMap.put("mappingData", (String)edocMap.get("mappingData"));

			// if(resultNF_PRDCT_EDOC_S_01.size() > 0) {
			// 여기서 EDOC_CD에 따른 DOCCODE를 찾아서 복구테이블에 같이 넣어주자.
			// for (int i = 0; i < resultNF_PRDCT_EDOC_S_01.size(); i++) {
			// HashMap subMap = resultNF_PRDCT_EDOC_S_01.get(i);
			// String subEdocCode = (String) subMap.get("EDOC_CD"); //EDOC_CD
			// String subDocCode = (String) subMap.get("APPLCD_GBN"); //DOC Code
			// String subEdocName = (String) subMap.get("EDOC_NM"); //EDOC_NM
			// if(edocCode.equalsIgnoreCase(subEdocCode)) {
			// edocMergeRcvryMap.put("APPLCD_GBN", subDocCode);
			// break;
			// }

			// PDF Merge를 위한 복구데이터 작업중.
			// String rcvryData = new Gson().toJson(edocMergeRcvryMap);

			// HashMap<String,Object> paramMERGE_RCVRY_EDOC_DATA = new
			// HashMap<String,Object>();
			// paramMERGE_RCVRY_EDOC_DATA.put("CUST_NO" , custNo);
			// paramMERGE_RCVRY_EDOC_DATA.put("TRAD_NO" , tradNo);
			// paramMERGE_RCVRY_EDOC_DATA.put("EDOC_CD" , (String)edocMap.get("edocCode"));
			// paramMERGE_RCVRY_EDOC_DATA.put("RCVRY_MK" , "CLIP_MERGE"); //PDF MERGE 복구데이터
			// paramMERGE_RCVRY_EDOC_DATA.put("RCVRY_DATA" , rcvryData);
			// int result = this.kfbdbDbClient.insert("MA3_CMM_EDC.MERGE_RCVRY_EDOC_DATA",
			// paramMERGE_RCVRY_EDOC_DATA);
			// }

			if (clipDocTitle.equals("")) {
				// 설정한 ClipDocTitle이 없을 경우 edocCode로 문서 제목 조회
				// 다건이면 대표 문서타이틀로 지정한다.
				String edocNm = nfEdocInfoDao.selectEdocNm(edocCode);
				clipDocTitle = edocNm;
			}
		}

		response.setClipReportUrl(clipReportUrl);

		return response;
	}

	@ServiceEndpoint(url = "/create", name = "전자문서 생성")
	public FncEdcCreateResponse create(IServiceContext ctx, FncEdcCreateRequest getEdocRequest) {
		FncEdcCreateResponse response = new FncEdcCreateResponse();

		String edocJsonStr = getEdocRequest.getEdocJsonStr();
		String bizType = getEdocRequest.getBizType();
		String custNo = getEdocRequest.getCustNo();
		String tradNo = getEdocRequest.getTradNo();

		ObjectMapper om = new ObjectMapper();

		List<Map<String, Object>> edocMapList = new ArrayList<Map<String, Object>>();
		try {
			edocMapList = om.readValue(edocJsonStr, new TypeReference<List<Map<String, Object>>>() {
			});
		} catch (Exception e) {
			edocMapList = List.of();
		}

		for (Map<String, Object> edocMap : edocMapList) {
			JSONObject resultObject = new JSONObject();

			// 전자문서 정보 조회
			String edocCd = StringUtils.defaultIfBlank((String) edocMap.get("edocCode"), "");
			EdocInfoResult edocInfoResult = nfEdocInfoDao.selectEdocInfo(edocCd);

			String edocSignYN = String.valueOf(edocInfoResult.getEdocSignYn()); // 전자서명 여부
			String edocTsaYN = String.valueOf(edocInfoResult.getEdocTsaYn()); // TSA 여부
			String edocContractOfferYN = String.valueOf(edocInfoResult.getEdocContractOfferYn()); // 계약서류제공여부

			if (("FHLS".equals(bizType) || "MHLS".equals(bizType)) && !("MA3_FHLS_B16".equals(edocCd))) {
				// 퍼스트홈론 전자서명/TSA여부 전자등기에서처리
				edocSignYN = "N";
				edocTsaYN = "N";
			}

			// edocCode , TRAD_NO , CUST_NO으로 기존에 Xvarm에 쌓인 내용이 있는지 확인한다.
			// 존재한다면 해당값을 Clip 서버에 넘겨줘서 HASH 추출 및 파일 암호화 , Xvarm 저장로직을 SKIP 하도록 진행해야한다.
			EdocFileInfoParameter edocFileInfoParameter = EdocFileInfoParameter.builder()
					.edocCd(edocCd)
					.custNo(custNo)
					.tradNo(tradNo)
					.build();

			EdocFileInfoResult edocFileInfoResult = nfEdocFileListMgtDao.selectEdocFileInfo(edocFileInfoParameter);
			// edocCode , TRAD_NO , CUST_NO으로 기존에 Xvarm에 쌓인 내용이 있는지 확인한다.
			// 존재한다면 해당값을 Clip 서버에 넘겨줘서 HASH 추출 및 파일 암호화 , Xvarm 저장로직을 SKIP 하도록 진행해야한다.

			if (edocFileInfoResult == null) {
				resultObject.put("ELEMENT_ID", "");
				resultObject.put("edoc_replace_yn", "N");
			} else {
				// TODO: IMPORTANT : ELEMENT_ID 라는 필드가 없는데 어떻게 처리해야할지
				// String elementId =
				// StringUtils.defaultString((String)resultNF_EDOC_FILE_LIST.get("ELEMENT_ID") ,
				// "");
				// resultObject.put("ELEMENT_ID" , elementId);
				resultObject.put("ELEMENT_ID", "");
				resultObject.put("edoc_replace_yn", "Y"); // 기존에 있는경우. Xvarm에 Replace 시도함.
			}

			resultObject.put("edocSignYN", edocSignYN);
			resultObject.put("edocTsaYN", edocTsaYN);
			resultObject.put("contractDocFlag", edocContractOfferYN);

			resultObject.put("mappingData", (String) edocMap.get("mappingData"));
			resultObject.put("edocCode", (String) edocMap.get("edocCode"));
			// resultObject.put("crfJsonData", (String)edocMap.get("crfJsonData"));
			resultObject.put("docCode", (String) edocMap.get("docCode"));

			// resultEdocList.add(resultObject);

		}
		return response;
	}

	/*
	 * private static class ClipServerFactory {
	 * 
	 * private int index = 0;
	 * 
	 * private List<String> serverList = new ArrayList<>();
	 * 
	 * private static ClipServerFactory instance;
	 * 
	 * private ClipServerFactory () {
	 * this.init();
	 * }
	 * 
	 * public synchronized static ClipServerFactory getInstance() {
	 * if (instance == null) {
	 * instance = new ClipServerFactory();
	 * }
	 * return instance;
	 * }
	 * 
	 * private void init() {
	 * if (this.serverList == null) {
	 * this.serverList = new ArrayList<>();
	 * }//end if
	 * 
	 * String value = PropertiesUtils.getString("CLIP_REPORT_URL");
	 * String[] array = value.split(",");
	 * 
	 * for(String url : array) {
	 * if (StringUtils.isEmpty(url)) {
	 * continue;
	 * }//end if
	 * this.serverList.add(url);
	 * }//end for
	 * 
	 * }
	 * 
	 * private static final String CLIP_SERVER_URL_KEY = "__clip_server__";
	 * 
	 * private String next(HttpServletRequest request) {
	 * String url = "";
	 * HttpSession session = request.getSession();
	 * if (session != null) {
	 * Object value = session.getAttribute(CLIP_SERVER_URL_KEY);
	 * if (value == null) {
	 * String _url = this.serverList.get(index);
	 * index = (index + 1) % this.serverList.size();
	 * url = _url;
	 * session.setAttribute(CLIP_SERVER_URL_KEY, url);
	 * } else {
	 * url = String.valueOf(value);
	 * }
	 * }
	 * return url;
	 * }
	 * }
	 */

	private void setEdocViewLoanResponse(FncEdcViewRequest request, FncEdcViewResponse response) {
		List<EdocData> edocDataList = request.getEdocList();
		String custNo = StringUtils.nvl(request.getCustNo(), "");
		String tradNo = StringUtils.nvl(request.getTradNo(), "");
		String bizType = StringUtils.nvl(request.getBizType(), "");
		String prdctId = StringUtils.nvl(request.getPrdctId(), "");
		String clipDocTitle = StringUtils.nvl(request.getClipDocTitle(), "");

		response.setClipDocTitle(clipDocTitle);
		response.setCustNo(custNo);
		response.setTradNo(tradNo);
		response.setBizType(bizType);
		response.setPrdctId(prdctId);
		response.setEdocDataList(edocDataList);

		// 메서드 분리
		// output.setBody("edocCode", (String)edocMap.get("edocCode"));
		// output.setBody("mappingData", (String)edocMap.get("mappingData"));
		// output.setBody("crfJsonData", (String)edocMap.get("crfJsonData"));
		// }

	}

	private record params_MA3CMMEDC001_103S(
			String custNo,
			String tradNo,
			String bizType,
			String prdctId) {
	}

	private record return_MA3CMMEDC001_103S(
			String bizType,
			String prdctId) {
	}

	// 통합상담대출용 문서분류체계 판단
	// NF_TRADINFO_MGT 상품ID와 실제 LPC에서 사용하는 문서분류체계가 다를 경우
	// 상품ID , LOAN_DOC_INVSTGT_FLG에 따라 문서분류체계를 결정한다.
	private return_MA3CMMEDC001_103S temp_MA3CMMEDC001_103S(params_MA3CMMEDC001_103S params) {
		String custNo = StringUtils.defaultIfEmpty(params.custNo, "");
		String tradNo = StringUtils.defaultIfEmpty(params.tradNo, "");
		String bizType = ""; // 업무타입
		String prdctId = ""; // 상품ID
		String invstgt_flg = ""; // 서류심사여부

		EdocTradInfoParameter parameter = EdocTradInfoParameter.builder()
				.custNo(custNo)
				.tradNo(tradNo)
				.build();
		if (log.isDebugEnabled()) {
			log.debug("FunctionsEdocService > temp_MA3CMMEDC001_103S - parameter : [{}]", parameter);
		}

		EdocTradInfoResult result = nfTradInfoDao.selectEdocTradInfo(parameter);

		if (log.isDebugEnabled()) {
			log.debug("FunctionsEdocService > temp_MA3CMMEDC001_103S - result : [{}]", result);
		}

		if (result != null) {
			bizType = StringUtils.defaultIfEmpty(result.getBizType(), ""); // 업무타입
			prdctId = StringUtils.defaultIfEmpty(result.getPrdctId(), ""); // 상품ID
			invstgt_flg = StringUtils.defaultIfEmpty(result.getLoanDocInvstgtFlg(), ""); // 심사여부
		} else {
			bizType = StringUtils.defaultIfEmpty(params.bizType, ""); // 업무타입
			prdctId = StringUtils.defaultIfEmpty(params.prdctId, ""); // 상품ID
		}

		String tempProductId = "";
		String tempBizType = "";

		// 드림론이면서 AutoBooking 대상일경우 직장인중금리 문서체계코드를 따라간다.
		if (bizType.equals("CRPL") && invstgt_flg.equals("N")) {
			tempBizType = "SPPL";
			tempProductId = CodeUtils.getCodeValue("PRODUCT_ID", "SPPL");
		}
		// 직장인중금리이면서 AutoBooking 대상이 아닐경우 드림론_순수 문서체계코드를 따라간다.
		else if (bizType.equals("SPPL") && invstgt_flg.equals("Y")) {
			tempBizType = "CRPL";
			tempProductId = CodeUtils.getCodeValue("PRODUCT_ID", "CRPL");
		}
		// 드림론 이벤트인 경우
		else if (bizType.equals("DLEV")) {
			tempBizType = "SPPL";
			tempProductId = CodeUtils.getCodeValue("PRODUCT_ID", "SPPL");
		}
		// One Product이면서 AutoBooking 대상일경우 직장인중금리 문서체계코드를 따라간다.
		else if (bizType.equals("OPPL") && invstgt_flg.equals("N")) {
			tempBizType = "SPPL";
			tempProductId = CodeUtils.getCodeValue("PRODUCT_ID", "SPPL");
		}
		// 새희망홀씨2이면서 AutoBooking 대상이 아닐경우 드림론_순수 문서체계코드를 따라간다.
		else if (bizType.equals("NSPL") && invstgt_flg.equals("Y")) {
			tempBizType = "CRPL";
			tempProductId = CodeUtils.getCodeValue("PRODUCT_ID", "CRPL");
		}

		bizType = StringUtils.defaultIfEmpty(tempBizType, bizType);
		prdctId = StringUtils.defaultIfEmpty(tempProductId, prdctId);

		return new return_MA3CMMEDC001_103S(bizType, prdctId);
	}
}
