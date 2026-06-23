package com.scbank.process.api.svc.common.service.functions.dto.account;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class AccountInfo implements IMessageObject {
    // Ti1Ibk03H035ResGrid START
    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "assort", name = "임시필드이름")
    private String assort;

    @MessageField(id = "drawYN", name = "임시필드이름")
    private String drawYN;

    @MessageField(id = "depKind", name = "임시필드이름")
    private String depKind;

    @MessageField(id = "balSign", name = "임시필드이름")
    private String balSign;

    @MessageField(id = "balance", name = "임시필드이름")
    private BigDecimal balance;

    @MessageField(id = "curcy", name = "임시필드이름")
    private String curcy;

    @MessageField(id = "drawAcctNameAlias", name = "임시필드이름")
    private String drawAcctNameAlias;

    @MessageField(id = "acctDisplay", name = "임시필드이름")
    private String acctDisplay;

    @MessageField(id = "savingStartDate", name = "임시필드이름")
    private String savingStartDate;

    @MessageField(id = "savingEndDate", name = "임시필드이름")
    private String savingEndDate;

    @MessageField(id = "fundGubun", name = "임시필드이름")
    private String fundGubun;

    @MessageField(id = "fundType", name = "임시필드이름")
    private String fundType;

    @MessageField(id = "fundCode", name = "임시필드이름")
    private String fundCode;

    @MessageField(id = "custodianAmountSign", name = "임시필드이름")
    private String custodianAmountSign;

    @MessageField(id = "custodianAmount", name = "임시필드이름")
    private BigDecimal custodianAmount;

    @MessageField(id = "estimateAmountSign", name = "임시필드이름")
    private String estimateAmountSign;

    @MessageField(id = "estimateAmount", name = "임시필드이름")
    private BigDecimal estimateAmount;

    @MessageField(id = "sumReciveRateSign", name = "임시필드이름")
    private String sumReciveRateSign;

    @MessageField(id = "sumReciveRate", name = "임시필드이름")
    private BigDecimal sumReciveRate;

    @MessageField(id = "aliveAccountSign", name = "임시필드이름")
    private String aliveAccountSign;

    @MessageField(id = "aliveAccount", name = "임시필드이름")
    private BigDecimal aliveAccount;

    @MessageField(id = "saleStandardPriceSign", name = "임시필드이름")
    private String saleStandardPriceSign;

    @MessageField(id = "saleStandardPrice", name = "임시필드이름")
    private BigDecimal saleStandardPrice;

    @MessageField(id = "umbrellaGroup", name = "임시필드이름")
    private String umbrellaGroup;

    @MessageField(id = "umbrellaTransferYN", name = "임시필드이름")
    private String umbrellaTransferYN;

    @MessageField(id = "umbrellaCancelYN", name = "임시필드이름")
    private String umbrellaCancelYN;

    @MessageField(id = "umbrellaAutoTransferYN", name = "임시필드이름")
    private String umbrellaAutoTransferYN;

    @MessageField(id = "yopanyn", name = "임시필드이름")
    private String yopanyn;

    @MessageField(id = "yomanyn", name = "임시필드이름")
    private String yomanyn;

    @MessageField(id = "paymentEstimateAmountSign", name = "임시필드이름")
    private String paymentEstimateAmountSign;

    @MessageField(id = "paymentEstimateAmount", name = "임시필드이름")
    private BigDecimal paymentEstimateAmount;

    @MessageField(id = "loanBalanceSign", name = "임시필드이름")
    private String loanBalanceSign;

    @MessageField(id = "loanBalance", name = "임시필드이름")
    private BigDecimal loanBalance;

    @MessageField(id = "loanRepayPrinAmt", name = "임시필드이름")
    private BigDecimal loanRepayPrinAmt;

    @MessageField(id = "expectedDate", name = "임시필드이름")
    private String expectedDate;

    @MessageField(id = "loanRate", name = "임시필드이름")
    private BigDecimal loanRate;

    @MessageField(id = "cardType", name = "임시필드이름")
    private String cardType;

    @MessageField(id = "familyType", name = "임시필드이름")
    private String familyType;

    @MessageField(id = "paymentDate", name = "임시필드이름")
    private String paymentDate;

    @MessageField(id = "openDate", name = "임시필드이름")
    private String openDate;

    @MessageField(id = "period", name = "임시필드이름")
    private String period;

    @MessageField(id = "checkCardType", name = "임시필드이름")
    private String checkCardType;

    @MessageField(id = "coOperCardCode", name = "임시필드이름")
    private String coOperCardCode;

    @MessageField(id = "acctType", name = "임시필드이름")
    private String acctType;

    @MessageField(id = "loanKind", name = "임시필드이름")
    private String loanKind;

    @MessageField(id = "loanAcctKmCD", name = "임시필드이름")
    private String loanAcctKmCD;
    // Ti1Ibk03H035ResGrid END

    // original AccountInfo Data
    @MessageField(id = "drawAcctName", name = "계좌명")
    private String drawAcctName;

    @MessageField(id = "drawAcctEngName", name = "계좌영문명")
    private String drawAcctEngName;

    @MessageField(id = "drawAcctTrustName", name = "신탁계좌명")
    private String drawAcctTrustName;

    @MessageField(id = "investAssetNo", name = "신탁운용자산번호")
    private String investAssetNo;
}
