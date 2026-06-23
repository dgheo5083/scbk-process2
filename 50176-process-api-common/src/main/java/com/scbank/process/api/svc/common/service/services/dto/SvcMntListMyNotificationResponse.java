package com.scbank.process.api.svc.common.service.services.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 내소식 알림 정보 조회
 */
@Data
@IntegrationMessage(id = "SvcMntListMyNotificationResponse", type = Type.RESPONSE)
public class SvcMntListMyNotificationResponse implements IMessageObject {

    @MessageField(id = "webUrl", name = "")
    private String webUrl;

    @MessageField(id = "currData", name = "")
    private String currData;

    @MessageField(id = "resultList", name = "")
    @RepeatedField
    private List<PushData> resultList;

    @MessageField(id = "resultSize", name = "")
    private int resultSize;

    @MessageField(id = "bnkingId", name = "")
    private String bnkingId;

    @MessageField(id = "breezePushJoinYN", name = "")
    private String breezePushJoinYN;

    @MessageField(id = "accountList", name = "")
    @RepeatedField
    private List<AccountInfo> accountList;

    @Data
    public static class PushData implements IMessageObject {

        @MessageField(id = "msgSeq", name = "")
        private String msgSeq;

        @MessageField(id = "msgType", name = "")
        private String msgType;

        @MessageField(id = "bnkingId", name = "")
        private String bnkingId;

        @MessageField(id = "acctNo", name = "")
        private String acctNo;

        @MessageField(id = "inoutKind", name = "")
        private String inoutKind;

        @MessageField(id = "bankName", name = "")
        private String bankName;

        @MessageField(id = "lonely", name = "")
        private String lonely;

        @MessageField(id = "amount", name = "")
        private String amount;

        @MessageField(id = "balance", name = "")
        private String balance;

        @MessageField(id = "sendDate", name = "")
        private String sendDate;

        @MessageField(id = "campSeq", name = "")
        private String campSeq;

        @MessageField(id = "title", name = "")
        private String title;

        @MessageField(id = "cnts", name = "")
        private String cnts;

        @MessageField(id = "contentsMsg", name = "")
        private String contentsMsg;

        @MessageField(id = "webUrl", name = "")
        private String webUrl;

        @MessageField(id = "imgUrl", name = "")
        private String imgUrl;

        @MessageField(id = "vectorId", name = "")
        private String vectorId;

    }

    @Data
    public static class AccountInfo implements IMessageObject {

        @MessageField(id = "acctType", name = "")
        private String acctType;

        @MessageField(id = "savingStartDate", name = "")
        private String savingStartDate;

        @MessageField(id = "drawAcctNameAlias", name = "")
        private String drawAcctNameAlias;

        @MessageField(id = "drawYn", name = "")
        private String drawYn;

        @MessageField(id = "drawAcctName", name = "")
        private String drawAcctName;

        @MessageField(id = "drawAcctNum", name = "")
        private String drawAcctNum;

        @MessageField(id = "assort", name = "")
        private String assort;

    }

}
