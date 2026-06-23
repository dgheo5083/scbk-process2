package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecApiTokenEnabledTokenParameter;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecTokenInfo;

@DaoComponent(database = "kfbdb", name = "오픈뱅킹 API 통신로그 DAO")
public interface FaceRecApiTokenDao {
	
	/**
	 * 안면인식 API 토큰 비활성화
	 */
    @ComponentOperation(name = "안면인식 API 토큰 비활성화")
    void disabledToken();
    
    /**
	 * 안면인식 API 토큰 활성화
	 */
    @ComponentOperation(name = "안면인식 API 토큰 활성화")
    int enabledToken(FaceRecApiTokenEnabledTokenParameter param);
    
    /**
	 * 안면인식 API 토큰 조회
	 */
    @ComponentOperation(name = "안면인식 API 토큰 조회")
    FaceRecTokenInfo selectToken();
}
