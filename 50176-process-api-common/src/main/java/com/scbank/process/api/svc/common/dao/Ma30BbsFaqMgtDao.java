package com.scbank.process.api.svc.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.FaqResult;

@DaoComponent(id = "Ma30BbsFaqMgtDao", database = "kfbdb", description = "FAQ 조회", author = "951301")
public interface Ma30BbsFaqMgtDao {

    @ComponentOperation(name = "FAQ 조회", description = "FAQ 조회")
    List<FaqResult> selectFaq(@Param("searchWord") String searchWord, @Param("loctnCd") String loctnCd);
}
