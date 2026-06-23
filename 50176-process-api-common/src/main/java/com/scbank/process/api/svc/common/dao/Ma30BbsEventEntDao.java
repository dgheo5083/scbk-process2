package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.EventApplyCountParameter;
import com.scbank.process.api.svc.common.dao.dto.EventApplyParameter;

@DaoComponent(id = "Ma30BbsEventEntDao", database = "kfbdb", description = "EVENT 조회", author = "송지섭")
public interface Ma30BbsEventEntDao {

    @ComponentOperation(name = "이벤트응모건수조회", description = "이벤트응모건수조회")
    int selectEventApplyCount(EventApplyCountParameter param);

    @ComponentOperation(name = "이벤트응모", description = "이벤트응모")
    int insertEventApply(EventApplyParameter param);

}
