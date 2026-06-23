package com.scbank.process.api.svc.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.ProductFileResult;
import com.scbank.process.api.svc.common.dao.dto.ProductResult;

@DaoComponent(id = "Ma30FnanclPrdctCmmnDao", database = "kfbdb", description = "상품 조회", author = "951301")
public interface Ma30FnanclPrdctCmmnDao {

    @ComponentOperation(name = "상품 조회", description = "상품 조회")
    List<ProductResult> selectProduct(@Param("searchWord") String searchWord, @Param("loctnCd") String loctnCd);

    @ComponentOperation(name = "계약서류조회 사모펀드 핵심상품설명서 파일명 조회", description = "계약서류조회 사모펀드 핵심상품설명서 파일명 조회")
    List<ProductFileResult> selectFundDocumentFile(List<String> fundCodeList);

    @ComponentOperation(name = "[미스터리쇼핑] 펀드코드로 투자설명서, 간이투자설명서 파일명 조회", description = "[미스터리쇼핑] 펀드코드로 투자설명서, 간이투자설명서 파일명 조회")
    List<ProductFileResult> selectFundCodeFile(List<String> fundCodeList);
}