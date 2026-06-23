package com.scbank.process.api.edmi.dto.mci;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbLmla0103Res", type = Type.RESPONSE, description = "증명서>인터넷증명서조회-금융거래,부채잔액")
public class MciIbLmla0103Res implements IMessageObject {
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

    @MessageField(id = "PAGENO", name = "현재페이지", length = 3)
    private String PAGENO;

    @MessageField(id = "NEXTYN", name = "다음페이지 존재유무", length = 1)
    private String NEXTYN;

    @MessageField(id = "TOTCNT", name = "총 ROW카운트", length = 3)
    private String TOTCNT;

    @MessageField(id = "TOTPAGE", name = "총페이지갯수", length = 3)
    private String TOTPAGE;

    @MessageField(id = "PAGEUNIT", name = "한페이지당 ROW수", length = 3)
    private String PAGEUNIT;

    @MessageField(id = "LISTTOTCNT", name = "조립건수(목록정보)", length = 3)
    private Integer LISTTOTCNT;

    @MessageField(id = "LISTSEL", name = "반복부(목록정보)")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbLmla0103Res/LISTTOTCNT")
    private List<LISTSEL> LISTSEL;

    @Data // 반복부(목록정보)
    public static class LISTSEL implements IMessageObject {

        @MessageField(id = "SRCHGUBUN", name = "조회구분", length = 1)
        private String SRCHGUBUN;

        @MessageField(id = "USERNM", name = "예금주", length = 30, masking = true, maskingType = "03")
        private String USERNM;

        @MessageField(id = "ISSUEDT", name = "발급일", length = 8)
        private String ISSUEDT;

        @MessageField(id = "ISSUEBASEDT", name = "발급기준일", length = 8)
        private String ISSUEBASEDT;

        @MessageField(id = "ISSUENO", name = "발급번호", length = 16)
        private String ISSUENO;

        @MessageField(id = "FEEAMT", name = "수수료", length = 6)
        private String FEEAMT;

        @MessageField(id = "FEEGB", name = "수수료결제구분  , 1:계좌이체 , 2:포인트 , 3: 면제", length = 1)
        private String FEEGB;

        @MessageField(id = "REISSUEYN", name = "재발급가능여부", length = 1)
        private String REISSUEYN;

        @MessageField(id = "LANGCD", name = "발급종류, 1:한국어, 2:영어", length = 1)
        private String LANGCD;
    }

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
