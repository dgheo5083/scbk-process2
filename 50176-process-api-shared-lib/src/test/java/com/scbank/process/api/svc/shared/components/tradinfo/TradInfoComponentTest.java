package com.scbank.process.api.svc.shared.components.tradinfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scbank.process.api.edmi.dto.mci.MciDy7200170001Res;
import com.scbank.process.api.edmi.dto.mci.MciYp0049010001Res;
import com.scbank.process.api.edmi.dto.mci.MciYp0049020001Res;
import com.scbank.process.api.fw.base.integration.system.edmi.EdmiRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.customer.dao.NfCustomerMgtDao;
import com.scbank.process.api.svc.shared.components.customer.dao.dto.NonFaceCustomerInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.lms.LmsComponent;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.FnanclPrdctCommDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfCddEdocRcvryMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfCnnctnTradInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfScrapingMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfScreenInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfScrnInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoDtlDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoHistMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.NfTradeInfoMgtDao;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.CheckOngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.MainDepositAccountProductResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NfScrnInfoInqiryReult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.NonFaceScreenAndScrapingInfoResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.OngoingTradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dao.dto.TradeInfoInquiryResult;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.CheckOngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.NfScrnInfoInqiryRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.NfScrnInfoInqiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingProcessCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoCancelResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.OngoingTradeInfoInquiryResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegistIntlTradInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegistIntlTradInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterEdocRecoveryDataResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterNonFaceScreenInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterNonFaceScreenInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterOngoingTradeInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterScrapingDataRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.RegisterScrapingDataResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoRequest;
import com.scbank.process.api.svc.shared.components.tradinfo.dto.ScreenAndScrapingInfoResponse;
import com.scbank.process.api.svc.shared.components.tradinfo.mapper.TradeInfoMapper;
import com.scbank.process.api.svc.shared.dao.NfTradinfoBizcdMgtDao;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BankingBizUtils;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
@Slf4j
public class TradInfoComponentTest {

	@Mock private ISessionContextManager sessionManager;
    
	@Mock private LmsComponent lmsComponent;
	@Mock private HostClient hostClient;
	@Mock private OltpRequestOptions oltpRequestOptions;
	@Mock private EdmiRequestOptions edmiRequestOptions;
	@Mock private MciRequestOptions mciRequestOptions;
    
	@Mock private AccountListComponent accountListComponent;
    
	@Mock private NfTradeInfoMgtDao nfTradeInfoMgtDao;
	@Mock private FnanclPrdctCommDao fnanclPrdctCmmnDao;
	@Mock private NfTradeInfoHistMgtDao nfTradeInfoHistMgtDao;
	@Mock private NfCnnctnTradInfoMgtDao nfCnnctnTradInfoMgtDao;
	@Mock private NfCustomerMgtDao nfCustMgtDao;
	@Mock private NfTradeInfoDtlDao nfTradeInfoDtlDao;
	@Mock private NfScreenInfoMgtDao nfScreenInfoMgtDao;
	@Mock private NfScrapingMgtDao nfScrapingMgtDao;
	@Mock private NfCddEdocRcvryMgtDao nfCddEdocRcvryMgtDao;
	@Mock private NfScrnInfoMgtDao nfScrnInfoMgtDao;
	@Mock private NfTradinfoBizcdMgtDao nfTradinfoBizcdMgtDao;
	@Mock private TradeInfoMapper tradeInfoMapper;
	
	// 테스트 대상
	@InjectMocks private TradInfoComponent tradInfoComponent;
		
	MockedStatic<CodeUtils> mockCodeUtils;
	MockedStatic<SessionUtils> mockSessionUtils;
	MockedStatic<CommonBizUtils> mockCommonBizUtils;
	MockedStatic<BizCommonUtils> mockBizCommonUtils;
	MockedStatic<PropertiesUtils> mockPropertiesUtils;
	MockedStatic<BankingBizUtils> mockBankingBizUtils;
	
	@BeforeEach
	void setUp() {
		mockCodeUtils = Mockito.mockStatic(CodeUtils.class);
		mockSessionUtils = Mockito.mockStatic(SessionUtils.class);
		mockCommonBizUtils = Mockito.mockStatic(CommonBizUtils.class);
		mockBizCommonUtils = Mockito.mockStatic(BizCommonUtils.class);
		mockPropertiesUtils = Mockito.mockStatic(PropertiesUtils.class);
		mockBankingBizUtils = Mockito.mockStatic(BankingBizUtils.class);
	}
	
	@AfterEach
	void tearDown() {
		mockCodeUtils.close();
		mockSessionUtils.close();
		mockCommonBizUtils.close();
		mockBizCommonUtils.close();
		mockPropertiesUtils.close();
		mockBankingBizUtils.close();
	}
	
	@Nested
    @DisplayName("진행중인 업무 상태별 목록 조회 (asis : MA3CMMBIZ010_104S)")
	class getCheckOngoingTradeInfo {
		
		@ParameterizedTest
	    @ValueSource(strings = { "MCDD", "TDSB" })
		@DisplayName("성공 - case1")
		void getCheckOngoingTradeInfoTest1(String bizType) throws Exception {
			mockSessionUtils.when(()-> SessionUtils.isLoginOrAuth("LOGIN")).thenReturn(false);
			mockSessionUtils.when(()-> SessionUtils.isLoginOrAuth("AUTH")).thenReturn(false);
			mockSessionUtils.when(()-> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn("200112121******");
			
			when(sessionManager.getGlobalValue("PRDCT_ID", String.class)).thenReturn("PRDCT_ID");
			//CDD 프로세스인 경우 고객번호(CUST_NO) 조회 or 드림론이벤트인 경우
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				result.setCustNo("1405563");
				return result;
			});
			
			OngoingTradeInfoInquiryRequest request = new OngoingTradeInfoInquiryRequest();
			request.setBizType(bizType);
			request.setCustNo("1405563");
			
			CheckOngoingTradeInfoInquiryResponse response = tradInfoComponent.getCheckOngoingTradeInfo(request);
			assertThat(response).isNotNull();
		}
		
		@ParameterizedTest
	    @ValueSource(strings = { "CMJG" })
		@DisplayName("성공 - case2")
		void getCheckOngoingTradeInfoTest2(String bizType) throws Exception {
			mockSessionUtils.when(()-> SessionUtils.isLoginOrAuth("LOGIN")).thenReturn(true);
			mockSessionUtils.when(()-> SessionUtils.isLoginOrAuth("AUTH")).thenReturn(true);
			mockSessionUtils.when(()-> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn("200112121******");
			
			when(sessionManager.getGlobalValue("PRDCT_ID", String.class)).thenReturn("PRDCT_ID");
			//CDD 프로세스인 경우 고객번호(CUST_NO) 조회 or 드림론이벤트인 경우
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				result.setCustNo("1405563");
				return result;
			});
			//BIZ_TYPE = TDSB 메인 입출금계좌 상품 리스트 : FNANCL_PRDCT_CMMN_S_01
			when(fnanclPrdctCmmnDao.selectMainDepositAccountProduct()).thenAnswer(o->{
				List<MainDepositAccountProductResult> list = new ArrayList<>();
				MainDepositAccountProductResult item = new MainDepositAccountProductResult();
				item.setPrdctId("001");
            	item.setPrdctCd("001");
            	list.add(item);
				return list;
			});
			when(nfTradeInfoMgtDao.selectOngoingTradeInfoList(any())).thenAnswer(o->{
				List<CheckOngoingTradeInfoInquiryResult> list = new ArrayList<>();
				CheckOngoingTradeInfoInquiryResult item = new CheckOngoingTradeInfoInquiryResult();
				item.setCustNo("1405563");
				item.setTradNo("3502988");
				item.setPrdctId("001");
				item.setPrdctCd("001");
				item.setPrdctNm("001");
				item.setBizType("DLEV");
				item.setCallCntrStsCd("JGLP");
				list.add(item);
				return list;
			});
			
			// chkProcessState
			when(sessionManager.getGlobalValue("IS_CNNCTN_WAY", String.class)).thenReturn("Y");
			when(nfTradeInfoMgtDao.updateOngoingProductCancellation(any())).thenAnswer(o->{
				return 1;
			});
			

			OngoingTradeInfoInquiryRequest request = new OngoingTradeInfoInquiryRequest();
			request.setBizType(bizType);
			request.setCustNo("");//1405563
			
			CheckOngoingTradeInfoInquiryResponse response = tradInfoComponent.getCheckOngoingTradeInfo(request);
			assertThat(response).isNotNull();
		}
		
		@ParameterizedTest
	    @ValueSource(strings = { "MCDD", "TDSB", "DEV", "CMJG" })
		@DisplayName("성공 - case3")
		void getCheckOngoingTradeInfoTest3(String bizType) throws Exception {
			mockSessionUtils.when(()-> SessionUtils.isLoginOrAuth("LOGIN")).thenReturn(true);
			mockSessionUtils.when(()-> SessionUtils.isLoginOrAuth("AUTH")).thenReturn(true);
			mockSessionUtils.when(()-> SessionUtils.getSessionValue("PerBusNo", String.class)).thenReturn("200112121******");
			
			when(sessionManager.getGlobalValue("PRDCT_ID", String.class)).thenReturn("PRDCT_ID");
			//CDD 프로세스인 경우 고객번호(CUST_NO) 조회 or 드림론이벤트인 경우
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				result.setCustNo("1405563");
				return result;
			});
			//BIZ_TYPE = TDSB 메인 입출금계좌 상품 리스트 : FNANCL_PRDCT_CMMN_S_01
			when(fnanclPrdctCmmnDao.selectMainDepositAccountProduct()).thenAnswer(o->{
				List<MainDepositAccountProductResult> list = new ArrayList<>();
				MainDepositAccountProductResult item = new MainDepositAccountProductResult();
				item.setPrdctId("001");
            	item.setPrdctCd("001");
            	list.add(item);
				return list;
			});
			when(nfTradeInfoMgtDao.selectOngoingTradeInfoList(any())).thenAnswer(o->{
				List<CheckOngoingTradeInfoInquiryResult> list = new ArrayList<>();
				CheckOngoingTradeInfoInquiryResult item = new CheckOngoingTradeInfoInquiryResult();
				item.setCustNo("1405563");
				item.setTradNo("3502988");
				item.setPrdctId("001");
				item.setPrdctCd("001");
				item.setPrdctNm("001");
				item.setCallCntrStsCd("JGLP");
				item.setCnnctnWay("000");
				item.setBizType("");
				list.add(item);
				return list;
			});
			
			// chkProcessState
			when(sessionManager.getGlobalValue("IS_CNNCTN_WAY", String.class)).thenReturn("Y");
			when(nfTradeInfoMgtDao.updateOngoingProductCancellation(any())).thenAnswer(o->{
				return 1;
			});
			
			
			
			
			OngoingTradeInfoInquiryRequest request = new OngoingTradeInfoInquiryRequest();
			request.setBizType(bizType);
			request.setCustNo("1405563");//
			
			CheckOngoingTradeInfoInquiryResponse response = tradInfoComponent.getCheckOngoingTradeInfo(request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행중인 업무 단건 조회 (asis : MA3CMMBIZ010_106S)")
	class getOngoingTradeInfo {
		@Test
		@DisplayName("성공 - case1")
		void getOngoingTradeInfoTest1() throws Exception {
			
			when(nfTradeInfoMgtDao.selectOngoingTradeInfo(any())).thenAnswer(o->{
				OngoingTradeInfoInquiryResult result = new OngoingTradeInfoInquiryResult();
				return result;
			});
			
			when(tradeInfoMapper.toOngoingTradeInfoResponse(any())).thenAnswer(o->{
				OngoingTradeInfoInquiryResponse result = new OngoingTradeInfoInquiryResponse();
				return result;
			});
			
			OngoingTradeInfoInquiryRequest request = new OngoingTradeInfoInquiryRequest();
			//request.setBizType(bizType);
			request.setCustNo("1405563");//
			
			OngoingTradeInfoInquiryResponse response = tradInfoComponent.getOngoingTradeInfo(request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행상태 등록/수정, ScrnData/RcvryData 등록 (asis : MA3CMMBIZ010_105S)")
	class registOngoingTradeInfoAndScrnDataRcvryData {
		@Test
		@DisplayName("성공 - case1")
		void registOngoingTradeInfoAndScrnDataRcvryDataTest1() throws Exception {
			
			when(nfScreenInfoMgtDao.insertNonFaceScreenInfoManagement(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfCddEdocRcvryMgtDao.insertCddElectronicDocumentInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfTradeInfoMgtDao.insertOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfTradeInfoMgtDao.updateOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			HashMap<String,Object> tradInfoMap = new HashMap<String,Object>();
			tradInfoMap.put("PRDCT_ID", "3005");
			tradInfoMap.put("PRDCT_CD", "PRDLON0003005");
			tradInfoMap.put("PRDCT_NM", "퍼스트전세보증론");
			tradInfoMap.put("ALLIANC_CD", "");
			tradInfoMap.put("EFFCTV_INT", "");
			tradInfoMap.put("AUTHNT_IND", "");
			tradInfoMap.put("AUTHNT_IND_CD", "");
			tradInfoMap.put("AUTHNT_REQ_DT", "");
			tradInfoMap.put("AUTHNT_REQ_CMPLTN_DT", "");
			tradInfoMap.put("BRNCH_NO", "860");
			tradInfoMap.put("CDD_REQ_CD", "");
			tradInfoMap.put("CLERK_NO", "");
			tradInfoMap.put("ERR_CD", "");
			tradInfoMap.put("ERR_MSG", "");
			tradInfoMap.put("PER_PART", "");
			tradInfoMap.put("DOC_EVCD_CD", "");
			tradInfoMap.put("HL_PRSN_CD", "");
			tradInfoMap.put("AD_SBST_CD", "");
			tradInfoMap.put("KW_INSR_CD", "");
			tradInfoMap.put("GOVM_DATA_TYPE", "");
			tradInfoMap.put("GOVM_SIGN_TYPE", "");
			tradInfoMap.put("PRE_LOAN_MOVE_YN", "");
			tradInfoMap.put("REAL_LOAN_MOVE_YN", "");
			tradInfoMap.put("HQ_PRIME_RATE_APPLY_YN", "");
			tradInfoMap.put("HQ_PRIME_RATE", "");
			tradInfoMap.put("LOAN_PURPOSE", "");
			tradInfoMap.put("LOAN_PRD", "");
			tradInfoMap.put("RDMPTN_MTHD", "");
			tradInfoMap.put("EXEC_DT", "");
			tradInfoMap.put("RDMPTN_DT", "");
			tradInfoMap.put("LOAN_REQ_NO", "");
			tradInfoMap.put("LOAN_ACCPT_NO", "");
			tradInfoMap.put("COPL_CMMN_CLLTRL_IND", "");
			tradInfoMap.put("RJCTN_CD", "");
			tradInfoMap.put("RJCTN_MSG", "");
			tradInfoMap.put("LOAN_HOPE_DT", "");
			tradInfoMap.put("EASY_LOAN_YN", "");
			tradInfoMap.put("LOAN_DOC_INVSTGT_FLG", "");
			tradInfoMap.put("SECURITY_ACCT_NO", "");
			tradInfoMap.put("COLLATERAL_TYPE", "");
			tradInfoMap.put("OPL_CNSLT_YN", "");
			tradInfoMap.put("DEL_OBJ_FLG", "");
			tradInfoMap.put("ID_CARD_CD", "");
			tradInfoMap.put("ID_CARD_CNT", "");
			tradInfoMap.put("KFB_ACCT_NO", "86010034844");
			tradInfoMap.put("NEW_USER_FLG", "");
			tradInfoMap.put("REQ_AMT", "50000000");
			tradInfoMap.put("INTEGRATED_CONSELING_YN", "");
			
			
			mockBankingBizUtils.when(()-> BankingBizUtils.toJSONFromStr(any(), any())).thenReturn(tradInfoMap);
			
			
			
			String tradInfo = "";
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				tradInfo = objectMapper.writeValueAsString(tradInfoMap);
			} catch (JsonProcessingException e) {
				log.error("getScreenAndScrapingInfoTest error", e);
			}
			
			RegisterOngoingTradeInfoDataRequest request = new RegisterOngoingTradeInfoDataRequest();
			request.setTradInfo(tradInfo);
			request.setCustNo("1405563");
			request.setBizType("CMJG");
			request.setPrdctNm("퍼스트전세보증론");
			
			RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndScrnDataRcvryData(request);
			assertThat(response).isNotNull();
		}
		
		@Test
		@DisplayName("성공 - case2")
		void registOngoingTradeInfoAndScrnDataRcvryDataTest2() throws Exception {
			
			when(nfTradeInfoMgtDao.insertOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfTradeInfoMgtDao.updateOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap<String,Object> scrnDataInfoMap = new HashMap<String,Object>();
			scrnDataInfoMap.put("YOCDDOK", "N");
			scrnDataInfoMap.put("YOJMNO", "8907101");
			scrnDataInfoMap.put("EMPLY_NM", "");
			scrnDataInfoMap.put("YOFXYN", "N");
			scrnDataInfoMap.put("CLERK_NO", "");
			
			scrnDataInfoMap.put("YOIBUSER", "N");
			scrnDataInfoMap.put("CUST_NO", "4652");
			scrnDataInfoMap.put("BIZ_TYPE", "CASA");
			scrnDataInfoMap.put("isCSL", "N");
			scrnDataInfoMap.put("YOEXIST", "Y");
			scrnDataInfoMap.put("PRDCT_CD", "PRDACT0002464");
			scrnDataInfoMap.put("CNNCTN_WAY", "");
			scrnDataInfoMap.put("YOPAGI", "N");
			scrnDataInfoMap.put("PRDCT_NM", "내지갑통장");
			
			String scrnDataInfo = "";
			try {
				
				scrnDataInfo = objectMapper.writeValueAsString(scrnDataInfoMap);
			} catch (JsonProcessingException e) {
				log.error("getScreenAndScrapingInfoTest error", e);
			}
			
			HashMap<String,Object> tradInfoMap = new HashMap<String,Object>();
			tradInfoMap.put("PRDCT_ID", "3005");
			tradInfoMap.put("PRDCT_CD", "PRDLON0003005");
			tradInfoMap.put("PRDCT_NM", "퍼스트전세보증론");
			tradInfoMap.put("ALLIANC_CD", "");
			tradInfoMap.put("EFFCTV_INT", "");
			tradInfoMap.put("AUTHNT_IND", "");
			tradInfoMap.put("AUTHNT_IND_CD", "");
			tradInfoMap.put("AUTHNT_REQ_DT", "");
			tradInfoMap.put("AUTHNT_REQ_CMPLTN_DT", "");
			tradInfoMap.put("BRNCH_NO", "860");
			tradInfoMap.put("CDD_REQ_CD", "");
			tradInfoMap.put("CLERK_NO", "");
			tradInfoMap.put("ERR_CD", "");
			tradInfoMap.put("ERR_MSG", "");
			tradInfoMap.put("PER_PART", "");
			tradInfoMap.put("DOC_EVCD_CD", "");
			tradInfoMap.put("HL_PRSN_CD", "");
			tradInfoMap.put("AD_SBST_CD", "");
			tradInfoMap.put("KW_INSR_CD", "");
			tradInfoMap.put("GOVM_DATA_TYPE", "");
			tradInfoMap.put("GOVM_SIGN_TYPE", "");
			tradInfoMap.put("PRE_LOAN_MOVE_YN", "");
			tradInfoMap.put("REAL_LOAN_MOVE_YN", "");
			tradInfoMap.put("HQ_PRIME_RATE_APPLY_YN", "");
			tradInfoMap.put("HQ_PRIME_RATE", "");
			tradInfoMap.put("LOAN_PURPOSE", "");
			tradInfoMap.put("LOAN_PRD", "");
			tradInfoMap.put("RDMPTN_MTHD", "");
			tradInfoMap.put("EXEC_DT", "");
			tradInfoMap.put("RDMPTN_DT", "");
			tradInfoMap.put("LOAN_REQ_NO", "");
			tradInfoMap.put("LOAN_ACCPT_NO", "");
			tradInfoMap.put("COPL_CMMN_CLLTRL_IND", "");
			tradInfoMap.put("RJCTN_CD", "");
			tradInfoMap.put("RJCTN_MSG", "");
			tradInfoMap.put("LOAN_HOPE_DT", "");
			tradInfoMap.put("EASY_LOAN_YN", "");
			tradInfoMap.put("LOAN_DOC_INVSTGT_FLG", "");
			tradInfoMap.put("SECURITY_ACCT_NO", "");
			tradInfoMap.put("COLLATERAL_TYPE", "");
			tradInfoMap.put("OPL_CNSLT_YN", "");
			tradInfoMap.put("DEL_OBJ_FLG", "");
			tradInfoMap.put("ID_CARD_CD", "");
			tradInfoMap.put("ID_CARD_CNT", "");
			tradInfoMap.put("KFB_ACCT_NO", "86010034844");
			tradInfoMap.put("NEW_USER_FLG", "");
			tradInfoMap.put("REQ_AMT", "50000000");
			tradInfoMap.put("INTEGRATED_CONSELING_YN", "");
			
			mockBankingBizUtils.when(()-> BankingBizUtils.toJSONFromStr(any(), any())).thenReturn(tradInfoMap);
			String tradInfo = "";
			try {
				tradInfo = objectMapper.writeValueAsString(tradInfoMap);
			} catch (JsonProcessingException e) {
				log.error("getScreenAndScrapingInfoTest error", e);
			}
			
			RegisterOngoingTradeInfoDataRequest request = new RegisterOngoingTradeInfoDataRequest();
			request.setTradInfo(tradInfo);
			request.setScrnDataInfo(scrnDataInfo);
			request.setCustNo("1405563");
			request.setBizType("CMJG");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndScrnDataRcvryData(request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행상태 등록/수정(LMS발송) (asis : MA3CMMBIZ010_10AS)")
	class registOngoingTradeInfoAndSendLms {
		
		@Test
		@DisplayName("성공 - ")
		void registOngoingTradeInfoAndSendLmsTest1() throws Exception {
			
	
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			request.setCustNo("1405563");
			request.setBizType("CRPL");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			request.setCddReqCd("N");
			request.setCnnctnWay("111111111111");
			
			assertThrows(Exception.class, () -> {
				RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndSendLms(request);
			});
		}
		
		@Test
		@DisplayName("성공 - 등록 case2")
		void registOngoingTradeInfoAndSendLmsTest2() throws Exception {
			
			when(nfTradeInfoMgtDao.insertOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			request.setCustNo("1405563");
			request.setBizType("CRPL");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			request.setCddReqCd("N");
			RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndSendLms(request);
			assertThat(response).isNotNull();
		}
		
		@Test
		@DisplayName("성공 - 수정 case3")
		void registOngoingTradeInfoAndSendLmsTest3() throws Exception {
			
			when(nfTradeInfoMgtDao.updateOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				return result;
			});
			
			when(nfCustMgtDao.updateNonFaceCustomerInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(any())).thenAnswer(o->{
				TradeInfoInquiryResult result = new TradeInfoInquiryResult();
				result.setTradNo("3502988");
				result.setBizType("CRPL");
				result.setPrdctNm("모아예금");
				return result;
			});
			
			mockPropertiesUtils.when(()-> PropertiesUtils.getString("SALES_CASA_PRODUCT_NM")).thenReturn("제일EZ|내월급|마이100|내지갑|마이시그니처|마이줌|마이심플|두드림|두드림2U|Hi|모아예금");
			mockCodeUtils.when(()-> CodeUtils.getCodeValue(any(), any())).thenReturn("1588-1599###NB#입출금 신청완료#FJ5-LC6-DT7#[SC제일은행] [~~PRDCT_NM~~] ~~KFB_ACCT_NO~~계좌개설이 완료되었어요. 해당 계좌는 비대면으로 개설된 계좌로 출금 및 이체한도 금액에 제한이 있어요. 한도 제한해제를 원하시는 고객님께서는 모바일뱅킹 앱의 한도제한 해제 메뉴를 이용하시거나 신분증 및 금융거래목적확인서류를 지참 후 가까운 영업점을 방문해 주세요. ~~host~~");
			
			
			when(lmsComponent.sendMain(any())).thenAnswer(o->{
				return "O";
			});
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			request.setCustNo("1405563");
			request.setBizType("CASA");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			request.setPrgrssStsCd("DONE");
			request.setLmsID("CASA");
			RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndSendLms(request);
			assertThat(response).isNotNull();
		}
	}
	
	@Nested
    @DisplayName("진행상태 등록/변경 및 제휴처 거래 관련 처리 (asis : MA3CMMBIZ010_103S)")
	class registOngoingTradeInfoAndPartnerTransaction {
		
		@Test
		@DisplayName("성공 - ")
		void registOngoingTradeInfoAndPartnerTransactionTest1() throws Exception {
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			request.setCustNo("1405563");
			request.setBizType("CRPL");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			request.setCddReqCd("N");
			request.setCnnctnWay("111111111111");
			
			assertThrows(Exception.class, () -> {
				RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndPartnerTransaction(request);
			});
		}
		
		@Test
		@DisplayName("성공 - 등록 case2")
		void registOngoingTradeInfoAndPartnerTransactionTest2() throws Exception {
			
			when(nfTradeInfoMgtDao.insertOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			request.setCustNo("1405563");
			request.setBizType("CRPL");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			request.setCddReqCd("N");
			request.setTargetProcess("NF_TRADINFO_MGT_I_02");
			RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndPartnerTransaction(request);
			assertThat(response).isNotNull();
		}
		
		@Test
		@DisplayName("성공 - 수정 case3")
		void registOngoingTradeInfoAndPartnerTransactionTest3() throws Exception {
			
			when(nfTradeInfoMgtDao.updateOngoingTradeInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				return result;
			});
			
			when(nfCustMgtDao.updateNonFaceCustomerInfo(any())).thenAnswer(o->{
				return 1;
			});
			
			when(nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(any())).thenAnswer(o->{
				TradeInfoInquiryResult result = new TradeInfoInquiryResult();
				result.setTradNo("3502988");
				result.setBizType("CRPL");
				result.setPrdctNm("모아예금");
				return result;
			});
			
			mockPropertiesUtils.when(()-> PropertiesUtils.getString("toss.cnnctn_way")).thenReturn("000120");
			
			RegisterOngoingTradeInfoRequest request = new RegisterOngoingTradeInfoRequest();
			request.setCustNo("1405563");
			request.setBizType("MAPL");
			request.setPrdctNm("퍼스트전세보증론");
			request.setTradNo("3502988");
			request.setTargetProcess("NF_TRADINFO_MGT_U_01");
			request.setPrgrssStsCd("DONE");
			request.setCallCntrStsCd("PLE2");
			request.setCnnctnWay("000120");
			RegisterOngoingTradeInfoResponse response = tradInfoComponent.registOngoingTradeInfoAndPartnerTransaction(request);
			assertThat(response).isNotNull();
		}
	}
	
	@Test
	@DisplayName("진행중인 상품 취소 처리 (asis : MA3CMMBIZ010_107S)")
	void cancelOngoingTradeInfoTest() throws Exception {
		
		when(nfTradeInfoMgtDao.updateOngoingProductCancellation(any())).thenAnswer(o->{
			return 1;
		});
		
		OngoingTradeInfoCancelRequest request = new OngoingTradeInfoCancelRequest();
		request.setCustNo("1405563");
		request.setTradNo("3502988");
		OngoingTradeInfoCancelResponse response = tradInfoComponent.cancelOngoingTradeInfo(request);
		assertThat(response).isNotNull();
	}
	
	@Nested
    @DisplayName("UI 진행상태 업데이트 처리 – 대출 취소(MCI) (asis : MA3CMMBIZ010_108S)")
	class cancelOnoingProcess {
		@ParameterizedTest
		@ValueSource(strings = { "NSPL", "MAPL" })
		@DisplayName("성공 -1")
		void cancelOnoingProcessTest1(String bizType) throws Exception {
			when(sessionManager.isLogin()).thenReturn(true);
			when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("2012121******");
			
			when(nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(any())).thenAnswer(o->{
				TradeInfoInquiryResult result = new TradeInfoInquiryResult();
				result.setCustNo("1405563");
				result.setTradNo("3502988");
				result.setBizType(bizType);
				result.setCallCntrStsCd("PRGR");
				return result;
			});
			
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				result.setCustNo("1405563");
				return result;
			});
			
			MciResponse<MciYp0049020001Res> mciResponse = mock(MciResponse.class);
			when(hostClient.getMciRequestOptions("MCI_YP0049020001")).thenReturn(mciRequestOptions);
			when(hostClient.sendMci(any(), any(), eq(MciYp0049020001Res.class))).thenReturn(mciResponse);
			when(mciResponse.getResponse()).thenAnswer(o -> {
				MciYp0049020001Res hRes = new MciYp0049020001Res();
				return hRes;
			});
			when(tradeInfoMapper.toResponseFromMciYp0049010001Res(any())).thenAnswer(o->{
				OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
			
				return response;
			});
			when(tradeInfoMapper.toResponseFromMciYp0049020001Res(any())).thenAnswer(o->{
				OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
			
				return response;
			});
			when(tradeInfoMapper.toResponseFromMciDy7200170001Res(any())).thenAnswer(o->{
				OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
			
				return response;
			});
			
			
			MciResponse<MciYp0049010001Res> mciResponse2 = mock(MciResponse.class);
			when(hostClient.getMciRequestOptions("MCI_YP0049010001")).thenReturn(mciRequestOptions);
			when(hostClient.sendMci(any(), any(), eq(MciYp0049010001Res.class))).thenReturn(mciResponse2);
			when(mciResponse2.getResponse()).thenAnswer(o -> {
				MciYp0049010001Res hRes = new MciYp0049010001Res();
				return hRes;
			});
			
			MciResponse<MciDy7200170001Res> mciResponse3 = mock(MciResponse.class);
			when(hostClient.getMciRequestOptions("MCI_DY7200170001")).thenReturn(mciRequestOptions);
			when(hostClient.sendMci(any(), any(), eq(MciDy7200170001Res.class))).thenReturn(mciResponse3);
			when(mciResponse3.getResponse()).thenAnswer(o -> {
				MciDy7200170001Res hRes = new MciDy7200170001Res();
				return hRes;
			});
			
			OngoingProcessCancelRequest request = new OngoingProcessCancelRequest();
			request.setCustNo("");// 1405563
			request.setTradNo("3502988");
			OngoingProcessCancelResponse response = tradInfoComponent.cancelOnoingProcess(request);
			assertThat(response).isNotNull();
		}
		
		@ParameterizedTest
		@ValueSource(strings = { "CMJG" })
		@DisplayName("성공 -2")
		void cancelOnoingProcessTest2(String bizType) throws Exception {
			when(sessionManager.isLogin()).thenReturn(true);
			when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("2012121******");
			
			when(nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(any())).thenAnswer(o->{
				TradeInfoInquiryResult result = new TradeInfoInquiryResult();
				result.setCustNo("1405563");
				result.setTradNo("3502988");
				result.setBizType(bizType);
				result.setCallCntrStsCd("JGRR");
				return result;
			});
			
			when(nfCustMgtDao.selectNonFaceCustomerInfo(any())).thenAnswer(o->{
				NonFaceCustomerInfoInquiryResult result = new NonFaceCustomerInfoInquiryResult();
				result.setCustNo("1405563");
				return result;
			});
			
			MciResponse<MciYp0049020001Res> mciResponse = mock(MciResponse.class);
			when(hostClient.getMciRequestOptions("MCI_YP0049020001")).thenReturn(mciRequestOptions);
			when(hostClient.sendMci(any(), any(), eq(MciYp0049020001Res.class))).thenReturn(mciResponse);
			when(mciResponse.getResponse()).thenAnswer(o -> {
				MciYp0049020001Res hRes = new MciYp0049020001Res();
				return hRes;
			});
			when(tradeInfoMapper.toResponseFromMciYp0049010001Res(any())).thenAnswer(o->{
				OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
			
				return response;
			});
			when(tradeInfoMapper.toResponseFromMciYp0049020001Res(any())).thenAnswer(o->{
				OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
			
				return response;
			});
			when(tradeInfoMapper.toResponseFromMciDy7200170001Res(any())).thenAnswer(o->{
				OngoingProcessCancelResponse response = new OngoingProcessCancelResponse();
			
				return response;
			});
			
			
			MciResponse<MciYp0049010001Res> mciResponse2 = mock(MciResponse.class);
			when(hostClient.getMciRequestOptions("MCI_YP0049010001")).thenReturn(mciRequestOptions);
			when(hostClient.sendMci(any(), any(), eq(MciYp0049010001Res.class))).thenReturn(mciResponse2);
			when(mciResponse2.getResponse()).thenAnswer(o -> {
				MciYp0049010001Res hRes = new MciYp0049010001Res();
				return hRes;
			});
			
			MciResponse<MciDy7200170001Res> mciResponse3 = mock(MciResponse.class);
			when(hostClient.getMciRequestOptions("MCI_DY7200170001")).thenReturn(mciRequestOptions);
			when(hostClient.sendMci(any(), any(), eq(MciDy7200170001Res.class))).thenReturn(mciResponse3);
			when(mciResponse3.getResponse()).thenAnswer(o -> {
				MciDy7200170001Res hRes = new MciDy7200170001Res();
				return hRes;
			});
			
			OngoingProcessCancelRequest request = new OngoingProcessCancelRequest();
			request.setCustNo("");// 1405563
			request.setTradNo("3502988");
			
			OngoingProcessCancelResponse response = tradInfoComponent.cancelOnoingProcess(request);
			assertThat(response).isNotNull();
		}
	}
	
	
	@Test
	@DisplayName("비대면 화면정보관리 등록 (asis : MA3CMMBIZ010_109S)")
	void registNonFaceScreenInfoTest() throws Exception {
		
		when(nfScreenInfoMgtDao.insertNonFaceScreenInfoManagement(any())).thenAnswer(o->{
			return 1;
		});
		
		String scrnDataInfo = "{"
				+ " \"YOCDDOK\": \"N\","
				+ " \"YOJMNO\": \"8907101\","
				+ " \"EMPLY_NM\": \"\","
				+ " \"YOFXYN\": \"N\","
				+ " \"CLERK_NO\": \"\","
				+ " \"YOIBUSER\": \"N\","
				+ " \"CUST_NO\": \"4652\","
				+ " \"BIZ_TYPE\": \"CASA\","
				+ " \"isCSL\": \"N\","
				+ " \"YOEXIST\": \"Y\","
				+ " \"PRDCT_CD\": \"PRDACT0002464\","
				+ " \"CNNCTN_WAY\": \"\","
				+ " \"YOPAGI\": \"N\","
				+ " \"PRDCT_NM\": \"내지갑통장\","
				+ " \"CNNCTN_TRAD_NO\": \"\","
				+ " \"PRDCT_ID\": \"2464\","
				+ " \"DUSTYN\": \"N\","
				+ " \"YOACMFBRArray\": [\"860\"],"
				+ " \"YONAME\": \"업무테스트\","
				+ " \"YO_DASU\": \"1\","
				+ " \"YO_DEPO\": \"1\","
				+ " \"nextURL\": \"/product/casa/MA3ACTOPN001_30V.view\","
				+ " \"productCode\": \"2464\","
				+ " \"productName\": \"내지갑통장\"}";
		RegisterNonFaceScreenInfoRequest request = new RegisterNonFaceScreenInfoRequest();
		request.setCustNo("1405563");
		request.setTradNo("3502988");
		request.setScrnDataInfo(scrnDataInfo);
		
		RegisterNonFaceScreenInfoResponse response = tradInfoComponent.registNonFaceScreenInfo(request);
		assertThat(response).isNotNull();
	}
	
	@Test
	@DisplayName("스크래핑데이터 저장 (asis : MA3CMMBIZ015_101S)")
	void registScrapingDataTest() throws Exception {
		
		when(nfScrapingMgtDao.updateScrapingInfo(any())).thenAnswer(o->{
			return 1;
		});
		
		String scrppngData = "{"
				+ " \"YOCDDOK\": \"N\","
				+ " \"YOJMNO\": \"8907101\","
				+ " \"EMPLY_NM\": \"\","
				+ " \"YOFXYN\": \"N\","
				+ " \"CLERK_NO\": \"\","
				+ " \"YOIBUSER\": \"N\","
				+ " \"CUST_NO\": \"4652\","
				+ " \"BIZ_TYPE\": \"CASA\","
				+ " \"isCSL\": \"N\","
				+ " \"YOEXIST\": \"Y\","
				+ " \"PRDCT_CD\": \"PRDACT0002464\","
				+ " \"CNNCTN_WAY\": \"\","
				+ " \"YOPAGI\": \"N\","
				+ " \"PRDCT_NM\": \"내지갑통장\","
				+ " \"CNNCTN_TRAD_NO\": \"\","
				+ " \"PRDCT_ID\": \"2464\","
				+ " \"DUSTYN\": \"N\","
				+ " \"YOACMFBRArray\": [\"860\"],"
				+ " \"YONAME\": \"업무테스트\","
				+ " \"YO_DASU\": \"1\","
				+ " \"YO_DEPO\": \"1\","
				+ " \"nextURL\": \"/product/casa/MA3ACTOPN001_30V.view\","
				+ " \"productCode\": \"2464\","
				+ " \"productName\": \"내지갑통장\"}";
		RegisterScrapingDataRequest request = new RegisterScrapingDataRequest ();
		request.setCustNo("1405563");
		request.setScrppngData(scrppngData);
		
		RegisterScrapingDataResponse response = tradInfoComponent.registScrapingData(request);
		assertThat(response).isNotNull();
	}
	
	@Test
	@DisplayName("스크래핑데이터 저장 (asis : MA3CMMBIZ015_101S)")
	void registEdocRecoveryDataTest() throws Exception {
		
		when(nfCddEdocRcvryMgtDao.insertCddElectronicDocumentInfo(any())).thenAnswer(o->{
			return 1;
		});
		
		String recoveryData  = "{"
				+ " \"YOCDDOK\": \"N\","
				+ " \"YOJMNO\": \"8907101\","
				+ " \"EMPLY_NM\": \"\","
				+ " \"YOFXYN\": \"N\","
				+ " \"CLERK_NO\": \"\","
				+ " \"YOIBUSER\": \"N\","
				+ " \"CUST_NO\": \"4652\","
				+ " \"BIZ_TYPE\": \"CASA\","
				+ " \"isCSL\": \"N\","
				+ " \"YOEXIST\": \"Y\","
				+ " \"PRDCT_CD\": \"PRDACT0002464\","
				+ " \"CNNCTN_WAY\": \"\","
				+ " \"YOPAGI\": \"N\","
				+ " \"PRDCT_NM\": \"내지갑통장\","
				+ " \"CNNCTN_TRAD_NO\": \"\","
				+ " \"PRDCT_ID\": \"2464\","
				+ " \"DUSTYN\": \"N\","
				+ " \"YOACMFBRArray\": [\"860\"],"
				+ " \"YONAME\": \"업무테스트\","
				+ " \"YO_DASU\": \"1\","
				+ " \"YO_DEPO\": \"1\","
				+ " \"nextURL\": \"/product/casa/MA3ACTOPN001_30V.view\","
				+ " \"productCode\": \"2464\","
				+ " \"productName\": \"내지갑통장\"}";
		RegisterEdocRecoveryDataRequest request = new RegisterEdocRecoveryDataRequest ();
		request.setCustNo("1405563");
		request.setRcvryData(recoveryData);
		
		RegisterEdocRecoveryDataResponse response = tradInfoComponent.registEdocRecoveryData(request);
		assertThat(response).isNotNull();
	}
	
	@Test
	@DisplayName("화면정보 및 스크래핑 데이터 조회 (asis : MA3CMMBIZ015_102S)")
	void getScreenAndScrapingInfoTest() throws Exception {
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("TRAD_NO", "1405563");
		map.put("BIZ_TYPE", "SPPL-NAME");
		map.put("PRDCT_ID", "PRDCT_ID");
		map.put("PRDCT_CD", "PRDCT_CD");
		map.put("PRDCT_NM", "PRDCT_NM");
		
		when(nfScreenInfoMgtDao.selectScreenAndScrapingInfo(any())).thenAnswer(o->{
			NonFaceScreenAndScrapingInfoResult result = new NonFaceScreenAndScrapingInfoResult();
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				result.setScrnDataInfo(objectMapper.writeValueAsString(map));
			} catch (JsonProcessingException e) {
				log.error("getScreenAndScrapingInfoTest error", e);
			}
			result.setScrppngData("");
			return result;
		});

		
		mockBankingBizUtils.when(()-> BankingBizUtils.toJSONFromStr(any(), any())).thenReturn(map);
		
		when(nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(any())).thenAnswer(o->{
			TradeInfoInquiryResult result = new TradeInfoInquiryResult();
			result.setCustNo("1405563");
			result.setTradNo("3502988");
			result.setBizType("SPPL");
			result.setCallCntrStsCd("PRGR");
			return result;
		});
		
		ScreenAndScrapingInfoRequest request = new ScreenAndScrapingInfoRequest ();
		request.setCustNo("1405563");
		request.setTradNo("1405563");
		request.setBizType("SPPL");
		ScreenAndScrapingInfoResponse response = tradInfoComponent.getScreenAndScrapingInfo(request);
		assertThat(response).isNotNull();
	}
	
	@Test
	@DisplayName("비대면 화면정보 조회 [MA3CMMBIZ015_102S]")
	void getNfScrnInfoTest() throws Exception {
		
		when( nfScrnInfoMgtDao.selectNfScrnInfo(any())).thenAnswer(o->{
			NfScrnInfoInqiryReult  result = new NfScrnInfoInqiryReult ();
			result.setCustNo("1405563");
			result.setTradNo("3502988");
			result.setBizType("SPPL");
			return result;
		});
		
		when(nfScreenInfoMgtDao.selectScreenAndScrapingInfo(any())).thenAnswer(o->{
			NonFaceScreenAndScrapingInfoResult result = new NonFaceScreenAndScrapingInfoResult();
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("TRAD_NO", "1405563");
			map.put("BIZ_TYPE", "SPPL-NAME");
			map.put("PRDCT_ID", "PRDCT_ID");
			map.put("PRDCT_CD", "PRDCT_CD");
			map.put("PRDCT_NM", "PRDCT_NM");
			
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				result.setScrnDataInfo(objectMapper.writeValueAsString(map));
			} catch (JsonProcessingException e) {
				log.error("getScreenAndScrapingInfoTest error", e);
			}
			result.setScrppngData("");
			return result;
		});

		when(nfTradeInfoMgtDao.selectTradeInfoAndCustomerSsn(any())).thenAnswer(o->{
			TradeInfoInquiryResult result = new TradeInfoInquiryResult();
			result.setCustNo("1405563");
			result.setTradNo("3502988");
			result.setBizType("SPPL");
			result.setCallCntrStsCd("PRGR");
			return result;
		});
		
		NfScrnInfoInqiryRequest request = new NfScrnInfoInqiryRequest ();
		request.setBizType("SPPL");
		NfScrnInfoInqiryResponse response = tradInfoComponent.getNfScrnInfo(request);
		assertThat(response).isNotNull();
	}
	
	@Test
	@DisplayName("해외거래내역 저장 [MA3CMMBIZ018_301S]")
	void registIntlTradInfoTest() throws Exception {
		
		when(nfTradinfoBizcdMgtDao.deleteTradInfoBizCd(any())).thenAnswer(o->{
			return 1;
		});
		
		List<HashMap<String,Object>> list = new ArrayList<>();
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("CODE", "CODE");
		map.put("CODE_NM", "CODE_NM");
		map.put("CNT", "CNT");

		list.add(map);
		
		mockBankingBizUtils.when(()-> BankingBizUtils.toJSONFromStr(any(), any())).thenReturn(list);
		
		String gaeSaCodeStr = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			gaeSaCodeStr = objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			log.error("getScreenAndScrapingInfoTest error", e);
		}
		
		RegistIntlTradInfoRequest request = new RegistIntlTradInfoRequest ();
		request.setGaesaCode(gaeSaCodeStr);
		request.setCustNo("1405563");
		request.setTradNo("3502988");
		RegistIntlTradInfoResponse response = tradInfoComponent.registIntlTradInfo(request);
		assertThat(response).isNotNull();
	}
}
