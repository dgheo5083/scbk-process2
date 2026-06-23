package com.scbank.process.api.svc.shared.components.account.mapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03100Res.DrawAcctNumListRecord;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H03300Res.REC_01;
import com.scbank.process.api.edmi.dto.oltp.CbIbk03H03500Res.CbIbk03H03500ResGrid;
import com.scbank.process.api.fw.core.utils.StringUtils;
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
import com.scbank.process.api.svc.shared.components.account.dto.session.OpenbankingAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.PaymentAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.PaymentNormalAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.PensionTrustAccountInfoSession;
import com.scbank.process.api.svc.shared.components.account.dto.session.RepresentAccountInfoSession;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    public List<AllAccountInfo> toAllAccountListFromSession(List<AllAccountInfoSession> allAccountInfoSessionList);

    @Mapping(target = "drawYn", source = "drawYN") // 구분
    public AllAccountInfoSession toAllAccountInfo(CbIbk03H03500ResGrid h035);

    public LoanAccountInfoSession toLoanAccountInfoSession(REC_01 grid);

    default List<LoanAccountInfoSession> toLoanAccountInfoSessionList(List<REC_01> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toLoanAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<LoanAccountInfo> toLoanAccountListFromSession(List<LoanAccountInfoSession> list);

    public FundAccountInfoSession toFundAccountInfoSession(
            com.scbank.process.api.edmi.dto.oltp.CbIbk01H03200Res.REC_01 grid);

    default List<FundAccountInfoSession> toFundAccountInfoSessionList(
            List<com.scbank.process.api.edmi.dto.oltp.CbIbk01H03200Res.REC_01> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toFundAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<FundAccountInfo> toFundAccountListFromSession(List<FundAccountInfoSession> list);

    public DepositAccountInfoSession toDepositAccountInfoSession(DrawAcctNumListRecord grid);

    default List<DepositAccountInfoSession> toDepositAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("10", "20", "30");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toDepositAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok);
                })
                .toList();
    }

    public List<DepositAccountInfo> toDepositAccountListFromSession(
            List<DepositAccountInfoSession> list);

    public List<OpenbankingAccountInfo> toOpenbankingAccountListFromSession(
            List<OpenbankingAccountInfoSession> list);

    public OpenbankingAccountInfoSession toOpenbankingAccountInfoSession(OpenbankingAccountInfo grid);

    default List<OpenbankingAccountInfoSession> toOpenbankingAccountList(
            List<OpenbankingAccountInfo> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toOpenbankingAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getAcctNo()))
                .toList();
    }

    public DepositTrustAccountInfoSession toDepositTrustAccountInfoSession(DrawAcctNumListRecord grid);

    default List<DepositTrustAccountInfoSession> toDepositTrustAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toDepositTrustAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<DepositTrustAccountInfo> toDepositTrustAccountListFromSession(
            List<DepositTrustAccountInfoSession> list);

    public DepositSavingAccountInfoSession toDepositSavingAccountInfoSession(DrawAcctNumListRecord grid);

    default List<DepositSavingAccountInfoSession> toDepositSavingAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("10", "20", "30", "80", "85", "88", "90");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toDepositSavingAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok);
                })
                .toList();
    }

    public List<DepositSavingAccountInfo> toDepositSavingAccountListFromSession(
            List<DepositSavingAccountInfoSession> list);

    public PaymentAccountInfoSession toPaymentAccountInfoSession(DrawAcctNumListRecord grid);

    default List<PaymentAccountInfoSession> toPaymentAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toPaymentAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<PaymentAccountInfo> toPaymentAccountListFromSession(
            List<PaymentAccountInfoSession> list);

    public PaymentNormalAccountInfoSession toPaymentNormalAccountInfoSession(DrawAcctNumListRecord grid);

    default List<PaymentNormalAccountInfoSession> toPaymentNormalAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("10", "20", "30");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toPaymentNormalAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok);
                })
                .toList();
    }

    public List<PaymentNormalAccountInfo> toPaymentNormalAccountListFromSession(
            List<PaymentNormalAccountInfoSession> list);

    public DemandDepositAccountInfoSession toDemandDepositAccountInfoSession(DrawAcctNumListRecord grid);

    default List<DemandDepositAccountInfoSession> toDemandDepositAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {
        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toDemandDepositAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<DemandDepositAccountInfo> toDemandDepositAccountListFromSession(
            List<DemandDepositAccountInfoSession> list);

    public FxAccountInfoSession toFxAccountInfoSession(DrawAcctNumListRecord grid);

    default List<FxAccountInfoSession> toFxAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("85", "86", "88", "89");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toFxAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok);
                })
                .toList();
    }

    public List<FxAccountInfo> toFxAccountListFromSession(List<FxAccountInfoSession> list);

    public CardAccountInfoSession toCardAccountInfoSession(
            com.scbank.process.api.edmi.dto.oltp.CbIbk03H03400Res.REC_01 grid);

    default List<CardAccountInfoSession> toCardAccountInfoSessionList(
            List<com.scbank.process.api.edmi.dto.oltp.CbIbk03H03400Res.REC_01> list) {

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toCardAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<CardAccountInfo> toCardAccountListFromSession(
            List<CardAccountInfoSession> list);

    public MtsAccountInfoSession toMtsAccountInfoSession(DrawAcctNumListRecord grid);

    default List<MtsAccountInfoSession> toMtsAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("59");
        Set<String> allowAssort = Set.of("31", "35", "41", "45");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toMtsAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String assort = item.getAssort();

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok) && allowAssort.contains(assort);
                })
                .toList();
    }

    public List<MtsAccountInfo> toMtsAccountListFromSession(
            List<MtsAccountInfoSession> list);

    public IsaAccountInfoSession toIsaAccountInfoSession(DrawAcctNumListRecord grid);

    default List<IsaAccountInfoSession> toIsaAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toIsaAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String assort = item.getAssort();

                    String kwamok = drawAcctNum.substring(3, 5);
                    return "59".equals(kwamok) && "22".equals(assort);
                })
                .toList();
    }

    public List<IsaAccountInfo> toIsaAccountListFromSession(
            List<IsaAccountInfoSession> list);

    public MortgageLoanAccountInfoSession toMortgageLoanAccountInfoSession(DrawAcctNumListRecord grid);

    default List<MortgageLoanAccountInfoSession> toMortgageLoanAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("46", "48", "49", "70", "80", "90", "94");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toMortgageLoanAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok);
                })
                .toList();
    }

    public List<MortgageLoanAccountInfo> toMortgageLoanAccountListFromSession(
            List<MortgageLoanAccountInfoSession> list);

    public RepresentAccountInfoSession toRepresentAccountInfoSession(DrawAcctNumListRecord grid);

    default List<RepresentAccountInfoSession> toRepresentAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toRepresentAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .toList();
    }

    public List<RepresentAccountInfo> toRepresentAccountListFromSession(
            List<RepresentAccountInfoSession> list);

    public PaymentAccountInfoSession toPaymentAccountInfo(AllAccountInfoSession allAccountInfoSession);

    public DemandDepositAccountInfoSession toDemandDepositAccountInfo(AllAccountInfoSession allAccountInfoSession);

    public PensionTrustAccountInfoSession toPensionTrustAccountInfoSession(DrawAcctNumListRecord grid);

    default List<PensionTrustAccountInfoSession> toPensionTrustAccountInfoSessionList(
            List<DrawAcctNumListRecord> list) {

        Set<String> allowKwmok = Set.of("54");
        Set<String> allowAssort = Set.of("21", "23", "25", "26", "27", "28");

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toPensionTrustAccountInfoSession).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    if (drawAcctNum.length() < 5) {
                        return false;
                    }

                    String kwamok = drawAcctNum.substring(3, 5);
                    return allowKwmok.contains(kwamok) && allowAssort.contains(item.getAssort());
                })
                .toList();
    }

    public List<PensionTrustAccountInfo> toPensionTrustAccountListFromSession(
            List<PensionTrustAccountInfoSession> list);

}
