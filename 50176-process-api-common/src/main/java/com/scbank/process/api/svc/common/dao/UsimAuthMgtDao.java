package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.TokenAuthParameter;
import com.scbank.process.api.svc.common.dao.dto.TokenAuthResult;

@DaoComponent(database = "kfbdb", author = "2034236", description = "USIM 인증관리 테이블")
public interface UsimAuthMgtDao {

    @ComponentOperation(name = "토큰인증 등록 여부", description = "토큰인증 등록 여부", author = "2034236")
    List<TokenAuthResult> selectTokenAuth(TokenAuthParameter input);

    @ComponentOperation(name = "토큰인증 등록", description = "토큰인증 등록", author = "2034236")
    int insertTokenAuth(TokenAuthParameter input);

}
