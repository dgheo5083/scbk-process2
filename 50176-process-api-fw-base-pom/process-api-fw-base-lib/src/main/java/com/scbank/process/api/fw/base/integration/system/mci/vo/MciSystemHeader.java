package com.scbank.process.api.fw.base.integration.system.mci.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * MCI 시스템헤더 VO
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 22.
 */
@Data
public class MciSystemHeader implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "crypDvCd", length = 1, defaultValue = "0")
    private String crypDvCd;

    @MessageField(id = "tmsgWrtgDt", length = 8)
    private String tmsgWrtgDt;

    @MessageField(id = "tmsgWrtgTm", length = 6)
    private String tmsgWrtgTm;

    @MessageField(id = "trscGrcoCd", length = 2, defaultValue = "01")
    private String trscGrcoCd;

    @MessageField(id = "tmsgCreSysNm", length = 8)
    private String tmsgCreSysNm;

    @MessageField(id = "issSrlNo", length = 3)
    private String issSrlNo;

    @MessageField(id = "ipv6Adr", length = 16)
    private String ipv6Adr;

    @MessageField(id = "inptDlvCd", length = 1, defaultValue = "A")
    private String inptDlvCd;

    @MessageField(id = "envrInfoDvCd", length = 1)
    private String envrInfoDvCd;

    @MessageField(id = "rqstRspsDvCd", length = 1, defaultValue = "Q")
    private String rqstRspsDvCd;

    @MessageField(id = "trscSyncDvCd", length = 1, defaultValue = "S")
    private String trscSyncDvCd;

    @MessageField(id = "tranCd", length = 12)
    private String tranCd;

    @MessageField(id = "tmsgRspsDtm", length = 16)
    private String tmsgRspsDtm;

    @MessageField(id = "procRsltDvcd", length = 1)
    private String procRsltDvcd;

    @MessageField(id = "chnlTypCd", length = 3)
    private String chnlTypCd;

    @MessageField(id = "mciNdNo", length = 2)
    private int mciNdNo;

    @MessageField(id = "mciSessIdNo", length = 8)
    private String mciSessIdNo;
}
