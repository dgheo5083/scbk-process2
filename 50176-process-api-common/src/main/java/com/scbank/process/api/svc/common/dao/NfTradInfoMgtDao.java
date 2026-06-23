package com.scbank.process.api.svc.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.CnnctnTradInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocTradInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EdocTradInfoResult;
import com.scbank.process.api.svc.common.dao.dto.IdCardShotInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.IdCardShotInfoResult;
import com.scbank.process.api.svc.common.dao.dto.IdVerificationCountParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthInfoResult;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthTradInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceTradInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SsaTruthInfoParameter;

@DaoComponent(database = "kfbdb", description = "진행 상품 조회", author = "2034263")
public interface NfTradInfoMgtDao {

    @ComponentOperation(name = "전자문서 진행 상품 조회", description = "상품아이디 / 비즈니스 유형 조회")
    EdocTradInfoResult selectEdocTradInfo(EdocTradInfoParameter parameter);
    
    @ComponentOperation(name = "비대면신분증 촬영 고객정보조회", description = "비대면 신청 정보조회")
    List<IdCardShotInfoResult> selectIdCardShotInfo(IdCardShotInfoParameter parameter);
    
    @ComponentOperation(name = "사본판별결과 저장", description = "사본판별결과 저장")
    int updateSSATruthInfo(SsaTruthInfoParameter parameter);
    
    @ComponentOperation(name = "비대면인증 거래정보 수정", description = "비대면인증 거래정보 수정")
    int updateNonFaceAuthTradInfo(NonFaceAuthTradInfoParameter parameter);
    
    @ComponentOperation(name = "제휴처 거래정보 등록", description = "제휴처 거래정보 등록")
    int insertCnnctnTradInfo(CnnctnTradInfoParameter parameter);
    
    @ComponentOperation(name = "비대면거래정보 조회", description = "비대면거래정보 조회(NF_TRADINFO_MGT_S_11)")
    NonFaceTradInfoResult selectNonFaceTradInfo(@Param("tradNo")String tradNo);
    
    @ComponentOperation(name = "신분증진위확인 카운트 수정", description = "신분증진위확인 카운트 수정(NF_TRADINFO_MGT_U_04)")
    int updateIdVerificationCount(IdVerificationCountParameter parameter);
    
    @ComponentOperation(name = "비대면인증 정보 조회", description = "비대면인증 정보 조회(NF_TRADINFO_MGT_S_06)")
    NonFaceAuthInfoResult selectNonFaceAuthInfo(NonFaceAuthInfoParameter parameter);
}
