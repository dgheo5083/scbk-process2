package com.scbank.process.api.svc.shared.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.account.dto.OpenBankUserInfoParameter;
import com.scbank.process.api.svc.shared.components.account.dto.OpenBankUserInfoResult;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankAccountMgtListParameter;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankAccountMgtListResult;

/**
 * 오픈뱅킹 사용자 DAO
 */
@DaoComponent(id = "OpenBankUserMgtDao", database = "kfbdb", description = "오픈뱅킹 사용자", author = "김기주")
public interface OpenBankUserMgtDao {

    @ComponentOperation(name = "오픈뱅킹: 사용자 조회", description = "오픈뱅킹: 사용자 조회", author = "김기주")
    OpenBankUserInfoResult selectOpenBankUser(OpenBankUserInfoParameter parameter);

    @ComponentOperation(name = "오픈뱅킹: 등록된타행출금계좌 목록 조회", description = "오픈뱅킹: 등록된타행출금계좌 목록 조회", author = "김기주")
    List<OpenBankAccountMgtListResult> selectOpenBankAcctMgtList(OpenBankAccountMgtListParameter parameter);

}
