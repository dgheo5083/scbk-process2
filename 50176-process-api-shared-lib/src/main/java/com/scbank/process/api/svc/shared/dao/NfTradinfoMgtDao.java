package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.NfAuthCompleteYnParameter;
import com.scbank.process.api.svc.shared.dao.dto.NfAuthCompleteYnResult;

@DaoComponent(database = "kfbdb", description = "거래정보관리", author = "2038565")
public interface NfTradinfoMgtDao {
	
	@ComponentOperation(name = "대표계좌정보 조회", description = "userId로 대표계좌정보 조회")
	NfAuthCompleteYnResult selectNfAuthCompleteYn(NfAuthCompleteYnParameter parameter);
}
