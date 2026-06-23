package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmCountParameter;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmParameter;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmResult;

@DaoComponent(database = "kfbdb", description = "영업점/ATM찾기", author = "송지섭")
public interface EmapBranchInfoDao {

    @ComponentOperation(name = "영업점/ATM 데이터 수", description = "영업점/ATM 데이터 수", author = "송지섭")
    int selectFindBranchAtmCount(FindBranchAtmCountParameter param);

    @ComponentOperation(name = "영업점 찾기(전체)", description = "영업점 찾기(전체)", author = "송지섭")
    List<FindBranchAtmResult> selectFindBranchAtm(FindBranchAtmParameter param);

}
