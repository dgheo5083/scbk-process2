package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.NoticeForPopupResult;

@DaoComponent(database = "kfbdb", description = "공지사항 조회", author = "허정우")
public interface Ma30BbsMyDataItemDao {

    @ComponentOperation(name = "홈 슬라이드 긴급조회 팝업 조회", description = "홈 슬라이드 긴급조회 팝업 조회")
    List<NoticeForPopupResult> selectNoticeForPopup();

}
