package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NfScrnInfoInqiryParam;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NfScrnInfoInqiryReult;

@DaoComponent(database = "kfbdb", description = "비대면인증 화면정보관리", author = "")
public interface NfScrnInfoMgtDao {
	@ComponentOperation(name = "비대면 화면정보 조회", description = "비대면 화면정보 조회")
	NfScrnInfoInqiryReult selectNfScrnInfo(NfScrnInfoInqiryParam param);
}
