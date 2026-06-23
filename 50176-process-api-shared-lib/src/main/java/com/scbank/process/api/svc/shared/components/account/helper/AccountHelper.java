package com.scbank.process.api.svc.shared.components.account.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03100Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03100Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03200Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03200Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03300Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H44K01Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H44K01Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03500Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03500Res;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.account.AccountReboundStrategy;
import com.scbank.process.api.svc.shared.components.account.constant.AccountSessionType;
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
import com.scbank.process.api.svc.shared.integration.HostClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountHelper {

    /**
     * EDMI 통합 클라이언트
     */
    private final HostClient hostClient;

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * 신탁계좌여부
     * 
     * @param acctType
     * @param acctNum
     * @return
     */
    public boolean isTrustAccount(String acctType, String acctNum) {
        if ("1".equals(acctType)) {

            if (StringUtils.isBlank(acctNum)) {
                return true;
            }

            String kwamok = acctNum.substring(3, 5);
            if ("59".equals(kwamok)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 펀드계좌여부
     * 
     * @param acctType
     * @return
     */
    public boolean isFundAccount(String acctType) {
        if ("2".equals(acctType)) {
            return true;
        }

        return false;

    }

    /**
     * 로그인 전문 host 요청
     * 
     * @return
     */
    public CbIbk03H03500Res sendHostTi1ibk03H035() {
        // 등록점번호
        String regiBranchNum = StringUtils
                .defaultString(sessionManager.getLoginValue("RegiBranchNum", String.class));
        // 주민번호 구분
        String perNoPart = StringUtils.defaultString(sessionManager.getLoginValue("PerNoPart", String.class));

        OltpRequestOptions hostCfg = this.hostClient
                .getOltpRequestOptions("CB_IBK03_H035");
        // 필수 공통부 설정

        // if ("3".equals(perNoPart)) {
        // hostCfg.setImsTranCd("TI1IBK04");
        // } else {
        // hostCfg.setImsTranCd("TI1IBK03");
        // }

        hostCfg.setImsTranCd("TI1IBK03");

        hostCfg.setInClassCd("H035");
        hostCfg.setSvcCd("035");
        hostCfg.setProcessTp("F");

        if (StringUtils.isNotBlank(regiBranchNum)) {
            hostCfg.setBranchNo(regiBranchNum);
        }

        CbIbk03H03500Req req = new CbIbk03H03500Req();

        req.setUserID(sessionManager.getLoginValue("UserID", String.class));
        req.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));
        req.setInputJumin(sessionManager.getLoginValue("PerBusNo", String.class));
        req.setAccountListCount(25);
        req.setConnectType("3");

        String phoneNo1 = sessionManager.getLoginValue("HPOne", String.class);
        String phoneNo2 = sessionManager.getLoginValue("HPTwo", String.class);
        String phoneNo3 = sessionManager.getLoginValue("HPThree", String.class);
        req.setHPNumber(phoneNo1 + phoneNo2 + phoneNo3);
        req.setYIIPN(IpUtils.getClientIp());
        req.setYIMAC("");

        log.debug("req, {}", req.toString());

        OltpResponse<CbIbk03H03500Res> hostResponse = this.hostClient.sendOltpWithRebound(hostCfg, req,
                CbIbk03H03500Res.class, new AccountReboundStrategy());

        return hostResponse.getResponse();
    }

    /**
     * 전계좌조회 전문 host 요청
     * 
     * @return
     */
    public CbIbk01H03100Res sendHostCbIbk01H031() {
        // 등록점번호
        String regiBranchNum = StringUtils
                .defaultString(sessionManager.getLoginValue("RegiBranchNum", String.class));
        // 주민번호 구분
        String perNoPart = StringUtils.defaultString(sessionManager.getLoginValue("PerNoPart", String.class));

        OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H031"); // CB_{trancode}_{n}
        // 필수 공통부 설정

        // if ("3".equals(perNoPart)) {
        // hostCfg.setImsTranCd("TI1IBK04");
        // } else {
        // hostCfg.setImsTranCd("TI1IBK03");
        // }
        hostCfg.setImsTranCd("TI1IBK01");

        hostCfg.setInClassCd("H031");
        hostCfg.setSvcCd("031");

        if (StringUtils.isNotBlank(regiBranchNum)) {
            hostCfg.setBranchNo(regiBranchNum);
        }

        CbIbk01H03100Req req = new CbIbk01H03100Req();

        req.setUserID(sessionManager.getLoginValue("UserID", String.class));
        req.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));
        req.setPrintCount(25);

        OltpResponse<CbIbk01H03100Res> hostResponse = this.hostClient.sendOltpWithRebound(hostCfg, req,
                CbIbk01H03100Res.class, new AccountReboundStrategy());

        return hostResponse.getResponse();
    }

    /**
     * 펀드계좌조회 전문 host 요청
     * 
     * @return
     */
    public CbIbk01H03200Res sendHostCbIbk01H032() {
        return this.sendHostCbIbk01H032(60);
    }

    public CbIbk01H03200Res sendHostCbIbk01H032(Integer printCount) {
        // 등록점번호
        String regiBranchNum = StringUtils
                .defaultString(sessionManager.getLoginValue("RegiBranchNum", String.class));
        // 주민번호 구분
        String perNoPart = StringUtils.defaultString(sessionManager.getLoginValue("PerNoPart", String.class));

        OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H032");
        // 필수 공통부 설정

        // if ("3".equals(perNoPart)) {
        // hostCfg.setImsTranCd("TI1IBK04");
        // } else {
        // hostCfg.setImsTranCd("TI1IBK03");
        // }
        hostCfg.setImsTranCd("TI1IBK01");

        hostCfg.setInClassCd("H032");
        hostCfg.setSvcCd("032");

        if (StringUtils.isNotBlank(regiBranchNum)) {
            hostCfg.setBranchNo(regiBranchNum);
        }

        CbIbk01H03200Req req = new CbIbk01H03200Req();

        req.setUserID(sessionManager.getLoginValue("UserID", String.class));
        req.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));
        req.setPrintCount(printCount);

        OltpResponse<CbIbk01H03200Res> hostResponse = this.hostClient.sendOltpWithRebound(hostCfg, req,
                CbIbk01H03200Res.class, new AccountReboundStrategy());

        return hostResponse.getResponse();
    }

    /**
     * 대출계좌조회 전문 host 요청
     * 
     * @return
     */
    public CbIbk01H03300Res sendHostCbIbk01H033() {
        // 등록점번호
        String regiBranchNum = StringUtils
                .defaultString(sessionManager.getLoginValue("RegiBranchNum", String.class));
        // 주민번호 구분
        String perNoPart = StringUtils.defaultString(sessionManager.getLoginValue("PerNoPart", String.class));

        OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H033");
        // 필수 공통부 설정
        // if ("3".equals(perNoPart)) {
        // hostCfg.setImsTranCd("TI1IBK04");
        // } else {
        // hostCfg.setImsTranCd("TI1IBK03");
        // }
        hostCfg.setImsTranCd("TI1IBK01");

        hostCfg.setInClassCd("H033");
        hostCfg.setSvcCd("033");

        if (StringUtils.isNotBlank(regiBranchNum)) {
            hostCfg.setBranchNo(regiBranchNum);
        }

        CbIbk01H03300Req req = new CbIbk01H03300Req();

        req.setUserID(sessionManager.getLoginValue("UserID", String.class));
        req.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));
        req.setPrintCount(25);

        OltpResponse<CbIbk01H03300Res> hostResponse = this.hostClient.sendOltpWithRebound(hostCfg, req,
                CbIbk01H03300Res.class, new AccountReboundStrategy());

        return hostResponse.getResponse();
    }

    public CbIbk03H03400Res sendHostCbIbk03H034() {
        // 등록점번호
        String regiBranchNum = StringUtils
                .defaultString(sessionManager.getLoginValue("RegiBranchNum", String.class));
        // 주민번호 구분
        String perNoPart = StringUtils.defaultString(sessionManager.getLoginValue("PerNoPart", String.class));

        OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK03_H034");
        // 필수 공통부 설정

        // if ("3".equals(perNoPart)) {
        // hostCfg.setImsTranCd("TI1IBK04");
        // } else {
        // hostCfg.setImsTranCd("TI1IBK03");
        // }

        hostCfg.setImsTranCd("TI1IBK03");

        hostCfg.setInClassCd("H034");
        hostCfg.setSvcCd("034");

        if (StringUtils.isNotBlank(regiBranchNum)) {
            hostCfg.setBranchNo(regiBranchNum);
        }

        CbIbk03H03400Req req = new CbIbk03H03400Req();

        req.setUserID(sessionManager.getLoginValue("UserID", String.class));
        req.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));
        req.setPrintCount(60);

        OltpResponse<CbIbk03H03400Res> hostResponse = this.hostClient.sendOltpWithRebound(hostCfg, req,
                CbIbk03H03400Res.class, new AccountReboundStrategy());

        return hostResponse.getResponse();
    }

    /**
     * 신탁자산명조회 전문 host 요청
     * 
     * @return
     */
    public CbIbk01H44K01Res sendHostCbIbk01H44K01() {
        OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H44K");

        // 필수 공통부 설정
        hostCfg.setImsTranCd("TI1IBK01");
        hostCfg.setInClassCd("H44K");
        hostCfg.setSvcCd("44K");

        CbIbk01H44K01Req req = new CbIbk01H44K01Req();

        req.setUserID(sessionManager.getLoginValue("UserID", String.class));
        req.setYI_UPGB("1");

        OltpResponse<CbIbk01H44K01Res> hostResponse = this.hostClient.sendOltp(hostCfg, req,
                CbIbk01H44K01Res.class);

        return hostResponse.getResponse();
    }

    /**
     * 로그인세션에 해당 계좌목록이 존재하는지 여부 판단
     * 
     * @param accountSessionType : 계좌목록 세션키
     * @return
     */
    public boolean isAccountListSession(AccountSessionType type) {
        return sessionManager.getLoginValue(type.getSessionKey(), List.class) != null;
    }

    /**
     * 로그인세션 - 전계좌조회
     * 
     * @return
     */
    public List<AllAccountInfoSession> getAllAcountSessionList() {
        return this.getAccountSessionList(AccountSessionType.ALL, AllAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 신규 계좌조회
     */
    public List<NewAccountInfoSession> getNewAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.NEW, NewAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 대출 계좌조회
     */
    public List<LoanAccountInfoSession> getLoanAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.LOAN, LoanAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 펀드 계좌조회
     */
    public List<FundAccountInfoSession> getFundAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.FUND, FundAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 예적금 계좌조회
     */
    public List<DepositAccountInfoSession> getDepositAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.DEPOSIT, DepositAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 예적금 및 신탁 계좌조회
     */
    public List<DepositTrustAccountInfoSession> getDepositTrustAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.DEPOSIT_TRUST, DepositTrustAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 예적금(10,20,30,80,85,88,90) 계좌조회
     */
    public List<DepositSavingAccountInfoSession> getDepositSavingAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.DEPOSIT_SAVING, DepositSavingAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 출금 계좌조회
     * 
     * @return
     */
    public List<PaymentAccountInfoSession> getPaymentAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.PAYMENT, PaymentAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 출금(10,20,30 계좌만) 계좌조회
     * 
     * @return
     */
    public List<PaymentNormalAccountInfoSession> getPaymentNormalAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.PAYMENT_NORMAL, PaymentNormalAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 예금 계좌조회
     * 
     * @return
     */
    public List<DemandDepositAccountInfoSession> getDemandDepositAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.DEMAND_DEPOSIT, DemandDepositAccountInfoSession.class);
    }

    /**
     * 로그인세션 - FX 계좌조회
     * 
     * @return
     */
    public List<FxAccountInfoSession> getFxAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.FX, FxAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 카드 계좌조회
     * 
     * @return
     */
    public List<CardAccountInfoSession> getCardAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.CARD, CardAccountInfoSession.class);
    }

    /**
     * 로그인세션 - MTS 계좌조회
     * 
     * @return
     */
    public List<MtsAccountInfoSession> getMtsAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.MTS, MtsAccountInfoSession.class);
    }

    /**
     * 로그인세션 - ISA 계좌조회
     * 
     * @return
     */
    public List<IsaAccountInfoSession> getIsaAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.ISA, IsaAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 예적금담보대출 담보계좌조회
     * 
     * @return
     */

    public List<MortgageLoanAccountInfoSession> getMortgageLoanAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.MORTGAGE_LOAN, MortgageLoanAccountInfoSession.class);
    }

    /**
     * 로그인세션 - 대표 계좌조회
     * 
     * @return
     */

    public List<RepresentAccountInfoSession> getRepresentAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.REPRESENT, RepresentAccountInfoSession.class);
    }

    /**
     * 연금신탁 계좌조회
     * 
     * @return
     */

    public List<PensionTrustAccountInfoSession> getPensionTrustAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.PENSION_TRUST, PensionTrustAccountInfoSession.class);
    }

    /**
     * 오픈뱅킹 계좌조회
     * 
     * @return
     */

    public List<OpenbankingAccountInfoSession> getOpenbankingAccountSessionList() {
        return this.getAccountSessionList(AccountSessionType.OBS_ALL, OpenbankingAccountInfoSession.class);
    }

    @SuppressWarnings("unchecked")
    public <S> List<S> getAccountSessionList(AccountSessionType accountSessionType, Class<S> clazz) {

        if (!accountSessionType.getSessionClass().equals(clazz)) {
            throw new IllegalArgumentException("타입 불일치");
        }

        return (List<S>) sessionManager.getLoginValue(accountSessionType.getSessionKey(), List.class);
    }

}
