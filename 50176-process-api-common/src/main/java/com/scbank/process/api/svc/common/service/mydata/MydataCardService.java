package com.scbank.process.api.svc.common.service.mydata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.Ma3MydataOrgMgtDao;
import com.scbank.process.api.svc.common.dao.dto.SelectMydataOrgMgtResult;
import com.scbank.process.api.svc.common.service.mydata.dto.GetCardListDataRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetCardListDataResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetCardBasicInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetCardBasicInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetJoinInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetJoinInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostSpentLastMonthRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostSpentLastMonthResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostUsedThreeMonthRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetMostUsedThreeMonthResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.GetPaymentLastFiveDaysRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.GetPaymentLastFiveDaysResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.CardInfoDto;
import com.scbank.process.api.svc.common.service.mydata.dto.ThreeMonthInfoDto;
import com.scbank.process.api.svc.common.service.mydata.dto.FiveDaysInfoDto;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardLoanRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardLoanResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardManageInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardManageInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardOrganInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListCardOrganInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListJoinOrganInfoRequest;
import com.scbank.process.api.svc.common.service.mydata.dto.ListJoinOrganInfoResponse;
import com.scbank.process.api.svc.common.service.mydata.dto.ListJoinOrganInfoResponse.TranListDto;
import com.scbank.process.api.svc.common.service.mydata.dto.LongTranInfoDto;
import com.scbank.process.api.svc.common.service.mydata.dto.OrgCardInfoDto;
import com.scbank.process.api.svc.common.service.mydata.dto.RevolvingInfoDto;
import com.scbank.process.api.svc.common.service.mydata.dto.ShortTranInfoDto;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataHelper;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataApiClient;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntityBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "마이데이터 API 거래", url = "/mydata/card")

public class MydataCardService {

    // Session 매니저 주입 //
    private final ISessionContextManager sessionManager;

    // MyData 컴포넌트
    private final MdcMyDataApiClient mdcMyDataApiClient;

    // DB 연계 관련 컴포넌트 주입
    private final Ma3MydataOrgMgtDao ma3MydataOrgMgtDao;

    @Transactional(transactionManager = "kfbdbTransactionManager", rollbackFor = Exception.class)
    @ServiceEndpoint(url = "/getJoinInfo", name = "마이데이터 > 현대카드 가입여부조회 API (as-is service = MA3MDTINQ000_103S)")
    public GetJoinInfoResponse getJoinInfo(IServiceContext ctx, GetJoinInfoRequest request)
            throws Exception {
        mdcMyDataApiClient.init();

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        JSONObject entrncApiParam = new JSONObject();
        entrncApiParam.put("party_ci_key", party_ci_key); // 고객CI번호
        entrncApiParam.put("scrn_mk_cd", "LD00"); // 화면구분코드

        // 마이데이터 가입유무 조회 API
        MyDataHttpRequestEntity requestEntrncEntity = MyDataHttpRequestEntityBuilder
                .builder(MdcMyDataHelper.REGISTYN_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(entrncApiParam)
                .build();

        // 클라이언트 출력부 DTO 세팅
        GetJoinInfoResponse response = new GetJoinInfoResponse();

        try {
            log.info("::::: 마이데이터 가입유무 조회 시작.....");
            JSONObject entrncResult = mdcMyDataApiClient.execute(requestEntrncEntity).throwException()
                    .getAsJsonObject();

            log.info("::::: 마이데이터 가입유무 조회  result = [" + entrncResult.toString() + "]");

            String entrncFlg = StringUtils.defaultIfEmpty((String) entrncResult.get("entrnc_flg"), "");
            String hycardFlg = StringUtils.defaultIfEmpty((String) entrncResult.get("hycard_flg"), "");
            String srvcUsePrvsn = StringUtils.defaultIfEmpty((String) entrncResult.get("srvc_use_prvsn"), "");
            String rqrdAgrmrDoc = StringUtils.defaultIfEmpty((String) entrncResult.get("rqrd_agrmr_doc"), "");
            String slctAgrmrDoc = StringUtils.defaultIfEmpty((String) entrncResult.get("slct_agrmr_doc"), "");

            response.setEntrncFlg(entrncFlg); // 가입여부(Y:가입/N:미가입/E:에러)
            response.setHycardFlg(hycardFlg); // SC 제휴 현대카드 보유여부
            response.setSrvcUsePrvsn(srvcUsePrvsn);
            response.setRqrdAgrmrDoc(rqrdAgrmrDoc);
            response.setSlctAgrmrDoc(slctAgrmrDoc);

        } catch (PRCServiceException pe) {
            log.error("마이데이터 가입유무 조회 에러.", pe);
            response.setEntrncFlg("E"); // 가입여부(E일 경우 전문거래만 가능 하도록.)
            return response;
        }

        return response;

    }

    @ServiceEndpoint(url = "/listCardOrganInfo", name = "마이데이터 > 현대카드 CARDDETAIL_DATA 조회 API (as-is service = MA3MDTINQ000_104S)")
    public ListCardOrganInfoResponse listCardOrganInfo(IServiceContext ctx,
            ListCardOrganInfoRequest request)
            throws Exception {

        // 클라이언트 출력부 DTO 세팅.
        ListCardOrganInfoResponse response = new ListCardOrganInfoResponse();

        String isRtFlg = StringUtils.defaultIfBlank(request.getIsRtFlg(), "N"); // 새로고침여부
        String hdFlag = StringUtils.defaultIfBlank(request.getHdFlag(), "H"); // 현대카드 flag
        String reqNo = StringUtils.defaultIfBlank(request.getReqNo(), ""); // reqNo
        String orgCd = StringUtils.defaultIfBlank(request.getOrgCd(), "D1AAFO0000"); // 기관코드

        if (!"H".equals(hdFlag)) {
            isRtFlg = "Y";
        }

        mdcMyDataApiClient.init();

        JSONObject scrapingAccountsReqData = new JSONObject();

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        scrapingAccountsReqData.put("party_ci_key", party_ci_key);
        scrapingAccountsReqData.put("org_cd", orgCd);
        scrapingAccountsReqData.put("req_no", reqNo);

        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.CARDDETAIL_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(scrapingAccountsReqData).build();

        // API 응답 DATA
        JSONObject scrapingAccountsRspData = mdcMyDataApiClient.execute(requestEntity).throwException()
                .getAsJsonObject();

        if (scrapingAccountsRspData != null) {
            // API 응답 결과
            String scrapingAccountsHomeResultType = (String) scrapingAccountsRspData.get(MdcMyDataHelper.RESULT_TYPE);

            String rspCode = (String) scrapingAccountsRspData.get("rsp_code");
            String rspMsg = (String) scrapingAccountsRspData.get("rsp_msg");
            reqNo = (String) scrapingAccountsRspData.get("req_no");
            orgCd = (String) scrapingAccountsRspData.get("org_cd");
            String orgNm = (String) scrapingAccountsRspData.get("org_nm");
            String stlmtExpctdAmt = (String) scrapingAccountsRspData.get("stlmt_expctd_amt");
            String stlmtExpctdDt = (String) scrapingAccountsRspData.get("stlmt_expctd_dt");
            String bllngAmt = (String) scrapingAccountsRspData.get("bllng_amt");
            String bllngYymm = (String) scrapingAccountsRspData.get("bllng_yymm");
            String blingDt = (String) scrapingAccountsRspData.get("bling_dt");
            String blingYn = (String) scrapingAccountsRspData.get("bling_yn");
            String pointYn = (String) scrapingAccountsRspData.get("point_yn");
            String rvlvngYn = (String) scrapingAccountsRspData.get("rvlvng_yn");
            String loanYn = (String) scrapingAccountsRspData.get("loan_yn");
            String lumpSumAmt = (String) scrapingAccountsRspData.get("lump_sum_amt");
            String instllmntAmt = (String) scrapingAccountsRspData.get("instllmnt_amt");
            String loanShortAmt = (String) scrapingAccountsRspData.get("loan_short_amt");
            String rvlvngAmt = (String) scrapingAccountsRspData.get("rvlvng_amt");
            String loanLongAmt = (String) scrapingAccountsRspData.get("loan_long_amt");
            String etcAmt = (String) scrapingAccountsRspData.get("etc_amt");

            Iterator<String> iterator = scrapingAccountsRspData.keySet().iterator();
            while (iterator.hasNext()) {
                String apikey = iterator.next();

                if (apikey.equals("card_list") && scrapingAccountsRspData.get("card_list") != null
                        && !scrapingAccountsRspData.get("card_list").equals("")) {

                    JSONArray jsonArray = (JSONArray) scrapingAccountsRspData.get("card_list");
                    log.debug("%%% card_list jsonArray ::: {}", jsonArray);
                    List<CardInfoDto> cardList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        CardInfoDto dto = new CardInfoDto();

                        dto.setCardId(String.valueOf(obj.get("card_id")));
                        dto.setPpId(String.valueOf(obj.get("pp_id")));
                        dto.setTotBsAmt(String.valueOf(obj.get("tot_bs_amt")));
                        dto.setCardProdNm(String.valueOf(obj.get("card_prod_nm")));
                        dto.setCardNo(String.valueOf(obj.get("card_no")));
                        dto.setCardMkCd(String.valueOf(obj.get("card_mk_cd")));
                        dto.setCardMkNm(String.valueOf(obj.get("card_mk_nm")));
                        dto.setInTimestamp(String.valueOf(obj.get("in_timestamp")));
                        dto.setProdVeriFlg(String.valueOf(obj.get("prod_veri_flg")));
                        dto.setRspCode(String.valueOf(obj.get("rsp_code")));

                        String updateTime = ""; // 최근 업데이트 시간
                        Object value = obj.get("in_timestamp");
                        String selectUpdateTime = value == null ? "" : value.toString();

                        selectUpdateTime = selectUpdateTime.replace("-", "");
                        // 업데이트 시간 최신화
                        if (StringUtils.isBlank(selectUpdateTime)) {
                            selectUpdateTime = "0";
                        } else {
                            selectUpdateTime = selectUpdateTime.substring(0, 8);
                        }

                        updateTime = compareTosearchDate(updateTime, selectUpdateTime);
                        dto.setUpdateTime(updateTime);

                        cardList.add(dto);
                    }
                    response.setCardList(cardList);
                }

                response.setIsRtFlg(isRtFlg);
                response.setReqNo(reqNo);
                response.setOrgCd(orgCd);
                response.setOrgNm(orgNm);
                response.setBlingDt(blingDt);
                response.setBlingYn(blingYn);
                response.setBllngAmt(bllngAmt);
                response.setBllngYymm(bllngYymm);
                response.setEtcAmt(etcAmt);
                response.setInstllmntAmt(instllmntAmt);
                response.setLoanLongAmt(loanLongAmt);
                response.setLoanYn(loanYn);
                response.setLumpSumAmt(lumpSumAmt);
                response.setOrgCd(orgCd);
                response.setPointYn(pointYn);
                response.setRvlvngAmt(rvlvngAmt);
                response.setRvlvngYn(rvlvngYn);
                response.setLoanShortAmt(loanShortAmt);
                response.setStlmtExpctdAmt(stlmtExpctdAmt);
                response.setStlmtExpctdDt(stlmtExpctdDt);
            }

            /*
             * 정상 반환 가능한 코드 먼저 처리 (고객이 가입을 안하여 발급된 토큰이 없을경우 40403으로 API 에서 내려옴(고객정보 없음) | 카드
             * 해지했을 경우 40305)
             */
            if ("40403".equals(rspCode) || "40305".equals(rspCode) || "60005".equals(rspCode)
                    || "60003".equals(rspCode)) {
                response.setRspCode(rspCode);
                response.setRspMsg(rspMsg);
                return response;
            }

            // 실패
            if (!MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
                PRCServiceException error = new PRCServiceException(rspCode);
                error.setErrorMessage(rspMsg);
                throw error;
            }
        } else {
            throw new PRCServiceException("CARDDETAIL_DATA API IS EMPTY");
        }

        return response;

    }

    // 날짜 비교
    private String compareTosearchDate(String currentUpdateDate, String newUpdateDate) {
        if ("".equals(currentUpdateDate)) {
            currentUpdateDate = newUpdateDate;
        } else if (!"".equals(currentUpdateDate) && !"".equals(newUpdateDate)) {
            int dateCompare = compare(currentUpdateDate, newUpdateDate); // 2: 크다. 1:같다. 0:작다.
            if (dateCompare < 1) {
                currentUpdateDate = newUpdateDate;
            }
        }
        log.debug("###############  ompareTosearchDate currentUpdateDate : ["
                + currentUpdateDate + "] ##################");
        return currentUpdateDate;
    }

    @ServiceEndpoint(url = "/getCardListData", name = "마이데이터 > 현대카드목록 조회 (CARDLIST_DATA) API (as-is service = MA3MDTINQ001_201S)")
    public GetCardListDataResponse getCardListData(IServiceContext ctx,
            GetCardListDataRequest request)
            throws Exception {

        // 클라이언트 출력부 DTO 세팅.
        GetCardListDataResponse response = new GetCardListDataResponse();

        String isRtFlg = StringUtils.defaultIfEmpty(request.getIsRtFlg(), "N"); // 새로고침여부 >
                                                                                // ''Y':새로고침(실시간API호출),'N':DB조회(최근이력)

        mdcMyDataApiClient.init();

        JSONObject scrapingAccountsReqData = new JSONObject();

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        scrapingAccountsReqData.put("party_ci_key", party_ci_key);
        scrapingAccountsReqData.put("is_rt_flg", isRtFlg);

        MyDataHttpRequestEntity requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.CARDLIST_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(scrapingAccountsReqData).build();

        // API 응답 DATA
        JSONObject scrapingAccountsRspData = mdcMyDataApiClient.execute(requestEntity).throwException()
                .getAsJsonObject();

        if (scrapingAccountsRspData != null) {

            // StringUtils.defaultString로 null 제거
            String scrapingAccountsHomeResultType = StringUtils
                    .defaultString((String) scrapingAccountsRspData.get(MdcMyDataHelper.RESULT_TYPE)); // API응답
            // 결과타입
            String rspCode = StringUtils
                    .defaultString((String) scrapingAccountsRspData.get("rsp_code"));
            String rspMsg = StringUtils
                    .defaultString((String) scrapingAccountsRspData.get("rsp_msg"));

            /*
             * 정상 반환 가능한 코드 먼저 처리 (고객이 가입을 안하여 발급된 토큰이 없을경우 40403으로 API 에서 내려옴(고객정보 없음) | 카드
             * 해지했을 경우 40305)
             */
            if ("40403".equals(rspCode) || "40305".equals(rspCode) || "60005".equals(rspCode)
                    || "60003".equals(rspCode)) {
                response.setRspCode(rspCode);
                response.setRspMsg(rspMsg);
                return response;
            }
            // 응답 코드가 없을 때
            if (StringUtils.isBlank(rspCode)) {
                throw new PRCServiceException("CARDLIST_DATA API IS EMPTY");
            }
            // 실패
            if (!MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
                PRCServiceException error = new PRCServiceException(rspCode);
                error.setErrorMessage(rspMsg);
                throw error;
            }
            /*
             * 
             * 기관명 pms 테이블 목록에서 가져오도록 함 / MA3_MDS_API.MYDATA_ORG_S_02 - 관리자에 등록된 모든 기관대상
             */
            // List<SelectMydataOrgMgtResult> resultList =
            // this.ma3MydataOrgMgtDao.selectMyDataOrg();

            Iterator<String> iterator = scrapingAccountsRspData.keySet().iterator();
            System.out.println("###### here");
            while (iterator.hasNext()) {
                String apikey = iterator.next();

                if (apikey.equals("org_card_list") && scrapingAccountsRspData.get("org_card_list") != null
                        && !scrapingAccountsRspData.get("org_card_list").equals("")) {

                    JSONArray jsonArray = (JSONArray) scrapingAccountsRspData.get("org_card_list");

                    List<OrgCardInfoDto> orgCardList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        OrgCardInfoDto dto = new OrgCardInfoDto();
                        String updateTime = ""; // 최근 업데이트 시간
                        // 정정후금액 소수점 제거
                        BigDecimal stlmtExpctdAmt = BigDecimal
                                .valueOf(Double.parseDouble((String) String.valueOf(obj.get("stlmt_expctd_amt"))));

                        String selectUpdateTime = (String) String.valueOf(obj.get("in_timestamp"));
                        if ("".equals(selectUpdateTime)) {
                            selectUpdateTime = "0";
                        } else {
                            selectUpdateTime = (selectUpdateTime).substring(0, 8);
                        }

                        dto.setBlingYn(String.valueOf(obj.get("bling_yn")));
                        dto.setInTimeStamp(String.valueOf(obj.get("in_timestamp")));
                        dto.setRspCode(String.valueOf(obj.get("rsp_code")));
                        dto.setRspMsg(String.valueOf(obj.get("rsp_msg")));
                        dto.setOrgCd(String.valueOf(obj.get("org_cd")));
                        dto.setOrgNm(String.valueOf(obj.get("org_nm")));
                        dto.setOrgCardCnt(String.valueOf(obj.get("org_card_cnt")));
                        dto.setStlmtExpctdAmt(stlmtExpctdAmt.setScale(0, RoundingMode.FLOOR));
                        dto.setLumpSumAmt(String.valueOf(obj.get("lump_sum_amt")));
                        dto.setInstllmntAmt(String.valueOf(obj.get("instllmnt_amt")));
                        dto.setLoanShortAmt(String.valueOf(obj.get("loan_short_amt")));
                        dto.setRvlvngAmt(String.valueOf(obj.get("rvlvng_amt")));
                        dto.setLoanLongAmt(String.valueOf(obj.get("loan_long_amt")));
                        dto.setEtcAmt(String.valueOf(obj.get("etc_amt")));
                        dto.setStlmtExpctdDt(String.valueOf(obj.get("stlmt_expctd_dt")));
                        dto.setReqNo(String.valueOf(obj.get("req_no")));
                        dto.setLoanTermGb(String.valueOf(obj.get("loan_term_gb")));
                        dto.setRvlvngFlg(String.valueOf(obj.get("rvlvng_flg")));
                        dto.setUpdateTime(updateTime);

                        orgCardList.add(dto);
                    }
                    response.setOrgCardList(orgCardList);
                }

            }
        } else {
            throw new PRCServiceException("CARDLIST_DATA API IS EMPTY");
        }

        return response;

    }

    private static int compare(String cDate, String oDate) {
        try {
            long cdt = Long.parseLong(cDate);
            long odt = Long.parseLong(oDate);
            if (cdt > odt) {
                return 2;
            } else if (cdt == odt) {
                return 1;
            } else {
                return cdt < odt ? 0 : -1;
            }
        } catch (Exception var4) {
            return -1;
        }
    }

    @ServiceEndpoint(url = "/listCardManageInfo", name = "마이데이터 > 현대카드 관리(상세, 리볼빙, 단기대출, 장기대출) API (as-is service = MA3MDTINQ006_101S)")
    public ListCardManageInfoResponse listCardManageInfo(IServiceContext ctx,
            ListCardManageInfoRequest request)
            throws Exception {

        // 클라이언트 출력부 DTO 세팅
        ListCardManageInfoResponse response = new ListCardManageInfoResponse();

        String nextKey = StringUtils.defaultIfBlank(request.getNextKey(), ""); // 다음페이지KEY
        String limitCnt = StringUtils.defaultIfBlank(request.getLimitCnt(), "10"); // 페이지건수
        String orgCd = StringUtils.defaultIfBlank(request.getOrgCd(), "D1AAFO0000"); // 기관코드
        String orgName = StringUtils.defaultIfBlank(request.getOrgName(), "현대카드"); // 기관이름
        String orgImg = StringUtils.defaultIfBlank(request.getOrgImg(), ""); // 기관이미지
        String blingYn = StringUtils.defaultIfBlank(request.getBlingYn(), "N"); // 수신구분
        String reqNo = StringUtils.defaultIfBlank(request.getReqNo(), "");
        String currentDate = DateUtils.getCurrentDate("yyyyMMdd").substring(0, 6); // 현재날짜 (YYYYMM)
        String searchMonth = StringUtils.defaultIfEmpty(request.getSearchMonth(), currentDate); // 조회년월
        String tabGubun = StringUtils.defaultIfBlank(request.getTabGubun(), "1"); // 탭 구분

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;

        JSONObject scrapingAccountsReqData = new JSONObject();

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        scrapingAccountsReqData.put("party_ci_key", party_ci_key); // 고객CI번호
        scrapingAccountsReqData.put("org_cd", orgCd);
        scrapingAccountsReqData.put("req_no", reqNo);

        // 구분값마다 거래하는 조회 다름 (3:리볼빙, 4:단기카드대출, 5:장기카드대출)
        if ("5".equals(tabGubun)) { // 장기카드대출 거래내역조회
            scrapingAccountsReqData.put("search_month", searchMonth);
        } else if ("6".equals(tabGubun)) {// 장기카드대출거래내역 조회
            String loanNo = StringUtils.defaultIfEmpty(request.getLoanNo(), ""); // 대출번호

            scrapingAccountsReqData.put("loan_no", loanNo); // 대출번호
            scrapingAccountsReqData.put("next_key", nextKey); // 다음페이지KEY
            scrapingAccountsReqData.put("limit_cnt", limitCnt);
        }

        String apiGubun = ""; // API 구분

        if ("1".equals(tabGubun)) { // 상세
            apiGubun = MdcMyDataHelper.CARDDETAIL_DATA;
        } else if ("3".equals(tabGubun)) { // 리볼빙
            apiGubun = MdcMyDataHelper.CARDRVLVNG_DATA;
        } else if ("4".equals(tabGubun)) { // 단기대출
            apiGubun = MdcMyDataHelper.CARDSHORTTRMLOAN_DATA;
        } else if ("5".equals(tabGubun)) { // 장기대출
            apiGubun = MdcMyDataHelper.CARDLONGTRMLOAN_DATA;
        }

        requestEntity = MyDataHttpRequestEntityBuilder.builder(apiGubun)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(scrapingAccountsReqData).build();

        JSONObject scrapingAccountsRspData = mdcMyDataApiClient.execute(requestEntity).throwException()
                .getAsJsonObject();

        if (scrapingAccountsRspData == null) {
            throw new Exception("MYDATA API NO Response! [1]");
        }

        String scrapingAccountsHomeResultType = (String) scrapingAccountsRspData.get(MdcMyDataHelper.RESULT_TYPE);
        String eCode = (String) scrapingAccountsRspData.get("rsp_code");
        String errMsg = (String) scrapingAccountsRspData.get("rsp_msg");

        if ("40403".equals(eCode)) {
            response.setECode(eCode);
            response.setErrMsg(errMsg);
            return response;
        }

        // 응답 코드가 없을 때
        if (StringUtils.isBlank(eCode)) {
            throw new PRCServiceException("CARDLIST_DATA API IS EMPTY");
        }
        if (StringUtils.isBlank(eCode) || !MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
            PRCServiceException appException = new PRCServiceException(eCode);
            appException.setErrorMessage(errMsg);
            throw appException;
        }

        response.setBlingYn(blingYn);

        // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
        Iterator<String> iterator = scrapingAccountsRspData.keySet().iterator();

        while (iterator.hasNext()) {
            String apikey = iterator.next();

            // 금액 소수점 지우기 START
            String bllng_amt = "0";// 청구금액
            String annual_fee = "0";// 상품 연회비

            if (apikey.equals("bllng_amt") && scrapingAccountsRspData.get("bllng_amt") != null
                    && !scrapingAccountsRspData.get("bllng_amt").equals("")) {
                Object apivalue = scrapingAccountsRspData.get(apikey);
                apivalue = apivalue.equals("") ? "" : apivalue.toString();
                BigDecimal amt = BigDecimal.valueOf(Double.parseDouble((String) apivalue));
                amt = amt.setScale(0, RoundingMode.FLOOR);

                bllng_amt = amt.toString();
                response.setBllngAmt(bllng_amt);
            }

            if (apikey.equals("annual_fee") && scrapingAccountsRspData.get("annual_fee") != null
                    && !scrapingAccountsRspData.get("annual_fee").equals("")) {
                Object apivalue = scrapingAccountsRspData.get(apikey);
                apivalue = apivalue.equals("") ? "" : apivalue.toString();
                BigDecimal amt = BigDecimal.valueOf(Double.parseDouble((String) apivalue));
                amt = amt.setScale(0, RoundingMode.FLOOR);

                annual_fee = amt.toString();
                response.setAnnualFee(annual_fee);

            }

            // 리볼빙
            if (apikey.equals("rvlvng_list") && scrapingAccountsRspData.get("rvlvng_list") != null
                    && !scrapingAccountsRspData.get("rvlvng_list").equals("")) {

                JSONArray jsonArray = (JSONArray) scrapingAccountsRspData.get("rvlvng_list");
                ArrayList<RevolvingInfoDto> revolvingList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    RevolvingInfoDto dto = new RevolvingInfoDto();
                    dto.setReqDt(String.valueOf(obj.get("req_dt")));
                    dto.setEndDt(String.valueOf(obj.get("end_dt")));
                    dto.setCloseDt(String.valueOf(obj.get("close_dt")));
                    dto.setAgreedPayAmt(String.valueOf(obj.get("agreed_pay_amt")));
                    dto.setAgreedPayRate(String.valueOf(obj.get("agreed_pay_rate")));
                    dto.setMinPayAmt(String.valueOf(obj.get("min_pay_amt")));
                    dto.setMinPayRate(String.valueOf(obj.get("min_pay_rate")));
                    dto.setRemainedAmt(String.valueOf(obj.get("remained_amt")));

                    revolvingList.add(dto);
                }
                response.setRvlvngList(revolvingList);
            }

            // 단기대출
            if (apikey.equals("short_trm_list") && scrapingAccountsRspData.get("short_trm_list") != null
                    && !scrapingAccountsRspData.get("short_trm_list").equals("")) {

                JSONArray jsonArray = (JSONArray) scrapingAccountsRspData.get("short_trm_list");
                ArrayList<ShortTranInfoDto> shortTrmList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    ShortTranInfoDto dto = new ShortTranInfoDto();

                    String intRateStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("int_rate"), ""),
                            "0");
                    String balanceAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("balance_amt"), ""),
                            "0");
                    String loanAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("loan_amt"), ""),
                            "0");

                    BigDecimal int_rate = new BigDecimal(intRateStr); // 연 이자율
                    BigDecimal balance_amt = new BigDecimal(balanceAmtStr); // 대출잔액
                    BigDecimal loan_amt = new BigDecimal(loanAmtStr); // 이용금액

                    int_rate = int_rate.movePointRight(2);
                    int_rate = int_rate.setScale(0, RoundingMode.FLOOR);
                    loan_amt = loan_amt.setScale(0, RoundingMode.FLOOR);
                    balance_amt = balance_amt.setScale(0, RoundingMode.FLOOR);

                    dto.setLoanAmt(loan_amt.toPlainString());
                    dto.setBalanceAmt(balance_amt.toPlainString());
                    dto.setIntRate(int_rate.toPlainString());
                    dto.setStlmtExpctdDt(String.valueOf(obj.get("stlmt_expctd_dt")));
                    dto.setLoanDt(String.valueOf(obj.get("loan_dt")));
                    dto.setRdmptnOrgCd(String.valueOf(obj.get("rdmptn_org_cd")));
                    dto.setRdmptnOrgNm(String.valueOf(obj.get("rdmptn_org_nm")));
                    dto.setRdmptnAcctNo(String.valueOf(obj.get("rdmptn_acct_no")));

                    shortTrmList.add(dto);
                }

                // 이자율

                log.debug(">>>>> 단기 shortTrmList ::::: {}", shortTrmList);
                response.setShortTrmList(shortTrmList);
                /*
                 * 
                 * 기관명 pms 테이블 목록에서 가져오도록 함 / MA3_MDS_API.MYDATA_ORG_S_02 - 관리자에 등록된 모든 기관대상
                 */
                List<SelectMydataOrgMgtResult> resultList = this.ma3MydataOrgMgtDao.selectMyDataOrg();
                log.debug(">>>>> 단기 SelectMydataOrgMgtResult ::::: {}", resultList);

                for (int k = 0; k < shortTrmList.size(); k++) {
                    for (int idx = 0; idx < resultList.size(); idx++) { // 기관별 이름 아이콘 정리
                        SelectMydataOrgMgtResult result = resultList.get(idx);
                        String itemOrgCode = result.getOrgCd();

                        for (int i = 0; i < shortTrmList.size(); i++) {
                            String apiOrgCode = shortTrmList.get(i).getRdmptnOrgCd(); // 상환기관코드

                            if (itemOrgCode.equals(apiOrgCode)) {
                                String apiOrgName = result.getOrgName();
                                shortTrmList.get(i).setRdmptnOrgNm(apiOrgName);// 기관명
                            }
                        }
                    }
                }

            }

            // 장기대출
            if (apikey.equals("long_trm_list") && scrapingAccountsRspData.get("long_trm_list") != null
                    && !scrapingAccountsRspData.get("long_trm_list").equals("")) {

                JSONArray jsonArray = (JSONArray) scrapingAccountsRspData.get("long_trm_list");

                List<LongTranInfoDto> longTrmList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    LongTranInfoDto dto = new LongTranInfoDto();

                    String intRateStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("int_rate"), ""),
                            "0");
                    String balanceAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("balance_amt"), ""),
                            "0");
                    String loanAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("loan_amt"), ""),
                            "0");

                    BigDecimal int_rate = new BigDecimal(intRateStr); // 연 이자율
                    BigDecimal balance_amt = new BigDecimal(balanceAmtStr); // 대출잔액
                    BigDecimal loan_amt = new BigDecimal(loanAmtStr); // 이용금액

                    int_rate = int_rate.movePointRight(2);
                    int_rate = int_rate.setScale(0, RoundingMode.FLOOR);
                    loan_amt = loan_amt.setScale(0, RoundingMode.FLOOR);
                    balance_amt = balance_amt.setScale(0, RoundingMode.FLOOR);

                    dto.setLoanNo(String.valueOf(obj.get("loan_no")));
                    dto.setLoanCnt(String.valueOf(obj.get("loan_cnt")));
                    dto.setProdNm(String.valueOf(obj.get("prod_nm")));
                    dto.setLoanAmt(loan_amt.toPlainString());
                    dto.setIntRate(int_rate.toPlainString());
                    dto.setExpireDt(String.valueOf(obj.get("expire_dt")));
                    dto.setBalanceAmt(balance_amt.toPlainString());
                    dto.setRdmptnMthdCd(String.valueOf(obj.get("rdmptn_mthd_cd")));
                    dto.setLoanDt(String.valueOf(obj.get("loan_dt")));
                    dto.setRdmptnOrgCd(String.valueOf(obj.get("rdmptn_org_cd")));
                    dto.setRdmptnOrgNm(String.valueOf(obj.get("rdmptn_org_nm")));
                    dto.setRdmptnAcctNo(String.valueOf(obj.get("rdmptn_acct_no")));

                    longTrmList.add(dto);
                }
                response.setLongTrmList(longTrmList);
                log.debug(">>>>> 장기 longTrmList ::::: ", longTrmList);
                /*
                 * 
                 * 기관명 pms 테이블 목록에서 가져오도록 함 / MA3_MDS_API.MYDATA_ORG_S_02 - 관리자에 등록된 모든 기관대상
                 */

                List<SelectMydataOrgMgtResult> resultList = this.ma3MydataOrgMgtDao.selectMyDataOrg();
                log.debug(">>>>> 장기 resultList ::::: ", resultList);

                for (int k = 0; k < longTrmList.size(); k++) {
                    for (int idx = 0; idx < resultList.size(); idx++) { // 기관별 이름 아이콘 정리

                        SelectMydataOrgMgtResult result = resultList.get(idx);
                        String itemOrgCode = result.getOrgCd();

                        for (int i = 0; i < longTrmList.size(); i++) {
                            String apiOrgCode = (String) longTrmList.get(i).getRdmptnOrgCd(); // 상환기관코드

                            if (itemOrgCode.equals(apiOrgCode)) {
                                String apiOrgName = result.getOrgName();
                                longTrmList.get(i).setRdmptnOrgNm(apiOrgName); // 기관명
                            }
                        }
                    }
                }
            }
        }

        // 카드 브랜드 코드, 상환방법 코드
        ArrayList<HashMap<String, Object>> orgArrayList_1 = new ArrayList<HashMap<String, Object>>(); // 업권정보(기관전체)
        List<Map<String, Object>> cardTypeList = new ArrayList<>();
        List<Map<String, Object>> orgMydataInfoList = new ArrayList<>();

        List<ICodeItemInfo> orgMydata = new ArrayList<ICodeItemInfo>();

        // 장기카드대출
        if ("5".equals(tabGubun)) {
            orgMydata = CodeUtils.getCodes("MYDATA_BANCTYPE"); // 상환방법 코드
            log.debug(">>>>>>>>>>> orgMydata ::: ", orgMydata);

            Object obj = scrapingAccountsRspData.get("long_trm_list");

            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObj = jsonArray.getJSONObject((i));
                    HashMap<String, Object> map = new HashMap<>();
                    Iterator<String> keys = jsonObj.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        map.put(key, jsonObj.get(key));
                    }
                    orgArrayList_1.add(map);
                }

            }

            for (int i = 0; i < orgArrayList_1.size(); i++) {
                HashMap<String, Object> long_trm_list = orgArrayList_1.get(i);
                // list엔 put 없
                Map<String, Object> orgMydataInfo = new HashMap<>();
                orgMydataInfo.put("rdmptn_mthd_cd", (String) long_trm_list.get("rdmptn_mthd_cd")); // 상환방법 코드
                orgMydataInfoList.add(orgMydataInfo);
            }

            // 기관별 이름 아이콘 정리
            for (Map<String, Object> s : orgMydataInfoList) {
                String code = (String) s.get("rdmptn_mthd_cd");

                for (ICodeItemInfo c : orgMydata) {
                    // String orgMydataKey = c.getKey();
                    if (Objects.equals(code, c.getKey())) {
                        // String[] value = c.getValue().split(";;");
                        Map<String, Object> orgMydataInfo = new HashMap<>();
                        orgMydataInfo.put("rdmptnMthdCd", c.getValue());
                        orgMydataInfo.put("rdmptnMthdOd", c.getOrder());
                        cardTypeList.add(orgMydataInfo);

                        response.setProdType(cardTypeList);
                        break;
                    }
                }
            }
        }

        response.setOrgCd(orgCd);
        response.setOrgName(orgName);
        response.setOrgImg(orgImg);

        return response;
    }

    @ServiceEndpoint(url = "/listCardLoan", name = "마이데이터 > 현대카드 장기카드대출 거래내역조회 API (as-is service = MA3MDTINQ006_501S/MA3MDTINQ006_50P)")
    public ListCardLoanResponse listCardLoan(IServiceContext ctx,
            ListCardLoanRequest request)
            throws Exception {

        String orgCd = StringUtils.defaultIfEmpty(request.getOrgCd(), "D1AAFO0000"); // 기관코드
        String reqNo = StringUtils.defaultIfEmpty(request.getReqNo(), "0");
        String loanNo = StringUtils.defaultIfEmpty(request.getLoanNo(), ""); // 대출번호
        String nextKey = StringUtils.defaultIfEmpty(request.getNextKey(), ""); // 다음페이지KEY
        String limitCnt = StringUtils.defaultIfEmpty(request.getLimitCnt(), "10"); // 페이지건수
        String currentDate = DateUtils.getCurrentDate("yyyyMMdd").substring(0, 6); // 현재년월
        String searchMonth = StringUtils.defaultIfEmpty(request.getSearchMonth(), currentDate); // 조회년월
        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;

        // 클라이언트 출력부 DTO 세팅.
        ListCardLoanResponse response = new ListCardLoanResponse();

        JSONObject requestAPIData = new JSONObject();

        requestAPIData.put("party_ci_key", party_ci_key); // 고객CI번호
        requestAPIData.put("org_cd", orgCd); // 기관코드
        requestAPIData.put("req_no", reqNo);
        requestAPIData.put("loan_no", loanNo); // 대출번호
        requestAPIData.put("limitCnt", limitCnt);
        requestAPIData.put("nextKey", nextKey);
        requestAPIData.put("search_month", searchMonth);

        // 장기카드대출 거래내역 API
        requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.CARDLONGTRMLOANTRAN_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken()).bodyParameters(requestAPIData).build();

        JSONObject responseAPIData = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();

        if (responseAPIData == null || responseAPIData.isEmpty()) {
            throw new PRCServiceException("CARDLONGTRMLOANTRAN_DATA API IS EMPTY");
        }

        String scrapingAccountsHomeResultType = (String) responseAPIData.get(MdcMyDataHelper.RESULT_TYPE);
        String eCode = (String) responseAPIData.get("rsp_code");
        String errMsg = (String) responseAPIData.get("rsp_msg");
        if ("40403".equals(eCode)) {
            response.setECode(eCode);
            response.setErrMsg(errMsg);
            return response;
        }

        if (StringUtils.isBlank(eCode) || !MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
            PRCServiceException appException = new PRCServiceException(eCode);
            appException.setErrorMessage(errMsg);
            throw appException;
        }

        if (responseAPIData.get("tran_list") != null
                && !responseAPIData.get("tran_list").equals("")) {

            // 수정
            Object obj = responseAPIData.get("tran_list");
            ArrayList<HashMap<String, Object>> tran_list = new ArrayList<>();
            if (obj instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) obj;
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObj = jsonArray.getJSONObject((i));
                    HashMap<String, Object> map = new HashMap<>();
                    Iterator<String> keys = jsonObj.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        map.put(key, jsonObj.get(key));
                    }
                    tran_list.add(map);
                }
                log.info("★★★★★★tran_list ★★★★★★ [" + tran_list + "]");
                response.setTranListJson(tran_list);
            }

            response.setNextKey((String) responseAPIData.get("next_key"));
        }

        return response;

    }

    @ServiceEndpoint(url = "/listJoinOrganInfo", name = "마이데이터 > 현대카드 이용내역조회 API (as-is service = MA3INQALL010_112S, MA3MDTINQ006_101S)")
    public ListJoinOrganInfoResponse listJoinOrganInfo(IServiceContext ctx,
            ListJoinOrganInfoRequest request)
            throws Exception {

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;

        // 클라이언트 출력부 DTO 세팅.
        ListJoinOrganInfoResponse response = new ListJoinOrganInfoResponse();

        String hycardFlg = StringUtils.defaultIfBlank(request.getHycardFlg(), "N"); // 현대카드 연결여부
        String entrncFlg = StringUtils.defaultIfBlank(request.getEntrncFlg(), "N"); // 마이데이터 가입여부
        String nextKey = StringUtils.defaultIfBlank(request.getNextKey(), ""); // 다음페이지KEY
        String limitCnt = StringUtils.defaultIfBlank(request.getLimitCnt(), "10"); // 페이지건수
        String orgCd = StringUtils.defaultIfBlank(request.getOrgCd(), "D1AAFO0000"); // 기관코드
        String blingYn = StringUtils.defaultIfBlank(request.getBlingYn(), ""); // 수신구분
        String reqNo = StringUtils.defaultIfBlank(request.getReqNo(), "");
        String cardNo = StringUtils.defaultIfBlank(request.getCardNo(), "");
        String cardPpId = StringUtils.defaultIfBlank(request.getCardPpId(), ""); // 선불카드식별자
        String hdFlag = StringUtils.defaultIfBlank(request.getHdFlag(), ""); // 현대카드 플래그 (전체 메뉴에서 들어왔을 때 공백)
        // String currentDate = DateUtils.getCurrentDate("yyyyMMdd").substring(0, 6); //
        // 현재날짜 (YYYYMM)
        // String toDate = DateUtils.getDateFormatFromMonth("yyyyMMdd", currentDate, 0);
        // // 현재월
        // String fromDate = DateUtils.getDateFormatFromMonth("yyyyMMdd", currentDate,
        // -3);// 현재월 - 3개월
        // String searchMonth = StringUtils.defaultIfEmpty(request.getSearchMonth(),
        // currentDate); // 조회년월

        log.info("★★★★★★listJoinOrganInfo  request ★★★★★★{}", request);
        String cardId = "";
        // 전체 > 승인내역조회 진입 시 마이데이터 가입 및 현대카드 연결했을 때 현대카드 목록 조회 API
        if ("Y".equals(hycardFlg) && "Y".equals(entrncFlg) && "".equals(hdFlag)) {
            ListCardOrganInfoRequest apiRequest = new ListCardOrganInfoRequest();
            ListCardOrganInfoResponse listCardOrganInfo = this.listCardOrganInfo(ctx, apiRequest);
            List<CardInfoDto> cardList = listCardOrganInfo.getCardList();
            if (cardList != null && !cardList.isEmpty()) {
                cardId = cardList.get(0).getCardId();
            }
        } else {
            cardId = StringUtils.defaultIfEmpty(request.getCardId(), ""); // 카드아이디
        }

        JSONObject scrapingAccountsReqData = new JSONObject();

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        scrapingAccountsReqData.put("party_ci_key", party_ci_key); // 고객CI번호
        scrapingAccountsReqData.put("org_cd", orgCd);
        scrapingAccountsReqData.put("req_no", reqNo);
        scrapingAccountsReqData.put("pp_id", cardPpId); // 선불카드 식별자
        scrapingAccountsReqData.put("card_id", cardId);
        scrapingAccountsReqData.put("next_key", nextKey); // 다음페이지KEY
        scrapingAccountsReqData.put("limit_cnt", limitCnt);
        // scrapingAccountsReqData.put("search_month", searchMonth);
        // scrapingAccountsReqData.put("to_date", toDate);
        // scrapingAccountsReqData.put("from_date", fromDate);

        // 마이데이터 현대카드 이용내역 조회
        requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.CARDTRAN_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(scrapingAccountsReqData).build();

        JSONObject scrapingAccountsRspData = mdcMyDataApiClient.execute(requestEntity).throwException()
                .getAsJsonObject();

        if (scrapingAccountsRspData == null || scrapingAccountsRspData.isEmpty()) {
            throw new PRCServiceException("CARDTRAN_DATA API IS EMPTY");
        }

        String scrapingAccountsHomeResultType = (String) scrapingAccountsRspData.get(MdcMyDataHelper.RESULT_TYPE);
        String eCode = (String) scrapingAccountsRspData.get("rsp_code");
        String errMsg = (String) scrapingAccountsRspData.get("rsp_msg");
        String consentMrchntNmYn = (String) scrapingAccountsRspData.get("consent_mrchnt_nm_yn"); // 가맹점명여부

        if ("40403".equals(eCode)) {
            response.setECode(eCode);
            response.setErrMsg(errMsg);
            return response;
        }

        if (StringUtils.isBlank(eCode) || !MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
            PRCServiceException appException = new PRCServiceException(eCode);
            appException.setErrorMessage(errMsg);
            throw appException;
        }

        // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
        Iterator<String> iterator = scrapingAccountsRspData.keySet().iterator();
        while (iterator.hasNext()) {
            String apikey = iterator.next();

            // 금액 소수점 지우기 START [거래내역]
            if (apikey.equals("tran_list") && scrapingAccountsRspData.get("tran_list") != null
                    && !scrapingAccountsRspData.get("tran_list").equals("")) {

                JSONArray jsonArray = (JSONArray) scrapingAccountsRspData.get("tran_list");

                List<TranListDto> tranList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    TranListDto dto = new TranListDto();

                    dto.setApprvlTimestamp(String.valueOf(obj.get("apprvl_timestamp")));
                    dto.setApprvlDt(String.valueOf(obj.get("apprvl_dt")));
                    dto.setTranTimestamp(String.valueOf(obj.get("tran_timestamp")));
                    dto.setTranDt(String.valueOf(obj.get("tran_dt")));
                    dto.setMrchntNm(String.valueOf(obj.get("mrchnt_nm")));
                    dto.setMrchntRegno(String.valueOf(obj.get("mrchnt_regno")));
                    dto.setTotalInstallCnt(String.valueOf(obj.get("total_install_cnt")));
                    dto.setStlmtStsCd(String.valueOf(obj.get("stlmt_sts_cd")));
                    dto.setStlmtStsNm(String.valueOf(obj.get("stlmt_sts_nm")));
                    dto.setStlmtNtnlCd(String.valueOf(obj.get("stlmt_ntnl_cd")));
                    dto.setStlmtCrncyCd(String.valueOf(obj.get("stlmt_crncy_cd")));
                    dto.setBuyDt(String.valueOf(obj.get("buy_dt")));
                    dto.setSaleDt(String.valueOf(obj.get("sale_dt")));
                    dto.setPriApprvlTimestamp(String.valueOf(obj.get("pri_apprvl_timestamp")));
                    dto.setPriApprivlAmt(String.valueOf(obj.get("pri_apprivl_amt")));
                    dto.setPriKrwAmt(String.valueOf(obj.get("pri_krw_amt")));
                    dto.setPriCrncyCd(String.valueOf(obj.get("pri_crncy_cd")));
                    dto.setWonAmt(String.valueOf(obj.get("won_amt")));
                    dto.setTranTypeCd(String.valueOf(obj.get("tran_type_cd")));
                    dto.setTranTypeNm(String.valueOf(obj.get("tran_type_nm")));
                    dto.setWonAmt(String.valueOf(obj.get("won_amt")));
                    dto.setTranOrgCd(String.valueOf(obj.get("tran_org_cd")));
                    dto.setTranId(String.valueOf(obj.get("tran_id")));
                    dto.setApprovedNum(String.valueOf(obj.get("approved_num")));
                    dto.setNextKey(String.valueOf(obj.get("next_key")));
                    dto.setTotalCnt(String.valueOf(obj.get("total_cnt")));

                    // 거래내역 조회 (통화코드 있음)
                    String crncy_cd = StringUtils.defaultIfEmpty(String.valueOf(obj.get("stlmt_crncy_cd")), "").trim();
                    String tranAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("tran_amt"), ""), "0");
                    String balanceAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("balance_amt"), ""),
                            "0");
                    String approvedAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("approved_amt"), ""),
                            "0");
                    String modifiedAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("modified_amt"), ""),
                            "0");

                    // 선불카드일 경우
                    BigDecimal tran_amt = new BigDecimal(tranAmtStr); // (선불카드) 거래금액
                    BigDecimal balance_amt = new BigDecimal(balanceAmtStr); // 거래후금액

                    // 통화코드가 공백이면 KRW
                    if (crncy_cd.equals("") || "KRW".equals(crncy_cd)) {

                        dto.setStlmtCrncyCd("KRW");

                        BigDecimal approved_amt = new BigDecimal(approvedAmtStr);// 이용금액
                        BigDecimal modified_amt = new BigDecimal(modifiedAmtStr); // 정정후금액

                        // 선불카드일 경우에만 값 있을 수 있음
                        tran_amt = tran_amt.setScale(0, RoundingMode.FLOOR);
                        balance_amt = balance_amt.setScale(0, RoundingMode.FLOOR);

                        approved_amt = approved_amt.setScale(0, RoundingMode.FLOOR);
                        modified_amt = modified_amt.setScale(0, RoundingMode.FLOOR);

                        dto.setTranAmt(tran_amt);
                        dto.setBalanceAmt(balance_amt);
                        dto.setApprovedAmt(approved_amt);// 포맷팅
                        dto.setModifiedAmt(modified_amt);

                    } else {
                        BigDecimal approved_amt = new BigDecimal(approvedAmtStr);// 이용금액
                        BigDecimal modified_amt = new BigDecimal(modifiedAmtStr); // 정정후금액
                        approved_amt = approved_amt.movePointRight(2);
                        modified_amt = modified_amt.movePointRight(2);
                        approved_amt = approved_amt.setScale(0, RoundingMode.FLOOR);
                        modified_amt = modified_amt.setScale(0, RoundingMode.FLOOR);

                        dto.setApprovedAmt(approved_amt);
                        dto.setModifiedAmt(modified_amt);
                    }

                    tranList.add(dto);
                    log.info("★★★★★★tranList ★★★★★★ [" + tranList + "]");
                }

                response.setTranList(tranList);
            }

        }
        response.setReqNo(reqNo);
        response.setCardNo(cardNo);
        response.setCardPpId(cardPpId);
        response.setHdFlag(hdFlag);
        response.setCardId(cardId);
        response.setBlingYn(blingYn);
        response.setNextKey((String) scrapingAccountsRspData.get("next_key"));
        response.setConsentMrchntNmYn(consentMrchntNmYn);

        return response;
    }

    @ServiceEndpoint(url = "/getPaymentLastFiveDays", name = "마이데이터 > 현대카드 최근 5일 결제금액 조회 API (as-is service = MA3INQALL010_112S)")
    public GetPaymentLastFiveDaysResponse getPaymentLastFiveDays(IServiceContext ctx,
            GetPaymentLastFiveDaysRequest request)
            throws Exception {

        String orgCd = StringUtils.defaultIfEmpty(request.getOrgCd(), "D1AAFO0000"); // 기관코드 (현대카드)

        // 클라이언트 출력부 DTO 세팅.
        GetPaymentLastFiveDaysResponse response = new GetPaymentLastFiveDaysResponse();
        /*
         * ObjectMapper mapper = new ObjectMapper();
         * List<HashMap<String, Object>> resultRecordList = new
         * ArrayList<HashMap<String, Object>>();
         * 
         * if (!cardArray.equals("")) {
         * try {
         * resultRecordList = mapper.readValue(cardArray, new
         * TypeReference<List<HashMap<String, Object>>>() {
         * });
         * log.debug("########KYM######## size ########### : ",
         * resultRecordList.size());
         * log.debug("########KYM######## resultRecordList ########## : ",
         * resultRecordList);
         * } catch (IOException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         * }
         */
        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        JSONObject APIReqData = new JSONObject();
        APIReqData.put("party_ci_key", party_ci_key); // 고객CI번호
        APIReqData.put("org_cd", orgCd);

        // 현대카드 최근 5일 결제금액 조회
        requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.HYUNDAICARD_FIVEDAYS_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(APIReqData).build();

        JSONObject APIRspData = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();

        if (APIRspData == null || APIRspData.isEmpty()) {
            throw new PRCServiceException("HYUNDAICARD_FIVEDAYS_DATA API IS EMPTY");
        }

        String scrapingAccountsHomeResultType = StringUtils
                .defaultString((String) APIRspData.get(MdcMyDataHelper.RESULT_TYPE));
        String eCode = StringUtils
                .defaultString((String) APIRspData.get("rsp_code"));
        String errMsg = StringUtils
                .defaultString((String) APIRspData.get("rsp_msg"));

        if ("40403".equals(eCode)) {
            response.setECode(eCode);
            response.setErrMsg(errMsg);
            return response;
        }

        if (StringUtils.isBlank(eCode) || !MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
            PRCServiceException appException = new PRCServiceException(eCode);
            appException.setErrorMessage(errMsg);
            throw appException;
        }

        // String is_rt_flg = "N"; // 새로고침 플래그> 새로고침 : Y , 초기 : N
        // String refreshReqTime = "";
        // String signTime = StringUtils.defaultIfEmpty(request.getSignTime(), "");
        // 새로고침 누른 시간

        // 기관코드 있을 경우, 새로고침 flag = Y
        // if (!"".equals(orgCd)) {
        // is_rt_flg = "Y";
        // refreshReqTime = signTime;
        // }

        // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
        Iterator<String> iterator = APIRspData.keySet().iterator();
        while (iterator.hasNext()) {
            String apikey = iterator.next();

            // 최근 5일 결제금액 조회
            if (apikey.equals("fivedays_list") && APIRspData.get("fivedays_list") != null
                    && !APIRspData.get("fivedays_list").equals("")) {

                Integer toDay = DateUtils.getDay(DateUtils.getDate("yyyyMMdd", 'D', 0));
                Integer oneDay = DateUtils.getDay(DateUtils.getDate("yyyyMMdd", 'D', -1));
                Integer twoDay = DateUtils.getDay(DateUtils.getDate("yyyyMMdd", 'D', -2));
                Integer threeDay = DateUtils.getDay(DateUtils.getDate("yyyyMMdd", 'D', -3));
                Integer fourDay = DateUtils.getDay(DateUtils.getDate("yyyyMMdd", 'D', -4));

                Integer[] arr = { fourDay, threeDay, twoDay, oneDay, toDay };

                String strDate = "";
                List<String> strArray = new ArrayList<String>();

                for (int i = 0; i < 5; i++) {

                    if (arr[i].equals(1)) {
                        strDate = "일";
                    } else if (arr[i].equals(2)) {
                        strDate = "월";
                    } else if (arr[i].equals(3)) {
                        strDate = "화";
                    } else if (arr[i].equals(4)) {
                        strDate = "수";
                    } else if (arr[i].equals(5)) {
                        strDate = "목";
                    } else if (arr[i].equals(6)) {
                        strDate = "금";
                    } else {
                        strDate = "토";
                    }

                    strArray.add(strDate);
                }

                String numToDay = DateUtils.getDate("yyyyMMdd", 'D', 0);
                String numOneDay = DateUtils.getDate("yyyyMMdd", 'D', -1);
                String numTwoDay = DateUtils.getDate("yyyyMMdd", 'D', -2);
                String numThreeDay = DateUtils.getDate("yyyyMMdd", 'D', -3);
                String numFourDay = DateUtils.getDate("yyyyMMdd", 'D', -4);

                String[] day = { numFourDay, numThreeDay, numTwoDay, numOneDay, numToDay };
                List<String> dayArr = new ArrayList<String>();

                for (int i = 0; i < 5; i++) {
                    dayArr.add(day[i]);
                }

                JSONArray jsonArray = (JSONArray) APIRspData.get("fivedays_list");

                List<FiveDaysInfoDto> fiveDaysList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject((i));
                    FiveDaysInfoDto dto = new FiveDaysInfoDto();

                    String useAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("use_amt"), ""),
                            "0");
                    BigDecimal use_amt = new BigDecimal(useAmtStr); // 이용금액

                    dto.setUseAmt(use_amt);
                    dto.setAmtCnt(String.valueOf(obj.get("amt_cnt")));
                    dto.setUseDt(String.valueOf(obj.get("use_dt")));

                    fiveDaysList.add(dto);

                }

                response.setDayArr(dayArr);
                response.setStrArray(strArray);
                response.setFivedaysList(fiveDaysList);
                response.setInTimestamp((String) APIRspData.get("in_timestamp"));
                response.setCurrDate(DateUtils.getCurrentDate());
                response.setFiveDays(DateUtils.getDate("yyyyMMdd", 'D', -4));
            }
        }

        return response;

    }

    @ServiceEndpoint(url = "/getMostUsedThreeMonth", name = "마이데이터 > 현대카드 최근 3개월 결제금액 조회 API (as-is service = MA3INQALL010_112S)")
    public GetMostUsedThreeMonthResponse getMostUsedThreeMonth(IServiceContext ctx,
            GetMostUsedThreeMonthRequest request)
            throws Exception {

        String orgCd = StringUtils.defaultIfEmpty(request.getOrgCd(), "D1AAFO0000"); // 기관코드 (현대카드)

        // 클라이언트 출력부 DTO 세팅.
        GetMostUsedThreeMonthResponse response = new GetMostUsedThreeMonthResponse();

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;
        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        JSONObject APIReqData = new JSONObject();
        APIReqData.put("party_ci_key", party_ci_key); // 고객CI번호
        APIReqData.put("org_cd", orgCd);

        // 현대카드 가장 많이 쓴 3일 조회
        requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.HYUNDAICARD_THREEMONTH_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(APIReqData).build();

        JSONObject APIRspData = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();

        if (APIRspData == null || APIRspData.isEmpty()) {
            throw new PRCServiceException("HYUNDAICARD_THREEMONTH_DATA API IS EMPTY");
        }

        String scrapingAccountsHomeResultType = (String) APIRspData.get(MdcMyDataHelper.RESULT_TYPE);
        String eCode = (String) APIRspData.get("rsp_code");
        String errMsg = (String) APIRspData.get("rsp_msg");

        if ("40403".equals(eCode)) {
            response.setECode(eCode);
            response.setErrMsg(errMsg);
            return response;
        }

        if (StringUtils.isBlank(eCode) || !MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
            PRCServiceException appException = new PRCServiceException(eCode);
            appException.setErrorMessage(errMsg);
            throw appException;
        }

        // JSON Obejct 의 키 밸류 뽑아서 output에 저장.
        Iterator<String> iterator = APIRspData.keySet().iterator();
        while (iterator.hasNext()) {
            String apikey = iterator.next();

            // 가장 많이 쓴 3개월 조회
            if (apikey.equals("three_month_list") && APIRspData.get("three_month_list") != null
                    && !APIRspData.get("three_month_list").equals("")) {

                JSONArray jsonArray = (JSONArray) APIRspData.get("three_month_list");
                List<ThreeMonthInfoDto> threeMonthList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject((i));
                    ThreeMonthInfoDto dto = new ThreeMonthInfoDto();

                    String useAmtStr = StringUtils.defaultIfBlank(Objects.toString(obj.get("use_amt"), ""),
                            "0");

                    BigDecimal use_amt = new BigDecimal(useAmtStr); // 이용금액
                    use_amt = use_amt.setScale(0, RoundingMode.FLOOR);
                    dto.setUseAmt(use_amt);
                    dto.setUseDt(String.valueOf(obj.get("use_dt")));

                    threeMonthList.add(dto);
                }
                response.setThreeMonthList(threeMonthList);
            }
        }

        return response;

    }

    @ServiceEndpoint(url = "/getMostSpentLastMonth", name = "마이데이터 > 현대카드 지난달 가장 많이 쓴 가맹점 및 금액 조회 API(as-is service = MA3INQALL010_112S)")
    public GetMostSpentLastMonthResponse getMostSpentLastMonth(IServiceContext ctx,
            GetMostSpentLastMonthRequest request)
            throws Exception {

        String orgCd = StringUtils.defaultIfEmpty(request.getOrgCd(), "D1AAFO0000"); // 기관코드 (현대카드)
        // 클라이언트 출력부 DTO 세팅.
        GetMostSpentLastMonthResponse response = new GetMostSpentLastMonthResponse();

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        JSONObject APIReqData = new JSONObject();
        APIReqData.put("party_ci_key", party_ci_key); // 고객CI번호
        APIReqData.put("org_cd", orgCd);

        // 현대카드 지난달 가장많이쓴 가맹점(금액) 조회
        requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.HYUNDAICARD_LASTMONTH_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken())
                .bodyParameters(APIReqData).build();

        JSONObject APIRspData = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();

        if (APIRspData == null || APIRspData.isEmpty()) {
            throw new PRCServiceException("HYUNDAICARD_LASTMONTH_DATA API IS EMPTY");
        }
        String mrchnt_nm = (String) APIRspData.get("mrchnt_nm");
        String use_amt = (String) APIRspData.get("use_amt");

        response.setMrchntNm(mrchnt_nm);// 가맹점명
        response.setUseAmt(use_amt);// 이용금액
        response.setPrevMonth(DateUtils.getDate("YYYYMMDD", 'M', -1).substring(4, 6));

        return response;

    }

    @ServiceEndpoint(url = "/getCardBasicInfo", name = "마이데이터 > 현대카드 카드기본정보조회 API(as-is service = MA3INQALL010_114S)")
    public GetCardBasicInfoResponse getCardBasicInfo(IServiceContext ctx,
            GetCardBasicInfoRequest request)
            throws Exception {

        String orgCd = StringUtils.defaultIfEmpty(request.getOrgCd(), "D1AAFO0000"); // 기관코드 (현대카드)
        String reqNo = StringUtils.defaultIfEmpty(request.getReqNo(), ""); // req_no
        String ppId = StringUtils.defaultIfEmpty(request.getPpId(), ""); // 선불카드 식별자
        String cardId = StringUtils.defaultIfEmpty(request.getCardId(), ""); // 카드 id
        String isHCD = StringUtils.defaultIfEmpty(request.getIsHCD(), ""); // 전체 > 현대카드 진입 플래그

        // 클라이언트 출력부 DTO 세팅.
        GetCardBasicInfoResponse response = new GetCardBasicInfoResponse();

        mdcMyDataApiClient.init();
        MyDataHttpRequestEntity requestEntity;

        String L_USER_CI_INFO = sessionManager.getLoginValue("USER_CI_INFO", String.class);
        String G_USER_CI_INFO = sessionManager.getGlobalValue("USER_CI_INFO", String.class);
        String party_ci_key = StringUtils.defaultIfEmpty(L_USER_CI_INFO, G_USER_CI_INFO); // 고객CI번호

        JSONObject APIReqData = new JSONObject();
        APIReqData.put("party_ci_key", party_ci_key); // 고객CI번호
        APIReqData.put("org_cd", orgCd);
        APIReqData.put("req_no", reqNo);
        APIReqData.put("pp_id", ppId);
        APIReqData.put("card_id", cardId);

        // 카드 기본정보 조회
        requestEntity = MyDataHttpRequestEntityBuilder.builder(MdcMyDataHelper.CARDBASICINFOR_DATA)
                .accessToken(mdcMyDataApiClient.getAuth().getAccessToken()).bodyParameters(APIReqData).build();

        JSONObject APIRspData = mdcMyDataApiClient.execute(requestEntity).throwException().getAsJsonObject();

        if (APIRspData == null || APIRspData.isEmpty()) {
            throw new PRCServiceException("CARDBASICINFOR_DATA IS EMPTY");
        }

        String scrapingAccountsHomeResultType = (String) APIRspData.get(MdcMyDataHelper.RESULT_TYPE);
        String eCode = (String) APIRspData.get("rsp_code");
        String errMsg = (String) APIRspData.get("rsp_msg");

        if ("40403".equals(eCode)) {
            response.setRspCode(eCode);
            response.setRspMsg(errMsg);
            return response;
        }

        if (StringUtils.isBlank(eCode) || !MdcMyDataHelper.SUCCESS_RESULT_TYPE.equals(scrapingAccountsHomeResultType)) {
            PRCServiceException appException = new PRCServiceException(eCode);
            appException.setErrorMessage(errMsg);
            throw appException;
        }
        response.setIsHCD(isHCD);
        response.setIssueDt(StringUtils.defaultIfEmpty((String) APIRspData.get("issue_dt"), "")); // 발급일

        return response;

    }

}
