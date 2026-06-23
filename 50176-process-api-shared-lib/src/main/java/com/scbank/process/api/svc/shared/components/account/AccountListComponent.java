package com.scbank.process.api.svc.shared.components.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03100Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03200Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03300Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H44K01Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H44K01Res.YO_JRPGSU;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03500Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03500Res.CbIbk03H03500ResGrid;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03900Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03900Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03900Res.YOGJARR;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.account.constant.AccountSessionType;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.CardAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DemandDepositAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositSavingAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositTrustAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.FundAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.FxAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.IsaAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo;
import com.scbank.process.api.svc.shared.components.account.dto.ListAccountHeldInfo.yoMyInfRecList;
import com.scbank.process.api.svc.shared.components.account.dto.ListMydataCIFromDBResponse;
import com.scbank.process.api.svc.shared.components.account.dto.ListRegisteredOpenBankAccountFromAPIResponse;
import com.scbank.process.api.svc.shared.components.account.dto.LoanAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.MortgageLoanAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.MtsAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.OpenBankUserInfoParameter;
import com.scbank.process.api.svc.shared.components.account.dto.OpenBankUserInfoResult;
import com.scbank.process.api.svc.shared.components.account.dto.OpenbankingAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.PaymentAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.PaymentNormalAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.PensionTrustAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.RepresentAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.session.AllAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.CardAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.DemandDepositAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.DepositAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.DepositSavingAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.DepositTrustAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.FundAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.FxAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.IsaAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.LoanAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.MortgageLoanAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.MtsAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.NewAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.OpenbankingAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.PaymentAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.PaymentNormalAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.PensionTrustAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.RepresentAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.helper.AccountHelper;
import com.scbank.process.api.svc.shared.components.account.mapper.AccountMapper;
import com.scbank.process.api.svc.shared.components.account.mapper.ListAccountHeldMapper;
import com.scbank.process.api.svc.shared.components.obs.kftc.interceptor.KftcObsLogInterceptor;
import com.scbank.process.api.svc.shared.components.obs.kftc.service.KftcObsApiClient;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntityBuilder;
import com.scbank.process.api.svc.shared.components.obs.utils.KftcObsHelper;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.scbank.process.api.svc.shared.dao.Ma3RprsntAcctMgtDao;
import com.scbank.process.api.svc.shared.dao.MydataOrgMgtDao;
import com.scbank.process.api.svc.shared.dao.OpenBankUserMgtDao;
import com.scbank.process.api.svc.shared.dao.SaleFundInfoDao;
import com.scbank.process.api.svc.shared.dao.SpcfcMoneyTrustIsaPrdctDao;
import com.scbank.process.api.svc.shared.dao.dto.MyDataCIInfoResult;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankAccountMgtListParameter;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankAccountMgtListResult;
import com.scbank.process.api.svc.shared.dao.dto.RepresentAccountResult;
import com.scbank.process.api.svc.shared.dao.dto.SaleFundInfoResult;
import com.scbank.process.api.svc.shared.dao.dto.SpcfcTrustPrdctResult;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.FormatUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class AccountListComponent {

    /**
     * EDMI 통합 클라이언트
     */
    private final HostClient hostClient;

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * 오픈뱅킹 API 클라이언트 공유 컴포넌트
     */
    private final KftcObsApiClient obsClient;

    /**
     * MAPPER
     */
    private final AccountHelper helper;

    private final AccountMapper mapper;

    private final ListAccountHeldMapper listAccountHeldMapper;

    /**
     * DAO
     */
    private final SaleFundInfoDao saleFundInfoDao;

    private final SpcfcMoneyTrustIsaPrdctDao spcfcMoneyTrustIsaPrdctDao;

    private final Ma3RprsntAcctMgtDao ma3RprsntAcctMgtDao;

    private final OpenBankUserMgtDao ma3OpenBankUserMgtDao;

    private final MydataOrgMgtDao ma3MydataOrgMgtDao;

    @ComponentOperation(name = "전계좌목록조회(세션)")
    public List<AllAccountInfo> getAllAccountList() {
        return this.getAllAccountList(false);
    }

    @ComponentOperation(name = "전계좌목록조회")
    public List<AllAccountInfo> getAllAccountList(boolean isReload) {

        List<AllAccountInfo> allAccountList = new ArrayList<AllAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.ALL)) {
            if (helper.isAccountListSession(AccountSessionType.ALL)) {
                allAccountList = mapper.toAllAccountListFromSession(getSimpleAllAccountList());
            } else {
                // h035로그인 전문을 날려서 전계좌목록을 재생성한다.
                CbIbk03H03500Res res = helper.sendHostTi1ibk03H035();
                allAccountList = mapper.toAllAccountListFromSession(resetAllAccountList(res));
            }

        } else {
            allAccountList = mapper.toAllAccountListFromSession(helper.getAllAcountSessionList());
        }

        return allAccountList;
    }

    @ComponentOperation(name = "대출계좌목록조회(세션)")
    public List<LoanAccountInfo> getLoanAccountList() {
        return getLoanAccountList(false);
    }

    @ComponentOperation(name = "대출계좌목록조회")
    public List<LoanAccountInfo> getLoanAccountList(boolean isReload) {

        List<LoanAccountInfo> loanAccountList = new ArrayList<LoanAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.LOAN)) {
            loanAccountList = mapper.toLoanAccountListFromSession(resetLoanAccount());
        } else {
            loanAccountList = mapper.toLoanAccountListFromSession(helper.getLoanAccountSessionList());
        }

        return loanAccountList;
    }

    @ComponentOperation(name = "펀드계좌목록조회(세션)")
    public List<FundAccountInfo> getFundAccountList() {
        return getFundAccountList(false);
    }

    @ComponentOperation(name = "펀드계좌목록조회")
    public List<FundAccountInfo> getFundAccountList(boolean isReload) {

        List<FundAccountInfo> fundAccountList = new ArrayList<FundAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.FUND)) {
            fundAccountList = mapper.toFundAccountListFromSession(resetFundAccount());
        } else {
            fundAccountList = mapper.toFundAccountListFromSession(helper.getFundAccountSessionList());
        }

        return fundAccountList;
    }

    @ComponentOperation(name = "예적금 계좌목록조회")
    public List<DepositAccountInfo> getDepositAccountList() {

        return this.getDepositAccountList(false);

    }

    @ComponentOperation(name = "예적금 계좌목록조회")
    public List<DepositAccountInfo> getDepositAccountList(boolean isReload) {

        List<DepositAccountInfo> depositAccountList = new ArrayList<DepositAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.DEPOSIT)) {
            depositAccountList = mapper.toDepositAccountListFromSession(resetDepositAccount());
        } else {
            depositAccountList = mapper.toDepositAccountListFromSession(helper.getDepositAccountSessionList());
        }

        return depositAccountList;

    }

    @ComponentOperation(name = "예적금 및 신탁 계좌목록조회(세션)")
    public List<DepositTrustAccountInfo> getDepositTrustAccountList() {
        return this.getDepositTrustAccountList(false);
    }

    @ComponentOperation(name = "예적금 및 신탁 계좌목록조회")
    public List<DepositTrustAccountInfo> getDepositTrustAccountList(boolean isReload) {

        List<DepositTrustAccountInfo> depositTrustAccountList = new ArrayList<DepositTrustAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.DEPOSIT_TRUST)) {
            depositTrustAccountList = mapper.toDepositTrustAccountListFromSession(resetDepositTrustAccount());
        } else {
            depositTrustAccountList = mapper
                    .toDepositTrustAccountListFromSession(helper.getDepositTrustAccountSessionList());
        }

        return depositTrustAccountList;

    }

    @ComponentOperation(name = "예적금(10,20,30,80,85,88,90) 계좌목록조회(세션)")
    public List<DepositSavingAccountInfo> getDepositSavingAccountList() {
        return getDepositSavingAccountList(false);
    }

    @ComponentOperation(name = "예적금(10,20,30,80,85,88,90) 계좌목록조회")
    public List<DepositSavingAccountInfo> getDepositSavingAccountList(boolean isReload) {

        List<DepositSavingAccountInfo> depositSavingAccountList = new ArrayList<DepositSavingAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.DEPOSIT_SAVING)) {
            depositSavingAccountList = mapper.toDepositSavingAccountListFromSession(resetDepositSavingAccount());
        } else {
            depositSavingAccountList = mapper
                    .toDepositSavingAccountListFromSession(helper.getDepositSavingAccountSessionList());
        }

        return depositSavingAccountList;

    }

    @ComponentOperation(name = "출금 계좌목록조회")
    public List<PaymentAccountInfo> getPaymentAccountList() {
        return this.getPaymentAccountList(false);
    }

    @ComponentOperation(name = "출금 계좌목록조회")
    public List<PaymentAccountInfo> getPaymentAccountList(boolean isReload) {
        List<PaymentAccountInfo> paymentAccountList = new ArrayList<PaymentAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.PAYMENT)) {
            paymentAccountList = mapper.toPaymentAccountListFromSession(resetPaymentAccount());
        } else {
            paymentAccountList = mapper
                    .toPaymentAccountListFromSession(helper.getPaymentAccountSessionList());
        }

        return paymentAccountList;
    }

    @ComponentOperation(name = "출금(10,20,30 계좌만) 계좌목록조회")
    public List<PaymentNormalAccountInfo> getPaymentNormalAccountList() {
        return this.getPaymentNormalAccountList(false);
    }

    @ComponentOperation(name = "출금(10,20,30 계좌만) 계좌목록조회")
    public List<PaymentNormalAccountInfo> getPaymentNormalAccountList(boolean isReload) {
        List<PaymentNormalAccountInfo> paymentNormalAccountList = new ArrayList<PaymentNormalAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.PAYMENT_NORMAL)) {
            paymentNormalAccountList = mapper.toPaymentNormalAccountListFromSession(resetPaymentNormalAccount());
        } else {
            paymentNormalAccountList = mapper
                    .toPaymentNormalAccountListFromSession(helper.getPaymentNormalAccountSessionList());
        }

        return paymentNormalAccountList;
    }

    @ComponentOperation(name = "예금 계좌목록조회(세션)")
    public List<DemandDepositAccountInfo> getDemandDepositAccountList() {
        return this.getDemandDepositAccountList(false);
    }

    @ComponentOperation(name = "예금 계좌목록조회")
    public List<DemandDepositAccountInfo> getDemandDepositAccountList(boolean isReload) {
        List<DemandDepositAccountInfo> demandDepositAccountList = new ArrayList<DemandDepositAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.DEMAND_DEPOSIT)) {
            demandDepositAccountList = mapper.toDemandDepositAccountListFromSession(resetDemandDepositAccount());
        } else {
            demandDepositAccountList = mapper
                    .toDemandDepositAccountListFromSession(helper.getDemandDepositAccountSessionList());
        }

        return demandDepositAccountList;
    }

    @ComponentOperation(name = "FX 계좌목록조회(세션)")
    public List<FxAccountInfo> getFxAccountList() {
        return getFxAccountList(false);
    }

    @ComponentOperation(name = "FX 계좌목록조회")
    public List<FxAccountInfo> getFxAccountList(boolean isReload) {
        List<FxAccountInfo> fxAccountList = new ArrayList<FxAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.FX)) {
            fxAccountList = mapper.toFxAccountListFromSession(resetFxAccount());
        } else {
            fxAccountList = mapper
                    .toFxAccountListFromSession(helper.getFxAccountSessionList());
        }

        return fxAccountList;
    }

    @ComponentOperation(name = "카드 계좌목록조회(세션)")
    public List<CardAccountInfo> getCardAccountList() {
        return getCardAccountList(false);
    }

    @ComponentOperation(name = "카드 계좌목록조회")
    public List<CardAccountInfo> getCardAccountList(boolean isReload) {
        List<CardAccountInfo> cardAccountList = new ArrayList<CardAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.CARD)) {
            cardAccountList = mapper.toCardAccountListFromSession(resetCardAccount());
        } else {
            cardAccountList = mapper.toCardAccountListFromSession(helper.getCardAccountSessionList());
        }

        return cardAccountList;
    }

    @ComponentOperation(name = "MTS 계좌목록조회(세션)")
    public List<MtsAccountInfo> getMtsAccountList() {
        return getMtsAccountList(false);
    }

    @ComponentOperation(name = "MTS 계좌목록조회")
    public List<MtsAccountInfo> getMtsAccountList(boolean isReload) {
        List<MtsAccountInfo> mtsAccountList = new ArrayList<MtsAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.MTS)) {
            mtsAccountList = mapper.toMtsAccountListFromSession(resetMtsAccount());
        } else {
            mtsAccountList = mapper.toMtsAccountListFromSession(helper.getMtsAccountSessionList());
        }

        return mtsAccountList;
    }

    @ComponentOperation(name = "ISA 계좌목록조회(세션)")
    public List<IsaAccountInfo> getIsaAccountList() {
        return getIsaAccountList(false);
    }

    @ComponentOperation(name = "ISA 계좌목록조회")
    public List<IsaAccountInfo> getIsaAccountList(boolean isReload) {
        List<IsaAccountInfo> isaAccountList = new ArrayList<IsaAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.ISA)) {
            isaAccountList = mapper.toIsaAccountListFromSession(resetIsaAccount());
        } else {
            isaAccountList = mapper.toIsaAccountListFromSession(helper.getIsaAccountSessionList());
        }

        return isaAccountList;
    }

    @ComponentOperation(name = "예적금담보대출 담보계좌(46,48,49,70,80,90,94) 목록조회(세션)")
    public List<MortgageLoanAccountInfo> getMortgageLoanAccountList() {
        return getMortgageLoanAccountList(false);
    }

    @ComponentOperation(name = "예적금담보대출 담보계좌(46,48,49,70,80,90,94) 목록조회")
    public List<MortgageLoanAccountInfo> getMortgageLoanAccountList(boolean isReload) {
        List<MortgageLoanAccountInfo> mnortgageLoanAccountList = new ArrayList<MortgageLoanAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.MORTGAGE_LOAN)) {
            mnortgageLoanAccountList = mapper.toMortgageLoanAccountListFromSession(resetMortgageLoanAccount());
        } else {
            mnortgageLoanAccountList = mapper
                    .toMortgageLoanAccountListFromSession(helper.getMortgageLoanAccountSessionList());
        }

        return mnortgageLoanAccountList;
    }

    @ComponentOperation(name = "대표 계좌목록조회(세션)")
    public List<RepresentAccountInfo> getRepresentAccountList() {
        return this.getRepresentAccountList(false);
    }

    @ComponentOperation(name = "대표 계좌목록조회")
    public List<RepresentAccountInfo> getRepresentAccountList(boolean isReload) {
        List<RepresentAccountInfo> representAccountList = new ArrayList<RepresentAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.REPRESENT)) {
            representAccountList = mapper.toRepresentAccountListFromSession(resetRepresentAccount());
        } else {
            representAccountList = mapper
                    .toRepresentAccountListFromSession(helper.getRepresentAccountSessionList());
        }

        return representAccountList;
    }

    @ComponentOperation(name = "오픈뱅킹 계좌목록조회(세션)")
    public List<OpenbankingAccountInfo> getOpenbankingAccountList() {
        return this.getOpenbankingAccountList(false, "");
    }

    @ComponentOperation(name = "오픈뱅킹 계좌목록조회")
    public List<OpenbankingAccountInfo> getOpenbankingAccountList(boolean isReload, String isApiSkip) {

        String obsAgreeApiCheck = StringUtils
                .defaultIfEmpty(sessionManager.getLoginValue("obsAgreeApiCheck", String.class), "");
        String apiSkipFlag = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("apiSkipFlag", String.class), "");

        List<OpenbankingAccountInfo> openbankingAccountList = new ArrayList<OpenbankingAccountInfo>();

        // session에 사용자 정보 없을 경우 실행
        String obsUserSeqNo = sessionManager.getLoginValue("OBS_USER_SEQ_NO", String.class);

        if (StringUtils.defaultIfEmpty(obsUserSeqNo, "").equals("")) {
            initObsUserInfo();
        }

        // (재조회여부|| session의 계좌리스트 null 체크) && 오픈뱅킹가입체크.
        if ((isReload || !helper.isAccountListSession(AccountSessionType.OBS_ALL))
                || (!"Y".equals(obsAgreeApiCheck) && !"Y".equals(apiSkipFlag))) {
            // 오픈뱅킹 서비스 가입된 고객의 경우 계좌 조회.
            if (!"".equals(obsUserSeqNo)) {
                openbankingAccountList = getObsAllAccountList(isApiSkip);
            }
        } else {
            openbankingAccountList = mapper
                    .toOpenbankingAccountListFromSession(helper.getOpenbankingAccountSessionList());
        }

        return openbankingAccountList;
    }

    @ComponentOperation(name = "연금신탁 계좌목록조회(세션)")
    public List<PensionTrustAccountInfo> getPensionTrustAccountList() {

        return this.getPensionTrustAccountList(false);
    }

    @ComponentOperation(name = "연금신탁 계좌목록조회")
    public List<PensionTrustAccountInfo> getPensionTrustAccountList(boolean isReload) {

        List<PensionTrustAccountInfo> pensionTrustAccountList = new ArrayList<PensionTrustAccountInfo>();

        if (isReload || !helper.isAccountListSession(AccountSessionType.PENSION_TRUST)) {
            pensionTrustAccountList = mapper.toPensionTrustAccountListFromSession(resetPensionTrustAccount());
        } else {
            pensionTrustAccountList = mapper
                    .toPensionTrustAccountListFromSession(helper.getPensionTrustAccountSessionList());
        }

        return pensionTrustAccountList;
    }

    @ComponentOperation(name = "전체 계좌목록 초기화")
    public List<AllAccountInfoSession> resetAllAccountList(CbIbk03H03500Res h035) {

        List<CbIbk03H03500ResGrid> h035GridList = h035.getREC_01();
        String virtualAccountYn = h035.getVirtualAccountYN();
        String userId = h035.getUserID();

        List<AllAccountInfoSession> allAccountList = new ArrayList<AllAccountInfoSession>();

        List<PaymentAccountInfoSession> paymentAccountList = new ArrayList<PaymentAccountInfoSession>();
        List<DemandDepositAccountInfoSession> demandDepositAccountList = new ArrayList<DemandDepositAccountInfoSession>();

        List<String> trustCodeList = new ArrayList<String>();
        List<String> fundCodeList = new ArrayList<String>();

        String bcCardYn = "N"; // BC카드 보유여부
        String hdCardYn = "N"; // 현대카드 보유여부

        String usingFamilyCardYn = "N"; // 가족카드여부

        Locale locale = LocaleContextHolder.getLocale();

        for (CbIbk03H03500ResGrid h035Grid : h035GridList) {

            if (StringUtils.isNotBlank(h035Grid.getDrawAcctNum())) {

                AllAccountInfoSession account = mapper.toAllAccountInfo(h035Grid);

                String kwamok = StringUtils.nvl(account.getDrawAcctNum(), "00000000000000").substring(3, 5);
                String assort = account.getAssort();

                if ("1".equals(account.getAcctType())) {
                    account.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                    if ("Y".equals(virtualAccountYn)) {
                        if ("15|16".contains(kwamok)) {
                            account.setDrawAcctName("연계계좌");
                            account.setDrawAcctNameAlias("연계계좌");
                        }
                    }

                    if ("59".equals(kwamok)) {

                        String investAssetNo = account.getFundCode(); // 신탁명 운용자산번호로 셋팅
                        if ("33|43".contains(assort)) {
                            account.setDrawAcctTrustName("MMT");
                        }

                        if (StringUtils.isNotBlank(investAssetNo)) {
                            account.setInvestAssetNo(investAssetNo);
                        }
                    }

                } else if ("2".equals(account.getAcctType())) { // 펀드계좌

                    if (Locale.KOREA.equals(locale)) {
                        String acctName = StringUtils.defaultIfEmpty(account.getDrawAcctName(), "펀드");
                        account.setDrawAcctName(acctName);
                    } else {
                        String acctName = StringUtils.defaultIfEmpty(account.getDrawAcctEngName(), "Fund");
                        account.setDrawAcctName(acctName);
                    }

                    fundCodeList.add(account.getFundCode());
                } else if ("3".equals(account.getAcctType())) { // 대출계좌
                    if (!kwamok.startsWith("7")) {
                        // 7로 시작하지 않은 경우에는 한도대출(마이너스대출) Set
                        if (Locale.KOREA.equals(locale)) {
                            account.setDrawAcctName(" 한도대출(마이너스대출)");
                        } else {
                            account.setDrawAcctName(" Credit Line");
                        }
                    } else {
                        account.setDrawAcctName(PRCSharedUtils.getLoanAccountName(kwamok, assort, account.getLoanKind(),
                                account.getLoanAcctKmCD()));
                    }
                } else if ("4".equals(account.getAcctType())) { // 카드
                    if (!Locale.KOREA.equals(locale)) {
                        account.setDrawAcctName(PRCSharedUtils.getCardNameByKind(account.getCardType()));
                    }

                    if ("4".equals(account.getFamilyType())) {
                        usingFamilyCardYn = "Y";
                    }

                    // DepKind - C: BC카드, H: 현대카드
                    if ("C".equals(account.getDepKind())) {
                        bcCardYn = "Y";
                    }

                } else if ("5".equals(account.getAcctType())) { // 현대카드
                    // DepKind - C: BC카드, H: 현대카드
                    if ("H".equals(account.getDepKind())) {
                        hdCardYn = "Y";
                    }
                }

                if (account.getDrawAcctNum().length() > 8) {
                    if ("1".equals(account.getDrawYn()) && "1".equals(account.getAcctType())) {
                        paymentAccountList.add(mapper.toPaymentAccountInfo(account));
                    }

                    if ("1".equals(account.getAcctType())) {
                        demandDepositAccountList.add(mapper.toDemandDepositAccountInfo(account));
                    }
                }

                sessionManager.setGlobalValue("HDCardYN", hdCardYn);
                sessionManager.setLoginValue("HDCardYN", hdCardYn);

                sessionManager.setGlobalValue("BCCardYN", bcCardYn);
                sessionManager.setLoginValue("BCCardYN", bcCardYn);

                if (helper.isTrustAccount(account.getAcctType(), account.getDrawAcctNum())) {
                    trustCodeList.add(account.getFundCode());
                }

                allAccountList.add(account);
            }
        }

        // 가족카드여부 로그인 세션에 넣어줌.
        sessionManager.setLoginValue("UsingFamilyCardYN", usingFamilyCardYn);

        // 신탁/펀드 계좌정보 설정
        setTrustAndFundAccountList(allAccountList, trustCodeList, fundCodeList);

        // 대표계좌설정
        setRepresentAccountList(userId, allAccountList, demandDepositAccountList, paymentAccountList);

        // 전계좌목록 세션저장
        sessionManager.setLoginValue(AccountSessionType.ALL.getSessionKey(), allAccountList);

        // 출금계좌목록 세션저장
        sessionManager.setLoginValue(AccountSessionType.PAYMENT.getSessionKey(), paymentAccountList);

        // 요구불계좌목록 세션저장
        sessionManager.setLoginValue(AccountSessionType.DEMAND_DEPOSIT.getSessionKey(), demandDepositAccountList);

        return allAccountList;
    }

    @ComponentOperation(name = "보유계좌 목록조회[MA3CMMBIZ001_115S]")
    public ListAccountHeldInfo getListAccountHeld(CbIbk01H86600Req h866Req) {
        ListAccountHeldInfo listAccountHeld = new ListAccountHeldInfo();

        log.debug("####################### 보유계좌조회 [MA3CMMBIZ001_115S] START #######################");

        String YISMSYN = StringUtils.defaultIfEmpty(h866Req.getYISMSYN(), "");
        String YIHANDO = StringUtils.defaultIfEmpty(h866Req.getYIHANDO(), "");
        String YIOIHWA = StringUtils.defaultIfEmpty(h866Req.getYIOIHWA(), "");
        String YIGIBONCH = StringUtils.defaultIfEmpty(h866Req.getYIGIBONCH(), "");

        // 공통부 세팅
        OltpRequestOptions hostCfg = hostClient.getOltpRequestOptions("CB_IBK01_H866");

        hostCfg.setImsTranCd("TI1IBK01");
        hostCfg.setInClassCd("H866");
        hostCfg.setSvcCd("866");
        hostCfg.setVanTp("56");
        hostCfg.setCaptureSystem("OLTP");

        String userID = StringUtils.defaultIfEmpty(h866Req.getUserID(), "");
        String tspwd = h866Req.getTSPassword();
        String jmno = h866Req.getYIJMNO();

        if (StringUtils.isEmpty(userID)) {
            userID = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("UserID"),
                    sessionManager.getGlobalValue("uid", String.class));
        }

        if (StringUtils.isEmpty(tspwd)) {
            tspwd = SessionUtils.getSessionValue("TSPassword");
        }

        if (StringUtils.isEmpty(jmno)) {
            jmno = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"),
                    sessionManager.getGlobalValue("cid", String.class));
        }

        CbIbk01H86600Req sendData = new CbIbk01H86600Req();

        sendData.setUserID(userID);
        sendData.setTSPassword(tspwd);
        sendData.setYIJMNO(jmno);

        if (StringUtils.isNotEmpty(YISMSYN)) {
            sendData.setYISMSYN(StringUtils.defaultIfEmpty(h866Req.getYISMSYN(), ""));
        }

        if (StringUtils.isNotEmpty(YIHANDO)) {
            sendData.setYIHANDO(StringUtils.defaultIfEmpty(h866Req.getYIHANDO(), ""));
        }

        if (StringUtils.isNotEmpty(YIOIHWA)) {
            sendData.setYIOIHWA(StringUtils.defaultIfEmpty(h866Req.getYIOIHWA(), ""));
        }

        // 이용자번호 베이스로 조회 시 플래그값 세팅 및 주민번호 세팅X
        if (StringUtils.isNotEmpty(YIGIBONCH) && "Y".equals(YIGIBONCH)) {
            sendData.setYIGIBONCH(YIGIBONCH);
            sendData.setYIJMNO("");
        }

        OltpResponse<CbIbk01H86600Res> hostResponse = hostClient.sendOltp(hostCfg, sendData,
                CbIbk01H86600Res.class);

        CbIbk01H86600Res output = hostResponse.getResponse();

        listAccountHeld = listAccountHeldMapper.toListAccountHeldInfo(output);

        List<yoMyInfRecList> yomyInfoRecList = new ArrayList<>();

        yomyInfoRecList = listAccountHeld.getYoMyInfRec();

        if (yomyInfoRecList != null) {
            for (yoMyInfRecList rec : yomyInfoRecList) {
                String kwamok = rec.getYoMyGj().substring(3, 5);
                String assort = rec.getYoZong();

                rec.setYoZongCode(kwamok + assort);
                rec.setYoMyGjNm(PRCSharedUtils.getAccountName("", kwamok, assort));
                rec.setYoMyGjFmt(FormatUtils.getFrmAcct(rec.getYoMyGj()));
            }
        }

        return listAccountHeld;
    }

    /**
     * 신탁/펀드계좌 정보설정
     * 
     * @param allAccountList 전계좌목록
     * @param trustCodeList  신탁코드목록
     * @param fundCodeList   펀드코드목록
     */
    private void setTrustAndFundAccountList(List<AllAccountInfoSession> allAccountList, List<String> trustCodeList,
            List<String> fundCodeList) {
        if (fundCodeList.size() > 0 || trustCodeList.size() > 0) {

            List<SpcfcTrustPrdctResult> spcfcTrustPrdctResult = new ArrayList<SpcfcTrustPrdctResult>();
            List<SaleFundInfoResult> saleFundInfoList = new ArrayList<SaleFundInfoResult>();

            if (trustCodeList.size() > 0) {
                spcfcTrustPrdctResult = spcfcMoneyTrustIsaPrdctDao.selectSpcfcTrustPrdct(trustCodeList);
            }

            if (fundCodeList.size() > 0) {
                saleFundInfoList = saleFundInfoDao.selectSaleFundInfoList(fundCodeList);
            }

            for (AllAccountInfoSession account : allAccountList) {
                if (helper.isTrustAccount(account.getAcctType(), account.getDrawAcctNum())) {
                    if (spcfcTrustPrdctResult != null) {
                        for (SpcfcTrustPrdctResult spcfcTrustPrdct : spcfcTrustPrdctResult) {
                            if (account.getFundCode().equals(spcfcTrustPrdct.getPrdctCd())) {
                                account.setDrawAcctTrustName(spcfcTrustPrdct.getPrdctNm());
                                break;
                            }
                        }
                    }

                } else if (helper.isFundAccount(account.getAcctType())) {
                    if (saleFundInfoList != null) {
                        for (SaleFundInfoResult saleFundInfo : saleFundInfoList) {
                            if (account.getFundCode().equals(saleFundInfo.getSaleFundCd())) {
                                account.setDrawAcctName(saleFundInfo.getFundKrnNm());
                                account.setDrawAcctEngName(saleFundInfo.getFundEngNm());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 대표계좌설정
     * 
     * @param userId                   사용자ID
     * @param allAccountList           전계좌목록
     * @param demandDepositAccountList 요구불계좌목록
     * @param paymentAccountList       출금계좌목록
     */
    private void setRepresentAccountList(String userId, List<AllAccountInfoSession> allAccountList,
            List<DemandDepositAccountInfoSession> demandDepositAccountList,
            List<PaymentAccountInfoSession> paymentAccountList) {

        List<RepresentAccountInfoSession> representAccountList = new ArrayList<RepresentAccountInfoSession>();

        RepresentAccountResult representAccountResult = ma3RprsntAcctMgtDao.selectRepresentAccount(userId);

        if (representAccountResult != null) {
            // 요구불 계좌 목록과 비교 하여 같은 계좌의 목록을 세션에 저장
            // 10,20,30 계좌만 등록 대상
            // 대표계좌 설정은 3개 기준임

            if (!CollectionUtils.isEmpty(demandDepositAccountList)) {
                for (DemandDepositAccountInfoSession demandDepositAccount : demandDepositAccountList) {
                    String acctNum = StringUtils.nvl(demandDepositAccount.getDrawAcctNum(), "00000000000000");
                    String kwamok = acctNum.substring(3, 5);
                    if ("10".equals(kwamok) || "20".equals(kwamok) || "30".equals(kwamok)) {

                        for (int i = 1; i < 4; i++) {
                            String myRprsntAcct = ReflectionUtils.getFieldValue(representAccountResult,
                                    "MyRprsntAcct" + i);

                            if (acctNum.equals(myRprsntAcct)) {
                                RepresentAccountInfoSession representAccountInfo = new RepresentAccountInfoSession();

                                representAccountInfo.setDrawAcctNum(demandDepositAccount.getDrawAcctNum());
                                representAccountInfo.setDrawAcctName(demandDepositAccount.getDrawAcctName());
                                representAccountInfo.setBalance(demandDepositAccount.getBalance());
                                representAccountInfo.setBalSign(demandDepositAccount.getBalSign());
                                representAccountInfo.setNumber(1);

                                if (demandDepositAccount.getUmbrellaGroup() != null) {
                                    representAccountInfo.setUmbrellaGroup(demandDepositAccount.getUmbrellaGroup());
                                }
                                if (demandDepositAccount.getUmbrellaTransferYN() != null) {
                                    representAccountInfo
                                            .setUmbrellaTransferYN(demandDepositAccount.getUmbrellaTransferYN()); // 한도중지제한
                                }

                                representAccountList.add(representAccountInfo);
                            }
                        }
                    }
                }

                representAccountList.sort(Comparator.comparingInt(RepresentAccountInfoSession::getNumber));
            }

        } else {

            if (!CollectionUtils.isEmpty(paymentAccountList)) {

                PaymentAccountInfoSession paymentAccountInfo = paymentAccountList.get(0);

                RepresentAccountInfoSession representAccountInfo = new RepresentAccountInfoSession();

                representAccountInfo.setDrawAcctNum(paymentAccountInfo.getDrawAcctNum());
                representAccountInfo.setDrawAcctName(paymentAccountInfo.getDrawAcctName());
                representAccountInfo.setBalance(paymentAccountInfo.getBalance());
                representAccountInfo.setBalSign(paymentAccountInfo.getBalSign());
                representAccountInfo.setNumber(1);

                representAccountList.add(representAccountInfo);
            } else {
                if (!CollectionUtils.isEmpty(allAccountList)) {
                    for (AllAccountInfoSession account : allAccountList) {
                        String kwamok = account.getDrawAcctNum().substring(3, 5);
                        if ("10|20|30".contains(kwamok)) {
                            RepresentAccountInfoSession representAccountInfo = new RepresentAccountInfoSession();

                            representAccountInfo.setDrawAcctNum(account.getDrawAcctNum());
                            representAccountInfo.setDrawAcctName(account.getDrawAcctName());
                            representAccountInfo.setBalance(account.getBalance());
                            representAccountInfo.setBalSign(account.getBalSign());
                            representAccountInfo.setNumber(1);

                            representAccountList.add(representAccountInfo);
                            break;
                        }

                    }
                }

            }
        }

        sessionManager.setLoginValue(AccountSessionType.REPRESENT.getSessionKey(), representAccountList);
    }

    /**
     * 전계좌 리스트 ( H039 금액 Refrash )
     */
    private List<AllAccountInfoSession> getSimpleAllAccountList() {
        List<AllAccountInfoSession> allAccountSessionList = helper.getAllAcountSessionList();
        List<NewAccountInfoSession> newAccountSessionList = helper.getNewAccountSessionList();

        if (!CollectionUtils.isEmpty(newAccountSessionList)) {

            for (NewAccountInfoSession newAccountSession : newAccountSessionList) {

                String drawKind = newAccountSession.getNEW_ACCOUNT_NUM().substring(3, 5);
                String acctType = drawKind.equals("73") ? "3" : drawKind.equals("61") ? "2" : "1";

                /*
                 * H039NewAccountYN = "Y"
                 * 신규로 생성되어 add 된 경우 H035 데이터가 없음
                 * > 재로그인하지않으면 홈화면 스크립트에서 substring 등 로직에 오류 발생할 수 있음
                 * > 신규로 만들어진 계좌인경우 substring 등 오류 유발할 수 있는 스크립트 호출하지않도록 플래그 추가
                 */

                AllAccountInfoSession allAccountInfo = new AllAccountInfoSession();

                allAccountInfo.setDrawAcctNum(newAccountSession.getNEW_ACCOUNT_NUM());
                allAccountInfo.setAcctType(acctType);
                allAccountInfo.setBalance(new BigDecimal(0));
                allAccountInfo.setH039NewAccountYN("Y");

                allAccountSessionList.add(allAccountInfo);
            }
        }

        List<YOGJARR> compareYogjarrList = new ArrayList<YOGJARR>();

        int maxCnt = 100;
        int recordIdx = 0;
        BigDecimal num1 = new BigDecimal(allAccountSessionList.size());
        BigDecimal num2 = new BigDecimal(maxCnt);
        BigDecimal calResult = num1.divide(num2, 0, RoundingMode.UP);

        int loopCnt = calResult.intValue();

        for (int i = 0; i < loopCnt; i++) {

            int subLoopCnt = 0;

            if (allAccountSessionList.size() % maxCnt == 0) {
                subLoopCnt = maxCnt;
            } else {
                subLoopCnt = (i == (loopCnt - 1)) ? allAccountSessionList.size() % maxCnt : maxCnt;
            }

            OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK03_H039");
            // 필수 공통부 설정
            hostCfg.setImsTranCd("TI1IBK03");
            hostCfg.setInClassCd("H039");
            hostCfg.setSvcCd("039");

            CbIbk03H03900Req inputDto = new CbIbk03H03900Req();

            inputDto.setYIMSSU(subLoopCnt);

            for (int j = 0; j < subLoopCnt; j++) {
                AllAccountInfoSession account = allAccountSessionList.get(recordIdx);

                try {

                    ReflectionUtils.setFieldValue(inputDto, "YITONM" + j,
                            StringUtils.defaultIfEmpty(account.getCurcy(), "KRW"));
                    ReflectionUtils.setFieldValue(inputDto, "YIGJNO" + j, account.getDrawAcctNum());
                    ReflectionUtils.setFieldValue(inputDto, "BalanceSign" + j, account.getBalSign());
                    ReflectionUtils.setFieldValue(inputDto, "YIBALANCE" + j, account.getBalance());

                    if (account.getDrawAcctNum().substring(3, 5).equals("61")) {
                        ReflectionUtils.setFieldValue(inputDto, "YIBALANCE" + j, account.getEstimateAmount());
                    }

                    ReflectionUtils.setFieldValue(inputDto, "YIKIND" + j, account.getAcctType());

                } catch (Exception e) {
                    throw new PRCServiceException("", "");
                }

                recordIdx++;
            }

            log.debug("host inputDto dto : {}", inputDto);

            OltpResponse<CbIbk03H03900Res> hostResponse = this.hostClient.sendOltp(hostCfg, inputDto,
                    CbIbk03H03900Res.class);

            CbIbk03H03900Res outputDto = hostResponse.getResponse();

            List<YOGJARR> yogjarrList = outputDto.getYOGJARR();

            if (yogjarrList != null) {
                for (YOGJARR yogjarr : yogjarrList) {

                    String refreshAcctNum = yogjarr.getYOGJNO(); // 계좌
                    String refreshBalance = yogjarr.getYOBALANCE(); // 최초원장잔액
                    String refreshAfterBalance = yogjarr.getYOBALANCE2(); // 변경원장잔액
                    String refreshAcctRemoveYn = yogjarr.getYOHJYB(); // 계좌해지여부 해지 : Y
                    boolean refreshAmountFlag = refreshBalance.equals(refreshAfterBalance) ? false : true;

                    if (refreshAmountFlag) {
                        compareYogjarrList.add(yogjarr);
                    }

                    if ("Y".equals(refreshAcctRemoveYn)) {
                        for (AllAccountInfoSession account : allAccountSessionList) {
                            if (refreshAcctNum.equals(account.getDrawAcctNum())) {
                                allAccountSessionList.remove(account);
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (YOGJARR compareYogjarr : compareYogjarrList) {
            String refreshAcctNum = compareYogjarr.getYOGJNO();
            String refreshAfterBalSign = compareYogjarr.getYOBALANCE2Sign(); // 변경부호
            String refreshAfterBalance = compareYogjarr.getYOBALANCE2();
            String refreshAfterKind = compareYogjarr.getYOKIND();

            for (AllAccountInfoSession account : allAccountSessionList) {
                if (refreshAfterKind.equals(account.getAcctType()) && refreshAcctNum.equals(account.getDrawAcctNum())) {
                    if ("2".equals(refreshAfterKind)) {
                        account.setEstimateAmountSign(refreshAfterBalSign);
                        account.setEstimateAmount(new BigDecimal(refreshAfterBalance));
                    } else {
                        account.setBalSign(refreshAfterBalSign);
                        account.setBalance(new BigDecimal(refreshAfterBalance));
                    }
                }
            }
        }

        if (newAccountSessionList != null) {
            for (NewAccountInfoSession newAccount : newAccountSessionList) {
                String newAcctName = newAccount.getNEW_ACCOUNT_NAME();
                String newAcctNum = newAccount.getNEW_ACCOUNT_NUM();
                String newAcctCurcy = newAccount.getNEW_ACCOUNT_CURCY();

                for (AllAccountInfoSession account : allAccountSessionList) {
                    String acctNum = account.getDrawAcctNum();

                    if (acctNum.equals(newAcctNum)) {
                        account.setDrawAcctName(newAcctName);
                        account.setCurcy(StringUtils.defaultIfEmpty(newAcctCurcy, "KRW"));
                        break;
                    }

                }
            }
        }

        sessionManager.setLoginValue(AccountSessionType.ALL.getSessionKey(), allAccountSessionList);
        sessionManager.removeLoginValue(AccountSessionType.NEW.getSessionKey());

        return allAccountSessionList;

    }

    /**
     * 대출 계좌 초기화
     */
    private List<LoanAccountInfoSession> resetLoanAccount() {

        Locale locale = LocaleContextHolder.getLocale();

        CbIbk01H03300Res res = helper.sendHostCbIbk01H033();

        List<LoanAccountInfoSession> loanAccountSessionList = mapper.toLoanAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(loanAccountSessionList)) {
            for (LoanAccountInfoSession loanAccountSession : loanAccountSessionList) {
                String kwamok = loanAccountSession.getDrawAcctNum().substring(3, 5);

                if (StringUtils.isNotBlank(loanAccountSession.getDrawAcctNum())) {
                    if (!kwamok.startsWith("7")) {
                        // 7로 시작하지 않은 경우에는 한도대출(마이너스대출) Set
                        if (Locale.KOREA.equals(locale)) {
                            loanAccountSession.setDrawAcctName(" 한도대출(마이너스대출)");
                        } else {
                            loanAccountSession.setDrawAcctName(" Credit Line");
                        }
                    } else {
                        String addInfoInqFlag = StringUtils
                                .defaultString(sessionManager.getLoginValue("addInfoInqFlag", String.class));

                        boolean isAddLoanInfoInq = false;

                        if ("Y".equals(addInfoInqFlag) && "72".equals(kwamok)) {
                            isAddLoanInfoInq = true;
                        }

                        if ("73".equals(kwamok) || isAddLoanInfoInq) {

                            List<AllAccountInfo> allAccountList = this.getAllAccountList();

                            for (AllAccountInfo allAccount : allAccountList) {
                                String allAcctNum = allAccount.getDrawAcctNum();
                                String allKwamok = allAcctNum.substring(3, 5);

                                loanAccountSession.setLoanAcctKmCd(allAccount.getLoanAcctKmCD());
                                loanAccountSession.setLoanKind(allAccount.getLoanKind());

                                if (("73".equals(allKwamok) || isAddLoanInfoInq)
                                        && loanAccountSession.getDrawAcctNum().equals(allAcctNum)) {
                                    loanAccountSession.setDrawAcctName(PRCSharedUtils.getLoanAccountName(kwamok,
                                            loanAccountSession.getAssort(), allAccount.getLoanKind(),
                                            allAccount.getLoanAcctKmCD()));
                                } else {
                                    loanAccountSession.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok,
                                            loanAccountSession.getAssort()));
                                }
                            }
                        } else {
                            loanAccountSession.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok,
                                    loanAccountSession.getAssort()));
                        }

                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.LOAN.getSessionKey(), loanAccountSessionList);
        }

        return loanAccountSessionList;
    }

    /**
     * 펀드 계좌 초기화
     */
    private List<FundAccountInfoSession> resetFundAccount() {

        CbIbk01H03200Res res = helper.sendHostCbIbk01H032(45);

        List<FundAccountInfoSession> fundAccountSessionList = mapper.toFundAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(res.getREC_01())) {

            List<String> fundCodeList = new ArrayList<String>();

            for (FundAccountInfoSession fundAccount : fundAccountSessionList) {
                fundCodeList.add(fundAccount.getFundCode());
            }

            List<SaleFundInfoResult> saleFundInfoList = saleFundInfoDao.selectSaleFundInfoList(fundCodeList);

            for (FundAccountInfoSession fundAccount : fundAccountSessionList) {
                for (SaleFundInfoResult saleFundInfo : saleFundInfoList) {
                    if (fundAccount.getFundCode().equals(saleFundInfo.getSaleFundCd())) {
                        fundAccount.setDrawAcctName(saleFundInfo.getFundKrnNm());
                        fundAccount.setDrawAcctEngName(saleFundInfo.getFundEngNm());
                        break;
                    }
                }

                if (StringUtils.isBlank(fundAccount.getDrawAcctName())) {
                    fundAccount.setDrawAcctName("펀드");
                }
            }

            sessionManager.setLoginValue(AccountSessionType.FUND.getSessionKey(), fundAccountSessionList);

        }

        return fundAccountSessionList;
    }

    /**
     * 예적금 계좌 초기화
     */
    private List<DepositAccountInfoSession> resetDepositAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<DepositAccountInfoSession> depositAccountSessionList = mapper
                .toDepositAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(depositAccountSessionList)) {

            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (DepositAccountInfoSession depositAccount : depositAccountSessionList) {
                String kwamok = depositAccount.getDrawAcctNum().substring(3, 5);
                String assort = depositAccount.getAssort();
                depositAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentAccount : paymentSessionList) {
                        if (depositAccount.getDrawAcctNum().equals(paymentAccount.getDrawAcctNum())) {
                            depositAccount.setDrawYn(paymentAccount.getDrawYn());
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.DEPOSIT.getSessionKey(), depositAccountSessionList);
        }

        return depositAccountSessionList;
    }

    /**
     * 예적금 및 신탁 계좌 초기화
     */
    private List<DepositTrustAccountInfoSession> resetDepositTrustAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<DepositTrustAccountInfoSession> depositTrustAccountSessionList = mapper
                .toDepositTrustAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(depositTrustAccountSessionList)) {

            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (DepositTrustAccountInfoSession depositTrustAccount : depositTrustAccountSessionList) {
                String kwamok = depositTrustAccount.getDrawAcctNum().substring(3, 5);
                String assort = depositTrustAccount.getAssort();
                depositTrustAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentAccount : paymentSessionList) {
                        if (depositTrustAccount.getDrawAcctNum().equals(paymentAccount.getDrawAcctNum())) {
                            depositTrustAccount.setDrawYn(paymentAccount.getDrawYn());
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.DEPOSIT_TRUST.getSessionKey(),
                    depositTrustAccountSessionList);

        }

        return depositTrustAccountSessionList;

    }

    /**
     * 예적금(10,20,30,80,85,88,90) 계좌 초기화
     */
    private List<DepositSavingAccountInfoSession> resetDepositSavingAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<DepositSavingAccountInfoSession> depositSavingAccountSessionList = mapper
                .toDepositSavingAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(depositSavingAccountSessionList)) {

            for (DepositSavingAccountInfoSession depositSavingAccount : depositSavingAccountSessionList) {
                String kwamok = depositSavingAccount.getDrawAcctNum().substring(3, 5);
                String assort = depositSavingAccount.getAssort();
                depositSavingAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));
            }

            sessionManager.setLoginValue(AccountSessionType.DEPOSIT_SAVING.getSessionKey(),
                    depositSavingAccountSessionList);

        }

        return depositSavingAccountSessionList;

    }

    /**
     * 출금 계좌 초기화
     */
    private List<PaymentAccountInfoSession> resetPaymentAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<PaymentAccountInfoSession> paymentAccountSessionList = mapper
                .toPaymentAccountInfoSessionList(res.getREC_01());

        List<PaymentAccountInfoSession> tempPaymentSessionList = new ArrayList<PaymentAccountInfoSession>();

        if (!CollectionUtils.isEmpty(paymentAccountSessionList)) {

            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (PaymentAccountInfoSession paymentAccount : paymentAccountSessionList) {
                String kwamok = paymentAccount.getDrawAcctNum().substring(3, 5);
                String assort = paymentAccount.getAssort();
                paymentAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentSession : paymentSessionList) {
                        if (paymentAccount.getDrawAcctNum().equals(paymentSession.getDrawAcctNum())) {
                            paymentAccount.setDrawYn(paymentSession.getDrawYn());
                            tempPaymentSessionList.add(paymentAccount);
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.PAYMENT.getSessionKey(), tempPaymentSessionList);
        }

        return tempPaymentSessionList;

    }

    /**
     * 출금(10,20,30 계좌만) 계좌 초기화
     */
    private List<PaymentNormalAccountInfoSession> resetPaymentNormalAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<PaymentNormalAccountInfoSession> newPaymentNormalAccountSessionList = new ArrayList<PaymentNormalAccountInfoSession>();
        List<PaymentNormalAccountInfoSession> paymentNormalAccountSessionList = mapper
                .toPaymentNormalAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(paymentNormalAccountSessionList)) {

            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (PaymentNormalAccountInfoSession paymentNormalAccount : paymentNormalAccountSessionList) {
                String kwamok = paymentNormalAccount.getDrawAcctNum().substring(3, 5);
                String assort = paymentNormalAccount.getAssort();
                paymentNormalAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));
                paymentNormalAccount.setDrawBnkCd(PRCSharedConstants.SC_BANK_CODE);
                paymentNormalAccount.setDrawBnkNm(PRCSharedUtils.getScBankName());

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentSession : paymentSessionList) {
                        if (paymentNormalAccount.getDrawAcctNum().equals(paymentSession.getDrawAcctNum())) {
                            paymentNormalAccount.setDrawYn(paymentSession.getDrawYn());
                            newPaymentNormalAccountSessionList.add(paymentNormalAccount);
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.PAYMENT_NORMAL.getSessionKey(),
                    newPaymentNormalAccountSessionList);
        }

        return newPaymentNormalAccountSessionList;
    }

    /**
     * 예금 계좌 초기화
     */
    private List<DemandDepositAccountInfoSession> resetDemandDepositAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<DemandDepositAccountInfoSession> demandDepositAccountSessionList = mapper
                .toDemandDepositAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(demandDepositAccountSessionList)) {
            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (DemandDepositAccountInfoSession demandDepositAccount : demandDepositAccountSessionList) {
                String kwamok = demandDepositAccount.getDrawAcctNum().substring(3, 5);
                String assort = demandDepositAccount.getAssort();
                demandDepositAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentSession : paymentSessionList) {
                        if (demandDepositAccount.getDrawAcctNum().equals(paymentSession.getDrawAcctNum())) {
                            demandDepositAccount.setDrawYn(paymentSession.getDrawYn());
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.DEMAND_DEPOSIT.getSessionKey(),
                    demandDepositAccountSessionList);
        }

        return demandDepositAccountSessionList;
    }

    /**
     * FX 계좌 초기화
     */
    private List<FxAccountInfoSession> resetFxAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<FxAccountInfoSession> fxAccountSessionList = mapper.toFxAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(fxAccountSessionList)) {
            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (FxAccountInfoSession fxAccount : fxAccountSessionList) {
                String kwamok = fxAccount.getDrawAcctNum().substring(3, 5);
                String assort = fxAccount.getAssort();
                fxAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentSession : paymentSessionList) {
                        if (fxAccount.getDrawAcctNum().equals(paymentSession.getDrawAcctNum())) {
                            fxAccount.setDrawYn(paymentSession.getDrawYn());
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.FX.getSessionKey(), fxAccountSessionList);
        }

        return fxAccountSessionList;

    }

    /**
     * 카드 계좌 초기화
     */
    private List<CardAccountInfoSession> resetCardAccount() {

        Locale locale = LocaleContextHolder.getLocale();

        CbIbk03H03400Res res = helper.sendHostCbIbk03H034();

        sessionManager.setLoginValue("AdvanceBankCode", StringUtils.defaultString(res.getYOBANKCD())); // 은행코드
        sessionManager.setLoginValue("AdvanceAccount", res.getYOBCGJNO()); // 계좌번호?
        sessionManager.setLoginValue("AdvanceYN", res.getYOBSBB2()); // 타행선결제구분
        sessionManager.setLoginValue("PaymentDay", res.getYOCGIL()); // 대금결제일

        List<CardAccountInfoSession> cardAccountSessionList = mapper.toCardAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(cardAccountSessionList)) {

            String usingFamilyCardYn = "N";

            for (CardAccountInfoSession cardAccount : cardAccountSessionList) {
                if (!Locale.KOREA.equals(locale)) {
                    cardAccount.setDrawAcctName(
                            PRCSharedUtils.getCardNameByKind(cardAccount.getCardType()));
                }

                if ("4".equals(cardAccount.getFamilyType())) {
                    usingFamilyCardYn = "Y";
                }
            }

            sessionManager.setLoginValue("UsingFamilyCardYN", usingFamilyCardYn); // 가족카드여부 로그인 세션에 넣어줌.
            sessionManager.setLoginValue(AccountSessionType.CARD.getSessionKey(), cardAccountSessionList);

        }

        return cardAccountSessionList;
    }

    /**
     * MTS 계좌 초기화
     */
    private List<MtsAccountInfoSession> resetMtsAccount() {
        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<MtsAccountInfoSession> mtsAccountSessionList = mapper.toMtsAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(mtsAccountSessionList)) {

            CbIbk01H44K01Res h44kRes = helper.sendHostCbIbk01H44K01();

            for (MtsAccountInfoSession mtsAccount : mtsAccountSessionList) {
                for (YO_JRPGSU yoJrpgsu : h44kRes.getTR44K01()) {
                    if (mtsAccount.getDrawAcctNum().equals(yoJrpgsu.getYO_GJNO())) {
                        mtsAccount.setDrawAcctName(yoJrpgsu.getYO_JSNM()); // MTS 자산명
                        mtsAccount.setMtsAssetName(yoJrpgsu.getYO_JSNM()); // MTS 자산명
                        mtsAccount.setMtsAssetNo(yoJrpgsu.getYO_GJNO()); // MTS 자산계좌번호
                        mtsAccount.setMtsNo(yoJrpgsu.getYO_JSNO()); // MTS 자산번호
                        break;
                    }
                }

            }

            sessionManager.setLoginValue(AccountSessionType.MTS.getSessionKey(), mtsAccountSessionList);
        }

        return mtsAccountSessionList;
    }

    /**
     * ISA 계좌 초기화
     */
    private List<IsaAccountInfoSession> resetIsaAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<IsaAccountInfoSession> isaAccountSessionList = mapper.toIsaAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(isaAccountSessionList)) {
            sessionManager.setLoginValue(AccountSessionType.ISA.getSessionKey(), isaAccountSessionList);
        }

        return isaAccountSessionList;
    }

    /**
     * 예적금담보대출 담보계좌 초기화
     */
    private List<MortgageLoanAccountInfoSession> resetMortgageLoanAccount() {
        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<MortgageLoanAccountInfoSession> mortgageLoanAccountSessionList = mapper
                .toMortgageLoanAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(mortgageLoanAccountSessionList)) {
            for (MortgageLoanAccountInfoSession mortgageLoanAccount : mortgageLoanAccountSessionList) {
                String kwamok = mortgageLoanAccount.getDrawAcctNum().substring(3, 5);
                String assort = mortgageLoanAccount.getAssort();
                mortgageLoanAccount.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                sessionManager.setLoginValue(AccountSessionType.MORTGAGE_LOAN.getSessionKey(),
                        mortgageLoanAccountSessionList);
            }
        }

        return mortgageLoanAccountSessionList;
    }

    /**
     * 대표 계좌 초기화
     */
    private List<RepresentAccountInfoSession> resetRepresentAccount() {

        List<AllAccountInfo> allAccountList = this.getAllAccountList();
        List<RepresentAccountInfoSession> representAccountSessionList = helper.getRepresentAccountSessionList();
        List<RepresentAccountInfoSession> responseList = new ArrayList<RepresentAccountInfoSession>();

        if (!CollectionUtils.isEmpty(allAccountList) && !CollectionUtils.isEmpty(representAccountSessionList)) {
            for (RepresentAccountInfoSession representAccount : representAccountSessionList) {
                for (AllAccountInfo allAccount : allAccountList) {
                    if (representAccount.getDrawAcctNum().equals(allAccount.getDrawAcctNum())) {
                        representAccount.setBalSign(allAccount.getBalSign());
                        representAccount.setBalance(allAccount.getBalance());

                        if (StringUtils.isNotBlank(allAccount.getUmbrellaGroup())
                                && StringUtils.isNotBlank(allAccount.getUmbrellaAutoTransferYn())) {
                            representAccount.setUmbrellaGroup(allAccount.getUmbrellaGroup());
                            representAccount.setUmbrellaTransferYN(allAccount.getUmbrellaTransferYn());
                        }

                        responseList.add(representAccount);
                        break;
                    }
                }
            }
        }

        return responseList;
    }

    /**
     * 연금신탁 계좌 초기화
     */
    private List<PensionTrustAccountInfoSession> resetPensionTrustAccount() {

        CbIbk01H03100Res res = helper.sendHostCbIbk01H031();

        List<PensionTrustAccountInfoSession> pensionTrustAccountSessionList = mapper
                .toPensionTrustAccountInfoSessionList(res.getREC_01());

        if (!CollectionUtils.isEmpty(pensionTrustAccountSessionList)) {

            List<PaymentAccountInfoSession> paymentSessionList = helper.getPaymentAccountSessionList();

            for (PensionTrustAccountInfoSession pensionTrustAccountSession : pensionTrustAccountSessionList) {
                String kwamok = pensionTrustAccountSession.getDrawAcctNum().substring(3, 5);
                String assort = pensionTrustAccountSession.getAssort();
                pensionTrustAccountSession.setDrawAcctName(PRCSharedUtils.getAccountName(kwamok, assort));

                if (!CollectionUtils.isEmpty(paymentSessionList)) {
                    for (PaymentAccountInfoSession paymentSession : paymentSessionList) {
                        if (pensionTrustAccountSession.getDrawAcctNum().equals(paymentSession.getDrawAcctNum())) {
                            pensionTrustAccountSession.setDrawYn(paymentSession.getDrawYn());
                            break;
                        }
                    }
                }
            }

            sessionManager.setLoginValue(AccountSessionType.PENSION_TRUST.getSessionKey(),
                    pensionTrustAccountSessionList);
        }

        return pensionTrustAccountSessionList;
    }

    private void initObsUserInfo() {

        OpenBankUserInfoParameter paramMap = new OpenBankUserInfoParameter();
        paramMap.setUserCifNo(sessionManager.getLoginValue("UserCifNo",
                String.class));
        paramMap.setUserId(sessionManager.getLoginValue("UserID", String.class));
        paramMap.setIbCloseYn("N"); // 인터넷뱅킹 해지여부 Y:해지상태, N:정상상태
        log.debug("★★★★ initObsUserInfo > param : " + paramMap.toString());

        // 오픈뱅킹사용자여부조회
        OpenBankUserInfoResult opaUserMap = ma3OpenBankUserMgtDao.selectOpenBankUser(paramMap);
        log.debug("★★★★ initObsUserInfo > result : " + opaUserMap);

        sessionManager.setLoginValue("OBS_USER_SEQ_NO", ""); // session 값 초기화
        sessionManager.setLoginValue("OBS_IB_CLOSE_YN", ""); // session 값 초기화
        sessionManager.setLoginValue("OBS_UPDATETIME", ""); // session 값 초기화
        sessionManager.setLoginValue("OBS_UPDATETIME", ""); // session 값 초기화

        if (opaUserMap != null) {
            log.debug("★★★★ initObsUserInfo > result.toString() : " + opaUserMap.toString());
            sessionManager.setLoginValue("OBS_USER_SEQ_NO", opaUserMap.getUserSeqNo()); // KFTC사용자일련번호.
            sessionManager.setLoginValue("OBS_IB_CLOSE_YN", opaUserMap.getIbCloseYn()); // 인터넷뱅킹 해지여부 Y:해지상태, N:정상상태
        }
    }

    private List<OpenbankingAccountInfo> getObsAllAccountList(String ApiSkipFlag) throws PRCServiceException {
        // 마이데이터 은행 CI 조회
        ListMydataCIFromDBResponse mdtCIData = listMydataCIFromDB();
        String resultNullYN = mdtCIData.getResultNullYN();
        String pmsImgRootUrl = mdtCIData.getPmsImgRootUrl();
        List<MyDataCIInfoResult> mdtCIList = mdtCIData.getMdtCIList();

        List<OpenbankingAccountInfo> resultRecordList = new ArrayList<>();

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"), Locale.KOREA);
        calendar.roll(Calendar.YEAR, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int remitAgrDate = Integer.parseInt(sdf.format(calendar.getTime()));

        OpenBankAccountMgtListParameter paramMap = new OpenBankAccountMgtListParameter();
        paramMap.setUserCifNo(sessionManager.getLoginValue("UserCifNo",
                String.class));
        paramMap.setUserId(sessionManager.getLoginValue("UserID", String.class));

        paramMap.setIbCloseYn("N"); // 인터넷뱅킹 해지여부 Y:해지상태, N:정상상태
        log.debug("★★★★ getObsAllAccountList > param : " + paramMap.toString());

        // 오픈뱅킹 타행계좌목록
        List<OpenBankAccountMgtListResult> openBankList = ma3OpenBankUserMgtDao.selectOpenBankAcctMgtList(paramMap);
        log.debug("★★★★ getObsAllAccountList > result : " + openBankList);

        // 재동의 일자 확인을 위한 등록계좌조회 API
        ListRegisteredOpenBankAccountFromAPIResponse apiObsAllAcct = new ListRegisteredOpenBankAccountFromAPIResponse();
        String apiSkipFlag = StringUtils.defaultIfEmpty(ApiSkipFlag, "");

        // 메인, 조회 화면은 계좌선택 시에만 타도록 수정
        if (!"Y".equals(apiSkipFlag)) {
            apiObsAllAcct = listRegisteredOpenBankAccountFromAPI();
        }

        if (openBankList != null && openBankList.size() > 0) {
            log.debug("★★★★ getObsAllAccountList > result.toString() : " + openBankList.toString());
            for (int i = 0; i < openBankList.size(); i++) {
                OpenbankingAccountInfo record = new OpenbankingAccountInfo();
                OpenBankAccountMgtListResult item = openBankList.get(i);

                String BANK_CD = StringUtils.defaultIfEmpty((String) item.getBankCd(), "").trim();
                String BKCODE_OBS = StringUtils.defaultIfEmpty(CodeUtils.getCodeValue("BKCODE_OBS", BANK_CD), "")
                        .trim();
                String[] BANK_CD_INFO = ("".equals(BKCODE_OBS)) ? new String[2] : BKCODE_OBS.split(";;");

                record.setBankCd(BANK_CD);
                record.setBankNm(StringUtils.defaultIfEmpty(BANK_CD_INFO[0], "").trim()); // 은행명
                record.setBankImg(StringUtils.defaultIfEmpty(BANK_CD_INFO[1], "").trim()); // 은행이미지
                // 마이데이터 기관 조회 결과가 있는 경우
                if ("N".equals(resultNullYN) && mdtCIList != null) {
                    for (int cnt = 0; cnt < mdtCIList.size(); cnt++) {
                        MyDataCIInfoResult resultMap = mdtCIList.get(cnt);

                        String OPEN_BNK_CD = resultMap.getOpenBnkCd() != null ? (String) resultMap.getOpenBnkCd() : "";
                        String ORG_IMG_NAME = resultMap.getOrgImgName() != null ? (String) resultMap.getOrgImgName()
                                : "";
                        OPEN_BNK_CD = OPEN_BNK_CD.trim();
                        ORG_IMG_NAME = ORG_IMG_NAME.trim();

                        if (!"".equals(OPEN_BNK_CD) && BANK_CD.equals(OPEN_BNK_CD) && !"".equals(ORG_IMG_NAME)) {
                            record.setBankImg(pmsImgRootUrl + ORG_IMG_NAME); // 은행이미지
                            break;
                        }
                    }
                }

                String AGR_STS = StringUtils.defaultIfEmpty((String) item.getAgrSts(), "").trim();
                String AGR_DATE = StringUtils.defaultIfEmpty((String) item.getAgrDate(), "").trim();
                int intAGR_DATE = Integer.parseInt(AGR_DATE.substring(0, 8));
                // remitAgrDate = 20191106;//##SK2.삭제## 테스트에서 11월06일 이후 데이터는 재동의 받도록 설정.

                log.debug("★ remitAgrDate : " + remitAgrDate + " || intAGR_DATE : " + intAGR_DATE + " ("
                        + (intAGR_DATE <= remitAgrDate) + ")");
                if (intAGR_DATE <= remitAgrDate && "Y".equals(AGR_STS)) {
                    record.setAgrSts("R"); // 재동의 대상 처리
                } else {
                    record.setAgrSts(StringUtils.defaultIfEmpty(item.getAgrSts(), "").trim()); // 동의상태
                }
                record.setFintechUseNum(StringUtils.defaultIfEmpty(item.getFintechUseNum(), "").trim()); // 핀테크이용번호
                record.setPayerNum(StringUtils.defaultIfEmpty(item.getPayerNum(), "").trim()); // 납부자번호
                record.setFcode(StringUtils.defaultIfEmpty(item.getFcode(), "").trim()); // 기관코드
                record.setAcctNo(StringUtils.defaultIfEmpty(item.getAcctNo(), "").trim()); // 계좌번호
                record.setAcctNm(StringUtils.defaultIfEmpty(item.getAcctNm(), "입출금통장").trim()); // 계좌명
                record.setAcctComment(StringUtils.defaultIfEmpty(item.getAcctComment(), "").trim()); // 계좌별명
                record.setUpdateTime(StringUtils.defaultIfEmpty(item.getUpdateTime(), "").trim()); // 변경등록시간
                record.setRegistTime(StringUtils.defaultIfEmpty(item.getRegistTime(), "").trim()); // 최초등록시간
                // 메인, 조회 화면은 계좌선택 시에만 타도록 수정
                if (!"Y".equals(apiSkipFlag)) {
                    // 제3자동의 만료일 확인을 위해 등록계좌 조회 API 추가
                    try {
                        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
                        String acctJsonString = apiObsAllAcct.getOpen_regAcctList();

                        String recAcctNo = StringUtils.defaultIfEmpty(item.getAcctNo(), "").trim();

                        if (acctJsonString != null && !"".equals(acctJsonString)) {
                            JSONArray acctJsonArr = (JSONArray) parser.parse(acctJsonString);
                            for (Iterator iterator = acctJsonArr.iterator(); iterator.hasNext();) {
                                JSONObject data = (JSONObject) iterator.next();
                                String apiAcctNo = (String) data.get("account_num");
                                String agreeDate = (String) data.get("inquiry_agree_dtime");

                                // API와 DB간에 등록계좌 체크
                                if (apiAcctNo.equals(recAcctNo)) {
                                    // 일수로 5년 계산
                                    String agreeExpireDateStr = DateUtils.getDate(agreeDate, "yyyyMMdd", 'D', 1825);

                                    // 현재 날짜와 만료 날짜 비교 일수
                                    // Date agreeExpireDate = DateUtils.getStringToDate(agreeExpireDateStr);
                                    // Date currentDate = DateUtils.getStringToDate(DateUtils.getCurrentDate());
                                    long expireDayCount = DateUtils.getDayBetween(DateUtils.getCurrentDate(),
                                            agreeExpireDateStr);

                                    if (expireDayCount > 0 && expireDayCount <= 30) {
                                        record.setAgreeExpireFlag("Y");
                                        record.setAgreeExpireDate(agreeExpireDateStr);
                                    }
                                    break;
                                }
                            }
                        }
                        // 동의 만료 API 조회 여부
                        sessionManager.setLoginValue("obsAgreeApiCheck", "Y");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                resultRecordList.add(record);
            }
        }
        log.debug("★★★★ record Last : " + resultRecordList.toString());
        List<OpenbankingAccountInfoSession> openbankingAccountListSession = mapper
                .toOpenbankingAccountList(resultRecordList);
        sessionManager.setLoginValue(AccountSessionType.OBS_ALL.getSessionKey(), openbankingAccountListSession); // 세션에

        return resultRecordList;
    }

    // 마이데이터 CI 조회 [ASIS:MA3TRNSTR001_224S]
    private ListMydataCIFromDBResponse listMydataCIFromDB() throws PRCServiceException {

        ListMydataCIFromDBResponse response = new ListMydataCIFromDBResponse();

        String pmsImgRootUrl = PropertiesUtils.getString("PMS_IMG_ROOT");
        String resultNullYN = "Y";

        try {
            List<MyDataCIInfoResult> selectMyDataCIInfoList = new ArrayList<MyDataCIInfoResult>();

            selectMyDataCIInfoList = ma3MydataOrgMgtDao.selectMyDataCIInfo();

            resultNullYN = selectMyDataCIInfoList != null ? "N" : "Y";

            response.setMdtCIList(selectMyDataCIInfoList);
        } catch (PRCServiceException e) {
            resultNullYN = "Y";

            e.printStackTrace();
        }

        response.setPmsImgRootUrl(pmsImgRootUrl);
        response.setResultNullYN(resultNullYN);

        return response;
    }

    // 오픈뱅킹 > 제3자 정보제공 재동의 > 등록 계좌조회 API [ASIS:MA3MSCOBS004_100S]
    private ListRegisteredOpenBankAccountFromAPIResponse listRegisteredOpenBankAccountFromAPI() {
        ListRegisteredOpenBankAccountFromAPIResponse output = new ListRegisteredOpenBankAccountFromAPIResponse();

        String open_regAcctList = sessionManager.getLoginValue("open_regAcctList", String.class);

        // session에 사용자 정보 없을 경우 실행
        log.debug("###### initObsUserInfo 실행 체크.");
        // session에 사용자 정보 없을 경우 실행
        String obsUserSeqNo = sessionManager.getLoginValue("OBS_USER_SEQ_NO", String.class);

        if (StringUtils.defaultIfEmpty(obsUserSeqNo, "").equals("")) {
            initObsUserInfo();
        }

        if (open_regAcctList != null && !"".equals(open_regAcctList)) {
            output.setOpen_regAcctList(open_regAcctList);
        } else {
            obsClient.init();

            String user_seq_no = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("OBS_USER_SEQ_NO",
                    String.class), "");
            JSONObject params = new JSONObject();
            params.put("user_req_no", user_seq_no); // 사용자일련번호
            params.put("include_cancel_yn", "N"); // 해지계좌 포함여부
            params.put("sort_order", "D"); // 정렬 기준 : 조회서비스 동의일시, 출금서비스 동의일시 중 최근 값

            ObsHttpRequestEntity requestEntity = ObsHttpRequestEntityBuilder.builder(KftcObsHelper.ACCOUNT_LIST)
                    .accessToken(obsClient.getAuth().getAccessToken()).bodyParameters(params)
                    .interceptor(KftcObsLogInterceptor.client(SessionUtils.getSessionValue("UserID"),
                            SessionUtils.getSessionValue("UserCifNo"), 0))
                    .build();

            // 잔액API조회 결과
            JSONObject resultJSON = obsClient.execute(requestEntity).throwException().getAsJsonObject();
            String rspCode = StringUtils.defaultIfEmpty((String) resultJSON.get("rsp_code"), "");

            if ("A0000".equals(rspCode)) {
                String resCnt = StringUtils.defaultIfEmpty((String) resultJSON.get("res_cnt"), "");
                if (!"".equals(resCnt) && !"0".equals(resCnt)) {
                    JSONArray reslist = (JSONArray) resultJSON.get("res_list");
                    String resListStr = reslist.toString();

                    sessionManager.setLoginValue("open_regAcctList", resListStr);
                    output.setOpen_regAcctList(open_regAcctList);
                }
            }
        }

        log.debug("#################[MA3MSCOBS004_100S] API param : " + output.toString());
        return output;
    }
}
