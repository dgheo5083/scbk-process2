package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.SelectMydataOrgMgtResult;

@DaoComponent(id = "Ma3MydataOrgMgtDao", database = "kfbdb", description = "MYDATA 업권 기관코드 리스트", author = "")
public interface Ma3MydataOrgMgtDao {

    @ComponentOperation(name = "MYDATA 업권 기관코드 리스트", description = "MYDATA 업권 기관코드 리스트")
    List<SelectMydataOrgMgtResult> selectMyDataOrg();
    // List<Map<String, Object>> selectMyDataOrg();
}
