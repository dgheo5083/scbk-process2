package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecApiLogInsertParameter;

@DaoComponent(database = "kfbdb", name = "오픈뱅킹 API 통신로그 DAO")
public interface FaceRecApiLogDao {
	
	/**
     * 안면인식 API 통신로그를 적재한다.
     * @param parameter {@link FaceRecApiLogInsertParameter}
     */
    @ComponentOperation(name = "오픈뱅킹 API 통신로그를 적재한다.")
	int insertFaceRecApiLog(FaceRecApiLogInsertParameter param);
}
