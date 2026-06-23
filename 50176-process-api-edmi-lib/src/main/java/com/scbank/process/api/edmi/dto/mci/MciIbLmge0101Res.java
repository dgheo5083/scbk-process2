package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbLmge0101Res", type = Type.RESPONSE, description = "증명서>금융거래신청")
public class MciIbLmge0101Res implements IMessageObject {
    @MessageField(id = "WSF", name = "WSF", length = 1)
    private String WSF;

    @MessageField(id = "LL", name = "LL", length = 2)
    private String LL;

    @MessageField(id = "SFID", name = "SFID", length = 1)
    private String SFID;

    @MessageField(id = "SFTP", name = "SFTP", length = 1)
    private String SFTP;

    @MessageField(id = "ERRCOD", name = "ERRCOD", length = 4)
    private String ERRCOD;

    @MessageField(id = "ERRNAME", name = "ERRNAME", length = 30)
    private String ERRNAME;

    @MessageField(id = "ISSUESEQNO", name = "발급일련번호", length = 15)
    private String ISSUESEQNO;

    @MessageField(id = "ISSUENO", name = "발급번호", length = 16)
    private String ISSUENO;

    @MessageField(id = "FEEEXEMPYN", name = "수수료면제대상여부", length = 1)
    private String FEEEXEMPYN;

    @MessageField(id = "FEEAMT01", name = "결제수수료", length = 6)
    private String FEEAMT01;

    @MessageField(id = "MSGCODE", name = "메시지코드", length = 30)
    private String MSGCODE;

    @MessageField(id = "MSGCONT", name = "메시지내용", length = 300)
    private String MSGCONT;

    @MessageField(id = "FLDdelimiter1", name = "1", length = 1, defaultValue = "31")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "2", length = 1, defaultValue = "30")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "3", length = 1, defaultValue = "255")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "4", length = 1, defaultValue = "255")
    private String ENDdelimiter2;
}
