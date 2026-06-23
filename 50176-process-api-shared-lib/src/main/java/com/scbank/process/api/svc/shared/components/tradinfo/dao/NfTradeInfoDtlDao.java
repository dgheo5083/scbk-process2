package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceTradeInfoDetailParameter;

@DaoComponent(database = "kfbdb", description = "비대면거래관리테이블상세", author = "김진수")
public interface NfTradeInfoDtlDao {
	
	@ComponentOperation(name = "비대면거래관리테이블상세 등록", description = "비대면거래관리테이블상세 등록 (asis : NF_TRADINFO_DTL_I_01)")
    int insertTradeInfoDetail(NonFaceTradeInfoDetailParameter parameter);

}
