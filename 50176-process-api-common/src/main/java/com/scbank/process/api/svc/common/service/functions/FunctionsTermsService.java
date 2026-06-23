package com.scbank.process.api.svc.common.service.functions;

import java.util.List;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.common.dao.Ma30PrdctPrvsnMgtDao;
import com.scbank.process.api.svc.common.dao.PrdctPrvsnMgtDao;
import com.scbank.process.api.svc.common.dao.dto.ProductTermsInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.ProductTermsInfoResult;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoResult;
import com.scbank.process.api.svc.common.mapper.FunctionsTermsMapper;
import com.scbank.process.api.svc.common.service.functions.dto.terms.FncTrmInquiryProductTermsRequest;
import com.scbank.process.api.svc.common.service.functions.dto.terms.FncTrmInquiryProductTermsResponse;
import com.scbank.process.api.svc.common.service.functions.dto.terms.FncTrmInquiryTermsRequest;
import com.scbank.process.api.svc.common.service.functions.dto.terms.FncTrmInquiryTermsResponse;
import com.scbank.process.api.svc.common.service.functions.dto.terms.ProductTermsInfo;
import com.scbank.process.api.svc.common.service.functions.dto.terms.TermsInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/functions/terms", name = "공통 약관 조회 서비스")
public class FunctionsTermsService {

    /**
     * 약관조회 Dao
     */
    private final Ma30PrdctPrvsnMgtDao ma30PrdctPrvsnMgtDao;

    /**
     * 약관 관련 Mapper
     */
    private final FunctionsTermsMapper termsInfoMapper;

    /**
     * 상품 약관조회 Dao
     */
    private final PrdctPrvsnMgtDao prdctPrvsnMgtDao;

    @ServiceEndpoint(url = "/inquiryTerms", name = "공통 약관 조회")
    public FncTrmInquiryTermsResponse inquiryTerms(IServiceContext serviceContext,
            FncTrmInquiryTermsRequest request) {
        FncTrmInquiryTermsResponse response = new FncTrmInquiryTermsResponse();

        // if(log.isDebugEnabled()) {
        // log.debug("SampleTestService > testTerms > db2 : [{}]",
        // com.ibm.db2.jcc.DB2Driver.class.getProtectionDomain().getCodeSource().getLocation());
        // log.debug("SampleTestService > testTerms > db2 : [{}]",
        // System.getProperty("db2.jcc.charsetDecoderEncoder"));
        // }

        String actionType = request.getActionType();
        List<String> prvsnCdList = request.getPrvsnCdList();
        List<String> loctnCdList = request.getLoctnCdList();

        if (actionType.isBlank() || prvsnCdList.isEmpty() || loctnCdList.isEmpty()) {
            throw new PRCServiceException("MA3CMM0035", "등록된 약관이 없습니다. 약관정보를 확인해 주세요.");
        }

        TermsInfoParameter termsInfoParameter = TermsInfoParameter.builder()
                .actionType(actionType)
                .loctnCdList(loctnCdList)
                .build();

        if (actionType.equalsIgnoreCase("pdfViewer")) {
            // pdfViewer
            termsInfoParameter.setPrvsnCdList(prvsnCdList);
        } else {
            // htmlExplain | pdfDownload
            termsInfoParameter.setPrvsnCd(prvsnCdList.get(0));
        }

        List<TermsInfoResult> termsInfoResultList = ma30PrdctPrvsnMgtDao.selectTermsInfo(termsInfoParameter);

        if (termsInfoResultList == null || termsInfoResultList.isEmpty()) {
            throw new PRCServiceException("MA3CMM0035", "등록된 약관이 없습니다. 약관정보를 확인해 주세요.");
        }

        List<TermsInfo> termsInfoList = termsInfoResultList.stream()
                .map(termsInfoMapper::toTermsInfo)
                .toList();

        if (log.isDebugEnabled()) {
            log.debug("SampleTestService > testTerms > termsInfoList : [{}]", termsInfoList);
        }

        // List<TermsInfo> testTermsInfoList =
        // termsMapper.toTermsInfoList(termsInfoResultList);

        // if(log.isDebugEnabled()) {
        // log.debug("SampleTestService > testTerms > testTermsInfoList : [{}]",
        // testTermsInfoList);
        // }

        String PMS_RESOURCE_ROOT = "https://ma3ut.standardchartered.co.kr/mpms_RESOURCE";

        response.setResCode("00");
        response.setTermsInfoList(termsInfoList);
        response.setResourceRoot(PMS_RESOURCE_ROOT);

        return response;
    }

    @ServiceEndpoint(url = "/inquiryProductTerms", name = "상품 약관 조회")
    public FncTrmInquiryProductTermsResponse inquiryProductTerms(IServiceContext serviceContext,
            FncTrmInquiryProductTermsRequest request) {
        FncTrmInquiryProductTermsResponse response = new FncTrmInquiryProductTermsResponse();

        // String prdctId = StringUtils.defaultIfEmpty(request.getPrdctId(),
        // StringUtils.defaultIfEmpty(SessionManager.getGlobalValue("PRDCT_ID"),""));
        String prdctPrvsnId = StringUtils.defaultIfEmpty(request.getPrdctPrvsnId(), "");
        // String prdctCd = StringUtils.defaultIfEmpty(request.getPrdctCd(),
        // StringUtils.defaultIfEmpty(SessionManager.getGlobalValue("PRDCT_CD"),""));
        String prdctPrvsnCd = StringUtils.defaultIfEmpty(request.getPrdctPrvsnCd(), "");
        String prdctPrvsnType = StringUtils.defaultIfEmpty(request.getPrdctPrvsnType(), ""); // DPST : 수신, LOAN:
                                                                                             // 여신, TRUST: 신탁,
                                                                                             // CARD: 카드,
                                                                                             // INSRNC: 보험,
                                                                                             // ETC_FIX: 기타
        // String etcFix = StringUtils.defaultIfEmpty(request.getEtcFix(), "");
        // //PMS2.0에 등록안된 상품은 하드코딩
        String prdctViewType = StringUtils.defaultIfEmpty(request.getPrdctViewType(), ""); // "1"~"15"
        String scrapFlag = StringUtils.defaultIfEmpty(request.getScrapFlag(), ""); // 1:title빈값 , 2:title 정의

        /*
         * if(ETC_FIX.equals("ETC_FIX")) { //통합상담은 ID 조회한다.
         * PRDCT_TYPE = "ETC_FIX";
         * 
         * // 5843 : [운영계]대출정보 열람청구 및 상환위임장
         * // 5814 : [테스트계]대출정보 열람청구 및 상환위임장
         * 
         * // 대출이동제 동의 약관은 아래 3개 상품에 다 같은 파일이 들어있지만 화면에서 직장인중금리대출 코드를 사용하고 있음
         * // 4218 : [테스트,운영 동일]대출이동제 동의 약관-직장인중금리대출
         * // 3840 : [테스트,운영 동일]대출이동제 동의 약관-드림론 집단대출
         * // 128 : [테스트,운영 동일]대출이동제 동의 약관-돌려드림론
         * 
         * if ( RunningMode.PRODUCT.equals(SystemUtils.getRunningMode()) ) { //운영환경
         * if( !"5843".equals(PRDCT_ID) && !"4218".equals(PRDCT_ID) ) {
         * // 통합상담
         * if(PRDCT_CD.equals("PRDLON0100000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //통합상담 PRDCT_ID -->드림론으로 하드코딩
         * // 대출이동
         * if(PRDCT_CD.equals("PRDLON0200000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //대출이동 PRDCT_ID -->드림론으로 하드코딩
         * // 신용대출 연기갱신
         * if(PRDCT_CD.equals("PRDLON0400000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //신용대출연기갱신 PRDCT_ID -->드림론으로 하드코딩
         * // SC제일대출 추가
         * if(PRDCT_CD.equals("PRDLON0500000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //SC제일대출 추가 PRDCT_ID -->드림론으로 하드코딩
         * }
         * }else {
         * if( !"5814".equals(PRDCT_ID) && !"4218".equals(PRDCT_ID) ) {
         * // 통합상담
         * if(PRDCT_CD.equals("PRDLON0100000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //통합상담 PRDCT_ID -->드림론으로 하드코딩
         * // 대출이동
         * if(PRDCT_CD.equals("PRDLON0200000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //대출이동 PRDCT_ID -->드림론으로 하드코딩
         * // 신용대출 연기갱신
         * if(PRDCT_CD.equals("PRDLON0400000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //신용대출연기갱신 PRDCT_ID -->드림론으로 하드코딩
         * // SC제일대출 추가
         * if(PRDCT_CD.equals("PRDLON0500000") && !"1".equals(ScrapType)) PRDCT_ID =
         * "128"; //SC제일대출 추가 PRDCT_ID -->드림론으로 하드코딩
         * }
         * }
         * }
         */

        ProductTermsInfoParameter productTermsInfoParameter = ProductTermsInfoParameter.builder()
                .prdctId(prdctPrvsnId)
                .prdctCd(prdctPrvsnCd)
                .prdctType(prdctPrvsnType)
                .build();

        // 상품 약관 조회 결과
        ProductTermsInfoResult productTermsInfoResult = new ProductTermsInfoResult();

        if (!"Y".equals(scrapFlag)) {
            productTermsInfoResult = prdctPrvsnMgtDao
                    .selectProductTermsInfoByPrdctType(productTermsInfoParameter);
        } else {
            // 2025.12.23 차세대전 ASIS 주석
            // 공공데이터 PMS2.0 전용QUERY
            // 해당 QUERY는 API확장성을 위해 크게 잡아놓은 상태. 현재 6번까지 사용가능함.
            productTermsInfoResult = prdctPrvsnMgtDao.selectProductTermsInfo(productTermsInfoParameter);
        }

        if (productTermsInfoResult != null) {
            String attFileNm = "";

            // TODO: 리팩토링 대상
            if (prdctViewType.equals("1")) {
                attFileNm = productTermsInfoResult.getPrvsnNm1();
            } else if (prdctViewType.equals("2")) {
                attFileNm = productTermsInfoResult.getPrvsnNm2();
            } else if (prdctViewType.equals("3")) {
                attFileNm = productTermsInfoResult.getPrvsnNm3();
            } else if (prdctViewType.equals("4")) {
                attFileNm = productTermsInfoResult.getPrvsnNm4();
            } else if (prdctViewType.equals("5")) {
                attFileNm = productTermsInfoResult.getPrvsnNm5();
            } else if (prdctViewType.equals("6")) {
                attFileNm = productTermsInfoResult.getPrvsnNm6();
            } else if (prdctViewType.equals("7")) {
                attFileNm = productTermsInfoResult.getPrvsnNm7();
            } else if (prdctViewType.equals("8")) {
                attFileNm = productTermsInfoResult.getPrvsnNm8();
            } else if (prdctViewType.equals("9")) {
                attFileNm = productTermsInfoResult.getPrvsnNm9();
            } else if (prdctViewType.equals("10")) {
                attFileNm = productTermsInfoResult.getPrvsnNm10();
            } else if (prdctViewType.equals("11")) {
                attFileNm = productTermsInfoResult.getPrvsnNm11();
            } else if (prdctViewType.equals("12")) {
                attFileNm = productTermsInfoResult.getPrvsnNm12();
            } else if (prdctViewType.equals("13")) {
                attFileNm = productTermsInfoResult.getPrvsnNm13();
            } else if (prdctViewType.equals("14")) {
                attFileNm = productTermsInfoResult.getPrvsnNm14();
            } else if (prdctViewType.equals("15")) {
                attFileNm = productTermsInfoResult.getPrvsnNm15();
            }

            response.setProductTermsInfo(ProductTermsInfo.builder()
                    .prdctPrvsnId(productTermsInfoResult.getPrdctId())
                    .prdctPrvsnCd(prdctPrvsnCd)
                    .prdctPrvsnNm(productTermsInfoResult.getPrdctNm())
                    .attFileNm(attFileNm)
                    .build());
        } else {
            response.setProductTermsInfo(ProductTermsInfo.builder()
                    .prdctPrvsnId(prdctPrvsnId)
                    .prdctPrvsnCd(prdctPrvsnCd)
                    .build());
        }

        // 상품명이 있고 / 상품ID가 128일경우 상품명 변경
        // if (resultData.PRDCT_NM != undefined) {
        // if (resultData.PRDCT_ID != undefined && resultData.PRDCT_ID == "128") {
        // resultData.PRDCT_NM = "신용대출";
        // }}

        String defaultResourceRoot = "";
        if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
            defaultResourceRoot = "https://www.standardchartered.co.kr/hp/file/ap/pd/";
        } else {
            defaultResourceRoot = "https://owwwsit.standardchartered.co.kr/hp/file/ap/pd/";
        }

        response.setResCode("00");
        response.setResourceRoot(StringUtils.defaultIfEmpty(PropertiesUtils.getString("HOMEPAGE_PDF_URL"),
                defaultResourceRoot));

        return response;
    }

    /**
     * 
     */
    // <!-- 약관 조회 -->
    // <select id="MA3LON_MBL_PRVSN_MGT_01_S" parameterType="hashmap"
    // resultType="hashmap" resultMap = "clobHashMap">
    // SELECT
    // A.SEQNO,
    // A.PRDCT_CTGRY,
    // A.PRDCT_ID,
    // A.PRDCT_NM,
    // A.PRVSN_NM,
    // A.PRVSN_CD,
    // A.LANG_CD,
    // A.USE_FLG,
    // B.PRVSN_CNTNT AS PRVSN_CNTNT
    // FROM MBL_PRVSN_MGT A, MBL_PRVSN_DTL_INFO B
    // WHERE A.SEQNO = B.SEQNO
    // AND A.USE_FLG = 'Y'
    // AND A.PRVSN_CD IN (244,2005,217)
    // ORDER BY A.SEQNO
    // WITH UR
    // </select>
}