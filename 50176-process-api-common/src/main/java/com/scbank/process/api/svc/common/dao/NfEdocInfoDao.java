package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.EdocInfoResult;

@DaoComponent(database = "kfbdb", description = "전자문서 정보", author = "951303")
public interface NfEdocInfoDao {

    @ComponentOperation(name = "전자문서 제목 조회", description = "정의된 상품 코드 데이터 조회")
    String selectEdocNm(String edocCd);

    @ComponentOperation(name = "전자문서 정보 조회", description = "전자문서정보 조회")
    EdocInfoResult selectEdocInfo(String edocCd);
}
