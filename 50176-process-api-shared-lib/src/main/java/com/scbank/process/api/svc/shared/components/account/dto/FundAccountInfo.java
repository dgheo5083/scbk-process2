package com.scbank.process.api.svc.shared.components.account.dto;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FundAccountInfo", type = Type.RESPONSE, description = "펀드계좌목록조회 응답")
public class FundAccountInfo implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "assort", name = "종별")
    private String assort;

    @MessageField(id = "depKind", name = "계좌종류")
    private String depKind;

    @MessageField(id = "balSign", name = "잔액부호")
    private String balSign;

    @MessageField(id = "balance", name = "잔액")
    private BigDecimal balance;

    @MessageField(id = "curcy", name = "통화")
    private String curcy;

    @MessageField(id = "savingStartDate", name = "예금신규일")
    private String savingStartDate;

    @MessageField(id = "savingEndDate", name = "예금만기일")
    private String savingEndDate;

    @MessageField(id = "gubun", name = "구분")
    private String gubun;

    @MessageField(id = "fundType", name = "펀드종류")
    private String fundType;

    @MessageField(id = "fundCode", name = "펀드코드")
    private String fundCode;

    @MessageField(id = "estimateAmountSign", name = "평가금액부호")
    private String estimateAmountSign;

    @MessageField(id = "estimateAmount", name = "평가금액")
    private String estimateAmount;

    @MessageField(id = "sumReciveRateSign", name = "누적수익율 잔액")
    private String sumReciveRateSign;

    @MessageField(id = "sumReciveRate", name = "누적수익율")
    private Integer sumReciveRate;

    @MessageField(id = "aliveAccountSign", name = "잔존좌수부호")
    private String aliveAccountSign;

    @MessageField(id = "aliveAccount", name = "잔존좌수")
    private String aliveAccount;

    @MessageField(id = "saleStandardPrice", name = "매매기준가격")
    private String saleStandardPrice;

    @MessageField(id = "umbrellaGroup", name = "엄블렐러펀드그룹")
    private String umbrellaGroup;

    @MessageField(id = "umbrellaTransferYn", name = "전환가능BIT")
    private String umbrellaTransferYn;

    @MessageField(id = "umbrellaCancelYn", name = "전환등록취소가능")
    private String umbrellaCancelYn;

    @MessageField(id = "umbrellaAutoTransferYn", name = "자동전환등록취소가능")
    private String umbrellaAutoTransferYn;

    @MessageField(id = "yopanyn", name = "판매사이동가능여부")
    private String YOPANYN;

    @MessageField(id = "yomanyn", name = "만기연장가능여부")
    private String YOMANYN;

    @MessageField(id = "balSign1", name = "지급부호")
    private String balSign1;

    @MessageField(id = "balance1", name = "지급잔액")
    private BigDecimal balance1;

    @MessageField(id = "yosgsayu", name = "신규사유")
    private String YOSGSAYU;

    @MessageField(id = "drawAcctName", name = "계좌명")
    private String drawAcctName;

    @MessageField(id = "drawAcctEngName", name = "계좌영문명")
    private String drawAcctEngName;

}
