package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.CddElectronicDocumentParameter;

@DaoComponent(database = "kfbdb", description = "전지거래복구관리", author = "김진수")
public interface NfCddEdocRcvryMgtDao {
	@ComponentOperation(name = "CDD전자문서 정보 등록", description = "CDD전자문서 정보 등록 (asis : NF_EDOC_RCVRY_MGT_I_01)")
    int insertCddElectronicDocumentInfo(CddElectronicDocumentParameter parameter);
}
