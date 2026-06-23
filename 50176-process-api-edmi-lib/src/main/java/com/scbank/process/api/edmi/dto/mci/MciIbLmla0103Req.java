package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbLmla0103Req", type = Type.REQUEST, description = "증명서>인터넷증명서조회-금융거래,부채잔액")
public class MciIbLmla0103Req implements IMessageObject {
    @MessageField(id = "FIDUMMY15", name = "API HEAD IN", length = 15)
    private String FIDUMMY15;

    @MessageField(id = "RESEVE", name = "RESERVE", length = 9)
    private String RESEVE;

    @MessageField(id = "PAGENO", name = "PAGENO", length = 3)
    private String PAGENO;

    @MessageField(id = "USERID", name = "USERID", length = 10)
    private String USERID;

    @MessageField(id = "BRANCHCODE", name = "지점코드", length = 3)
    private String BRANCHCODE;

    @MessageField(id = "CHNNLGB", name = "채널구분", length = 2)
    private String CHNNLGB;

    @MessageField(id = "LOCALIP", name = "로컬아이피", length = 15)
    private String LOCALIP;

    @MessageField(id = "TRCODE", name = "트랜잭션코드", length = 13)
    private String TRCODE;

    @MessageField(id = "INFNO", name = "실행메소드", length = 50)
    private String INFNO;

    @MessageField(id = "SRCHGUBUN", name = "조회구분", length = 1)
    private String SRCHGUBUN;

    @MessageField(id = "WORKGUBUN", name = "업무구분", length = 1)
    private String WORKGUBUN;

    @MessageField(id = "ISSUEBASEDT", name = "발급기준일", length = 8)
    private String ISSUEBASEDT;

    @MessageField(id = "SRCHDTTO", name = "발급일 조회시작", length = 8)
    private String SRCHDTTO;

    @MessageField(id = "SRCHDTFROM", name = "발급일 조회종료", length = 8)
    private String SRCHDTFROM;

    @MessageField(id = "ISSUENO", name = "발급번호", length = 16)
    private String ISSUENO;

    @MessageField(id = "USERNM", name = "예금주", length = 30, maskingType = "03")
    private String USERNM;

    @MessageField(id = "USERSSN", name = "주민/사업자번호", length = 13, maskingType = "01")
    private String USERSSN;

    @MessageField(id = "FLDdelimiter1", name = "1", length = 1, defaultValue = "31")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "2", length = 1, defaultValue = "30")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "3", length = 1, defaultValue = "255")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "4", length = 1, defaultValue = "255")
    private String ENDdelimiter2;
}
