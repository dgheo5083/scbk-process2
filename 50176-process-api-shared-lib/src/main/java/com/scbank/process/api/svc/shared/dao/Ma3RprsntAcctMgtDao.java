package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.RepresentAccountResult;

@DaoComponent(database = "kfbdb", description = "대표계좌관리", author = "2034263")
public interface Ma3RprsntAcctMgtDao {

    @ComponentOperation(name = "대표계좌정보 조회", description = "userId로 대표계좌정보 조회")
    RepresentAccountResult selectRepresentAccount(String userId);

    @ComponentOperation(name = "대표계좌정보 삭제", description = "userId로 대표계좌정보 삭제")
    int deleteRepresentAccount(String userId);
}
