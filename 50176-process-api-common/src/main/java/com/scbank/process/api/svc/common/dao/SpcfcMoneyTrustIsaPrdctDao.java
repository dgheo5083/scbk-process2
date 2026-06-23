package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.TrustContractDocumentResult;

@DaoComponent(id = "SpcfcMoneyTrustIsaPrdctDao", database = "kfbdb", description = "상품 약관 정보", author = "2038756")
public interface SpcfcMoneyTrustIsaPrdctDao {

    @ComponentOperation(name = "계약서류조회", description = "신탁 개별 계약서류 조회")
    TrustContractDocumentResult selectTrustContractDocument(String trustCd);
}
