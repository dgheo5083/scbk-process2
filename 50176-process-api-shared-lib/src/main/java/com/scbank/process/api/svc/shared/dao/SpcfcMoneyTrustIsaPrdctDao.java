package com.scbank.process.api.svc.shared.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.SpcfcTrustPrdctResult;

@DaoComponent(database = "kfbdb", description = "신탁정보", author = "2034263")
public interface SpcfcMoneyTrustIsaPrdctDao {

    @ComponentOperation(name = "신탁정보 조회", description = "trustCodeList로 신탁정보 조회")
    List<SpcfcTrustPrdctResult> selectSpcfcTrustPrdct(@Param("trustCodeList") List<String> trustCodeList);
}
