package com.scbank.process.api.svc.shared.components.account.constant;

import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.CardAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DemandDepositAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositSavingAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.DepositTrustAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.FundAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.FxAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.IsaAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.LoanAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.MortgageLoanAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.MtsAccountInfo;
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountSessionType {
    ALL("ALL_ACCOUNTLIST", AllAccountInfoSession.class, AllAccountInfo.class),
    LOAN("LOAN_ACCOUNTLIST", LoanAccountInfoSession.class, LoanAccountInfo.class),
    FUND("FUND_ACCOUNTLIST", FundAccountInfoSession.class, FundAccountInfo.class),
    DEPOSIT("DEPOSIT_ACCOUNTLIST", DepositAccountInfoSession.class, DepositAccountInfo.class),
    DEPOSIT_TRUST("DEPOSIT_TRUST_ACCOUNTLIST", DepositTrustAccountInfoSession.class, DepositTrustAccountInfo.class),
    DEPOSIT_SAVING("DEPOSIT_SAVING_ACCOUNTLIST", DepositSavingAccountInfoSession.class, DepositSavingAccountInfo.class),
    PAYMENT("PAYMENT_ACCOUNTLIST", PaymentAccountInfoSession.class, PaymentAccountInfo.class),
    PAYMENT_NORMAL("PAYMENT_NORMAL_ACCOUNTLIST", PaymentNormalAccountInfoSession.class, PaymentNormalAccountInfo.class),
    DEMAND_DEPOSIT("DEMAND_DEPOSIT_ACCOUNTLIST", DemandDepositAccountInfoSession.class, DemandDepositAccountInfo.class),
    FX("FX_ACCOUNTLIST", FxAccountInfoSession.class, FxAccountInfo.class),
    CARD("CARD_ACCOUNTLIST", CardAccountInfoSession.class, CardAccountInfo.class),
    MTS("MTS_ACCOUNTLIST", MtsAccountInfoSession.class, MtsAccountInfo.class),
    ISA("ISA_ACCOUNTLIST", IsaAccountInfoSession.class, IsaAccountInfo.class),
    MORTGAGE_LOAN("MORTGAGE_LOAN_ACCOUNTLIST", MortgageLoanAccountInfoSession.class, MortgageLoanAccountInfo.class),
    REPRESENT("REPRESENT_ACCOUNTLIST", RepresentAccountInfoSession.class, RepresentAccountInfo.class),
    NEW("H039_NEW_ACCOUNTLIST", NewAccountInfoSession.class, AllAccountInfo.class),
    PENSION_TRUST("PENSION_TRUST_ACCOUNTLIST", PensionTrustAccountInfoSession.class, PensionTrustAccountInfo.class),
    OBS_ALL("OBS_ALL_ACCOUNTLIST", OpenbankingAccountInfoSession.class, OpenbankingAccountInfo.class);

    private final String sessionKey;
    private final Class<?> sessionClass;
    private final Class<?> outputClass;
}
