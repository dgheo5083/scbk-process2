package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScrapingInfoParameter;

@DaoComponent(database = "kfbdb", description = "스크래핑관리", author = "김진수")
public interface NfScrapingMgtDao {

	@ComponentOperation(name = "스크래핑데이터 저장", description = "스크래핑데이터 저장 (asis : NF_SCRPPNG_MGT_U_01)", author = "김진수")
    int updateScrapingInfo(NonFaceScrapingInfoParameter parameter);
}
