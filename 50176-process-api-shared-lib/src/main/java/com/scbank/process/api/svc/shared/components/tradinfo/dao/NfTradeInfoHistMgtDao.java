package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceTradeInfoHistoryParameter;


@DaoComponent(database = "kfbdb", description = "거래정보이력관리", author = "김진수")
public interface NfTradeInfoHistMgtDao {

	@ComponentOperation(name = "진행상태 History 등록", description = "진행상태 History 등록 (as0is : NF_TRADINFO_HIST_MGT_I_01)")
    int insertTradInfoHist(NonFaceTradeInfoHistoryParameter parameter);
}
