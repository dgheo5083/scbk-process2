package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.FaceRecgParameter;

@DaoComponent(database = "kfbdb", description = "안면인식정보", author = "2038565")
public interface FaceRecgTblDao {
	
	@ComponentOperation(name = "안면인식 정보 등록", description = "안면인식 정보 등록", author = "")
	int insertFaceRecg(FaceRecgParameter param);
}
