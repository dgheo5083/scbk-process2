package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterPartnerTradeInfoParameter;

@DaoComponent(database = "kfbdb", description = "제휴처거래정보관리", author = "김진수")
public interface NfCnnctnTradInfoMgtDao {
	
	@ComponentOperation(name = "제휴처 거래 정보 등록", description = "제휴처 거래 정보 등록 (asis : NF_CNNCTN_TRADINFO_MGT_I_01)")
    int insertPartnerTransactionInfo(RegisterPartnerTradeInfoParameter parameter);

    @ComponentOperation(name = "제휴처 거래완료 업데이트", description = "제휴처 거래완료 업데이트 (asis : NF_CNNCTN_TRADINFO_MGT_U_01)")
    int updatePartnerTransactionComplete(RegisterPartnerTradeInfoParameter parameter);

}
