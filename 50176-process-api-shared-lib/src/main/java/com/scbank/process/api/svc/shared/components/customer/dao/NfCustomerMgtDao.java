package com.scbank.process.api.svc.shared.components.customer.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoParameter;

@DaoComponent(id = "NfCustomerMgtDao", database = "kfbdb", description = "비대면고객정보관리", author = "")
public interface NfCustomerMgtDao {

	@ComponentOperation(name = "비대면 고객정보 조회", description = "비대면 고객정보 조회 (asis : NF_CUST_MGT_S_01)", author = "김진수")
	NonFaceCustomerInfoInquiryResult selectNonFaceCustomerInfo(NonFaceCustomerInfoParameter parameter);
	
	@ComponentOperation(name = "비대면 고객정보 등록", description = "비대면 고객정보 등록 (asis : NF_CUST_MGT_I_01)", author = "김진수")
    int insertNonFaceCustomerInfo(NonFaceCustomerInfoParameter parameter);
	
	@ComponentOperation(name = "비대면 고객정보 수정", description = "비대면 고객정보 수정 (asis : NF_CUST_MGT_U_01)", author = "김진수")
    int updateNonFaceCustomerInfo(NonFaceCustomerInfoParameter parameter);
}
