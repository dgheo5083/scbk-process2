package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.NonFaceTblParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectNonFaceInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.SelectNonFaceInfoResult;
import com.scbank.process.api.svc.common.dao.dto.UpdateNfInfoParameter;

@DaoComponent(database = "kfbdb", description = "비대면 신청 정보", author = "")
public interface NonFaceTblDao {
	@ComponentOperation(name = "비대면 신청 정보 일련번호 조회", description = "비대면 신청 정보 일련번호 조회")
    String selectNonFaceTblSeqNo(String seqNo);
	
	@ComponentOperation(name = "비대면 신청 정보 일련번호 조회", description = "비대면 신청 정보 일련번호 조회")
    int insertNonFaceTbl(NonFaceTblParameter param);
	
	@ComponentOperation(name = "비대면 신청 정보 조회", description = "비대면 신청 정보 조회 NON_FACE_TBL_S_02")
	SelectNonFaceInfoResult selectNonFaceInfo(SelectNonFaceInfoParameter param);
	
	@ComponentOperation(name = "비대면인증 이미지 전송여부 수정", description = "비대면인증 이미지 전송여부 수정 NON_FACE_TBL_U_02")
	int updateNonFaceImgSendYn(UpdateNfInfoParameter param);
	
	@ComponentOperation(name = "비대면인증 정보 수정", description = "비대면인증 정보 수정 NON_FACE_TBL_U_01")
	int updateNonFaceInfo(UpdateNfInfoParameter param);
}
