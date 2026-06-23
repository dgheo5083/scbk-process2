package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.PmsNoticeResult;

@DaoComponent(database = "kfbdb", description = "공지사항관리", author = "2034236")
public interface Ma30FwNoticeMgtDao {

    @ComponentOperation(name = "PMS 공지조회", description = "PMS 공지조회")
    PmsNoticeResult selectPmsNotice(String ctrgyCd);
}
