package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.NfCustInfoResult;

@DaoComponent(database = "kfbdb", description = "비대면인증 고객관리", author = "2034236")
public interface NfCustMgtDao {

    @ComponentOperation(name = "비대면인증 고객관리 조회", description = "비대면인증 고객관리 조회")
    NfCustInfoResult selectNfCustInfo(String ssn);
}
