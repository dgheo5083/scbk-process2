package com.scbank.process.api.svc.shared.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserParameter;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;

@DaoComponent(database = "ipinside", name = "PC 지정서비스 DAO")
public interface DeviceAuthUserDao {

    @ComponentOperation(name = "단말기 지정 서비스 등록건수 조회")
    int selectDeviceAuthCount(DeviceAuthUserParameter params);

    @ComponentOperation(name = "단말기 지정 서비스 등록정보 조회")
    List<DeviceAuthUserResult> selectDeviceAuthInfo(DeviceAuthUserParameter params);

    @ComponentOperation(name = "단말기 지정 서비스 등록 + 다른 단말 허용여부 조회")
    DeviceAuthUserResult selectDeviceAuthUserInfo(DeviceAuthUserParameter params);

    @ComponentOperation(name = "전자금융사기예방 단말 등록")
    int insertDeviceAuthSvc(DeviceAuthUserParameter params);

    @ComponentOperation(name = "전자금융사기예방 미지정 단말 거래 허용으로 변경")
    int updateAllowOtherDeviceAuthUser(DeviceAuthUserParameter params);

    @ComponentOperation(name = "전자금융사기예방 미지정 단말 거래 허용불가로 변경 ")
    int updateBlockOtherDeviceAuthUser(DeviceAuthUserParameter params);

    @ComponentOperation(name = "전자금융사기예방 사용자정보 등록(미지정단말 허용)")
    int insertAllowOtherDeviceAuthUser(DeviceAuthUserParameter params);

    @ComponentOperation(name = "전자금융사기예방 사용자정보 등록(미지정단말 허용불가)")
    int insertBlockOtherDeviceAuthUser(DeviceAuthUserParameter params);
}
