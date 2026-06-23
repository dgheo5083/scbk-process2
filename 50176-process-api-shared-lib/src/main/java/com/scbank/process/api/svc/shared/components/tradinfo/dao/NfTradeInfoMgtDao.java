package com.scbank.process.api.svc.shared.components.tradinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.CheckOngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceBasicTradeInfoResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceCddResultParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingProductInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoCancelParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoInquiryParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.RegisterOngoingTradeInfoParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.TradeInfoInquiryParameter;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.TradeInfoInquiryResult;

@DaoComponent(database = "kfbdb", description = "거래정보관리", author = "김진수")
public interface NfTradeInfoMgtDao {

    @ComponentOperation(name = "거래정보및고객정보(SSN)조회", description = "거래정보및고객정보(SSN)조회 (asis : NF_TRADINFO_MGT_S_05)", author = "김진수")
    TradeInfoInquiryResult selectTradeInfoAndCustomerSsn(TradeInfoInquiryParameter parameter);

    @ComponentOperation(name = "진행중인 업무 조회", description = "진행중인 업무 조회 (asis : NF_TRADINFO_MGT_S_02)", author = "김진수")
    OngoingTradeInfoInquiryResult selectOngoingTradeInfo(OngoingTradeInfoInquiryParameter parameter);

    @ComponentOperation(name = "진행중인 업무 목록 조회", description = "진행중인 업무 목록 조회 (asis : NF_TRADINFO_MGT_S_03)", author = "김진수")
    List<CheckOngoingTradeInfoInquiryResult> selectOngoingTradeInfoList(OngoingTradeInfoInquiryParameter parameter);

    @ComponentOperation(name = "진행중인 상품 목록 조회", description = "진행중인 상품 목록 조회 (asis : NF_TRADINFO_MGT_S_04)", author = "김진수")
    List<OngoingProductInfoInquiryResult> selectOngoingProductInfoList(OngoingTradeInfoInquiryParameter parameter);

    @ComponentOperation(name = "진행상태 심플정보 등록", description = "진행상태 심플정보 등록 (asis : NF_TRADINFO_MGT_I_01)", author = "김진수")
    int insertOngoingTradeSimpleInfo(RegisterOngoingTradeInfoParameter parameter);

    @ComponentOperation(name = "진행상태 등록", description = "진행상태 등록 (asis : NF_TRADINFO_MGT_I_02)", author = "김진수")
    int insertOngoingTradeInfo(RegisterOngoingTradeInfoParameter parameter);

    @ComponentOperation(name = "신분증 인식개선 공통화용 거래정보 등록", description = "신분증 인식개선 공통화용 거래정보 등록 (asis: NF_TRADINFO_MGT_I_03)", author = "허동규")
    int insertNonfaceTradeInfo(RegisterOngoingTradeInfoParameter parameter);

    @ComponentOperation(name = "진행상태 수정", description = "진행상태 수정  (asis : NF_TRADINFO_MGT_U_01)", author = "김진수")
    int updateOngoingTradeInfo(RegisterOngoingTradeInfoParameter parameter);

    @ComponentOperation(name = "비대면 진행 상품 취소", description = "비대면 진행 상품 취소 (asis : NF_TRADINFO_MGT_U_02)", author = "김진수")
    int updateOngoingProductCancellation(OngoingTradeInfoCancelParameter parameter);

    @ComponentOperation(name = "기본거래정보", description = " TRAD_NO로 기본 거래정보 조회 (asis : NF_TRADINFO_MGT_S_09)", author = "김진수")
    NonFaceBasicTradeInfoResult selectBasicTradeInfo(@Param("tradNo") String tradNo);

    @ComponentOperation(name = "KCDD 응답 결과 업데이트", description = "KCDD 응답 결과 업데이트 (asis : NF_TRADINFO_MGT_U_06)", author = "김진수")
    int updateTradeInfoCddResult(NonFaceCddResultParameter parameter);
}
