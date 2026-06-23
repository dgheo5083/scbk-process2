package com.scbank.process.api.svc.shared.components.cert.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.LdapInfoResult;

@DaoComponent(database = "oppra", description = "LDAP_INFO 테이블 조회", author = "이완주")
public interface LdapInfoDao {

    @ComponentOperation(name = "serial로 당행 인증서 조회", description = "시리얼로 당행 인증서 조회", author = "이완주")
    List<LdapInfoResult> selectOppraLdapInfo(String serial);

    @ComponentOperation(name = "userid로 당행 인증서 조회", description = "userid로 당행 인증서 조회", author = "이완주")
    List<LdapInfoResult> selectOppraLdapInfoUserid(String userId);

}