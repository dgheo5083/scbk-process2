package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.ProductTermsInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.ProductTermsInfoResult;

@DaoComponent(database = "kfbdb", description = "상품 약관 정보", author = "2034263")
public interface PrdctPrvsnMgtDao {

    @ComponentOperation(name = "상품타입에 따라 상품 약관정보 조회", description = "PMS2.0 연동하여 상품설명서 및 약관 PDF 가져오기(상품군별로 분기처리하고 예외상황일경우 PRDCT_ID를 넘겨서 강제로 가져오기)")
    ProductTermsInfoResult selectProductTermsInfoByPrdctType(ProductTermsInfoParameter parameter);

    @ComponentOperation(name = "상품 약관정보 조회", description = "공공데이터 PMS2.0 상품설명서 및 약관 PDF 가져오기")
    ProductTermsInfoResult selectProductTermsInfo(ProductTermsInfoParameter parameter);
}
