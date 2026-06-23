package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H23200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "대출원금/원리금 상환 요청부")
public class CbIbk01H23200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "JsNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JsNum;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "CgAcctPassword", name = "출금계좌번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String CgAcctPassword;

    @MessageField(id = "JgCode", name = "지급구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JgCode;

    @MessageField(id = "GrNum", name = "관리번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GrNum;

    @MessageField(id = "LoanAcctNum", name = "대출계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String LoanAcctNum;

    @MessageField(id = "LoanPart", name = "대출구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LoanPart;

    @MessageField(id = "ShAmt", name = "상환금액(입력)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal ShAmt;

    @MessageField(id = "LoyGB", name = "로열티구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LoyGB;

    @MessageField(id = "LoyPoint", name = "로열티포인트", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LoyPoint;

    @MessageField(id = "YISKIP", name = "잔액체크스킵Y", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISKIP;

    @MessageField(id = "YIWRK2", name = "중복거래방지체크", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIWRK2;

    @MessageField(id = "YICNCCD", name = "저당권말소동의구분(1:말소2:유지)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICNCCD;

    @MessageField(id = "Dummy", name = "dummy", length = 292, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "")
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "dummy1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "dummy2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;
}
