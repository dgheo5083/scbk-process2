package com.scbank.process.api.svc.common.service.support;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.common.components.BranchAtmComponent;
import com.scbank.process.api.svc.common.components.dto.GetForeignBranchAtmRequest;
import com.scbank.process.api.svc.common.components.dto.GetForeignBranchAtmResponse;
import com.scbank.process.api.svc.common.dao.EmapBranchInfoDao;
import com.scbank.process.api.svc.common.dao.GlbAtmMgtDao;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmCountParameter;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmParameter;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmResult;
import com.scbank.process.api.svc.common.dao.dto.ListForeignBranchAtmParameter;
import com.scbank.process.api.svc.common.dao.dto.ListForeignBranchAtmResult;
import com.scbank.process.api.svc.common.dao.dto.SearchCityParameter;
import com.scbank.process.api.svc.common.dao.dto.SearchCityResult;
import com.scbank.process.api.svc.common.dao.dto.SearchLocalParameter;
import com.scbank.process.api.svc.common.dao.dto.SearchLocalResult;
import com.scbank.process.api.svc.common.dao.dto.SearchNationResult;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindBranchAtmRequest;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindBranchAtmResponse;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindForeignBranchAtmRequest;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindForeignBranchAtmResponse;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindForeignBranchAtmResponse.ForeignBranchAtm;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindForeignBranchCategoryRequest;
import com.scbank.process.api.svc.common.service.support.dto.branchAtm.SupLocFindForeignBranchCategoryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-영업점/ATM찾기", url = "/support/branchAtm", author = "송지섭")
public class SupportBranchAtmService {

    private final EmapBranchInfoDao emapBranchInfoDao;
    private final GlbAtmMgtDao glbAtmMgtDao;

    private final BranchAtmComponent branchAtmComponent;

    /**
     * 영업점/ATM찾기
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3CSTATM005_101S
     */
    @ServiceEndpoint(url = "/findBranchAtm", name = "영업점/ATM찾기 [ASIS:MA3CSTATM005_101S]", author = "송지섭")
    public SupLocFindBranchAtmResponse findBranchAtm(IServiceContext ctx,
            SupLocFindBranchAtmRequest input) {
        SupLocFindBranchAtmResponse response = new SupLocFindBranchAtmResponse();
        String keyword = StringUtils.defaultIfEmpty(input.getKeyword(), "");
        String searchType = StringUtils.defaultIfEmpty(input.getType(), "");
        String currPage = StringUtils.defaultIfEmpty(input.getCurrPage(), "1");
        String moreInfo = "N";
        int listCountPerPage = Integer.parseInt(StringUtils.defaultIfEmpty(input.getListCountPerPage(), "10"));
        int start = 0;
        int end = 0;

        /* 영업점/ATM 데이터 수 조회 */
        FindBranchAtmCountParameter param = new FindBranchAtmCountParameter();
        param.setKeyword(keyword);
        param.setSubType(searchType);
        int totalRowCount = emapBranchInfoDao.selectFindBranchAtmCount(param);
        List<FindBranchAtmResult> findBranchAtmResultList = new ArrayList<FindBranchAtmResult>();
        if (totalRowCount > 0) {
            start = (Integer.parseInt(currPage) - 1) * listCountPerPage;
            end = (start + listCountPerPage < totalRowCount) ? start + listCountPerPage : totalRowCount;
            moreInfo = (end < totalRowCount) ? "Y" : "N";

            /* 영업점/ATM 찾기 조회 */
            FindBranchAtmParameter findBranchAtmParameter = new FindBranchAtmParameter();
            findBranchAtmParameter.setKeyword(keyword);
            findBranchAtmParameter.setSubType(searchType);
            findBranchAtmParameter.setPageSize(listCountPerPage);
            findBranchAtmParameter.setPaging(Integer.parseInt(currPage));
            findBranchAtmResultList = emapBranchInfoDao
                    .selectFindBranchAtm(findBranchAtmParameter);
        }
        response.setFindBranchAtmResultList(findBranchAtmResultList);
        response.setMoreInfoYn(moreInfo);

        return response;
    }

    /**
     * 해외 영업점 카테고리 검색
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3CSTATM005_108S
     */
    @ServiceEndpoint(url = "/findForeignBranchCategory", name = "해외 영업점 카테고리 검색 [ASIS:MA3CSTATM005_108S]", author = "송지섭")
    public SupLocFindForeignBranchCategoryResponse findForeignBranchCategory(IServiceContext ctx,
            SupLocFindForeignBranchCategoryRequest input) {
        SupLocFindForeignBranchCategoryResponse response = new SupLocFindForeignBranchCategoryResponse();
        String NTNL = StringUtils.defaultIfEmpty(input.getNtnl(), "");
        String CITY = StringUtils.defaultIfEmpty(input.getCity(), "");
        String searchKey = StringUtils.defaultIfEmpty(input.getSearchKey(), "");

        List<SearchNationResult> searchNationList = new ArrayList<>();
        List<SearchCityResult> searchCityList = new ArrayList<>();
        List<SearchLocalResult> searchLocalList = new ArrayList<>();

        // 국가 검색
        if (("1").equals(searchKey)) {
            searchNationList = glbAtmMgtDao.selectSearchNation();
        }

        // 도시 검색
        if (("2").equals(searchKey)) {
            SearchCityParameter param = new SearchCityParameter();
            param.setNtnl(NTNL);
            searchCityList = glbAtmMgtDao.selectSearchCity(param);
        }

        // 지역 검색
        if (("3").equals(searchKey)) {
            SearchLocalParameter param = new SearchLocalParameter();
            param.setNtnl(NTNL);
            param.setCity(CITY);
            searchLocalList = glbAtmMgtDao.selectSearchLocal(param);
        }

        response.setSearchNationList(searchNationList);
        response.setSearchCityList(searchCityList);
        response.setSearchLocalList(searchLocalList);

        return response;
    }

    /**
     * 해외 영업점/ATM 찾기
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3CSTATM005_106S
     */
    @ServiceEndpoint(url = "/findForeignBranchAtm", name = "해외 영업점/ATM 찾기 [ASIS:MA3CSTATM005_106S]", author = "송지섭")
    public SupLocFindForeignBranchAtmResponse findForeignBranchAtm(IServiceContext ctx,
            SupLocFindForeignBranchAtmRequest input) {
        SupLocFindForeignBranchAtmResponse response = new SupLocFindForeignBranchAtmResponse();

        String keyword = StringUtils.defaultIfEmpty(input.getKeyWord(), "");
        String PAGING = StringUtils.defaultIfEmpty(input.getPageCount(), "1");
        String PAGESIZE = "10";

        String NTNL = StringUtils.defaultIfEmpty(input.getNtnl(), "");
        String CITY = StringUtils.defaultIfEmpty(input.getCity(), "");
        String LOCAL = StringUtils.defaultIfEmpty(input.getLocal(), "");

        int pageNum = Integer.parseInt(PAGING);

        // 해외영업점찾기 count조회 컴포넌트 호출
        GetForeignBranchAtmRequest request = new GetForeignBranchAtmRequest();
        request.setKeyWord(keyword);
        request.setNtnl(NTNL);
        request.setCity(CITY);
        request.setLocal(LOCAL);
        GetForeignBranchAtmResponse getForeignBranchAtmResponse = branchAtmComponent.getForeignBranchAtm(request);
        int countRecord = getForeignBranchAtmResponse.getCountData();

        // 해외영업점 목록 조회
        ListForeignBranchAtmParameter listForeignBranchAtm = new ListForeignBranchAtmParameter();
        listForeignBranchAtm.setKeyWord(keyword);
        listForeignBranchAtm.setPageSize(PAGESIZE);
        listForeignBranchAtm.setPaging(PAGING);
        listForeignBranchAtm.setNtnl(NTNL);
        listForeignBranchAtm.setCity(CITY);
        listForeignBranchAtm.setLocal(LOCAL);
        List<ListForeignBranchAtmResult> resATMSelect = glbAtmMgtDao.selectListForeignBranchAtm(listForeignBranchAtm);
        List<ForeignBranchAtm> foreignBranchAtmList = new ArrayList<ForeignBranchAtm>();
        if (countRecord % 10 != 0 && (countRecord / 10) + 1 >= pageNum) {
            if (resATMSelect.size() > 0) {
                for (int i = 0; i < resATMSelect.size(); i++) {
                    ForeignBranchAtm foreignBranchAtm = new ForeignBranchAtm();

                    String valSERNO = resATMSelect.get(i).getSerno();
                    String valTBL = resATMSelect.get(i).getAcctTbl();
                    String[] latlng = valTBL.split(",");
                    String lat = latlng[0];
                    String lng = latlng[1];
                    String valTYPE = resATMSelect.get(i).getChnnlMk();
                    String valNAME = resATMSelect.get(i).getChnnlNm();
                    String valADDRESS = resATMSelect.get(i).getChnnlAddr();
                    String valTEL = resATMSelect.get(i).getCntctPlce();
                    String valBUSINESSHOURS = resATMSelect.get(i).getCntctPlce();

                    foreignBranchAtm.setSerno(valSERNO); // 고유번호
                    foreignBranchAtm.setLat(lat); // 위도
                    foreignBranchAtm.setLng(lng); // 경도
                    foreignBranchAtm.setType(valTYPE); // 분류(Branch, Security Broker, Priority Banking Service, SME
                                                       // Center, ATM)
                    foreignBranchAtm.setName(valNAME); // 영업점/ATM 명
                    foreignBranchAtm.setAddress(valADDRESS); // 주소
                    foreignBranchAtm.setBusinessHours(valBUSINESSHOURS); // 영업시간
                    foreignBranchAtm.setTel(valTEL); // 영업점번호

                    foreignBranchAtmList.add(foreignBranchAtm);
                }
                response.setAtmSelect(foreignBranchAtmList);
            } else {
                response.setAtmSelect(null);
            }

            if (resATMSelect.size() % 10 == countRecord % 10) {
                response.setMoreData("N");
            } else {
                response.setMoreData("Y");
            }
        }

        response.setPageCount(pageNum);

        if (!"".equals(keyword)) {
            response.setKeyWord(keyword);
        }

        return response;
    }

}
