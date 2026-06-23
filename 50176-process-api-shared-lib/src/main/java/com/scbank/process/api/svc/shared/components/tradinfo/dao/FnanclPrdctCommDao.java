package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.MainDepositAccountProductResult;

@DaoComponent(database = "kfbdb", description = "메인 입출금 계좌 상품")
public interface FnanclPrdctCommDao {

	@ComponentOperation(name = "메인 입출금 계좌 상품 리스트", description = "메인 입출금 계좌 상품 리스트 (asis : FNANCL_PRDCT_CMMN_S_01)")
    List<MainDepositAccountProductResult> selectMainDepositAccountProduct();
}
