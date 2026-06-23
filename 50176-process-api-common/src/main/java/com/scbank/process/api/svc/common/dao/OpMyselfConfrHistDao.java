package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupInfoResult;

@DaoComponent(database = "kfbdb", description = "Fido Backup", author = "2034263")
public interface OpMyselfConfrHistDao {

    @ComponentOperation(name = "디지털인증서 Backup 사용자 조회", description = "디지털인증서 Backup 사용자 조회")
    FidoBackupInfoResult selectFidoBackupInfo(FidoBackupInfoParameter parameter);

}
