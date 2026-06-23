package com.scbank.process.api.svc.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.BusinessHourParameter;
import com.scbank.process.api.svc.common.dao.dto.BusinessHourResult;
import com.scbank.process.api.svc.common.dao.dto.EventResult;

@DaoComponent(id = "Ma30BbsEventMgtDao", database = "kfbdb", description = "EVENT 조회", author = "951301")
public interface Ma30BbsEventMgtDao {

    @ComponentOperation(name = "EVENT 조회", description = "EVENT 조회")
    List<EventResult> selectEvent(@Param("searchWord") String searchWord, @Param("loctnCd") String loctnCd);

    @ComponentOperation(name = "이용시간 안내 조회", description = "이용시간 안내 조회")
    List<BusinessHourResult> selectBusinessHour(BusinessHourParameter param);
}
