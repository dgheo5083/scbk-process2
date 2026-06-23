package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankApiTokenEnabledTokenParameter;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankApiTokenSelectTokenResult;

/**
 * 금결원 오픈뱅킹 API 토큰 관리(조회/활성화/폐기) DAO
 */
@DaoComponent(database = "kfbdb", name = "금결원 오픈뱅킹 API 토큰 관리 DAO")
public interface OpenBankApiTokenDao {

    /**
     * 유효한 API토큰정보 획득
     * 
     * @return USE_YN = 'Y' 인 API 토큰 정보 조회 결과값
     */
    @ComponentOperation(name = "")
    OpenBankApiTokenSelectTokenResult selectToken();

    /**
     * API토근 활성화
     */
    @ComponentOperation(name = "")
    void enabledToken(OpenBankApiTokenEnabledTokenParameter parameter);

    /**
     * API토근 폐기 처리
     */
    @ComponentOperation(name = "")
    void disabledToken();
}
