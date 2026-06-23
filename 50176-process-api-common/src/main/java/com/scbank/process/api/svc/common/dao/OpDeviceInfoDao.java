package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.FidoLogInfoParameter;

@DaoComponent(database = "kfbdb", description = "Fido Log", author = "2034263")
public interface OpDeviceInfoDao {

    @ComponentOperation(name = "디지털인증서 Fido Log 적재", description = "디지털인증서 Fido Log 적재")
    void insertFidoLogInfo(FidoLogInfoParameter parameter);

}
