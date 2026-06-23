package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.AddCntrctDocumentResult;

@DaoComponent(id = "PrdctDocListDao", database = "kfbdb", description = "상품 약관 목록 정보", author = "2038756")
public interface PrdctDocListDao {

    @ComponentOperation(name = "계약서류조회", description = "펀드 공통 추가 계약서류 조회")
    List<AddCntrctDocumentResult> selectTrustCommonDocumentList(String input);

    @ComponentOperation(name = "계약서류조회", description = "추가서류 기타 조회")
    List<AddCntrctDocumentResult> selectAppendCommonDocumentList(String input);
}
