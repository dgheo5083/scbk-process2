package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListPushMsgResult {

    private String msgSeq;

    private String msgType;

    private String bnkingId;

    private String acctNo;

    private String inoutKind;

    private String bankName;

    private String lonely;

    private String amount;

    private String balance;

    private String sendDate;

}
