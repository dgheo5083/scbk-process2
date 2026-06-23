package com.scbank.process.api.svc.shared.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.SaleFundInfoResult;

@DaoComponent(database = "kfbdb", name = "펀드계좌관리 DAO")
public interface SaleFundInfoDao {

    @ComponentOperation(name = "")
    List<SaleFundInfoResult> selectSaleFundInfoList(@Param("fundCodeList") List<String> fundCodeList);

}
