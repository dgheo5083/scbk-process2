package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.BranchNameResult;

@DaoComponent(database = "kfbdb", description = "영업점 정보", author = "")
public interface TblBranchDao {
	@ComponentOperation(name = "영업점명 조회", description = "영업점명 조회")
	List<BranchNameResult> selectBranchName(List<String> branchCodes);
}
