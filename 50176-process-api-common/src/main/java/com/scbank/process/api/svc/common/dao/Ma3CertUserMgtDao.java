package com.scbank.process.api.svc.common.dao;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.CertUserMgtParameter;
import com.scbank.process.api.svc.common.dao.dto.DeviceCountResult;
import com.scbank.process.api.svc.common.dao.dto.FinTechCertUserInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.FinTechCertUserInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SimpleFinCertUserInfoResult;
import com.scbank.process.api.svc.common.dao.dto.VpcgUsrMgtParameter;
import com.scbank.process.api.svc.common.dao.dto.VpcgUsrMgtResult;

@DaoComponent(database = "kfbdb", description = "인증서 사용자 관리", author = "이완주")
public interface Ma3CertUserMgtDao {

    @ComponentOperation(name = "금융인증서 사용자 정보 UPDATE", description = "생체인증 등록시 현재 device에 등록된 user중 현재 등록하려는 user를 제외한 user들의 JOIN_FLG와 DEVICE_ID 초기화", author = "이완주")
    int updateJoinFlgByDeviceId(@Param("deviceId") String deviceId, @Param("userId") String userId); // MA3_CERT_USER_MGT_U_03

    @ComponentOperation(name = "금융인증서 사용자 정보 조회", description = "금융인증서 사용자 정보 조회", author = "이완주")
    int countCertUserMgtBtDeviceId(String deviceId); // MA3_CERT_USER_MGT_S_01

    @ComponentOperation(name = "금융인증서 사용자 정보 조회", description = "금융인증서 사용자 정보 조회", author = "이완주")
    DeviceCountResult selectCountDevice(String userId); // MA3_CERT_USER_MGT_S_03

    @ComponentOperation(name = "인증서 해지시 가입여부와 최근 변경일자 업데이트", description = "인증서 해지시 가입여부와 최근 변경일자 업데이트", author = "이완주")
    int updateJoinFlg(@Param("deviceId") String deviceId, @Param("userId") String userId); // MA3_CERT_USER_MGT_U_02

    @ComponentOperation(name = "금융인증서 사용자 정보 INSERT", description = "금융인증서 사용자 정보 INSERT", author = "이완주")
    int insertCertUserMgt(CertUserMgtParameter parameter); // MA3_CERT_USER_MGT_I_01

    @ComponentOperation(name = "금융인증서 사용자 정보 UPDATE", description = "금융인증서 사용자 정보 UPDATE", author = "이완주")
    int updateCertUserMgt(CertUserMgtParameter parameter); // MA3_CERT_USER_MGT_U_01

    @ComponentOperation(name = "금융인증서 사용자 정보 조회", description = "금융인증서 사용자 정보 조회", author = "이완주")
    int countCertUserMgt(String userId); // MA3_CERT_USER_MGT_S_04

    @ComponentOperation(name = "금융인증서 갱신을 진행한 User중 등록되어 있는 정보를 모두 사용불가로 변경처리", description = "금융인증서 갱신을 진행한 User중 등록되어 있는 정보를 모두 사용불가로 변경처리", author = "이완주")
    int updateCertUserMgtByRenew(String userId); // MA3_CERT_USER_MGT_S_04

    @ComponentOperation(name = "FinTech(Veraport CG) 인증 가입 고객 유무 조회", description = "FinTech(Veraport CG) 인증 가입 고객 유무 조회", author = "이완주")
    VpcgUsrMgtResult selectVpcgUsrMgt(@Param("userId") String userId, @Param("connectType") String connectType);

    @ComponentOperation(name = "핀서트인증서 사용자 등록", description = "핀서트인증서 사용자 등록", author = "이완주")
    int insertVpcgUsrMgt(VpcgUsrMgtParameter parameter); // MA3_CERT_USER_MGT_I_01

    @ComponentOperation(name = "핀서트인증서 사용자 재등록", description = "핀서트인증서 사용자 재등록", author = "이완주")
    int updateVpcgUsrMgt(VpcgUsrMgtParameter parameter); // MA3_CERT_USER_MGT_U_01

    @ComponentOperation(name = "핀서트인증서 사용자 해지", description = "핀서트인증서 사용자 해지", author = "이완주")
    int deleteVpcgUsrMgt(VpcgUsrMgtParameter parameter); // MA3_CERT_USER_MGT_U_02

    @ComponentOperation(name = "금융인증서 간편로그인 사용자 정보 조회", description = "금융인증서 간편로그인 사용자 정보 조회", author = "2034263")
    SimpleFinCertUserInfoResult selectSimpleFinCertUserInfo(String deviceId);

    @ComponentOperation(name = "핀테크 인증 가입 사용자 정보 조회", description = "핀테크 인증 가입 사용자 정보 조회", author = "2034263")
    FinTechCertUserInfoResult selectFinTechCertJoinedUserInfo(FinTechCertUserInfoParameter parameter);

    @ComponentOperation(name = "핀테크 인증 사용자 정보 조회", description = "핀테크 인증 사용자 정보 조회", author = "2034263")
    FinTechCertUserInfoResult selectFinTechCertUserInfo(FinTechCertUserInfoParameter parameter);

}