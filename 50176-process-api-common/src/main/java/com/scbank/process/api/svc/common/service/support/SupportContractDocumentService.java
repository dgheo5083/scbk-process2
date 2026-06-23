package com.scbank.process.api.svc.common.service.support;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.initech.core.util.URLDecoder;
import com.scbank.process.api.edmi.dto.mci.MciIbEdms001Res;
import com.scbank.process.api.edmi.dto.mci.MciIbEdms002Req;
import com.scbank.process.api.edmi.dto.mci.MciIbEdms002Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38100Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38100Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38300Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38500Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38500Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H38600Res;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
// import com.scbank.process.api.fw.channel.message.file.FileDownloadObject;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.SupportCustomerCenter;
import com.scbank.process.api.svc.common.dao.Ma30FnanclPrdctCmmnDao;
import com.scbank.process.api.svc.common.dao.PrdctDisclosureMgtDao;
import com.scbank.process.api.svc.common.dao.PrdctDocListDao;
import com.scbank.process.api.svc.common.dao.SpcfcMoneyTrustIsaPrdctDao;
import com.scbank.process.api.svc.common.dao.dto.AddCntrctDocument;
import com.scbank.process.api.svc.common.dao.dto.AddCntrctDocumentResult;
import com.scbank.process.api.svc.common.dao.dto.CommonAgreeResult;
import com.scbank.process.api.svc.common.dao.dto.ContractDocumentParameter;
import com.scbank.process.api.svc.common.dao.dto.ContractDocumentResult;
import com.scbank.process.api.svc.common.dao.dto.ProductFileResult;
import com.scbank.process.api.svc.common.dao.dto.TrustContractDocumentResult;
import com.scbank.process.api.svc.common.mapper.SupportContractDocumentMapper;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetEdmsPdfFileRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetEdmsPdfFileResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetFundDetailRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetFundDetailResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetLoanDetailRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetLoanDetailResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetTrustAccountDetailRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocGetTrustAccountDetailResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocHasEdmsPdfFileRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocHasEdmsPdfFileResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListApplyRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListApplyResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListCardRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListCardResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListDepositAndWithdrawalRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListDepositAndWithdrawalResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListDocProvisionAccountRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListDocProvisionAccountResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListForeignRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListForeignResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListFundRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListFundRequestRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListFundRequestResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListFundResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListTrustAccountRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocListTrustAccountResponse;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocUpdateEdmsPdfFileDownloadCountRequest;
import com.scbank.process.api.svc.common.service.support.dto.contractdocument.SupDocUpdateEdmsPdfFileDownloadCountResponse;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.edoc.EdocAPComponent;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.FormatUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-계약서류조회", url = "/support/contractDocument")
public class SupportContractDocumentService {

	/**
	 * 업무 컴포넌트
	 */
	private final AccountListComponent accountListComponent;
	// 세션
	private final ISessionContextManager sessionManager;

	private final Ma30FnanclPrdctCmmnDao ma30FnanclPrdctCmmnDao;

	private final SpcfcMoneyTrustIsaPrdctDao spcfcMoneyTrustIsaPrdctDao;

	private final PrdctDisclosureMgtDao prdctDisclosureMgtDao;

	private final PrdctDocListDao prdctDocListDao;

	private final EdocAPComponent edocAPComponent;

	// private final FileDownloadObject fileDownloadObject;

	// EDMI 연동 클라이언트 주입
	private final HostClient hostClient;

	// DTO 변환 Mapper 컴포넌트 주입
	private final SupportContractDocumentMapper mapper;

	private final SupportCustomerCenter supportCustomerCenter;

	@ServiceEndpoint(url = "/listDocProvisionAccount", name = "계약서류제공 계좌목록 조회")
	public SupDocListDocProvisionAccountResponse listDocProvisionAccount(IServiceContext serviceContext,
			SupDocListDocProvisionAccountRequest request) {
		// ASIS :: MA3CSTDOC000_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		String finalUseDate = FormatUtils
				.getFrmDate(StringUtils.nvl(sessionManager.getLoginValue("FinalUseDate", String.class), "-"));
		String finalUseTime = FormatUtils
				.getFrmDate(StringUtils.nvl(sessionManager.getLoginValue("FinalUseTime", String.class), ""));
		String loginType = sessionManager.getLoginValue("ConnectType", String.class); // 로그인 타입 구분 1:cert, 2:id/pw,
																						// 8:BlockChainLogin 뱅크사인,
																						// 9:디지털인증
		// 응답 객체
		SupDocListDocProvisionAccountResponse response = new SupDocListDocProvisionAccountResponse();

		// try {

		// List<DepositAccountInfo> depositAcctList = null; // 예적금 계좌목록조회
		// List<DepositSavingAccountInfo> savingDepositAcctList = null; //
		// 예적금(10,20,30,80,85,88,90) 계좌목록조회
		// List<FxAccountInfo> foreignAcctList = null; // FX 계좌목록조회
		// List<DepositTrustAccountInfo> trustAcctList = null; // 신탁 계좌목록조회

		// List<FundAccountInfo> fundAcctList = null; // 신탁 펀드계좌목록조회
		// List<LoanAccountInfo> loanAcctList = null; // 대출계좌목록조회
		// List<CardAccountInfo> cardAcctList = null; // 카드 계좌목록조회

		// List<AllAccountInfo> allAccountList =
		// this.accountListComponent.getAllAccountList();

		// depositAcctList = this.accountListComponent.getDepositAccountList();
		// savingDepositAcctList =
		// this.accountListComponent.getDepositSavingAccountList();
		// foreignAcctList = this.accountListComponent.getFxAccountList();
		// trustAcctList = this.accountListComponent.getDepositTrustAccountList();
		// fundAcctList = this.accountListComponent.getFundAccountList();
		// loanAcctList = this.accountListComponent.getLoanAccountList(false);
		// cardAcctList = this.accountListComponent.getCardAccountList();
		List<AllAccountInfo> depositAcctList = new ArrayList<>(); // 입출금
		List<AllAccountInfo> savingDepositAcctList = new ArrayList<>(); // 예적금
		List<AllAccountInfo> foreignAcctList = new ArrayList<>(); // 예금 외환
		List<AllAccountInfo> trustAcctList = new ArrayList<>(); // 예금 ISA/신탁
		List<AllAccountInfo> fundAcctList = new ArrayList<>(); // 펀드
		List<AllAccountInfo> loanAcctList = new ArrayList<>(); // 대출
		List<AllAccountInfo> cardAcctList = new ArrayList<>(); // 카드전체
		List<AllAccountInfo> hdCardAcctList = new ArrayList<>(); // 현대카드
		List<AllAccountInfo> bcCardAcctList = new ArrayList<>(); // bc카드

		BigDecimal depositTotalAmt = BigDecimal.ZERO;
		BigDecimal savingDepositTotalAmt = BigDecimal.ZERO;
		BigDecimal trustTotalAmt = BigDecimal.ZERO;
		BigDecimal fundTotalAmt = BigDecimal.ZERO;
		BigDecimal loanTotalAmt = BigDecimal.ZERO;
		// BigDecimal allTotalAmt = BigDecimal.ZERO;

		Integer acctSubCode = 0;// 계좌 과목코드
		String acctNum = ""; // 계좌번호
		String acctType = ""; // 계좌구분
		String depKind = ""; // 카드구분
		BigDecimal balance = BigDecimal.ZERO;

		List<AllAccountInfo> allAcctList = this.accountListComponent.getAllAccountList();
		if (!CollectionUtils.isEmpty(allAcctList)) {
			for (AllAccountInfo info : allAcctList) {
				acctNum = info.getDrawAcctNum();
				acctSubCode = Integer.parseInt(acctNum.substring(3, 5));
				acctType = info.getAcctType();
				depKind = info.getDepKind();
				balance = info.getBalance() == null ? BigDecimal.ZERO : info.getBalance();

				if ("1".equals(acctType) && acctSubCode != 73) {
					if (acctSubCode >= 51 && acctSubCode <= 59) { // ISA/신탁
						trustAcctList.add(info);

						if (!"35".equals(info.getAssort())) { // 금액 합계 외화제외
							if ("-".equals(info.getBalSign())) {
								trustTotalAmt = trustTotalAmt.subtract(balance);
							} else {
								trustTotalAmt = trustTotalAmt.add(balance);
							}
						}
					} else if (acctSubCode == 46 || acctSubCode == 48 || acctSubCode == 49 || acctSubCode == 70
							|| acctSubCode == 90 || acctSubCode == 80) {
						savingDepositAcctList.add(info);
						if ("-".equals(info.getBalSign())) {
							savingDepositTotalAmt = savingDepositTotalAmt.subtract(balance);
						} else {
							savingDepositTotalAmt = savingDepositTotalAmt.add(balance);
						}
					} else if (acctSubCode >= 85 && acctSubCode <= 89) { // 예금 외환
						foreignAcctList.add(info);
					} else if (acctSubCode < 60) { // 예금
						depositAcctList.add(info);
						if ("-".equals(info.getBalSign())) {
							depositTotalAmt = depositTotalAmt.subtract(balance);
						} else {
							depositTotalAmt = depositTotalAmt.add(balance);
						}
					}
				} else if ("2".equals(acctType)) { // 펀드
					fundAcctList.add(info);
					if ("KRW".equals(info.getCurcy())) {
						BigDecimal estimateAmount = info.getEstimateAmount() == null ? BigDecimal.ZERO
								: info.getEstimateAmount();
						if ("-".equals(info.getBalSign())) {
							fundTotalAmt = fundTotalAmt.subtract(estimateAmount);
						} else {
							fundTotalAmt = fundTotalAmt.add(estimateAmount);
						}
					}
				} else if ("3".equals(acctType) || acctSubCode == 73) { // 대출
					loanAcctList.add(info);
					if ("-".equals(info.getBalSign())) {
						loanTotalAmt = loanTotalAmt.subtract(balance);
					} else {
						loanTotalAmt = loanTotalAmt.add(balance);
					}
				} else if ("4".equals(acctType)) {
					if (serviceContext.locale().getLanguage().equalsIgnoreCase("en")) {
						if ("1".equals(info.getCheckCardType())) {
							info.setDrawAcctNameAlias("Credit card");
						} else {
							info.setDrawAcctNameAlias("Debit Card");
						}
					}

					if ("C".equals(depKind)) {
						bcCardAcctList.add(info); // BC카드
						cardAcctList.add(info); // 카드전체
					}

				} else if ("5".equals(acctType)) { // 현대카드
					if (serviceContext.locale().getLanguage().equalsIgnoreCase("en")) {
						if ("1".equals(info.getCheckCardType())) {
							info.setDrawAcctNameAlias("Credit card");
						} else {
							info.setDrawAcctNameAlias("Debit Card");
						}
					}

					// 2022.07.05 현대카드PJ : 현대카드/BC카드 리스트 분리
					// BC카드, 현대카드 구분하여
					if ("H".equals(depKind)) {
						hdCardAcctList.add(info); // 현대카드
						cardAcctList.add(info); // 카드전체
					}

				}

			}
		}

		log.debug("allAccountList :: {}", allAcctList);
		log.debug("depositAcctList :: {}", depositAcctList);
		log.debug("savingDepositAcctList :: {}", savingDepositAcctList);
		log.debug("foreignAcctList :: {}", foreignAcctList);
		log.debug("trustAcctList :: {}", trustAcctList);

		log.debug("fundAcctList :: {}", fundAcctList);
		log.debug("loanAcctList :: {}", loanAcctList);
		log.debug("cardAcctList :: {}", cardAcctList);

		response.setDepositAcctList(depositAcctList);
		response.setSavingDepositAcctList(savingDepositAcctList);
		response.setForeignAcctList(foreignAcctList);
		response.setTrustAcctList(trustAcctList);
		response.setFundAcctList(fundAcctList);
		response.setLoanAcctList(loanAcctList);
		response.setCardAcctList(cardAcctList);

		log.debug("*** response :: {}", response);
		// response.setHdCardYn(hdCardYN);
		// response.setBcCardYn(bcCardYN);
		// response.setLoginType(loginType);

		// // 푸시알림 UX 개선 및 푸시알림 휴일 서비스 개발(sprint27) -2024.03.27
		// // 전체 메뉴 인입 시 기기변경 체크 로직 추가 개발
		// try {
		// String userId = sessionManager.getLoginValue("UserID", String.class);

		// // TODO DB:SMS 추가 필요 => 오라클
		// // smsDbClient.selectList("MA3_MSC.TMB_OBJ_USR_MGT_S_01", paramMap);
		// // List<PushJoinStatusResult> pushJoinYnList =
		// // tmbObjUsrMgtDao.selectPushJoinStatus(userId);
		// List<PushJoinStatusResult> pushJoinYnList = null;
		// if (!pushJoinYnList.isEmpty()) {
		// if (pushJoinYnList.size() > 0) {
		// for (int i = 0; i < 1; i++) {
		// response.setSerno(pushJoinYnList.get(i).getSerNo()); // 고객일련번호
		// }
		// /**
		// * TODO 확인필요 화면에서 사용하는 곳 없는데?
		// * //입출금 내역
		// * output.put("IOTRANLIST_FLAG",
		// pushJoinYnList.get(i).get("IOTRANLIST_FLAG"));
		// * //환율알림 설정
		// * output.put("EXRATE_FLG" , pushJoinYnList.get(i).get("EXRATE_FLG" ));
		// * //맞춤혜택 안내
		// * output.put("BENEFIT_FLAG" , pushJoinYnList.get(i).get("BENEFIT_FLAG" ));
		// * //금융시장 정보
		// * output.put("FINANCE_FLAG" , pushJoinYnList.get(i).get("FINANCE_FLAG" ));
		// * //금융시장 정보 값 - 1:전체, 2:주간정보, 3:월간정보
		// * output.put("FINANCE_VAL" , pushJoinYnList.get(i).get("FINANCE_VAL" ));
		// * //WMLOUNGE 정보
		// * output.put("WMLOUNGE_FLAG" , pushJoinYnList.get(i).get("WMLOUNGE_FLAG" ));
		// */
		// }
		// }

		// // DB조회 결과 저장
		// List<PushJoinAlarmListResult> pushJoinAlarmYnList = null;
		// // pushJoinAlarmYnList = tmbObjUsrMgtDao.selectPushJoinAlarmList(userId);
		// if (pushJoinAlarmYnList != null && !pushJoinAlarmYnList.isEmpty()) {
		// for (int i = 0; i < pushJoinAlarmYnList.size(); i++) {
		// if (i == 0) {
		// if (pushJoinAlarmYnList.get(i).getCnfrmNo() == null
		// || "".equals(pushJoinAlarmYnList.get(i).getCnfrmNo())) {
		// response.setCnfrmNo("");
		// } else {
		// response.setCnfrmNo(pushJoinAlarmYnList.get(i).getCnfrmNo());
		// }
		// /**
		// * TODO 확인필요 화면에서 사용하는 곳 없는데?
		// * if(pushJoinAlarmYnList.get(i).get("APP_GB") == null ||
		// * "".equals(pushJoinAlarmYnList.get(i).get("APP_GB"))){
		// * output.put("APP_GB", "");
		// * }else {
		// * output.put("APP_GB", pushJoinAlarmYnList.get(i).get("APP_GB"));
		// * }
		// */
		// }
		// }
		// } else {
		// /*
		// * 등록된 계좌가 없을 경우 기기변경 되었다고 나오는 버그 수정
		// * 푸쉬등록 정보를 가져와 등록된 기기토큰 값을 출력한다.
		// */
		// PushJoinStatusResult pushJoinAlarmYn = null;
		// // pushJoinAlarmYn = tmbObjUsrMgtDao.selectPushJoinAlarm(userId);
		// if (pushJoinAlarmYn != null) {
		// if (pushJoinAlarmYn.getCnfrmNo() == null ||
		// "".equals(pushJoinAlarmYn.getCnfrmNo())) {
		// response.setCnfrmNo("");
		// } else {
		// response.setCnfrmNo(pushJoinAlarmYn.getCnfrmNo());
		// }
		// /**
		// * TODO 확인필요 화면에서 사용하는 곳 없는데?
		// * if(pushJoinAlarmYn.get("APP_GB") == null ||
		// * "".equals(pushJoinAlarmYn.get("APP_GB"))){
		// * output.put("APP_GB", "");
		// * }else {
		// * output.put("APP_GB", pushJoinAlarmYn.get("APP_GB"));
		// * }
		// */
		// }
		// }

		// } catch (Exception e) {

		// }

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	/**
	 * @param serviceContext
	 * @param request
	 * @return
	 */
	@ServiceEndpoint(url = "/listDepositAndWithdrawal", name = "계약서류제공 수신(입출금, 예금, 적금) 신청내역조회")
	public SupDocListDepositAndWithdrawalResponse listDepositAndWithdrawal(IServiceContext serviceContext,
			SupDocListDepositAndWithdrawalRequest request) {
		// ASIS :: MA3CSTDOC001_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		String pAcctNum = StringUtils.nvl(request.getDrawAcctNum(), "");
		// String yiURL = StringUtils.nvl(request.getYiURL(), "");
		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		String tsPassword = sessionManager.getGlobalValue("TSPassword", String.class);
		log.info(" UserID : {} ", userID);

		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		log.info(" perBusNo : {} ", perBusNo);

		// 응답 객체
		SupDocListDepositAndWithdrawalResponse response = new SupDocListDepositAndWithdrawalResponse();

		// try {

		// 공통부 세팅
		// TI1IBK01_H386 (CB_IBK01_H385)
		OltpRequestOptions h385Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H385");

		h385Option.setImsTranCd("TI1IBK01");
		h385Option.setInClassCd("H385");
		h385Option.setSvcCd("385");
		h385Option.setCaptureSystem("OLTP");

		// 개별부 세팅
		CbIbk01H38500Req h38500Req = new CbIbk01H38500Req();
		h38500Req.setUserID(userID); // 이용자번호
		h38500Req.setTSPassword(tsPassword); // 통신비밀번호
		// h38500Req.setYIJUMIN("");
		h38500Req.setYIUPGB("9");
		h38500Req.setYIBRNO(Integer.valueOf(pAcctNum.substring(0, 3))); // 점 번호
		h38500Req.setYIKMCD(Integer.valueOf(pAcctNum.substring(3, 5))); // 과목코드
		h38500Req.setYIBUNHO(Integer.valueOf(pAcctNum.substring(5, 11))); // 계좌번호
		// scbkSendData.putBody("YIUSID", SessionManager.getLoginValue("UserID")); //
		// 이용자번호
		// scbkSendData.putBody("YIPASS", SessionManager.getLoginValue("TSPassword"));
		// // 로그인비밀번호
		// scbkSendData.putBody("YIUPGB", "9"); // 업무구분
		// scbkSendData.putBody("YIBRNO", AcctNum.substring(0, 3)); // 점 번호
		// scbkSendData.putBody("YIKMCD", AcctNum.substring(3, 5)); // 과목코드
		// scbkSendData.putBody("YIBUNHO", AcctNum.substring(5, 11)); // 계좌번호
		log.debug("*** CbIbk01H38500Req :: {}", h38500Req);

		String subjectCode = "";
		// try {
		// 전문 호출
		CbIbk01H38500Res h38500Res = this.hostClient.sendOltp(h385Option, h38500Req,
				CbIbk01H38500Res.class)
				.getResponse();
		log.debug("*** CbIbk01H38500Res :: {}", h38500Res);

		response = this.mapper.toDepositAndWithdrawal(h38500Res);
		subjectCode = h38500Res.getYOZONG();
		// } catch (OltpSystemException e) {
		// response.setErrorCode(e.getErrorCode());
		// response.setErrorMessage(e.getErrorMessage());
		// response.setErrorModule(e.getErrorModule());
		// }

		response.setPerBusNo(perBusNo);

		log.debug("*** response :: {}", response);

		String acctType = "0";
		String acctCode = pAcctNum.substring(3, 5);
		if ("10".equals(acctCode)
				|| "20".equals(acctCode)
				|| "30".equals(acctCode)) {
			acctType = "0";
		} else if ("26".equals(acctCode)
				|| "47".equals(acctCode)
				|| "80".equals(acctCode)
				|| "94".equals(acctCode)
				|| "95".equals(acctCode)) {

			acctType = "1";
		} else if ("46".equals(acctCode)
				|| "48".equals(acctCode)
				|| "49".equals(acctCode)
				|| "60".equals(acctCode)
				|| "70".equals(acctCode)
				|| "90".equals(acctCode)) {

			acctType = "2";
		}

		String homepageUrl = PropertiesUtils.getString("HOMEPAGE_URL"); // HOMEPAGE_URL
		response.setHomepageUrl(homepageUrl);
		response.setPdfUrl("/hp/file/ap/pd/");
		response.setAcctNum(pAcctNum);
		response.setAcctType(acctType);
		// response.setYoSGJAN(FormatUtils.getFrmFore2(h38500Res.getYOSGJAN(), 2));

		// 기본 계약서류 조회
		// String subjectCode = h38500Res.getYOZONG();
		if ("".equals(subjectCode)) {
			subjectCode = " ";
		}

		ContractDocumentParameter inParameter = ContractDocumentParameter.builder()
				.mappingCd1(acctCode)
				.mappingCd2(subjectCode)
				.mappingCd3("")
				.build();

		ContractDocumentResult dbDocResult = prdctDisclosureMgtDao.selectContractDocument(inParameter);

		log.debug("dbDocResult DB 조회 결과 :: {}", dbDocResult);
		if (dbDocResult != null) {

			response.setCmmAgreeFileNm(dbDocResult.getCmmAgreeFileNm()); // 공통약관파일명
			response.setCmmAgreeNm(dbDocResult.getCmmAgreeNm()); // 공통약관명
			response.setCmmAgreeCntrctOfferCode(dbDocResult.getCmmAgreeCntrctOfferCode()); // 공통약관아이디계약서류제공여부
			response.setCtgryCmmAgreeFileNm(dbDocResult.getCtgryCmmAgreeFileNm()); // 카테고리공통약관파일명
			response.setCtgryCmmAgreeNm(dbDocResult.getCtgryCmmAgreeNm()); // 카테고리공통약관명
			response.setCtgryCmmAgreeCntrctOfferCode(dbDocResult.getCtgryCmmAgreeCntrctOfferCode()); // 카테고리공통약관아이디계약서류제공여부
			response.setPrdctAgreeFileNm(dbDocResult.getPrdctAgreeFileNm()); // 상품약관명파일명
			response.setPrdctAgreeNm(dbDocResult.getPrdctAgreeNm()); // 상품약관명
			response.setPrdctAgreeCntrctOfferCode(dbDocResult.getPrdctAgreeCntrctOfferCode()); // 상품약관아이디계약서류제공여부
			response.setPrdctExplnFileNm(dbDocResult.getPrdctExplnFileNm()); // 상품설명서파일명
			response.setPrdctExplnNm(dbDocResult.getPrdctExplnNm()); // 상품설명서명
			response.setPrdctExplnCntrctOfferCode(dbDocResult.getPrdctExplnCntrctOfferCode()); // 상품설명서계약서류제공여부

			List<AddCntrctDocumentResult> addCntrctDocumentList = prdctDocListDao
					.selectAppendCommonDocumentList(dbDocResult.getPrdctId()); // 추가 계약서류 조회
			log.debug("*** addCntrctDocumentList :: {}", addCntrctDocumentList);
			response.setDepsitDocList(this.mapper.toCntrctDocumentList(addCntrctDocumentList));

		}

		// } catch (Exception e) {

		// }

		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/listTrustAccount", name = "계약서류제공 신탁 계약서조회")
	public SupDocListTrustAccountResponse listTrustAccount(IServiceContext serviceContext,
			SupDocListTrustAccountRequest request) {
		// ASIS :: MA3CSTDOC002_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		// String pAcctNum = StringUtils.nvl(request.getAcctNum(), "0000000000000000");
		// String yiURL = StringUtils.nvl(request.getYiURL(), "");

		String pAcctNum = StringUtils.defaultIfEmpty(request.getAcctNum(), "0000000000000000");
		String yiURL = StringUtils.defaultIfEmpty(request.getYiURL(), "");

		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		log.info(" perBusNo : {} ", perBusNo);

		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		log.info(" UserID : {} ", userID);
		// 응답 객체
		SupDocListTrustAccountResponse response = new SupDocListTrustAccountResponse();

		// try {

		// 공통부 세팅
		// TI1IBK01_H383 (CB_IBK01_H383)
		OltpRequestOptions h383Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H383");

		h383Option.setImsTranCd("TI1IBK01");
		h383Option.setInClassCd("H383");
		h383Option.setSvcCd("383");
		h383Option.setCaptureSystem("OLTP");
		// 개별부 세팅
		CbIbk01H38300Req h38300Req = new CbIbk01H38300Req();
		h38300Req.setUserID(userID); // 이용자번호
		// h38300Req.setTSPassword(UserID); // 통신비밀번호
		h38300Req.setYIGRGB("2"); // 거래구분조회
		if ("".equals(yiURL)) {
			// scbkSendData.putBody("YIBUNHO",pAcctNum);
			// scbkSendData.putBody("YIURL","");
			h38300Req.setYIBUNHO(pAcctNum); // 계좌번호
			h38300Req.setYIURL(""); // URL정보
		} else {
			// scbkSendData.putBody("YIBUNHO","");
			// scbkSendData.putBody("YIURL",YIURL);
			h38300Req.setYIBUNHO(""); // 계좌번호
			h38300Req.setYIURL(yiURL); // URL정보
		}

		// try {
		CbIbk01H38300Res h38300Res = this.hostClient.sendOltp(h383Option, h38300Req,
				CbIbk01H38300Res.class)
				.getResponse();

		log.debug("*** CbIbk01H38300Res :: {}", h38300Res.toString());

		response = this.mapper.toTrustAccount(h38300Res);
		log.debug("*** response :: {}", response);
		log.debug("*** h38300Res.getYOPRID() :: {}", h38300Res.getYOPRID());

		String trustCode = StringUtils.nvl(h38300Res.getYOPRID(), "");

		if (!"".equals(trustCode)) { // MMF 신탁인 경우 신탁코드가 없기 때문에 개별약관 조회 안함
			log.debug("*** trustCode :: {}", trustCode);
			TrustContractDocumentResult dbResult = spcfcMoneyTrustIsaPrdctDao
					.selectTrustContractDocument(trustCode);
			log.debug("dbResult DB 조회 결과 :: {}", dbResult);
			if (dbResult != null) {

				response.setPrdctExplnDoc1(dbResult.getPrdctExplnDoc1()); // 운용자산별 상품설명서(KYIR)
			} else {

				response.setPrdctExplnDoc1(""); // 운용자산별 상품설명서(KYIR)
			}
		}

		// } catch (OltpSystemException e) {
		// response.setErrorCode(e.getErrorCode());
		// response.setErrorMessage(e.getErrorMessage());
		// response.setErrorModule(e.getErrorModule());
		// }

		// paramSelectCommon.put("PUB_NTC_PRDCT_TYPE_CD", "H002"); // H002 : 신탁
		String pubNtcPrdctTypeCd = "H002"; // StringUtils.nvl(h38300Res.getYOPRID(), "");
		// CommonAgreeParameter inParameter = CommonAgreeParameter.builder()
		// .pubNtcPrdctTypeCd(pubNtcPrdctTypeCd) // H002 : 신탁
		// .build();

		CommonAgreeResult dbAgrResult = prdctDisclosureMgtDao.selectCommonAgree(pubNtcPrdctTypeCd);

		log.debug("dbAgrResult DB 조회 결과 :: {}", dbAgrResult);
		if (dbAgrResult != null) {

			response.setCmmAgreeFileNm(dbAgrResult.getCmmAgreeFileNm()); // 공통약관파일명
			response.setCmmAgreeNm(dbAgrResult.getCmmAgreeNm()); // 공통약관명
			response.setCmmAgreeCntrctOfferCode(dbAgrResult.getCmmAgreeCntrctOfferCode()); // 공통약관아이디계약서류제공여부
			response.setCtgryCmmAgreeFileNm(dbAgrResult.getCtgryCmmAgreeFileNm()); // 카테고리공통약관파일명
			response.setCtgryCmmAgreeNm(dbAgrResult.getCtgryCmmAgreeNm()); // 카테고리공통약관명
			response.setCtgryCmmAgreeCntrctOfferCode(dbAgrResult.getCtgryCmmAgreeCntrctOfferCode()); // 카테고리공통약관아이디계약서류제공여부
			response.setPrdctAgreeFileNm(dbAgrResult.getPrdctAgreeFileNm()); // 상품약관명파일명
			response.setPrdctAgreeNm(dbAgrResult.getPrdctAgreeNm()); // 상품약관명
			response.setPrdctAgreeCntrctOfferCode(dbAgrResult.getPrdctAgreeCntrctOfferCode()); // 상품약관아이디계약서류제공여부
			response.setPrdctExplnFileNm(dbAgrResult.getPrdctExplnFileNm()); // 상품설명서파일명
			response.setPrdctExplnNm(dbAgrResult.getPrdctExplnNm()); // 상품설명서명
			response.setPrdctExplnCntrctOfferCode(dbAgrResult.getPrdctExplnCntrctOfferCode()); // 상품설명서계약서류제공여부

			List<AddCntrctDocumentResult> addCntrctDocumentList = prdctDocListDao
					.selectTrustCommonDocumentList(pubNtcPrdctTypeCd); // 신탁 펀드 공통 추가 계약서류 조회

			response.setTrustCommDocList(this.mapper.toCntrctDocumentList(addCntrctDocumentList));

		}

		response.setPerBusNo(perBusNo);

		log.debug("*** response :: {}", response);

		String homepageUrl = PropertiesUtils.getString("HOMEPAGE_URL"); // HOMEPAGE_URL
		response.setHomepageUrl(homepageUrl);
		response.setPdfUrl("/hp/file/ap/pd/");
		response.setPdfUrlKyir("/hp/file/ab/ac/");
		response.setAcctNum(pAcctNum);

		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getTrustAccountDetail", name = "신탁 계약서 상세조회")
	public SupDocGetTrustAccountDetailResponse getTrustAccountDetail(IServiceContext serviceContext,
			SupDocGetTrustAccountDetailRequest request) {
		// ASIS :: MA3CSTDOC002_102S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 응답 객체
		SupDocGetTrustAccountDetailResponse response = new SupDocGetTrustAccountDetailResponse();

		// try {

		String yoJAJUM = StringUtils.defaultIfEmpty(request.getYoJAJUM(), "").trim();
		String yoJAKM = StringUtils.defaultIfEmpty(request.getYoJAKM(), "").trim();
		String yoJABUN = StringUtils.defaultIfEmpty(request.getYoJABUN(), "").trim();

		int yoJAJUM1 = Integer.parseInt(yoJAJUM);
		int yoJAKM1 = Integer.parseInt(yoJAKM);
		int yoJABUN1 = Integer.parseInt(yoJABUN);
		String jAcctNum = "";

		String yoOPEIL = StringUtils.defaultIfEmpty(request.getYoOPEIL(), "").replaceAll("-", ""); // 신규계좌일
		String chkcRisk = "";

		if (yoJAJUM1 != 0 && yoJAKM1 != 0 && yoJABUN1 != 0) { // 신탁의 경우 연결계좌번호가 없는 경우 빈값으로 내려줌
			jAcctNum = yoJAJUM + "-" + yoJAKM + "-" + yoJABUN; // 연결계좌번호
		}

		response.setAcctNum(request.getAcctNum()); // 계좌번호
		response.setYoCNAME(request.getYoCNAME()); // 고객명
		response.setYoTOCD(request.getYoTOCD()); // 통화코드
		response.setYoSINAK(request.getYoSINAK()); // 계좌개설금액
		response.setYoPNAME(request.getYoPNAME()); // 상품명
		response.setYoOPEIL(request.getYoOPEIL()); // 계좌개설일자
		response.setYoGIGAN(request.getYoGIGAN()); // 계약기간
		response.setYoWBIL(request.getYoWBIL()); // 만기일자
		response.setYoPBOSU(request.getYoPBOSU()); // 선취보수
		response.setYoGBOSU(request.getYoGBOSU()); // 후취보수
		response.setYoJDSSR(request.getYoJDSSR()); // 중도해지수수료율
		response.setYoUNGB(request.getYoUNGB()); // 운용보고서 통보
		response.setYoSUGB(request.getYoSUGB()); // 계산서 통보
		response.setYoALGB(request.getYoALGB()); // 이벤트 통보
		response.setYoCRISK(request.getYoCRISK()); // 고객투자등급
		response.setYoPRISK(request.getYoPRISK()); // 상품위험등급
		response.setYoTONM(request.getYoTONM()); // 통화명
		response.setJacctNum(jAcctNum); // 연결계좌번호

		// [FSR]CIP등급변경에 따른 체크 로직 추가 - 계좌신규일이 2023.9.18 이전 P, 2023.9.18 이후면 A
		if (20230918 > Integer.parseInt(yoOPEIL)) {
			chkcRisk = "P";
		} else {
			chkcRisk = "A";
		}

		// 방문판매 여부 추가 - 20241021
		response.setYoBD2DC(request.getYoBD2DC()); // 방문판매여부 추가

		response.setChKCRISK(chkcRisk);

		if (!sessionManager.isLogin()) { // LMS 전송시 비로그인상태이므로 , 주민등록번호를 세팅해준다.
			// output.put("LMS_PerBusNo", input.getBody("LMS_PerBusNo"));

			response.setLmsPerBusNo(request.getLmsPerBusNo()); // 방문판매여부 추가
		}

		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/listFund", name = "계약서류제공 펀드 계약서조회")
	public SupDocListFundResponse listFund(IServiceContext serviceContext,
			SupDocListFundRequest request) {
		// ASIS :: MA3CSTDOC003_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		// String pAcctNum = StringUtils.nvl(request.getAcctNum(), "0000000000000000");
		// String yiURL = StringUtils.nvl(request.getYiURL(), "");

		String pAcctNum = StringUtils.defaultIfEmpty(request.getAcctNum(), "0000000000000000");
		String yiURL = StringUtils.defaultIfEmpty(request.getYiURL(), "");

		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);

		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		log.info(" UserID : {} ", userID);
		log.info(" perBusNo : {} ", perBusNo);
		// 응답 객체
		SupDocListFundResponse response = new SupDocListFundResponse();

		// try {

		// 공통부 세팅
		// TI1IBK01_H383 (CB_IBK01_H383)
		OltpRequestOptions h383Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H383");

		h383Option.setImsTranCd("TI1IBK01");
		h383Option.setInClassCd("H383");
		h383Option.setSvcCd("383");
		h383Option.setCaptureSystem("OLTP");
		// 개별부 세팅
		CbIbk01H38300Req h38300Req = new CbIbk01H38300Req();
		h38300Req.setUserID(userID); // 이용자번호
		// h38300Req.setTSPassword(UserID); // 통신비밀번호
		h38300Req.setYIGRGB("2"); // 거래구분조회
		if ("".equals(yiURL)) {
			// scbkSendData.putBody("YIBUNHO",pAcctNum);
			// scbkSendData.putBody("YIURL","");
			h38300Req.setYIBUNHO(pAcctNum); // 계좌번호
			h38300Req.setYIURL(""); // URL정보
		} else {
			// scbkSendData.putBody("YIBUNHO","");
			// scbkSendData.putBody("YIURL",YIURL);
			h38300Req.setYIBUNHO(""); // 계좌번호
			h38300Req.setYIURL(yiURL); // URL정보
		}

		String fundCode = "";
		String fundPrdType = "";

		boolean edMSQueryYN = false;

		// try {
		CbIbk01H38300Res h38300Res = this.hostClient.sendOltp(h383Option, h38300Req,
				CbIbk01H38300Res.class)
				.getResponse();

		log.debug("*** CbIbk01H38300Res :: {}", h38300Res.toString());

		response = this.mapper.toFund(h38300Res);
		log.debug("*** response :: {}", response);
		// String fundCode = StringUtils.nvl(h38300Res.getYOPRID(), "");
		// String fundPrdType = StringUtils.nvl(h38300Res.getYOFDSPYH(), "");
		fundCode = StringUtils.defaultIfEmpty(h38300Res.getYOPRID(), "");
		fundPrdType = StringUtils.defaultIfEmpty(h38300Res.getYOFDSPYH(), "");

		log.debug("*** h38300Res.fundCode  :: {}", fundCode);
		log.debug("*** h38300Res.fundPrdType  :: {}", fundPrdType);

		edMSQueryYN = h38300Res.getYOJHYB().equalsIgnoreCase("Y");

		// } catch (OltpSystemException e) {
		// response.setErrorCode(e.getErrorCode());
		// response.setErrorMessage(e.getErrorMessage());
		// response.setErrorModule(e.getErrorModule());
		// }
		// paramSelect.put("FUND_CD",fundCode); //펀드코드로 개별약관 조회

		List<String> fundPrvSnList = new ArrayList<String>();
		fundPrvSnList.add(fundCode);

		String prvsnMk2Filenm = ""; // 투자설명서
		String prvsnMk3Filenm = ""; // 간이투자설명서
		String prvsnMk5Filenm = ""; // 사모집합투자증권 핵심요약상품설명서
		String prvsnMk6Filenm = ""; // 핵심상품설명서

		// 2026.03.27
		// 초기값을 셋팅
		response.setEdMSQueryFlag("N");
		// if(StringUtils.isNotEmpty(fundPrdType) && "11".equals(fundPrdType)) {
		if ("11".equals(fundPrdType)) {
			// 사모펀드는 간이투자설명서노출X, 핵심요약상품설명서(ATF018), 핵심상품설명서(ATF17) 별도조회
			log.debug("#########_MA3CSTDOC003_101S 사모펀드 투자설명서 조회 START");

			// List<Map<String, Object>> RESULT_MA30_FNANCL_PRDCT_CMMN_S_03 =
			// this.kfbdbDbClient
			// .selectList("MA3_PRD_FND.MA30_FNANCL_PRDCT_CMMN_S_03", paramSelect);

			List<ProductFileResult> dbFileList = ma30FnanclPrdctCmmnDao.selectFundDocumentFile(fundPrvSnList);

			if (null != dbFileList && dbFileList.size() > 0) {
				ProductFileResult fileResult = dbFileList.get(0);
				// Map subMap = RESULT_MA30_FNANCL_PRDCT_CMMN_S_03.get(0);
				// PRVSN_MK6_FILENM = subMap.get("PRVSN_MK6_FILENM") != null ? (String)
				// subMap.get("PRVSN_MK6_FILENM")
				// : ""; // 핵심상품설명서
				// PRVSN_MK5_FILENM = subMap.get("PRVSN_MK5_FILENM") != null ? (String)
				// subMap.get("PRVSN_MK5_FILENM")
				// : ""; // 사모집합투자증권 핵심요약상품설명서
				prvsnMk6Filenm = fileResult.getPrvsnMk6Filenm() != null ? fileResult.getPrvsnMk6Filenm() : "";// 핵심상품설명서
				prvsnMk5Filenm = fileResult.getPrvsnMk5Filenm() != null ? fileResult.getPrvsnMk5Filenm() : "";// 사모집합투자증권
																												// 핵심요약상품설명서
			}
			log.debug("#########_MA3CSTDOC003_101S 사모펀드 PRVSN_MK6_FILENM ::" + prvsnMk6Filenm + "::");
			log.debug("#########_MA3CSTDOC003_101S 사모펀드 PRVSN_MK5_FILENM ::" + prvsnMk5Filenm + "::");

			// output.put("PRVSN_MK6_FILENM", PRVSN_MK6_FILENM); // 핵심상품설명서
			// output.put("PRVSN_MK5_FILENM", PRVSN_MK5_FILENM); // 사모집합투자증권 핵심요약상품설명서
			response.setPrvsnMk6Filenm(prvsnMk6Filenm); // 핵심상품설명서
			response.setPrvsnMk5Filenm(prvsnMk5Filenm); // 사모집합투자증권 핵심요약상품설명서
		} else {
			// 사모펀드이외 개별 약관 조회
			// List<Map<String, Object>> RESULT_MA30_FNANCL_PRDCT_CMMN_S_01 =
			// this.kfbdbDbClient
			// .selectList("MA3_PRD_FND.MA30_FNANCL_PRDCT_CMMN_S_01", paramSelect);

			List<ProductFileResult> dbFileList = ma30FnanclPrdctCmmnDao.selectFundCodeFile(fundPrvSnList);

			// if (RESULT_MA30_FNANCL_PRDCT_CMMN_S_01 != null &&
			// RESULT_MA30_FNANCL_PRDCT_CMMN_S_01.size() > 0) {
			if (null != dbFileList && dbFileList.size() > 0) {
				log.debug("dbFileList : " + dbFileList.get(0));
				// Map subMap = RESULT_MA30_FNANCL_PRDCT_CMMN_S_01.get(0);
				ProductFileResult fileExpResult = dbFileList.get(0);
				// PRVSN_MK2_FILENM = (String) subMap.get("PRVSN_MK2_FILENM"); // 투자설명서
				// PRVSN_MK3_FILENM = (String) subMap.get("PRVSN_MK3_FILENM"); // 간이투자설명서
				prvsnMk2Filenm = fileExpResult.getPrvsnMk2Filenm();// 투자설명서
				prvsnMk3Filenm = fileExpResult.getPrvsnMk3Filenm();// 간이투자설명서
				log.debug("###PSH MA3CSTDOC003_101S PRVSN_MK2_FILENM 파일명일치 : " + prvsnMk2Filenm);
				log.debug("###PSH MA3CSTDOC003_101S PRVSN_MK3_FILENM 파일명일치 : " + prvsnMk3Filenm);
			}

			log.debug("###PSH MA3CSTDOC003_101S PRVSN_MK2_FILENM  : " + prvsnMk2Filenm);
			log.debug("###PSH MA3CSTDOC003_101S PRVSN_MK3_FILENM  : " + prvsnMk3Filenm);

			// output.put("INVEST_EXPLN_DOC", PRVSN_MK2_FILENM); // 투자설명서
			// output.put("TMP_INVEST_EXPLN_DOC", PRVSN_MK3_FILENM); // 간이투자설명서
			response.setInvestExplnDoc(prvsnMk2Filenm); // 핵심상품설명서
			response.setTmpInvestExplnDoc(prvsnMk3Filenm); // 사모집합투자증권 핵심요약상품설명서
		}

		int fundStatus = Integer.parseInt(fundCode.substring(3, 4));

		log.info("fundStatus ::::::::" + fundStatus);

		// paramSelectCommon.put("PUB_NTC_PRDCT_TYPE_CD", "H002"); // H002 : 신탁
		String pubNtcPrdctTypeCd = ""; // StringUtils.nvl(h38300Res.getYOPRID(), "");
		// CommonAgreeParameter inParameter = CommonAgreeParameter.builder()
		// .pubNtcPrdctTypeCd(pubNtcPrdctTypeCd) // H002 : 신탁
		// .build();

		if (fundStatus >= 1 && fundStatus <= 6) { // 펀드코드 10자리 중 네번째 자리가 1~6에 해당하는 펀드 (역내펀드)
			// output.put("fundStatusKRW", "Y"); // 역내일 경우 해당 플래그를 Y로 세팅
			// paramSelectCommon.put("PUB_NTC_PRDCT_TYPE_CD", "G002"); // G002 : 역내펀드 G003 :
			// 역외펀드

			response.setFundStatusKRW("Y"); // 역내일 경우 해당 플래그를 Y로 세팅
			pubNtcPrdctTypeCd = "G002"; // G002 : 역내펀드 G003 : 역외펀드

		} else if (fundStatus >= 7 && fundStatus <= 9) { // 펀드코드 10자리 중 네번째 자리가 7~9에 해당하는 펀드 (역외펀드)
			// output.put("fundStatusKRW", "N"); // 역외일 경우 해당 플래그를 N로 세팅
			// paramSelectCommon.put("PUB_NTC_PRDCT_TYPE_CD", "G003"); // G002 : 역내펀드 G003 :
			// 역외펀드

			response.setFundStatusKRW("N"); // 역외일 경우 해당 플래그를 N로 세팅
			pubNtcPrdctTypeCd = "G003"; // G002 : 역내펀드 G003 : 역외펀드
		}

		CommonAgreeResult dbAgrResult = prdctDisclosureMgtDao.selectCommonAgree(pubNtcPrdctTypeCd);

		log.debug("dbAgrResult DB 조회 결과 :: {}", dbAgrResult);
		if (dbAgrResult != null) {

			response.setCmmAgreeFileNm(dbAgrResult.getCmmAgreeFileNm()); // 공통약관파일명
			response.setCmmAgreeNm(dbAgrResult.getCmmAgreeNm()); // 공통약관명
			response.setCmmAgreeCntrctOfferCode(dbAgrResult.getCmmAgreeCntrctOfferCode()); // 공통약관아이디계약서류제공여부
			response.setCtgryCmmAgreeFileNm(dbAgrResult.getCtgryCmmAgreeFileNm()); // 카테고리공통약관파일명
			response.setCtgryCmmAgreeNm(dbAgrResult.getCtgryCmmAgreeNm()); // 카테고리공통약관명
			response.setCtgryCmmAgreeCntrctOfferCode(dbAgrResult.getCtgryCmmAgreeCntrctOfferCode()); // 카테고리공통약관아이디계약서류제공여부
			response.setPrdctAgreeFileNm(dbAgrResult.getPrdctAgreeFileNm()); // 상품약관명파일명
			response.setPrdctAgreeNm(dbAgrResult.getPrdctAgreeNm()); // 상품약관명
			response.setPrdctAgreeCntrctOfferCode(dbAgrResult.getPrdctAgreeCntrctOfferCode()); // 상품약관아이디계약서류제공여부
			response.setPrdctExplnFileNm(dbAgrResult.getPrdctExplnFileNm()); // 상품설명서파일명
			response.setPrdctExplnNm(dbAgrResult.getPrdctExplnNm()); // 상품설명서명
			response.setPrdctExplnCntrctOfferCode(dbAgrResult.getPrdctExplnCntrctOfferCode()); // 상품설명서계약서류제공여부

			List<AddCntrctDocumentResult> addCntrctDocumentList = prdctDocListDao
					.selectTrustCommonDocumentList(pubNtcPrdctTypeCd); // 신탁 펀드 공통 추가 계약서류 조회

			response.setFundCommDocList(this.mapper.toCntrctDocumentList(addCntrctDocumentList));
			// List<Map<String, Object>> trustCommDocList =
			// this.kfbdbDbClient.selectList("MA3_CST_DOC.MA30_PMS_PRVSN_FUND_MGT_S_02",
			// paramSelectCommon);
			// output.put("trustCommDocList", trustCommDocList);
		}

		/* PMS2.0 공통 약관 조회 */
		// HashMap<String, Object> trustCommDoc = this.kfbdbDbClient
		// .selectOne("MA3_CST_DOC.MA30_PMS_PRVSN_FUND_MGT_S_01", paramSelectCommon);

		// if (sessionManager.isLogin()) {
		// sessionManager.setLoginValue("TelCode", h006Res.getTelCode());
		// sessionManager.setLoginValue("SafeCardState", "1");
		// sessionManager.setLoginValue("SafeCardSeq1", h006Res.getSafeCardIndex1());
		// sessionManager.setLoginValue("SafeCardSeq2", h006Res.getSafeCardIndex2());
		// sessionManager.setLoginValue("SafeCardSeq3", h006Res.getSafeCardIndex3());
		// sessionManager.setLoginValue("SafeCardIndex1", h006Res.getSafeCardIndex1());
		// sessionManager.setLoginValue("SafeCardIndex2", h006Res.getSafeCardIndex2());
		// sessionManager.setLoginValue("SafeCardIndex3", h006Res.getSafeCardIndex3());
		// sessionManager.setLoginValue("TSPassword", h006Res.getTSPassword());
		// sessionManager.setLoginValue("InforGubun", h006Res.getInforGubun());
		// }

		String homepageUrl = PropertiesUtils.getString("HOMEPAGE_URL"); //
		String ma30Url = StringUtils.defaultIfEmpty(PropertiesUtils.getString("MA30_URL"), "https://m.sc.co.kr");

		// HOMEPAGE_URL
		response.setHomepageUrl(homepageUrl);
		response.setMa30Url(ma30Url);
		response.setMa30PdfUrl("/mpms_RESOURCE/hp/file/ap/pf/");
		response.setPdfUrl("/hp/file/ap/pd/"); // 공통약관일 경우 pd 경로
		response.setPms20Path2("/hp/file/ap/pf/"); // 개별약관일 경우 pf 경로

		response.setAcctNum(pAcctNum);

		response.setPerBusNo(perBusNo);

		log.debug("response 1st :: {}", response);

		// boolean edMSQueryYN = h38300Res.getYOJHYB().equalsIgnoreCase("Y");
		log.debug("response 2nd :: {}", response);
		log.debug("edMSQueryYN :: {}", edMSQueryYN);

		if (edMSQueryYN) {
			log.debug("response 3rd :: {}", response);

			MciIbEdms001Res mciRes = supportCustomerCenter.listFundRequest(pAcctNum);
			log.debug("mciRes :: {}", response);
			if (null != mciRes) {

				SupDocListFundRequestResponse resSupDocList = this.mapper.toFundRequest(mciRes);
				log.debug("*** resSupDocList :: {}", resSupDocList);

				List<SupDocListFundRequestResponse.DOCU> documentList = resSupDocList.getDocuList();

				log.debug("*** documentList bf :: {}", documentList);
				if (documentList != null) {
					documentList.remove("custSsn");
					log.debug("*** documentList af :: {}", documentList);
					// for (SupDocListFundRequestResponse.DOCU resDocu : documentList) {
					// resDocu.
					// }

					JSONArray arrayList = new JSONArray(documentList);
					log.debug("*** arrayList af :: {}", arrayList);
					// ObjectMapper objectMapper = new ObjectMapper();
					// Object objJson = objectMapper.readValues(arrayList);
					JSONObject requestparam = supportCustomerCenter.hasEdmsPdfFile(arrayList.toString());
					log.debug("*** requestparam af :: {}", requestparam);
					JSONArray documentCheckList = (JSONArray) requestparam.get("edmsXvarmDocList");
					log.debug("*** documentCheckList af :: {}", documentCheckList);
					for (int i = 0; i < documentCheckList.length(); i++) {
						JSONObject subMap = (JSONObject) documentCheckList.get(i);
						try {
							String docName = URLDecoder.decode(String.valueOf(subMap.get("DOC_NAME")), "UTF-8");
							subMap.put("DOC_NAME", docName);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					log.debug("*** documentCheckList for af :: {}", documentCheckList);
					// response.setEdMSContractList(documentCheckList);

					response.setEdMSQueryFlag("Y");
				}
			}
		}
		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }

		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/listFundRequest", name = "여신, 펀드 계약서류제공 신청내역 조회")
	public SupDocListFundRequestResponse listFundRequest(IServiceContext serviceContext,
			SupDocListFundRequestRequest request) {
		// ASIS :: MA3CSTDOC004_103S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		String pAcctNum = StringUtils.nvl(request.getAcctNum(), "");

		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		log.info(" UserID : {} ", userID);
		log.info(" perBusNo : {} ", perBusNo);
		// 응답 객체
		SupDocListFundRequestResponse response = new SupDocListFundRequestResponse();

		// List<MciIbEdms001Res.DOCU> docuList = new ArrayList<>();
		// try {

		// SCBKActionData output = this.transfer(ctx, "MCI_IB_EDMS_001", sendData);
		// 공통부 세팅
		// MciRequestOptions mciOption =
		// this.hostClient.getMciRequestOptions("MCI_IB_EDMS_001");
		// mciOption.setTranCd("IB_EDMS_001"); // AS-IS BUSINESS_FUNCTION_ID
		// mciOption.setBlngBrNo("0019"); // AS-IS AGENTBRANCH
		// mciOption.setTxnBrNo("0019"); // AS-IS HOMEBRANCH

		// // 개별부 세팅
		// MciIbEdms001Req mciReq = MciIbEdms001Req.builder()
		// .ACCT_NO(pAcctNum)
		// .build();
		// log.debug("*** mciReq :: {}", mciReq);

		// MciIbEdms001Res mciRes = this.hostClient.sendMci(mciOption, mciReq,
		// MciIbEdms001Res.class).getResponse();
		// log.debug("*** mciRes :: {}", mciRes);
		MciIbEdms001Res mciRes = supportCustomerCenter.listFundRequest(pAcctNum);
		// log.debug("*** mciRes :: {}", mciRes);

		if (null != mciRes) {

			response = this.mapper.toFundRequest(mciRes);
			log.debug("*** response :: {}", response);

			// for (MciIbEdms001Res.DOCU rowData : docuList) {
			// DocumentResult doc = new DocumentResult();

			// // doc.setElementId(rowData.getELEMENT_ID());
			// // doc.setTranDt(rowData.getTRAN_DT());
			// // doc.setLoanReqNo(rowData.getLOAN_REQ_NO()); // 공통약관아이디계약서류제공여부
			// // doc.setAcctNo(rowData.getACCT_NO()); // 공통약관파일명
			// // doc.setCustSsn(rowData.getCUST_SSN()); // 공통약관명
			// // doc.setDocCode(rowData.getDOC_CODE()); // 공통약관아이디계약서류제공여부
			// // doc.setDocName(rowData.getDOC_NAME()); // 공통약관파일명
			// // doc.setBrchNo(rowData.getBRCH_NO()); // 공통약관명
			// // doc.setEmpNo(rowData.getEMP_NO()); // 공통약관아이디계약서류제공여부
			// // doc.setChnnlMk(rowData.getCHNNL_MK()); // 공통약관파일명
			// // doc.setJobMk(rowData.getJOB_MK()); // 공통약관명
			// // doc.setCreateDocDt(rowData.getCREATE_DOC_DT()); // 공통약관아이디계약서류제공여부

			// documentResultList.add(doc);
			// }
			// log.debug("*** documentResultList :: {}", documentResultList);

			// response.setDocuList( );
		}

		response.setPerBusNo(perBusNo);

		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }
		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/hasEdmsPdfFile", name = "EDMS 계약서류조회 PDF 파일이 존재하는지 체크")
	public SupDocHasEdmsPdfFileResponse hasEdmsPdfFile(IServiceContext serviceContext,
			SupDocHasEdmsPdfFileRequest request) {
		// ASIS :: MA3CSTDOC004_106S

		// String edmsXvarmDocMeta =
		// StringUtils.defaultIfEmpty(input.getString("edmsXvarmDocMeta"), "[{}]");
		String edmsXvarmDocMeta = StringUtils.nvl(request.getEdmsXvarmDocMeta().trim(), "[{}]");
		// 응답 객체
		SupDocHasEdmsPdfFileResponse response = new SupDocHasEdmsPdfFileResponse();

		// try {

		JSONObject requestparam = supportCustomerCenter.hasEdmsPdfFile(edmsXvarmDocMeta);
		// JSONObject result = edocAPConnectService.requestEdmsXvarmDocCheck(input);

		// output.setBody("edmsXvarmDocList", result.get("edmsXvarmDocList"));
		// response.setEdmsXvarmDocList(result);
		response.setEdmsXvarmDocList(String.valueOf(requestparam.get("edmsXvarmDocList")));

		// } catch (Exception e) {

		// }

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getFundDetail", name = "펀드 계약서 상세조회")
	public SupDocGetFundDetailResponse getFundDetail(IServiceContext serviceContext,
			SupDocGetFundDetailRequest request) {
		// ASIS :: MA3CSTDOC003_102S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 응답 객체
		SupDocGetFundDetailResponse response = new SupDocGetFundDetailResponse();

		// try {

		String yoJAJUM = StringUtils.nvl(request.getYoJAJUM().trim(), "");
		String yoJAKM = StringUtils.nvl(request.getYoJAKM().trim(), "");
		String yoJABUN = StringUtils.nvl(request.getYoJABUN().trim(), "");

		int yoJAJUM1 = Integer.parseInt(yoJAJUM);
		int yoJAKM1 = Integer.parseInt(yoJAJUM);
		int yoJABUN1 = Integer.parseInt(yoJABUN);
		String jAcctNum = "";

		String yoOPEIL = StringUtils.nvl(request.getYoOPEIL().replaceAll("-", ""), ""); // 신규계좌일
		String chkcRisk = "";

		if (yoJAJUM1 != 0 && yoJAKM1 != 0 && yoJABUN1 != 0) {
			jAcctNum = yoJAJUM + "-" + yoJAKM + "-" + yoJABUN; // 연결계좌번호
		}

		response.setAcctNum(request.getAcctNum()); // 계좌번호
		response.setYoCNAME(request.getYoCNAME()); // 고객명
		response.setYoTOCD(request.getYoTOCD()); // 통화코드
		response.setYoSINAK(request.getYoSINAK()); // 계좌개설금액
		response.setYoPNAME(request.getYoPNAME()); // 상품명
		response.setYoOPEIL(request.getYoOPEIL()); // 계좌개설일자
		// response.setYoGIGAN(request.getYoGIGAN()); // 계약기간
		response.setYoWBIL(request.getYoWBIL()); // 만기일자
		response.setYoPBOSU(request.getYoPBOSU()); // 선취보수
		response.setYoGBOSU(request.getYoGBOSU()); // 후취보수
		response.setYoJDSSR(request.getYoJDSSR()); // 중도해지수수료율
		response.setYoUNGB(request.getYoUNGB()); // 운용보고서 통보
		response.setYoSUGB(request.getYoSUGB()); // 계산서 통보
		response.setYoALGB(request.getYoALGB()); // 이벤트 통보
		response.setYoCRISK(request.getYoCRISK()); // 고객투자등급
		response.setYoPRISK(request.getYoPRISK()); // 상품위험등급
		response.setYoTONM(request.getYoTONM()); // 통화명
		response.setYoZONG(request.getYoZONG()); // 종별
		response.setYoGRGB(request.getYoGRGB()); // 거래구분
		response.setYoHANDO(request.getYoHANDO()); // 세제한도금액
		response.setYoYSPCD(request.getYoYSPCD()); // 세제유형
		response.setYoGRBB(request.getYoGRBB()); // 거래방법
		response.setYoTOGB(request.getYoTOGB()); // 계약서류통보방법
		response.setJacctNum(jAcctNum); // 연결계좌번호

		// MS SP15 자동이체추가
		response.setYoWJAIL(request.getYoWJAIL()); // 자동이체일
		response.setYoWJAAMT(request.getYoWJAAMT()); // 자동이체금액
		response.setYoWSTIL(request.getYoWSTIL()); // 자동이체시작일
		response.setYoWLTIL(request.getYoWLTIL()); // 자동이체종료일

		// [FSR]CIP등급변경에 따른 체크 로직 추가 - 계좌신규일이 2023.9.18 이전 P, 2023.9.18 이후면 A
		if (20230918 > Integer.parseInt(yoOPEIL)) {
			chkcRisk = "P";
		} else {
			chkcRisk = "A";
		}

		// 방문판매 여부 추가 - 20241021
		response.setYoBD2DC(request.getYoBD2DC()); // 방문판매여부 추가

		response.setChKCRISK(chkcRisk);

		if (!sessionManager.isLogin()) { // LMS 전송시 비로그인상태이므로 , 주민등록번호를 세팅해준다.
			// output.put("LMS_PerBusNo", input.getBody("LMS_PerBusNo"));

			response.setLmsPerBusNo(request.getLmsPerBusNo()); // 방문판매여부 추가
		}

		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/listApply", name = "계약서류제공 신청내역 조회")
	public SupDocListApplyResponse listApply(IServiceContext serviceContext,
			SupDocListApplyRequest request) {

		// ASIS :: MA3CSTDOC004_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 공통부 세팅
		// 대출 계약서류조회 TI1IBK01 H381 계약서류조회(여신)
		OltpRequestOptions h381Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H381");
		h381Option.setImsTranCd("TI1IBK01");
		h381Option.setInClassCd("H381");
		h381Option.setSvcCd("381");
		h381Option.setCaptureSystem("OLTP");

		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		String acctNum = StringUtils.defaultIfEmpty(request.getAcctNum(), "");
		String kwamok = acctNum.substring(3, 5);
		String acctName = ""; // 계좌명
		String assort = StringUtils.defaultIfEmpty(request.getAssort(), "");

		// 개별부 세팅
		CbIbk01H38100Req h38100Req = new CbIbk01H38100Req();
		if (!sessionManager.isLogin()) {
			h38100Req.setUserID("FIRST999"); // 이용자번호
			h38100Req.setTSPassword("99999999");
		}

		h38100Req.setYIBUNHO(acctNum); // 계좌번호
		h38100Req.setYIGRGBN(StringUtils.defaultIfEmpty(request.getYiGRGBN(), "1")); // 1 조회

		CbIbk01H38100Res h38100Res = this.hostClient.sendOltp(h381Option, h38100Req,
				CbIbk01H38100Res.class)
				.getResponse();
		SupDocListApplyResponse response = this.mapper.toApply(h38100Res);

		String yoGUBUN = h38100Res.getYOGUBUN(); // 상품구분 1-담보, 2-신용, 3-BB, 4-예펀담대 5-수담대, 6-카드론
		String yoGYECD = h38100Res.getYOGYECD(); // 계정코드
		String yoJAGUM = h38100Res.getYOJAGUM(); // 대출종류
		String yoDAEGB = h38100Res.getYODAEGB(); // 대출구분

		// 신용여신 상품명 xml에서 가져옴
		if ("2".equals(yoGUBUN)) {
			if (!kwamok.startsWith("7")) {
				// 7로 시작하지 않은 경우에는 한도대출(마이너스대출) Set
				acctName = "한도대출(마이너스대출)";
			} else {
				acctName = CodeUtils.getCodeValue("OGYECDACCTNM", yoGYECD + "|" + yoJAGUM, "ko");
			}

			if ("".equals(acctName)) {
				acctName = h38100Res.getYOGYENM();
			}
		} else {
			acctName = h38100Res.getYOGYENM();
		}

		// 기본 계약서류 조회
		ContractDocumentParameter inParameter = ContractDocumentParameter.builder()
				.mappingCd1(kwamok) // 과목
				.mappingCd2(yoGYECD) // 계정코드
				.mappingCd3("002111".equals(yoGYECD) || "002015".equals(yoGYECD) ? yoDAEGB : yoJAGUM) // 대출구분
				.build();

		ContractDocumentResult dbDocResult = prdctDisclosureMgtDao.selectContractDocument(inParameter);
		List<AddCntrctDocument> resultList = null;

		if (dbDocResult != null) {
			response.setCmmAgreeFileNm(dbDocResult.getCmmAgreeFileNm()); // 공통약관파일명
			response.setCmmAgreeNm(dbDocResult.getCmmAgreeNm()); // 공통약관명
			response.setCmmAgreeCntrctOfferCode(dbDocResult.getCmmAgreeCntrctOfferCode()); // 공통약관아이디계약서류제공여부
			response.setCtgryCmmAgreeFileNm(dbDocResult.getCtgryCmmAgreeFileNm()); // 카테고리공통약관파일명
			response.setCtgryCmmAgreeNm(dbDocResult.getCtgryCmmAgreeNm()); // 카테고리공통약관명
			response.setCtgryCmmAgreeCntrctOfferCode(dbDocResult.getCtgryCmmAgreeCntrctOfferCode()); // 카테고리공통약관아이디계약서류제공여부
			response.setPrdctAgreeFileNm(dbDocResult.getPrdctAgreeFileNm()); // 상품약관명파일명
			response.setPrdctAgreeNm(dbDocResult.getPrdctAgreeNm()); // 상품약관명
			response.setPrdctAgreeCntrctOfferCode(dbDocResult.getPrdctAgreeCntrctOfferCode()); // 상품약관아이디계약서류제공여부
			response.setPrdctExplnFileNm(dbDocResult.getPrdctExplnFileNm()); // 상품설명서파일명
			response.setPrdctExplnNm(dbDocResult.getPrdctExplnNm()); // 상품설명서명
			response.setPrdctExplnCntrctOfferCode(dbDocResult.getPrdctExplnCntrctOfferCode()); // 상품설명서계약서류제공여부
			// response.setprdctnm(dbDocResult.getprdctnm()); // 상품명

			// 추가 계약서류 조회
			resultList = this.mapper
					.toCntrctDocumentList(prdctDocListDao.selectAppendCommonDocumentList(dbDocResult.getPrdctId()));
		}

		response.setPerBusNo(perBusNo);
		response.setHomepageUrl(PropertiesUtils.getString("HOMEPAGE_URL"));
		response.setPdfUrl("/hp/file/ap/pd/");

		response.setBalance(StringUtils.defaultIfEmpty(request.getBalance(), "")); // 대출잔액(전계좌 잔액으로 변경)
		response.setAcctName(acctName); // 대출계좌이름
		response.setAcctNum(acctNum); // 대출계좌번호
		response.setLoanRepayPrinAmt(StringUtils.defaultIfEmpty(request.getLoanRepayPrinAmt(), "")); // 승인한도
		response.setSavingStartDate(StringUtils.defaultIfEmpty(request.getSavingStartDate(), "")); // 신규일

		response.setLoanKind(yoJAGUM); // 대출종류
		response.setLoanAcctKmCD(yoGYECD); // 계정과목코드
		response.setAssort(assort); // 종별코드
		response.setKwamok(kwamok); // 과목코드

		response.setApplyDocList(resultList);
		response.setRequestURLCheck(StringUtils.defaultIfEmpty(request.getRequestURLCheck(), ""));

		boolean edMSQueryYN = h38100Res.getYOJHYN().equalsIgnoreCase("Y");
		response.setEdMSQueryFlag("N"); // 초기값으로 N 으로 셋팅

		if (edMSQueryYN) {

			MciIbEdms001Res mciRes = supportCustomerCenter.listFundRequest(acctNum);
			// if (null != mciRes) {
			SupDocListFundRequestResponse resSupDocList = this.mapper.toFundRequest(mciRes);
			List<SupDocListFundRequestResponse.DOCU> documentList = resSupDocList.getDocuList();
			log.debug("*** documentList bf :: {}", documentList);

			if (documentList != null) {
				documentList.remove("custSsn");
				log.debug("*** documentList af :: {}", documentList);

				JSONArray arrayList = new JSONArray(documentList);
				log.debug("*** arrayList af :: {}", arrayList);
				JSONObject requestparam = supportCustomerCenter.hasEdmsPdfFile(arrayList.toString());
				log.debug("*** requestparam af :: {}", requestparam);
				JSONArray documentCheckList = (JSONArray) requestparam.get("edmsXvarmDocList");
				log.debug("*** documentCheckList af :: {}", documentCheckList);

				for (int i = 0; i < documentCheckList.length(); i++) {
					JSONObject subMap = (JSONObject) documentCheckList.get(i);
					try {
						String docName = URLDecoder.decode(String.valueOf(subMap.get("DOC_NAME")), "UTF-8");
						subMap.put("DOC_NAME", docName);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.debug("*** documentCheckList for af :: {}", documentCheckList);
				response.setEdMSContractList(documentCheckList);
				response.setEdMSQueryFlag("Y");
			}
		}

		log.debug("*** response :: {}", response);
		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getLoanDetail", name = "여신 계약서 상세조회")
	public SupDocGetLoanDetailResponse getLoanDetail(IServiceContext serviceContext,
			SupDocGetLoanDetailRequest request) {
		// ASIS :: MA3CSTDOC004_102S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		String PerBusNo = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class),
				sessionManager.getGlobalValue("PerBusNo", String.class));
		// 응답 객체
		SupDocGetLoanDetailResponse response = new SupDocGetLoanDetailResponse();

		// try {
		String listCnt = StringUtils.nvl(request.getYoSUCNT().trim(), "0");
		int iYosucnt = Integer.parseInt(listCnt);
		log.debug("*** iYosucnt :: {}", iYosucnt);

		log.debug("*** request :: {}", request);
		log.debug("*** response before :: {}", response);
		BeanUtils.copyProperties(request, response);
		log.debug("*** response after :: {}", response);

		// BeanUtils.copyProperties(request, response) 사용으로 하단 Logic 구현
		// if (iYosucnt > 0) {
		// List<Collateral> yoSUTBLList = request.getYoSUTBL();
		// log.debug("*** yoSUTBLList :: {}", yoSUTBLList);

		// // // LoanAccResult loanAcc = new LoanAccResult();
		// response.setYoSUTBL(yoSUTBLList);
		// }
		if (!sessionManager.isLogin()) { // LMS 전송시 비로그인상태이므로 , 주민등록번호를 세팅해준다.
			// output.put("LMS_PerBusNo", input.getBody("LMS_PerBusNo"));

			response.setLmsPerBusNo(PerBusNo); // 방문판매여부 추가
		}

		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/listForeign", name = "계약서류제공 외화(보통예금, 정기예금) 신청내역조회")
	public SupDocListForeignResponse listForeign(IServiceContext serviceContext,
			SupDocListForeignRequest request) {
		// ASIS :: MA3CSTDOC005_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		String pAcctNum = StringUtils.nvl(request.getDrawAcctNum(), "");
		// String yiURL = StringUtils.nvl(request.getYiURL(), "");
		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		String tsPassword = sessionManager.getGlobalValue("TSPassword", String.class);
		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		log.info(" UserID : {} ", userID);
		log.info(" perBusNo : {} ", perBusNo);
		// String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
		// String userId = sessionManager.getGlobalValue("UserID", String.class);
		// 응답 객체
		SupDocListForeignResponse response = new SupDocListForeignResponse();

		// try {

		// 공통부 세팅
		// TI1IBK01_H386 (CB_IBK01_H386)
		OltpRequestOptions h386Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H386");

		h386Option.setImsTranCd("TI1IBK01");
		h386Option.setInClassCd("H386");
		h386Option.setSvcCd("386");
		h386Option.setCaptureSystem("OLTP");
		// 개별부 세팅
		CbIbk01H38600Req h38600Req = new CbIbk01H38600Req();
		h38600Req.setUserID(userID); // 이용자번호
		h38600Req.setTSPassword(tsPassword); // 통신비밀번호
		h38600Req.setYIJUMIN(perBusNo);
		h38600Req.setYIUPGB("9");
		h38600Req.setYIBRNO(Integer.valueOf(pAcctNum.substring(0, 3))); // 점 번호
		h38600Req.setYIKMCD(Integer.valueOf(pAcctNum.substring(3, 5))); // 과목코드
		h38600Req.setYIBUNHO(Integer.valueOf(pAcctNum.substring(5, 11))); // 계좌번호

		String subjectCode = "";
		String acctCode = "";
		// try {
		CbIbk01H38600Res h38600Res = this.hostClient.sendOltp(h386Option, h38600Req,
				CbIbk01H38600Res.class)
				.getResponse();

		log.debug("*** CbIbk01H38600Res :: {}", h38600Res);

		response = this.mapper.toForeign(h38600Res);

		log.debug("*** response :: {}", response);

		String acctType = "0";
		acctCode = pAcctNum.substring(3, 5);

		if ("85".equals(acctCode)
				|| "86".equals(acctCode)
				|| "89".equals(acctCode)) {
			acctType = "0";
		} else if ("88".equals(acctCode)) {
			acctType = "1";
		}

		subjectCode = h38600Res.getYOZONG();

		String homepageUrl = PropertiesUtils.getString("HOMEPAGE_URL"); // HOMEPAGE_URL
		response.setHomepageUrl(homepageUrl);
		response.setPdfUrl("/hp/file/ap/pd/");
		response.setAcctNum(pAcctNum);
		response.setAcctType(acctType);
		response.setYoSGJAN(FormatUtils.getFrmFore2(h38600Res.getYOSGJAN(), 2));

		// } catch (OltpSystemException e) {
		// response.setErrorCode(e.getErrorCode());
		// response.setErrorMessage(e.getErrorMessage());
		// response.setErrorModule(e.getErrorModule());
		// }

		// 기본 계약서류 조회
		if ("".equals(subjectCode)) {
			subjectCode = " ";
		}

		ContractDocumentParameter inParameter = ContractDocumentParameter.builder()
				.mappingCd1(acctCode)
				.mappingCd2(subjectCode)
				.mappingCd3("")
				.build();

		ContractDocumentResult dbDocResult = prdctDisclosureMgtDao.selectContractDocument(inParameter);

		log.debug("dbDocResult DB 조회 결과 :: {}", dbDocResult);
		if (dbDocResult != null) {

			response.setCmmAgreeFileNm(dbDocResult.getCmmAgreeFileNm()); // 공통약관파일명
			response.setCmmAgreeNm(dbDocResult.getCmmAgreeNm()); // 공통약관명
			response.setCmmAgreeCntrctOfferCode(dbDocResult.getCmmAgreeCntrctOfferCode()); // 공통약관아이디계약서류제공여부
			response.setCtgryCmmAgreeFileNm(dbDocResult.getCtgryCmmAgreeFileNm()); // 카테고리공통약관파일명
			response.setCtgryCmmAgreeNm(dbDocResult.getCtgryCmmAgreeNm()); // 카테고리공통약관명
			response.setCtgryCmmAgreeCntrctOfferCode(dbDocResult.getCtgryCmmAgreeCntrctOfferCode()); // 카테고리공통약관아이디계약서류제공여부
			response.setPrdctAgreeFileNm(dbDocResult.getPrdctAgreeFileNm()); // 상품약관명파일명
			response.setPrdctAgreeNm(dbDocResult.getPrdctAgreeNm()); // 상품약관명
			response.setPrdctAgreeCntrctOfferCode(dbDocResult.getPrdctAgreeCntrctOfferCode()); // 상품약관아이디계약서류제공여부
			response.setPrdctExplnFileNm(dbDocResult.getPrdctExplnFileNm()); // 상품설명서파일명
			response.setPrdctExplnNm(dbDocResult.getPrdctExplnNm()); // 상품설명서명
			response.setPrdctExplnCntrctOfferCode(dbDocResult.getPrdctExplnCntrctOfferCode()); // 상품설명서계약서류제공여부

			List<AddCntrctDocumentResult> addCntrctDocumentList = prdctDocListDao
					.selectAppendCommonDocumentList(dbDocResult.getPrdctId()); // 추가 계약서류 조회
			response.setForeignDocList(this.mapper.toCntrctDocumentList(addCntrctDocumentList));

		}

		response.setPerBusNo(perBusNo);

		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }

		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/listCard", name = "계약서류제공 카드 계약서조회")
	public SupDocListCardResponse listCard(IServiceContext serviceContext,
			SupDocListCardRequest request) {
		// ASIS :: MA3CSTDOC006_101S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		String pAcctNum = StringUtils.nvl(request.getDrawAcctNum(), "");
		// String yiURL = StringUtils.nvl(request.getYiURL(), "");
		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		log.info(" UserID : {} ", userID);
		log.info(" perBusNo : {} ", perBusNo);
		// String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
		// String userId = sessionManager.getGlobalValue("UserID", String.class);
		// 응답 객체
		SupDocListCardResponse response = new SupDocListCardResponse();

		// try {

		// 공통부 세팅
		// TI1IBK01_H384 (CB_IBK01_H384)
		OltpRequestOptions h384Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H384");

		h384Option.setImsTranCd("TI1IBK01");
		h384Option.setInClassCd("H384");
		h384Option.setSvcCd("384");
		h384Option.setCaptureSystem("OLTP");
		// 개별부 세팅
		CbIbk01H38400Req h38400Req = new CbIbk01H38400Req();

		h38400Req.setYIGUBUN("1"); // 1:카드번호, 2:주민+일자+고유번호
		h38400Req.setYIHWNO(pAcctNum); // 카드번호

		// try {
		// 전문 호출
		CbIbk01H38400Res h38400Res = this.hostClient.sendOltp(h384Option, h38400Req,
				CbIbk01H38400Res.class)
				.getResponse();

		log.debug("*** CbIbk01H38400Res :: {}", h38400Res);

		response = this.mapper.toCard(h38400Res);

		log.debug("*** response :: {}", response);
		if (null != h38400Res) {
			String yoHMJSO1 = h38400Res.getYOHMJSO1();
			if (yoHMJSO1 != null) {
				h38400Res.setYOHMJSO1(yoHMJSO1.replace(",", "."));
			}
			String yoHMJSO2 = h38400Res.getYOHMJSO2();
			if (yoHMJSO2 != null) {
				h38400Res.setYOHMJSO2(yoHMJSO2.replace(",", "."));
			}
		}
		// } catch (OltpSystemException e) {
		// response.setErrorCode(e.getErrorCode());
		// response.setErrorMessage(e.getErrorMessage());
		// response.setErrorModule(e.getErrorModule());
		// }

		response.setDrawAcctNum(pAcctNum);
		String homepageUrl = PropertiesUtils.getString("HOMEPAGE_URL"); // HOMEPAGE_URL
		response.setHomepageUrl(homepageUrl);
		response.setPdfUrl("/hp/file/ap/pd/");

		ContractDocumentParameter inParameter = ContractDocumentParameter.builder()
				.mappingCd1(request.getCoOperCardCode())
				.mappingCd2("")
				.mappingCd3("")
				.build();

		// 기본 계약서류 조회
		ContractDocumentResult dbDocResult = prdctDisclosureMgtDao.selectContractDocument(inParameter);

		log.debug("dbDocResult DB 조회 결과 :: {}", dbDocResult);
		if (dbDocResult != null) {

			response.setCmmAgreeFileNm(dbDocResult.getCmmAgreeFileNm()); // 공통약관파일명
			response.setCmmAgreeNm(dbDocResult.getCmmAgreeNm()); // 공통약관명
			response.setCmmAgreeCntrctOfferCode(dbDocResult.getCmmAgreeCntrctOfferCode()); // 공통약관아이디계약서류제공여부
			response.setCtgryCmmAgreeFileNm(dbDocResult.getCtgryCmmAgreeFileNm()); // 카테고리공통약관파일명
			response.setCtgryCmmAgreeNm(dbDocResult.getCtgryCmmAgreeNm()); // 카테고리공통약관명
			response.setCtgryCmmAgreeCntrctOfferCode(dbDocResult.getCtgryCmmAgreeCntrctOfferCode()); // 카테고리공통약관아이디계약서류제공여부
			response.setPrdctAgreeFileNm(dbDocResult.getPrdctAgreeFileNm()); // 상품약관명파일명
			response.setPrdctAgreeNm(dbDocResult.getPrdctAgreeNm()); // 상품약관명
			response.setPrdctAgreeCntrctOfferCode(dbDocResult.getPrdctAgreeCntrctOfferCode()); // 상품약관아이디계약서류제공여부
			response.setPrdctExplnFileNm(dbDocResult.getPrdctExplnFileNm()); // 상품설명서파일명
			response.setPrdctExplnNm(dbDocResult.getPrdctExplnNm()); // 상품설명서명
			response.setPrdctExplnCntrctOfferCode(dbDocResult.getPrdctExplnCntrctOfferCode()); // 상품설명서계약서류제공여부

			List<AddCntrctDocumentResult> addCntrctDocumentList = prdctDocListDao
					.selectAppendCommonDocumentList(dbDocResult.getPrdctId()); // 추가 계약서류 조회
			response.setCardDocList(this.mapper.toCntrctDocumentList(addCntrctDocumentList));

		}

		response.setPerBusNo(perBusNo);

		log.debug("*** response :: {}", response);

		// } catch (Exception e) {

		// }

		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	// @ServiceEndpoint(url = "/getEdmsPdfFile", name = "EDMS 계약서류조회 PDF 다운로드진행")
	// public SupDocGetEdmsPdfFileResponse getEdmsPdfFile(IServiceContext
	// serviceContext,
	// SupDocGetEdmsPdfFileRequest request) {

	// // ASIS :: MA3CSTDOC004_104S
	// log.debug("🍕 {} 🍕", new Object() {
	// }.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
	// // CbIbk01H38300Req sendData = new CbIbk01H38300Req();
	// // String yiURL = StringUtils.nvl(request.getYiURL(), "");
	// // session데이터 Get.
	// String userID =
	// StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID",
	// String.class),
	// sessionManager.getGlobalValue("UserID", String.class));
	// log.info(" UserID : {} ", userID);
	// String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
	// // String userId = sessionManager.getGlobalValue("UserID", String.class);
	// String elementId = StringUtils.defaultIfEmpty(request.getElementId(), ""); //
	// ElementID
	// String docCode = StringUtils.defaultIfEmpty(request.getDocCode(), ""); //
	// DocCode
	// String acctNo = StringUtils.defaultIfEmpty(request.getAcctNo(), ""); // 계좌번호
	// String chnnlMk = StringUtils.defaultIfEmpty(request.getChnnlMk(), ""); //
	// 채널구분 01:UI 02:모바일 03:MP 04:PPR
	// String jobMk = StringUtils.defaultIfEmpty(request.getJobMk(), ""); // 업무구분
	// 01:여신 02:펀드
	// String barcodeData = StringUtils.defaultIfEmpty(request.getBarcodeData(),
	// ""); // 바코드데이터(UI이면서펀드일떄)

	// String custSsn = StringUtils.nvl(perBusNo, "");
	// String isApp = StringUtils.defaultIfEmpty(request.getIsApp(), "N");

	// EdocPayloadInfo payload = new EdocPayloadInfo();

	// payload.setElementId(elementId);
	// payload.setAcctNo(acctNo);
	// payload.setCustSsn(custSsn);
	// payload.setDocCode(docCode);
	// payload.setChnnlMk(chnnlMk);
	// payload.setJobMk(jobMk);
	// payload.setBarcodeData(barcodeData);
	// // input.setBody("elementId", elementId);
	// // input.setBody("acctNo", acctNo);
	// // input.setBody("docCode", docCode);
	// // input.setBody("custSsn", custSsn);
	// // input.setBody("chnnlMk", chnnlMk);
	// // input.setBody("jobMk", jobMk);
	// // input.setBody("barcodeData", barcodeData);

	// // String edmsXvarmDocMeta =
	// // StringUtils.nvl(request.getEdmsXvarmDocMeta().trim(), "[{}]");
	// // 응답 객체
	// SupDocGetEdmsPdfFileResponse response = new SupDocGetEdmsPdfFileResponse();

	// response.setFilePath("C:\\jdk\\doc\\12345987123_20_Ele_dnc.pdf");
	// // response.setContractPdfData(contractPdfData);
	// response.setIsApp(isApp);

	// FileOutputStream fos = null;
	// FileInputStream inputStream = null;
	// byte[] byteFile = null;

	// FileSystemResource fsr = null;
	// File file = null;
	// try {

	// Path filePath = Paths.get("C:\\jdk\\doc\\12345987123_20_Ele_dnc.pdf");
	// log.debug("*** filePath :: {}", filePath);
	// file = new File(filePath.toString());
	// log.debug("*** file :: {}", file);
	// inputStream = new FileInputStream(file);
	// log.debug("*** inputStream :: {}", inputStream);
	// byteFile = ByteStreams.toByteArray(inputStream);
	// response.setByteFile(byteFile);
	// // response.setStrFile(new String(byteFile, StandardCharsets.UTF_8));

	// ObjectMapper mapper = new ObjectMapper();

	// response.setStrFile(mapper.writeValueAsString(byteFile));

	// } catch (Exception e) {

	// // } finally {
	// // if (fos != null)
	// // try {
	// // fos.close();
	// // } catch (Exception e) {
	// // }
	// // if (inputStream != null)
	// // try {
	// // inputStream.close();
	// // } catch (Exception e) {
	// // }
	// }

	// log.debug("🍕 {} 종료 🍕", new Object() {
	// }.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
	// return response;
	// }

	@ServiceEndpoint(url = "/getEdmsPdfFile", name = "EDMS 계약서류조회 PDF 다운로드진행")
	public SupDocGetEdmsPdfFileResponse getEdmsPdfFile(IServiceContext serviceContext,
			SupDocGetEdmsPdfFileRequest request) {

		// ASIS :: MA3CSTDOC004_104S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		// CbIbk01H38300Req sendData = new CbIbk01H38300Req();
		// String yiURL = StringUtils.nvl(request.getYiURL(), "");
		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID",
				String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		log.info(" UserID : {} ", userID);
		String perBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
		// String userId = sessionManager.getGlobalValue("UserID", String.class);
		String elementId = StringUtils.defaultIfEmpty(request.getElementId(), ""); // ElementID
		String docCode = StringUtils.defaultIfEmpty(request.getDocCode(), ""); // DocCode
		String acctNo = StringUtils.defaultIfEmpty(request.getAcctNo(), ""); // 계좌번호
		String chnnlMk = StringUtils.defaultIfEmpty(request.getChnnlMk(), ""); // 채널구분 01:UI 02:모바일 03:MP 04:PPR
		String jobMk = StringUtils.defaultIfEmpty(request.getJobMk(), ""); // 업무구분 01:여신 02:펀드
		String barcodeData = StringUtils.defaultIfEmpty(request.getBarcodeData(), ""); // 바코드데이터(UI이면서펀드일떄)

		String custSsn = StringUtils.nvl(perBusNo, "");
		String isApp = StringUtils.defaultIfEmpty(request.getIsApp(), "N");

		EdocPayloadInfo payload = new EdocPayloadInfo();

		payload.setElementId(elementId);
		payload.setAcctNo(acctNo);
		payload.setCustSsn(custSsn);
		payload.setDocCode(docCode);
		payload.setChnnlMk(chnnlMk);
		payload.setJobMk(jobMk);
		payload.setBarcodeData(barcodeData);
		// input.setBody("elementId", elementId);
		// input.setBody("acctNo", acctNo);
		// input.setBody("docCode", docCode);
		// input.setBody("custSsn", custSsn);
		// input.setBody("chnnlMk", chnnlMk);
		// input.setBody("jobMk", jobMk);
		// input.setBody("barcodeData", barcodeData);

		// String edmsXvarmDocMeta =
		// StringUtils.nvl(request.getEdmsXvarmDocMeta().trim(), "[{}]");
		// 응답 객체
		SupDocGetEdmsPdfFileResponse response = new SupDocGetEdmsPdfFileResponse();

		// response.setFilePath("C:\\jdk\\doc\\12345987123_20_Ele_dnc.pdf");
		// response.setContractPdfData(contractPdfData);
		response.setIsApp(isApp);

		// FileOutputStream fos = null;
		// FileInputStream inputStream = null;
		// try {

		// TEST 20260331 start
		JSONObject result = edocAPComponent.requestEdmsXvarmDocDownload(payload);

		log.debug("*** result :: {}", result);
		// JSONObject result = edocAPConnectService.requestEdmsXvarmDocCheck(input);

		// output.setBody("edmsXvarmDocList", result.get("edmsXvarmDocList"));
		// response.setEdmsXvarmDocList(result);
		/**
		 * STEP01 : contractFileData를 가져옴( Seed 128 암호화 + BASE64 encode + URLEncode
		 * 되어있는
		 * 데이터 )
		 * STEP02 : contractFileData를 URLDecode + Base64Decode 진행
		 * STEP03 : 파일로 작성
		 * STEP04 : Seed128 복호화 진행
		 * STEP05 : 복호화된 파일 Base64 데이터로 변환 + 파일삭제처리
		 * STEP06 : Client로 전달
		 */

		// String encFileData = String.valueOf(result.get("contractFileData"));
		// log.debug("*** encFileData :: {}", encFileData);
		// byte[] decFileData =
		// Base64.getDecoder().decode(URLDecoder.decode(encFileData, "UTF-8"));

		// TEST 20260331 end
		// byte[] decFileData =
		// Files.readAllBytes(Paths.get("C:\\jdk\\doc\\12345987123_20_Ele_dnc.pdf"));
		// log.debug("*** fos :: {}", decFileData);

		// String filePath = "/APP/mpms_RESOURCE/TEMP/";
		// String filePath = PropertiesUtils.getString("CONTRACT_DOC_TEMP_PATH");
		// String encFilePath = acctNo + "_" + docCode + "_" + elementId + "_enc.pdf";
		// String decFilePath = acctNo + "_" + docCode + "_" + elementId + ".pdf";

		// String filePath = "C:\\jdk\\doc\\";
		// String encFilePath = "qwer.pdf";
		// String decFilePath = acctNo + "_" + docCode + "_" + elementId + ".pdf";
		// File encFile = new File(filePath + encFilePath);
		// fos = new FileOutputStream(encFile);
		// // log.debug("*** fos :: {}", fos);

		// fos.write(decFileData);
		// fos.close();

		// boolean fileDecryptFlag = EdocUtils.deCryptionFile(filePath, filePath,
		// encFilePath, decFilePath);

		// log.debug("###PSH MA3CSTDOC004_104S fileDecryptFlag : " + fileDecryptFlag);

		// // File decFile = new File(filePath + decFilePath); // Decrypt된 파일
		// File decFile = new File("C:\\jdk\\doc\\12345987123_20_Ele_dnc.pdf"); //
		// Decrypt된 파일
		// log.debug("*** decFile :: {}", decFile.getAbsolutePath());

		// inputStream = new FileInputStream(decFile);
		// response.setDecFile(decFile.getAbsolutePath());

		// ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();

		// int len = 0;

		// byte[] buf = new byte[1024];

		// while ((len = inputStream.read(buf)) != -1) {
		// byteOutStream.write(buf, 0, len);
		// }

		// byte[] fileArray = byteOutStream.toByteArray();
		// log.debug("*** fileArray :: {}", fileArray);

		// String contractPdfData = new
		// String(Base64.getEncoder().encode(fileArray));
		// log.debug("*** contractPdfData :: {}", contractPdfData);

		// output.setBody("contractPdfData", contractPdfData);
		// test
		response.setEdmsXvarmDoc(result);

		// // SCBKLogger.debug("###PSH MA3CSTDOC004_104S isNative : [" +
		// // SessionManager.isNative() + "]");

		// if (!"Y".equals(isApp)) {
		// // file download
		// // output.setAttachment("fileKey", decFile);
		// // } else {
		// if (encFile.exists())
		// encFile.delete();
		// if (decFile.exists())
		// decFile.delete();
		// }
		// } catch (Exception e) {

		// // } finally {
		// // if (fos != null)
		// // try {
		// // fos.close();
		// // } catch (Exception e) {
		// // }
		// // if (inputStream != null)
		// // try {
		// // inputStream.close();
		// // } catch (Exception e) {
		// // }
		// }

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/updateEdmsPdfFileDownloadCount", name = "EDMS 계약서류조회 PDF 조회 , 다운로드횟수 증가")
	public SupDocUpdateEdmsPdfFileDownloadCountResponse updateEdmsPdfFileDownloadCount(IServiceContext serviceContext,
			SupDocUpdateEdmsPdfFileDownloadCountRequest request) {
		// ASIS :: MA3CSTDOC004_105S
		log.debug("🍕 {} 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		String elementId = StringUtils.nvl(request.getElementId(), "");
		String docCode = StringUtils.nvl(request.getDocCode(), "");
		String acctNo = StringUtils.nvl(request.getAcctNo(), ""); // 계좌번호
		String procMk = StringUtils.nvl(request.getProcMk(), ""); // 01 : 조회 02 : 다운로드 없으면 둘다진행

		// String custSsn = StringUtils.defaultIfEmpty(
		// SessionManager.getGlobalValue("PerBusNo") , "");
		// session데이터 Get.
		String userID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
				sessionManager.getGlobalValue("UserID", String.class));
		log.info(" UserID : {} ", userID);
		// 응답 객체
		SupDocUpdateEdmsPdfFileDownloadCountResponse response = new SupDocUpdateEdmsPdfFileDownloadCountResponse();

		// try {

		// SCBKActionData output = this.transfer(ctx, "MCI_IB_EDMS_002", sendData);
		// 공통부 세팅
		MciRequestOptions mciOption = this.hostClient.getMciRequestOptions("MCI_IB_EDMS_002");
		mciOption.setTranCd("IB_EDMS_002"); // AS-IS BUSINESS_FUNCTION_ID
		mciOption.setBlngBrNo("0019"); // AS-IS AGENTBRANCH
		mciOption.setTxnBrNo("0019"); // AS-IS HOMEBRANCH
		// sendData.putMciComm("SUBTASKCODE", "0001");

		// 개별부 세팅
		MciIbEdms002Req mciReq = MciIbEdms002Req.builder()
				.ELEMENT_ID(elementId)
				.ACCT_NO(acctNo)
				.DOC_CODE(docCode)
				.build();
		log.debug("*** mciReq :: {}", mciReq);
		MciIbEdms002Res mciRes = null;
		if ("01".equals(procMk)
				|| "02".equals(procMk)) {
			log.debug("*** procMk :: {}", procMk);
			MciIbEdms002Req addMciReq = mciReq.builder()
					.PROC_MK(procMk)
					.build();
			log.debug("*** mciReq :: {}", mciReq);
			// output = this.transfer(ctx, "MCI_IB_EDMS_002", sendData);
			mciRes = this.hostClient.sendMci(mciOption, addMciReq, MciIbEdms002Res.class).getResponse();

		} else {
			String[] PROC_MK_ARRAY = "01|02".split("[|]");
			for (int i = 0; i < PROC_MK_ARRAY.length; i++) {
				// sendData.putBody("PROC_MK", PROC_MK_ARRAY[i]); // 처리구분 01:조회 02:다운로드
				// output = this.transfer(ctx, "MCI_IB_EDMS_002", sendData);
				// SCBKLogger.debug("####PSH MA3CSTDOC004_105S output : " +
				// output.getBodyData());
				MciIbEdms002Req addMciReq = mciReq.builder()
						.PROC_MK(PROC_MK_ARRAY[i])
						.build();
				log.debug("*** mciReq :: {}", mciReq);
				// output = this.transfer(ctx, "MCI_IB_EDMS_002", sendData);
				mciRes = this.hostClient.sendMci(mciOption, addMciReq, MciIbEdms002Res.class).getResponse();
			}
		}
		// MciIbEdms001Res mciRes = this.hostClient.sendMci(mciOption, mciReq,
		// MciIbEdms001Res.class).getResponse();

		log.debug("*** mciRes :: {}", mciRes);

		if (null != mciRes) {
			response.setDummyTemp1(mciRes.getDUMMY_TEMP1());
			response.setResCode(mciRes.getRESCODE());
			response.setBodyLength(mciRes.getBODY_LENGTH());
			response.setAomacroNm(mciRes.getAOMACRONM());
			response.setDummyTemp2(mciRes.getDUMMY_TEMP2());
		}
		// } catch (Exception e) {

		// }

		log.debug("*** response :: {}", response);

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}
}