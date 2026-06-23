package com.scbank.process.api.svc.shared.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.MyDataCIInfoResult;
import com.scbank.process.api.svc.shared.dao.dto.MyDataExposureOrgParameter;
import com.scbank.process.api.svc.shared.dao.dto.MyDataExposureOrgResult;

@DaoComponent(id = "MydataOrgMgtDao", database = "kfbdb", description = "마이데이터기관조회", author = "곽동현")
public interface MydataOrgMgtDao {

    @ComponentOperation(name = "이체 입금은행 마이데이터기관조회", description = "이체 입금은행 마이데이터기관조회", author = "곽동현")
    List<MyDataCIInfoResult> selectMyDataCIInfo();

    @ComponentOperation(name = "MYDATA 업권노출 기관코드", description = "MYDATA 업권노출 기관코드(개별인증, 통합인증 선택화면에서만 컨트롤)", author = "김기주")
    List<MyDataExposureOrgResult> selectMyDataExposureOrg(MyDataExposureOrgParameter parameter);

    @ComponentOperation(name = "MYDATA 업권 기관코드", description = "MYDATA 업권 기관코드", author = "김기주")
    List<MyDataExposureOrgResult> selectMyDataOrg();

}
