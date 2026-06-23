package com.scbank.process.api.fw.base.integration.log.dao;

import com.scbank.process.api.fw.base.integration.log.dao.dto.MciLogParameter;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;

/**
 * MCI 전문로그 테이블 적재 Dao
 */
@DaoComponent(database = "kfbdb", name = "MCI 전문로그 테이블 적재 Dao")
public interface MciLogDao {

	@ComponentOperation(name = "MCI/EDMI 전문로그를 테이블 적재한다.")
	void insertMciLog(MciLogParameter parameter);
}
