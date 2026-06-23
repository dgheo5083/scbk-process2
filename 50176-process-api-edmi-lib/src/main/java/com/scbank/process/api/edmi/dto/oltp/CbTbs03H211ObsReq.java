package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H211ObsReq", type = Type.REQUEST, description = "오픈뱅킹 이체 FDS로그용")
public class CbTbs03H211ObsReq implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPassword;

    @MessageField(id = "JsNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JsNum;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgAcctNum;

    @MessageField(id = "CgAcctPassword", name = "출금계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private String CgAcctPassword;

    @MessageField(id = "JgCode", name = "지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JgCode;

    @MessageField(id = "HgSNameSO1", name = "비고란(송금인명)입금통장표시내용 한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSNameSO1;

    @MessageField(id = "HgSName", name = "비고란(송금인명)입금통장표시내용", length = 24, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSName;

    @MessageField(id = "HgSNameSI1", name = "비고란(송금인명)입금통장표시내용 한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgSNameSI1;

    @MessageField(id = "GrNum", name = "관리번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GrNum;

    @MessageField(id = "JYCode", name = "즉시예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JYCode;

    @MessageField(id = "IgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgAcctNum", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgAcctNum;

    @MessageField(id = "TotSgAmt", name = "총송금금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotSgAmt;

    @MessageField(id = "TotFeeAmt", name = "총수수료금액", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TotFeeAmt;

    @MessageField(id = "SgAmt", name = "송금금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SgAmt;

    @MessageField(id = "FeeAmt", name = "수수료금액", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FeeAmt;

    @MessageField(id = "FeeCode", name = "수수료구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FeeCode;

    @MessageField(id = "TransCode", name = "이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransCode;

    @MessageField(id = "JbCode", name = "증권/보험사코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JbCode;

    @MessageField(id = "CMSCode", name = "의뢰인코드", length = 24, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CMSCode;

    @MessageField(id = "HgRNameSO2", name = "출금통장표시내용 한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRNameSO2;

    @MessageField(id = "HgRName", name = "출금통장표시내용", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRName;

    @MessageField(id = "HgRNameSI2", name = "출금통장표시내용 한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String HgRNameSI2;

    @MessageField(id = "YyDate", name = "예약이체일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YyDate;

    @MessageField(id = "YyTime", name = "예약이체시간", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YyTime;

    @MessageField(id = "YINEWIPGJ", name = "신입금계좌여부", length = 1, align = AlignType.LEFT)
    private String YINEWIPGJ;

    @MessageField(id = "YISIMPLE", name = "간편이체여부", length = 1, align = AlignType.LEFT)
    private String YISIMPLE;

    @MessageField(id = "YIMOBILE3", name = "모바일3.0여부", length = 1, align = AlignType.LEFT)
    private String YIMOBILE3;

    @MessageField(id = "YIPRESENT", name = "선물하기여부 Y", length = 1, align = AlignType.LEFT)
    private String YIPRESENT;

    @MessageField(id = "Dummy", name = "더미", length = 198, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "전화국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "U정보화마을주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "U정보화마을주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "더미1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "더미2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;

    @MessageField(id = "CgBankCode", name = "출금은행코드(오픈뱅킹)", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CgBankCode;
}
