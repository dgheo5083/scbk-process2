package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.EdocFileInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocFileInfoResult;

@DaoComponent(database = "kfbdb", description = "전자문서 파일 정보", author = "2034263")
public interface NfEdocFileListMgtDao {

	@ComponentOperation(name = "전자문서 파일정보 조회", description = "전자문서 파일목록관리 EDOC_CD 조회결과 가져오기")
	EdocFileInfoResult selectEdocFileInfo(EdocFileInfoParameter parameter);

}