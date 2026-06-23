package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.GetForeignBranchAtmCountParameter;
import com.scbank.process.api.svc.common.dao.dto.ListForeignBranchAtmParameter;
import com.scbank.process.api.svc.common.dao.dto.ListForeignBranchAtmResult;
import com.scbank.process.api.svc.common.dao.dto.SearchCityParameter;
import com.scbank.process.api.svc.common.dao.dto.SearchCityResult;
import com.scbank.process.api.svc.common.dao.dto.SearchLocalParameter;
import com.scbank.process.api.svc.common.dao.dto.SearchLocalResult;
import com.scbank.process.api.svc.common.dao.dto.SearchNationResult;

@DaoComponent(database = "kfbdb", description = "해외 영업점 카테고리 검색", author = "송지섭")
public interface GlbAtmMgtDao {

    @ComponentOperation(name = "국가 검색", description = "국가 검색", author = "송지섭")
    List<SearchNationResult> selectSearchNation();

    @ComponentOperation(name = "도시 검색", description = "도시 검색", author = "송지섭")
    List<SearchCityResult> selectSearchCity(SearchCityParameter param);

    @ComponentOperation(name = "지역 검색", description = "지역 검색", author = "송지섭")
    List<SearchLocalResult> selectSearchLocal(SearchLocalParameter param);

    @ComponentOperation(name = "해외영업점/ATM 데이터 수", description = "해외영업점/ATM 데이터 수", author = "송지섭")
    int selectGetForeignBranchAtmCount(GetForeignBranchAtmCountParameter param);

    @ComponentOperation(name = "해외영업점/ATM 목록", description = "해외영업점/ATM 목록", author = "송지섭")
    List<ListForeignBranchAtmResult> selectListForeignBranchAtm(ListForeignBranchAtmParameter param);

}
