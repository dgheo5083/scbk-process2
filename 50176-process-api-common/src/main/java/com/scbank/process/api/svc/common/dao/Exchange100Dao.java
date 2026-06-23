package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.SelectExchangeInfoResult;

@DaoComponent(id = "Exchange100Dao", database = "kfbdb", description = "환율 PUSH", author = "김기주")
public interface Exchange100Dao {

    @ComponentOperation(name = "환율조회 (한글)", description = "환율조회 (한글)")
    List<SelectExchangeInfoResult> selectExchangeInfo();

}
