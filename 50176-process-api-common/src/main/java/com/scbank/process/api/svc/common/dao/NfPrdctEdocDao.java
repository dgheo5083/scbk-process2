package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.EdocPrdctInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocPrdctInfoResult;

@DaoComponent(database = "kfbdb", description = "전자문서 상품 정보", author = "951303")
public interface NfPrdctEdocDao {

    @ComponentOperation(name = "전자문서 상품 문서코드 중복 조회", description = "전자문서 상품 문서코드 중복 조회")
    List<EdocPrdctInfoResult> selectEdocPrdctInfoList(EdocPrdctInfoParameter parameter);
}
