package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.DetailNoticeRecordParameter;
import com.scbank.process.api.svc.common.dao.dto.DetailNoticeRecordResult;
import com.scbank.process.api.svc.common.dao.dto.ListNoticeRecordParameter;
import com.scbank.process.api.svc.common.dao.dto.ListNoticeRecordResult;
import com.scbank.process.api.svc.common.dao.dto.NoticeParameter;
import com.scbank.process.api.svc.common.dao.dto.NoticeResult;
import com.scbank.process.api.svc.common.dao.dto.RecordCountParameter;
import com.scbank.process.api.svc.common.dao.dto.RecordCountResult;

@DaoComponent(database = "kfbdb", description = "공지사항 조회")
public interface Ma30BbsNoticeMgtDao {

    @ComponentOperation(name = "공지사항 조회", description = "공지사항 조회", author = "951301")
    List<NoticeResult> selectNoticeList(NoticeParameter parameter);

    @ComponentOperation(name = "공지사항 레코드 개수 조회", description = "공지사항 레코드 개수 조회")
    RecordCountResult selectRecordCount(RecordCountParameter parameter);

    @ComponentOperation(name = "공지사항 목록 조회", description = "공지사항 목록 조회")
    List<ListNoticeRecordResult> selectListNoticeRecord(ListNoticeRecordParameter parameter);

    @ComponentOperation(name = "공지사항 상세 조회", description = "공지사항 상세 조회")
    List<DetailNoticeRecordResult> selectDetailNoticeRecord(DetailNoticeRecordParameter parameter);

    @ComponentOperation(name = "공지사항 조회수 카운트", description = "공지사항 조회수 카운트")
    int updateNoticeCount(DetailNoticeRecordParameter parameter);

}
