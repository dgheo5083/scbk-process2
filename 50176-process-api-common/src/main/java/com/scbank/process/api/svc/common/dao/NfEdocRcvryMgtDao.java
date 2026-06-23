package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.EdocRecoveryInfoParameter;

@DaoComponent(database = "kfbdb", description = "전자문서", author = "2034263")
public interface NfEdocRcvryMgtDao {

    @ComponentOperation(name = "전자문서복구정보저장", description = "전자문서복구정보저장")
    void insertEdocRecoveryInfo(EdocRecoveryInfoParameter parameter);

}
