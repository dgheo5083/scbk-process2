package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupCertListDataParameter;

@DaoComponent(database = "kfbdb", description = "디지털 인증서 관리", author = "이완주")
public interface FidoCertificateInfoDao {

	@ComponentOperation(name = "디지털인증서 시리얼정보 틀린 인증서 파기", description = "디지털인증서 시리얼정보 틀린 인증서 파기", author = "이완주")
	int updateExpiredCertificateIssued(String serialNumber); // FIDO_EXPIRED_CERTIFICATE_ISSUED
	
	@ComponentOperation(name = "디지털인증서 Backup 정보 등록", description = "디지털인증서 Backup 정보 등록", author = "이완주")
	int insertFidoBackupCertListData(FidoBackupCertListDataParameter parameter); // FIDO_BACKUP_CERTLIST_DATA_I

}

