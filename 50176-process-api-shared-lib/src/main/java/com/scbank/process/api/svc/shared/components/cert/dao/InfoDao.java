package com.scbank.process.api.svc.shared.components.cert.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.InfoResult;

@DaoComponent(database = "oppra", description = "INFO 테이블 조회", author = "이완주")
public interface InfoDao {

	@ComponentOperation(name = "시리얼로 타기관 인증서 조회", description = "시리얼로 타기관 인증서 조회", author = "이완주")
	List<InfoResult> selectSerialInfo(@Param("serial") String serial, @Param("oid") String oid);

}