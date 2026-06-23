package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbLmla0101Res", type = Type.RESPONSE, description = "증명서>부채잔액신청")
public class MciIbLmla0101Res implements IMessageObject {
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

    @MessageField(id = "FEEEXEMPYN", name = "수수료결제구분  , 1:계좌이체 , 2:포인트 , 3: 면제", length = 1)
    private String FEEEXEMPYN;

    @MessageField(id = "FEEAMT01", name = "결제수수료", length = 6)
    private String FEEAMT01;

    @MessageField(id = "USERNM", name = "이름", length = 30, masking = true, maskingType = "03")
    private String USERNM;

    @MessageField(id = "USERNMENG", name = "영어이름", length = 30, masking = true, maskingType = "03")
    private String USERNMENG;

    @MessageField(id = "ZIPCD", name = "우편번호", length = 6, masking = true, maskingType = "05")
    private String ZIPCD;

    @MessageField(id = "ADDR1", name = "우편주소", length = 80, masking = true, maskingType = "05")
    private String ADDR1;

    @MessageField(id = "ADDR2", name = "상세주소", length = 100, masking = true, maskingType = "05")
    private String ADDR2;

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
