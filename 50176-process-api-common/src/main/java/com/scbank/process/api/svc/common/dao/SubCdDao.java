package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.SelectPushServiceCdResult;

@DaoComponent(id = "SubCdDao", database = "kfbdb", description = "공통 코드", author = "김기주")
public interface SubCdDao {

    @ComponentOperation(name = "서비스 동의 항목 및 툴팀 정보 조회", description = "서비스 동의 항목 및 툴팀 정보 조회")

    List<SelectPushServiceCdResult> selectPushServiceCd();

}
