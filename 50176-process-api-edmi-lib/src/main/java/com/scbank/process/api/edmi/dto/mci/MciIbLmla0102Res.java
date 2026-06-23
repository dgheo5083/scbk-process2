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
@IntegrationMessage(id = "MciIbLmla0102Res", type = Type.RESPONSE, description = "증명서>부채잔액상세")
public class MciIbLmla0102Res implements IMessageObject {
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

    @MessageField(id = "ERRNAME", name = "ERRNAME", length = 50)
    private String ERRNAME;

    @MessageField(id = "LANGCD", name = "발급종류", length = 1)
    private String LANGCD;

    @MessageField(id = "ISSUENO", name = "발급번호", length = 16)
    private String ISSUENO;

    @MessageField(id = "ISSUEBASEDT", name = "발급기준일", length = 8)
    private String ISSUEBASEDT;

    @MessageField(id = "ISSUEDT", name = "발급일", length = 8)
    private String ISSUEDT;

    @MessageField(id = "USEOBJCTV", name = "이용용도", length = 2)
    private String USEOBJCTV;

    @MessageField(id = "USEPURPOSE", name = "이용목적", length = 50)
    private String USEPURPOSE;

    @MessageField(id = "TOTLOANAMT", name = "대출잔액", length = 15)
    private String TOTLOANAMT;

    @MessageField(id = "NONSTLMTAMT", name = "미결제타점권", length = 15)
    private String NONSTLMTAMT;

    @MessageField(id = "TOTAPPRVLAMT", name = "한도금액", length = 15)
    private String TOTAPPRVLAMT;

    @MessageField(id = "TOTLOANAMTUSD", name = "대출잔액(USD)", length = 15)
    private String TOTLOANAMTUSD;

    @MessageField(id = "TOTAPPRVLAMTUSD", name = "한도금액(USD)", length = 15)
    private String TOTAPPRVLAMTUSD;

    @MessageField(id = "USDEXCHRATE", name = "외화환율", length = 7)
    private String USDEXCHRATE;

    @MessageField(id = "ZIPCD", name = "우편번호", length = 6, masking = true, maskingType = "05")
    private String ZIPCD;

    @MessageField(id = "ADDR1", name = "우편주소", length = 80, masking = true, maskingType = "05")
    private String ADDR1;

    @MessageField(id = "ADDR2", name = "상세주소", length = 100, masking = true, maskingType = "05")
    private String ADDR2;

    @MessageField(id = "USERSSN", name = "생년월일/사업자번호", length = 13, masking = true, maskingType = "01")
    private String USERSSN;

    @MessageField(id = "USERNM", name = "신청인성명", length = 30, masking = true, maskingType = "03")
    private String USERNM;

    @MessageField(id = "LBLTYTOTCNT", name = "조립건수(부채정보)", length = 3)
    private Integer LBLTYTOTCNT;

    @MessageField(id = "LBLTYSEL", name = "반복부(부채정보)")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbLmla0102Res/LBLTYTOTCNT")
    private List<LBLTYSEL> LBLTYSEL;

    @Data // 반복부(부채정보) - 최근3개월이내 10일 이상 계속된 연체명세
    public static class LBLTYSEL implements IMessageObject {
        @MessageField(id = "SBJCTNM", name = "계정과목명", length = 40)
        private String SBJCTNM;

        @MessageField(id = "LBLTYACCTNO", name = "부채계좌번호", length = 13, masking = true, maskingType = "02")
        private String LBLTYACCTNO;

        @MessageField(id = "LOANREGISTDT", name = "대출일자", length = 8)
        private String LOANREGISTDT;

        @MessageField(id = "LOANDEADLNDT", name = "대출기한", length = 8)
        private String LOANDEADLNDT;

        @MessageField(id = "LOANAMT", name = "대출잔액", length = 20)
        private String LOANAMT;

        @MessageField(id = "NONSTLMTAMT", name = "미결제타점권", length = 15)
        private String NONSTLMTAMT;

        @MessageField(id = "APPRVLAMT", name = "한도금액", length = 15)
        private String APPRVLAMT;

        @MessageField(id = "DLNQCD", name = "연체구분", length = 1)
        private String DLNQCD;

        @MessageField(id = "CLLTRLNM", name = "담보종류", length = 40)
        private String CLLTRLNM;

        @MessageField(id = "LOANAMTUSD", name = "대출잔액(USD)", length = 15)
        private String LOANAMTUSD;

        @MessageField(id = "APPRVLAMTUSD", name = "한도금액(USD)", length = 15)
        private String APPRVLAMTUSD;

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
