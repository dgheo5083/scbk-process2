package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.InsertPushCllctUsageAgrmntHistoryParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertPushCllctUsageAgrmntParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectPushCllctUsageAgrmntResult;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushCllctUsageAgrmntParameter;

@DaoComponent(database = "kfbdb", description = "마케팅 개인정보수집이용동의", author = "김기주")
public interface PushCllctUsageAgrmntDao {

    @ComponentOperation(name = "PUSH_마케팅_개인정보수집이용동의 원장 SELECT", description = "PUSH_마케팅_개인정보수집이용동의 원장 SELECT")
    SelectPushCllctUsageAgrmntResult selectPushCllctUsageAgrmnt(String ssn);

    @ComponentOperation(name = "PUSH_마케팅_개인정보수집이용동의 원장 UPDATE", description = "PUSH_마케팅_개인정보수집이용동의 원장 UPDATE")
    int updatePushCllctUsageAgrmnt(UpdatePushCllctUsageAgrmntParameter parameter);

    @ComponentOperation(name = "PUSH_마케팅_개인정보수집이용동의 원장 INSERT", description = "PUSH_마케팅_개인정보수집이용동의 원장 INSERT")
    int insertPushCllctUsageAgrmnt(InsertPushCllctUsageAgrmntParameter parameter);

    @ComponentOperation(name = "PUSH_마케팅_개인정보수집이용동의 이력 INSERT", description = "PUSH_마케팅_개인정보수집이용동의 이력 INSERT")
    int insertPushCllctUsageAgrmntHistory(InsertPushCllctUsageAgrmntHistoryParameter parameter);

}
