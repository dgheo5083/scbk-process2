package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
// import com.scbank.process.api.svc.common.dao.dto.CommonAgreeParameter;
import com.scbank.process.api.svc.common.dao.dto.CommonAgreeResult;
import com.scbank.process.api.svc.common.dao.dto.ContractDocumentParameter;
import com.scbank.process.api.svc.common.dao.dto.ContractDocumentResult;

@DaoComponent(id = "PrdctDisclosureMgtDao", database = "kfbdb", description = "공통 약관", author = "2038756")
public interface PrdctDisclosureMgtDao {

    @ComponentOperation(name = "계약서류조회", description = "공통 약관 조회")
    CommonAgreeResult selectCommonAgree(String parameter);

    @ComponentOperation(name = "계약서류조회", description = "계약서류 조회")
    ContractDocumentResult selectContractDocument(ContractDocumentParameter parameter);
}
