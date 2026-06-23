package com.scbank.process.api.svc.shared.dao;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.Ma3MydataApiTokenParameter;
import com.scbank.process.api.svc.shared.dao.dto.Ma3MydataApiTokenResult;

@DaoComponent(database = "kfbdb", name = "마이데이터 API 토큰 DAO")
public interface Ma3MydataApiTokenDao {

    /**
     * 유효한 마이데이터 API 토큰 정보를 가져온다.
     * 
     * @param scope
     */
    @ComponentOperation(name = "유효한 마이데이터 API 토큰 정보 획득", description = "유효한 마이데이터 API 토큰 정보를 가져온다.")
    Ma3MydataApiTokenResult selectToken(@Param("scope") String scope);

    @ComponentOperation(name = "마이데이터 API 토큰 활성화", description = "마이데이터 API 토큰 활성화")
    void enabledToken(Ma3MydataApiTokenParameter parameter);

    @ComponentOperation(name = "마이데이터 API 토큰 폐기", description = "마이데이터 API 토큰 폐기")
    void disabledToken(@Param("scope") String scope);
}
