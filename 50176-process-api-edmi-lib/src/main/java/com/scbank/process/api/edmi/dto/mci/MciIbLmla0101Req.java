package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbLmla0101Req", type = Type.REQUEST, description = "증명서>부채잔액신청")
public class MciIbLmla0101Req implements IMessageObject {
    @MessageField(id = "FIDUMMY15", name = "API HEAD IN", length = 15)
    private String FIDUMMY15;

    @MessageField(id = "RESEVE", name = "RESERVE", length = 9)
    private String RESEVE;

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

    @MessageField(id = "PROCGUBUN", name = "처리구분", length = 1)
    private String PROCGUBUN;

    @MessageField(id = "LANGCD", name = "발급종류", length = 1)
    private String LANGCD;

    @MessageField(id = "CORPMKCD", name = "입력구분", length = 1)
    private String CORPMKCD;

    @MessageField(id = "USERNM", name = "이름", length = 30, masking = true, maskingType = "03")
    private String USERNM;

    @MessageField(id = "USERNMENG", name = "영어이름", length = 30)
    private String USERNMENG;

    @MessageField(id = "USERSSN", name = "주민번호/사업자등록번호", length = 13, masking = true, maskingType = "01")
    private String USERSSN;

    @MessageField(id = "ISSUEBASEDT", name = "발급기준일", length = 8)
    private String ISSUEBASEDT;

    @MessageField(id = "FEEACCNO", name = "수수료결제계좌", length = 11, masking = true, maskingType = "02")
    private String FEEACCNO;

    @MessageField(id = "ACCPASSWORD", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
    private String ACCPASSWORD;

    @MessageField(id = "POINTPAYYN", name = "포인트결제여부", length = 1)
    private String POINTPAYYN;

    @MessageField(id = "FEEAMT", name = "발급수수료", length = 6)
    private String FEEAMT;

    @MessageField(id = "USEOBJCTV", name = "이용용도", length = 2)
    private String USEOBJCTV;

    @MessageField(id = "USEPURPOSE", name = "이용목적", length = 50)
    private String USEPURPOSE;

    @MessageField(id = "ADDR2", name = "주소", length = 100, masking = true, maskingType = "05")
    private String ADDR2;

    @MessageField(id = "FLDdelimiter1", name = "1", length = 1, defaultValue = "31")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "2", length = 1, defaultValue = "30")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "3", length = 1, defaultValue = "255")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "4", length = 1, defaultValue = "255")
    private String ENDdelimiter2;
}
