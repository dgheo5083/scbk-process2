package com.scbank.process.api.svc.common.components.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

/**
 * 입출금 내역 푸시 설정 화면 호출 및 설정 계좌 조회 응답
 */
@Data
@IntegrationMessage(id = "SetTransferListPushSettingPageResponse", type = Type.RESPONSE)
public class SetTransferListPushSettingPageResponse implements IMessageObject {

    @MessageField(id = "cnfrmNo", name = "")
    private String cnfrmNo;

    @MessageField(id = "appGb", name = "")
    private String appGb;

    @MessageField(id = "registAcctlist", name = "")
    @RepeatedField
    private List<Acct> registAcctlist;

    @MessageField(id = "nonRegistAcctlist", name = "")
    @RepeatedField
    private List<Acct> nonRegistAcctlist;

    @MessageField(id = "okCount", name = "")
    private int okCount;

    @MessageField(id = "noCount", name = "")
    private int noCount;

    @MessageField(id = "serno", name = "")
    private String serno;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "iotranlistFlag", name = "")
    private String iotranlistFlag;

    @MessageField(id = "notyExrateFlg", name = "")
    private String notyExrateFlg;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

    @Data
    public static class Acct implements IMessageObject {

        @MessageField(id = "drawAcctNum", name = "")
        private String drawAcctNum;

        @MessageField(id = "drawAcctName", name = "")
        private String drawAcctName;

        @MessageField(id = "serno", name = "")
        private String serno;

    }

}
