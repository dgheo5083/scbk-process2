package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.AcctAuthNumberParameter;
import com.scbank.process.api.svc.common.dao.dto.AcctAuthNumberResult;
import com.scbank.process.api.svc.common.dao.dto.CntrtyRemitReqCntParameter;
import com.scbank.process.api.svc.common.dao.dto.RemitCntParameter;
import com.scbank.process.api.svc.common.dao.dto.RemitInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.RemitInfoResult;

@DaoComponent(database = "kfbdb", description = "비대면 송금 관리", author = "")
public interface NfRemitMgtDao {
	
	@ComponentOperation(name = "타행송금 요청건수 조회", description = "타행송금 요청건수 조회")
	String selectCntrtyRemitReqCnt(CntrtyRemitReqCntParameter parameter);
	
	@ComponentOperation(name = "타행송금 실패건수 조회", description = "타행송금 실패건수 조회")
	String selectCntrtyAuthFailCnt(CntrtyRemitReqCntParameter parameter);
	
	@ComponentOperation(name = "1원 송금 횟수 조회", description = "1원 송금 횟수 조회")
	Integer selectRemitCnt(RemitCntParameter parameter);
	
	@ComponentOperation(name = "송금정보 생성", description = "송금정보 생성")
	Integer insertRemitInfo(RemitInfoParameter parameter);
	
	@ComponentOperation(name = "송금정보 수정", description = "송금정보 수정")
	Integer updateRemitInfo(RemitInfoParameter parameter);
	
	@ComponentOperation(name = "송금정보 조회", description = "10분 이내 송금진행 정보 조회")
	List<RemitInfoResult> selectRemitInfo(RemitInfoParameter parameter);
	
	@ComponentOperation(name = "계좌인증번호 조회", description = "역송금 인증코드(난수) 조회")
	AcctAuthNumberResult selectAcctAuthNumber(AcctAuthNumberParameter parameter);
	
	@ComponentOperation(name = "계좌인증 실패건수 수정", description = "계좌인증 실패건수 수정")
	Integer updateRemitFailCnt(RemitInfoParameter parameter);
	
}
