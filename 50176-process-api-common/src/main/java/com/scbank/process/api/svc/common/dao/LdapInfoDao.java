package com.scbank.process.api.svc.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.InfoOtherResult;
import com.scbank.process.api.svc.common.dao.dto.LdapInfoResult;

@DaoComponent(database = "oppra", author = "이완주", description = "ldap_info")
public interface LdapInfoDao {

    @ComponentOperation(name = "시리얼번호로 사용자 아이디 변경", description = "시리얼번호로 사용자 아이디 변경", author = "이완주")
    int updateUseridBySerial(@Param("serial") String serial, @Param("userId") String userId);
    
    @ComponentOperation(name = "LDAP_INFO 테이블 페기 정보 조회", description = "LDAP_INFO 테이블 페기 정보 조회", author = "오은진")
    List<LdapInfoResult> selectForDelete(@Param("policy") String policy, @Param("userId") String userId);
    
    @ComponentOperation(name = "userid로 REFLAG 1인 당행 인증서 조회", description = "userid로 REFLAG 1인 당행 인증서 조회", author = "오은진")
    List<LdapInfoResult> selectOppraLdapInfoByUserid(@Param("userId") String userId);
    
    @ComponentOperation(name = "시리얼번호로 LDAP_INFO 조회", description = "시리얼번호로 LDAP_INFO 조회", author = "이완주")
    LdapInfoResult selectLdapInfoBySerial(String serial);
    
    @ComponentOperation(name = "userid로 금융인증서 조회", description = "userid로 금융인증서 조회", author = "이완주")
    int selectFincertCount(String userId);
    
    @ComponentOperation(name = "사용자아이디로 타기관 인증서 조회", description = "사용자아이디로 타기관 인증서 조회", author = "오은진")
    List<InfoOtherResult> selectInfoOther(@Param("userId") String userId);

    @ComponentOperation(name = "타기관 인증서 삭제", description = "타기관 인증서 삭제", author = "오은진")
    void deleteInfoOther(@Param("userId") String userId, @Param("idNum") String idNum, @Param("oid") String oid);
    
}