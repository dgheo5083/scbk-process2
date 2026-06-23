package com.scbank.process.api.fw.base.integration.system.mci.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * MCI 거래헤더
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 22.
 */
@Data
public class MciTranCommHeader implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "tmsgMsgTypCd", length = 1, defaultValue = "1")
    private String tmsgMsgTypCd;

    @MessageField(id = "blngBrNo", length = 4)
    private String blngBrNo;

    @MessageField(id = "empNo", length = 7)
    private String empNo;

    @MessageField(id = "txnBrNo", length = 4)
    private String txnBrNo;

    @MessageField(id = "serverId", length = 1, defaultValue = "H")
    private String serverId;

    @MessageField(id = "termId", length = 8)
    private String termId;

    @MessageField(id = "cancelKey", length = 1)
    private String cancelKey;

    @MessageField(id = "mciSvrDummy01", length = 34)
    private String mciSvrDummy01;
}
