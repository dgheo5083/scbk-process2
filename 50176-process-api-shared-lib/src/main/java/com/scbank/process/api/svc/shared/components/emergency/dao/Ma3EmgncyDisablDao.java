package com.scbank.process.api.svc.shared.components.emergency.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.emergency.dao.dto.Ma3EmgncyDisablResult;

@DaoComponent(database = "kfbdb", name = "긴급장애테이블 Dao", author = "sungdon.choi")
public interface Ma3EmgncyDisablDao {

	/**
	 * 긴급 장애 목록조회
	 * @return 긴급 장애 목록조회 결과
	 */
	@ComponentOperation(name = "긴급 장애 목록조회", author = "sungdon.choi")
	List<Ma3EmgncyDisablResult> selectMa3EmgncyDisablList();
}
