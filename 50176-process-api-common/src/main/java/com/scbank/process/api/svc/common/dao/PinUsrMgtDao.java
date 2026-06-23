package com.scbank.process.api.svc.common.dao;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtByDeviceResult;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtParameter;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtUpdateParameter;

@DaoComponent(database = "kfbdb", description = "PinUsrMgtDao", author = "이완주")
public interface PinUsrMgtDao {

    @ComponentOperation(name = "selectCountPinUsrMgtByDeviceId", description = "selectCountPinUsrMgtByDeviceId", author = "이완주")
    int selectCountPinUsrMgtByDeviceId(String deviceId); // SELECT_COUNT_PIN_USR_MGT

    @ComponentOperation(name = "updatePinUsrMgt", description = "updatePinUsrMgt", author = "이완주")
    int updatePinUsrMgt(PinUsrMgtParameter parameter); // UPDATE_PIN_USR_MGT

    @ComponentOperation(name = "insertPinUsrMgt", description = "insertPinUsrMgt", author = "이완주")
    int insertPinUsrMgt(PinUsrMgtParameter parameter); // INSERT_PIN_USR_MGT

    @ComponentOperation(name = "PIN사용자 기기이름으로 검색", description = "PIN사용자 기기이름으로 검색", author = "이완주")
    PinUsrMgtByDeviceResult selectPinUsrMgtByDeviceId(String deviceId); // PIN_USR_MGT_S_01

    @ComponentOperation(name = "안드로이드 deviceId 업데이트", description = "deviceId 를 WidevineID 에서 AndroidId로 업데이트", author = "2034263")
    void updatePinUsrMgtDeviceId(PinUsrMgtUpdateParameter parameter);

}