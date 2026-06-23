package com.scbank.process.api.svc.common.service.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.context.IServiceContext;
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

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
@Slf4j
@SuppressWarnings({"unchecked", "unused"})
public class FunctionsTradeInfoServiceTest {
	
	@Mock private TradInfoComponent tradInfoComponent;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS) private IServiceContext ctx;
	
	// 테스트 대상
	@InjectMocks private FunctionsTradeInfoService functionsTradeInfoService;
	
	@BeforeEach
	void setUp() {
	}
	
	@AfterEach
	void tearDown() {
	}
	
	@Nested
    @DisplayName("진행중인 업무 상태별 목록 조회 (asis : MA3CMMBIZ010_104S)")
	class getCheckOngoingTradeInfo {
		@Test 
		@DisplayName("성공")
		void getCheckOngoingTradeInfoTest1() throws Exception {
			when(tradInfoComponent.getCheckOngoingTradeInfo(any())).thenAnswer(o->{
				CheckOngoingTradeInfoInquiryResponse res = new CheckOngoingTradeInfoInquiryResponse();
				return res;
			});
			
			OngoingTradeInfoInquiryRequest request = new OngoingTradeInfoInquiryRequest();
			CheckOngoingTradeInfoInquiryResponse response = functionsTradeInfoService.getCheckOngoingTradeInfo(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행중인 업무 단건 조회 (asis : MA3CMMBIZ010_106S)")
	class getOngoingTradeInfo {
		@Test 
		@DisplayName("성공")
		void getOngoingTradeInfoTest1() throws Exception {
			when(tradInfoComponent.getOngoingTradeInfo(any())).thenAnswer(o->{
				OngoingTradeInfoInquiryResponse res = new OngoingTradeInfoInquiryResponse();
				return res;
			});
			
			OngoingTradeInfoInquiryRequest request = new OngoingTradeInfoInquiryRequest();
			OngoingTradeInfoInquiryResponse response = functionsTradeInfoService.getOngoingTradeInfo(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행상태 등록/수정(ScrnData/RcvryData 등록) (asis : MA3CMMBIZ010_105S)")
	class registOngoingTradeInfoAndScrnDataRcvryData {
		@Test 
		@DisplayName("성공")
		void registOngoingTradeInfoAndScrnDataRcvryDataTest1() throws Exception {
			when(tradInfoComponent.registOngoingTradeInfoAndScrnDataRcvryData(any())).thenAnswer(o->{
				RegisterOngoingTradeInfoResponse res = new RegisterOngoingTradeInfoResponse();
				return res;
			});
			
			RegisterOngoingTradeInfoDataRequest request = new RegisterOngoingTradeInfoDataRequest();
			RegisterOngoingTradeInfoResponse response = functionsTradeInfoService.registOngoingTradeInfoAndScrnDataRcvryData(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행상태 등록/수정(LMS발송) (asis : MA3CMMBIZ010_10AS)")
	class registOngoingTradeInfoAndSendLms {
		@Test 
		@DisplayName("성공")
		void registOngoingTradeInfoAndSendLmsTest1() throws Exception {
			when(tradInfoComponent.registOngoingTradeInfoAndSendLms(any())).thenAnswer(o->{
				RegisterOngoingTradeInfoResponse res = new RegisterOngoingTradeInfoResponse();
				return res;
			});
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			RegisterOngoingTradeInfoResponse response = functionsTradeInfoService.registOngoingTradeInfoAndSendLms(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행상태 등록/수정(거래처거래정보 처리) (asis : MA3CMMBIZ010_103S)")
	class registOngoingTradeInfoAndPartnerTransaction {
		@Test 
		@DisplayName("성공")
		void registOngoingTradeInfoAndPartnerTransactionTest1() throws Exception {
			when(tradInfoComponent.registOngoingTradeInfoAndPartnerTransaction(any())).thenAnswer(o->{
				RegisterOngoingTradeInfoResponse res = new RegisterOngoingTradeInfoResponse();
				return res;
			});
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			RegisterOngoingTradeInfoResponse response = functionsTradeInfoService.registOngoingTradeInfoAndPartnerTransaction(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행중인 상품 취소 처리 (asis : MA3CMMBIZ010_107S)")
	class cancelOngoingTradeInfo {
		@Test 
		@DisplayName("성공")
		void cancelOngoingTradeInfoTest1() throws Exception {
			when(tradInfoComponent.cancelOngoingTradeInfo(any())).thenAnswer(o->{
				OngoingTradeInfoCancelResponse res = new OngoingTradeInfoCancelResponse();
				return res;
			});
			
			OngoingTradeInfoCancelRequest request = new OngoingTradeInfoCancelRequest();
			OngoingTradeInfoCancelResponse response = functionsTradeInfoService.cancelOngoingTradeInfo(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("UI 진행상태 업데이트 처리 – 대출 취소(MCI) (asis : MA3CMMBIZ010_108S)")
	class cancelOnoingProcess {
		@Test 
		@DisplayName("성공")
		void cancelOnoingProcessTest1() throws Exception {
			when(tradInfoComponent.cancelOnoingProcess(any())).thenAnswer(o->{
				OngoingProcessCancelResponse res = new OngoingProcessCancelResponse();
				return res;
			});
			
			OngoingProcessCancelRequest request = new OngoingProcessCancelRequest();
			OngoingProcessCancelResponse response = functionsTradeInfoService.cancelOnoingProcess(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("스크래핑데이터 저장 (asis : MA3CMMBIZ015_101S)")
	class registScrapingData {
		@Test 
		@DisplayName("성공")
		void registScrapingDataTest1() throws Exception {
			when(tradInfoComponent.registScrapingData(any())).thenAnswer(o->{
				RegisterScrapingDataResponse res = new RegisterScrapingDataResponse();
				return res;
			});
			
			RegisterScrapingDataRequest request = new RegisterScrapingDataRequest();
			RegisterScrapingDataResponse response = functionsTradeInfoService.registScrapingData(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("CDD전자문서 정보 등록 (asis : MA3CMMBIZ013_101S)")
	class registEdocRecoveryData {
		@Test 
		@DisplayName("성공")
		void registScrapingDataTest1() throws Exception {
			when(tradInfoComponent.registEdocRecoveryData(any())).thenAnswer(o->{
				RegisterEdocRecoveryDataResponse res = new RegisterEdocRecoveryDataResponse();
				return res;
			});
			
			RegisterEdocRecoveryDataRequest request = new RegisterEdocRecoveryDataRequest();
			RegisterEdocRecoveryDataResponse response = functionsTradeInfoService.registEdocRecoveryData(ctx, request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("화면정보 및 스크래핑 데이터 조회 (asis : MA3CMMBIZ015_102S)")
	class getScreenAndScrapingInfo {
		@Test 
		@DisplayName("성공")
		void getScreenAndScrapingInfoTest1() throws Exception {
			when(tradInfoComponent.getScreenAndScrapingInfo(any())).thenAnswer(o->{
				ScreenAndScrapingInfoResponse res = new ScreenAndScrapingInfoResponse();
				return res;
			});
			
			ScreenAndScrapingInfoRequest request = new ScreenAndScrapingInfoRequest();
			ScreenAndScrapingInfoResponse response = functionsTradeInfoService.getScreenAndScrapingInfo(ctx, request);
			assertThat(response).isNotNull();
		}
	}
}
