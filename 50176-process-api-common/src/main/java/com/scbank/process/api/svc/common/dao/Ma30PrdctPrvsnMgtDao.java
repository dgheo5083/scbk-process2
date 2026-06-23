package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoResult;

@DaoComponent(database = "kfbdb", description = "약관 정보", author = "2034263")
public interface Ma30PrdctPrvsnMgtDao {

    @ComponentOperation(name = "약관정보 조회", description = "약관정보 가져오기")
    List<TermsInfoResult> selectTermsInfo(TermsInfoParameter parameter);
}
