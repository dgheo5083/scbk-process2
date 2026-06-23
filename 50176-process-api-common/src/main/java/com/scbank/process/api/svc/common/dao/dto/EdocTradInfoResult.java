package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class EdocTradInfoResult {

    private String prdctId;

    private String bizType;

    private String kfbAcctNo;

    private String loanReqNo;

    private String loanAccptNo;

    private String prdctCd;

    private String loanDocInvstgtFlg;

}
