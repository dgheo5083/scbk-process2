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
@IntegrationMessage(id = "CbTbs03D44200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "펀드 환매신청 예비/본거래")
public class CbTbs03D44200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "CloseAcctNum", name = "환매해약계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CloseAcctNum;

    @MessageField(id = "CloseAcctPasswd", name = "환매해약계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String CloseAcctPasswd;

    @MessageField(id = "FundCode", name = "펀드코드", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundCode;

    @MessageField(id = "CloseAmt", name = "환매금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal CloseAmt;

    @MessageField(id = "CloseAcctSu", name = "환매좌수", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal CloseAcctSu;

    @MessageField(id = "TranGubun", name = "거래구분(3일부환매,4해약)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TranGubun;

    @MessageField(id = "InputGubun", name = "입력구분(1금액,2좌수)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String InputGubun;

    @MessageField(id = "CGAcctNum", name = "입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CGAcctNum;

    @MessageField(id = "YICFXGUBN", name = "외화구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICFXGUBN;

    @MessageField(id = "YICFXJWASU", name = "환매좌수(외화펀드)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YICFXJWASU;

    @MessageField(id = "YIZEROHY", name = "Y:0원해약", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIZEROHY;

    @MessageField(id = "Dummy", name = "dummy", length = 291, align = AlignType.LEFT, padding = StringUtils.SPACE)
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

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
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