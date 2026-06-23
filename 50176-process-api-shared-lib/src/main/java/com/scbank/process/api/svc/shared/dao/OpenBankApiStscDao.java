package com.scbank.process.api.svc.shared.dao;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;

@DaoComponent(database = "kfbdb", name = "금결원 오픈뱅킹 API 통계 DAO")
public interface OpenBankApiStscDao {

    /**
     * 오픈뱅킹 API LOG 상태 통계 등록
     * 
     * @param tradDate 거래일자
     */
    @ComponentOperation(name = "")
    void insertApiLogStsc(@Param("tradDate") String tradDate);

    /**
     * 오픈뱅킹 API LOG 상태 통계 등록
     * 
     * @param tradDate
     */
    @ComponentOperation(name = "")
    void deleteApiLogStsc(@Param("tradDate") String tradDate);
}
