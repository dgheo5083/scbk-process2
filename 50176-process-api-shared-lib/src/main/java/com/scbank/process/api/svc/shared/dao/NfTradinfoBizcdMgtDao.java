package com.scbank.process.api.svc.shared.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.TradInfoBizCdMgtParam;

@DaoComponent(database = "kfbdb", description = "거래정보 업무코드 관리", author = "2038565")
public interface NfTradinfoBizcdMgtDao {
	
	@ComponentOperation(name = "거래정보 업무코드 테이블 삭제", description = "거래정보 업무코드 테이블 삭제")
	Integer deleteTradInfoBizCd(TradInfoBizCdMgtParam parameter);
	
	@ComponentOperation(name = "거래정보 업무코드 테이블 등록", description = "거래정보 업무코드 테이블 등록")
	Integer insertTradInfoBizCd(TradInfoBizCdMgtParam parameter);
}
