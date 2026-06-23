package com.scbank.process.api.svc.common.service.mydata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMyDataRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMyDataResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ValidateGovmDataCertRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ValidateGovmDataCertResponse;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.CertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinTechCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataHelper;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataApiClient;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntityBuilder;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;
import com.wizvera.WizveraConfig;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "마이데이터 > 공공마이데이터", url = "/mydata/public")
public class MydataPublicService {

    @Value("${delfino.config.path:}")
    private String configPath; // delfino.properties

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /*
     * 마이데이터 API 클라이언트
     */
    private final MdcMyDataApiClient mdcMyDataApiClient;

    private final CertUtils certUtils;

    private final FinCertVerifyComponent finCertVerifyComponent;

    private final CertVerifyComponent certVerifyComponent;

    private final FinTechCertVerifyComponent finTechCertVerifyComponent;

    /**
     * 공공꾸러미 API 호출
     * 
     * @param GetMyDataRequest
     * @return GetMyDataResponse
     */
    @Transactional(transactionManager = "kfbdbTransactionManager", rollbackFor = Exception.class)
    @ServiceEndpoint(url = "/getMyData", name = "공공꾸러미 API 호출 [ASIS:MA3PUPAPI001_101S]")
    public GetMyDataResponse getMyData(IServiceContext ctx, GetMyDataRequest input) {

        // TODO. 마이데이터 테스트 추후에 진행.(테스트 데이터 필요)
        // 현재 데이터 형식을 알 수가 없어 테스트 불가.
        GetMyDataResponse output = new GetMyDataResponse();
        String apiType = StringUtils.defaultIfEmpty(input.getApiType(), ""); // api구분
        String apikey = StringUtils.defaultIfEmpty(input.getApiKey(), ""); // apiurl 구분
        JSONObject returnApiParam = new JSONObject();
        log.debug("########### getMyData START :: input : [" + input.toString() + "]  ################");

        // 공공마이데이터 dummy 조회(개발에서만)
        log.debug("#### getMyData RunMode = {}", RuntimeContext.getRunMode());
        if (!RunMode.PRD.equals(RuntimeContext.getRunMode())) {
            String dummyFilePath = StringUtils.defaultIfEmpty(input.getDtarget(), "");
            if (StringUtils.isNotEmpty(dummyFilePath)) {
                // output.setResData(returnApiParam);
                // output.setBody(getMdcDummy(dummyFilePath));
                output = getMdcDummy(URLDecoder.decode(dummyFilePath));
                return output;
            }
        }

        // 업무와 연관된 케이스만 구현
        try {
            switch (apiType) {
                case "1":
                    returnApiParam = registInfoList(input);// 사업자등록증/폐업사실증명/휴업사실증명
                    break;
                case "2":
                    returnApiParam = taxInfo(input, apikey);// 부가가치세과세표준증명
                    break;
                case "3":
                    returnApiParam = registInfo(input);// 사업자등록증명
                    break;
                case "4":
                    returnApiParam = hlthInfo(input, apikey);// 건강보험자격득실확인서
                    break;
                case "5":
                    returnApiParam = mdclList(input, apikey);// 건강장기요양보험료납부확인서(개인|직장가입자)
                    break;
                case "6":
                    returnApiParam = rsdntInfo(input);// 주민등록등초본
                    break;
                case "7":
                    returnApiParam = ntnlInfo(input);// (국세)납세증명서
                    break;
                case "8":
                    returnApiParam = incomelist(input);// 소득금액증명
                    break;
                case "9":
                    // 비과세종합저축 관련 우대사항확인(MDS0007931)
                    returnApiParam = prfrnclCaseCnfrm(input);
                    break;
                case "10":
                    returnApiParam = pensionInfo(input); // 공무원연금공단
                    break;
                default:
                    throw new PRCServiceException("9999", "잘못된 요청입니다.(API REQUEST ERROR)");
            }
        } catch (PRCServiceException e) {
            // log.debug("###### MA3PUPAPI001_101S API SCBKAppException ERROR");
            // e.printStackTrace();
            // throw new PRCServiceException(e.getErrorCode(), e.getErrorMessage());
            String errCode = e.getErrorCode();
            String errMessage = e.getErrorMessage();
            log.debug("###### getMyData APIGubun:[0] Ma30ApplicationException ERROR ");
            e.printStackTrace();
            output.setIsErr("Y");
            output.setRspCode(errCode);
            output.setRspMsg(errMessage);

            log.debug("###### getMyData API ERR output :" + output.toString());
            return output;
        }

        output.setRspCode("200");
        output.setRspMsg("정상");
        output.setResData(returnApiParam.toString());
        log.debug("> > > > > > > > > getMyData output :" + output.toString());

        return output;
    }

    // 사업자등록증/폐업사실증명/휴업사실증명(/sc/regist/infoList)
    private JSONObject registInfoList(GetMyDataRequest input) throws PRCServiceException {

        JSONObject resultApiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String ntv_frnr_se = "L";

        String isForeigner = CommonBizUtils.isForeigner(perBusNo) ? "Y" : "N";

        if ("Y".equals(isForeigner)) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.registInfoList isForeigner : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty("USER_CI_INFO", ""))); // CI값
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면구분
        requestApiParam.put("id_no", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "")); // 주민등록번호
        requestApiParam.put("nm", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l022k001", StringUtils.defaultIfEmpty(input.getL022K001(), "")); // 사업자등록번호앞번호
        requestApiParam.put("l022k002", StringUtils.defaultIfEmpty(input.getL022K002(), "")); // 사업자등록번호중간번호
        requestApiParam.put("l022k003", StringUtils.defaultIfEmpty(input.getL022K003(), "")); // 사업자등록번호뒷번호
        if ("Y".equals(isForeigner)) {
            requestApiParam.put("l022k004", "02"); // 내외국인구분코드
        } else {
            requestApiParam.put("l022k004", "01"); // 내외국인구분코드
        }

        // 개별 비필수 세팅
        requestApiParam.put("l023k001", "");// 사업자등록번호앞번호
        requestApiParam.put("l023k002", "");// 사업자등록번호중간번호
        requestApiParam.put("l023k003", "");// 사업자등록번호뒷번호
        requestApiParam.put("l023k004", "");// 상호
        requestApiParam.put("l023k005", "");// 내외국인구분코드

        requestApiParam.put("l024k001", "");// 사업자등록번호앞번호
        requestApiParam.put("l024k002", "");// 사업자등록번호중간번호
        requestApiParam.put("l024k003", "");// 사업자등록번호뒷번호
        requestApiParam.put("l024k004", "");// 내외국인구분코드

        requestApiParam.put("l030k001", "");// 신청자사업자등록번호
        log.debug(
                "##### getMyData.registInfoList 사업자등록증/폐업사실증명/휴업사실증명 requestApiParam  = [" + requestApiParam.toString()
                        + "]");

        /*
         * 공공데이터 사업자등록증/폐업사실증명/휴업사실증명 API CALL START
         */

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder
                .builder(MdcMyDataHelper.REGISTINFOLIST_DATA) // 사업자등록증/폐업사실증명/휴업사실증명
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### getMyData.registInfoList 사업자등록증/폐업사실증명/휴업사실증명 result  = [" + result.toString() + "]");
        String repCode = result.optString("RSPNS_CD"); // 응답코드
        String repMsg = result.optString("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.debug("##### getMyData.registInfoList API CALL ERROR [1] 사업자등록증/폐업사실증명/휴업사실증명 조회 에러#####");
            log.debug("resCode = " + repCode);
            log.debug("repMsg = " + repMsg);
            // error.code - APICALLERROR[1]
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Object의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apiKey = iterator.next();
                Object apivalue = result.get(apiKey);

                resultApiParam.put(apiKey, apivalue);
            }

            /*
             * 비대면한도제한해지에서는 수기입력이기때문에
             * 사업자등록증OUTPUT의 정보와 진행중인정보를 비교해야함
             * (업무에서비교하기위해 세션적재)
             */
            String job_mk = StringUtils.defaultIfEmpty(input.getJobMk(), "");
            if ("11".equals(job_mk) && resultApiParam.opt("regist_list") != null) {
                sessionManager.setGlobalValue("ldm007_registList", resultApiParam.opt("regist_list"));
            }
        }

        return resultApiParam;
    }

    // 부가가치세과세표준증명(/sc/regist/infoList)
    private JSONObject taxInfo(GetMyDataRequest input, String key) throws PRCServiceException {
        String apiKey = key;// API구분
        JSONObject resultApiParam = new JSONObject();// API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject();// API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String ntv_frnr_se = "L";

        String isForeigner = CommonBizUtils.isForeigner(perBusNo) ? "Y" : "N";

        if ("Y".equals(isForeigner)) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### MA3PUPAPI001_101S taxInfo : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty("USER_CI_INFO", ""))); // CI값
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면구분
        requestApiParam.put("id_no", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "")); // 주민등록번호
        requestApiParam.put("nm", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l025k001", StringUtils.defaultIfEmpty(input.getL025K001(), "")); // 신청사업자등록번호
        requestApiParam.put("l025k002", StringUtils.defaultIfEmpty(input.getL025K002(), "")); // 신청개업일자
        requestApiParam.put("l025k003", StringUtils.defaultIfEmpty(input.getL025K003(), "")); // 신청과세시작년월
        requestApiParam.put("l025k004", StringUtils.defaultIfEmpty(input.getL025K004(), "")); // 신청과세종료년월

        requestApiParam.put("l028k001", StringUtils.defaultIfEmpty(input.getL028K001(), "")); // 신청사업자등록번호
        requestApiParam.put("l028k002", StringUtils.defaultIfEmpty(input.getL028K002(), "")); // 신청개업일자
        requestApiParam.put("l028k003", StringUtils.defaultIfEmpty(input.getL028K003(), "")); // 신청과세시작년월
        requestApiParam.put("l028k004", StringUtils.defaultIfEmpty(input.getL028K004(), "")); // 신청과세종료년월

        log.debug("##### MA3PUPAPI001_101S 부가가치세과세표준증명 requestApiParam  = [" + requestApiParam.toString() + "]");

        /*
         * 공공데이터 부가가치세과세표준증명 API CALL START
         */

        log.debug("##### MA3PUPAPI001_101S apiKey = [" + apiKey + "]");
        String APIGUBUN = "";// :::: 추후 공공데이터 URL생성후 변경
        if ("receiveInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.TAXRECEIVEINFO_DATA; // 부가가치세과세표준증명Receive
        } else if ("loanInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.TAXLOANINFO_DATA; // 부가가치세과세표준증명Loan
        } else {

        }

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(APIGUBUN)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### MA3PUPAPI001_101S 부가가치세과세표준증명 result  = [" + result.toString() + "]");
        String repCode = (String) result.get("RSPNS_CD"); // 응답코드
        String repMsg = (String) result.get("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.info("##### MA3PUPAPI001_101S API CALL ERROR [1] 부가가치세과세표준증명 조회 에러#####");
            log.info("resCode = " + repCode);
            log.info("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }
        }

        return resultApiParam;
    }

    // 사업자등록증명(/sc/cert/certInfo)
    private JSONObject registInfo(GetMyDataRequest input) throws PRCServiceException {
        JSONObject resultApiParam = new JSONObject();// API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject();// API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String ntv_frnr_se = "L";

        String isForeigner = CommonBizUtils.isForeigner(perBusNo) ? "Y" : "N";

        if ("Y".equals(isForeigner)) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### MA3PUPAPI001_101S taxInfo : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty("USER_CI_INFO", ""))); // CI값
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면구분
        requestApiParam.put("id_no", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "")); // 주민등록번호
        requestApiParam.put("nm", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l022k001", StringUtils.defaultIfEmpty(input.getL022K001(), ""));// 사업자등록번호앞번호
        requestApiParam.put("l022k002", StringUtils.defaultIfEmpty(input.getL022K002(), ""));// 사업자등록번호중간번호
        requestApiParam.put("l022k003", StringUtils.defaultIfEmpty(input.getL022K003(), ""));// 사업자등록번호뒷번호
        if ("Y".equals(isForeigner)) {
            requestApiParam.put("l022k004", "02");// 내외국인구분코드
        } else {
            requestApiParam.put("l022k004", "01");// 내외국인구분코드
        }

        // 개별 비필수 세팅
        requestApiParam.put("l023k001", "");// 사업자등록번호앞번호
        requestApiParam.put("l023k002", "");// 사업자등록번호중간번호
        requestApiParam.put("l023k003", "");// 사업자등록번호뒷번호
        requestApiParam.put("l023k004", "");// 상호
        requestApiParam.put("l023k005", "");// 내외국인구분코드

        requestApiParam.put("l024k001", "");// 사업자등록번호앞번호
        requestApiParam.put("l024k002", "");// 사업자등록번호중간번호
        requestApiParam.put("l024k003", "");// 사업자등록번호뒷번호
        requestApiParam.put("l024k004", "");// 내외국인구분코드

        requestApiParam.put("l030k001", "");// 신청자사업자등록번호

        log.debug("##### MA3PUPAPI001_101S 사업자등록증명 requestApiParam  = [" + requestApiParam.toString() + "]");

        /*
         * 공공데이터 사업자등록증명 API CALL START
         */
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.CERTINFO_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### MA3PUPAPI001_101S 사업자등록증명 result  = [" + result.toString() + "]");
        String repCode = (String) result.get("RSPNS_CD"); // 응답코드
        String repMsg = (String) result.get("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.info("##### MA3PUPAPI001_101S API CALL ERROR [1] 사업자등록증명 조회 에러#####");
            log.info("resCode = " + repCode);
            log.info("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }
        }

        return resultApiParam;
    }

    // 건강보험자격득실확인서(/sc/hlth/hlthReceiveInfo, /sc/hlth/hlthCardInfo,
    // /sc/hlth/hlthLoanInfo)
    private JSONObject hlthInfo(GetMyDataRequest input, String key) throws PRCServiceException {

        String apiKey = key; // API 구분
        JSONObject resultApiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String ntv_frnr_se = "L";

        String isForeigner = CommonBizUtils.isForeigner(perBusNo) ? "Y" : "N";

        if ("Y".equals(isForeigner)) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.hlthInfo isForeigner : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI 값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "")); // 주민등록번호
        requestApiParam.put("nm", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l006k001", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("l006k002",
                StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo").substring(0, 6), "")); // 주민번호앞자리
        requestApiParam.put("l006k003",
                StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo").substring(6, 13), "")); // 주민번호뒷자리

        // 개별 비필수 세팅
        requestApiParam.put("l007k001", "");// 성명
        requestApiParam.put("l007k002", "");// 주민번호앞자리
        requestApiParam.put("l007k003", "");// 주민번호뒷자리

        log.debug("##### getMyData.hlthInfo 건강보험자격득실확인서 requestApiParam  = [" + requestApiParam.toString() + "]");

        /*
         * 공공데이터 건강보험자격득실확인서 API CALL START
         */

        log.debug("##### getMyData.hlthInfo apiKey = [" + apiKey + "]");
        String APIGUBUN = "";
        if ("receiveInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.HLTHRECEIVEINFO_DATA; // 건강보험자격득실확인서Receive
        } else if ("loanInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.HLTHLOANINFO_DATA; // 건강보험자격득실확인서Loan
        } else if ("cardInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.HLTHCARDINFO_DATA; // 건강보험자격득실확인서Card
        } else {

        }
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(APIGUBUN)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();

        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### getMyData.hlthInfo 건강보험자격득실확인서 result  = [" + result.toString() + "]");
        JSONObject hlth_info = result.getJSONObject("hlth_info");

        String repCode = result.optString("RSPNS_CD"); // 응답코드
        String repMsg = result.optString("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.debug("##### getMyData.hlthInfo API CALL ERROR [1] 건강보험자격득실확인서 조회 에러#####");
            log.debug("resCode = " + repCode);
            log.debug("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Object의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }

            log.debug("##### getMyData.hlthInfo 건강보험자격득실확인서 resultApiParam  = [" + resultApiParam.toString() + "]");
            // 비고
            String PROC_RESULT = StringUtils.defaultIfEmpty(
                    (hlth_info.optString("PROC_RESULT") != null ? String.valueOf(hlth_info.optString("PROC_RESULT"))
                            : ""),
                    "");
            log.debug("##### getMyData.hlthInfo 건강보험자격득실확인서 PROC_RESULT  = [" + PROC_RESULT + "]");

            if ("".equalsIgnoreCase(PROC_RESULT)) {
                resultApiParam.put("PROC_RESULT_FLAG", "Y");
            } else {
                resultApiParam.put("PROC_RESULT_FLAG", "N");
            }
        }
        return resultApiParam;
    }

    // 건강장기요양보험료납부확인서(개인|직장가입자)(/sc/mdcl/mdclReceiveList, /sc/mdcl/mdclCardList,
    // /sc/mdcl/mdclLoanList)
    private JSONObject mdclList(GetMyDataRequest input, String key) throws PRCServiceException {

        String apiKey = key; // API구분
        JSONObject resultApiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String ntv_frnr_se = "L";

        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);

        if (isForeigner) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.mdclList isForeigner : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "")); // 주민등록번호
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l008k001", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("l008k002",
                StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo").substring(0, 6), "")); // 주민등록번호앞번호
        requestApiParam.put("l008k003",
                StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo").substring(6, 13), "")); // 주민등록번호뒷번호
        requestApiParam.put("l008k004", StringUtils.defaultIfEmpty(input.getL008K004(), "")); // 조회년도
        requestApiParam.put("l008k005", StringUtils.defaultIfEmpty(input.getL008K005(), "")); // 업무구분
        requestApiParam.put("l008k006", StringUtils.defaultIfEmpty(input.getL008K006(), "")); // 보험구분

        // 개별 비필수 세팅
        requestApiParam.put("l010k001", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "")); // 성명
        requestApiParam.put("l010k002",
                StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo").subSequence(0, 6), "")); // 주민등록번호앞번호
        requestApiParam.put("l010k003",
                StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo").substring(6, 13), "")); // 주민등록번호뒷번호
        requestApiParam.put("l010k004", StringUtils.defaultIfEmpty(input.getL008K004(), "")); // 조회년도
        requestApiParam.put("l010k005", StringUtils.defaultIfEmpty(input.getL008K005(), "")); // 업무구분
        requestApiParam.put("l010k006", StringUtils.defaultIfEmpty(input.getL008K006(), "")); // 보험구분

        log.debug("##### getMyData.mdclList 건강장기요양보험료납부확인서 requestApiParam  = [" + requestApiParam.toString() + "]");

        /*
         * 공공데이터 건강장기요양보험료납부확인서 API CALL START
         */

        log.debug("##### getMyData.mdclList apiKey = [" + apiKey + "]");
        String APIGUBUN = "";
        if ("receiveInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.MDCLRECEIVELIST_DATA; // 건강보험납부확인서Receive
        } else if ("loanInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.MDCLLOANLIST_DATA; // 건강보험납부확인서Loan
        } else if ("cardInfo".equals(apiKey)) {
            APIGUBUN = MdcMyDataHelper.MDCLCARDLIST_DATA; // 건강보험납부확인서Card
        } else {

        }
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(APIGUBUN)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### getMyData.mdclList 건강장기요양보험료납부확인서 result  = [" + result.toString() + "]");
        String repCode = result.optString("RSPNS_CD"); // 응답코드
        String repMsg = result.optString("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.debug("##### getMyData.mdclList API CALL ERROR [1] 건강장기요양보험료납부확인서 조회 에러#####");
            log.debug("resCode = " + repCode);
            log.debug("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Object의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }

            log.debug("##### getMyData.mdclList 건강장기요양보험료납부확인서 resultApiParam  = [" + resultApiParam.toString() + "]");
            // 비고
            // 건강장기요양보험료납부확인서(개인)_비고
            String L008_PROC_RESULT = StringUtils.defaultIfEmpty(
                    (result.optString("L008_PROC_RESULT") != null ? String.valueOf(result.optString("L008_PROC_RESULT"))
                            : ""),
                    "");
            // 건강장기요양보험료납부확인서(직장)_비고
            String L010_PROC_RESULT = StringUtils.defaultIfEmpty(
                    (result.optString("L010_PROC_RESULT") != null ? String.valueOf(result.optString("L010_PROC_RESULT"))
                            : ""),
                    "");
            log.debug("##### getMyData.mdclList 건강장기요양보험료납부확인서 L008_PROC_RESULT  = [" + L008_PROC_RESULT + "]");
            log.debug("##### getMyData.mdclList 건강장기요양보험료납부확인서 L010_PROC_RESULT  = [" + L010_PROC_RESULT + "]");

            if ("".equalsIgnoreCase(L008_PROC_RESULT)) {
                resultApiParam.put("L008_PROC_RESULT", "Y");
            } else {
                resultApiParam.put("L008_PROC_RESULT", "N");
            }

            if ("".equalsIgnoreCase(L010_PROC_RESULT)) {
                resultApiParam.put("L010_PROC_RESULT", "Y");
            } else {
                resultApiParam.put("L010_PROC_RESULT", "N");

            }

        }

        return resultApiParam;
    }

    // 주민등록등초본(PRFRNCLCASECNFRM_DATA - MDS0001617)
    private JSONObject rsdntInfo(GetMyDataRequest input) throws PRCServiceException {
        JSONObject resultApiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String custName = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "");
        String ntv_frnr_se = "L";

        String perBusNo01 = "";
        String perBusNo02 = "";

        if (!"".equalsIgnoreCase(perBusNo)) {
            perBusNo01 = perBusNo.substring(0, 6);
            perBusNo02 = perBusNo.substring(6, 13);
        }

        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);
        if (isForeigner) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.rsdntInfo isForeigner : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI 값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));

        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", perBusNo); // 주민등록번호
        requestApiParam.put("nm", custName); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l001k001", StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), ""));// 성명
        requestApiParam.put("l001k002", perBusNo01);// 주민등록번호앞번호
        requestApiParam.put("l001k003", perBusNo02);// 주민등록번호뒷번호

        // 2022.05.31 주민등록등초본 거소신고번호 input값 추가
        requestApiParam.put("l012k003", perBusNo01);// 거소신고번호앞번호
        requestApiParam.put("l012k004", perBusNo02);// 거소신고번호뒷번호

        // 개별 비필수 세팅
        requestApiParam.put("l045k001", ""); // 기록대조시작일년
        requestApiParam.put("l045k002", ""); // 기록대조시작일월
        requestApiParam.put("l045k003", ""); // 기록대조시작일일
        requestApiParam.put("l045k004", ""); // 성명
        requestApiParam.put("l045k005", ""); // 주민등록번호앞번호
        requestApiParam.put("l045k006", ""); // 주민등록번호뒷번호
        requestApiParam.put("l045k007", ""); // 남북왕래
        requestApiParam.put("l045k008", ""); // 선원기록
        requestApiParam.put("l100k001", ""); // 성명
        requestApiParam.put("l100k002", ""); // 주민등록번호앞번호
        requestApiParam.put("l100k003", ""); // 주민등록번호뒷번호

        /**
         * 공공데이터 주민등록등초본 API CALL START
         */
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.RSDNTINFO_DATA) // 주민등록초본
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### getMyData.rsdntInfo 주민등록등초본 result  = [" + result.toString() + "]");
        String repCode = result.optString("RSPNS_CD"); // 응답코드
        String repMsg = result.optString("RSPNS"); // 응답메세지
        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.debug("##### MA3PUPAPI001_101S API CALL ERROR [1] 주민등록등초본 조회 에러#####");
            log.debug("resCode = " + repCode);
            log.debug("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }
        }

        return resultApiParam;
    }

    // (국세)납세증명서(/sc/ntnl/ntnlInfo)
    private JSONObject ntnlInfo(GetMyDataRequest input) throws PRCServiceException {
        JSONObject resultApiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String custName = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "");
        String ntv_frnr_se = "L";

        String perBusNo01 = "";
        String perBusNo02 = "";

        if (!"".equalsIgnoreCase(perBusNo)) {
            perBusNo01 = perBusNo.substring(0, 6);
            perBusNo02 = perBusNo.substring(6, 13);
        }

        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);
        if (isForeigner) {
            ntv_frnr_se = "F"; // 외국인
        }

        log.debug("########### getMyData.ntnlInfo isForeigner : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI 값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));

        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", perBusNo); // 주민등록번호
        requestApiParam.put("nm", custName); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅
        requestApiParam.put("l021k001", StringUtils.defaultIfEmpty(input.getL021K001(), ""));// 신청인정보사업자구분
        requestApiParam.put("l021k003", custName);// 성명
        requestApiParam.put("l021k004", perBusNo01);// 주민등록번호1
        requestApiParam.put("l021k005", perBusNo02);// 주민등록번호2
        requestApiParam.put("l021k006", StringUtils.defaultIfEmpty(input.getL021K006(), ""));// 사업자등록번호1
        requestApiParam.put("l021k007", StringUtils.defaultIfEmpty(input.getL021K007(), ""));// 사업자등록번호2
        requestApiParam.put("l021k008", StringUtils.defaultIfEmpty(input.getL021K008(), ""));// 사업자등록번호3
        requestApiParam.put("l021k009", StringUtils.defaultIfEmpty(input.getL021K009(), ""));// 식별번호 구분
        requestApiParam.put("l021k010", StringUtils.defaultIfEmpty(input.getL021K010(), ""));// 개인식별자
        if (isForeigner) {
            requestApiParam.put("l021k002", "9");// 내외국인구분코드
        } else {
            requestApiParam.put("l021k002", "1");// 내외국인구분코드
        }

        // 개별 비필수 세팅
        requestApiParam.put("l002k001", "");// 개인성명
        requestApiParam.put("l002k002", "");// 주민번호앞자리
        requestApiParam.put("l002k003", "");// 주민번호뒷자리
        requestApiParam.put("l002k009", "");// 대상년도시작
        requestApiParam.put("l002k010", "");// 대상년도종료
        requestApiParam.put("l002k011", "");// 기관코드
        requestApiParam.put("l002k012", "");// 기관명

        requestApiParam.put("l014k001", "");// 신청자구분코드
        requestApiParam.put("l014k003", "");// 법인명
        requestApiParam.put("l014k004", "");// 성명
        requestApiParam.put("l014k005", "");// 주민등록번호앞번호
        requestApiParam.put("l014k006", "");// 주민등록번호뒷번호
        requestApiParam.put("l014k007", "");// 법인번호앞번호
        requestApiParam.put("l014k008", "");// 법인번호뒷번호
        requestApiParam.put("l014k009", "");// 외국인등록번호
        requestApiParam.put("l014k010", "");// 등록번호
        requestApiParam.put("l014k002", "");// 내외국인구분코드

        requestApiParam.put("l035k001", "");// 신청자구분코드
        requestApiParam.put("l035k002", "");// 법인명
        requestApiParam.put("l035k003", "");// 법인번호앞번호
        requestApiParam.put("l035k004", "");// 법인번호뒷번호
        requestApiParam.put("l035k005", "");// 성명
        requestApiParam.put("l035k006", "");// 주민등록번호앞번호
        requestApiParam.put("l035k007", "");// 주민등록번호뒷번호
        requestApiParam.put("l035k008", "");// 등록번호

        log.debug("##### MA3PUPAPI001_101S (국세)납세증명서 requestApiParam  = [" + requestApiParam.toString() + "]");

        /*
         * 공공데이터 (국세)납세증명서 API CALL START
         */

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.NTNLINFO_DATA) // 납세증명서
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### MA3PUPAPI001_101S (국세)납세증명서 result  = [" + result.toString() + "]");
        String repCode = (String) result.get("RSPNS_CD"); // 응답코드
        String repMsg = (String) result.get("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.info("##### MA3PUPAPI001_101S API CALL ERROR [1] (국세)납세증명서 조회 에러#####");
            log.info("resCode = " + repCode);
            log.info("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }
        }

        return resultApiParam;
    }

    // 소득금액증명(PRFRNCLCASECNFRM_DATA - MDS0002427)
    private JSONObject incomelist(GetMyDataRequest input) throws PRCServiceException {
        JSONObject resultApiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String custName = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "");
        String ntv_frnr_se = "L";

        String perBusNo01 = "";
        String perBusNo02 = "";

        if (!"".equalsIgnoreCase(perBusNo)) {
            perBusNo01 = perBusNo.substring(0, 6);
            perBusNo02 = perBusNo.substring(6, 13);
        }

        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);
        if (isForeigner) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.incomelist isForeigner : [" + ntv_frnr_se + "]  ################");

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI 값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));

        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", perBusNo); // 주민등록번호
        requestApiParam.put("nm", custName); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 요청데이터 set 개별
        // 개별 필수 세팅 20220922 소득금액증명 문서 변경 1760 -> 2427
        requestApiParam.put("l170k001", custName);// 성명
        requestApiParam.put("l170k002", perBusNo01);// 주민등록번호앞번호
        requestApiParam.put("l170k003", perBusNo02);// 주민등록번호뒷번호
        requestApiParam.put("l170k004", StringUtils.defaultIfEmpty(input.getL170K004(), ""));// 과세기간시작연도
        requestApiParam.put("l170k005", StringUtils.defaultIfEmpty(input.getL170K005(), ""));// 과세기간종료연도
        // requestApiParam.put("l005k011",StringUtils.defaultIfEmpty(input.getString("L005K011"),
        // ""));//증명서구분코드

        // 개별 비필수 세팅
        requestApiParam.put("l026k002", "");// 신청사업자등록번호
        requestApiParam.put("l026k003", "");// 신청귀속연월
        requestApiParam.put("l026k005", "");// 신청소득구분
        requestApiParam.put("l040k001", "");// 조회은행명
        requestApiParam.put("l040k002", "");// 조회예금주명
        requestApiParam.put("l040k003", "");// 조회계좌번호

        /**
         * 공공데이터 소득금액증명 API CALL START
         */
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.INCOMELIST_DATA) // 소득금액증명
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### getMyData.rsdntInfo 소득금액증명 result  = [" + result.toString() + "]");
        String repCode = result.optString("RSPNS_CD"); // 응답코드
        String repMsg = result.optString("RSPNS"); // 응답메세지
        JSONObject income_list = (JSONObject) result.get("income_list");

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.debug("##### MA3PUPAPI001_101S API CALL ERROR [1] 주민등록등초본 조회 에러#####");
            log.debug("resCode = " + repCode);
            log.debug("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultApiParam.put(apikey, apivalue);
            }

            log.debug("##### MA3PUPAPI001_101S 소득금액증명 resultApiParam  = [" + resultApiParam.toString() + "]");
            // 비고
            String PROC_RESULT = StringUtils.defaultIfEmpty(
                    (income_list.get("PROC_RESULT") != null ? String.valueOf(income_list.get("PROC_RESULT")) : ""), "");
            log.debug("##### MA3PUPAPI001_101S 소득금액증명 PROC_RESULT  = [" + PROC_RESULT + "]");

            if ("".equalsIgnoreCase(PROC_RESULT)) {
                resultApiParam.put("PROC_RESULT_FLAG", "Y");
            } else {
                resultApiParam.put("PROC_RESULT_FLAG", "N");
            }
        }
        return resultApiParam;
    }

    // 비과세종합저축 관련 우대사항확인(PRFRNCLCASECNFRM_DATA - MDS0007931)
    private JSONObject prfrnclCaseCnfrm(GetMyDataRequest input) throws PRCServiceException {

        JSONObject resultAPiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        String UserID = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("UserID"), "");

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String custName = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "");
        String ntv_frnr_se = "L";

        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);

        if (isForeigner) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.prfrnclCaseCnfrm isForeigner : [" + ntv_frnr_se + "]  ################");

        String perBusNo01 = "";
        String perBusNo02 = "";

        if (!"".equalsIgnoreCase(perBusNo)) {
            perBusNo01 = perBusNo.substring(0, 6);
            perBusNo02 = perBusNo.substring(6, 13);
        }

        String l_srch_type = StringUtils.defaultIfEmpty(input.getLsrchType(), ""); // 우대사항확인 구분값

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI 값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));

        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", perBusNo); // 주민등록번호
        requestApiParam.put("nm", custName); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 개별 비필수 세팅
        if ("4".equalsIgnoreCase(l_srch_type)) { // 국민기초생활수급자증명서
            requestApiParam.put("l015k001", perBusNo01); // L015 국민기초생활수급자증명서 - 주민등록번호앞번호(6자리)
            requestApiParam.put("l015k002", perBusNo02); // L015 국민기초생활수급자증명서 - 주민등록번호뒷번호(7자리)
            requestApiParam.put("l015k003", custName); // L015 국민기초생활수급자증명서 - 성명(20자리)
        } else if ("2".equalsIgnoreCase(l_srch_type)) { // 장애인증명서
            requestApiParam.put("l016k001", custName); // L016 장애인증명서 - 성명(20자리)
            requestApiParam.put("l016k002", perBusNo01); // L016 장애인증명서 - 주민등록번호앞번호(6자리)
            requestApiParam.put("l016k003", perBusNo02); // L016 장애인증명서 - 주민등록번호뒷번호(7자리)
        } else if ("3".equalsIgnoreCase(l_srch_type) || "7".equalsIgnoreCase(l_srch_type)) { // 국가유공자유족확인
            // L398 국가유공자유족확인 - 보훈번호
            requestApiParam.put("l398k001", StringUtils.defaultIfEmpty(input.getL398K001(), ""));
            requestApiParam.put("l398k002", perBusNo); // L398 국가유공자유족확인 - 주민등록번호(13자리)
            requestApiParam.put("l398k003", custName); // L398 국가유공자유족확인 - 성명(20자리)
        } else if ("A".equalsIgnoreCase(l_srch_type)) { // 전체조회
            requestApiParam.put("l015k001", perBusNo01); // L015 국민기초생활수급자증명서 - 주민등록번호앞번호(6자리)
            requestApiParam.put("l015k002", perBusNo02); // L015 국민기초생활수급자증명서 - 주민등록번호뒷번호(7자리)
            requestApiParam.put("l015k003", custName); // L015 국민기초생활수급자증명서 - 성명(20자리)
            requestApiParam.put("l016k001", custName); // L016 장애인증명서 - 성명(20자리)
            requestApiParam.put("l016k002", perBusNo01); // L016 장애인증명서 - 주민등록번호앞번호(6자리)
            requestApiParam.put("l016k003", perBusNo02); // L016 장애인증명서 - 주민등록번호뒷번호(7자리)
            // L398 국가유공자유족확인 - 보훈번호
            requestApiParam.put("l398k001", StringUtils.defaultIfEmpty(input.getL398K001(), ""));
            requestApiParam.put("l398k002", perBusNo); // L398 국가유공자유족확인 - 주민등록번호(13자리)
            requestApiParam.put("l398k003", custName); // L398 국가유공자유족확인 - 성명(20자리)
        }

        log.debug("##### getMyData.prfrnclCaseCnfrm 비과세종합저축 우대사항확인 requestApiParam  = [" + requestApiParam.toString()
                + "]");

        /**
         * 공공데이터 비과세종합저축 우대사항확인 API CALL START
         */
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.PRFRNCLCASECNFRM) // 소득금액증명
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### getMyData.prfrnclCaseCnfrm 비과세종합저축 우대사항확인 result  = [" + result.toString() + "]");
        String repCode = result.optString("RSPNS_CD"); // 응답코드
        String repMsg = result.optString("RSPNS"); // 응답메세지

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.debug("##### MA3PUPAPI001_101S API CALL ERROR [1] 소득금액증명 조회 에러#####");
            log.debug("resCode = " + repCode);
            log.debug("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {
            String PROC_RESULT = "";

            if ("4".equalsIgnoreCase(l_srch_type)) { // 국민기초생활수급자증명서
                resultAPiParam = result.optJSONObject("life_supply_info");
                PROC_RESULT = StringUtils.defaultIfEmpty(
                        (resultAPiParam.optString("PROC_RESULT") != null
                                ? String.valueOf(requestApiParam.optString("PROC_RESULT"))
                                : ""),
                        "");
            } else if ("2".equalsIgnoreCase(l_srch_type)) { // 장애인증명서
                requestApiParam = result.optJSONObject("defect_proof_info");
                PROC_RESULT = StringUtils.defaultIfEmpty(
                        (requestApiParam.optString("PROC_RESULT") != null
                                ? String.valueOf(resultAPiParam.optString("PROC_RESULT"))
                                : ""),
                        "");
            } else if ("3".equalsIgnoreCase(l_srch_type) || "7".equalsIgnoreCase(l_srch_type)) { // 국가유공자확인
                resultAPiParam = result.optJSONObject("national_merit_info");
                PROC_RESULT = StringUtils.defaultIfEmpty(
                        (requestApiParam.optString("PROC_RESULT") != null
                                ? String.valueOf(resultAPiParam.optString("PROC_RESULT"))
                                : ""),
                        "");
            } else if ("A".equalsIgnoreCase(l_srch_type)) { // 전체조회
                // JSON Object의 키 밸류 뽑아서 output에 저장
                Iterator<String> iterator = result.keySet().iterator();
                while (iterator.hasNext()) {
                    String apiKey = iterator.next();
                    Object apivalue = result.get(apiKey);
                    resultAPiParam.put(apiKey, apivalue);
                }
            }

            log.debug("##### MA3PUPAPI001_101S 비과세종합저축 우대사항확인 l_srch_type  = [" + l_srch_type + "]");
            log.debug("##### MA3PUPAPI001_101S 비과세종합저축 우대사항확인 PROC_RESULT  = [" + PROC_RESULT + "]");

            if (!"A".equalsIgnoreCase(l_srch_type)) {
                resultAPiParam.put("RSPNS_CD", repCode);
                resultAPiParam.put("RSPNS", repMsg);

                PROC_RESULT = PROC_RESULT.trim();
                if ("".equalsIgnoreCase(PROC_RESULT)) {
                    resultAPiParam.put("PROC_RESULT_FLAG", "Y");
                } else {
                    resultAPiParam.put("PROC_RESULT_FLAG", "N");
                }
            }

            log.debug("##### MA3PUPAPI001_101S 비과세종합저축 우대사항확인 resultAPiParam  = [" + resultAPiParam.toString() + "]");

            sessionManager.setGlobalValue(UserID, resultAPiParam.toString());
        }

        return resultAPiParam;
    }

    // 공무원 연금 공단
    private JSONObject pensionInfo(GetMyDataRequest input) throws PRCServiceException {
        JSONObject resultAPiParam = new JSONObject(); // API OUTPUT DATA
        JSONObject requestApiParam = new JSONObject(); // API INPUT DATA

        String UserID = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("UserID"), "");

        // 외국인 체크
        String perBusNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), "");
        String custName = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName"), "");
        String ntv_frnr_se = "L";

        boolean isForeigner = CommonBizUtils.isForeigner(perBusNo);

        if (isForeigner) {
            ntv_frnr_se = "F"; // 외국인
        }
        log.debug("########### getMyData.pensionInfo isForeigner : [" + ntv_frnr_se + "]  ################");

        String perBusNo01 = "";
        String perBusNo02 = "";

        if (!"".equalsIgnoreCase(perBusNo)) {
            perBusNo01 = perBusNo.substring(0, 6);
            perBusNo02 = perBusNo.substring(6, 13);
        }

        // 요청데이터 set 공통
        requestApiParam.put("srvc", StringUtils.defaultIfEmpty(input.getSrvc(), "")); // 서비스 ID
        // CI 값
        requestApiParam.put("ci",
                StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("USER_CI_INFO", String.class),
                        StringUtils.defaultIfEmpty(sessionManager.getLoginValue("USER_CI_INFO", String.class), "")));

        requestApiParam.put("job_mk", StringUtils.defaultIfEmpty(input.getJobMk(), "")); // 업무구분
        requestApiParam.put("chnn_mk", StringUtils.defaultIfEmpty(input.getChnnMk(), "")); // 채널구분
        requestApiParam.put("agrmnt_flg", StringUtils.defaultIfEmpty(input.getAgrmntFlg(), "")); // 동의여부
        requestApiParam.put("untact_mk", StringUtils.defaultIfEmpty(input.getUntactMk(), "")); // 대면/비대면 구분
        requestApiParam.put("id_no", perBusNo); // 주민등록번호
        requestApiParam.put("nm", custName); // 성명
        requestApiParam.put("ntv_frnr_se", ntv_frnr_se); // 내외국인구분코드

        // 개별 비필수 세팅
        requestApiParam.put("l033k001", perBusNo); // 주민번호

        log.debug("##### MA3PUPAPI001_101S 공무원 연금 공단 reqestApiParam  = [" + requestApiParam.toString() + "]");

        /*
         * 공무원 연금 공단 API CALL START
         */
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.PENSION_DATA) // 소득금액증명
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(requestApiParam)
                .build();
        JSONObject result = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();
        log.debug("##### MA3PUPAPI001_101S 공무원 연금 공단 result  = [" + result.toString() + "]");
        String repCode = (String) result.get("RSPNS_CD"); // 응답코드
        String repMsg = (String) result.get("RSPNS"); // 응답메세지
        JSONObject pension_loan_info = (JSONObject) result.get("pension_loan_info");

        if (repCode == null || "".equals(repCode) || !repCode.equals("200")) {
            log.info("##### MA3PUPAPI001_101S API CALL ERROR [1] 공무원 연금 공단 조회 에러#####");
            log.info("resCode = " + repCode);
            log.info("repMsg = " + repMsg);
            throw new PRCServiceException(repCode, "서버와 통신중 오류가 발생하였습니다.");
        } else {

            // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();
                Object apivalue = result.get(apikey);
                resultAPiParam.put(apikey, apivalue);
            }

            log.debug("##### MA3PUPAPI001_101S 공무원연금공단 pension_loan_info  = [" + pension_loan_info.toString() + "]");
            log.debug("##### MA3PUPAPI001_101S 공무원연금공단 resultAPiParam  = [" + resultAPiParam.toString() + "]");
            // 비고
            String PROC_RESULT = StringUtils.defaultIfEmpty(
                    (pension_loan_info.get("PROC_RESULT") != null ? String.valueOf(pension_loan_info.get("PROC_RESULT"))
                            : ""),
                    "");
            log.debug("##### MA3PUPAPI001_101S 공무원연금공단 PROC_RESULT  = [" + PROC_RESULT + "]");

            if ("".equalsIgnoreCase(PROC_RESULT)) {
                resultAPiParam.put("PROC_RESULT_FLAG", "Y");
            } else {
                resultAPiParam.put("PROC_RESULT_FLAG", "N");
            }
            sessionManager.setGlobalValue(UserID + "_pensionInfo", resultAPiParam.toString());
        }

        return resultAPiParam;

    }

    private GetMyDataResponse getMdcDummy(String filePath) throws PRCServiceException {
        FileReader fr = null;
        BufferedReader br = null;
        JSONObject resultJsonObj = new JSONObject();
        GetMyDataResponse output = new GetMyDataResponse();
        // json 파일인 경우만 가져옴
        if (filePath.toLowerCase().endsWith(".json")) {
            try {
                StringBuilder response = new StringBuilder();
                String readLine = null;
                log.debug("@#@#@#@# MA3PUPAPI001_101S @#@#@#@### getMdcDummy : target[" + filePath + "] ###");
                fr = new FileReader(filePath);
                br = new BufferedReader(fr);

                while (((readLine = br.readLine()) != null)) {
                    response.append(readLine);
                }
                String jsonString = response.toString().replaceAll("(?s)/\\*.*?\\*/|//.*?(\r|\n)", "");
                log.debug("@#@#@#@# MA3PUPAPI001_101S @#@#@#@### getMdcDummy : jsonString[" + jsonString + "] ###");
                resultJsonObj = new JSONObject(jsonString);

                output.setRspCode(resultJsonObj.optString("rsp_code"));
                output.setResData(resultJsonObj.optString("resData").toString());
            } catch (Exception e) {
                // resultJsonObj.put("rsp_code", "9999");
                // resultJsonObj.put("rsp_msg", e.getMessage());
                output.setRspCode("9999");
                output.setRspMsg(e.getMessage());
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        br = null;
                    }
                }
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        fr = null;
                    }
                }
                // 오류 or 실패케이스 Dummy인 경우 에러 발생
                if (!"200".equals(resultJsonObj.optString("rsp_code"))) {
                    String errorCode = StringUtils.defaultIfEmpty(
                            (resultJsonObj.optString("rsp_code") != null
                                    ? String.valueOf(resultJsonObj.optString("rsp_code"))
                                    : ""),
                            "9999");
                    String errorMessage = StringUtils.defaultIfEmpty(
                            (resultJsonObj.optString("rsp_msg") != null
                                    ? String.valueOf(resultJsonObj.optString("rsp_msg"))
                                    : ""),
                            "Dummy 조회 오류");
                    PRCServiceException appException = new PRCServiceException(errorCode);
                    appException.setErrorMessage(errorMessage);
                    throw appException;
                }
            }
        } else { // 유효한 더미정보가 아닌 경우
            // resultJsonObj.put("rsp_code", "200");
            // resultJsonObj.put("rsp_msg", "Dummy 미존재");
            // resultJsonObj.put("resData", new JSONObject());
            output.setRspCode("200");
            output.setRspMsg("Dummy 미존재");
            output.setResData(new JSONObject().toString());
        }

        return output;
    }

    /**
     * 공공데이터 전자서명 검증
     *
     * @param ValidateGovmDataCertRequest
     * @return ValidateGovmDataCertResponse
     * @throws Exception
     * @description MA3CMMBIZ019_002S
     */
    @ServiceEndpoint(url = "/validateGovmDataCert", name = "공공데이터 전자서명 검증 [ASIS: MA3CMMBIZ019_002S]", author = "김기주")
    public ValidateGovmDataCertResponse validateGovmDataCert(IServiceContext ctx, ValidateGovmDataCertRequest input)
            throws Exception {

        // 환경설정 경로 지정 필요할경우
        WizveraConfig delfinoConfig = null;
        try {
            delfinoConfig = new WizveraConfig(configPath);
        } catch (IOException e) {
            log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
        }

        log.debug("@@@@validateGovmDataCert Start LOG" + input.toString());

        // TODO. 테스트 필요
        ValidateGovmDataCertResponse output = new ValidateGovmDataCertResponse();
        String CommonCertSignYN = StringUtils.defaultIfEmpty(input.getCommonCertSignYn(), "");
        String devlopeCertVerifyFlag = StringUtils.defaultIfEmpty(input.getDevlopeCertVerifyFlag(), "N");
        String checkFinFlag = StringUtils.defaultIfEmpty(input.getCheckFinFlag(), "");
        // 개발서버일때 인증서 검증을 무조건 하도록 조치
        sessionManager.setGlobalValue("devlopeCertVerifyFlag", devlopeCertVerifyFlag);
        String PKCS7SignedData = input.getPkcs7SignedData();
        String vidRandom = StringUtils.defaultIfEmpty(input.getVidRandom(), "");

        // [SC제일 신용대출] 사설인증서 관련 추가
        String FinTechSignYN = StringUtils.defaultIfEmpty(input.getFinTechSignYN(), ""); // 사설인증서 전자서명여부

        log.debug("@@@@CommonCertSignYN ::" + CommonCertSignYN);
        log.debug("@@@@devlopeCertVerifyFlag ::" + devlopeCertVerifyFlag);
        log.debug("@@@@FinTechSignYN ::" + FinTechSignYN);
        log.debug("@@@@PKCS7SignedData ::" + PKCS7SignedData);

        if ("Y".equals(CommonCertSignYN)) {
            // CommonCertSignYN : "Y", PKCS7SignedData : 있음 > 공동인증 검증
            // PKCS7SignedData = java.net.URLDecoder.decode(input.getPkcs7SignedData());
            // log.debug("@@@@@ signData 2[" + PKCS7SignedData + "]");

            try {
                // VID검사
                certVerifyComponent.selfVerify(PKCS7SignedData, vidRandom);

                // 인증서상태검증
                SignVerifier signer = new SignVerifier(delfinoConfig);
                SignVerifierResult signVerifierResult = signer.verifyPKCS7(PKCS7SignedData,
                        SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
                X509Certificate userCert = signVerifierResult.getSignerCertificate();
                certUtils.getUserInfoByScbDBEdoc(userCert, "");

            } catch (Exception e) {
                throw new PRCServiceException("MA3CRT9001", "제출하신 공인인증서의 검증에 실패 하였습니다. 인증서를 다시한번 확인해 주시기 바랍니다.");
            }

        } else if (!"Y".equals(CommonCertSignYN) && !StringUtils.isEmpty(PKCS7SignedData)
                && !"Y".equals(FinTechSignYN)) {
            if ("FC".equals(checkFinFlag)) {
                // 22.09.26 금융인증서 확인을 위해 flag 값 추가 (FINCERT - 금융인증서 스크래핑용)
                finCertVerifyComponent.finVerify("FC", PKCS7SignedData);
            } else {
                // CommonCertSignYN : 없음, PKCS7SignedData : 있음 > 금융인증 검증
                // 금융인증서 타행인증서 인증 내용 확인을 위해 MD추가 - 다른 곳에서 사용 X. 처음 추가되는 소스 20220520
                finCertVerifyComponent.verify("MD", PKCS7SignedData);
            }

        } else if ("Y".equals(FinTechSignYN)) {
            finTechCertVerifyComponent.authVerify(PKCS7SignedData);

        } else { // 간편인증
            // CommonCertSignYN : 없음, PKCS7SignedData : 없음 > 간편인증 (검증X)
        }

        return output;
    }
}
