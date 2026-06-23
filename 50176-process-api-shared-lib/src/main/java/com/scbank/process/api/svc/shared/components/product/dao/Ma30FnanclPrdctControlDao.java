package com.scbank.process.api.svc.shared.components.product.dao;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.product.dao.dto.PrdctAvailableInfoInqiryResult;

@DaoComponent(database = "kfbdb", description = "상품제어", author = "")
public interface Ma30FnanclPrdctControlDao {
	@ComponentOperation(name = "상품 이용가능시간 조회", description = "상품 이용가능시간 조회(MA30_FNANCL_PRDCT_CONTROL_01_S)", author = "")
	PrdctAvailableInfoInqiryResult selectPrdctAvailableInfo(@Param("prdctCd")String prdctCd);
}
