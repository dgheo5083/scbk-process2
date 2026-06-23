package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScreenAndScrapingInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScreenAndScrapingInfoResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterNonFaceScreenInfoParameter;


@DaoComponent(database = "kfbdb", description = "비대면화면정보관리", author = "김진수")
public interface NfScreenInfoMgtDao {
	@ComponentOperation(name = "비대면 화면정보관리 등록", description = "비대면 화면정보관리 등록 (asis : NF_SCRN_INFO_MGT_I_01)")
    int insertNonFaceScreenInfoManagement(RegisterNonFaceScreenInfoParameter parameter);

    @ComponentOperation(name = "비대면 화면정보 및 스크래핑 정보 조회", description = "비대면 화면정보 및 스크래핑 정보 조회 (asis : NF_SCRN_INFO_MGT_S_01)", author = "김진수")
    NonFaceScreenAndScrapingInfoResult selectScreenAndScrapingInfo(NonFaceScreenAndScrapingInfoParameter parameter);
}
