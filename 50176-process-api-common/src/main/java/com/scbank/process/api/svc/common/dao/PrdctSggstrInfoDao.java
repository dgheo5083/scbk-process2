package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.BranchInfoResult;
import com.scbank.process.api.svc.common.dao.dto.EmployeeInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EmployeeInfoResult;

@DaoComponent(database = "kfbdb", description = "소개자행번 정보", author = "2034236")
public interface PrdctSggstrInfoDao {

    @ComponentOperation(name = "소개자행번 검색", description = "소개자행번 검색")
    List<EmployeeInfoResult> selectEmployeeList(EmployeeInfoParameter parameter);

    @ComponentOperation(name = "영업점 찾기", description = "영업점 찾기")
    List<BranchInfoResult> selectBranchList(String searchKeyword);
}
