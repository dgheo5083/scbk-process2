package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.IPInsideAboardParameter;

@DaoComponent(database = "kfbdb", name = "IPinside DAO")
public interface IpIntrcptListInfoDao {

    @ComponentOperation(name = "")
    int selectAboardYn(IPInsideAboardParameter params);
}
