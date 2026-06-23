package com.scbank.process.api.svc.common.dao;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.MotpRegistrationInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.MotpRegistrationInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SaoOtpChargeParameter;

@DaoComponent(database = "kfbdb", description = "모바일OTP", author = "이완주")
public interface SaoOtpChargeDao {

	@ComponentOperation(name = "USERID로 과금테이블 건수 조회", description = "USERID로 과금테이블 건수 조회", author = "이완주")
	int countSaoOtpChargeByUserId(String userId); // MA3_CRT2.SAO_OTP_CHARGE_01_S

	@ComponentOperation(name = "DEVICEID로 과금테이블 건수 조회", description = "DEVICEID로 과금테이블 건수 조회", author = "이완주")
	int countSaoOtpChargeByDeviceId(@Param("deviceId") String deviceId, @Param("userId") String userId); // MA3_CRT02.SAO_OTP_CHARGE_02_S

	@ComponentOperation(name = "과금테이블 UPDATE", description = "과금테이블 UPDATE", author = "이완주")
	int updateSaoOtpCharge(SaoOtpChargeParameter parameter); // MA3_CRT02.SAO_OTP_CHARGE_U_01

	@ComponentOperation(name = "과금테이블 INSERT", description = "과금테이블 INSERT", author = "이완주")
	int insertSaoOtpCharge(SaoOtpChargeParameter parameter); // MA3_CRT02.SAO_OTP_CHARGE_I_01

	@ComponentOperation(name = "MOTP 등록 정보 조회", description = "MOTP 등록 정보 조회")
	MotpRegistrationInfoResult selectMotpRegistrationInfo(MotpRegistrationInfoParameter parameter);

}