package com.scbank.process.api.svc.common.service.functions;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.TradInfoComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.CheckOngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterScrapingDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterScrapingDataResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통기능 - 진행상태 관리(이어하기)", url = "/functions/tradeinfo")
public class FunctionsTradeInfoService {

	private final TradInfoComponent tradInfoComponent;
	
	@ServiceEndpoint(url = "/checkOngoingTradeInfo", name = "진행중인 업무 상태별 목록 조회 (asis : MA3CMMBIZ010_104S)")
    public CheckOngoingTradeInfoInquiryResponse getCheckOngoingTradeInfo(IServiceContext serviceContext, OngoingTradeInfoInquiryRequest request) {
		
		return tradInfoComponent.getCheckOngoingTradeInfo(request);
	}
	
	@ServiceEndpoint(url = "/ongoingTradeInfo", name = "진행중인 업무 단건 조회 (asis : MA3CMMBIZ010_106S)")
    public OngoingTradeInfoInquiryResponse getOngoingTradeInfo(IServiceContext serviceContext, OngoingTradeInfoInquiryRequest request) {
		
		return tradInfoComponent.getOngoingTradeInfo(request);
	}
	
	@ServiceEndpoint(url = "/registOngoingTradeInfoScrnRcvry", name = "진행상태 등록/수정(ScrnData/RcvryData 등록) (asis : MA3CMMBIZ010_105S)")
    public RegisterOngoingTradeInfoResponse registOngoingTradeInfoAndScrnDataRcvryData(IServiceContext serviceContext, RegisterOngoingTradeInfoDataRequest request) {
		
		return tradInfoComponent.registOngoingTradeInfoAndScrnDataRcvryData(request);
	}
	
	@ServiceEndpoint(url = "/registOngoingTradeInfoLms", name = "진행상태 등록/수정(LMS발송) (asis : MA3CMMBIZ010_10AS)")
    public RegisterOngoingTradeInfoResponse registOngoingTradeInfoAndSendLms(IServiceContext serviceContext, RegisterOngoingTradeInfoRequest request) {
		
		return tradInfoComponent.registOngoingTradeInfoAndSendLms(request);
	}
	
	@ServiceEndpoint(url = "/registOngoingTradeInfoPartnerProcess", name = "진행상태 등록/수정(거래처거래정보 처리) (asis : MA3CMMBIZ010_103S)")
    public RegisterOngoingTradeInfoResponse registOngoingTradeInfoAndPartnerTransaction(IServiceContext serviceContext, RegisterOngoingTradeInfoRequest request) {
		
		return tradInfoComponent.registOngoingTradeInfoAndPartnerTransaction(request);
	}
	
	@ServiceEndpoint(url = "/cancelOngoingTradeInfo", name = "진행중인 상품 취소 처리 (asis : MA3CMMBIZ010_107S)")
	public OngoingTradeInfoCancelResponse cancelOngoingTradeInfo(IServiceContext serviceContext, OngoingTradeInfoCancelRequest request) {
		
		return tradInfoComponent.cancelOngoingTradeInfo(request);
	}
	
	@ServiceEndpoint(url = "/cancelOnoingProcess", name = "UI 진행상태 업데이트 처리 – 대출 취소(MCI) (asis : MA3CMMBIZ010_108S)")
	public OngoingProcessCancelResponse cancelOnoingProcess(IServiceContext serviceContext, OngoingProcessCancelRequest request) {
		
		return tradInfoComponent.cancelOnoingProcess(request);
	}
	
	@ServiceEndpoint(url = "/registScrapingData", name = "스크래핑데이터 저장 (asis : MA3CMMBIZ015_101S)")
	public RegisterScrapingDataResponse registScrapingData(IServiceContext serviceContext, RegisterScrapingDataRequest request) {
		
		return tradInfoComponent.registScrapingData(request);
	}
	
	@ServiceEndpoint(url = "/registEdocRecoveryData", name = "CDD전자문서 정보 등록 (asis : MA3CMMBIZ013_101S)")
	public RegisterEdocRecoveryDataResponse registEdocRecoveryData(IServiceContext serviceContext, RegisterEdocRecoveryDataRequest request) {
		
		return tradInfoComponent.registEdocRecoveryData(request);
	}
	
	@ServiceEndpoint(url = "/screenAndScrapingInfo", name = "화면정보 및 스크래핑 데이터 조회 (asis : MA3CMMBIZ015_102S)")
	public ScreenAndScrapingInfoResponse getScreenAndScrapingInfo(IServiceContext serviceContext, ScreenAndScrapingInfoRequest request) {
		
		return tradInfoComponent.getScreenAndScrapingInfo(request);
	}
	
}
